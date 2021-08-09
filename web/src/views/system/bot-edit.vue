<template>
  <mu-container>
    <mu-form ref="form" :model="form" label-position="left" label-width="100">
      <mu-form-item prop="name" label="名称" :rules="nameRules">
        <mu-text-field v-model="form.name"></mu-text-field>
      </mu-form-item>
      <mu-form-item prop="token" label="TOKEN" :rules="tokenRules">
        <mu-text-field v-model="form.token"></mu-text-field>
      </mu-form-item>
      <mu-form-item prop="secret" label="SECRET" :rules="secretRules">
        <mu-text-field v-model="form.secret"></mu-text-field>
      </mu-form-item>
      <mu-button style="float: left;" color="secondary" @click="save" v-loading="saveLoading">保存</mu-button>
    </mu-form>
  </mu-container>
</template>

<script>
  export default {
    name: "bot-edit",
    data() {
      return {
        saveLoading: false,
        form: {
          id: null,
          name: null,
          token: null,
          secret: null
        },
        nameRules: [
          {validate: (val) => !!val, message: '必须填写名称'},
        ],
        tokenRules: [
          {validate: (val) => !!val, message: '必须填写TOKEN'},
        ],
        secretRules: [
          {validate: (val) => !!val, message: '必须填写SECRET'},
        ]
      }
    },
    methods: {
      getById() {
        if (this.form.id) {
          this.$requests.get("/dingtalkBot/getById", {id: this.form.id}).then(res => {
            if (res.data.code === 0) {
              this.form = res.data.data;
            } else {
              this.$snackbar(res.data.msg);
            }
          })
        }
      },
      save() {
        this.$refs.form.validate().then(validate => {
          if (validate) {
            this.saveLoading = true;
            this.$requests.post("/dingtalkBot/save", this.form).then(res => {
              if (res.data.code === 0) {
                this.$snackbar("保存成功");
                this.$router.push({name: "dingtalk-bot"});
              } else {
                this.$snackbar(res.data.msg);
              }
              this.saveLoading = false;
            })
          }
        })
      },
    },
    created() {

    },
    mounted() {
      if (this.$route.query) {
        let id = this.$route.query.id;
        if (id) {
          this.form.id = id;
          this.getById();
        }
      }
    }
  }
</script>

<style scoped>

</style>
