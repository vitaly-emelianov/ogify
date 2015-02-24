function getCookie(name) {
	var matches = document.cookie.match(new RegExp(
		"(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
	));
	return matches ? decodeURIComponent(matches[1]) : undefined;
}

var pos = {};
var marker;
var myLatlng;

var options = {
    enableHighAccuracy: true,
    timeout: 5000,
    maximumAge: 0
};

function success(position) {
    var crd = position.coords;
    pos.latitude =  crd.latitude;
    pos.longitude =  crd.longitude;
    myLatlng =  new google.maps.LatLng(pos.latitude, pos.longitude);
    map.setCenter( myLatlng, 1);

    marker = new google.maps.Marker({
        position: myLatlng,
        map: map,
        draggable:true,
        title: 'Вы находитесь здесь'
    });

};

function error(err) {
    console.warn('ERROR(' + err.code + '): ' + err.message);
};

//        navigator.geolocation.getCurrentPosition(success, error, options);

function loadScript(src){

    var script = document.createElement("script");
    script.type = "text/javascript";
    document.getElementsByTagName("head")[0].appendChild(script);
    script.src = src;
}
       // loadScript('http://maps.googleapis.com/maps/api/js?v=3&sensor=false&callback=initialize');

//        window.addEventListener('load',function(){
//            if(document.getElementById('world-map')){
//                var mapOptions = {
//                    zoom: 8,
//                    center: new google.maps.LatLng(pos.latitude, pos.longitude),
//                    mapTypeId: google.maps.MapTypeId.ROADMAP
//                };
//                map = new google.maps.Map(document.getElementById('world-map'),
//                        mapOptions);
//            }
//        },false);

function initialize() {
	geocoder = new google.maps.Geocoder();
	var latlng = new google.maps.LatLng(-34.397, 150.644);
	var mapOptions = {
	    zoom: 8,
	    center: latlng
	}
	map = new google.maps.Map(document.getElementById('world-map'), mapOptions);
	navigator.geolocation.getCurrentPosition(success, error, options);
}