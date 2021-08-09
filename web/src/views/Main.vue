<template>
  <div style="height: 100%">
    <mu-drawer :open.sync="open" :docked="docked" :z-depth="1">

      <mu-list toggle-nested>

        <mu-list-item button :to="{name:'index'}" @click="changeNavName('首页')">
          <mu-list-item-action>
            <mu-icon value="home"></mu-icon>
          </mu-list-item-action>
          <mu-list-item-title>首页</mu-list-item-title>
        </mu-list-item>

        <mu-list-item button nested :open="openItem === menu.title"
                      @toggle-nested="openItem = arguments[0] ? menu.title : ''" v-for="menu in menuList" :key="menu.path">
          <mu-list-item-action>
            <mu-icon :value="menu.icon"></mu-icon>
          </mu-list-item-action>
          <mu-list-item-title>{{menu.title}}</mu-list-item-title>
          <mu-list-item-action>
            <mu-icon class="toggle-icon" size="24" value="keyboard_arrow_down"></mu-icon>
          </mu-list-item-action>

          <mu-list-item :class="isActived(item.title)" button slot="nested" v-for="item in menu.children" :key="item.path"
                        :to="{name: item.name}" @click="changeNavName(item.title)">
            <mu-list-item-title>{{item.title}}</mu-list-item-title>
          </mu-list-item>
        </mu-list-item>

      </mu-list>

    </mu-drawer>

    <mu-appbar :class="['mu-appbar-header', isOpen]" color="primary">
      <mu-button icon slot="left" @click="toggleNav" v-if="!desktop">
        <mu-icon value="menu"></mu-icon>
      </mu-button>
      {{appBarName}}

      <div slot="right" class="avatar-button" style="margin-right: 5px;">
        <mu-button flat ref="avatarButton" @click="userMenu = !userMenu">
          <img :src="user.avatar" :onerror="errorImg">
        </mu-button>
        <mu-popover cover placement="left-start" :open.sync="userMenu" :trigger="userMenuTrigger">
          <div class="popover-div">
            <div class="info">
              <div class="username">{{user.nickname}}</div>
              <mu-button color="secondary" style="margin: 16px 0 0 18px" @click="toUserInfoPage">个人中心</mu-button>
            </div>
            <div class="bottom">
              <mu-button style="float: right" @click="logout">退出</mu-button>
            </div>
          </div>
        </mu-popover>
      </div>
    </mu-appbar>

    <div :class="['mu-container', isOpen]">

      <keep-alive :include="['index']">
        <router-view></router-view>
      </keep-alive>
    </div>
  </div>

</template>

<script>
  import Util from "@/libs/vueUtil";

  export default {
    components: {
    },
    data() {
      const desktop = this.isDesktop();
      return {
        user: {
          id: 0,
          nickname: "",
          username: "",
          token: "",
        },
        errorImg: "javascript:this.src='/static/img/defaultavatar.jpg';",
        open: desktop,
        docked: desktop,
        desktop: desktop,
        isOpen: desktop ? "is-open" : "",
        openItem: "",
        // 导航栏名称
        appBarName: "首页",
        menuList: [],
        // 用户菜单
        userMenu: false,
        // 菜单弹出绑定元素
        userMenuTrigger: null,
      }
    },
    computed: {
      isActived() {
        return (name) => {
          if (name == this.appBarName) {
            return ['actived'];
          } else {
            return [];
          }
        }
      }
    },
    methods: {

      toggleNav() {
        this.open = !this.open
      },
      changeNav() {
        const desktop = this.isDesktop();
        this.docked = desktop;
        if (desktop === this.desktop) {
          return;
        }
        if (!desktop && this.desktop && this.open) {
          this.open = false;
          this.isOpen = "";
        }
        if (desktop && !this.desktop && !this.open) {
          this.open = true;
          this.isOpen = "is-open";
        }
        this.desktop = desktop
      },

      // 改变菜单栏名称
      changeNavName(name) {
        this.appBarName = name;
        sessionStorage.setItem("openItem", this.openItem);
        sessionStorage.setItem("appBarName", this.appBarName);
      },

      // 是否是桌面端
      isDesktop() {
        const isDesktop = window.innerWidth > 993;
        this.$store.commit("setIsDesktop", isDesktop);
        return isDesktop;
      },

      // 注销
      logout() {
        this.$requests.get("/user/logout").then(res => {
          if (res.data.code === 0) {
            this.userMenu = false;
            this.user = {
              id: 0,
              username: "",
              nickname: "",
            };
            // 从vueX移除
            this.$store.commit("removeUser");
            // 移除token
            localStorage.removeItem("token");
            this.userMenuTrigger = null;
            this.$snackbar({message: "注销成功"});
          } else {
            this.$snackbar(res.data.msg);
          }
        })
      },
      getUserInfo() {
        this.$requests.get("/user/info").then(res => {
          if (res.data.code === 0) {
            // 判断登录状态
            const user = res.data.data;
            this.user = user;
            localStorage.setItem("role", this.user.role);
            this.$store.commit("setUser", user);
            // 重新绑定用户弹窗
            this.bindUserMenu();
          }
        })
      },
      bindUserMenu() {
        this.$nextTick(() => {
          //绑定菜单弹出元素
          this.userMenuTrigger = this.$refs.avatarButton.$el;
        })
      },
      toUserInfoPage() {
        this.$snackbar("暂未实现");
      }
    },
    created() {
      this.menuList = this.$router.options.routes.filter(route => !route.menuHide);
      this.changeNav();

      // 设置当前页面名称
      this.openItem = sessionStorage.getItem("openItem") ? sessionStorage.getItem("openItem") : "";
      this.appBarName = sessionStorage.getItem("appBarName") ? sessionStorage.getItem("appBarName") : "首页";
    },
    mounted() {
      this.user.role = parseInt(localStorage.getItem("role"));
      this.changeNav();
      this.getUserInfo();
      this.handleResize = () => {
        this.changeNav();
        // 重新绑定用户弹窗
        this.bindUserMenu();
      };
      // 拖动窗口事件
      window.addEventListener('resize', this.handleResize);
    },
    watch: {
      '$route': function (to, from) {
        // 移动端点击后自动关闭抽屉
        if (!this.isDesktop()) {
          if (this.open) {
            this.open = false;
          }
        }
      }
    }
  }
</script>

<style lang="less" scoped>

</style>
