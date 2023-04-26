package com.boot.exam.bean;

import java.util.List;

public class WxResp {
	// 成功状态下，没有错误提示语
	public int code = 1; // 1代表成功   0 代表失败   默认成功
	public String msg = ""; // 提示语   默认没有提示语
	public int uid = 0; // 登录用户的id
	public String name = ""; // 登录用户的姓名
	public List<String> exam; // 学科列表
	
	
	public List<ProductBean> products;
	
	// 失败函数
	public void fail(String msg) {
		this.code = 0; // 说明失败了
		this.msg = msg; //失败状态下就会有一个提示语
	}
	
	
}
