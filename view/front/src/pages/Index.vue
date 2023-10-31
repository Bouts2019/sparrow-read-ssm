<template>
<div style="background-color:#ece9ef; min-height: 100vh;">
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
  <!-- filling -->
  <div style="height: 60px; width: 100%"/>
  <div class="content-wrapper">
    <div class="content">
      <div class="content-articles" >
        <div class="content-articles-loading" v-if="articleLoading" v-loading="articleLoading"></div>
        <article-list :articles="articles" @loadMore="loadMore"></article-list>
<!--                      v-if="!articleLoading"-->
        <!--      <div>加载中</div>-->
      </div>
      <!-- 这里是侧边栏 -->
      <div class="content-sidebar">
        <div class="banner-wrapper">
          <h2> 👍 推荐活动<span>最热门的活动都在这里</span></h2>
          <div class="banners">
            <a v-for="banner in banners" :href="banner.linkSrc" target="_blank">
              <img :src="banner.imgSrc" alt="" width="340" height="60" style="border-radius: 5px;">
            </a>
          </div>
        </div>
        <div style="margin: 15px 0;">
          <hot-articles></hot-articles>
        </div>
        <div style="margin: 5px 0;">
          <recommend-author/>
        </div>
        <div>
          <more-info/>
        </div>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import jwtDecode from "jwt-decode";
import Navbar from "../components/Navbar";
import ArticleList from "../components/ArticleList";
import HotArticles from "../components/HotArticles";
import MoreInfo from "../components/MoreInfo";
import RecommendAuthor from "../components/RecommendAuthor";

export default {
  name: "Index",
  components: {RecommendAuthor, MoreInfo, HotArticles, ArticleList, Navbar},
  data() {
    return {
      articleLoading: true,
      articles: [],
      user: {},
      keyword: '',
      hotArticles: [],
      hotArticlesLoading: true,
      // hotSearchBoxShow: false,
      hotWords: [],

      // banners
      banners: [],

      // 页面滚动
      scrollAction: {x: 'undefined', y: 'undefined'},
      scrollDirection: '',
      lastScrollDirection: '',
      useDefaultNavClass: true
    }
  },
  methods: {

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











    loadMore() {
      // console.log('clicked')
      // this.articles.push(this.articles[this.articles.length - 1])
    }
  },
  mounted() {
    this.$http.get('http://localhost:8080/sparrow/pv/').then(res => {})
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

      // 无限滚动方法
      // console.log("scrollHeight: " + document.documentElement.scrollHeight);
      let scrollTop = window.document.documentElement.scrollTop
      let clientHeight = window.document.documentElement.clientHeight
      let scrollHeight = window.document.documentElement.scrollHeight
      if (scrollTop + clientHeight === scrollHeight) {
        this.articles.push(this.articles[this.articles.length - 1])
      }
      // console.log("clientHeight: " + document.documentElement.clientHeight);
    })


    // TODO: token过期的判断
    // 首页文章

    // this.$http.get('http://localhost:8080/sparrow/article/all').then(res => {
    //   this.articles = res.data.data
    // })

    this.$http.get('http://localhost:8080/sparrow/banner/all').then(res => {
      this.banners = res.data.data
    })


    // 获取redis中的热门 ->文章<- 数据
    this.$http.get('http://localhost:8080/sparrow/hot-article/findAll').then(res => {
      if (res.data.status) {
        this.hotArticles = res.data.data
        // console.log(this.hotArticles)
        this.hotArticlesLoading = false
      }
    })
    // this.$router

    // 数据库 -> es

    setTimeout(() => {
      this.$http.get('http://localhost:8080/sparrow/es/all').then(res => {
        this.articleLoading = false
        this.articles = res.data.data.content
        console.log(this.articles)
      })
    }, 1000)


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


.content-wrapper {
  justify-content: center;
}
.content {
  width: 1115px;
  margin: 0 auto;
  display: flex;
}
.content-articles {
  background-color: white;
  height: fit-content;
  box-shadow: rgba(0,0,0,.1) 0 1px 8px 0;
}
.content-articles-loading {
  padding: 20px;
  width: 700px;
  height: 200px;
}
.content-sidebar {
  margin-left: 30px;
  display: flex;
  flex-direction: column;
}

.rolling-bar {
  box-shadow: 0 7px 5px -5px #d9d9d9;
}
.banners {

}
.banner-wrapper { }
.banner-wrapper h2 {
  color: #333;
}
.banner-wrapper h2 span {
  margin-left: 10px;
  font-size: 10px;
  font-weight: 400;
  color: #999;
}

</style>
