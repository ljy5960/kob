<template>
    <div class="content-field">
        <slot></slot>
        <div class="go-back" @click="click_button()">
            返回
        </div>
    </div>
    </template>
    <script>
    import { useStore } from 'vuex';
    export default{
        setup(){
            const store=useStore();
            const click_button=()=>{
            store.commit("updateRoutername","record")
            }
            return{
                click_button,
            }
        }
    }
    </script>
    <style scoped>
    .go-back{
        position: absolute;
        right: 5vh;
        bottom:5vh;
        color: white;
        font-size: 24px;
        font-style: italic;
        font-weight: 600;
        cursor: pointer;
        user-select: none;
    }
    .go-back:hover{
        scale: 1.2;
        transition: 200ms;
    }
    </style>