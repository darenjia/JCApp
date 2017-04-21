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
    public final static String CREATE_CONSTRUCTION_TABLE = "create table ConstructionInfo(" +
            "id Integer primary key," +
            "name varchar(30)" +
            "username char(30)" +
            "lastupdatetime long" +
            "describtion text" +
            "state Bit" +
            ")";

    public final static String CREATE_CHECK_PLAN_TABLE = "create table checkplan(" +
            "identifier Integer primary key," +
            "name char(30)," +
            "state int(1)," +
            "address varchar(50)," +
            "area varchar(20)," +
            "type varchar(20)," +
            "tel varchar(20)," +
            "manager varchar(50)," +
            "user varchar(30)" +
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

}
