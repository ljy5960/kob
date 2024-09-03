<template>
    <div class="menu-field">
        <div class="menu">
            <div class="menu-item" @click="click_pk">对战</div>
            <div class="menu-item" @click="click_record">对局列表</div>
            <div class="menu-item" @click="click_record_content">排行榜</div>
            <div class="menu-item" @click="click_userbot">我的Bot</div>
        </div>
    </div>
</template>
<script>
import { useStore } from 'vuex';
export default{
    setup(){
    const store=useStore();
    const click_pk=()=>{
    store.commit("updateRoutername","pk");
    };
    const click_record=()=>{
    store.commit("updateRoutername","record");
    }
    const click_record_content=()=>{
    store.commit("updateRoutername","ranklist");
    }
    const click_userbot=()=>{
    store.commit("updateRoutername","user_bot");
    }
    return {
        click_pk,
        click_record,
        click_record_content,
        click_userbot
    }
}
    
}
</script>
<style scoped>
.menu-field{
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
}
.menu{
    width: 25vw;
    height: 30vh;
    background-color: rgba(0, 0,0, 0.2);
}
.menu-item{
    height: 7.5vh;
    width: 100%;
    font-size: 24px;
    line-height: 7.5vh;
    color:white;
    text-align: center;
    font-style: italic;
    font-weight: 600;
    cursor: pointer;
    user-select: none;
}
.menu-item:hover{
    transform: scale(1.2);
    transition: 200ms;
}
</style>