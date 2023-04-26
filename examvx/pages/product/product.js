const util = require('../../utils/util.js')

Page({
  data: {
    products: [], // 被填充值了
  },
  onLoad(options) { // 这个页面的主函数
    util.http('/wx/getProducts', resp=>{
      console.log(resp)
      // 左边是自己的products    右边的是java的products
      this.data.products = resp.products
      this.setData(this.data)
    })
  },
  onPullDownRefresh() {
    util.http('/wx/getProducts', resp=>{
      console.log(resp)
      // 左边是自己的products    右边的是java的products
      this.data.products = resp.products
      util.stopPullSetData(this)
    })
  }
})