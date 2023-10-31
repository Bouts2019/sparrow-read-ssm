<template>
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
</template>

<script>
export default {
  name: "NavBarTemplateUsage",
  data() {
    return {
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
  },
  mounted() {
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
      // let scrollTop = window.document.documentElement.scrollTop
      // let clientHeight = window.document.documentElement.clientHeight
      // let scrollHeight = window.document.documentElement.scrollHeight
      // if (scrollTop + clientHeight === scrollHeight) {
      //   this.articles.push(this.articles[this.articles.length - 1])
      // }
      // console.log("clientHeight: " + document.documentElement.clientHeight);
    })

  }
}
</script>

<style scoped>
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
</style>
