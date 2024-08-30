<template>
    <div v-if="$store.state.pk.loser!='none'" class="result-borad">
        <div class="result" v-if="$store.state.pk.loser==='all'">平局</div>
        <div class="result" v-else-if="$store.state.pk.loser==='A'&&$store.state.pk.a_id===parseInt($store.state.user.id)">LOSE</div>
        <div class="result" v-else-if="$store.state.pk.loser==='B'&&$store.state.pk.b_id===parseInt($store.state.user.id)">LOSE</div>
        <div class="result" v-else>WIN</div>
        <div class="result-button">
            <button type="button" @click="restart" class="btn btn-warning">重新匹配</button>
        </div>
    </div>
</template>
<script>
import { useStore } from 'vuex';
export default{
    setup(){
        
        const store=new useStore();
        const restart=()=>{
        store.commit("updateStatus","matching");
        store.commit("updateLoser","none");
        store.commit("updateOpponent",{
            username:"我的对手",
            photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
        })
        }
        return {
            restart,
        }
    }
}
</script>
<style scoped>
.result-borad{
    height: 30vh;
    width: 30vw;
    background-color: rgba(50, 50, 50,0.5);
    position: absolute;
    top: 30vh;
    left: 68vh;
    font-style:normal;
    padding-top: 5vh;
}
.result{
    text-align: center;
    font-size: 50px;
    color: white;
    font-weight: 600;
}
.result-button{
    text-align: center;
    padding: 7vh;
}
</style>