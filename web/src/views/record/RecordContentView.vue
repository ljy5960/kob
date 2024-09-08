<template>
    <PlayGround></PlayGround>
    <div class="button-field">
        <button type="button" class="btn btn-success" style="" @click="click_button">返回</button>
    </div>
    <!-- 在template中使用全局变量用$,在js中去掉$ -->
</template>
<script>
import PlayGround from '@/components/PlayGround.vue';
import router from '@/router';
// onMounted当组件被挂载时执行,当组件被卸载执行onUnmounted
export default{
    components:{
        PlayGround,
    },
    setup(){
   const click_button=()=>{
    router.push({name:'record_index'})
   }
   return{
    click_button,
   }
}}

</script>
<style scoped>
.button-field{
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: right;
    align-items: center;
    padding-right: 3vw;
    
}
</style>