package com.bokun.bkjcb.on_siteinspection.Domain;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by BKJCB on 2017/3/30.
 */

public class SerializableHashMap implements Serializable{
    private HashMap<String,Object> map;
    public SerializableHashMap(){
    }

    public HashMap<String, Object> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Object> map) {
        this.map = map;
    }
}
