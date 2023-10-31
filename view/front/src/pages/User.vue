<template>
  <div>
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
    <div style="height: 45px; width: 100%"/>
    <div class="content-wrapper">
      <div class="content">
        <div class="user-header-wrapper">
          <div class="user-header">
            <div class="user-avatar">
              <img :src="user.avatar" alt="">
            </div>
            <div class="user-nickname">
              <h2>{{user.nickname}}<i class="el-icon-male" :style=" { color: user.gender === 0 ? 'skyblue' : 'pink' } "></i></h2>
            </div>
            <div class="user-desc">
              <span>{{user.description}}</span>
            </div>
            <div class="user-actions">
              <span v-if="!isFollowed" class="user-action-follow-btn" @click="follow"><i class="el-icon-plus"></i> 关注</span>
              <span v-else class="user-action-unfollow-btn"><i class="el-icon-check" @click="unfollow"></i> 已关注</span>
            </div>
          </div>
        </div>
        <div class="user-body">
          <div class="body-sidebar">
            <div class="sidebar-metas">
              <div class="meta">
                <span>{{articleCount}}</span>
                <span>文章</span>
              </div>
              <div class="meta">
                <span>{{talkCount}}</span>
                <span>动态</span>
              </div>
              <div class="meta">
                <span>{{followCount}}</span>
                <span>关注</span>
              </div>
              <div class="meta">
                <span>{{fansCount}}</span>
                <span>粉丝</span>
              </div>
            </div>
            <div class="sidebar-infos">
              <span style="font-size: 14px; font-weight: 700; display: block; height: 30px;">个人信息：</span>
              <p>个人简介：{{user.description}}</p>
              <p><font-awesome-icon :icon="['fas', 'birthday-cake']" style="color: #888"/>&nbsp;&nbsp;生日: {{user.birthday}}</p>
              <p><font-awesome-icon :icon="['fas', 'calendar-alt']" style="color: #888"/>&nbsp;&nbsp;注册时间: {{user.regTime}}</p>
              <p><font-awesome-icon :icon="['fas', 'envelope']" style="color: #888"/>&nbsp;&nbsp;邮箱: {{user.email}}</p>
            </div>
            <ul class="sidebar-fans">
              <span style="display: block; height: 25px; font-size: 14px; color: #333; font-weight: 700; margin-left: 10px;">粉丝列表</span>
              <li class="sidebar-fan" v-for="fan in fans">
                <img :src="fan.avatar" alt="">
                <a :href="'/user/' + fan.uid" target="_blank">{{fan.nickname}}</a>
              </li>
            </ul>
          </div>
          <div class="body-dynamic">
            <el-tabs type="border-card" :stretch="true">
              <el-tab-pane :label="(this.user.gender === 0 ? '他' : '她') + '的动态'">
                <user-talks :user-id="parseInt(userId)"/>
              </el-tab-pane>
              <el-tab-pane :label="(this.user.gender === 0 ? '他' : '她') + '的文章'">
                <user-articles :user-id="parseInt(userId)"/>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from "../components/Navbar";
import HomeMyArticles from "../components/HomeMyArticles";
import UserTalks from "../components/UserTalks";
import UserArticles from "../components/UserArticles";
import jwtDecode from "jwt-decode";
export default {
  name: "User",
  components: {UserArticles, UserTalks, HomeMyArticles, Navbar},
  data() {
    return {
      isFollowed: false,
      me: {},
      user: { },
      articleCount: 999,
      talkCount: 999,
      followCount: 999,
      fansCount: 999,
      userId: null,
      fans: [],
      // 页面滚动
      scrollAction: {x: 'undefined', y: 'undefined'},
      scrollDirection: '',
      lastScrollDirection: '',
      useDefaultNavClass: true,
    }
  },
  methods: {
    follow() {
      this.$http.post('http://localhost:8080/sparrow/follow/follow', {
        from: this.me.uid,
        to: this.user.uid
      }).then(res => {
        this.$notify.success({
          title: '关注成功',
          message: '关注UP主：' + this.user.nickname + '成功',
          offset: 50,
        })
        this.isFollowed = true
      }).catch(err => {
        this.$notify.error({
          title: '关注异常',
          message: '关注UP主：' + this.user.nickname + '异常',
          offset: 50
        })
      })
    },
    unfollow() {
      if (parseInt(this.me.uid) === this.user.uid) {
        this.$alert('自己无法取关自己哦', {
          callback: action => {
          }
        })
        return
      }
      this.$confirm('确定取消关注该up主吗？/(ㄒoㄒ)/~~', '确定么', {
        confirmButtonText: '取关ta!',
        cancelButtonText: '再想想',
        type: 'warning'
      }).then(() => {
        this.$http.post('http://localhost:8080/sparrow/follow/unfollow', {
          from: this.me.uid,
          to: this.user.uid
        }).then(res => {
          this.$notify.success({
            title: '取消关注成功',
            message: '取消关注UP主：' + this.user.nickname + '成功',
            offset: 50,
          })
          this.isFollowed = false
        }).catch(err => {
          this.$notify.error({
            title: '取关异常',
            message: '取关UP主：' + this.user.nickname + '异常',
            offset: 50
          })
        })
      }).catch(() => {

      })
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
    loadInfos() {
      this.$http.get('http://localhost:8080/sparrow/user/' + this.userId).then(res => {
        this.user = res.data.data
        this.$http.all([
          this.$http.get('http://localhost:8080/sparrow/user/' + this.userId + '/count/article'),
          this.$http.get('http://localhost:8080/sparrow/talk/count/' + this.userId),
          this.$http.get('http://localhost:8080/sparrow/follow/count/follow/' + this.userId),
          this.$http.get('http://localhost:8080/sparrow/follow/count/fans/' + this.userId)
        ]).then(this.$http.spread((articleInfoData, talkInfoData, followCountData, fansCountData) => {
          this.articleCount = articleInfoData.data.data
          this.talkCount = talkInfoData.data.data
          this.followCount = followCountData.data.data
          this.fansCount = fansCountData.data.data
        }))
        if (parseInt(this.user.uid) === parseInt(this.me.uid)) {
          this.isFollowed = true
        } else {
          this.$http.get(`http://localhost:8080/sparrow/follow/isFollowed?from=${this.me.uid}&to=${this.user.uid}`).then(res => {
            this.isFollowed = res.data.data
          })
        }
      })
      this.$http.get('http://localhost:8080/sparrow/follow/fans/' + this.userId).then(res => {
        let allFans = res.data.data
        let length = Math.min(allFans.length, 3)
        if (length > 0) {
          let ids = []
          for (let i = 0; i < length; i++) ids.push(parseInt(allFans[i]))
          let queryString = ids.map(item => 'ids=' + item).join('&')
          console.log(queryString)
          this.$http.get('http://localhost:8080/sparrow/user/ids?' + queryString).then(res => {
            let fans = res.data.data
            fans.forEach(fan => this.fans.push(fan))
          })
        }
      })
    }
  },
  mounted() {
    if (localStorage.getItem("JWT_TOKEN") !== null) {
      this.me = jwtDecode(localStorage.getItem("JWT_TOKEN"))
    } else {
      this.me = null
    }
    this.userId = this.$route.params.id
    this.loadInfos()
  }
}
</script>

<style scoped>
.rolling-bar {
  box-shadow: 0 7px 5px -5px #d9d9d9;
}
.clearfix:after {
  visibility: hidden;
  display: block;
  font-size: 0;
  content: " ";
  clear: both;
  height: 0;
}
.content-wrapper {
  margin: 0;
  min-height: 100vh;
  width: 100%;
  background-color: rgba(245, 233, 211, .5);
  height: fit-content;
}
.content {
  margin: 0 auto;
  width: 960px;
  height: fit-content;
}
.user-header-wrapper {
  background: url("http://192.168.1.100/backgrounds/asteroids.jpg");
  /*filter: blur(2px);*/
}
.user-header {
  height: 300px;
  /*background-color: deepskyblue;*/
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.user-nickname,
.user-desc,
.user-actions {
  margin-top: 10px;
}
.user-avatar {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  border: 3px solid rgba(255, 255, 255, .5);
}
.user-avatar img {
  width: 110px;
  height: 110px;
  border-radius: 50%;
}
.user-nickname h2 {
  margin: 0;
  padding: 0;
  font-weight: 400;
  color: white;
  font-size: 20px;
}
.user-desc span {
  color: white;
  font-size: 14px;
}
.user-actions {

}
.user-actions .user-action-follow-btn {
  text-align: center;
  display: block;
  width: 100px;
  height: 30px;
  color: white;
  border-radius: 3px;
  cursor: pointer;
  font-size: 14px;
  line-height: 30px;
  background-color: burlywood;
}
.user-actions .user-action-unfollow-btn {
  text-align: center;
  display: block;
  width: 100px;
  height: 30px;
  color: white;
  border-radius: 3px;
  cursor: pointer;
  font-size: 14px;
  line-height: 30px;
  background-color: #999;
}

.user-body {
  display: flex;
  margin-top: 10px;
  justify-content: center;
}
.body-sidebar {
  width: 300px;
}
.body-dynamic {
  margin: 0 0 0 10px;
  padding: 0;
  width: 660px;
}
.sidebar-metas {
  margin: auto 0;
  width: 290px;
  height: 50px;
  background-color: white;
  border-radius: 5px;
  border: 1px solid #f2f2f5;
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.meta {
  height: 30px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}
.meta span:nth-child(1) {
  font-size: 12px;
  color: #333;
}
.meta span:nth-child(2) {
  font-size: 12px;
  color: #666;
}
.sidebar-infos {
  margin: 10px auto 0;
  padding: 10px 15px;
  box-sizing: border-box;
  width: 290px;
  height: fit-content;
  border-radius: 5px;
  border: 1px solid #f2f2f5;
  background-color: white;
  /*box-shadow: 0 7px 5px -5px #d9d9d9;*/
}
.sidebar-infos p {
  padding: 5px 0;
  margin: 0;
  font-size: 12px;
  color: #333;
}
.sidebar-infos p:nth-child(n + 2) {
  border-top: 1px solid #f4f4f4;
}
.sidebar-fans {
  padding: 10px;
  box-sizing: border-box;
  margin: 10px auto 0;
  width: 290px;
  border-radius: 5px;
  border: 1px solid #f2f2f5;
  height: fit-content;
  background-color: white;
}
.sidebar-fan {
  padding: 5px 5px;
  border-radius: 5px;
  display: flex;
  align-items: center;
}
.sidebar-fan:nth-child(n + 2) {
  border-top: 1px solid #f4f4f4;
}
.sidebar-fan img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 3px solid rgba(255, 255, 255, 0.5);
}
.sidebar-fan a {
  text-decoration: none;
  font-size: 14px;
  color: #333;
  font-weight: 700;
  margin-left: 10px;
}
.sidebar-fan a:hover {
  text-decoration: underline;
}
</style>
