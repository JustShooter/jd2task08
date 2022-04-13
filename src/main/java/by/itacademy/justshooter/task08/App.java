package by.itacademy.justshooter.task08;

import by.itacademy.justshooter.task08.dao.EntityDao;
import by.itacademy.justshooter.task08.dao.EntityDaoException;
import by.itacademy.justshooter.task08.dao.impl.ConnectionPool;
import by.itacademy.justshooter.task08.dao.impl.EntityDaoImpl;
import by.itacademy.justshooter.task08.entity.Car;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * There is a Person entity, it has
 * - an identifier;
 * - a first name
 * - a last name.
 *
 * The task is to create a project. Adding a table to the database must be
 * done through liquibase,
 * make tests using H2.
 * Cover functionality with tests and make a report using the jacoco plugin.
 * Connect checkstyle to the project.
 * For the Person entity, make a DAO over each field, write your own annotation
 * MyColumn(name - the name of the column) with the name of the column,
 * write the annotation MyTable(name - the name of the table)
 * above the Person class.
 * Implement CRUD operations on Person using jdbc
 *
 * - select
 * - update
 * - delete
 * - insert
 *
 * Moreover, these operations should make a request to the database
 * using the annotations
 * MyColumn and MyTable (through reflection). i.e. if the user of
 * this API creates another entity, then
 * - select
 * - update
 * - delete
 * - insert
 * should work without changing the internal logic
 *
 * @author LidiaZh
 * @author JustShooter Aliaksei Iyunski
 * @author Flashsan
 * @author Sergey060890
 * @author Yusikau Aliaksandr
 * @version 1.1rev2
 */

public final class App {
    /**
     * Constant String name of database.
     */
    public static final String DATABASE = "database";
    /**
     * Constant id for update.
     */
    public static final long ID_FOR_UPDATE = 4L;
    /**
     * Constant id for delete.
     */
    public static final long ID_FOR_DELETE = 4L;
    /**
     * Price 11000.
     */
    public static final int PRICE1 = 11000;
    /**
     * Price 2100.
     */
    public static final int PRICE2 = 2100;
    /**
     * Price 15000.
     */
    public static final int PRICE3 = 15000;

    private App() {
    }

    /**
     * Main class runner.
     *
     * @param args some args
     * @throws EntityDaoException Really rare exception
     */
    public static void main(final String[] args) throws EntityDaoException {
        ResourceBundle bundleMySql = ResourceBundle.getBundle(DATABASE);
        ConnectionPool connectionPool = new ConnectionPool(bundleMySql);
        EntityDao dao = new EntityDaoImpl(Car.class, connectionPool);

        printMap("Original DB:", dao.select());

        dao.insert(new Car("Haval", "Blue", PRICE1));
        printMap("\nInserted Car(Haval, Blue, 11000):", dao.select());

        dao.insert(new Car("Lada", "Green", PRICE2));
        printMap("\nInserted Car(Lada, Green, 2100):", dao.select());


        Car car1 = new Car("Ford", "Red", PRICE3);
        System.out.println("\nCreate Car(Ford, Red, 15000)");

        dao.update(ID_FOR_UPDATE, car1);
        printMap("\nUpdated position #" + ID_FOR_UPDATE
                + " by Car (Ford...):", dao.select());

        dao.delete(ID_FOR_DELETE);
        printMap("\nDeleted position #" + ID_FOR_DELETE
                + " :", dao.select());

    }

    private static void printMap(final String message,
                                 final Map<Long, Object> map) {
        System.out.println(message);
        for (Map.Entry<Long, Object> longObjectEntry : map.entrySet()) {
            System.out.println("ID = " + longObjectEntry.getKey());
            System.out.println(longObjectEntry.getValue());
        }
    }
}
