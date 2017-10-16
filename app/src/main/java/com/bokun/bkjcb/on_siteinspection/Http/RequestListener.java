package com.bokun.bkjcb.on_siteinspection.Http;

/**
 * Created by BKJCB on 2017/3/16.
 */

public interface RequestListener {

    public static final int EVENT_NETWORD_EEEOR = 1;
    public static final int EVENT_CLOSE_SOCKET = 2;
    public static final int EVENT_GET_DATA_EEEOR = 3;
    public static final int EVENT_NOT_NETWORD = 4;
    public static final int EVENT_GET_DATA_SUCCESS = 5;


    public void action(int i, Object object);

}
