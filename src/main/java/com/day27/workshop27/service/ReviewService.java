package com.day27.workshop27.service;

import java.io.StringReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.day27.workshop27.model.EditedComment;
import com.day27.workshop27.model.Review;
import com.day27.workshop27.repository.GameRepo;
import com.day27.workshop27.repository.ReviewRepo;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
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
        JsonObject reviewJson = review.toJson().build();

        // parse json to Document
        Document reviewDoc = Document.parse(reviewJson.toString());

        // pass Document to review Repo (inserting to Mongo)
        Document result = reviewRepo.insertReview(reviewDoc);

        return result.toJson().toString(); // successful insert
    }

    public Integer editReviewById(String edit, String reviewId) {
        // read json string to json object
        JsonReader reader = Json.createReader(new StringReader(edit));
        JsonObject obj = reader.readObject();
        // create json to add to edit array
        JsonObject newObj = Json.createObjectBuilder()
            .add("comment", obj.getString("comment"))
            .add("rating", obj.getInt("rating"))
            .add("posted", Timestamp.from(Instant.now()).toString())
            .build();
        System.out.println("Json Object parsed: " + newObj);
        
        Document d = Document.parse(newObj.toString());

        // Query Mongo
        // return results of update (1 = matched, 0 = not found)
        return reviewRepo.editReview(d, reviewId);
    }
    
    // c) method to query review by id
    public String getLatestCommentById(String id) throws ParseException {
        // extract relevant information from Repo
        System.out.println("ReviewSvc >>> ID received: " + id); //REMOVE
        Document d = reviewRepo.getReviewById(id);
        if (null == d) 
            return null;
        System.out.println("ReviewSvc >>> Document received: " + d); //REMOVE
        // update information to Review Object
        Review r = Review.create(d);
        // find the latest edit
        Timestamp latestTime = r.getPostedDate();
        List<EditedComment> listEdited = r.getEdited();
        // if edited, update latest from edit
        if (r.getIsEdited()) {
            for (EditedComment ec : listEdited) {
                Timestamp newTime = ec.getPosted();
                // returns a value "greater than 0" if date1 is after date2
                if (newTime.compareTo(latestTime) > 0) {
                    latestTime = newTime;
                    r.setPostedDate(newTime);
                    r.setRating(ec.getRating());
                    r.setComment(ec.getComment());
                }
            }
        }

        // format json information
        JsonObject reviewJson = r.toJson()
            .add("edited", r.getIsEdited())
            .add("timestamp", Timestamp.from(Instant.now()).toString())
            .build();
        System.out.println("\nNew Json Object (AFTER): " + reviewJson);

        // send back as String
        return reviewJson.toString();
    } 

    public String getHistoryById(String id) {
        // get Document from Mongo
        Document doc = reviewRepo.getReviewById(id);
        // to catch invalid requests
        if (null == doc) {
            return null;
        }
        // to catch invalid parse e
        Review review;
        try {
            review = Review.create(doc);
        } catch (ParseException e) {
            review = null;
            e.getMessage();
        }
        // building edited Json Array
        JsonArrayBuilder editedJsonArray = Json.createArrayBuilder();
        List<Document> editDocList = doc.getList("edited", Document.class);
        System.out.println("\nEdited Document List: "+ editDocList);
        for (Document d : editDocList) {
            JsonObject eObj = Json.createObjectBuilder()
                .add("comment", d.getString("comment"))
                .add("rating", d.getInteger("rating"))
                .add("posted", d.getString("posted"))
                .build();
            editedJsonArray.add(eObj);
        }
        // Object Builder
        JsonObject reviewJson = review.toJson()
            .add("edited", editedJsonArray.build())
            .build();
        System.out.println("\n\nJson Review: " + reviewJson);
        return reviewJson.toString();
    }
}
