const user = {
  state: {
    id: 0,
    username: "",
    nickname: "",
    avatar: "",
    role: null
  },
  mutations: {
    setUser(state, obj){
      state.id = obj.id;
      state.username = obj.username;
      state.nickname = obj.nickname;
      state.avatar = obj.avatar;
      state.role = obj.role;
    },
    removeUser(state){
      state.id = 0;
      state.username = "";
      state.nickname = "";
      state.avatar = "";
      state.role = null;
    }
  }
}
export default user;
