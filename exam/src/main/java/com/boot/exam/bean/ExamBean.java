package com.boot.exam.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("tbl_exam")
public class ExamBean { // 一张表对应一个Bean

	// 如果网页传一个空的id，就说明它想做添加操作(在添加的时候主键是自动增长的)
	// 如果网页传的是一个不为空的id，就说明它想做修改，按照主键id做相应的修改
	@TableId(type=IdType.AUTO) // 必须规定主键是自动增长类型
	public Integer id; // 数据库主键不会为空，但网页在传递主键的时候有可能传个空的
	public int uid; // 哪个人
	public int qid; // 哪道题
	public int ret; // 有没有做对
	
	// 框架需要set get
	// 生成set get方法
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	
	
	
	
	
	
}
