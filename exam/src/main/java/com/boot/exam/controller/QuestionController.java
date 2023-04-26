package com.boot.exam.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boot.exam.bean.QuestionBean;
import com.boot.exam.mapper.QuestionMapper;
import com.boot.exam.mapper.UserMapper;
import com.boot.exam.util.NotNullUtil;

@Controller // 如果想在类中配置地址，必须是Controller，使它成为控制器
@RequestMapping("/question")
public class QuestionController extends BaseController {
    @Autowired  // 相当于new
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;
    // 404 就是地址写错了 1.java地址写错 2.超链接地址写错了
    // 500错误，就是java报错，看java
    // get请求    <a href="超链接地址">  也是重定向   浏览器地址栏会发生改变
    // post请求  <form action="" method="post">
    // 其他区别：1. get请求地址在地址栏中可以看到，不太安全
    //				post请求地址栏中能看到地址，但看不到传参，相对安全
    //			2. get请求 地址长度有限制
    //				post请求地址长度没有限制，传参也没有限制

    @GetMapping("/add") // get请求，<a>按钮点进来的
    public String add(Integer id, String msg, HttpServletRequest req) { // req带参给网页
        // 三元表达式/三目表达式        条件  ?  语句1  :  语句2
        req.setAttribute("bean", id == null ? null : questionMapper.selectById(id));
        req.setAttribute("msg", msg);
        return "/question/add"; // 转发到 templates 下 /question/add.html
    }

    @PostMapping("/add")//post请求，<form>表单提交过来的
    public String add(QuestionBean bean) { // 题目  正确答案  出题人
        if (NotNullUtil.isBlank(bean)) { // 重定向 也是 get请求
            String msg = getUTF8Param("提交失败，请将信息填写完整"); // 支持中文
            return "redirect:/question/add?msg=" + msg + (bean.id == null ? "" : "&id=" + bean.id);
        }

        bean.uid = userMapper.selectUid(bean.username); // 所属学科
        if (bean.id == null) { // 添加操作，id是自动增长的
            bean.ctime = new Date(); // 在插入数据之前，设置一下ctime=当前时间
            questionMapper.insert(bean); // 把试题写入到数据库中 tbl_question表中
        } else { // id存在就是修改操作
            questionMapper.updateById(bean); // 通过id改这一行数据
        }
        return "redirect:/question/list?exam=" + bean.username; // 重新查询一下，页面中有没有这道题
    }

    @RequestMapping("/list")
    public String list(String exam, HttpServletRequest req) { // 要用转发传参，传到网页中
        System.out.println(exam);
        exam = exam == null ? "" : exam;
        req.setAttribute("exam", exam); // 再把exam带回网页，有什么用后面再说
        req.setAttribute("examList", questionMapper.selectExam());
        req.setAttribute("retList", questionMapper.selectList(exam, null)); // 重新查询一遍
        return "/question/list"; // 跳到 templates 下 /question/list.html
    }

    @RequestMapping("/del")
    public String del(int id, String exam) {
        questionMapper.deleteById(id); // 里面有SQL语句，mybatisplus已经给我们写好了
        return "redirect:/question/list?exam=" + exam;
    }

    @RequestMapping("/quit")
    public String quit(int id, String exam) { // 主键id    字符串比较是否相等 equals
        QuestionBean bean = questionMapper.selectById(id);
        bean.quit = bean.quit.equals("正常") ? "已弃用" : "正常"; // 三元表达式
        // 更新到数据库中
        questionMapper.updateById(bean);
        return "redirect:/question/list?exam=" + exam; // 刷新一下试题列表
    }

}
