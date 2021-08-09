<template>
  <mu-container>
    <mu-flex>
      <mu-button style="float: left;" color="secondary" @click="addPage">添加</mu-button>
    </mu-flex>
    <mu-data-table :columns="columns" :data="dataList" style="margin-top: 16px">
      <template slot-scope="scope">
        <td>{{scope.row.name}}</td>
        <td>{{scope.row.url}}</td>
        <td>{{scope.row.creator}}</td>
        <td>{{scope.row.createTime}}</td>
        <td>
          <mu-button style="float: left;" small color="secondary" @click="editPage(scope.row.id)">编辑</mu-button>
          <mu-button style="float: left;margin-left: 8px;" small color="red" @click="confirmDelete(scope.row.id)">删除</mu-button>
        </td>
      </template>
    </mu-data-table>
    <mu-dialog title="警告" width="600" max-width="80%" :esc-press-close="false" :overlay-close="false" :open.sync="deleteDialog">
      是否确认删除数据？
      <mu-button slot="actions" flat color="primary" @click="deleteDialog = false">取消</mu-button>
      <mu-button slot="actions" flat color="primary" @click="deleteData">确认</mu-button>
    </mu-dialog>
  </mu-container>
</template>

<script>
  export default {
    name: "contact",
    data() {
      return {
        columns: [
          {title: "名称", width: 150, name: "name"},
          {title: "地址", name: "url"},
          {title: "创建人", width: 150, name: "creator"},
          {title: "创建时间", width: 200, name: "createTime"},
          {title: "操作", name: "operate"},
        ],
        dataList: [],
        deleteDialog: false,
        deleteId: null,
      }
    },
    methods: {
      addPage() {
        this.$router.push({path: "/config-edit"});
      },
      editPage(id) {
        this.$router.push({path: "/config-edit", query: {id: id}});
      },
      getDataList() {
        this.$requests.get("/serviceConfig/getList").then(res => {
          if (res.data.code === 0) {
            this.dataList = res.data.data;
          } else {
            this.$snackbar(res.data.msg);
          }
        })
      },
      confirmDelete(id) {
        this.deleteId = id;
        this.deleteDialog = true;
      },
      deleteData() {
        let formData = new FormData;
        formData.append("id", this.deleteId);
        this.$requests.post("/serviceConfig/delete", formData).then(res => {
          if (res.data.code === 0) {
            this.$snackbar("删除成功");
            this.deleteDialog = false;
            this.getDataList();
          } else {
            this.$snackbar(res.data.msg);
          }
        })
      }
    },
    created() {
      this.getDataList();
    }
  }
</script>

<style scoped>

</style>
