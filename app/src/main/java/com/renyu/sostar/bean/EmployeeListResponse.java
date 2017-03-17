package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/3/17.
 */

public class EmployeeListResponse {
    int id=0;
    String name="";
    String groupName="";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
