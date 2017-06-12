package com.zjhj.commom.result;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/7.
 */
@Table(name="SearchHistory")
public class SearchHistory implements Serializable {

@Column(name = "id",isId = true)
    private int               id;
    @Column(name="keyword")
    private String            keyword;
    @Column(name = "createDate")
    private Date createDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}

