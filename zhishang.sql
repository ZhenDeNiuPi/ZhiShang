/*
Navicat MySQL Data Transfer

Source Server         : 本地mysql
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : zhishang

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-06-05 15:58:23
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `book_tb`
-- ----------------------------
DROP TABLE IF EXISTS `book_tb`;
CREATE TABLE `book_tb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL COMMENT '证书名称',
  `get_time` bigint(20) DEFAULT NULL COMMENT '获取时间',
  `content` varchar(200) DEFAULT NULL COMMENT '证书描述',
  `if_show` int(1) DEFAULT NULL COMMENT '是否显示 1是0否',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of book_tb
-- ----------------------------
INSERT INTO `book_tb` VALUES ('1', '啊实打实大所', '1592236800', '啊实打实大撒所大所大撒', '1');
INSERT INTO `book_tb` VALUES ('2', 'dddddddd', '1575302400', 'zzzzzzzzzzzzzzzzzzzzzzzzzzzzzz', '0');

-- ----------------------------
-- Table structure for `case_tb`
-- ----------------------------
DROP TABLE IF EXISTS `case_tb`;
CREATE TABLE `case_tb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `address` varchar(300) DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL,
  `ctime` bigint(20) DEFAULT NULL,
  `if_show` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of case_tb
-- ----------------------------

-- ----------------------------
-- Table structure for `info_tb`
-- ----------------------------
DROP TABLE IF EXISTS `info_tb`;
CREATE TABLE `info_tb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hot` varchar(50) DEFAULT NULL COMMENT '热线',
  `address` varchar(300) DEFAULT NULL COMMENT '地址',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(50) DEFAULT NULL COMMENT '固话',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机',
  `about` varchar(300) DEFAULT NULL,
  `four1` varchar(50) DEFAULT NULL,
  `four2` varchar(50) DEFAULT NULL,
  `four3` varchar(50) DEFAULT NULL,
  `four4` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of info_tb
-- ----------------------------
INSERT INTO `info_tb` VALUES ('1', '2', '1', '1', '1', '1', 'asdasdasdadasdasda', '1', '2', '3', '54');

-- ----------------------------
-- Table structure for `login_iperror_tb`
-- ----------------------------
DROP TABLE IF EXISTS `login_iperror_tb`;
CREATE TABLE `login_iperror_tb` (
  `lock_ip` varchar(100) DEFAULT NULL COMMENT '锁定ip',
  `lock_time` bigint(20) DEFAULT NULL COMMENT '锁定时的时间戳',
  `error_time` bigint(20) DEFAULT NULL COMMENT '错误次数'
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of login_iperror_tb
-- ----------------------------
INSERT INTO `login_iperror_tb` VALUES ('223.72.74.193', '1587286562', '12');
INSERT INTO `login_iperror_tb` VALUES ('60.209.169.53', '1587339736', '0');
INSERT INTO `login_iperror_tb` VALUES ('123.196.14.136', null, '9');
INSERT INTO `login_iperror_tb` VALUES ('223.104.212.202', null, '10');
INSERT INTO `login_iperror_tb` VALUES ('219.146.255.126', null, '10');
INSERT INTO `login_iperror_tb` VALUES ('60.208.194.134', null, '9');
INSERT INTO `login_iperror_tb` VALUES ('112.224.70.84', null, '1');
INSERT INTO `login_iperror_tb` VALUES ('219.146.91.110', '1587377281', '6');
INSERT INTO `login_iperror_tb` VALUES ('0:0:0:0:0:0:0:1', null, '0');
INSERT INTO `login_iperror_tb` VALUES ('127.0.0.1', null, '0');

-- ----------------------------
-- Table structure for `news_tb`
-- ----------------------------
DROP TABLE IF EXISTS `news_tb`;
CREATE TABLE `news_tb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` bigint(20) DEFAULT NULL COMMENT '新闻时间',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `stitle` varchar(100) DEFAULT NULL COMMENT '副标题 小标题',
  `creator` varchar(20) DEFAULT NULL COMMENT '作者',
  `content` longtext COMMENT 'html正文',
  `if_show` int(1) DEFAULT NULL COMMENT '是否展示 0否1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of news_tb
-- ----------------------------
INSERT INTO `news_tb` VALUES ('4', '1591632000', 'aa', 'vv', 'cc', '<p><br></p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</p><hr><h1>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; asdasdasdasd</h1><p><br></p><p><img src=\"news/getContentPic?filename=4_0&amp;s=0.7781713921399958\" style=\"width: 307px;\">aaa<br></p>', '1');

-- ----------------------------
-- Table structure for `user_tb`
-- ----------------------------
DROP TABLE IF EXISTS `user_tb`;
CREATE TABLE `user_tb` (
  `id` bigint(20) NOT NULL,
  `account` varchar(50) DEFAULT NULL COMMENT '账户',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of user_tb
-- ----------------------------
INSERT INTO `user_tb` VALUES ('1', 'admin', '12946e291689abaf5af28e859e93619c');


-- 20200609 联系我们ip限制表
DROP TABLE IF EXISTS `contact_ip_tb`;
CREATE TABLE `contact_ip_tb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- 20200620 新闻表添加类型 
ALTER TABLE `news_tb`
ADD COLUMN `type`  int(1) NULL DEFAULT 0 COMMENT '新闻类型 0新闻资讯1行业资讯' AFTER `if_show`;

-- 20200624 增加发展历史表  公司信息表添加公司理念  新闻表（现已是图文编辑表） 添加产品模块类型
DROP TABLE IF EXISTS `history_tb`;
CREATE TABLE `history_tb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) DEFAULT NULL COMMENT '发展历程标题',
  `content` varchar(500) DEFAULT NULL COMMENT '发展历程正文',
  `time` bigint(20) DEFAULT NULL COMMENT '发展历程时间线',
  `if_show` int(1) DEFAULT NULL COMMENT '是否显示 0否1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=gbk;

ALTER TABLE `info_tb`
ADD COLUMN `idea`  varchar(300) DEFAULT NULL COMMENT '公司理念' AFTER `four4`;
ALTER TABLE `news_tb`
MODIFY COLUMN `type`  int(1) NULL DEFAULT 0 COMMENT '新闻类型 0新闻资讯1行业资讯2产品模块1' AFTER `if_show`;

-- 20200628 四个优势 优势修改为标题+正文

ALTER TABLE `info_tb`
MODIFY COLUMN `about`  varchar(300) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '关于我们' AFTER `mobile`,
MODIFY COLUMN `four1`  varchar(50) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '优势1标题' AFTER `about`,
MODIFY COLUMN `four2`  varchar(50) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '优势2标题' AFTER `four1`,
MODIFY COLUMN `four3`  varchar(50) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '优势3标题' AFTER `four2`,
MODIFY COLUMN `four4`  varchar(50) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '优势4标题' AFTER `four3`,
ADD COLUMN `four1content`  varchar(300) NULL COMMENT '优势1正文' AFTER `idea`,
ADD COLUMN `four2content`  varchar(300) NULL COMMENT '优势2正文' AFTER `four1content`,
ADD COLUMN `four3content`  varchar(300) NULL COMMENT '优势3正文' AFTER `four2content`,
ADD COLUMN `four4content`  varchar(300) NULL COMMENT '优势4正文' AFTER `four3content`;


-- 20200704 团队介绍  贡献
ALTER TABLE `info_tb`
ADD COLUMN `intro1content`  varchar(50) NULL COMMENT '团队介绍1正文' AFTER `four4content`,
ADD COLUMN `intro1num`  varchar(20) NULL COMMENT '团队介绍1数量' AFTER `intro1content`,
ADD COLUMN `intro1unit`  varchar(5) NULL COMMENT '团队介绍1单位' AFTER `intro1num`,
ADD COLUMN `intro2content`  varchar(50) NULL COMMENT '团队介绍2正文' AFTER `intro1unit`,
ADD COLUMN `intro2num`  varchar(20) NULL COMMENT '团队介绍2数量' AFTER `intro2content`,
ADD COLUMN `intro2unit`  varchar(5) NULL COMMENT '团队介绍2单位' AFTER `intro2num`,
ADD COLUMN `intro3content`  varchar(50) NULL COMMENT '团队介绍3正文' AFTER `intro2unit`,
ADD COLUMN `intro3num`  varchar(20) NULL COMMENT '团队介绍3数量' AFTER `intro3content`,
ADD COLUMN `intro3unit`  varchar(5) NULL COMMENT '团队介绍3单位' AFTER `intro3num`;

ALTER TABLE `info_tb`
ADD COLUMN `contri1content`  varchar(50) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献1正文' AFTER `intro3unit`,
ADD COLUMN `contri1num`  varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献1数量' AFTER `contri1content`,
ADD COLUMN `contri1unit`  varchar(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献1单位' AFTER `contri1num`,
ADD COLUMN `contri2content`  varchar(50) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献2正文' AFTER `contri1unit`,
ADD COLUMN `contri2num`  varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献2数量' AFTER `contri2content`,
ADD COLUMN `contri2unit`  varchar(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献2单位' AFTER `contri2num`,
ADD COLUMN `contri3content`  varchar(50) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献3正文' AFTER `contri2unit`,
ADD COLUMN `contri3num`  varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献3数量' AFTER `contri3content`,
ADD COLUMN `contri3unit`  varchar(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献3单位' AFTER `contri3num`,
ADD COLUMN `contri4content`  varchar(50) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献3正文' AFTER `contri3unit`,
ADD COLUMN `contri4num`  varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献4数量' AFTER `contri4content`,
ADD COLUMN `contri4unit`  varchar(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '贡献4单位' AFTER `contri4num`;


-- 20200705
ALTER TABLE `info_tb`
ADD COLUMN `lat`  varchar(20) NULL COMMENT '纬度' AFTER `contri4unit`,
ADD COLUMN `lon`  varchar(20) NULL COMMENT '经度' AFTER `lat`;

