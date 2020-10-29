package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    //声明成员变量
    private JdbcTemplate jdbc = new JdbcTemplate(JDBCUtils.getDataSource());
    // 声明一个User成员变量
    private  User user = null;

    @Override
    public boolean getBoolean(User user) {
        try {
            //定义查询sql
            String sql = "select * from tab_user where username = ?";
            //使用jdbc数据库连接池对象，查询数据库
            jdbc.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), user.getUsername());
        }catch (Exception e){
            return false;
        }
       //没有数据返回false
        return true;
    }

    public void addUser(User user){
        //定义添加sql
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        // //使用jdbc数据库连接池对象，更新数据库数据
        jdbc.update(sql,user.getUsername(),
                user.getPassword(), user.getName(),
                user.getBirthday(), user.getSex(),
                user.getTelephone(),user.getEmail(),
                user.getStatus(),user.getCode());
    }

    @Override
    public User getUser(String string) {

        try {
            //定义sql
            String sql = "select * from tab_user where code = ?";
            //调用查询方法
            user = jdbc.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), string);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void upUser(User user) {
        //定义根据唯一的激活码，修改激活状态
        String sql = "update  tab_user set status = 'Y' where code = ?";

        //调用对象指向sql
        jdbc.update(sql,user.getCode());
    }

    @Override
    public User getUser(String username, String password) {

        try {
            //定义查询sql
            String sql = "select * from tab_user where username = ? and  password = ?";
            //使用jdbc数据库连接池对象，查询数据库
            user = jdbc.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }


}