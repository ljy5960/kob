import { createRouter, createWebHistory } from 'vue-router'
import NotFound from "@/views/error/NotFound.vue"
import PkIndexView from "@/views/pk/PkIndexView.vue"
import RankListIndexView from "@/views/ranklist/RankListIndexView.vue"
import RecordIndexView from "@/views/record/RecordIndexView.vue"
import UserBotIndexView from "@/views/user/bots/UserBotIndexView.vue"
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView.vue'
import RecordContentView from '@/views/record/RecordContentView.vue'
import ReceiveCode from '@/views/user/account/UserAccountAcwingWebReceiveCode.vue'
import store from '@/store'
const routes = [
  {
    path:"/pk/",
    name:"pk_index",
    component:PkIndexView,
    meta:{
      requestAuth:true
    }
  },
  {
    path:"/record/",
    name:"record_index",
    component:RecordIndexView,
    meta:{
      requestAuth:true
    }
  },
  {
    path:"/record/:recordId/",
    name:"record_content",
    component:RecordContentView,
    meta:{
      requestAuth:true
    }
  },
  {
    path:"/ranklist/",
    name:"ranklist_index",
    component:RankListIndexView,
    meta:{
      requestAuth:true
    }
  },
  {
    path:"/user/bot/",
    name:"user_bot_index",
    component:UserBotIndexView,
    meta:{
      requestAuth:true
    }
  },
  {
    path:"/404/",
    name:"404",
    component:NotFound,
    meta:{
      requestAuth:false
    }
  },
  {
    path:"/",
    name:"home",
    redirect:"/pk/",
    meta:{
      requestAuth:true
    }
  },
  {
    path:"/user/account/register/",
    name:"user_account_register",
    component:UserAccountRegisterView,
    meta:{
      requestAuth:false
    }
  },
  {
    path:"/user/account/login/",
    name:"user_account_login",
    component:UserAccountLoginView,
    meta:{
      requestAuth:false
    }
  },
  {
    path:"/user/account/acwing/web/receive_code/",
    name:"user_account_acwing_web_receive_code",
    component:ReceiveCode,
    meta:{
      requestAuth:false
    }
  },
  {
    path:"/:catchAll(.*)",
    redirect:"/404/",
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})
router.beforeEach((to,from,next)=>{//每次通过router进入某个页面之前会调用该函数to表示跳转到哪个页面
  //from表示从哪个页面跳转过去，next表示将页面要不要执行下一步操作
  //to：目标路由对象
  //form：当前所在路由对象
  //next()：控制放行，控制放行到哪个路由
  //写之前首先要记录每一个未授权界面
  //把一些额外信息放到一个额外的域里面，meta信息里面存一下是否要授权
  //如果需要授权而且没有登录，重定向到登录页面


if(to.meta.requestAuth && !store.state.user.is_login){
next({name:"user_account_login"})
}else{
  next();
}
})
export default router
