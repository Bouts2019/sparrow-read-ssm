<template>
<div style="background-color: #f4f4f4; ">
  <!-- Rolling Nav Bar -->
  <div class="clearfix rolling-bar" style="overflow: hidden; padding-top: 10px; position:fixed; width: 100%; z-index: 9999; background-color:white; min-width: 1920px; height: 50px;">
    <!-- rolling bar -->
    <div style="transition: .3s;" :style="{'margin-top': useDefaultNavClass ? 0 : '-51px'}">
      <navbar :isWritingPage="true"></navbar>
      <!-- 上下滚动的事件监听ui -->
      <div style="text-align: center">
        <el-button type="text" :plain="true" :round="true">{{article.title}}</el-button>
      </div>
    </div>
  </div>
  <div style="height: 60px; width: 100%"/>
  <div class="content-wrapper">
    <div class="editor-form-wrapper">
<!--      {{article}}-->
      <form autocomplete="off">
        <input type="text" name="title" placeholder="文章标题" v-model="article.title" class="article-title">
        <Tinymce v-model="article.content" :disabled="disabled" ref="editor">
        </Tinymce>
        <div class="options-area">
          <div class="feedback-options">
            <a href="#">出现问题？点我反馈</a>
          </div>
          <div style="padding-top: 8px; box-sizing: border-box; text-align: right;">
            <el-upload
              style="display: inline-block"
              action="http://localhost:8080/sparrow/article/image"
              :show-file-list="false"
              :on-success="imageUploadSuccessfully">
              <span v-if="article.picUrl !== ''">
                <span>预览效果（点击更换图片）</span>
                <br>
                <img :src="article.picUrl" class="upload-images" style="width: 100px; height: 150px;">
              </span>
              <span v-else style="margin-top: 100px; font-size: 14px; color: #999;">添加图片:（默认使用书籍封面）</span>
            </el-upload>
          </div>
          <div>
            <span style="color: #888888; font-size: 14px;">搜索书籍（必须是已经读过的书）：</span>
            <el-autocomplete
              popper-class="my-autocomplete"
              v-model="choseBook"
              placement="top"
              :fetch-suggestions="querySearch"
              placeholder="选择一本书" @select="handleSelect" style="margin-right: 10px;">
              <template slot-scope="{ item }">
                <div class="name">《{{ item.title }}》</div>
              </template>
            </el-autocomplete>
            <span style="color: #888888; font-size: 14px;">选定的书籍：</span>
            <span v-text="getChoseBook()" style="margin-right: 10px; color: #777; font-size: 14px;"></span>
            <el-button type="primary" @click="postArticle">提交</el-button>
          </div>
        </div>
      </form>
      <!--  遮罩提示层 -->
      <div v-if="showInfo">
        <!--    提示上传中-->
        <p v-if="uploading">上传中</p>
        <!--    提示上传中-->
        <p v-else :style="uploadSuccessfully ? {color: 'green'} : {color: 'red'}" v-text="uploadSuccessfully ? '上传成功' : '上传失败'"></p>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import jwtDecode from "jwt-decode";
import Tinymce from "../components/Tinymce";
import Navbar from "../components/Navbar";

export default {
  name: "ArticleEditor",
  components: {Navbar, Tinymce},
  data() {
    return {
      article: {
        bookId: null,
        userId: null,
        title: '',
        content: '',
        picUrl: '',
      },


      restaurants: [],
      state: '',

      // 页面滚动
      scrollAction: {x: 'undefined', y: 'undefined'},
      scrollDirection: '',
      lastScrollDirection: '',
      useDefaultNavClass: true,

      finishedBooks: [],
      choseBook: '',
      uploading: false,
      uploadSuccessfully: false,
      showInfo: false,
      disabled: false
    }
  },
  methods: {
    imageUploadSuccessfully(res, file, fileList) {
      this.article.picUrl = res.data
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

    postArticle() {
      this.showInfo = true
      this.uploading = true
      this.uploadSuccessfully = false
      this.$http.post('http://localhost:8080/sparrow/article/add', this.article).then(res => {
      //   console.log()
        this.uploading = false
        if (res.data.status) {
          this.uploadSuccessfully = true
        }
      }).catch(_ => {
        this.uploading = false
        this.uploadSuccessfully = false
        // console.log(e.response.data.msg)
        // TODO: 保护接口更好的方式
        // console.clear()
      })
    },

    querySearch(queryString, cb) {
      let books = this.finishedBooks;
      // let results = queryString ? books.filter(this.createFilter(queryString)) : books;
      // 调用 callback 返回建议列表的数据
      console.log(this.finishedBooks)
      let results = books
      cb(results);
    },
    createFilter(queryString) {
      // return (str) => {
      //   return (str.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      // };
    },
    getChoseBook() {
      if (this.article.bookId === null) return '尚未选定'
      else {
        console.log('==>')
        let book = this.finishedBooks.find(e => {
          return e.id === this.article.bookId
        })
        return  '《' + book.title + '》'
      }
    },
    handleSelect(item) {
      console.log(item);
      this.choseBook = item.title
      this.article.bookId = item.id
    },
  },
  mounted() {

    let user = jwtDecode(localStorage.getItem('JWT_TOKEN'))
    this.article.userId = user.uid

    this.$http.get('http://localhost:8080/sparrow/ubft/u/' + user.uid).then(res => {
      if (res.data.status) {
        res.data.data.forEach(e => {
          this.$http.get('http://localhost:8080/sparrow/book/' + e.bookId).then(findBookRes => {
            this.finishedBooks.push(findBookRes.data.data)
          })
        })
        console.log(this.finishedBooks)
      }
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

.content-wrapper {
  display: flex;
  justify-content: center;
}
.content-wrapper form {
  display: flex;
  flex-direction: column;
}
.content-wrapper form * {
  margin: 10px 0;
}
.editor-form-wrapper {
  padding: 30px;
  width: 1200px;
  background-color: white;
  box-shadow: 0 7px 5px 0 #d9d9d9;
}
.article-title {
  outline-style: none;
  border: none;
  font-size: 24px;
  padding-left: 20px;
  border-left: 5px solid #999;
  height: 48px;
  color: #222;
}

.options-area {
  /*background-color: skyblue;*/
  display: flex;
  justify-content: space-between;
}
.options-area .feedback-options {
  padding-top: 18px;
  padding-left: 10px;
}

.options-area .feedback-options a {
  text-decoration: none;
  font-size: 12px;
  color: #555;
}
.options-area .feedback-options a:hover {
  text-decoration: underline;
}
</style>
