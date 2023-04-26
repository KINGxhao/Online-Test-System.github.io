const util = require('../../utils/util.js') // 找到 util.js 这个文件
const app = getApp() // 获取 app.js 全局js文件
// Java控制规则：每个学科一共作答5道题，1道题20分
//    情况1：这个学科就只有不足5道题，像C只有1道题，就答这一道题就行了
//    情况2：这个学科有非常多的题目，随机抽取5道题，显示在小程序中
//          就会形成一种效果：每个人拿到的考卷都不一样

Page({
  data: { // 如果要使用全局变量，一定要加 this.data.前缀
    api: app.globalData.api, //  localhost...
    exam: '',     // 空字符串
    question: [], // 空数组
    currentQ: {}, // 空对象
    index: 0,     // 数组下标(5道题 题号)
    json: '', // 给Java准备的
  },
  onLoad(options) { // options从上一个页面带了一个参数到下一个页面来了
    if (options) {
      this.data.exam = options.exam
      util.http('/wx/random?exam='+this.data.exam, resp=>{
        this.data.question = resp
        this.data.currentQ = this.data.question[0] // 此时currentQ就是第一道题
        console.log(resp) // 有的时候需要观察数据
        this.setData(this.data)
      })
    }
  },
  answer(e) {
    var result = e.currentTarget.dataset.answer // 作答的答案，可能对，也可能错
    this.data.currentQ.result = result
    this.data.question[this.data.index].result = result
    this.setData(this.data) // 把变化的数据更新到页面中
    setTimeout(()=>{ // 简写形式   this作用域指向了当前页面
      this.switch()
    }, 500) // 等待0.5s
  },
  switch() { // 自己写的函数
    if (this.data.index < this.data.question.length - 1) {
      this.data.index++ // 移到下一个题号
      this.data.currentQ = this.data.question[this.data.index]
    } else {
      this.data.currentQ = {}
      this.data.currentQ.question = '已全部答完，请确认交卷'
      console.log(this.data.currentQ)
    }
    this.setData(this.data) // 更新页面
  },
  submit() { // "[{uid:1,qid:3,ret:1}, {...}, {...}]"
    console.log(this.data.question)
    var objList = [] // 空数组
    for (var bean of this.data.question) {
      var obj = {} // 先定义一个空对象obj
      obj.ret = bean.answer == bean.result ? 1 : 0
      obj.qid = bean.id
      obj.uid = wx.getStorageSync('uid')
      objList.push(obj)
    }
    console.log(objList)
    this.data.json = JSON.stringify(objList) // "[]" 地址太长了
    util.http('/wx/submit', this.data, resp=>{ // 三个参数就是POST
      util.alert('交卷成功')
      // 提交成功时，可以 this.data.json 来控制按钮的隐藏
      this.setData(this.data)
      setTimeout(()=>{
        wx.navigateBack()
      }, 1000) // 1s后 退出该页面
    })
  }
})