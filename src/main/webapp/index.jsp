<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.qiu.shu.busline.domain.Line" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>公交线路优化首页</title>
	<script type = "text/javascript"  src="js/jquery-3.4.0.js"></script>
    <script type = "text/javascript"  src="js/leaflet-ant-path.js"></script>

	<link rel="stylesheet" href="https://unpkg.com/leaflet@1.4.0/dist/leaflet.css"
		  integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
		  crossorigin=""/>
	<script src="https://unpkg.com/leaflet@1.4.0/dist/leaflet.js"
			integrity="sha512-QVftwZFqvtRNi0ZyCtsznlKSWOStnDORoefr1enyq5mVL4tmKB3S/EnC3rRJcxCPavG10IcrVGSmPh6Qw5lwrg=="
			crossorigin=""></script>
	<script src="https://cdn.jsdelivr.net/npm/leaflet.chinesetmsproviders@1.0.22/src/leaflet.ChineseTmsProviders.min.js"></script>

	<style type="text/css">
			#map{
				margin-left:200px;
				margin-top:30px;
				margin-bottom: 5px;
				width:1000px;
				height: 480px;
			 }
			.content{
				position: fixed; top:100px; width: 100px;height: 100px;
			}
            #addLine { /* 新建线路弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 100px;
            }
            #startNew{ /* 新增线路-开始的弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 100px;
            }
            #startMoreNew{ /* 新增线路-开始-添加站点至线路-继续添加的弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 100px;
            }
            #continueNew{ /* 新增线路-继续的弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 100px;
            }
            #addStop { /* 新建线路-添加站点至线路 添加原有站点和新站点（初始站点）的弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 100px;
            }
            #addCoord { /* 新建线路-开始-添加拐点至线路 弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 100px;
            }
            #continueAddCoord { /* 新建线路-继续-添加拐点至线路 弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 100px;
            }
            #correctLine { /* 修改线路的弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 100px;
            }
            #startCorrect{ /* 开始修改-修改站点 的弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 70px;
            }
            #correct{ /* 开始修改 的弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 70px;
            }
            #correctCoord{ /* 修改线路-增加拐点至线路的弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 100px;
            }
            #deleteLine{ /* 删除线路的弹框的页面*/
                width: 60px; /*宽度*/
                height: 100px; /*高度*/
                background: #fff; /*背景色*/
                display: none; /*隐藏*/
                position: fixed;
                top: 100px;
            }#correctLineStatus{ /*线路投入运营弹框的页面*/
                 width: 60px; /*宽度*/
                 height: 100px; /*高度*/
                 background: #fff; /*背景色*/
                 display: none; /*隐藏*/
                 position: fixed;
                 top: 100px;
             }#neighborCoverage{ /*居民区站点覆盖率查询页面*/
                   width: 60px; /*宽度*/
                   height: 100px; /*高度*/
                   background: #fff; /*背景色*/
                   display: none; /*隐藏*/
                   position: fixed;
                   top: 100px;
               }


	</style>
</head>

<body>
	<div><h2>欢迎进入公交线路优化仿真实验平台</h2></div>
	<div id ="map"></div>
</body>
<%
	//获取request域中的有效线路信息
    List<Line> linesResult = (List<Line>)request.getAttribute("linesResult");
%>

<div class = "wrap">
	<div id='form' class = "content">

<%--        公交线路查询<input type="text" id="queryLineName"/>--%>
        <label class="col-sm-2 control-label">公交线路查询</label>
        <div class="col-sm-4">
            <select name="queryLineID" id="queryLineID" class="text input-large form-control">
                <option value="">在运营公交线路列表</option>
            </select>
        </div>
        <input type="button" value="线路及站点显示" onclick="queryLineAndStops()">
<%--        <input type="button" value="线路查询" onclick="queryLine()"/>--%>
<%--        <input type="button" value="线路站点显示" onclick="queryLineOfStops()">--%>

        <p><input type="button" value="所有有效站点显示" onclick="stopShow()"/></p>

        <p><input type="button" value="所有有效线路显示" onclick="lineShow()"/></p>

        <p><input type="button" value="站点覆盖率展示" onclick="stopCoverage()"/></p>

<%--        <p><input type="text" id ="measure" autocomplete="on"/>--%>
<%--        <input type="button" value="米内居民区站点覆盖率展示" onclick="neighbourCoverageQuery()"></p>--%>
        <p><input type="button" value="居民区站点覆盖率查询" onclick="neighborCoverage()">

        <p><input type="button" value="线路删除" onclick="deleteLine()"></p>

        <p><input type="button"  value="新建线路" onclick="newLine()"></p>

        <p><input type="button"  value="修改线路" onclick="correctLine()"></p>
<%--        将status=3&&=5 的线路以下拉列表的形式，使其可以选中投入运营 线路状态改至1，包含站点的状态改至1--%>
        <p><input type="button"  value="线路投入运营" onclick="correctLineStatus()"></p>
	</div>

<%--bug！！--%>
    <div id="neighborCoverage" title="居民区站点覆盖率查询" style="" >
        <div style="width:500px;height: 40px;">
            <h3>居民区站点覆盖率查询</h3>
            <form>
                <label class="col-sm-2 control-label">输入查询半径：</label>
                <p><input type="text" id ="measure" autocomplete="on"/></p>

                <label class="col-sm-2 control-label">选择一个居民区：</label>
                <div class="col-sm-4">
                    <select name="neighborID" id="neighborID" class="text input-large form-control">
                        <option value="">居民区</option>
                    </select>
                </div>

                <p><input type="button" value="居民区站点覆盖率查询" onclick="neighbourCoverageByID()"></p>

                <p><input type="button" value="返回" onclick="backToForm()"></p>
            </form>
        </div>
    </div>


<%--    线路投入运营页面--%>
    <div id="correctLineStatus" title="线路投入运营" style="" >
        <div style="width:500px;height: 40px;">
            <h3>线路投入运营</h3>
            <form>
                <label class="col-sm-2 control-label">待投入运营公交线路</label>
                <div class="col-sm-4">
                    <select name="finishConstructLineID" id="finishConstructLineID" class="text input-large form-control">
                        <option value="">待投入运营公交线</option>
                    </select>
                </div>

                <p><input type="button" value="查看该线路走向及站点" onclick="getLineByIDtoShow()"></p>
<%--                <p><input type="button" value="查看该线路走向及站点和拐点" onclick="getLineByID()"></p>--%>

                <p><input type = "button" value="将此线路投入运营" onclick="busOnLine()"></p>
<%--                将线路状态改为1，新站点的status改为1，刷新FinishConsturctList()和FinishConstructList()--%>
                <p><input type="button" value="返回" onclick="busOnLineBack()"></p>
<%--                更新status1List()--%>
            </form>
        </div>
    </div>

<%--    删除线路弹出框div--%>
    <div id="deleteLine" title="删除线路" style="">
        <div style="width:500px;height: 40px;">
            <h3>删除线路</h3>
            <form>
<%--                <label class="col-sm-2 control-label">删除线路名 </label>--%>
                <div class="col-sm-4">
                    <select name="deleteID" id="deleteID" class="text input-large form-control">
                        <option value="">请选择要删除公交线路名</option>
                    </select>
                    <p><input type="button" value="查看该线路及站点" onclick="queryLineByID()"></p>
                    <p><input type="button" value="删除该线路" onclick="deleteLineByID()"></p>
                    <p><input type="button" value="返回" onclick="deleteBack()">
                </div>
            </form>
        </div>
    </div>


<%--    新建线路所有弹出框div  bug--%>
    <div id="dealAddLineDiv">
        <%--        新建线路--%>
        <div id="addLine" title="新建线路"  style="" >
            <div style="width:500px;height: 40px;">
                <h3>新建线路</h3>
                <form>
                    <p>新建的线路名：</p>
                    <p><input type="text" name = "newLineName" id="newLineName" /></p>
                    <p><input type="button" value="开始" onclick="startNew()"></p>

                    <label class="col-sm-2 control-label">在建线路 </label>
                    <div class="col-sm-4">
                        <select name="continueAddLineName" id="continueAddLineName" class="text input-large form-control">
                            <option value="">请选择在建的公交线路</option>
                        </select>
<%--                        显示线路添加界面及已添加的线路和站点信息显示--%>
                        <p><input type="button" value="继续" onclick="continueNew()"></p>
                        <p><input type="button" value="返回" onclick="addLineBack()"></p>

                    </div>
                </form>
            </div>
        </div>

<%--    新建线路-开始弹出框--%>
        <div id="startNew" title="开始新建线路"  style="" >
            <div style="width:500px;height: 40px;">
                <h3>添加站点或拐点至线路</h3>
                <form>
                    <p><input type="button" value="添加站点至线路" onclick="addStop()">
                    <p><input type="button" value="添加拐点至线路" onclick="addCoord()">
                        <%--                        返回新建线路主页面--%>
                    <input type="button" value="返回" onclick="addBack()"></p>
                </form>
            </div>
        </div>

        <%--    新建线路-开始-继续的跳转页面弹出框--%>
        <div id="startMoreNew" title="继续新建线路"  style="" >
            <div style="width:500px;height: 40px;">
                <h3>添加站点或拐点至线路</h3>
                <form>
                    <p><input type="button" value="添加站点至线路" onclick="addMoreStop()">
                    <p><input type="button" value="添加拐点至线路" onclick="addCoord()">
                    <p><input type="button" value="新建完成" onclick="finishConstruct()">
                        <%--                        返回新建线路主页面--%>
                        <input type="button" value="返回" onclick="addBack()"></p>
                </form>
            </div>
        </div>


        <%--    新建线路-继续弹出框--%>
        <div id="continueNew" title="继续新建线路"  style="" >
            <div style="width:500px;height: 40px;">
                <h3>继续添加站点或拐点</h3>
                <form>
                    <p><input type="button" value="添加站点至线路" onclick="continueLineByID()">
                    <p><input type="button" value="添加拐点至线路" onclick="continueAddCoord()">
                    <p><input type="button" value="新建完成" onclick="finishConstruct()">
                        <%--                        返回新建线路主页面--%>
                    <p><input type="button" value="返回" onclick="addBack()"></p>
                </form>
            </div>
        </div>

    <%--        新建线路-开始-添加站点的弹出框div--%>
        <div id="addStop" title="添加站点"  style="" >
            <div style="width:500px;height: 40px;">
                <h3>添加站点</h3>
                <form>
                    <input type="checkbox" name="old" id="old"/>原有站点
                    <input type="checkbox" name="new" id="new"/>新站点
                    <p>站点名：</p>
                    <p><input type="text" name="addStationName" id="addStationName" /></p>
                    <p>经纬度(原有可不填)：x,y</p>
                    <p><input type="text" name="addStationLocation" id="addStationLocation"></p>
                    <p>站点顺序:</p>
                    <p><input type="text" name="sequence" id="sequence"></p>
<%--                    根据checkbox的值选择添加原有站点还是新站点，返回添加站点的视图，保存添加的站点，刷新目前已添加的站点组成的线路--%>
                    <p><input type="button"  value="保存并继续添加" onclick="saveAndContinue()"></p>
                    <p><input type="button" value="返回" onclick="backToAdd()"></p>
                </form>
            </div>
        </div>

        <%--        新建线路-开始-添加拐点至线路 的弹出框div--%>
        <div id="addCoord" title="添加拐点至线路"  style="" >
            <div style="width:500px;height: 40px;">
                <h3>添加拐点至线路</h3>
                <form>
                    <p>拐点经纬度：x,y</p>
                    <p><input type="text" id="addCoordLoc"></p>
                    <p>添加至站点顺序为 后</p>
                    <p><input type="text" id="stopSequence"></p>
<%--                    保存拐点至线路，跳转至开始添加页面，线路展示--%>
                    <p><input type="button"  value="保存该拐点" onclick="saveCoord()"></p>
                    <p><input type="button"  value="返回" onclick="startMoreNew()"></p>
                </form>
            </div>
        </div>

        <%--        新建线路-继续-添加拐点至线路 的弹出框div--%>
        <div id="continueAddCoord" title="添加拐点至线路"  style="" >
            <div style="width:500px;height: 40px;">
                <h3>添加拐点至线路</h3>
                <form>
                    <p>拐点经纬度：x,y</p>
                    <p><input type="text" id="continueAddCoordLoc"></p>
                    <p>添加至站点顺序为 后</p>
                    <p><input type="text" id="continueStopSequence"></p>
                    <%--                    保存拐点至线路，跳转至继续添加页面，线路展示，参考saveCoord()--%>
                    <p><input type="button"  value="保存该拐点" onclick="continueSaveCoord()"></p>
                    <p><input type="button"  value="返回" onclick="continueNew()"></p>
                </form>
            </div>
        </div>



    </div>


<%--    修改线路所有弹出框div--%>
    <div id = "correctLineDiv">
        <%--        修改线路弹出框的div--%>
        <div id="correctLine" title="修改线路"  style="" >
            <div style="width:500px;height: 40px;">
                <h3>修改线路</h3>
                <form>
                    <label class="col-sm-2 control-label">可修改线路</label>
                    <div class="col-sm-4">
                        <select name="correctLineID" id="correctLineID" class="text input-large form-control">
                            <option value="">请选择需要修改的公交线路</option>
                        </select>
                    </div>

<%--                    <label class="col-sm-2 control-label">修改中的线路</label>--%>
<%--                    <div class="col-sm-4">--%>
<%--                        <select name="correctingLineID" id="correctingLineID" class="text input-large form-control">--%>
<%--                            <option value="">修改中的公交线路</option>--%>
<%--                        </select>--%>
<%--                    </div>--%>

                    <p><input type="button" value="查看该线路走向及站点和拐点" onclick="getLineByID()"></p>

<%--                    <p><input type="button" value="开始修改站点" onclick="startCorrect()"></p>--%>
                    <p><input type = "button" value="开始修改" onclick="correct()"></p>
                    <p><input type="button" value="返回" onclick="correctLineBack()"></p>
                </form>
            </div>
        </div>

        <%--        开始修改 弹出框的div--%>
        <div id="correct" title="开始修改线路"  style="" >
            <div style="width:500px;height: 40px;">
                <h3>修改线路站点及拐点</h3>
                <form>
                    <p><input type = "button" value="修改站点" onclick="startCorrect()"></p>
                    <p><input type="button" value="增加拐点至线路" onclick="correctCoord()"></p>
<%--                    页面跳转至correctLine，将目前修改的线路状态转为status=5--%>
                    <p><input type="button" value="完成修改" onclick="finishCorrect()"></p>
                    <p><input type="button" value="返回" onclick="backToCorrectMore()"></p>
                </form>
            </div>
        </div>

        <%--        修改线路-开始修改-修改拐点 的弹出框div--%>
        <div id="correctCoord" title="添加拐点至线路"  style="" >
            <div style="width:500px;height: 40px;">
                <h3>添加拐点至线路</h3>
                <form>
                    <p>拐点经纬度：x,y</p>
                    <p><input type="text" id="correctCoordLoc"></p>
                    <p>添加至站点顺序为 后</p>
                    <p><input type="text" id="correct_Sequence"></p>
                    <%--                    保存拐点至线路，跳转至开始添加页面，线路展示 参考saveCoord--%>
                    <p><input type="button"  value="保存该拐点" onclick="saveCorrectCoord()"></p>
                    <p><input type="button"  value="返回" onclick="correct()"></p>
                </form>
            </div>
        </div>

<%--            修改线路-开始修改-修改站点的弹出框div--%>
            <div id="startCorrect" title="修改线路" style="">
                <div style="width:500px;height: 40px;">
                    <h3>修改站点</h3>
                    <form>
                        <p>添加站点时请选择！！</p>
                        <input type="checkbox" name="oldStop" id="oldStop"/>原有站点
                        <input type="checkbox" name="newStop" id="newStop"/>新站点
                        <p>站点名（必填）：</p>
                        <p><input type="text" id="correctStationName" /></p>
                        <p>经纬度(新站点需填)：x,y</p>
                        <p><input type="text" id="correctStationLocation"></p>
                        <p>站点顺序（必填）:</p>
                        <p><input type="text" id="correctSequence"></p>
                        <%--    根据给到的站点名以及站点顺序，删除该线路的中的该站点 更改line的coord、stops以及station中的buslines  地图展示删除该站点后的线路图 页面跳转到开始修改后的页面--%>
                        <p><input type="button"  value="删除该站点" onclick="deleteStation()"></p>
                        <%--    根据checkbox的值区分新站点/老站点 新站点需要名字、loc、顺序 老站点需要名字、顺序， 把原先该顺序的站点往后顺延，更改line的coord、stops以及stationd的buslines 地图展示添加站点后的线路图 页面跳转到开始修改后的页面--%>
                        <p><input type="button"  value="添加该站点至线路" onclick="addStationToLine()"></p>
                        <%--   返回开始修改的页面--%>
                        <p><input type="button" value="返回" onclick="backToCorrect()"></p>
                    </form>
                </div>
            </div>

    </div>

<%--    控制右侧地图--%>
	<script>
        var global_query_line = null;
        var global_query_markers = [];
        document.oncontextmenu = function(){
            return false;
        }
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
		var normal = L.layerGroup([normalm]) ,
            queryLineMap=L.layerGroup([normalm]),
            stationCoverageMap=L.layerGroup([normalm]),
            stationMap=L.layerGroup([normalm]),
        neighbourCoverageMap = L.layerGroup([normalm]),
            underConstructionMap = L.layerGroup([normalm]),
            correctLineMap = L.layerGroup([normalm]),
            normal2=L.layerGroup([normalm]),
        image = L.layerGroup([imgm, imga]);

		var baseLayers = {
			"有效线路展示1": normal, //首页上面放置了所有有效的线路图展示
            "线路查询层":queryLineMap,
			"站点覆盖率展示":stationCoverageMap,
            "站点信息层":stationMap,
            "居民区站点覆盖率展示层":neighbourCoverageMap,
            "在建线路展示层":underConstructionMap,
            "修改线路展示层":correctLineMap,
            "有效线路展示2":normal2,//有效线路查询层
			"影像": image
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
		function showMapPosition(e)//eeee
		{
			alert(e.latlng);
			console.log(e.latlng)
            var s = e.latlng.lat.toString().substring(0,10) + "," + e.latlng.lng.toString().substring(0,10);
            console.log(s);
            $('#continueAddCoordLoc').val(s)
            $('#addStationLocation').val(s)
            $('#addCoordLoc').val(s)
            $('#correctCoordLoc').val(s)
            $('#correctStationLocation').val(s)
		}

		function addPoint(e)
		{
			var marker = L.marker([e.latlng.lat, e.latlng.lng]).addTo(map);
		}



		//所有有效线路的显示风格
		var myStyle = {
			"color": "#1E90FF",
			"weight": 3,
			"opacity": 0.85
		};
        var styleClick = {
            "color": "#ff0000",
            "weight": 17,
            "opacity": 0.85
        };
		//展示所有有效线路在 normal图层（有效线路展示层）
        <%
            for(Line line:linesResult){
        %>
            var myLines = eval(<%=line.getCoordinates()%>);
            <%--var myLayer = L.geoJSON().addTo(normal);--%>
            <%--myLayer.addData(myLines);--%>
            var lines = L.geoJSON(myLines, {
                style: myStyle
            }).addTo(normal);
            lines.bindPopup("<%=line.getLineName()%>");
        <% }
        %>
        var len = $("path.leaflet-interactive").length
        console.log("length = " ,len)
        document.oncontextmenu = function(){
            return false;
        }
        //右键某条线路，该线路会变红（还存在如何取消变红效果）
        for(var i=0;i<len;i++)
        {
            var xx = $("path.leaflet-interactive")[i]
            xx.onmousedown = function(e){
                if (e.button == 2 ){
                    console.log(xx)
                    this.setAttribute("stroke-opacity",1)
                    this.setAttribute("stroke-width",5)
                    console.log(this)
                }
            }
        }

        //首页-线路及站点显示
        function queryLineAndStops(){
            var $queryLineID = $("#queryLineID").val();
            $.ajax({
                url:"LineQueryByIDServlet",
                data:"queryLineID="+$queryLineID,
                dataType:"json",
                success:function (result) {
                    var lineData = eval(result) ;//Object形式
                    var line = JSON.parse(lineData.coordinates) //字符串转对象
                    console.log(line)
                    var style = {//查询线路展示的风格
                        "color": "#ff0000",
                        "weight": 2,
                        "opacity": 0.55
                    };
                    if(global_query_line!=null){
                        queryLineMap.removeLayer(global_query_line);
                    }
                    global_query_line = L.geoJSON([line], {
                        style: style
                    }).addTo(queryLineMap);
                    //显示站点
                    var stops= JSON.parse(lineData.stops)
                    var len = stops.length;
                    if(global_query_markers.length>0){
                        for(var i=0;i<global_query_markers.length;i++){
                            queryLineMap.removeLayer(global_query_markers[i]);
                        }
                        global_query_markers = [];
                    }
                    for(var j=0;j<len;j++) {
                        var stopLocation = stops[j].location;
                        var stationId = stops[j].id
                        var stationName = stops[j].name
                        var stationLoc = stops[j].location
                        var stationSequence = stops[j].sequence
                        global_query_markers[j] = L.marker(stopLocation)
                            .addTo(queryLineMap)
                            .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                            .openPopup();
                    }
                    //跳转至线路查询层
                    $("div.leaflet-control-layers-base").children().eq(1).children().eq(0).children().eq(0).click()
                },
                error:function(){
                    alert("系统异常！！")
                }
            })
        }

		// //首页公交线路查询-实现输入线路名称展示在线路展示层的异步查询，用两个颜色表示一条线路的a-b和b-a
		// function queryLine() {
		// 	var $queryLineName = $("#queryLineName").val();
		// 	$.ajax({
		// 		url:"LineQueryServlet",
		// 		data:"queryLineName="+$queryLineName,
		// 		dataType:"json",
		// 		success:function (result) {
		// 			if((result!=null) && (result!="false")){
		// 				var arrayData =eval(result);
		// 				var style1 = {//查询线路展示的风格
		// 					"color": "#FF00FF",
		// 					"weight": 3,
		// 					"opacity": 0.9
		// 				};
        //                 var style2 = {//查询线路展示的风格
        //                     "color": "#4876FF",
        //                     "weight": 3,
        //                     "opacity": 0.9
        //                 };
        //                 //var queryLayer = L.geoJSON().addTo(queryLineMap);
        //                 if(global_query_line!=null){
        //                     queryLineMap.removeLayer(global_query_line);
        //                 }
		// 				for(var i=0;i<arrayData.length;i++) {
		// 					// console.log(arrayData[i])
		// 					// queryLayer.addData([arrayData[i]]);
		// 					if(i%2==0) {
        //                         global_query_line = L.geoJSON([arrayData[i]], {
        //                             style: style1
        //                         }).addTo(queryLineMap);
        //                     }else{
        //                         global_query_line = L.geoJSON([arrayData[i]], {
        //                                 style: style2
        //                             }).addTo(queryLineMap);
        //                     }
		// 					//使其直接跳转到线路查询层
        //                     $("div.leaflet-control-layers-base").children().eq(1).children().eq(0).children().eq(0).click()
		// 				}
		// 			}
		// 			else{
		// 				alert("该线路不存在，请重新输入！！")
		// 			}
		// 		},
		// 		error:function(){
		// 			alert("系统异常！！")
		// 		}
		// 	})
		// }
        //
        // //线路站点显示 首页
        // function queryLineOfStops() {
        //     var $queryLineName = $("#queryLineName").val();
        //     $.ajax({
        //         url:"LineQueryLikeNameStopsListServlet",
        //         data:"queryLineName="+$queryLineName,
        //         dataType:"json",
        //         success:function (data) {
        //             var stopsList = data;
        //             var num = stopsList.length;//有几条公交就有多少
        //             if(global_query_markers.length>0){
        //                 for(var i=0;i<global_query_markers.length;i++){
        //                     queryLineMap.removeLayer(global_query_markers[i])
        //                 }
        //                 global_query_markers = [];
        //             }
        //             for(var i=0;i<num;i++)
        //             {
        //                 var stops= eval(stopsList[i]);
        //                 var len = stops.length;
        //                 for(var j=0;j<len;j++) {
        //                     var stopLocation = stops[j].location;
        //                     var stationId = stops[j].id;
        //                     var stationName = stops[j].name;
        //                     var stationLoc = stops[j].location;
        //                     var stationSequence = stops[j].sequence;
        //                     global_query_markers[i] = L.marker(stopLocation)
        //                         .addTo(queryLineMap)
        //                         .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
        //                         .openPopup();
        //                 }
        //             }
        //             //跳转至线路查询层
        //             $("div.leaflet-control-layers-base").children().eq(1).children().eq(0).children().eq(0).click()
        //         },
        //         error:function(){
        //             alert("系统异常！！")
        //         }
        //     })
        // }

        //站点覆盖率查询 按钮的实现
        var global_coverage_markers =[];
		function stopCoverage() {
			$.ajax({
				url:"StationServlet",
				dataType:"json",
				success:function (data) {
					var arraydata = eval(data);
					var len = arraydata.length;
					if(global_coverage_markers.length>0){
					    for(var i=0;i<global_coverage_markers.length;i++){
					        stationCoverageMap.removeLayer(global_coverage_markers[i])
                        }
					    global_coverage_markers = [];
                    }
					for(var i=0;i<len;i++)
					{
						var stopLocation = eval(arraydata[i].location);
						global_coverage_markers[i] = L.circle(stopLocation, {
							color: '#D1EEEE',
							weight: 0,
							fillColor: '#9932CC',
							radius: 1500,
							fillOpacity: 0.1
						}).addTo(stationCoverageMap)
					}
					//跳转至站点覆盖层
                    $("div.leaflet-control-layers-base").children().eq(2).children().eq(0).children().eq(0).click()
				},
				error:function(){
					alert("系统异常！！")
				}
			})
		}

        var global_show_markers =[];
		//所有有效站点显示 按钮的实现
        function stopShow() {
            $.ajax({
                url:"StationServlet",
                dataType:"json",
                success:function (data) {
                    var arraydata = eval(data);
                    var len = arraydata.length;
                    if(global_show_markers.length>0){
                        for(var i=0;i<global_show_markers.length;i++){
                            stationMap.removeLayer(global_show_markers[i])
                        }
                        global_show_markers = [];
                    }
                    for(var i=0;i<len;i++)
                    {
                        var stopLocation = eval(arraydata[i].location);
                        var stationName = arraydata[i].stationName
                        var stationLoc = arraydata[i].location
                        global_show_markers[i] = L.circle(stopLocation, {
                            color: '#D1EEEE',
                            weight: 0,
                            fillColor: '#FF0000',
                            radius: 50,
                            fillOpacity: 1
                        }).addTo(stationMap).bindPopup(stationName+" "+stationLoc).openPopup();
                        // global_show_markers[i] = L.marker(stopLocation)
                        //     .addTo(stationMap)
                        //     .bindPopup(stationName+" "+stationLoc)
                        //     .openPopup();
                    }
                    //跳转至站点信息层
                    $("div.leaflet-control-layers-base").children().eq(3).children().eq(0).children().eq(0).click()
                },
                error:function(){
                    alert("系统异常！！")
                }
            })
        }

        var global_normal_lines = [];
        var global_normal_markers = [];
        //所有有效公交线路显示
        function lineShow() {
            $.ajax({
                url: "AllValidLinesServlet",
                dataType: "json",
                success: function (result) {
                    var linesData = eval(result);//Object形式
                    console.log(linesData)
                    var style = {//查询线路展示的风格
                        "color": "#1E90FF",
                        "weight": 3,
                        "opacity": 0.85
                    };
                    if (global_normal_lines.length>0) {
                        for(var i=0;i<global_normal_lines.length;i++){
                            normal2.removeLayer(global_normal_lines[i]);
                        }
                    }
                    for(var j=0;j<linesData.length;j++){
                        var line = JSON.parse(linesData[j].coordinates); //字符串转对象
                        //console.log(linesData[j].coordinates);
                        global_normal_lines[j] = L.geoJSON([line], {
                            style: myStyle
                        }).addTo(normal2).bindPopup(linesData[j].lineName);
                    }
                    //跳转至有效线路展示层
                    $("div.leaflet-control-layers-base").children().eq(7).children().eq(0).children().eq(0).click()
                },
                error: function () {
                    alert("系统异常！！")
                }
            })
        }


        //使其加载首页的时候就能显示该列表
        $(function () {
            $.ajax({
                type: 'post',
                url: "QueryLineByStatusServlet",
                data:"status="+"1",
                dataType: "json",
                //data: {pid: 0},
                success: function (data) {
                    $("#queryLineID").empty();
                    $("#queryLineID").append("<option value=''>请选择一条在运营公交线路</option>");
                    for (var i = 0; i < data.length; i++) {
                        $("#queryLineID").append('<option value=' + data[i].id + '>' + data[i].lineName + '</option>');
                    }
                }
            });
        })
        //在运营公交线路下拉列表查询
        function status1List() {
            $(function () {
                $.ajax({
                    type: 'post',
                    url: "QueryLineByStatusServlet",
                    data:"status="+"1",
                    dataType: "json",
                    //data: {pid: 0},
                    success: function (data) {
                        $("#queryLineID").empty();
                        $("#queryLineID").append("<option value=''>请选择一条在运营公交线路</option>");
                        for (var i = 0; i < data.length; i++) {
                            $("#queryLineID").append('<option value=' + data[i].id + '>' + data[i].lineName + '</option>');
                        }
                    }
                });
            })
        }



        // 线路投入运营控制js
        //完成新建线路下拉列表
        function FinishConstructList() {
            $(function () {
                $.ajax({
                    type: 'post',
                    url: "QueryLineByStatusServlet",
                    data:"status="+"3",
                    dataType: "json",
                    //data: {pid: 0},
                    success: function (data) {
                        $("#finishConstructLineID").empty();
                        $("#finishConstructLineID").append("<option value=''>待投入运营公交线路列表</option>");
                        for (var i = 0; i < data.length; i++) {
                            $("#finishConstructLineID").append('<option value=' + data[i].id + '>' + data[i].lineName + '</option>');
                        }
                    }
                });
                $(function () {
                    $.ajax({
                        type: 'post',
                        url: "QueryLineByStatusServlet",
                        data:"status="+"5",
                        dataType: "json",
                        //data: {pid: 0},
                        success: function (data) {
                            for (var i = 0; i < data.length; i++) {
                                $("#finishConstructLineID").append('<option value=' + data[i].id + '>' + data[i].lineName + '</option>');
                            }
                        }
                    });
                })
            })
        }

        //线路投入运营按钮
        function correctLineStatus() {
            document.getElementById("form").style.display='none';
            document.getElementById("correctLineStatus").style.display='block';
            FinishConstructList();
            // FinishCorrectList();
        }

        //线路投入运营 -查看该线路及站点
        function getLineByIDtoShow() {
            var $finishConstructLineID = $("#finishConstructLineID").val();
            //var $finishCorrectLineID = $("#finishCorrectLineID").val();
            // var queryLineID = "" ;
            // if($finishConstructLineID != "" && $finishCorrectLineID == "" ){
            //     queryLineID = $finishConstructLineID;
            // }else if($finishConstructLineID == "" && $finishCorrectLineID != ""){
            //     queryLineID = $finishCorrectLineID;
            // }else if($finishConstructLineID == "" && $finishCorrectLineID == ""){
            //     alert("请从一个下拉框中选择想要查询的线路")
            // }else if($finishConstructLineID != "" && $finishCorrectLineID != ""){
            //     alert("只能从一个下拉框中选择想要查询的线路！！")
            // }
            var queryLineID = $finishConstructLineID;
            //console.log(queryLineID);
            if(queryLineID != "") {
                $.ajax({
                    url: "LineQueryByIDServlet",
                    data: "queryLineID=" + queryLineID,
                    dataType: "json",
                    success: function (result) {
                        var lineData = eval(result);//Object形式
                        var line = JSON.parse(lineData.coordinates) //字符串转对象
                        var style = {//查询线路展示的风格
                            "color": "#ff0000",
                            "weight": 2,
                            "opacity": 0.55
                        };
                        if (global_query_line != null) {
                            queryLineMap.removeLayer(global_query_line);
                        }
                        global_query_line = L.geoJSON([line], {
                            style: style
                        }).addTo(queryLineMap);
                        //显示站点
                        var stops = JSON.parse(lineData.stops)
                        var len = stops.length;
                        if (global_query_markers.length > 0) {
                            for (var i = 0; i < global_query_markers.length; i++) {
                                queryLineMap.removeLayer(global_query_markers[i]);
                            }
                            global_query_markers = [];
                        }
                        for (var j = 0; j < len; j++) {
                            var stopLocation = stops[j].location;
                            var stationId = stops[j].id
                            var stationName = stops[j].name
                            var stationLoc = stops[j].location
                            var stationSequence = stops[j].sequence
                            global_query_markers[j] = L.marker(stopLocation)
                                .addTo(queryLineMap)
                                .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                                .openPopup();
                        }
                        //跳转至线路查询层
                        $("div.leaflet-control-layers-base").children().eq(1).children().eq(0).children().eq(0).click()
                    },
                    error: function () {
                        alert("系统异常！！")
                    }
                })
            }else{
                alert("请从下拉列表中选择一条线路！")
            }
        }

        //线路投入运营
        function busOnLine() {
            var $finishConstructLineID = $("#finishConstructLineID").val();
            // var $finishCorrectLineID = $("#finishCorrectLineID").val();
            // var queryLineID = "" ;
            // if($finishConstructLineID != "" && $finishCorrectLineID == "" ){
            //     queryLineID = $finishConstructLineID;
            // }else if($finishConstructLineID == "" && $finishCorrectLineID != ""){
            //     queryLineID = $finishCorrectLineID;
            // }else if($finishConstructLineID == "" && $finishCorrectLineID == ""){
            //     alert("请从一个下拉框中选择想要查询的线路")
            // }else if($finishConstructLineID != "" && $finishCorrectLineID != ""){
            //     alert("只能从一个下拉框中选择想要查询的线路！！")
            // }
            var queryLineID = $finishConstructLineID;
            //console.log(queryLineID);
            if(queryLineID != "") {
                $.ajax({
                    url: "BusOnLineServlet",
                    data: "queryLineID=" + queryLineID,
                    dataType: "json",
                    success: function (result) {
                        var lineData = eval(result);//Object形式
                        var line = JSON.parse(lineData.coordinates) //字符串转对象
                        var style = {//查询线路展示的风格
                            "color": "#ff0000",
                            "weight": 2,
                            "opacity": 0.55
                        };
                        if (global_query_line != null) {
                            queryLineMap.removeLayer(global_query_line);
                        }
                        global_query_line = L.geoJSON([line], {
                            style: style
                        }).addTo(queryLineMap);
                        //显示站点
                        var stops = JSON.parse(lineData.stops)
                        var len = stops.length;
                        if (global_query_markers.length > 0) {
                            for (var i = 0; i < global_query_markers.length; i++) {
                                queryLineMap.removeLayer(global_query_markers[i]);
                            }
                            global_query_markers = [];
                        }
                        for (var j = 0; j < len; j++) {
                            var stopLocation = stops[j].location;
                            var stationId = stops[j].id
                            var stationName = stops[j].name
                            var stationLoc = stops[j].location
                            var stationSequence = stops[j].sequence
                            global_query_markers[j] = L.marker(stopLocation)
                                .addTo(queryLineMap)
                                .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                                .openPopup();
                        }
                        //FinishCorrectList();
                        FinishConstructList();
                        //跳转至线路查询层
                        $("div.leaflet-control-layers-base").children().eq(1).children().eq(0).children().eq(0).click()
                        alert("该线路已经投入运营！！")
                    },
                    error: function () {
                        alert("系统异常！！")
                    }
                })
            }else{
                alert("请从下拉列表中选择一条线路！！")
            }
        }

        //线路投入运营-返回
        function busOnLineBack() {
            document.getElementById('correctLineStatus').style.display='none';
            document.getElementById('form').style.display='block';
            status1List();
        }




        <%--    控制删除线路的弹出框等--%>
        //删除线路 按钮的界面显示
        function deleteLine() {
            document.getElementById('deleteLine').style.display='block';
            document.getElementById('form').style.display='none';
            deleteList();
        }
        //删除公交查询的下拉列表
        function deleteList() {
            $(function () {
                $.ajax({
                    type: 'post',
                    url: "QueryAllLinesServlet",
                    dataType: "json",
                    //data: {pid: 0},
                    success: function (data) {
                        $("#deleteID").empty();
                        $("#deleteID").append("<option value=''>请选择需要删除的公交线路</option>");
                        for (var i = 0; i < data.length; i++) {
                            $("#deleteID").append('<option value=' + data[i].id + '>' + data[i].lineName + '</option>');
                        }
                    }
                });
            })
        }

        //删除该线路 -查看该线路及站点
        function queryLineByID() {
            var $deleteID = $("#deleteID").val();
            $.ajax({
                url:"LineQueryByIDServlet",
                data:"queryLineID="+$deleteID,
                dataType:"json",
                success:function (result) {
                    var lineData = eval(result) ;//Object形式
                    var line = JSON.parse(lineData.coordinates) //字符串转对象
                    var style = {//查询线路展示的风格
                        "color": "#ff0000",
                        "weight": 2,
                        "opacity": 0.55
                    };
                    if(global_query_line!=null){
                        queryLineMap.removeLayer(global_query_line);
                    }
                    global_query_line = L.geoJSON([line], {
                        style: style
                    }).addTo(queryLineMap);
                    //显示站点
                    var stops= JSON.parse(lineData.stops)
                    var len = stops.length;
                    if(global_query_markers.length>0){
                        for(var i=0;i<global_query_markers.length;i++){
                            queryLineMap.removeLayer(global_query_markers[i]);
                        }
                        global_query_markers = [];
                    }
                    for(var j=0;j<len;j++) {
                        var stopLocation = stops[j].location;
                        var stationId = stops[j].id
                        var stationName = stops[j].name
                        var stationLoc = stops[j].location
                        var stationSequence = stops[j].sequence
                        global_query_markers[j] = L.marker(stopLocation)
                            .addTo(queryLineMap)
                            .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                            .openPopup();
                    }
                    //跳转至线路查询层
                    $("div.leaflet-control-layers-base").children().eq(1).children().eq(0).children().eq(0).click()
                },
                error:function(){
                    alert("系统异常！！")
                }
            })
        }

        //删除线路 根据下拉列表传递的ID进行删除
        function deleteLineByID() {
            var $deleteID = $("#deleteID").val();
            $.ajax({
                url:"DeleteLineByIdServlet",
                data:"id="+$deleteID,
                dataType:"json",
                success:function (result) {
                    if(result=="1"){
                        alert("该线路删除成功！！")
                        deleteList();
                        if(global_query_line!=null){
                            queryLineMap.removeLayer(global_query_line);
                        }
                        if(global_query_markers.length > 0){
                            for(var i = 0;i<global_query_markers.length;i++){
                                queryLineMap.removeLayer(global_query_markers[i])
                            }
                            global_query_markers = [];
                        }
                    }else if(result=="0"){
                        alert("该线路删除失败")
                    }
                    //跳转至线路查询层
                    $("div.leaflet-control-layers-base").children().eq(1).children().eq(0).children().eq(0).click()
                },
                error:function(){
                    alert("系统异常！！")
                }
            })
        }

        //删除线路-返回
        function deleteBack() {
            document.getElementById('deleteLine').style.display='none';
            document.getElementById('form').style.display='block';
            status1List();
        }

	</script>


<%--    控制居民区覆盖率展示--%>
    <script type="text/javascript">
        //控制居民区站点覆盖率页面跳转
        function neighborCoverage() {
            document.getElementById('neighborCoverage').style.display='block';
            document.getElementById('form').style.display='none';
            neighborList();
            $("#measure").val("");
        }

        //居民区站点覆盖率查询-返回
        function backToForm() {
            document.getElementById('neighborCoverage').style.display='none';
            document.getElementById('form').style.display='block';
        }

        //居民区下拉列表
        function neighborList() {
            $(function () {
                $.ajax({
                    url: "AllNeighborServlet",
                    dataType: "json",
                    //data: {pid: 0},
                    success: function (data) {
                        $("#neighborID").empty();
                        $("#neighborID").append("<option value=''>请选择一个居民区</option>");
                        for (var i = 0; i < data.length; i++) {
                            $("#neighborID").append('<option value=' + data[i].id + '>' + data[i].neighborhoodName + '</option>');
                        }
                    }
                });
            })
        }

        var global_neigh_circle=[];
        //查询x米内居民区附近拥有的站点数量覆盖率图
        function neighbourCoverageQuery() {
            var $measure = $("#measure").val();
            $.ajax({
                url:"NeighbourCoverageServlet",
                data:"measure="+$measure,
                dataType:"json",
                success:function (data) {
                    var obj = eval(data);
                    var len = obj.length;
                    if(global_neigh_circle.length>0){
                        for(var i=0;i<global_neigh_circle.length;i++){
                            neighbourCoverageMap.removeLayer(global_neigh_circle[i])
                        }
                        global_neigh_circle = [];
                    }
                    for(var i=0;i<len;i++)
                    {
                        var neighbourLoc = obj[i].location;
                        neighbourLoc = "[" + neighbourLoc + "]";
                        var loc = eval(neighbourLoc);
                        if(obj[i].number==0){
                            global_neigh_circle[i] = L.circle(loc, {
                                color: '#D1EEEE',
                                weight: 0,
                                fillColor: '#FF3030',
                                radius: $measure,
                                fillOpacity: 0.08
                            }).addTo(neighbourCoverageMap)
                        }
                        else if(obj[i].number<=2){
                            global_neigh_circle[i] = L.circle(loc, {
                                color: '#D1EEEE',
                                weight: 0,
                                fillColor: '#FF3030',
                                radius: $measure,
                                fillOpacity: 0.1
                            }).addTo(neighbourCoverageMap)
                        }else if(obj[i].number>=3 && obj[i].number<=5){
                            global_neigh_circle[i] = L.circle(loc, {
                                color: '#D1EEEE',
                                weight: 0,
                                fillColor: '#FF3030',
                                radius: $measure,
                                fillOpacity: 0.15
                            }).addTo(neighbourCoverageMap)
                        }else if(obj[i].number>5){
                            global_neigh_circle[i] = L.circle(loc, {
                                color: '#D1EEEE',
                                weight: 0,
                                fillColor: '#FF3030',
                                radius: $measure,
                                fillOpacity: 0.2
                            }).addTo(neighbourCoverageMap)
                        }
                    }
                    //跳转至居民区覆盖率层
                    $("div.leaflet-control-layers-base").children().eq(4).children().eq(0).children().eq(0).click()
                    //alert("【绿色及蓝色】表示站点数小于等于两个！！【红色及橙色】表示站点数大于等于三个！！")
                },
                error:function(){
                    alert("系统异常！！")
                }
            })
        }


        function neighbourCoverageByID() {
            var $measure = $("#measure").val();
            var $neighborID = $("#neighborID").val();
            if($measure == ""){
                alert("请输入查询半径！")
            }
            if($neighborID == ""){
                alert("请从下拉列表中选择一个居民区！！")
            }
            if($measure!="" && $neighborID!=""){
                console.log("半径："+$measure +",居民区ID："+ $neighborID)
                var obj = JSON.stringify({
                    'measure':$measure,
                    'id': $neighborID,
                });
                $.ajax({
                    url:"NeighbourCoverageByID",
                    data:"obj="+obj,
                    type:"post",
                    dataType:"json",
                    success:function (data) {
                        var result = eval(data);
                        if(global_neigh_circle.length>0){
                            for(var i=0;i<global_neigh_circle.length;i++){
                                neighbourCoverageMap.removeLayer(global_neigh_circle[i])
                            }
                            global_neigh_circle = [];
                        }

                        var neighbourLoc = result.location;
                        neighbourLoc = "[" + neighbourLoc + "]";
                        var loc = eval(neighbourLoc);
                        if(result.number==0){
                            global_neigh_circle[0] = L.circle(loc, {
                                color: '#D1EEEE',
                                weight: 0,
                                fillColor: '#FF3030',
                                radius: $measure,
                                fillOpacity: 0.1
                            }).addTo(neighbourCoverageMap).bindPopup("居民区："+result.neighborhoodName+" "+$measure+"米 范围内站点个数："+result.number)
                        }
                        else if(result.number<=2){
                            global_neigh_circle[0] = L.circle(loc, {
                                color: '#D1EEEE',
                                weight: 0,
                                fillColor: '#FF3030',
                                radius: $measure,
                                fillOpacity: 0.3
                            }).addTo(neighbourCoverageMap).bindPopup("居民区："+result.neighborhoodName+" "+$measure+"米 范围内站点个数："+result.number)
                        }else if(result.number>=3 && result.number<=5){
                            global_neigh_circle[0] = L.circle(loc, {
                                color: '#D1EEEE',
                                weight: 0,
                                fillColor: '#FF3030',
                                radius: $measure,
                                fillOpacity: 0.5
                            }).addTo(neighbourCoverageMap).bindPopup("居民区："+result.neighborhoodName+" "+$measure+"米 范围内站点个数："+result.number)
                        }else if(result.number>5){
                            global_neigh_circle[0] = L.circle(loc, {
                                color: '#D1EEEE',
                                weight: 0,
                                fillColor: '#FF3030',
                                radius: $measure,
                                fillOpacity: 0.7
                            }).addTo(neighbourCoverageMap).bindPopup("居民区："+result.neighborhoodName+" "+$measure+"米 范围内站点个数："+result.number)
                        }
                        global_neigh_circle[1] = L.circle(loc, {
                            color: '#D1EEEE',
                            weight: 0,
                            fillColor: '#FF3030',
                            radius: 20,
                            fillOpacity: 1
                        }).addTo(neighbourCoverageMap)

                        //跳转至居民区覆盖率层
                        $("div.leaflet-control-layers-base").children().eq(4).children().eq(0).children().eq(0).click()
                        //alert("【绿色及蓝色】表示站点数小于等于两个！！【红色及橙色】表示站点数大于等于三个！！")
                    },
                    error:function(){
                        alert("系统异常！！")
                    }
                })

            }
        }
    </script>

<%--    控制新建线路弹出框等--%>
    <script type="text/javascript">

        var global_stopAdd_circles = [];
        //显示所有站点于新增页面ccc
        function stopAdd() {
            $.ajax({
                url:"StationServlet",
                dataType:"json",
                success:function (data) {
                    var arraydata = eval(data);
                    var len = arraydata.length;
                    if(global_stopAdd_circles.length>0){
                        for(var i=0;i<global_stopAdd_circles.length;i++){
                            underConstructionMap.removeLayer(global_stopAdd_circles[i])
                        }
                        global_stopAdd_circles = [];
                    }
                    for(var i=0;i<len;i++)
                    {
                        var stopLocation = eval(arraydata[i].location);
                        var stationName = arraydata[i].stationName
                        var stationLoc = arraydata[i].location
                        global_stopAdd_circles[i] = L.circle(stopLocation, {
                            color: '#D1EEEE',
                            weight: 0,
                            fillColor: '#FF0000',
                            radius: 30,
                            fillOpacity: 0.7
                        }).addTo(underConstructionMap).bindPopup(stationName+" "+stationLoc).openPopup();
                    }
                    //跳转至站点信息层
                    //$("div.leaflet-control-layers-base").children().eq(5).children().eq(0).children().eq(0).click()
                },
                error:function(){
                    alert("系统异常！！")
                }
            })
        }


        var global_add_line;
        var global_add_markers = [];
        //添加线路
        function newLine() {
            document.getElementById('addLine').style.display='block';
            document.getElementById('startNew').style.display='none';
            document.getElementById('form').style.display='none';
            document.getElementById('startMoreNew').style.display='none';
            document.getElementById('continueNew').style.display='none';
            underList();
        }

        //添加线路 返回
        function addLineBack() {
            document.getElementById('startNew').style.display='none';
            document.getElementById('addLine').style.display='none';
            document.getElementById('form').style.display='block';
            document.getElementById('startMoreNew').style.display='none';
        }

        //添加线路-开始-返回
        function addBack() {
            document.getElementById('startNew').style.display='none';
            document.getElementById('addLine').style.display='block';
            document.getElementById('form').style.display='none';
            document.getElementById('continueNew').style.display='none';
            document.getElementById('startMoreNew').style.display='none';
            document.getElementById('addCoord').style.display='none';
            document.getElementById('continueAddCoord').style.display='none';
            document.getElementById('addStop').style.display='none';
            underList();
            $("#newLineName").val("");
        }

        //添加线路 开始
        function startNew() {
            document.getElementById('startNew').style.display='block';
            document.getElementById('addLine').style.display='none';
            document.getElementById('form').style.display='none';
            document.getElementById('continueNew').style.display='none';
            document.getElementById('addCoord').style.display='none';
            document.getElementById('continueAddCoord').style.display='none';
            document.getElementById('addStop').style.display='none';
            document.getElementById('startMoreNew').style.display='none';
            if(global_add_line!=null){
                underConstructionMap.removeLayer(global_add_line);
            }
            if(global_add_markers.length>0){
                for(var i=0;i<global_add_markers.length;i++){
                    underConstructionMap.removeLayer(global_add_markers[i]);
                }
                global_add_markers = [];
            }
        }

        //添加线路 开始-保存并继续后的页面
        function startMoreNew() {
            document.getElementById('startNew').style.display='none';
            document.getElementById('addLine').style.display='none';
            document.getElementById('form').style.display='none';
            document.getElementById('continueNew').style.display='none';
            document.getElementById('addCoord').style.display='none';
            document.getElementById('continueAddCoord').style.display='none';
            document.getElementById('addStop').style.display='none';
            document.getElementById('startMoreNew').style.display='block';
        }

        //添加线路 继续
        function continueNew() {
            document.getElementById('continueNew').style.display='block';
            document.getElementById('addLine').style.display='none';
            document.getElementById('form').style.display='none';
            document.getElementById('startNew').style.display='none';
            document.getElementById('addCoord').style.display='none';
            document.getElementById('continueAddCoord').style.display='none';
            document.getElementById('addStop').style.display='none';
            document.getElementById('startMoreNew').style.display='none';
            var $continueAddLineName=$('#continueAddLineName').val();
            // showStop();
            // alert("可在【站点信息层】查看所有有效站点");
            stopAdd();
            //根据在建列表的选择，显示正在建设中的线路
            $.ajax({
                url:"LineQueryByNameServlet",
                data:"lineName="+$continueAddLineName,
                dataType:"json",
                success:function (result) {
                    var lineData = eval(result) ;//Object形式
                    var line = JSON.parse(lineData.coordinates) //字符串转对象
                    var style = {//查询线路展示的风格
                        "color": "#ff0000",
                        "weight": 2,
                        "opacity": 0.55
                    };
                    if(global_add_line!=null){
                        underConstructionMap.removeLayer(global_add_line);
                    }
                    global_add_line = L.geoJSON([line], {
                        style: style
                    }).addTo(underConstructionMap);

                    //显示站点
                    var stops= JSON.parse(lineData.stops);
                    var len = stops.length;
                    if(global_add_markers.length > 0){
                        for(var i = 0;i < global_add_markers.length; i++ ){
                            underConstructionMap.removeLayer(global_add_markers[i]);
                        }
                        global_add_markers = [];
                    }
                    for(var j=0;j<len;j++) {
                        var stopLocation = stops[j].location;
                        var stationId = stops[j].id;
                        var stationName = stops[j].name;
                        var stationLoc = stops[j].location;
                        var stationSequence = stops[j].sequence;
                        global_add_markers[j] = L.marker(stopLocation)
                            .addTo(underConstructionMap)
                            .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                            .openPopup();
                    }
                    //跳转至在建线路展示层
                    $("div.leaflet-control-layers-base").children().eq(5).children().eq(0).children().eq(0).click()
                },
                error:function(){
                    alert("系统异常！！")
                }
            })
        }

        //添加线路-开始-添加拐点至线路
        function addCoord() {
            document.getElementById('addCoord').style.display='block';
            document.getElementById('continueAddCoord').style.display='none';
            document.getElementById('continueNew').style.display='none';
            document.getElementById('addLine').style.display='none';
            document.getElementById('form').style.display='none';
            document.getElementById('startNew').style.display='none';
            document.getElementById('startMoreNew').style.display='none';
            $('#continueAddCoordLoc').val("")
            $('#addStationLocation').val("")
            $('#addCoordLoc').val("")
            $('#correctCoordLoc').val("")
            $('#correctStationLocation').val("")
        }

        //添加线路 继续 添加拐点至线路
        function continueAddCoord() {
            document.getElementById('addCoord').style.display='none';
            document.getElementById('continueAddCoord').style.display='block';
            document.getElementById('continueNew').style.display='none';
            document.getElementById('addLine').style.display='none';
            document.getElementById('form').style.display='none';
            document.getElementById('startNew').style.display='none';
            $('#continueAddCoordLoc').val("")
            $('#addStationLocation').val("")
            $('#addCoordLoc').val("")
            $('#correctCoordLoc').val("")
            $('#correctStationLocation').val("")
        }

        //添加线路-开始 添加站点 展示所有有效站点+对输入的车站名进行保存，新增数据库
        function addStop(){
            var $newLineName = $("#newLineName").val();
            $.ajax({
                url:"AddStopStartServlet",
                data:"newLineName="+$newLineName,
                dataType:"json",
                success:function (data) {
                    if(data=="0"){
                        alert("新增线路失败！！")
                    }else if(data=="2"){
                        alert("该线路名已经存在！！请在在建列表中查看或是修改线路名")
                    }else {
                        alert("开始新建该线路！！")
                        document.getElementById('addStop').style.display='block';
                        document.getElementById('addLine').style.display='none';
                        document.getElementById('startNew').style.display='none';
                        document.getElementById('form').style.display='none';
                        $('#continueAddCoordLoc').val("")
                        $('#addStationLocation').val("")
                        $('#addCoordLoc').val("")
                        $('#correctCoordLoc').val("")
                        $('#correctStationLocation').val("")
                        stopAdd();
                        //跳转至在建线路层
                        $("div.leaflet-control-layers-base").children().eq(5).children().eq(0).children().eq(0).click()
                    }
                },
                error:function(){
                    alert("系统异常！！")
                }
            })
        }

        //添加线路-开始-保存并继续-添加站点至线路 与addStop区别：无需区分该线路是否存在
        function addMoreStop(){
            document.getElementById('addStop').style.display='block';
            document.getElementById('addCoord').style.display='none';
            document.getElementById('continueAddCoord').style.display='none';
            document.getElementById('continueNew').style.display='none';
            document.getElementById('addLine').style.display='none';
            document.getElementById('form').style.display='none';
            document.getElementById('startNew').style.display='none';
            document.getElementById('startMoreNew').style.display='none';
            $('#continueAddCoordLoc').val("")
            $('#addStationLocation').val("")
            $('#addCoordLoc').val("")
            $('#correctCoordLoc').val("")
            $('#correctStationLocation').val("")
        }

        //添加线路-开始-添加拐点至线路-保存该拐点
        function saveCoord(){
            var $newLineName = $("#newLineName").val();
            var $addCoordLoc = $("#addCoordLoc").val(); //x,y
            var $stopSequence = $("#stopSequence").val(); //拐点添加至该站点之后
            var obj = JSON.stringify({
                'lineName':$newLineName,
                'loc': $addCoordLoc,
                'sequence': $stopSequence
            });
            //console.log(obj);
            $.ajax({
                url:"AddCoordServlet",
                data:"obj="+ obj,
                type: "post",
                dataType:"json",
                success:function (result,testStatus) {
                    var lineData = eval(result) ;//Object形式
                    var line = JSON.parse(lineData.coordinates) //字符串转对象
                    var style = {//查询线路展示的风格
                        "color": "#ff0000",
                        "weight": 2,
                        "opacity": 0.55
                    };
                    if(global_add_line!=null){//清空之前图层中所有的线路
                        underConstructionMap.removeLayer(global_add_line)
                    }
                    global_add_line = L.geoJSON([line], { style: style}).addTo(underConstructionMap);

                    //显示站点
                    var stops= JSON.parse(lineData.stops)
                    var len = stops.length;
                    if(global_add_markers.length>0){
                        for(var i = 0;i<global_add_markers.length;i++){
                            underConstructionMap.removeLayer(global_add_markers[i]);
                        }
                        global_add_markers = [];
                    }
                    for(var j=0;j<len;j++) {
                        var stopLocation = stops[j].location;
                        var stationId = stops[j].id
                        var stationName = stops[j].name
                        var stationLoc = stops[j].location
                        var stationSequence = stops[j].sequence
                        global_add_markers[j] = L.marker(stopLocation)
                            .addTo(underConstructionMap)
                            .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                            .openPopup();
                    }
                    startMoreNew();
                    //跳转至在建线路展示层
                    $("div.leaflet-control-layers-base").children().eq(5).children().eq(0).children().eq(0).click()
                    alert("拐点已添加至线路")
                    $("#addCoordLoc").val("");
                    $("#stopSequence").val("");
                },
                error:function(xhr,errorMessage,e){
                    alert("系统异常！！")
                }
            })
        }


        //添加线路-继续-添加拐点至线路-保存该拐点
        function continueSaveCoord(){
            var $continueAddLineName = $('#continueAddLineName').val(); //在建线路名
            var $addCoordLoc = $('#continueAddCoordLoc').val();//x,y
            var $stopSequence = $('#continueStopSequence').val();//拐点添加至该站点之后
            var obj = JSON.stringify({
                'lineName':$continueAddLineName,
                'loc': $addCoordLoc,
                'sequence': $stopSequence
            });
            console.log(obj);
            $.ajax({
                url:"AddCoordServlet",
                data:"obj="+ obj,
                type: "post",
                dataType:"json",
                success:function (result) {
                    var lineData = eval(result) ;//Object形式
                    var line = JSON.parse(lineData.coordinates) //字符串转对象
                    var style = {//查询线路展示的风格
                        "color": "#ff0000",
                        "weight": 2,
                        "opacity": 0.55
                    };
                    if(global_add_line!=null){//清空之前图层中所有的线路
                        underConstructionMap.removeLayer(global_add_line)
                    }
                    global_add_line = L.geoJSON([line], { style: style}).addTo(underConstructionMap);

                    //显示站点
                    var stops= JSON.parse(lineData.stops)
                    var len = stops.length;
                    if(global_add_markers.length>0){
                        for(var i = 0;i<global_add_markers.length;i++){
                            underConstructionMap.removeLayer(global_add_markers[i]);
                        }
                        global_add_markers = [];
                    }
                    for(var j=0;j<len;j++) {
                        var stopLocation = stops[j].location;
                        var stationId = stops[j].id
                        var stationName = stops[j].name
                        var stationLoc = stops[j].location
                        var stationSequence = stops[j].sequence
                        global_add_markers[j] = L.marker(stopLocation)
                            .addTo(underConstructionMap)
                            .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                            .openPopup();
                    }
                    continueNew();
                    //跳转至在建线路展示层
                    $("div.leaflet-control-layers-base").children().eq(5).children().eq(0).children().eq(0).click()
                    alert("拐点已添加至线路");
                    $("#continueAddCoordLoc").val("");
                    $("#continueStopSequence").val("");

                },
                error:function(){
                    alert("系统异常！！");
                }
            })

        }


        //下拉在建中的线路列表
        function underList() {
            $(function () {
                $.ajax({
                    type: 'post',
                    url: "QueryUnderConstructLineServlet",
                    dataType: "json",
                    //data: {pid: 0},
                    success: function (data) {
                        // console.log(data);
                        $("#continueAddLineName").empty();
                        $("#continueAddLineName").append("<option value=''>请选择在建的公交线路</option>");
                        for (var i = 0; i < data.length; i++) {
                            $("#continueAddLineName").append('<option value=' + data[i].lineName + '>' + data[i].lineName + '</option>');
                        }
                    }
                });
            })
        }


        //添加线路-保存并继续 根据checkBox保存线路的站点 展示刚才保存的站点到线路 页面保持继续添加站点
        function saveAndContinue() {
            var $continueAddLineName=$('#continueAddLineName').val();
            var $newLineName=$("#newLineName").val();
            var $lineName = null;
            if($continueAddLineName==""){
                $lineName = $newLineName
                startMoreNew()//和startNew()的区别就是不用判断该线路是否存在
            }else if($newLineName==""){
                $lineName = $continueAddLineName
                continueNew()
            }
            var oldChecked = $('#old').is(":checked");
            var newChecked = $('#new').is(":checked");
            var $addStationName = $("#addStationName").val();
            var $addStationLocation = $("#addStationLocation").val();
            var $sequence = $("#sequence").val();

            if(oldChecked && (!newChecked)) { //如果选择的是原有站点
                var obj = JSON.stringify({
                    'type':"oldStop",
                    'lineName':$lineName,
                    'name': $addStationName,
                    'loc': $addStationLocation ,
                    'sequence': $sequence
                });
                //将原有站点的信息添加到线路的coord列、stops列，并将目前该线路的线路图展现在在建线路展示层中
                $.ajax({
                    url: "AddOldStopToLineServlet",
                    type: "post",
                    data: "obj="+ obj,
                    dataType: "json",
                    success:function (result,testStatus) {
                        var lineData = eval(result) ;//Object形式
                        var line = JSON.parse(lineData.coordinates) //字符串转对象
                        var style = {//查询线路展示的风格
                            "color": "#ff0000",
                            "weight": 2,
                            "opacity": 0.55
                        };
                        if(global_add_line!=null){//清空之前图层的线路
                            underConstructionMap.removeLayer(global_add_line);
                        }
                        global_add_line = L.geoJSON([line], {
                            style: style
                        }).addTo(underConstructionMap);

                        //显示站点
                        var stops= JSON.parse(lineData.stops)
                        if(global_add_markers.length>0){ //清空之前图层中留下的站点
                            for(var i = 0;i<global_add_markers.length;i++){
                                underConstructionMap.removeLayer(global_add_markers[i]);
                            }
                            global_add_markers = [];
                        }
                        var len = stops.length;
                        for(var j=0;j<len;j++) {
                            var stopLocation = stops[j].location;
                            var stationId = stops[j].id
                            var stationName = stops[j].name
                            var stationLoc = stops[j].location
                            var stationSequence = stops[j].sequence
                            global_add_markers[j] = L.marker(stopLocation)
                                .addTo(underConstructionMap)
                                .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                                .openPopup();
                            //console.log(stationName)
                        }
                        //跳转至在建线路展示层
                        $("div.leaflet-control-layers-base").children().eq(5).children().eq(0).children().eq(0).click()
                        alert("原有站点添加成功！请继续添加站点")
                        //清空
                        $("#addStationName").val("");
                        $("#addStationLocation").val("");
                        $("#sequence").val("");
                        $("#old").prop('checked',false);
                        $("#new").prop('checked',false);
                    },
                    error:function(){
                        alert("系统异常！！")
                    }
                })
            }else if(newChecked && (!oldChecked)){
                var obj = JSON.stringify({
                    'type':"newStop",
                    'lineName':$lineName,
                    'name': $addStationName,
                    'loc': $addStationLocation ,
                    'sequence': $sequence
                });
                //将新建的站点信息保存至station表，同时将新建的站点的信息添加到线路的coord列、stops列，并将目前该线路的线路图展现在在建线路展示层中
                $.ajax({
                    url: "AddNewStopToLineServlet",
                    type: "post",
                    data: "obj="+ obj,
                    dataType: "json",
                    success:function (result) {
                        var lineData = eval(result) ;//Object形式
                        var line = JSON.parse(lineData.coordinates) //字符串转对象
                        var style = {//查询线路展示的风格
                            "color": "#ff0000",
                            "weight": 2,
                            "opacity": 0.55
                        };
                        if(global_add_line!=null){//清空之前图层的线路
                            underConstructionMap.removeLayer(global_add_line);
                        }
                        global_add_line = L.geoJSON([line], {
                            style: style
                        }).addTo(underConstructionMap);

                        //显示站点
                        var stops= JSON.parse(lineData.stops)
                        var len = stops.length;
                        if(global_add_markers.length>0){ //清空之前图层中留下的站点
                            for(var i = 0;i<global_add_markers.length;i++){
                                underConstructionMap.removeLayer(global_add_markers[i]);
                            }
                            global_add_markers = [];
                        }
                        for(var j=0;j<len;j++) {
                            var stopLocation = stops[j].location;
                            var stationId = stops[j].id
                            var stationName = stops[j].name
                            var stationLoc = stops[j].location
                            var stationSequence = stops[j].sequence
                            global_add_markers[j] = L.marker(stopLocation)
                                .addTo(underConstructionMap)
                                .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                                .openPopup();
                            //console.log(stationName)
                        }
                        //跳转至在建线路展示层
                        $("div.leaflet-control-layers-base").children().eq(5).children().eq(0).children().eq(0).click()
                        alert("新站点添加成功！请继续添加站点")
                        $("#addStationName").val("");
                        $("#addStationLocation").val("");
                        $("#sequence").val("");
                        $("#old").prop('checked',false);
                        $("#new").prop('checked',false);
                    },
                    error:function(){
                        alert("系统异常！！")
                    }
                })
            }else{
                alert("请选择添加的站点是原有站点还是新站点！！")
                document.getElementById('addStop').style.display='block';
                document.getElementById('addLine').style.display='none';
                document.getElementById('form').style.display='none';
                document.getElementById('startNew').style.display='none';
                document.getElementById('startMoreNew').style.display = 'none';
                document.getElementById('continueNew').style.display = 'none';
            }
        }

        //添加线路 在建线路-继续 根据下拉选择在建线路的线路图 页面保持继续添加站点
        function continueLineByID() {
            var $continueAddLineName=$('#continueAddLineName').val();
            document.getElementById('addStop').style.display = 'block';
            document.getElementById('addLine').style.display = 'none';
            document.getElementById('form').style.display = 'none';
            document.getElementById('continueNew').style.display='none';
            $('#continueAddCoordLoc').val("")
            $('#addStationLocation').val("")
            $('#addCoordLoc').val("")
            $('#correctCoordLoc').val("")
            $('#correctStationLocation').val("")
            stopAdd();
            $.ajax({
                url:"LineQueryByNameServlet",
                data:"lineName="+$continueAddLineName,
                dataType:"json",
                success:function (result) {
                    var lineData = eval(result) ;//Object形式
                    console.log(lineData)
                    var line = JSON.parse(lineData.coordinates) //字符串转对象
                    console.log(line)
                    var style = {//查询线路展示的风格
                        "color": "#ff0000",
                        "weight": 2,
                        "opacity": 0.55
                    };
                    if(global_add_line!=null){
                        underConstructionMap.removeLayer(global_add_line);
                    }
                    global_add_line = L.geoJSON([line], {
                        style: style
                    }).addTo(underConstructionMap);

                    //显示站点
                    var stops= JSON.parse(lineData.stops)
                    var len = stops.length;
                    if(global_add_markers.length > 0){
                        for(var i = 0;i < global_add_markers.length; i++ ){
                            underConstructionMap.removeLayer(global_add_markers[i]);
                        }
                        global_add_markers = [];
                    }
                    for(var j=0;j<len;j++) {
                        var stopLocation = stops[j].location;
                        var stationId = stops[j].id
                        var stationName = stops[j].name
                        var stationLoc = stops[j].location
                        var stationSequence = stops[j].sequence
                        global_add_markers[j] = L.marker(stopLocation)
                            .addTo(underConstructionMap)
                            .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                            .openPopup();
                    }
                    //清空原有信息
                    $("#addStationName").val("");
                    $("#addStationLocation").val("");
                    $("#sequence").val("");
                    $("#old").prop('checked',false);
                    $("#new").prop('checked',false);
                    //跳转至线路查询层
                    $("div.leaflet-control-layers-base").children().eq(5).children().eq(0).children().eq(0).click()
                },
                error:function(){
                    alert("系统异常！！")
                }
            })
        }

        //新建-开始/继续-添加站点-返回
        function backToAdd() {
            var $continueAddLineName=$('#continueAddLineName').val();
            var $newLineName=$("#newLineName").val();
            if($continueAddLineName==""){
                startMoreNew()//和startNew()的区别就是不用判断该线路是否存在
            }else if($newLineName==""){
                continueNew()
            }
        }

        //完成新建 bbb
        function finishConstruct() {
            var $continueAddLineName=$('#continueAddLineName').val();
            var $newLineName=$("#newLineName").val();
            var lineName = "";
            if($continueAddLineName!="" && $newLineName==""){
                lineName = $continueAddLineName;
            }else if($newLineName!="" && $continueAddLineName==""){
                lineName = $newLineName
            }
            if(lineName != "") {
                $.ajax({
                    url: "FinishConstructServlet",
                    data: "lineName=" + lineName,
                    dataType: "json",
                    success: function (result) {
                        var lineData = eval(result);//Object形式
                        var line = JSON.parse(lineData.coordinates) //字符串转对象
                        var style = {//查询线路展示的风格
                            "color": "#ff0000",
                            "weight": 2,
                            "opacity": 0.55
                        };
                        if (global_query_line != null) {
                            queryLineMap.removeLayer(global_query_line);
                        }
                        global_query_line = L.geoJSON([line], {
                            style: style
                        }).addTo(queryLineMap);
                        //显示站点
                        var stops = JSON.parse(lineData.stops)
                        var len = stops.length;
                        if (global_query_markers.length > 0) {
                            for (var i = 0; i < global_query_markers.length; i++) {
                                queryLineMap.removeLayer(global_query_markers[i]);
                            }
                            global_query_markers = [];
                        }
                        for (var j = 0; j < len; j++) {
                            var stopLocation = stops[j].location;
                            var stationId = stops[j].id
                            var stationName = stops[j].name
                            var stationLoc = stops[j].location
                            var stationSequence = stops[j].sequence
                            global_query_markers[j] = L.marker(stopLocation)
                                .addTo(queryLineMap)
                                .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                                .openPopup();
                        }
                        underList();
                        //跳转至线路查询层
                        $("div.leaflet-control-layers-base").children().eq(1).children().eq(0).children().eq(0).click()
                        alert("该线路已【新建完毕】！！")
                        newLine()
                    },
                    error: function () {
                        alert("系统异常！！")
                    }
                })
            }

        }
    </script>

<%--    控制修改线路弹出框等--%>
    <script type="text/javascript">
        var global_correct_line =null;
        var global_correct_markers=[];
        var global_correct_coords = [];
        var global_tmp_stop_marker =  null;

        var global_stopCorrect_circles = [];
        //显示所有站点于修改页面ccc
        function stopCorrect() {
            $.ajax({
                url:"StationServlet",
                dataType:"json",
                success:function (data) {
                    var arraydata = eval(data);
                    var len = arraydata.length;
                    if(global_stopCorrect_circles.length>0){
                        for(var i=0;i<global_stopCorrect_circles.length;i++){
                            correctLineMap.removeLayer(global_stopCorrect_circles[i])
                        }
                        global_stopCorrect_circles = [];
                    }
                    for(var i=0;i<len;i++)
                    {
                        var stopLocation = eval(arraydata[i].location);
                        var stationName = arraydata[i].stationName
                        var stationLoc = arraydata[i].location
                        global_stopCorrect_circles[i] = L.circle(stopLocation, {
                            color: '#D1EEEE',
                            weight: 0,
                            fillColor: '#FF0000',
                            radius: 30,
                            fillOpacity: 0.7
                        }).addTo(correctLineMap).bindPopup(stationName+" "+stationLoc).openPopup();
                    }
                    //跳转至站点信息层
                    //$("div.leaflet-control-layers-base").children().eq(5).children().eq(0).children().eq(0).click()
                },
                error:function(){
                    alert("系统异常！！")
                }
            })
        }

        //修改线路
        function correctLine() {
            document.getElementById('correctLine').style.display='block';
            document.getElementById('form').style.display='none';
            finishList();
            //correctingList();
        }

        //修改线路-开始修改-增加拐点至线路
        function correctCoord() {
            document.getElementById('correctCoord').style.display='block';
            document.getElementById('correctLine').style.display='none';
            document.getElementById('form').style.display='none';
            document.getElementById('correct').style.display='none';
            document.getElementById('startCorrect').style.display='none';

        }

        function correct(){
            document.getElementById('correct').style.display='block';
            document.getElementById('correctLine').style.display='none';
            document.getElementById('form').style.display='none';
            document.getElementById('startCorrect').style.display='none';
            document.getElementById('correctCoord').style.display='none';
            $('#continueAddCoordLoc').val("")
            $('#addStationLocation').val("")
            $('#addCoordLoc').val("")
            $('#correctCoordLoc').val("")
            $('#correctStationLocation').val("")
        }

        //下拉 可修改列表 status=1,3,4，5
        function finishList() {
            $(function () {
                $.ajax({
                    type: 'post',
                    url: "QueryAllLinesServlet",
                    dataType: "json",
                    //data: {pid: 0},
                    success: function (data) {
                        // console.log(data);
                        $("#correctLineID").empty();
                        $("#correctLineID").append("<option value=''>请选择需要修改的公交线路</option>");
                        for (var i = 0; i < data.length; i++) {
                            $("#correctLineID").append('<option value=' + data[i].id + '>' + data[i].lineName + '</option>');
                        }
                    }
                });
                $.ajax({
                    type: 'post',
                    url: "QueryCorrectingLineServlet",
                    dataType: "json",
                    success: function (data) {
                        for (var i = 0; i < data.length; i++) {
                            $("#correctLineID").append('<option value=' + data[i].id + '>' + data[i].lineName + '</option>');
                        }
                    }
                });
            })
        }

        //修改线路-查看该线路走向及站点和拐点
        function getLineByID() {
            var $correctLineID = $("#correctLineID").val();
            // var $correctingLineID = $("#correctingLineID").val();
            // var queryLineID = "" ;
            // if($correctLineID != "" && $correctingLineID == "" ){
            //     queryLineID = $correctLineID;
            // }else if($correctLineID == "" && $correctingLineID != ""){
            //     queryLineID = $correctingLineID;
            // }else if($correctLineID == "" && $correctingLineID == ""){
            //     alert("请从一个下拉框中选择想要查询的线路")
            // }else if($correctLineID != "" && $correctingLineID != ""){
            //     alert("只能从一个下拉框中选择想要查询的线路！！")
            // }
            var queryLineID = $correctLineID;
            if(queryLineID != "") {
                $.ajax({
                    url: "LineQueryByIDServlet",
                    data: "queryLineID=" + queryLineID,
                    dataType: "json",
                    success: function (result, testStatus) {
                        var lineData = eval(result);//Object形式
                        var line = JSON.parse(lineData.coordinates) //字符串转对象
                        var style = {//查询线路展示的风格
                            "color": "#ff0000",
                            "weight": 2,
                            "opacity": 0.55
                        };

                        if (global_correct_line != null) {//若当前图层上有线路则清空原有线路
                            correctLineMap.removeLayer(global_correct_line)
                        }
                        global_correct_line = L.geoJSON([line], {
                            style: style
                        }).addTo(correctLineMap);
                        //显示站点
                        var stops = JSON.parse(lineData.stops)
                        var len = stops.length;
                        if (global_correct_markers.length > 0) {//若当前图层有其他线路的站点标记则清空
                            for (var j = 0; j < global_correct_markers.length; j++) {
                                correctLineMap.removeLayer(global_correct_markers[j])
                            }
                        }
                        global_correct_markers = [];
                        for (var j = 0; j < len; j++) {
                            var stopLocation = stops[j].location;
                            var stationId = stops[j].id
                            var stationName = stops[j].name
                            var stationLoc = stops[j].location
                            var stationSequence = stops[j].sequence
                            var stop = L.marker(stopLocation)
                                .addTo(correctLineMap)
                                .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                                .openPopup();
                            global_tmp_stop_marker = stop;
                            global_correct_markers[j] = stop
                        }
                        //显示线路拐点
                        $.ajax({
                            url: "GetCoordByID",
                            data: "queryLineID=" + queryLineID,
                            dataType: "json",
                            success: function (data) {
                                var arraydata = eval(data);
                                var len = arraydata.length;
                                if (global_correct_coords.length > 0) {//若当前展示图层上有其他线路的coord则先清空
                                    for (var i = 0; i < global_correct_coords.length; i++) {
                                        correctLineMap.removeLayer(global_correct_coords[i])
                                    }
                                    global_correct_coords = [];
                                }
                                for (var i = 0; i < len; i++) {
                                    var stopLoc = arraydata[i]; //array[2]  0: 31.238547 1: 121.3208
                                    global_correct_coords[i] = L.circle(stopLoc, {
                                        color: '#D1EEEE',
                                        weight: 0,
                                        fillColor: '#4876FF',
                                        radius: 10,
                                        fillOpacity: 1
                                    }).addTo(correctLineMap)
                                }
                            },
                            error: function (xhr, errorMessage, e) {
                                alert("系统异常！！")
                            }
                        })
                        //跳转至修改层
                        $("div.leaflet-control-layers-base").children().eq(6).children().eq(0).children().eq(0).click()
                    },
                    error: function () {
                        alert("系统异常！！")
                    }
                })
            }else{
                alert("请从下拉列表中选择想要查看的线路！！")
            }
        }


        //修改线路-返回
        function correctLineBack() {
            document.getElementById('form').style.display='block';
            document.getElementById('correctLine').style.display='none';
            document.getElementById('correct').style.display='none';
            document.getElementById('correctCoord').style.display='none';
            status1List();
        }

        //修改线路-返回开始修改
        function backToCorrect() {
            document.getElementById('correct').style.display='block';
            document.getElementById('startCorrect').style.display='none';
            document.getElementById('correctLine').style.display='none';
            document.getElementById('form').style.display='none';
            document.getElementById('correctCoord').style.display='none';
        }

        //修改线路-开始修改-修改站点
        function startCorrect() {
            stopCorrect();
            //跳转至修改层
            document.getElementById('startCorrect').style.display='block';
            document.getElementById('correctLine').style.display='none';
            document.getElementById('correct').style.display='none';
            $("#correctStationName").val("");
            $("#correctStationLocation").val("");
            $("#correctSequence").val("");
            $("#oldStop").prop('checked',false);
            $("#newStop").prop('checked',false);
        }

        //修改线路-开始修改-返回 返回继续修改其他线路
        function backToCorrectMore() {
            document.getElementById('startCorrect').style.display='none';
            document.getElementById('correctLine').style.display='block';
            document.getElementById('form').style.display='none';
            document.getElementById('correct').style.display='none';
            finishList();
            //correctingList();
        }

        //修改线路-删除该站点
        function deleteStation(){
            var $correctLineID = $("#correctLineID").val();
            // var $correctingLineID = $("#correctingLineID").val();
            var $correctStationName = $("#correctStationName").val();
            var $correctSequence = $("#correctSequence").val(); //拐点添加至该站点之后
            var lineID = $correctLineID
            //var lineID = "";
            // if($correctLineID != "" && $correctingLineID == "" ){
            //     lineID = $correctLineID;
            // }else if($correctLineID == "" && $correctingLineID != ""){
            //     lineID = $correctingLineID;
            // }else if($correctLineID == "" && $correctingLineID == ""){
            //     alert("请从一个下拉框中选择想要修改的线路")
            // }else if($correctLineID != "" && $correctingLineID != ""){
            //     alert("只能从一个下拉框中选择想要修改的线路！！")
            // }
            if(lineID!="") {
                var obj = JSON.stringify({
                    'lineID': lineID,
                    'name': $correctStationName,
                    'sequence': $correctSequence
                });
                console.log(obj);
                $.ajax({
                    url: "DeleteStationServlet",
                    type: "POST",
                    data: "obj=" + obj,   // http content type
                    dataType: "json",
                    success: function (result, testStatus) {
                        var lineData = eval(result);//Object形式
                        var line = JSON.parse(lineData.coordinates) //字符串转对象
                        var style = {//查询线路展示的风格
                            "color": "#ff0000",
                            "weight": 2,
                            "opacity": 0.55
                        };
                        if (global_correct_line != null) {//清空之前图层中所有的线路
                            correctLineMap.removeLayer(global_correct_line)
                        }
                        global_correct_line = L.geoJSON([line], {style: style}).addTo(correctLineMap);

                        //显示站点
                        var stops = JSON.parse(lineData.stops)
                        var len = stops.length;
                        if (global_correct_markers.length > 0) {
                            for (var i = 0; i < global_correct_markers.length; i++) {
                                correctLineMap.removeLayer(global_correct_markers[i]);
                            }
                            global_correct_markers = [];
                        }
                        if (global_correct_coords.length > 0) {
                            for (var i = 0; i < global_correct_coords.length; i++) {
                                correctLineMap.removeLayer(global_correct_coords[i]);
                            }
                            global_correct_coords = [];
                        }
                        for (var j = 0; j < len; j++) {
                            var stopLocation = stops[j].location;
                            var stationId = stops[j].id
                            var stationName = stops[j].name
                            var stationLoc = stops[j].location
                            var stationSequence = stops[j].sequence
                            global_correct_markers[j] = L.marker(stopLocation)
                                .addTo(correctLineMap)
                                .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                                .openPopup();
                        }
                        correct();

                        //跳转至在建线路展示层
                        $("div.leaflet-control-layers-base").children().eq(6).children().eq(0).children().eq(0).click()
                        alert("站点已经删除")
                        $("#correctStationName").val("");
                        $("#correctStationLocation").val("");
                        $("#correctSequence").val("");
                        $("#oldStop").prop('checked',false);
                        $("#newStop").prop('checked',false);
                    },
                    error: function () {
                        alert("系统异常！！")
                    }
                })
            }else{
                alert("请从下拉列表中选择一条想要修改的线路")
            }
        }

        //修改线路-保存该拐点
        function saveCorrectCoord(){
            var $correctLineID = $("#correctLineID").val();
            //var $correctingLineID = $("#correctingLineID").val();
            var $addCoordLoc = $("#correctCoordLoc").val(); //x,y
            var $stopSequence = $("#correct_Sequence").val(); //拐点添加至该站点之后
            // var lineID = "";
            // if($correctLineID != "" && $correctingLineID == "" ){
            //     lineID = $correctLineID;
            // }else if($correctLineID == "" && $correctingLineID != ""){
            //     lineID = $correctingLineID;
            // }else if($correctLineID == "" && $correctingLineID == ""){
            //     alert("请从一个下拉框中选择想要修改的线路")
            // }else if($correctLineID != "" && $correctingLineID != ""){
            //     alert("只能从一个下拉框中选择想要修改的线路！！")
            // }
            var lineID = $correctLineID;
            if(lineID!="") {
                var obj = JSON.stringify({
                    'lineID': lineID,
                    'loc': $addCoordLoc,
                    'sequence': $stopSequence
                });
                console.log(obj);
                $.ajax({
                    url: "AddCoord2Servlet",
                    data: "obj=" + obj,
                    type: "post",
                    dataType: "json",
                    success: function (result, testStatus) {
                        var lineData = eval(result);//Object形式
                        var line = JSON.parse(lineData.coordinates) //字符串转对象
                        var style = {//查询线路展示的风格
                            "color": "#ff0000",
                            "weight": 2,
                            "opacity": 0.55
                        };
                        if (global_correct_line != null) {//清空之前图层中所有的线路
                            correctLineMap.removeLayer(global_correct_line)
                        }
                        global_correct_line = L.geoJSON([line], {style: style}).addTo(correctLineMap);

                        //显示站点
                        var stops = JSON.parse(lineData.stops)
                        var len = stops.length;
                        if (global_correct_markers.length > 0) {
                            for (var i = 0; i < global_correct_markers.length; i++) {
                                correctLineMap.removeLayer(global_correct_markers[i]);
                            }
                            global_correct_markers = [];
                        }
                        if (global_correct_coords.length > 0) {
                            for (var i = 0; i < global_correct_coords.length; i++) {
                                correctLineMap.removeLayer(global_correct_coords[i]);
                            }
                            global_correct_coords = [];
                        }
                        for (var j = 0; j < len; j++) {
                            var stopLocation = stops[j].location;
                            var stationId = stops[j].id
                            var stationName = stops[j].name
                            var stationLoc = stops[j].location
                            var stationSequence = stops[j].sequence
                            global_correct_markers[j] = L.marker(stopLocation)
                                .addTo(correctLineMap)
                                .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                                .openPopup();
                        }
                        correct();

                        //跳转至在建线路展示层
                        $("div.leaflet-control-layers-base").children().eq(6).children().eq(0).children().eq(0).click()
                        alert("拐点已添加至线路")
                        $("#correctCoordLoc").val("");
                        $("#correct_Sequence").val("");
                    },
                    error: function () {
                        alert("系统异常！！")
                    }
                })
            }else{
                alert("请从下拉列表中选择一条想要修改的线路！")
            }
        }

        //修改线路-添加该站点至线路
        function addStationToLine(){
            var $correctLineID = $("#correctLineID").val();
            // var $correctingLineID = $("#correctingLineID").val();
            // var lineID = "";
            // if($correctLineID != "" && $correctingLineID == "" ){
            //     lineID = $correctLineID;
            // }else if($correctLineID == "" && $correctingLineID != ""){
            //     lineID = $correctingLineID;
            // }else if($correctLineID == "" && $correctingLineID == ""){
            //     alert("请从一个下拉框中选择想要修改的线路")
            // }else if($correctLineID != "" && $correctingLineID != ""){
            //     alert("只能从一个下拉框中选择想要修改的线路！！")
            // }
            var lineID = $correctLineID;
            var oldChecked = $('#oldStop').is(":checked");
            var newChecked = $('#newStop').is(":checked");
            var $stationName = $("#correctStationName").val();
            var $stationLocation = $("#correctStationLocation").val();
            var $sequence = $("#correctSequence").val();
            if(oldChecked && newChecked){
                alert("只能选择一种站点类型")
            }else if(!oldChecked && !newChecked){
                alert("请选择一种站点类型")
            }else if(lineID!="") {
                if(oldChecked && (!newChecked)) { //如果选择的是原有站点
                    var obj = JSON.stringify({
                        'type': "oldStop",
                        'lineID': lineID,
                        'name': $stationName,
                        'loc': $stationLocation,
                        'sequence': $sequence
                    });
                }
                if(!oldChecked && newChecked){//如果选择的是新站点
                    var obj = JSON.stringify({
                        'type': "newStop",
                        'lineID': lineID,
                        'name': $stationName,
                        'loc': $stationLocation,
                        'sequence': $sequence
                    });
                }
                console.log(obj);
                $.ajax({
                    url: "AddStopToLine",
                    data: "obj=" + obj,
                    type: "post",
                    dataType: "json",
                    success: function (result) {
                        var lineData = eval(result);//Object形式
                        var line = JSON.parse(lineData.coordinates) //字符串转对象
                        var style = {//查询线路展示的风格
                            "color": "#ff0000",
                            "weight": 2,
                            "opacity": 0.55
                        };
                        if (global_correct_line != null) {//清空之前图层中所有的线路
                            correctLineMap.removeLayer(global_correct_line)
                        }
                        global_correct_line = L.geoJSON([line], {style: style}).addTo(correctLineMap);

                        //显示站点
                        var stops = JSON.parse(lineData.stops)
                        var len = stops.length;
                        if (global_correct_markers.length > 0) {
                            for (var i = 0; i < global_correct_markers.length; i++) {
                                correctLineMap.removeLayer(global_correct_markers[i]);
                            }
                            global_correct_markers = [];
                        }
                        if (global_correct_coords.length > 0) {
                            for (var i = 0; i < global_correct_coords.length; i++) {
                                correctLineMap.removeLayer(global_correct_coords[i]);
                            }
                            global_correct_coords = [];
                        }
                        for (var j = 0; j < len; j++) {
                            var stopLocation = stops[j].location;
                            var stationId = stops[j].id
                            var stationName = stops[j].name
                            var stationLoc = stops[j].location
                            var stationSequence = stops[j].sequence
                            global_correct_markers[j] = L.marker(stopLocation)
                                .addTo(correctLineMap)
                                .bindPopup(stationId + " " + stationName + " 站点经纬度:" + stationLoc + " 站点顺序：" + stationSequence)
                                .openPopup();
                        }
                        correct();

                        //跳转至修改线路展示层
                        $("div.leaflet-control-layers-base").children().eq(6).children().eq(0).children().eq(0).click()
                        alert("站点已添加至线路")
                        $("#correctStationName").val("");
                        $("#correctStationLocation").val("");
                        $("#correctSequence").val("");
                        $("#oldStop").prop('checked',false);
                        $("#newStop").prop('checked',false);
                    },
                    error: function () {
                        alert("修改线路-添加站点至线路系统异常！！")
                    }
                })
            }
        }

        //修改线路-完成修改
        function  finishCorrect() {
            var $correctLineID = $("#correctLineID").val();
            // var $correctingLineID = $("#correctingLineID").val();
            // var lineID = "";
            // if($correctLineID != "" && $correctingLineID == "" ){
            //     lineID = $correctLineID;
            // }else if($correctLineID == "" && $correctingLineID != ""){
            //     lineID = $correctingLineID;
            // }else if($correctLineID == "" && $correctingLineID == ""){
            //     alert("请从一个下拉框中选择想要修改的线路")
            // }else if($correctLineID != "" && $correctingLineID != ""){
            //     alert("只能从一个下拉框中选择想要修改的线路！！")
            // }
            var lineID = $correctLineID;
            $.ajax({
                url: "FinishCorrectServlet",
                data: "lineID=" + lineID,
                type: "post",
                //dataType: "json",
                success: function (result) {
                    alert("线路状态更新成【修改完毕】")
                    backToCorrectMore();
                    $("#correctStationName").val("");
                    $("#correctStationLocation").val("");
                    $("#correctSequence").val("");
                    $("#correctCoordLoc").val("");
                    $("#correct_Sequence").val("");
                    $("#oldStop").prop('checked',false);
                    $("#newStop").prop('checked',false);
                },
                error: function () {
                    alert("完成修改线路功能出现异常")
                }
            })


        }

    </script>


</div>



</html>