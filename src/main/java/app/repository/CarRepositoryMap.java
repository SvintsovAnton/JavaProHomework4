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
    public void updateCar(Long id, BigDecimal newPrice) {

        database.forEach((keyID, valueCar) -> {
            if (keyID.toString().equals(id.toString())) {
                valueCar.setPrice(newPrice);
            }
        });
    }

    @Override
    public void deleteCar(Long id) {
        Iterator<Long> iterator = database.keySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.equals(id)) {
                iterator.remove();
            }
        }
    }

    @Override
    public Car save(Car car) {
        car.setId(++currentId);
        database.put(currentId, car);
        return car;
    }

}
