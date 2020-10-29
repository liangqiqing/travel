package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据账号查询数据库
     * @param user
     * @return
     */
    boolean getBoolean(User user);

    /**
     * 根据查询返回的数据进行注册操作
     * @param user
     */
    public void addUser(User user);

    /**
     * 根据请求参数激活码，查询数据库
     * @param string
     * @return
     */
    public User getUser(String string);

    /**
     * 对用户的激活的状态进行修改
     * @param user
     */
    void upUser(User user);

    /**
     * 根据参数账号和密码进行数据库查询
     * @param username  账号
     * @param password   密码
     * @return  查询结果
     */
    User getUser(String username, String password);


}
