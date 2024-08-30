export class Cell{//存放蛇的位置
    constructor(r,c){
        this.r=r;
        this.c=c;
        this.x=c+0.5;
        this.y=r+0.5;
    }
}