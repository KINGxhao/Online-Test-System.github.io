package com.boot.exam.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.exam.bean.UserBean;

// BaseMapper是MyBatisPlus提供的类，里面有一些增删改查方法
public interface UserMapper extends BaseMapper<UserBean> {
	// 登录，按照用户名和密码查询这个人，如果找到了就说明登录成功
	// @Select查询   @Insert添加   @Delete删除   @Update修改
	@Select("select * from tbl_user where username=#{username} and password=#{password} and status=#{status}")
	UserBean selectUser(UserBean bean); // 如果传入的是bean对象/对象，此时就不需要加@Param

	// 学生查找有没有这个账号
	@Select("select * from tbl_user where username=#{username} and status='学生'")
	UserBean selectUserByStudent(@Param("username")String username);
	
	
	@Select("select id from tbl_user where username=#{username}")
	int selectUid(@Param("username")String username);
	
}
