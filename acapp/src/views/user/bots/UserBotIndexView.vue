<template>
    <contentFieldVue>
        <div class="game-table">
            <div>
                <span style="font-size: 150%;">我的Bot</span>
                <button type="button" style="float: right;" @click="show_add_modal_handler(true)">
                    创建Bot
                </button>
                <!--modal-->
                <div class="game-modal" id="add-bot-btn" tabindex="-1" v-if="show_add_modal">
                    <div>
                        <h5 style="margin: 2px;">创建Bot</h5>
                    </div>
                    <div>
                        <div>
                            <label for="add-bot-title">名称</label>
                            <input style="width: 85%;" v-model="botadd.title" type="email" id="add-bot-title"
                                placeholder="请输入名称">
                        </div>
                        <div>
                            <label for="add-bot-description">简介</label>
                            <textarea style="width: 85%; margin-top:10px" v-model="botadd.description"
                                id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                        </div>
                        <div>
                            <label for="add-bot-code">代码</label>
                            <VAceEditor v-model:value="botadd.content" @init="editorInit" lang="c_cpp" theme="textmate"
                                style="height: 300px" :options="{
                                    enableBasicAutocompletion: true, //启用基本自动完成
                                    enableSnippets: true, // 启用代码段
                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                    fontSize: 14, //设置字号
                                    tabSize: 2, // 标签大小
                                    showPrintMargin: false, //去除编辑器里的竖线
                                    highlightActiveLine: true,
                                }" />
                        </div>
                    </div>
                    <div>
                        <div class="error-message" style="color: red;">{{ botadd.error_message }}</div>
                        <button type="button" @click="add_bot">创建</button>
                        <button type="button" @click="show_add_modal_handler(false)">取消</button>
                    </div>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>名称</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="bot in bots" :key="bot.id">
                            <td>{{ bot.title }}</td>
                            <td>{{ bot.createtime }}</td>
                            <td>
                                <button type="button" style="margin-right: 10px;" @click="show_update_modal_handler(bot.id,true)">
                                    修改
                                </button>
                                <button type="button" @click="delete_bot(bot)">删除</button>

                                <div class="game-modal" :id="'update-bot-modal-' + bot.id" tabindex="-1" v-if="bot.show_update_modal">
                                    <div>
                                        <h5 style="margin: 2px;">修改Bot</h5>
                                    </div>
                                    <div>
                                        <div>
                                            <label for="add-bot-title">名称</label>
                                            <input style="width: 85%;" v-model="bot.title" type="text"
                                                class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                        </div>
                                        <div>
                                            <label for="add-bot-description">简介</label>
                                            <textarea style="width: 85%; margin-top:10px" v-model="bot.description" id="add-bot-description" rows="3" placeholder="请输入Bot简介">
                                                    </textarea>
                                        </div>
                                        <div>
                                            <label for="add-bot-code">代码</label>
                                            <VAceEditor v-model:value="bot.content" @init="editorInit" lang="c_cpp"
                                                theme="textmate" style="height: 300px" :options="{
                                                    enableBasicAutocompletion: true, //启用基本自动完成
                                                    enableSnippets: true, // 启用代码段
                                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                                    fontSize: 14, //设置字号
                                                    tabSize: 2, // 标签大小
                                                    showPrintMargin: false, //去除编辑器里的竖线
                                                    highlightActiveLine: true,
                                                }" />
                                        </div>
                                    </div>
                                    <div>
                                        <div class="error-message" style="color: red;">{{
                                            botupdate.error_message }}</div>
                                        <button type="button" @click="update_bot(bot)">保存修改</button>
                                        <button type="button" @click="show_update_modal_handler(bot.id,ture)">取消</button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <BackButton></BackButton>
        </div>
    </contentFieldVue>
</template>
<script>
import { ref, reactive } from 'vue'//绑定变量ref绑定对象reactive
import $ from 'jquery'
import { useStore } from 'vuex';
import { VAceEditor } from "vue3-ace-editor";
import ace from "ace-builds";
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';
import 'ace-builds/src-noconflict/mode-c_cpp';
import BackButton from "@/components/BackButton.vue";
export default {
    components: {
        VAceEditor,
        BackButton,
    },
    setup() {
        ace.config.set(
            "basePath",
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require("ace-builds").version + "/src-noconflict/")
        let bots = ref([]);
        const store = useStore();
        let show_add_modal = ref(false);
        const botupdate = reactive({
            error_message: "",
        })
        const botadd = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        })

        const refresh_bots = () => {
            $.ajax({
                url: "https://www.ljy5960.cn/api/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    for(const bot of resp){
                        bot.show_update_modal=false;
                    }
                    bots.value = resp;
                }
            })
        }
        refresh_bots();
        const add_bot = () => {
            botadd.error_message = "",
                $.ajax({
                    url: "https://www.ljy5960.cn/api/user/bot/add/",
                    type: "post",
                    data: {
                        title: botadd.title,
                        description: botadd.description,
                        content: botadd.content,
                    },
                    headers: {
                        Authorization: "Bearer " + store.state.user.token
                    },
                    success(resp) {
                        if (resp.error_message === "success") {
                            botadd.title = "",
                                botadd.description = "",
                                botadd.content = "",
                                show_add_modal.value = false;
                            refresh_bots();
                        } else {
                            botadd.error_message = resp.error_message;
                        }
                    }
                })
        }
        const delete_bot = (bot) => {
            $.ajax({
                url: "https://www.ljy5960.cn/api/user/bot/remove/",
                type: "post",
                data: {
                    bot_id: bot.id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        refresh_bots();
                    }
                }
            })
        }
        const update_bot = (bot) => {
            botadd.error_message = "";
            $.ajax({
                url: "https://www.ljy5960.cn/api/user/bot/update/",
                type: "post",
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        refresh_bots();
                    } else {
                        botupdate.error_message = resp.error_message;
                        refresh_bots();
                    }
                }
            })
        }
        const show_add_modal_handler = is_show => {
            show_add_modal.value = is_show;
        }
        const show_update_modal_handler=(bot_id,is_show)=>{
            const new_bots=[];
            for(const bot of bots.value){
            if(bot.id===bot_id){
                bot.show_update_modal=is_show;
            }
            new_bots.push(bot);
            }
            bots.value=new_bots;
        }
        return {
            bots,
            botadd,
            add_bot,
            delete_bot,
            update_bot,
            botupdate,
            show_add_modal_handler,
            show_add_modal,
            show_update_modal_handler,
        }
    }
}
</script>
<style scoped>
th {
    text-align: center;
}

img.record-user-photo {
    width: 5vh;
    border-radius: 50%;
}

.game-table {
    display: flex;
    justify-content: center;
    width: 100%;
    height: calc(100%-5vh);
    padding-top: 5vh;
}

td {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 12vw;
    max-width: 12vw;
    text-align: center;
}

.game-table table {
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 5px;
}

.game-modal {
    background-color: white;
    padding: 10px;
    border-radius: 5px;
    position: absolute;
    width: 40vw;
    height: 65vh;
    left: 0;
    right: 0;
    bottom: 0;
    top: 0;
    margin: auto;
    text-align: left;
}
</style>