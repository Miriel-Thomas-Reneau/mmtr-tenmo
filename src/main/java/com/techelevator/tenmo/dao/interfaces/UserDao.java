package com.techelevator.tenmo.dao.interfaces;

import com.techelevator.tenmo.model.dto.RegisterUserDto;
import com.techelevator.tenmo.model.User;

public interface UserDao {

    User getUserById(Integer id);

    User getUserByUsername(String username);

    User createUser(User user);
        //if user.role = USER, call createTenmoAccount
}
