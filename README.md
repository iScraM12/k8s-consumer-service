# Car Consumer Service

This project is a Quarkus application that consumes an external Car Service API and provides a web UI for interaction.

It allows users to perform CRUD operations on Car entities through a Qute-based web interface, by making calls to a separate Car Service API (the provider).

## Core Technologies

- **Quarkus**: Supersonic Subatomic Java Framework
- **JAX-RS (REST)**: For exposing its own UI endpoints
- **Qute**: Quarkus' templating engine for dynamic web pages
- **MicroProfile REST Client**: For consuming the external Car Service API
- **Quarkus OpenAPI Generator**: For generating the client code for the external API based on its OpenAPI specification
- **JUnit 5 & Mockito**: For unit and integration testing

## API Endpoints (Consumer's UI)

The following UI endpoints are available:

- `GET /hello`: Retrieves a simple greeting.
- `GET /template`: Renders a Qute template, optionally with a name query parameter (e.g., `/template?name=World`).
- `GET /cars-ui`: Displays the Car Management web interface, listing all cars and providing forms for CRUD operations.
- `POST /cars-ui/add`: Adds a new car via the UI form.
- `POST /cars-ui/update`: Updates an existing car via the UI form.
- `POST /cars-ui/delete/{id}`: Deletes a car by its ID via the UI form.

The external Car Service API (provider) that this application consumes might be documented via its own OpenAPI Swagger UI, typically at `/q/swagger-ui` on the provider's address.

## Running the Application

### Development Mode

In development mode, the application will automatically pick up changes and restart. It is configured to run on port `8090`.

To start the application in dev mode, run:

```shell
mvn quarkus:dev
```

You can then access the UI at `http://localhost:8090/cars-ui`.

### Connecting to the External Car Service API (Provider)

This consumer application connects to an external Car Service API. The base URL for this API is configured in `src/main/resources/application.yml` (or `application-dev.yml`).

You can override the provider's URL at runtime. For example, to connect to a provider running on `http://my-provider:8080`:

```shell
mvn quarkus:dev -Dquarkus.rest-client.provider_yml.url=http://my-provider:8080
```

Ensure that your Car Service API (provider) is running and accessible at the configured URL.

### Building and Running as a Docker Container

First, build your Quarkus application and create a native executable:

```shell
mvn clean package -Dnative
```

Then, build the Docker image:

```shell
docker build -t k8s-consumer-service .
```

Finally, run your application container:

```shell
docker run -it --rm \
  --network k8s-provider-service_default \
  -p 8090:8080 \
  -e QUARKUS_REST_CLIENT_PROVIDER_YML_URL=http://k8s-provider-service:8080 \
  k8s-consumer-service # Adjust image name and tag if necessary
```

If your provider service is also in Docker, you might need to connect them via a Docker network.

## Running Tests

To run the full test suite (both integration and unit tests), use:

```shell
mvn test
```
