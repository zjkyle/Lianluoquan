package com.tianle.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.tianle.model.base.Article;
import com.tianle.model.base.Comment;
import com.tianle.model.logic.LogicArticle;
import com.tianle.service.article.PubArticle;
import com.tianle.service.article.RecArticle;
import com.tianle.service.article.ZanPlus;
import com.tianle.service.comment.AddComment;
import com.tianle.service.mainPage.MainPageService;
import com.tianle.service.organization.UseraddtoCircle;
import com.tianle.service.register.RegisterService;

public class Register extends HttpServlet {

	/**
	 * localhost/Lianluoquan/Register?type=article&userUUID=kyleuuid&circle=all
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String type = request.getParameter("type");
		PrintWriter out = response.getWriter();
		System.out.println(type);
		RegisterService rs = new RegisterService();
		if (type != null) {
			if (type.equals("register")) {
				// String reregisertinfor =
				// request.getParameter("registerInfor").replaceAll("id","userUUID");
				String reregisertinfor = request.getParameter("registerInfor");
				System.out.println(reregisertinfor);
				// 调用方法注册用户，包含（姓名，密码，学号，学院，班级）
				String uuid = rs.registerUser(reregisertinfor);
				if (uuid.equals("")) {
					out.println("false");
					out.close();
				} else {
					// 根据用户uuid返回用户详细信息
					// String logicUser =
					// rs.selcetStuInf(uuid).replaceAll("userUUID","id");
					String logicUser = rs.selcetStuInf(uuid);
					// logicUser = "{\"users\": ["+logicUser+"]}";
					System.out.println(logicUser);
					out.println(logicUser);
					out.close();
				}
			} else if (type.equals("login")) {
				// String loginInfor =
				// request.getParameter("registerInfor").replaceAll("id","userUUID");
				String loginInfor = request.getParameter("registerInfor");
				String uuid = rs.login(loginInfor);
				if (uuid.equals("")) {
					out.println("false");
					out.close();
				} else {
					// 根据用户uuid返回用户详细信息
					// String logicUser =
					// rs.selcetStuInf(uuid).replaceAll("userUUID","id");
					String logicUser = rs.selcetStuInf(uuid);
					// logicUser = "{\"users\": ["+logicUser+"]}";
					System.out.println(logicUser);
					out.println(logicUser);
					out.close();
				}
			} else if (type.equals("article")) {
				String userUUID = request.getParameter("userUUID");
				String circle = request.getParameter("circle");
				String page = request.getParameter("page");
				String time = request.getParameter("time");
				if (!userUUID.equals("") && !circle.equals("")) {
					MainPageService mps = new MainPageService();
					ArrayList<Article> articles = mps.mainPageResponse(
							userUUID, page, circle, time);
					ArrayList<LogicArticle> logicArticles = mps
							.formatArticle(articles);
					JSONArray jsonArray = JSONArray.fromObject(logicArticles);
					String sArticles = "{\"logicArticles\":"
							+ jsonArray.toString() + "}";
					out.println(sArticles);
					System.out.println(sArticles);
					out.close();
				}
			} else if (type.equals("uploadArticle")) {
				String uploadArt = request.getParameter("article");
				Article recArticle = new RecArticle().getArt(uploadArt);
				String articleUUID = new PubArticle().pubMyArticle(recArticle);
				System.out.println(articleUUID);
			} else if (type.equals("zan")) {
				String zanId = request.getParameter("articleuuid");
				ZanPlus zp = new ZanPlus();
				int result = zp.addZan(zanId);
				if(result > 0) {
					int count = zp.zanCount(zanId);
					out.println(count);
					out.close();
				}
			} else if (type.equals("comment")) {
				String commentJSON = request.getParameter("comment");
				AddComment ac = new AddComment();
				Comment comment = ac.getComment(commentJSON);
				String commentUUID = ac.addtoDataBase(comment);
				if(!commentUUID.equals("")) {
					System.out.println("评论成功，评论UUID为：   " + commentUUID);
					out.println("Oh！！！评论成功啦！！");
					out.close();
				}
			} else if (type.equals("addorg")) {
				String userUUID = request.getParameter("useruuid");
				String orgUUID = request.getParameter("orguuid");
				UseraddtoCircle uatc = new UseraddtoCircle();
				uatc.addCircle(userUUID, orgUUID);
			} else if (type.equals("refresh")) {
				String articleUUID = request.getParameter("articleUUID");
				MainPageService mps = new MainPageService();
				Article article = mps.getArtcleforUUID(articleUUID);
				ArrayList<Article> articles = new ArrayList<Article>();
				articles.add(article);
				ArrayList<LogicArticle> logicArticles = mps.formatArticle(articles);
				JSONArray jsonArray = JSONArray.fromObject(logicArticles);
				String s = jsonArray.toString();
				String sArticles = s.substring(1, s.length()-1);
				out.println(sArticles);
				System.out.println("点击查看文章详细内容： " + sArticles);
				out.close();
			}
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
