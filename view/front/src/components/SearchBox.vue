<template>
  <div>
    <div class="search-wrapper">
      <div style="transition: .3s;" :style="(boxFocusing || buttonFocusing) ? {'width': '0'} : {'width': '100px'}"></div>
      <el-dropdown trigger="click" size="medium" ref="dropdown" @command="handleDropItemClick">
        <el-input @keyup.enter.native="handleSearch" ref="searchBox" v-model="searchText" :placeholder="placeHolderText" style="transition: .3s;" :class="(boxFocusing || buttonFocusing) ? ['focusing'] : ['not-focusing']" @focus="boxFocusing = true" @blur="boxFocusing  = false"></el-input>
        <el-dropdown-menu slot="dropdown" style="width: 250px;">
          <div v-if="hotSearchDropDownItems.length === 0 || searchText !== ''">
            <el-dropdown-item disabled>
              <el-divider>空空如也</el-divider>
            </el-dropdown-item>
          </div>
          <div v-if="searchText === ''">
            <!-- 热搜遍历 -->
            <el-dropdown-item disabled>
              <el-divider><i class="el-icon-search"></i>热搜</el-divider>
            </el-dropdown-item>
            <el-dropdown-item v-for="(dropDownItem, index) in hotSearchDropDownItems" :key="index" :command="dropDownItem.value">
              <font-awesome-icon icon="burn" style="color: red;"/>
              &nbsp;&nbsp;{{dropDownItem.title}}
            </el-dropdown-item>
            <!-- 历史遍历 -->
<!--            <el-dropdown-item disabled>-->
<!--              <el-divider><i class="el-icon-time"></i>历史</el-divider>-->
<!--            </el-dropdown-item>-->
<!--            <el-dropdown-item v-for="(dropDownItem, index) in historySearchDropDownItems" :key="-1 * index">-->
<!--              <font-awesome-icon icon="history" />-->
<!--              {{dropDownItem.title}}-->
<!--            </el-dropdown-item>-->
          </div>
        </el-dropdown-menu>
      </el-dropdown>
      <el-button @focus="buttonFocusing = true" @blur="buttonFocusing = false" @click="handleSearch" style="margin-left: 10px;" type="primary" :circle="true" :round="true" :plain="true" icon="el-icon-search"></el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "SearchBox",
  data() {
    return {
      searchText: '',
      boxFocusing: false,
      buttonFocusing: false,
      placeHolderText: '搜点什么'
    }
  },
  props: {
    // { title, value }
    historySearchDropDownItems: [Array],
    hotSearchDropDownItems: [Array]
  },
  methods: {
    handleSearch() {
      this.focusing = true
      if (this.searchText === '') {
        this.focusing = true
        this.$refs.searchBox.focus()
      } else {
        console.log('搜索')
      }
      this.search()
    },
    handleDropItemClick(str) {
      this.searchText = str
      this.search()
    },
    search() {
      let keyword = this.searchText
      if (keyword === '') {
        keyword = this.hotSearchDropDownItems.find(e => {
          return e.title === this.placeHolderText
        }).value
      }
      let history = JSON.parse(localStorage.getItem('history'))
      if (history === null) history = []
      let found = false
      history.forEach(item => found |= item.value === keyword)
      if (!found) history.push({value: keyword})
      localStorage.setItem('history', JSON.stringify(history))

      // localStorage.setItem('history', history)
      let searchUrl = this.$router.resolve({
        path: '/search',
        query: {
          q: keyword
        }
      })
      window.open(searchUrl.href, '_blank')
    },
  },
  mounted() {
    window.addEventListener('scroll', e => {
      if (this.boxFocusing) {
        this.boxFocusing = false
        this.buttonFocusing = false
        this.$refs.searchBox.$el.click()
        this.$refs.searchBox.blur()
      }
    })
    setInterval(() => {
      if (this.hotSearchDropDownItems !== undefined) {
        this.placeHolderText = this.hotSearchDropDownItems[Math.floor(Math.random() * this.hotSearchDropDownItems.length)].title
      }
    }, 10 * 1000)
  }
}
</script>

<style>
.search-wrapper {
  display: flex;
  width: 300px;
}
.focusing {
  width: 250px;
}
.not-focusing {
  width: 160px;
}
</style>
