package com.example.administrator.base;

/**
 * 左边底部类别服务 item的信息实体类
 * Created by Administrator on 2014/12/16.
 */
public class ItemCategoryModel {
    private Integer id;//图标资源的id
    private String name;// 列表Item的名称
    private String e_name;//列表Item的英文名称

    public ItemCategoryModel() {
        super();
    }

    public ItemCategoryModel(Integer id, String name, String e_name) {
        super();
        this.id = id;
        this.name = name;
        this.e_name = e_name;
    }

    /*得到图片资源ID的方法*/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return e_name;
    }

    public void setTitle(String e_name) {
        this.e_name = e_name;
    }

    @Override
    public String toString() {
        return "ItemCategoryModel [id=" + id + ", name=" + name + ", e_name="
                + e_name + "]";
    }

}
