const express = require("express");
const cors = require("cors");
const { Pool } = require("pg");
const axios = require("axios");

const app = express();
const port = 3000;
const { DateTime } = require("luxon");

// Middleware
app.use(cors());
app.use(express.json());

//Key de la api de vuelos

const API_KEY = "";
// Configuración de PostgreSQL
const pool = new Pool({
  user: "",
  host: "",
  database: "",
  password: "",
  port: 5433,
});

//RUTAS DE USUARIO

// Ruta POST para registrar usuario
app.post("/registerUser", async (req, res) => {
  const { email, uuid, username } = req.body;

  try {
    await pool.query(
      "INSERT INTO users (email, uuid, username) VALUES ($1, $2, $3)",
      [email, uuid, username]
    );
    res.status(201).json({ message: "Usuario registrado" });
  } catch (err) {
    console.error("Error al insertar:", err);
    res.status(500).json({ error: "Error interno del servidor" });
  }
});

// Ruta para obtener toda la información del usuario
app.get("/getUser/:uuid", async (req, res) => {
  const { uuid } = req.params;
  console.log("UUID recibido:", uuid);

  try {
    const result = await pool.query("SELECT * FROM users WHERE uuid = $1", [
      uuid,
    ]);
    if (result.rows.length > 0) {
      res.json(result.rows[0]);
    } else {
      res.status(404).json({ message: "User not found" });
    }
  } catch (error) {
    console.error("Error en getUser:", error);
    res.status(500).json({ message: "Internal server error" });
  }
});

//Hacer uan reserva
app.post("/reservation", async (req, res) => {
  console.log("BODY RECIBIDO:", JSON.stringify(req.body, null, 2));

  const { uuid, reservation_name, flights } = req.body;
  const client = await pool.connect();

  try {
    await client.query("BEGIN");

    const userResult = await client.query(
      "SELECT id_user FROM users WHERE uuid = $1",
      [uuid]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({ error: "Usuario no encontrado" });
    }

    const userId = userResult.rows[0].id_user;
   

    const reservationResult = await client.query(
      `INSERT INTO reservation (id_user, reservation_name)
       VALUES ($1, $2)
       RETURNING id_reservation`,
      [userId, reservation_name]
    );

    const reservationId = reservationResult.rows[0].id_reservation;

    for (const flight of flights) {
      const { codeFlight, persons, cost, baggage = {} } = flight;

      let dbFlight = await client.query(
        "SELECT * FROM flights WHERE code_flight = $1 LIMIT 1",
        [codeFlight]
      );

      let departureDate, arrivalDate;

      if (dbFlight.rows.length === 0) {
        const apiResponse = await axios.get(
          "http://api.aviationstack.com/v1/flights",
          {
            params: {
              access_key: API_KEY,
              flight_iata: codeFlight,
            },
          }
        );

        const flightData = apiResponse.data.data[0];
        if (!flightData) {
          throw new Error(`The flight with code  '${codeFlight}' does not exist or cannot be found.`);
        }
        

        departureDate = flightData.departure.scheduled;
        arrivalDate = flightData.arrival.scheduled;

        const airlineResult = await client.query(
          `INSERT INTO airlines (name, code_iata, code_icao)
           VALUES ($1, $2, $3)
           ON CONFLICT (code_iata) DO UPDATE SET name = EXCLUDED.name
           RETURNING id_airline`,
          [
            flightData.airline.name,
            flightData.airline.iata,
            flightData.airline.icao,
          ]
        );

        const insertAirport = async (airport) => {
          const result = await client.query(
            `INSERT INTO airport (name, code_iata, code_icao)
             VALUES ($1, $2, $3)
             ON CONFLICT (code_iata) DO UPDATE SET name = EXCLUDED.name
             RETURNING id_airport`,
            [airport.airport, airport.iata, airport.icao]
          );
          return result.rows[0].id_airport;
        };

        const departureId = await insertAirport(flightData.departure);
        const arrivalId = await insertAirport(flightData.arrival);

        await client.query(
          `INSERT INTO flights (code_flight, departure_date, arrival_date, id_airline, departure_airport, arrival_airport)
           VALUES ($1, $2, $3, $4, $5, $6)
           ON CONFLICT DO NOTHING`,
          [
            codeFlight,
            departureDate,
            arrivalDate,
            airlineResult.rows[0].id_airline,
            departureId,
            arrivalId,
          ]
        );
      } else {
        departureDate = dbFlight.rows[0].departure_date;
        arrivalDate = dbFlight.rows[0].arrival_date;
      }

      const flightDetails = await client.query(
        `SELECT * FROM flights
         WHERE code_flight = $1 AND departure_date = $2 AND arrival_date = $3`,
        [codeFlight, departureDate, arrivalDate]
      );

      const flightRow = flightDetails.rows[0];

      console.log("Insertando en flight_reservation:", {
        codeFlight: flightRow.code_flight,
        departureDate: flightRow.departure_date,
        arrivalDate: flightRow.arrival_date,
      });

      await client.query(
        `INSERT INTO flight_reservation (id_reservation, code_flight, departure_date, arrival_date, cost, persons)
         VALUES ($1, $2, $3, $4, $5, $6)`,
        [
          reservationId,
          flightRow.code_flight,
          flightRow.departure_date,
          flightRow.arrival_date,
          cost,
          persons,
        ]
      );

      for (const [type, amount] of Object.entries(baggage)) {
        if (amount > 0) {
          await client.query(
            `INSERT INTO luggage (id_luggage, id_reservation, code_flight, departure_date, arrival_date, type, amount)
             VALUES (DEFAULT, $1, $2, $3, $4, $5, $6)`,
            [
              reservationId,
              flightRow.code_flight,
              flightRow.departure_date,
              flightRow.arrival_date,
              type,
              amount,
            ]
          );
        }
      }
    }

    await client.query("COMMIT");
    res.json({ message: "Reserva con varios vuelos creada", reservationId });
  } catch (err) {
    await client.query("ROLLBACK");
    console.error("Error:", err.message);
    res.status(500).json({ error: err.message });
  } finally {
    client.release();
  }
});

app.post("/reservation/:id/add-flight", async (req, res) => {
  const reservationId = req.params.id;
  const { flights } = req.body;
  const client = await pool.connect();

  try {
    await client.query("BEGIN");

    for (const flight of flights) {
      const { codeFlight, persons, cost, baggage = {} } = flight;

      let dbFlight = await client.query(
        "SELECT * FROM flights WHERE code_flight = $1 LIMIT 1",
        [codeFlight]
      );

      let departureDate, arrivalDate;

      if (dbFlight.rows.length === 0) {
        const apiResponse = await axios.get(
          "http://api.aviationstack.com/v1/flights",
          {
            params: {
              access_key: API_KEY,
              flight_iata: codeFlight,
            },
          }
        );

        const flightData = apiResponse.data.data[0];
        if (!flightData) {
          throw new Error(`The flight with code  '${codeFlight}' does not exist or cannot be found.`);
        }

        

        departureDate = flightData.departure.scheduled;
        arrivalDate = flightData.arrival.scheduled;

        const airlineResult = await client.query(
          `INSERT INTO airlines (name, code_iata, code_icao)
           VALUES ($1, $2, $3)
           ON CONFLICT (code_iata) DO UPDATE SET name = EXCLUDED.name
           RETURNING id_airline`,
          [flightData.airline.name, flightData.airline.iata, flightData.airline.icao]
        );

        const insertAirport = async (airport) => {
          const result = await client.query(
            `INSERT INTO airport (name, code_iata, code_icao)
             VALUES ($1, $2, $3)
             ON CONFLICT (code_iata) DO UPDATE SET name = EXCLUDED.name
             RETURNING id_airport`,
            [airport.airport, airport.iata, airport.icao]
          );
          return result.rows[0].id_airport;
        };

        const departureId = await insertAirport(flightData.departure);
        const arrivalId = await insertAirport(flightData.arrival);

        await client.query(
          `INSERT INTO flights (code_flight, departure_date, arrival_date, id_airline, departure_airport, arrival_airport)
           VALUES ($1, $2, $3, $4, $5, $6)
           ON CONFLICT DO NOTHING`,
          [codeFlight, departureDate, arrivalDate, airlineResult.rows[0].id_airline, departureId, arrivalId]
        );
      } else {
        departureDate = dbFlight.rows[0].departure_date;
        arrivalDate = dbFlight.rows[0].arrival_date;
      }

      const flightRow = (
        await client.query(
          `SELECT * FROM flights WHERE code_flight = $1 AND departure_date = $2 AND arrival_date = $3`,
          [codeFlight, departureDate, arrivalDate]
        )
      ).rows[0];

      await client.query(
        `INSERT INTO flight_reservation (id_reservation, code_flight, departure_date, arrival_date, cost, persons)
         VALUES ($1, $2, $3, $4, $5, $6)`,
        [reservationId, flightRow.code_flight, flightRow.departure_date, flightRow.arrival_date, cost, persons]
      );

      for (const [type, amount] of Object.entries(baggage)) {
        if (amount > 0) {
          await client.query(
            `INSERT INTO luggage (id_luggage, id_reservation, code_flight, departure_date, arrival_date, type, amount)
             VALUES (DEFAULT, $1, $2, $3, $4, $5, $6)`,
            [reservationId, flightRow.code_flight, flightRow.departure_date, flightRow.arrival_date, type, amount]
          );
        }
      }
    }

    await client.query("COMMIT");
    res.json({ message: "Vuelos añadidos a la reserva" });
  } catch (err) {
    await client.query("ROLLBACK");
    console.error("Error:", err.message);
    res.status(500).json({ error: err.message });
  } finally {
    client.release();
  }
});


//Endpoint para obtener la informacion de los vuelos del usuario
app.get("/user/:uuid/flights", async (req, res) => {
  const { uuid } = req.params;

  try {
    // Obtener ID del usuario
    const userResult = await pool.query(
      "SELECT id_user FROM users WHERE uuid = $1",
      [uuid]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({ error: "Usuario no encontrado" });
    }

    const userId = userResult.rows[0].id_user;

    // Obtener reservas y vuelos
    const flightsResult = await pool.query(
      `
      SELECT 
        f.code_flight, 
        f.departure_date, 
        f.arrival_date, 
        a.code_iata AS airline_code_iata,
        a.name AS airline_name, 
        dap.code_iata AS departure_airport,
        dap.name AS departure_airport_name, 
        aap.code_iata AS arrival_airport,
        aap.name AS arrival_airport_name,
        fr.cost, 
        fr.persons,
        r.id_reservation
      FROM reservation r
      JOIN flight_reservation fr ON r.id_reservation = fr.id_reservation
      JOIN flights f ON fr.code_flight = f.code_flight 
                     AND fr.departure_date = f.departure_date 
                     AND fr.arrival_date = f.arrival_date
      JOIN airlines a ON f.id_airline = a.id_airline
      JOIN airport dap ON f.departure_airport = dap.id_airport
      JOIN airport aap ON f.arrival_airport = aap.id_airport
      WHERE r.id_user = $1
      ORDER BY f.departure_date DESC
      `,
      [userId]
    );

    const flights = flightsResult.rows;

    // Obtener maletas para cada reserva+vuelo
    const fullResults = await Promise.all(
      flights.map(async (flight) => {
        const luggageResult = await pool.query(
          `
          SELECT type, amount
          FROM luggage
          WHERE id_reservation = $1
            AND code_flight = $2
          `,
          [flight.id_reservation, flight.code_flight]
        );
    
        const departureDate = DateTime.fromJSDate(flight.departure_date, { zone: 'utc' })
          .setZone('Europe/Madrid')
          .toISO({ suppressMilliseconds: true });
    
        const arrivalDate = DateTime.fromJSDate(flight.arrival_date, { zone: 'utc' })
          .setZone('Europe/Madrid')
          .toISO({ suppressMilliseconds: true });
    
        return {
          ...flight,
          departure_date: departureDate,
          arrival_date: arrivalDate,
          luggage: luggageResult.rows
        };
      })
    );
    

    res.json(fullResults);
  } catch (err) {
    console.error("Error al obtener vuelos del usuario:", err.message);
    res.status(500).json({ error: "Error interno del servidor" });
  }
});

//Eliminar vuelo 
app.delete('/reservation/:id_reservation/flight/:code_flight', async (req, res) => {
  const { id_reservation, code_flight } = req.params;

  // 1. Comprobar cuántos vuelos tiene esa reserva
  const result = await pool.query(
    `SELECT COUNT(*) FROM flight_reservation WHERE id_reservation = $1`,
    [id_reservation]
  );

  const flightCount = parseInt(result.rows[0].count);

  if (flightCount <= 1) {
    return res.status(400).json({
      error: "You cannot remove the only flight from a booking. Delete the entire booking.",
    });
  }

  // 2. Eliminar el vuelo
  await pool.query(
    `DELETE FROM flight_reservation WHERE id_reservation = $1 AND code_flight = $2`,
    [id_reservation, code_flight]
  );

  return res.status(200).json({ message: "Flight successfully removed" });
});


//Eliminar reserva 
app.delete('/reservation/:id_reservation', async (req, res) => {
  const { id_reservation} = req.params;
  // 1. Eliminar la reserva
  await pool.query(
    `DELETE FROM public.reservation WHERE id_reservation= $1;`,
    [id_reservation]
  );

  return res.status(200).json({ message: "Reserve successfully deleted" });
});




app.get("/flights/:idReservation/:code/:departure/:arrival", async (req, res) => {
  const { idReservation, code, departure, arrival } = req.params;

  try {
    const flightsResult = await pool.query(
      `
      SELECT 
        r.id_reservation,
        f.code_flight, 
        f.departure_date, 
        f.arrival_date, 
        a.code_iata AS airline_code_iata,
        a.name AS airline_name, 
        dap.code_iata AS departure_airport,
        dap.name AS departure_airport_name, 
        aap.code_iata AS arrival_airport,
        aap.name AS arrival_airport_name,
        fr.cost, 
        fr.persons
      FROM reservation r
      JOIN flight_reservation fr ON r.id_reservation = fr.id_reservation
      JOIN flights f ON fr.code_flight = f.code_flight 
                     AND fr.departure_date = f.departure_date 
                     AND fr.arrival_date = f.arrival_date
      JOIN airlines a ON f.id_airline = a.id_airline
      JOIN airport dap ON f.departure_airport = dap.id_airport
      JOIN airport aap ON f.arrival_airport = aap.id_airport
      WHERE r.id_reservation = $1
        AND f.code_flight = $2
        AND f.departure_date = $3
        AND f.arrival_date = $4
      `,
      [idReservation, code, departure, arrival]
    );

    if (flightsResult.rows.length === 0) {
      return res.status(404).json({ error: "Vuelo no encontrado" });
    }

    const flight = flightsResult.rows[0];

    // Obtener maletas
    const luggageResult = await pool.query(
      `
      SELECT type, amount
      FROM luggage
      WHERE id_reservation = $1
        AND code_flight = $2
      `,
      [idReservation, code]
    );

    const departureDate = DateTime.fromJSDate(flight.departure_date, { zone: 'utc' })
      .setZone('Europe/Madrid')
      .toISO({ suppressMilliseconds: true });

    const arrivalDate = DateTime.fromJSDate(flight.arrival_date, { zone: 'utc' })
      .setZone('Europe/Madrid')
      .toISO({ suppressMilliseconds: true });

    const result = {
      ...flight,
      departure_date: departureDate,
      arrival_date: arrivalDate,
      luggage: luggageResult.rows
    };

    res.json(result);
  } catch (err) {
    console.error("Error al obtener vuelo:", err.message);
    res.status(500).json({ error: "Error interno del servidor" });
  }
});

// Obtener resumen de una reserva por su ID
app.get("/user/flight/:id_reservation", async (req, res) => {
  const { id_reservation } = req.params;

  try {
    const reservationResult = await pool.query(
      `
      SELECT 
        r.id_reservation,
        r.reservation_name,
        r.total_cost,
        COUNT(fr.code_flight) AS flight_count
      FROM reservation r
      LEFT JOIN flight_reservation fr ON r.id_reservation = fr.id_reservation
      WHERE r.id_reservation = $1
      GROUP BY r.id_reservation, r.reservation_name, r.total_cost
      `,
      [id_reservation]
    );

    if (reservationResult.rows.length === 0) {
      return res.status(404).json({ error: "Reserva no encontrada" });
    }

    const r = reservationResult.rows[0];

    const reservationCard = {
      id_reservation: r.id_reservation,
      reservation_name: r.reservation_name,
      total_cost: parseFloat(r.total_cost),
      flight_count: parseInt(r.flight_count),
    };

    res.json(reservationCard);
  } catch (err) {
    console.error("Error al obtener reserva por ID:", err.message);
    res.status(500).json({ error: "Error interno del servidor" });
  }
});


//Obtener los datos basicos del usuario para la homescreen
app.get("/user/:uuid/stats", async (req, res) => {
  const { uuid } = req.params;

  try {
    // Buscar id del usuario
    const userResult = await pool.query(
      "SELECT id_user FROM users WHERE uuid = $1",
      [uuid]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({ error: "Usuario no encontrado" });
    }

    const userId = userResult.rows[0].id_user;

    const statsResult = await pool.query(
      `
      WITH user_flights AS (
        SELECT f.id_airline, f.arrival_airport
        FROM reservation r
        JOIN flight_reservation fr ON r.id_reservation = fr.id_reservation
        JOIN flights f ON fr.code_flight = f.code_flight
                       AND fr.departure_date = f.departure_date
                       AND fr.arrival_date = f.arrival_date
        WHERE r.id_user = $1
      ),
      stats AS (
      SELECT 
          COUNT(*) AS total_flights
      FROM reservation r
      JOIN flight_reservation fr ON r.id_reservation = fr.id_reservation
      WHERE r.id_user = 1

      ),
      total_spent AS (
        SELECT COALESCE(SUM(total_cost), 0) AS total_spent
        FROM reservation
        WHERE id_user = $1
      ),
      most_used_airline_data AS (
        SELECT id_airline, COUNT(*) AS airline_count
        FROM user_flights
        GROUP BY id_airline
        ORDER BY airline_count DESC
        LIMIT 1
      ),
      most_visited_destination_data AS (
        SELECT arrival_airport, COUNT(*) AS destination_count
        FROM user_flights
        GROUP BY arrival_airport
        ORDER BY destination_count DESC
        LIMIT 1
      )
      SELECT 
        stats.total_flights,
        total_spent.total_spent,

        a.name AS most_used_airline,
        mua.airline_count AS airline_flight_count,

        ap.name AS most_visited_destination,
        mvd.destination_count AS destination_flight_count

      FROM stats, total_spent
      LEFT JOIN most_used_airline_data mua ON TRUE
      LEFT JOIN airlines a ON mua.id_airline = a.id_airline
      LEFT JOIN most_visited_destination_data mvd ON TRUE
      LEFT JOIN airport ap ON mvd.arrival_airport = ap.id_airport
    `,
      [userId]
    );

    res.json(statsResult.rows[0]);
  } catch (error) {
    console.error("Error al obtener estadísticas del usuario:", error.message);
    res.status(500).json({ error: "Error interno del servidor" });
  }
});

//Obtener las Reservas del Usuario
app.get("/user/:uuid/reservations-summary", async (req, res) => {
  const { uuid } = req.params;

  try {
    // 1. Obtener el ID del usuario
    const userResult = await pool.query(
      "SELECT id_user FROM users WHERE uuid = $1",
      [uuid]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({ error: "Usuario no encontrado" });
    }

    const userId = userResult.rows[0].id_user;

    // 2. Obtener las reservas del usuario con el número de vuelos por reserva
    const reservationsResult = await pool.query(
      `
      SELECT 
        r.id_reservation,
        r.reservation_name,
        r.total_cost,
        COUNT(fr.code_flight) AS flight_count
      FROM reservation r
      LEFT JOIN flight_reservation fr ON r.id_reservation = fr.id_reservation
      WHERE r.id_user = $1
      GROUP BY r.id_reservation, r.reservation_name
      ORDER BY r.id_reservation DESC
      `,
      [userId]
    );

    // 3. Formatear y devolver el resultado
    const reservations = reservationsResult.rows.map((r) => ({
      id_reservation: r.id_reservation,
      reservation_name: r.reservation_name,
      flight_count: parseInt(r.flight_count),
      total_cost: r.total_cost,
    }));

    res.json(reservations);
  } catch (err) {
    console.error("Error al obtener resumen de reservas:", err.message);
    res.status(500).json({ error: "Error interno del servidor" });
  }
});

//Obtiene la informacion de la ultima reserva 
app.get("/user/:uuid/last-reservation-summary", async (req, res) => {
  const { uuid } = req.params;

  try {
    // 1. Obtener el ID del usuario
    const userResult = await pool.query(
      "SELECT id_user FROM users WHERE uuid = $1",
      [uuid]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({ error: "Usuario no encontrado" });
    }

    const userId = userResult.rows[0].id_user;

    // 2. Obtener solo la reserva más reciente (mayor id_reservation)
    const reservationResult = await pool.query(
      `
      SELECT 
        r.id_reservation,
        r.reservation_name,
        r.total_cost,
        COUNT(fr.code_flight) AS flight_count
      FROM reservation r
      LEFT JOIN flight_reservation fr ON r.id_reservation = fr.id_reservation
      WHERE r.id_user = $1
      GROUP BY r.id_reservation, r.reservation_name
      ORDER BY r.id_reservation DESC
      LIMIT 1
      `,
      [userId]
    );

    if (reservationResult.rows.length === 0) {
      return res.status(404).json({ error: "No se encontraron reservas para el usuario" });
    }

    const r = reservationResult.rows[0];

    // 3. Devolver solo la última reserva
    const reservation = {
      id_reservation: r.id_reservation,
      reservation_name: r.reservation_name,
      flight_count: parseInt(r.flight_count),
      total_cost: r.total_cost,
    };

    res.json(reservation);
  } catch (err) {
    console.error("Error al obtener la última reserva:", err.message);
    res.status(500).json({ error: "Error interno del servidor" });
  }
});


//Obtener la reserva por su id

app.get("/reservation/:id_reservation", async (req, res) => {
  const { id_reservation } = req.params;

  try {
    // 1. Obtener info básica de la reserva
    const reservationResult = await pool.query(
      `
      SELECT reservation_name, total_cost
      FROM reservation
      WHERE id_reservation = $1
    `,
      [id_reservation]
    );

    if (reservationResult.rows.length === 0) {
      return res.status(404).json({ error: "Reserva no encontrada" });
    }

    const { reservation_name, total_cost } = reservationResult.rows[0];

    // 2. Obtener vuelos asociados a la reserva
    const flightsResult = await pool.query(
      `
      SELECT 
      fr.id_reservation,
      f.code_flight,
      f.departure_date,
      f.arrival_date,
      a.code_iata AS airline_code_iata,
      a.name AS airline_name,
      dap.code_iata AS departure_airport, 
      dap.name AS departure_airport_name,
      aap.code_iata AS arrival_airport,   
      aap.name AS arrival_airport_name,
      fr.cost,
      fr.persons
      FROM flight_reservation fr
      JOIN flights f ON fr.code_flight = f.code_flight AND fr.departure_date = f.departure_date AND fr.arrival_date = f.arrival_date
      JOIN airlines a ON f.id_airline = a.id_airline
      JOIN airport dap ON f.departure_airport = dap.id_airport
      JOIN airport aap ON f.arrival_airport = aap.id_airport
      WHERE fr.id_reservation = $1
    `,
      [id_reservation]
    );

    const flights = await Promise.all(
      flightsResult.rows.map(async (flight) => {
        // 3. Obtener equipaje por cada vuelo de la reserva
        const luggageResult = await pool.query(
          `
        SELECT type, amount
        FROM luggage
        WHERE id_reservation = $1 AND code_flight = $2 AND departure_date = $3 AND arrival_date = $4
      `,
          [
            id_reservation,
            flight.code_flight,
            flight.departure_date,
            flight.arrival_date,
          ]
        );

        return {
          ...flight,
          luggage: luggageResult.rows,
        };
      })
    );

    // 4. Responder con la estructura esperada
    res.json({
      id_reservation,
      reservation_name,
      total_cost,
      flight_count: flights.length,
      flights,
    });
  } catch (err) {
    console.error("Error al obtener detalles de la reserva:", err.message);
    res.status(500).json({ error: "Error interno del servidor" });
  }
});

app.put("/flight/update", async (req, res) => {
  const { idReservation, codeFlight, cost, passengers, departureDate, arrivalDate } = req.query;
  const luggageList = req.body;

  const client = await pool.connect();

  try {
    await client.query("BEGIN");

    // 1. Actualizar datos del vuelo en la reserva
    await client.query(
      `
      UPDATE flight_reservation
      SET cost = $1, persons = $2
      WHERE id_reservation = $3 AND code_flight = $4
      `,
      [cost, passengers, idReservation, codeFlight]
    );

    // 2. Eliminar equipaje anterior para ese vuelo+reserva
    await client.query(
      `
      DELETE FROM luggage
      WHERE id_reservation = $1 AND code_flight = $2
      `,
      [idReservation, codeFlight]
    );

    // 3. Insertar nuevo equipaje
    for (const luggage of luggageList) {
      await client.query(
        `INSERT INTO luggage (id_reservation, code_flight, departure_date, arrival_date, amount, type)
         VALUES ($1, $2, $3, $4, $5, $6)`,
        [idReservation, codeFlight, departureDate, arrivalDate, luggage.amount, luggage.type]
      );
    }

    // 4. Recalcular el coste total de la reserva
    const totalResult = await client.query(
      `
      SELECT SUM(cost) AS total
      FROM flight_reservation
      WHERE id_reservation = $1
      `,
      [idReservation]
    );

    const newTotal = parseFloat(totalResult.rows[0].total || 0);

    await client.query(
      `
      UPDATE reservation
      SET total_cost = $1
      WHERE id_reservation = $2
      `,
      [newTotal, idReservation]
    );

    await client.query("COMMIT");

    res.status(200).json({ message: "Detalles del vuelo actualizados correctamente", newTotal });
  } catch (err) {
    await client.query("ROLLBACK");
    console.error("Error al actualizar el vuelo:", err.message);
    res.status(500).json({ error: "Error interno del servidor" });
  } finally {
    client.release();
  }
});

app.listen(port, "0.0.0.0", () => {
  console.log(`Servidor escuchando en http://localhost:${port}`);
});
