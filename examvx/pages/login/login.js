const util = require('../../utils/util.js') // 找到 util.js 这个文件

Page({
  data: { // 这个页面的全局变量
    username: '', // 第一个输入框内容
    password: '', // 第二个输入框内容
    name: '',     // 第三个输入框内容
    hidden: true,
  },
  onLoad(options) { // 页面的主函数
    if (wx.getStorageSync('uid')) { // 说明之前登录过了
      wx.switchTab({ // 切换到哪个页面 -> 首页
        url: '../index/index',
      })
    }
  },
  onInput(e) {
    this.data[e.currentTarget.dataset.name] = e.detail.value
  },
  submit() {
    util.http('/wx/login', this.data, resp=>{
      if (resp.code == 1) {
        console.log(resp.uid + '------------' + resp.name)
        // wx是小程序官方对象，是一个官方工具
        wx.setStorageSync('uid', resp.uid)    // 存储uid，存到小程序内部
        wx.setStorageSync('name', resp.name)  // 存储name，存到小程序内部

        wx.switchTab({ // 切换到哪个页面 -> 首页
          url: '../index/index',
        })
      } else {
        util.alert(resp.msg)
        if (resp.msg == '请注册该学号') {
          this.data.hidden = false
          this.setData(this.data) // 一旦全局变量发生改变，就需要加这句话更新页面
        }
      }
    })
  }
})