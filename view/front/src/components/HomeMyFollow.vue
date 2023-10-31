<template>
<div>
  <ul v-if="myFollows.length === 0" class="no-follow">
    <li>
      <span>列表为空</span>
      <i class="el-icon-document"></i>
      <span>什么都没有，去关注一个UP主？</span>
    </li>
  </ul>
  <ul v-else class="my-follow-list">
    <li class="my-follow-item" v-for="(myFollow, index) in myFollows">
      <div class="follow-infos">
        <div class="follow-avatar">
          <img :src="myFollow.avatar" alt="" class="follow-avatar-img">
        </div>
        <div class="follow-info-text">
          <a class="info-text-nickname" :href="'/user/' + myFollow.uid">
            {{myFollow.nickname}}
            <span v-if="myFollow.gender === 0" style="color: blue">
              <i class="el-icon-male"></i>
            </span>
            <span v-else style="color: deeppink">
              <i class="el-icon-female"></i>
            </span>
          </a>
          <span class="info-text-description">{{myFollow.description}}</span>
        </div>
      </div>
      <div class="follow-operations">
<!--        <span class="follow-btn">关注</span>-->
        <span class="unfollow-btn" @click="unfollow(index)">取消关注</span>
        <span class="send-message-btn">发送私信</span>
      </div>
    </li>
  </ul>
</div>
</template>

<script>
export default {
  name: "HomeMyFollow",
  data() {
    return {
      myFollows: []
    }
  },
  props: {
    myId: [Number]
  },
  methods: {
    getMyFollows() {
      return this.$http.get('http://localhost:8080/sparrow/follow/' + this.myId)
    },
    convertToArray(ids) {
      return ids.map(id => 'ids=' + id).join('&')
    },
    findUsersByIds(ids) {
      return this.$http.get('http://localhost:8080/sparrow/user/ids?' + this.convertToArray(ids))
    },
    unfollow(index) {
      this.$confirm('确定取消关注该up主吗？/(ㄒoㄒ)/~~', '确定么', {
        confirmButtonText: '取关ta!',
        cancelButtonText: '再想想',
        type: 'warning'
      }).then(() => {
        this.$http.post('http://localhost:8080/sparrow/follow/unfollow', {
          from: this.myId,
          to: this.myFollows[index].uid
        }).then(res => {
          this.$notify.success({
            title: '取消关注成功',
            message: '取消关注UP主：' + this.myFollows[index].nickname + '成功',
            offset: 50,
          })
          this.myFollows.splice(index)
        }).catch(err => {
          this.$notify.error({
            title: '取关异常',
            message: '取关UP主：' + this.myFollows[index].nickname + '异常',
            offset: 50
          })
        })
      }).catch(() => {

      })
    },
  },
  created() {
  },
  mounted() {

  },
  watch: {
    myId: function () {
      let ids = []
      this.getMyFollows().then(res => {
        ids = res.data.data
        if (ids.length !== 0) {
          this.findUsersByIds(ids).then(users => {
            this.myFollows = users.data.data
            console.log(this.myFollows)
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.no-follow {
  list-style: none;
  padding-left: 0;
}
.no-follow li {
  display: flex;
  height: 200px;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
}
.no-follow li span:nth-child(1) {
  font-size: 14px;
  color: #999;
}
.no-follow li i:nth-child(2) {
  font-size: 80px;
  color: #ccc;
}
.no-follow li span:nth-child(3) {
  color: #999;
}
.my-follow-list {
  list-style: none;
  padding-left: 0;
}
.my-follow-item {
  height: 50px;
  padding: 0 20px 20px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.my-follow-item:nth-child(n + 2) {
  margin-top: 5px;
  padding: 20px;
  border-top: 1px solid #f4f4f4;
}
.follow-infos {
  display: flex;
  align-items: center;
}
.follow-avatar {
  height: 64px;
  width: 64px;
}
.follow-avatar-img {
  height: 64px;
  width: 64px;
  border: 1px solid #999;
  border-radius: 50%;
}
.follow-info-text {
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
.follow-operations {
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
