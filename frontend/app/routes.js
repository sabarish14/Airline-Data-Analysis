var Flight = require('./models/flight');

function getOrig(res){
	Flight.distinct('ORIGIN',function(err, orig) {

			// if there is an error retrieving, send the error. nothing after res.send(err) will execute
			if (err)
				res.send(err)

			res.json(orig); // return all orig in JSON format
		});
};

function getDest(res){
	Flight.distinct('DEST',function(err, dest) {

			// if there is an error retrieving, send the error. nothing after res.send(err) will execute
			if (err)
				res.send(err)

			res.json(dest); // return all dest in JSON format
		});
};
function getCarrier(res){
	Flight.distinct('CARRIER',function(err, carrier) {

			// if there is an error retrieving, send the error. nothing after res.send(err) will execute
			if (err)
				res.send(err)

			res.json(carrier); // return all carrier in JSON format
		});
};
function predict(orig,dest,carrier,date,res){
			var month = parseInt( date[5]+date[6]);
			var day   = parseInt(date[8]+date[9]);
			var hour  = parseInt(date[11]+date[12]);
			console.log("day:"+day+"month:"+month);
			Flight.findOne({'ORIGIN':orig,'DEST':dest,'CARRIER':carrier,'MONTH':month,"DAY_OF_MONTH":day},{'prediction':1},function(err, prediction) {

                        // if there is an error retrieving, send the error. nothing after res.send(err) will execute
                        if (err)
                                res.send(err)
			console.log(prediction);
                        res.json(prediction); // return prediction in JSON format
                });
};


module.exports = function(app) {

	// api ---------------------------------------------------------------------
	// get orig
	app.get('/api/orig', function(req, res) 
	{

		// use mongoose to get all todos in the database
		getOrig(res);
	});
	app.get('/api/dest', function(req, res) 
	{

		// use mongoose to get all todos in the database
		getDest(res);
	});
	app.get('/api/carrier', function(req, res) 
	{

		// use mongoose to get all todos in the database
		getCarrier(res);
	});



	// create todo and send back all todos after creation
	app.post('/api/predict', function(req, res) {
		// get all fields from form, information comes from AJAX request from Angular
			var datetime = req.param('date');
			var origin = req.body.origin;
			var dest=req.body.dest;
			var carrier=req.body.carrier;
			console.log(origin);
			console.log(dest);
			console.log(carrier);
			console.log(datetime);
			predict(origin,dest,carrier,datetime,res);
		
	});
		
		
	// application -------------------------------------------------------------
	app.get('*', function(req, res) {
		res.sendfile('./public/index.html'); // load the single view file (angular will handle the page changes on the front-end)
	});
};
