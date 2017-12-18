// Author: Naor Bar
// Date: July 2017
// Description: This js file includes all client methods

var stepsList = null;
var requestsList = null;
/*
 var myData = {
    content_text: $('#contentText').val(),
    name: $('#Name').val(),
    event_id: $('#eventId').val()
};
 */
/*var requestsList = [
                	{
                		"id": "1",
                        "className": "com.ca.steps.impl.CopyFileStepImpl",
                        "members": { 
                        	"source": "C:\\temp1.txt",
                        	"target": "C:\\temp2.txt"
                        			},
                        "requestType": "ALL"
                    },
                    {
                    	"id": "2",
                        "className": "com.ca.steps.impl.CopyFileStepImpl",
                        "requestType": "ALL"
                    },
                    {
                    	"id": "3",
                        "className": "com.ca.steps.impl.Test",
                        "requestType": "ALL"
                    }
                ];*/
var responsesMap = null;

// Run steps:
function runSteps() {
	$('#spinner').show();
	
	$.when(ajaxRunSteps(requestsList))
	.done(function()
	{
		//console.log("runSteps - SUCCEEDED!");
		$('#spinner').hide();
	})
	.fail(function() {
		console.log("runSteps - ERROR!");
		$('#spinner').hide();
	});
}

// Get steps status:
function getStepsStatus() {
	//$('#spinner').show();
	
	$.when(ajaxGetStepsStatus())
	.done(function()
	{
		//console.log("getStepsStatus - SUCCEEDED!");
		//$('#spinner').hide();
	})
	.fail(function() {
		console.log("getStepsStatus - ERROR!");
		//$('#spinner').hide();
	});
}
