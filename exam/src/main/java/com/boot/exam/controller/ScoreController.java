package com.boot.exam.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boot.exam.mapper.ExamMapper;
import com.boot.exam.mapper.UserMapper;
import com.google.gson.Gson;

@Controller
@RequestMapping("/score")
public class ScoreController {
	@Autowired
	ExamMapper examMapper;
	@Autowired
	UserMapper userMapper;
	// http://localhost:8080/score/echarts?exam=java
	@RequestMapping("/echarts") // 传入java
	public String echarts(String exam, HttpServletRequest req) {
		List<Integer> uidList = examMapper.selectUid(exam);
		req.setAttribute("exam", exam); // 再把它带回网页
		List<Integer> scoreList = new ArrayList<>(); // 分数数组
		List<String> nameList = new ArrayList<>(); // 人名数组
		for (int uid : uidList) { // 每个人都查一遍
			int count = examMapper.selectCount(uid, exam);
			int yes = examMapper.selectYesCount(uid, exam);
			int score = yes * 100 / count;
			scoreList.add(score);
			
			nameList.add(userMapper.selectById(uid).name);
		}
		Gson gson = new Gson();
		req.setAttribute("scoreList", gson.toJson(scoreList)); // "[1,3,4,5,6,7]"
		req.setAttribute("nameList", gson.toJson(nameList));
		return "/score/echarts"; // templates   /score/echarts.html
	}
	
}
