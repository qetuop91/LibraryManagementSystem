<template>
  <div style="width: 70%">
    <h2 style="margin-bottom: 30px">新增分类</h2>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">
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
  name: 'AddCategory',
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
  },
  methods: {
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/category/save', this.form).then(res => {
            // console.log('res: ', res)
            if (res.code === 200) {
              this.$notify.success('新增成功');
              this.$refs['ruleForm'].resetFields();
            } else {
              this.$notify.error(res.msg)
            }
          })
        }
      })

    }
  }
}

</script>

<style>


</style>