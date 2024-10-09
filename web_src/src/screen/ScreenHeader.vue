<template>
  <div class="header">
    <div class="header-left">
      <div class="header-left-select">
        <el-select v-model="year" class="header-left-select1" placeholder="">
        <el-option
          v-for="item in optionsYear"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
        </el-select>
        <el-divider direction="vertical" class="header-left-divider"></el-divider>
        <el-select v-model="month" class="header-left-select2" placeholder="">
          <el-option
            v-for="item in optionsMonth"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </div>
     
    </div>
    <div class="header-center">
      <h1 >国标管理平台</h1>
    </div>
    <div class="header-right">
      <div class="header-right-label">
        <label style="margin-right: 5px;">晴转多云</label>
        <img src="@/assets/screen/weather.png" style="margin-right: 10px;"/>
        <label>2024-01-03  13:49:12</label>
        <label>admin</label>
      </div>
    </div>
  </div>
</template>

<script>
import changePasswordDialog from '../components/dialog/changePassword.vue'
import userService from '../components/service/UserService'
import {Notification} from 'element-ui';

export default {
  name: "screenHeader",
  components: {Notification, changePasswordDialog},
  data() {
    return {
      optionsYear: [{
          value: '2023',
          label: '2023年'
        }, {
          value: '2024',
          label: '2024年'
        }],
        year: '2024',
        optionsMonth: [{
          value: '1',
          label: '1月'
        }, {
          value: '2',
          label: '2月'
        }],
        month: '2'
    };
  },
  created() {
   
  },
  mounted() {

  },
  methods: {

  },
  destroyed() {
   
  },

}

</script>
<style scoped>
@font-face {
  font-family: HarmonyOS_Sans_Regular;
  src: url('../assets/fonts/HarmonyOS_Sans_Regular.ttf');
}
.header {
    background-image: url('../assets/screen/header.png'); /* 背景图路径 */
    background-size: 100% 100%; /* 背景图铺满 */
    background-position: top center; /* 背景图居中对齐 */
    background-repeat: no-repeat; /* 不重复背景图 */
    height: 90px;
    display: flex; /* 使用flex布局 */
    flex-direction: row; /* 主轴方向为水平 */
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
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
