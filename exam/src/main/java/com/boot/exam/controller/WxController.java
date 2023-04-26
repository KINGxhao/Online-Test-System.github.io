package com.boot.exam.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.exam.bean.ExamBean;
import com.boot.exam.bean.QuestionBean;
import com.boot.exam.bean.UserBean;
import com.boot.exam.bean.WxResp;
import com.boot.exam.mapper.ExamMapper;
import com.boot.exam.mapper.ProductMapper;
import com.boot.exam.mapper.QuestionMapper;
import com.boot.exam.mapper.UserMapper;
import com.boot.exam.util.NotNullUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping("/wx")
public class WxController extends BaseController {
	@Autowired
	UserMapper userMapper;
	@Autowired
	QuestionMapper questionMapper;
	@Autowired
	ExamMapper examMapper;
	
	@Autowired
	ProductMapper productMapper;
	
	//     /wx/getProducts
	@RequestMapping("/getProducts")
	public void getProducts(HttpServletResponse resp) {
		WxResp wx = new WxResp();
		wx.products = productMapper.selectList(null);
		outRespJson(wx, resp);
	}

	
	
	@RequestMapping("/check")
	public void check(int uid, String exam, HttpServletResponse resp) {
		WxResp wx = new WxResp();
		int count = examMapper.selectCount(uid, exam);
		if (count != 0) { // count答了多少个
			int yes = examMapper.selectYesCount(uid, exam); // yes答对了多少个
			int score = yes * 100 / count;
			wx.fail("你考完该科目了，得了 " + score + " 分");
		}
		outRespJson(wx, resp);
	}
	
	// 小程序没有办法将数组传给Java
	// 小程序只能将字符串传给Java
	@RequestMapping("/submit") // HttpServletRequest 小程序给Java的   HttpServletResponse Java给小程序的响应
	public void submit(String json, HttpServletResponse resp) { // 接收数组  "[{uid:1,qid:3,ret:1}, {...}, {...}]"
		System.out.println(json);
		// 把长得像数组的字符串再转回数组
		// gson 谷歌提供的工具 json是字符串
		Gson gson = new Gson();
		List<ExamBean> objList = gson.fromJson(json, new TypeToken<List<ExamBean>>(){}.getType());
		for (ExamBean bean : objList) {
			examMapper.insert(bean);
		}           // WxResp默认成功
		outRespJson(new WxResp(), resp);
	}
	
	@ResponseBody
	@RequestMapping("/random")
	public List<QuestionBean> random(String exam) { // 考试科目
		List<QuestionBean> questionList = null; // 先定义好试题列表
		List<QuestionBean> allQuestion = questionMapper.selectList(exam, "正常");
		if (allQuestion.size() <= 5) {
			questionList = allQuestion;
		} else { // >5道题，随机抽取5道题   假如说总共10道题
			Random random = new Random(); // 随机数工具对象
			Set<Integer> set = new HashSet<>(); // 列表，里面的元素永远不会重复
			while (true) {
				int r = random.nextInt(allQuestion.size()); // [0,10) 随机抽个数
				set.add(r);
				if (set.size() == 5) { // 找到了这5道题的题号(下标)，题号不会重复
					break; // 跳出死循环
				}
			}
			questionList = new ArrayList<>(); // 常规列表，数据可以重复，而且还有顺序
			for (int r : set) {
				questionList.add(allQuestion.get(r));
			}
		}
		return questionList;
	}
	

	//     /wx/exam   就可以得到一个   exam数组
	@GetMapping("/exam")
	public void exam(HttpServletResponse resp) {
		WxResp wx = new WxResp(); // 默认成功
		wx.exam = questionMapper.selectRunningExam(); // [java,h5...]
		outRespJson(wx, resp); // 输出给小程序
	}
	
	
	@PostMapping("/login") // 小程序只能用POST提交
	public void login(UserBean bean, HttpServletResponse resp) {
		WxResp wx = new WxResp(); // 默认成功
		bean.status = "学生"; // 以学生的身份登录，去数据表找这个学生
		if (NotNullUtil.isBlank(bean)) { // 校验username和password
			wx.fail("请将信息填写完整");
			outRespJson(wx, resp);
			return;
		}
		
		try {
		if (StringUtils.isNotBlank(bean.name)) { // 这个人填写了姓名，因为它要注册
			userMapper.insert(bean);
			wx.uid = bean.id; // 直接获取新增数据的id
			wx.name = bean.name;
			outRespJson(wx, resp);
			return;
		}} catch (Exception e) {
		}
		
		UserBean student = userMapper.selectUserByStudent(bean.username);
		if (student == null) { // 说明没有注册这个学号
			wx.fail("请注册该学号");
			outRespJson(wx, resp); // 告诉小程序
			return;
		}
		
		UserBean user = userMapper.selectUser(bean);
		if (user == null) {
			wx.fail("用户名或密码错误"); // 登录失败
			outRespJson(wx, resp);
			return;
		}
		
		wx.uid = user.id;
		wx.name = user.name;
		outRespJson(wx, resp); // 登录成功
	}
	
}
