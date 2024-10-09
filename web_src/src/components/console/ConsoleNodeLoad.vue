<template>
  <div id="ConsoleNodeLoad" style="width: 100%; height: 100%; background: #FFFFFF; text-align: center">
    <!-- <ve-histogram ref="consoleNodeLoad" :data="chartData" :extend="extend"  :settings="chartSettings" width="100%" height="100%" :legend-visible="true"></ve-histogram> -->
    <div ref="chart" :style="{ width: '100%', height: '100%' }"></div>
 </div>
</template>

<script>


import moment from "moment/moment";
import * as echarts from 'echarts';

export default {
  name: 'ConsoleNodeLoad',
  components: {
    echarts,
  },
  data() {
    return {
      chart: {},
      option: {},
      chartData: {
        columns: ['id', 'push', 'proxy', 'gbReceive', 'gbSend'],
        rows: []
      },
      chartSettings: {
        labelMap: {
          'push': '直播推流',
          'proxy': '拉流代理',
          'gbReceive': '国标收流',
          'gbSend': '国标推流',
        },
      },
      extend: {
        title: {
          show: true,
          text: "节点负载",
          left: "center",
          top: 20,

        },
        legend: {
          left: "center",
          bottom: "15px",
        },
        label: {
          show: true,
          position: "top"
        }
      }
    };
  },
 
  methods: {
    setData: function(data) {
      this.chartData .rows = data;
    // 将数据映射到 x 轴和 y 轴
    var xData = data.map(item => item.id); 
        var push = data.map(item => item.push);
        var proxy = data.map(item => item.proxy);
        var gbReceive = data.map(item => item.gbReceive);
        var gbSend = data.map(item => item.gbSend);
        // 只更新 xAxis 和 series 数据
        this.option.xAxis.data = xData; // 更新 xAxis 的数据
        this.option.series =  [
        {
            name: '直播推流',
            type: 'bar',
            itemStyle: {
              normal: {
                color: '#3DFEFACC'
              },
              emphasis: {
                color: '#3DFEFA'
              }
            },
            data: push
        },
        {
            name: '拉流代理',
            type: 'bar',
            itemStyle: {
              normal: {
                color: '#9BD801CC'
              },
              emphasis: {
                color: '#9BD801'
              }
            },
            data: proxy
        },
        {
            name: '国际收流',
            type: 'bar',
            itemStyle: {
              normal: {
                color: '#D83D6CCC'
              },
              emphasis: {
                color: '#D83D6C'
              }
            },
            data: gbReceive
        },
        {
            name: '国标推流',
            type: 'bar',
            itemStyle: {
              normal: {
                color: '#137E5ACC',
              },
              emphasis: {
                color: '#137E5A'
              }
            },
            data: gbSend
        }]
        console.log(this.option)
        // 更新图表
        this.chart.setOption(this.option, true);
    },
  },
  mounted() {
    this.$nextTick(_ => {
      setTimeout(()=>{
        this.$refs.consoleNodeLoad.echarts.resize()
      }, 100)
    })
    this.chart = echarts.init(this.$refs.chart);
      this.option = {
        legend: {
            data:['直播推流','拉流代理','国际收流','国标推流'],
            textStyle: {
              color: 'auto'
            }
          },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar'
          }
        ]
      };
      this.chart.setOption(this.option,true);
  },
  destroyed() {
  },
};
</script>
