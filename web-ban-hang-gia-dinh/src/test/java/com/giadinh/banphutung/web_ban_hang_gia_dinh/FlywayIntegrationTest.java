package com.giadinh.banphutung.web_ban_hang_gia_dinh;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.testcontainers.containers.PostgreSQLContainer;
import org.flywaydb.core.Flyway;

public class FlywayIntegrationTest {

    @Test
    @Disabled("Requires Docker environment")
    @DisabledIfSystemProperty(named = "skipIntegrationTests", matches = "true")
    void flywayRunsOnPostgres() {
        try (PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:15")) {
            pg.start();

            Flyway flyway = Flyway.configure()
                    .dataSource(pg.getJdbcUrl(), pg.getUsername(), pg.getPassword())
                    .locations("classpath:db/migration")
                    .load();
            flyway.migrate();
            pg.stop();
        }
    }
}
