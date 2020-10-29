package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImlp;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 发送接收邮件的servlet
 */
@WebServlet("/emailServlet")
public class EmailServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //在获取激活码
        String checked = request.getParameter("checked");

        //拿着激活码去查询数据库，看是否存在激活码
        UserService service = new UserServiceImlp();

        //查询到会返回一个对象，在进行逻辑判断操作
        User user = service.getUser(checked);

        if (user == null || !checked.equals(user.getCode())){

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("<h1>你的激活码错误，请联系管理员</h1>");

        }else {
            //进行激活操作
            service.upUser(user);

            //资源重定向
            response.sendRedirect(request.getContextPath()+"/index.html");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
