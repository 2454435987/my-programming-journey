-- ==============================================================
-- 桶装水配送管理系统 (Water Delivery Management System)
-- 数据库初始化脚本: 建库、建表、索引与初始演示数据
-- 数据库版本: MySQL 8.0+
-- 字符集: utf8mb4 / 排序规则: utf8mb4_general_ci
-- ==============================================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `water_delivery_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `water_delivery_db`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -------------------------------------------------------------
-- 2. 依次创建 10 张核心业务表
-- -------------------------------------------------------------

-- 1) 系统角色表 (sys_role)
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色主键ID',
  `role_code` VARCHAR(32) NOT NULL COMMENT '角色标识编码 (ROLE_ADMIN / ROLE_COURIER)',
  `role_name` VARCHAR(64) NOT NULL COMMENT '角色中文名称',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '角色职责描述',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 2) 系统用户表 (sys_user)
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户主键ID',
  `username` VARCHAR(64) NOT NULL COMMENT '统一登录用户名/工号',
  `password` VARCHAR(128) NOT NULL COMMENT 'BCrypt强哈希密码密文',
  `real_name` VARCHAR(64) NOT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) NOT NULL COMMENT '联系手机号',
  `role_id` BIGINT NOT NULL COMMENT '所属角色ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 (1=正常, 0=停用锁定)',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户账号表';

-- 3) 服务片区字典表 (t_region)
DROP TABLE IF EXISTS `t_region`;
CREATE TABLE `t_region` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '片区主键ID',
  `region_code` VARCHAR(32) NOT NULL COMMENT '片区业务编码',
  `region_name` VARCHAR(64) NOT NULL COMMENT '片区名称',
  `coverage_desc` VARCHAR(255) DEFAULT NULL COMMENT '片区覆盖网格街道范围',
  `leader_name` VARCHAR(32) DEFAULT NULL COMMENT '片区负责人姓名',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_region_code` (`region_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务片区表';

-- 4) 配送员（送水工）信息表 (t_courier)
DROP TABLE IF EXISTS `t_courier`;
CREATE TABLE `t_courier` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配送员信息主键ID',
  `user_id` BIGINT NOT NULL COMMENT '关联的用户账号ID (1:1)',
  `courier_no` VARCHAR(32) NOT NULL COMMENT '送水工工号',
  `name` VARCHAR(64) NOT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) NOT NULL COMMENT '联系手机号',
  `region_id` BIGINT NOT NULL COMMENT '主责网格片区ID',
  `vehicle_type` VARCHAR(32) DEFAULT '电动三轮车' COMMENT '配送交通工具',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '在职状态 (1=在职, 2=休假, 0=停用离职)',
  `entry_date` DATE NOT NULL COMMENT '入职日期',
  `id_card` VARCHAR(32) DEFAULT NULL COMMENT '身份证号码 (脱敏存储)',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_courier_no` (`courier_no`),
  KEY `idx_region_id` (`region_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配送员（送水工）信息表';

-- 5) 客户档案信息表 (t_customer)
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '客户主键ID',
  `customer_no` VARCHAR(32) NOT NULL COMMENT '客户编号',
  `name` VARCHAR(64) NOT NULL COMMENT '客户姓名/企业名称',
  `phone` VARCHAR(20) NOT NULL COMMENT '主要联系手机号 (核销主查字段)',
  `region_id` BIGINT NOT NULL COMMENT '所属服务片区ID',
  `address_detail` VARCHAR(255) NOT NULL COMMENT '完整详细门牌地址',
  `floor_num` INT NOT NULL DEFAULT 1 COMMENT '所在楼层高度',
  `has_elevator` TINYINT NOT NULL DEFAULT 1 COMMENT '是否有电梯 (1=有电梯, 0=步梯爬楼)',
  `customer_type` TINYINT NOT NULL DEFAULT 1 COMMENT '客户分类 (1=家庭散户, 2=企业商业大客户)',
  `unreturned_barrels` INT NOT NULL DEFAULT 0 COMMENT '当前累计未还空桶欠数',
  `warning_threshold` INT NOT NULL DEFAULT 2 COMMENT '余票预警触发阈值',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建档时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_no` (`customer_no`),
  KEY `idx_phone` (`phone`),
  KEY `idx_region_id` (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户档案信息表';

-- 6) 水票品牌与规格表 (t_ticket_type)
DROP TABLE IF EXISTS `t_ticket_type`;
CREATE TABLE `t_ticket_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '规格唯一ID',
  `brand_name` VARCHAR(64) NOT NULL COMMENT '水票品牌名称',
  `spec_desc` VARCHAR(64) NOT NULL COMMENT '规格容量描述 (如 19L天然饮用水)',
  `unit_price` DECIMAL(10,2) NOT NULL COMMENT '单张水票基准售价 (元)',
  `barrel_deposit` DECIMAL(10,2) NOT NULL DEFAULT 40.00 COMMENT '每桶对应空桶押金基准金额 (元)',
  `is_active` TINYINT NOT NULL DEFAULT 1 COMMENT '在售状态 (1=在售, 0=停用下架)',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水票品牌与规格表';

-- 7) 客户水票账户余额表 (t_ticket_account)
DROP TABLE IF EXISTS `t_ticket_account`;
CREATE TABLE `t_ticket_account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '水票账户主键ID',
  `customer_id` BIGINT NOT NULL COMMENT '客户ID',
  `type_id` BIGINT NOT NULL COMMENT '水票品牌规格ID',
  `balance` INT NOT NULL DEFAULT 0 COMMENT '当前可用水票余票 (核销核心扣减字段)',
  `total_recharge` INT NOT NULL DEFAULT 0 COMMENT '历史累计充值发放总张数',
  `total_consumed` INT NOT NULL DEFAULT 0 COMMENT '历史累计核销配送总张数',
  `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号 (防止并发超扣)',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cust_type` (`customer_id`, `type_id`),
  KEY `idx_type_id` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户水票账户余额表';

-- 8) 水票变动流水明细表 (t_ticket_flow)
DROP TABLE IF EXISTS `t_ticket_flow`;
CREATE TABLE `t_ticket_flow` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '流水全局主键ID',
  `flow_no` VARCHAR(64) NOT NULL COMMENT '业务流水单号',
  `customer_id` BIGINT NOT NULL COMMENT '关联客户ID',
  `type_id` BIGINT NOT NULL COMMENT '水票品牌规格ID',
  `biz_type` TINYINT NOT NULL COMMENT '业务类型 (1=充值发放, 2=配送核销, 3=作废扣减, 4=退票回收)',
  `change_amount` INT NOT NULL COMMENT '变动数量 (正数代表增加, 负数代表扣减)',
  `before_balance` INT NOT NULL COMMENT '变动前该规格可用余额',
  `after_balance` INT NOT NULL COMMENT '变动后该规格可用余额',
  `ref_order_no` VARCHAR(64) DEFAULT NULL COMMENT '关联业务单号 (配送单号或充值流水号)',
  `operator_id` BIGINT NOT NULL COMMENT '执行操作员工号/送水工ID',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '流水备注说明',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录生成时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_flow_no` (`flow_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_biz_type` (`biz_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水票变动流水明细表';

-- 9) 配送核销订单表 (t_delivery_order)
DROP TABLE IF EXISTS `t_delivery_order`;
CREATE TABLE `t_delivery_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配送订单主键ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '配送核销单号',
  `customer_id` BIGINT NOT NULL COMMENT '收货客户ID',
  `courier_id` BIGINT NOT NULL COMMENT '负责送水工ID',
  `type_id` BIGINT NOT NULL COMMENT '核销的水票规格ID',
  `delivery_qty` INT NOT NULL DEFAULT 1 COMMENT '实送重桶数量',
  `recycle_qty` INT NOT NULL DEFAULT 0 COMMENT '现场实收空桶数量',
  `delivery_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '配送完成核销时间戳',
  `order_status` TINYINT NOT NULL DEFAULT 1 COMMENT '订单状态 (1=配送完成已核销, 2=异常争议)',
  `cost_minutes` INT DEFAULT 35 COMMENT '履约耗时(分钟)',
  `is_overdue` TINYINT NOT NULL DEFAULT 0 COMMENT '是否超时逾期 (1=超时逾期, 0=按期交付)',
  `sign_method` VARCHAR(32) DEFAULT '核销码确认' COMMENT '签收凭证方式',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_courier_id` (`courier_id`),
  KEY `idx_delivery_time` (`delivery_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配送核销订单表';

-- 10) 空桶回收与流转对冲表 (t_barrel_recycle)
DROP TABLE IF EXISTS `t_barrel_recycle`;
CREATE TABLE `t_barrel_recycle` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '空桶流转记录主键ID',
  `order_id` BIGINT NOT NULL COMMENT '关联的配送核销单ID',
  `customer_id` BIGINT NOT NULL COMMENT '收货客户ID',
  `courier_id` BIGINT NOT NULL COMMENT '负责送水工ID',
  `delivered_barrels` INT NOT NULL COMMENT '送出重桶数 (应回参考基数)',
  `recycled_barrels` INT NOT NULL COMMENT '现场实收空桶数',
  `barrel_diff` INT NOT NULL COMMENT '空桶差额 (送出数 - 回收数，正数表增欠)',
  `ai_inspect_status` VARCHAR(32) NOT NULL DEFAULT 'PASSED' COMMENT 'AI质检评级 (PASSED=合格, NEED_CLEAN=需清洗, SCRAP=报废)',
  `ai_inspect_desc` VARCHAR(500) DEFAULT NULL COMMENT '多模态AI给出的诊断原因与损伤说明',
  `suggest_deduct_deposit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT 'AI建议扣减押金金额 (元)',
  `inspect_image_url` VARCHAR(255) DEFAULT NULL COMMENT '质检存证照片URL',
  `recycle_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回仓对冲时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_courier_id` (`courier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='空桶回收与流转对冲表';

-- -------------------------------------------------------------
-- 3. 录入完整规范的初始演示数据 (涵盖正常、缺水、爬楼、AI质检不同场景)
-- -------------------------------------------------------------

-- 角色初始化
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `description`) VALUES
(1, 'ROLE_ADMIN', '系统管理员', '最高权限管理者，负责全站用户、水票全生命周期、全局调度与财务统计看板'),
(2, 'ROLE_COURIER', '送水工/配送员', '移动作业角色，负责片区待配任务执行、现场水票核销、AI空桶质检与个人业绩查看');

-- 初始用户 (密码统一为 123456，BCrypt: $2a$10$ynD2nDQHep6puOcUrTHjvuyMOGfBLAYew6rhU8rM5NNi12SrZt3R.)
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `role_id`, `status`) VALUES
(1, 'admin', '$2a$10$ynD2nDQHep6puOcUrTHjvuyMOGfBLAYew6rhU8rM5NNi12SrZt3R.', '水站站长(管理员)', '13800000001', 1, 1),
(2, 'SG8001', '$2a$10$ynD2nDQHep6puOcUrTHjvuyMOGfBLAYew6rhU8rM5NNi12SrZt3R.', '张志强(送水工)', '13911112222', 2, 1),
(3, 'SG8002', '$2a$10$ynD2nDQHep6puOcUrTHjvuyMOGfBLAYew6rhU8rM5NNi12SrZt3R.', '李铁柱(送水工)', '13933334444', 2, 1);

-- 初始片区
INSERT INTO `t_region` (`id`, `region_code`, `region_name`, `coverage_desc`, `leader_name`) VALUES
(1, 'REG_EAST_01', '城东阳光花园片区', '覆盖阳光花园1-15栋、学府名苑社区、朝阳商业街', '张志强'),
(2, 'REG_WEST_02', '城西金融街片区', '覆盖恒隆国际广场、招商局大厦、高新科技园区写字楼', '李铁柱');

-- 配送员档案
INSERT INTO `t_courier` (`id`, `user_id`, `courier_no`, `name`, `phone`, `region_id`, `vehicle_type`, `status`, `entry_date`, `id_card`) VALUES
(1, 2, 'SG8001', '张志强', '13911112222', 1, '电动三轮车', 1, '2025-03-01', '110101198805121234'),
(2, 3, 'SG8002', '李铁柱', '13933334444', 2, '平板微货', 1, '2025-06-15', '110101199208204567');

-- 水票品牌规格
INSERT INTO `t_ticket_type` (`id`, `brand_name`, `spec_desc`, `unit_price`, `barrel_deposit`, `is_active`) VALUES
(1, '农夫山泉', '19L优质天然饮用水', 28.00, 40.00, 1),
(2, '怡宝', '18.9L优质饮用纯净水', 24.00, 40.00, 1),
(3, '雀巢', '18.9L优活饮用水', 22.00, 35.00, 1);

-- 客户档案 (覆盖：电梯正常户、步梯爬楼低余票预警户、企业大客户、断水急需续费户)
INSERT INTO `t_customer` (`id`, `customer_no`, `name`, `phone`, `region_id`, `address_detail`, `floor_num`, `has_elevator`, `customer_type`, `unreturned_barrels`, `warning_threshold`) VALUES
(1, 'KH2026001', '王建国', '13812345678', 1, '阳光花园 3栋2单元501室', 5, 1, 1, 1, 2),
(2, 'KH2026002', '陈秀芳', '13987654321', 1, '阳光花园 7栋1单元602室(步梯爬楼)', 6, 0, 1, 2, 2),
(3, 'KH2026003', '未来科技有限公司(行政部)', '13666668888', 2, '恒隆国际广场 A座1808室', 18, 1, 2, 5, 5),
(4, 'KH2026004', '赵大姐沿街便利店', '13799990000', 1, '朝阳商业街 102号商铺', 1, 1, 1, 0, 2);

-- 客户水票账户余额
INSERT INTO `t_ticket_account` (`id`, `customer_id`, `type_id`, `balance`, `total_recharge`, `total_consumed`, `version`) VALUES
(1, 1, 1, 8, 10, 2, 0),    -- 王建国: 农夫山泉 8 桶 (充足)
(2, 2, 1, 1, 10, 9, 0),    -- 陈秀芳: 农夫山泉 1 桶 (触发<=2预警!)
(3, 3, 2, 15, 30, 15, 0),  -- 未来科技: 怡宝 15 桶 (企业充足)
(4, 4, 1, 0, 5, 5, 0);     -- 赵大姐: 农夫山泉 0 桶 (严重断水预警!)

-- 水票初始变动流水
INSERT INTO `t_ticket_flow` (`id`, `flow_no`, `customer_id`, `type_id`, `biz_type`, `change_amount`, `before_balance`, `after_balance`, `ref_order_no`, `operator_id`, `remark`, `create_time`) VALUES
(1, 'TF202609010001', 1, 1, 1, 10, 0, 10, 'RC20260901001', 1, '客户开户充值10张农夫山泉水票', '2026-09-01 09:30:00'),
(2, 'TF202609020002', 2, 1, 1, 10, 0, 10, 'RC20260902001', 1, '老客户充值10张农夫山泉水票', '2026-09-02 10:15:00'),
(3, 'TF202609050003', 3, 2, 1, 30, 0, 30, 'RC20260905001', 1, '未来科技企业批量采购30张怡宝水票', '2026-09-05 14:00:00'),
(4, 'TF202609100004', 1, 1, 2, -1, 10, 9, 'DO20260910001', 2, '配送员张志强上门核销配送1桶', '2026-09-10 11:20:00'),
(5, 'TF202609140005', 1, 1, 2, -1, 9, 8, 'DO20260914002', 2, '配送员张志强上门核销配送1桶', '2026-09-14 16:45:00');

-- 初始配送订单记录 (包含正常配送与超时配送样本)
INSERT INTO `t_delivery_order` (`id`, `order_no`, `customer_id`, `courier_id`, `type_id`, `delivery_qty`, `recycle_qty`, `delivery_time`, `order_status`, `cost_minutes`, `is_overdue`, `sign_method`) VALUES
(1, 'DO20260910001', 1, 1, 1, 1, 1, '2026-09-10 11:20:00', 1, 28, 0, '核销码确认'),
(2, 'DO20260914002', 1, 1, 1, 1, 0, '2026-09-14 16:45:00', 1, 45, 0, '核销码确认'),
(3, 'DO20260915003', 3, 2, 2, 5, 4, '2026-09-15 15:30:00', 1, 135, 1, '前台代签'); -- 超时逾期单(>120分钟)

-- 初始空桶回收对冲与 AI 质检存证
INSERT INTO `t_barrel_recycle` (`id`, `order_id`, `customer_id`, `courier_id`, `delivered_barrels`, `recycled_barrels`, `barrel_diff`, `ai_inspect_status`, `ai_inspect_desc`, `suggest_deduct_deposit`, `inspect_image_url`, `recycle_time`) VALUES
(1, 1, 1, 1, 1, 1, 0, 'PASSED', 'AI检测分析结论：桶身完好平整无挤压裂纹，内部清澈无青苔异物，质检评定为合格品，准予正常入库流转。', 0.00, '/static/inspect/barrel_sample_passed.jpg', '2026-09-10 11:22:00'),
(2, 2, 1, 1, 1, 0, 1, 'PASSED', '本次客户现场无空桶退还，系统记录欠桶+1', 0.00, NULL, '2026-09-14 16:45:00'),
(3, 3, 3, 2, 5, 4, 1, 'NEED_CLEAN', 'AI检测分析结论：其中1只空桶底部检测到轻度茶渍沉淀，未见破损裂纹，建议转入深度清洗除垢车间消毒。', 0.00, '/static/inspect/barrel_sample_clean.jpg', '2026-09-15 15:35:00');

SET FOREIGN_KEY_CHECKS = 1;

-- ==============================================================
-- 脚本执行完毕：数据库已就绪，10张表及演示数据构建成功！
-- ==============================================================
