package com.genersoft.iot.vmp.vmanager.bean;


import com.genersoft.iot.vmp.gb28181.bean.DeviceChannel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class DeviceChannelTreeItem {

    /**
     * 通道国标编号
     */
    @Schema(description = "通道国标编号")
    private String channelId;

    /**
     * 设备国标编号
     */
    @Schema(description = "设备国标编号")
    private String deviceId;

    /**
     * 通道名
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 生产厂商
     */
    @Schema(description = "生产厂商")
    private String manufacture;

    /**
     * 型号
     */
    @Schema(description = "型号")
    private String model;


    /**
     * 是否有子设备 1有, 0没有
     */
    @Schema(description = "是否有子设备 1有, 0没有")
    private int parental;

    /**
     * IP地址
     */
    @Schema(description = "IP地址")
    private String ipAddress;

    /**
     * 端口号
     */
    @Schema(description = "端口号")
    private int port;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 云台类型
     */
    @Schema(description = "云台类型")
    private int ptzType;

    /**
     * 云台类型描述字符串
     */
    @Schema(description = "云台类型描述字符串")
    private String ptzTypeText;

    /**
     * 在线/离线
     * 1在线,0离线
     * 默认在线
     * 信令:
     * <Status>ON</Status>
     * <Status>OFF</Status>
     * 遇到过NVR下的IPC下发信令可以推流， 但是 Status 响应 OFF
     */
    @Schema(description = "在线/离线， 1在线,0离线")
    private boolean status;

    /**
     * 经度
     */
    @Schema(description = "经度")
    private double longitude;

    /**
     * 纬度
     */
    @Schema(description = "纬度")
    private double latitude;

    /**
     * 经度
     */
    @Schema(description = "自定义经度")
    private double customLongitude;

    /**
     * 纬度
     */
    @Schema(description = "自定义纬度")
    private double customLatitude;

    /**
     * 经度 GCJ02
     */
    @Schema(description = "GCJ02坐标系经度")
    private double longitudeGcj02;

    /**
     * 纬度 GCJ02
     */
    @Schema(description = "GCJ02坐标系纬度")
    private double latitudeGcj02;

    /**
     * 经度 WGS84
     */
    @Schema(description = "WGS84坐标系经度")
    private double longitudeWgs84;

    /**
     * 纬度 WGS84
     */
    @Schema(description = "WGS84坐标系纬度")
    private double latitudeWgs84;

    /**
     * 子设备数
     */
    @Schema(description = "子设备数")
    private int subCount;

    /**
     * 流唯一编号，存在表示正在直播
     */
    @Schema(description = "流唯一编号，存在表示正在直播")
    private String  streamId;

    /**
     *  是否含有音频
     */
    @Schema(description = "是否含有音频")
    private Boolean hasAudio;

    /**
     * 标记通道的类型，0->国标通道 1->直播流通道 2->业务分组/虚拟组织/行政区划
     */
    @Schema(description = "标记通道的类型，0->国标通道 1->直播流通道 2->业务分组/虚拟组织/行政区划")
    private int channelType;

    /**
     * 是否加入视频轮播
     */
    private boolean videoCarousel;

    /**
     * 视频轮播时长（分钟）
     */
    private Integer videoCarouselDuration;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setPtzType(int ptzType) {
        this.ptzType = ptzType;
        switch (ptzType) {
            case 0:
                this.ptzTypeText = "未知";
                break;
            case 1:
                this.ptzTypeText = "球机";
                break;
            case 2:
                this.ptzTypeText = "半球";
                break;
            case 3:
                this.ptzTypeText = "固定枪机";
                break;
            case 4:
                this.ptzTypeText = "遥控枪机";
                break;
        }
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getParental() {
        return parental;
    }

    public void setParental(int parental) {
        this.parental = parental;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPtzType() {
        return ptzType;
    }

    public String getPtzTypeText() {
        return ptzTypeText;
    }

    public void setPtzTypeText(String ptzTypeText) {
        this.ptzTypeText = ptzTypeText;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitudeGcj02() {
        return longitudeGcj02;
    }

    public void setLongitudeGcj02(double longitudeGcj02) {
        this.longitudeGcj02 = longitudeGcj02;
    }

    public double getLatitudeGcj02() {
        return latitudeGcj02;
    }

    public void setLatitudeGcj02(double latitudeGcj02) {
        this.latitudeGcj02 = latitudeGcj02;
    }

    public double getLongitudeWgs84() {
        return longitudeWgs84;
    }

    public void setLongitudeWgs84(double longitudeWgs84) {
        this.longitudeWgs84 = longitudeWgs84;
    }

    public double getLatitudeWgs84() {
        return latitudeWgs84;
    }

    public void setLatitudeWgs84(double latitudeWgs84) {
        this.latitudeWgs84 = latitudeWgs84;
    }

    public int getSubCount() {
        return subCount;
    }

    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    public Boolean getHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(Boolean hasAudio) {
        this.hasAudio = hasAudio;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public double getCustomLongitude() {
        return customLongitude;
    }

    public void setCustomLongitude(double customLongitude) {
        this.customLongitude = customLongitude;
    }

    public double getCustomLatitude() {
        return customLatitude;
    }

    public void setCustomLatitude(double customLatitude) {
        this.customLatitude = customLatitude;
    }

    public boolean isVideoCarousel() {
        return videoCarousel;
    }

    public void setVideoCarousel(boolean videoCarousel) {
        this.videoCarousel = videoCarousel;
    }

    public Integer getVideoCarouselDuration() {
        return videoCarouselDuration;
    }

    public void setVideoCarouselDuration(Integer videoCarouselDuration) {
        this.videoCarouselDuration = videoCarouselDuration;
    }

}
