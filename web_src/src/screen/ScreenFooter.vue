<template>
  <div class="footer">
    <div class="footer-background">
    </div>
    <div class="footer-content" >
      <div class="div-menu">
        <img src="@/assets/screen/menu1.png" />
        <label>综合态势</label>
      </div>
      <div class="div-menu">
        <img src="@/assets/screen/menu2.png" />
        <label>企业能耗</label>
      </div>
      <div class="div-menu">
        <img src="@/assets/screen/menu3.png" />
        <label>光储充电</label>
      </div>
      <div class="div-menu">
        <img src="@/assets/screen/menu4.png" />
        <label>视频监控</label>
      </div>
    </div>
  </div>
</template>

<script>

import userService from '../components/service/UserService'
import {Notification} from 'element-ui';

export default {
  name: "screenFooter",
}

</script>
<style>
@font-face {
  font-family: HarmonyOS_Sans_Regular;
  src: url('../assets/fonts/HarmonyOS_Sans_Regular.ttf');
}
.footer {
    height: 60px;
    display: flex; /* 使用flex布局 */
    flex-direction: row; /* 主轴方向为水平 */
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
  }
  .footer-background {
    position: absolute;
    background-image: url('../assets/screen/menu-bg.png');
    background-size: 100% 100%; /* 背景图铺满 */
    background-position: top center; /* 背景图居中对齐 */
    background-repeat: no-repeat; /* 不重复背景图 */
    width: 800px;
    height: 35px;
  }
  .footer-content {
    position: absolute;
    display: flex; /* 使用flex布局 */
    flex-direction: row; /* 主轴方向为水平 */
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
  }
  .div-menu {
    margin-right: 40px;
    display: flex; /* 使用flex布局 */
    flex-direction: column; /* 主轴方向为水平 */
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
  }
  .div-menu img {
    height: 40px;
  }
  .div-menu label {
    font-family: "HarmonyOS_Sans_Regular";
    font-size: 13px;
    font-weight: 200;
    letter-spacing: 4px;
    line-height: 15px;
    color: rgba(255, 255, 255, 1);
    text-align: center;
    vertical-align: top;
    margin-top: 0px;
  }
  .header-left {
    flex: 1;
    height: 100%;
    text-align: left;
    align-items: center; /* 垂直居中 */
    display: flex; /* 使用 Flexbox 布局 */
  }
  .header-center {
    flex: 1;
    height: 100%;
    /** 文本1 */
    font-weight: bold; /* 加粗 */
    font-style: italic; /* 斜体 */
    font-family: "PingFang SC";
    font-size: 25px;
    font-weight: 400;
    letter-spacing: 4px;
    line-height: 15px;
    color: rgba(255, 255, 255, 1);
    text-align: center;
    vertical-align: top;
  }
  .header-right {
    flex: 1;
    height: 100%;
    text-align: right;
    align-items: center; /* 垂直居中 */
    color: white;
    display: flex; /* 使用 Flexbox 布局 */
  }
  .header-left-select {
    background-image: url('../assets/screen/header-left.png'); /* 背景图路径 */
    background-size: 100% 100%; /* 背景图铺满 */
    background-position: top center; /* 背景图居中对齐 */
    background-repeat: no-repeat; /* 不重复背景图 */
    width: 160px;
    height: 40px;
    display: flex; /* 使用 Flexbox 布局 */
    align-items: center; /* 垂直居中对齐 */
  }
  .header-left-divider {
    .el-divider--vertical {
      margin: 0px;
    }
  }
  .header-left-select1 {
    margin: 0px;
    padding: 0px;
    display: flex;
    width: 70px;
    padding: 0px;
    /* height: 40px; */
    background: transparent;
    background-color: transparent;
    .el-input__inner {
      height: 40px;
      width: 70px;
      line-height: 40px;
      padding-right: 10px;
      padding-left: 0px;
      background-color: transparent;
      border: 0px;
      /* background-color: red; */
      color: white;
      border-color: white;
    }
    .el-input__prefix, .el-input__suffix {
      -webkit-transition: all .3s;
      color: white;
    }
  }
  .header-left-select2 {
    margin: 0px;
    padding: 0px;
    display: flex;
    width: 70px;
    padding: 0px;
    /* height: 40px; */
    background: transparent;
    background-color: transparent;
    .el-input__inner {
      height: 40px;
      width: 70px;
      line-height: 40px;
      padding-right: 10px;
      padding-left: 10px;
      background-color: transparent;
      border: 0px;
      /* background-color: red; */
      color: white;
      border-color: white;
    }
  }

  .header-right-label {
    flex: 1;
    width: 160px;
    height: 40px;
    text-align: right;
    display: flex; /* 使用 Flexbox 布局 */
    align-items: center; /* 垂直居中对齐 */
    justify-content: flex-end; /* 内容靠右对齐 */
    padding-right: 50px;
  }
  .header-right-label label{
    font-family: "HarmonyOS_Sans_Regular";
    font-size: 16px;
    font-weight: 400;
    letter-spacing: 0px;
    line-height: 18.75px;
    color: rgba(255, 255, 255, 1);
    text-align: left;
    vertical-align: top;
    margin-right: 10px;
  }
</style>
