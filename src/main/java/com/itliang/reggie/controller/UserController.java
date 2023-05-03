package com.itliang.reggie.controller;

import antlr.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itliang.reggie.common.R;
import com.itliang.reggie.entity.User;
import com.itliang.reggie.service.UserService;
import com.itliang.reggie.utils.SMSUtils;
import com.itliang.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.xml.bind.util.ValidationEventCollector;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userServive;


    /**
     * 发送手机验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> send(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        if(phone!=null){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
//            SMSUtils.sendMessage("reggie","",phone,code);
            session.setAttribute(phone,code);
            return R.success("发送验证码成功");
        }
        return R.error("发送失败");
    }

    /**
     * 登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);
        //进行验证码比对
        if(codeInSession!=null&&codeInSession.equals(code)){
            //如果能够比对成功，说明登陆成功
            //判断当前手机号对应的用户是否为新用户，是新用户就自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userServive.getOne(queryWrapper);
            if(user==null){
                user = new User();
                user.setPhone(phone);
                userServive.save(user);
            }

            session.setAttribute("user",user.getId());

            return R.success(user);
        }
        return R.error("登录失败");
    }
}
