package com.boot.exam.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.boot.exam.bean.UserBean;
import com.boot.exam.mapper.UserMapper;
import com.boot.exam.util.FileUtil;

@Controller // 使类成为控制器，它就会被SpringMvc管理
public class IndexController extends BaseController {
	@Autowired
	UserMapper userMapper;
	
	
	@RequestMapping("/upload")
	public void upload(MultipartFile file, HttpServletResponse resp) {
		String fileName = file.getOriginalFilename(); // 获取文件的原文件名
		print(fileName);
		// 文件/文件夹操作工具
		FileUtil.createFile("E:/create/exam/logo"); // 创建文件夹
		// file图片转存
		try {
			file.transferTo(new File("E:/create/exam/logo/" + fileName));
		} catch (Exception e) {
			print("请检查磁盘路径是不是写错了...");
		} // 保存成功之后，将图片的保存路径返回给网页js中
		// outRespJson  1.直接返回给网页一个字符串   2.给网页返回一个JSON字符串
		// 浏览器不能去盘符下找文件
		outRespJson("/exam/logo/" + fileName, resp); // resp是响应对象
	}
	
	//   http://localhost:8080/register?username=xxx&password=xxx
	@RequestMapping("/register")
	public String register(UserBean bean) {
		if (StringUtils.isBlank(bean.username)) {
			return "redirect:/index.html?msg=" + getUTF8Param("用户名不能为空");
		}
		try {
			bean.status = "老师"; // 注册的是一个老师，身份是老师
			bean.name = bean.username + "老师";
			userMapper.insert(bean); // 有可能会重复注册，一旦用户名重复会报错
			return "redirect:/index.html?msg=" + getUTF8Param(bean.username+"注册成功请登录");
		} catch (Exception e) {
			// 一旦userMapper.insert(bean);报错，就会立即跳转到catch中执行代码
			return "redirect:/index.html?msg=" + getUTF8Param(bean.username+"已经注册过了！");
		}
	}

	//   http://localhost:8080/  ---->  static/index.html   SpringBoot首页
	//   http://localhost:8080/login?username=xxx&password=xxx 或者通过表单传进来
	@RequestMapping("/login") // 给这个方法配置一个可以访问的地址
	public String login(UserBean bean) { // username password
		bean.status = "老师";
		UserBean user = userMapper.selectUser(bean);
		if (user == null) { // redirect 重定向
			String msg = getUTF8Param("用户名或密码错误");
			// 进行编码，让它支持中文
			return "redirect:/index.html?msg=" + msg;
		} else { // 登录成功要进入到系统主页面
			return "redirect:/main?uid=" + user.id;
		}
	}
	
	// 登录成功之后跑这里了
	@RequestMapping("/main") // HttpServletRequest是一个隐藏参数，需要用时  再调出来
	public String main(int uid, HttpServletRequest req) {//BaseMapper selectById()按照id找到这一行
		req.setAttribute("bean", userMapper.selectById(uid)); // 转发带参数到网页中
		// req.setAttribute("uid", uid); // 把登录成功用户的id带到 main.html 中
		return "/main"; // 转发到templates里面的 /main.html 网页中
	}
	
	// 重定向
	// 语法："redirect:/地址"
	// 可以打开static里面的文件
	// 不可以打开templates里面的文件
	// 打开网页时带参数到网页中    redirect:/xxx/xxx/xxx?参数名=参数值
	// 重定向是浏览器的行为，在浏览器地址栏中可以看到地址
	// 可以定向到Controller中的地址
	
	// 转发
	// 语法：直接写地址 "地址" 此时不能加后缀
	// 只能打开templates里面的文件
	// 转发不能打开static里面的文件
	// 转发是Java的行为，浏览器地址栏不会看到转发的新地址
	// 通过 req.setAttribute("变量名", 值); 带参到网页中
	
}
