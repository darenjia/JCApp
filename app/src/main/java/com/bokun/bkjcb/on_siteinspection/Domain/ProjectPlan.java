package com.bokun.bkjcb.on_siteinspection.Domain;

import android.support.annotation.NonNull;

import com.google.gson.JsonElement;

import java.io.Serializable;

/**
 * Created by DengShuai on 2017/6/2.
 */

public class ProjectPlan implements Serializable, Comparable<ProjectPlan> {
    private int aq_lh_yyh;
    private int aq_lh_seqid;
    private String aq_lh_id;
    private String aq_lh_jcrq;
    private String aq_lh_allbj;
    private String aq_lh_jcmc;
    private String aq_lh_szqx;
    private JsonElement aq_lh_jchy;
    private String aq_lh_qxjd;
    private String aq_jctype;
    private String aq_sysid;
    private String aq_jctz_zt;
    private int AQ_JCTZ_sfjc;

    public int getAQ_JCTZ_sfjc() {
        return AQ_JCTZ_sfjc;
    }

    public void setAQ_JCTZ_sfjc(int AQ_JCTZ_sfjc) {
        this.AQ_JCTZ_sfjc = AQ_JCTZ_sfjc;
    }

    private int state_upload = 0;//上传状态，0未开始，1等待开始,2,正在进行,3暂停,4已完成

    public int getAq_lh_yyh() {
        return aq_lh_yyh;
    }

    public void setAq_lh_yyh(int aq_lh_yyh) {
        this.aq_lh_yyh = aq_lh_yyh;
    }

    public int getAq_lh_seqid() {
        return aq_lh_seqid;
    }

    public void setAq_lh_seqid(int aq_lh_seqid) {
        this.aq_lh_seqid = aq_lh_seqid;
    }

    public String getAq_lh_id() {
        return aq_lh_id;
    }

    public void setAq_lh_id(String aq_lh_id) {
        this.aq_lh_id = aq_lh_id;
    }

    public String getAq_lh_jcrq() {
        return aq_lh_jcrq;
    }

    public void setAq_lh_jcrq(String aq_lh_jcrq) {
        this.aq_lh_jcrq = aq_lh_jcrq;
    }

    public String getAq_lh_allbj() {
        return aq_lh_allbj;
    }

    public void setAq_lh_allbj(String aq_lh_allbj) {
        this.aq_lh_allbj = aq_lh_allbj;
    }

    public String getAq_lh_jcmc() {
        return aq_lh_jcmc;
    }

    public void setAq_lh_jcmc(String aq_lh_jcmc) {
        this.aq_lh_jcmc = aq_lh_jcmc;
    }

    public String getAq_lh_szqx() {
        return aq_lh_szqx;
    }

    public void setAq_lh_szqx(String aq_lh_szqx) {
        this.aq_lh_szqx = aq_lh_szqx;
    }

    public JsonElement getAq_lh_jchy() {
        return aq_lh_jchy;
    }

    public void setAq_lh_jchy(JsonElement aq_lh_jchy) {
        this.aq_lh_jchy = aq_lh_jchy;
    }

    public String getAq_lh_qxjd() {
        return aq_lh_qxjd;
    }

    public void setAq_lh_qxjd(String aq_lh_qxjd) {
        this.aq_lh_qxjd = aq_lh_qxjd;
    }

    public String getAq_jctype() {
        return aq_jctype;
    }

    public void setAq_jctype(String aq_jctype) {
        this.aq_jctype = aq_jctype;
    }

    public String getAq_sysid() {
        return aq_sysid;
    }

    public void setAq_sysid(String aq_sysid) {
        this.aq_sysid = aq_sysid;
    }

    public String getAq_jctz_zt() {
        return aq_jctz_zt;
    }

    public void setAq_jctz_zt(String aq_jctz_zt) {
        this.aq_jctz_zt = aq_jctz_zt;
    }

    public int getState_upload() {
        return state_upload;
    }

    public void setState_upload(int state_upload) {
        this.state_upload = state_upload;
    }

    @Override
    public int compareTo(@NonNull ProjectPlan o) {
        return this.getState_upload() > o.getState_upload() ? 1 : -1;
    }
}
