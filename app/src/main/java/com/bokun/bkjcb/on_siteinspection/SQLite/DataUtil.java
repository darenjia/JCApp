package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.Context;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;
import com.bokun.bkjcb.on_siteinspection.Domain.FinishedPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.User;
import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.Utils.FileUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.orhanobut.logger.Logger;

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
                if (result.getId() != -1 && daolmpl.queryById(result.getIdentifier())) {
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

    public static ArrayList<CheckResult> readData(Context context, int Identifier) {
        CheckResultDaolmpl daolmpl = new CheckResultDaolmpl(context);
        CheckPlanDaolmpl planDaolmpl = new CheckPlanDaolmpl(context);
        ArrayList<CheckResult> results = new ArrayList<>();
        if (planDaolmpl.queryCheckPlan(Identifier) == null) {
            planDaolmpl.colseDateBase();
            return results;
        }
        results = daolmpl.queryCheckResult(Identifier);
        daolmpl.closeDatabase();
        LogUtil.logI("查询所有该计划的结果" + "identity" + Identifier + " size:" + results.size());
        return results;
    }

    public static void cleanData(Context context, int Identifier) {
        CheckResultDaolmpl daolmpl = new CheckResultDaolmpl(context);
        CheckPlanDaolmpl planDaolmpl = new CheckPlanDaolmpl(context);
        if (planDaolmpl.queryCheckPlan(Identifier) == null) {
            planDaolmpl.colseDateBase();
            return;
        }
        ArrayList<CheckResult> results = daolmpl.queryCheckResult(Identifier);
        FileUtils.deleteFile(results);
        daolmpl.clean(Identifier);
        daolmpl.closeDatabase();
    }

    public static void insertCheckPlans(Context context, ArrayList<CheckPlan> plans) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        for (CheckPlan plan : plans) {
            int state = daolmpl.queryCheckPlanIsNull(plan.getIdentifier());
            if (state == -1) {
                daolmpl.insertCheckPlan(plan);
                Logger.i(plan.getName());
            } else if (state == 0) {
                daolmpl.updateCheckPlan(plan);
            }
        }
        daolmpl.colseDateBase();
    }

    public static void insertCheckPlan(Context context, CheckPlan plan) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        int state = daolmpl.queryCheckPlanIsNull(plan.getIdentifier());
        if (state == -1) {
            daolmpl.insertCheckPlan(plan);
            // LogUtil.logI("加入一条检查计划" + plan.getIdentifier());
        } else if (state == 0) {
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

    public static ArrayList<ProjectPlan> queryProjectPlan(String state, int userId) {
        ProjectPlanDao dao = new ProjectPlanDao(JCApplication.getContext());
        ArrayList<ProjectPlan> plans = dao.queryNo(state, userId);
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

    public static int queryCheckPlanState(Context context, int indentifier) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        int state = daolmpl.queryCheckPlanState(indentifier);
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
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(JCApplication.getContext());
        dao.update(plan.getAq_lh_id(), plan.getAq_jctz_zt());
        String[] ids = plan.getAq_sysid().split(",");
        for (String id : ids) {
            flag = daolmpl.updateCheckPlanState(id, 2);
        }
        daolmpl.colseDateBase();
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
        ArrayList<ProjectPlan> projectPlans;
        ArrayList<CheckResult> checkResults = new ArrayList<>();
        projectPlans = dao.query("上传完成", user.getId());
        ArrayList<CheckPlan> checkPlans = new ArrayList<>();
        for (ProjectPlan p : projectPlans) {
            String[] strings = p.getAq_sysid().split(",");
            for (String s : strings) {
                checkPlans.add(daolmpl.queryCheckPlan(s));
//                daolmpl.delete(s);
            }
            dao.delete(p.getAq_lh_id());
        }
        for (CheckPlan c : checkPlans) {
            checkResults.addAll(daoR.queryCheckResult(c.getIdentifier()));
//            cleanData(JCApplication.getContext(), c.getIdentifier());
            daoR.clean(c.getIdentifier());
        }
        FileUtils.deleteFile(checkResults);

    }

    public static void insertUser(User user) {
        UserDao dao = new UserDao();
        if (!dao.getUserIs(user.getUserName())) {
            dao.addUser(user);
        } else {
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
}
