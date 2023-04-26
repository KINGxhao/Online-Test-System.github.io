const util = require('../../utils/util.js') // 找到 util.js 这个文件

Page({
  data: {
    exam: [],
  },
  onLoad(options) {
    util.http('/wx/exam', resp=>{
      this.data.exam = resp.exam
      this.setData(this.data) // 更新页面
    })
  },
  exam(e) { // data-item="{{item}}"  
    // 如果这个人还没考试，才可以进去考试
    var exam = e.currentTarget.dataset.item
    util.http('/wx/check?exam='+exam+'&uid='+wx.getStorageSync('uid'), resp=>{
      if (resp.code == 1) { // 成功
        util.redirect({
          url: 'exam', // 直接写上要跳转页面的名字即可
          exam: exam  // 每一个学科
        })
      } else { // 失败
        util.alert(resp.msg)
      }
    })
  
  },
  onPullDownRefresh() {
    util.http('/wx/exam', resp=>{
      this.data.exam = resp.exam
      util.stopPullSetData(this) // 数据已经更新出来，停止刷新动作
    })
  }
})