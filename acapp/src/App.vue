<template>
    <div class="game-body">
      <MenuView v-if="$store.state.router.router_name==='menu'"></MenuView>
      <PkIndexView v-else-if="$store.state.router.router_name==='pk'"></PkIndexView>
      <RankListIndexView v-else-if="$store.state.router.router_name==='ranklist'"></RankListIndexView>
      <RecordContentView v-else-if="$store.state.router.router_name==='record_content'"></RecordContentView>
      <RecordIndexView v-else-if="$store.state.router.router_name==='record'"></RecordIndexView>
      <UserBotIndexView v-else-if="$store.state.router.router_name==='user_bot'"></UserBotIndexView>
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
  const jwt_token="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5ZWFkZTFhZWZjZjA0YWQxOTMwNzE5N2QzYjJjOTA2MyIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTcyNDIwNDgzOCwiZXhwIjoxNzI1NDE0NDM4fQ.sVMVn3dJiJf455EZkU93STdn3umTouECQn_lXzM_gC8";
  const store=useStore();
        if(jwt_token){
            //调用mutations函数使用commit
            //调用action函数用dispatch
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
            store.commit("updatePullingInfo",false);
        }

 }
}
</script>


<style scoped>
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
