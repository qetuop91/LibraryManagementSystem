<template>
  <div>
    <div style="margin: 20px 0">
      <el-select class="input" v-model="timeRange" placeholder="请选择" @change="load">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
    </div>
    <el-card>
      <div id="line" style="width: 100%; height: 400px"></div>
    </el-card>
  </div>

</template>

<script>
import Cookies from "js-cookie";
import {checkAdmin} from "@/utils/check";
import * as echarts from 'echarts';
import request from "@/utils/request";


const option = {
  title: {
    text: '图书借还统计'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['借书数量', '还书数量']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  toolbox: {
    feature: {
      saveAsImage: {}
    }
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '借书数量',
      type: 'line',
      stack: 'Total',
      smooth: true,
      data: []
    },
    {
      name: '还书数量',
      type: 'line',
      stack: 'Total',
      smooth: true,
      data: []
    }
  ]
};

export default {
  data() {
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      lineBox: null,
      timeRange: 'week',
      options: [
        {label: '最近一周', value: 'week'},
        {label: '最近一个月', value: 'month'},
        {label: '最近两个月', value: 'month2'},
        {label: '最近三个月', value: 'month3'},
      ]
    }
  },
  mounted() {
    this.load()
  },
  created() {
    checkAdmin()
  },
  methods: {
    load() {
      if (!this.lineBox) {
        this.lineBox = echarts.init(document.getElementById('line'))
      }
      request.get('/borrow/lineCharts/' + this.timeRange).then(res => {
        console.log(res)
        //设置x轴月份
        option.xAxis.data = res.data.date
        //设置借书数量
        option.series[0].data = res.data.borrowCount
        //设置还书数量
        option.series[1].data = res.data.returnCount
        //重新设置容器的属性值，当数据发生变化的时候一定要重新setOption
        this.lineBox.setOption(option)
      })
    }
  }
}
</script>

<style>
.item {
  margin: 10px 0;
  color: #666;
}
</style>
