package com.genersoft.iot.vmp.gb28181.bean;

/**
 * 统计设备总告警、当天告警、状态
 */
public class AlarmDevInfo {
    String devId;
    int today;
    int total;
    String status;

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public int getToday() {
        return today;
    }

    public void setToday(int today) {
        this.today = today;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
