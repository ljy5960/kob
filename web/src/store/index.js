import { createStore } from 'vuex'
import Moduleuser from '@/store/user'
export default createStore({
  state: {//存储全局变量
  },
  getters: {//需要计算的变量通过写方法来实现对state中的变量进行计算但不能修改state中的变量
  },
  mutations: {//定义对state的操作能修改state，对state直接修改的操作，
    //mutatioon不能执行异步操作即从云端获取信息不能直接更新state，因为获取云端信息是异步操作
  },
  actions: {//定义对state的操作但不能修改state，即对state的操作方式，从云端获取信息,异步操作
  },
  modules: {//将state进行分割
    user:Moduleuser,
  }
})
