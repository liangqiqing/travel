package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.ImgDao;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ImgDaoImpl implements ImgDao {
    //成员变量区域
    //获取JDBC操作对象
    private JdbcTemplate jdbc = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<RouteImg> getImg(int rid) {

        String sql = "select * from tab_route_img  where rid = ?";


        return jdbc.query(sql,new BeanPropertyRowMapper<RouteImg>(RouteImg.class),rid);
    }
}
