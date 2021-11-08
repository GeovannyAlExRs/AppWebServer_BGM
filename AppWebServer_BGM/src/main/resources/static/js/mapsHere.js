/**Mapa Provincia Santa Elena
 * @param  {H.Map} map      A HERE Map instance within the application
 */

function moveMap(map){ 
    map.setCenter({lat:-2.32, lng:-80.75});
    map.setZoom(11);
}
// Draw Marker
function addDraggableMarker(map){ 
    //map.setCenter({lat:-2.32, lng:-80.75});
    //var iconMarker = URL("../img");
    //var icon = new H.map.Icon('../img/ico/icomarker.png');
    if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (pos) => {
                const {coords} = pos;
                console.log(coords);

                map.setZoom(11);
                
                var marker = new H.map.Marker(
                    {lat:coords.latitude, lng:coords.longitude}, //{icon: icon},
                    // mark the object as volatile for the smooth dragging
                    {volatility: true}
                );

                // Ensure that the marker can receive drag events
                marker.draggable = true;
                map.addObject(marker);
            }
        );
    }
}

//Step 1: initialize communication with the platform
// In your own code, replace variable window.apikey with your own apikey
const platform = new H.service.Platform({
    apikey: "kRAN8rRPFLUiyLVzPLr08u00jwEy7Xb95IJ4j8eI4E0"
    // appid: "oV7PSjQiZ5GUsM1FngMR"
});

const defaultLayers = platform.createDefaultLayers();

//Step 2: initialize a map - this map is centered over Europe
const map = new H.Map(document.getElementById('map'),
    defaultLayers.vector.normal.map,{
        center: {lat:-2.31, lng:-80.75},
        zoom: 12,
        pixelRatio: window.devicePixelRatio || 1
    }
);

// Texto del mapa
// H.tileLayer(tilesProvider, {
//     attribution: 'Map data &copy; Coop. &copy <a href="http://localhost:8081/busgeomap/">BusGeoMap</a>'
// }).addTo(map);

// add a resize listener to make sure that the map occupies the whole container
window.addEventListener('resize', () => map.getViewPort().resize());

  //Step 3: make the map interactive
  // MapEvents enables the event system
  // Behavior implements default interactions for pan/zoom (also on mobile touch environments)
var behavior = new H.mapevents.Behavior(new H.mapevents.MapEvents(map));

  // Create the default UI components
var ui = H.ui.UI.createDefault(map, defaultLayers);

//
const geocoderService = platform.getGeocodingService();

  // Now use the map as required...
window.onload = function () {
    //moveMap(map);
    addDraggableMarker(map);
}


