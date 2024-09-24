<template>
  <div id="app" class="app-container">
  <NavBar></NavBar>
  <router-view class="content"></router-view><!-- 动态渲染 -->
  <BeiAn></BeiAn>
</div>
</template>

<script>
import NavBar from "@/components/NavBar.vue"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
import BeiAn from "@/components/BeiAn.vue"
export default{
  components: {
    NavBar,
    BeiAn
  },
}
</script>

<style>

body{
  background-image: url("@/assets/images/background.png");
  background-size: cover;
}
html, body {
  height: 100%; /* 设置高度为100% */
  margin: 0; /* 去掉默认的边距 */
}

.app-container {
  display: flex;
  flex-direction: column; /* 纵向排列 */
  min-height: 100vh; /* 最小高度为100vh，确保内容填满整个视口 */
}

.content {
  flex: 1; /* 内容区域扩展以填充剩余空间 */
}
</style>
