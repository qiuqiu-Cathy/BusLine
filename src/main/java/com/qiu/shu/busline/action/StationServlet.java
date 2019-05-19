package com.qiu.shu.busline.action;
import com.qiu.shu.busline.domain.Station;
import com.qiu.shu.busline.service.StationService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
public class StationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");

		Gson gson=new Gson();
		PrintWriter out = response.getWriter();
		StationService service = new StationService();
		List<Station> stations = service.queryAllValidStation();

		String info=gson.toJson(stations);
		System.out.println(info);
//		String list_str = StringUtils.join(list,",");
//		String json = JSONArray.fromObject(list).toString();

		out.write(info);

		System.out.println(stations);

	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//TODO Auto-generated method stub
		doGet(request, response);
	}

}
