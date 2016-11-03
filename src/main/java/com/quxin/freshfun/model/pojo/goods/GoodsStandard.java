package com.quxin.freshfun.model.pojo.goods;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by qingtian on 2016/10/26.
 *  商品规格表
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoodsStandard {
    private Integer id;
    /**
     * 商品编号
     */
    private Long goodsId;
    /**
     * 品名
     */
    private String name;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 产地
     */
    private String productPlace;
    /**
     * 规格
     */
    private String goodsStandard;
    /**
     * 净含量（数字＋中英文文字自定义，单位：ml、g、kg)
     */
    private String netContents;
    /**
     * 保质期(数字 单位：天、月、年）
     */
    private String shelfLife;
    /**
     * 储藏方式
     */
    private String storageMethod;
    /**
     * 配料表
     */
    private String ingredientList;
    /**
     * 是否含糖   1: 是 2:否
     */
    private String isSugary;
    /**
     * 是否有机  1: 是 2:否
     */
    private String isOrganic;
    /**
     * 是否进口  1: 是 2:否
     */
    private String isImported;
    /**
     * 是否盒装  1: 是 2:否
     */
    private String isBoxPacked;
    /**
     * 套餐份量（数字，单位：人份）
     */
    private String packageComponent;
    /**
     * 口味
     */
    private String taste;
    /**
     * 功能
     */
    private String facility;
    /**
     * 不适宜人群
     */
    private String unsuitable;
    /**
     * 适宜人群
     */
    private String suitable;
    /**
     * 产品剂型
     */
    private String productForm;
    /**
     * 食品添加剂
     */
    private String foodAdditives;
    /**
     * 套餐周期（单位：周、月、年）
     */
    private String setCycle;
    /**
     * 厂名
     */
    private String factoryName;
    /**
     * 厂址
     */
    private String factorySite;
    /**
     * 产品标准号
     */
    private String productStandardNum;
    /**
     * 生鲜储存温度（数字，单位：℃）
     */
    private String freshStoreTemp;
    /**
     * 酒精度数
     */
    private String proof;
    /**
     * 度数（数字，单位：%vol）
     */
    private String degree;
    /**
     * 适用场景
     */
    private String adaptiveScene;
    /**
     * 包装方式
     */
    private String packingMethod;
    /**
     * 包装种类
     */
    private String packingType;
    /**
     * 葡萄酒种类
     */
    private String wineStyle;
    /**
     * 套装规格（数字，单位:件装 或自定义）
     */
    private String suitSpecification;
    /**
     * 醒酒时间（数字 单位：分钟）
     */
    private String decanteDuration;
    /**
     * 年份（数字＋文字自定义，1年以上、3年以下、5年及以上
     */
    private String particularYear;
    /**
     * 香味
     */
    private String smell;
    /**
     * 颜色分类
     */
    private String colourSort;
    /**
     * 风格类型
     */
    private String styleType;
    /**
     * 尺寸
     */
    private String size;
    /**
     * 特产品种
     */
    private String specialty;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductPlace() {
        return productPlace;
    }

    public void setProductPlace(String productPlace) {
        this.productPlace = productPlace;
    }

    public String getGoodsStandard() {
        return goodsStandard;
    }

    public void setGoodsStandard(String goodsStandard) {
        this.goodsStandard = goodsStandard;
    }

    public String getNetContents() {
        return netContents;
    }

    public void setNetContents(String netContents) {
        this.netContents = netContents;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getStorageMethod() {
        return storageMethod;
    }

    public void setStorageMethod(String storageMethod) {
        this.storageMethod = storageMethod;
    }

    public String getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(String ingredientList) {
        this.ingredientList = ingredientList;
    }

    public String getIsSugary() {
        return isSugary;
    }

    public void setIsSugary(String isSugary) {
        this.isSugary = isSugary;
    }

    public String getIsOrganic() {
        return isOrganic;
    }

    public void setIsOrganic(String isOrganic) {
        this.isOrganic = isOrganic;
    }

    public String getIsImported() {
        return isImported;
    }

    public void setIsImported(String isImported) {
        this.isImported = isImported;
    }

    public String getIsBoxPacked() {
        return isBoxPacked;
    }

    public void setIsBoxPacked(String isBoxPacked) {
        this.isBoxPacked = isBoxPacked;
    }

    public String getPackageComponent() {
        return packageComponent;
    }

    public void setPackageComponent(String packageComponent) {
        this.packageComponent = packageComponent;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getUnsuitable() {
        return unsuitable;
    }

    public void setUnsuitable(String unsuitable) {
        this.unsuitable = unsuitable;
    }

    public String getSuitable() {
        return suitable;
    }

    public void setSuitable(String suitable) {
        this.suitable = suitable;
    }

    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
    }

    public String getFoodAdditives() {
        return foodAdditives;
    }

    public void setFoodAdditives(String foodAdditives) {
        this.foodAdditives = foodAdditives;
    }

    public String getSetCycle() {
        return setCycle;
    }

    public void setSetCycle(String setCycle) {
        this.setCycle = setCycle;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getFactorySite() {
        return factorySite;
    }

    public void setFactorySite(String factorySite) {
        this.factorySite = factorySite;
    }

    public String getProductStandardNum() {
        return productStandardNum;
    }

    public void setProductStandardNum(String productStandardNum) {
        this.productStandardNum = productStandardNum;
    }

    public String getFreshStoreTemp() {
        return freshStoreTemp;
    }

    public void setFreshStoreTemp(String freshStoreTemp) {
        this.freshStoreTemp = freshStoreTemp;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getAdaptiveScene() {
        return adaptiveScene;
    }

    public void setAdaptiveScene(String adaptiveScene) {
        this.adaptiveScene = adaptiveScene;
    }

    public String getPackingMethod() {
        return packingMethod;
    }

    public void setPackingMethod(String packingMethod) {
        this.packingMethod = packingMethod;
    }

    public String getPackingType() {
        return packingType;
    }

    public void setPackingType(String packingType) {
        this.packingType = packingType;
    }

    public String getWineStyle() {
        return wineStyle;
    }

    public void setWineStyle(String wineStyle) {
        this.wineStyle = wineStyle;
    }

    public String getSuitSpecification() {
        return suitSpecification;
    }

    public void setSuitSpecification(String suitSpecification) {
        this.suitSpecification = suitSpecification;
    }

    public String getDecanteDuration() {
        return decanteDuration;
    }

    public void setDecanteDuration(String decanteDuration) {
        this.decanteDuration = decanteDuration;
    }

    public String getParticularYear() {
        return particularYear;
    }

    public void setParticularYear(String particularYear) {
        this.particularYear = particularYear;
    }

    public String getSmell() {
        return smell;
    }

    public void setSmell(String smell) {
        this.smell = smell;
    }

    public String getColourSort() {
        return colourSort;
    }

    public void setColourSort(String colourSort) {
        this.colourSort = colourSort;
    }

    public String getStyleType() {
        return styleType;
    }

    public void setStyleType(String styleType) {
        this.styleType = styleType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
