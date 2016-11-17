package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.pojo.address.AddressUtilPOJO;

import java.util.List;

/**
 * Created by Ziming on 2016/11/14.
 */
public interface AddressUtilMapper {
    /**
     * 通过code取name
     * @return name
     */
    String selectNameByCode(Integer code);

    /**
     * 根据父级id查询地址信息
     * @param parentNo 父级id
     * @return 地址信息
     */
    List<AddressUtilPOJO> selectCityArea(Integer parentNo);
}