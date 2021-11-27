package demo;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;
import com.zaxxer.hikari.*;

import javax.sql.DataSource;

/**
 * Result (Benchmark)
 * Condition : If we try create 1000 connections.
 * - Default DataSource                             ~6 Seconds.
 * - Custom DataSourcePool with proxy Connection    ~7 Millis.
 * - DataSource with HikariCP                       ~25 Millis.
 */

public class JdbcApiDemo {
    private static DataSource dataSource;

    @SneakyThrows
    public static void main(String[] args) {
        initializeHikari();

        var start = System.nanoTime();
        for (int i = 1; i < 1000; i++) {
            try (var connection = dataSource.getConnection()) {
            }
        }
        var end = System.nanoTime();
        System.out.println((end - start) / 1_000_000 + " millis");
    }

    @SneakyThrows
    private static void initializer() {
        var pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL("jdbc:postgresql://localhost:5432/university");
        pgSimpleDataSource.setUser("user");
        pgSimpleDataSource.setPassword("password");
        dataSource = pgSimpleDataSource;
    }

    private static void initializeCustomPoolDataSource() {
        var customPoolDataSource = new CustomPoolDataSource(
                "jdbc:postgresql://localhost:5432/university",
                "user",
                "password"
        );
        dataSource = customPoolDataSource;
    }

    private static void initializeHikari() {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/university");
        hikariConfig.setUsername("user");
        hikariConfig.setPassword("password");
        dataSource = new HikariDataSource(hikariConfig);

    }
}
