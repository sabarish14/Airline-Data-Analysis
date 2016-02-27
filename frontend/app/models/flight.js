var mongoose = require('mongoose');

module.exports = mongoose.model('Flight', {
	ARR_DELAY  : {type : String, default: ''},
	CARRIER: {type : String, default: ''},
	CRS_ARR_TIME: {type : String, default: ''},
	DAY_OF_MONTH: {type : Number, default: ''},
	DEST: {type : String, default: ''},
	MONTH: {type : Number, default: ''},
	ORIGIN: {type : String, default: ''},
	prediction: {type : String, default: ''}

},'prediction'
);
