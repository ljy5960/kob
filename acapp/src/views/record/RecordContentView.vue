<template>
    <ContentField>
        <PlayGround></PlayGround>
        <BackButtonRecord></BackButtonRecord>
    </ContentField>
    <!-- 在template中使用全局变量用$,在js中去掉$ -->
</template>
<script>
import PlayGround from '@/components/PlayGround.vue';
import ContentField from '@/components/ContentField.vue'
import BackButtonRecord from '@/components/BackButtonRecord.vue';
// onMounted当组件被挂载时执行,当组件被卸载执行onUnmounted
export default{
    components:{
        PlayGround,
        ContentField,
        BackButtonRecord,
    },
    setup(){
   
}}

</script>
<style scoped>
</style>