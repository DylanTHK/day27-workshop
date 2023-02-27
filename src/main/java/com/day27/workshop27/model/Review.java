package com.day27.workshop27.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.util.MultiValueMap;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

public class Review {
    // form inputs
    private String user;
    private Integer rating;
    private String comment;
    private Integer id;
    private Timestamp postedDate; // generate
    private String gameName; // query from GameRepo
    private Boolean isEdited = false;
    private List<EditedComment> edited;
    
    public Review() {}

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
    public Boolean getIsEdited() {
        return isEdited;
    }
    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }
    public List<EditedComment> getEdited() {
        return edited;
    }
    public void setEdited(List<EditedComment> edited) {
        this.edited = edited;
    }

    @Override
    public String toString() {
        return "Review [user=" + user + ", rating=" + rating + ", comment=" + comment + ", id=" + id + ", postedDate="
                + postedDate + ", gameName=" + gameName + ", isEdited=" + isEdited + ", edited=" + edited + "]";
    }

    // method to convert POJO to Json for a)
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder jObj = Json.createObjectBuilder()
            .add("user", this.getUser())
            .add("rating", this.getRating())
            .add("comment", this.getComment())
            .add("ID", this.getId())
            .add("posted", this.getPostedDate().toString())
            .add("name", this.getGameName());
        return jObj;
    }

    // user, rating, comment, id, postedDate, 
    // gameName, isEdited, List<EditedComment> edited;
    public static Review create(Document d) throws ParseException {
        Review r = new Review();
        r.setUser(d.getString("user"));
        r.setRating(d.getInteger("rating"));
        r.setComment(d.getString("comment"));
        r.setId(d.getInteger("ID"));
        r.setPostedDate(Timestamp.valueOf(d.getString("posted")));
        // r.setPostedDate(convertStringtoTimeStamp(d.getString("posted")));
        r.setGameName(d.getString("name"));
        // creating list from list of documents
        List<EditedComment> listEdited = new LinkedList<>();
        List<Document> dList = d.getList("edited", Document.class);

        if (null != dList) {
            for (Document doc : dList) {
                // System.out.println("List of Documents: " + doc); //REMOVE
                listEdited.add(EditedComment.create(doc));
            }
            r.setIsEdited(true);
            r.setEdited(listEdited);
        }
        System.out.println("\n\nCreated Review: " + r);
        return r;
    }

}
