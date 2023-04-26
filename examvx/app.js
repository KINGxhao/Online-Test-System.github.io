// app.js
App({ // App({}) 这是一个函数   在js中{}代表1.函数体  2.对象
  // 整个小程序的主函数，当点击编译或者保存，第一个要执行的函数就是 onLaunch()
  onLaunch() { // 函数的简写形式  onLaunch() 就是一个函数 {} 这这里代表函数体
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || [] // 存了日志，什么时间登录的
    logs.unshift(Date.now()) // logs数组内部 开头添加 Date.now()当前时间
    wx.setStorageSync('logs', logs) // 再把logs数组再存入小程序内部

    // 微信账号(你的微信账号)登录，不使用这种方式，因为要连外网
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    })
  },
  globalData: { // 整个小程序的全局变量，它的值是一个对象
    // api被封装到了util里面去了
    api: 'http://localhost:8080', // 网站域名 网站的IP地址
    userInfo: null // 用户信息
  }
})
