<template>
  <div>
    <ul v-if="talks.length === 0" class="no-talk">
      <li>
        <span>列表为空</span>
        <i class="el-icon-document"></i>
        <span>他 / 她 还没有发布过动态</span>
      </li>
    </ul>
    <ul v-else class="talk-list">
      <li class="talk-item" v-for="(talk, index) in talks">
        <div class="talk-header">
          <div class="header-infos">
            <div class="header-avatar">
              <img :src="talk.userAvatar" alt="">
            </div>
            <div class="header-text-infos">
              <div>
                <span class="text-info-user-nickname">{{talk.userNickname}}</span>
              </div>
              <div>
                <span class="text-info-user-desc">{{talk.userDesc}}</span> -
                <span class="text-info-user-publish-time">{{getDateString(talk.publishTime)}}</span>
              </div>
            </div>
          </div>
<!--          <div class="header-actions">-->
<!--            <span>关注</span>-->
<!--          </div>-->
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
<!--          <div class="operation-other">-->
<!--            <font-awesome-icon :icon="['far', 'share-square']"/>-->
<!--          </div>-->
        </div>
        <div class="talk-comments" v-if="talk.showComments">
          <div class="comment-textarea-wrapper">
            <div class="comment-me-avatar">
              <img :src="user.avatar" alt="">
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
                        <a :href="'/user/' + comment.userId" class="comment-user-nickname" v-text="comment.userNickname" target="_blank"></a>
                        <span class="comment-comment-time" v-text="comment.commentTime"></span>
                        </span>
                    <span>
                          <span class="comment-like-btn" style="margin-right: 10px;"> <font-awesome-icon :icon="['far', 'thumbs-up']"/> {{comment.likeCount}}</span>
                      <!--                          <span class="comment-dislike-btn"> <font-awesome-icon :icon="['far', 'thumbs-down']"/> </span>-->
                          <span class="comment-reply-btn" @click="replyTalkComment(comment.userId, comment.userNickname)">回复</span>
                          <span class="comment-index" v-text="commentIndex + 1"></span>
                        </span>
                  </div>
                  <div class="comment-content">
                    <a v-if="comment.replyUserId !== null" :href="'/user/' + comment.replyUserId" v-text="comment.replyUserName" target="_blank"></a>
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
</template>

<script>
import {formatDate} from "element-ui/lib/utils/date-util";
import {friendlyFormatTime} from "../apis/time";
import jwtDecode from "jwt-decode";

export default {
  name: "UserTalks",
  data() {
    return {
      me: {},
      isLoggin: false,
      user: {},
      talks: [],
      commentEditorFocus: true,
      myComment: {
        commentContent: '',
        replyUserId: null,
        replyUserNickname: null
      }
    }
  },
  props: {
    userId: [Number]
  },
  watch: {
    userId: function () {
      this.loadMyInfo()
      this.findTalks()
    }
  },
  methods: {
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
    loadMyInfo() {
      this.$http.all([
        this.$http.get('http://localhost:8080/sparrow/user/' + this.userId),
      ]).then(this.$http.spread((userInfo) => {
        this.user = userInfo.data.data
      }))
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
      this.myComment.userId = this.user.uid
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
    findTalks() {
      this.$http.get('http://localhost:8080/sparrow/talk/user/' + this.userId).then(res => {
        this.talks = res.data.data
        for (let i = 0; i < this.talks.length; i++) {
          this.talks[i].overflowed = this.talks[i].content.length > 100
          this.talks[i].expanded = false
          this.talks[i].showComments = false
          this.talks[i].comments = []
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
        console.log('show->')
        console.log(this.talks)
      })
    },
  },
  mounted() {
    if (localStorage.getItem("JWT_TOKEN") !== null) {
      this.me = jwtDecode(localStorage.getItem("JWT_TOKEN"))
      this.isLogging = true
    } else {
      this.me = null
      this.isLogging = false
    }
  }
}
</script>

<style scoped>
.talk-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.talk-item {
  margin: 10px 0;
  padding: 20px 20px 5px 20px;
  background-color: white;
  border-radius: 5px;
  transition: .3s;
}
.talk-item:nth-child(n + 2) {
  border-top: 1px solid #efefef;
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

.header-actions span {
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
  border-top: 1px solid #efefef;
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
  border-left: 1px solid #efefef;
}
.operation-other {
  cursor: pointer;
  text-align: center;
  flex: 1;
  color: #999;
  border-left: 1px solid #efefef;
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
  background-color: #efefef;
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


.no-talk {
  list-style: none;
  padding-left: 0;
}
.no-talk li {
  display: flex;
  height: 200px;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
}
.no-talk li span:nth-child(1) {
  font-size: 14px;
  color: #999;
}
.no-talk li i:nth-child(2) {
  font-size: 80px;
  color: #ccc;
}
.no-talk li span:nth-child(3) {
  color: #999;
}
</style>
