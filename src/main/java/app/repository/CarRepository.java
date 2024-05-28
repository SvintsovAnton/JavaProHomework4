package app.repository;

import app.domain.Car;

import java.math.BigDecimal;
import java.util.List;

public interface CarRepository {

    List<Car> getAll();

    Car getCar(Long id);

    void updateCar(Long id, BigDecimal newPrice);

    void deleteCar(Long id);

    Car save(Car car);
}
