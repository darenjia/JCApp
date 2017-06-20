package com.bokun.bkjcb.on_siteinspection.SQLite;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;

import java.util.ArrayList;

/**
 * Created by BKJCB on 2017/3/31.
 */

public abstract class CheckPlanDao {
    public abstract void insertCheckPlan(CheckPlan plan);

    public abstract boolean updateCheckPlan(CheckPlan plan);

    public abstract CheckPlan queryCheckPlan(int identifier);

    public abstract CheckPlan queryCheckPlan(String sysID);

    public abstract boolean queryCheckPlanIsNull(int identifier);

    public abstract ArrayList<CheckPlan> queryCheckPlan();

    public abstract ArrayList<CheckPlan> query(String name);

    public abstract ArrayList<CheckPlan> queryFinishedCheckPlan();

    public abstract ArrayList<CheckPlan> queryCanUpLoadCheckPlan();

    public abstract boolean updateCheckPlanState(CheckPlan plan);
}
