/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80020 (8.0.20)
 Source Host           : localhost:3306
 Source Schema         : gbs12

 Target Server Type    : MySQL
 Target Server Version : 80020 (8.0.20)
 File Encoding         : 65001

 Date: 20/06/2026 03:30:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `cid` int NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `pid` int NULL DEFAULT NULL COMMENT '商品ID',
  `pquantity` int NULL DEFAULT NULL COMMENT '商品数量',
  `uid` int NULL DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`cid`) USING BTREE,
  INDEX `fk_cart_product`(`pid` ASC) USING BTREE,
  INDEX `fk_cart_user`(`uid` ASC) USING BTREE,
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`pid`) REFERENCES `t_product` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '购物车表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (49, 2, 1, 10);
INSERT INTO `cart` VALUES (54, 39, 1, 11);
INSERT INTO `cart` VALUES (55, 38, 1, 11);
INSERT INTO `cart` VALUES (56, 2, 23, 11);
INSERT INTO `cart` VALUES (57, 2, 2, 17);
INSERT INTO `cart` VALUES (59, 3, 1, 17);
INSERT INTO `cart` VALUES (61, 3, 3, 11);

-- ----------------------------
-- Table structure for location
-- ----------------------------
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location`  (
  `aid` int NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `realName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `identity` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号 ',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `oid` int NULL DEFAULT NULL COMMENT '订单ID',
  `uid` int NULL DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`aid`) USING BTREE,
  INDEX `fk_location_user`(`uid` ASC) USING BTREE,
  CONSTRAINT `fk_location_user` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收货地址表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of location
-- ----------------------------
INSERT INTO `location` VALUES (16, '张三123', '123', '5451327577', '湖北武汉', 38, 17);
INSERT INTO `location` VALUES (17, '阿道夫', '12345678901', '5451327577', '湖北武汉', 39, 17);
INSERT INTO `location` VALUES (18, 'xiaoye', '12345678901', '5451327577', '湖北武汉', 40, 11);
INSERT INTO `location` VALUES (43, '吴琦', '18727367877', '45', '42', NULL, 11);
INSERT INTO `location` VALUES (44, '吴琦', '18727367877', '45', '42', NULL, 11);
INSERT INTO `location` VALUES (45, '吴琦', '18727367877', '45', '42', NULL, 11);
INSERT INTO `location` VALUES (48, '吴琦', '18727367877', '45', '42', NULL, 11);
INSERT INTO `location` VALUES (52, '吴琦', '18727367877', '45', '42', NULL, 11);
INSERT INTO `location` VALUES (53, '吴琦', '18727367877', '45', '42', NULL, 11);
INSERT INTO `location` VALUES (54, '吴琦', '18727367877', '45', '42', NULL, 11);
INSERT INTO `location` VALUES (55, '吴琦', '18727367877', '45', '42', NULL, 11);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `rid` int NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `roleName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '管理员');
INSERT INTO `role` VALUES (2, '普通用户');

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `sid` int NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `mall_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商城名称',
  `logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商城LOGO',
  `carousel2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '轮播图2',
  `carousel1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '轮播图1',
  `carousel3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '轮播图3',
  `carousel4` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '轮播图4',
  `carousel5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '轮播图5',
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES (1, '游戏商城', NULL, '/img/02.png', '/img/01.png', '/img/03.jpg', '/img/04.jpg', '/img/05.jpg');

-- ----------------------------
-- Table structure for t_collect
-- ----------------------------
DROP TABLE IF EXISTS `t_collect`;
CREATE TABLE `t_collect`  (
  `cid` int NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `uid` int NOT NULL COMMENT '用户ID',
  `pid` int NOT NULL COMMENT '商品ID',
  PRIMARY KEY (`cid`) USING BTREE,
  INDEX `fk_collect_user`(`uid` ASC) USING BTREE,
  INDEX `fk_collect_product`(`pid` ASC) USING BTREE,
  CONSTRAINT `fk_collect_product` FOREIGN KEY (`pid`) REFERENCES `t_product` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_collect_user` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_collect
-- ----------------------------
INSERT INTO `t_collect` VALUES (3, 17, 2);
INSERT INTO `t_collect` VALUES (5, 17, 4);
INSERT INTO `t_collect` VALUES (6, 17, 39);
INSERT INTO `t_collect` VALUES (7, 17, 38);
INSERT INTO `t_collect` VALUES (8, 17, 7);
INSERT INTO `t_collect` VALUES (9, 17, 40);
INSERT INTO `t_collect` VALUES (10, 17, 41);
INSERT INTO `t_collect` VALUES (11, 17, 42);
INSERT INTO `t_collect` VALUES (15, 11, 4);
INSERT INTO `t_collect` VALUES (16, 11, 3);
INSERT INTO `t_collect` VALUES (17, 11, 7);
INSERT INTO `t_collect` VALUES (18, 11, 38);
INSERT INTO `t_collect` VALUES (19, 11, 39);
INSERT INTO `t_collect` VALUES (21, 17, 3);
INSERT INTO `t_collect` VALUES (22, 43, 2);
INSERT INTO `t_collect` VALUES (23, 43, 7);
INSERT INTO `t_collect` VALUES (24, 43, 3);
INSERT INTO `t_collect` VALUES (25, 43, 38);
INSERT INTO `t_collect` VALUES (26, 11, 2);
INSERT INTO `t_collect` VALUES (27, 17, 44);

-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`  (
  `cid` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `uid` int NOT NULL COMMENT '用户ID',
  `pid` int NOT NULL COMMENT '商品ID',
  `oid` int NOT NULL COMMENT '订单ID',
  `content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `score` int NULL DEFAULT 5 COMMENT '评分',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`cid`) USING BTREE,
  INDEX `fk_comment_user`(`uid` ASC) USING BTREE,
  INDEX `fk_comment_product`(`pid` ASC) USING BTREE,
  INDEX `fk_comment_order`(`oid` ASC) USING BTREE,
  CONSTRAINT `fk_comment_order` FOREIGN KEY (`oid`) REFERENCES `t_order` (`oid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_product` FOREIGN KEY (`pid`) REFERENCES `t_product` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_comment
-- ----------------------------
INSERT INTO `t_comment` VALUES (1, 17, 2, 58, '666', 5, '2026-06-14 21:24:09');
INSERT INTO `t_comment` VALUES (2, 17, 2, 58, '这瓜保熟吗', 3, '2026-06-14 21:24:43');
INSERT INTO `t_comment` VALUES (3, 11, 2, 36, '很好用', 5, '2026-06-16 16:24:21');
INSERT INTO `t_comment` VALUES (4, 11, 2, 36, '很好用', 5, '2026-06-16 16:24:22');

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `oid` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `total_price` decimal(12, 3) NULL DEFAULT NULL COMMENT '订单总金额',
  `uid` int NULL DEFAULT NULL COMMENT '买家ID',
  `merchant_id` int NULL DEFAULT NULL COMMENT '商家ID',
  `createtime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付状态',
  `goodsStatus` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流状态',
  PRIMARY KEY (`oid`) USING BTREE,
  INDEX `fk_order_buyer`(`uid` ASC) USING BTREE,
  INDEX `fk_order_merchant`(`merchant_id` ASC) USING BTREE,
  CONSTRAINT `fk_order_buyer` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_merchant` FOREIGN KEY (`merchant_id`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES (36, 2000.000, 11, NULL, '2022-06-30 14:38:42', '2026-06-03 03:59:08', '已支付', '已发货');
INSERT INTO `t_order` VALUES (37, 1400.000, 11, NULL, '2022-06-30 14:41:22', '2026-06-03 03:59:08', '已支付', '已发货');
INSERT INTO `t_order` VALUES (51, 700.000, 11, 17, '2026-06-14 18:15:21', '2026-06-03 03:59:08', '未支付', '未发货');
INSERT INTO `t_order` VALUES (52, 16.000, 11, 11, '2026-06-14 18:37:33', '2026-06-03 03:59:08', '未支付', '未发货');
INSERT INTO `t_order` VALUES (53, 600.000, 11, 17, '2026-06-14 18:49:37', '2026-06-03 03:59:08', '未支付', '未发货');
INSERT INTO `t_order` VALUES (54, 18.000, 11, 11, '2026-06-14 19:44:34', '2026-06-03 03:59:08', '未支付', '未发货');
INSERT INTO `t_order` VALUES (55, 18.000, 11, 11, '2026-06-14 19:59:37', '2026-06-14 19:59:51', '已支付', '已发货');
INSERT INTO `t_order` VALUES (56, 20.000, 11, 11, '2026-06-14 20:10:21', '2026-06-14 20:11:28', '已支付', '已发货');
INSERT INTO `t_order` VALUES (57, 21.000, 11, 11, '2026-06-14 20:46:49', '2026-06-14 20:47:00', '已支付', '已发货');
INSERT INTO `t_order` VALUES (58, 1.000, 17, 11, '2026-06-14 21:16:20', '2026-06-14 21:16:25', '已支付', '已发货');
INSERT INTO `t_order` VALUES (59, 2.000, 17, 11, '2026-06-14 21:17:04', '2026-06-14 21:17:09', '已支付', '已发货');
INSERT INTO `t_order` VALUES (60, 1.000, 11, 11, '2026-06-14 21:43:31', '2026-06-14 21:43:40', '已支付', '已发货');
INSERT INTO `t_order` VALUES (61, 4200.000, 17, 17, '2026-06-15 18:58:31', '2026-06-15 18:58:36', '已支付', '已发货');
INSERT INTO `t_order` VALUES (62, 458.000, 17, 11, '2026-06-15 19:03:17', NULL, '未支付', '未发货');
INSERT INTO `t_order` VALUES (63, 458.000, 43, 11, '2026-06-15 20:38:48', NULL, '未支付', '未发货');
INSERT INTO `t_order` VALUES (64, 458.000, 17, 11, '2026-06-16 08:39:41', '2026-06-16 08:39:49', '已支付', '已发货');
INSERT INTO `t_order` VALUES (65, 458.000, 17, 11, '2026-06-16 08:45:25', '2026-06-16 08:45:31', '已支付', '已发货');
INSERT INTO `t_order` VALUES (66, 12600.000, 11, 17, '2026-06-19 17:09:36', '2026-06-19 17:09:39', '已支付', '未发货');
INSERT INTO `t_order` VALUES (67, 8000.000, 11, 11, '2026-06-19 17:10:02', '2026-06-19 17:10:05', '已支付', '未发货');
INSERT INTO `t_order` VALUES (69, 800.000, 11, 17, '2026-06-19 17:13:53', NULL, '未支付', '未发货');
INSERT INTO `t_order` VALUES (70, 800.000, 11, 17, '2026-06-19 17:46:27', '2026-06-19 17:46:31', '已支付', '未发货');

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product`  (
  `pid` int NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `pname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `price` decimal(12, 3) NULL DEFAULT NULL COMMENT '商品价格',
  `pfile` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品描述',
  `stock` int NULL DEFAULT 0 COMMENT '库存',
  `uid` int NULL DEFAULT NULL COMMENT '商家ID',
  `status` int NULL DEFAULT 1 COMMENT '商品状态 1上架 0下架',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `sales` int NULL DEFAULT 0 COMMENT '销量',
  `audit_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核状态',
  PRIMARY KEY (`pid`) USING BTREE,
  INDEX `fk_product_user`(`uid` ASC) USING BTREE,
  CONSTRAINT `fk_product_user` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES (2, '谪星化霜弦', 458.000, '/img/0559cf7e-9c92-4380-9d58-b02b4a74adc111.png', '金 | 弓箭', 99, 11, 1, '2026-06-09 05:00:36', 1, '已审核');
INSERT INTO `t_product` VALUES (3, '谪星摧山', 4200.000, '/img/2835f16d-ffb6-4965-9b16-31caaebc857a1.png', '红 | 斩马刀', 9, 17, 1, '2026-06-09 05:00:36', 1, '已审核');
INSERT INTO `t_product` VALUES (4, '谪星澄明', 400.000, '/img/18d63b56-71d1-4c11-9e7d-e5b1b053f6a827.png', '金 | 扇', 98, 17, 1, '2026-06-09 05:00:36', 2, '已审核');
INSERT INTO `t_product` VALUES (7, '谪星引魂姬', 4100.000, '/img/e0b2c446-f751-4e76-9b0d-872f286ef58010.png', '红 | 太刀', 10, 11, 1, '2026-06-09 05:00:36', 0, '已审核');
INSERT INTO `t_product` VALUES (38, '谪星神劫', 499.500, '/img/e9efd0ae-ef94-4a85-83e3-4e74a40ff4e61.jpg', '金 | 双刀', 100, 17, 1, '2026-06-11 01:20:25', 0, '已审核');
INSERT INTO `t_product` VALUES (39, '谪星青羽契', 5150.000, '/img/2cec8889-de48-4586-8dc9-3a3154c0504013.png', '红 | 飞刀', 10, 11, 1, '2026-06-11 02:17:33', 0, '已审核');
INSERT INTO `t_product` VALUES (40, '谪星焰狸官', 400.000, '/img/f5f9cbe0-a318-4b02-aacd-2f7573e3416612.png', '金 | 火炮', 100, 17, 1, '2026-06-11 16:02:26', 0, '已审核');
INSERT INTO `t_product` VALUES (41, '谪星扶桑劫', 4000.000, '/img/a0397a64-3913-4857-94ee-d6fd46ef7d9c14.png', '红 | 棍', 19, 11, 1, '2026-06-11 16:34:19', 1, '已审核');
INSERT INTO `t_product` VALUES (42, '谪星·天流星辉', 5900.000, '/img/d4f27f30-ad06-4ff6-9209-ecc422350d7e15.png', '红 | 枪', 5, 11, 1, '2026-06-11 16:49:44', 0, '已审核');
INSERT INTO `t_product` VALUES (44, '谪星花重锦', 16549.000, '/img/bc89f20a-084d-4760-b964-f9d0a2b48e5e16.png', '红 | 长剑', 5, 11, 1, '2026-06-15 22:21:46', 0, '已审核');
INSERT INTO `t_product` VALUES (45, '谪星炽魂', 11888.000, '/img/9b6fd6ef-b71a-4ae2-ae39-dd4374ac0fd017.png', '红 | 双截棍', 10, 11, 1, '2026-06-15 22:24:22', 0, '已审核');
INSERT INTO `t_product` VALUES (47, '谪星化霜弦', 458.000, '/img/0559cf7e-9c92-4380-9d58-b02b4a74adc111.png', '金 | 弓箭', 1, 17, 0, '2026-06-16 08:45:31', 0, '已审核');
INSERT INTO `t_product` VALUES (49, '谪星摧山', 4200.000, '/img/2835f16d-ffb6-4965-9b16-31caaebc857a1.png', '红 | 斩马刀', 1, 11, 1, '2026-06-19 17:09:40', 0, '已审核');
INSERT INTO `t_product` VALUES (50, '谪星扶桑劫', 4000.000, '/img/a0397a64-3913-4857-94ee-d6fd46ef7d9c14.png', '红 | 棍', 1, 11, 0, '2026-06-19 17:10:06', 0, '已审核');
INSERT INTO `t_product` VALUES (51, '谪星澄明', 400.000, '/img/18d63b56-71d1-4c11-9e7d-e5b1b053f6a827.png', '金 | 扇', 2, 11, 0, '2026-06-19 17:46:31', 0, '已审核');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `telephone` bigint NULL DEFAULT NULL,
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `role_id` int NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`uid`) USING BTREE,
  INDEX `fk_user_role`(`role_id` ASC) USING BTREE,
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`rid`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (10, 'xiaoye', 'xiaoye', 4564, '2022-06-24 23:10:18', 1);
INSERT INTO `user` VALUES (11, 'admin', 'admin', 45, '2022-06-11 22:46:33', 1);
INSERT INTO `user` VALUES (17, 'xiaoye', '123123', 18727367877, '2022-06-12 09:34:14', 2);
INSERT INTO `user` VALUES (18, 'xiaoye123', '123123123', 12, '2022-06-12 09:35:40', 2);
INSERT INTO `user` VALUES (28, 'Ma ', 'GNZsSEhnAf', 4534, '2019-09-19 10:43:55', 2);
INSERT INTO `user` VALUES (36, 'Zheng Yuning', 'gmLf3A0ZQR', 3453345344, '2020-09-09 03:08:29', 1);
INSERT INTO `user` VALUES (37, 'Takeuchi Eita', 'zMGVtP2xCu', 5434, '2025-07-21 13:17:23', 2);
INSERT INTO `user` VALUES (43, 'a123', '123123', 18723467896, '2026-06-15 14:43:10', 2);

SET FOREIGN_KEY_CHECKS = 1;
