<template>
  <ul v-if="myFans.length === 0" class="no-fans">
    <li>
      <span>列表为空</span>
      <i class="el-icon-document"></i>
      <span>什么都没有，去写一篇文章获取粉丝？</span>
    </li>
  </ul>
  <ul v-else class="my-fans-list">
    <li class="my-fan-item" v-for="(myFan, index) in myFans">
      <div class="fan-infos">
        <div class="fan-avatar">
          <img :src="myFan.avatar" alt="" class="fan-avatar-img">
        </div>
        <div class="fan-info-text">
          <a class="info-text-nickname" :href="'/user/' + myFan.uid">
            {{myFan.nickname}}
            <span v-if="myFan.gender === 0" style="color: blue">
              <i class="el-icon-male"></i>
            </span>
            <span v-else style="color: deeppink">
              <i class="el-icon-female"></i>
            </span>
          </a>
          <span class="info-text-description">{{myFan.description}}</span>
        </div>
      </div>
      <div class="fan-operations">
        <span v-if="!followStatus[index]" class="follow-btn" @click="follow(index)">关注</span>
        <span v-else class="unfollow-btn" @click="unfollow(index)"><i class="el-icon-check"></i>&nbsp;已互关</span>
        <span class="send-message-btn">发送私信</span>
      </div>
    </li>
  </ul>
</template>

<script>
export default {
  name: "HomeMyFans",
  data() {
    return {
      myFans: [],
      followStatus: []
    }
  },
  props: {
    myId: [Number]
  },
  methods: {
    getMyFans() {
      return this.$http.get('http://localhost:8080/sparrow/follow/fans/' + this.myId)
    },
    convertToArray(ids) {
      return ids.map(id => 'ids=' + id).join('&')
    },
    findUsersByIds(ids) {
      return this.$http.get('http://localhost:8080/sparrow/user/ids?' + this.convertToArray(ids))
    },
    follow(index) {
      this.$http.post('http://localhost:8080/sparrow/follow/follow', {
        from: this.myId,
        to: this.myFans[index].uid
      }).then(res => {
        this.$notify.success({
          title: '关注成功',
          message: '关注UP主：' + this.myFans[index].nickname + '成功',
          offset: 50,
        })
        this.$set(this.followStatus, index, true)
      }).catch(err => {
        this.$notify.error({
          title: '关注异常',
          message: '关注UP主：' + this.myFans[index].nickname + '异常',
          offset: 50
        })
      })
    },
    unfollow(index) {
      this.$confirm('确定取消关注该up主吗？/(ㄒoㄒ)/~~', '确定么', {
        confirmButtonText: '取关ta!',
        cancelButtonText: '再想想',
        type: 'warning'
      }).then(() => {
        this.$http.post('http://localhost:8080/sparrow/follow/unfollow', {
          from: this.myId,
          to: this.myFans[index].uid
        }).then(res => {
          this.$notify.success({
            title: '取消关注成功',
            message: '取消关注UP主：' + this.myFans[index].nickname + '成功',
            offset: 50,
          })
          this.$set(this.followStatus, index, false)
        }).catch(err => {
          this.$notify.error({
            title: '取关异常',
            message: '取关UP主：' + this.myFans[index].nickname + '异常',
            offset: 50
          })
        })
      }).catch(() => {

      })
    },
  },
  watch: {
    myId: function () {
      let ids = []
      this.getMyFans().then(res => {
        ids = res.data.data
        if (ids.length !== 0) {
          this.findUsersByIds(ids).then(users => {
            this.myFans = users.data.data
            this.followStatus = []
            this.myFans.forEach(fan => {
              this.$http.get(`http://localhost:8080/sparrow/follow/isFollowed?from=${this.myId}&to=${fan.uid}`).then(status => {
                this.followStatus.push(status.data.data)
              })
            })
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.no-fans {
  list-style: none;
  padding-left: 0;
}
.no-fans li {
  display: flex;
  height: 200px;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
}
.no-fans li span:nth-child(1) {
  font-size: 14px;
  color: #999;
}
.no-fans li i:nth-child(2) {
  font-size: 80px;
  color: #ccc;
}
.no-fans li span:nth-child(3) {
  color: #999;
}
.my-fans-list {
  list-style: none;
  padding-left: 0;
}
.my-fan-item {
  height: 50px;
  padding: 0 20px 20px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.my-fan-item:nth-child(n + 2) {
  margin-top: 5px;
  padding: 20px;
  border-top: 1px solid #f4f4f4;
}
.fan-infos {
  display: flex;
  align-items: center;
}
.fan-avatar {
  height: 64px;
  width: 64px;
}
.fan-avatar-img {
  height: 64px;
  width: 64px;
  border: 1px solid #999;
  border-radius: 50%;
}
.fan-info-text {
  margin-left: 15px;
  height: 80px;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: end;
}
.info-text-nickname {
  font-size: 16px;
  font-weight: 700;
  color: #333;
  text-decoration: none;
}
.info-text-nickname:hover {
  text-decoration: underline;
}
.info-text-description {
  font-size: 12px;
  color: #777;
}
.fan-operations {
  display: flex;
  align-items: center;
}
.follow-btn {
  display: block;
  height: 30px;
  width: 80px;
  font-size: 14px;
  line-height: 30px;
  text-align: center;
  border: 1px solid #0085ea;
  color: #0085ea;
  cursor: pointer;
  margin-right: 10px;
}
.unfollow-btn {
  display: block;
  height: 30px;
  width: 80px;
  font-size: 14px;
  line-height: 30px;
  text-align: center;
  border: 1px solid #999;
  color: #999;
  cursor: pointer;
  margin-right: 10px;
}
.send-message-btn {
  display: block;
  height: 30px;
  width: 80px;
  font-size: 14px;
  line-height: 30px;
  text-align: center;
  border: 1px solid deeppink;
  color: deeppink;
  cursor: pointer;
}
</style>
