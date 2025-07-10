package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.interfaces.TenmoAccountDao;
import com.techelevator.tenmo.dao.interfaces.UsdAccountDao;
import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.UsdAccount;
import jakarta.validation.Valid;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.dto.LoginResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.techelevator.tenmo.dao.interfaces.UserDao;
import com.techelevator.tenmo.model.dto.LoginDto;
import com.techelevator.tenmo.model.dto.RegisterUserDto;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

/**
 * Controller to authenticate users.
 */
@RestController
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserDao userDao;
    private final TenmoAccountDao tenmoAccountDao;
    private final UsdAccountDao usdAccountDao;

    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                                    UserDao userDao, TenmoAccountDao tenmoAccountDao, UsdAccountDao usdAccountDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.tenmoAccountDao = tenmoAccountDao;
        this.usdAccountDao = usdAccountDao;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public LoginResponseDto login(@Valid @RequestBody LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            if(authentication.isAuthenticated()){
                String jwt = tokenProvider.createToken(authentication, false);
                User user = userDao.getUserByUsername(loginDto.getUsername());
                return new LoginResponseDto(jwt, user);
            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public User register(@Valid @RequestBody RegisterUserDto newUser) {

        if(!newUser.isPasswordsMatch()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password and confirm password do not match");
        }

        try {
            if (userDao.getUserByUsername(newUser.getUsername()) != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists.");
            }

            User user = userDao.createUser(new User(newUser.getUsername(), newUser.getPassword(), newUser.getRole()));
            if (!user.getRole().equals("ROLE_ADMIN")) {
                TenmoAccount newTenmoAccount = tenmoAccountDao.createTenmoAccount(user.getId());
                UsdAccount newUsdAccount = new UsdAccount();
                newUsdAccount.setTenmoAccountId(newTenmoAccount.getTeAccountId());
                newUsdAccount.setUsdBucksBalance(BigDecimal.ZERO);
                newUsdAccount.setUser_id(user.getId());
                usdAccountDao.createUsdAccount(newUsdAccount);
            }
            return user;
        }
        catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

}

