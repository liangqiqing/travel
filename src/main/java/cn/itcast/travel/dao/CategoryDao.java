package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface CategoryDao {

    /**
     * 查询导航条表，获取全部信息
     * @return
     */
    List<Category> getAll();

    /**
     *  查询总数据数
     * @param cid_name
     * @param rname
     * @return
     */
    int getSize(int cid_name ,String rname);

    /**
     * 根据参数查询表，在根据开始索引，和每页展示数，进行分页
     * @param cid_name
     * @param stats
     * @param page
     * @param rname
     * @return
     */
    List<Route> getRoutes(int cid_name, int stats, int page ,String rname);

    /**
     * 根据rid去查询数据库
     * @param rid
     * @return
     */
    Route getRoute(int rid);

    int getCount(int rid);
}
