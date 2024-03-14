<template>
  <div style="width: 70%">
    <h2 style="margin-bottom: 30px">新增借阅信息</h2>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100">
      <el-form-item label="书籍标准码" prop="bookNumber">
        <el-select v-model="form.bookNumber" filterable placeholder="请输入书籍标准码" @change="selectBookNumber">
          <el-option
              v-for="book in books"
              :key="book.id"
              :label="book.bookNumber"
              :value="book.bookNumber">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="书籍名称" prop="bookName">
        <el-input v-model="form.bookName" disabled></el-input>
      </el-form-item>
      <el-form-item label="书籍积分" prop="score">
        <el-input v-model="form.score" disabled></el-input>
      </el-form-item>
      <el-form-item label="书籍数量: " prop="nums">
        <el-input v-model="form.nums" disabled></el-input>
      </el-form-item>

      <br>

      <el-form-item label="用户卡号" prop="userUsername">
        <el-select v-model="form.userUsername" filterable placeholder="请输入用户卡号" @change="selectUserCard">
          <el-option
              v-for="user in users"
              :key="user.id"
              :label="user.username"
              :value="user.username">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="用户名称" prop="userName">
        <el-input v-model="form.userName" disabled></el-input>
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model="form.userId" disabled></el-input>
      </el-form-item>
      <el-form-item label="联系方式" prop="userPhone">
        <el-input v-model="form.userPhone" disabled></el-input>
      </el-form-item>
      <el-form-item label="用户账户积分" prop="account">
        <el-input v-model="form.account" disabled></el-input>
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
  name: 'AddBorrow',
  data() {
    return {
      form: {},
      books: [],
      users: [],
      rules: {
        bookNumber: [
          {required: true, message: '请输入书籍标准码', trigger: 'blur'}
        ],
        userUsername: [
          {required: true, message: '请输入用户卡号', trigger: 'blur'}
        ],

      }
    }
  },
  created() {
    checkAdmin()
    request.get('/book/list').then(res => {
      console.log('/book/list: ', res)
      this.books = res.data
    })

    request.get('/user/list').then(res => {
      console.log('/user/list: ', res)
      this.users = res.data
    })
  },
  methods: {
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          console.log(this.form)
          request.post('/borrow/save', this.form).then(res => {
            if (res.code === 200) {
              this.$notify.success('新增成功');
              this.$refs['ruleForm'].resetFields();
            } else {
              this.$notify.error(res.msg)
            }
          })
        }
      })
    },
    selectBookNumber() {
      const book = this.books.find(v => v.bookNumber === this.form.bookNumber)
      request.get('/book/' + book.id).then(res => {
        this.$set(this.form, 'bookName', res.data.bookName)
        this.form.score = res.data.score
        this.form.nums = res.data.nums
      })
    },
    selectUserCard() {
      const user = this.users.find(v => v.username === this.form.userUsername)
      request.get('/user/' + user.id).then(res => {
        this.$set(this.form, 'userName', res.data.name)
        this.$set(this.form, 'userId', res.data.id)
        this.form.userPhone = res.data.phone
        this.form.account = res.data.account
      })
    }
  }
}

</script>

<style>


</style>