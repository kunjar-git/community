package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    private AlphaService alphaService;
    @RequestMapping("/hello")
    @ResponseBody
    // 返回文字，非html
    public String StringReturn() {
        return "hello new user!";
    }
    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }
    // 底层的servlet实现方式

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());

        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("<h1>牛客网</h1>");
    }
    // get请求
    // student?current=1&limit=20
    @RequestMapping(path = "/student", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(@RequestParam(name = "current", required = false, defaultValue = "0") int current,
                            @RequestParam(name = "limit", required = false, defaultValue = "1") int limit){
        System.out.println(current);
        System.out.println(limit);
        return "??";
    }
    //
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudentsbyId(@PathVariable("id") int id){
        System.out.println(id);
        return "??";
    }

    // post请求
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age){
        System.out.println("name = " + name + "age = " + age);
        return "success";
    }

    // 响应HTML数据
    @GetMapping(path = "/teacher")
    public ModelAndView getTeacher() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "Shk");
        modelAndView.addObject("age", "22");
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }
    @GetMapping(path = "/school")
    public String getTeacher(Model model) {
        model.addAttribute("name", "NJU");
        model.addAttribute("age", "22");
        return "/demo/view";
    }
    @GetMapping(path = "/emp")
    @ResponseBody
    public Map<String,Object> getEmp() {
        Map<String,Object> map = new HashMap<>();
        map.put("name", "NJU");
        map.put("age",121);
        return map;
    }
    // 相应JSON数据，异步请求AJax
    @GetMapping("/cookie/set")
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        //key->value
        Cookie cookie = new Cookie("code",CommunityUtil.generateUUID());
        // 设置生效范围
        cookie.setPath("/community/alpha");
        // 设置生存时间
        cookie.setMaxAge(60 * 10);
        // 发送cookie
        response.addCookie(cookie);
        return "set cookie";
    }
    @GetMapping("/cookie/get")
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get cookie";
    }
    @GetMapping("/session/set")
    @ResponseBody
    public String setSession(HttpSession session) {
        session.setAttribute("id", 1);
        session.setAttribute("name", "Test");
        return "set session";
    }
    @GetMapping("/session/get")
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }
    // ajax示例
    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0, "操作成功!");
    }
}
