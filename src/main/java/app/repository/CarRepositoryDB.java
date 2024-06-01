package app.repository;

import app.domain.Car;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static app.constants.Constants.*;

public class CarRepositoryDB implements CarRepository {

    private Connection getConnection() {
        try {
            //подгрузили драйвер для БД
            Class.forName(DB_DRIVER_PATH);
            // создаем путь к БД
            String dbUrl = String.format("%s%s?user=%s&password=%s", DB_ADDRESS, DB_NAME, DB_USERNAME, DB_PASSWORD);
            //возвращаем соединение с БД
            return DriverManager.getConnection(dbUrl);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String query = String.format("SELECT * FROM car");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Car car = new Car(resultSet.getLong("id"), resultSet.getString("brand"), resultSet.getBigDecimal("price"), resultSet.getInt("year"));
                cars.add(car);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cars;
    }

    @Override
    public Car getCar(Long id) {
        String query = String.format("SELECT * FROM car\n" + "WHERE id=%s", id);
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
            Car car = new Car(resultSet.getLong("id"), resultSet.getString("brand"), resultSet.getBigDecimal("price"), resultSet.getInt("year"));
            return car;}else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCar(Car car) {

        try (Connection connection = getConnection()) {
            String query = String.format("UPDATE car SET price =%s WHERE car.id=%d;", car.getPrice(), car.getId());
            Statement statement = connection.createStatement();
            statement.execute(query, statement.RETURN_GENERATED_KEYS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCar(Long id) {
        try (Connection connection = getConnection()) {
            String query = String.format("DELETE FROM car WHERE id=%s", id);
            Statement statement = connection.createStatement();
            statement.execute(query, statement.RETURN_GENERATED_KEYS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car save(Car car) {

        try (Connection connection = getConnection()) {
            //created SQL
            String query = String.format("INSERT INTO car(brand,price,year) VALUES ('%s','%s','%d');", car.getBrand(), car.getPrice(), car.getYear());

            // создаем объект стэйтмента
            Statement statement = connection.createStatement();
            //отправляем запрос в базу данных -
            statement.execute(query, statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();

            Long id = resultSet.getLong(1);
            car.setId(id);
            return car;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
