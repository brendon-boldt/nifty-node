var http = require('http')
var url = require('url')
var db = require('./db.js')


var server = http.createServer(function (req, res) {
  var u = url.parse(req.url, true)
  var result
  if (/^\/file/.test(u.pathname))
    result = getFile(u.query["name"],res)
  if (/^\/json/.test(u.pathname)) {
    result = req.on('data',db.parseQuery)
    res.end()
  }
  if (/^\/image/.test(u.pathname))
    result = receiveImage(req, res)
});

var receiveImage = function (req, res) {
  var fs = require('fs')
  //fs.createWriteStream('outfile').pipe(req)
  /*
  req.on('data', function (data) {
    fs.createWriteStream('outfile').pipe(data)
  })
  */
  res.writeHead(200)
  res.end("Transmission successful.")
}


var getFile = function (fileName,res) {
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
}

server.listen(8000)
