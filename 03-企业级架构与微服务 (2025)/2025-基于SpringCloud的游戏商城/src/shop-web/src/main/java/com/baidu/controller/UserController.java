package com.baidu.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.baidu.feignservice.RoleFeignService;
import com.baidu.feignservice.UserFeignService;
import com.baidu.feignservice.OrderFeignService;
import com.baidu.pojo.Cart;
import com.baidu.pojo.Role;
import com.baidu.pojo.User;
import com.baidu.pojo.Order;
import com.baidu.utils.AuthUtils;
import com.baidu.utils.PageResult;
import com.baidu.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RoleFeignService roleFeignService;
    @Autowired
    private UserFeignService userFeignService;
    @Autowired
    private OrderFeignService orderFeignService;
    @Autowired
    private com.baidu.feignservice.ProductFeignService productFeignService;
    @Autowired
    private com.baidu.feignservice.CartFeignService cartFeignService;
    @Autowired
    private com.baidu.feignservice.LocationFeignService locationFeignService;

    @PostMapping("/sendSms")
    @ResponseBody
    public R sendSms(String cellphone, String graphicCode, HttpSession session) {

        // 1. 基本校验
        if (cellphone == null || graphicCode == null) {
            return R.error().put("msg", "手机号或图形验证码不能为空");
        }

        // 2. 从 Session 中取出第一步存进去的图形验证码正确答案
        String correctGraphicCode = (String) session.getAttribute("GRAPHIC_CAPTCHA");
        if (correctGraphicCode == null) {
            return R.error().put("msg", "图形验证码已过期，请点击图片刷新重试");
        }

        // 3. 将用户填写的和正确答案比对（equalsIgnoreCase 忽略大小写）
        if (!graphicCode.equalsIgnoreCase(correctGraphicCode)) {
            return R.error().put("msg", "图形验证码错误，请重新输入");
        }

        // 3.5 核心逻辑：获取验证码前，必须先确认该手机号是不是真的是我们的注册用户！
        User dbUser = userFeignService.getUserByPhone(cellphone);
        if (dbUser == null) {
            return R.error().put("msg", "该手机号尚未注册，请检查或先去注册");
        }

        // 4. 恭喜！图形验证码校验通过，且用户存在，开始生成 6 位短信数字验证码
        String smsCode = RandomUtil.randomNumbers(6);

        // 5. 【核心步骤】把这 6 个数字存入 Session，键名必须绑定手机号！
        // 比如存成：SMS_CODE_13812345678 -> 654321，有效期通常跟着 Session 走（默认 30 分钟）
        session.setAttribute("SMS_CODE_" + cellphone, smsCode);

        // 6. 模拟调用第三方短信接口（未来买短信服务后，在这里写发送逻辑）
        System.out.println("======================================");
        System.out.println("【模拟发送短信】目标手机号: " + cellphone);
        System.out.println("【模拟发送短信】您的短信验证码是: " + smsCode);
        System.out.println("======================================");

        // 7. 发完短信后，立刻废弃当前的图形验证码，防止别人拿旧验证码狂刷短信！
        session.removeAttribute("GRAPHIC_CAPTCHA");

        return R.right().put("msg", "短信验证码已发送");
    }

    /**
     * 校验短信验证码
     */
    @PostMapping("/verifySmsCode")
    @ResponseBody
    public R verifySmsCode(String cellphone, String vercode, HttpSession session) {
        // 1. 从 Session 拿出正确的短信验证码
        String correctSmsCode = (String) session.getAttribute("SMS_CODE_" + cellphone);
        
        if (correctSmsCode == null) {
            return R.error("验证码已过期或未发送，请重新获取");
        }
        
        // 2. 比对用户填写的数字
        if (!vercode.equals(correctSmsCode)) {
            return R.error("短信验证码错误！");
        }
        
        // 3. 【核心环节】验证成功！
        session.setAttribute("RESET_PASSWORD_PHONE", cellphone);
        session.removeAttribute("SMS_CODE_" + cellphone);
        
        return R.right("身份验证通过");
    }

    /**
     * 执行重置密码
     */
    @PostMapping("/resetPassword")
    @ResponseBody
    public R resetPassword(String password, HttpSession session) {
        String cellphone = (String) session.getAttribute("RESET_PASSWORD_PHONE");
        if (cellphone == null) {
            return R.error("非法访问！请重新验证身份");
        }

        boolean success = userFeignService.updatePasswordByPhone(cellphone, password); 

        if (success) {
            session.removeAttribute("RESET_PASSWORD_PHONE");
            session.removeAttribute("user1"); // 强制下线，要求重新登录
            return R.right("密码重置成功");
        }
        return R.error("密码重置失败，可能该手机号未注册");
    }

    //获取图形验证码
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response,HttpSession session) throws IOException {
        // 1. 生成带有干扰线的图形验证码（长116宽36，包含4个字符，10条干扰线）
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(116, 36, 4, 10);
        // 2.【核心步骤】将验证码的正确答案（如"A8B2"）存入Session中
        // 这样下一步发送短信时，我们就可以从Session里拿出来跟用户输入的做比对了
        session.setAttribute("GRAPHIC_CAPTCHA", lineCaptcha.getCode());
        // 3. 告诉浏览器我们要返回的是一张图片，不要缓存
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");

        // 4. 将画好的验证码图片通过流的形式写给前端
        lineCaptcha.write(response.getOutputStream());
        response.getOutputStream().close();
    }


    //跳转到忘记密码页面
    @GetMapping("/toForget")
    public String toForget(){
        System.out.println("--------toForget--------");
        return "/user/forget";
    }

    //注册账号
    @GetMapping("/userRegister")
    @ResponseBody
    public R userRegister(@RequestParam("name")String name,@RequestParam("password") String password,@RequestParam("telephone")String telephone){
        System.out.println("--------userRegister--------name:"+name+" password:"+password);
        
        // 校验账号是否被注册
        User existingUser = userFeignService.getUserByName(name);
        if(existingUser != null){
            return R.error("账号已被注册");
        }

        User user = new User();

        user.setCreateTime(new Date());//设置创建时间
        user.setRoleId(2);//默认是普通用户
        user.setName(name);
        user.setPassword(password);
        user.setTelephone(Long.parseLong(telephone));

        boolean flag = userFeignService.addUser(user);
        if (flag)
            return R.right("注册成功！");
        return R.error("注册失败请重试！");
    }

    //跳转到注册界面
    @GetMapping("/toResgister")
    public String  toResgister(){
        System.out.println("------toResgister--------");
        return "user/reg";
    }
    
    //跳转到最终lastPay界面
    @GetMapping("/lastPay")
    public String lastPay(HttpSession session){
        System.out.println("--------lastPay-------");
        // 获取当前订单，修改支付状态和支付时间
        Order order = (Order) session.getAttribute("order");
        if (order != null) {
            order.setStatus("已支付");
            order.setGoodsStatus("未发货"); //支付后应该是未发货，等待卖家发货
            order.setPayTime(new Date());
            orderFeignService.getOrderById(order);
            

            List<Integer> buyingCids = (List<Integer>) session.getAttribute("buyCids");
            if (buyingCids != null && !buyingCids.isEmpty()) {
                Integer[] cidsArray = buyingCids.toArray(new Integer[0]);
                cartFeignService.deleteCartByCids(cidsArray);
                session.removeAttribute("buyCids");
                session.removeAttribute("buyPids");
            } else {
                List<Integer> buyingPids = (List<Integer>) session.getAttribute("buyPids");
                if (buyingPids != null && !buyingPids.isEmpty()) {
                    session.removeAttribute("buyPids"); // 结算完成，清空缓存
                }
            }
            // ===========================================

            // 更新 session，确保后续页面回显的是最新状态
            session.setAttribute("order", order);
        }
        return "user/lastPay";
    }

    //跳转到支付界面3
    @GetMapping("/toPay3")
    public String toPay3(){
        System.out.println("--------toPay3--------");
        return "user/pay3";
    }

    //跳转到支付页面
    @GetMapping("/toPay")
    public String toPay(HttpSession session, org.springframework.ui.Model model){
        System.out.println("--------toPay-------");
        User user = (User) session.getAttribute("user1");
        if(user != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("uid", user.getUid());
            map.put("page", 1);
            map.put("limit", 100);
            PageResult<com.baidu.pojo.Location> page = locationFeignService.getPage(map);
            model.addAttribute("addressList", page.getData());
        }
        return "user/pay";
    }


    //退出登录
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        System.out.println("---------logout------");
        session.invalidate();
        return "redirect:/user/toLogin";
    }
    //修改用户信息
    @PostMapping("/editUser")
    @ResponseBody
    public R editUser(User user){
        System.out.println("-------editUser-------");
        boolean flag = userFeignService.editUser(user);
        if (flag){
            return R.right();
        }
        return R.error();
    }


    //跳转到修改页面
    @GetMapping("/toEditUser/{uid}")
    public String toEditUser(@PathVariable("uid") Integer uid,HttpServletRequest request){
        System.out.println("---------toEditUser--------");
        User user = userFeignService.getUser(uid);
        List<Role> list = roleFeignService.getAll();
        request.setAttribute("list",list);
        request.setAttribute("user",user);
        return "user/edit";
    }

    // ========== 个人信息修改模块 ==========
    @GetMapping("/toMyInfo")
    public String toMyInfo(HttpSession session, HttpServletRequest request) {
        User user1 = (User) session.getAttribute("user1");
        if (user1 != null) {
            User user = userFeignService.getUser(user1.getUid());
            request.setAttribute("user", user);
        }
        return "user/myInfo";
    }

    @PostMapping("/updateMyInfo")
    @ResponseBody
    public R updateMyInfo(User user, HttpSession session) {
        User user1 = (User) session.getAttribute("user1");
        if (user1 == null) return R.error().put("msg", "请先登录");
        user.setUid(user1.getUid()); // 确保只能修改自己的信息
        user.setRoleId(user1.getRoleId()); // 保护角色不被修改
        boolean flag = userFeignService.editUser(user);
        if (flag) {
            // 更新 session 中的用户信息，以便头部的名字刷新
            user1.setName(user.getName());
            user1.setTelephone(user.getTelephone());
            session.setAttribute("user1", user1);
            return R.right();
        }
        return R.error();
    }

    //删除单个用户
    @PostMapping("/deleteUser/{uid}")
    @ResponseBody
    public R deleteUser(@PathVariable("uid") Integer uid){
        System.out.println("----------deleteUser---------");
        boolean flag = userFeignService.deleteUser(uid);
        if (flag){
            return R.right();
        }
        return R.error();
    }

    //添加用户
    @PostMapping("/addUser")
    @ResponseBody
    public R addUser(User user){
        System.out.println("-------------addUser--------------");
        user.setCreateTime(new Date());
        boolean flag = userFeignService.addUser(user);

        if(flag){
            return R.right();
        }else {
            return R.error();
        }
    }

    //上传头像
    @PostMapping("/upload")
    @ResponseBody
    public R uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        System.out.println("----------uploadAvatar----------");
        R r = new R();
        if(file.isEmpty()){
            return r.error(1,"上传文件不能为空");
        }

        try {
            // 1. 获取原文件名及后缀
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 2. 使用 UUID 生成唯一的新文件名，防止文件名冲突
            String newFilename = UUID.randomUUID().toString() + suffix;
            // 3. 确定保存的文件目录路径（例如：保存到 shop-web 的 static/img/ 目录中）
            String realPath = request.getServletContext().getRealPath("/static/img/");
            File dir = new File(realPath);
            if (!dir.exists()) {
                dir.mkdirs(); // 自动创建目录
            }
            // 4. 保存文件到本地
            File dest = new File(dir, newFilename);
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            r.error(1, "服务端保存文件失败：" + e.getMessage());
        }
        return r;
    }

    //跳转添加页面
    @GetMapping("/toAddUser")
    public String toAddUser(HttpServletRequest request){
        System.out.println("-----------toAddUser----------");
        List<Role> list = roleFeignService.getAll();
        request.setAttribute("list", list);
        return "user/add";
    }

    //实现批量删除用户
    @PostMapping("/batchDelUser")
    @ResponseBody
    public R batchDelUser(@RequestParam("userIds[]")Integer userIds[]){
        System.out.println("----------batchDelUser--------");
        boolean flag = userFeignService.batchDelUser(userIds);
        Map<String,Object> map = new HashMap<>();
        if(flag){
           return R.right();
        }else{
           return  R.error();
        }

    }

    //分页查询或模糊分页查询用户
    @PostMapping("/userList")
    @ResponseBody
    public PageResult<User> getUserPageByUidAndName(@RequestParam(value = "pname",defaultValue = "${user.list.default-name:}")String name,
                                                    @RequestParam(defaultValue = "${user.list.default-page:1}")Integer page,
                                                    @RequestParam(defaultValue = "${user.list.default-limit:5}")Integer limit,
                                                    HttpSession session){
        System.out.println("--------userList--------");
        System.out.println("name:"+name);
        System.out.println("page:"+page);
        System.out.println("limit:"+limit);
        Map<String,Object> map = new HashMap<>();

        map.put("name",name);
        map.put("currentPage",page);
        map.put("pageSize",limit);
        User user = (User) session.getAttribute("user1");
        if(AuthUtils.isAdmin(session)){//判断是否为管理员
            PageResult<User> pageResult = userFeignService.getUserByPage(map);
            return pageResult;
        }else {

            PageResult<User> pageResult = userFeignService.getUserAndRoleByPage(user.getUid());
            return pageResult;
        }

    }


    //跳转到用户管理
    @RequestMapping("/toUserList")
    public String toUserList(){
        System.out.println("--------toUserList--------");
;
        return "user/list";
    }

    //跳转到普通用户登录的页面
    @RequestMapping("/toUserLogin")
    public String toUserLogin(HttpSession session){
        System.out.println("-----------toUserLogin-------------");
        User user1 = (User) session.getAttribute("user1");
        if (user1 != null) {
            if (user1.getRoleId() == 1) {
                return "redirect:/toIndex";
            } else {
                return "redirect:/toUserIndex";
            }
        }
        return "user/userLogin";
    }

    @RequestMapping("/userLogin")
    @ResponseBody
    public R UserLogin(User user,HttpSession session){
        System.out.println("-----------UserLogin-------------");
        //默认是2普通用户
        user.setRoleId(2);
        User user1 = userFeignService.login(user);
        System.out.println("user1:"+user1);
        System.out.println("flag:"+user1!=null);
        if(user1!=null){
            session.setAttribute("user1",user1);
            return R.right();
        }else {
            return R.error("登录失败，账号或密码错误！");
        }
    }
    @RequestMapping("/toLogin")
    public String toLogin(HttpServletRequest request, HttpSession session){
        System.out.println("----------toLogin----------");
        User user1 = (User) session.getAttribute("user1");
        if (user1 != null) {
            if (user1.getRoleId() == 1) {
                return "redirect:/toIndex";
            } else {
                return "redirect:/toUserIndex";
            }
        }
        List<Role> list = roleFeignService.getAll();
        request.setAttribute("list",list);
        return "user/login";
    }

    @RequestMapping("/login")
    @ResponseBody
    public R login(User user, HttpSession session){
        System.out.println("--------------login--------------");
        System.out.println("user:"+user);
        User user1 = userFeignService.login(user);
        if(user1!=null){
            session.setAttribute("user1",user1);
            return R.right();
        }else {
            return R.error();
        }
    }

    @GetMapping("/toMyInfoFront")
    public String toMyInfoFront(HttpSession session, HttpServletRequest request) {
        User user1 = (User) session.getAttribute("user1");
        if (user1 != null) {
            User user = userFeignService.getUser(user1.getUid());
            request.setAttribute("user", user);
        }
        return "user/myInfo_front";
    }
}
