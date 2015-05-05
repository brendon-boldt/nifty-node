var m = require('mongoose')
var db = m.connection
db.on('error', console.error)
db.once('open', function () {
  Query.remove({}, function (){});
})
m.connect('mongodb://localhost/test')

var qSchema = new m.Schema({
  question: String,
  response: String
})

var Query = m.model('Query', qSchema);

exports.parseQuery = function (json) {
  try {
    var q = new Query(JSON.parse(json.toString()))
  q.save(function (err) {
    Query.find(function (err,data){
      console.log(data[0]['question'])
      console.log(data[0]['response'])
    })
  })
    } catch (e) {
      console.error(e) 
    }
}
