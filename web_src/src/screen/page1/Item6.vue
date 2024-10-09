<template>
  <div class="div-item-content">
    <item-box :style="{ width: '550px', height: '100%', }" text="报警信息">
        <dv-scroll-board class="dv-scroll-board" ref="scrollBoard" :config="config" :style="{ width: '550px', height: boardHeight }" />
    </item-box>
  </div>
</template>

<script>
import itemBox from "../ItemBox.vue";
export default {
  name: "item5",
  components: {
    itemBox,
  },
  props: {
    height: {
      type: Number,
      default: 500, // 如果没有传递 height 时的默认值
    }
  },
  data() {
    return {
      boardHeight: '100px',
      config: {
        header: ['时间', '类型', '设备', '信息'],
        data: [
          ['2019-07-01 19:25:00', '热源','球机测试', 'xxxxxxx'],
          ['2019-07-02 17:25:00', '热源','球机测试',  'xxxxxxx'],
          ['2019-07-03 16:25:00', '热源','球机测试', 'xxxxxxx'],
          ['2019-07-04 15:25:00', '热源','球机测试',  'xxxxxxx'],
          ['2019-07-05 14:25:00', '热源','球机测试', 'xxxxxxx'],
          ['2019-07-06 13:25:00', '热源','球机测试',  'xxxxxxx'],
          ['2019-07-07 12:25:00', '热源','球机测试',  'xxxxxxx'],
          ['2019-07-08 11:25:00', '热源','球机测试',  'xxxxxxx'],
          ['2019-07-09 10:25:00', '热源','球机测试',  'xxxxxxx'],
          ['2019-07-10 09:25:00', '热源','球机测试',  'xxxxxxx']
        ],
        index: true,
        columnWidth: [60, 160,50, 100],
        align: ['center'],
        rowNum: 7,
        headerBGC: 'rgba(61, 254, 250, 0.15)',
        headerHeight: 45,
        oddRowBGC: 'rgba(19, 126, 90, 0.5)',
        evenRowBGC: 'rgba(19, 126, 90, 0.2)'
      },
      
    };
  },
  created() {
    this.getLoad();
    this.loopForSystemInfo();
  },
  mounted() {
    this.getLaster();
      // TODO: 此处延迟连接 sse, 避免 sse 连接时 browserId 还未生成, 后续待优化
    setTimeout(() => {
        this.sseControl()
    }, 3000);
  },
  watch: {
    // 监听 height prop 的变化
    height(newHeight) {
      this.boardHeight = newHeight - 50 + 'px';
    }
  },
  destroyed() {
    if (this.sseSource != null) {
      this.sseSource.removeEventListener('open', null);
      this.sseSource.removeEventListener('message', null);
      this.sseSource.removeEventListener('error', null);
      this.sseSource.close();
    }
  },
  methods: {
    loopForSystemInfo: function () {
      if (this.timer != null) {
        window.clearTimeout(this.timer);
      }
      this.timer = setTimeout(() => {
        this.getLoad();
        this.timer = null;
        this.loopForSystemInfo();
      }, 2000);
    },
    getLoad: function () {
      this.$axios({
        method: "get",
        url: `/api/server/media_server/load`,
      })
        .then((res) => {
          if (res.data.code === 0) {
            this.updateData(res.data.data);
          }
        })
        .catch((error) => {});
    },
    getLaster() {
      this.$axios({
        method: 'get',
        url: "/api/alarm/getLatest",
        params: {
          count: 10
        }
      }).then((res) => {
        this.config.data.splice(0, this.config.data.length);
        if(res.data && res.data.data) {
          let data = [];
          for (let item of res.data.data) {
            let alarmTime = item.alarmTime;
            let alarmType = "其他";
            let alarmName = "";
            let alarmInfo = "";

            // 判断 alarmDescription 是否存在并且不为空
            if (item.alarmDescription && item.alarmDescription.length > 0) {
              let result = item.alarmDescription.split(',');

              // 获取 alarmName 和 alarmType
              alarmName = result[0] || "";  // 默认值为空字符串
              if (result.length > 1) {
                if (result[1].includes("FireAlarm")) {
                  alarmType = "热源";
                }
              }
            }

            // 经纬度拼接信息
            if (item.latitude !== undefined && item.longitude !== undefined) {
              alarmInfo = "经纬度(" + item.latitude + ',' + item.longitude + ')';
            }

            // 把新数组 push 到 data 中
            data.push([alarmTime, alarmType, alarmName, alarmInfo]);
          }
          
          this.config.data.splice(0, this.config.data.length, ...data);
          if(data.length > 0) {
            this.$refs['scrollBoard'].updateRows(data);
          }
        }
      }).catch((error) => {
        console.error(error)
      });
    },
    sseControl() {
      let that = this;
      console.log("申请SSE推送API调用，浏览器ID: " + this.$browserId);
        // this.sseSource = new EventSource('http://10.21.0.61:18080/api/emit?browserId=' + this.$browserId);
        this.sseSource = new EventSource('http://127.0.0.1:18080/api/emit?browserId=' + this.$browserId);
        this.sseSource.addEventListener('message', function (evt) {
          let data = JSON.parse(evt.data);
          that.config.data.splice(0, 1);
          let alarmTime = data.alarmTime;
          let alarmType = "其他";
          let alarmName = "";
          let alarmInfo = "";
            // 判断 alarmDescription 是否存在并且不为空
          if (data.alarmDescription && data.alarmDescription.length > 0) {
            let result = data.alarmDescription.split(',');
            // 获取 alarmName 和 alarmType
            alarmName = result[0] || "";  // 默认值为空字符串
            if (result.length > 1) {
              if (result[1].includes("FireAlarm")) {
                alarmType = "热源";
              }
            }
          }

            // 经纬度拼接信息
          if (data.latitude !== undefined && data.longitude !== undefined) {
            alarmInfo = "经纬度(" + data.latitude + ',' + data.longitude + ')';
          }
          that.config.data.splice(0, that.config.data.length,[alarmTime, alarmType, alarmName, alarmInfo]);
          that.$refs['scrollBoard'].updateRows(that.config.data);
        });
        this.sseSource.addEventListener('open', function (e) {
          console.log("SSE连接打开.");
        }, false);
        this.sseSource.addEventListener('error', function (e) {
          if (e.target.readyState == EventSource.CLOSED) {
            console.log("SSE连接关闭");
          } else {
            console.log(e.target.readyState);
          }
        }, false);
    },
  },
};
</script>
<style scoped>
@font-face {
  font-family: HarmonyOS_Sans_Regular;
  src: url("../../assets/fonts/HarmonyOS_Sans_Regular.ttf");
}
.div-item-content {
  width: 100%;
  height: 100%;
}
#scroll-board {
  width: 50%;
  box-sizing: border-box;
  margin-left: 20px;
  height: 100%;
  overflow: hidden;
}
</style>
