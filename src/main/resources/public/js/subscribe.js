function getTimeFromDate(timestamp) {
	  var date = new Date(timestamp * 1000);
	  var hours = date.getHours();
	  var minutes = date.getMinutes();
	  return hours+" : "+minutes;
}

$(document).ready(function () {
	subscribe();
	});

var subscribe = function() {
	
	var eventSource= new EventSource('/react/recieveMessage');
	
	
	 eventSource.addEventListener("MESSAGE_EVENT", function(event) {
			const eventData=JSON.parse(event.data);
			let className='recieved_text';
			if(eventData.sender === currentUser){
				className='sent_text';
			}
			else if(eventData.sender !== chatPartner){
				 $('.usersList ul > li:contains('+eventData.sender+')').addClass('unreadMsg');
				 $('.usersList ul > li:contains('+eventData.sender+')').bind('click', function(){
					$(this).removeClass('unreadMsg'); 
				 });
				return 0;				
			}
			const data = '<span class="'+className+'"><div class="msg_body">'+eventData.messageBody+'</div><div class="msg_time">'+getTimeFromDate(eventData.date)+'</div></span>';
			$('#msgs_recieved').append(data);
			scrollToBottom();

		});
	
	
	 eventSource.addEventListener("NEW_USER_EVENT", function(event) {
		 const user = event.data;
		const userList = $('.usersList ul');
		userList.append(function () {
    		return $('<li></li>').text(user).click(() =>{
    			chatPartner = user;
    			$('#infoOnLogin').hide();
    			$('div#msgs_sent').css({"display": "flex"});
    			$('#msgs_recieved').empty();
    			$('#currentReceiver').text(user);
    			getusername(user);
    			populateChat(user);
				})
		});

		});

	eventSource.error = function(event) {
		console.log("Error");
	};

	eventSource.onopen = function(event) {
		console.log("Conncetion Opened");
	}

}


var getusername = function(userName) {
	username=userName;
}


const getUserList = function() {
	
	jQuery.ajax({
		url: '/react/getCurrentUser',
		async: false,
		method: 'GET',

		success: function (result) {
			currentUser = result;
		}
	});
	
	jQuery.ajax({
		url: '/react/populateUsers',
		async: false,
		method: 'GET',

		success: function (result) {
			var ul=$('<ul></ul>');

		  result.forEach(function(user){
		    ul.append(function () {
		    		return $('<li></li>').text(user).click(() =>{
		    			chatPartner = user;
		    			$('#infoOnLogin').hide();
		    			$('div#msgs_sent').css({"display": "flex"});
		    			$('#msgs_recieved').empty();
		    			$('#currentReceiver').text(user);
		    			getusername(user);
		    			populateChat(user);
					});
				});
		  });

		  $('.usersList').html(ul);
			console.log(result);
			// handle user list here
		}
	});
	
}

var populateChat = function(username){
	
	
	jQuery.ajax({
		url: '/react/populateChat',
		method: 'POST',
		type : 'POST',
		contentType : 'application/json',
		data: username
	});
	
	
	
	
	
}

function scrollToBottom () {
	  // Selectors
	  var messages = jQuery('#msgs_recieved');
	  var newMessage = messages.children('span:last-child')
	  // Heights
	  var clientHeight = messages.prop('clientHeight');
	  var scrollTop = messages.prop('scrollTop');
	  var scrollHeight = messages.prop('scrollHeight');
	  var newMessageHeight = newMessage.innerHeight();
	  var lastMessageHeight = newMessage.prev().innerHeight();

	  if (clientHeight + scrollTop + newMessageHeight + lastMessageHeight >= scrollHeight) {
	    messages.scrollTop(scrollHeight);
	  }
	}


