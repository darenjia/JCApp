package com.bokun.bkjcb.on_siteinspection.Domain;

/**
 * Created by DengShuai on 2017/10/13.
 */

public class JingGao {


    /**
     * SysId : 0
     * SysGcxxdjh : 0
     * ScGgSzlc : 1
     * ScGgLcjg : 2.2～2.8M
     * SeqId : 0
     * Shengchan01 : 0
     * SysTime : 2017-10-12T00:00:00+08:00
     * IsChanged : true
     */

    private int SysId;
    private int ScGgSzlc;
    private String ScGgLcjg;

    public int getSysId() {
        return SysId;
    }

    public void setSysId(int sysId) {
        SysId = sysId;
    }

    public int getScGgSzlc() {
        return ScGgSzlc;
    }

    public void setScGgSzlc(int ScGgSzlc) {
        this.ScGgSzlc = ScGgSzlc;
    }

    public String getScGgLcjg() {
        return ScGgLcjg;
    }

    public void setScGgLcjg(String ScGgLcjg) {
        this.ScGgLcjg = ScGgLcjg;
    }
}
