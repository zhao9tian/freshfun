package com.quxin.freshfun.service.address;

import com.quxin.freshfun.model.pojo.address.AddressUtilPOJO;

import java.util.List;

/**
 * Created by Ziming on 2016/11/14.
 */
public interface AddressUtilService {
    /**
     * 根据codes获取省市区字符串
     * @param provCode 省级code
     * @param cityCode 市级code
     * @param distCode 县区级code
     * @return 省市区字符串
     */
    String queryNameByCode(Integer provCode,Integer cityCode,Integer distCode);

    /**
     * 根据name获取code
     * @param name name
     * @return code
     */
    Integer queryCodeByName(String name,Integer areaLevel);

    /**
     * 根据父级id查询地址信息
     * @param parentNo 父级id
     * @return 地址信息
     */
    List<AddressUtilPOJO> queryCityOrArea(Integer parentNo);
}
