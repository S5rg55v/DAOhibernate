package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernateImpl.class);
    private final SessionFactory sessionFactory = Util.getConnection();

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("CREATE TABLE IF NOT EXISTS jdbcconnect.users (" +
                    "id mediumint not null AUTO_INCREMENT, " +
                    "name VARCHAR(50), " +
                    "lastName VARCHAR(50), " +
                    "age tinyint, " +
                    "PRIMARY KEY (id))").executeUpdate();
            transaction.commit();
            logger.info("Table Created");
        } catch (Exception e) {
            logger.error("Error creating table", e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE jdbcconnect.users").executeUpdate();
            transaction.commit();
            logger.info("Table Dropped");
        } catch (Exception e) {
            logger.error("Error dropping table", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = "INSERT INTO users (name, lastName, age) VALUES (:name, :lastName, :age)";
            Query query = session.createNativeQuery(sql);
            query.setParameter("name", name);
            query.setParameter("lastName", lastName);
            query.setParameter("age", age);
            query.executeUpdate();

            transaction.commit();
            logger.info("User {} Saved", name);
        } catch (Exception e) {
            logger.error("Error saving user", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                transaction.commit();
                logger.info("User {} Removed", id);
            } else {
                logger.warn("User with id {} not found", id);
            }
        } catch (Exception e) {
            logger.error("Error removing user", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            list = session.createQuery("FROM User", User.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error getting all users", e);
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE jdbcconnect.users").executeUpdate();
            transaction.commit();
            logger.info("Table Cleaned");
        } catch (Exception e) {
            logger.error("Error cleaning table", e);
        }
    }
}

