<template>
  <div id="login-container">
    <div class="login-form">
      <mu-form ref="loginForm" :model="loginData" label-position="top" label-width="100">
        <mu-form-item prop="username" label="用户名" :rules="usernameRules" icon="account_circle">
          <mu-text-field v-model="loginData.username"></mu-text-field>
        </mu-form-item>

        <mu-form-item prop="password" label="密码" :rules="passwordRules" icon="locked">
          <mu-text-field v-model="loginData.password"
                         @keyup.enter.native="login"
                         :action-icon="loginVisibility ? 'visibility_off' : 'visibility'"
                         :action-click="() => (loginVisibility = !loginVisibility)"
                         :type="loginVisibility ? 'text' : 'password'"></mu-text-field>
        </mu-form-item>

        <mu-form-item style="margin-left: 16px;">
          <mu-checkbox v-model="loginData.remember" value="true" label="记住密码"></mu-checkbox>
        </mu-form-item>

        <mu-button style="float: right;" color="secondary" v-loading="loginLoading" data-mu-loading-size="24" @click="login">登录</mu-button>

      </mu-form>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Login",
    data() {
      return {
        // 按钮状态
        loginLoading: false,
        // 登录密码状态
        loginVisibility: false,
        loginData: {
          username: "",
          password: "",
          remember: false
        },
        usernameRules: [
          {validate: (val) => !!val, message: '必须填写用户名'},
        ],
        passwordRules: [
          {validate: (val) => !!val, message: '必须填写密码'},
        ],
      }
    },
    methods: {
      // 登录
      login() {
        this.$refs.loginForm.validate().then((validate) => {
          if (validate) {
            this.loginLoading = true;
            if (this.loginData.remember) {
              localStorage.setItem("username", this.loginData.username);
              localStorage.setItem("password", this.loginData.password);
            } else {
              localStorage.removeItem("username");
              localStorage.removeItem("password");
            }
            let form = new FormData;
            form.append("username", this.loginData.username);
            form.append("password", this.loginData.password);
            this.$requests.post("/user/login", form).then(res => {
              if (res.data.code === 0) {
                this.$snackbar({message: "登录成功"});
                this.user = res.data.data;
                // 存储到vueX
                this.$store.commit("setUser", this.user);
                // 存储token
                localStorage.setItem("token", this.user.token);
                setTimeout(() => {
                  this.$router.push({path: "index"});
                })
              } else {
                this.$snackbar({message: res.data.msg});
              }
              this.loginLoading = false;
              this.loginData.username = null;
              this.loginData.password = null;
            })
          }
        })
      },
    }
  }
</script>

<style lang="less" scoped>
  #login-container {
    width: 100%;
    height: 100%;
    //background: rgb(222, 222, 222);
  }
  .login-form {
    background: white;
    border-radius: 5px;
    height: 350px;
    width: 400px;
    position: absolute;
    top: 50%;
    left: 50%;
    margin-top: -200px;
    margin-left: -175px;
  }
</style>
