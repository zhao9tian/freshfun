package com.quxin.freshfun.service.impl.address;

import com.quxin.freshfun.dao.AddressUtilMapper;
import com.quxin.freshfun.model.pojo.address.AddressUtilPOJO;
import com.quxin.freshfun.service.address.AddressUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ASus on 2016/11/14.
 */
@Service("addressUtilService")
public class AddressUtilServiceImpl implements AddressUtilService {

    @Autowired
    private AddressUtilMapper addressUtilMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 根据codes获取省市区字符串
     * @param provCode 省级code
     * @param cityCode 市级code
     * @param distCode 县区级code
     * @return 省市区字符串
     */
    public String queryNameByCode(Integer provCode,Integer cityCode,Integer distCode) {
        if(provCode==null||provCode==0||cityCode==null||cityCode==0||distCode==null||distCode==0){
            logger.warn("根据codes获取省市区字符串,入参有误");
            return "";
        }
        return addressUtilMapper.selectNameByCode(provCode)+addressUtilMapper.selectNameByCode(cityCode)+addressUtilMapper.selectNameByCode(distCode);
    }

    @Override
    public Integer queryCodeByName(String name) {
        if(name==null||"".equals(name)){
            logger.warn("根据codes获取省市区字符串,入参有误");
            return 0;
        }
        return addressUtilMapper.selectCodeByName(name);
    }

    /**
     * 根据父级id查询地址信息
     * @param parentNo 父级id
     * @return 地址信息
     */
    @Override
    public List<AddressUtilPOJO> queryCityOrArea(Integer parentNo) {
        if(parentNo==null){
            logger.warn("根据父级id查询地址信息,入参有误");
            return null;
        }
        return addressUtilMapper.selectCityArea(parentNo);
    }
}
