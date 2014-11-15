var API_URL = "http://107.170.81.127:8777/UFVQuestAPI";
var USER;

function getFormObj(form) {
    var formObj = {};
    var inputs = $(form).serializeArray();
    $.each(inputs, function (i, input) {
        formObj[input.name] = input.value;
    });
    return formObj;
}

Number.prototype.toRad = function() {
   return this * Math.PI / 180;
}

function getDistanceBetween(latlng1, latlng2) {
	
	var lat2 = latlng2.lat(); 
	var lon2 = latlng2.lng(); 
	var lat1 = latlng1.lat(); 
	var lon1 = latlng1.lng(); 

	var R = 6371; // km 
	var x1 = lat2-lat1;
	var dLat = x1.toRad();  
	var x2 = lon2-lon1;
	var dLon = x2.toRad();  
	var a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
					Math.cos(lat1.toRad()) * Math.cos(lat2.toRad()) * 
					Math.sin(dLon/2) * Math.sin(dLon/2);  
	var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	var d = R * c * 1000; 
	
	return d;
}