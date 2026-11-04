# Spring Boot Docker Support Libraries

## Overview

This project includes comprehensive Docker support through Spring Boot and Testcontainers integration.

## Dependencies Added

### 1. Spring Boot Docker Compose Support

**Dependency:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-docker-compose</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

**Features:**
- Automatic Docker Compose lifecycle management
- Starts Docker Compose services when the application starts
- Stops Docker Compose services when the application stops
- Configures connection properties automatically

**Configuration:**
```properties
spring.docker.compose.enabled=true
spring.docker.compose.file=docker-compose.yml
spring.docker.compose.lifecycle-management=start_and_stop
```

**Usage:**
```bash
# Run with Docker Compose auto-start
./mvnw spring-boot:run -Dspring-boot.run.profiles=docker
```

### 2. Testcontainers

**Dependencies:**
```xml
<!-- Core Testcontainers -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers</artifactId>
    <scope>test</scope>
</dependency>

<!-- MySQL Testcontainers (Testcontainers 2.x artifact name) -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers-mysql</artifactId>
    <scope>test</scope>
</dependency>

<!-- JUnit Jupiter Integration (Testcontainers 2.x artifact name) -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>

<!-- Spring Boot @ServiceConnection support -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-testcontainers</artifactId>
    <scope>test</scope>
</dependency>
```

> **Note:** Testcontainers 2.x (managed by Spring Boot 4) renamed the modules with a `testcontainers-` prefix
> (`mysql` → `testcontainers-mysql`, `junit-jupiter` → `testcontainers-junit-jupiter`) and moved
> `MySQLContainer` to the `org.testcontainers.mysql` package.

**Features:**
- Docker-based integration testing
- Automatic container lifecycle management
- Supports MySQL 9.7.2 for testing
- Database schema validation
- Connection pooling tests

**Sample Test** (`src/test/java/com/hendisantika/integration/MySQLContainerTest.java`):
```java
@SpringBootTest
@Testcontainers(disabledWithoutDocker = true) // skipped when Docker is not available
class MySQLContainerTest {

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer(DockerImageName.parse("mysql:9.7.2"))
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
    void testDatabaseConnection() {
        assertThat(jdbcTemplate.queryForObject("SELECT VERSION()", String.class)).startsWith("9.7");
    }
}
```

`@ServiceConnection` wires the datasource and Redis connection details automatically, so no
`@DynamicPropertySource` is needed.

**Running Tests:**
```bash
# Run specific integration test
./mvnw test -Dtest=MySQLContainerTest

# Run all tests including Testcontainers
./mvnw verify
```

### 3. Cloud Native Buildpacks Support

**Configuration in pom.xml:**
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <image>
            <name>hendisantika/${project.artifactId}:${project.version}</name>
            <env>
                <BP_JVM_VERSION>25</BP_JVM_VERSION>
            </env>
        </image>
    </configuration>
</plugin>
```

**Building Docker Image with Buildpacks:**
```bash
# Build Docker image using Cloud Native Buildpacks
./mvnw spring-boot:build-image

# Run the built image
docker run -p 8080:8080 -p 8443:8443 \
  hendisantika/user-management-web-app:0.0.1-SNAPSHOT
```

**Features:**
- No Dockerfile required
- Automatic optimization
- Security best practices
- Layer caching for faster builds
- Java 25 runtime environment

## Benefits

### 1. Development Productivity
- **Auto-start services**: Docker Compose starts automatically with the app
- **Faster feedback**: Testcontainers run tests against real databases
- **No manual setup**: Services configured automatically

### 2. Testing Quality
- **Real database**: Tests run against actual MySQL 9.7.2
- **Isolated environment**: Each test gets a fresh container
- **Parallel execution**: Multiple test suites can run simultaneously

### 3. Production Parity
- **Same database**: Testing with the exact MySQL version used in production
- **Same configuration**: Docker Compose settings mirror production
- **Consistent environment**: Development, testing, and production use identical setups

## Usage Examples

### Example 1: Local Development with Auto-Start

```bash
# Start application - Docker Compose starts automatically
./mvnw spring-boot:run -Dspring-boot.run.profiles=docker

# Application runs with MySQL and Redis from Docker Compose
# When you stop the app, Docker Compose stops automatically
```

### Example 2: Integration Testing

```java
@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:tc:mysql:9.7.2:///testdb"
})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");

        UserEntity saved = userRepository.save(user);

        assertNotNull(saved.getId());
    }
}
```

### Example 3: Building Production Images

```bash
# Option 1: Using Dockerfile (current approach)
docker compose build

# Option 2: Using Cloud Native Buildpacks
./mvnw spring-boot:build-image

# Option 3: Using Jib (alternative)
# Add Jib plugin and run:
# ./mvnw compile jib:dockerBuild
```

## Configuration Files

### docker-compose.yml
Main Docker Compose configuration for the application stack.

### application-docker.properties
Spring Boot configuration for Docker environment with automatic service discovery.

### Dockerfile
Multi-stage build for optimized container images.

## Best Practices

1. **Use Testcontainers for integration tests**
   - Test against real databases
   - Validate schema migrations
   - Test complex queries

2. **Enable Docker Compose support in development**
   - Reduces manual setup
   - Ensures service availability
   - Matches production configuration

3. **Build images with Buildpacks**
   - Better security
   - Automatic optimizations
   - Standardized approach

4. **Version control your Docker config**
   - docker-compose.yml in git
   - Document environment variables
   - Maintain compatibility

## Troubleshooting

### Testcontainers Issues

**Problem:** Testcontainers can't find Docker
```bash
# Solution: Ensure Docker is running
docker ps

# Set DOCKER_HOST if needed
export DOCKER_HOST=unix:///var/run/docker.sock
```

**Problem:** Permission denied on Docker socket
```bash
# Solution: Add user to docker group
sudo usermod -aG docker $USER
# Then log out and log back in
```

### Spring Boot Docker Compose Issues

**Problem:** Services don't start automatically
```bash
# Check if Docker Compose support is enabled
# In application-docker.properties:
spring.docker.compose.enabled=true
```

**Problem:** Port conflicts
```bash
# Stop existing containers
docker compose down

# Check ports
lsof -i :3306
lsof -i :6379
```

## References

- [Spring Boot Docker Compose Support](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.docker-compose)
- [Testcontainers Documentation](https://www.testcontainers.org/)
- [Cloud Native Buildpacks](https://buildpacks.io/)
- [Spring Boot Maven Plugin - Docker Images](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#build-image)

## Version Information

- **Spring Boot:** 4.1.2
- **Testcontainers:** 2.0.5 (managed by Spring Boot)
- **MySQL Container:** 9.7.2
- **Redis Container:** 8-alpine
- **Java Version:** 25
