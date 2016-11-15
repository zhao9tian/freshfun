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
     * 获取省份信息
     * @return 省份信息
     */
    @Override
    public List<AddressUtilPOJO> queryProvince() {
        return addressUtilMapper.selectProvince();
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
