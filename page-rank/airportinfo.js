var getAirportsInfo = function() {
    var airportInfo = {}
    , n = 3; // last iteration of PageRank
    
    var airportNodes = db["Graph_"+n].find()
    , flightInfo, id, airportNode;

    while (airportNodes.hasNext()) {
        airportNode = airportNodes.next();
        id = airportNode["_id"];
        print (JSON.stringify(airportNode.value))
        airportInfo[id] = {"pg" : airportNode.value.pg};
        flightInfo = db.flights_original.findOne({"origAirportId" : parseInt(id)});

        airportInfo[id]["airportCode"] = flightInfo["origAirport"];
        airportInfo[id]["airportState"] = flightInfo["origState"];
        airportInfo[id]["airportStateId"] = flightInfo["origStateId"];
        airportInfo[id]["airportCity"] = flightInfo["origCity"];
    }

    return airportInfo;
};

var info = getAirportsInfo();
//print(JSON.stringify(info));