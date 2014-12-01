var isMenuOpen = false;
var isAddQuestButtonActive = false;
var map;
var addQuestMarker = null;
var lighboxOriginalForm;
var questTypes;
var quests = [];
var questsInfo = [];
var rankingUsers;

$(document).ready(function () {
	
	// Initializing map
	var mapOptions = {
		center: new google.maps.LatLng(-20.762392, -42.868431),
		zoom: 16,
		panControl: false,
		zoomControlOptions: {
			style: google.maps.ZoomControlStyle.LARGE,
			position: google.maps.ControlPosition.RIGHT_TOP
		},
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);

	// Initialing USER
	if (sessionStorage.getItem("user")) {
		USER = JSON.parse(sessionStorage.user);
		$(".user-name").html(USER.name);
		$(".user-points").html((USER.points ? USER.points : "0") + "pts");
		$(".user-avatar").css("background", "url(" + USER.avatar + ") no-repeat");
		$(".user-avatar").css("background-size", "80px auto");
	} else {
		window.location = "index.html";
	}
	
	lighboxOriginalForm = $(".add-quest-lighbox-inputs-form").html();
	
	// Initializing quest types
	$.ajax({
		type: "GET",
		url: API_URL + "/getQuestTypes",
		success: function(data){
			if (data.status == 1) {
				questTypes = data.questTypes;
			} else {
				alert("Erro loco na hora de pegar os questtypes");
			}
		}
	});
	
	// Getting quests
	$.ajax({
		type: "GET",
		url: API_URL + "/getActiveQuests",
		success: function(data){
			if (data.status == 1) {
				for (var i = 0; i < data.quests.length; i++) {
					var icon = "../img/";
					if (data.quests[i].type == "gtaa")
						icon += "marker_gtaa.png";
					else if (data.quests[i].type == "saa")
						icon += "marker_saa.png";
						
					quests.push(new google.maps.Marker({
						position: new google.maps.LatLng(data.quests[i].latitude, data.quests[i].longitude),
						map: map,
						title: 'Quest',
						icon: icon
					}));
					
					questsInfo.push(new google.maps.InfoWindow({
						content: "<div class='info-downloaded-quest'><h3>" + data.quests[i].title + "</h3>" + 
								"<div class='info-downloaded-quest-location'><b>Lugar</b>: " + data.quests[i].place_name + "</div>" +
								"<div class='info-donwloaded-quest-description'>" + data.quests[i].description + "</div></div>"
					}));
					google.maps.event.addListener(quests[i], 'click', function(innerKey) {
						return function() {
							questsInfo[innerKey].open(map, quests[innerKey]);
						}
					}(i));
				}
			} else {
				alert("Erro loco na hora de pegar os questtypes");
			}
		}
	});
	
	$(".open-menu-button").hover(
		function() {
			$(".open-menu-button").animate({left: "+=15", duration: 50});
		},
		function() {
			$(".open-menu-button").animate({left: "-=15", duration: 50});
		}
	);
	
});

function openMenu() {
	if(isMenuOpen) {
		$(".side-menu-wrapper").animate({left: "-=300", duration: 200});
		isMenuOpen = false;
	} else {
		$(".side-menu-wrapper").animate({left: "+=300", duration: 200});
		isMenuOpen = true;
	}
}

// onChange select option to add quest
function changeQuest (select) {
	$(".add-quest-description").html(questTypes[select.value].fields.description);
}

// Open and close light box to add 
function openAddQuestLightBox() {
	$(".total-dim").css("display", "block");
	$(".add-quest-lightbox").css("display", "block");
	$(".add-quest-lighbox-inputs-form").html(lighboxOriginalForm);
	$(".add-quest-lighbox-inputs-form").append(questTypes[$(".add-quest-select").val()].fields.html);
	$(".add-quest-lightbox-type").html(questTypes[$(".add-quest-select").val()].fields.name);
}

function closeAddQuestLightBox() {
	$(".total-dim").css("display", "none");
	$(".add-quest-lightbox").css("display", "none");
}

// Add Quest button on InfoWindow clicked
function onAddQuestButtonClick() {
	if (isAddQuestButtonActive) {
		isAddQuestButtonActive = false;
		google.maps.event.clearListeners(map, 'click');
		$(".add-quest-button").removeClass("add-quest-active");
		if (addQuestMarker != null) addQuestMarker.setMap(null);
	} else {
		
		if (USER.time_since_last_quest <= 7) {
			alert("Espere " + (7 - USER.time_since_last_quest) + " dias para cadastrar uma nova quest");
			return;
		}
		
		// Generating InfoWindow content
		var infoContent = "<div class='add-quest-info-wrapper'><h3 class='info-title'>Escolha o tipo de Quest</h3><br/>" + "<form class='info-add-quest-form' onsubmit='return false;'>" + 
							"<select onchange='changeQuest(this);' class='add-quest-select'>";
							
		for (var i = 0; i < questTypes.length; i++) 
			infoContent += "<option value='" + i + "'>" + questTypes[i].fields.name + "</option>";
			
		infoContent += "</select>" + "<div class='add-quest-description'>" + questTypes[0].fields.description + "</div>" + "<input onclick='openAddQuestLightBox()' type='submit' value='Cadastrar' />" + "</form>" + "</div>"; 
		
		isAddQuestButtonActive = true;
		
		$(".add-quest-button").addClass("add-quest-active");
		
		google.maps.event.addListener(map, 'click', function(event) {
			
			var itsOk = true;
			
			for (var i = 0; i < quests.length; i++) {
				if(getDistanceBetween(event.latLng, quests[i].getPosition()) < 20) {
					itsOk = false;
					break;
				}
			}
			
			if (itsOk) {
				if (addQuestMarker != null) addQuestMarker.setMap(null);
				addQuestMarker = new google.maps.Marker({
					position: event.latLng,
					map: map,
					title: 'Adicionando quest',
					icon: "../img/marker_newquest.png"
				});
				var infowindow = new google.maps.InfoWindow({
					content: infoContent
				});
				infowindow.open(map, addQuestMarker);
				google.maps.event.addListener(addQuestMarker, 'click', function() {
					infowindow.open(map, addQuestMarker);
				});
			} else {
				alert("Não é possível cadastrar uma quest tão próxima de outra ativa");
			}
		});
		
	}
}

// Upload quest button on lightbox
function uploadQuest(form) {
	var formObj = getFormObj(form);
	formObj.latitude = addQuestMarker.getPosition().lat();
	formObj.longitude = addQuestMarker.getPosition().lng();
	formObj.facebook_id = USER.facebook_id;
	formObj.api_key = USER.api_key;
	formObj.answer = formObj.answer.toLowerCase();
	
	var apiFunction;
	if(formObj.quest_type == "gtaa")
		apiFunction = "/createQuestGoToAndAnswer";
	else if(formObj.quest_type == "saa")
		apiFunction = "/createQuestSeekAndAnswer";
	
	$.ajax({
		type: "POST",
		url: API_URL + apiFunction,
		data: formObj,
		success: function(data){
			console.log(data);
			if (data.status == 1) {
				closeAddQuestLightBox();
				$(".lightbox-error").html();
				
				location.reload();
				
				
			} else {
				$(".lightbox-error").html("Ocorreu um erro ao cadastrar sua quest. Tente novamente");
			}
		}
	});
}

/***** Ranking ******/
function openRankingLightbox() {
	$(".total-dim").css("display", "block");
	$(".user-ranking-lightbox").css("display", "block");
	$(".loader").html("<img src='img/loading.gif' class='loading-img' />");
	$(".ranking-indeed").html("");
	$.ajax({
		type: "GET",
		url: API_URL + "/getUserRanking",
		success: function(data){
			if (data.status == 1) {
				rankingUsers = data;
				
				setTimeout(function () {
					$(".loader").html("");
					populateRanking('w', $(".ranking-tabs li")[0]);
				}, 2000);
			} else {
				alert("Erro loco na hora de baixar o ranking");
				closeRankingLightbox();
			}
		}
	});
}

function closeRankingLightbox() {
	$(".total-dim").css("display", "none");
	$(".user-ranking-lightbox").css("display", "none");
}

function populateRanking(ranking, li) {
	var data;
	ranking == 'w' ? data = rankingUsers.week : data = rankingUsers.total;
	
	$(".ranking-tabs li").each(function() { $(this).removeClass("ranking-tab-active") });
	$(li).addClass("ranking-tab-active");
	
	$(".ranking-indeed").html("");
	
	for (var i = 0; i < data.length; i++) {
		$(".ranking-indeed").append(
			"<div class='ranking-item'>" +
				"<div class='ranking-item-position'>" + (i+1) + "º</div><div class='ranking-item-avatar'></div><div class='ranking-item-name'>" + data[i].name + "</div><div class='ranking-item-points'>" + data[i].points + "pts</div>" +
			"</div>");
		$(".ranking-indeed .ranking-item:last-child .ranking-item-avatar").css("background", "url(" + data[i].avatar + ") no-repeat");
		$(".ranking-indeed .ranking-item:last-child .ranking-item-avatar").css("background-size", "80px auto");
		
	}
	
}

/******* Quest History ********/
function openQuestHistoryLightbox() {
	$(".total-dim").css("display", "block");
	$(".quest-history-lightbox").css("display", "block");
	
	$(".loader").html("<img src='img/loading.gif' class='loading-img' />");
	$(".quest-history-indeed").html("");
	
	$.ajax({
		type: "POST",
		url: API_URL + "/getQuestHistory",
		data: {facebook_id: USER.facebook_id, api_key: USER.api_key},
		success: function(data){
			$(".loader").html("");
			if (data.status == 1) {
				var hist = data.history;
				$(".quest-history-indeed").html("");
				for (var i = 0; i < hist.length; i++) {
					$(".quest-history-indeed").append(
						"<div class='history-item'>" +
							"<div class='history-date'>" + formatDate(hist[i].timestamp) + "</div>" + 
							"<img class='history-quest-icon' src='../img/" + getQuestHistoryIcon(hist[i].quest_type) + "' />" +
							"<div class='history-quest-info'>" + 
								"<div class='history-quest-title'>" + hist[i].quest_title + "</div>" + 
								"<div class='history-quest-desc'>" + hist[i].quest_description + "</div>" +
							"</div>" +
							"<img class='history-solved-img' src='../img/" + (hist[i].solved ? "quest_solved.png" : "quest_failed.png") + "' />" +
							"<div class='history-points'>" + (hist[i].solved ? hist[i].points + "pts" : "") + "</div>" +
						"</div>");
				}
			} else {
				alert("Erro loco na hora de baixar o ranking");
				closeQuestHistoryLightbox();
			}
		}
	});
}

function formatDate(date) {
	return date.substring(8, 10) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4);
}

function getQuestHistoryIcon(type) {
	switch(type) {
		case "gtaa": return "marker_gtaa_large.png";
		case "saa":  return "marker_saa_large.png";
	}
}

function closeQuestHistoryLightbox() {
	$(".total-dim").css("display", "none");
	$(".quest-history-lightbox").css("display", "none");
}