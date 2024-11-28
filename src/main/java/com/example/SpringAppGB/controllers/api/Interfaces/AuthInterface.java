package com.example.SpringAppGB.controllers.api.Interfaces;

import com.example.SpringAppGB.model.DTO.JwtRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthInterface {

    ResponseEntity<?> login (@Valid @RequestBody JwtRequest userDTO, BindingResult bindingResult,
                             HttpServletResponse response);

}
