<template>
  <mu-container>
    <mu-form ref="form" :model="form" label-position="left" label-width="100">
      <mu-form-item prop="name" label="名称" :rules="nameRules">
        <mu-text-field v-model="form.name"></mu-text-field>
      </mu-form-item>
      <mu-form-item prop="url" label="地址" :rules="urlRules">
        <mu-text-field v-model="form.url"></mu-text-field>
      </mu-form-item>
      <mu-form-item prop="status" label="状态">
        <mu-radio :value="item.value" v-model="form.status"  :label="item.name" :key="'radio' + item.id" v-for="item in status"></mu-radio>
      </mu-form-item>
      <mu-form-item label="邮件联系人">
        <div>
          <div>
            <mu-checkbox label="全选" :input-value="checkAll" @change="handleCheckAll" :checked-icon="checkIcon"></mu-checkbox>
          </div>
          <mu-checkbox :key="'contact' + item.id" v-for="item in contactList"
                       :value="item.id" v-model="form.mailContactList" :label="item.name" @change="checkContact"></mu-checkbox>
        </div>
      </mu-form-item>
      <mu-form-item label="钉钉机器人">
        <div>
          <div v-for="item in dingtalkBotList" style="display: flex">
            <div style="width: 150px; margin: 4px 0">
              <mu-checkbox :key="'bot' + item.id" :value="item.id" v-model="serviceDingtalkList" :label="item.name"
                           style="align-items: center; justify-content: center;" @change="checkBot"></mu-checkbox>
            </div>
            <mu-button small color="secondary" v-show="serviceDingtalkList.indexOf(item.id) > -1" @click="openDingtalkContactDialog(item.id)">机器人联系人</mu-button>
          </div>
        </div>
      </mu-form-item>
      <mu-button style="float: left;" color="secondary" @click="save" v-loading="saveLoading" data-mu-loading-size="24">保存</mu-button>
    </mu-form>

    <mu-dialog title="机器人联系人" width="480" :open.sync="dingtalkContactDialog">
      <div id="bot-dialog">
        <div>
          <div>
            <mu-checkbox label="全选" :input-value="checkBotAll" @change="handleBotCheckAll" :checked-icon="checkBotIcon"></mu-checkbox>
          </div>
          <mu-checkbox :key="'botContact' + item.id" v-for="item in contactList"
                       :value="item.id" v-model="botContactList" :label="item.name" @change="checkBotContact"></mu-checkbox>
        </div>
      </div>
      <mu-button slot="actions" flat @click="saveBotContact">保存</mu-button>
    </mu-dialog>
  </mu-container>
</template>

<script>
  export default {
    name: "service-edit",
    data() {
      return {
        saveLoading: false,
        form: {
          id: null,
          name: null,
          status: 0,
          mailContactList: [],
          serviceDingtalkContactMap: null,
        },
        status: [
          {
            id: 1,
            value: 0,
            name: "启用"
          },
          {
            id: 2,
            value: 1,
            name: "禁用"
          }
        ],
        serviceDingtalkList: [],
        nameRules: [
          {validate: (val) => !!val, message: '必须填写名称'},
        ],
        urlRules: [
          {validate: (val) => !!val, message: '必须填写地址'},
        ],
        dingtalkBotList: [],
        contactList: [],
        checkAll: false,
        checkBotAll: false,
        dingtalkContactDialog: false,
        dingtalkId: null,
        botContactList: []
      }
    },
    computed: {
      checkIcon() {
        if (this.form.mailContactList.length === 0) {
          return undefined;
        }
        return this.form.mailContactList.length < this.contactList.length ? 'indeterminate_check_box' : undefined;
      },
      checkBotIcon() {
        if (this.botContactList.length === 0) {
          return undefined;
        }
        return this.botContactList.length < this.contactList.length ? 'indeterminate_check_box' : undefined;
      }
    },
    methods: {
      getById() {
        if (this.form.id) {
          this.$requests.get("/serviceConfig/getById", {id: this.form.id}).then(res => {
            if (res.data.code === 0) {
              this.form = res.data.data;
              for (let key in this.form.serviceDingtalkContactMap) {
                this.serviceDingtalkList.push(parseInt(key));
              }
              if (this.form.mailContactList.length > 0) {
                this.checkAll = true;
              }
            } else {
              this.$snackbar(res.data.msg);
            }
            console.log("getById");
          })
        }
      },
      save() {
        this.$refs.form.validate().then(validate => {
          if (validate) {
            this.saveLoading = true;
            this.$requests.post("/serviceConfig/save", this.form).then(res => {
              if (res.data.code === 0) {
                this.$snackbar("保存成功");
                this.$router.push({name: "service-config"});
              } else {
                this.$snackbar(res.data.msg);
              }
              this.saveLoading = false;
            })
          }
        })
      },
      getDingtalkBotList(callback) {
        this.$requests.get("/dingtalkBot/getList").then(res => {
          if (res.data.code === 0) {
            this.dingtalkBotList = res.data.data;
          } else {
            this.$snackbar(res.data.msg);
          }
          if (callback) {
            callback();
          }
          console.log("getDingtalkBotList");
        })
      },
      getContactList(callback) {
        this.$requests.get("/contact/getList").then(res => {
          if (res.data.code === 0) {
            this.contactList = res.data.data;
          } else {
            this.$snackbar(res.data.msg);
          }
          if (callback) {
            callback();
          }
          console.log("getContactList");
        })
      },
      axiosAll(callback) {
        let p1 = new Promise((resolve, reject) => {
          this.getDingtalkBotList(() => {
            resolve();
          });
        })
        let p2 = new Promise((resolve, reject) => {
          this.getContactList(() => {
            resolve();
          });
        })
        Promise.all([p1, p2]).then(() => {
          if (callback) {
            callback();
          }
        })
      },
      handleCheckAll() {
        this.form.mailContactList = this.contactList.map(contact => contact.id);
        this.checkAll = true;
      },
      checkContact() {
        this.checkAll = this.form.mailContactList.length !== 0;
      },
      checkBot() {
        let map = this.form.serviceDingtalkContactMap;
        let newMap = {};
        for (let i of this.serviceDingtalkList) {
          newMap[i] = map[i];
        }
        this.form.serviceDingtalkContactMap = newMap;
      },
      openDingtalkContactDialog(id) {
        this.dingtalkId = id;
        this.dingtalkContactDialog = true;
        this.botContactList = this.form.serviceDingtalkContactMap[id] ? this.form.serviceDingtalkContactMap[id] : [];
        this.checkBotContact();
      },
      handleBotCheckAll() {
        this.checkBotAll = true;
        this.botContactList = this.contactList.map(contact => contact.id);
      },
      checkBotContact() {
        this.checkBotAll = this.botContactList.length !== 0;
      },
      saveBotContact() {
        this.form.serviceDingtalkContactMap[this.dingtalkId] = this.botContactList;
        this.dingtalkContactDialog = false;
      },
    },
    created() {
      if (this.$route.query) {
        let id = this.$route.query.id;
        if (id) {
          this.form.id = id;
          this.axiosAll(() => {
            this.getById();
          })
        }
      }
    },
    mounted() {

    }
  }
</script>

<style lang="less" scoped>
  #bot-dialog {
    /deep/ .mu-checkbox {
      margin-right: 16px;
    }
  }
</style>
