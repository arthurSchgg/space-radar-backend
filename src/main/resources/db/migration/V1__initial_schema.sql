CREATE TABLE IF NOT EXISTS tb_asteroid (
    id UUID PRIMARY KEY,
    nasa_id BIGINT NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    min_diameter_m DOUBLE PRECISION,
    max_diameter_m DOUBLE PRECISION,
    dangerous BOOLEAN NOT NULL,
    url_jpl VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS tb_close_approach (
    id UUID PRIMARY KEY,
    asteroid_id UUID NOT NULL REFERENCES tb_asteroid(id),
    date_approach TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    velocity_kms DOUBLE PRECISION,
    distance_km DOUBLE PRECISION,
    lunar_distance DOUBLE PRECISION
);

