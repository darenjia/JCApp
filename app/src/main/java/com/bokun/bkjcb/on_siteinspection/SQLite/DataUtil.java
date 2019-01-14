package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;
import com.bokun.bkjcb.on_siteinspection.Domain.FinishedPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.User;
import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.Utils.FileUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.elvishew.xlog.XLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BKJCB on 2017/3/31.
 */

public class DataUtil {

    public static boolean saveData(Context context, List<CheckResult> results) {
        CheckResultDaolmpl daolmpl = new CheckResultDaolmpl(context);
        CheckResult result;
        try {
            for (int i = 0; i < results.size(); i++) {
                result = results.get(i);
                if (daolmpl.queryById(result.getIdentifier(), result.getAq_lh_id(),result.getNum())) {
                    daolmpl.updateCheckResult(result);
                } else {
                    daolmpl.insertCheckResult(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            daolmpl.closeDatabase();
            return false;
        }
        daolmpl.closeDatabase();
        return true;
    }

    public static boolean saveData(Context context, CheckResult result) {
        CheckResultDaolmpl daolmpl = new CheckResultDaolmpl(context);
        try {

            if (daolmpl.queryById(result.getIdentifier(), result.getAq_lh_id(),result.getNum())) {
                daolmpl.updateCheckResult(result);
            } else {
                daolmpl.insertCheckResult(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            daolmpl.closeDatabase();
            return false;
        }
        daolmpl.closeDatabase();
        return true;
    }

    public static ArrayList<CheckResult> readData(Context context, int sysId, String aq_lh_id) {
        CheckResultDaolmpl daolmpl = new CheckResultDaolmpl(context);
        CheckPlanDaolmpl planDaolmpl = new CheckPlanDaolmpl(context);
        ArrayList<CheckResult> results = new ArrayList<>();
        if (planDaolmpl.queryCheckPlan(sysId) == null) {
            planDaolmpl.colseDateBase();
            return results;
        }
        results = daolmpl.queryCheckResult(sysId, aq_lh_id);
        daolmpl.closeDatabase();
        LogUtil.logI("查询所有该计划的结果" + "identity" + sysId + " size:" + results.size());
        return results;
    }

    public static void cleanData(Context context, int sysId, String aq_lh_id_) {
        CheckResultDaolmpl daolmpl = new CheckResultDaolmpl(context);
        CheckPlanDaolmpl planDaolmpl = new CheckPlanDaolmpl(context);
        if (planDaolmpl.queryCheckPlan(sysId) == null) {
            planDaolmpl.colseDateBase();
            return;
        }
        ArrayList<CheckResult> results = daolmpl.queryCheckResult(sysId, aq_lh_id_);
        FileUtils.deleteFile(results);
        if (results.size() > 0) {
            daolmpl.clean(sysId, results.get(0).getAq_lh_id());
        }
        daolmpl.closeDatabase();
    }

    public static void insertCheckPlans(Context context, ArrayList<CheckPlan> plans) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        for (CheckPlan plan : plans) {
            String state = daolmpl.queryCheckPlanIsNull(plan.getSysId());
            if (state == null) {
                daolmpl.insertCheckPlan(plan);
                XLog.i("新增计划" + plan.getName());
            } else if (state.equals("")) {
                daolmpl.updateCheckPlan(plan);
            } else {
                Utils.deleteFile(state);
                daolmpl.updateCheckPlan(plan);
            }
        }
        daolmpl.colseDateBase();
    }

    public static void insertCheckPlan(Context context, CheckPlan plan) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        String state = daolmpl.queryCheckPlanIsNull(plan.getIdentifier());
        if (state == null) {
            daolmpl.insertCheckPlan(plan);
            // LogUtil.logI("加入一条检查计划" + plan.getIdentifier());
        } else if (state.equals("")) {
            daolmpl.updateCheckPlan(plan);
        }
    }

    public static CheckPlan queryCheckPlan(Context context, int indentifier) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        CheckPlan plan = daolmpl.queryCheckPlan(indentifier);
        //LogUtil.logI("查询一条检查计划" + plan.getIdentifier() + " state:" + plan.getState());
        daolmpl.colseDateBase();
        return plan;
    }

    public static ArrayList<CheckPlan> queryCheckPlan(Context context) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        ArrayList<CheckPlan> plans = daolmpl.queryCheckPlan();
        daolmpl.colseDateBase();
        LogUtil.logI("查询所有检查计划" + plans.size());
        return plans;
    }

    public static ArrayList<CheckPlan> queryCheckPlan(String name) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(JCApplication.getContext());
        ArrayList<CheckPlan> plans = daolmpl.query(name);
        daolmpl.colseDateBase();
        LogUtil.logI("查询所有检查计划" + plans.size());
        return plans;
    }

    public static ArrayList<ProjectPlan> queryProjectPlan(String state) {
        ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
        ArrayList<ProjectPlan> plans = dao.queryNo(state);
        dao.close();
        return plans;
    }

    public static ArrayList<ProjectPlan> queryProjectPlan(int userId) {
        ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
        ArrayList<ProjectPlan> plans = dao.queryNo(userId);
        dao.close();
        return plans;
    }

    public static ProjectPlan queryProjectPlanById(String id) {
        ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
        ProjectPlan plan = dao.queryById(id);
        dao.close();
        return plan;
    }

    public static ArrayList<CheckPlan> queryCheckPlanFinished(Context context) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        ArrayList<CheckPlan> plans = daolmpl.queryFinishedCheckPlan();
        daolmpl.colseDateBase();
        LogUtil.logI("查询未完成检查计划" + plans.size());
        return plans;
    }

    public static ArrayList<CheckPlan> queryCheckPlanCanUpLoad(Context context) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        ArrayList<CheckPlan> plans = daolmpl.queryCanUpLoadCheckPlan();
        daolmpl.colseDateBase();
        LogUtil.logI("查询可上传检查计划" + plans.size());
        return plans;
    }

    public static int queryCheckPlanState(Context context, int sysId, int plan_type) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        int state = daolmpl.queryCheckPlanState(sysId, plan_type);
        daolmpl.colseDateBase();
        // LogUtil.logI("查询一条检查计划状态" + indentifier + " state:" + state);
        return state;
    }

    public static boolean updateCheckPlan(Context context, CheckPlan plan) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        boolean is = daolmpl.updateCheckPlan(plan);
        daolmpl.colseDateBase();
        LogUtil.logI("更新一条检查计划" + plan.getIdentifier() + " state:" + plan.getState());
        return is;
    }

    public static boolean updateCheckPlanState(Context context, CheckPlan plan) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        boolean is = daolmpl.updateCheckPlanState(plan);
        daolmpl.colseDateBase();
        LogUtil.logI("更新一条检查计划状态" + plan.getIdentifier() + " state:" + plan.getState());
        return is;
    }

    public static ArrayList<CheckPlan> getCheckPlan(String[] ids) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(JCApplication.getContext());
        ArrayList<CheckPlan> plans = new ArrayList<>();
        CheckPlan plan;
        for (String id : ids) {
            plan = daolmpl.queryCheckPlan(id);
            if (plan != null) {
                plans.add(plan);
            }
        }
        daolmpl.colseDateBase();
        return plans;
    }

    public static FinishedPlan getFinishedPlan(String SysId) {
        FinishedPlanDao dao = new FinishedPlanDao(JCApplication.getContext());
        FinishedPlan plan = dao.query(SysId);
        dao.close();
        return plan;
    }

    public static boolean saveFinishedPlan(FinishedPlan plan) {
        boolean flag = false;
        FinishedPlanDao dao = new FinishedPlanDao(JCApplication.getContext());
        if (dao.queryById(String.valueOf(plan.getSysID()))) {
            flag = dao.save(plan);
        } else {
            flag = dao.update(plan);
        }
        dao.close();
        return flag;
    }

    public static ArrayList<ProjectPlan> getProjectByState(String state, User user) {
        ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
        ArrayList<ProjectPlan> list = dao.query(state, user.getId());
        dao.close();
        return list;
    }

    public static boolean saveProjectPlan(ArrayList<ProjectPlan> plans, User user) {
        if (plans.size() > 0) {
            ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
            for (ProjectPlan p : plans) {
                if (p.getAQ_JCTZ_sfjc() == 1 || p.getAQ_JCTZ_sfjc() == 0) {
                    continue;
                }
                if (p.getAQ_JCTZ_sfjc() == 4) {
                    deleteProjectPlan(p);
                    continue;
                }
                String id = dao.issaved(p.getAq_lh_seqid());
                if (id == null) {
                    dao.save(p, user.getId());
                } else {
                    dao.update(id, p, user);
                }
            }
            dao.close();
        } else {
            return false;
        }
        return true;
    }

    public static boolean saveProjectPlan(ProjectPlan plan, User user) {
        if (plan != null) {
            ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
            String id = dao.issaved(plan.getAq_lh_seqid());
            if (id == null) {
                dao.save(plan, user.getId());
            } else {
                dao.update(id, plan, user);
            }
            dao.close();
        } else {
            return false;
        }
        return true;
    }

    /**
     * 更新安全检查状态
     */
    public static boolean changeProjectState(ProjectPlan plan) {
        boolean flag = false;
        ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(JCApplication.getContext());
        dao.update(plan.getAq_lh_id(), plan.getAq_jctz_zt());
        String[] ids = plan.getAq_sysid().split(",");
        for (String id : ids) {
            flag = daolmpl.updateCheckPlanState(id, 3);
        }
        daolmpl.colseDateBase();
        dao.close();
        return flag;
    }

    public static boolean changeProjectState1(ProjectPlan plan) {
        boolean flag = false;
        ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
//        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(JCApplication.getContext());
        dao.update(plan.getAq_lh_id(), plan.getAq_jctz_zt());
       /* String[] ids = plan.getAq_sysid().split(",");
        for (String id : ids) {
            flag = daolmpl.updateCheckPlanState(id, 2);
        }
        daolmpl.colseDateBase();*/
        dao.close();
        return flag;
    }

    public static boolean updateProjectState(ProjectPlan plan) {
        boolean flag = false;
        ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(JCApplication.getContext());
        dao.update(plan.getAq_lh_id(), plan.getAq_jctz_zt());
        dao.close();
        return flag;
    }

    public static void deleteFinishedProjectPlan(User user) {
        ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(JCApplication.getContext());
        CheckResultDaolmpl daoR = new CheckResultDaolmpl(JCApplication.getContext());
        FinishedPlanDao finishedPlanDao = new FinishedPlanDao(JCApplication.getContext());
        ArrayList<ProjectPlan> projectPlans;
        projectPlans = dao.query("上传完成", user.getId());
        ArrayList<CheckPlan> checkPlans = new ArrayList<>();
        for (ProjectPlan p : projectPlans) {
            String[] strings = p.getAq_sysid().split(",");
            for (String s : strings) {
                checkPlans.clear();
                checkPlans.add(daolmpl.queryCheckPlan(s));
//                daolmpl.delete(s);
                if (checkPlans.size() > 0) {
                    for (CheckPlan c : checkPlans) {
                        FinishedPlan plan = DataUtil.getFinishedPlan(String.valueOf(c.getSysId()));
                        FileUtils.deleteFile(daoR.queryCheckResult(c.getSysId(), p.getAq_lh_id()));
                        daolmpl.delete(String.valueOf(c.getSysId()), String.valueOf(plan.getType()));
                        finishedPlanDao.deleteFinished(String.valueOf(c.getSysId()), p.getAq_lh_id());
                        daoR.clean(c.getSysId(), p.getAq_lh_id());
                    }
                }
            }
            dao.delete(p.getAq_lh_id());
        }
        /*if (projectPlans.size() > 0) {
            for (CheckPlan c : checkPlans) {
                checkResults.addAll(daoR.queryCheckResult(c.getIdentifier(), projectPlans.get(0).getAq_lh_id()));
//            cleanData(JCApplication.getContext(), c.getIdentifier());
                if (checkResults.size() > 0) {
                    FinishedPlan plan = DataUtil.getFinishedPlan(String.valueOf(c.getSysId()));
                    daoR.clean(c.getIdentifier(), checkResults.get(0).getAq_lh_id());
                    daolmpl.delete(String.valueOf(c.getSysId()), String.valueOf(plan.getType()));
                    finishedPlanDao.deleteFinished(c.getSysId());
                }
            }
        }
        FileUtils.deleteFile(checkResults);*/

    }

    public static void deleteProjectPlan(ProjectPlan p) {
        ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(JCApplication.getContext());
        CheckResultDaolmpl daoR = new CheckResultDaolmpl(JCApplication.getContext());
        FinishedPlanDao finishedPlanDao = new FinishedPlanDao(JCApplication.getContext());
        ArrayList<CheckPlan> checkPlans = new ArrayList<>();
        String[] strings = p.getAq_sysid().split(",");
        for (String s : strings) {
            checkPlans.add(daolmpl.queryCheckPlan(s));
            if (checkPlans.size() > 0) {
                for (CheckPlan c : checkPlans) {
                    if (c == null) {
                        continue;
                    }
                    FinishedPlan plan = DataUtil.getFinishedPlan(String.valueOf(c.getSysId()));
                    if (plan == null) {
                        continue;
                    }
                    FileUtils.deleteFile(daoR.queryCheckResult(c.getSysId(), p.getAq_lh_id()));
                    daolmpl.delete(String.valueOf(c.getSysId()), String.valueOf(plan.getType()));
                    finishedPlanDao.deleteFinished(String.valueOf(c.getSysId()), p.getAq_lh_id());
                    daoR.clean(c.getSysId(), p.getAq_lh_id());
                }
            }
            dao.delete(p.getAq_lh_id());
        }
    }

    public static void insertUser(User user) {
        UserDao dao = new UserDao();
        if (!dao.getUserIs(user.getUserName())) {
            dao.addUser(user);
        } else {
            dao.updateUser(user);
            user.setId(dao.getUser(user.getUserName()).getId());

        }
        dao.close();
    }

    public static User getUser(String name) {
        UserDao dao = new UserDao();
        User user = dao.getUser(name);
        dao.close();
        return user;
    }

    public static String queryFileUrl(String id) {
        CheckPlanDaolmpl dao = new CheckPlanDaolmpl(JCApplication.getContext());
        return dao.queryCheckPlanFileUrl(id);
    }

    public static ProjectPlan queryCheckPlanIsFinished(CheckPlan checkPlan) {
        ProjectPlan plan;
        ProjectPlanDao planDao = new ProjectPlanDao(JCApplication.getContext());
        plan = planDao.queryBySysID(String.valueOf(checkPlan.getSysId()));
        return plan;
    }

    public static void initCheckResult(SQLiteDatabase db) {
        ArrayList<CheckPlan> list = queryByCheckPlanState(db);
        for (CheckPlan plan : list) {
            String aq_lh_id = queryAq_lh_id(db, plan.getSysId());
            if (aq_lh_id == null) {
                continue;
            }
            changeCheckResult(db, String.valueOf(plan.getSysId()), aq_lh_id);
        }
    }

    private static ArrayList<CheckPlan> queryByCheckPlanState(SQLiteDatabase db) {
        CheckPlan plan = null;
        ArrayList<CheckPlan> checkPlans = new ArrayList<>();
        Cursor cursor = db.query("checkplan", null, "state = 1 or state = 2", null, null, null, null);
        while (cursor.moveToNext()) {
            plan = new CheckPlan();
            plan.setIdentifier(cursor.getInt(cursor.getColumnIndex("identifier")));
            plan.setState(cursor.getInt(cursor.getColumnIndex("state")));
            plan.setSysId(cursor.getInt(cursor.getColumnIndex("sysId")));
            checkPlans.add(plan);
        }
        cursor.close();
        return checkPlans;
    }

    private static String queryAq_lh_id(SQLiteDatabase db, int sysId) {
        Cursor cursor = db.query("constructioninfo", null, "aq_sysid like ?", new String[]{"%" + sysId + "%"}, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex("aq_lh_id"));
        }
        cursor.close();
        return null;
    }

    private static void changeCheckResult(SQLiteDatabase db, String identifier, String aq_lh_id) {
        ContentValues values = new ContentValues();
        values.put("aq_lh_id", aq_lh_id);
        db.update("checkresult", values, "identifier = ?", new String[]{identifier});
    }

}
