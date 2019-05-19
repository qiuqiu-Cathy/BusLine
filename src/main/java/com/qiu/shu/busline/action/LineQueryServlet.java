package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Coordinates;
import com.qiu.shu.busline.service.LineService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.qiu.shu.busline.Util.DealCoordUtil.*;


//控制器层：接受view请求，并分发给model
//通过前端传来的公交线路名称来查询公交的线路图
public class LineQueryServlet extends HttpServlet {
	//还存在万一前端输入的名字线路不存在，前端alert不能正确显示区别线路是否存在 dao层需修改
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//接受前端传来的查询公交线路名
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();
		LineService service = new LineService();
		Gson gson = new Gson();

		String lineName = request.getParameter("queryLineName");
		ArrayList<String> coords = service.queryLineByName(lineName);
		//String coord = LineQueryDao.queryLineByName(lineName);
		ArrayList<String> coordsJson = new ArrayList<String>();
		if(coords!=null){
			//System.out.println(coord);
			List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
			for(String coord:coords) {
				Coordinates c = changeIntoCoord(coord);
				coordinatesList.add(c);

			}

			String coordsInfo = gson.toJson(coordinatesList);
			System.out.println(coordsInfo);
			out.write(coordsInfo);

		}else{
			out.write("false");
		}
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
