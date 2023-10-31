<template>
  <div class="content-wrapper">
<!--    <div>-->
<!--      <img class="website-logo" src="../../static/img/uugai.com-1608446530929.png" alt="">-->
<!--    </div>-->
<!--    <form action="">-->
<!--      <input v-model="form.account" type="text" name="account" placeholder="手机/邮箱">-->
<!--      <br>-->
<!--      <input v-model="form.password" type="password" name="password" placeholder="密码">-->
<!--      <br>-->
<!--      <button id="login-btn" @click.prevent="login">登录</button>-->
<!--    </form>-->
    <div class="content">
      <div class="left-part">
        <div class="logo-part">
          <img src="../../static/img/uugai.com-1608446530929.png" alt="图标">
        </div>
        <div class="download-part">
          <el-dropdown class="dropdown-app" placement="bottom">
            <span class="download-app">下载麻雀APP</span>
            <el-dropdown-menu slot="dropdown" style="padding: 10px;">
              <img src="https://sf6-scmcdn2-tos.pstatp.com/xitu_juejin_web/img/app-install.6226a3b.png" alt="logo" style="height: 100px; width: 100px;">
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
      <div class="right-part">
        <div class="type-choice">
          <span @click="changePage(true)" :class="isLoggingPage ? ['type', 'type-now'] : ['type']">登录</span>
          ·
          <span @click="changePage(false)" :class="!isLoggingPage ? ['type', 'type-now'] : ['type']">注册</span>
        </div>
        <div class="input-wrapper">
          <div class="login-part" v-if="isLoggingPage">
            <div class="login-phone">
              <font-awesome-icon icon="mobile-alt" class="input-icon" style=""/>
              <input v-model="loginForm.account" type="text" placeholder="手机号">
            </div>
            <div class="password-part">
              <font-awesome-icon icon="lock" class="input-icon" style=""/>
              <input v-model="loginForm.password" type="password" placeholder="密码" >
            </div>
          </div>
          <div class="reg-part" v-else>
            <div class="reg-nickname">
              <font-awesome-icon icon="user" class="input-icon" />
              <input v-model="regForm.nickname" type="text" placeholder="昵称" @blur="validateNickname">
            </div>
            <div class="reg-phone">
              <font-awesome-icon icon="mobile-alt" class="input-icon" style=""/>
              <input v-model="regForm.account" type="text" placeholder="手机号" @blur="validateAccount">
            </div>
            <div class="reg-password">
              <font-awesome-icon icon="lock" class="input-icon" style=""/>
              <input v-model="regForm.password" type="password" placeholder="密码" @blur="validatePassword">
            </div>
          </div>
        </div>

        <div class="other-options" v-if="isLoggingPage">
          <div class="login-options">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
            <a href="">登录遇到问题？</a>
          </div>
        </div>

        <div class="main-actions">
          <div class="login-btn" v-if="isLoggingPage">
            <span @click="login" v-text="isLogging ? '登 录 中 ...' : '登 录'"></span>
          </div>
          <div class="reg-btn" v-else>
            <span @click="registerUser" v-text="isRegistering ? '注 册 中 ...' : '注 册'"></span>
          </div>
        </div>

        <div class="other-infos">
          <div class="login-other-info" v-if="isLoggingPage">
            <span>登录即代表同意 <a href=""> 用户协议 </a> </span>
          </div>
          <div class="reg-other-info" v-else>
            <p>点击注册表示您同意并且愿意遵守麻雀阅读 </p>
            <p>
              <a href="">用户协议</a>
              和
              <a href="">隐私政策</a>
            </p>
          </div>
        </div>

        <div class="other-way-part">
          <div class="other-way-title" v-if="isLoggingPage">
<!--            登陆的时候显示 -- 社交帐号登录 -->
            <span>————&nbsp;&nbsp;&nbsp;社交帐号登录&nbsp;&nbsp;&nbsp;————</span>
          </div>
          <div class="other-way-title" v-else>
            <span>————&nbsp;&nbsp;&nbsp;社交帐号直接注册&nbsp;&nbsp;&nbsp;————</span>
          </div>
          <div class="other-way-action" v-if="isLoggingPage">
            <a href="">
              <img src="https://img.icons8.com/color/2x/weibo.png" alt="">
            </a>
            <a href="">
              <img src="https://img.icons8.com/color/2x/weixing.png" alt="">
            </a>
            <a href="">
              <img src="https://img.icons8.com/color/2x/qq.png" alt="">
            </a>
          </div>
          <div class="other-way-action" v-else>
            <a href="">
              <img src="https://img.icons8.com/color/2x/weibo.png" alt="">
            </a>
            <a href="">
              <img src="https://img.icons8.com/color/2x/weixing.png" alt="">
            </a>
            <a href="">
              <img src="https://img.icons8.com/color/2x/qq.png" alt="">
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import jwtDecode from "jwt-decode";

export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        account: '',
        password: '',
      },
      regForm: {
        nickname: '',
        account: '',
        password: '',
      },
      isLogging: false,
      isRegistering: false,
      rememberMe: true,
      isLoggingPage: true,

      // error 提示
      errorInNickname: true,
      errorInAccount: true,
      errorInPassword: true,
    }
  },
  methods: {
    validateNickname() {
      if (this.regForm.nickname.length < 3 || this.regForm.nickname.length > 10) {
        this.errorInNickname = true
        this.$message.error('昵称长度非法')
      } else {
        this.errorInNickname = false
      }
    },
    validateAccount() {
      if (this.regForm.account.length < 11) {
        this.errorInAccount = true
        this.$message.error('手机号长度非法')
      } else {
        this.errorInAccount = false
      }
    },
    validatePassword() {
      if (this.regForm.password.length < 6) {
        this.errorInPassword = true
        this.$message.error('密码过短')
      } else {
        this.errorInPassword = false
      }
    },
    registerUser() {
      if (this.errorInAccount || this.errorInNickname || this.errorInPassword) {
        this.$message.error('检查参数合法性')
        return
      }
      this.isRegistering = true
      this.$http.post('http://localhost:8080/sparrow/user/register', this.regForm).then(res => {
        this.isRegistering = false
        this.$message({
          message: '注册成功',
          type: 'success',
          showClose: true
        })
        window.location.href = '#login'
        this.isLoggingPage = true
      }).catch(e => {
        this.isRegistering = false
        if (e.response.status === 401) {
          this.$message.error('账号已经注册')
        } else if (e.response.status === 404 || e.response.status === 403) {
          this.$message.error('参数非法')
        } else if (e.response.status === 402) {
          this.$message.error('未知错误，请联系管理员')
        }
      })
    },
    login() {
      if (this.loginForm.account === '' || this.loginForm.password === '') {
        this.$message.error('参数不能为空')
        return
      }
      this.isLogging = true
      this.$http.post('http://localhost:8080/sparrow/user/login-account', this.loginForm).then(res => {
        // 路由中已经拦截过，再拦截一次
        this.isLogging = false
        if (res.data.status) {
          let token = res.data.data
          localStorage.setItem('JWT_TOKEN', token)
          if (window.parent.opener !== null) window.parent.opener.location.reload();
          window.close();
          // window.close()
          // console.log(jwtDecode(token));
        }
      }).catch(e => {
        this.isLogging = false
        if (e.response.status === 401) {
          this.$message.error('账号已经注册')
        } else if (e.response.status === 404 || e.response.status === 403) {
          this.$message.error('密码错误')
        } else if (e.response.status === 402) {
          this.$message.error('账号未知')
        }
      })
    },
    changePage(toLogin) {
      if (toLogin) {
        window.location.href = '#login'
        this.isLoggingPage = true
      } else {
        window.location.href = '#register'
        this.isLoggingPage = false
      }
    }
  },
  mounted() {
    let hash = this.$route.hash
    if (hash === '#login') this.isLoggingPage = true
    else this.isLoggingPage = false
  }
}
</script>

<style scoped>
.content-wrapper {
  height: 100vh;
  background-color: #eeeeee;
}
.website-logo {
  position: absolute;
  top: -50px;
  left: -50px;
  width: 300px;
  height: 300px;
}
.content {
  position: absolute;
  top: -50px;
  right: 0;
  bottom: 0;
  left: 0;
  margin: auto auto auto auto;
  width: 800px;
  height: 560px;
  /*background-color: skyblue;*/
  display: flex;
}
.left-part,
.right-part {
  width: 400px;
  height: 560px;
  /*background-color: red;*/
}
.left-part {
  /*background-color: yellow;*/
  display: flex;
  flex-direction: column;
}
.left-part .logo-part {

}
.left-part .logo-part img {
  width: 400px;
  height: 400px;
}
@keyframes lovely-bird {
  from {
    transform: scaleX(1) scaleY(1);
  }
  33% {
    transform: scaleX(0.8) scaleY(1.2);
  }
  66% {
    transform: scaleX(1.2) scaleY(0.8);
  }
  to {
    transform: scaleX(1) scaleY(1);
  }
}
.left-part .logo-part img:active {
  animation-name: lovely-bird;
  animation-duration: .5s;
  animation-fill-mode: backwards;
  animation-iteration-count: revert;
}
.left-part .download-part {
  display: flex;
  justify-content: center;
}
.download-app {
  /*background-color: red;*/
  line-height: 45px;
  text-align: center;
  height: 45px;
  width: 245px;
  font-size: 18px;
  background-color: #6666ff;
  color: white;
  border: 1px solid #4739d4;
  border-radius: 40px;
  display: block;
  cursor: pointer;
}
.right-part {
  background-color: #f9f9f9;
  border-radius: 3px;
  transition: .3s;
  border: 1px solid #ddd;
}
.right-part:hover {
  box-shadow: 0 0 5px -2px #d9d9d9;
  border: 1px solid #d9d9d9;;
}

.download-app {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.type-choice {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 140px;
  width: 100%;
  line-height: 40px;
  text-align: center;
}

.type {
  font-size: 20px;
  margin: 0 20px;
  cursor: pointer;
}

.type-now {
  font-weight: 700;
  color: #5385ff;
  /*TODO: 不变更上下位置*/
  border-bottom: 5px solid #5385ff;
}
.input-wrapper {
  padding: 0 50px;
}
.input-wrapper input {
  outline-style: none;
  display: block;
  width: 100%;
  height: 48px;
  line-height: 48px;
  font-size: 14px;
  padding-left: 40px;
  color: #333;
  border: 1px solid #ccc;
}
.password-part,
.reg-phone,
.reg-password {
  margin-top: -1px;
}
.input-icon {
  margin-top: 16px;
  font-size: 20px;
  position: absolute;
  margin-left: 12px;
  color: #aaa;
}

.login-phone,
.password-part,
.reg-nickname,
.reg-phone,
.reg-password {
  display: flex;
}

.login-phone input,
.reg-nickname input {
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
}
.password-part input,
.reg-password input {
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
}


.other-options,
.main-actions,
.other-infos {
  margin: 20px 0;
  padding: 0 50px;
}
.main-actions .login-btn {
  display: flex;
  justify-content: center;
}
.main-actions .login-btn span {
  cursor: pointer;
  display: block;
  width: 245px;
  height: 45px;
  color: white;
  font-size: 18px;
  background-color: #5385ff;
  border: 1px solid #409eff;
  text-align: center;
  line-height: 45px;
  border-radius: 45px;
}

.reg-btn {
  display: flex;
  justify-content: center;
}
.reg-btn span {
  cursor: pointer;
  display: block;
  width: 245px;
  height: 45px;
  color: white;
  font-size: 18px;
  background-color: #2d956b;
  border: 1px solid #277b2b;
  text-align: center;
  line-height: 45px;
  border-radius: 45px;
}

.login-options {
  display: flex;
  padding: 0 10px;
  justify-content: space-between;
}
.login-options a {
  font-size: 12px;
  color: #999999;
  text-decoration: none;
  margin-top: 1px;
}

.other-infos {
  display: flex;
  justify-content: center;
}
.other-infos .login-other-info a,
.other-infos .login-other-info span {
  font-size: 13px;
  text-decoration: none;
}
.other-infos .login-other-info span {
  color: #999;
}
.other-infos .login-other-info a {
  color: #0085ea;
}
.other-infos .reg-other-info {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.other-infos .reg-other-info p {
  color: #999;
  font-size: 13px;
  margin: 3px 0;
}
.other-infos .reg-other-info a {
  text-decoration: none;
  color: #0085ea;
}

.other-way-part {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.other-way-title {
  font-size: 12px;
  color: #999;
}
.other-way-action {
  display: flex;
  margin-top: 20px;
}
.other-way-action a {
  margin: 0 10px;
  display: flex;
}
.other-way-action img {
  border: 1px solid #999;
  padding: 5px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
}


</style>
