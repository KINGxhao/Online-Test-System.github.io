package com.boot.exam.bean;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.exam.util.NotNull;

@TableName("tbl_question")  // 试题表
public class QuestionBean {
	// xxxBean 跟 tbl_xxx 表一一对应，连里面的字段也要一一对应
	@TableField(exist=false) // 添加数据时，把这个字段看成不存在
	public String username; // 关联查询，多出一个username
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@TableId(type=IdType.AUTO) // 说明是主键，自动增长
	public Integer id; // int  Integer都表示整型，int不能为null  Integer可以为null
	@NotNull // 不能为空
	public String question;
	@NotNull
	public String answer;
	public int uid; // 外键，可以用int
	public String logo; // 此处指的是  图片的保存地址，保存地址是字符串类型
	public Date ctime; // util.Date
	public String quit; // 正常     弃用
	// 最后一步，生成set get方法
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	public String getQuit() {
		return quit;
	}
	public void setQuit(String quit) {
		this.quit = quit;
	}
	
	
	
	
	
}
