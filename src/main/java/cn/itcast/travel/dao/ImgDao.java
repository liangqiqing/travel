package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface ImgDao {
    /**
     * 根据rid ，去查询tab_img表
     * @param rid
     * @return
     */
    List<RouteImg> getImg(int rid);
}
