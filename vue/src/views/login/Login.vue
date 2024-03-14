<template>

  <div style="height: 100vh; overflow: hidden; position: relative">
    <el-card class="cover" v-if="loginAdmin.id">
      <div style="text-align: right; margin: 0; padding: 0">
        <i class="el-icon-close" style="font-size: 30px; cursor: pointer" @click="esc"></i>
      </div>
      <slide-verify :l="42"
                    :r="10"
                    :w="310"
                    :h="155"
                    :imgs="imgList"
                    slider-text="向右滑动"
                    @success="onSuccess"
                    @fail="onFail"
                    @refresh="onRefresh"
      ></slide-verify>
    </el-card>

    <div
        style="width: 500px; height: 400px; background-color: white; border-radius: 10px;margin: 150px auto; padding: 40px 50px">
      <div style="margin: 30px; text-align: center;font-size: 30px; font-weight: bold; color: dodgerblue">登录</div>
      <el-form :model="admin" ref="loginForm" :rules="rules">
        <el-form-item prop="username">
          <el-input placeholder="请输入账号" prefix-icon="el-icon-user" size="medium" v-model="admin.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" show-password prefix-icon="el-icon-user" size="medium" v-model="admin.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" size="medium" type="primary" @click="login">Login</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import {setToken} from "@/utils/auth";
import Cookies from "js-cookie";
import axios from "axios";

export default {
  name: "Login",
  data() {
    return {
      admin: {},
      loginAdmin: {},
      imgList: [
        require('../../assets/img1.jpg'),
        require('../../assets/img2.jpg'),
        require('../../assets/img3.jpg'),
        require('../../assets/img4.jpg'),
      ],
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在3-10个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在3-10个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    login() {
      this.$refs['loginForm'].validate((valid) => {
        if (valid) {
          request.post('/login', this.admin).then(res => {
            // console.log("test")
            if (res.code === 200) {
              // 弹出滑块组件并将后端返回的数据赋值给loginAdmin
              this.loginAdmin = res.data
              localStorage.setItem("token", this.loginAdmin.token)
              setToken(this.loginAdmin.token)
            } else {
              this.$notify.error(res.msg)
            }
          })
        }
      })
    },
    // 滑块验证成功后
    onSuccess() {
      console.log('onSuccess')
      let json;
      if (this.loginAdmin !== null) {

        json = {
          "id": Number(this.loginAdmin.id),
          "username": this.loginAdmin.username,
          "phone": this.loginAdmin.phone,
          "email": this.loginAdmin.email
        }
        Cookies.set("admin", JSON.stringify(json))
      }
      console.log("login")
      if (Number(this.loginAdmin.id) === 1) {
        this.$notify.success("超级管理员" + this.loginAdmin.username + "，你好！")
      }else{
        this.$notify.success("普通管理员" + this.loginAdmin.username + "，你好！")
      }
      this.$router.push('/')
    },
    onFail() {
      console.log('onFail')
    },
    onRefresh() {
      console.log('onRefresh')
    },
    esc() {
      this.loginAdmin = {}
    }
  }
}
</script>

<style>
.cover{
  width: fit-content;
  background-color: white;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}
</style>