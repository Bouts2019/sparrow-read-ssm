<template>
<div style="background-color: #f4f4f4; min-height: 100vh;">

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
      <div class="main-body" v-loading="isLoading">
        <div class="search-tips">
          <span class="search-count-tips">找到约 {{articles.length}} 条目</span>
        </div>
        <div class="search-filter-link">
          <a href="javascript:void(0);" @click.prevent="queryFilter('time')" :class="{'active-filter': (filterType === 'time' || filterType === '' || filterType === null || filterType == undefined)}">综合最新</a>
          <a href="javascript:void(0);" @click.prevent="queryFilter('read')" :class="{'active-filter': filterType === 'read'}">访问量最高</a>
          <a href="javascript:void(0);" @click.prevent="queryFilter('like')" :class="{'active-filter': filterType === 'like'}">点赞最多</a>
        </div>
        <div v-if="articles.length === 0 && !isLoading">
          <div class="no-result-item">
            <span>这里什么都没有</span>
            <i class="el-icon-document"></i>
            <span>什么都没有，不如换个关键词？</span>
          </div>
        </div>
        <div v-if="articles.length !== 0 && !isLoading" class="search-result">
          <ul class="search-result-list">
            <li class="search-result-item" v-for="article in articles">
              <div class="result-item-infos">
                <span><img :src="article.userAvatar === null ? 'http://m.imeitou.com/uploads/allimg/2020110210/da4rsi0mlkj-lp.jpg' : article.userAvatar" alt="" style="margin-right: 8px; width: 35px; height: 35px; border-radius: 50%; border: 1px solid #999; box-sizing: border-box;"></span> ·
                <span class="search-item-user-nickname"><a href="" v-html="article.userName" target="_blank" :href="'/user/' + article.userId"></a></span>
                <span><font-awesome-icon :icon="['far', 'eye']"/> {{article.readCount}}</span>
                <span><font-awesome-icon :icon="['far', 'thumbs-up']"/> {{article.likeCount}}</span>
                <span><font-awesome-icon :icon="['far', 'comment']"/> {{article.commentCount}}</span>
                <span v-html="'《' + article.bookName + '》'"></span>
              </div>
              <div class="result-item-article">
                <h2>
                  <a :href="'/article/' + article.articleId" v-html="article.articleTitle" target="_blank"></a>
                </h2>
                <p>{{article.content}}</p>
              </div>
              <div class="result-item-publish-time">
                <span>发布时间 {{formatArticleDate(new Date(article.publishTime))}}</span>
              </div>
            </li>
          </ul>
        </div>
      </div>
      <div class="sidebar">
        <div class="banner-wrapper">
          <h2> ✨ 推荐活动 ✨ </h2>
          <div class="banners">
            <a v-for="banner in banners" :href="banner.linkSrc" target="_blank">
              <img :src="banner.imgSrc" alt="" width="260" height="50" style="border-radius: 5px;">
            </a>
          </div>
        </div>
<!--        <div class="choose-type-options">-->
<!--          <ul class="choose-type-options-list">-->
<!--            <li class="search-type">-->
<!--              <div class="search-type-icon">-->
<!--                <i class="el-icon-time"></i>-->
<!--              </div>-->
<!--              <div class="search-type-text">-->
<!--                文章-->
<!--              </div>-->
<!--            </li>-->
<!--            <li class="search-type">-->
<!--              <div class="search-type-icon">-->
<!--                <i class="el-icon-time"></i>-->
<!--              </div>-->
<!--              <div class="search-type-text">-->
<!--              用户-->
<!--              </div>-->
<!--            </li>-->
<!--            <li class="search-type">-->
<!--              <div class="search-type-icon">-->
<!--                <i class="el-icon-time"></i>-->
<!--              </div>-->
<!--              <div class="search-type-text">-->
<!--              书名-->
<!--              </div>-->
<!--            </li>-->
<!--            <li class="search-type">-->
<!--              <div class="search-type-icon">-->
<!--                <i class="el-icon-time"></i>-->
<!--              </div>-->
<!--              <div class="search-type-text">-->
<!--              类别-->
<!--              </div>-->
<!--            </li>-->
<!--          </ul>-->
<!--        </div>-->
        <div class="hot-search-tips">
          <div class="hot-search-tips-title">
            <font-awesome-icon icon="search" class="tip-icon"/>
            <span>热门搜索</span>
          </div>
          <ul class="hot-search-tips-list">
            <li class="hot-search-item" v-for="(hotSearchItem, index) in hotSearchItems" @click="hotSearchClick(index)">
              <div class="hot-search-item-text">
                <span>{{hotSearchItem.value}}</span>
                <font-awesome-icon icon="fire-alt" class="tip-icon" style="color: red;"/>
              </div>
            </li>
          </ul>
        </div>
        <div class="history-tips">
          <div class="history-tips-title">
            <font-awesome-icon icon="history" class="tip-icon"/>
            <span>搜索历史</span>
          </div>
          <ul class="history-tips-list">
            <li class="history-item" v-for="(historyItem, index) in historyItems">
              <div class="history-item-text" @click="historySearch(index)">
                <font-awesome-icon icon="undo" class="tip-icon"/>
                <span>
                {{historyItem.value}}
                </span>
              </div>
              <div class="history-item-clear-btn" @click="clearHistoryItem(index)">
                <i class="el-icon-circle-close"></i>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import Navbar from "../components/Navbar";
export default {
  name: "Search",
  components: {Navbar},
  data() {
    return {
      articles: [],
      isLoading: true,
      notFound: false,
      // 页面滚动
      scrollAction: {x: 'undefined', y: 'undefined'},
      scrollDirection: '',
      lastScrollDirection: '',
      useDefaultNavClass: true,
      banners: [],
      historyItems: [],
      hotSearchItems: [],
      filterType: ''
    }
  },
  methods: {
    queryFilter(type) {
      // window.location.href = this.$route.path
      let queryString = this.$route.query.q
      window.location.href = `/search?q=${queryString}&t=${type}`
    },
    hotSearchClick(index) {
      let searchUrl = this.$router.resolve({
        path: '/search',
        query: {
          q: this.hotSearchItems[index].value
        }
      })
      window.open(searchUrl.href, '_blank')
    },
    loadHotSearch() {
      this.$http.get('http://localhost:8080/sparrow/hot-search/findAll').then(res => {
        if (res.data.status) {
          res.data.data.forEach(item => {
            this.hotSearchItems.push({
              title: item.title,
              value: item.value
            })
          })
          console.log(this.hotSearchItems)
          // this.hotArticlesLoading = false
        }
      })
    },
    historySearch(index) {
      let searchUrl = this.$router.resolve({
        path: '/search',
        query: {
          q: this.historyItems[index].value
        }
      })
      window.open(searchUrl.href, '_blank')
    },
    clearHistoryItem(index) {
      this.historyItems.splice(index, 1)
      localStorage.setItem('history', JSON.stringify(this.historyItems))
    },
    formatArticleDate(date) {
      return this.dateFormat('yyyy-MM-dd hh:mm:ss', date)
    },
    dateFormat(fmt, date) {
      let o = {
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
  },
  mounted() {
    this.filterType = this.$route.query.t
    this.$http.get('http://localhost:8080/sparrow/banner/all').then(res => {
      this.banners = res.data.data
    })
    let keyword = this.$route.query.q
    this.$http.get(`http://localhost:8080/sparrow/search`, {
      params: {
        q: this.$route.query.q,
        t: this.$route.query.t,
        d: this.$route.query.d
      }
    }).then(res => {
      this.articles = res.data.data
      console.log('articles')
      console.log(this.articles)
      if (this.articles === null) this.articles = []
      this.isLoading = false
      this.notFound = this.articles === null
    })
    let history = JSON.parse(localStorage.getItem('history'))
    if (history === null) history = []
    this.historyItems = history

    this.loadHotSearch()
  }
}
</script>

<style scoped>
.banner-wrapper {
  margin-top: 20px;
}
.banner-wrapper h2 {
  font-size: 16px;
  text-align: center;
}
.banner-wrapper a {
  padding-left: 20px;
  text-align: center;
}

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
  display: flex;
  justify-content: center;
  min-height: 90vh;
}
.content {
  box-shadow: 0 0 7px 3px #eee;
  margin-top: 10px;
  width: 960px;
  background-color: white;
  display: flex;
}
.main-body {
  width: 640px;
  padding: 10px 10px 10px 20px;
}
.main-body .search-tips {
  width: 100%;
  /*background-color: skyblue;*/
  height: 20px;
  line-height: 20px;
  font-size: 12px;
  color: #666;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}
.main-body .search-filter-link {
  height: 20px;
  padding: 10px 0;
  line-height: 20px;
  font-size: 14px;
  color: #666;
  border-bottom: 1px solid #eee;
}
.main-body .search-filter-link a {
  text-decoration: none;
  color: #666;
}
.main-body .search-filter-link a.active-filter {
  color: #0085ea;
}
.main-body .search-filter-link a:nth-child(n + 2)::before {
  content: '‧';
  margin: 0 10px;
}
.main-body .search-result-list {
  list-style: none;
  margin: 0;
  padding: 10px 0 0 0;
}
.search-result-item:first-child {
  padding-bottom: 20px;
}
.search-result-item:nth-child(n + 2) {
  padding: 20px 0;
  border-top: 1px solid #eee;
}
.result-item-infos {
  height: 32px;
  /*line-height: 32px;*/
  display: flex;
  align-items: center;
  /*line-height: 30px;*/
  font-size: 12px;
  color: #666;
}
.result-item-infos span {
  display: block;
}
.result-item-infos img {
  display: block;
}
.result-item-infos span:nth-child(2) {
  margin-left: 5px;
}
.result-item-infos span:nth-child(n + 3)::before {
  content: '‧';
  font-size: 14px;
  margin: 0 5px;
}
.result-item-article h2 {
  margin: 0;
}
.result-item-article h2 a {
  text-decoration: none;
  font-size: 16px;
  color: #333;
}
.result-item-article p {
  font-size: 12px;
  color: #555;
}
.result-item-publish-time {
  height: 20px;
  line-height: 20px;
  font-size: 12px;
  color: #666;
}


.sidebar {
  width: 320px;
}
.choose-type-options {
  border-bottom: 1px solid #f4f4f4;
}
.choose-type-options-list {
  list-style: none;
  margin-top: 20px;
  padding-left: 20px;
  padding-right: 20px;
}
.search-type {
  box-sizing: border-box;
  width: 100%;
  height: 50px;
  /*background-color: #eee;*/
  padding-left: 10px;
  font-size: 16px;
  line-height: 35px;
  color: #999;
  border-radius: 10px;
  display: flex;
  align-items: center;
  cursor: pointer;
}
.search-type:hover {
  background-color: #eee;
}
.search-type-icon {
  width: 35px;
  height: 35px;
  border-radius: 5px;
  background-color: #aaa;
  text-align: center;
  color: #f4f4f4;
  font-size: 18px;
}
.search-type-text {
  font-size: 16px;
  margin-left: 10px;
}


.hot-search-tips {
  margin-top: 10px;
  border-bottom: 1px solid #f4f4f4;
}
.hot-search-tips-list {
  list-style: none;
  margin: 10px 0;
  padding-left: 30px;
  padding-right: 30px;
}
.hot-search-tips-title {
  padding-left: 30px;
  height: 30px;
  line-height: 30px;
  font-size: 16px;
  color: #888;
}
.hot-search-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 10px;
  height: 20px;
  border-radius: 5px;
  cursor: pointer;
}
.hot-search-item:hover {
  background-color: #eee;
}
.hot-search-item-text {
  line-height: 20px;
  color: #888;
}

.tip-icon {
  color: #999999;
  font-size: 14px;
}


.history-tips {
  margin-top: 10px;
}
.history-tips-title {
  padding-left: 30px;
  height: 30px;
  line-height: 30px;
  font-size: 16px;
  color: #888;
}
.history-tips-list {
  list-style: none;
  margin: 10px 0;
  padding-left: 30px;
  padding-right: 30px;
}
.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 10px;
  height: 20px;
  border-radius: 5px;
  cursor: pointer;
}
.history-item:hover {
  background-color: #eee;
}
.history-item:hover .history-item-clear-btn {
  color: #888;
}
.history-item-text {
  line-height: 20px;
  color: #888;
}
.history-item-clear-btn {
  display: block;
  height: 20px;
  width: 20px;
  border-radius: 20px;
  color: white;
  text-align: center;
  line-height: 20px;
  font-size: 20px;
}

.no-result-item {
  padding-left: 0;
  display: flex;
  height: 200px;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
}
.no-result-item span:nth-child(1) {
  font-size: 14px;
  color: #999;
}
.no-result-item i:nth-child(2) {
  font-size: 80px;
  color: #ccc;
}
.no-result-item span:nth-child(3) {
  color: #999;
}
.search-item-user-nickname a {
  font-size: 14px;
  color: #333;
  font-weight: 700;
  text-decoration: none;
}
.search-item-user-nickname a:hover {
  text-decoration: underline;
}
.result-item-article {
  margin-top: 10px;
}
</style>
