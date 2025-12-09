# Taskflow Backend

REST API server for the Taskflow application, built with **Quarkus** and **MongoDB**.

## Features

- User registration and login with JWT authentication
- Project management with collaborators and phases
- Task CRUD operations and assignment
- Notifications system

## Requirements

- Java 17+
- Maven 3.8+
- MongoDB instance (local or remote)

## Configuration

Application settings are in `src/main/resources/application.properties`.

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `MONGODB_URI` | MongoDB connection string | — |
| `MONGODB_DATABASE` | Database name | — |
| `PORT` | HTTP server port | `8080` |

### JWT Configuration

JWT keys are stored in `src/main/resources/keys/`:
- `private.pem` — used for signing tokens
- `public.pem` — used for verifying tokens

> **Warning**: Do not commit production private keys to the repository.

Related properties:
```properties
# Verification
mp.jwt.verify.issuer=taskflow-app
mp.jwt.verify.publickey.location=keys/public.pem
mp.jwt.verify.publickey.algorithm=RS256

# Signing
smallrye.jwt.sign.key.location=keys/private.pem
smallrye.jwt.sign.key-format=pem
```

## Running the Application

### Development Mode

```bash
./mvnw quarkus:dev
```

### Production Build

```bash
./mvnw package
java -jar target/taskflow-be-1.0-SNAPSHOT-runner.jar
```

For an uber-jar (single fat JAR):

```bash
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

## API Documentation

See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for detailed endpoint documentation with request/response examples.

## Project Structure

```
src/main/java/org/taskflow/
├── config/         # Application configuration
├── controller/     # REST endpoints
├── dto/            # Request/Response objects
├── exception/      # Exception handlers
├── model/          # Domain entities
├── repository/     # Data access layer
└── service/        # Business logic
```

## Contributing

Follow the existing commit message style:
- `Add [feature] for [purpose]`
- `Enhance [component] with [improvement]`
- `Update [file] with [changes]`

## License

MIT

