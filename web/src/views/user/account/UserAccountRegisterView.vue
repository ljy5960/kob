<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
               <form @submit.prevent="register"><!--@submit.prevent="login" 将submit与login函数绑到一起prevent阻止默认行为-->
                    <div class="mb-3">
                      <label for="username" class="form-label">用户名</label>
                      <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div><!--v-model="password"将下面的变量与组件绑定-->
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                      </div>
                      <div class="mb-3">
                        <label for="password" class="form-label">确认密码</label>
                        <input v-model="confirmedPassword" type="password" class="form-control" id="confirmedPassword" placeholder="请再次输入密码">
                      </div>
                    <div class="error-message">{{ error_message }}</div>
                    <div class="mb-3">
                        <router-link :to="{name:'user_account_login'}">去登陆</router-link>
                    </div>
                    <button type="submit" class="btn btn-primary">注册</button>
                 </form>
            </div>
           </div>
    </ContentField>
    </template>
    <script>
    import ContentField from "@/components/ContentField.vue";
    import { ref } from "vue";
    import router from "@/router";
    import $ from 'jquery';
    export default{
        components:{
            ContentField
        },

        setup(){
       let username=ref('');
       let password=ref('');
       let confirmedPassword=ref('');
       let error_message=ref('');
       const register=()=>{
        error_message.value="";
        $.ajax({
            url:"https://www.ljy5960.cn/api/user/account/register/",
            type:"post",
            data:{
           username:username.value,
           password:password.value,
           confirmedPassword:confirmedPassword.value,
            },
            success(resp){
                if(resp.error_message==="success")
                {  router.push({name:'user_account_login'})}
                else{
                    error_message.value=resp.error_message;
                }
            },
        });
       }
       return {
        username,
        password,
        confirmedPassword,
        error_message,
        register,
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