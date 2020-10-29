package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImlp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取响应信息的对象
        ResultInfo info = new ResultInfo();

        //获取java转json格式的对象
        ObjectMapper mapper = new ObjectMapper();

        //获取请求验证码值
        String check = request.getParameter("check");

        //获取程序验证码值
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");

        //为确保验证码唯一，每获取完一次，立马就删除
        session.removeAttribute("CHECKCODE_SERVER");
        //对请求的值进行判断
        if (check == null || !(check.equalsIgnoreCase(checkcode_server))) {
            //输入的为空 或 和程序生成的不匹配
            //在反馈对象中储存错误提示信息
            info.setFlag(false);
            info.setErrorMsg("验证码错误！");

        }

        //检验验证码通过进行，下面的查询数据库逻辑操作
        //获取请求参数，map集合
        Map<String, String[]> map = request.getParameterMap();

        //获取User对象
        User user = new User();

        //获取BeanUtils工具类进行自动封装
        try {
            BeanUtils.populate(user, map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //调用service去查询数据库
        UserService userService = new UserServiceImlp();
        User u = userService.getUser(user.getUsername(), user.getPassword());

        //查询返回的数据进行逻辑判断处理
        if (u == null) {
            //为true，说明数据库中没有信息
            info.setFlag(false);
            info.setErrorMsg("账号或密码错误！请重新输入。");

        }
        if (u != null && !(u.getStatus().equals("Y"))) {
            //为true，激活码还没激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活！");

        }
        if (u != null && u.getStatus().equals("Y")) {
            //为true，说明输入正确并且也成功激活了
            info.setFlag(true);
            //并且在session域存储user对象
            request.getSession().setAttribute("user",u);
        }


        //声明响应字符集
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      this.doPost(request,response);
    }
}
