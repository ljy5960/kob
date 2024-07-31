<template>
    <ContentField>我的Bot</ContentField>
    </template>
    <script>
    import ContentField from "@/components/ContentField.vue";
    import $ from 'jquery'
    import { useStore } from "vuex";
    export default{
        components:{
            ContentField
        },
        // setup(){
        //     const store=useStore();
        // $.ajax({
        // url:"http://localhost:9090/user/bot/add/",
        // type:"post",
        // data:{
        //     title:"我的bot",
        //     description:"我的bot",
        //     content:"代码"

        // },
        // headers:{
        // Authorization:"Bearer "+store.state.user.token
        // },
        // success(resp){
        //     console.log(resp);
        // },
        // error(resp){
        //     console.log(resp);
        // }
        // })
        // }
        // setup(){
        //     const store=useStore(); 
        //     $.ajax({
        //         url:"http://localhost:9090/user/bot/remove/",
        //         type:"post",
        //         data:{
        //             bot_id: 5,
        //         },
        //         headers:{
        //      Authorization:"Bearer "+store.state.user.token
        //       },
        //         success(resp){
        //      console.log(resp);
        // },
        //  error(resp){
        //    console.log(resp);
        // }
        //     })
        // }
        // setup(){
        //     const store=useStore();
        // $.ajax({
        // url:"http://localhost:9090/user/bot/update/",
        // type:"post",
        // data:{
        //     bot_id: 6,
        //     title:"我的botq",
        //     description:"我的botq",
        //     content:"代码q"

        // },
        // headers:{
        // Authorization:"Bearer "+store.state.user.token
        // },
        // success(resp){
        //     console.log(resp);
        // },
        // error(resp){
        //     console.log(resp);
        // }
        // })
        // }
        setup(){
            const store=useStore();
        $.ajax({
        url:"http://localhost:9090/user/bot/getlist/",
        type:"get",
        headers:{
        Authorization:"Bearer "+store.state.user.token
        },
        success(resp){
            console.log(resp);
        },
        error(resp){
            console.log(resp);
        }
        })
        }
    }
    </script>
    <style scoped>
    </style>