<template>
  <div class="div-content">
    <div ref="divLeft" class="div-left">
      <item1 :height="heightItem1" />
      <item2 :height="heightItem2" style="margin-top: 10px;"/>
      <item5 :height="heightItem5" style="margin-top: 10px;"/>
    </div>
    <div ref="divCenter" class="div-center">
      <item8 ref="item7Component" />
    </div>
    <div ref="divRight" class="div-right">
      <item4 :height="heightItem4"/>
      <item6 :height="heightItem6" style="margin-top: 10px;"/>
    </div>
  </div>
</template>

<script>
import item1 from "./Item1.vue";
import item2 from "./Item2.vue";
import item3 from "./Item3.vue";
import item4 from "./Item4.vue";
import item5 from "./Item5.vue";
import item6 from "./Item6.vue";
import item7 from "./Item7.vue";
import item8 from "./Item8.vue";
export default {
  name: "page1",
  components: {
    item1,item2,item3,item4,item5,item6,item7,item8
  },
  data() {
    return {
      heightItem1: 200,
      heightItem2: 300,
      heightItem3: 300,
      heightItem4: 400,
      heightItem5: 300,
      heightItem6: 300,
      heightItem7: 0,
      widthItem7: 0,
    };
  },
  mounted() {
    // 创建 ResizeObserver 实例
    this.resizeObserver = new ResizeObserver(entries => {
      for (let entry of entries) {
        if(entry.target === this.$refs.divLeft) {
          const { height } = entry.contentRect;
          this.heightItem1 = height * 0.2;
          this.heightItem2 = height * 0.4 - 15;
          this.heightItem5 = height * 0.4 - 15;
        } else if(entry.target === this.$refs.divRight) {
          const { height } = entry.contentRect;
          this.heightItem4 = height * 0.5;
          this.heightItem6 = height * 0.5 - 15;
        } else if(entry.target === this.$refs.divCenter) {
          const { height,width } = entry.contentRect;
          this.heightItem7 = height;
          this.widthItem7 = width;
          this.$refs.item7Component.resize(width,height);
        }
      }
    });
    
    // 监听元素
    this.resizeObserver.observe(this.$refs.divLeft);
    this.resizeObserver.observe(this.$refs.divRight);
    this.resizeObserver.observe(this.$refs.divCenter);
  },
  beforeDestroy() {
    // 在组件销毁前停止监听
    if (this.resizeObserver) {
      this.resizeObserver.disconnect();
    }
  },
};
</script>
<style scoped>
.div-content {
    display: flex;
    flex-direction: row; 
    align-items: center;
    justify-content: space-between;
    height: 100%;
}
.div-left{
    width: 400px;
    display: flex;
    flex-direction: column; 
    align-items: top;
    justify-content: space-between;
    height: 100%;
}
.div-center{
    flex-grow: 1; /* 中间区域占满剩余空间 */
    display: flex;
    flex-direction: column; 
    align-items: top;
    justify-content: space-between;
    height: 100%;
}
.div-right{
    width: 550px;
    display: flex;
    flex-direction: column; 
    align-items: top;
    justify-content: space-between;
    height: 100%;
}
</style>
