$(document).ready(getUserList());

$(window).on('load',function(){
	var sendMessage = function() {
		var finaldata = { "msgtxt":$('#msg_txt').val(), "username":username}
		console.log('LOG', finaldata);

		$.ajax({
			url : "/react/sendMessage/",
			type : 'POST',
			contentType : 'application/json',
			data: JSON.stringify(finaldata)
		});
	}


	$('#send_msg').click(function(e){
		e.preventDefault();
		sendMessage();
	});
	$('#msg_txt').on('keydown',function(event){
		if(event.keyCode==13){
			event.preventDefault();
			$('#send_msg').click();
		}
	});
})