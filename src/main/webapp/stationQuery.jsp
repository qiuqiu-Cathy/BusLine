<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <title>站点覆盖率</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.4.0/dist/leaflet.css"
   integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
   crossorigin=""/>
    <style type="text/css">
    body {
        padding: 0;
        margin: 0;
    }
    
    html,
    body,
    #map {
        height: 100%;
    }
    </style>
    <script src="https://unpkg.com/leaflet@1.4.0/dist/leaflet.js"
   integrity="sha512-QVftwZFqvtRNi0ZyCtsznlKSWOStnDORoefr1enyq5mVL4tmKB3S/EnC3rRJcxCPavG10IcrVGSmPh6Qw5lwrg=="
   crossorigin=""></script>
    <script src="https://cdn.jsdelivr.net/npm/leaflet.chinesetmsproviders@1.0.22/src/leaflet.ChineseTmsProviders.min.js"></script>
</head>

<body>
    <div id='map'></div>
</body>

<%
    //获取request域中的有效站点信息
    ArrayList<String> stations = (ArrayList<String>)request.getAttribute("stations");

%>

<script type="text/javascript">
	var normalm = L.tileLayer.chinaProvider('GaoDe.Normal.Map', {
	    maxZoom: 18,
	    minZoom: 5
	});
	var imgm = L.tileLayer.chinaProvider('GaoDe.Satellite.Map', {
	    maxZoom: 18,
	    minZoom: 5
	});
	var imga = L.tileLayer.chinaProvider('GaoDe.Satellite.Annotion', {
	    maxZoom: 18,
	    minZoom: 5
	});
	var normal = L.layerGroup([normalm]),
	    image = L.layerGroup([imgm, imga]);
	var baseLayers = {
	    "地图": normal,
	    "影像": image,
	}
	var map = L.map("map", {
	    //center: [31.39, 121.26],
	    center: [31.283503,121.312477],
	    zoom: 12,
	    layers: [normal],
	    zoomControl: false
	});
	L.control.layers(baseLayers, null).addTo(map);
	L.control.zoom({
	    zoomInTitle: '放大',
	    zoomOutTitle: '缩小'
	}).addTo(map);
	L.control.scale().addTo(map);  //比例尺
	L.control.attribution({ position: 'bottomleft', prefix: 'myMap' }).addTo(map); //添加地图名
	
	map.on('click', showMapPosition);    //点击地图
	map.on('dblclick',addPoint);             //双击地图

	//map.off(....) 关闭该事件

	function showMapPosition(e)
	{    
	    alert(e.latlng);
	}

	function addPoint(e)
	{
	    var marker = L.marker([e.latlng.lat, e.latlng.lng]).addTo(map);   
	}
        <%
            for(String station:stations){
        %>
                L.circle(<%=station%>,{color:'#D1EEEE',weight:0, fillColor:'#9932CC',radius:450,fillOpacity:0.45}).addTo(map)
        <%
            }
        %>
</script>

</html>