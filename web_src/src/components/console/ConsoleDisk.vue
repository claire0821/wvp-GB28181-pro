<template>
  <div id="ConsoleDisk" style="width: 100%; height: 100%; background: #FFFFFF; text-align: center">
    <!-- <ve-bar ref="ConsoleNet" :data="chartData" :extend="extend" :settings="chartSettings" width="100%" height="100%" ></ve-bar> -->
    <div ref="chart" :style="{ width: '100%', height: '100%' }"></div>
  </div>
</template>

<script>


import moment from "moment/moment";
import * as echarts from 'echarts';
export default {
  name: 'ConsoleDisk',
  components: {
    echarts,
  },
  data() {
    return {
      chart: {},
      chartData: {
        columns: ['path','free','use'],
        rows: []
      },
      chartSettings: {
        stack: {
          'xxx': ['free', 'use']
        },
        labelMap: {
          'free': '剩余',
          'use': '已使用'
        },
      },
      extend: {
        title: {
          show: true,
          text: "磁盘",
          left: "center",
          top: 20,
        },
        grid: {
          show: true,
          right: "30px",
          containLabel: true,
        },
        series: {
          barWidth: 30
        },
        legend: {
          left: "center",
          bottom: "15px",
        },
        tooltip: {
          trigger: 'axis',
          formatter: (data)=>{
            console.log(data)
            let relVal = "";
            for (let i = 0; i < data.length; i++) {
              relVal +=  data[i].marker + data[i].seriesName + ":" + data[i].value.toFixed(2) + "GB"
              if (i < data.length - 1) {
                relVal += "</br>";
              }
            }
            return relVal;
          }
        },

      }
    };
  },
  mounted() {
    this.$nextTick(_ => {
      setTimeout(()=>{
        this.$refs.ConsoleNet.echarts.resize()
      }, 100)
    })
    this.chart = echarts.init(this.$refs.chart);
    this.option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          // Use axis to trigger tooltip
          type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
        }
      },
      legend: {},
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'value'
      },
      yAxis: {
        type: 'category',
        data: []
      },
      series: [
        {
          name: 'use',
          type: 'bar',
          stack: 'total',
          label: {
            show: false
          },
          emphasis: {
            focus: 'series'
          },
          data: [320, 302, 301, 334, 390, 330, 320]
        },
        {
          name: 'free',
          type: 'bar',
          stack: 'total',
          label: {
            show: false
          },
          emphasis: {
            focus: 'series'
          },
          data: [120, 132, 101, 134, 90, 230, 210]
        },

      ]
    };
    // 更新图表
    this.chart.setOption(this.option, true);
  },
  destroyed() {
  },
  methods: {
    setData: function(data) {
      this.chartData.rows = data;
      console.log(data)
      var path = data.map(item => item.path); 
      var free = data.map(item => item.free); 
      var use = data.map(item => item.use); 
      this.option.yAxis.data = path;
      this.option.series[0].data = use;
      this.option.series[1].data = free;
      this.chart.setOption(this.option, true);
    }
  }
};
</script>
