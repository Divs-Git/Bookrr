package com.divyansh.bookrr.service.interfaces;

import com.divyansh.bookrr.dto.LoginRequest;
import com.divyansh.bookrr.dto.Response;
import com.divyansh.bookrr.dto.UserDto;
import com.divyansh.bookrr.entity.User;

public interface IUserService {
    Response registerUser(User user);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
