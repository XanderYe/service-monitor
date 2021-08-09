<template>
  <mu-container>
    <mu-form ref="form" :model="form" label-position="left" label-width="100">
      <mu-form-item prop="name" label="名称" :rules="nameRules">
        <mu-text-field v-model="form.name"></mu-text-field>
      </mu-form-item>
      <mu-form-item prop="email" label="邮箱" :rules="emailRules">
        <mu-text-field v-model="form.email"></mu-text-field>
      </mu-form-item>
      <mu-form-item prop="phone" label="手机号" :rules="phoneRules">
        <mu-text-field v-model="form.phone"></mu-text-field>
      </mu-form-item>
      <mu-button style="float: left;" color="secondary" @click="save" v-loading="saveLoading">保存</mu-button>
    </mu-form>
  </mu-container>
</template>

<script>
  export default {
    name: "contact-edit",
    data() {
      return {
        saveLoading: false,
        form: {
          id: null,
          name: null,
          email: null,
          phone: null
        },
        nameRules: [
          {validate: (val) => !!val, message: '必须填写名称'},
        ],
        emailRules: [
          {validate: (val) => !!val, message: '必须填写邮箱'},
          {validate: (val) => /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(val), message: '邮箱格式错误'},
        ],
        phoneRules: [
          {validate: (val) => !!val, message: '必须填写手机号'},
          {validate: (val) => /1[0-9]{10}/.test(val), message: '手机号格式错误'},
        ]
      }
    },
    methods: {
      getById() {
        if (this.form.id) {
          this.$requests.get("/contact/getById", {id: this.form.id}).then(res => {
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
            this.$requests.post("/contact/save", this.form).then(res => {
              if (res.data.code === 0) {
                this.$snackbar("保存成功");
                this.$router.push({name: "contact"});
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
