package com.testing.login;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.testing.mysql.ConnectMysql;
import com.testing.mysql.UseMysql;

/**
 * Servlet implementation class Login
 */
@WebServlet("/LoginTry")
public class LoginTry extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginTry() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 返回值编码的修改
		response.setContentType("text/html;charset=UTF-8");
		// 收到的参数编码
		request.setCharacterEncoding("UTF-8");
		String user = request.getParameter("user");
		String pwd = request.getParameter("pwd");
		response.getWriter().append(user + pwd);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// 返回值编码的修改
		response.setContentType("text/html;charset=UTF-8");
		// 收到的参数编码
		request.setCharacterEncoding("UTF-8");
		String user = request.getParameter("user");
		String pwd = request.getParameter("pwd");
		String res = "{";
		//设置session超时时间为30分钟
		request.getSession().setMaxInactiveInterval(1800);
		//获取本次会话的sessionID
		String ssIDvalue=request.getSession().getId();
		
		// 判断用户名密码不为空
		if (user != null && pwd != null) {

			if (user.length() > 2 && user.length() < 17 && pwd.length() > 2 && pwd.length() < 17) {
				// 判断不包含特殊字符
				// 判断长度
				String regEx = "[^a-zA-Z0-9_-]";
				Pattern p = Pattern.compile(regEx);
				// 调用pattern对象p的mathcer方法，生成一个匹配matcher对象
				Matcher m = p.matcher(user);
				Matcher m1 = p.matcher(pwd);
				if (!m.find() && !m1.find()) {
					if (request.getSession().getAttribute("loginName") == null) {
						// 创建sql连接以及实例化usemysql类型
						ConnectMysql connSql = new ConnectMysql();
						UseMysql mySql = new UseMysql(connSql.conn);
						if (mySql.PLogin(user, pwd)) {
							res += "\"status\":200,\"msg\":\"恭喜您，登录成功！\"}";
							// 在session当中记录本次登录的用户名
							request.getSession().setAttribute("loginName", user);
							//创建cookie
							Cookie ssID=new Cookie("JSESSIONID",ssIDvalue);
							//设置cookie有效期
							ssID.setMaxAge(180);
							//返回cookie给客户端
							response.addCookie(ssID);
						} else {
							res += "\"status\":3000,\"msg\":\"用户名密码不匹配！\"}";
						}
					}
					// session当中有相应的loginName记录
					else {
						if (request.getSession().getAttribute("loginName").equals(user)) {
							res += "\"status\":3001,\"msg\":\"用户已经登录不能重复登录！\"}";
						} else {
							res += "\"status\":3002,\"msg\":\"已经有其他用户登录，不能重复登录！\"}";
						}
					}
				} else {
					res += "\"status\":3003,\"msg\":\"用户名密码不能包含特殊字符！\"}";
				}
			} else {
				res += "\"status\":3004,\"msg\":\"用户名密码长度必须是3至16位！\"}";
			}
		} else {
			res += "\"status\":3005,\"msg\":\"用户名密码长度不能为空！\"}";
		}
		res=res.replace("}", ",");
		res+="\"JSESSIONID\":\""+ssIDvalue+"\"}";
		// 返回值中显示本次请求时的sessionID
		response.getWriter().append(res);

	}

}
