package com.water.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.water.delivery.dto.LoginDTO;
import com.water.delivery.dto.PasswordDTO;
import com.water.delivery.entity.SysUser;
import com.water.delivery.vo.LoginVO;

public interface UserService extends IService<SysUser> {
    LoginVO login(LoginDTO loginDTO);
    LoginVO getUserInfo(Long userId);
    boolean changePassword(Long userId, PasswordDTO passwordDTO);
}
