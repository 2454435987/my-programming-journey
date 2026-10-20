package com.water.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.water.delivery.common.ResultCode;
import com.water.delivery.common.exception.BusinessException;
import com.water.delivery.config.JwtProperties;
import com.water.delivery.dto.LoginDTO;
import com.water.delivery.dto.PasswordDTO;
import com.water.delivery.entity.*;
import com.water.delivery.mapper.*;
import com.water.delivery.security.JwtTokenProvider;
import com.water.delivery.service.UserService;
import com.water.delivery.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {

    private final SysRoleMapper roleMapper;
    private final CourierMapper courierMapper;
    private final RegionMapper regionMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 1. 根据用户名/工号检索用户
        SysUser user = baseMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, loginDTO.getUsername()));

        if (user == null) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR.getCode(), "用户名或密码错误");
        }

        // 2. 校验在职/正常状态
        if (user.getStatus() != 1) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 3. 校验 BCrypt 密码散列
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR.getCode(), "用户名或密码错误");
        }

        // 4. 查询所属角色信息
        SysRole role = roleMapper.selectById(user.getRoleId());
        String roleCode = (role != null) ? role.getRoleCode() : "ROLE_COURIER";
        String roleName = (role != null) ? role.getRoleName() : "普通用户";

        // 5. 若为送水工角色，关联查询其配送员档案与片区
        Long courierId = null;
        Long regionId = null;
        String regionName = null;

        if ("ROLE_COURIER".equals(roleCode)) {
            Courier courier = courierMapper.selectOne(new LambdaQueryWrapper<Courier>()
                    .eq(Courier::getUserId, user.getId()));
            if (courier != null) {
                if (courier.getStatus() == 0) {
                    throw new BusinessException(ResultCode.COURIER_DISABLED);
                }
                courierId = courier.getId();
                regionId = courier.getRegionId();
                if (regionId != null) {
                    Region region = regionMapper.selectById(regionId);
                    if (region != null) {
                        regionName = region.getRegionName();
                    }
                }
            }
        }

        // 6. 生成无状态 JWT 令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("realName", user.getRealName());
        if (courierId != null) {
            claims.put("courierId", courierId);
        }
        String token = tokenProvider.generateToken(user.getId(), user.getUsername(), roleCode, claims);

        // 7. 构建返回 VO
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setTokenPrefix(jwtProperties.getPrefix());
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setRoleCode(roleCode);
        vo.setRoleName(roleName);
        vo.setCourierId(courierId);
        vo.setRegionId(regionId);
        vo.setRegionName(regionName);

        log.info("用户登录成功: username={}, role={}", user.getUsername(), roleCode);
        return vo;
    }

    @Override
    public LoginVO getUserInfo(Long userId) {
        SysUser user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        SysRole role = roleMapper.selectById(user.getRoleId());
        String roleCode = (role != null) ? role.getRoleCode() : "";
        String roleName = (role != null) ? role.getRoleName() : "";

        Long courierId = null;
        Long regionId = null;
        String regionName = null;

        if ("ROLE_COURIER".equals(roleCode)) {
            Courier courier = courierMapper.selectOne(new LambdaQueryWrapper<Courier>()
                    .eq(Courier::getUserId, user.getId()));
            if (courier != null) {
                courierId = courier.getId();
                regionId = courier.getRegionId();
                if (regionId != null) {
                    Region region = regionMapper.selectById(regionId);
                    if (region != null) {
                        regionName = region.getRegionName();
                    }
                }
            }
        }

        LoginVO vo = new LoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setRoleCode(roleCode);
        vo.setRoleName(roleName);
        vo.setCourierId(courierId);
        vo.setRegionId(regionId);
        vo.setRegionName(regionName);
        return vo;
    }

    @Override
    public boolean changePassword(Long userId, PasswordDTO passwordDTO) {
        SysUser user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码校验错误，请重新输入");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        baseMapper.updateById(user);
        log.info("用户密码修改成功: userId={}", userId);
        return true;
    }
}
