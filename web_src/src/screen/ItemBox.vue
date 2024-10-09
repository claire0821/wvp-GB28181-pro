<template>
     <div class="item-container">
    <div class="item-header">
        <img src="@/assets/screen/item-box-header-icon.png" />
        <div class="gradient-text">
            {{text}}
        </div>
    </div>
    <div class="content">
      <slot></slot>
    </div>
  </div>
</template>
<script>
export default {
  name: "itemBox",
  props: {
    text: {
      type: String,
      required: true // 确保外部传入文字
    }
  }
}

</script>
<style scoped>
@font-face {
  font-family: YouSheBiaoTiHei-2;
  src: url('../assets/fonts/YouSheBiaoTiHei-2.ttf');
}
  .item-container {
    width: 100%;
    height: 100%;
  }
  .item-header {
    background-image: url('../assets/screen/item-box-header.png');
    background-size: 100% 100%; /* 背景图铺满 */
    background-position: top center; /* 背景图居中对齐 */
    background-repeat: no-repeat; /* 不重复背景图 */
    width: 100%;
    height: 50px;
    display: flex; /* 使用flex布局 */
    flex-direction: row; /* 主轴方向为水平 */
    justify-content: left; /* 水平居中 */
    align-items: center; /* 垂直居中 */
  }
  .item-header img {
    margin-left: 5px;
  }
  .gradient-text {
    font-size: 20px;
    font-weight: 400;
    letter-spacing: 1px;
    line-height: 20.8px;
    margin-left: 10px;
    background: linear-gradient(to bottom, #FFFFFF 29.17%, #137E5A); /* 定义渐变颜色 */
    -webkit-background-clip: text; /* 让背景应用到文本上 */
    color: transparent; /* 隐藏文本颜色，显示背景渐变 */
    font-family: "YouSheBiaoTiHei-2";
    }

  .header-title {
    color: rgba(165, 216, 252, 1);
    font-size: 20px;
    font-weight: 500;
    z-index: 2;
    margin-left: 10px;
    margin-bottom: 0px;
    text-align: left;
  }
  
</style>
