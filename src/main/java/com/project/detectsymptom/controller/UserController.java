package com.project.detectsymptom.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.detectsymptom.service.GeminiService;
import com.project.detectsymptom.service.JSON_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    @Autowired
    private JSON_Service jsonService; // Inject the service

//    @PostMapping("/user")
//    public ResponseEntity<?> User(@RequestBody UserModel userModel) {
//
//        if (userModel.getUsername() == null || userModel.getAge() == null || userModel.getGender() == null || userModel.getSymptoms() == null) {
//            return ResponseEntity.badRequest().body("All fields are required.");
//        }
//
//        String username = userModel.getUsername();
//        String gender = userModel.getGender();
//        String age = userModel.getAge();
//        List<String> symptoms = userModel.getSymptoms();
//
//        Map<String, Double> analyzer;
//        try {
//            analyzer = userSymptomAnalyzerService.analyzeUserSymptoms(symptoms.toArray(new String[0])); // Use the injected service
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        return new ResponseEntity<>(analyzer, HttpStatus.OK);
//    }

    /*
  GEMINI_API_KEY = AIzaSyDx2Vq_FGhvCjpVfa9WTVQJSq62YTMs3-k
  PROJECT_ID = intense-climber-415609
**/
    @GetMapping("/gemini")
    public Object Gemini(){

        //String prompt = "Suggest all the probable one/two words symptoms for migraine in an array form like [symptoms] , don't write any unnecessary text , just return the array of symptoms";
        String prompt = "Suggest all the probable diseases based on the symptoms provided. Return the results in json form having 3 fixed keys , disease , symptoms and precautions(one single string). Make sure NOT to return in markdown format but as raw string of array of jsons."+
                "Also, write the other related symptoms(one/two words only) too since provided symptoms may be very few. The result should be of type json array and must follow the given format. It should not deviate from the instructions as the returned response is known to be json array and will be further used in the project, wrong format can crash the project. Do not inckude any other words or phrases just the json array." +
                "The symptoms for the disease to be predicted are ['headache' , 'tension' ,'wrinkles'] , make sure to include these symptoms too in the ouput";
        GeminiService geminiService = new GeminiService();
        CompletableFuture<String> responseFuture = geminiService.generateContent(prompt);

        // Wait for the response
        String response = responseFuture.join();

        // Proceed with further processing using the response
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            // Assuming there's only one candidate in the response
            JsonNode contentNode = jsonNode.at("/candidates/0/content/parts/0/text");
            System.out.println(contentNode);

            return contentNode.asText();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }


    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestBody List<String> symptoms) {

        Map<Double , String> analyzer;
        try {
            analyzer = jsonService.analyzeUserSymptoms(symptoms); // Use the injected service
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(analyzer, HttpStatus.OK);
    }


}

