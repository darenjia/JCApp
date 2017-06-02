package com.bokun.bkjcb.on_siteinspection.Domain;

import com.google.gson.JsonElement;

/**
 * Created by DengShuai on 2017/6/2.
 */

public class FinishedPlan {
    private int SysID;
    private int SysGcxxdjh;
    private String FinishedTime;
    private String Username;
    private String AQ_LH_ID;
    private JsonElement Result;

    public int getSysID() {
        return SysID;
    }

    public void setSysID(int sysID) {
        SysID = sysID;
    }

    public int getSysGcxxdjh() {
        return SysGcxxdjh;
    }

    public void setSysGcxxdjh(int sysGcxxdjh) {
        SysGcxxdjh = sysGcxxdjh;
    }

    public String getFinishedTime() {
        return FinishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        FinishedTime = finishedTime;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getAQ_LH_ID() {
        return AQ_LH_ID;
    }

    public void setAQ_LH_ID(String AQ_LH_ID) {
        this.AQ_LH_ID = AQ_LH_ID;
    }

    public JsonElement getResult() {
        return Result;
    }

    public void setResult(JsonElement result) {
        Result = result;
    }
}
