// This is called with the results from from FB.getLoginStatus().
function statusChangeCallback(response) {
	console.log(response);
	// The response object is returned with a status field that lets the
	// app know the current login status of the person.
	// Full docs on the response object can be found in the documentation
	// for FB.getLoginStatus().
	if (response.status === 'connected') {
		// Logged into your app and Facebook.
		registerUser();
	}
}

// This function is called when someone finishes with the Login
// Button.  See the onlogin handler attached to it in the sample
// code below.
function checkLoginState() {
	FB.getLoginStatus(function(response) {
		statusChangeCallback(response);
	});
}

window.fbAsyncInit = function() {
	FB.init({
		appId      : '1522065684698000',
		cookie     : true, 
		// the session
		xfbml      : true,  // parse social plugins on this page
		version    : 'v2.1' // use version 2.1
	});

	FB.getLoginStatus(function(response) {
		statusChangeCallback(response);
	});

};

// Load the SDK asynchronously
(function(d, s, id) {
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id)) return;
	js = d.createElement(s); js.id = id;
	js.src = "//connect.facebook.net/pt_BR/sdk.js";
	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

function registerUser() {
	FB.api('/me', function(response) { 
		$.ajax({
			type: "POST",
			url: API_URL + "/createUser",
			data: { name: response.name, facebook_id: response.id, gender: response.gender },
			success: function(data){
				if (data.status == 1 || data.status == -7) {
					USER = {facebook_id: data.facebook_id, name: data.name, energy_left: data.energy_left, api_key: data.api_key, 
							gender: data.gender, avatar: data.avatar, points: data.points, time_since_last_quest: data.time_since_last_quest};
					sessionStorage.user = JSON.stringify(USER);
					window.location = "ufvquest.html";
				}
			}
		});
	});
}