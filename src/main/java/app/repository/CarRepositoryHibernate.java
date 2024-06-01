package app.repository;

import app.constants.Constants;
import app.domain.Car;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.hibernate.cfg.Configuration;


import java.util.List;

public class CarRepositoryHibernate implements CarRepository {

    //это объект, которые знает все о нашей ЬД и будет с ней работать
    private EntityManager entityManager;

    //Создается новый объект класса Configuration из библиотеки Hibernate.
    public CarRepositoryHibernate() {
        entityManager = new Configuration().configure("hibernate/postgres.cfg.xml")
                .buildSessionFactory()
                .createEntityManager();
    }

    @Override
    public List<Car> getAll() {
        Query query = entityManager.createNativeQuery("SELECT * FROM car",Car.class);
        return query.getResultList();
    }

    @Override
    public Car getCar(Long id) {
        return entityManager.find(Car.class, id);
    }

    @Override
    public void updateCar(Car car) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(car);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Transaction cannceled");
        }
    }

    @Override
    public void deleteCar(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.find(Car.class, id));
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Transaction cannceld");
        }

    }

    @Override
    public Car save(Car car) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(car);
            entityManager.getTransaction().commit();
            return car;
        } catch (Exception e) {
            throw new RuntimeException("Transaction cannceled");
        }
    }
}
