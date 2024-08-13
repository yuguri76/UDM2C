package com.example.livealone.global.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoadBallancerController {

    @GetMapping("/status")
    public ResponseEntity<Void> getHealtyCheck(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
