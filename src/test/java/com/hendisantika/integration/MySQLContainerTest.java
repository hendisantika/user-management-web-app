package com.hendisantika.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Link : s.id/hendisantika
 * Date: 04/11/26
 * Time: 06.53
 * <p>
 * Integration test using Testcontainers (MySQL 9.5.0 & Redis 8).
 * Connection details are wired automatically via {@link ServiceConnection}.
 * The test is skipped when Docker is not available.
 * <p>
 * Run with: ./mvnw test -Dtest=MySQLContainerTest
 */
@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class MySQLContainerTest {

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer(DockerImageName.parse("mysql:9.5.0"))
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Container
    @ServiceConnection(name = "redis")
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:8-alpine"))
            .withExposedPorts(6379);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testContainersAreRunning() {
        assertThat(mysql.isRunning()).as("MySQL container should be running").isTrue();
        assertThat(redis.isRunning()).as("Redis container should be running").isTrue();
    }

    @Test
    void testDatabaseConnection() {
        String version = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
        assertThat(version).startsWith("9.5");
    }
}
