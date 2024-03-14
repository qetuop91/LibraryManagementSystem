<template>
  <div style="width: 70%">
    <h2 style="margin-bottom: 30px">新增用户</h2>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model="form.age" placeholder="请输入年龄"></el-input>
      </el-form-item>
      <el-form-item label="性别">
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
  name: 'AddUser',
  data() {
    const checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('年龄不能为空'));
      }
      if (!/^[0-9]+/.test(value)) {
        callback(new Error('请输入数字值'));
      }
      if (parseInt(value) > 150 || parseInt(value) <= 0) {
        callback(new Error('请输入合理的年龄'))
      }
      callback()
    };
    const checkPhone = (rule, value, callback) => {
      if (!/^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value)) {
        callback(new Error('请输入合法的手机号'))
      }
      callback()
    };
    return {
      form: {
        sex: '武装直升机',
        account: 1000
      },
      rules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
        ],
        age: [
          {validator: checkAge, trigger: 'blur'}
        ],
        phone: [
          {validator: checkPhone, trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/user/save', this.form).then(res => {
            console.log(res)
            if (res.code === 200) {
              this.$notify.success('新增成功')
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