<template>
  <mu-snackbar :position="position" :open.sync="open">
    {{message}}
    <mu-button flat slot="action" color="secondary" @click="open = false">关闭</mu-button>
  </mu-snackbar>
</template>

<script>
  export default {
    name: "Snackbar",
    data() {
      return {
        message: "",
        position: "top",
        open: false,
        time: 3000,
      }
    },
    methods: {
      close() {
        setTimeout(() => {
          if (this.open) {
            this.open = false;
          }
        }, this.time);
      }
    },
    mounted() {
      this.close();
    },
  }
</script>

<style scoped>

</style>
