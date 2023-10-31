<template>
  <div>
    <div class="clearfix rolling-bar" style="overflow: hidden; padding-top: 10px; position:fixed; width: 100%; z-index: 9999; background-color:white; min-width: 1920px; height: 50px;">
      <!-- rolling bar -->
      <div style="transition: .3s;" :style="{'margin-top': useDefaultNavClass ? 0 : '-51px'}">
        <navbar ></navbar>
        <!-- 上下滚动的事件监听ui -->
        <div style="text-align: center">
          <el-button type="text" :plain="true" :round="true" >每一个不曾起舞的日子，都是对生命的亵渎。——尼采</el-button>
        </div>
      </div>
    </div>
    <div style="height: 60px; width: 100%"/>
    <div class="content">
      <h2>您的反馈对我很重要🍭🍭🍭</h2>
      <el-form ref="form" :model="form" label-width="80px" style="margin-top: 30px;">
        <el-form-item label="反馈标题">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="反馈内容">
          <el-input type="textarea" v-model="form.content"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">发送反馈</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Navbar from "../components/Navbar";
import jwtDecode from "jwt-decode";
export default {
name: "Feedback",
  components: {Navbar},
  data() {
    return {
      useDefaultNavClass: true,
      form: {
        title: '',
        content: '',
        uid: ''
      }
    }
  },
  methods: {
    onSubmit() {
      this.form.uid = parseInt(this.me.uid)
      this.$http.post('http://localhost:8080/sparrow/feedback', this.form).then(res => {
        this.$alert('反馈发送成功', '反馈', {
          type: 'success',
        })
      })
    }
  },
  mounted() {
    if (localStorage.getItem("JWT_TOKEN") !== null) {
      this.me = jwtDecode(localStorage.getItem("JWT_TOKEN"))
      this.isLogging = true
    } else {
      this.me = null
      this.isLogging = false
    }
    window.addEventListener('scroll', e => {
      this.scrollFunc();
      if (this.scrollDirection === 'down' && this.scrollDirection !== this.lastScrollDirection) {
        this.lastScrollDirection = this.scrollDirection
        this.useDefaultNavClass = false
      } else if (this.scrollDirection === 'up' && this.scrollDirection !== this.lastScrollDirection) {
        this.lastScrollDirection = this.scrollDirection
        this.useDefaultNavClass = true
      }
    })
  }
}
</script>

<style scoped>
.clearfix:after {
  visibility: hidden;
  display: block;
  font-size: 0;
  content: " ";
  clear: both;
  height: 0;
}

.rolling-bar {
  box-shadow: 0 7px 5px -5px #d9d9d9;
}
.content {
  width: 800px;
  margin: 100px auto 0;
  border: 1px solid #f4f4f4;
  border-radius: 10px;
  transition: .3s;
  padding: 20px;
}
.content:hover {
  border: 1px solid #999;
}
</style>
