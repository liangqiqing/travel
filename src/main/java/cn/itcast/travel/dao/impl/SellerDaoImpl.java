package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class SellerDaoImpl implements SellerDao {
    //成员变量区域
    //获取JDBC操作对象
    private JdbcTemplate jdbc = new JdbcTemplate(JDBCUtils.getDataSource());


    @Override
    public Seller getSeller(int sid) {
        String sql = "select * from tab_seller  where sid = ?";


        return jdbc.queryForObject(sql,new BeanPropertyRowMapper<Seller>(Seller.class),sid);
    }
}
