package com.quxin.freshfun.model.pojo.address;

/**
 * Created by Ziming on 2016/11/14.
 */
public class AddressUtilPOJO {
    private Integer areaNo;
    private String areaName;
    private Integer parentNo;
    private String areaCode;
    private Byte areaLevel;
    private String typeName;

    public Integer getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(Integer areaNo) {
        this.areaNo = areaNo;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getParentNo() {
        return parentNo;
    }

    public void setParentNo(Integer parentNo) {
        this.parentNo = parentNo;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Byte getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Byte areaLevel) {
        this.areaLevel = areaLevel;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
