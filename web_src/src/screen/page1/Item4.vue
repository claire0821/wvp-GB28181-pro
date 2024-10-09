<template>
  <div class="div-item-content">
    <item-box style="width: 550px; height: 100%" text="报警总览">
        <div ref="chart" :style="{ width: '550px', height: height }"></div>
    </item-box>
  </div>
</template>

<script>
import itemBox from "../ItemBox.vue";
import * as echarts from 'echarts';
export default {
  name: "item4",
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
            x2: "30px",
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
          color: 'auto'
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
