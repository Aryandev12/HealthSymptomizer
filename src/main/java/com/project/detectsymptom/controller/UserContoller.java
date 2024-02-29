package com.project.detectsymptom.controller;


import com.project.detectsymptom.model.UserModel;
import com.project.detectsymptom.service.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserContoller {

    @Autowired
    Json json;

    @PostMapping("/user")
    public ResponseEntity<?> User(@RequestBody UserModel userModel){

        if(userModel.getUsername() == null || userModel.getAge() == null || userModel.getGender() == null || userModel.getSymptoms()== null){
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        String username = userModel.getUsername();
        String gender = userModel.getGender();
        String age = userModel.getAge();
        List<String> symptoms = userModel.getSymptoms();
        System.out.println(userModel.toString());
        json = new Json(symptoms);
        System.out.println(json.test());




        UserModel user = new UserModel(username,gender,age,symptoms);



        return new ResponseEntity<>(user, HttpStatus.OK);


    }

}