package com.bokun.bkjcb.on_siteinspection.SQLite;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;

import java.util.ArrayList;

/**
 * Created by BKJCB on 2017/3/20.
 */

public abstract class CheckResultDao {
    public abstract boolean insertCheckResult(CheckResult result);

    public abstract ArrayList<CheckResult> queryCheckResult(int Identifier);

    public abstract boolean queryById(int id);

    public abstract boolean updateCheckResult(CheckResult result);

    public abstract void clean(int id);

    public abstract void changeCheckResult(CheckResult result);
}
