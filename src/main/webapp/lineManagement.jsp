<%@ page import="com.qiu.shu.busline.domain.Line" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: 邱邱
  Date: 2019/4/18
  Time: 0:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript" src="js/jquery-3.4.0.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            $("tr:odd").css("background-color","lightgray");
        });
    </script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>线路管理</title>
</head>
<body>
<table border="1px">
    <tr>
        <th>线路id</th>
        <th>线路名称</th>
        <th>线路站点名</th>
        <th>操作</th>
    </tr>

    <%
        //获取request域中的数据
        List<Line> lines = (List<Line>)request.getAttribute("lines") ;
        for(Line line:lines){
    %>
    <tr>
        <td><%=line.getId() %></td>
        <td><a href="LineQueryServlet?lineName=<%=line.getLineName() %>"><%=line.getLineName() %></a>      </td>
<%--        <td><%=line.getStops() %></td>--%>
        <td> <a href="DeleteLineByIdServlet?id=<%=line.getId() %>   ">删除</a> </td>

    </tr>
    <%
        }
    %>

</table>
<%--<a href="addLine.jsp">新增</a>--%>
</body>
</html>
