<template>
<div ref="parent" class="gamemap">
    <canvas ref="canvas" tabindex="0"></canvas>
</div>
</template>
<script>
import { GameMap } from '@/assets/scripts/GameMap';
import { ref , onMounted} from 'vue'
import { useStore } from 'vuex';
export default{
    setup(){
        // canva的引入
        const store=useStore();
    let parent = ref (null);//指向
    let canvas = ref (null);//指向
    onMounted(()=>{
        //挂载后创建对象
    new GameMap(canvas.value.getContext('2d'),parent.value,store);
    });
    return {
        parent,
        canvas
    }
}
}
</script>
<style scoped>
.gamemap{
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
<!-- display: flex;

解释：将元素的显示类型设置为 flex 容器，使其子元素成为 flex 项目。Flexbox 是一种一维布局模型，用于横向或纵向排列子元素。
作用：启用 Flexbox 布局，使得子元素可以方便地水平或垂直对齐和分布。
justify-content: center;

解释：在水平轴上（主轴）将 Flex 项目对齐到容器的中心。只有在元素的 display 属性设置为 flex 或 inline-flex 时，此属性才有效。
作用：使所有子元素在容器中水平居中排列。
align-items: center;

解释：在垂直轴上（交叉轴）将 Flex 项目对齐到容器的中心。只有在元素的 display 属性设置为 flex 或 inline-flex 时，此属性才有效。
作用：使所有子元素在容器中垂直居中排列。 -->