package com.day27.workshop27.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.day27.workshop27.service.ReviewService;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path="/review")
public class ReviewRestController {
    
    private final String MESSAGE_BAD_REQUEST = """
                {"error": "Invalid ID",
                "error type": "Bad Request"}
                """;

    @Autowired
    private ReviewService reviewSvc;

    // a) 
    @PostMapping(
        consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
        produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postReview(@RequestBody MultiValueMap<String, String> form) {
        // inserting to Mongo through Svc (result json string)
        String jsonResult = reviewSvc.insertForm(form);
        
        if (null != jsonResult) {
            return new ResponseEntity<>(jsonResult, HttpStatus.OK);
        } else {
            JsonObject message = Json.createObjectBuilder()
                .add("error", "Invalid Game ID")
                .add("type", "Bad request")
                .build();
            return new ResponseEntity<>(
                message.toString(), 
                HttpStatus.BAD_REQUEST);
        }
    }

    // b)
    @PutMapping(path="/{reviewId}",
        consumes=MediaType.APPLICATION_JSON_VALUE,
        produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateReview(
        @PathVariable String reviewId, 
        @RequestBody String editedReview) {

        Integer result = reviewSvc.editReviewById(editedReview, reviewId);
        if (result == 1) {
            return new ResponseEntity<>("Updated review %s".formatted(reviewId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Id not found", HttpStatus.BAD_REQUEST);
        }
    }

    // c) get request to retrieve lastest info for review by ID
    @GetMapping(path="/{reviewId}", produces=MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<String> getLatestReviewById(@PathVariable String reviewId) throws ParseException {
        // query service for json

        String result = reviewSvc.getLatestCommentById(reviewId);

        if (null != result) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
        } else {
            // json formatted string
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(MESSAGE_BAD_REQUEST);
        }
    }

    // d) get all details of comment with all edits
    @GetMapping(path="/{_id}/history", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> method(@PathVariable String _id) {
        System.out.println("Received String _id: " + _id);
        
        String result = reviewSvc.getHistoryById(_id);
        if (null != result) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
        } else {

            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(MESSAGE_BAD_REQUEST);
        }
    }


}
