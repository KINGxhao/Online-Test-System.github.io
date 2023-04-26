var app = getApp()
function formatTime(date) {
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  var hour = date.getHours()
  var minute = date.getMinutes()
  var second = date.getSeconds()


  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

function formatNumber(n) {
  n = n.toString()
  return n[1] ? n : '0' + n
}

var nowWeek = function (startDate) { // [年,月,日]
  let [year, month, day] = startDate // [年,月,日]
  startDate = new Date(year, month-1, day)
  var nowDate = new Date() // 当前时间
  var diffTime = parseInt((nowDate.getTime()-startDate.getTime())/1000)
  diffTime += 24 * 60 * 60 // 再加一天
  var week = parseInt(diffTime/60/60/24/7) + 1
  if (week < 0) week = 0
  return week
}

// 弹出提示语
var alert = function (msg) {
  if (Object.prototype.toString.call(msg) === '[object Object]' ||
      Object.prototype.toString.call(msg) === '[object Array]')
    msg = JSON.stringify(msg)
  wx.showToast({
    title: msg + '',
    icon: 'none',
    duration: 2000,
    mask: true
  })
}

var http = function (url, data, callback) {
  if (!callback) {
    httpGet(url, data)
  } else {
    httpPost(url, data, callback)
  }
}

var httpLoading = function (url, data, callback) {
  if (!callback) {
    core(true, url, data)
  } else {
    core(true, url, callback, "POST", data)
  }
}

var httpGet = function (url, callback) {
  core(false, url, callback)
}

var httpPost = function (url, data, callback) {
  core(false, url, callback, "POST", data)
}

var core = function (loading, url, callback, method, data) {
  if (loading) {
    wx.showLoading()
  }
  wx.request({
    url: app.globalData.api ? (app.globalData.api + url) : url,
    method: !method ? "GET" : method,
    dataType: "json",
    data: !data ? null : data,
    header: {
      'content-type': "GET" == method ?
        'application/json' : 'application/x-www-form-urlencoded'
    },
    success: function (response) {
      let result = response.data
      if (result) {
        callback(result)
      }
      if (loading) {
        wx.hideLoading()
      }
    }
  })
}

var stopPullSetData = function (_this) {
  setTimeout(function () {
    wx.stopPullDownRefresh()
    _this.setData(_this.data)
  }, 500)
}

var getStorageObj = function (key) {
  var value = wx.getStorageSync(key)
  return value ? value : []
}

var addStorageObj = function (key, obj) {
  var value = this.getStorageObj(key)
  if (value.length) {
    var idx = 0
    for (var i in value) {
      if (obj.id == value[i].id) {
        idx = i
        break
      }
    }
    value.splice(idx, 0, obj)
  } else {
    value.push(obj)
  }
  wx.setStorageSync(key, value)
  return value.length
}

var delStorageObj = function (key, col, val, match) {
  var value = this.getStorageObj(key)
  var len = 0
  if (value.length) {
    for (var i = 0; i < value.length;) {
      if (value[i][col] == val) {
        value.splice(i, 1)
        if (!match) {
          break
        }
      } else {
        i++
      }
    }
    wx.setStorageSync(key, value)
    len = value.length
  }
  return len
}

var setTabBarBadgeNumber = function (index, len) {
  if (len <= 0) {
    wx.removeTabBarBadge({
      index: index
    })
  } else {
    wx.setTabBarBadge({
      index: index,
      text: len + ''
    })
  }
}

// 获取购物车数据核心算法
// key:'cart'，按键取值
// col:'id'，if id重复，数组去重
// count:'count'，去重之后，count++
var getStorageCart = function (key, col, count, asyncList) {
  var cart = this.getStorageObj(key)
  for (var i = 0, len = cart.length; i < len; i++) {
    cart[i][count] = 1
    for (var j = i + 1; j < len; j++) {
      if (cart[i][col] == cart[j][col]) {
        cart[i][count]++
        cart.splice(j, 1)
        len--
        j--
      }
    }
  }
  if (asyncList) {
    for (var i in asyncList) {
      asyncList[i][count] = 0
      for (var j in cart) {
        if (asyncList[i][col] == cart[j][col]) {
          asyncList[i][count] = cart[j][count]
        }
      }
    }
  }
  return cart
}

var enableAddStorageObj = function (key, col, num, obj, callback) {
  var cart = this.getStorageCart(key, col, 'count')
  var len = true
  for (var i in cart) {
    if (cart[i][col] == obj[col] && cart[i].count >= obj[num]) {
      callback()
      len = false
      break
    }
  }
  if (obj[num] == 0) {
    callback()
    len = false
  }
  return len ? this.addStorageObj(key, obj) : this.getStorageObj(key).length
}

var redirect = function (page) {
  var url = ''
  var param = ''
  if (typeof (page) != 'string') {
    for (var i in page) {
      param += i + '=' + page[i] + '&'
    }
    param = '?' + param.substring(0, param.length - 1)
    page = page.url
  }
  url = page.indexOf('/') >= 0 ? ('/pages' + page + page + param) : 
    ('/pages/' + page + '/' + page + param)
  wx.navigateTo({ url: url })
}

module.exports = {
  formatTime: formatTime,
  nowWeek: nowWeek,
  alert: alert,
  http: http,
  httpGet: httpGet,
  httpPost: httpPost,
  httpLoading: httpLoading,
  stopPullSetData: stopPullSetData,
  setTabBarBadgeNumber: setTabBarBadgeNumber,
  getStorageObj: getStorageObj,
  addStorageObj: addStorageObj,
  delStorageObj: delStorageObj,
  getStorageCart: getStorageCart,
  redirect: redirect,
  enableAddStorageObj: enableAddStorageObj
}
