/*
Navicat MariaDB Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 100500
Source Host           : localhost:3306
Source Database       : mybatis

Target Server Type    : MariaDB
Target Server Version : 100500
File Encoding         : 65001

Date: 2020-04-23 16:26:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='学生表';

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1', 'king', null);
INSERT INTO `student` VALUES ('2', 'king', null);
INSERT INTO `student` VALUES ('3', 'king', null);
INSERT INTO `student` VALUES ('4', '1', null);
INSERT INTO `student` VALUES ('5', '2', null);
INSERT INTO `student` VALUES ('6', '3', null);
INSERT INTO `student` VALUES ('7', '1', null);
INSERT INTO `student` VALUES ('8', '2', null);
INSERT INTO `student` VALUES ('9', '3', null);
INSERT INTO `student` VALUES ('10', '小老虎', '23');
