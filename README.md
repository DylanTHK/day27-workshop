# day27-workshop
Workshop27 Boardgame with MongoDB

## Learning Points
1. Convert String to TimeStamp
- https://www.baeldung.com/java-string-to-timestamp
```
<!-- extract time from document as string -->
String timestampAsString = "Nov 12, 2018 13:02:56.12345678";
Timestamp timestamp = Timestamp.valueOf(timestampAsString)
```
2. get list from Document (bson)
```
List<Document> dList = d.getList(<attribute_in_doc>, Document.class);
```
3. Inserting document into Mongo
```
Document result = mongoTemplate.insert(<document>, <collection_name>);
```
4. Getting current timestamp
```
Timestamp currentTime = Timestamp.from(Instant.now());
```
5. comparing timestamps
```
<!-- return 0  if dates are equal-->
<!-- returns > 0 (more than 0) if date1 is after date2 -->
<!-- returns < 0 (less than 0) if date1 is before date2 -->
timestamp1.compareTo(timestamp2)
```
6. useful packages to import
```
<!-- store dates as timestamp -->
import java.sql.Timestamp;
import java.time.Instant;
```

## Flow
1. Controller
- Requests from postman
- sends information to service methods(form, pathvariables, requestparam)

2. Service (Business Logic)
- Service converts Documents -> Review and Edited Comment Objects
- unpack data to POJO
- Manipulate data to suit requirements (Edit data to show)
- returns json / string to controller

3. Repo (Query Mongo)
- Repo query from Mongo (Documents)
- returns document to service for processing

## Queries in MongoDB
### a1) insert review w comments with given json input
```
===== Mongo =====
db.review.insert({
    user: "user1",
    rating: 1,
    comment: "insert comment",
    id: 1,
    posted: "date",
    name: "Die Macher" 
});
===== Java =====
<!-- Convert multivaluemap -> POJO -> Json -> Document (doc) -->
<!-- input doc to mongoTemplate.insert() -->
Document result = mongoTemplate.insert(doc, COLLECTION_REVIEW);
```

### b) add editing of EXISTING review content
```
===== Mongo =====
db.review.updateOne(
    { _id: ObjectId("63fac578c701042afa618e62")},
    {
        $push: {edited: "< edit content >"}
    }
);
===== Java =====
ObjectId objId = new ObjectId(id);
Query query = Query.query(Criteria.where("_id").is(objId));
System.out.println("ReviewRepo >>> Query passed: " + query);
Update updateOps = new Update().push("edited").each(edit);
<!-- UpdateResult return results of update (1 = matched, 0 = not found) -->
UpdateResult updateResult = mongoTemplate.updateFirst(query, updateOps, Document.class, COLLECTION_REVIEW);
```
### c & d) 
```
===== Mongo =====
db.review.find({
    _id: ObjectId("63fac578c701042afa618e62")
});
===== Java =====
ObjectId mongoId = new ObjectId(id);
Document review = mongoTemplate.findById(mongoId, Document.class, COLLECTION_REVIEW);
<!-- finding of entire document and Json/Document manipulation in Java -->
```
### d) 
```
<!-- Same as c, with additional Document / Json manipulation -->
```
