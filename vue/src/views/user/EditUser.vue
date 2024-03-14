<template>
  <div style="width: 70%">
    <h2 style="margin-bottom: 30px">编辑用户</h2>
    <el-form :inline="true" :model="form"label-width="100px">
      <el-form-item label="卡号" prop="username">
        <el-input v-model="form.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model="form.age" placeholder="请输入年龄"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
<!--        <el-input v-model="form.sex" placeholder="请输入性别"></el-input>-->
        <el-radio v-model="form.sex" label="男">男</el-radio>
        <el-radio v-model="form.sex" label="女">女</el-radio>
        <el-radio v-model="form.sex" label="武装直升机">武装直升机</el-radio>
        <el-radio v-model="form.sex" label="沃尔玛购物袋">沃尔玛购物袋</el-radio>
      </el-form-item>
      <el-form-item label="联系方式" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="form.address" placeholder="请输入地址"></el-input>
      </el-form-item>
      <el-form-item label="账户余额" prop="address">
        <el-input v-model="form.account" disabled placeholder="请输入账户余额"></el-input>
      </el-form-item>
      <div style="text-align: center; margin-top: 30px">
        <el-button type="primary" @click="save" size="medium">提交</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: 'EditUser',
  data() {
    return {
      form: {}
    }
  },
  created() {
    const id = this.$route.query.id
    request.get("/user/" + id).then(res => {
      console.log(res)
      this.form = res.data
    })
  },
  methods: {
    save() {
      request.put('/user/update', this.form).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$notify.success('更新成功')
          this.$router.push('/userList')
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