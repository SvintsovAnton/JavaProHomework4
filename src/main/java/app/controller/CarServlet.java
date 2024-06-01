package app.controller;

import app.domain.Car;
import app.repository.CarRepository;
import app.repository.CarRepositoryDB;
import app.repository.CarRepositoryHibernate;
import app.repository.CarRepositoryMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

public class CarServlet extends HttpServlet {

    //принип слабой связанности
    private CarRepository repository = new CarRepositoryHibernate();
//GET http://10.2.3.4:8080/cars
//GET http://10.2.3.4:8080/cars?id=1

    //HttpServletRequest req - объект запроса, который прислал клиент, из него можем извлечь все
    //HttpServletResponse resp объект ответа, которй будет отправлен клиенту псоле того как отрбаотает данный метод
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        Map<String, String[]> parameterMap = req.getParameterMap();

        if (parameterMap.containsKey("id")) {
            try {
                Car car = repository.getCar(Long.parseLong(parameterMap.get("id")[0]));
                if (car != null) {
                    resp.getWriter().write(car.toString());
                } else {
                    resp.getWriter().write("Car not find");
                }
            } catch (NumberFormatException e) {
                resp.getWriter().write("Invalid format");
            }
        } else if (parameterMap.isEmpty()) {
            List<Car> cars = repository.getAll();
            cars.forEach(x -> {
                try {
                    resp.getWriter().write(x.toString() + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    //POST http://10.2.3.4:8080/cars?brand=Subaru&price=15000&year=2012
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        BufferedReader reader = req.getReader();
        Car car;
        try {
            car = mapper.readValue(reader, Car.class);
            repository.save(car);
            resp.getWriter().write("Car added: " + car.toString());
        } catch (Exception e) {
            resp.getWriter().write("Error" + e.getMessage());
        }
    }


    //PUT http://10.2.3.4:8080/cars?id=1&rice=15000
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        BufferedReader reader = req.getReader();
        Car car;
        try {
            car = mapper.readValue(reader, Car.class);
            repository.updateCar(car);
            resp.getWriter().write("Car updated: " + car.toString());
        } catch (Exception e) {
            resp.getWriter().write("Etwas wrong, car don´t update :" + e.getMessage());
        }
    }


    //DELETE http://10.2.3.4:8080/cars?deleteID=1
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        BufferedReader reader = req.getReader();
        try {
            JsonNode rootNode = objectMapper.readTree(reader);
            Long id = rootNode.get("id").asLong();
            repository.deleteCar(id);
            resp.getWriter().write("car with id " + id + " deleted");
        } catch (Exception e) {
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }
}
