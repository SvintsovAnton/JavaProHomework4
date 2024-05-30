package app.repository;

import app.domain.Car;

import java.math.BigDecimal;
import java.util.List;

public interface CarRepository {

    List<Car> getAll();

    Car getCar(Long id);

    void updateCar(Car car);
    //Car update(Car car); -

    void deleteCar(Long id);
    //Car delete

    Car save(Car car);
}
