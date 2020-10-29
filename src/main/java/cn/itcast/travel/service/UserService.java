package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 根据账号查询数据库
     * @param user 带有全部信息的对象
     * @return  查询结果
     */
    public boolean getBoolean(User user);

    /**
     * 根据参数账号和密码进行数据库查询
     * @param username  账号
     * @param Password   密码
     * @return  查询结果
     */
    public User getUser(String username , String Password);


    /**
     * 根据查询返回的结果是否要进行存储操作
     * @param user
     */
    public void addUser(User user);

    /**
     * 根据请求的参数进行数据库查询
     * @param checked
     * @return
     */
    User getUser(String checked);

    /**
     * 对用户的激活状态进行修改
     * @param user
     */
    void upUser(User user);
}
