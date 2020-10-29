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

@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //申请一个响应客户端信息的对象
        ResultInfo info = new ResultInfo();

        //申请一个序列化JSON格式的对象
        ObjectMapper mapper = new ObjectMapper();

        //获取用户输入的验证码
        String check = request.getParameter("check");

        //获取程序生成存储在session域中的验证码值
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");

        //为确保每个验证码唯一，每当用户提交一次表单立马就把程序生成的验证码删除
        session.removeAttribute("CHECKCODE_SERVER");

        //对验证码进行判断
        if (check == null || !check.equalsIgnoreCase(checkcode_server)){

            //条件成立，用户输入和程序的值不一致，就进行逻辑处理，响应提示用户
            info.setFlag(true);
            info.setErrorMsg("验证码错误！");
            String s = mapper.writeValueAsString(info);

            //设置响应头
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(s);

            return;
        }


        //获取请求参数Map
        Map<String, String[]> map = request.getParameterMap();

        //获取一个user空参对象
        User user = new User();

        //使用BeanUtils工具类快速封装
        try {
            BeanUtils.populate(user,map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //调用service对象对数据进行逻辑处理
        UserService userService = new UserServiceImlp();
        boolean b = userService.getBoolean(user);

        //根据查询返回的结果进行逻辑处理
        if (b){

            //为true说明数据库存有数据，不进行添加操作，给客户端响应提示信息
            info.setFlag(b);
            info.setErrorMsg("账号已经存在，请更换！");
            String s = mapper.writeValueAsString(info);

            //设置响应头
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(s);

        }else {

            //为false 执行添加方法
            userService.addUser(user);
            info.setFlag(b);
            String s = mapper.writeValueAsString(info);

            //设置响应头
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(s);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
