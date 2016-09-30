package com.quxin.freshfun.model.entity;

/**
 * Created by tianmingzhao on 16/9/30.
 */
public class BaseEntity {
    /** 分页offset */
    private Integer start;
    /** 每页的条数 */
    private Integer pageSize;
    /** 总页数 */
    private Integer total;
    private Long created;
    private Long updated;
    private Integer isDeleted = 0;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
