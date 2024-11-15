package com.example.SpringAppGB.services.Interfaces;

import com.example.SpringAppGB.model.User;

import java.util.List;

public interface UserServiceInterface {
    List<User> findUserByUserNameOrByEmail(String findString);
}
