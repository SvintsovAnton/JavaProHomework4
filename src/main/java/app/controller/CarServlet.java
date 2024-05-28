package app.controller;

import app.domain.Car;
import app.repository.CarRepository;
import app.repository.CarRepositoryMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

public class CarServlet extends HttpServlet {

    private CarRepository repository = new CarRepositoryMap();

//GET http://10.2.3.4:8080/cars
//GET http://10.2.3.4:8080/cars?id=1

    //HttpServletRequest req - объект запроса, который прислал клиент, из него можем извлечь все
    //HttpServletResponse resp объект ответа, которй будет отправлен клиенту псоле того как отрбаотает данный метод
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //получение из БД всех автомобилей
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

    //POST http://10.2.3.4:8080/cars?newBrand=Subaru&newPrice=15000&newYear=2012
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (parameterMap.containsKey("newBrand") && parameterMap.containsKey("newPrice") && parameterMap.containsKey("newYear")) {
            try {
                Car car = new Car(parameterMap.get("newBrand")[0], new BigDecimal(parameterMap.get("newPrice")[0]), Integer.parseInt(parameterMap.get("newYear")[0]));
                repository.save(car);
                resp.getWriter().write("Car aded: " + car.toString());
            } catch (NumberFormatException exception) {
                resp.getWriter().write("Invalid format");
            }
        }
    }


    //PUT http://10.2.3.4:8080/cars?updateID=1&updatePrice=15000
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (parameterMap.containsKey("updateID") && parameterMap.containsKey("updatePrice")) {
            try {
                repository.updateCar(Long.parseLong(parameterMap.get("updateID")[0]), new BigDecimal(parameterMap.get("updatePrice")[0]));
                resp.getWriter().write("Car updeted " + repository.getCar(Long.parseLong(parameterMap.get("updateID")[0])).toString());
            } catch (NumberFormatException exception) {
                resp.getWriter().write("Invalid format");
            }
        }
    }


    //DELETE http://10.2.3.4:8080/cars?deleteID=1
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (parameterMap.containsKey("deleteID")) {
            try {
                repository.deleteCar(Long.parseLong(parameterMap.get("deleteID")[0]));
                resp.getWriter().write("Car with id = " + parameterMap.get("deleteID")[0] + " deleted ");
            } catch (NumberFormatException exception) {
                resp.getWriter().write("Invalid format");
            }
        }
    }
}
