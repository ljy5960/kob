<template>
    <ContentField  v-if="!$store.state.user.pulling_info">
       <div class="row justify-content-md-center">
        <div class="col-3">
           <form @submit.prevent="login"><!--@submit.prevent="login" 将submit与login函数绑到一起prevent阻止默认行为-->
                <div class="mb-3">
                  <label for="username" class="form-label">用户名</label>
                  <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                </div><!--v-model="password"将下面的变量与组件绑定-->
                <div class="mb-3">
                  <label for="password" class="form-label">密码</label>
                  <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                </div>
                 <div class="mb-3">
                 <router-link :to="{name:'user_account_register'}">注册</router-link>
                </div>
                <div class="error-message">{{ error_message }}</div>
                <button type="submit" class="btn btn-primary">登录</button>
             </form>
        </div>
       </div>
    </ContentField>
    </template>
    <script>
    //如果模块使用default导出，导入时不需要使用大括号 {}。默认导出是指一个模块只能有一个默认导出。
    //如果模块使用命名导出，导入时必须使用大括号 {}。命名导出允许一个模块导出多个成员。
    import ContentField from "@/components/ContentField.vue";
    import { useStore } from 'vuex';
    import { ref } from 'vue';
    import router from "@/router";
    export default{
        components:{
            ContentField
        },
        setup(){//修改state的值就写入router user
        const store =useStore();
        let username =ref('');
        let password =ref('');
        let error_message=ref('');
        const jwt_token=localStorage.getItem("jwt_token");
        if(jwt_token){
            //调用mutations函数使用commit
            //调用action函数用dispatch
            store.commit("updateToken",jwt_token);
            store.dispatch("getinfo",{
                success(){
                router.push({name:"home"});
                store.commit("updatePullingInfo",false);
                },
                error(){
                    store.commit("updatePullingInfo",false);
                }
            })
  
        }else{
            store.commit("updatePullingInfo",false);
        }

        const login = () =>{//箭头函数
        error_message.value=""
        store.dispatch("login",{
            username:username.value,
            password:password.value,
            success(){
                //调用mutations函数使用commit
                //调用action函数用dispatch
                store.dispatch("getinfo",{
                    success(){
                        router.push({name:'home'});
                    }
                })
            },
            error(){
               error_message.value="用户名或密码错误"
            }
        }
      
        )//想调用action中的函数要用dispatch
        }
        return {
            username,
            password,
            error_message,
            login, 
        }
        }
    }
    </script>
    <style scoped>
    button{
        width: 100%;
    }
    .error-message{
        color: red;
    }
    </style>