var http = require('http')
var url = require('url')
var db = require('./db.js')


var server = http.createServer(function (req, res) {
  var u = url.parse(req.url, true);
  var result;
  if (/^\/file/.test(u.pathname)) {
    result = getFile(u.query["name"],res)
  } else if (/^\/new/.test(u.pathname)) {
    req.on('data',function (data) {db.newQuery(data, res)})
  } else if (/^\/get/.test(u.pathname)) {
    // This function may need to be moved to db.js
    db.getQuery( function (err,data) {
      console.log("Gotten data:" + data);
      res.end(data.toString());
    })
  } else if (/^\/respond/.test(u.pathname)) {
    result = req.on('data', function (data) {
        db.respondToQuery(data, res)
    })
  } else if (/^\/image/.test(u.pathname)) {
    result = receiveImage(req, res)
  }
});

var receiveImage = function (req, res) {
  var fs = require('fs')
  //fs.createWriteStream('outfile').pipe(req)
  
  //req.on('data', function (data) {
  //  fs.createWriteStream('outfile').pipe(data)
  //[bracket] )
  
  res.writeHead(200)
  res.end("Transmission successful.")
}


var getFile = function (fileName,res) {
  console.log(fileName);
  try {
    var fs = require('fs');
    fs.readFile(fileName, function (err,data) {
      if (!err) {
        res.writeHead(200)
        res.write(data)
      } else {
        console.log(404)
        res.writeHead(404)
      }
      res.end()
    })
  } catch (e) {}
}

server.listen(8000)
