package com.iscram.k8s.consumer.control;

import com.iscram.k8s.consumer.provider.api.api.CarResourceApi;
import com.iscram.k8s.consumer.provider.api.model.Car;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.smallrye.common.annotation.Blocking; // Import für die @Blocking Annotation

import java.util.List;

@ApplicationScoped
public class CarService {

    @RestClient
    CarResourceApi carResourceApi;

    public List<Car> getAllCars() {
        return carResourceApi.getAllCars();
    }

    public Car getCarById(Long id) {
        return carResourceApi.getCarById(id);
    }

    public Car addCar(Car car) {
        return carResourceApi.createCar(car);
    }

    public Car updateCar(Long id, Car car) { // String -> Long
        return carResourceApi.updateCar(id, car);
    }

    public void deleteCar(Long id){
        carResourceApi.deleteCar(id);
    }
}
