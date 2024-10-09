<template>
  <el-container style="height: 100%" class="container">
    <el-header style="height: 90px;">
      <screen-header/>
    </el-header>
    <el-main style="background: transparent;">
      <router-view></router-view>
    </el-main>
    <el-footer style="height: 60px;"><screen-footer /></el-footer>
  </el-container>
</template>

<script>
import screenHeader from "./ScreenHeader.vue";
import screenFooter from "./ScreenFooter.vue";

export default {
  name: "screen",
  components: {
    screenHeader,screenFooter
  },
}
</script>
<style>
body{
  font-family: sans-serif;
}
.container {
    background-image: url('../assets/screen/bg.png'); 
    background-size: 100% 100%; /* 背景图铺满 */
    background-position: top center; /* 背景图居中对齐 */
    background-repeat: no-repeat; /* 不重复背景图 */
  }

</style>
