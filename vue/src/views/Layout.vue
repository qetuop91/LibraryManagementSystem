<template>
  <div>
    <!--  头部标题  -->
    <div style="height: 60px; line-height: 60px; background-color: white; margin-bottom: 2px; display: flex">
      <div style="width: 300px">
        <img src="@/assets/logo.png" alt="" style="width: 40px; position: relative; top: 10px; left: 20px">
        <span style="margin-left: 25px; font-size: 24px">图书管理系统</span>
      </div>
      <div style="flex: 1; text-align: right; padding-right: 20px;">
        <el-dropdown size="medium">
          <span class="el-dropdown-link" style="cursor: pointer">
            {{ admin.username }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown" style="margin-top: -5px">
            <el-dropdown-item>
              <div style="width: 60px; text-align: center" @click="logout">登出</div>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!--  侧边栏布局  -->
    <div style="display: flex;">
      <!--  侧边栏导航  -->
      <div style="width: 200px; min-height: calc(100vh - 62px);overflow: hidden; margin-right: 2px;background-color: white">
        <el-menu :default-active="$route.path" router class="el-menu-demo">
          <el-menu-item index="/">
            <i class="el-icon-s-home"></i>
            <span>首页</span>
          </el-menu-item>
          <!-- User -->
          <el-submenu index="user">
            <template slot="title">
              <i class="el-icon-s-custom"></i>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/addUser"><span>用户添加</span></el-menu-item>
            <el-menu-item index="/userList"><span>用户列表</span></el-menu-item>
          </el-submenu>
          <!-- Admin -->
          <el-submenu index="admin">
            <template slot="title">
              <i class="el-icon-user-solid"></i>
              <span>管理员管理</span>
            </template>
            <el-menu-item index="/addAdmin"><span>管理员添加</span></el-menu-item>
            <el-menu-item index="/adminList"><span>管理员列表</span></el-menu-item>
          </el-submenu>
          <!-- Category -->
          <el-submenu index="category">
            <template slot="title">
              <i class="el-icon-s-operation"></i>
              <span>分类管理</span>
            </template>
            <el-menu-item index="/addCategory"><span>主分类添加</span></el-menu-item>
            <el-menu-item index="/categoryList"><span>分类列表</span></el-menu-item>
          </el-submenu>
          <!-- Book -->
          <el-submenu index="book">
            <template slot="title">
              <i class="el-icon-notebook-1"></i>
              <span>书籍管理</span>
            </template>
            <el-menu-item index="/addBook"><span>书籍添加</span></el-menu-item>
            <el-menu-item index="/bookList"><span>书籍列表</span></el-menu-item>
          </el-submenu>
          <!-- Borrow -->
          <el-submenu index="borrow">
            <template slot="title">
              <i class="el-icon-notebook-2"></i>
              <span>借阅管理</span>
            </template>
            <el-menu-item index="/addBorrow"><span>添加借阅信息</span></el-menu-item>
            <el-menu-item index="/borrowList"><span>借阅信息列表</span></el-menu-item>
          </el-submenu>
        </el-menu>
      </div>
      <!--  主体数据  -->
      <!--  width: 0 可以限制子元素容器的宽度，保证不会被子元素无限撑开  -->
      <div style="flex: 1;width: 0; background-color: white; padding: 10px">
        <router-view/>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import {removeToken, setToken} from "@/utils/auth";
import Cookies from "js-cookie";
import {checkAdmin} from "@/utils/check";

export default {
  name: "Layout.vue",
  data() {
    return {
      form: {},
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
    }
  },
  created() {
    checkAdmin()
  },
  methods: {
    logout() {
      request.post('/logout', this.form).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$router.push('/login')
          console.log("logout")
          localStorage.removeItem("token")
          removeToken()
          Cookies.remove('admin')
          console.log(res)
          this.$notify.success("成功退出登录")
        } else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>