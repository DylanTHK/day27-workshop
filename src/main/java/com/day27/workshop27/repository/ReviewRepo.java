package com.day27.workshop27.repository;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import static com.day27.workshop27.Constants.*;

@Repository
public class ReviewRepo {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    // db.review.insert({
    //     user: "user1",
    //     rating: 1,
    //     comment: "insert comment",
    //     id: 1,
    //     posted: "date",
    //     name: "Die Macher" 
    // });
    public Document insertReview(Document doc) {
        Document result = mongoTemplate.insert(doc, COLLECTION_REVIEW);
        return result;
    }

    // db.review.updateOne(
    // { _id: ObjectId("63fac578c701042afa618e62")},
    // {
    //     $push: {edited: "< edit content >"}
    // });
    public Integer editReview(Document edit, String id) {
        System.out.println("Entered editReview");
        // check if id exists
        ObjectId objId; 
        try {
            objId = new ObjectId(id);
            System.out.println("Obj ID: " + objId);
        } catch(IllegalArgumentException e) {
            e.getMessage();
            return 0;
        }

        // find document by id
        Query query = Query.query(Criteria.where("_id").is(objId));
        System.out.println("ReviewRepo >>> Query passed: " + query);
        Update updateOps = new Update().push("edited").each(edit);

        UpdateResult updateResult = mongoTemplate.updateFirst(query, updateOps, Document.class, COLLECTION_REVIEW);
        System.out.println("ReviewRepo >>> Number of matched updates: " + updateResult.getMatchedCount());
        
        System.out.println("ReviewRepo >>> Result of update: " + updateResult);

        // return results of update (1 = matched, 0 = not found)
        return (int) updateResult.getMatchedCount();
    }

    // db.review.find({
    //     _id: ObjectId("63fac578c701042afa618e62")
    // });
    public Document getReviewById(String id) {
        ObjectId mongoId = new ObjectId(id);
        Document review = mongoTemplate.findById(mongoId, Document.class, COLLECTION_REVIEW);
        return review;
    }
}
