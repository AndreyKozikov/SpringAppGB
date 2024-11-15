package com.example.SpringAppGB.controllers.api.Interfaces;

import com.example.SpringAppGB.model.DTO.JwtRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthInterface {

    ResponseEntity<?> login (@RequestBody JwtRequest userDTO, HttpServletResponse response);
}
