<template>
  <div style="width: 70%">
    <h2 style="margin-bottom: 30px">编辑用户</h2>
    <el-form :inline="true" :model="form"label-width="100px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="联系方式" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <div style="text-align: center; margin-top: 30px">
        <el-button type="primary" @click="save" size="medium">提交</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import request from "@/utils/request";
import {checkAdmin} from "@/utils/check";

export default {
  name: 'EditAdmin',
  data() {
    return {
      form: {}
    }
  },
  created() {
    checkAdmin()
    const id = this.$route.query.id
    request.get("/admin/" + id).then(res => {
      console.log("aaa",res)
      this.form = res.data
    })
  },
  methods: {
    save() {
      request.put('/admin/update', this.form).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$notify.success('更新成功')
          this.$router.push('/adminList')
        }else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}

</script>

<style>


</style>