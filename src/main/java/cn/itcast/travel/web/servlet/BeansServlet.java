package cn.itcast.travel.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射类，作用于功能方法调用和方法分发执行
 */
public class BeansServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /**
         * 使用反射技术，对类的字节码文件调用，而不动类代码
         */
        //获取请求路径
        String uri = req.getRequestURI();
        System.out.println(uri);

        //截取路径，拿出请求的方法名称 在截取字符串的时候，是含头不含尾的，所有在取的时候索引加上1
        uri = uri.substring(uri.lastIndexOf("/") + 1);


        //通过关键字this.获取this对应的字节码对象
        try {
            //根据方法名称和参数获取对应的方法对象
            Method method = this.getClass().getMethod(uri,HttpServletRequest.class,HttpServletResponse.class);
            //执行使用方法对象执行自身
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (uri.equals("register")){
            System.out.println("有用户访问注册页面："+uri);
        }

        if (uri.equals("login")){
            System.out.println("有访问用户登录页面："+uri);
        }
    }
}
