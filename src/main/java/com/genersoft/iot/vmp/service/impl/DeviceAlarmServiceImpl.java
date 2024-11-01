package com.genersoft.iot.vmp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.genersoft.iot.vmp.gb28181.bean.*;
import com.genersoft.iot.vmp.service.IDeviceAlarmService;
import com.genersoft.iot.vmp.storager.dao.DeviceAlarmMapper;
import com.genersoft.iot.vmp.storager.dao.DeviceMapper;
import com.genersoft.iot.vmp.vmanager.bean.AlarmType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

@Service
@DS("master")
public class DeviceAlarmServiceImpl implements IDeviceAlarmService {

    @Autowired
    private DeviceAlarmMapper deviceAlarmMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String url;
    @Override
    public PageInfo<DeviceAlarmInfo> getAllAlarm(int page, int count, String deviceId, String alarmPriority, String alarmMethod, String alarmType, String startTime, String endTime) {
        PageHelper.startPage(page, count);
        List<DeviceAlarm> all = deviceAlarmMapper.query(deviceId, alarmPriority, alarmMethod, alarmType, startTime, endTime);
        List<DeviceAlarmInfo> deviceAlarmInfoList = new ArrayList<>();
        for (DeviceAlarm deviceAlarm : all) {
            DeviceAlarmInfo deviceAlarmInfo = new DeviceAlarmInfo();
            BeanUtils.copyProperties(deviceAlarm,deviceAlarmInfo);
            Device deviceByDeviceId = deviceMapper.getDeviceByDeviceId(deviceAlarm.getDeviceId());
            if(deviceByDeviceId != null) {
                deviceAlarmInfo.setDeviceName(deviceByDeviceId.getName());
            }
            if(deviceAlarmInfo.getAlarmType() == null) {
                deviceAlarmInfo.setAlarmType(AlarmType.OTHER.getMsg());
            } else if(deviceAlarmInfo.getAlarmType().equals(AlarmType.FIRE.getCode().toString())) {
                deviceAlarmInfo.setAlarmType(AlarmType.FIRE.getMsg());
            } else if(deviceAlarmInfo.getAlarmType().equals(AlarmType.OVERTEMPERATUREOVERTEMPERATURE.getCode().toString())) {
                deviceAlarmInfo.setAlarmType(AlarmType.OVERTEMPERATUREOVERTEMPERATURE.getMsg());
            } else if(deviceAlarmInfo.getAlarmType().equals(AlarmType.BREAKIN.getCode().toString())) {
                deviceAlarmInfo.setAlarmType(AlarmType.BREAKIN.getMsg());
            }
            deviceAlarmInfoList.add(deviceAlarmInfo);
        }
        return new PageInfo<>(deviceAlarmInfoList);
    }

    @Override
    public void add(DeviceAlarm deviceAlarm) {
        deviceAlarmMapper.add(deviceAlarm);
    }

    @Override
    public int clearAlarmBeforeTime(Integer id, List<String> deviceIdList, String time) {
        return deviceAlarmMapper.clearAlarmBeforeTime(id, deviceIdList, time);
    }

    @Override
    public List<DeviceAlarm> queryLatest(int count) {
        return deviceAlarmMapper.queryLatest(count);
    }

    @Override
    public List<AlarmCountInfo> countAlarmsByMonth() {
        if(url.contains("postgresql")) {
            return deviceAlarmMapper.countAlarmsByMonthPgsql();
        }
        return deviceAlarmMapper.countAlarmsByMonth();
    }

    @Override
    public List<AlarmCountInfo> countAlarmsByMonthType() {
        if(url.contains("postgresql")) {
            return deviceAlarmMapper.countAlarmsByMonthTypePgsql();
        }
        return deviceAlarmMapper.countAlarmsByMonthType();
    }

    @Override
    public List<AlarmCountInfo> countAlarmsByType() {
        return deviceAlarmMapper.countAlarmsByType();
    }

    @Override
    public List<AlarmCountInfo> countAlarmsBy6MonthType(String startTime) {
        if(url.contains("postgresql")) {
            return deviceAlarmMapper.countAlarmsBy6MonthTypePgsql(startTime);
        }
        return deviceAlarmMapper.countAlarmsBy6MonthType();
    }

    @Override
    public List<AlarmCountInfo> countAlarmsByDay(String startTime) {
        if(url.contains("postgresql")) {
            return deviceAlarmMapper.countAlarmsByDayPgsql(startTime);
        }
        return deviceAlarmMapper.countAlarmsByDay(startTime);
    }

    @Override
    public List<AlarmDevInfo> countTotalAlarmsByDev() {
        return deviceAlarmMapper.countTotalAlarmsByDev();
    }

    @Override
    public List<AlarmDevInfo> countTodayAlarmsByDev() {
        if(url.contains("postgresql")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();
            return deviceAlarmMapper.countAlarmsByDayDevPgsql(currentDate.format(formatter));
        }
        return deviceAlarmMapper.countTodayAlarmsByDev();
    }
    @Override
    public List<String> queryAlarmDeviceList() {
        return deviceAlarmMapper.queryAlarmDeviceList();
    }
}
