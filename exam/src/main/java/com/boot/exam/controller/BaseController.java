package com.boot.exam.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class BaseController {
	
	public void respJson(String json, HttpServletResponse resp) {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter pw = null;
		try {
			pw = resp.getWriter();
			pw.print(json);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pw != null) {
					pw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	// alert
	public String jsAlert(String msg, String url, HttpServletResponse resp) {
		respJson("<script>alert('" + msg + "');window.location.href='" + url + "'</script>", resp);
		return null;
	}
	// 打印JSON
	public static void print(Object obj) {
		System.out.println(obj instanceof String ? (String) obj : new Gson().toJson(obj));
	}
	// 打印JSON并格式化
	public static void System_out_println(Object obj) {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		System.out.println(obj instanceof String ? (String) obj : gson.toJson(obj));
	}
	// 打印JSON到网页中
	public String outRespJson(Object obj, HttpServletResponse resp) {
		String json = obj instanceof String ? (String) obj : new Gson().toJson(obj);
		respJson(json, resp);
		return null;
	}
	
	public String getUTF8Param(String param) {
		try {
			return URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
