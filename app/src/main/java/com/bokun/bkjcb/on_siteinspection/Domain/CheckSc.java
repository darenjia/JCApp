package com.bokun.bkjcb.on_siteinspection.Domain;

import java.io.Serializable;

/**
 * Created by DengShuai on 2017/5/25.
 */

public class CheckSc implements Serializable {
    private int SysId;//系统ID
    private int SysGcxxdjh;//工程信息登记号
    private String SysSsmc;//是什么车
    private String SysQuXian;//区县
    private String ScJbGcmc;//上传基本工程名称
    private String ScJbGcdz;//上传基本工程地址
    private double ScXzJzmj;//面积
    private int state;//四种状态，0：未检查,1:已检查，但为检查完，2：检查完，未上传，3：上传完成
    private int state_upload;//上传状态，0未开始，1等待开始,2,正在进行,3暂停,4已完成

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState_upload() {
        return state_upload;
    }

    public void setState_upload(int state_upload) {
        this.state_upload = state_upload;
    }

    public int getSysId() {
        return SysId;
    }

    public void setSysId(int sysId) {
        SysId = sysId;
    }

    public int getSysGcxxdjh() {
        return SysGcxxdjh;
    }

    public void setSysGcxxdjh(int sysGcxxdjh) {
        SysGcxxdjh = sysGcxxdjh;
    }

    public String getSysSsmc() {
        return SysSsmc;
    }

    public void setSysSsmc(String sysSsmc) {
        SysSsmc = sysSsmc;
    }

    public String getSysQuXian() {
        return SysQuXian;
    }

    public void setSysQuXian(String sysQuXian) {
        SysQuXian = sysQuXian;
    }

    public String getScJbGcmc() {
        return ScJbGcmc;
    }

    public void setScJbGcmc(String scJbGcmc) {
        ScJbGcmc = scJbGcmc;
    }

    public String getScJbGcdz() {
        return ScJbGcdz;
    }

    public void setScJbGcdz(String scJbGcdz) {
        ScJbGcdz = scJbGcdz;
    }

    public double getScXzJzmj() {
        return ScXzJzmj;
    }

    public void setScXzJzmj(double scXzJzmj) {
        ScXzJzmj = scXzJzmj;
    }
}
