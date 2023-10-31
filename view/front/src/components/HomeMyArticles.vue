<template>
<div>
  <ul v-if="myArticles.length === 0" class="no-article">
    <li>
      <span>列表为空</span>
      <i class="el-icon-document"></i>
      <span>这里什么都没有，不如去发表文章？</span>
    </li>
  </ul>
  <ul v-else class="my-article-list">
    <li class="my-article-item" v-for="myArticle in myArticles">
      <div class="item-infos">
        <a :href="'/article/' + myArticle.id" class="article-title" target="_blank">
          {{myArticle.title}}
        </a>
        <div class="item-detail">
          <span class="article-publish-time">
            <i class="el-icon-time"></i>
            发布于: {{myArticle.publishTime}}
          </span>
            <span class="article-read-count">
            阅读量: {{myArticle.readCount}}
          </span>
            <span class="article-like-count">
            点赞数: {{myArticle.likeCount}}
          </span>
            <span class="article-comment-count">
            评论数: {{myArticle.commentCount}}
          </span>
        </div>
      </div>
      <div class="item-operation">
        <span class="article-status">
          <span v-if="myArticle.status === 0" style="color: green">已过审</span>
          <span v-else-if="myArticle.status === 1" style="color: orange">等待审核</span>
          <span v-else-if="myArticle.status === 2" style="color: darkred">被驳回</span>
        </span>
        <span class="item-actions">
          <span style="color: red">删除</span>
        </span>
      </div>
    </li>
  </ul>
</div>
</template>

<script>
export default {
  name: "HomeMyArticles",
  data() {
    return {
      myArticles: []
    }
  },
  props: {
    myId: [Number]
  },
  methods: {
    loadMyArticles() {
      this.$http.get('http://localhost:8080/sparrow/article/uid/' + this.myId).then(res => {
        this.myArticles = res.data.data
        console.log(this.myArticles)
        this.myArticles.sort((a, b) => b.id - a.id)
        // console.log(this.myArticles)
      })
    }
  },
  mounted() {
  },
  watch: {
    myId: function () {
      this.loadMyArticles()
    }
  }
}
</script>

<style scoped>
.no-article {
  list-style: none;
  padding-left: 0;
}
.no-article li {
  display: flex;
  height: 200px;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
}
.no-article li span:nth-child(1) {
  font-size: 14px;
  color: #999;
}
.no-article li i:nth-child(2) {
  font-size: 80px;
  color: #ccc;
}
.no-article li span:nth-child(3) {
  color: #999;
}
.my-article-list {
  list-style: none;
  padding-left: 0;
}
.my-article-item {
  border: 1px solid #eee;
  height: 40px;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: .3s;
}
.my-article-item:hover {
  border: 1px solid #aaa;
}
.item-infos {
}
.item-detail {
  margin-top: 10px;
}
.article-title {
  line-height: 18px;
  font-size: 18px;
  color: #333;
  font-weight: 700;
  text-decoration: none;
}
.article-title:hover {
  text-decoration: underline;
}
.article-publish-time,
.article-read-count,
.article-like-count,
.article-comment-count {
  line-height: 12px;
  font-size: 12px;
  color: #999;
}
.article-status {
  font-size: 14px;
  margin-right: 10px;
}
.item-actions span {
  font-size: 12px;
  cursor: pointer;
}
</style>
