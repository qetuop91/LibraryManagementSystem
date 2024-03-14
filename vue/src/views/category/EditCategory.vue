<template>
  <div style="width: 70%">
    <h2 style="margin-bottom: 30px">编辑分类</h2>
    <el-form :inline="true" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="分类名" prop="categoryName">
        <el-input v-model="form.categoryName" placeholder="请输入分类名"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" placeholder="请输入备注"></el-input>
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
  name: 'EditCategory',
  data() {
    return {
      form: {},
      rules: {
        categoryName: [
          {required: true, message: '请输入分类名', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    checkAdmin()
    const id = this.$route.query.id
    request.get("/category/" + id).then(res => {
      this.form = res.data
    })
  },
  methods: {
    save() {
      request.put('/category/update', this.form).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$notify.success('更新成功')
          this.$router.push('/categoryList')
        } else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}

</script>

<style>


</style>