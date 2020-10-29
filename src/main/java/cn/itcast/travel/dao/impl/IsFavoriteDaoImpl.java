package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.IsFavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;



public class IsFavoriteDaoImpl implements IsFavoriteDao {
    //成员变量区域
    //获取JDBC操作对象
    private JdbcTemplate jdbc = new JdbcTemplate(JDBCUtils.getDataSource());


    @Override
    public Favorite getFavorite(int rid, int uid) {
        Favorite favorite = null;
        try{
            String sql = "select * from tab_favorite where rid = ? and uid = ?";

            favorite = jdbc.queryForObject(sql,new BeanPropertyRowMapper<Favorite>(Favorite.class),rid,uid);
        }catch (Exception e){
            e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public void addOne(int id, int uid) {

            String sql = "insert into tab_favorite values(?,?,?)";
            jdbc.update(sql,id,null,uid);

    }

}
