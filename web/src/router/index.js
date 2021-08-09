import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/views/Main.vue';
import Login from '@/views/Login.vue';

Vue.use(Router);

export const router = new Router({
  routes: [
    {
      path: '/',
      name: 'main',
      redirect: '/index',
      component: Main,
      menuHide: true,
      children: [
        {
          path: 'index',
          title: '首页',
          name: 'index',
          component: () => import('@/views/index.vue'),
        },
      ]
    },
    {
      path: '/login',
      title: '登录',
      name: 'login',
      menuHide: true,
      component: Login,
    },
    {
      path: '/service',
      title: '服务管理',
      name: 'service',
      component: Main,
      icon: 'cloud',
      children: [
        {
          path: 'service-config',
          name: 'service-config',
          title: '监控地址配置',
          component: () => import('@/views/service/config.vue'),
        },
        {
          path: 'contact',
          name: 'contact',
          title: '联系人管理',
          component: () => import('@/views/service/contact.vue'),
        },
      ]
    },
    {
      path: '/system',
      title: '系统管理',
      name: 'system',
      component: Main,
      icon: 'settings',
      children: [
        {
          path: 'dingtalk-bot',
          name: 'dingtalk-bot',
          title: '钉钉机器人管理',
          component: () => import('@/views/system/dingtalk-bot.vue'),
        },
        {
          path: 'log',
          name: 'log',
          title: '日志管理',
          component: () => import('@/views/system/log.vue'),
        },
      ]
    },
    {
      path: '/',
      name: 'main',
      menuHide: true,
      component: Main,
      children: [
        {
          path: 'config-edit',
          name: 'config-edit',
          title: '编辑监控配置',
          component: () => import('@/views/service/config-edit.vue'),
        },
        {
          path: 'contact-edit',
          name: 'contact-edit',
          title: '编辑联系人',
          component: () => import('@/views/service/contact-edit.vue'),
        },
        {
          path: 'bot-edit',
          name: 'bot-edit',
          title: '编辑机器人',
          component: () => import('@/views/system/bot-edit.vue'),
        },
      ]
    },
  ]
});
