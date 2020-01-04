var Client = require('mongodb').MongoClient;
var db;
Client.connect('mongodb://localhost:27017/firstDB', function(error, database){
    if(error) {
        console.log(error);
    } else {
        
        db = database.db('firstDB')
        console.log("connected:"+db.databaseName);
        // var michael = {name:'AA', number: 2};
        // db.collection('contact').insertMany([{name:'AA', number: 2},{name:'AA', number: 2},{name:'AA', number: 2}]);
        db.collection('book').insertMany([{ name: 'a'},{ name: 'a'},{ name: 'a'}]);

        database.close();
    }
});