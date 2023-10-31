import Vue from 'vue'
import Router from 'vue-router'
import Article from "../pages/Article";
import Index from "../pages/Index";
import Login from "../pages/Login";
import Search from "../pages/Search";
import Home from "../pages/Home";
import AdminLogin from "../pages/AdminLogin";
import AdminIndex from "../pages/AdminIndex";
import ArticleEditor from "../pages/ArticleEditor";
import About from "../pages/About";
import Talk from "../pages/Talk";
import User from "../pages/User";
import Feedback from "../pages/Feedback";

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/index',
      redirect: '/'
    },
    {
      path: '/',
      name: 'Index',
      component: Index
    },
    {
      path: '/article/:id',
      name: 'Article',
      component: Article
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/search',
      name: 'Search',
      component: Search
    },
    {
      path: '/home',
      name: 'Home',
      component: Home
    },
    {
      path: '/admin/login',
      name: 'AdminLogin',
      component: AdminLogin
    },
    {
      path: '/admin/index',
      name: '/admin/index',
      component: AdminIndex
    },
    {
      path: '/editor/article',
      name: '/editor/article',
      component: ArticleEditor
    },
    {
      path: '/about',
      name: 'About',
      component: About
    },
    {
      path: '/talk',
      name: 'Talk',
      component: Talk
    },
    {
      path: '/user/:id',
      name: 'User',
      component: User
    },
    {
      path: '/feedback',
      name: 'Feedback',
      component: Feedback
    }
  ]
})

router.beforeEach(((to, from, next) => {
  // if (localStorage.getItem('JWT_TOKEN')) {
  //   if (to.name === 'Login') next('/')
  //   else next()
  // } else {
  // if (to.name === 'Home') next('/login')
  // else next()
  // }
  if (to.name === 'Login' && localStorage.getItem('JWT_TOKEN')) {
    // 登录信息保存着
    // 那么访问Login页面直接返回首页
    next('/')
  } else if (to.name === 'Home' && !localStorage.getItem('JWT_TOKEN')) {
    // 登录信息已被清除或者尚未登录
    // 如果访问个人主页，那么直接返回登陆页面
    next('/login')
  } else if (to.name ==='/admin/index' && !localStorage.getItem('ADMIN_JWT_TOKEN')) {
    next('/admin/login')
  } else if (to.name === '/editor/article' && !localStorage.getItem('JWT_TOKEN')) {
    next('/login')
  } else {
    next()
  }

}))

export default router
