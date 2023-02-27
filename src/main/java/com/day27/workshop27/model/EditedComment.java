package com.day27.workshop27.model;

import java.sql.Timestamp;

import org.bson.Document;

public class EditedComment {
    private String comment;
    private Integer rating;
    private Timestamp posted;

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public Timestamp getPosted() {
        return posted;
    }
    public void setPosted(Timestamp posted) {
        this.posted = posted;
    }

    @Override
    public String toString() {
        return "EditedComment [comment=" + comment + ", rating=" + rating + ", posted=" + posted + "]";
    }
    
    public static EditedComment create(Document e) {
        EditedComment ec = new EditedComment();
        ec.setComment(e.getString("comment"));
        ec.setRating(e.getInteger("rating"));
        ec.setPosted(Timestamp.valueOf(e.getString("posted")));
        return ec;
    }
    
}
