# Docker Quick Reference Guide

## Running the Application

Start all services:

```bash
docker compose up -d
```

## Service Details

### MySQL 9.5.0

- **Container Name:** user-management-mysql
- **External Port:** 3307
- **Internal Port:** 3306
- **Database:** user_management
- **Username:** yu71
- **Password:** 53cret
- **Root Password:** 53cret

### Redis 7

- **Container Name:** user-management-redis
- **Port:** 6379

### Spring Boot Application

- **Container Name:** user-management-app
- **HTTP Port:** 8080
- **HTTPS Port:** 8443

## Accessing Services

### Web Application

- **HTTPS:** https://localhost:8443
- **HTTP:** http://localhost:8080 (redirects to HTTPS)

### MySQL Database

```bash
# Connect to MySQL
docker exec -it user-management-mysql mysql -uyu71 -p53cret user_management

# Run SQL query
docker exec user-management-mysql mysql -uyu71 -p53cret -e "USE user_management; SELECT * FROM user;"
```

### Redis

```bash
# Connect to Redis CLI
docker exec -it user-management-redis redis-cli

# Check Redis keys
docker exec user-management-redis redis-cli KEYS '*'
```

### Application Logs

```bash
# View all logs
docker compose logs -f

# View specific service logs
docker compose logs -f app
docker compose logs -f mysql
docker compose logs -f redis

# Last 100 lines
docker compose logs --tail=100 app
```

## Container Management

### Start/Stop

```bash
# Start all services
docker compose up -d

# Stop all services
docker compose down

# Stop and remove volumes (clean state)
docker compose down -v

# Restart a specific service
docker compose restart app
```

### Rebuild

```bash
# Rebuild after code changes
docker compose up -d --build

# Rebuild specific service
docker compose up -d --build app
```

### Status and Health

```bash
# Check container status
docker compose ps

# Check resource usage
docker stats user-management-app user-management-mysql user-management-redis

# Check health
docker inspect user-management-mysql | grep Health -A 10
```

## Database Operations

### Backup

```bash
# Backup database
docker exec user-management-mysql mysqldump -uyu71 -p53cret user_management > backup.sql

# Restore database
docker exec -i user-management-mysql mysql -uyu71 -p53cret user_management < backup.sql
```

### Reset Database

```bash
# Drop and recreate database
docker exec user-management-mysql mysql -uyu71 -p53cret -e "DROP DATABASE IF EXISTS user_management; CREATE DATABASE user_management;"

# Restart application to recreate tables
docker compose restart app
```

## Troubleshooting

### Container won't start

```bash
# Check logs for errors
docker compose logs app

# Remove and recreate containers
docker compose down
docker compose up -d
```

### Port conflicts

```bash
# Check what's using the port
lsof -i :8080
lsof -i :3307

# Modify docker-compose.yml to use different ports
```

### MySQL initialization taking long

```bash
# Wait for MySQL to be healthy (30-60 seconds on first run)
watch docker compose ps

# Force restart after initialization
docker compose restart app
```

### Clear everything and start fresh

```bash
docker compose down -v
docker system prune -a
docker compose up -d --build
```

## Development Workflow

### Code Changes

```bash
# 1. Make code changes
# 2. Rebuild application
docker compose up -d --build app

# 3. Check logs
docker compose logs -f app
```

### Debug Mode

```bash
# Access application container
docker exec -it user-management-app sh

# Check Java process
docker exec user-management-app ps aux | grep java

# Check network connectivity
docker exec user-management-app ping mysql
docker exec user-management-app ping redis
```

## Network Information

All containers are on the `app-network` bridge network:

```bash
# Inspect network
docker network inspect user-management-web-app_app-network

# List containers on network
docker network inspect user-management-web-app_app-network --format='{{json .Containers}}' | jq
```

## Volume Management

### List Volumes

```bash
docker volume ls | grep user-management
```

### Inspect MySQL Volume

```bash
docker volume inspect user-management-web-app_mysql-data
```

### Backup Volume

```bash
docker run --rm -v user-management-web-app_mysql-data:/data -v $(pwd):/backup alpine tar czf /backup/mysql-data-backup.tar.gz -C /data .
```

### Restore Volume

```bash
docker run --rm -v user-management-web-app_mysql-data:/data -v $(pwd):/backup alpine sh -c "cd /data && tar xzf /backup/mysql-data-backup.tar.gz"
```

## Performance Monitoring

### Resource Usage

```bash
# Real-time stats
docker stats

# Container logs with timestamps
docker compose logs -f --timestamps app
```

### Health Checks

```bash
# Application health
curl -k https://localhost:8443/actuator/health

# MySQL health
docker exec user-management-mysql mysqladmin ping -h localhost -u root -p53cret

# Redis health
docker exec user-management-redis redis-cli ping
```

## Production Considerations

For production deployment, consider:

1. **Environment Variables**: Use `.env` file for sensitive data
2. **Volume Backups**: Regular backups of MySQL data
3. **Resource Limits**: Add memory and CPU limits in docker-compose.yml
4. **Logging**: Configure log rotation and external log aggregation
5. **SSL Certificates**: Use valid SSL certificates instead of self-signed
6. **Secrets Management**: Use Docker secrets for passwords
7. **Health Checks**: Monitor health check endpoints
8. **Reverse Proxy**: Use nginx or traefik for SSL termination
9. **Database Tuning**: Optimize MySQL configuration for production load
10. **Monitoring**: Add Prometheus/Grafana for metrics
