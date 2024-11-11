package com.genersoft.iot.vmp.vmanager.gb28181.alarm;

import com.genersoft.iot.vmp.conf.exception.ControllerException;
import com.genersoft.iot.vmp.conf.security.JwtUtils;
import com.genersoft.iot.vmp.gb28181.bean.*;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.ISIPCommander;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.ISIPCommanderForPlatform;
import com.genersoft.iot.vmp.service.IDeviceAlarmService;
import com.genersoft.iot.vmp.storager.IRedisCatchStorage;
import com.genersoft.iot.vmp.storager.IVideoManagerStorage;
import com.genersoft.iot.vmp.utils.DateUtil;
import com.genersoft.iot.vmp.vmanager.bean.AlarmType;
import com.genersoft.iot.vmp.vmanager.bean.ErrorCode;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.util.LittleEndian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Tag(name = "报警信息管理")
@RestController
@RequestMapping("/api/alarm")
public class AlarmController {

    @Autowired
    private IRedisCatchStorage redisCatchStorage;

    private final static Logger logger = LoggerFactory.getLogger(AlarmController.class);

    @Autowired
    private IDeviceAlarmService deviceAlarmService;

    @Autowired
    private ISIPCommander commander;

    @Autowired
    private ISIPCommanderForPlatform commanderForPlatform;

    @Autowired
    private IVideoManagerStorage storage;


    /**
     *  删除报警
     *
     * @param id 报警id
     * @param deviceIds 多个设备id,逗号分隔
     * @param time 结束时间(这个时间之前的报警会被删除)
     * @return
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除报警", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @Parameter(name = "id", description = "ID")
    @Parameter(name = "deviceIds", description = "多个设备id,逗号分隔")
    @Parameter(name = "time", description = "结束时间")
    public Integer delete(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String deviceIds,
            @RequestParam(required = false) String time
    ) {
        if (ObjectUtils.isEmpty(id)) {
            id = null;
        }
        if (ObjectUtils.isEmpty(deviceIds)) {
            deviceIds = null;
        }

        if (ObjectUtils.isEmpty(time)) {
            time = null;
        }else if (!DateUtil.verification(time, DateUtil.formatter) ){
            throw new ControllerException(ErrorCode.ERROR400.getCode(), "time格式为" + DateUtil.PATTERN);
        }
        List<String> deviceIdList = null;
        if (deviceIds != null) {
            String[] deviceIdArray = deviceIds.split(",");
            deviceIdList = Arrays.asList(deviceIdArray);
        }

        return deviceAlarmService.clearAlarmBeforeTime(id, deviceIdList, time);
    }

    @PostMapping("/deleteByIds")
    @Operation(summary = "根据id批量删除报警", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @Parameter(name = "ids", description = "报警id列表")
    public int deleteByIds(@RequestBody Map<String, List<Integer>> ids) {
        List<Integer> alarmIds = ids.get("ids"); // 获取传递的 ID 列表
        if(CollectionUtils.isEmpty(alarmIds)){
            throw new ControllerException(ErrorCode.ERROR100.getCode(),"至少选择一条删除");
        }
        int res = 0;
        for (Integer id : alarmIds) {
            res += deviceAlarmService.clearAlarmBeforeTime(id, null, null);
        }
        return res;
    }
    /**
     *  测试向上级/设备发送模拟报警通知
     *
     * @param deviceId 报警id
     * @return
     */
    @GetMapping("/test/notify/alarm")
    @Operation(summary = "测试向上级/设备发送模拟报警通知", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @Parameter(name = "deviceId", description = "设备国标编号")
    public void delete(@RequestParam String deviceId) {
        Device device = storage.queryVideoDevice(deviceId);
        ParentPlatform platform = storage.queryParentPlatByServerGBId(deviceId);
        DeviceAlarm deviceAlarm = new DeviceAlarm();
        deviceAlarm.setChannelId(deviceId);
        deviceAlarm.setAlarmDescription("test");
        deviceAlarm.setAlarmMethod("1");
        deviceAlarm.setAlarmPriority("1");
        deviceAlarm.setAlarmTime(DateUtil.getNow());
        deviceAlarm.setAlarmType("1");
        deviceAlarm.setLongitude(115.33333);
        deviceAlarm.setLatitude(39.33333);

        if (device != null && platform == null) {

            try {
                commander.sendAlarmMessage(device, deviceAlarm);
            } catch (InvalidArgumentException | SipException | ParseException e) {

            }
        }else if (device == null && platform != null){
            try {
                commanderForPlatform.sendAlarmMessage(platform, deviceAlarm);
            } catch (SipException | InvalidArgumentException | ParseException e) {
                logger.error("[命令发送失败] 国标级联 发送BYE: {}", e.getMessage());
                throw new ControllerException(ErrorCode.ERROR100.getCode(), "命令发送失败: " +  e.getMessage());
            }
        }else {
            throw new ControllerException(ErrorCode.ERROR100.getCode(),"无法确定" + deviceId + "是平台还是设备");
        }

    }

    /**
     *  分页查询报警
     *
     * @param deviceId 设备id
     * @param page 当前页
     * @param count 每页查询数量
     * @param alarmPriority  报警级别
     * @param alarmMethod 报警方式
     * @param alarmType  报警类型
     * @param startTime  开始时间
     * @param endTime 结束时间
     * @return
     */
    @Operation(summary = "分页查询报警", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @Parameter(name = "page",description = "当前页",required = true)
    @Parameter(name = "count",description = "每页查询数量",required = true)
    @Parameter(name = "deviceId",description = "设备id")
    @Parameter(name = "alarmPriority",description = "查询内容")
    @Parameter(name = "alarmMethod",description = "查询内容")
    @Parameter(name = "alarmType",description = "每页查询数量")
    @Parameter(name = "startTime",description = "开始时间")
    @Parameter(name = "endTime",description = "结束时间")
    @GetMapping("/all")
    public PageInfo<DeviceAlarmInfo> getAll(
            @RequestParam int page,
            @RequestParam int count,
            @RequestParam(required = false)  String deviceId,
            @RequestParam(required = false) String alarmPriority,
            @RequestParam(required = false) String alarmMethod,
            @RequestParam(required = false) String alarmType,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime
    ) {
        if (ObjectUtils.isEmpty(alarmPriority)) {
            alarmPriority = null;
        }
        if (ObjectUtils.isEmpty(alarmMethod)) {
            alarmMethod = null;
        }
        if (ObjectUtils.isEmpty(alarmType)) {
            alarmType = null;
        }

        if (ObjectUtils.isEmpty(startTime)) {
            startTime = null;
        }else if (!DateUtil.verification(startTime, DateUtil.formatter) ){
            throw new ControllerException(ErrorCode.ERROR400.getCode(), "startTime格式为" + DateUtil.PATTERN);
        }

        if (ObjectUtils.isEmpty(endTime)) {
            endTime = null;
        }else if (!DateUtil.verification(endTime, DateUtil.formatter) ){
            throw new ControllerException(ErrorCode.ERROR400.getCode(), "endTime格式为" + DateUtil.PATTERN);
        }
        return deviceAlarmService.getAllAlarm(page, count, deviceId, alarmPriority, alarmMethod,
                alarmType, startTime, endTime);
    }

    /**
     *  查询最近指定数量报警
     *
     * @param count 每页查询数量
     * @return
     */
    @Operation(summary = "查询最近指定数量报警", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @Parameter(name = "count",description = "查询最近指定数量报警",required = true)
    @GetMapping("/getLatest")
    public List<DeviceAlarm> getLatest(
            @RequestParam(required = true, defaultValue = "10")  int count) {
        return deviceAlarmService.queryLatest(count);
    }

    /**
     *  查询全部报警数量，每月统计一次
     *
     * @return
     */
    @Operation(summary = "查询全部报警数量，每月统计一次", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @GetMapping("/countAlarmsByMonth")
    public List<AlarmCountInfo> countAlarmsByMonth() {
        List<AlarmCountInfo> alarmCountInfos = deviceAlarmService.countAlarmsByMonth();
        if(alarmCountInfos.size() <= 1) {
            return alarmCountInfos;
        }
        //列出全部日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate startTime = YearMonth.parse(alarmCountInfos.get(0).getTime(), DateTimeFormatter.ofPattern("yyyy-MM")).atDay(1);
        LocalDate endTime = YearMonth.parse(alarmCountInfos.get(alarmCountInfos.size() - 1).getTime(), DateTimeFormatter.ofPattern("yyyy-MM")).atDay(1);
        List<String> monthList = new ArrayList<>();
        monthList.add(startTime.format(formatter));
        while (startTime.isBefore(endTime)) {
            startTime = startTime.plusMonths(1);
            monthList.add(startTime.format(formatter));
        }
        List<AlarmCountInfo> list = new ArrayList<>();
        for (String s : monthList) {
            list.add(getAlarmCount(alarmCountInfos,null,s));
        }
        return list;
    }

    @Operation(summary = "查询全部报警数量，每月统计一次,区分报警类型", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @GetMapping("/countAlarmsByMonthType")
    public List<AlarmCountInfo> countAlarmsByMonthType() {
        List<AlarmCountInfo> alarmCountInfos = deviceAlarmService.countAlarmsByMonthType();
        if(alarmCountInfos.size() <= 0) {
            throw new ControllerException(ErrorCode.ERROR404);
        }
        //列出全部日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate startTime = YearMonth.parse(alarmCountInfos.get(0).getTime(), DateTimeFormatter.ofPattern("yyyy-MM")).atDay(1);
        LocalDate endTime = YearMonth.parse(alarmCountInfos.get(alarmCountInfos.size() - 1).getTime(), DateTimeFormatter.ofPattern("yyyy-MM")).atDay(1);
        List<String> monthList = new ArrayList<>();
        monthList.add(startTime.format(formatter));
        while (startTime.isBefore(endTime)) {
            startTime = startTime.plusMonths(1);
            monthList.add(startTime.format(formatter));
        }
        List<AlarmCountInfo> list = new ArrayList<>();
        for (String s : monthList) {
            list.add(getAlarmCount(alarmCountInfos,AlarmType.FIRE.getCode(),s));
            list.add(getAlarmCount(alarmCountInfos,AlarmType.BREAKIN.getCode(),s));
            list.add(getAlarmCount(alarmCountInfos,AlarmType.OVERTEMPERATUREOVERTEMPERATURE.getCode(),s));
        }
        return list;
    }
    @Operation(summary = "查询全部报警数量，区分报警类型", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @GetMapping("/countAlarmsByType")
    public List<AlarmCountInfo> countAlarmsByType() {
        List<AlarmCountInfo> alarmCountInfos = deviceAlarmService.countAlarmsByType();
        List<AlarmCountInfo> list = new ArrayList<>();
        list.add(getAlarmCount(alarmCountInfos,AlarmType.FIRE.getCode(),""));
        list.add(getAlarmCount(alarmCountInfos,AlarmType.BREAKIN.getCode(),""));
        list.add(getAlarmCount(alarmCountInfos,AlarmType.OVERTEMPERATUREOVERTEMPERATURE.getCode(),""));
        list.add(getAlarmCount(alarmCountInfos,AlarmType.OTHER.getCode(),""));
        return list;
    }
    @Operation(summary = "查询最近半年报警数量，按月分组，区分报警类型", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @GetMapping("/countAlarmsBy6MonthType")
    public List<AlarmCountInfo> countAlarmsBy6MonthType() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        List<String> monthList = new ArrayList<>();
        monthList.add((currentDate.format(formatter)));
        for(int i = 0; i < 5; i++) {
            currentDate = currentDate.minusMonths(1);
            monthList.add((currentDate.format(formatter)));
        }
        Collections.reverse(monthList);


        List<AlarmCountInfo> alarmCountInfos = deviceAlarmService.countAlarmsBy6MonthType(currentDate.format(formatter));
        List<AlarmCountInfo> list = new ArrayList<>();
        for (String s : monthList) {
            list.add(getAlarmCount(alarmCountInfos,AlarmType.FIRE.getCode(),s));
            list.add(getAlarmCount(alarmCountInfos,AlarmType.BREAKIN.getCode(),s));
            list.add(getAlarmCount(alarmCountInfos,AlarmType.OVERTEMPERATUREOVERTEMPERATURE.getCode(),s));
        }
        return list;
    }

    @Operation(summary = "查询最近一个星期报警数量，按天分组，区分报警类型", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @GetMapping("/countAlarmsByDay")
    public List<AlarmCountInfo> countAlarmsByDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        List<String> monthList = new ArrayList<>();
        monthList.add((currentDate.format(formatter)));
        for(int i = 0; i < 6; i++) {
            currentDate = currentDate.minusDays(1);
            monthList.add((currentDate.format(formatter)));
        }
        Collections.reverse(monthList);

        List<AlarmCountInfo> alarmCountInfos = deviceAlarmService.countAlarmsByDay(currentDate.format(formatter));
        List<AlarmCountInfo> list = new ArrayList<>();
        for (String s : monthList) {
            list.add(getAlarmCount(alarmCountInfos,AlarmType.FIRE.getCode(),s));
            list.add(getAlarmCount(alarmCountInfos,AlarmType.BREAKIN.getCode(),s));
            list.add(getAlarmCount(alarmCountInfos,AlarmType.OVERTEMPERATUREOVERTEMPERATURE.getCode(),s));
        }
        //将时间转换成周几输出
        for (AlarmCountInfo alarmCountInfo : list) {
            LocalDate date = LocalDate.parse(alarmCountInfo.getTime(), formatter);
            // 获取该日期的星期几
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            String weekDay = convertToWeekDay(dayOfWeek);
            alarmCountInfo.setTime(weekDay);
        }
        return list;
    }
    @Operation(summary = "查询全部设备总告警数，当天告警数，设备状态", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @GetMapping("/countAlarmsByDev")
    public List<AlarmDevInfo> countAlarmsByDev() {
        List<AlarmDevInfo> alarmDevInfoTotal = deviceAlarmService.countTotalAlarmsByDev();
        List<AlarmDevInfo> alarmDevInfosToday = deviceAlarmService.countTodayAlarmsByDev();
        if(CollectionUtils.isEmpty(alarmDevInfoTotal)){
            throw new ControllerException(ErrorCode.ERROR404);
        }
        for (AlarmDevInfo alarmDevInfo : alarmDevInfoTotal) {
            Optional<AlarmDevInfo> first = alarmDevInfosToday.stream().filter(item -> item.getDevId().equals(alarmDevInfo.getDevId())).findFirst();
            alarmDevInfo.setToday(0);
            alarmDevInfo.setStatus("Offline");
            if(first.isPresent()) {
                alarmDevInfo.setToday(first.get().getToday());
            }
            if(redisCatchStorage.deviceIsOnline(alarmDevInfo.getDevId())) {
                alarmDevInfo.setStatus("Online");
            }
        }
        return alarmDevInfoTotal;
    }
    private AlarmCountInfo getAlarmCount(List<AlarmCountInfo> alarmCountInfos,Integer type, String time) {
        AlarmCountInfo alarmCountInfo = new AlarmCountInfo();
        alarmCountInfo.setTime(time);
        alarmCountInfo.setCount(0);
        alarmCountInfo.setType(type);
        if(CollectionUtils.isEmpty(alarmCountInfos)) {
            return alarmCountInfo;
        }
        if(type != null && time.length() > 0) {
            Optional<AlarmCountInfo> first = alarmCountInfos.stream().filter(item -> Objects.equals(item.getType(), type) && time.equals((item.getTime()))).findFirst();
            if(first.isPresent()) {
                alarmCountInfo.setCount(first.get().getCount());
            }
        }
        else if(type == null && time.length() > 0) {
            Optional<AlarmCountInfo> first = alarmCountInfos.stream().filter(item -> time.equals((item.getTime()))).findFirst();
            if(first.isPresent()) {
                alarmCountInfo.setCount(first.get().getCount());
            }
        }
        else if(type != null && time.length() <= 0) {
            Optional<AlarmCountInfo> first = alarmCountInfos.stream().filter(item -> Objects.equals(item.getType(), type)).findFirst();
            if(first.isPresent()) {
                alarmCountInfo.setCount(first.get().getCount());
            } else {
                if(type == AlarmType.OTHER.getCode()) {
                    first = alarmCountInfos.stream().filter(item -> item.getType() == null).findFirst();
                    if (first.isPresent()) {
                        alarmCountInfo.setCount(first.get().getCount());
                    }
                }
            }
        }
        return alarmCountInfo;
    }
    // 自定义的转换方法
    private static String convertToWeekDay(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return "周一";
            case TUESDAY:
                return "周二";
            case WEDNESDAY:
                return "周三";
            case THURSDAY:
                return "周四";
            case FRIDAY:
                return "周五";
            case SATURDAY:
                return "周六";
            case SUNDAY:
                return "周日";
            default:
                throw new IllegalArgumentException("未知的星期几: " + dayOfWeek);
        }
    }

    @Operation(summary = "查询全部报警设备号", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @GetMapping("/queryAlarmDeviceList")
    public List<String> queryAlarmDeviceList() {
        return deviceAlarmService.queryAlarmDeviceList();
    }
}
