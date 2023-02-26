package com.day27.workshop27.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import org.springframework.util.MultiValueMap;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Review {
    // form inputs
    private String user;
    private Integer rating;
    private String comment;
    private Integer id;

    private Timestamp postedDate; // generate
    private String gameName; // query from GameRepo

    public Review(MultiValueMap<String, String> form) {
        this.user = form.getFirst("name");
        this.rating = Integer.parseInt(form.getFirst("rating"));
        this.comment = form.getFirst("comment");
        this.id = Integer.parseInt(form.getFirst("game_id"));
        this.postedDate = Timestamp.from(Instant.now());
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Timestamp getPostedDate() {
        return postedDate;
    }
    public void setPostedDate(Timestamp postedDate) {
        this.postedDate = postedDate;
    }
    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    @Override
    public String toString() {
        return "Review [user=" + user + ", rating=" + rating + ", comment=" + comment + ", id=" + id + ", postedDate="
                + postedDate + ", gameName=" + gameName + "]";
    }

    public JsonObject toJson() {
        JsonObject jObj = Json.createObjectBuilder()
            .add("user", this.getUser())
            .add("rating", this.getRating())
            .add("comment", this.getComment())
            .add("ID", this.getId())
            .add("posted", this.getPostedDate().toString())
            .add("name", this.getGameName())
            .build();
        return jObj;
    }

}
