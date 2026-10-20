/*
 Navicat Premium Data Transfer

 Source Server         : bb
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : springboot2zemx

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 28/02/2026 23:04:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人',
  `phone` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电话',
  `isdefault` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否默认地址[是/否]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '地址' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES (1, '2026-02-28 01:03:56', 11, '宇宙银河系金星1号', '金某', '13823888881', '是');
INSERT INTO `address` VALUES (2, '2026-02-28 01:03:56', 12, '宇宙银河系木星1号', '木某', '13823888882', '是');
INSERT INTO `address` VALUES (3, '2026-02-28 01:03:56', 13, '宇宙银河系水星1号', '水某', '13823888883', '是');
INSERT INTO `address` VALUES (4, '2026-02-28 01:03:56', 14, '宇宙银河系火星1号', '火某', '13823888884', '是');
INSERT INTO `address` VALUES (5, '2026-02-28 01:03:56', 15, '宇宙银河系土星1号', '土某', '13823888885', '是');
INSERT INTO `address` VALUES (6, '2026-02-28 01:03:56', 16, '宇宙银河系月球1号', '月某', '13823888886', '是');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tablename` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'siliaoshoumai' COMMENT '商品表名',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `goodid` bigint(20) NOT NULL COMMENT '商品id',
  `goodname` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `buynumber` int(11) NOT NULL COMMENT '购买数量',
  `price` float NULL DEFAULT NULL COMMENT '单价',
  `discountprice` float NULL DEFAULT NULL COMMENT '会员价',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1747657751411 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '购物车表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (1747657751410, '2026-02-28 20:29:11', 'siliaoshoumai', 11, 51, '宠物用品名称1', 'http://localhost:8080/springboot2zemx/upload/siliaoshoumai_tupian1.jpg', 1, 99.9, NULL);

-- ----------------------------
-- Table structure for chongwuleibie
-- ----------------------------
DROP TABLE IF EXISTS `chongwuleibie`;
CREATE TABLE `chongwuleibie`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `chongwuleibie` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宠物类别',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '宠物类别' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chongwuleibie
-- ----------------------------
INSERT INTO `chongwuleibie` VALUES (31, '2026-02-28 01:03:56', '宠物类别1');
INSERT INTO `chongwuleibie` VALUES (32, '2026-02-28 01:03:56', '宠物类别2');
INSERT INTO `chongwuleibie` VALUES (33, '2026-02-28 01:03:56', '宠物类别3');
INSERT INTO `chongwuleibie` VALUES (34, '2026-02-28 01:03:56', '宠物类别4');
INSERT INTO `chongwuleibie` VALUES (35, '2026-02-28 01:03:56', '宠物类别5');
INSERT INTO `chongwuleibie` VALUES (36, '2026-02-28 01:03:56', '宠物类别6');

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置参数值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '配置文件' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 'picture1', 'http://localhost:8080/springboot2zemx/upload/picture1.jpg');
INSERT INTO `config` VALUES (2, 'picture2', 'http://localhost:8080/springboot2zemx/upload/picture2.jpg');
INSERT INTO `config` VALUES (3, 'picture3', 'http://localhost:8080/springboot2zemx/upload/picture3.jpg');
INSERT INTO `config` VALUES (6, 'homepage', NULL);

-- ----------------------------
-- Table structure for discusssiliaoshoumai
-- ----------------------------
DROP TABLE IF EXISTS `discusssiliaoshoumai`;
CREATE TABLE `discusssiliaoshoumai`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `nickname` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `reply` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回复内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 137 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '宠物用品售卖评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of discusssiliaoshoumai
-- ----------------------------
INSERT INTO `discusssiliaoshoumai` VALUES (131, '2026-02-28 01:03:56', 1, 1, '用户名1', '评论内容1', '回复内容1');
INSERT INTO `discusssiliaoshoumai` VALUES (132, '2026-02-28 01:03:56', 2, 2, '用户名2', '评论内容2', '回复内容2');
INSERT INTO `discusssiliaoshoumai` VALUES (133, '2026-02-28 01:03:56', 3, 3, '用户名3', '评论内容3', '回复内容3');
INSERT INTO `discusssiliaoshoumai` VALUES (134, '2026-02-28 01:03:56', 4, 4, '用户名4', '评论内容4', '回复内容4');
INSERT INTO `discusssiliaoshoumai` VALUES (135, '2026-02-28 01:03:56', 5, 5, '用户名5', '评论内容5', '回复内容5');
INSERT INTO `discusssiliaoshoumai` VALUES (136, '2026-02-28 01:03:56', 6, 6, '用户名6', '评论内容6', '回复内容6');

-- ----------------------------
-- Table structure for discussyiqichongwuxinxi
-- ----------------------------
DROP TABLE IF EXISTS `discussyiqichongwuxinxi`;
CREATE TABLE `discussyiqichongwuxinxi`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `nickname` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `reply` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回复内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '遗弃宠物信息评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of discussyiqichongwuxinxi
-- ----------------------------
INSERT INTO `discussyiqichongwuxinxi` VALUES (121, '2026-02-28 01:03:56', 1, 1, '用户名1', '评论内容1', '回复内容1');
INSERT INTO `discussyiqichongwuxinxi` VALUES (122, '2026-02-28 01:03:56', 2, 2, '用户名2', '评论内容2', '回复内容2');
INSERT INTO `discussyiqichongwuxinxi` VALUES (123, '2026-02-28 01:03:56', 3, 3, '用户名3', '评论内容3', '回复内容3');
INSERT INTO `discussyiqichongwuxinxi` VALUES (124, '2026-02-28 01:03:56', 4, 4, '用户名4', '评论内容4', '回复内容4');
INSERT INTO `discussyiqichongwuxinxi` VALUES (125, '2026-02-28 01:03:56', 5, 5, '用户名5', '评论内容5', '回复内容5');
INSERT INTO `discussyiqichongwuxinxi` VALUES (126, '2026-02-28 01:03:56', 6, 6, '用户名6', '评论内容6', '回复内容6');

-- ----------------------------
-- Table structure for forum
-- ----------------------------
DROP TABLE IF EXISTS `forum`;
CREATE TABLE `forum`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帖子内容',
  `parentid` bigint(20) NULL DEFAULT NULL COMMENT '父节点id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `isdone` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '爱心论坛' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of forum
-- ----------------------------
INSERT INTO `forum` VALUES (61, '2026-10-10 10:15:20', '帖子标题1', '帖子内容1', 0, 11, '用户1', '开放');
INSERT INTO `forum` VALUES (62, '2026-10-10 16:08:30', '帖子标题2', '帖子内容2', 0, 12, '用户2', '开放');
INSERT INTO `forum` VALUES (63, '2026-10-11 10:22:45', '帖子标题3', '帖子内容3', 0, 13, '用户3', '开放');
INSERT INTO `forum` VALUES (64, '2026-10-11 19:40:12', '帖子标题4', '帖子内容4', 0, 14, '用户4', '开放');
INSERT INTO `forum` VALUES (65, '2026-10-12 11:15:20', '帖子标题5', '帖子内容5', 0, 15, '用户5', '开放');
INSERT INTO `forum` VALUES (66, '2026-10-12 14:25:36', '帖子标题6', '帖子内容6', 0, 16, '用户6', '开放');

-- ----------------------------
-- Table structure for lingyangxinxi
-- ----------------------------
DROP TABLE IF EXISTS `lingyangxinxi`;
CREATE TABLE `lingyangxinxi`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `chongwubianhao` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宠物编号',
  `chongwumingcheng` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '宠物名称',
  `chongwuleibie` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '宠物类别',
  `tupian` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `zhanghao` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `mima` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `shouji` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `sfsh` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '否' COMMENT '是否审核',
  `shhf` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '审核回复',
  `userid` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1744605441490 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '领养信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lingyangxinxi
-- ----------------------------
INSERT INTO `lingyangxinxi` VALUES (41, '2026-02-28 01:03:56', '宠物编号1', '宠物名称1', '宠物类别1', 'http://localhost:8080/springboot2zemx/upload/lingyangxinxi_tupian1.jpg', '账号1', '密码1', '手机1', '是', '', 1);
INSERT INTO `lingyangxinxi` VALUES (42, '2026-02-28 01:03:56', '宠物编号2', '宠物名称2', '宠物类别2', 'http://localhost:8080/springboot2zemx/upload/lingyangxinxi_tupian2.jpg', '账号2', '密码2', '手机2', '是', '', 2);
INSERT INTO `lingyangxinxi` VALUES (43, '2026-02-28 01:03:56', '宠物编号3', '宠物名称3', '宠物类别3', 'http://localhost:8080/springboot2zemx/upload/lingyangxinxi_tupian3.jpg', '账号3', '密码3', '手机3', '是', '', 3);
INSERT INTO `lingyangxinxi` VALUES (44, '2026-02-28 01:03:56', '宠物编号4', '宠物名称4', '宠物类别4', 'http://localhost:8080/springboot2zemx/upload/lingyangxinxi_tupian4.jpg', '账号4', '密码4', '手机4', '是', '', 4);
INSERT INTO `lingyangxinxi` VALUES (45, '2026-02-28 01:03:56', '宠物编号5', '宠物名称5', '宠物类别5', 'http://localhost:8080/springboot2zemx/upload/lingyangxinxi_tupian5.jpg', '账号5', '密码5', '手机5', '是', '', 5);
INSERT INTO `lingyangxinxi` VALUES (46, '2026-02-28 01:03:56', '宠物编号6', '宠物名称6', '宠物类别6', 'http://localhost:8080/springboot2zemx/upload/lingyangxinxi_tupian6.jpg', '账号6', '密码6', '手机6', '是', '', 6);
INSERT INTO `lingyangxinxi` VALUES (1712023565195, '2024-04-02 10:06:04', '宠物编号1', '宠物名称1', '宠物类别1', 'http://localhost:8080/springboot2zemx/upload/yiqichongwuxinxi_tupian1.jpg', '用户1', '123456', '13823888881', '', '', 11);
INSERT INTO `lingyangxinxi` VALUES (1744605441489, '2025-04-14 12:37:21', '宠物编号2', '宠物名称2', '宠物类别2', 'http://localhost:8080/springboot2zemx/upload/yiqichongwuxinxi_tupian2.jpg', '用户1', '123456', '13823888881', '否', NULL, 11);

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `introduction` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '简介',
  `picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 117 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '领养知识培训' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (111, '2026-02-28 01:03:56', '标题1', '简介1', 'http://localhost:8080/springboot2zemx/upload/news_picture1.jpg', '内容1');
INSERT INTO `news` VALUES (112, '2026-02-28 01:03:56', '标题2', '简介2', 'http://localhost:8080/springboot2zemx/upload/news_picture2.jpg', '内容2');
INSERT INTO `news` VALUES (113, '2026-02-28 01:03:56', '标题3', '简介3', 'http://localhost:8080/springboot2zemx/upload/news_picture3.jpg', '内容3');
INSERT INTO `news` VALUES (114, '2026-02-28 01:03:56', '标题4', '简介4', 'http://localhost:8080/springboot2zemx/upload/news_picture4.jpg', '内容4');
INSERT INTO `news` VALUES (115, '2026-02-28 01:03:56', '标题5', '简介5', 'http://localhost:8080/springboot2zemx/upload/news_picture5.jpg', '内容5');
INSERT INTO `news` VALUES (116, '2026-02-28 01:03:56', '标题6', '简介6', 'http://localhost:8080/springboot2zemx/upload/news_picture6.jpg', '内容6');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `orderid` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `tablename` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'siliaoshoumai' COMMENT '商品表名',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `goodid` bigint(20) NOT NULL COMMENT '商品id',
  `goodname` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `buynumber` int(11) NOT NULL COMMENT '购买数量',
  `price` float NOT NULL DEFAULT 0 COMMENT '价格/积分',
  `discountprice` float NULL DEFAULT 0 COMMENT '折扣价格',
  `total` float NOT NULL DEFAULT 0 COMMENT '总价格/总积分',
  `discounttotal` float NULL DEFAULT 0 COMMENT '折扣总价格',
  `type` int(11) NULL DEFAULT 1 COMMENT '支付类型',
  `status` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `tel` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `consignee` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `orderid`(`orderid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1744605487253 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1712023700639, '2024-04-02 10:08:20', '2024421082030926795', 'siliaoshoumai', 11, 51, '宠物用品名称1', 'http://localhost:8080/springboot2zemx/upload/siliaoshoumai_tupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, 1, '已支付', '宇宙银河系金星1号', '13823888881', '金某');
INSERT INTO `orders` VALUES (1744605487252, '2025-04-14 12:38:07', '2025414123871431954', 'siliaoshoumai', 11, 51, '宠物用品名称1', 'http://localhost:8080/springboot2zemx/upload/siliaoshoumai_tupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, 1, '未支付', '宇宙银河系金星1号', '13823888881', '金某');

-- ----------------------------
-- Table structure for siliaoshoumai
-- ----------------------------
DROP TABLE IF EXISTS `siliaoshoumai`;
CREATE TABLE `siliaoshoumai`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `siliaobianhao` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '宠物用品编号',
  `siliaomingcheng` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '宠物用品名称',
  `tupian` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `siliaoguige` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宠物用品规格',
  `shihechongwu` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '适合宠物',
  `zhuyishixiang` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注意事项',
  `siliaoxiangqing` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '宠物用品详情',
  `thumbsupnum` int(11) NULL DEFAULT 0 COMMENT '赞',
  `crazilynum` int(11) NULL DEFAULT 0 COMMENT '踩',
  `clicktime` datetime NULL DEFAULT NULL COMMENT '最近点击时间',
  `price` float NOT NULL COMMENT '价格',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `siliaobianhao`(`siliaobianhao`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '宠物用品售卖' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of siliaoshoumai
-- ----------------------------
INSERT INTO `siliaoshoumai` VALUES (51, '2026-02-28 01:03:56', '宠物用品编号1', '宠物用品名称1', 'http://localhost:8080/springboot2zemx/upload/siliaoshoumai_tupian1.jpg', '宠物用品规格1', '猫', '注意事项1', '宠物用品详情1', 1, 1, '2026-02-28 20:29:09', 99.9);
INSERT INTO `siliaoshoumai` VALUES (52, '2026-02-28 01:03:56', '宠物用品编号2', '宠物用品名称2', 'http://localhost:8080/springboot2zemx/upload/siliaoshoumai_tupian2.jpg', '宠物用品规格2', '猫', '注意事项2', '宠物用品详情2', 2, 2, '2021-04-29 01:03:56', 99.9);
INSERT INTO `siliaoshoumai` VALUES (53, '2026-02-28 01:03:56', '宠物用品编号3', '宠物用品名称3', 'http://localhost:8080/springboot2zemx/upload/siliaoshoumai_tupian3.jpg', '宠物用品规格3', '猫', '注意事项3', '宠物用品详情3', 3, 3, '2023-04-09 17:39:34', 99.9);
INSERT INTO `siliaoshoumai` VALUES (54, '2026-02-28 01:03:56', '宠物用品编号4', '宠物用品名称4', 'http://localhost:8080/springboot2zemx/upload/siliaoshoumai_tupian4.jpg', '宠物用品规格4', '猫', '注意事项4', '宠物用品详情4', 4, 4, '2021-04-29 01:03:56', 99.9);
INSERT INTO `siliaoshoumai` VALUES (55, '2026-02-28 01:03:56', '宠物用品编号5', '宠物用品名称5', 'http://localhost:8080/springboot2zemx/upload/siliaoshoumai_tupian5.jpg', '宠物用品规格5', '猫', '注意事项5', '宠物用品详情5', 5, 5, '2021-04-29 01:03:56', 99.9);
INSERT INTO `siliaoshoumai` VALUES (56, '2026-02-28 01:03:56', '宠物用品编号6', '宠物用品名称6', 'http://localhost:8080/springboot2zemx/upload/siliaoshoumai_tupian6.jpg', '宠物用品规格6', '猫', '注意事项6', '宠物用品详情6', 6, 6, '2021-04-29 01:03:56', 99.9);

-- ----------------------------
-- Table structure for storeup
-- ----------------------------
DROP TABLE IF EXISTS `storeup`;
CREATE TABLE `storeup`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `refid` bigint(20) NULL DEFAULT NULL COMMENT '收藏id',
  `tablename` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收藏名称',
  `picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收藏图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1712023512737 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of storeup
-- ----------------------------
INSERT INTO `storeup` VALUES (1712023512736, '2024-04-02 10:05:12', 11, 21, 'yiqichongwuxinxi', '宠物名称1', 'http://localhost:8080/springboot2zemx/upload/yiqichongwuxinxi_tupian1.jpg');

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `tablename` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `role` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色',
  `token` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'token表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of token
-- ----------------------------
INSERT INTO `token` VALUES (1, 1, 'abo', 'users', '管理员', 'd616b4ygxdus2qokiw57zyoe2xc77rbm', '2026-02-28 01:05:39', '2026-02-28 21:17:43');
INSERT INTO `token` VALUES (2, 11, '用户1', 'yonghu', '用户', 'tygqypze94clzx5lx8vxq3xbg6p2bsep', '2026-02-28 01:07:10', '2025-05-26 12:13:40');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `role` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'abo', 'abo', '管理员', '2026-02-28 01:03:56');

-- ----------------------------
-- Table structure for yiqichongwuxinxi
-- ----------------------------
DROP TABLE IF EXISTS `yiqichongwuxinxi`;
CREATE TABLE `yiqichongwuxinxi`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `chongwubianhao` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宠物编号',
  `chongwumingcheng` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '宠物名称',
  `chongwuleibie` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '宠物类别',
  `tupian` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `xingbie` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `nianling` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年龄',
  `shifoujueyu` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否绝育',
  `xingqing` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性情',
  `beizhu` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `chongwubianhao`(`chongwubianhao`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '遗弃宠物信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of yiqichongwuxinxi
-- ----------------------------
INSERT INTO `yiqichongwuxinxi` VALUES (21, '2026-02-28 01:03:56', '宠物编号1', '宠物名称1', '宠物类别1', 'http://localhost:8080/springboot2zemx/upload/yiqichongwuxinxi_tupian1.jpg', '公', '年龄1', '是', '性情1', '<p>备注1</p>');
INSERT INTO `yiqichongwuxinxi` VALUES (22, '2026-02-28 01:03:56', '宠物编号2', '宠物名称2', '宠物类别2', 'http://localhost:8080/springboot2zemx/upload/yiqichongwuxinxi_tupian2.jpg', '公', '年龄2', '是', '性情2', '备注2');
INSERT INTO `yiqichongwuxinxi` VALUES (23, '2026-02-28 01:03:56', '宠物编号3', '宠物名称3', '宠物类别3', 'http://localhost:8080/springboot2zemx/upload/yiqichongwuxinxi_tupian3.jpg', '公', '年龄3', '是', '性情3', '备注3');
INSERT INTO `yiqichongwuxinxi` VALUES (24, '2026-02-28 01:03:56', '宠物编号4', '宠物名称4', '宠物类别4', 'http://localhost:8080/springboot2zemx/upload/yiqichongwuxinxi_tupian4.jpg', '公', '年龄4', '是', '性情4', '备注4');
INSERT INTO `yiqichongwuxinxi` VALUES (25, '2026-02-28 01:03:56', '宠物编号5', '宠物名称5', '宠物类别5', 'http://localhost:8080/springboot2zemx/upload/yiqichongwuxinxi_tupian5.jpg', '公', '年龄5', '是', '性情5', '备注5');
INSERT INTO `yiqichongwuxinxi` VALUES (26, '2026-02-28 01:03:56', '宠物编号6', '宠物名称6', '宠物类别6', 'http://localhost:8080/springboot2zemx/upload/yiqichongwuxinxi_tupian6.jpg', '公', '年龄6', '是', '性情6', '备注6');

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zhanghao` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `mima` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `xingming` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `xingbie` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `shouji` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `youxiang` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `shenfenzheng` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `zhaopian` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '照片',
  `money` float NULL DEFAULT 0 COMMENT '余额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `zhanghao`(`zhanghao`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of yonghu
-- ----------------------------
INSERT INTO `yonghu` VALUES (11, '2026-02-28 01:03:56', '用户1', '123456', '姓名1', '男', '13823888881', '773890001@qq.com', '440300199101010001', 'http://localhost:8080/springboot2zemx/upload/yonghu_zhaopian1.jpg', 0.1);
INSERT INTO `yonghu` VALUES (12, '2026-02-28 01:03:56', '用户2', '123456', '姓名2', '男', '13823888882', '773890002@qq.com', '440300199202020002', 'http://localhost:8080/springboot2zemx/upload/yonghu_zhaopian2.jpg', 100);
INSERT INTO `yonghu` VALUES (13, '2026-02-28 01:03:56', '用户3', '123456', '姓名3', '男', '13823888883', '773890003@qq.com', '440300199303030003', 'http://localhost:8080/springboot2zemx/upload/yonghu_zhaopian3.jpg', 100);
INSERT INTO `yonghu` VALUES (14, '2026-02-28 01:03:56', '用户4', '123456', '姓名4', '男', '13823888884', '773890004@qq.com', '440300199404040004', 'http://localhost:8080/springboot2zemx/upload/yonghu_zhaopian4.jpg', 100);
INSERT INTO `yonghu` VALUES (15, '2026-02-28 01:03:56', '用户5', '123456', '姓名5', '男', '13823888885', '773890005@qq.com', '440300199505050005', 'http://localhost:8080/springboot2zemx/upload/yonghu_zhaopian5.jpg', 100);
INSERT INTO `yonghu` VALUES (16, '2026-02-28 01:03:56', '用户6', '123456', '姓名6', '男', '13823888886', '773890006@qq.com', '440300199606060006', 'http://localhost:8080/springboot2zemx/upload/yonghu_zhaopian6.jpg', 100);

SET FOREIGN_KEY_CHECKS = 1;
