package com.boot.exam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.exam.bean.ExamBean;

public interface ExamMapper extends BaseMapper<ExamBean> {
	// 查一下他答了多少个
	@Select("select count(*) from v_exam where uid=#{uid} and username=#{exam}")
	int selectCount(@Param("uid")int uid, @Param("exam")String exam);
	
	// 查一下他答对了多少个
	@Select("select count(*) from v_exam where uid=#{uid} and username=#{exam} and ret=1")
	int selectYesCount(@Param("uid")int uid, @Param("exam")String exam);
	
	// 查一下谁答完卷子了
	@Select("select uid from v_exam where username=#{exam} group by uid")
	List<Integer> selectUid(@Param("exam")String exam);
	
}
