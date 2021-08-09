<template>
  <mu-container>
    <div>
      <h2>首页</h2>
    </div>
  </mu-container>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {

      }
    },
    methods: {

    },
    created() {

    },
    activated() {

    },
    watch: {
      '$route': function (to, from) {
        if (to.path === from.path) {

        }
      }
    }
  }
</script>

<style scoped>
  .md-card {
    margin-bottom: 10px;
  }
</style>
