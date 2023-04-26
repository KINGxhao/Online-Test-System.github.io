package com.boot.exam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.exam.bean.QuestionBean;

public interface QuestionMapper extends BaseMapper<QuestionBean> {
	// 直接查询视图
	// 视图  只能查询数据，不能修改 添加 删除数据
	// SQL脚本，里面可以写判断分支结构
	@Select("<script>"
			+ "select * from v_question "
			+ "<where> "
				+ "<if test='exam!=null and exam!=\"\"'>"  // exam!=null 并且 exam!=空字符串
					+ "username = #{exam}"
				+ "</if>"
				+ "<if test='quit!=null and quit!=\"\"'>"
					+ "and quit = #{quit}"
				+ "</if>"
			+ "</where>"
		+ "</script>")
	List<QuestionBean> selectList(@Param("exam")String exam, @Param("quit")String quit);
	// ["java", "html5", "python"]
	@Select("select username from v_question group by username")
	List<String> selectExam(); // 选择考试类型，选择学科
	
	// 统计一下没有被弃用的题目，一共有哪几个学科
	@Select("select username from v_question where quit='正常' group by username")
	List<String> selectRunningExam();
	
}
