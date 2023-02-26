package com.day27.workshop27.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static com.day27.workshop27.Constants.*;

import java.util.List;

@Repository
public class GameRepo {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    // query to get game name by id
    // db.game.find({
    //     gid: 1
    // });
    public String getGameNameById(Integer gameId) {
        Criteria c = Criteria.where("gid").is(gameId);
        Query query = Query.query(c);
        List<Document> result = mongoTemplate.find(query, Document.class, COLLECTION_GAME);
        System.out.println("List of Documents: " + result);
        if (result.isEmpty()) 
            return null;

        return result.get(0).getString("name");
    }


}
