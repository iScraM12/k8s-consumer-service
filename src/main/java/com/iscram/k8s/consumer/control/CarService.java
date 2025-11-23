package com.iscram.k8s.consumer.control;

import com.iscram.k8s.consumer.provider.api.api.CarResourceApi;
import com.iscram.k8s.consumer.provider.api.model.Car;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.smallrye.common.annotation.Blocking;
import lombok.extern.slf4j.Slf4j; // Import für @Slf4j

import java.util.List;

@Slf4j // Lombok Annotation für Logger
@ApplicationScoped
public class CarService {

    @RestClient
    CarResourceApi carResourceApi;

    @Blocking
    public List<Car> getAllCars() {
        log.info("Fetching all cars from provider API");
        return carResourceApi.getAllCars();
    }

    @Blocking
    public Car getCarById(Long id) {
        log.info("Fetching car with ID: {}", id);
        return carResourceApi.getCarById(id);
    }

    @Blocking
    public Car addCar(Car car) {
        log.info("Adding new car: {}", car);
        return carResourceApi.createCar(car); // Methode addCar laut OpenAPI
    }

    @Blocking
    public Car updateCar(Long id, Car car) {
        log.info("Updating car with ID: {}. New data: {}", id, car);
        return carResourceApi.updateCar(id, car); // Methode updateCarById laut OpenAPI
    }

    @Blocking
    public void deleteCar(Long id) {
        log.info("Deleting car with ID: {}", id);
        carResourceApi.deleteCar(id); // Methode deleteCarById laut OpenAPI
    }
}
