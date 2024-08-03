

export default{
    state: {//存储全局变量
       socket:null,//socket链接
       status:"matching",//matching表示匹配界面，playing表示对战页面
       opponent_username:"",//对手
       opponent_photo:"",
       gamemap:null, //对手
    },
    getters: {//需要计算的变量通过写方法来实现对state中的变量进行计算但不能修改state中的变量
    },
    mutations: {//定义对state的操作能修改state，对state直接修改的操作，
      //mutatioon不能执行异步操作即从云端获取信息不能直接更新state，因为获取云端信息是异步操作
      //异步操作不需要等到当前操作结束后便可执行
      updateGamemap(state,gamemap){
        state.gamemap=gamemap;
      },
      updateSocket(state,socket){
      state.socket=socket;
      },
      updateOpponent(state,opponent){
        state.opponent_username=opponent.username;
        state.opponent_photo=opponent.photo
      },
      updateStatus(state,status){
        state.status=status;
      }
    },
    actions: {//定义对state的操作但不能修改state，即对state的操作方式，从云端获取信息,异步操作
    },
    modules: {//将state进行分割
      
    }
}