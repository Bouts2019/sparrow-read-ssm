<template>
<div>
  <div v-if="activities.length === 0" class="no-read">
    <h2>还没有阅读完的书，添加一本？📖</h2>
  </div>
  <div v-else class="timeline">
    <el-timeline >
      <el-timeline-item
        v-for="(activity, index) in activities"
        :key="index"
        :timestamp="activity.timestamp">
        <span>完成阅读: </span>
        《<a href="" class="book-name">{{activity.bookName}}</a>》
      </el-timeline-item>
    </el-timeline>
  </div>
  <div class="operations">
    <el-button type="success" :circle="true" icon="el-icon-plus" @click="showAddPanelAction()"></el-button>
  </div>
  <el-dialog title="添加书籍" :visible.sync="showAddPanel">
    <el-form :model="userInput">
      <el-form-item label="书本名称" :label-width="'100px'">
        <el-input v-model="userInput.title" autocomplete="off" @blur="findBook()"></el-input>
        信息：<span v-html="tipMessage"></span>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="showAddPanel = false">取 消</el-button>
      <el-button type="primary" @click="addReadBook()" :disabled="book === null">确 定</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
export default {
  name: "HomeMyRead",
  data() {
    return {
      showAddPanel: false,
      tipMessage: '输入正确的书名即可',
      userInput: {
        title: ''
      },
      book: null,
      reverse: true,
      activities: []
    };
  },
  props: {
    myId: [Number]
  },
  watch: {
    myId: function () {
      this.$http.get('http://localhost:8080/sparrow/ubft/u/' + this.myId).then(res => {
        let finishReadObj = res.data.data
        finishReadObj.forEach(item => {
          this.$http.get('http://localhost:8080/sparrow/book/' + item.bookId).then(bookRes => {
            console.log(bookRes)
            this.activities.push({
              bookName: bookRes.data.data.title,
              bookId: item.bookId,
              timestamp: item.finishTime
            })
          })
        })
      })
    }
  },
  methods: {

    addReadBook() {
      console.log('123')
      let obj = {}
      obj.uid = this.myId
      obj.bid = this.book.id
      this.$http.post('http://localhost:8080/sparrow/ubft/read', obj).then(res => {
        this.$alert('ok', {
          type: 'success',
          callback: action => {
            this.showAddPanel = false
          }
        })
      }).catch(err => {
        this.$alert('ok', {
          type: 'error',
          callback: action => {
            this.showAddPanel = false
          }
        })
      })
    },
    showAddPanelAction() {
      this.showAddPanel = true
      this.book = null
    },
    findBook() {
      if (this.userInput.title !== '') {
        this.tipMessage = '正在搜索中...'
        this.$http.get('http://localhost:8080/sparrow/book/title-like?title=' + this.userInput.title).then(book => {
          if (book.data.data === null) {
            this.tipMessage = '没有找到，请检查书名？'
            this.book = null
            return
          }
          this.book = book.data.data
          this.$http.get('http://localhost:8080/sparrow/author/' + this.book.authorId).then(author => {
            this.tipMessage = `
            <br>
            书名: ${this.book.title}
            <br>
            作者: ${author.data.data.name}
            `
          })
        })
      }
    }
  }
}
</script>

<style scoped>
.timeline {
  margin-top: 10px;
}
.operations {
  text-align: center;
}
.book-name {
  color: #333;
  text-decoration: none;
  font-weight: 700;
  font-size: 14px;
}
.no-read h2 {
  color: #333;
  font-size: 18px;
  text-align: center;
}
</style>
