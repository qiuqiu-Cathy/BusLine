<%@ page import="java.lang.reflect.Array" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <title>所有有效线路展示</title>
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
    ArrayList<String> coordsJson = (ArrayList<String>) request.getAttribute("coordsJson");
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
        center: [31.39, 121.26],
        //center: [31.283503,121.312477],
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
    //style风格
    var myStyle = {
        "color": "#ff0000",
        "weight": 4,
        "opacity": 0.55
    };
    <%
        for(String coord:coordsJson){
    %>
        var myLines = [<%=coord%>];
        var myLayer = L.geoJSON().addTo(map);
        myLayer.addData(myLines);
        L.geoJSON(myLines, {
            style: myStyle
        }).addTo(map);
    <%    }
    %>


</script>

</html>