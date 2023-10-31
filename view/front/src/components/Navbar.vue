<template>
<div>
  <div class="navbar-row-wrapper">
    <el-row :gutter="10">
      <el-col :offset="3" :span="2">
        <img src="../../static/img/uugai.com_1608212800504.png" alt="" style="max-width: 300px; height: 40px; cursor: pointer">
      </el-col>
      <el-col :span="1" class="domain-link-col" >
        <a class="domain-link" href="/" target="_blank">首页</a>
      </el-col>
      <el-col :span="1" class="domain-link-col">
        <a class="domain-link" href="/talk" target="_blank">看客</a>
      </el-col>
      <el-col :span="2" class="domain-link-col">
        <a class="domain-link">
          <el-popover
            placement="bottom"
            width="150"
            trigger="hover">
            <div style="text-align: center">
              <img style="width: 150px; height: 150px;" src="https://sf6-scmcdn2-tos.pstatp.com/xitu_juejin_web/img/app-install.6226a3b.png" alt="app">
            </div>
            <el-button type="text" slot="reference" class="domain-link">下载APP</el-button>
          </el-popover>
        </a>
      </el-col>
      <el-col :offset="2" :span="4">
        <search-box  :hotSearchDropDownItems="hotSearchItems" :historySearchDropDownItems="hotSearchItems"></search-box>
      </el-col>
      <!--      6-->
<!--      <el-col :span="1">-->
<!--      </el-col>-->
      <el-col :span="6" style="display: flex">
        <!-- 用户登录注册，以及头像框 -->
        <div class="user-action">
          <div v-if="!logged" style="display: flex">
            <div class="user-action-login-btn">
              <el-button type="text" @click="handleLogin">登录</el-button>
            </div>
            <a href="/login#register" class="user-action-reg-btn" target="_blank">
              注册
            </a>
          </div>
          <div v-else style="display: flex; align-items: center">
            <el-dropdown @command="handleUserActions">
<!--              <el-avatar :size="'large'" :src="circleUrl" class="navbar-avatar"></el-avatar>-->
              <img :src="user.avatar" alt="" class="navbar-avatar">
<!--              <el-avatar :size="'large'" :src="user.avatar" class="navbar-avatar" :fit="'contain'"></el-avatar>-->
              <el-dropdown-menu slot="dropdown" class="user-options">
                <el-dropdown-item command="home">
                  <i class="el-icon-user"></i>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="message">
                  <i class="el-icon-bell"></i>
                  <el-badge :value="unreadMessageCount > 999 ? '999+' : unreadMessageCount" :hidden="unreadMessageCount === 0" class="item">
                  消息通知
                  </el-badge>
                </el-dropdown-item>
                <el-dropdown-item command="home">
                  <i class="el-icon-magic-stick"></i>
                    文章管理
                </el-dropdown-item>
                <el-dropdown-item divided>
                </el-dropdown-item>
                <el-dropdown-item command="feedback">
                  <i class="el-icon-edit"></i>
                  意见反馈
                </el-dropdown-item>
                <el-dropdown-item divided/>
                  <el-dropdown-item command="logout">
                    <i class="el-icon-upload2"></i>
                    退出登录
                  </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <div class="navbar-user-symbols">
              <img class="navbar-user-symbol" src="http://192.168.1.100/user_symbol/authenticated_user.png" alt="symbol" width="20" height="20">
              <img class="navbar-user-symbol" src="http://192.168.1.100/user_symbol/level_6.png" alt="symbol" width="20" height="20">
            </div>
            <a href=""
               class="navbar-username">
              {{user.nickname}}</a>
          </div>
        </div>
        <!-- 写文章按钮 -->
        <div class="editor-action">
          <el-button v-if="!isWritingPage" @click="openEditor" type="primary" :round="true" icon="el-icon-edit-outline">写文章</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
  <el-dialog
    title="消息通知"
    :visible.sync="messageTipsVisible"
    width="50%"
    :before-close="handleMessageTipsClose">
    <div style="height: 600px; overflow: auto; padding: 0 10px; ">
      <el-timeline :reverse="true">
        <el-timeline-item :key="message.body" v-for="message in messages" :timestamp="message.datetime" placement="top">
          <el-card class="message">
            <h4 v-html="message.content"></h4>
            <p>{{message.title}}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
    <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="messageTipsVisible = false">阅 读 完 毕</el-button>
  </span>
  </el-dialog>
</div>
</template>

<script>
import SearchBox from "./SearchBox";
import jwtDecode from "jwt-decode";
import {formatDate} from "element-ui/lib/utils/date-util";
export default {
  name: "Navbar",
  components: {SearchBox},
  props: {
    isWritingPage: {
      default: false
    }
  },
  data() {
    return {
      unreadMessageCount: 0,
      messageTipsVisible: false,
      logged: false,
      user: {},
      hotSearchItems: [],
      messages: []
    }
  },
  methods: {
    openFeedback() {
      window.open('/feedback', '_blank');
    },
    handleMessageTipsClose(done) {
      done();
    },
    handleUserActions(command) {
      if (command === 'logout') this.logout()
      else if (command === 'home') this.goToHome()
      else if (command === 'message') this.openMessage()
      else if (command === 'feedback') this.openFeedback()
    },
    openMessage() {
      this.messageTipsVisible = true
      this.messages = []
      // this.messages
      this.$http.get('http://localhost:8080/sparrow/message/all/' + this.user.uid).then(res => {
        let msgs = res.data.data
        msgs.sort((a, b) => b.datetime - a.datetime)
        msgs.forEach(item => {
          let eachMsg = JSON.parse(item)
          eachMsg.datetime = formatDate(new Date(eachMsg.datetime), 'yyyy/MM/dd HH:mm:ss')
          this.messages.push(eachMsg)
        })
        console.log(this.messages)
      })
    },
    goToHome() {
      window.open('/home', '_blank');
    },
    logout() {
      localStorage.removeItem('JWT_TOKEN')
      // Location().reload()
      // window.open('/login', '_blank');
      window.location.reload()
    },
    openEditor() {
      if (this.logged) window.open('/editor/article', '_blank')
      else {
        this.$message({
          message: '请先登录',
          offset: 70
        })
        setTimeout(() => {
          window.open('/editor/article', '_blank')
        }, 1000)
      }
    },
    handleLogin() {
      window.open('/login#login', '_blank')
    },

  },
  mounted() {
    //     @GetMapping("/count/unread/{uid}")
    // console.log(localStorage.getItem('JWT_TOKEN'))
    this.logged = localStorage.getItem('JWT_TOKEN') != null
    console.log(this.logged)
    if (this.logged) {
      this.user = jwtDecode(localStorage.getItem('JWT_TOKEN'))

      // 获取未读消息的条数
      this.$http.get('http://localhost:8080/sparrow/message/count/unread/' + this.user.uid).then(res => {
        this.unreadMessageCount = res.data.data
      })
    } else {
      this.user = {
        avatar: ''
      }
    }

    // 获取redis中的热门 ->搜索<- 数据
    this.$http.get('http://localhost:8080/sparrow/hot-search/findAll').then(res => {
      if (res.data.status) {
        res.data.data.forEach(item => {
          this.hotSearchItems.push({
            title: item.title,
            value: item.value
          })
        })
        // this.hotArticlesLoading = false
      }
    })
    window.setInterval(() => {
      if (this.logged) {
        let before = this.unreadMessageCount
        let curMessageCount = 0
        this.$http.get('http://localhost:8080/sparrow/message/count/unread/' + this.user.uid).then(res => {
          curMessageCount = res.data.data
          if (curMessageCount - before > 0) {
            this.$notify({
              type: 'info',
              title: '您有' + (curMessageCount - before) + '条新的未读消息',
              offset: 50
            })
          }
          this.unreadMessageCount = curMessageCount
        })
      }
    }, 5000)
  }
}
</script>

<style scoped>
.domain-link-col {
  height: 57px;
  margin-top: -8px;
  line-height: 55px;
  text-align: center;
  transition: .3s;
}
.domain-link-col:hover {
  background-color: #f2f2f2;
}
.domain-link {
  text-decoration: none;
  cursor: pointer;
  font-size: 17px;
  color: #828282;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
}
.navbar-row-wrapper {
  /*width: 100%;*/
  height: 50px;
}
.navbar-avatar {
  border-radius: 50%;
  border: 1px solid #cecece;
  transition: .3s;
  margin: 0 25px 0 10px;
  cursor:pointer;
  width: 40px;
  height: 40px;
}
.navbar-avatar:hover {
  transform: scale(1.2);
}

.user-action {
  margin-left: 5px;
  margin-right: 5px;
  display: flex;
}
.user-action-login-btn {
  margin-right: 5px;
}

.user-action-reg-btn {
  transition: .3s;
  margin: 0 14px;
  top: -1px;
  position: relative;
  padding: 10px 40px;
  border-radius: 28px;
  font-size: 17px;
  color: #FFF;
  text-decoration: none;
  background-image: linear-gradient(90deg, #03a9f4, #f441a5, #ffeb3b, #03a9f4);
  background-size: 400%;
  z-index: 1;
}

.user-action-reg-btn:hover {
  animation: show 8s linear infinite;
}

@keyframes show {
  0% {
    background-position: 0;
  }
  100% {
    background-position: 400%;
  }
}
.user-action-reg-btn::before{
  transition: .3s;
  position: absolute;
  content: "";
  top:-5px;
  left:-5px;
  right:-5px;
  bottom:-5px;
  background-image: linear-gradient(90deg, #03a9f4, #f441a5, #ffeb3b, #03a9f4);
  background-size: 400%;
  border-radius: 40px;
  z-index: -1;
  filter: blur(20px);
  opacity: 0;
}
.user-action-reg-btn:hover::before{
  opacity: 1;
  animation: show 8s linear infinite;
}
.navbar-username {
  margin-top: -3px;
  height: 40px;
  line-height: 40px;
  text-decoration: none;
  /*color:#666;*/
  color: #d55656;
  margin-left: -10px;
  margin-right: 5px;
}
.navbar-user-symbols {
  margin-left: -14px;
  display: flex;
  align-items: center;
  margin-right: 10px;
}
.navbar-user-symbol {
  margin: 0 4px;
}
.message h4 a {
  text-decoration: none;
  color: #333;
}
</style>
