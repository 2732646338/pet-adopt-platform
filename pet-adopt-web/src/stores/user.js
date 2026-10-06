import { defineStore } from 'pinia'
export const useUserStore = defineStore('user',{
  state(){
    let userInfo = {}
    try{
      userInfo = JSON.parse(localStorage.getItem('user') || '{}')
    }catch(e){
      userInfo = {}
    }
    return {
      token: localStorage.getItem('token') || '',
      userInfo
    }
  },
  actions:{
    login(token,user){
      this.token = token
      this.userInfo = user
      localStorage.setItem('token',token)
      localStorage.setItem('user',JSON.stringify(user))
    },
    setUserInfo(user){
      this.userInfo = user
      localStorage.setItem('user',JSON.stringify(user))
    },
    logout(){
      this.token = ''
      this.userInfo = {}
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})