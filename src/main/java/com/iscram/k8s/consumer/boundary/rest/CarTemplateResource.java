package com.iscram.k8s.consumer.boundary.rest;

import com.iscram.k8s.consumer.control.CarService;
import com.iscram.k8s.consumer.provider.api.model.Car; // Importiert das generierte Car-Modell
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.List;
@Path("/cars-ui")
@Produces(MediaType.TEXT_HTML)
@Blocking
public class CarTemplateResource {

    @Inject
    Template cars; // Das cars.html Template

    @Inject
    CarService carService;

    @GET
    public TemplateInstance getCarsPage(@QueryParam("error") String error) {
        List<Car> allCars = carService.getAllCars();
        return cars.data("cars", allCars, "error", error);
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addCar(@FormParam("brand") String brand,
                           @FormParam("model") String model,
                           @FormParam("color") String color) {
        try {
            Car newCar = new Car(); // Standardkonstruktor
            // ID wird hier nicht gesetzt, da sie typischerweise vom Backend generiert wird
            newCar.setBrand(brand);
            newCar.setModel(model);
            newCar.setColor(color);
            carService.addCar(newCar);
            return Response.seeOther(URI.create("/cars-ui")).build();
        } catch (Exception e) {
            return Response.seeOther(UriBuilder.fromUri("/cars-ui").queryParam("error", e.getMessage()).build()).build();
        }
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateCar(@FormParam("id") Long id,
                              @FormParam("brand") String brand,
                              @FormParam("model") String model,
                              @FormParam("color") String color) {
        try {
            Car updatedCar = new Car(); // Standardkonstruktor
            updatedCar.setId(id);
            updatedCar.setBrand(brand);
            updatedCar.setModel(model);
            updatedCar.setColor(color);
            carService.updateCar(id, updatedCar);
            return Response.seeOther(URI.create("/cars-ui")).build();
        } catch (Exception e) {
            return Response.seeOther(UriBuilder.fromUri("/cars-ui").queryParam("error", e.getMessage()).build()).build();
        }
    }

    @POST
    @Path("/delete/{id}")
    public Response deleteCar(@PathParam("id") Long id) { // Typ Long, passend zum CarService
        try {
            carService.deleteCar(id);
            return Response.seeOther(URI.create("/cars-ui")).build();
        } catch (Exception e) {
            return Response.seeOther(UriBuilder.fromUri("/cars-ui").queryParam("error", e.getMessage()).build()).build();
        }
    }
}
