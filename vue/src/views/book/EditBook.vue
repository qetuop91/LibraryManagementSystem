<template>
  <div style="width: 70%">
    <h2 style="margin-bottom: 30px">编辑书籍信息</h2>
    <el-form :inline="true" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="书籍名称" prop="bookName">
        <el-input v-model="form.bookName" placeholder="请输入书籍名称"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" style="width: 500px" v-model="form.description" placeholder="请输入描述"></el-input>
      </el-form-item>
      <el-form-item label="出版日期" prop="publishDate">
        <el-date-picker
            v-model="form.publishDate"
            type="date"
            placeholder="选择出版日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input v-model="form.author" placeholder="请输入作者"></el-input>
      </el-form-item>
      <el-form-item label="出版社" prop="publish">
        <el-input v-model="form.publish" placeholder="请输入出版社"></el-input>
      </el-form-item>
      <el-form-item label="分类" prop="categories">
        <el-cascader
            style="width: 220px"
            :props="{value:'categoryName', label:'categoryName'}"
            v-model="form.categories"
            :options="categories"></el-cascader>
      </el-form-item>
      <el-form-item label="书籍标准码" prop="bookNumber">
        <el-input v-model="form.bookNumber" placeholder="请输入书籍标准码"></el-input>
      </el-form-item>
      <el-form-item label="书籍积分" prop="score">
        <el-input-number v-model="form.score" :min="10" :max="30" label="所需积分"></el-input-number>
      </el-form-item>
      <el-form-item label="书籍数量" prop="nums">
        <el-input-number v-model="form.nums" :min="0" label="请输入书籍数量"></el-input-number>
      </el-form-item>
      <br>
      <el-form-item label="封面" prop="cover">
        <!-- 这里的action根据tomcat和本机部署进行切换 -->
<!--            :action="'http://localhost:8080/api/book/upload?token=' + getAdminToken"-->
        <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :action="'http://10.23.127.99:8080/LibraryManagement/api/book/upload?token=' + getAdminToken"
            :auto-upload="true"
            :on-success="handleCoverSuccess"
            :headers="headerObj"
        >
          <img v-if="form.cover" :src="form.cover" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>

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
import {getToken} from "@/utils/auth";

export default {
  name: 'EditBook',
  data() {
    return {
      headerObj:{
        Authorization: getToken()
      },
      getAdminToken: getToken(),
      form: {
        score: 10,
        nums: 0
      },
      categories: [],
      rules: {
        bookName: [
          {required: true, message: '请输入书籍名称', trigger: 'blur'}
        ],
        // description: [
        //   {required: true, message: '请输入书籍描述', trigger: 'blur'}
        // ],
        // publishDate: [
        //   {required: true, message: '请选择书籍出版日期', trigger: 'blur'}
        // ],
        // author: [
        //   {required: true, message: '请输入书籍作者', trigger: 'blur'}
        // ],
        // publish: [
        //   {required: true, message: '请输入书籍出版社', trigger: 'blur'}
        // ],
        // categories: [
        //   {required: true, message: '请选择书籍分类', trigger: 'blur'}
        // ],
        bookNumber: [
          {required: true, message: '请输入书籍标准码', trigger: 'blur'}
        ],
        // cover: [
        //   {required: true, message: '请输入书籍封面', trigger: 'blur'}
        // ],
        score: [
          {required: true, message: '请输入书籍积分', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    checkAdmin()
    const id = this.$route.query.id
    request.get("/book/" + id).then(res => {
      console.log(res)
      this.form = res.data
      if (this.form.category) {
        this.form.categories = this.form.category.split(' > ')
        console.log(this.form.categories)
      }
    })
    request.get('/book/tree').then(res => {
      console.log(res)
      this.categories = res.data
    })
  },
  methods: {
    save() {
      request.put('/book/update', this.form).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$notify.success('更新成功')
          this.$router.push('/bookList')
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    handleCoverSuccess(res) {
      console.log('handleSuccess: ', res)
      let imgUrl = ''
      if (res.code === 200) {
        imgUrl = res.data + '?token=' + getToken()
        console.log(imgUrl)
        this.$set(this.form, 'cover', imgUrl)
        // this.form.cover = imgUrl
      }
    }
  }
}

</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>