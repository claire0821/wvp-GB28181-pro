<template>
  <div class="div-item-content">
    <item-box style="width: 400px; height: 100%" text="CPU">
        <div ref="chart" :style="{ width: '400px', height: height }"></div>
    </item-box>
  </div>
</template>

<script>
import itemBox from "../ItemBox.vue";
// import echarts from "echarts";
import * as echarts from 'echarts';
import moment from "moment/moment";

export default {
  name: "item2",
  components: {
    itemBox,
    echarts,
  },
  props: {
    height: {
      type: Number,
      default: 300, // 如果没有传递 height 时的默认值
    }
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
  watch: {
    // 监听 height prop 的变化
    height(newHeight) {
      this.$refs.chart.style.height = newHeight - 50 + 'px';
      if(this.chart) {
        this.chart.resize();
      }
    }
  },
  destroyed() {
    if(this.chart) {
      this.chart.dispose();
    }
  },
  methods: {
    initChart: function () {
    // this.chart = this.$echarts(this.$el);
    this.chart = echarts.init(this.$refs.chart);
    // 更新图表
    this.option = {
         title: {
            show: false,
          },
          grid: {
            x: "40px",
            y: "25px",
            x2: "20px",
            y2: "20px",
            backgroundColor: 'rgba(255,0,0,0)',
            borderColor: '#FFFFFF'
          },
          legend: {
            show: true,
            x: 'center',
            y: 'top',
            data:['CPU','内存'],
            textStyle: {
                color: '#FFFFFF'
              }
           },
          xAxis: {
            data: [],
            time: "time",
            max: 'dataMax',
            boundaryGap: ['20%', '20%'],
            axisLabel: {
              formatter:(v)=>{
                return moment(v).format("HH:mm:ss");
              },
              showMaxLabel: true,
              textStyle: {
                color: '#FFFFFF'
              }
            },
            axisLine: {
                lineStyle: {
                    color: '#FFFFFF',
                }
            }
          },
          yAxis: {
            type: 'value',
            boundaryGap: false,
            min: 0,
            max: 1,
            //分割段数
            splitNumber: 2,
            position: "left",
            silent: true,
            axisLabel: {
              formatter: (v)=>{
                return v*100 + "%";
              },
              textStyle: {
                color: '#FFFFFF'
              }
            },
            //坐标轴线
            axisLine: {
                show: false
            },
            //分割线
            splitLine: {
                lineStyle: {
                    color: ['#FFFFFF'],
                    width: 0.5,
                    type: 'solid '
                }
            }
          },
          tooltip: {
            trigger: 'axis',
            formatter: (data)=>{
              return moment(data[0].data[0]).format("HH:mm:ss") +  "</br> "
                + data[0].marker + "使用：" + (data[0].data[1]*100).toFixed(2) + "%";
            }
          },
          series: [{
            smooth: true,
            name: "CPU",
            type: 'line', // 指定 series 的类型为折线图
            symbol: 'circle', // 设置标记的图形为circle
            itemStyle: {
              color: "rgba(61, 254, 250, 1)",
            //   borderColor: '#FFFFFF'
            },
            lineStyle: {
                color: "rgba(61, 254, 250, 1)",
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(61, 254, 250, 0.1)' // 0% 处的颜色
                }, {
                  offset: 1, color: 'rgba(61, 254, 250, 1)' // 100% 处的颜色
                }],
                global: false // 缺省为 false
              }
            },
            data: [],
          },
          {
            name: "内存",
            smooth: true,
            type: 'line', // 指定 series 的类型为折线图
            symbol: 'circle', // 设置标记的图形为circle
            itemStyle: {
              color: "rgba(19, 126, 90, 1)",
            //   borderColor: '#FFFFFF'
            },
            lineStyle: {
                color: "rgba(19, 126, 90, 1)",
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(19, 126, 90, 0.1)' // 0% 处的颜色
                }, {
                  offset: 1, color: 'rgba(19, 126, 90, 1)' // 100% 处的颜色
                }],
                global: false // 缺省为 false
              }
            },
            data: [],
          }]
    }


    this.chart.setOption(this.option);
    },
    updateData: function (xData,cpuData,memoryData) {
        // 只更新 xAxis 和 series 数据
        this.option.xAxis.data = xData; // 更新 xAxis 的数据
        this.option.series[0].data = cpuData; // 更新 series 的数据
        this.option.series[1].data = memoryData; // 更新 series 的数据
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
            var xData = res.data.data.cpu.map(item => item.time); 
            var cpuData = res.data.data.cpu.map(item => item.data);
            var memoryData = res.data.data.mem.map(item => item.data);
            this.updateData(xData,cpuData,memoryData);
          
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
</style>
