-- If upgrading an old database with duplicate entries, remove duplicates in a reviewed backup first.
CREATE UNIQUE INDEX IF NOT EXISTS uk_approach_asteroid_date
    ON tb_close_approach(asteroid_id, date_approach);
CREATE INDEX IF NOT EXISTS idx_approach_date ON tb_close_approach(date_approach);
CREATE INDEX IF NOT EXISTS idx_approach_asteroid ON tb_close_approach(asteroid_id);

