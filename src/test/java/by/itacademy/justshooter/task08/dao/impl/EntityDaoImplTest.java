package by.itacademy.justshooter.task08.dao.impl;

import by.itacademy.justshooter.task08.entity.Car;
import by.itacademy.justshooter.task08.dao.EntityDao;
import by.itacademy.justshooter.task08.dao.EntityDaoException;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntityDaoImplTest extends Assert {
    public static final String DATABASE = "database";
    private static ConnectionPool connectionPool;
    private static EntityDao dao;
    private static Map<Long, Object> map = new HashMap<>();
    private long finishKeyMap;
    private static Car car1;
    private static Car car2;
    private static Car car3;
    private static Car car4;
    private static Car car5;
    private static Object fakeEntity = new Object();

    public EntityDaoImplTest() {
    }

    @BeforeClass
    public static void setUp() throws Exception {
        ResourceBundle bundleH2 = ResourceBundle.getBundle(DATABASE);
        connectionPool = new ConnectionPool(bundleH2);
        assertNotNull(connectionPool.getConnection());
        java.sql.Connection connection = DriverManager
                .getConnection("jdbc:h2:mem:jd2task08;DB_CLOSE_DELAY=-1"
                                + ";DATABASE_TO_UPPER=false"
                                + ";TRACE_LEVEL_SYSTEM_OUT=3",
                        "sa",
                        "");
        try {
            Database database = DatabaseFactory
                    .getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase =
                    new Liquibase("liquibase/changeLog/changelog.xml",
                            new ClassLoaderResourceAccessor(),
                            database);
            liquibase.update(new Contexts());
        } finally {
            if (connection != null) {
                connection.rollback();
                connection.close();
            }
        }
        dao = new EntityDaoImpl(Car.class, connectionPool);
        assertNotNull(dao);
        car1 = new Car("Skoda", "Purple", 30000);
        car2 = new Car("Renault", "Silver", 14000);
        car3 = new Car("Hummer", "Cyan", 58000);
        car4 = null;
        car5 = new Car("Tesla", "Magenta", 66000);
    }

    @Test
    public void testInsertDAO() throws EntityDaoException {
        dao.insert(car1);
        dao.insert(car2);
        dao.insert(car3);
        for (Long key : dao.select().keySet()) {
            if (key.intValue() > finishKeyMap) {
                finishKeyMap = key.intValue();
            }
        }
        map.put(finishKeyMap - 2, car1);
        map.put(finishKeyMap - 1, car2);
        map.put(finishKeyMap, car3);
        assertEquals("Mistake! Element with ID: "
                        + finishKeyMap
                        + " not added!",
                map.get(finishKeyMap),
                dao.select().get(finishKeyMap));
        assertEquals("Mistake! Element with ID: "
                        + (finishKeyMap - 1)
                        + " not added!",
                map.get(finishKeyMap - 1),
                dao.select().get(finishKeyMap - 1));
        assertEquals("Mistake! Element with ID: "
                        + (finishKeyMap - 2)
                        + " not added!",
                map.get(finishKeyMap - 2),
                dao.select().get(finishKeyMap - 2));
    }

    @Test
    public void testUpdateDao() throws EntityDaoException {//проверка метода UPDATE
        long updateElement = 1L;
        map.put(updateElement, car3);
        map.replace(updateElement, car3);
        try {
            dao.update(updateElement, car3);
        } catch (EntityDaoException e) {
            e.printStackTrace();
            System.out.println("Mistake! Attempt to replace the data of a non-existent string!");
        }
        Assert.assertEquals("Mistake! Data update operation failed!",
                map.get(updateElement),
                dao.select().get(updateElement));
        Assert.assertEquals(dao.select().get(updateElement).hashCode(),
                car3.hashCode());
    }

    @Test(expected = EntityDaoException.class)
    public void testDeleteDaoException() throws EntityDaoException{
        long deleteElement = 2L;
        try {
            dao.delete(deleteElement);
        } catch (EntityDaoException e) {
            e.printStackTrace();
            System.out.println("Mistake! Attempt to delete a non-existent row!");
        }
            dao.delete(deleteElement);
    }

    @Test
    public void testSelectDao() throws EntityDaoException {
        long elementSelect = 3L;
        Assert.assertNotNull("Mistake! Row not found!", dao.select().get(elementSelect));
    }

    @Test(expected = EntityDaoException.class)
    public void testUpdateException() throws EntityDaoException {
        Long updateException = 218L;
        dao.update(updateException, car5);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = NullPointerException.class)
    public void testInsertException() throws EntityDaoException {
        dao.insert(car4);
        Assert.fail("Mistake! Exception: " + NullPointerException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = EntityDaoException.class)
    public void testEntityDaoImpl() throws EntityDaoException {
        class MyFakeClass {
        }
        EntityDao dao1 = new EntityDaoImpl(MyFakeClass.class, connectionPool);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = EntityDaoException.class)
    public void testGetObjectParam() throws EntityDaoException {
        EntityDaoImpl entityDao = new EntityDaoImpl(Car.class, connectionPool);
        entityDao.update(1L, fakeEntity);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = EntityDaoException.class)
    public void testCreateEntity() throws EntityDaoException {
        EntityDao dao2 = new EntityDaoImpl(Car.class, connectionPool);
        dao2.insert(fakeEntity);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

}