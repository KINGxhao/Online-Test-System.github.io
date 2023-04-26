package com.boot.exam.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boot.exam.mapper.ProductMapper;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductMapper productMapper;
	//     /product/list
	@RequestMapping("/list")
	public String list(HttpServletRequest req) { // 带参到网页中
		req.setAttribute("productList", productMapper.selectList(null)); // 查询全部数据
		return "/product/list";
	}
	
}
