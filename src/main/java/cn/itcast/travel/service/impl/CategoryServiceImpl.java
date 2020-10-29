package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.ImgDao;
import cn.itcast.travel.dao.IsFavoriteDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.dao.impl.ImgDaoImpl;
import cn.itcast.travel.dao.impl.IsFavoriteDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
                //成员变量区域
            //  声明一个CategoryDao对象
    private CategoryDao categoryDao = new CategoryDaoImpl();
            //  声明一个 imgDao对象；
    private ImgDao imgDao = new ImgDaoImpl();
            // 声明一个 SellerDao对象
    private SellerDao sellerDao = new SellerDaoImpl();
            // 声明一个list集合变量
    private List<Category> list = null;
            // 声明一个Category变量
    private Category category = null;

    private IsFavoriteDao isFavorite = new IsFavoriteDaoImpl();
            //声明一个redis对象
    Jedis jedis = JedisUtil.getJedis();

    @Override
    public List<Category> getAll() {
        //使用redis缓冲思想进行程序优化，查询储存在有序且不能重复元素的域，Category键
        //Set<String> set = jedis.zrange("Category", 0, -1);
        Set<Tuple> set = jedis.zrangeWithScores("Category", 0, -1);
        //对象查询返回的值进行逻辑判断
        if (set == null || set.size() == 0){
            System.out.println("走数据库查询........");
            //为true，证明，缓冲区没有数据，那么就得去查询数据库
            List<Category> list = categoryDao.getAll();
            //查询出的结果,进行遍历依次存储数据
            for (Category key : list) {
                jedis.zadd("Category",key.getCid(),key.getCname());
            }
        }else{
            System.out.println("走缓存区查询......");
            //为false，证明缓冲区有数据，那么我们就和进行类型转换
            list = new ArrayList<Category>();
            //对Set<String> category数组进行遍历
            for (Tuple name : set) {
                category = new Category();
                category.setCid((int)name.getScore());
                category.setCname(name.getElement());
                list.add(category);
            }
        }

        return list;
    }

    @Override
    public PageBean<Route> getPageBean(int current, int page, int cid_name,String rname) {
        //获取一个PageBeam对象
        PageBean<Route> bean = new PageBean<>();
        //查询数据库获取总数据数
        int cid = categoryDao.getSize(cid_name,rname);
        bean.setTotalCount(cid);
            //设置每页展示数据条
        bean.setPageSize(page);
            //计算总页码，总数据数%每页展示数 == 0 ? 总数据数/每页展示数 : 总数据数/每页展示数 + 1 ;
        int pages = cid % page == 0 ? cid / page : (cid / page) + 1;
             //设置总页码
        bean.setTotalPage(pages);
            //当前页码
        bean.setCurrentPage(current);
            //获取查询开始索引  当前页码 - 1 * 要展示页码
        int stats = (current-1) * page;
            //查询数据库获取展示数据
        List<Route> list = categoryDao.getRoutes(cid_name,stats,page,rname);
            //设置每页展示数据集合
        bean.setList(list);

        return bean;
    }

    @Override
    public Route getRoute(String rid) {
        //根据rid调用dao去查询数据库 tab_route表
        Route route = categoryDao.getRoute(Integer.parseInt(rid));

        //根据rid调用到去查询数据库 tab_img表
        List<RouteImg> list = imgDao.getImg(route.getRid());

        //给route对象的list设置值
        route.setRouteImgList(list);

        //根据sid调用dao查询数据库  tab_seller表
        Seller seller = sellerDao.getSeller(route.getSid());

        //给route对象的seller设置值
        route.setSeller(seller);

        //给route对象seller设置值
        int count = categoryDao.getCount(Integer.parseInt(rid));
        route.setCount(count);

        return route;
    }

    @Override
    public boolean getBoolean(String rid, int uid) {

        Favorite favorite = isFavorite.getFavorite(Integer.parseInt(rid),uid);

        if (favorite == null){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void addOne(String id, int uid) {

        isFavorite.addOne(Integer.parseInt(id),uid);
    }


}
