<template>
  <mu-container>
    <mu-data-table stripe :columns="columns" :data="dataList" style="margin-top: 16px;">
      <template slot-scope="scope">
        <td>{{scope.row.id}}</td>
        <td>{{scope.row.user != null ? scope.row.user.nickname : "系统"}}</td>
        <td>{{scope.row.ip}}</td>
        <td>{{scope.row.moduleName}}</td>
        <mu-tooltip :content='scope.row.message'><td style="white-space: nowrap">{{scope.row.message}}</td></mu-tooltip>
        <mu-tooltip :content='scope.row.result ? scope.row.result : "无"'><td style="white-space: nowrap">{{scope.row.result ? scope.row.result : "无"}}</td></mu-tooltip>
        <td>{{scope.row.createTime}}</td>
      </template>
    </mu-data-table>
    <mu-flex justify-content="end" style="margin-top: 16px; padding-bottom: 16px;" v-if="dataList.length > 0">
      <mu-pagination raised circle :page-size="pageInfo.size" :total="pageInfo.total" :current.sync="pageInfo.num"
                     @change="getLogList"></mu-pagination>
    </mu-flex>
  </mu-container>
</template>

<script>
  export default {
    name: "log",
    data() {
      return {
        dataList: [],
        columns: [
          { title: 'ID', width: 100, name: 'id' },
          { title: '昵称', width: 100, name: 'nickname' },
          { title: 'IP', width: 150, name: 'ip' },
          { title: '模块名称', width: 100, name: 'moduleName' },
          { title: '详情', name: 'message' },
          { title: '结果', width: 100, name: 'result' },
          { title: '创建时间', width: 170, name: 'createTime' }
        ],
        pageInfo: {
          num: 1,
          size: 20,
          total: 0,
        }
      }
    },
    methods: {
      getLogList() {
        this.$requests.get("/system/getAccessLogList", {
          pageNum: this.pageInfo.num,
          pageSize: this.pageInfo.size
        }).then(res => {
          if (res.data.code === 0) {
            this.dataList = res.data.data.records;
            this.pageInfo.total = res.data.data.total;
          } else {
            this.$snackbar(res.data.msg);
          }
        })
      }
    },
    created() {
      this.getLogList();
    }
  }
</script>

<style scoped>

</style>
