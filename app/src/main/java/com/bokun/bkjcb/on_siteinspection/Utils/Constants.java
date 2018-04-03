package com.bokun.bkjcb.on_siteinspection.Utils;

/**
 * Created by BKJCB on 2017/3/16.
 * 系统常量
 */

public class Constants {

    public final static int NETWORK_WIFI = 1;
    public final static int NETWORK_MOBILE = 2;
    public final static boolean ISLOG = true;
    //用户表
    public final static String CREATE_USER_TABLE = "create table UserInfo(" +
            "id Integer primary key," +
            "name varchar(10)," +
            "password char(30)," +
            "quxian char(10)," +
            "roles char(50)," +
            "sys_realname char(20)" +
            ")";
    //检查计划表
    public final static String CREATE_PLAN_TABLE = "create table PlanInfo(" +
            "id Integer primary key," +
            "name varchar(30)" +
            "username char(30)" +
            "lastupdatetime long" +
            "describtion text" +
            "state Bit" +
            ")";
    //工程信息表
    public final static String CREATE_CONSTRUCTION_TABLE = "create table constructioninfo(" +
            "id Integer primary key," +
            "aq_lh_seqid int(20)," +
            "aq_lh_yyh int(20)," +
            "aq_lh_id char(30)," +
            "aq_lh_allbj char(30)," +
            "aq_lh_jcrq char(30)," +
            "aq_lh_jcmc char(30)," +
            "aq_lh_szqx char(100)," +
            "aq_lh_qxjd char(30)," +
            "aq_lh_jchy char(30)," +
            "aq_jctype char(30)," +
            "aq_sysid char(100)," +
            "aq_jctz_zt char(30)," +
            "AQ_JCTZ_sfjc int(5)," +
            "userId int(5)" +
            ")";

    public final static String CREATE_CHECK_PLAN_TABLE = "create table checkplan(" +
            "identifier Integer primary key," +
            "sysId int(12)," +
            "name char(30)," +
            "state int(1)," +
            "address varchar(50)," +
            "quxian varchar(10)," +
            "area varchar(20)," +
            "type varchar(20)," +
            "tel varchar(20)," +
            "manager varchar(50)," +
            "user varchar(30)," +
            "url varchar(50)," +
            "plan_type int(8)" +
            ")";
    public final static String CREATE_FINISHED_PALN = "create table finishedplan(" +
            "id Integer primary key," +
            "SysId int(20)," +
            "SysGcxxdjh int(20)," +
            "FinishedTime char(20)," +
            "Username char(10)," +
            "AQ_LH_ID char(20)," +
            "Type int(8)" +
            ")";
    public final static String CREATE_CHECK_RESULT = "create table checkresult(" +
            "id Integer primary key," +
            "identifier int(10)," +
            "num int(10)," +
            "checkresult int(1)," +
            "comment text," +
            "audio char(100)," +
            "image char(100)," +
            "video char(100)," +
            "aq_lh_id char(30)" +
            ")";
    public final static String CREATE_SEARCH_HISTORY = "create table searchhistory(" +
            "id Integer primary key," +
            "name varchar(20)," +
            "type int(1)" +
            ")";
    //修改checkplan表
    public final static String CREATE_TEMP_TABLE = "alter table checkplan rename to _temp_checkplan";
    public final static String CREATE_NEW_CHECKPALN = "create table checkplan(" +
            "identifier Integer primary key," +
            "sysId int(12)," +
            "name char(30)," +
            "state int(1)," +
            "address varchar(50)," +
            "quxian varchar(10)," +
            "area varchar(20)," +
            "type varchar(20)," +
            "tel varchar(20)," +
            "manager varchar(50)," +
            "user varchar(30)," +
            "url varchar(50)" +
            ")";
    public final static String INSERT_DATA_CHECKPLAN = "insert into checkplan select *,'' from _temp_checkplan";
    public final static String DROP_TEMP = "drop table _temp_checkplan";

    //修改checkresult表
    public final static String CREATE_TEMP_CHECK_RESULT = "alter table checkresult rename to _temp_checkresult";
    public final static String CREATE_NEW_CHECK_RESULT = "create table checkresult(" +
            "id Integer primary key," +
            "identifier int(10)," +
            "num int(10)," +
            "checkresult int(1)," +
            "comment text," +
            "audio char(100)," +
            "image char(100)," +
            "video char(100)," +
            "aq_lh_id char(30)" +
            ")";
    public final static String INSERT_DATA_CHECK_RESULT = "insert into checkresult select *,'' from _temp_checkresult";
    public final static String DROP_TEMP_CHECK_RESULT = "drop table _temp_checkresult";

    //修改CREATE_FINISHED_PALN表
    public final static String CREATE_TEMP_FINISHED_PALN = "alter table finishedplan rename to _temp_finishedplan";
    public final static String CREATE_NEW_FINISHED_PALN = "create table finishedplan(" +
            "id Integer primary key," +
            "SysId int(20)," +
            "SysGcxxdjh int(20)," +
            "FinishedTime char(20)," +
            "Username char(10)," +
            "AQ_LH_ID char(20)," +
            "Type int(8)" +
            ")";
    public final static String INSERT_DATA_FINISHED_PALN = "insert into finishedplan select *,0 from _temp_finishedplan";
    public final static String DROP_TEMP_FINISHED_PALN = "drop table _temp_finishedplan";


    //修改checkplan1表
    public final static String CREATE_TEMP_TABLE1 = "alter table checkplan rename to _temp_checkplan";
    public final static String CREATE_NEW_CHECKPALN1 = "create table checkplan(" +
            "identifier Integer primary key," +
            "sysId int(12)," +
            "name char(30)," +
            "state int(1)," +
            "address varchar(50)," +
            "quxian varchar(10)," +
            "area varchar(20)," +
            "type varchar(20)," +
            "tel varchar(20)," +
            "manager varchar(50)," +
            "user varchar(30)," +
            "url varchar(50)," +
            "plan_type int(8)" +
            ")";
    public final static String INSERT_DATA_CHECKPLAN1 = "insert into checkplan select *,0 from _temp_checkplan";
    public final static String DROP_TEMP1 = "drop table _temp_checkplan";
    /*
    *正式IP地址
    * */

    public static final String HTTPURL = "http://43.254.152.169:9003/zgzxjkWebService.asmx";
    public static final String FTP_HOST_DEFAULT = "43.254.152.169";
    public static final int FTP_HOST_PORT = 9004;//9004
    public static final String FTP_USER_DEFAULT = "zgzx1";
    public static final String FTP_PASSWORD_DEFAULT = "zgzx";
    public static final String URL = "http://43.254.152.169:9003/downpdf/Temp/";
    public static final String URL_SOFT = "http://43.254.152.169:9001/Aqgl/xiazaiapp";


    /*
    * 测试IP地址
    * */
    public static String TEST_HTTPURL = "http://192.168.100.211:8080/zgzxjkWebService.asmx";
    /*  public static String FTP_HOST_DEFAULT = "192.168.100.136";
      public static final int FTP_HOST_PORT = 18000;
      public static final String FTP_USER_DEFAULT = "zgzx1";
      public static final String FTP_PASSWORD_DEFAULT = "zgzx";*/
    public static final String TEST_URL = "http://192.168.100.211:8080/downpdf/Temp/";
    public static final String URL_CHECK = "http://192.168.100.211:8080/zgzxjkWebService.asmx";
//    public static final String URL_SOFT = "http://101.231.52.50:8081/Aqgl/xiazaiapp";
    /**
     * ftp状态
     */
    public static final String FTP_CONNECT_SUCCESSS = "ftp连接成功";
    public static final String FTP_CONNECT_FAIL = "ftp连接失败";
    public static final String FTP_DISCONNECT_SUCCESS = "ftp断开连接";
    public static final String FTP_FILE_NOTEXISTS = "ftp上文件不存在";

    public static final String FTP_UPLOAD_SUCCESS = "ftp文件上传成功";
    public static final String FTP_UPLOAD_FAIL = "ftp文件上传失败";
    public static final String FTP_UPLOAD_LOADING = "ftp文件正在上传";

    public static final String FTP_DOWN_LOADING = "ftp文件正在下载";
    public static final String FTP_DOWN_SUCCESS = "ftp文件下载成功";
    public static final String FTP_DOWN_FAIL = "ftp文件下载失败";

    public static final String FTP_DELETEFILE_SUCCESS = "ftp文件删除成功";
    public static final String FTP_DELETEFILE_FAIL = "ftp文件删除失败";

    public static final String FTP_GETALLPICPATH_SUCCESS = "ftp获取所有图片路径成功";
    public static final String FTP_GETALLPICPATH_FAIL = "ftp获取所有图片路径失败";

    public static final String CAAHE_KEY = "projectkey22335151";
    public static final String FILE_PATH = "/CheckApp/file";


}
