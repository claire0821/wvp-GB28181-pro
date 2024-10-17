package com.genersoft.iot.vmp.gb28181.bean;

public class AlarmCountInfo {
    private String time;
    private Integer count;
    private Integer type;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
