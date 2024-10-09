<template>
  <div class="div-item-content">
    <item-box :style="{ width: '400px', height: '100%', }" text="节点负载">
        <div ref="chart" :style="{ width: '400px', height: height }"></div>
    </item-box>
  </div>
</template>

<script>
import itemBox from "../ItemBox.vue";
import * as echarts from 'echarts';
export default {
  name: "item5",
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
      chart : {},
    };
  },
  created() {
    this.getLoad();
    this.loopForSystemInfo();
  },
  mounted() {
    this.initChart();
    // const resizeObserver = new ResizeObserver(entries => {
    //   for(let entry of entries) {
    //     this.chart.resize();
    //   }
    // })
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
         title: {
            show: false,
          },
          grid: {
            x: "40px",
            y: "20px",
            x2: "20px",
            y2: "50px",
            backgroundColor: 'rgba(255,0,0,0)',
            borderColor: '#FFFFFF'
          },
          legend: {
            x: 'center',
            y: 'bottom',
            data:['直播推流','拉流代理','国际收流','国标推流'],
            textStyle: {
              color: 'auto'
            }
          },
          xAxis: {
            type: 'category',
            data: [],
            max: 'dataMax',
            axisLabel: {
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
            // boundaryGap: false,
            min: 0,
            max: 10,
            // //分割段数
            splitNumber: 2,
            position: "left",
            silent: true,
            axisLabel: {
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
           
          },
          series:[]
        }
        this.chart.setOption(this.option,true);
    },
    updateData: function (data) {
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
        // 更新图表
        this.chart.setOption(this.option, true);
      
    },
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
