package com.iscram.k8s.consumer.boundary.rest;

import com.iscram.k8s.consumer.control.CarService;
import com.iscram.k8s.consumer.provider.api.model.Car;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty; // Import für @ConfigProperty

import java.net.URI;
import java.util.List;

@Slf4j
@Path("/cars-ui")
@Produces(MediaType.TEXT_HTML)
@Blocking
public class CarTemplateResource {
    @Inject
    Template cars;

    @Inject
    CarService carService;

    @ConfigProperty(name = "ui.cars.base.path", defaultValue = "/") // Injiziert den Konfigurationswert
    String carsUiBasePath;

    @ConfigProperty(name = "hostname", defaultValue = "local")
    String hostname;

    @GET
    public TemplateInstance getCarsPage(@QueryParam("error") String error) {
        log.info("Accessing car management UI. Error: {}", error);
        List<Car> allCars = carService.getAllCars();
        // Übergibt den Pfad als Variable an das Template
        return cars.data("cars", allCars, "error", error, "carsUiBasePath", carsUiBasePath, "hostname", hostname);
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addCar(@FormParam("brand") String brand,
                           @FormParam("model") String model,
                           @FormParam("color") String color) {
        log.info("Attempting to add new car: brand={}, model={}, color={}", brand, model, color);
        try {
            Car newCar = new Car();
            newCar.setBrand(brand);
            newCar.setModel(model);
            newCar.setColor(color);
            carService.addCar(newCar);
            log.info("Successfully added car: {}", newCar);
            return Response.seeOther(URI.create(carsUiBasePath+"cars-ui")).build();
        } catch (Exception e) {
            log.error("Error adding car: {}", e.getMessage(), e);
            return Response.seeOther(UriBuilder.fromUri(carsUiBasePath+"cars-ui").queryParam("error", e.getMessage()).build()).build();
        }
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateCar(@FormParam("id") Long id,
                              @FormParam("brand") String brand,
                              @FormParam("model") String model,
                              @FormParam("color") String color) {
        log.info("Attempting to update car with ID: {}. New data: brand={}, model={}, color={}", id, brand, model, color);
        try {
            Car updatedCar = new Car();
            updatedCar.setId(id);
            updatedCar.setBrand(brand);
            updatedCar.setModel(model);
            updatedCar.setColor(color);
            carService.updateCar(id, updatedCar);
            log.info("Successfully updated car with ID: {}", id);
            return Response.seeOther(URI.create(carsUiBasePath+"cars-ui")).build();
        } catch (Exception e) {
            log.error("Error updating car with ID {}: {}", id, e.getMessage(), e);
            return Response.seeOther(UriBuilder.fromUri(carsUiBasePath+"cars-ui").queryParam("error", e.getMessage()).build()).build();
        }
    }

    @POST
    @Path("/delete/{id}")
    public Response deleteCar(@PathParam("id") Long id) {
        log.info("Attempting to delete car with ID: {}", id);
        try {
            carService.deleteCar(id);
            log.info("Successfully deleted car with ID: {}", id);
            return Response.seeOther(URI.create(carsUiBasePath+"cars-ui")).build();
        } catch (Exception e) {
            log.error("Error deleting car with ID {}: {}", id, e.getMessage(), e);
            return Response.seeOther(UriBuilder.fromUri(carsUiBasePath+"cars-ui").queryParam("error", e.getMessage()).build()).build();
        }
    }
}
