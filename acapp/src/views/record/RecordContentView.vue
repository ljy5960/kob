<template>
    <PlayGround></PlayGround>
    <!-- 在template中使用全局变量用$,在js中去掉$ -->
</template>
<script>
import PlayGround from '@/components/PlayGround.vue';
// onMounted当组件被挂载时执行,当组件被卸载执行onUnmounted
export default{
    components:{
        PlayGround,
    },
    setup(){
   
}}

</script>
<style scoped>
</style>