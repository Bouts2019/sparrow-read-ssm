<template>
<div>
  <h1>管理员首页</h1>
  <p>注意：当关闭或者刷新本页面的时候清空本地存储的管理员token</p>
<!--  <iframe src="http://localhost:8080/sparrow/druid/index.html" width="800" height="600">-->
<!--  </iframe>-->

  <!-- 本table -> 用户管理 -->
  <table border="1" cellspacing="0">
    <caption>用户管理</caption>
    <thead>
    <tr>
      <th>UID</th>
      <th>昵称</th>
      <th>电话</th>
      <th>邮箱</th>
      <th>头像</th>
      <th>描述</th>
      <th>注册时间</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="user in users">
      <td v-text="user.uid"></td>
      <td v-text="user.nickname"></td>
      <td v-text="user.phone"></td>
      <td v-text="user.email"></td>
      <td>
        <img width="40" height="40" :src="user.avatar" alt="用户头像">
      </td>
      <td v-text="user.description"></td>
      <td v-text="user.regTime"></td>
      <td>
        <button @click="updateUser(user.uid)">编辑</button>
        <button>删除</button>
      </td>
    </tr>
    </tbody>
  </table>
  <!-- 本table -> 文章管理 -->
  <table border="1" cellspacing="0">
    <caption>文章管理</caption>
    <thead>
    <tr>
      <th>ID</th>
      <th>用户ID</th>
      <th>图书ID</th>
      <th>发布时间</th>
      <th>更新时间</th>
      <th>标题</th>
      <th>内容</th>
      <th>点赞数量</th>
      <th>收藏数量</th>
      <th>评论数量</th>
      <th>审核状态</th>
      <th>操作<button @click="loadArticle">刷新文章</button></th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="article in articles">
      <td v-text="article.id"></td>
      <td v-text="article.userId"></td>
      <td v-text="article.bookId"></td>
      <td v-text="article.publishTime"></td>
      <td v-text="article.updateTime"></td>
      <td v-text="article.title"></td>
      <td v-text="article.content"></td>
      <td v-text="article.likeCount"></td>
      <td v-text="article.favoriteCount"></td>
      <td v-text="article.commentCount"></td>
      <td v-text="article.status === 0 ? '正常' : (article.status === 1 ? '待审核' : (article.status === 2 ? '驳回' : (article.status === 3 ? '已删除' : '未知')))"></td>
      <td>
        <button @click="updateArticle(article.id)">编辑</button>
        <button @click="approveArticle(article.id)">通过该文章</button>
        <button @click="disapproveArticle(article.id)">退回该文章</button>
        <button @click="deleteArticle(article.id)">删除该文章</button>
      </td>
    </tr>
    </tbody>
  </table>
  <!-- 本table -> 图书管理 -->
  <table border="1" cellspacing="0">
    <caption>图书管理</caption>
    <thead>
    <tr>
      <th>ID</th>
      <th>ISBN</th>
      <th>封面</th>
      <th>书名</th>
      <th>作者ID</th>
      <th>类型ID</th>
      <th>出版社ID</th>
      <th>出版日期</th>
      <th>一星人数</th>
      <th>二星人数</th>
      <th>三星人数</th>
      <th>四星人数</th>
      <th>五星人数</th>
      <th>简介</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="book in books">
      <td v-text="book.id"></td>
      <td v-text="book.isbn"></td>
      <td v-text="book.imgUrl"></td>
      <td v-text="book.title"></td>
      <td v-text="book.authorId"></td>
      <td v-text="book.typeId"></td>
      <td v-text="book.expressId"></td>
      <td v-text="book.expressDate"></td>
      <td v-text="book.stars1"></td>
      <td v-text="book.stars2"></td>
      <td v-text="book.stars3"></td>
      <td v-text="book.stars4"></td>
      <td v-text="book.stars5"></td>
      <td v-text="book.description"></td>
      <td>
        <button @click="updateBook(book.id)">更新</button>
        <button @click="deleteBook(book.id)">删除</button>
      </td>
    </tr>
    </tbody>
  </table>
  <!-- 本table -> 作者管理 -->
  <table border="1" cellspacing="0">
    <caption>作者管理</caption>
    <thead>
    <tr>
      <th>ID</th>
      <th>姓名</th>
      <th>简介</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="author in authors">
      <td v-text="author.id"></td>
      <td v-text="author.name"></td>
      <td v-text="author.desc"></td>
    </tr>
    </tbody>
  </table>
  <!-- 本table -> 出版社管理 -->
  <table border="1" cellspacing="0">
    <caption>出版社管理</caption>
    <thead>
    <tr>
      <th>ID</th>
      <th>名称</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="express in expresses">
      <td v-text="express.id"></td>
      <td v-text="express.name"></td>
    </tr>
    </tbody>
  </table>
  <!-- 本table -> 图书类别管理 -->
  <table border="1" cellspacing="0">
    <caption>图书类别</caption>
    <thead>
    <tr>
      <th>ID</th>
      <th>名称</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="type in types">
      <td v-text="type.id"></td>
      <td v-text="type.name"></td>
    </tr>
    </tbody>
  </table>
  <!-- 本table -> 热门文章管理 -->
  <!-- 本table -> 热搜管理 -->
  <!-- -->
</div>
</template>

<script>
export default {
  name: "AdminIndex",
  data() {
    return {
      users: [],
      articles: [],
      books: [],
      authors: [],
      expresses: [],
      types: []
    }
  },
  methods: {
    // 加载数据方法
    loadUser() {
      // 用户信息获取
      this.$http.get('http://localhost:8080/sparrow/user/all').then(res => {
        if (res.data.status) {
          this.users = res.data.data
        }
      })
    },
    loadArticle() {
      // 文章信息获取
      this.$http.get('http://localhost:8080/sparrow/article/all').then(res => {
        if (res.data.status) {
          this.articles = res.data.data
        }
      })
    },
    loadBook() {
      // 图书信息获取
      this.$http.get('http://localhost:8080/sparrow/book/all').then(res => {
        if (res.data.status) {
          this.books = res.data.data
        }
      })
    },
    // =============
    updateUser(id) {
      console.log(id)
    },
    updateArticle(id) {
      console.log(id)
    },
    approveArticle(id) {
      /*
       * status:
       *  0 -> ok,
       *  1 -> waiting for approving,
       *  2 -> disapprove
       *  3 -> deleted
       */
      this.$http.put(`http://localhost:8080/sparrow/article/approve/${id}`, {esId: null}).then(res => {
        if (res.data.status) {
          console.log('ok')
          this.articles.find(e => e.id === id).status = 0
        }
      })
    },
    disapproveArticle(id) {
      console.log(id)
    },
    deleteArticle(id) {
      console.log(id)
    },
    updateBook(id) {
      console.log(id)
    },
    deleteBook(id) {
      console.log(id)
    }
  },
  created() {
    window.onbeforeunload = function (e) {
      localStorage.removeItem('ADMIN_JWT_TOKEN')
      // TODO: 分离移除已完成
      return true
    }
  },
  mounted() {
    this.loadUser()
    this.loadArticle()
    this.loadBook()
    // 作者信息获取
    this.$http.get('http://localhost:8080/sparrow/author/all').then(res => {
      if (res.data.status) {
        this.authors = res.data.data
      }
    })
    // 出版社管理
    this.$http.get('http://localhost:8080/sparrow/express/all').then(res => {
      if (res.data.status) {
        this.expresses = res.data.data
      }
    })
    // 图书类型管理
    this.$http.get('http://localhost:8080/sparrow/type/all').then(res => {
      if (res.data.status) {
        this.types = res.data.data
      }
    })
  }
  // TODO: 外链拦截
}
</script>

<style scoped>

</style>
