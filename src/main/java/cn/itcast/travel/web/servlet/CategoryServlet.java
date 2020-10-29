package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BeansServlet {
        //成员变量区域
                // 声明一个CategoryService对象
    private CategoryService categoryService = new CategoryServiceImpl();
                // 声明一个java转json格式的对象
    private ObjectMapper mapper = new ObjectMapper();
                //声明响应数据对象
    private ResultInfo info = new ResultInfo();

    /**
     * 展示导航条信息功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void all(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用CategoryService对象中的getAll方法查询导航条表，返回所有一个list集合，存储所有信息
        List<Category> list = categoryService.getAll();
        //设置响应字符集
        response.setContentType("application/json;charset=utf-8");
        //把数据以Json格式响应会个客户端
        mapper.writeValue(response.getOutputStream(),list);

    }

    /**
     * 完成信息展示和分页条功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void route(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //需要获取请求中的3个参数
        //获取当前页码
        String currentPage = request.getParameter("currentPage");
        //获取每页展示数据条
        String pageSize = request.getParameter("pageSize");
        //获取查询数据库的cid
        String cid = request.getParameter("cid");
        //获取rname请求参数
        String rname = request.getParameter("val");
        //逻辑判断，有值进行字符集解析
        if (rname != null && rname.length() > 0 && !"null".equals(rname)){
            rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
        }

        System.out.println("currentPage:"+currentPage+"-- pageSize:"+pageSize+"-- cid:"+cid+"-- rname:"+rname);

        //PageBean中是int类型，所以进行类型转换，和进行防止出现空异处理
        int current = 0;
        if (currentPage != null && currentPage.length() >0){
          current  =  Integer.parseInt(currentPage);
        }else {
            //如果参数是空，或是空字符，就给赋值
            current  = 1;
        }

        int page = 0;
        if (pageSize != null && pageSize.length() >0){
            page  =  Integer.parseInt(pageSize);
        }else {
            //如果参数是空，或是空字符，就给赋值5,也可以在前端定死数据
            page  = 5;
        }

        int cid_name = 0;
        if (cid != null && cid.length() > 0 && !"null".equals(cid)){
            cid_name  =  Integer.parseInt(pageSize);
        }else {
            //如果参数是空，或是空字符，就给赋值5,因为现在数据只存在5，所以初始赋值为五
            cid_name  = 5;
        }

        //获取一个完整的PageBean对象
       PageBean<Route> bean = categoryService.getPageBean(current,page,cid_name,rname);
        //设置响应头字符集
        response.setContentType("application/json");
        //把数据以JSON格式写回客户端
        mapper.writeValue(response.getOutputStream(),bean);
    }

    public void detailed(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求参数rid
        String rid = request.getParameter("rid");
        System.out.println(rid);
        //调用service对象去执行第二层结构
        Route route = categoryService.getRoute(rid);
        //设置响应头
        response.setContentType("application/json");
        //根据查询返回一个比较完整的route对象，再一json格式写会客户端
        mapper.writeValue(response.getOutputStream(),route);
    }

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求参数rid
        String rid = request.getParameter("rid");
        //获取session域中user对象
        User user = (User) request.getSession().getAttribute("user");
        //如果域中对象为空，直接返回
        if (user == null){
            //为true，说明没有值
           return;
        }else {
            int uid = user.getUid();
            //不为空说明域中存在对象
        boolean b = categoryService.getBoolean(rid,uid);
            if (b){
                //为true，说明有值
                info.setFlag(true);
            }else{
                //为false，没有值
              return;
            }
        }
        //声明响应头格式
        response.setContentType("application/json");
         //把数据响应会客户端
        mapper.writeValue(response.getOutputStream(),info);
    }

    public void addOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求参数rid
        String rid = request.getParameter("rid");
        //获取session域中user对象
        User user = (User) request.getSession().getAttribute("user");
        //如果域中对象为空，直接返回
        if (user != null){
            int uid = user.getUid();
            //为true，说明有值
            categoryService.addOne(rid,uid);

            info.setFlag(true);
        }else {
           //没有值直接返回
            info.setFlag(false);
        }
        //声明响应头格式
        response.setContentType("application/json");
        //把数据响应会客户端
        mapper.writeValue(response.getOutputStream(),info);
    }


}
