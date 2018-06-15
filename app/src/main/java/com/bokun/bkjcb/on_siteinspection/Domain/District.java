package com.bokun.bkjcb.on_siteinspection.Domain;

/**
 * Created by DengShuai on 2018/6/15.
 * Description :
 */
public class District {

    /**
     * name : 浦东新区
     * val : 浦东
     */

    private String name;
    private String val;

    public District(String name, String val) {
        this.name = name;
        this.val = val;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
