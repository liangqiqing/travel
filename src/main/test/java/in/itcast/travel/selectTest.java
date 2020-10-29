package in.itcast.travel;

import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.dao.impl.IsFavoriteDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import cn.itcast.travel.service.impl.UserServiceImlp;
import org.junit.Test;

import java.util.List;

public class selectTest {

    @Test
    public void test1(){
        User user = new User();
        user.setUsername("zhansan");

        UserService userService = new UserServiceImlp();
        boolean b = userService.getBoolean(user);
        System.out.println(b);
    }

    @Test
    public  void test2(){
        User user = new User();
        user.setUsername("zhangsan");
        user.setPassword("123456");

        UserService userService = new UserServiceImlp();
        User b = userService.getUser(user.getUsername(),user.getPassword());
        System.out.println(b);
    }

    @Test
    public void test3(){
        String s = "123";
        String l = "456";
        String e = "457";
        s += l;
        s += e;
        System.out.println(s);
    }

    @Test
    public void test4(){
        CategoryDaoImpl categoryDao = new CategoryDaoImpl();
        List<Category> list = categoryDao.getAll();
        for (Category key : list) {
            System.out.println(key);
        }
    }

    @Test
    public void test5(){
        CategoryServiceImpl categoryService = new CategoryServiceImpl();
        List<Category> all = categoryService.getAll();
        for (Category category : all) {
            System.out.println(category);
        }
    }

    @Test
    public void test6(){
        CategoryDaoImpl categoryDao = new CategoryDaoImpl();
        List<Route> list = categoryDao.getRoutes(5, 1, 5, "西安");
        for (Route s : list) {
            System.out.println(s.toString());
        }
    }

    @Test
    public void test7(){
        IsFavoriteDaoImpl isFavoriteDao = new IsFavoriteDaoImpl();
        isFavoriteDao.addOne(6,15);
    }
}
