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
            "name varchar(10)" +
            "password char(30)" +
            "lastupdatetime long" +
            "plancount int" +
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
            "AQ_JCTZ_sfjc int(5)"+
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
            "user varchar(30)" +
            ")";
    public final static String CREATE_FINISHED_PALN = "create table finishedplan(" +
            "id Integer primary key," +
            "SysId int(20)," +
            "SysGcxxdjh int(20)," +
            "FinishedTime char(20)," +
            "Username char(10)," +
            "AQ_LH_ID char(20)" +
            ")";
    public final static String CREATE_CHECK_RESULT = "create table checkresult(" +
            "id Integer primary key," +
            "identifier int(10)," +
            "num int(10)," +
            "checkresult int(1)," +
            "comment text," +
            "audio char(100)," +
            "image char(100)," +
            "video char(100)" +
            ")";
    public final static String CREATE_SEARCH_HISTORY = "create table searchhistory(" +
            "id Integer primary key," +
            "name varchar(20)," +
            "type int(1)" +
            ")";
    public final static String GetXxclSc = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
            "  <soap12:Body>\n" +
            "    <GetXxclSc xmlns=\"http://zgzxjk/\">\n" +
            "      <quxian>quxian</quxian>\n" +
            "    </GetXxclSc>\n" +
            "  </soap12:Body>\n" +
            "</soap12:Envelope>";
    public final static String GetUser = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
            "  <soap12:Body>\n" +
            "    <GetUser xmlns=\"http://zgzxjk/\">\n" +
            "      <user>UserName</user>\n" +
            "      <password>UserPwd</password>\n" +
            "    </GetUser>\n" +
            "  </soap12:Body>\n" +
            "</soap12:Envelope>";

    /*
    *正式IP地址
    * */

//    public static final String HTTPURL = "http://101.231.52.50:8080/zgzxjkWebService.asmx";
//    public static final String FTP_HOST_DEFAULT = "101.231.52.50";
//    public static final int FTP_HOST_PORT = 18000;
//    public static final String FTP_USER_DEFAULT = "zgzx1";
//    public static final String FTP_PASSWORD_DEFAULT = "zgzx";


    /*
    * 测试IP地址
    * */
//    public static final String HTTPURL = "http://192.168.100.211:8080/zgzxjkWebService.asmx";
    public static final String HTTPURL = "http://192.168.100.136:8080/zgzxjkWebService.asmx  ";
    public static final String FTP_HOST_DEFAULT = "192.168.100.211";
    public static final int FTP_HOST_PORT = 21;
    public static final String FTP_USER_DEFAULT = "zgzx";
    public static final String FTP_PASSWORD_DEFAULT = "123456";

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

}
