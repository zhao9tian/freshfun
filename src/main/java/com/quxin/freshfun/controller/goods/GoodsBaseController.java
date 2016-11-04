package com.quxin.freshfun.controller.goods;

import com.google.common.collect.Maps;
import com.quxin.freshfun.model.outparam.goods.BannerOut;
import com.quxin.freshfun.model.outparam.goods.GoodsOut;
import com.quxin.freshfun.model.outparam.goods.SpecialOut;
import com.quxin.freshfun.service.goods.GoodsBaseService;
import com.quxin.freshfun.utils.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingtian on 2016/10/26.
 */
@Controller
@RequestMapping("/goods")
public class GoodsBaseController {
    @Autowired
    private GoodsBaseService goodsBaseService;

    @RequestMapping("/getSpecialTheme")
    @ResponseBody
    public Map<String, Object> getSpecialTheme(Integer page, Integer pageSize) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> resultData = new HashMap<>();
        if (pageValidate(page, pageSize, map, resultMap)) return resultMap;

        int currentPage = (page-1)*pageSize;
        List<SpecialOut> specialTheme = goodsBaseService.getSpecialTheme(currentPage, pageSize);
        map.put("code", 1001);
        map.put("msg", "请求成功");
        resultMap.put("status", map);
        resultData.put("specialList",specialTheme);
        resultMap.put("data",resultData);
        return resultMap;
    }

    /**
     * 推荐接口
     *
     * @return
     */
    @RequestMapping("/recommendList")
    @ResponseBody
    public Map<String, Object> recommendList() throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> resultData = Maps.newHashMap();
        List<BannerOut> bannerList = goodsBaseService.getBannerList();
        List<GoodsOut> selectionList = goodsBaseService.getSelectionList();
        List<SpecialOut> specialList = goodsBaseService.getSpecialList();
        List<GoodsOut> goodsSort = goodsBaseService.getGoodsSortList();
        List<GoodsOut> goodsLimitList = goodsBaseService.getGoodsLimitList();
        map.put("code", 1001);
        map.put("msg", "请求成功");
        resultMap.put("status", map);
        resultData.put("bannerList",bannerList);
        resultData.put("selectionList",selectionList);
        resultData.put("specialList",specialList);
        resultData.put("goodsSort",goodsSort);
        resultData.put("limitGoodsList",goodsLimitList);
        resultMap.put("data", resultData);
        return resultMap;
    }

    @RequestMapping("/getGoodsList")
    @ResponseBody
    public Map<String,Object> getGoodsList(Integer page,Integer pageSize) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        if (pageValidate(page, pageSize, map, resultMap)) return resultMap;
        int currentPage = (page - 1) * pageSize;
        List<GoodsOut> goodsList = goodsBaseService.getGoodsList(currentPage, pageSize);
        if(goodsList == null){
            map.put("code", 1004);
            map.put("msg", "查询数据失败");
            resultMap.put("status", map);
            return resultMap;
        }
        map.put("code", 1001);
        map.put("msg", "请求成功");
        resultMap.put("status", map);
        resultMap.put("data",goodsList);
        return resultMap;
    }

    /**
     * 分页参数校验
     * @param page 当前页参数
     * @param pageSize  每页显示参数
     * @param map  状态集合
     * @param resultMap  返回集合
     * @return
     */
    private boolean pageValidate(Integer page, Integer pageSize, Map<String, Object> map, Map<String, Object> resultMap) {
        if (null == page || 0 == page || null == pageSize || 0 == pageSize) {
            map.put("code", 1004);
            map.put("msg", "传入当前页码不正确");
            resultMap.put("status", map);
            return true;
        }
        return false;
    }

    /**
     * 获取类目列表
     * @param categoryKey
     * @return 类目编号
     */
    @RequestMapping("/getCategory")
    @ResponseBody
    public Map<String,Object> getCategory(Integer categoryKey,Integer page,Integer pageSize) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> resultData = Maps.newHashMap();
        if (isParameter(categoryKey, map, resultMap)) return resultMap;
        if (pageValidate(page, pageSize, map, resultMap)) return resultMap;

        int currentPage = (page - 1) * pageSize;
        List<GoodsOut> category = goodsBaseService.getCategory(categoryKey,currentPage,pageSize);
        String categoryImg = null;
        if(page == 1) {
            categoryImg = goodsBaseService.getCategoryImg(categoryKey);
        }
        resultData.put("img",categoryImg);
        resultData.put("goodsList",category);
        map.put("code", 1001);
        map.put("msg", "请求成功");
        resultMap.put("status", map);
        resultMap.put("data",resultData);
        return resultMap;
    }

    @RequestMapping("/getSpecialDetails")
    @ResponseBody
    public Map<String,Object> getSpecialDetails(Long specialId) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        SpecialOut specialOut = goodsBaseService.getSpecialDetails(specialId);

        map.put("code", 1001);
        map.put("msg", "请求成功");
        resultMap.put("status", map);
        if(specialOut == null)
            resultMap.put("data","");
        else
            resultMap.put("data",specialOut);
        return resultMap;
    }

    /**
     * 查询商品详情
     * @param goodsId 商品编号
     * @return
     */
    @RequestMapping("/findGoodsDetails")
    @ResponseBody
    public Map<String,Object> findGoodsDetails(Long goodsId) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        if (resultError(goodsId, map, resultMap,"参数错误")) return resultMap;
        Map<String, Object> goodsDetailsList = goodsBaseService.findGoodsDetails(goodsId);
        if(goodsDetailsList == null){
            map.put("code", 1004);
            map.put("msg", "查询数据不存在或已下架");
            resultMap.put("status", map);
            return resultMap;
        }
        map.put("code", 1001);
        map.put("msg", "请求成功");
        resultMap.put("status", map);
        resultMap.put("data",goodsDetailsList);

        return resultMap;
    }

    private boolean resultError(Long goodsId, Map<String, Object> map, Map<String, Object> resultMap,String msg) {
        if(goodsId == null){
            map.put("code", 1004);
            map.put("msg", msg);
            resultMap.put("status", map);
            return true;
        }
        return false;
    }

    /**
     * 参数校验
     * @return
     */
    private boolean isParameter(Integer bannerId, Map<String, Object> map, Map<String, Object> resultMap) {
        if(bannerId == null || bannerId == 0){
            map.put("code", 1004);
            map.put("msg", "参数错误");
            resultMap.put("status", map);
            return true;
        }
        return false;
    }


}
