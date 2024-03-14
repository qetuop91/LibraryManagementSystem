<template>
  <div>
    <div>
      <!-- 搜索表单 -->
      <el-input style="width: 240px" placeholder="请输入用户名" v-model="params.username"></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入联系方式" v-model="params.phone"></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入邮箱" v-model="params.email"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i> 搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-refresh"></i> 重置</el-button>
    </div>
    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="编号" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="phone" label="联系方式"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="updateTime" label="修改时间"></el-table-column>
      <!-- 状态栏 -->
      <el-table-column label="状态" width="80px">
        <template v-slot="scope">
          <el-switch
              v-model="scope.row.status"
              @change="changeStatus(scope.row)"
              active-color="#13ce66"
              inactive-color="#ff4949">
          </el-switch>
        </template>
      </el-table-column>
      <!-- 操作栏 -->
      <el-table-column v-if="isAdmin" label="操作" width="250px" style="text-align: center">
        <template v-slot="scope"><!-- scope.row就是当前行数据 -->
          <!-- 编辑 -->
          <el-button type="primary" @click="$router.push('/editAdmin?id=' + scope.row.id)">编辑</el-button>
          <!-- 删除 -->
          <el-popconfirm style="margin-left: 5px" title="确定删除这一数据?" @confirm="del(scope.row.id)">
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
          <!-- 修改密码 -->
          <el-button type="warning" style="margin-left: 5px" @click="changePwd(scope.row)">修改密码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--  分页  -->
    <div style="margin-top: 20px;">
      <el-pagination
          background
          :current-page="params.pageNum"
          :page-size="params.pageSize"
          layout="prev, pager, next, total"
          @current-change="handleCurrentChange"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" label-width="100px" ref="formRef" :rules="rules">
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="savePwd">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import Cookies from "js-cookie";
import {removeToken} from "@/utils/auth";
import {checkAdmin} from "@/utils/check";

export default {
  name: 'AdminList',
  data() {
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      tableData: [],
      total: 0,
      form: {},
      dialogFormVisible: false,
      isAdmin: true,
      params: {
        pageNum: 1,
        pageSize: 10,
        username: '',
        phone: '',
        email: '',
      },
      rules: {
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在3-10个字符', trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    checkAdmin()
    this.load()
  },
  methods: {
    load() {
      request.get('/admin/page', {
        params: this.params
      }).then(res => {
        if (res.code === 200) {
          console.log(res)
          this.isSuperAdmin()
          // console.log('this.params: ', this.params)
          // console.log('total: ', res.data.total)
          // console.log('pageNum: ', res.data.current)
          // console.log('pageSize: ', res.data.size)
          this.total = Number(res.data.total)
          this.tableData = res.data.records
        }
      })
    },
    reset() {
      this.params = {
        pageNum: 1,
        pageSize: 10,
        username: '',
        phone: '',
        email: ''
      }
      this.load()
    },
    isSuperAdmin() {
      const id = JSON.parse(Cookies.get('admin')).id;
      console.log(id);
      if (id === 1) {
        this.isAdmin = true
      }else {
        this.isAdmin = false
      }
    },
    handleCurrentChange(pageNum) {
      // 点击触发分页按钮
      console.log(pageNum)
      this.params.pageNum = pageNum
      this.load()
    },
    del(id) {
      request.delete('/admin/delete/' + id).then(res => {
        console.log('delRes: ', res)
        if (this.admin.id === id) {// 当前删除的用户id等于当前登陆的管理员id
          this.$notify.warning('不能删除当前正在登录的账号')
          return
        }
        if (res.code === 200) {
          this.$notify.success('删除成功')
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    changePwd(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    savePwd() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          request.put('/admin/changePassword', this.form).then(res => {
            if (res.code === 200) {
              this.$notify.success('修改密码成功')
              if (this.form.id === this.admin.id) { // 当前修改的用户id等于当前登陆的管理员id，修改成功之后需要重新登陆
                Cookies.remove('Admin-Token')
                Cookies.remove('admin')
                this.$notify.success('当前登录用户密码被修改，请重新登录')
                this.$router.push('/login')
              }
              // console.log(res)
              this.dialogFormVisible = false
              this.load()
            } else {
              this.$notify.error('修改密码失败')
            }
          })
        }
      })
    },
    changeStatus(row) {
      if (row.id === 1) {
        row.status = true
        this.$notify.warning("超级管理员状态不可被修改")
        return
      }
      if (this.admin.id === row.id) {
        row.status = true
        this.$notify.warning("当前已登录的用户状态不可修改")
        return
      }
      request.put('admin/update', row).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$notify.success('状态更新成功')
          this.load()
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