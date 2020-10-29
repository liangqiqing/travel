package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
        //成员变量区域
        //获取JDBC操作对象
    private JdbcTemplate jdbc = new JdbcTemplate(JDBCUtils.getDataSource());
        //声明一个List<Category> query 对象
    private     List<Category> query = null;

    @Override
    public List<Category> getAll() {
        try {
            //定义查询sql
            String sql = "select * from tab_category";
            //执行sql
            query = jdbc.query(sql, new BeanPropertyRowMapper<Category>(Category.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return query;
    }

    @Override
    public int getSize(int cid_name,String rname) {

        //String sql = "select count(*) from tab_route where cid = ?";
        //定义初始sql模板
        String sql = "select count(*) from tab_route where 1 = 1 ";
        //创建一个字符串缓冲区对象
        StringBuilder sb = new StringBuilder(sql);
        //获取一个存放参数的list集合
        List<Object> list = new ArrayList<Object>();
        if (cid_name > 0){
            sb.append(" and  cid  =  ? ");
            list.add(cid_name);
        }
        if (rname != null && rname.length() > 0  && !"null".equals(rname)){
            sb.append("  and  rname  like  ? ");
            list.add("%"+rname+"%");
        }
        sql = sb.toString();
        System.out.println(sql);

        return jdbc.queryForObject(sql,Integer.class,list.toArray());
    }

    @Override
    public List<Route> getRoutes(int cid_name, int stats, int page , String rname) {

        //String sql = "select * from tab_route where cid = ? limit ? , ? ";

        //定义初始sql模板
        String sql = "select *  from tab_route where 1 = 1 ";
        //创建一个字符串缓冲区对象
        StringBuilder sb = new StringBuilder(sql);
        //获取一个存放参数的list集合
        List<Object> list = new ArrayList<Object>();
        if (cid_name > 0){
            sb.append(" and  cid  =  ? ");
            list.add(cid_name);
        }
        if (rname != null && rname.length() > 0 && !"null".equals(rname)){
            sb.append("  and  rname  like ? ");
            list.add("%"+rname+"%");
        }
        sb.append("  limit  ? , ?");
        list.add(stats);
        list.add(page);
        sql = sb.toString();
        System.out.println(sql);

        return jdbc.query(sql, new BeanPropertyRowMapper<Route>(Route.class),list.toArray());
    }

    @Override
    public Route getRoute(int rid) {
        String sql = "select * from tab_route where rid = ?";

        return jdbc.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }

    @Override
    public int getCount(int rid) {

        String sql = "select count(*) from tab_favorite where rid = ?";

        return jdbc.queryForObject(sql,Integer.class,rid);
    }
}
