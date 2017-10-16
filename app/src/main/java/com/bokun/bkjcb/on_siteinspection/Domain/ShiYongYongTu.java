package com.bokun.bkjcb.on_siteinspection.Domain;

/**
 * Created by DengShuai on 2017/10/13.
 */

public class ShiYongYongTu {

    /**
     * SeqId : 0
     * SysId : 0
     * SysTime : 2017-10-12T00:00:00+08:00
     * SysGcxxdjh : 0
     * SyScSsmc : 汽车库
     * SyScJzmj : 1795
     * SyScCws : 70
     * SyScQtyt : null
     * SyScCz : 0
     * SyScRy : 0
     * SyScCw : 0
     * SyScBawh : null
     * SyScMr01 :
     * SyScMr02 :
     * SyScMr03 :
     * Shengchan01 : 0
     * IsChanged : true
     */

    private int SysId;
    private String SyScSsmc;
    private int SyScJzmj;
    private int SyScCws;
    private String SyScQtyt;

    public int getSysId() {
        return SysId;
    }

    public void setSysId(int sysId) {
        SysId = sysId;
    }

    public String getSyScSsmc() {
        return SyScSsmc;
    }

    public void setSyScSsmc(String SyScSsmc) {
        this.SyScSsmc = SyScSsmc;
    }

    public int getSyScJzmj() {
        return SyScJzmj;
    }

    public void setSyScJzmj(int SyScJzmj) {
        this.SyScJzmj = SyScJzmj;
    }

    public int getSyScCws() {
        return SyScCws;
    }

    public void setSyScCws(int SyScCws) {
        this.SyScCws = SyScCws;
    }

    public String getSyScQtyt() {
        return SyScQtyt;
    }

    public void setSyScQtyt(String SyScQtyt) {
        this.SyScQtyt = SyScQtyt;
    }
}
