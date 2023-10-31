<template>
<div>
  <div class="recommend-author-wrapper">
    <div class="recommend-author-tool-tip">
      <div class="recommend-author-tool-tip-title">
        <h5>🎖️&nbsp;推荐作者</h5>
      </div>
      <div class="recommend-author-tool-tip-refresh">
        <a @click.prevent="refresh"><i :class="refreshing ? 'el-icon-loading' : 'el-icon-refresh'"></i>&nbsp;换一批</a>
      </div>
    </div>
    <div class="recommend-author-content-wrapper">
      <ul class="recommend-author-list">
        <li class="recommend-author-item" v-for="(recommendAuthor, index) in recommendAuthors">
          <div class="recommend-author-avatar">
            <img class="recommend-author-avatar-img" :src="recommendAuthor.avatar" alt="">
          </div>
          <div class="recommend-author-info">
            <a :href="'/user/' + recommendAuthor.id" class="recommend-author-nickname-span" v-text="recommendAuthor.nickname" target="_blank"></a>
            <span class="recommend-author-description-span" v-text="recommendAuthor.description"></span>
          </div>
          <div class="recommend-author-action" v-if="isLogging">
            <a v-if="!recommendAuthor.isFollowed" @click.prevent="followAuthor(index)"><i class="el-icon-plus"></i>&nbsp;关注</a>
            <a href="" style="color: #999; text-decoration: none; " @click.prevent="unfollowAuthor(index)" v-else>已关注</a>
          </div>
        </li>
      </ul>
    </div>
  </div>
</div>
</template>

<script>
import jwtDecode from "jwt-decode";

export default {
  name: "RecommendAuthor",
  data() {
    return {
      me: {},
      isLogging: false,
      refreshing: false,
      recommendAuthors: [
        {
          avatar: 'https://sf1-ttcdn-tos.pstatp.com/img/user-avatar/cbe45f9b6de3b6bf03caae1601994000~300x300.image',
          nickname: 'CodingStartup',
          description: 'Welcome to CodingStartup~'
        },
        {
          avatar: 'https://sf1-ttcdn-tos.pstatp.com/img/user-avatar/cbe45f9b6de3b6bf03caae1601994000~300x300.image',
          nickname: 'A',
          description: 'B'
        }
      ]
    }
  },
  methods: {
    unfollowAuthor(index) {
      if (parseInt(this.me.uid) === parseInt(this.recommendAuthors[index].id)) {
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
          to: this.recommendAuthors[index].id
        }).then(res => {
          this.$notify.success({
            title: '取消关注成功',
            message: '取消关注UP主：' + this.recommendAuthors[index].nickname + '成功',
            offset: 50,
          })
          this.recommendAuthors[index].isFollowed = false
          this.$set(this.recommendAuthors, index, this.recommendAuthors[index])
        }).catch(err => {
          this.$notify.error({
            title: '取关异常',
            message: '取关UP主：' + this.recommendAuthors[index].nickname + '异常',
            offset: 50
          })
        })
      }).catch(() => {

      })
    },
    followAuthor(index) {
      this.$http.post('http://localhost:8080/sparrow/follow/follow', {
        from: this.me.uid,
        to: this.recommendAuthors[index].id
      }).then(res => {
        this.$notify.success({
          title: '关注成功',
          message: '关注UP主：' + this.recommendAuthors[index].nickname + '成功',
          offset: 50,
        })
        this.recommendAuthors[index].isFollowed = true
        this.$set(this.recommendAuthors, index, this.recommendAuthors[index])
      }).catch(err => {
        this.$notify.error({
          title: '关注异常',
          message: '关注UP主：' + this.recommendAuthors[index].nickname + '异常',
          offset: 50
        })
      })
    },
    refreshFollowRelation() {
      for (let i = 0; i < this.recommendAuthors.length; i++) {
        let author = this.recommendAuthors[i]
        if (parseInt(author.id) === parseInt(this.me.uid)) {
          console.log('有自己')
          author.isFollowed = true
          this.$set(this.recommendAuthors, i, author)
        } else {
          this.$http.get('http://localhost:8080/sparrow/follow/isFollowed', {
            params: {
              from: this.me.uid,
              to: author.id
            }
          }).then(res => {
            author.isFollowed = res.data.data
            this.$set(this.recommendAuthors, i, author)
            console.log(res.data.data)
          })
        }
      }
      console.log(this.recommendAuthors)
    },
    refresh() {
      this.refreshing = true
      let tmpRecommendAuthors = []
      this.$http.get('http://localhost:8080/sparrow/recommend-author/random').then(res => {
        tmpRecommendAuthors = res.data.data
      })
      setTimeout(() => {
        this.refreshing = false
        this.recommendAuthors = tmpRecommendAuthors
        if (this.isLogging) this.refreshFollowRelation()
      }, 500)
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
    this.$http.get('http://localhost:8080/sparrow/recommend-author/random').then(res => {
      this.recommendAuthors = res.data.data
      if (this.isLogging) this.refreshFollowRelation()
    })
  }
}
</script>

<style scoped>
.recommend-author-wrapper {
  box-shadow: rgba(0,0,0,.1) 0 1px 8px 0;
  width: 340px;
  background-color: #f9f9f9;
  box-sizing: border-box;
  padding: 0 15px;
}
.recommend-author-tool-tip {
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}
.recommend-author-tool-tip-title h5 {
  color: #333;
  font-size: 16px;
  font-weight: 400;
}
.recommend-author-tool-tip-refresh a {
  color: #999;
  font-size: 14px;
  cursor: pointer;
}
.recommend-author-content-wrapper {

}
.recommend-author-list {
  padding-left: 10px;
  padding-bottom: 10px;
  list-style: none;
}
.recommend-author-item {
  height: 60px;
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}
.recommend-author-item:nth-child(n + 2) {
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #eee;
}
.recommend-author-avatar {
  flex: 1;
}
.recommend-author-avatar-img {
  width: 48px;
  height: 48px;
  border-radius: 48px;
  border: 1px solid #999;
}
.recommend-author-info {
  flex: 3;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  height: 50px;
  align-items: end;
}
.recommend-author-nickname-span {
  text-decoration: none;
  font-size: 14px;
  color: #333;
}
.recommend-author-nickname-span:hover {
  text-decoration: underline;
}
.recommend-author-description-span {
  font-size: 12px;
  color: #999;
}
.recommend-author-action {
  flex: 1;
}
.recommend-author-action a {
  font-size: 14px;
  color: green;
  cursor: pointer;
}

</style>
