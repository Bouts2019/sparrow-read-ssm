<template>
<div style="min-height: 100vh; background-color:#f4f4f4;">
  <!-- Rolling Nav Bar -->
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
  <div class="content-wrapper">
    <div class="content">
      <div class="content-main">
        <div class="main-header-info">
          <div class="header-info-avatar">
            <img :src="this.me.avatar" alt="">
          </div>
          <div class="header-info-text">
            <div class="info-text-nickname">
              <span>
                <i v-if="this.me.gender === '0'" class="el-icon-male" style="color: blue"></i>
                <i v-else class="el-icon-female" style="color: deeppink"></i>
              </span>
              <span v-text="this.me.nickname"></span>
              <span>uid: {{this.me.uid}}</span>
            </div>
            <div class="info-text-description">
              <span v-text="this.me.description"></span>
            </div>
            <div class="info-text-meta-wrapper">
              <div class="info-text-meta">
                <div class="text-meta-info-title">关注</div>
                <div class="text-meta-info-data" v-text="this.followCount"></div>
              </div>
              <div class="info-text-meta">
                <div class="text-meta-info-title">粉丝</div>
                <div class="text-meta-info-data" v-text="this.fansCount"></div>
              </div>
              <div class="info-text-meta">
                <div class="text-meta-info-title">文章</div>
                <div class="text-meta-info-data" v-text="this.articleCount"></div>
              </div>
              <div class="info-text-meta">
                <div class="text-meta-info-title">看点</div>
                <div class="text-meta-info-data" v-text="this.talkCount"></div>
              </div>
              <div class="info-text-meta">
                <div class="text-meta-info-title">硬币</div>
                <div class="text-meta-info-data" v-text="this.me.coin"></div>
              </div>
            </div>
          </div>
        </div>
        <div class="main-body-tab">
          <el-tabs v-model="activeName" @tab-click="changeTab">
            <el-tab-pane label="我的文章" name="articles">
              <home-my-articles :my-id="parseInt(this.me.uid)"></home-my-articles>
            </el-tab-pane>
            <el-tab-pane label="我的看点" name="second">
              <home-my-talk :my-id="parseInt(this.me.uid)"></home-my-talk>
            </el-tab-pane>
            <el-tab-pane label="我的读书" name="book">
              <home-my-read :my-id="parseInt(this.me.uid)"></home-my-read>
            </el-tab-pane>
            <el-tab-pane label="我的关注" name="follow">
              <home-my-follow :my-id="parseInt(this.me.uid)"></home-my-follow>
            </el-tab-pane>
            <el-tab-pane label="我的粉丝" name="fans">
              <home-my-fans :my-id="parseInt(this.me.uid)"></home-my-fans>
            </el-tab-pane>
            <el-tab-pane label="个人设置" name="settings">
              <home-settings :me="this.me"></home-settings>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
      <div class="content-sidebar"></div>
    </div>
  </div>
</div>
</template>

<script>
import jwtDecode from "jwt-decode";
import Navbar from "../components/Navbar";
import HomeSettings from "../components/HomeSettings";
import HomeMyArticles from "../components/HomeMyArticles";
import HomeMyFollow from "../components/HomeMyFollow";
import HomeMyFans from "../components/HomeMyFans";
import HomeMyRead from "../components/HomeMyRead";
import HomeMyTalk from "../components/HomeMyTalk";

export default {
  name: "Home",
  components: {HomeMyTalk, HomeMyRead, HomeMyFans, HomeMyFollow, HomeMyArticles, HomeSettings, Navbar},
  data() {
    return {
      me: {},
      // 页面滚动
      scrollAction: {x: 'undefined', y: 'undefined'},
      scrollDirection: '',
      lastScrollDirection: '',
      useDefaultNavClass: true,
      fansCount: 0,
      followCount: 0,
      articleCount: 0,
      talkCount: 0,

      // 标签页
      // activeName: 'settings'
      activeName: 'articles'
    }
  },
  methods: {
    changeTab() {

    },
    //监听页面滚动事件
    scrollFunc() {
      if (typeof this.scrollAction.x == 'undefined') {
        this.scrollAction.x = window.pageXOffset;
        this.scrollAction.y = window.pageYOffset;
      }
      let diffX = this.scrollAction.x - window.pageXOffset;
      let diffY = this.scrollAction.y - window.pageYOffset;
      if (diffX < 0) {
        // Scroll right
        this.scrollDirection = 'right';
      } else if (diffX > 0) {
        // Scroll left
        this.scrollDirection = 'left';
      } else if (diffY < 0) {
        // Scroll down
        this.scrollDirection = 'down';
      } else if (diffY > 0) {
        // Scroll up
        this.scrollDirection = 'up';
      } else {
        // First scroll event
      }
      this.scrollAction.x = window.pageXOffset;
      this.scrollAction.y = window.pageYOffset;
    },
    loadFollowCount() {
      return this.$http.get('http://localhost:8080/sparrow/follow/count/follow/' + this.me.uid)
    },
    loadFansCount() {
      return this.$http.get('http://localhost:8080/sparrow/follow/count/fans/' + this.me.uid)
    },
    loadArticleCount() {
      return this.$http.get(`http://localhost:8080/sparrow/user/${this.me.uid}/count/article`)
    }
  },
  mounted() {
    // 上下鼠标事件
    window.addEventListener('scroll', e => {
      // document.
      this.scrollFunc();
      if (this.scrollDirection === 'down' && this.scrollDirection !== this.lastScrollDirection) {
        this.lastScrollDirection = this.scrollDirection
        this.useDefaultNavClass = false
      } else if (this.scrollDirection === 'up' && this.scrollDirection !== this.lastScrollDirection) {
        this.lastScrollDirection = this.scrollDirection
        this.useDefaultNavClass = true
      }
    })
    if (localStorage.getItem("JWT_TOKEN") === null || localStorage.getItem("JWT_TOKEN") === undefined) {
      window.location.href = '/login'
      return
    }
    this.me = jwtDecode(localStorage.getItem("JWT_TOKEN"))
    console.log(this.me)
    // 完整信息加载
    this.$http.get('http://localhost:8080/sparrow/user/' + this.me.uid).then(res => {
      this.me = res.data.data
      // 根据id获取关注列表、粉丝列表总数
      console.log('me')
      console.log(this.me)
      // 并发请求
      this.$http
        .all([
          this.loadFollowCount(),
          this.loadFansCount(),
          this.loadArticleCount(),
          this.$http.get('http://localhost:8080/sparrow/talk/count/' + this.me.uid)
        ])
        .then(this.$http.spread((followCountData, fansCountData, articleCountData, talkCountData) => {
          this.followCount = followCountData.data.data
          this.fansCount = fansCountData.data.data
          this.articleCount = articleCountData.data.data
          this.talkCount = talkCountData.data.data
      }))
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

.content-wrapper {
  background-color: #f4f4f4;
  display: flex;
  justify-content: center;
}
.content {
  margin-top: 15px;
  width: 1200px;
  display: flex;
  justify-content: space-between;
}
.content-main {
  width: 780px;
  background-color: white;
  box-shadow: 0 7px 5px -5px #d9d9d9;
}
.content-sidebar {
  width: 380px;
  height: fit-content;
  background-color: white;
}
.main-header-info {
  height: 100px;
  display: flex;
  align-items: center;
  padding: 10px;
}
.header-info-avatar {
  width: 100px;
  height: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.header-info-avatar img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 1px solid #999;
}
.header-info-text {
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  margin-left: 10px;
}
.info-text-nickname span {
  line-height: 24px;
  font-size: 24px;
  font-weight: 700;
  color: #333;
}
.info-text-nickname span:last-of-type {
  color: #bbb;
  font-size: 12px;
  font-weight: 400;
  margin-left: 10px;
}
.info-text-description span {
  font-size: 14px;
  color: #999;
}
.info-text-meta-wrapper {
  margin-top: 5px;
  display: flex;
  justify-content: left;
}
.info-text-meta {
  width: 50px;
  height: 30px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.info-text-meta:nth-child(n + 2){
  padding-left: 10px;
  border-left: 2px solid #f4f4f4;
}
.text-meta-info-title {
  font-size: 12px;
  color: #999;
}
.text-meta-info-data {
  font-size: 12px;
  color: #666;
}

.main-body-tab {
  padding: 10px 20px;
}

</style>
