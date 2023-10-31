<template>
<div style='min-height: 100vh; background-color: #ece9ef; background-size: 1920px 1080px; background-repeat: no-repeat; background-attachment: fixed;'>
  <!-- background-image: url("../../static/img/talk-background.jpg"); -->
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
      <div class="sidebar-left">
        <ul class="topic-links-list">
          <li @click="changeFilter('new')" class="topic-links-item" :class="{active: activeId === 0}">最新</li>
          <li class="topic-links-item-divider"></li>
          <li @click="changeFilter('topic1')" class="topic-links-item" :class="{active: activeId === 1}">疯狂吐槽</li>
          <li @click="changeFilter('topic2')" class="topic-links-item" :class="{active: activeId === 2}">大师我悟了</li>
          <li @click="changeFilter('topic3')" class="topic-links-item" :class="{active: activeId === 3}">xswl</li>
          <li @click="changeFilter('topic4')" class="topic-links-item" :class="{active: activeId === 4}">问问xdm</li>
        </ul>
      </div>
      <div class="main-body">
        <div class="main-body-editor" :class="{writing: isWriting}">
          <div id="mytalk-editor" class="inner-editor" contenteditable="true" @input="inputMyTalkContent" placeholder="" @focus="isWriting = true" @blur="isWriting = false">
          </div>
          <div class="inner-editor-actions">
            <div>
              <span style="font-size: 14px; color: #999;">添加图片: </span>
              <el-upload
                style="display: inline-block"
                action="http://localhost:8080/sparrow/talk/image"
                :show-file-list="false"
                :on-success="imageUploadSuccessfully"
                :before-upload="beforeImageUpload">
              <span v-if="myTalk.imgList.length > 0">
                <img v-for="imgSrc in myTalk.imgList" :src="imgSrc" class="upload-images">
              </span>
                <span v-else style="font-size: 14px; color: #999;">最多上传3张图片</span>
                <span class="img-uploader-icon">+</span>
              </el-upload>
              <span v-if="myTalk.imgList.length > 0" class="img-uploader-icon" @click.prevent="removeOneImage">-</span>
            </div>
            <div>
              <span class="length-tip" :class="{exceed: myTalk.content.length > 300}">{{myTalk.content.length}} / 300</span>
              <el-dropdown @command="chooseTag">
              <span class="el-dropdown-link">
<!--                下拉菜单<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
                <el-tag size="mini" :type="this.myTalk.tagName === null ? 'info' : 'primary'">
                  {{this.myTalk.tagName === null ? '不加标签' : this.myTalk.tagName}}
                </el-tag>
              </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="0">不加标签</el-dropdown-item>
                  <el-dropdown-item divided></el-dropdown-item>
                  <el-dropdown-item command="1">疯狂吐槽</el-dropdown-item>
                  <el-dropdown-item command="2">大师我悟了</el-dropdown-item>
                  <el-dropdown-item command="3">xswl</el-dropdown-item>
                  <el-dropdown-item command="4">问问xdm</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
              <el-button type="primary"
                         size="mini"
                         :disabled="myTalk.content.length > 300 || myTalk.content.length === 0"
                         @click="sendTalk">
                发送
              </el-button>
            </div>
          </div>
        </div>
        <ul class="talk-list">
          <li class="talk-item" v-for="(talk, index) in talks">
            <div class="talk-header">
              <div class="header-infos">
                <div class="header-avatar">
                  <img :src="talk.userAvatar" alt="">
                </div>
                <div class="header-text-infos">
                  <div>
                    <a target="_blank" :href="'/user/' + talk.userId" class="text-info-user-nickname">{{talk.userNickname}}</a>
                  </div>
                  <div>
                    <span class="text-info-user-desc">{{talk.userDesc}}</span> -
                    <span class="text-info-user-publish-time">{{getDateString(talk.publishTime)}}</span>
                  </div>
                </div>
              </div>
              <div class="header-actions" :class="{ isFollowed: talk.isFollowed }">
                <span v-if="!talk.isFollowed" class="follow-talk-user-btn" @click="follow(index)"><i class="el-icon-plus"></i> 关注</span>
                <span v-else class="unfollow-talk-user-btn" @click="unfollow(index)"><i class="el-icon-check"></i> 已关注</span>
              </div>
            </div>
            <div class="talk-body">
              <div class="talk-body-text" :class="{expansion: !talk.expanded}">
                <span>{{talk.content}}</span>
              </div>
              <div v-if="talk.overflowed" class="talk-body-text-expand">
                <div v-if="!talk.expanded">
                  <div class="overflow"></div>
                  <span @click="expand(index, true)">展开</span>
                </div>
                <div v-else>
                  <span @click="expand(index, false)">收缩</span>
                </div>
              </div>
              <div class="talk-body-images">
                <img v-for="imgSrc in talk.imgList" :src="imgSrc" alt="" @click="openImage(imgSrc)">
              </div>
<!--              <div class="talk-body-links"></div>-->
              <div class="talk-body-tags" v-if="talk.tagName !== null">
                <span class="talk-body-tag">{{talk.tagName}}</span>
              </div>
            </div>
            <div class="talk-operations">
              <div class="operation-likes" @click="likeThisTalk(index)" :class="{isLike: talk.isLike}">
                <font-awesome-icon :icon="['far', 'thumbs-up']"/>
                <span>{{talk.likeCount}}</span>
              </div>
              <div class="operation-comments" @click="showOrHideTalkComment(index)">
                <font-awesome-icon :icon="['far', 'comment']"/>
                <span>{{talk.commentCount}}</span>
              </div>
              <!-- todo: 有时间再写 -->
<!--              <div class="operation-other">-->
<!--                <font-awesome-icon :icon="['far', 'share-square']"/>-->
<!--              </div>-->
            </div>
            <div class="talk-comments" v-if="talk.showComments">
              <div class="comment-textarea-wrapper">
                <div class="comment-me-avatar">
                  <img :src="me.avatar" alt="">
                </div>
                <div class="comment-textarea-operation">
                  <div contentEditable="true"
                       class="talk-comment-editor"
                       @focus="commentEditorFocus = true"
                       @blur="commentEditorFocus = false"
                       :class="{ active: commentEditorFocus }"
                       @input="inputTalkComment"
                  >
                  </div>
                  <div class="comment-operations">
                    <span class="comment-length-tips">{{myComment.commentContent.length}} / 100</span>
                    <el-button :disabled="myComment.commentContent.length === 0 || myComment.commentContent.length > 100" type="primary" size="mini" @click="postTalkComment(index)" class="post-comment-btn">发送</el-button>
                    <el-button
                      v-if="myComment.replyUserId !== null"
                      type="info"
                      :plain="true"
                      size="mini"
                      v-text="'取消回复 @ ' + myComment.replyUserNickname"
                      @click="cancelReplyTalkComment">
                    </el-button>
                  </div>
                </div>
              </div>
              <div v-if="talk.comments.length === 0">
                <div style="padding-bottom: 20px; height: 20px; text-align: center;">
                  <span style="line-height: 20px; font-size: 14px; color: #999;">这里还没有内容哦</span>
                </div>
              </div>
              <div v-else class="">
                <ul class="comment-list">
                  <li v-for="(comment, commentIndex) in talk.comments" class="comment-item">
                    <div class="comment-user-avatar">
                      <img :src="comment.userAvatar" alt="">
                    </div>
                    <div class="comment-infos">
                      <div class="comment-user-info">
                        <span>
                        <a :href="'/user/' + comment.userId" class="comment-user-nickname" v-text="comment.userNickname"></a>
                        <span class="comment-comment-time" v-text="comment.commentTime"></span>
                        </span>
                        <span>
<!--                          <span class="comment-like-btn" style="margin-right: 10px;"> <font-awesome-icon :icon="['far', 'thumbs-up']"/> {{comment.likeCount}}</span>-->
<!--                          <span class="comment-dislike-btn"> <font-awesome-icon :icon="['far', 'thumbs-down']"/> </span>-->
                          <span class="comment-reply-btn" @click="replyTalkComment(comment.userId, comment.userNickname)">回复</span>
                          <span class="comment-index" v-text="commentIndex + 1"></span>
                        </span>
                      </div>
                      <div class="comment-content">
                        <a v-if="comment.replyUserId !== null" :href="'/user/' + comment.replyUserId" v-text="comment.replyUserName"></a>
                        <span v-text="comment.commentContent"></span>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </li>
        </ul>
      </div>
      <div class="sidebar-right">
        <div class="my-info-wrapper">
          <div class="my-info">
            <div class="my-avatar">
              <img class="my-avatar-img" :src="me.avatar" alt="">
            </div>
            <div class="my-text-info">
              <span>{{me.nickname}}</span>
              <span v-text="me.description"></span>
            </div>
          </div>
          <div class="my-meta-list">
            <div class="my-meta-item">
              <span @click="$router.push('/home')">看点</span>
              <span>{{me.talkCount}}</span>
            </div>
            <div class="my-meta-item">
              <span>关注</span>
              <span>{{me.followCount}}</span>
            </div>
            <div class="my-meta-item">
              <span>粉丝</span>
              <span>{{me.fansCount}}</span>
            </div>
          </div>
        </div>
        <div class="notice-wrapper">
          <div class="notice-title">
            <span>👀用户守则</span>
          </div>
          <div class="notice-content">
            <p>提倡正确导向，反对不良网风。</p>
            <p>提倡遵纪守法，反对违规违纪。</p>
            <p>提倡客观真实，反对虚假新闻。</p>
            <p>提倡先进文化，反对落后文化。</p>
            <p>提倡格调高雅，反对低级媚俗。</p>
            <p>提倡公平守信，反对恶性竞争。</p>
            <p>提倡科技创新，反对墨守成规。</p>
            <p>提倡团结协作，反对损人利己。</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import Navbar from "../components/Navbar";
import {formatDate} from "element-ui/lib/utils/date-util";
import {friendlyFormatTime} from '../apis/time'
import jwtDecode from "jwt-decode";
export default {
  components: {Navbar},
  data() {
    return {
      activeId: 0,
      scrollAction: {x: 'undefined', y: 'undefined'},
      scrollDirection: '',
      lastScrollDirection: '',
      useDefaultNavClass: true,
      me: {},
      myTalk: {
        tagId: null,
        tagName: null,
        content: '',
        imgList: []
      },
      talks: [],
      isWriting: false,
      commentEditorFocus: true,
      myComment: {
        commentContent: '',
        replyUserId: null,
        replyUserNickname: null
      }
    }
  },
  methods: {
    changeFilter(filter) {
      if (this.$route.hash === '#' + filter || (this.$route.hash === undefined && filter === 'new') || (this.$route.hash === '' && filter === 'new')) {
        return
      }
      if (filter === 'new') {
        this.$router.push('/talk')
        this.$router.go(0)
        return
      }
      this.$router.push(`/talk#${filter}`)
      this.$router.go(0)
    },
    follow(index) {
      this.$http.post('http://localhost:8080/sparrow/follow/follow', {
        from: this.me.uid,
        to: this.talks[index].userId
      }).then(res => {
        this.$notify.success({
          title: '关注成功',
          message: '关注UP主：' + this.talks[index].userNickname + '成功',
          offset: 50,
        })
        let curId = this.talks[index].userId
        for (let i = 0; i < this.talks.length; i++) {
          if (this.talks[i].userId === curId) {
            this.talks[i].isFollowed = true
          }
          this.$set(this.talks, i, this.talks[i])
        }
        this.me.followCount++
      }).catch(err => {
        this.$notify.error({
          title: '关注异常',
          message: '关注UP主：' + this.talks[index].userNickname + '异常',
          offset: 50
        })
      })
    },
    unfollow(index) {
      if (parseInt(this.me.uid) === this.talks[index].userId) {
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
          to: this.talks[index].userId
        }).then(res => {
          this.$notify.success({
            title: '取消关注成功',
            message: '取消关注UP主：' + this.talks[index].userNickname + '成功',
            offset: 50,
          })
          let curId = this.talks[index].userId
          for (let i = 0; i < this.talks.length; i++) {
            if (this.talks[i].userId === curId) {
              this.talks[i].isFollowed = false
              this.$set(this.talks, i, this.talks[i])
            }
          }
          this.me.followCount--
        }).catch(err => {
          this.$notify.error({
            title: '取关异常',
            message: '取关UP主：' + this.talks[index].userNickname + '异常',
            offset: 50
          })
        })
      }).catch(() => {
      })
    },
    likeThisTalk(index) {
      if (this.me === null) {
        this.$alert('您还没有登录~')
      } else {
        if (this.talks[index].isLike) {
          this.$confirm('确定取消嘛', '确定', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'info'
          }).then(_ => {
            this.$http.post('http://localhost:8080/sparrow/talk/toggle-like', {tid: this.talks[index].talkId, uid: this.me.uid}).then(res => {
              this.talks[index].likeCount--
              this.talks[index].isLike = false
            })
          }).catch(_ => {

          })
        } else {
          this.$http.post('http://localhost:8080/sparrow/talk/toggle-like', {tid: this.talks[index].talkId, uid: this.me.uid}).then(res => {
            this.talks[index].likeCount++
            this.talks[index].isLike = true
          })
        }
      }
    },
    // 取消回复talk评论
    cancelReplyTalkComment() {
      this.myComment.replyUserId = null
      this.myComment.replyUserNickname = null
    },
    // 回复talk评论
    replyTalkComment(userId, userNickname) {
      this.myComment.replyUserId = userId
      this.myComment.replyUserNickname = userNickname
    },
    // 输入评论内容
    inputTalkComment(e) {
      this.myComment.commentContent = e.target.innerText
    },
    // 发送评论talk
    postTalkComment(index) {
      let talkId = this.talks[index].talkId
      this.myComment.userId = this.me.uid
      this.$http.post('http://localhost:8080/sparrow/talk-comment/add/' + talkId, this.myComment).then(res => {
        this.$message({
          type: 'success',
          message: '评论成功！',
          offset: 80
        })
        this.talks[index].commentCount++
        document.getElementsByClassName('talk-comment-editor')[0].innerHTML = ''
        this.myComment.commentContent = ''
        this.myComment.replyUserId = null
        this.myComment.replyUserNickname = null
        this.getCommentsByTalkIndex(index)
      })
    },
    // 获取对应的编码的talk的评论
    getCommentsByTalkIndex(index) {
      this.$http.get('http://localhost:8080/sparrow/talk-comment/all/' + this.talks[index].talkId).then(res => {
        this.talks[index].comments = res.data.data
        this.talks[index].comments.map(comment => {
          let dateString = formatDate(new Date(comment.commentTime), 'yyyy-MM-dd HH:mm:ss')
          comment.commentTime =  friendlyFormatTime(dateString)
          this.$http.get('http://localhost:8080/sparrow/user/' + comment.userId).then(res => {
            comment.userNickname = res.data.data.nickname
            comment.userAvatar = res.data.data.avatar
            this.isCommentLoading = false

            this.$set(this.talks, index, this.talks[index])
          })
          if (comment.replyUserId !== null) {
            this.$http.get('http://localhost:8080/sparrow/user/' + comment.replyUserId).then(res => {
              comment.replyUserName = res.data.data.nickname

              this.$set(this.talks, index, this.talks[index])
            })
          }
        })
      })
    },
    // 显示或者隐藏talk的评论
    showOrHideTalkComment(index) {
      this.talks[index].showComments = !this.talks[index].showComments
      if (this.talks[index].showComments) {
        this.getCommentsByTalkIndex(index)
      }
      this.$set(this.talks, index, this.talks[index])
    },
    scrollFunc() {
      if (typeof this.scrollAction.x == 'undefined') {
        this.scrollAction.x = window.pageXOffset;
        this.scrollAction.y = window.pageYOffset;
      }
      let diffX = this.scrollAction.x - window.pageXOffset;
      let diffY = this.scrollAction.y - window.pageYOffset;
      if (diffX < 0) this.scrollDirection = 'right';
      else if (diffX > 0) this.scrollDirection = 'left'
      else if (diffY < 0) this.scrollDirection = 'down'
      else if (diffY > 0) this.scrollDirection = 'up'
      else {}
      this.scrollAction.x = window.pageXOffset;
      this.scrollAction.y = window.pageYOffset;
    },
    expand(index, status) {
      this.talks[index].expanded = status
      this.$set(this.talks, index, this.talks[index])
    },
    getDateString(date) {
      let dateString = formatDate(date, 'yyyy-MM-dd HH:mm:ss')
      return friendlyFormatTime(dateString)
    },
    openImage(src) {
      window.open(src, '_blank')
    },
    inputMyTalkContent(e) {
      this.myTalk.content = e.target.innerText
    },
    chooseTag(command) {
      switch (command) {
        case '0':
          this.myTalk.tagId = null
          this.myTalk.tagName = null
          break
        case '1':
          this.myTalk.tagId = 1
          this.myTalk.tagName = '疯狂吐槽'
          break
        case '2':
          this.myTalk.tagId = 2
          this.myTalk.tagName = '大师我悟了'
          break
        case '3':
          this.myTalk.tagId = 3
          this.myTalk.tagName = 'xswl'
          break
        case '4':
          this.myTalk.tagId = 4
          this.myTalk.tagName = '问问xdm'
          break
      }
    },
    removeOneImage() {
      this.myTalk.imgList.pop()
    },
    beforeImageUpload() {
      if (this.myTalk.imgList.length >= 3) {
        this.$alert('最多只能上传3张图片！', {
          type: 'error'
        })
        return false
      }
    },
    imageUploadSuccessfully(res, file, fileList) {
      this.myTalk.imgList.push(res.data)
    },
    loadTalkData() {
      let now = formatDate(new Date(), 'yyyy-MM-dd HH:mm:ss')
      this.$http({
        method: 'get',
        url: 'http://localhost:8080/sparrow/talk',
        params: {
          time: now,
          filter: this.activeId
        }
      }).then(res => {
        this.talks = res.data.data
        for (let i = 0; i < this.talks.length; i++) {
          this.talks[i].overflowed = this.talks[i].content.length > 100
          this.talks[i].expanded = false
          this.talks[i].showComments = false
          this.talks[i].comments = []
          if (parseInt(this.talks[i].userId) === parseInt(this.me.uid)) {
            this.talks[i].isFollowed = true
          } else {
            this.$http.get(`http://localhost:8080/sparrow/follow/isFollowed?from=${this.me.uid}&to=${this.talks[i].userId}`).then(res => {
              this.talks[i].isFollowed = res.data.data
            })
          }
          this.$http.get('http://localhost:8080/sparrow/talk/count/like/' + this.talks[i].talkId).then(res => {
            this.talks[i].likeCount = res.data.data
          })
          this.$http.get('http://localhost:8080/sparrow/talk/is-like?tid=' + this.talks[i].talkId + '&uid=' + this.me.uid).then(res => {
            this.talks[i].isLike = res.data.data
          })
          this.$http.get('http://localhost:8080/sparrow/talk-comment/size/' + this.talks[i].talkId).then(res => {
            this.talks[i].commentCount = res.data.data
          })
        }
      })
    },
    sendTalk() {
      let me = jwtDecode(localStorage.getItem('JWT_TOKEN'))
      this.myTalk.userId = me.uid
      this.$http.post('http://localhost:8080/sparrow/talk/one', this.myTalk).then(res => {
        this.$alert('发布成功！o(*￣▽￣*)ブ', {
          type: 'success',
          callback: action => {
            this.myTalk.userId = null
            this.myTalk.tagId = null
            this.myTalk.tagName = null
            this.myTalk.imgList = []
            this.myTalk.content = ''
            this.me.talkCount++
            this.loadTalkData()
            document.getElementById('mytalk-editor').innerText = ''
          }
        })
      })
    },
    loadMyInfo() {
      this.$http.all([
        this.$http.get('http://localhost:8080/sparrow/user/' + this.me.uid),
        this.$http.get('http://localhost:8080/sparrow/follow/count/follow/' + this.me.uid),
        this.$http.get('http://localhost:8080/sparrow/follow/count/fans/' + this.me.uid),
        this.$http.get('http://localhost:8080/sparrow/talk/count/' + this.me.uid)
      ]).then(this.$http.spread((myInfo, followInfo, fansInfo, talkCountInfo) => {
        this.me.description = myInfo.data.data.description
        this.me.followCount = followInfo.data.data
        this.me.fansCount = fansInfo.data.data
        this.me.talkCount = talkCountInfo.data.data
      }))
    }
  },
  mounted() {
    if (localStorage.getItem("JWT_TOKEN") !== null) {
      this.me = jwtDecode(localStorage.getItem("JWT_TOKEN"))
      this.isLogging = true
      this.loadMyInfo()
    } else {
      this.me = null
      this.isLogging = false
    }
    let hash = this.$route.hash
    if (hash === undefined || hash === null || hash === '') this.activeId = 0
    else if (hash === '#topic1') this.activeId = 1
    else if (hash === '#topic2') this.activeId = 2
    else if (hash === '#topic3') this.activeId = 3
    else if (hash === '#topic4') this.activeId = 4
    else this.activeId = 0
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
    this.loadTalkData()
  },
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
  display: flex;
  justify-content: center;
}
.content {
  margin-top: 10px;
  width: 1200px;
  height: fit-content;
  display: flex;
}
.sidebar-left {
  width: 100px;
}
.topic-links-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.topic-links-item {
  width: 90px;
  height: 30px;
  text-align: center;
  line-height: 30px;
  margin: 3px 0;
  border-radius: 5px;
  font-size: 14px;
  color: #333;
  transition: .3s;
  cursor: pointer;
}
.topic-links-item-divider {
  height: 1px;
  width: 70px;
  margin: 10px 0;
  background-color: #999999;
}
.topic-links-item:hover {
  color: #0088ff;
  /*background-color: #0085ea;*/
  background-color: white;
}
.topic-links-item.active {
  color: white;
  background-color: #0085ea;
}



.main-body {
  width: 800px;
}
.main-body-editor {
  background-color: white;
  border-radius: 2px;
  box-sizing: border-box;
  border: 1px solid white;
  padding: 10px;
}
.main-body-editor.writing {
  border: 1px solid #0085ea;
}
.main-body-editor .inner-editor {
  box-sizing: border-box;
  /*outline-style: none;*/
  border-radius: 5px;
  padding: 5px;
  width: 99%;
  overflow-y: hidden;
  resize: none;
  border: none;
  font-size: 14px;
  min-height: 80px;
  outline: 1px solid #f4f4f4;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
}
.main-body-editor .inner-editor:empty::before {
  content: '想说点什么？';
  color: #aaa;
}
.inner-editor-actions {
  margin-top: 10px;
  display: flex;
  padding: 0 10px;
  justify-content: space-between;
}
.upload-images {
  width: 80px;
  height: 80px;
  border: 1px dashed #999;
}
.img-uploader-icon {
  display: inline-block;
  font-size: 15px;
  color: #0085ea;
  width: 15px;
  height: 15px;
  line-height: 15px;
  text-align: center;
  border: 1px dashed #0085ea;
  cursor: pointer;
}
.length-tip {
  font-size: 12px;
  color: #999;
}
.length-tip.exceed {
  color: red;
}
.sidebar-right {
  width: 200px;
}
.talk-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.talk-item {
  margin: 10px 0;
  padding: 20px 20px 5px 20px;
  background-color: white;
  border: 1px solid #f4f4f4;
  border-radius: 5px;
  transition: .3s;
}
.talk-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-infos {
  display: flex;
}
.header-avatar {
  width: 48px;
  height: 48px;
}
.header-avatar img {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: 1px solid #999;
}
.header-text-infos {
  margin-left: 10px;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
}
.text-info-user-nickname {
  text-decoration: none;
  color: #333;
  display: block;
  font-size: 14px;
  font-weight: 700;
  line-height: 14px;
  height: 14px;
}
.text-info-user-desc, .text-info-user-publish-time {
  font-size: 14px;
  color: #777;
}
.header-actions {
  border: 1px solid green;
  color: green;
  text-align: center;
  width: 60px;
  height: 25px;
}
.header-actions.isFollowed {
  border: 1px solid #888;
  color: #888;
  width: 80px;
  text-align: center;
  height: 25px;
}

.header-actions span.follow-talk-user-btn {
  display: block;
  width: 100%;
  text-align: center;
  height: 25px;
  line-height: 25px;
  font-size: 14px;
  cursor: pointer;
}
.header-actions span.unfollow-talk-user-btn {
  display: block;
  width: 100%;
  text-align: center;
  height: 25px;
  line-height: 25px;
  font-size: 14px;
  cursor: pointer;
}
.talk-body {
}
.talk-body-text {
  padding: 10px 60px;
}
.talk-body-text.expansion {
  white-space:pre-line;
  overflow: hidden;
  -webkit-line-clamp: 4;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  box-sizing: content-box;
  margin: 0 60px;
  padding: 10px 0;
}
.talk-body-text span {
}
.overflow {
  position: relative;
}
.overflow::after {
  content: '';
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 25px;
  background: linear-gradient(rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.8)); /* 溢出部分那显示渐变 */
  pointer-events: none;
}
.talk-body-text-expand {
  padding-left: 60px;
}
.talk-body-text-expand span:nth-of-type(1) {
  display: block;
  /*padding-left: 70px;*/
  /*padding-top: 10px;*/
  font-size: 14px;
  margin-top: 5px;
  color: #5385ff;
  cursor: pointer;
  /*padding-top: 10px;*/
  /*margin-top: -20px;*/
  /*background-image: linear-gradient(rgba(255, 255, 255, .8), white);*/
  /*background: linear-gradient(yellow, green);*/
  /*mask-image: linear-gradient(yellow, green);*/
}
.talk-body-images {
  padding: 10px 60px 15px 60px;
  display: flex;
}
.talk-body-images img {
  display: block;
  width: 190px;
  height: 190px;
  border: 1px solid #999;
  cursor: zoom-in;
}
.talk-body-images img:nth-child(n + 2) {
  margin-left: 10px;
}
.talk-body-tags {
  padding: 0 60px 10px 60px;
}
.talk-body-tag {
  display: block;
  padding: 6px 8px;
  line-height: 10px;
  font-size: 14px;
  width: fit-content;
  height: 10px;
  text-align: center;
  border: 1px solid #5385ff;
  color: #5385ff;
  border-radius: 15px;
  cursor: pointer;
  transition: .3s;
}
.talk-body-tag:hover {
  border: 1px solid #6390ff;
  color: #6390ff;
}
.talk-operations {
  padding: 5px 0 2px 0;
  border-top: 1px solid #ccc;
  display: flex;
}
.operation-likes {
  cursor: pointer;
  text-align: center;
  color: #999;
  flex: 1;
}
.operation-likes.isLike {
  color: #0085ea;
}
.operation-comments {
  cursor: pointer;
  text-align: center;
  flex: 1;
  color: #999;
  border-left: 1px solid #ccc;
}
.operation-other {
  cursor: pointer;
  text-align: center;
  flex: 1;
  color: #999;
  border-left: 1px solid #ccc;
}
.operation-likes,
.operation-comments,
.operation-other {
  transition: .3s;
}
.operation-likes:hover,
.operation-comments:hover,
.operation-other:hover {
  color: #555;
}

.my-info-wrapper {
  border-top: 10px solid pink;
  width: 100%;
  margin-left: 10px;
  padding: 5px 15px;
  background-color: white;
  box-shadow: 0 7px 5px -5px #d9d9d9;
}
.my-info {
  height: 80px;
  display: flex;
  align-items: center;
}
.my-avatar {
  width: 64px;
  height: 64px;
}
.my-avatar img {
  width: 64px;
  height: 64px;
  border: 1px solid #999;
  border-radius: 50%;
}
.my-text-info {
  height: 80px;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  margin-left: 10px;
}
.my-text-info span:nth-child(1) {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}
.my-text-info span:nth-child(2) {
  font-size: 14px;
  color: #777;
}
.my-meta-list {
  margin-top: 10px;
  border-top: 1px solid #eee;
  height: 50px;
  display: flex;
  align-items: center;
}
.my-meta-item {
  flex: 1;
  height: 40px;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
  cursor: pointer;
}
.my-meta-item:nth-child(n + 2) {
  border-left: 1px solid #eee;
}
.my-meta-item span:nth-child(1) {
  font-size: 14px;
  color: #333;
}
.my-meta-item span:nth-child(2) {
  font-size: 12px;
  color: #333;
}
.notice-wrapper {
  border-top: 10px solid #0085ea;
  margin-top: 10px;
  margin-left: 10px;
  padding: 10px 15px;
  width: 100%;
  height: fit-content;
  background-color: #fff;
  box-shadow: 0 7px 5px -5px #d9d9d9;
  text-align: center;
}
.notice-title span {
  color: #777;
  font-weight: 700;
  font-size: 14px;
}
.notice-content p {
  font-size: 12px;
  font-weight: 700;
  color: #888;
}
.comment-list {
  padding: 5px;
}
.comment-item {
  display: flex;
  align-items: start;
  padding: 10px 0;
}

.comment-item:nth-child(n + 2) {
  border-top: 1px solid #f4f4f4;
}
.comment-item:nth-child(1) {
  border-top: 1px solid #e4e4e4;
}

.comment-user-avatar {
  margin: 5px 10px;
  width: 40px;
  height: 40px;
}
.comment-user-avatar img {
  width: 40px;
  height: 40px;
  border: 1px solid #999;
  border-radius: 50%;
}
.comment-infos {
  width: 100%;
  display: flex;
  flex-direction: column;
  min-height: 50px;
}
.comment-user-info {
  padding: 3px 5px;
  height: 16px;
  display: flex;
  justify-content: space-between;
}
.comment-user-nickname {
  text-decoration: none;
  line-height: 16px;
  font-size: 14px;
  color: #333;
  font-weight: 700;
}
.comment-user-nickname:hover {
  text-decoration: underline;
}
.comment-comment-time {
  margin-left: 10px;
  line-height: 20px;
  font-size: 12px;
  color: #888;
}
.comment-index {
  font-size: 12px;
  background-color: #ccc;
  display: inline-block;
  width: 20px;
  height: 12px;
  line-height: 12px;
  color: #aaa;
  font-weight: 700;
  text-align: center;
  border-radius: 8px;
}
.comment-content {
  padding-left: 5px;
  margin-top: 10px;
}
.comment-content span {
  font-size: 14px;
  color: #111;
}
.comment-content a {
  font-size: 14px;
  margin-right: 3px;
  color: #999;
  text-decoration: none;
}
.comment-content a:before {
  content: '@ ';
  color: #666;
  font-weight: 700;
}
.comment-content a:hover {
  text-decoration: underline;
}
.comment-like-btn,
.comment-dislike-btn,
.comment-reply-btn {
  cursor: pointer;
  font-size: 12px;
  color: #999;
  transition: .3s;
}

.comment-like-btn:hover,
.comment-dislike-btn:hover,
.comment-reply-btn:hover {
  color: #0085ea;
}

.comment-textarea-wrapper {
  width: 100%;
  display: flex;
  background-color: #fafbfc;
  padding: 10px 10px 0;
}
.comment-me-avatar {
  margin-right: 12px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.comment-me-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.comment-textarea-operation {
  width: 700px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.comment-textarea-operation .talk-comment-editor {
  min-height: 40px;
  background-color: white;
  box-sizing: border-box;
  resize: none;
  font-size: 14px;
  width: 100%;
  padding: 3px 5px;
  outline: 1px solid #f4f4f4;
}
.comment-textarea-operation .talk-comment-editor.active {
  outline: 1px solid #0085ea;
}
.comment-textarea-operation .talk-comment-editor:empty:before {
  content: '输入评论...';
  color: #999;
}
.post-comment-btn {
  margin-top: 5px;
}
.comment-operations {
  width: 100%;
  text-align: right;
}
.comment-length-tips {
  margin-top: 5px;
  margin-right: 10px;
  font-size: 14px;
  color: #999;
}
</style>
