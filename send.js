var fs = require('fs')
var http = require('http')
var request = require('request')

/*
var req = http.request({
  hostname: 'http://localhost:8000',
  path: '/image',
  method: 'POST'
});
*/
fs.createReadStream('k.jpg').pipe(request.post('http://localhost:8000/image'))
