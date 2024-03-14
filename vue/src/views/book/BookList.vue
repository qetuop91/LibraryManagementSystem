<template>
  <div>
    <div>
      <!-- 搜索表单 -->
      <el-input style="width: 240px" placeholder="请输入书籍名称" v-model="params.bookName"></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入书籍标准码" v-model="params.bookNumber"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i> 搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-refresh"></i> 重置</el-button>
    </div>
    <el-table :data="tableData" stripe row-key="id" default-expand-all>
      <el-table-column prop="id" label="编号" width="80"></el-table-column>
      <el-table-column prop="bookName" label="书籍名称"></el-table-column>
      <el-table-column prop="description" label="备注" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="publishDate" label="出版日期"></el-table-column>
      <el-table-column prop="author" label="作者"></el-table-column>
      <el-table-column prop="publish" label="出版社"></el-table-column>
      <el-table-column prop="category" label="分类"></el-table-column>
      <el-table-column prop="bookNumber" label="书籍标准码"></el-table-column>
      <el-table-column prop="score" label="书籍积分"></el-table-column>
      <el-table-column prop="nums" label="书籍数量"></el-table-column>
      <el-table-column prop="cover" label="封面">
        <template v-slot="scope">
          <el-image :src="scope.row.cover" :preview-src-list="[scope.row.cover]"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="updateTime" label="修改时间"></el-table-column>
      <!-- 操作栏 -->
      <el-table-column label="操作" width="250px" style="text-align: center">
        <!-- scope.row就是当前行数据 -->
        <template v-slot="scope">
          <!-- 编辑 -->
          <el-button type="primary" @click="$router.push('/editBook?id=' + scope.row.id)">编辑</el-button>
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
  </div>
</template>

<script>
import request from "@/utils/request";
import Cookies from "js-cookie";
import {checkAdmin} from "@/utils/check";

export default {
  name: 'BookList',
  data() {
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      tableData: [],
      total: 0,
      params: {
        pageNum: 1,
        pageSize: 10,
        bookName: '',
        bookNumber: ''
      }
    }
  },
  created() {
    checkAdmin()
    this.load()
  },
  methods: {
    load() {
      request.get('/book/page', {
        params: this.params
      }).then(res => {
        if (res.code === 200) {
          console.log(res)
          this.total = Number(res.data.total)
          this.tableData = res.data.records
        }
      })
    },
    reset() {
      this.params = {
        pageNum: 1,
        pageSize: 10,
        bookName: '',
        bookNumber: ''
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
      request.delete('/book/delete/' + id).then(res => {
        if (res.code === 200) {
          this.$notify.success('删除成功')
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    stateFormat(row, column, cellValue) {
      if (!cellValue) return ''
      if (cellValue.length > 10) {       //最长固定显示10个字符
        return cellValue.slice(0, 10) + '...'
      }
      return cellValue
    }
  }
}
</script>

<style scoped>

</style>