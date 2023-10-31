// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import Axios from "axios";

import { library } from '@fortawesome/fontawesome-svg-core'
import { faEnvelope, faCalendarAlt, faBirthdayCake, faUndo, faFireAlt, faSearch, faBurn, faHistory, faThumbsUp, faThumbsDown, faMobileAlt, faLock, faUser, faComment, faShare } from '@fortawesome/free-solid-svg-icons'
import { faThumbsUp as farThumbsUp, faComment as farComment, faShareSquare as farShareSquare, faThumbsDown as farThumbsDown, faEye as farEye} from '@fortawesome/free-regular-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
library.add( faEnvelope, faCalendarAlt, faBirthdayCake, faUndo, faFireAlt, faSearch, faBurn, faHistory, faThumbsUp, faThumbsDown, faMobileAlt, faLock, faUser, faComment, faShare, farThumbsUp, farComment, farShareSquare, farThumbsDown, farEye)

Vue.component('font-awesome-icon', FontAwesomeIcon)

Axios.interceptors.request.use(
  config => {
    // 本地存储空间是否有jwt_token
    if (localStorage.getItem('JWT_TOKEN')) {
      // 有就带上
      config.headers.Authorization = `${localStorage.JWT_TOKEN}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
    // return error.response
  }
)
// Axios.interceptors.response.use(
//   response => {
//     if (response.status === 200) {
//       return Promise.resolve(response)
//     } else {
//       return Promise.reject(response)
//     }
//     return response
//   },
//   error => {
//     return error
//   }
// )

Vue.use(ElementUI)
Vue.prototype.$http = Axios
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
