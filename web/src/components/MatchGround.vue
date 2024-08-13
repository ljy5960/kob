<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo"><img :src="$store.state.user.photo"></div>
                <!-- 属性名写表达式要加:即v-bind: -->
                <div class="user-name">{{$store.state.user.username}}</div>
            </div>
            <div class="col-4">
                <div class="user-select-bot">
                    <!-- v-model 可以让表单元素和 Vue 组件的数据保持同步。用户在输入框中输入数据时，
                     Vue 组件的数据会立即更新；如果组件的数据发生变化，输入框中的显示也会自动更新。 -->
                    <select v-model="select_bot" class="form-select" aria-label="Default select example">
                        <option value="-1" selected>亲自上阵</option>
                        <option :value="bot.id" v-for="bot in bots" :key="bot.id">{{bot.title}}</option>
                      </select>
                </div>
            </div>
            <div class="col-4">
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
    import $ from 'jquery';
    export default{
        components:{
        },
        setup(){
        const store=useStore();
        let match_btn_info=ref("开始匹配");
        let bots=ref({});
        let select_bot=ref("-1");

        const click_match_btn=()=>{
            if(match_btn_info.value==='开始匹配'){
                match_btn_info.value='取消';
               store.state.pk.socket.send(JSON.stringify({
                //将json封装成字符串
                //JSON.stringify 是一个全局方法，用于将 JavaScript 对象或值转换为 JSON 字符串。
                event:"start-matching",
                bot_id:select_bot.value,
               }));
            }else{
                match_btn_info.value='开始匹配'
                store.state.pk.socket.send(JSON.stringify({
                event:"stop-matching",
               }));
            }
        }

        const refresh_bots = () => {
            $.ajax({
                url: "http://localhost:9090/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                }
            })
        }
        refresh_bots();//从云端获取bot
        return{
            match_btn_info,
            click_match_btn,
            bots,
            select_bot,
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
    .user-select-bot{
        padding-top: 20vb;
    }
    .user-select-bot>select{
     width: 60%;
     margin: 0 auto;
    }
    </style>
    <!-- text-align 是 CSS 中的一个属性，用于设置元素中的文本内容如何水平对齐。 -->