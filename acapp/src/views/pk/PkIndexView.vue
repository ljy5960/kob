<template>
    <div class="user-color" v-if="$store.state.pk.status==='playing'&&parseInt($store.state.user.id)===parseInt($store.state.pk.a_id)">左下角</div>
    <div class="user-color" v-if="$store.state.pk.status==='playing'&&parseInt($store.state.user.id)===parseInt($store.state.pk.b_id)">右上角</div>
    <PlayGround v-if="$store.state.pk.status==='playing'"></PlayGround>
    <MatchGround v-if="$store.state.pk.status==='matching'"></MatchGround>
    <ResultBoard v-if="$store.state.pk.loser!='none'"> </ResultBoard>
    <BackButton v-if="$store.state.pk.status==='matching'"></BackButton>
    <!-- 在template中使用全局变量用$,在js中去掉$ -->
</template>
<script>
import MatchGround from "@/components/MatchGround.vue";
import PlayGround from "@/components/PlayGround.vue"
import ResultBoard from "@/components/ResultBoard.vue"
import { onMounted,onUnmounted } from "vue";
import { useStore } from "vuex";
import BackButton from "@/components/BackButton.vue";
// onMounted当组件被挂载时执行,当组件被卸载执行onUnmounted
export default{
    components:{
        PlayGround,
        MatchGround,
        ResultBoard,
        BackButton
    },
    setup(){
    const store=useStore();
    const socketurl=`wss://app6957.acapp.acwing.com.cn/websocket/${store.state.user.token}/`
    let socket=null;
    store.commit("updateLoser","none");
    store.commit("updateIsRecord",false);
    onMounted(()=>{
    socket=new WebSocket(socketurl);
    socket.onopen=()=>{
        console.log("connected!");
        store.commit("updateSocket",socket);
        store.commit("updateOpponent",{
            username:"我的对手",
            photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
        })
    },
    socket.onmessage=msg=>{
         const data=JSON.parse(msg.data);
         if(data.event==="start-matching"){//匹配成功
         store.commit("updateOpponent",{
            username:data.opponent_username,
            photo:data.opponent_photo,
         });
         setTimeout(()=>{
            store.commit("updateStatus","playing");
         },2000)
         store.commit("updateGame",data.game);
         }else if(data.event==="move"){
            console.log(data);
         const game=store.state.pk.gameObject;
         const [snake0,snake1]=game.snakes;
         snake0.set_direction(data.a_direction);
         snake1.set_direction(data.b_direction);
         }else if(data.event==="result"){
            const game=store.state.pk.gameObject;
             const [snake0,snake1]=game.snakes;
             if(data.loser==="all"||data.loser==="A"){
                 snake0.status="die"
             }
             if(data.loser==="all"||data.loser==="B"){
                 snake1.status="die";
             }
             store.commit("updateLoser",data.loser);
         }
    }
    socket.onclose=()=>{
        console.log("disconnected!");
    }
    });
    onUnmounted(()=>{
        socket.close();
        store.commit("updateStatus", "matching");

    })
    }
}
</script>
<style scoped>
.user-color{
    text-align: center;
    color: white;
    font-size: 30px;
    font-weight: 600;;
}
</style>