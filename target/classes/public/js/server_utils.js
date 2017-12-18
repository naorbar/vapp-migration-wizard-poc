// Author: Naor Bar
// Date: July 2017
// Description: This js file includes all server methods; i.e. get/post/update/delete:

// Note: Make the calls async, to make it smooth... 

// Get available steps metadata from the server:
// Note: this call should be synchronous!
function ajaxGetStepsMetadata() {
	return $.ajax({
		url: "steps/metadata",
		dataType: 'json',
		async: false,
		success: function(data) {
			stepsList = data;
			//console.log("ajaxGetStepsMetadata returned OK");
		},
		error: function(data) {
			if (data.responseJSON != undefined) {
				console.log("Failed to get steps metadata: " + data.responseJSON.error + " - " + data.responseJSON.message);
				//alert("Failed to get steps metadata: " + data.responseJSON.error + " - " + data.responseJSON.message);
			} else {
				console.log("Failed to get steps metadata");
				//alert("Failed to get steps metadata");
			}
		}
	});
}

// Run list of steps on the server:
function ajaxRunSteps() {
	return $.ajax({
		type: "POST",
		url: "steps/run",
		//dataType: "json", // Note: Comment this if the rest returns void, otherwise the call will result with error!
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(requestsList),
		success: function(data) {
			//console.log("ajaxRunSteps returned OK");
		},
		error: function(data) {
			if (data.responseJSON != undefined) {
				console.log("Failed to run steps: " + data.responseJSON.error + " - " + data.responseJSON.message);
				//alert("Failed to run steps: " + data.responseJSON.error + " - " + data.responseJSON.message);
			} else {
				console.log("Failed to run steps");
				//alert("Failed to run steps");
			}
		}
	});
}

// Get the steps status from the server:
function ajaxGetStepsStatus() {
	return $.ajax({
		url: "steps/status",
		dataType: 'json',
		success: function(data) {
			responsesMap = data;
			//console.log("ajaxGetStepsStatus returned OK");
		},
		error: function(data) {
			if (data.responseJSON != undefined) {
				console.log("Failed to get steps status: " + data.responseJSON.error + " - " + data.responseJSON.message);
				//alert("Failed to get steps status: " + data.responseJSON.error + " - " + data.responseJSON.message);
			} else {
				console.log("Failed to get steps status");
				//alert("Failed to get steps status");
			}
		}
	});
}

