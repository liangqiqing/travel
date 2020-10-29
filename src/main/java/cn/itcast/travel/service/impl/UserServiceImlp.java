package cn.itcast.travel.service.impl;


import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImlp implements UserService {
            //成员变量区域
        //声明一个UserDao成员对象
     private UserDao userDao = new UserDaoImpl();


    @Override
    public boolean getBoolean(User user) {
       return   userDao.getBoolean(user);
    }

    @Override
    public User getUser(String username, String Password) {
        return userDao.getUser(username, Password);
    }

    @Override
    public void addUser(User user) {
        //设置激活码
        user.setCode(UuidUtil.getUuid());
        //设置激活状态
        user.setStatus("N");
        userDao.addUser(user);

        //定义邮件内容
        String email = "<a href = 'http://localhost:80/travel/user/email?checked="+user.getCode()+"'><b>点击进入激活账号</b></a>";

        //调用发邮件的对象给用户发送激活邮件
        MailUtils.sendMail(user.getEmail(),email,"激活邮件");
    }

    @Override
    public User getUser(String checked) {
        return userDao.getUser(checked);
    }

    @Override
    public void upUser(User user) {
        userDao.upUser(user);
    }
}
