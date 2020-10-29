package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface IsFavoriteDao {
    /**
     * 根据线路id和用户id，查询数据库
     * @param parseInt
     * @param uid
     * @return
     */
    Favorite getFavorite(int parseInt, int uid);

    /**
     * 根据线路id和用户id，添加数据
     * @param id
     * @param uid
     */
    void addOne(int id, int uid);


}
