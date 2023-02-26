use review;

//db.game.find({
//    gid: 1   
//});

// a) add a new collection review
db.review.insert({
    user: "user1",
    rating: 1,
    comment: "insert comment",
    id: 1,
    posted: "date",
    name: "Die Macher" 
})

db.review.find();

// b) update a review that was posted




// c) retrieve and display most recent comments and ratings

// d) returns a comment and all edits (if any)

