import { createRouter, createWebHistory } from 'vue-router'
import NotFound from "@/views/error/NotFound.vue"
import PkIndexView from "@/views/pk/PkIndexView.vue"
import RankListIndexView from "@/views/ranklist/RankListIndexView.vue"
import RecordIndexView from "@/views/record/RecordIndexView.vue"
import UserBotIndexView from "@/views/user/bots/UserBotIndexView.vue"
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView.vue'
const routes = [
  {
    path:"/pk/",
    name:"pk_index",
    component:PkIndexView,
  },
  {
    path:"/record/",
    name:"record_index",
    component:RankListIndexView,
  },
  {
    path:"/ranklist/",
    name:"ranklist_index",
    component:RecordIndexView,
  },
  {
    path:"/user/bot/",
    name:"user_bot_index",
    component:UserBotIndexView,
  },
  {
    path:"/404/",
    name:"404",
    component:NotFound,
  },
  {
    path:"/",
    name:"home",
    redirect:"/pk/",
  },
  {
    path:"/user/account/register/",
    name:"user_account_register",
    component:UserAccountRegisterView,
  },
  {
    path:"/user/account/login/",
    name:"user_account_login",
    component:UserAccountLoginView,
  },
  {
    path:"/:catchAll(.*)",
    redirect:"/404/"
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
