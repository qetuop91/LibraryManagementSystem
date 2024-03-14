<template>
  <div>
    <div>
      <!-- 搜索表单 -->
      <el-input style="width: 240px" placeholder="请输入分类名称" v-model="params.categoryName"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i> 搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-refresh"></i> 重置</el-button>
    </div>
    <el-table :data="tableData" stripe row-key="id" default-expand-all>
      <el-table-column prop="id" label="编号" width="80"></el-table-column>
      <el-table-column prop="categoryName" label="分类名"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="updateTime" label="修改时间"></el-table-column>

      <!-- 操作栏 -->
      <el-table-column label="操作" width="250px" style="text-align: center">
        <!-- scope.row就是当前行数据 -->
        <template v-slot="scope">
          <!-- 编辑 -->
          <el-button type="success" v-if="!scope.row.pid" @click="handleAdd(scope.row)">添加子分类</el-button>
          <el-button type="primary" @click="$router.push('/editCategory?id=' + scope.row.id)">编辑</el-button>
          <!-- 删除 -->
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

    <el-dialog title="添加子分类" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" label-width="100px" ref="ruleForm" :rules="rules" style="width: 85%">
        <el-form-item label="分类名" prop="categoryName">
          <el-input v-model="form.categoryName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import Cookies from "js-cookie";
import {checkAdmin} from "@/utils/check";

export default {
  name: 'CategoryList',
  data() {
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      tableData: [],
      total: 0,
      dialogFormVisible: false,
      form: {},
      pid: null,
      params: {
        pageNum: 1,
        pageSize: 10,
        categoryName: ''
      },
      rules: {
        categoryName: [
          {required: true, message: '请输入分类名', trigger: 'blur'}
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
      request.get('/category/page', {
        params: this.params
      }).then(res => {
        if (res.code === 200) {
          console.log(res)
          this.total = Number(res.data.total)
          this.tableData = res.data.list
        }
      })
    },
    reset() {
      this.params = {
        pageNum: 1,
        pageSize: 10,
        categoryName: ''
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
      request.delete('/category/delete/' + id).then(res => {
        if (res.code === 200) {
          this.$notify.success('删除成功')
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    handleAdd(row) {
      //将当前行的id作为二级分类的pid
      this.pid = row.id
      this.dialogFormVisible = true
    },
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          this.form.pid = this.pid
          request.post('/category/save', this.form).then(res => {
            if (res.code === 200) {
              this.$notify.success('新增子分类成功');
              this.$refs['ruleForm'].resetFields();
              this.dialogFormVisible = false;
              this.load()
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

<style scoped>

</style>