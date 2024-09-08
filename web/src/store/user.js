import $ from 'jquery'
export default{
    state: {
        id:"",
        username:"",
        photo:"",
        token:"",
        is_login:false,
        pulling_info:true,//是否正在从云端拉取信息
    },
    getters: {
    },
    mutations: {
        updateUser(state,user){
            state.id=user.id;
            state.username=user.username;
            state.photo=user.photo;
            state.is_login=user.is_login;
            },
        updateToken(state,token){
            state.token=token;
        },
        logout(state){
            state.id="";
            state.username="";
            state.photo="";
            state.token="";
            state.is_login=false;
        },
        updatePullingInfo(state,pulling_info){
            state.pulling_info=pulling_info;
        }
    },
    actions: {
        login(context,data){
        //，context参数可以表示请求的上下文信息，包括请求头、请求体、用户信息等
        //context通常是指在Vuex中用于调用mutations或actions的上下文对象。
        //data包括用户名密码
        //data是一个对象，用于传递登录所需的用户名和密码，以及处理成功和错误响应的回调函数。
        $.ajax({
            url:"https://www.ljy5960.cn/api/user/account/token/",
            type:"post",
            data:{
                username:data.username,
                password:data.password,
            },

        //    context参数：
        //    context.commit("updateToken", resp.token): 通过调用context对象的commit方法，
        //    将resp.token提交给名为updateToken的mutation。
        //    data参数：
        //    data.username和data.password：用来发送登录请求所需的用户名和密码。
        //    data.success和data.error：分别在请求成功和失败时调用的回调函数。
            success(resp){
                if(resp.error_message==="success"){
                    localStorage.setItem("jwt_token",resp.token);
                    //调用mutations函数使用commit
                    //调用action函数用dispatch
                    context.commit("updateToken",resp.token);//调用函数用字符串调用，更新token
                    data.success(resp);
                    //调用该函数时需要将两个回调函数传入
                     //回调函数将结果回调给调用者
                }else{
                    data.error(resp);
                    //回调函数将结果回调给调用者
                }
            },
            error(resp){
                data.error(resp);
            }
        });
        },
        getinfo(context,data){
            $.ajax({
                url:"https://www.ljy5960.cn/api/user/account/info/",
                type:"get",
                headers:{
                    Authorization:"Bearer "+context.state.token,
                },
                success(resp){
                    if(resp.error_message==="success"){
                        context.commit("updateUser",{
                            ...resp,//将resp解析出来
                            is_login:true
                        })
                        data.success(resp);
                    }else{
                        data.error(resp);
                    }
                },
                error(resp){
                    data.error(resp);
                }
            })
        },
        logout(context){
        localStorage.removeItem("jwt_token");
        context.commit("logout");
        }
    },
    modules: {
    }
}