<template>
<div>
  <el-card class="box-card">
    <span> ✨ 热门文章 <span style=" margin-left: 10px; color: #b4b4b4; font-size: 10px; vertical-align: baseline">编辑推荐的热门文章</span></span>
    <el-divider></el-divider>
    <ul class="hot-article-list">
      <li class="hot-article" v-for="hotArticle in hotArticles">
        <font-awesome-icon icon="burn" style="color: red;"/>
        <span style="margin-left: 3px; display:inline-block; width: 14px; text-align: center; height: 14px; font-size: 12px; line-height: 14px; border: 1px dotted red; border-radius: 2px; color: #ffb700;">热</span>
        <a target="_blank" :href="'/article/' + hotArticle.id" v-text="hotArticle.title"></a>
      </li>
    </ul>
  </el-card>
</div>
</template>

<script>
export default {
  name: "HotArticles",
  data() {
    return {
      hotArticles: [
      ]
    }
  },
  mounted() {
    this.$http.get('http://localhost:8080/sparrow/hot-article/findAll').then(res => {
      this.hotArticles = res.data.data
    })
  }
}
</script>

<style scoped>

.text {
  font-size: 14px;
}

.item {
  padding: 18px 0;
}

.box-card {
  box-shadow: rgba(0,0,0,.1) 0 1px 8px 0;
  width: 340px;
}
.hot-article-list {
  padding: 0 0 0 10px;
  list-style: none;
}
.hot-article {
  margin: 10px 0;
}
.hot-article a {
  margin-left: 5px;
  text-decoration: none;
  font-size: 15px;
  color: #555;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
}
</style>
