<template>
  <div style="text-align: center">
    <div style="margin-top: 150px; font-size: 100px">404</div>
    <div style="font-size: 50px">页面不存在哦~</div>
    <div>
      <el-button type="text" style="margin-top: 20px; font-size: 20px" @click="$router.push('/')">
        点击返回首页
      </el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "404.vue"
}
</script>

<style scoped>

</style>