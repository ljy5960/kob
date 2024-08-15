
export default{
    state: {
      is_record:false,//蛇内判断
      a_step:[],
      b_step:[],
      record_loser:"",
    },
    getters: {
    },
    mutations: {
      updateIsRecord(state,is_record){
        state.is_record=is_record;
      },
      updateSteps(state,data){
        state.a_step=data.a_step;
        state.b_step=data.b_step;
      },
      updateRecordLoser(state,loser){
        state.record_loser=loser;
      }

    },
    actions: {
    },
    modules: {
      
    }
}