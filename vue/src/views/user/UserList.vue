<template>
  <div>
    <div>
      <!-- 搜索表单 -->
      <el-input style="width: 240px" placeholder="请输入名称" v-model="params.name"></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入联系方式" v-model="params.phone"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i> 搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-refresh"></i> 重置</el-button>
    </div>
    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="编号" width="80"></el-table-column>
      <el-table-column prop="username" label="卡号"></el-table-column>
      <el-table-column prop="account" label="积分余额"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="age" label="年龄"></el-table-column>
      <el-table-column prop="sex" label="性别"></el-table-column>
      <el-table-column prop="phone" label="电话号码"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="borrowBook" label="已借书籍数量"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="updateTime" label="修改时间"></el-table-column>
      <el-table-column label="操作" width="210">
        <template v-slot="scope">
          <!-- scope.row就是当前行数据 -->
          <el-button type="warning" @click="handleAccountAdd(scope.row)">充值</el-button>
          <el-button type="primary" @click="$router.push('/editUser?id=' + scope.row.id)">编辑</el-button>

          <el-popconfirm style="margin-left: 5px" title="确定删除这一数据?" @confirm="del(scope.row.id)">
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
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

    <el-dialog title="充值" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" label-width="100px" ref="ruleForm" :rules="rules" style="width: 85%">
        <el-form-item label="当前账户积分" prop="account">
          <el-input disabled v-model="form.account" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="积分" prop="score">
          <el-input v-model="form.score" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addAccount">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import {getToken} from "@/utils/auth";

export default {
  name: 'UserList',
  data() {
    const checkNums = (rule, value, callback) => {
      value = parseInt(value)
      if(value < 10 || value > 1000) {
        callback(new Error('请输入大于等于10或小于等于1000的整数'))
      }
      callback()
    };
    return {
      tableData: [],
      total: 0,
      params: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        phone: ''
      },
      dialogFormVisible: false,
      form: {},
      rules: {
        score:[
          {require: true, message: '请输入积分', trigger: 'blur'},
          {validator: checkNums, trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      request.get('/user/page', {
        params: this.params
      }).then(res => {
        if (res.code === 200) {
          this.total = Number(res.data.total)
          this.tableData = res.data.records
        }
      })
    },
    reset() {
      this.params = {
        pageNum: 1,
        pageSize: 10,
        name: '',
        phone: ''
      }
      this.load()
    },
    handleCurrentChange(pageNum) {
      // 点击触发分页按钮
      console.log(pageNum)
      this.params.pageNum = pageNum
      this.load()
    },
    del(id) {
      request.delete('/user/delete/' + id).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$notify.success('删除成功')
          this.load()
        }else {
          this.$notify.error(res.msg)
        }
      })
    },
    handleAccountAdd(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    addAccount() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/user/account', this.form).then(res => {
            if (res.code === 200) {
              console.log(res)
              this.$notify.success('充值成功')
              this.dialogFormVisible = false
              this.load()
            }else {
              this.$notify.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>