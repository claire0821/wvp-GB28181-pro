package com.genersoft.iot.vmp.vmanager.bean;

public enum AlarmType {
    BREAKIN(6, "闯入告警"),
    FIRE(1005, "火情告警"),
    OVERTEMPERATUREOVERTEMPERATURE(1006, "超温告警"),
    OTHER(0, "其他告警");

    private final int code;
    private final String msg;
    AlarmType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
