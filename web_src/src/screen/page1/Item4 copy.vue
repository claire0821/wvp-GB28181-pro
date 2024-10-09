<template>
  <div class="div-item-content">
    <item-box style="width: 400px; height: 400px" text="报警总览">
        <div ref="chart" style="width: 400px; height: 350px;"></div>
    </item-box>
  </div>
</template>

<script>
import itemBox from "../ItemBox.vue";
import moment from "moment/moment";
import echarts from "echarts";
export default {
  name: "item4",
  components: {
    itemBox,
    echarts,
  },
  data() {
    return {
      option: {},
      chart : {}
    };
  },
  created() {
    this.getSystemInfo();
    // this.getLoad();
    // this.getResourceInfo();
    this.loopForSystemInfo();
  },
  mounted() {
    this.initChart();
  
  },
  methods: {
    initChart: function () {
    this.chart = echarts.init(this.$refs.chart);
    // 更新图表
    this.option = {
    title : {
      show: false,
    },
    grid: {
            x: "40px",
            y: "25px",
            x2: "200px",
            y2: "20px",
          },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient : 'vertical',
        x : 'left',
        data:['热源报警','超温报警','闯入报警'],
        textStyle: {
          color: '#FFFFFF'
        }
    },
    toolbox: {
        show : false
    },
    calculable : false,
    series : (function (){
        var series = [];
        for (var i = 0; i < 30; i++) {
            series.push({
                name:'报警',
                type:'pie',
                itemStyle : {
                  normal: {
                    color: function(params) {
                        // 自定义颜色函数
                        var colors = ['rgba(61, 254, 250, 1)','#3DFEFA50', '#F69B52', '#9BD801']; // 你的颜色数组
                        return colors[params.dataIndex % colors.length]; // 根据数据索引返回颜色
                    },
                    label: { show: i > 28 },
                    labelLine: { show: i > 28, length: 20 },
                }
                },
                radius : [i * 4 + 40, i * 4 + 43],
                data:[
                    {value: i * 128 + 80,  name:'热源报警'},
                    {value: i * 64  + 160,  name:'超温报警'},
                    {value: i * 32  + 320,  name:'闯入报警'}
                ]
            })
        }
        series[0].markPoint = {
            symbol:'emptyCircle',
            symbolSize:series[0].radius[0],
            effect:{show:true,scaleSize:12,color:'rgba(250,225,50,0.8)',shadowBlur:10,period:30},
            data:[{x:'50%',y:'50%'}]
        };
            return series;
          })()
      };
      this.chart.setOption(this.option,true);
    },
    updateData: function (xData,yData) {
        console.log(this.option)
        // 只更新 xAxis 和 series 数据
        this.option.xAxis.data = xData; // 更新 xAxis 的数据
        this.option.series.data = yData; // 更新 series 的数据

        // 更新图表
        this.chart.setOption(this.option, true);
    },
    loopForSystemInfo: function () {
      if (this.timer != null) {
        window.clearTimeout(this.timer);
      }
      this.timer = setTimeout(() => {
        this.getSystemInfo();
        // this.getLoad();
        this.timer = null;
        this.loopForSystemInfo();
        // this.getResourceInfo()
      }, 2000);
    },
    getSystemInfo: function () {
      this.$axios({
        method: "get",
        url: `/api/server/system/info`,
      })
        .then((res) => {
          if (res.data.code === 0) {
            // 将数据映射到 x 轴和 y 轴
            var xData = res.data.data.mem.map(item => item.time); 
            var yData = res.data.data.mem.map(item => item.data);
            this.updateData(xData,yData);
          
            // this.chartData.rows = res.data.data.cpu;
            // this.$refs.consoleMem.setData(res.data.data.mem)
            // this.$refs.consoleNet.setData(res.data.data.net, res.data.data.netTotal)
            // this.$refs.consoleDisk.setData(res.data.data.disk)
          }
        })
        .catch((error) => {});
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
.div-item {
  display: flex; /* 使用flex布局 */
  flex-direction: column; /* 主轴方向为水平 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}
.div-item img {
  height: 55px;
}

.div-text-item {
  width: 90px;
  display: flex;
  flex-direction: column; /* 垂直排列 label */
  align-items: center; /* 水平居中 label */
}
.label1 {
  font-family: "HarmonyOS_Sans_Regular";
  font-size: 15px; /* 设置字体大小 */
  line-height: 1.5; /* 设置行高，控制行距 */
  text-align: center; /* 居中对齐文本 */
  color: white;
}
.label2 {
  font-family: "HarmonyOS_Sans_Regular";
  font-size: 20px; /* 设置字体大小 */
  line-height: 1.5; /* 设置行高，控制行距 */
  text-align: center; /* 居中对齐文本 */
  color: #2fb88a;
}
.div-items {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
</style>
