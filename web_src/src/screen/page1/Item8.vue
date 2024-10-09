<template>
  <div class="div-item-content">
    <div id="echart_china" ref="echart_china" :style="{ width: mapWidth, height: mapHeight}" ></div>
  </div>
</template>
<script>
// import chinamap from "echarts/map/json/china.json";
import "@/lib/china.js"
import * as echarts from 'echarts';
import 'echarts-gl';

export default {
  data() {
    return {
      width: 0,
      height: 0,
      mapWidth: '100px',
      mapHeight: '100px',
      myChart: null,
      sseSource: null,
    };
  },
  mounted() {
    this.myChart = echarts.init(this.$refs.echart_china);
    this.init();
    setTimeout(() => {
      if(this.myChart) {
        this.myChart.resize();
      }
    }, 1000);
 
  },
  destroyed() {
    if(this.myChart) {
      this.myChart.dispose();
    }
  },
  methods: {
    /*
      显示中国地图
    */
    init() {
      // this.mapWidth = this.width + 'px';
      // this.mapHeight = this.height + 'px';
      // console.log(this.width + ":" + this.height)
      if(this.myChart) {
        this.myChart.resize();
      }

      // 3. 设置图表 option
      var option = {
        backgroundColor: 'transparent',
        globe: {
          baseTexture: require('@/assets/map/earth.jpg'),
          heightTexture: require('@/assets/map/bathymetry_bw_composite_4k.jpg'),
          displacementScale: 0.1,
          shading: 'lambert',
          // environment: require('@/assets/map/starfield.jpg'),
          light: {
            ambient: {
              intensity: 0.1
            },
            main: {
              intensity: 1.5
            }
          },
          layers: [
            {
              type: 'blend',
              blendTo: 'emission',
              texture: require('@/assets/map/night.jpg') 
            },
            {
              type: 'overlay',
              texture: require('@/assets/map/clouds.png'),
              shading: 'lambert',
              distance: 5
            }
          ]
        },
        series: []
      };
      // 4. 显示地图
      this.myChart.setOption(option); // 用 option 和 option2 效果一样
    },
    resize(width,height) {
      this.mapWidth = width + 'px';
      this.mapHeight = height + 'px';
      if(this.myChart) {
        this.myChart.resize();
      }
    },
  
  },
};
</script>
<style scoped>
.div-item-content {
  width: 100%;
  height: 100%;
}
</style>