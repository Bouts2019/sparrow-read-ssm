<template>
  <div>
    <!-- Rolling Nav Bar -->
    <div class="clearfix rolling-bar" style="overflow: hidden; padding-top: 10px; position:fixed; width: 100%; z-index: 9999; background-color:white; min-width: 1920px; height: 50px;">
      <!-- rolling bar -->
      <div style="transition: .3s;" :style="{'margin-top': useDefaultNavClass ? 0 : '-51px'}">
        <navbar ></navbar>
        <!-- 上下滚动的事件监听ui -->
        <div style="display: flex; justify-content: center">
          <div style="width: 1200px;">
            <h1 style="display: inline; width: 100%; font-size: 32px;">
              {{article.title}}
            </h1>
          </div>
        </div>
      </div>
    </div>
    <div style="height: 60px; width: 100%"/>

    <div class="content-wrapper">
      <div class="content">
        <div class="content-main-body">
          <h1 class="article-title">{{article.title}}</h1>
          <div class="article-infos">
            <div class="left-part-infos">
              <img :src="user.avatar" alt="" style="width: 40px; height: 40px; border: 1px solid #e3e3e3; border-radius: 50%;">
            </div>
            <div class="right-part-infos">
              <div class="article-author-infos">
                <a href="" target="_blank">{{user.nickname}}</a>
                <i :class="user.gender === 0 ? 'el-icon-male' : 'el-icon-female' "
                   :style="user.gender === 0 ? {'color': 'blue'} : {'color': 'pink'}"></i>
                <span v-if="isLogging && !isFollowed" class="follow-btn" @click="follow">关注</span>
                <span v-else-if="isLogging && isFollowed" class="dis-follow-btn" @click="unfollow">取消关注</span>
              </div>
              <div class="article-other-infos">
                <span>发表日期: {{article.publishTime}}</span>
                &nbsp;
                <span>字数: {{getPureStringLength(article.content)}}</span>
                &nbsp;
                <span>阅读量: {{article.readCount}}</span>
              </div>
            </div>
          </div>
          <div class="article-body" v-html="article.content">
          </div>

          <div class="user-actions">
            <div class="like-action" :class="{ isLike }" @click="likeThisArticle()">
              <font-awesome-icon icon="thumbs-up" class="like-action-icon" style=""/>
            </div>
            <div class="other-like-count">
              <p> {{ article.likeCount }} 个人觉得很赞 >> </p>
            </div>
<!--            <div class="like-action">-->
<!--              <font-awesome-icon icon="thumbs-down" class="like-action-icon" style=""/>-->
<!--            </div>-->
          </div>
          <!-- 分割线 -->
          <div class="split-line"></div>
          <div class="reward-author-area">
            <div class="reward-tip">
              <span>觉得他 / 她写得不错？</span>
            </div>
            <div class="reward-btn">
              <span @click="rewardPanelVisible = true">打赏ta!</span>
            </div>
            <div class="reward-info">
              ❀ 赠人玫瑰，手有余香 ❀
            </div>
          </div>
          <div class="comment-area">
            <div class="part-title">
              <span>编写评论</span>
            </div>
            <div class="comment-area-editor-area">
              <div class="comment-textarea">
                <textarea v-model="myComment.content" name="comment" id="comment-area" cols="30" rows="5"></textarea>
              </div>
              <div class="comment-editor-action">
                <span v-text="myComment.replyUserId === null ? '发布' : '回复 @ ' + myComment.replyUserNickname" class="publish-comment-btn" @click="postComment"></span>
                <span v-text="myComment.replyUserId === null ? '清空' : '取消回复'" class="clear-comment-btn" @click="clearOrCancelReply"></span>
              </div>
            </div>

            <div class="part-title">
              <span>全部评论 💬 ({{comments.length}})</span>
            </div>

            <div class="comment-area-main-body">
              <!-- TODO: 开发评论模块，此处填充当没有评论时候的评论 -->
              <div v-if="comments.length === 0" class="no-comment-display">
                <p> 这里空空如也 </p>
                <p> 不如赶紧抢个沙发？😀 </p>
              </div>
              <div v-else class="" v-loading="isCommentLoading">
                <ul class="comment-list">
                  <li v-for="(comment, index) in comments" class="comment-item">
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
<!--                          <span class="comment-like-btn"> <font-awesome-icon icon="thumbs-up" class="comment-like-btn" style="font-size: 12px;"/> {{comment.likeCount}}</span>-->
<!--                          <span class="comment-dislike-btn"> <font-awesome-icon icon="thumbs-down" class="comment-dislike-btn" style="font-size: 12px;"/> </span>-->
                          <span class="comment-reply-btn" @click="replyComment(comment.userId, comment.userNickname)">回复</span>
                          <span class="comment-index" v-text="index + 1"></span>
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
          </div>

          <div class="article-footer">
            <div class="part-title">
              <span>推送给您 🍕</span>
            </div>
            <!-- TODO: 推荐文章：这里显示没有推荐时候的填充 -->
            <div v-if="randomArticles.length > 0" class="random-articles">
              <div class="random-article" v-for="randomArticle in randomArticles">
                <h2><a :href="'/article/' + randomArticle.id" target="_blank">{{randomArticle.title}}</a></h2>
                <p>{{randomArticle.content}}</p>
              </div>
            </div>
            <div v-else class="no-recommend-display">
              <p>还没有推荐的文章哦~ 😅</p>
            </div>
          </div>
        </div>
        <div class="content-sidebar">
          <div class="content-sidebar-infos">
            <div class="content-sidebar-item article-user-infos">
              <div class="sidebar-part-title">
                <p>关于作者</p>
              </div>
              <div class="sidebar-user-main-body">
                <div class="sidebar-user-main-infos">
                  <div class="sidebar-user-main-avatar">
                    <img :src="user.avatar" alt=""
                         style="width: 60px; height: 60px; border: 1px solid #777; border-radius: 50%;">
                  </div>
                  <div class="sidebar-user-main-text">
                    <a href="" target="_blank">{{user.nickname}}</a>
                    <span class="user-desc">个人简介：{{user.description}}</span>
                    <span class="user-reg-time"><i class="el-icon-time"></i>入站时间：{{user.regTime}}</span>
                  </div>
                </div>
                <div v-if="isLogging" class="sidebar-user-main-actions">
                  <span v-if="!isFollowed" class="sidebar-follow-btn" @click="follow">关注</span>
                  <span v-else class="sidebar-dis-follow-btn" @click="unfollow">取消关注</span>
<!--                  <span class="sidebar-send-message-btn">发送私信</span>-->
                </div>
              </div>
            </div>
            <div class="sidebar-split-line"></div>
            <div class="content-sidebar-item article-book-author-infos">
              <div class="sidebar-part-title">
                <p> {{bookAuthor.name}} >> </p>
              </div>
              <div class="sidebar-book-author-info">
                <p>{{bookAuthor.desc}}</p>
              </div>
            </div>
            <div class="sidebar-split-line"></div>
            <div class="content-sidebar-item article-book-infos">
              <div class="sidebar-part-title">
                <p> {{book.title}} >> </p>
              </div>
              <div class="sidebar-book-info-wrapper">
                <div class="sidebar-book-img">
                  <!-- TODO::  图片比例 -->
                  <img :src="book.imgUrl" :alt="book.title" style="width: 120px; height: 150px;">
<!--                  <el-rate-->
<!--                    :title="'评分: ' + bookScore + ' 点击进入评分详情'"-->
<!--                    v-model="bookScore"-->
<!--                    :disabled="true"-->
<!--                    :colors="colors">-->
<!--                  </el-rate>-->
                </div>
                <div class="sidebar-book-text-wrapper">
                  <span>书名：{{book.title}}</span>
                  <span>作者：{{bookAuthor.name}}</span>
                  <span>出版社：{{express.name}}</span>
                  <span>出版时间：{{book.expressDate}}</span>
                  <span>分类：{{bookType.name}}</span>
                  <span>ISBN：{{book.isbn}}</span>
                  <span class="overflow" :title="book.description">
                    简介:
                    <a href="" target="_blank">
                      {{book.description}}
                    </a>
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog
      title="打赏"
      :visible.sync="rewardPanelVisible"
      width="50%">
      <div class="reward-btn-wrapper">
        <div id="one-coin" class="reward-btn-one" @click="reward(2)">
          <span style="font-size: 42px; display: block">打赏</span>
          <span style="font-size: 42px; display: block" >💰</span>
          <span style="font-size: 42px; display: block">1 枚银币</span>
        </div>
        <div id="two-coin" class="reward-btn-two" @click="reward(2)">
          <span style="font-size: 42px; display: block">打赏</span>
          <span style="font-size: 42px; display: block" >💰💰</span>
          <span style="font-size: 42px; display: block">2 枚银币</span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
  </span>
    </el-dialog>
  </div>
</template>

<script>
import Navbar from "../components/Navbar";
import jwtDecode from "jwt-decode";
import {formatDate} from "element-ui/lib/utils/date-util";
import {friendlyFormatTime} from "../apis/time";
export default {
  name: "Article",
  components: {Navbar},
  data() {
    return {
      rewardPanelVisible: false,
      me: {},
      isLogging: false,
      article: {},
      isSelf: false,
      isFollowed: true,
      isLike: false, // 赞过这篇文章
      user: {
        avatar: 'http://m.imeitou.com/uploads/allimg/2020110210/da4rsi0mlkj-lp.jpg'
      },
      book: {},
      // TODO: change here
      bookScore: 3.4,
      bookAuthor: {},
      express: {},
      bookType: {},
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'],  // 等同于 { 2: '#99A9BF', 4: { value: '#F7BA2A', excluded: true }, 5: '#FF9900' }

      // 页面滚动
      scrollAction: {x: 'undefined', y: 'undefined'},
      scrollDirection: '',
      lastScrollDirection: '',
      useDefaultNavClass: true,

      // comment
      // comment: {
      //   userId: this.me.id,
      //   replay_userId: null,
      // }
      comments: [
        {
          userId: 0,
          userNickname: '',
          userAvatar: '',
          commentTime: '',
          commentContent: '',
          replyUserId: 0,
          replyUserName: ''
        },
      ],
      myComment: {
        content: '',
        replyUserId: null,
        replyUserNickname: null
      },
      isCommentLoading: false,
      randomArticles: []
    }
  },
  methods: {
    reward(number) {
      let obj = {
        from: this.me.uid,
        to: this.user.uid,
        number
      }
      this.$http.post('http://localhost:8080/sparrow/reward/reward', obj).then(res => {
        this.$alert('打赏成功', '成功', {
          type: 'success',
          callback: action => {
            this.rewardPanelVisible = false
          }
        })
      })
    },
    // 清空或者取消评论
    clearOrCancelReply() {
      if (this.myComment.replyUserId !== null) {
        this.myComment.replyUserId = null
        this.myComment.replyUserNickname = null
      } else {
        this.myComment.content = ''
      }
    },
    // 回复评论
    replyComment(id, nickname) {
      this.myComment.replyUserId = id
      this.myComment.replyUserNickname = nickname
    },
    // 发表评论 / 回复某人
    postComment() {
      if (this.myComment.content === '') {
        this.$message({
          message: '内容不能为空',
          offset: 100
        })
        return
      }
      this.myComment.userId = this.me.uid
      this.myComment.articleId = this.article.id
      this.$http.post('http://localhost:8080/sparrow/comment/one', this.myComment).then(res => {
        this.isCommentLoading = false
        this.$message({
          type: 'success',
          message: '评论成功！',
          offset: 80
        })
        this.comments.push({
          userId: this.me.uid,
          userNickname: this.me.nickname,
          userAvatar: this.me.avatar,
          commentTime: friendlyFormatTime(formatDate(new Date(), 'yyyy-MM-dd HH:mm:ss')),
          commentContent: this.myComment.content,
          replyUserId: this.myComment.replyUserId,
          replyUserName: this.myComment.replyUserNickname
        })
        this.myComment.content = ''
        this.myComment.replyUserId = null
        this.myComment.replyUserNickname = null
      })
    },
    // 关注
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
    // 纯文字长度的获取
    getPureStringLength(str) {
      if (str !== undefined) {
        // TODO 这里用这种方式判断是不是为undefined，应该有更好的方式
        return str.replace(/<[^>]+>/g, '').length
      } else {
        return 0
      }
      // return str.replace(/(^\s*)|(\s*$)/g, "").length
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
    refreshComments() {
      this.isCommentLoading = true
      this.$http.get('http://localhost:8080/sparrow/comment/all/' + this.article.id).then(res => {
        this.comments = res.data.data
        // console.log(this.comments)
        this.comments.map(comment => {
          let dateString = formatDate(new Date(comment.commentTime), 'yyyy-MM-dd HH:mm:ss')
          comment.commentTime =  friendlyFormatTime(dateString)
          this.$http.get('http://localhost:8080/sparrow/user/' + comment.userId).then(res => {
            comment.userNickname = res.data.data.nickname
            comment.userAvatar = res.data.data.avatar
            this.isCommentLoading = false
          })
          if (comment.replyUserId !== null) {
            this.$http.get('http://localhost:8080/sparrow/user/' + comment.replyUserId).then(res => {
              comment.replyUserName = res.data.data.nickname
            })
          }
        })
      })
    },
    likeThisArticle() {
      if (this.isLike) {
        this.$confirm('确认取消点赞么╰(*°▽°*)╯', 'sure?', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(res => {
          this.$http.post('http://localhost:8080/sparrow/article/toggle-like', {uid: this.me.uid, aid: this.article.id}).then(_ => {
            this.$notify({
              type: 'info',
              title: '取消点赞成功༼ つ ◕_◕ ༽つ',
              offset: 50
            })
            this.isLike = !this.isLike
            this.article.likeCount--
          })
        }).catch(res => {

        })
      } else {
        this.$http.post('http://localhost:8080/sparrow/article/toggle-like', {uid: this.me.uid, aid: this.article.id}).then(_ => {
          this.$notify({
            type: 'success',
            title: '点赞成功',
            offset: 50
          })
          this.isLike = !this.isLike
          this.article.likeCount++
        })
      }
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
    // 获取推荐
    this.$http.get('http://localhost:8080/sparrow/article/random', {
      params: {
        size: 3
      }
    }).then(res => {
      this.randomArticles = []
      console.log(res.data.data)
      res.data.data.forEach(item => {
        if (item !== null) this.randomArticles.push(item)
      })
    })
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

    // 增加阅读量
    this.$http.get('http://localhost:8080/sparrow/article/read-count/' + this.$route.params.id).then(res => {
      console.log('read count: ')
      console.log(res)
    })

    // TODO：大量信息的获取建议改为使用redis
    // TODO: 这里的层级关系
    this.$http.get('http://localhost:8080/sparrow/article/' + this.$route.params.id).then(res => {
      this.article = res.data.data
      document.title = this.article.title + ' - 麻雀阅读'
      // 是否喜欢这篇文章
      if (this.isLogging) {
        this.$http.get('http://localhost:8080/sparrow/article/is-like', { params: {uid: this.me.uid, aid: this.article.id}}).then(res => {
          this.isLike = res.data.data
        })
      }
      // 获取评论
      // TODO: 懒加载
      this.refreshComments()
      // 后端安全问题，如果返回密码
      this.$http.get('http://localhost:8080/sparrow/user/sec/' + this.article.userId).then(findUserRes => {
        this.user = findUserRes.data.data
        // 如果没登陆就不获取
        if (this.me !== null) {
          // 如果是自己就不判断了
          this.isSelf = this.user.uid === parseInt(this.me.uid)
          // TODO: 不是自己，关注信息的获取
          if (!this.isSelf) {
            this.$http.get(`http://localhost:8080/sparrow/follow/isFollowed?from=${this.me.uid}&to=${this.user.uid}`).then(res => {
              this.isFollowed = res.data.data
            })
          }
        }
      })
      this.$http.get('http://localhost:8080/sparrow/book/' + this.article.bookId).then(findBookRes => {
        this.book = findBookRes.data.data
        this.$http.get('http://localhost:8080/sparrow/author/' + this.book.authorId).then(findBookAuthorRes => {
          this.bookAuthor = findBookAuthorRes.data.data
        })
        this.$http.get('http://localhost:8080/sparrow/type/' + this.book.typeId).then(findTypeRes => {
          this.bookType = findTypeRes.data.data
        })
        this.$http.get('http://localhost:8080/sparrow/express/' + this.book.expressId).then(findExpressRes => {
          this.express = findExpressRes.data.data
        })
      })
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
  display: flex;
  justify-content: center;
  background-color: #f4f4f4;
}
.content {
  background-color: #f4f4f4;
  margin-top: 10px;
  width: 1200px;
  padding: 10px 30px;
  display: flex;
  justify-content: space-between;
}
.content-main-body {
  padding: 0 30px;
  background-color: white;
  width: 785px;
  /*box-shadow: 0 7px 5px -5px #d9d9d9;*/
  /* TODO: 这个外阴影可能需要微调一下 */
  box-shadow: 0 0 3px 0 #d9d9d9;
}
.content-sidebar {
  width: 335px;
  /*padding: 20px;*/
  /*background-color: purple;*/
  /*display: flex;*/
  /*flex-direction: column;*/
  /*height: fit-content;*/
}
.content-sidebar-infos {
  /*width: 100%;*/
  padding: 20px;
  background-color: white;
  /* TODO: 这个外阴影也可能需要微调一下 */
  box-shadow: 0 0 3px 0 #d9d9d9;
  /*height: fit-content;*/
}
.content-sidebar-item {
  width: 100%;
}
.content-sidebar-item:nth-child(n + 2) {
  margin: 10px 0;
}

.article-title {
  font-size: 30px;
  font-weight: 700;
}

/*<div class="article-infos">*/
/*                          <div class="left-part-infos">*/
.article-infos {
  display: flex;
  align-items: center;
}
.left-part-infos {
  width: 40px;
  height: 40px;
}
.right-part-infos {
  margin-left: 10px;
}
.right-part-infos a {
  text-decoration: none;
  font-size: 16px;
  color: #333;
}
.right-part-infos .article-other-infos {
  margin-top: 3px;
  font-size: 10px;
  color: #999999;
}
.follow-btn {
  display: inline-block;
  width: 40px;
  height: 20px;
  line-height: 20px;
  font-size: 12px;
  border: 1px solid #308db1;
  color: #6464ff;
  text-align: center;
  cursor: pointer;
}
.dis-follow-btn {
  display: inline-block;
  width: 54px;
  height: 20px;
  line-height: 20px;
  font-size: 12px;
  border: 1px solid #aaa;
  color: #aaa;
  text-align: center;
  cursor: pointer;
}
.article-body {
  margin-top: 15px;
  font-size: 16px;
  line-height: 1.8;
}


.user-actions {
  margin-top: 40px;
  padding: 0 10px 0 0;
  display: flex;
  align-items: center;
  /*background-color: skyblue;*/
}
.other-like-count {
  margin-left: 10px;
  font-size: 16px;
  color: #888;
}
.like-action {
  margin: 0 0 0 10px;
  cursor: pointer;
  height: 22px;
  width: 22px;
  border: 1px solid #888;
  display: inline-block;
  padding: 8px;
  border-radius: 50%;
}
.like-action.isLike {
  border: 1px solid #0085ea;
}
.like-action.isLike .like-action-icon {
  color: #0085ea;
}
.like-action-icon {
  font-size: 22px;
  transition: .3s;
  color: #ccc;
}
.like-action-icon:hover {
  color: #aaa;
}
.split-line {
  width: 800px;
  height: 1px;
  margin: 10px 0;
  background-color: #ccc;
}

.reward-author-area {
  margin-top: 20px;
  width: 100%;
  height: 170px;
  background-color: #f8f8f8;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}
/*TODO: 详细分层，span范围有点大*/
.reward-btn span {
  display: inline-block;
  width: 100px;
  height: 40px;
  text-align: center;
  font-size: 16px;
  line-height: 40px;
  border: 1px solid #5385ff;
  color: #0090ff;
  cursor: pointer;
  transition: .3s;
}
.reward-btn span:hover {

  color: #9125ff;
}

.comment-area {
  margin-top: 20px;
}

.part-title {
  display: flex;
  align-items: center;
  margin: 30px 0;
  height: 30px;
}
.part-title span {
  border-left: 5px solid #0085ea;
  line-height: 30px;
  font-size: 18px;
  padding-left: 10px;
  color: #777;
}

.comment-area-editor-area {
  display: flex;
  flex-direction: column;

  align-items: center;
  /*justify-content: center;*/
}

.comment-area textarea {
  border: 1px solid #ddd;
  outline-style: none;
  color: #333;
  font-size: 13px;
  padding: 10px;
  resize: none;
  width: 720px;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
}
.comment-editor-action {
  display: flex;
  width: 100%;
  justify-content: center;
  justify-items: right;
  margin: 10px 0;
}
.comment-editor-action .clear-comment-btn,
.comment-editor-action .publish-comment-btn {
  cursor: pointer;
  display: block;
  min-width: 38px;
  height: 13px;
  line-height: 13px;
  font-size: 14px;
  border: 1px solid #888;
  padding: 8px;
  color: #777;
  text-align: center;
  margin: 0 3px;
  transition: .3s;
}
.comment-editor-action .clear-comment-btn:hover {
  background-color: #888;
  color: white;
}
.comment-editor-action .publish-comment-btn:hover {
  color: #427389;
  border: 1px solid #a3d7fa;
  background-color: #b4e0fd;
}


.no-comment-display {
  height: 150px;
  width: 100%;
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: #999;
  font-size: 20px;
}
/*.no-comment-display p {*/
/*  display: block;*/
/*}*/
.no-recommend-display {
  height: 150px;
  width: 100%;
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: #999;
  font-size: 20px;
}


.sidebar-part-title {
  border-left: 5px solid #0085ea;
  height: 28px;
  padding-left: 10px;
}
.sidebar-part-title a {
  text-decoration: none;
  font-size: 16px;
  color: #333;
}
.sidebar-part-title p {
  margin: 0;
  line-height: 28px;
  font-size: 16px;
  font-weight: 700;
  color: #777;
}

.article-user-infos {
  width: 100%;
  /*background-color: pink;*/
}
.article-book-infos {
  width: 100%;
  /*background-color: blue;*/
}
.article-book-author-infos {
  width: 100%;
  /*background-color: green;*/
}
.sidebar-split-line {
  width: 100%;
  height: 1px;
  margin: 10px 0;
  background-color: #eee;
}

.sidebar-user-main-body {
  padding: 10px 0;
  margin-top: 10px;
  display: flex;
  flex-direction: column;
}
.sidebar-user-main-infos {
  display: flex;
  align-items: center;
  /*background-color: skyblue;*/
}
.sidebar-user-main-text {
  margin-left: 10px;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
}
.sidebar-user-main-text a {
  cursor: pointer;
  text-decoration: none;
  font-size: 16px;
  line-height: 16px;
  color: #888888;
  transition: .3s;
}
.sidebar-user-main-text a:hover {
  color: #777;
}
.user-desc {
  font-size: 14px;
  margin: 5px 0;
  color: #999;
}
.user-reg-time {
  font-size: 14px;
  color: #999;
}
.sidebar-user-main-actions {
  margin-top: 15px;
  display: flex;
  justify-content: space-evenly;
}
.sidebar-user-main-actions .sidebar-follow-btn {
  display: block;
  width: 150px;
  height: 30px;
  border: 1px solid #3333fd;
  color: #2525ff;
  font-size: 14px;
  line-height: 30px;
  text-align: center;
  cursor: pointer;
}
.sidebar-user-main-actions .sidebar-dis-follow-btn {
  display: block;
  width: 150px;
  height: 30px;
  border: 1px solid #aaa;
  color: #aaa;
  font-size: 14px;
  line-height: 30px;
  text-align: center;
  cursor: pointer;
}
.sidebar-user-main-actions .sidebar-send-message-btn {
  display: block;
  width: 100px;
  height: 30px;
  border: 1px solid #ff637e;
  color: #ff3263;
  font-size: 14px;
  line-height: 30px;
  text-align: center;
  cursor: pointer;
}

.sidebar-book-info-wrapper {
  margin-top: 10px;
  display: flex;
}
.sidebar-book-text-wrapper {
  padding-left: 7px;
  display: flex;
  flex-direction: column;
  width: 180px;
}
.sidebar-book-text-wrapper span {
  line-height: 1.5;
  /*background-color: skyblue;*/
  font-size: 14px;
  color: #555;
}
.sidebar-book-text-wrapper a {
  text-decoration: none;
  color: #555;
}
.overflow {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sidebar-book-author-info p {
  line-height: 1.5;
  color: #333;
  font-size: 14px;
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

.comment-user-avatar {
  margin: 5px 10px;
  width: 50px;
  height: 50px;
}
.comment-user-avatar img {
  width: 50px;
  height: 50px;
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

.random-articles {
  display: flex;
  justify-content: space-around;
}
.random-article {
  width: 220px;
  border: 1px solid #f4f4f4;
  height: 240px;
  box-sizing: border-box;
  padding: 20px;
  border-radius: 5px;
  transition: .3s;
  overflow: hidden;
  text-overflow: ellipsis;
}
.random-article:hover {
  border: 1px solid #999;
}
.random-article h2 {
  margin: 0;
  padding: 0;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  /*text-overflow: ellipsis;*/
}
.random-article p {
  font-size: 14px;
  display: block;
  overflow: hidden;
  line-height: 1.5;
  height: 152px;
  color: #777;
}
.random-article a {
  font-size: 22px;
  font-weight: 700;
  color: #333;
  text-decoration: none;
}
.reward-btn-wrapper {
  height: 400px;
  overflow: auto;
  padding: 0 10px;
  display: flex;
  justify-content: space-around;
}
.reward-btn-one {
  cursor: pointer;
  height: 300px;
  width: 300px;
  display: flex;
  justify-content: space-around;
  flex-direction: column;
  border-radius: 10px;
  align-items: center;
  border: 1px solid #f4f4f4;
  transition: .3s;
}
.reward-btn-two {
  cursor: pointer;
  height: 300px;
  width: 300px;
  display: flex;
  justify-content: space-around;
  flex-direction: column;
  border-radius: 10px;
  align-items: center;
  border: 1px solid #f4f4f4;
  transition: .3s;
}
.reward-btn-one:hover,
.reward-btn-two:hover {
  border: 1px solid #999;
}
</style>
