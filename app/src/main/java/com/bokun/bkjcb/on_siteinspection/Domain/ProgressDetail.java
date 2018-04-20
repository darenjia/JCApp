package com.bokun.bkjcb.on_siteinspection.Domain;

/**
 * Created by DengShuai on 2018/4/19.
 * Description :
 */

public class ProgressDetail {


    /**
     * aq_lh_yyh : 0
     * aq_lh_seqid : 0
     * aq_lh_allbj : 0
     * aq_gcxxdjh : 9050273
     * aq_gcmc : 福苑小区地下室
     * aq_gcdz : 宜山路655弄6号半幢
     * gzyh : 1.手提式灭火器过期，配备的灭火器数量少。
     * aq_fkzt : 1
     * fkfj : 无
     */

    private String aq_gcxxdjh; //工程登记号
    private String aq_gcmc; //工程名称
    private String aq_gcdz; //工程地址
    private String gzyh; //工程隐患
    private String fkfj; //反馈附件

    public String getAq_gcxxdjh() {
        return aq_gcxxdjh;
    }

    public void setAq_gcxxdjh(String aq_gcxxdjh) {
        this.aq_gcxxdjh = aq_gcxxdjh;
    }

    public String getAq_gcmc() {
        return aq_gcmc;
    }

    public void setAq_gcmc(String aq_gcmc) {
        this.aq_gcmc = aq_gcmc;
    }

    public String getAq_gcdz() {
        return aq_gcdz;
    }

    public void setAq_gcdz(String aq_gcdz) {
        this.aq_gcdz = aq_gcdz;
    }

    public String getGzyh() {
        return gzyh;
    }

    public void setGzyh(String gzyh) {
        this.gzyh = gzyh;
    }

    public String getFkfj() {
        return fkfj;
    }

    public void setFkfj(String fkfj) {
        this.fkfj = fkfj;
    }
}
