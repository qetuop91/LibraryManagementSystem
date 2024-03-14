<template>
  <div style="width: 70%">
    <h2 style="margin-bottom: 30px">编辑借阅信息</h2>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100">
        <el-form-item label="书籍标准码" prop="bookNumber">
          <el-select v-model="form.bookNumber" filterable placeholder="请输入书籍标准码" @change="selectBookNumber">
            <el-option
                v-for="item in books"
                :key="item.id"
                :label="item.bookNumber"
                :value="item.bookNumber">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="书籍名称" prop="bookName">
          <el-input v-model="form.bookName" disabled placeholder="书籍名称"></el-input>
        </el-form-item>
        <el-form-item label="书籍积分" prop="score">
          <el-input v-model="form.score" disabled placeholder="积分"></el-input>
        </el-form-item>
        <el-form-item label="书籍数量: " prop="nums">
          <el-input v-model="form.nums" disabled placeholder="数量"></el-input>
        </el-form-item>

        <br>

        <el-form-item label="用户卡号" prop="userUsername">
          <el-select v-model="form.userUsername" filterable placeholder="请输入用户卡号" @change="selectUserCard">
            <el-option
                v-for="item in users"
                :key="item.id"
                :label="item.username"
                :value="item.username">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="form.userName" disabled placeholder="用户名称"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="userPhone">
          <el-input v-model="form.userPhone" disabled placeholder="联系方式"></el-input>
        </el-form-item>
        <el-form-item label="用户账户积分" prop="account">
          <el-input v-model="form.account" disabled placeholder="积分"></el-input>
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
  name: 'EditBorrow',
  data() {
    return {
      form: {},
      books: [],
      users: [],
      rules: {
        bookNumber:[
          {required: true, message: '请输入书籍标准码', trigger: 'blur'}
        ],
        userUsername:[
          {required: true, message: '请输入用户卡号', trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    checkAdmin()
    const id = this.$route.query.id
    request.get("/borrow/" + id).then(res => {
      console.log('created res: ', res)
      this.form = res.data
    })

    request.get('/book/list').then(res => {
      console.log(res)
      this.books = res.data
    })

    request.get('/user/list').then(res => {
      console.log(res)
      this.users = res.data
    })
  },
  methods: {
    save() {
      request.put('/borrow/update', this.form).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$notify.success('更新成功')
          this.$router.push('/borrowList')
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    selectBookNumber() {
      const book = this.books.find(v => v.bookNumber === this.form.bookNumber)
      // this.form.bookName = book.bookName
      // this.form.score = book.score
      // this.form.nums = book.nums
      request.get('/book/' + book.id).then(res => {
        this.form.bookName = res.data.bookName
        this.form.score = res.data.score
        this.form.nums = res.data.nums
      })
    },
    selectUserCard() {
      const user = this.users.find(v => v.username === this.form.userUsername)
      // this.form.userName = user.name
      // this.form.userPhone = user.phone
      // this.form.account = user.account
      request.get('/user/' + user.id).then(res => {
        this.form.userName = res.data.name
        this.form.userPhone = res.data.phone
        this.form.account = res.data.account
      })
    }
  }
}

</script>

<style>


</style>