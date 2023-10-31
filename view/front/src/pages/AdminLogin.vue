<template>
  <div>
    <h1>管理员登陆</h1>
    <input type="text" v-model="form.jobNumber" name="job-number" placeholder="工号">
    <input type="password" v-model="form.password" name="password" placeholder="密码">
    <button @click.prevent="login">登录</button>
  </div>
</template>

<script>
export default {
  name: "AdminLogin",
  data() {
    return {
      form: {
        jobNumber: '',
        password: ''
      }
    }
  },
  methods: {
    login() {
      this.$http.post('http://localhost:8080/sparrow/admin/login', this.form).then(res => {
        let token = res.data.data
        localStorage.setItem('ADMIN_JWT_TOKEN', token)
        console.log('changed')
        window.location.replace('/admin/index')
      })
    }
  }
}
</script>

<style scoped>

</style>
