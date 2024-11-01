package com.genersoft.iot.vmp.vmanager.bean;


import java.util.List;

public class DeviceChannelTree {
    /**
     * 设备国标编号
     */
    private String deviceId;
    /**
     * 设备名
     */
    private String name;
    /**
     * wan地址_ip
     */
    private String ip;
    /**
     * wan地址_port
     */
    private int port;
    /**
     * 在线
     */
    private boolean onLine;

    /**
     * 通道
     */
    private List<DeviceChannelTreeItem> children;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public List<DeviceChannelTreeItem> getChildren() {
        return children;
    }

    public void setChildren(List<DeviceChannelTreeItem> children) {
        this.children = children;
    }
}
