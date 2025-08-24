package com.divyansh.bookrr.service.impl;

import com.divyansh.bookrr.dto.LoginRequest;
import com.divyansh.bookrr.dto.Response;
import com.divyansh.bookrr.dto.UserDto;
import com.divyansh.bookrr.entity.User;
import com.divyansh.bookrr.exception.GlobalException;
import com.divyansh.bookrr.repository.UserRepository;
import com.divyansh.bookrr.service.interfaces.IUserService;
import com.divyansh.bookrr.utils.JWTUtils;
import com.divyansh.bookrr.utils.Utils;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils  jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response registerUser(User user) {
        Response response = new Response();

        try {
            if(user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }

            if(userRepository.existsByEmail(user.getEmail())) {
                throw new GlobalException("Email Already Exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            UserDto userDto = Utils.mapUserEntityToUserDto(savedUser);

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setUser(userDto);
        } catch (GlobalException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        Response response = new Response();

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new GlobalException("User Not Found"));
            var token =  jwtUtils.generateToken(user);

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 days");
        } catch (GlobalException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try {
            List<User> users = userRepository.findAll();
            List<UserDto> userDtoList = Utils.mapUserListEntityToUserListDTO(users);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setUserList(userDtoList);

        }  catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User Not Found"));
            UserDto userDto = Utils.mapUserEntityToUserDtoPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setUser(userDto);
        } catch (GlobalException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }  catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();

        try {
            userRepository.deleteById(userId);
            response.setStatusCode(200);
            response.setMessage("Success");

        }  catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try {
            User user =  userRepository.findById(userId).orElseThrow(() -> new GlobalException("User Not Found"));
            UserDto userDto = Utils.mapUserEntityToUserDto(user);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setUser(userDto);
        } catch (GlobalException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }   catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try {
            User user =  userRepository.findByEmail(email).orElseThrow(() -> new GlobalException("User Not Found"));
            UserDto userDto = Utils.mapUserEntityToUserDto(user);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setUser(userDto);
        } catch (GlobalException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }   catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " + e.getMessage());
        }

        return response;
    }
}
