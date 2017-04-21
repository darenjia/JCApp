package com.bokun.bkjcb.on_siteinspection.Domain;

import java.io.Serializable;

/**
 * Created by BKJCB on 2017/3/31.
 * R.id.construction_name);
 * (R.id.construction_id);
 * R.id.construction_address);
 * R.id.construction_area);
 * R.id.construction_type);
 * (R.id.construction_tel);
 * (R.id.construction_manager);
 * TextView mViewUser
 */

public class CheckPlan implements Serializable {
    private int identifier;//计划编号
    private String name;//检查计划名称
    private int state;//四种状态，0：未检查,1:已检查，但为检查完，2：检查完，未上传，3：上传完成
    private String address;
    private String area;
    private String type;
    private String tel;
    private String manager;
    private String user;
    private int state_upload;//上传状态，0未开始，1等待开始,2,正在进行,3暂停,4已完成

    public CheckPlan() {
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getState_upload() {
        return state_upload;
    }

    public void setState_upload(int state_upload) {
        this.state_upload = state_upload;
    }
}
