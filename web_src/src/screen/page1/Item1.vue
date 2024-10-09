<template>
    <div class="div-item-content">
      <item-box :style="{ width: '400px', height: height }" text="系统总览">
        <div class="div-items">
          <div class="div-item">
            <img src="@/assets/screen/icon1.png" />
              <div class="div-text-item">
                <label class="label1">设备总数</label>
                <label class="label2">2</label>
              </div>
          </div>
          <div class="div-item">
            <img src="@/assets/screen/icon1.png" />
              <div class="div-text-item">
                <label class="label1">通道总数</label>
                <label class="label2">2</label>
              </div>
          </div>
          <div class="div-item">
            <img src="@/assets/screen/icon1.png" />
              <div class="div-text-item">
                <label class="label1">推流总数</label>
                <label class="label2">2</label>
              </div>
          </div>
          <div class="div-item">
            <img src="@/assets/screen/icon1.png" />
              <div class="div-text-item">
                <label class="label1">拉流代理总数</label>
                <label class="label2">2</label>
              </div>
          </div>
        </div>
      </item-box>
    </div>
  </template>
  
  <script>
  import itemBox from "../ItemBox.vue";
  export default {
    name: "item1",
    components: {
      itemBox,
    },
    props: {
    height: {
      type: Number,
      default: 200, // 如果没有传递 height 时的默认值
    }
  },
  };
  </script>
  <style scoped>
  @font-face {
    font-family: HarmonyOS_Sans_Regular;
    src: url('../../assets/fonts/HarmonyOS_Sans_Regular.ttf');
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
    color: #2FB88A;
  }
  .div-items {
     display: flex;
     flex-direction: row; 
     align-items: center;
     justify-content: space-between;
     margin-top: 10px;
     margin-bottom: 10px;
  }
  </style>
  