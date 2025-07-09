package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.dto.RegisterUserDto;
import com.techelevator.tenmo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcUserDaoTest extends BaseDaoTest {
    protected static final User USER_1 = new User(1, "user1", "user1", "ROLE_USER");
    protected static final User USER_2 = new User(2, "user2", "user2", "ROLE_USER");
    private static final User USER_3 = new User(3, "user3", "user3", "ROLE_USER");

    private JdbcUserDao sut;

    @BeforeEach
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void getUserByUsername_given_null_returns_null_user() {
        User user = sut.getUserByUsername(null);
        assertNull(user, "getUserByUsername with null username did not return null user");
    }

    @Test
    public void getUserByUsername_given_invalid_username_returns_null_user() {
        User user = sut.getUserByUsername("invalid");
        assertNull(user, "getUserByUsername with invalid username did not return null user");
    }

    @Test
    public void getUserByUsername_given_valid_user_returns_correct_user() {
        User actualUser = sut.getUserByUsername(USER_1.getUsername());
        assertEquals(USER_1, actualUser,
                "getUserByUsername with valid username did not return correct user");
    }

    @Test
    public void getUserById_given_invalid_user_id_returns_null() {
        User user = sut.getUserById(-1);
        assertNull(user, "getUserById with invalid userId did not return null user");
    }

    @Test
    public void getUserById_given_valid_user_id_returns_user() {
        User actualUser = sut.getUserById(USER_1.getId());
        assertEquals(USER_1, actualUser, "getUserById with valid id did not return correct user");
    }

    @Test
    public void createUser_with_null_username() {
        try {
            sut.createUser(new User(null, USER_3.getHashedPassword(), "ROLE_USER"));
            fail("Expected createUser() with null username to throw DaoException, but it didn't throw any exception");
        } catch (DaoException e) {
            // expected
        } catch (Exception e) {
            fail("Expected createUser() with null username to throw DaoException, but threw a different exception");
        }
    }

    @Test
    public void createUser_with_existing_username() {
        try {
            sut.createUser(new User(USER_1.getUsername(), USER_3.getHashedPassword(), "ROLE_USER"));
            fail("Expected createUser() with existing username to throw DaoException, but it didn't throw any exception");
        } catch (DaoException e) {
            // expected condition
        } catch (Exception e) {
            fail("Expected createUser() with existing username to throw DaoException, but threw a different exception");
        }
    }

    @Test
    public void createUser_with_null_password() {
        try {
            sut.createUser(new User(USER_3.getUsername(), null, "ROLE_USER"));
            fail("Expected createUser() with null password to throw DaoException, but it didn't throw any exception");
        } catch (DaoException e) {
            // expected condition
        } catch (Exception e) {
            fail("Expected createUser() with null password to throw DaoException, but threw a different exception");
        }
    }

    @Test
    public void createUser_creates_a_user() {
        User newUser = new User("new", "user", "ROLE_USER");

        User createdUser = sut.createUser(newUser);
        assertNotNull(createdUser, "Call to create should return non-null user");

        User retrievedUser = sut.getUserById(createdUser.getId());
        assertNotNull(retrievedUser, "Call to getUserById after call to create should return non-null user");

        newUser.setId(retrievedUser.getId());
        retrievedUser.setHashedPassword(newUser.getHashedPassword()); // reset password back to unhashed password for testing
        assertEquals(newUser, retrievedUser);
    }
}
