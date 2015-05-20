var mongoose = require('mongoose')
var ObjectId = require('mongoose').Types.ObjectId
var db = mongoose.connection
db.on('error', console.error)
db.once('open', function () {
})
mongoose.connect('mongodb://localhost/test')

var querySchema = new mongoose.Schema({
  question: String,
  answer: String,
  responses:[
    {text: String}
  ]
})

var Query = mongoose.model('Query', querySchema);

// Begin removal callback
Query.remove({}, function (){

exports.newQuery = function (json, res) {
  try {
    var newQuery = new Query(JSON.parse(json.toString()))
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

exports.getQuery = function (callback,res) {
  try {
    Query.findOne(callback)
  } catch (e) {
    console.error(e)
  }
}

exports.respondToQuery = function (queryId, json) {
  try {
    console.log("ObjectId: " + ObjectId(queryId))
    Query.findOne({'_id':ObjectId(queryId)}, function (err,data) {
      data.responses.push(JSON.parse(json).text)
      console.log(data)
    })
  } catch (e) {
    console.error(e)
  }
}

});
