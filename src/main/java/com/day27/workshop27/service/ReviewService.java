package com.day27.workshop27.service;

import java.io.StringReader;
import java.sql.Timestamp;
import java.time.Instant;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.day27.workshop27.model.Review;
import com.day27.workshop27.repository.GameRepo;
import com.day27.workshop27.repository.ReviewRepo;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private GameRepo gameRepo;

    // return jsonString 
    public String insertForm(MultiValueMap<String, String> form) {
        // query game name
        Integer gameId = Integer.parseInt(form.getFirst("game_id"));
        String gameName = gameRepo.getGameNameById(gameId);
        if (null == gameName)
            return null; // unsuccessful insert

        // create new Review object
        Review review = new Review(form);
        review.setGameName(gameName);
        System.out.println("ReviewService >>> Review Object created: " + review);
        
        // convert Review Obj to Json
        JsonObject reviewJson = review.toJson();

        // parse json to Document
        Document reviewDoc = Document.parse(reviewJson.toString());

        // pass Document to review Repo (inserting to Mongo)
        Document result = reviewRepo.insertReview(reviewDoc);

        return result.toJson().toString(); // successful insert
    }

    public Integer editReviewById(String edit, String reviewId) {
        System.out.println("Entered ReviewSvc");
        System.out.println("Edit String: " + edit);

        // read json string to json object
        JsonReader reader = Json.createReader(new StringReader(edit));
        JsonObject obj = reader.readObject();

        // add attribute
        JsonObject newObj = Json.createObjectBuilder()
            .add("comment", obj.getString("comment"))
            .add("rating", obj.getInt("rating"))
            .add("posted", Timestamp.from(Instant.now()).toString())
            .build();
        System.out.println("Json Object parsed: " + newObj);
        

        Document d = Document.parse(newObj.toString());

        // return results of update
        return reviewRepo.editReview(d, reviewId);

    }
    

}
