import { createStore } from 'vuex'

 const store = createStore({
  state: {
    user:{}
  },
  getters: {
  },
  mutations: {
    setUser(state,user){
      console.log("store user: ", user);
      state.user = user;
    }
  },
  actions: {
  },
  modules: {
  }
})


export default store