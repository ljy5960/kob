<template>
  <div class="window">
    <div class="game-body">
    <MenuView v-if="$store.state.router.router_name==='menu'"></MenuView>
    <PkIndexView v-else-if="$store.state.router.router_name==='pk'"></PkIndexView>
    <RankListIndexView v-else-if="$store.state.router.router_name==='ranklist'"></RankListIndexView>
    <RecordContentView v-else-if="$store.state.router.router_name==='record_content'"></RecordContentView>
    <RecordIndexView v-else-if="$store.state.router.router_name==='record'"></RecordIndexView>
    <UserBotIndexView v-else-if="$store.state.router.router_name==='user_bot'"></UserBotIndexView>
  </div>
  </div>
</template>

<script>
import { useStore } from "vuex";
import MenuView from "@/views/MenuView.vue"
import PkIndexView from "./views/pk/PkIndexView.vue";
import RankListIndexView from "./views/ranklist/RankListIndexView.vue";
import RecordIndexView from "./views/record/RecordIndexView.vue";
import RecordContentView from "./views/record/RecordContentView.vue";
import UserBotIndexView from "./views/user/bots/UserBotIndexView.vue";
import $ from 'jquery';


export default{
 components:{
  MenuView,
  PkIndexView,
  RankListIndexView,
  RecordContentView,
  RecordIndexView,
  UserBotIndexView,

 },
 setup(){
  const store=useStore();
  $.ajax({
    url:"https://www.ljy5960.cn/api/user/account/acwing/acapp/apply_code/",
    type:"get",
    success:resp=>{
      if(resp.result==="success"){
        store.state.user.AcWingOS.api.oauth2.authorize(resp.appid, resp.redirect_uri, resp.scope, resp.state, resp=>{
          if(resp.result==="success"){
          //调用mutations函数使用commit
            //调用action函数用dispatch
            const jwt_token=resp.jwt_token;
            store.commit("updateToken",jwt_token);
            store.dispatch("getinfo",{
                success(){
                store.commit("updatePullingInfo",false);
                },
                error(){
                    store.commit("updatePullingInfo",false);
                }
            })
          }else{
            store.state.user.AcWingOS.api.window.close();
          }
        });
      }else{
        store.state.user.AcWingOS.api.window.close();
      }
    }
  })
   }
  }
</script>


<style>
body{
  margin: 0;
}
.game-body{
  background-image: url("@/assets/images/background.png");
  background-size: cover;
  width: 100%;
  height: 100%;
}
.window{
  height: 100vh;
  width: 100vw;
}
</style>
