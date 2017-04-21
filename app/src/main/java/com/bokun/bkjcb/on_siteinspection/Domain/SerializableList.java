package com.bokun.bkjcb.on_siteinspection.Domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by BKJCB on 2017/3/30.
 */

public class SerializableList implements Serializable {
    private ArrayList<CheckResult> results;
    private ArrayList<String> strings;


    public SerializableList() {
    }

    public ArrayList<CheckResult> getList() {
        return results;
    }

    public void setList(ArrayList<CheckResult> list) {
        this.results = list;
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    public void setStrings(ArrayList<String> strings) {
        this.strings = strings;
    }
}
