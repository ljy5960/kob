<template>
    <div class="matchground">
        <div class="row">
            <div class="col-6">
                <div class="user-photo"><img :src="$store.state.user.photo"></div>
                <!-- 属性名写表达式要加:即v-bind: -->
                <div class="user-name">{{$store.state.user.username}}</div>
            </div>
            <div class="col-6">
                <div class="opponent-photo"><img :src="$store.state.pk.opponent_photo"></div>
                <!-- 属性名写表达式要加:即v-bind: -->
                <div class="opponent-name">{{$store.state.pk.opponent_username}}</div>
            </div>
            <div class="col-12" @click="click_match_btn()" style="text-align: center; padding-top:15vh"><button type="button" class="btn btn-success btn-lg">{{match_btn_info}}</button></div>
        </div>
    </div>
    </template>
    <script>
    import { ref } from 'vue'
    import { useStore } from 'vuex';
    export default{
        components:{
        },
        setup(){
        const store=useStore();
        let match_btn_info=ref("开始匹配");
        const click_match_btn=()=>{
            if(match_btn_info.value==='开始匹配'){
                match_btn_info.value='取消';
               store.state.pk.socket.send(JSON.stringify({
                //将json封装成字符串
                //JSON.stringify 是一个全局方法，用于将 JavaScript 对象或值转换为 JSON 字符串。
                event:"start-matching",
               }));
            }else{
                match_btn_info.value='开始匹配'
                store.state.pk.socket.send(JSON.stringify({
                event:"stop-matching",
               }));
            }
        }
        return{
            match_btn_info,
            click_match_btn
        }
        }
    }
    </script>
    <style scoped>
    .matchground{
        width: 60vw;
        height: 70vh;
        margin: 40px auto;
        background-color: rgba(50,50,50,0.5);
    }
    .user-photo{
        padding-top: 10vh;
        text-align: center;
    }
    .user-photo > img{
        border-radius: 50%;
        width: 20vh;
    }
    .user-name{
        text-align: center;
        font-size: 24px;
        font-weight: 600;
        color: white;
        padding-top: 2vh;
    }
    .opponent-photo{
        padding-top: 10vh;
        text-align: center;
    }
    .opponent-photo > img{
        border-radius: 50%;
        width: 20vh;
    }
    .opponent-name{
        text-align: center;
        font-size: 24px;
        font-weight: 600;
        color: white;
        padding-top: 2vh;
    }
    </style>
    <!-- text-align 是 CSS 中的一个属性，用于设置元素中的文本内容如何水平对齐。 -->