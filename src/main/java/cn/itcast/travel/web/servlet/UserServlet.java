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


/**
 * 用户功能集成类，作用于容易管理和维护
 */
@WebServlet("/user/*")
public class UserServlet extends BeansServlet {
    //成员变量区域
            //申请一个响应客户端信息的对象
     private  ResultInfo info = new ResultInfo();

            //申请一个序列化JSON格式的对象
     private ObjectMapper mapper = new ObjectMapper();

            //调用service对象对数据进行逻辑处理
     private UserService userService = new UserServiceImlp();

            //获取User对象
      private User user =null;
    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    /**
     * 激活功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void email(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //在获取激活码
        String checked = request.getParameter("checked");


        //查询到会返回一个对象，在进行逻辑判断操作
        User user = userService.getUser(checked);

        if (user == null || !checked.equals(user.getCode())){

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("<h1>你的激活码错误，请联系管理员</h1>");

        }else {
            //进行激活操作
            userService.upUser(user);

            //资源重定向
            response.sendRedirect(request.getContextPath()+"/index.html");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        user = new User();

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

    /**
     * 登录成功，信息展示功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void look(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取存在在session域的user对象
        User user = (User) request.getSession().getAttribute("user");
        //在把数据以ajax格式写会响应
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(),user);
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //让存在在session域自毁
        request.getSession().invalidate();

        //资源重定向回页面首页
        response.setContentType("text/html;charset=utf-8");
        response.sendRedirect(request.getContextPath()+"/index.html");
    }
}
