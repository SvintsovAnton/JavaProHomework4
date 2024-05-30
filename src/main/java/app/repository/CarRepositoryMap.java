package app.repository;

import app.domain.Car;

import javax.swing.text.html.HTMLDocument;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

public class CarRepositoryMap implements CarRepository {

    private Map<Long, Car> database = new HashMap<>();
    private long currentId;

    public CarRepositoryMap() {
        save(new Car("Volkswagen", new BigDecimal(10000), 2010));
        save(new Car("Mazda", new BigDecimal(30000), 2015));
        save(new Car("Honda", new BigDecimal(50000), 2020));
    }

    @Override
    public List<Car> getAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Car getCar(Long id) {
        return database.get(id);
    }

    @Override
    public void updateCar(Car car) {
        Car existingCar = database.get(car.getId());
        if (existingCar != null) {
            existingCar.setPrice(car.getPrice());
        } else {
            throw new IllegalArgumentException("car with id " + car.getId() + "not found");
        }
    }

    @Override
    public void deleteCar(Long id) {
        database.remove(id);
    }


    @Override
    public Car save(Car car) {
        car.setId(++currentId);
        database.put(currentId, car);
        return car;
    }

}
