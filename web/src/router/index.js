import { createRouter, createWebHistory } from 'vue-router'
import NotFound from "@/views/error/NotFound.vue"
import PkIndexView from "@/views/pk/PkIndexView.vue"
import RankListIndexView from "@/views/ranklist/RankListIndexView.vue"
import RecordIndexView from "@/views/record/RecordIndexView.vue"
import UserBotIndexView from "@/views/user/bots/UserBotIndexView.vue"
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
    path:"/:catchAll(.*)",
    redirect:"/404/"
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
