const mysql = require('mysql');
const express = require('express');
var app = express();
const bodyparser = require('body-parser');

var session = require('express-session');
var bodyParser = require('body-parser');
var path = require('path');

app.use(bodyparser.json());

var connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'testing'
});

connection.connect((err) => {
    if (!err)
        console.log('DB connection succeded.');
    else
        console.log('DB connection failed \n Error : ' + JSON.stringify(err, undefined, 2));
});




app.get('/jquery-3.4.1.min.js',(req,res) => {
  res.sendFile(path.join(__dirname + '/jquery-3.4.1.min.js'));
})

app.get('/',(req,res) => {
  res.sendFile(path.join(__dirname + '/home.html'));

})
app.get('/world',(req,res) => {
  res.sendFile(path.join(__dirname + '/world.html'));

})
app.get('/countr',(req,res) => {
  res.sendFile(path.join(__dirname + '/countries.html'));

})
app.use(express.urlencoded());

// Parse JSON bodies (as sent by API clients)
app.use(express.json());
app.use(bodyparser.urlencoded({ extended: false }));

var resultados;
var autocomplete_results;

app.post('/api', function(request, response,next){
  var condition = request.body.condition; //ανάκτηση σταλμένων δεδομένων
  var query = "SELECT countries.COUNTRY,COUNT(*), conditions.COND FROM countries,conditions WHERE countries.UID = conditions.UID AND conditions.COND= ? GROUP BY countries.COUNTRY ORDER BY COUNT(*) DESC";

  // SELECT countries.COUNTRY,COUNT(*), conditions.COND FROM countries,conditions
  // WHERE countries.UID = conditions.UID AND conditions.COND= ?
  // GROUP BY countries.COUNTRY
  // ORDER BY COUNT(*) DESC;

  var values = [
      [condition],
  ];
  //εκτέλεση του query
  connection.query(query,[values] ,function (error, results, fields) {        
    if (error) throw err;
    resultados = results;
    response.redirect('/countr');
  });

  
});


app.get('/api', function(req, res, next) {
  res.send(resultados);
});

app.listen(3001, () => console.log('Express server is runnig at port no : 3001'));
