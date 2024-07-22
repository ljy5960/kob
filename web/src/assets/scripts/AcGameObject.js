const AC_GAME_OBJECTS=[];

export  class AcgAameObject{
    constructor(){
        AC_GAME_OBJECTS.push(this);
        this.timedelta=0;//这一桢和上一桢的时间间隔
        this.has_called_start=false;
    }
    start(){//只执行一次

   }
    update(){//每一帧执行一次第一次除外

    }
    on_destroy(){  //删除之前执行

    }
    destroy(){//删除
        this.on_destroy();
        for(let i in AC_GAME_OBJECTS){
            const obj =AC_GAME_OBJECTS[i];
            if(obj===this){
                AC_GAME_OBJECTS.splice(i);
                break;
            }
        }
    }
}
let last_timestamp;//上一次执行的时刻
//timestamp是当前函数执行的时刻
//timestamp是浏览器传入的
const step =timestamp=>{
    
    //因此，虽然 GameMap 类在实例化时只执行了构造函数和可能的初始化方法（如 start 方法），
    //但是 requestAnimationFrame 的执行不是由对象实例化过程决定的，而是由开发者显式调用。
    //通常情况下，requestAnimationFrame 是作为整个应用或游戏引擎的主循环中的一部分来使用的，用来管理和驱动所有游戏对象的更新和渲染过程。
    //of 是值 in是下标  
    for(let obj of AC_GAME_OBJECTS){
     if(!obj.has_called_start){
        obj.has_called_start=true;
        obj.start();
     }
     else{
        obj.timedelta=timestamp-last_timestamp;
        obj.update();
     }
    }
    last_timestamp=timestamp;
    requestAnimationFrame(step)
}
// requestAnimationFrame是一次性的
requestAnimationFrame(step)