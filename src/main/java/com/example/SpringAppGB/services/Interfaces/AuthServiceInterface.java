package com.example.SpringAppGB.services.Interfaces;

import com.example.SpringAppGB.model.DTO.JwtRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthServiceInterface {

    ResponseEntity<?> createAuthToken(JwtRequest user, HttpServletResponse response);
}
