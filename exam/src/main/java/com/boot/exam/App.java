package com.boot.exam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
// java注解
@Configuration
@SpringBootApplication // SpringBoot项目启动类
@MapperScan("com.boot.exam.mapper") // 扫描包路径
public class App implements WebMvcConfigurer {
	// 主函数
	public static void main(String[] args) {
		SpringApplication.run(App.class, args); // Spring应用.run 跑起来这个项目
		System.out.println("(♥◠‿◠)ﾉﾞ  后台启动成功   ლ(´ڡ`ლ)ﾞ  \n" ); // 输出一个笑脸
	}
// 一旦出现8080端口被占用
// 两条命令cmd
//    netstat -ano
//    taskkill /t /f /pid 8080端口号对应的进程ID
	
/* 现在项目中所用到的框架
 SpringBoot框架 = SSM = SpringMvc + Spring + MyBatis + MyBatisPlus + Tomcat (8080)
 	SpringMvc 负责管理Controller控制层
 	MyBatis   负责管理 Mapper 数据访问层
 bean包里面放的是  普通的Java类  这个类跟数据库表有关
 任何系统一上来都要先做登录注册   所有的软件系统必须要有用户表
*/

	
//    @Bean
//    public MybatisPlusInterceptor page() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        return interceptor;
//    }
	
	@Override // 上传图片 上传视频 上传文件 需要在这里进行配置
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 做映射，当浏览器需要找到  /exam/.... 这个文件，就映射到 E:/create 下面去找
		registry.addResourceHandler("/exam/**").addResourceLocations("file:E:/create/exam/");
	}
	
}
