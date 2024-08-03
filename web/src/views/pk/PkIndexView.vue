<template>
    <PlayGround v-if="$store.state.pk.status==='playing'"></PlayGround>
    <MatchGround v-if="$store.state.pk.status==='matching'"></MatchGround>
    <!-- 在template中使用全局变量用$,在js中去掉$ -->
</template>
<script>
import MatchGround from "@/components/MatchGround.vue";
import PlayGround from "@/components/PlayGround.vue"
import { onMounted,onUnmounted } from "vue";
import { useStore } from "vuex";
// onMounted当组件被挂载时执行,当组件被卸载执行onUnmounted
export default{
    components:{
        PlayGround,
        MatchGround,
    },
    setup(){
    const store=useStore();
    const socketurl=`ws://localhost:9090/websocket/${store.state.user.token}/`
    let socket=null;
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
         store.commit("updateGamemap",data.map);
         }
    }
    socket.onclose=()=>{
        socket.close();
        store.commit("updateStatus","matching");
    }
    });
    onUnmounted(()=>{
        socket.close();
    })
    }
}
</script>
<style scoped>
</style>