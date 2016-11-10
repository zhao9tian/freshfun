package com.quxin.freshfun.controller.activity;

import com.google.common.collect.Maps;
import com.quxin.freshfun.model.outparam.goods.GoodsOut;
import com.quxin.freshfun.service.goods.GoodsBaseService;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 双十一活动
 * Created by qucheng on 16/11/9.
 */
@Controller
@RequestMapping("/activity")
public class DoubleElevenController {

    @Autowired
    private GoodsBaseService goodsBaseService;


    /**
     * 双十一活动商品列表
     * @param goodsIdsParam 商品Id列表
     * @return 返回请求结果
     */
    @RequestMapping(value = "/queryGoodsByGoodsIds" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String , Object> queryActivityGoodsByGoodsId(@RequestBody Map<String , List<Integer>> goodsIdsParam) throws BusinessException {
        Map<String , Object> result;
        //校验商品Id -- 重复  存在
        try{
            List<Integer> goodsIds = goodsIdsParam.get("goodsIds");
            if(goodsIds != null && goodsIds.size() > 0){
                if(!validateRepeat(goodsIds)){
                    List<Map<String , Object>> goodsList = new ArrayList<>();
                    for(Integer goodsId : goodsIds){
                        Map<String , Object> goodsMap = goodsBaseService.findGoodsDetails(Long.valueOf(goodsId));
                        if(goodsMap == null){
                            return ResultUtil.fail(1004 , "id为:"+goodsId+"的商品不存在");
                        }else{
                            Map<String , Object> goods = Maps.newHashMap();
                            GoodsOut goodsOut = (GoodsOut) goodsMap.get("goods");
                            goods.put("goodsId" , goodsOut.getGoodsId());
                            goods.put("goodsTitle" , goodsOut.getGoodsName());
                            goods.put("goodsSubTitle" ,goodsOut.getGoodsDesc());
                            goods.put("goodsOriginPrice", goodsOut.getMarketMoney());
                            goods.put("goodsShopPrice" , goodsOut.getShopMoney());
                            goods.put("goodsImg" , goodsOut.getGoodsImg());
                            goodsList.add(goods);
                        }
                    }
                    Map<String , Object> data = Maps.newHashMap();
                    data.put("goodsList" , goodsList);
                    result = ResultUtil.success(data);
                }else{
                    result = ResultUtil.fail(1004 , "传入的商品Id有重复");
                }
            }else{
                result = ResultUtil.fail(1004 , "未传入商品Id");
            }
        }catch (ClassCastException e){
            return ResultUtil.fail(1004 , "商品Id类型不为整型");
        }
        return result;
    }


    /**
     * 检验id参数是否重复
     * @param ids 列表
     * @return 返回是否有重复 true : 重复了  false : 未重复
     */
    private Boolean validateRepeat(List<Integer> ids){
        Boolean isRepeat = false;
        if(ids != null && ids.size() > 0){
            HashSet set = new HashSet<>(ids);
            if (set.size() != ids.size()){
                isRepeat = true ;
            }
        }
        return isRepeat;
    }

}
