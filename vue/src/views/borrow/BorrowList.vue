<template>
  <div>
    <div>
      <!-- 搜索表单 -->
      <el-input style="width: 240px" placeholder="请输入书籍名称" v-model="params.bookName"></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入书籍标准码" v-model="params.bookNumber"></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入用户名称" v-model="params.userName"></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入用户卡号" v-model="params.userUsername"></el-input>
      <el-select style="width: 240px; margin-left: 5px"  v-model="params.status" placeholder="请选择状态">
        <el-option
            v-for="item in statusList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i> 搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-refresh"></i> 重置</el-button>
    </div>
    <el-table :data="tableData" stripe row-key="id" default-expand-all>
      <el-table-column prop="id" label="编号" width="80"></el-table-column>
      <el-table-column prop="bookName" label="书籍名称"></el-table-column>
      <el-table-column prop="bookNumber" label="书籍标准码"></el-table-column>
      <el-table-column prop="userId" label="用户id" width="60"></el-table-column>
      <el-table-column prop="userName" label="用户名称"></el-table-column>
      <el-table-column prop="userUsername" label="用户卡号"></el-table-column>
      <el-table-column prop="userPhone" label="联系方式"></el-table-column>
      <el-table-column prop="score" label="书籍积分" width="80"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="140"></el-table-column>
<!--      <el-table-column prop="updateTime" label="修改时间"></el-table-column>-->
      <el-table-column prop="status" label="借出状态" width="80"></el-table-column>
      <el-table-column prop="days" label="借出天数" width="80"></el-table-column>
      <el-table-column prop="remind" label="过期提醒" width="80">
        <template v-slot="scope">
          <el-tag type="success" v-if="scope.row.days <= 14">正常</el-tag>
          <el-tag type="primary" v-if="scope.row.days === 14">即将到期</el-tag>
          <el-tag type="warning" v-if="scope.row.days === 15">已到期</el-tag>
          <el-tag type="danger" v-if="scope.row.days > 15">已过期</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="returnDate" label="归还日期" width="140"></el-table-column>

      <el-table-column label="管理" width="80"  style="text-align: center">
        <template v-slot="scope">
          <!-- 还书 -->
          <el-popconfirm title="确定还书?" @confirm="returnBook(scope.row.id)">
            <el-button type="warning" v-if="scope.row.status === '未归还'" slot="reference">还书</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
      <!-- 操作栏 -->
      <el-table-column label="操作"  style="text-align: center">
        <!-- scope.row就是当前行数据 -->
        <template v-slot="scope">
          <!-- 编辑 -->
<!--          <el-button type="primary" @click="$router.push('/editBorrow?id=' + scope.row.id)">编辑</el-button>-->

          <!-- 删除 -->
          <el-popconfirm title="确定删除这一数据?"style="margin-left: 5px" @confirm="del(scope.row.id)">
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
  name: 'BorrowList',
  data() {
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      tableData: [],
      data: {},
      total: 0,
      statusList: [{value: '已归还', label: '已归还'}, {value: '未归还', label: '未归还'}],
      params: {
        pageNum: 1,
        pageSize: 10,
        bookName: '',
        bookNumber: '',
        userName: '',
        userUsername: ''
      }
    }
  },
  created() {
    checkAdmin()
    this.load()
  },
  methods: {
    load() {
      request.get('/borrow/page', {
        params: this.params
      }).then(res => {
        if (res.code === 200) {
          console.log(res)
          this.total = Number(res.data.total)
          this.tableData = res.data.records
          this.data = res.data
          console.log('this.data: ', this.data)
        }
      })
    },
    reset() {
      this.params = {
        pageNum: 1,
        pageSize: 10,
        bookName: '',
        bookNumber: '',
        userName: '',
        userUsername: ''
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
      request.delete('/borrow/delete/' + id).then(res => {
        if (res.code === 200) {
          this.$notify.success('删除成功')
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    returnBook(id) {
      request.post('/borrow/returnBook/' + id).then(res => {
        console.log(res);
        if (res.code === 200) {
          this.$notify.success('还书成功')
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