<template>
    <div class="content-field">
        <div class="playground">
            <GameMap></GameMap>
        </div>
    </div>
</template>
<script>
import  GameMap  from '@/components/GameMap.vue';
export default{
    components:{
        GameMap,
    }
}
</script>
<style scoped>
.playground{
    width: 40vw;
    height: 50vh;
}
.content-field{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
}
</style>