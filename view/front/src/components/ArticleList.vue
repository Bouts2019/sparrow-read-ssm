<template>
  <div>
    <ul class="article-list">
      <li class="article-wrapper" v-for="article in articles">
        <div class="article-infos">
          <!-- 表示文章前面的 > 文学 / 图书 -->
          <div class="article-nav">
            <!--          <i class="el-icon-arrow-right"/>-->

            <a href="" class="article-type-name">{{article.typeName}}</a>
            &nbsp;&nbsp;·
            <a href="" class="article-book-name">《{{article.bookName}}》</a>
          </div>
          <a class="article-title" :href="'/article/' + article.articleId" target="_blank">{{article.articleTitle}}</a>
          <p class="article-abstract">{{article.content}}...</p>
          <div class="meta-info-wrapper">
            <a class="meta-info meta-info-author-name" :href="'/user/' + article.userId" target="_blank"><i class="el-icon-user"></i>
              {{article.userName}}</a>
            <a class="meta-info meta-info-comment-count"><i class="el-icon-chat-dot-square"></i> 阅读 {{article.readCount}}</a>
            <a class="meta-info meta-info-favorite-count"><i class="el-icon-star-off"></i> 点赞{{article.likeCount}}</a>
            <a class="meta-info meta-info-like-count"><i class="el-icon-thumb"></i> 评论{{article.commentCount}}</a>
          </div>
          <div class="article-publish-time">
            <span> <i class="el-icon-time"></i> 发表于：{{formatArticleDate(new Date(article.publishTime))}} </span>
          </div>
        </div>
        <div class="article-img-wrapper">
          <img
            class="article-img"
            :src="article.picUrl"
            alt="pic">
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "ArticleList",
  methods: {
    formatArticleDate(date) {
      return this.dateFormat('yyyy-MM-dd hh:mm:ss', date)
    },
    dateFormat(fmt, date) {
      var o = {
        "M+" : date.getMonth()+1,                 //月份
        "d+" : date.getDate(),                    //日
        "h+" : date.getHours(),                   //小时
        "m+" : date.getMinutes(),                 //分
        "s+" : date.getSeconds(),                 //秒
        "q+" : Math.floor((date.getMonth()+3)/3), //季度
        "S"  : date.getMilliseconds()             //毫秒
      };
      if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
      }
      for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
          fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
      }
      return fmt;
    }
  },
  props: {
    articles: [Array]
  },
  watch: {
    articles: function (val) {
    }
  }
}
</script>

<style scoped>
.article-list {
  list-style: none;
  padding-left: 20px;
  padding-right: 20px;
}

.article-wrapper {
  width: 700px;
  margin: 20px 0;
  padding: 10px 20px 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-sizing: border-box;
  transition: .3s;
}

.article-wrapper:hover {
  box-shadow: rgba(0, 0, 0, .2) 0 1px 5px 0;
}

.article-wrapper:first-child {
  margin-top: 15px;
}

.article-wrapper:nth-child(n + 2) {
  border-top: 1px solid #eeeeee;
}

.article-title {
  font-size: 18px;
  color: #333;
  font-weight: 700;
}

.article-title:hover {
  text-decoration: underline;
}

.article-infos {
  width: 400px;
}

.article-infos a {
  text-decoration: none;
}

.article-abstract {
  font-size: 13px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
}

.meta-info-wrapper {
  font-size: 12px;
  color: #b4b4b4;
}

.meta-info-author-name {
  color: #b4b4b4;
}

.meta-info:nth-child(n + 2)::before {
  /*content: '|';*/
  content: '·';
  margin: 0 3px;
}

.article-img {
  width: 100px;
  height: 150px;
  border-radius: 5px;
}

.article-nav {
  border-left: 3px solid green;
  padding-left: 8px;
  font-size: 12px;
  color: #b4b4b4;
  margin-bottom: 7px;
}

.article-nav a {
  text-decoration: none;
  font-size: 12px;
  color: #999999;
}

.article-publish-time {
  margin-top: 10px;
  font-size: 11px;
  color: #b4b4b4;
}
</style>
