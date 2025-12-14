-- Tabla: users
CREATE TABLE IF NOT EXISTS public.users (
    id_user SERIAL PRIMARY KEY,
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    uuid character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_username_key UNIQUE (username),
    CONSTRAINT users_uuid_key UNIQUE (uuid)
);

-- Tabla: airport
CREATE TABLE IF NOT EXISTS public.airport (
    id_airport SERIAL PRIMARY KEY,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    code_iata character varying(3) COLLATE pg_catalog."default" NOT NULL,
    code_icao character varying(4) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT airport_code_iata_key UNIQUE (code_iata),
    CONSTRAINT airport_code_icao_key UNIQUE (code_icao)
);

-- Tabla: airlines
CREATE TABLE IF NOT EXISTS public.airlines (
    id_airline SERIAL PRIMARY KEY,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    code_iata character varying(10) COLLATE pg_catalog."default" NOT NULL,
    code_icao character varying(10) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT airlines_code_iata_key UNIQUE (code_iata),
    CONSTRAINT airlines_code_icao UNIQUE (code_icao)
);

-- Tabla: reservation
CREATE TABLE IF NOT EXISTS public.reservation (
    id_reservation SERIAL PRIMARY KEY,
    total_cost numeric(10,2) DEFAULT 0,
    id_user integer NOT NULL,
    reservation_name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT reservation_id_user_fkey FOREIGN KEY (id_user)
        REFERENCES public.users (id_user)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- Tabla: flights
CREATE TABLE IF NOT EXISTS public.flights (
    code_flight character varying(10) COLLATE pg_catalog."default" NOT NULL,
    departure_date timestamp without time zone NOT NULL,
    arrival_date timestamp without time zone NOT NULL,
    id_airline integer NOT NULL,
    departure_airport integer NOT NULL,
    arrival_airport integer NOT NULL,
    CONSTRAINT flights_pkey PRIMARY KEY (code_flight, departure_date, arrival_date),
    CONSTRAINT flights_arrival_airport_fkey FOREIGN KEY (arrival_airport)
        REFERENCES public.airport (id_airport)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT flights_departure_airport_fkey FOREIGN KEY (departure_airport)
        REFERENCES public.airport (id_airport)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT flights_id_airline_fkey FOREIGN KEY (id_airline)
        REFERENCES public.airlines (id_airline)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- Tabla: flight_reservation
CREATE TABLE IF NOT EXISTS public.flight_reservation (
    id_reservation integer NOT NULL,
    code_flight character varying(10) COLLATE pg_catalog."default" NOT NULL,
    departure_date timestamp without time zone NOT NULL,
    arrival_date timestamp without time zone NOT NULL,
    cost numeric(10,2),
    persons integer,
    CONSTRAINT flight_reservation_pkey PRIMARY KEY (id_reservation, code_flight, departure_date, arrival_date),
    CONSTRAINT flight_reservation_code_flight_departure_date_arrival_date_fkey FOREIGN KEY (code_flight, departure_date, arrival_date)
        REFERENCES public.flights (code_flight, departure_date, arrival_date)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT flight_reservation_id_reservation_fkey FOREIGN KEY (id_reservation)
        REFERENCES public.reservation (id_reservation)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- Tabla: luggage
CREATE TABLE IF NOT EXISTS public.luggage (
    id_luggage SERIAL,
    id_reservation integer NOT NULL,
    code_flight character varying(10) NOT NULL,
    departure_date timestamp NOT NULL,
    arrival_date timestamp NOT NULL,
    type character varying(50) NOT NULL,
    amount integer NOT NULL,
    CONSTRAINT luggage_pkey PRIMARY KEY (id_luggage, id_reservation, code_flight),
    CONSTRAINT luggage_code_flight_departure_date_arrival_date_fkey FOREIGN KEY (code_flight, departure_date, arrival_date)
        REFERENCES public.flights (code_flight, departure_date, arrival_date)
        ON UPDATE NO ACTION ON DELETE CASCADE,
    CONSTRAINT luggage_id_reservation_fkey FOREIGN KEY (id_reservation)
        REFERENCES public.reservation (id_reservation)
        ON UPDATE NO ACTION ON DELETE CASCADE
);


-- Función: update_total_cost
CREATE OR REPLACE FUNCTION public.update_total_cost()
RETURNS trigger
LANGUAGE 'plpgsql'
COST 100
VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    UPDATE reservation
    SET total_cost = (
        SELECT COALESCE(SUM(cost), 0)
        FROM flight_reservation
        WHERE id_reservation = COALESCE(NEW.id_reservation, OLD.id_reservation)
    )
    WHERE id_reservation = COALESCE(NEW.id_reservation, OLD.id_reservation);

    RETURN NULL;
END;
$BODY$;

ALTER FUNCTION public.update_total_cost()
    OWNER TO postgres;

-- Trigger: trigger_update_total_cost
CREATE OR REPLACE TRIGGER trigger_update_total_cost
AFTER INSERT OR DELETE OR UPDATE 
ON public.flight_reservation
FOR EACH ROW
EXECUTE FUNCTION public.update_total_cost();
