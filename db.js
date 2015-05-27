var mongoose = require('mongoose');
var ObjectId = require('mongoose').Types.ObjectId;
var SchemaObjectId = require('mongoose').Schema.Types.ObjectId;
var db = mongoose.connection
db.on('error', console.error)
db.once('open', function () {
})
mongoose.connect('mongodb://localhost/test')

// Schemas will need asker/responder ID's eventually
// Same with required fields
var responseSchema = new mongoose.Schema({
  text: String
});

var querySchema = new mongoose.Schema({
  question: String,
  answer: String,
  responseIds: [SchemaObjectId]
});

var Response = mongoose.model('Response', responseSchema);
var Query = mongoose.model('Query', querySchema);

// Begin removal callback
Query.remove({}, function (){
Response.remove({}, function (){

exports.newQuery = function (json, res) {
  try {
    debugger;
    //console.log(typeof json);
    //console.log(typeof json.toString());
    //console.log(typeof JSON.parse(json.toString()));
    var newQuery = new Query(JSON.parse(json.toString())['query']);
    newQuery.save(function (err) {
      Query.find(function (err,data){
        console.log(data[0]['question']);
        console.log(data[0]['answer']);
        res.end(newQuery['_id'].toString());
      })
    })
  } catch (e) {
    console.error(e) 
  }
}

// This function gets a "random" query to be responded to.
exports.getQuery = function (callback,res) {
  try {
    Query.findOne(callback);
  } catch (e) {
    console.error(e)
  }
}

exports.respondToQuery = function (jsonBuffer, res) {
  try {
    var json = JSON.parse(jsonBuffer.toString());
    var newResponse = new Response(JSON.parse(jsonBuffer.toString())['response']);
    newResponse.save(function (err) {
      Query.findOne({'_id':ObjectId(json['queryId'])}, function (err,query) {
        if (!err) {
          query.responseIds.push(ObjectId(newResponse['_id']));
          console.log(query);
          Response.findOne({'_id':query.responseIds[0]}, function (err, resp) {
          //Response.findOne({'_id':newResponse['_id']}, function (err, resp) {
          //Response.find({}, function (err, resp) {
            console.log("newR: " + newResponse['_id']);
            console.log("arrR: " + query.responseIds[0]);
            console.log(resp);
          });
        }
      });
      res.end(newResponse['_id'].toString());
    });
  } catch (e) {
    console.error(e);
  }
}

});
});
