package cn.itcast.travel.service;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface CategoryService {
    /**
     * 查询导航条表，获取全部信息
     * @return
     */
    List<Category> getAll();

    /**
     * 返回一个带有分页数据和数据展示的PageBean对象
     * @param current
     * @param page
     * @param cid_name
     * @param rname
     * @return
     */
    PageBean<Route> getPageBean(int current, int page, int cid_name,String rname);

    /**
     * 根据一个id去出现数据库
     * @param rid
     * @return
     */
    Route getRoute(String rid);

    /**
     * 根据线路id，和用户id，去查询数据
     * @param rid
     * @param uid
     * @return
     */
    boolean getBoolean(String rid, int uid);

    /**
     * 在收藏表进行添加动作
     * @param id
     * @param uid
     */
    void addOne(String id, int uid);


}

