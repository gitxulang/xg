/*
 Navicat Premium Data Transfer

 Source Server         : 10.240.53.28
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 10.240.53.28:3306
 Source Schema         : ant

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 01/07/2021 19:10:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for nms_admin
-- ----------------------------
DROP TABLE IF EXISTS `nms_admin`;
CREATE TABLE `nms_admin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id,关联nms_system_role表的id',
  `dept_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门id, 关联nms_department的id值,采用uuid为主键',
  `username` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码，加密处理',
  `realname` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `sex` int(1) NULL DEFAULT 1 COMMENT '性别, 1表示男, 2表示女',
  `phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `deled` int(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记, 0未删除, 1删除',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  `last_error_time` timestamp(0) NOT NULL DEFAULT '2018-12-31 03:00:00' COMMENT '最近一次登录错误的时间',
  `login_count` int(2) NOT NULL DEFAULT 5 COMMENT '剩余登录次数',
  `last_password_change_time` timestamp(0) NOT NULL DEFAULT '2018-12-31 03:00:00' COMMENT '最新密码修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_admin_rid`(`role_id`) USING BTREE,
  INDEX `fk_admin_deptid`(`dept_id`) USING BTREE,
  CONSTRAINT `fk_admin_deptid` FOREIGN KEY (`dept_id`) REFERENCES `nms_department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_admin_rid` FOREIGN KEY (`role_id`) REFERENCES `nms_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_admin
-- ----------------------------
INSERT INTO `nms_admin` VALUES (1, 1, '1', 'sysadm', '82D9DE5F63430CDF41C90E559AC0A1E4', 'sysadm', 1, '', '', 0, '2021-07-01 13:01:17', '2021-07-01 13:00:27', 5, '2021-07-01 11:21:15');
INSERT INTO `nms_admin` VALUES (2, 2, '1', 'secadm', '82D9DE5F63430CDF41C90E559AC0A1E4', 'secadm', 1, '', '', 0, '2021-07-01 13:01:14', '2020-07-03 02:44:33', 5, '2021-07-01 11:21:15');
INSERT INTO `nms_admin` VALUES (3, 3, '1', 'auditadm', '82D9DE5F63430CDF41C90E559AC0A1E4', 'auditadm', 1, '', '', 0, '2021-07-01 13:01:11', '2020-06-24 16:09:40', 5, '2021-07-01 11:21:15');
INSERT INTO `nms_admin` VALUES (4, 1, '1', 'root', '82D9DE5F63430CDF41C90E559AC0A1E4', 'root', 1, '', '', 0, '2021-07-01 11:06:08', '2021-07-01 10:56:55', 5, '2021-07-01 11:21:15');

-- ----------------------------
-- Table structure for nms_alarm
-- ----------------------------
DROP TABLE IF EXISTS `nms_alarm`;
CREATE TABLE `nms_alarm`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT '资产id, 与nms_asset表id相关联',
  `a_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称, 定义规则指标的告警名称',
  `a_index` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产指标索引值, 例nms_rule_asset_index的index_id',
  `a_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警事件类型，人工处理，系统轮询，自动恢复',
  `a_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '告警事件具体内容',
  `a_level` int(3) NULL DEFAULT NULL COMMENT '告警事件级别 1, 2, 3',
  `d_status` int(3) NULL DEFAULT NULL COMMENT '告警事件deal状态 0未处理, 1处理中, 2已处理, 3自动恢复, 4逻辑删除',
  `d_people` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警事件处理人',
  `d_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警处理时间, 2018-02-02 12:52:52',
  `s_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第一次告警开始的时间, 2018-02-02 12:52:52',
  `a_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警发生的时间, 2018-02-02 12:52:52',
  `a_count` int(11) NOT NULL DEFAULT 0 COMMENT '告警连续累加的次数',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '告警入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_alarm_assetid`(`asset_id`) USING BTREE,
  CONSTRAINT `fk_alarm_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_alarm_report
-- ----------------------------
DROP TABLE IF EXISTS `nms_alarm_report`;
CREATE TABLE `nms_alarm_report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `alarm_id` int(11) NULL DEFAULT NULL COMMENT '告警事件表nms_alarm表的id, 为其外键',
  `r_people` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报告report填写人',
  `r_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '报告填写内容',
  `d_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解决告警事件时间, 2018-02-02 12:52:52',
  `r_time` timestamp(0) NULL DEFAULT NULL COMMENT '填写报告的时间',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '告警入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_alarmreport_alarmid`(`alarm_id`) USING BTREE,
  CONSTRAINT `fk_alarmreport_alarmid` FOREIGN KEY (`alarm_id`) REFERENCES `nms_alarm` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_alarm_soft
-- ----------------------------
DROP TABLE IF EXISTS `nms_alarm_soft`;
CREATE TABLE `nms_alarm_soft`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `soft_id` int(11) NULL DEFAULT NULL COMMENT '软件id, 与nms_soft表id相关联',
  `a_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称, 定义规则指标的告警名称',
  `a_index` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '软件指标索引值, 例nms_rule_soft_index的index_id',
  `a_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警事件类型，人工处理，系统轮询，自动恢复',
  `a_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '告警事件具体内容',
  `a_level` int(3) NULL DEFAULT NULL COMMENT '告警事件级别 1, 2, 3',
  `d_status` int(3) NULL DEFAULT NULL COMMENT '告警事件deal状态 0未处理, 1处理中, 2已处理, 3自动恢复, 4逻辑删除',
  `d_people` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警事件处理人',
  `d_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警处理时间, 2018-02-02 12:52:52',
  `s_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第一次告警开始的时间, 2018-02-02 12:52:52',
  `a_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警发生的时间, 2018-02-02 12:52:52',
  `a_count` int(11) NOT NULL DEFAULT 0 COMMENT '告警连续累加的次数',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '告警入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_alarm_softid`(`soft_id`) USING BTREE,
  CONSTRAINT `fk_alarm_softid` FOREIGN KEY (`soft_id`) REFERENCES `nms_soft` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_alarm_soft_report
-- ----------------------------
DROP TABLE IF EXISTS `nms_alarm_soft_report`;
CREATE TABLE `nms_alarm_soft_report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `alarm_id` int(11) NULL DEFAULT NULL COMMENT '告警事件表nms_alarm_soft表的id, 为其外键',
  `r_people` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报告report填写人',
  `r_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '报告填写内容',
  `d_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解决告警事件时间, 2018-02-02 12:52:52',
  `r_time` timestamp(0) NULL DEFAULT NULL COMMENT '填写报告的时间',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '告警入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_alarmsoftreport_alarmid`(`alarm_id`) USING BTREE,
  CONSTRAINT `fk_alarmsoftreport_alarmid` FOREIGN KEY (`alarm_id`) REFERENCES `nms_alarm_soft` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_asset
-- ----------------------------
DROP TABLE IF EXISTS `nms_asset`;
CREATE TABLE `nms_asset`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `a_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '设备ip,唯一存在',
  `bm_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '设备bm_ip',
  `yw_ip` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '设备yw_ip,多个用_分割',
  `a_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '设备名称',
  `a_no` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '设备soc卡Id,唯一存在',
  `type_id` int(11) NULL DEFAULT 0 COMMENT '设备类别,与nms_asset_type表id关联',
  `online` int(11) NULL DEFAULT 0 COMMENT '客户端在线离线状态:0离线,1在线',
  `a_pos` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '设备存放位置',
  `a_manu` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '设备厂商名称',
  `a_date` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '设备采购日期,按照2017-05-05格式存储',
  `a_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '设备责任人,通过选择nms_user表中人名,这里不做外键关联只做asset表的属性',
  `dept_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备所属部门,与nms_department中id关联',
  `colled` tinyint(2) NULL DEFAULT 0 COMMENT '是否采集数据,0采集,1不采集',
  `coll_mode` int(3) NULL DEFAULT 0 COMMENT '设备数据采集方式ZYJ:0,ICMP:1,SNMP:2等方式',
  `auth_pass` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '专用服务端口,SNMP服务端口',
  `r_comm` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'SNMPv1和v2c服务读团体',
  `w_comm` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'SNMPv1和v2c服务写团体',
  `username` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'SNMPv3服务用户名,SSH服务用户名',
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'SNMPv3服务用户密码,设备SSH服务用户密码',
  `sshport` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '22' COMMENT 'SNMPv3服务认证密码,设备SSH服务端口号',
  `deled` int(1) NULL DEFAULT 0 COMMENT '逻辑删除标记0:未删除,1已删除',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_asset_typeid`(`type_id`) USING BTREE,
  INDEX `fd_asset_deptid`(`dept_id`) USING BTREE,
  CONSTRAINT `fd_asset_deptid` FOREIGN KEY (`dept_id`) REFERENCES `nms_department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_asset_typeid` FOREIGN KEY (`type_id`) REFERENCES `nms_asset_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_asset_type
-- ----------------------------
DROP TABLE IF EXISTS `nms_asset_type`;
CREATE TABLE `nms_asset_type`  (
  `id` int(11) NOT NULL COMMENT '主键id表示设备类别',
  `ch_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '大类中文名称, 例如服务器',
  `ch_subtype` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '小类中文名称, 例如中科方德专用服务器v1.0',
  `node_tag` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'net' COMMENT 'net表示设备资产表,sof表示软件资产',
  `img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '某类资产在topo树中的图片',
  `img_wid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '监控节点图片的宽度',
  `img_hei` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '监控节点图片的高度',
  `rel_id` int(11) NOT NULL COMMENT '关联nms_collect表rel_id字段,0表示专用协议不需要采集器',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_asset_type
-- ----------------------------
INSERT INTO `nms_asset_type` VALUES (0, '其它设备', '其它设备', 'net', './img/server/server_special_green.png', '70', '70', 0, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (1, '专用计算机', '中科方德专用计算机', 'net', './img/middleware/middleware_special_green.png', '70', '70', 0, '2020-12-04 11:29:33');
INSERT INTO `nms_asset_type` VALUES (2, '专用计算机', '中标麒麟专用计算机', 'net', './img/middleware/middleware_special_green.png', '70', '70', 0, '2020-12-04 11:29:33');
INSERT INTO `nms_asset_type` VALUES (3, '专用计算机', '银河麒麟V10专用计算机', 'net', './img/middleware/middleware_special_green.png', '70', '70', 0, '2020-12-04 11:29:33');
INSERT INTO `nms_asset_type` VALUES (4, '专用工作站', '中科方德专用工作站', 'net', './img/server/server_special_green.png', '70', '70', 0, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (5, '专用服务器', '中科方德专用服务器', 'net', './img/server/server_special_green.png', '70', '70', 0, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (6, '专用服务器', '中标麒麟专用服务器', 'net', './img/server/server_special_green.png', '70', '70', 0, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (7, '专用服务器', '银河麒麟V10专用服务器', 'net', './img/server/server_special_green.png', '70', '70', 0, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (8, '管理服务器', '专用管理服务器', 'net', './img/server/server_special_green.png', '70', '70', 0, '2020-09-08 17:00:00');
INSERT INTO `nms_asset_type` VALUES (9, '通用服务器', 'LINUX通用服务器', 'net', './img/server/server_linux_common_green.png', '70', '70', 1, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (10, '通用服务器', 'WINDOWS通用服务器', 'net', './img/server/server_windows_common_green.png', '70', '70', 2, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (11, '专用数通设备', '华为专用交换机', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 4, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (12, '专用数通设备', '迈普专用交换机', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 5, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (13, '专用数通设备', '锐捷专用交换机', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 6, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (14, '专用数通设备', '风云专用交换机', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 7, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (15, '专用数通设备', '中国网安专用交换机', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 8, '2020-10-20 11:23:36');
INSERT INTO `nms_asset_type` VALUES (16, '通用数通设备', '华为通用S2700交换机', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 3, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (17, '通用数通设备', '华为通用S5700交换机', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 3, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (18, '通用数通设备', '华为通用S3700交换机', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 3, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (19, '专用网络安全设备', '专用IDS', 'net', './img/IDS/IDS_common_green.png', '70', '70', 0, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (20, '专用网络安全设备', '专用防火墙', 'net', './img/firewall/firewall_common_green.png', '70', '70', 0, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (21, '专用网络安全设备', '专用网络接入控制网关', 'net', './img/IDS/IDS_common_green.png', '70', '70', 0, '2020-12-04 11:25:08');
INSERT INTO `nms_asset_type` VALUES (22, '专用数据库', '专用数据库', 'sof', './img/database/database_common_green.png', '70', '70', 0, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (23, '专用中间件', '专用中间件', 'sof', './img/database/middleware_common_green.png', '70', '70', 0, '2020-09-01 23:10:15');
INSERT INTO `nms_asset_type` VALUES (24, '专用数通设备', '华为专用路由器', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 4, '2020-10-20 00:00:00');
INSERT INTO `nms_asset_type` VALUES (25, '专用数通设备', '迈普专用路由器', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 5, '2020-10-20 00:00:00');
INSERT INTO `nms_asset_type` VALUES (26, '专用数通设备', '锐捷专用路由器', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 6, '2020-10-20 00:00:00');
INSERT INTO `nms_asset_type` VALUES (27, '专用数通设备', '风云专用路由器', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 7, '2020-10-20 00:00:00');
INSERT INTO `nms_asset_type` VALUES (28, '专用数通设备', '中国网安专用路由器', 'net', './img/interchanger/interchanger_access_common_green.png', '70', '70', 8, '2020-10-20 00:00:00');

-- ----------------------------
-- Table structure for nms_audit_config
-- ----------------------------
DROP TABLE IF EXISTS `nms_audit_config`;
CREATE TABLE `nms_audit_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `dbbasedir` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '数据库安装目录',
  `dbdatadir` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '数据库数据目录',
  `dbdatasize` bigint(20) NOT NULL DEFAULT 0 COMMENT '数据库数据总容量单位KB',
  `partdir` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '数据库所在分区路径',
  `partsize` bigint(20) NOT NULL DEFAULT 0 COMMENT '数据库所在分区总容量单位KB',
  `partused` bigint(20) NOT NULL DEFAULT 0 COMMENT '数据库所在分区已用容量单位KB',
  `rule` int(11) NOT NULL DEFAULT 50 COMMENT '数据库所在分区告警阈值单位%',
  `alarm` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '数据库所在分区告警内容没有告警则空',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_audit_config
-- ----------------------------
INSERT INTO `nms_audit_config` VALUES (1, '/usr', '/var/lib/mysql/', 0, '/', 314419200, 15075264, 50, '数据库所在磁盘分区使用率正常！', '2020-12-31 01:44:21');

-- ----------------------------
-- Table structure for nms_audit_log
-- ----------------------------
DROP TABLE IF EXISTS `nms_audit_log`;
CREATE TABLE `nms_audit_log`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键uui',
  `ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '日志主体操作的ip地址',
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '日志主体: 登录用户名',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '日志事件内容',
  `modname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '日志客体: 操作的具体模块',
  `logtype` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '日志类型:管理员日志',
  `a_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '日志时间',
  `logrest` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '日志结果: 成功还是失败',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '日志入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_audit_log
-- ----------------------------
INSERT INTO `nms_audit_log` VALUES ('261cc1408cd24992b3d7190366c155d7', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 18:59:26', '成功', '2021-07-01 18:59:26');
INSERT INTO `nms_audit_log` VALUES ('2fc3ac8687394b259abaa73fdc3428a4', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 18:59:24', '成功', '2021-07-01 18:59:24');
INSERT INTO `nms_audit_log` VALUES ('36781bb0a4304281a124882a2c7fa5b4', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 19:00:36', '成功', '2021-07-01 19:00:35');
INSERT INTO `nms_audit_log` VALUES ('3e82e1b1ba2c4633beca6540440c8c5d', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 18:59:28', '成功', '2021-07-01 18:59:28');
INSERT INTO `nms_audit_log` VALUES ('4db49463a797435da59728de16203869', '0:0:0:0:0:0:0:1', 'root', 'root登录系统', '登录管理', '系统登录', '2021-07-01 18:51:22', '成功', '2021-07-01 18:51:22');
INSERT INTO `nms_audit_log` VALUES ('56e2b30fd02f42d3b8b2654588ee488e', '0:0:0:0:0:0:0:1', 'secadm', 'secadm注销登录，登出系统', '登录管理', '系统登出', '2021-07-01 19:02:40', '成功', '2021-07-01 19:02:40');
INSERT INTO `nms_audit_log` VALUES ('6cb5353ce2d84b9ba5aa7de7800ca8c6', '0:0:0:0:0:0:0:1', 'auditadm', 'auditadm登录系统', '登录管理', '系统登录', '2021-07-01 18:52:11', '成功', '2021-07-01 18:52:11');
INSERT INTO `nms_audit_log` VALUES ('6fc4f079906541a4b4921cba745c011b', '0:0:0:0:0:0:0:1', 'secadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 19:02:36', '成功', '2021-07-01 19:02:36');
INSERT INTO `nms_audit_log` VALUES ('7570ea5858bc4250b458fe16572c8838', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 19:00:18', '成功', '2021-07-01 19:00:18');
INSERT INTO `nms_audit_log` VALUES ('77642bcd4a59491e8fa12543cc841307', '0:0:0:0:0:0:0:1', 'auditadm', 'auditadm注销登录，登出系统', '登录管理', '系统登出', '2021-07-01 18:51:16', '成功', '2021-07-01 18:51:16');
INSERT INTO `nms_audit_log` VALUES ('83c98c34b7b54963bf3a8556f0484d8b', '0:0:0:0:0:0:0:1', 'secadm', 'secadm登录系统', '登录管理', '系统登录', '2021-07-01 19:02:26', '成功', '2021-07-01 19:02:26');
INSERT INTO `nms_audit_log` VALUES ('84817e78b97248dab18bb79b0cc02824', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 18:59:19', '成功', '2021-07-01 18:59:19');
INSERT INTO `nms_audit_log` VALUES ('9064b8c523154868a7f418ce78b55258', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 19:00:43', '成功', '2021-07-01 19:00:43');
INSERT INTO `nms_audit_log` VALUES ('99576395027e4fc0ae86db7259ce5e7c', '0:0:0:0:0:0:0:1', 'auditadm', 'auditadm注销登录，登出系统', '登录管理', '系统登出', '2021-07-01 19:00:47', '成功', '2021-07-01 19:00:47');
INSERT INTO `nms_audit_log` VALUES ('a88e8a40717d4a3c9e833f551637304b', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 18:59:31', '成功', '2021-07-01 18:59:31');
INSERT INTO `nms_audit_log` VALUES ('bad46a054a304a2ea5c81c27254aa15d', '0:0:0:0:0:0:0:1', 'root', 'root登录系统', '登录管理', '系统登录', '2021-07-01 19:02:44', '成功', '2021-07-01 19:02:44');
INSERT INTO `nms_audit_log` VALUES ('c0a5347ede4445108fc38e4111b6bd08', '0:0:0:0:0:0:0:1', 'secadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 19:02:27', '成功', '2021-07-01 19:02:27');
INSERT INTO `nms_audit_log` VALUES ('c694830878534389a4a46657afa10d64', '0:0:0:0:0:0:0:1', 'secadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 19:02:38', '成功', '2021-07-01 19:02:38');
INSERT INTO `nms_audit_log` VALUES ('c9f27a3aba344538897a078ddc32fb08', '0:0:0:0:0:0:0:1', 'root', 'root注销登录，登出系统', '登录管理', '系统登出', '2021-07-01 18:52:01', '成功', '2021-07-01 18:52:01');
INSERT INTO `nms_audit_log` VALUES ('cd3436f250b1424f87d6e72cb26ddf1b', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 19:00:31', '成功', '2021-07-01 19:00:31');
INSERT INTO `nms_audit_log` VALUES ('d24774f414b9454a86eeee4ad3980552', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 18:47:04', '成功', '2021-07-01 18:47:04');
INSERT INTO `nms_audit_log` VALUES ('e2cca22091ce4fe788d733fe4b78d032', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 18:44:28', '成功', '2021-07-01 18:44:28');
INSERT INTO `nms_audit_log` VALUES ('e51f550161684d43a0cb26db4fffcdf8', '0:0:0:0:0:0:0:1', 'root', '修改了权限 [审计管理员] 成功', '系统管理', '修改数据', '2021-07-01 18:51:36', '成功', '2021-07-01 18:51:36');
INSERT INTO `nms_audit_log` VALUES ('e9ac0393039d44b5bc51922c3d31a42e', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 18:54:40', '成功', '2021-07-01 18:54:40');
INSERT INTO `nms_audit_log` VALUES ('f346bb59b1aa4a80b4b53ea589e05d0b', '0:0:0:0:0:0:0:1', 'auditadm', '查看审计日志', '日志管理', '查询数据', '2021-07-01 18:47:52', '成功', '2021-07-01 18:47:52');

-- ----------------------------
-- Table structure for nms_bmc_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_bmc_info`;
CREATE TABLE `nms_bmc_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `cpu_num` int(4) NULL DEFAULT NULL COMMENT 'cpu插槽个数, ipmi读取的cpu插槽温度个数',
  `cpu_temp` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '每个cpu插槽温度, 如果no reading或者ns显示 -100, 保留2位小数, 用逗号分割',
  `dimm_num` int(4) NULL DEFAULT NULL COMMENT '内存插槽dimm个数, ipmi读取的内存插槽温度个数',
  `dimm_temp` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内存插槽dimm温度, 如果no reading或者ns显示 -100, 保留2位小数, 用逗号分割',
  `raid_temp` float NULL DEFAULT NULL COMMENT 'raid卡温度, 如果no reading或者ns显示 -100, 保留2位小数',
  `inlet_temp` float NULL DEFAULT NULL COMMENT '机箱进风口温度, 如果no reading或者ns显示 -100, 保留2位小数',
  `outlet_temp` float NULL DEFAULT NULL COMMENT '机箱出风口温度, 如果no reading或者ns显示 -100, 保留2位小数',
  `fan_num` int(4) NULL DEFAULT NULL COMMENT '风扇个数, ipmi读取风扇的个数',
  `fan_speed` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风扇转速, 如果no reading或者不存在 -100, 用逗号分割',
  `psu_num` int(4) NULL DEFAULT NULL COMMENT '电源个数, ipmi读取电源的个数',
  `psu_temp` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电源温度, 如果no reading或者不存在 -100, 保留2位小数, 用逗号分割',
  `psu_power` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电源功率, 如果no reading或者不存在 -100, 用逗号分割',
  `voltage_12v` float NULL DEFAULT NULL COMMENT '12v供电电压, 如果no reading或者不存在 -100, 保留3位小数',
  `voltage_5v` float NULL DEFAULT NULL COMMENT '5v供电电压, 如果no reading或者不存在 -100, 保留3位小数',
  `voltage_3v3` float NULL DEFAULT NULL COMMENT '3.3v供电电压, 如果no reading或者不存在 -100, 保留3位小数',
  `voltage_bat` float NULL DEFAULT NULL COMMENT 'vbat电池电压, 如果no reading或者不存在 -100, 保留3位小数',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_bmcassetid_assetid`(`asset_id`) USING BTREE,
  CONSTRAINT `fk_bmcassetid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_collect_module
-- ----------------------------
DROP TABLE IF EXISTS `nms_collect_module`;
CREATE TABLE `nms_collect_module`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `rel_id` int(11) NULL DEFAULT NULL COMMENT '采集器关联id',
  `coll_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '采集器名称',
  `coll_cycle` int(11) NULL DEFAULT NULL COMMENT '采集器周期, 单位秒',
  `coll_thread` int(11) NULL DEFAULT NULL COMMENT '最大线程数',
  `coll_process` int(11) NULL DEFAULT NULL COMMENT '最大进程数',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '插入时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 45 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_collect_module
-- ----------------------------
INSERT INTO `nms_collect_module` VALUES (1, 1, 'staticInfoCollector', 10, 1, 1, '2021-01-21 05:13:12');
INSERT INTO `nms_collect_module` VALUES (2, 1, 'dynamicInfoCollector', 10, 1, 1, '2021-01-21 05:13:15');
INSERT INTO `nms_collect_module` VALUES (3, 1, 'linuxCpuCollector', 1, 1, 1, '2019-08-05 14:22:12');
INSERT INTO `nms_collect_module` VALUES (4, 1, 'linuxMemCollector', 1, 1, 1, '2019-08-05 14:22:12');
INSERT INTO `nms_collect_module` VALUES (5, 1, 'linuxFilesysCollector', 180, 1, 1, '2019-11-26 23:46:56');
INSERT INTO `nms_collect_module` VALUES (6, 1, 'interface32Collector', 5, 1, 1, '2019-08-05 14:22:12');
INSERT INTO `nms_collect_module` VALUES (7, 1, 'linuxProcessCollector', 180, 1, 1, '2019-11-26 23:46:56');
INSERT INTO `nms_collect_module` VALUES (8, 2, 'staticInfoCollector', 10, 1, 1, '2021-01-21 05:13:22');
INSERT INTO `nms_collect_module` VALUES (9, 2, 'dynamicInfoCollector', 10, 1, 1, '2021-01-21 05:13:24');
INSERT INTO `nms_collect_module` VALUES (10, 2, 'windowsCpuCollector', 1, 1, 1, '2019-08-05 14:22:12');
INSERT INTO `nms_collect_module` VALUES (11, 2, 'windowsMemCollector', 5, 1, 1, '2019-08-05 14:22:12');
INSERT INTO `nms_collect_module` VALUES (12, 2, 'windowsFilesysCollector', 180, 1, 1, '2019-11-26 23:46:56');
INSERT INTO `nms_collect_module` VALUES (13, 2, 'interface32Collector', 5, 1, 1, '2019-08-05 14:22:12');
INSERT INTO `nms_collect_module` VALUES (14, 2, 'windowsProcessCollector', 180, 1, 1, '2019-11-26 23:46:56');
INSERT INTO `nms_collect_module` VALUES (15, 3, 'staticInfoCollector', 10, 1, 1, '2021-01-21 05:13:31');
INSERT INTO `nms_collect_module` VALUES (16, 3, 'dynamicInfoCollector', 10, 1, 1, '2021-01-21 05:13:34');
INSERT INTO `nms_collect_module` VALUES (17, 3, 'hwSwitchCpuCollector', 1, 1, 1, '2019-08-05 14:22:12');
INSERT INTO `nms_collect_module` VALUES (18, 3, 'hwSwitchMemCollector', 10, 1, 1, '2019-08-05 14:22:12');
INSERT INTO `nms_collect_module` VALUES (19, 3, 'interface64Collector', 5, 1, 1, '2019-08-05 14:22:12');
INSERT INTO `nms_collect_module` VALUES (32, 6, 'rjSwitchCpuCollectorV3', 1, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (31, 6, 'dynamicInfoCollectorV3', 10, 1, 1, '2021-01-21 05:13:36');
INSERT INTO `nms_collect_module` VALUES (30, 6, 'rjStaticInfoCollectorV3', 10, 1, 1, '2021-01-21 05:13:39');
INSERT INTO `nms_collect_module` VALUES (29, 5, 'interface64CollectorV3', 10, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (28, 5, 'mpSwitchMemCollectorV3', 10, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (27, 5, 'mpSwitchCpuCollectorV3', 1, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (26, 5, 'dynamicInfoCollectorV3', 10, 1, 1, '2021-01-21 05:13:44');
INSERT INTO `nms_collect_module` VALUES (25, 5, 'mpStaticInfoCollectorV3', 10, 1, 1, '2021-01-21 05:13:48');
INSERT INTO `nms_collect_module` VALUES (24, 4, 'hwInterface64CollectorV3', 10, 1, 1, '2020-12-17 23:11:31');
INSERT INTO `nms_collect_module` VALUES (23, 4, 'hwSwitchMemCollectorV3', 10, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (22, 4, 'hwSwitchCpuCollectorV3', 1, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (21, 4, 'hwDynamicInfoCollectorV3', 10, 1, 1, '2021-01-21 05:13:49');
INSERT INTO `nms_collect_module` VALUES (20, 4, 'hwStaticInfoCollectorV3', 10, 1, 1, '2021-01-21 05:13:51');
INSERT INTO `nms_collect_module` VALUES (33, 6, 'rjSwitchMemCollectorV3', 10, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (34, 6, 'interface64CollectorV3', 10, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (35, 7, 'fyStaticInfoCollectorV3', 10, 1, 1, '2021-01-21 05:13:54');
INSERT INTO `nms_collect_module` VALUES (36, 7, 'dynamicInfoCollectorV3', 10, 1, 1, '2021-01-21 05:13:57');
INSERT INTO `nms_collect_module` VALUES (37, 7, 'fySwitchCpuCollectorV3', 1, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (38, 7, 'fySwitchMemCollectorV3', 10, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (39, 7, 'interface64CollectorV3', 10, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (40, 8, 'zgwaStaticInfoCollectorV3', 10, 1, 1, '2021-01-21 05:14:00');
INSERT INTO `nms_collect_module` VALUES (41, 8, 'dynamicInfoCollectorV3', 10, 1, 1, '2021-01-21 05:14:02');
INSERT INTO `nms_collect_module` VALUES (42, 8, 'zgwaSwitchCpuCollectorV3', 1, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (43, 8, 'zgwaSwitchMemCollectorV3', 10, 1, 1, '2020-10-19 14:00:00');
INSERT INTO `nms_collect_module` VALUES (44, 8, 'interface64CollectorV3', 10, 1, 1, '2020-10-19 14:00:00');

-- ----------------------------
-- Table structure for nms_config
-- ----------------------------
DROP TABLE IF EXISTS `nms_config`;
CREATE TABLE `nms_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `jms_switch` int(11) NOT NULL DEFAULT 1 COMMENT 'jms接收基础数据开关:0不接收,1接收',
  `jms_interval` int(11) NOT NULL DEFAULT 5 COMMENT '检测jms服务的周期单位分钟',
  `jms_address` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '127.0.0.1' COMMENT '检测jms服务的IP地址',
  `jms_port` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '61616' COMMENT '检测jms服务的端口号',
  `jms_agpt_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'jms接收安管平台数据运行日志的IP地址',
  `jms_sfrz_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'jms接收身份认证平台数据运行日志的IP地址',
  `jms_ptgl_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'jms接收配置管理平台数据运行日志的IP地址',
  `db_delete_interval` int(11) NOT NULL DEFAULT 3 COMMENT '检测dbrouter的删除周期单位天',
  `db_online_interval` int(11) NOT NULL DEFAULT 120 COMMENT '检测dbrouter的online周期单位秒',
  `alarm_ping_interval` int(11) NOT NULL DEFAULT 30 COMMENT '检测alarmservice的ping周期单位秒',
  `reserve1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '预留字段1',
  `reserve2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '预留字段2',
  `reserve3` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '预留字段3',
  `reserve4` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '预留字段4',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_config
-- ----------------------------
INSERT INTO `nms_config` VALUES (1, 1, 5, '127.0.0.1', '61616', '127.0.0.1', '127.0.0.1', '127.0.0.1', 3, 120, 30, '', '', '', '', '2021-02-02 04:00:00');

-- ----------------------------
-- Table structure for nms_cpu_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_cpu_info`;
CREATE TABLE `nms_cpu_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `freq` int(11) NULL DEFAULT NULL COMMENT '采集次数标记, 对于多个cpu, 统一采集标记',
  `cpu_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cpu物理描述类型',
  `cpu_cores` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cpu物理核数量',
  `cpu_freq` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单个cpu频率',
  `cpu_rate` float NULL DEFAULT NULL COMMENT '单个cpu过去1秒钟实时利用率',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_cpuaid_assetid`(`asset_id`) USING BTREE,
  INDEX `nms_cpu_assetid_freq_info`(`asset_id`, `freq`) USING BTREE,
  INDEX `nms_cpu_freq_info`(`freq`) USING BTREE,
  CONSTRAINT `fk_cpuaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_database_basic
-- ----------------------------
DROP TABLE IF EXISTS `nms_database_basic`;
CREATE TABLE `nms_database_basic`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `soft_id` int(11) NULL DEFAULT NULL COMMENT 'asoft_id关联nms_soft表主键id',
  `name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `version` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库版本',
  `install_time` bigint(15) NULL DEFAULT NULL COMMENT '数据库安装时间戳，精确到秒',
  `process_name` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据库进程名称，多个之间用英文分号隔开',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_id_softid`(`soft_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_database_config
-- ----------------------------
DROP TABLE IF EXISTS `nms_database_config`;
CREATE TABLE `nms_database_config`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `soft_id` int(11) NULL DEFAULT NULL COMMENT 'soft_id关联nms_soft表主键id',
  `start_time` bigint(15) NULL DEFAULT NULL COMMENT '数据库启动时间戳，精确到秒',
  `total_size` bigint(15) NULL DEFAULT NULL COMMENT '数据库存储容量，单位KB',
  `instance_names` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据库实例名，多个实例之间用英文分号分割',
  `max_conn_num` int(11) NULL DEFAULT NULL COMMENT '数据库配置的最大连接数',
  `max_mem_size` int(11) NULL DEFAULT NULL COMMENT '数据库配置的最大内存，单位KB',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_id_softid`(`soft_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_database_sql
-- ----------------------------
DROP TABLE IF EXISTS `nms_database_sql`;
CREATE TABLE `nms_database_sql`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `status_id` bigint(11) NULL DEFAULT NULL COMMENT 'status_id关联nms_database_status表主键id',
  `slow_sql` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据库执行的慢SQL语句',
  `exec_time` bigint(15) NULL DEFAULT NULL COMMENT '慢SQL语句执行时间，单位毫秒',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_id_statusid`(`status_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 293 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_database_status
-- ----------------------------
DROP TABLE IF EXISTS `nms_database_status`;
CREATE TABLE `nms_database_status`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `soft_id` int(11) NULL DEFAULT NULL COMMENT 'soft_id关联nms_soft表主键id',
  `total_size` bigint(15) NULL DEFAULT NULL COMMENT '数据库实际总数据大小，单位KB',
  `mem_size` bigint(15) NULL DEFAULT NULL COMMENT '数据库实际使用内存，单位KB',
  `tps` int(11) NULL DEFAULT NULL COMMENT '总的事务量自启动以来，单位个',
  `io_busy` bigint(20) NULL DEFAULT NULL COMMENT '数据库自启动以来读取和写入字节之和，单位KB',
  `conn_num` int(11) NULL DEFAULT NULL COMMENT '当前连接数',
  `active_conn_num` int(11) NULL DEFAULT NULL COMMENT '数据库当前活跃连接数',
  `process_num` int(11) NULL DEFAULT NULL COMMENT '数据库当前运行进程或线程数',
  `dead_lock_num` int(11) NULL DEFAULT NULL COMMENT '数据库当前死锁数',
  `user_list` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库当前用户列表，多个用户之间用英文分号分割',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_id_softid`(`soft_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_database_storage
-- ----------------------------
DROP TABLE IF EXISTS `nms_database_storage`;
CREATE TABLE `nms_database_storage`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `status_id` bigint(11) NULL DEFAULT NULL COMMENT 'status_id关联nms_database_status表主键id',
  `path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库存储数据路径',
  `total_size` bigint(15) NULL DEFAULT NULL COMMENT '数据库总存储容量，单位KB',
  `used_size` bigint(15) NULL DEFAULT NULL COMMENT '数据库已经使用的存储容量，单位KB',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_id_statusid`(`status_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_department
-- ----------------------------
DROP TABLE IF EXISTS `nms_department`;
CREATE TABLE `nms_department`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门编号父ID',
  `d_name` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  `d_desc` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '部门描述',
  `deled` int(1) NULL DEFAULT 0 COMMENT '逻辑删除标记0表示正常,1表示已删除',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_department
-- ----------------------------
INSERT INTO `nms_department` VALUES ('1', '0', '未初始化单位', '', 0, '2020-10-29 05:24:59');
INSERT INTO `nms_department` VALUES ('4028e48376b776390176b7854c6b0000', '1', '未初始化部门', '', 0, '2020-12-31 01:38:55');

-- ----------------------------
-- Table structure for nms_diskio_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_diskio_info`;
CREATE TABLE `nms_diskio_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `freq` int(11) NULL DEFAULT NULL COMMENT '采集次数标记, 对于多个cpu, 统一采集标记',
  `disk_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '磁盘名称, sda',
  `disk_sn` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '磁盘序列号, 23134ABFD-SD',
  `read_num` float NULL DEFAULT NULL COMMENT '磁盘平均读操作速率, 8 read/s(保留2位小数)',
  `write_num` float NULL DEFAULT NULL COMMENT '磁盘平均写操作速率, 8 write/s(保留2位小数)',
  `kb_read` float NULL DEFAULT NULL COMMENT '磁盘平均每秒读字节数, 5.25kB/s(保留2位小数)',
  `kb_wrtn` float NULL DEFAULT NULL COMMENT '磁盘平均每秒写字节数, 5.25kB/s(保留2位小数)',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_diskaid_assetid`(`asset_id`) USING BTREE,
  INDEX `nms_diskio_assetid_freq_info`(`asset_id`, `freq`) USING BTREE,
  CONSTRAINT `fk_diskaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_dynamic_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_dynamic_info`;
CREATE TABLE `nms_dynamic_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `sys_uptime` bigint(20) NULL DEFAULT NULL COMMENT '操作系统自启动以来的时间戳, 现在时间戳减去该值是系统启动时间, 精确到秒',
  `sys_update_time` bigint(20) NULL DEFAULT NULL COMMENT '操作系统更新的时间戳, 该值就是更新的时间, 精确到秒',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_dynaid_assetid`(`asset_id`) USING BTREE,
  CONSTRAINT `fk_dynaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_filesys_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_filesys_info`;
CREATE TABLE `nms_filesys_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `freq` int(11) NULL DEFAULT NULL COMMENT '采集次数标记, 对于多个cpu, 统一采集标记',
  `file_sys` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件分区名称, /dev/sda1',
  `file_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件分区类型, ext4等',
  `part_total` float NULL DEFAULT NULL COMMENT '文件系统分区总大小, 500.25GB(保留2位小数), 单位GB',
  `part_free` float NULL DEFAULT NULL COMMENT '文件系统分区剩余大小, 250.50GB(保留2位小数), 单位GB',
  `part_inode_num` int(11) NULL DEFAULT NULL COMMENT '文件系统分区inode总数',
  `part_inode_free` int(11) NULL DEFAULT NULL COMMENT '文件系统分区inode剩余数量',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_filesysaid_assetid`(`asset_id`) USING BTREE,
  INDEX `nms_filesys_assetid_freq_info`(`asset_id`, `freq`) USING BTREE,
  CONSTRAINT `fk_filesysaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_function
-- ----------------------------
DROP TABLE IF EXISTS `nms_function`;
CREATE TABLE `nms_function`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `function_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `chinese_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示中文权限名称',
  `level_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限级别描述',
  `father_node` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限父节点',
  `fun_url` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `img_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留字段',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_function
-- ----------------------------
INSERT INTO `nms_function` VALUES (1, '0A', '平台首页', '1', '0', '/Department/all;/assetType/all;/AuditLog/add;/Admin/getuser;/Asset/typeAsset;/Admin/getAdminById;/Admin/updateUser;', '', '2021-07-01 18:20:39');
INSERT INTO `nms_function` VALUES (2, '0A', '网络拓扑', '1', '0', '/Topo/findByIdMap;/Topo/all;/AuditLog/add;/Topo/updateEntityNodeToTable;/Topo/refreshLink;/Topo/refreshImage;/Topo/updateEntityNodeToTable;/Topo/getShowNodeMessage;/Topo/getShowLinkMessage;/Topo/addNode;/Topo/addEntityLink;/Topo/addHinNode;/Topo/addHintLink;/Topo/Edittopomap;/Topo/saveSubMap;/Topo/saveRelationSubMap;/Topo/updateLinkWidth;/Topo/updateNodeAlias;/Topo/selectName;/Topo/all;/Topo/deleteNode;/Topo/cancelRelationMap;/Topo/deleteLink;/Topo/createHintMeta;/Topo/readyAddNodes;/Topo/readyAddHintLink;/Topo/readyAddEntityLink;/Topo/deleteSubMap;/Topo/send;/Topo/accept;/Topo/readyTopoArea;/Topo/editTopoArea;/Topo/topoAreas;/Topo/updateTopoArea;/Topo/deleteTopoArea;/Topo/updateNodeBinding;/Topo/addTopoArea', '', '2021-03-03 14:35:36');
INSERT INTO `nms_function` VALUES (5, '0A', '资产管理', '1', '0', '/Asset/menu;/Asset/list/date/condition;/Asset/loadLastAsset;/Asset/addAsset;/Asset/list/delete;/Asset/findByIdToUpdate;/Asset/updateAsset;/Asset/ifIp;/AuditLog/add;/deviceimport/input;/template/importExcel;/Soft/list/date/condition;/Soft/list/delete;/Soft/loadLastSoft;/Soft/addSoft;/Soft/findByIdToUpdate;/Soft/updateSoft;/Soft/ifIpOrPort;/softimport/input', '', '2020-10-21 02:25:03');
INSERT INTO `nms_function` VALUES (6, '0A', '性能管理', '1', '0', '/Asset/typeAssetAlarmDepartment;/Asset/findById;/Asset/SystermInfo/Overview;/CpuInfo/ServerDetail/PerformanceInfoV02;/MemInfo/ServerDetail/MemInfoV02;/PingInfo/ServerDetail/PerformanceInfoV02;/FilesysInfo/ServerDetail/DiskUtilizationV02;/NetifInfo/ServerDetail/NetifInfo;/ProcessInfo/list/date;/AuditLog/add;/MysqlInfo/getMysqlById;/TomcatInfo/getTomcatById;/ProcessInfo/ServerDetail/ProcessInfo;/SoftInfo/ServerDetail/SoftInfo;/AccountInfo/ServerDetail/AccountInfo;/DatabaseBasic/Overview;/MiddlewareBasic/Overview;/AppInfo/ServerDetail/AppInfo;/AppAccountInfo/list/date;/performance/soft/list/page/condition;/alarm/soft/list/page/condition;/alarm/soft/deleteAlarmById;/alarm/soft/findByAlarmId;/alarm/soft/deal/report;/alarm/soft/findById;/alarm/soft/deal/update', '', '2020-10-21 01:29:05');
INSERT INTO `nms_function` VALUES (7, '0A', '告警管理', '1', '0', '/Asset/findById;/alarm/list/page/condition;/Asset/typeAssetAlarm;/alarm/list/page/condition;/alarm/deleteAlarmById;/alarm/findById;/alarm/findByAlarmId;/alarm/deal/report;/alarm/deal/update;/AuditLog/add;/alarm/soft/list/page/condition;/alarm/list/alarm/soft/condition/exportExcel;/alarm/soft/deleteAlarmById;/alarm/soft/findByAlarmId;/alarm/soft/deal/report;/alarm/soft/findById;/alarm/soft/deal/update;/RuleSoft/findNmsRuleSoft;/RuleSoft/updateNmsRuleSoftById;/RuleSoft/findNmsRuleSoftById;/Rule/updateNmsRuleMonitorById;/RuleAsset/updateNmsRuleAssetMonitorById', '', '2021-01-20 20:56:53');
INSERT INTO `nms_function` VALUES (8, '0A', '审计日志', '1', '0', '/AuditLog/list/find;/AuditLog/add;/AuditLog/exportExcel;/AuditConfig/all;/AuditConfig/loadinfo;/AuditConfig/loadpart;/AuditConfig/update;/AuditConfig/alarm;/AuditLog/importExcel', '', '2019-12-03 21:23:12');
INSERT INTO `nms_function` VALUES (9, '0A', '报表管理', '1', '0', '/Asset/list/date/reportSelect;/Asset/list/date/reportSelect/exportExcel;/alarm/list/statics/asset/condition;/alarm/list/page/condition/exportExcel;/performance/list/page/condition/realtime;/performance/list/page/condition/realtime/ExportExcel;/Asset/findById;/CpuInfo/list/date;/CpuInfo/list/date/ExportExcel;/DiskioInfo/list/date;/DiskioInfo/list/date/ExportExcel;/DynamicInfo/list/date;/DynamicInfo/list/date/ExportExcel;/FilesysInfo/list/date;/FilesysInfo/list/date/ExportExcel;/MemInfo/list/date;/MemInfo/list/date/ExportExcel;/NetifInfo/list/date;/NetifInfo/list/date/ExportExcel;/NetifInfo/detail;/PingInfo/list/date;/PingInfo/list/date/ExportExcel;/ProcessInfo/list/date;/ProcessInfo/list/date/ExportExcel;/StaticInfo/list/date;/StaticInfo/list/date/ExportExcel;/StaticInfo/detail;/AuditLog/add;/MysqlInfo/list/date;/MysqlInfo/list/date/ExportExcel;/TomcatInfo/list/date;/TomcatInfo/list/date/ExportExcel;/alarm/list/alarm/condition/exportExcel;/alarm/list/statics/asset/condition/exportExcel;/DatabaseBasic/list/date;/MiddlewareBasic/list/date;/DatabaseConfig/list/date;/MiddlewareConfig/list/date;/MiddlewareStatus/list/date;/DatabaseStatus/list/date;/MiddlewareInstance/list/date;/DatabaseSql/list/date;/DatabaseStorage/list/date;/MiddlewareBasic/list/date/ExportExcel;/MiddlewareConfig/list/date/ExportExcel;/MiddlewareStatus/list/date/ExportExcel;/MiddlewareInstance/list/date/ExportExcel;/DatabaseBasic/list/date/ExportExcel;/DatabaseConfig/list/date/ExportExcel;/DatabaseStatus/list/date/ExportExcel;/DatabaseSql/list/date/ExportExcel;/DatabaseStorage/list/date/ExportExcel;/SostInfo/list/date;/SoftInfo/list/date/ExportExcel;/AccountInfo/list/date;/AccountInfo/list/date/ExportExcel;/AppInfo/list/date;/AppInfo/list/date/ExportExcel;/AppAccountInfo/list/date;/Soft/list/date/reportSelect;/Soft/list/date/reportSelect/exportExcel;/Soft/findById;/alarm/list/statics/soft/condition;/alarm/list/statics/soft/condition/exportExcel;/alarm/list/page/soft/condition/exportExcel', '', '2020-10-17 14:43:10');
INSERT INTO `nms_function` VALUES (10, '0A', '用户管理', '1', '0', '/Admin/all;/Admin/list/deleteUser;/Admin/ifUser;/Admin/add;/Admin/getAdminById;/Admin/updateUser;/AuditLog/add;/License/Licensecreate;/License/Licenseregister;/License/Licenseinfo;/Admin/list/page/condition;/role/all;/User/list/page/condition;/User/deleteUser;/User/ifUser;/User/addUser;/User/getUserById;/User/updateUser;/Department/tree;/Department/add;/Department/update;/Department/delete;/Department/findById', '', '2020-10-31 11:30:17');
INSERT INTO `nms_function` VALUES (11, '0A', '权限管理', '1', '0', '/NmsRoleFunction/loadRoleList;/NmsFunction/all;/role/add;/NmsRoleFunction/addRoleFunctions;/NmsRoleFunction/findByRoleId;/role/getRoleById;/role/updateRole;/NmsRoleFunction/updateRoleFunction;/AuditLog/add;/NmsRoleFunction/deleteRoleFunction', '', '2020-07-31 04:27:29');
INSERT INTO `nms_function` VALUES (13, '0A', '安全管理', '1', '0', '/SecRule/all;/SecRule/updateRule;/SecRule/findByIdToUpdate;/Config/getConfigById;/Config/updateConfig/Config/syncConfig', '', '2021-07-04 20:22:19');
INSERT INTO `nms_function` VALUES (14, '0A', '阈值策略', '1', '0', '/Rule/findNmsRules;/Rule/findNmsRuleById;/Rule/updateNmsRuleById;/RuleAsset/findNmsRuleAsset;/RuleAsset/findNmsRuleAssetById;/RuleAsset/updateNmsRuleAssetById;/AuditLog/add', '', '2019-09-20 23:24:53');
-- ----------------------------
-- Table structure for nms_license
-- ----------------------------
DROP TABLE IF EXISTS `nms_license`;
CREATE TABLE `nms_license`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `license` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '加密后的证书序列号',
  `regtime` bigint(20) NOT NULL DEFAULT 0 COMMENT '证书注册时间戳,管理员成功导入证书时间戳',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_license
-- ----------------------------
INSERT INTO `nms_license` VALUES (68, '74cBsQMFvINLz6I6A12K488K1jE+byoOy3KX+Kqc39XgBYS4/UuOTw==', 1585722037712, '2020-04-06 22:51:50');
INSERT INTO `nms_license` VALUES (69, '+Y0fOmqb9/rtAVHJL0sU+r+e317K52RNk9vn978han2GkvCEcBBCXA==', 1587436431450, '2020-04-20 22:33:56');

-- ----------------------------
-- Table structure for nms_mem_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_mem_info`;
CREATE TABLE `nms_mem_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `mem_total` bigint(20) NULL DEFAULT NULL COMMENT '内存总大小, 单位kB',
  `mem_free` bigint(20) NULL DEFAULT NULL COMMENT '对操作系统内存可利用大小, 单位kB',
  `mem_available` bigint(20) NULL DEFAULT NULL COMMENT '对应用程序可利用的内存大小, 单位kB',
  `buffers` bigint(20) NULL DEFAULT NULL COMMENT 'buffer大小, 单位kB',
  `cached` bigint(20) NULL DEFAULT NULL COMMENT 'cache大小, 单位kB',
  `swap_total` bigint(20) NULL DEFAULT NULL COMMENT '交换内存总大小, 单位kB',
  `swap_free` bigint(20) NULL DEFAULT NULL COMMENT '交换内存剩余大小, 单位kB',
  `swap_cached` bigint(20) NULL DEFAULT NULL COMMENT '交换内存cache大小, 单位kB',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_memaid_assetid`(`asset_id`) USING BTREE,
  CONSTRAINT `fk_memaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_middleware_basic
-- ----------------------------
DROP TABLE IF EXISTS `nms_middleware_basic`;
CREATE TABLE `nms_middleware_basic`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `soft_id` int(11) NULL DEFAULT NULL COMMENT 'soft_id关联nms_soft表主键id',
  `name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中间件名称',
  `version` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中间件版本',
  `jdk_version` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'JDK版本',
  `install_time` bigint(15) NULL DEFAULT NULL COMMENT '中间件安装时间戳，精确到秒',
  `process_name` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '中间件进程名称，多个之间用英文分号隔开',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_id_softid`(`soft_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_middleware_config
-- ----------------------------
DROP TABLE IF EXISTS `nms_middleware_config`;
CREATE TABLE `nms_middleware_config`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `soft_id` int(11) NULL DEFAULT NULL COMMENT 'soft_id关联nms_soft表主键id',
  `start_time` bigint(15) NULL DEFAULT NULL COMMENT '中间件启动时间戳，精确到秒',
  `https_protocol` int(2) NOT NULL DEFAULT 0 COMMENT '是否启用HTTPS协议，0表示未启用, 1表示已启用',
  `protocols` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中间件支持的证书加密协议, 多个协议之间用应为分号分开',
  `listen_ports` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中间件监听使用的端口号, 多个端口之间用英文分号分开',
  `max_conn_num` int(11) NULL DEFAULT NULL COMMENT '中间件配置的最大连接数',
  `max_hread_num` int(11) NULL DEFAULT NULL COMMENT '中间件配置的最大JVM线程数',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_id_softid`(`soft_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_middleware_instance
-- ----------------------------
DROP TABLE IF EXISTS `nms_middleware_instance`;
CREATE TABLE `nms_middleware_instance`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `config_id` int(11) NULL DEFAULT NULL COMMENT 'config_id关联nms_middleware_config表主键id',
  `name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例名称',
  `ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例使用的IP地址',
  `domain` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例域名',
  `listen_ports` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例使用的端口号, 多个端口之间用英文分号分开',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_id_configid`(`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_middleware_status
-- ----------------------------
DROP TABLE IF EXISTS `nms_middleware_status`;
CREATE TABLE `nms_middleware_status`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `soft_id` int(11) NULL DEFAULT NULL COMMENT 'soft_id关联nms_soft表主键id',
  `conn_num` int(11) NULL DEFAULT NULL COMMENT '中间件当前实时连接数',
  `rps` int(11) NULL DEFAULT NULL COMMENT '中间件每秒请求数',
  `mem_total` bigint(15) NULL DEFAULT NULL COMMENT '中间件最大内存总大小（堆内存+非堆内存）, 单位KB',
  `mem_used` bigint(15) NULL DEFAULT NULL COMMENT '中间件已用的内存大小（堆内存+非堆内存）, 单位KB',
  `heap_total` bigint(15) NULL DEFAULT NULL COMMENT '中间件总的堆内存大小, 单位KB',
  `heap_used` bigint(15) NULL DEFAULT NULL COMMENT '中间件已用的堆内存大小, 单位KB',
  `nonheap_total` bigint(15) NULL DEFAULT NULL COMMENT '中间件总的非堆内存大小, 单位KB',
  `nonheap_used` bigint(15) NULL DEFAULT NULL COMMENT '中间件已用的非堆内存大小, 单位KB',
  `jvm_thread_num` int(11) NULL DEFAULT NULL COMMENT '中间件当前JVM线程数',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_id_softid`(`soft_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_mysql_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_mysql_info`;
CREATE TABLE `nms_mysql_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT 0 COMMENT 'asset_id关联nms_asset表主键id',
  `db_version` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库版本',
  `max_connections` int(11) NULL DEFAULT 0 COMMENT '最大连接数',
  `threads_connected` int(11) NULL DEFAULT 0 COMMENT '当前连接数',
  `threads_running` int(11) NULL DEFAULT 0 COMMENT '正在处理的连接数',
  `db_read_only` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库是否可读写OFF, ON',
  `qps` int(11) NULL DEFAULT 0 COMMENT '每秒查询数量',
  `tps` int(11) NULL DEFAULT 0 COMMENT '每秒事务数量',
  `aborted_clients` int(11) NULL DEFAULT 0 COMMENT '客户端被异常终端数',
  `questions` int(11) NULL DEFAULT 0 COMMENT '每秒钟获得的查询数量',
  `processlist` int(11) NULL DEFAULT 0 COMMENT '客户端连接的进程数',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `asset`(`asset_id`) USING BTREE,
  CONSTRAINT `asset` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_netif_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_netif_info`;
CREATE TABLE `nms_netif_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `freq` int(11) NULL DEFAULT NULL COMMENT '采集次数标记, 对于多个cpu, 统一采集标记',
  `if_index` int(11) NULL DEFAULT NULL COMMENT '网络接口索引值, 能够唯一区别多个接口的标志',
  `if_descr` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网络接口描述, 例如lo, eth0, eth1',
  `if_type` int(11) NULL DEFAULT NULL COMMENT '网络接口类型, 24表示lo, 6表示以太网等, 按照通用标准定义',
  `if_mtu` int(11) NULL DEFAULT NULL COMMENT '网络接口传输数据包最大字节数, 例1500',
  `if_speed` bigint(20) NULL DEFAULT NULL COMMENT '网络接口最大传输速率, 单位是Mb/s, 例1000Mb/s',
  `if_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网络接口对应的ip地址, 例192.168.100.100, 如无则空',
  `if_submask` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网络接口对应的子网掩码, 例255.255.255.0, 如无则空',
  `if_gateway` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网络接口对应的网关地址, 例192.168.100.254, 如无则空',
  `if_physaddr` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网络接口物理地址mac, 例44:a8:42:ff:6a:e9, 如没有则空格式要求小写用:分开',
  `if_admin_status` int(11) NULL DEFAULT NULL COMMENT '网络接口配置状态, 1表示up, 2表示dowm, 3表示其它所有状态',
  `if_oper_status` int(11) NULL DEFAULT NULL COMMENT '网络接口运行状态, 1表示up, 2表示dowm, 3表示其它所有状态',
  `if_in_octets` bigint(20) NULL DEFAULT NULL COMMENT '网络接口接收到的字节数, 单位byte, 此处由于会超过long型, 用16位字符串代替',
  `if_in_ucastpkts` bigint(20) NULL DEFAULT NULL COMMENT '网络接口接收到的单播包数',
  `if_in_nucastpkts` bigint(20) NULL DEFAULT NULL COMMENT '网络接口接收到的广播包数',
  `if_in_discards` bigint(20) NULL DEFAULT NULL COMMENT '网络接口由于资源紧张丢弃的接收包的个数',
  `if_in_errors` bigint(20) NULL DEFAULT NULL COMMENT '网络接口由于出错导致丢弃的接收包的个数',
  `if_out_octets` bigint(20) NULL DEFAULT NULL COMMENT '网络接口发送的字节数, 单位byte, 此处由于会超过long型, 用16位字符串代替',
  `if_out_ucastpkts` bigint(20) NULL DEFAULT NULL COMMENT '网络接口发送的单播包数',
  `if_out_nucastpkts` bigint(20) NULL DEFAULT NULL COMMENT '网络接口发送的广播包数',
  `if_out_discards` bigint(20) NULL DEFAULT NULL COMMENT '网络接口由于资源紧张丢弃的发送包的个数',
  `if_out_errors` bigint(20) NULL DEFAULT NULL COMMENT '网络接口由于出错导致丢弃的发送包的个数',
  `if_in_icmps` bigint(20) NULL DEFAULT NULL COMMENT '网络接口接收icmp的报文总数',
  `if_out_icmps` bigint(20) NULL DEFAULT NULL COMMENT '网络接口发送icmp的报文总数',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `nms_netif_assetid_freq_info`(`asset_id`, `freq`) USING BTREE,
  CONSTRAINT `fk_netifaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_online_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_online_info`;
CREATE TABLE `nms_online_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `freq` bigint(20) NULL DEFAULT NULL COMMENT '时间戳精确到秒,每次采集过来的时间戳,如果有数据更新没有则修改',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_memaid_assetid`(`asset_id`) USING BTREE,
  CONSTRAINT `fk_onlineaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_ping_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_ping_info`;
CREATE TABLE `nms_ping_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `ping_rate` float NULL DEFAULT NULL COMMENT '设备可达率',
  `ping_rtt` int(11) NULL DEFAULT NULL COMMENT '响应时间',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_pingid_assetid`(`asset_id`) USING BTREE,
  CONSTRAINT `fk_pingid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_process_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_process_info`;
CREATE TABLE `nms_process_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `freq` bigint(20) NULL DEFAULT NULL COMMENT '采集次数标记, 对于多个cpu, 统一采集标记',
  `proc_id` bigint(20) NULL DEFAULT NULL COMMENT '进程id',
  `proc_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进程名称',
  `proc_path` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进程路径',
  `proc_state` int(4) NULL DEFAULT NULL COMMENT '进程状态,0表示running,表示stopped,2表示sleeping,3表示zombie',
  `proc_cpu` float NULL DEFAULT NULL COMMENT '进程cpu实时占用率, 保留2位小数 0 ~ 100区间, 88.88表示88.88%',
  `proc_mem` float NULL DEFAULT NULL COMMENT '进程mem实时占用率, 保留2位小数 0 ~ 100区间, 88.88表示88.88%',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_procaid_assetid`(`asset_id`) USING BTREE,
  INDEX `nms_process_assetid_freq_info`(`asset_id`, `freq`) USING BTREE,
  CONSTRAINT `fk_procaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_role
-- ----------------------------
DROP TABLE IF EXISTS `nms_role`;
CREATE TABLE `nms_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `role` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `deled` int(1) NULL DEFAULT 0 COMMENT '逻辑删除标记0未删除,1已删除',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_role
-- ----------------------------
INSERT INTO `nms_role` VALUES (1, '系统管理员', 0, '2021-07-01 11:08:37');
INSERT INTO `nms_role` VALUES (2, '安全保密员', 0, '2021-07-01 11:08:34');
INSERT INTO `nms_role` VALUES (3, '审计管理员', 0, '2021-07-01 11:08:33');
INSERT INTO `nms_role` VALUES (4, '网络系统管理员', 1, '2021-07-01 15:13:51');

-- ----------------------------
-- Table structure for nms_role_function
-- ----------------------------
DROP TABLE IF EXISTS `nms_role_function`;
CREATE TABLE `nms_role_function`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '关联nms_role表的id',
  `function_id` int(11) NULL DEFAULT NULL COMMENT '关联nms_function表的id',
  `deled` int(1) NULL DEFAULT 0 COMMENT '逻辑删除标记0未删除,1已删除',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_rfrid_roleid`(`role_id`) USING BTREE,
  INDEX `fk_rffid_funcid`(`function_id`) USING BTREE,
  CONSTRAINT `fk_rffid_funcid` FOREIGN KEY (`function_id`) REFERENCES `nms_function` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_rfrid_roleid` FOREIGN KEY (`role_id`) REFERENCES `nms_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 156 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_role_function
-- ----------------------------
INSERT INTO `nms_role_function` VALUES (1, 1, 1, 0, '2019-08-08 04:00:00');
INSERT INTO `nms_role_function` VALUES (2, 2, 1, 0, '2019-08-08 04:00:00');
INSERT INTO `nms_role_function` VALUES (3, 3, 1, 0, '2019-08-08 04:00:00');
INSERT INTO `nms_role_function` VALUES (4, 1, 2, 0, '2019-08-08 18:43:51');
INSERT INTO `nms_role_function` VALUES (7, 1, 5, 0, '2019-08-08 18:43:51');
INSERT INTO `nms_role_function` VALUES (8, 1, 6, 1, '2019-08-08 18:43:51');
INSERT INTO `nms_role_function` VALUES (9, 1, 7, 0, '2019-08-08 18:43:51');
INSERT INTO `nms_role_function` VALUES (11, 1, 9, 0, '2019-08-08 18:43:51');
INSERT INTO `nms_role_function` VALUES (12, 1, 10, 0, '2019-08-08 18:43:51');
INSERT INTO `nms_role_function` VALUES (15, 2, 11, 1, '2019-08-13 04:29:03');
INSERT INTO `nms_role_function` VALUES (67, 3, 8, 0, '2019-08-13 05:48:16');
INSERT INTO `nms_role_function` VALUES (80, 2, 13, 1, '2019-09-20 06:57:02');
INSERT INTO `nms_role_function` VALUES (81, 2, 8, 0, '2019-09-20 06:57:13');
INSERT INTO `nms_role_function` VALUES (90, 2, 14, 0, '2019-09-21 07:25:51');
INSERT INTO `nms_role_function` VALUES (115, 1, 11, 0, '2020-10-11 11:57:32');
INSERT INTO `nms_role_function` VALUES (118, 2, 10, 1, '2020-10-11 11:57:46');
INSERT INTO `nms_role_function` VALUES (135, 4, 1, 0, '2020-11-03 05:40:52');
INSERT INTO `nms_role_function` VALUES (136, 4, 2, 0, '2020-11-03 05:40:52');
INSERT INTO `nms_role_function` VALUES (139, 4, 5, 0, '2020-11-03 05:40:52');
INSERT INTO `nms_role_function` VALUES (140, 4, 6, 0, '2020-11-03 05:40:52');
INSERT INTO `nms_role_function` VALUES (141, 4, 7, 0, '2020-11-03 05:40:52');
INSERT INTO `nms_role_function` VALUES (142, 4, 8, 0, '2020-11-03 05:40:52');
INSERT INTO `nms_role_function` VALUES (143, 4, 9, 0, '2020-11-03 05:40:52');
INSERT INTO `nms_role_function` VALUES (144, 4, 10, 0, '2020-11-03 05:40:52');
INSERT INTO `nms_role_function` VALUES (145, 4, 11, 0, '2020-11-03 05:40:52');
INSERT INTO `nms_role_function` VALUES (147, 4, 13, 0, '2020-11-03 05:40:52');
INSERT INTO `nms_role_function` VALUES (148, 4, 14, 0, '2020-11-03 05:40:52');
INSERT INTO `nms_role_function` VALUES (150, 1, 13, 1, '2021-07-01 15:13:35');
INSERT INTO `nms_role_function` VALUES (151, 1, 14, 1, '2021-07-01 15:13:35');
INSERT INTO `nms_role_function` VALUES (153, 1, 8, 1, '2021-07-01 15:22:56');
INSERT INTO `nms_role_function` VALUES (154, 3, 5, 1, '2021-07-01 18:39:26');
INSERT INTO `nms_role_function` VALUES (155, 3, 10, 1, '2021-07-01 18:39:26');
INSERT INTO `nms_role_function` VALUES (156, 1, 6, 0, '2021-07-02 15:10:04');
INSERT INTO `nms_role_function` VALUES (157, 2, 13, 0, '2021-07-04 20:25:32');

-- ----------------------------
-- Table structure for nms_room_page
-- ----------------------------
DROP TABLE IF EXISTS `nms_room_page`;
CREATE TABLE `nms_room_page`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `room_id` int(20) NULL DEFAULT NULL COMMENT '机房id',
  `web_json` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端配置json 废弃',
  `room_desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机房描述',
  `room_location` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机房位置',
  `room_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机房编码',
  `is_default` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '首页默认机房',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_rule
-- ----------------------------
DROP TABLE IF EXISTS `nms_rule`;
CREATE TABLE `nms_rule`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `atype_id` int(11) NULL DEFAULT NULL COMMENT '资产类别id, 与nms_asset_type表id相关联',
  `d_type` int(3) NULL DEFAULT NULL COMMENT '0:number, 1:boolean, 2:string',
  `r_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称, 定义规则指标的告警名称',
  `r_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警内容, cpu利用率超过阈值等',
  `r_unit` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阀值单位, %, kb等',
  `r_seq` int(3) NULL DEFAULT NULL COMMENT '阀值比较顺序, 0按降序比较, 1按生序比较',
  `r_enable` int(2) NOT NULL DEFAULT 1 COMMENT '告警使能状态, 0表示不告警, 1表示告警',
  `r_value1` bigint(20) NULL DEFAULT NULL COMMENT '对number类型, 1级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `r_value2` bigint(20) NULL DEFAULT NULL COMMENT '对number类型, 2级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `r_value3` bigint(20) NULL DEFAULT NULL COMMENT '对number类型, 3级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '规则添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_asset_type`(`atype_id`) USING BTREE,
  CONSTRAINT `fk_asset_type` FOREIGN KEY (`atype_id`) REFERENCES `nms_asset_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 405 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_rule
-- ----------------------------
INSERT INTO `nms_rule` VALUES (1, 0, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2020-01-13 15:32:16');
INSERT INTO `nms_rule` VALUES (2, 0, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (3, 1, 0, 'cpu_rate', 'CPU利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:24:46');
INSERT INTO `nms_rule` VALUES (4, 1, 0, 'mem_rate', '物理内存利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:25:51');
INSERT INTO `nms_rule` VALUES (5, 1, 0, 'swap_rate', '交换内存利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:26:07');
INSERT INTO `nms_rule` VALUES (6, 1, 0, 'filesys_rate', '文件系统分区利用率超过阀值', '%', 1, 1, 85, 90, 95, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (7, 1, 0, 'if_speed', '实时流速超过阀值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (8, 1, 0, 'if_discard', '实时丢弃包数超过阀值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (9, 1, 0, 'if_error', '实时出错包数超过阀值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (10, 1, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (11, 1, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (12, 1, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (13, 1, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (14, 1, 2, 'disk_sn', '硬盘序列号发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (15, 1, 2, 'sys_version', '操作系统版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (16, 1, 2, 'core_version', '操作系统内核版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (17, 1, 2, 'soc_version', 'SOC安全卡版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (18, 1, 2, 'io_version', '三合一版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (19, 2, 0, 'cpu_rate', 'CPU利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:24:46');
INSERT INTO `nms_rule` VALUES (20, 2, 0, 'mem_rate', '物理内存利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:25:51');
INSERT INTO `nms_rule` VALUES (21, 2, 0, 'swap_rate', '交换内存利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:26:07');
INSERT INTO `nms_rule` VALUES (22, 2, 0, 'filesys_rate', '文件系统分区利用率超过阀值', '%', 1, 1, 85, 90, 95, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (23, 2, 0, 'if_speed', '实时流速超过阀值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (24, 2, 0, 'if_discard', '实时丢弃包数超过阀值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (25, 2, 0, 'if_error', '实时出错包数超过阀值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (26, 2, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (27, 2, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (28, 2, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (29, 2, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (30, 2, 2, 'disk_sn', '硬盘序列号发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (31, 2, 2, 'sys_version', '操作系统版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (32, 2, 2, 'core_version', '操作系统内核版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (33, 2, 2, 'soc_version', 'SOC安全卡版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (34, 2, 2, 'io_version', '三合一版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (35, 3, 0, 'cpu_rate', 'CPU利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:24:46');
INSERT INTO `nms_rule` VALUES (36, 3, 0, 'mem_rate', '物理内存利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:25:51');
INSERT INTO `nms_rule` VALUES (37, 3, 0, 'swap_rate', '交换内存利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:26:07');
INSERT INTO `nms_rule` VALUES (38, 3, 0, 'filesys_rate', '文件系统分区利用率超过阀值', '%', 1, 1, 85, 90, 95, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (39, 3, 0, 'if_speed', '实时流速超过阀值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (40, 3, 0, 'if_discard', '实时丢弃包数超过阀值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (41, 3, 0, 'if_error', '实时出错包数超过阀值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (42, 3, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (43, 3, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (44, 3, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (45, 3, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (46, 3, 2, 'disk_sn', '硬盘序列号发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (47, 3, 2, 'sys_version', '操作系统版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (48, 3, 2, 'core_version', '操作系统内核版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (49, 3, 2, 'soc_version', 'SOC安全卡版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (50, 3, 2, 'io_version', '三合一版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (51, 4, 0, 'cpu_rate', 'CPU利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:24:46');
INSERT INTO `nms_rule` VALUES (52, 4, 0, 'mem_rate', '物理内存利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:25:51');
INSERT INTO `nms_rule` VALUES (53, 4, 0, 'swap_rate', '交换内存利用率超过阀值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:26:07');
INSERT INTO `nms_rule` VALUES (54, 4, 0, 'filesys_rate', '文件系统分区利用率超过阀值', '%', 1, 1, 85, 90, 95, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (55, 4, 0, 'if_speed', '实时流速超过阀值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (56, 4, 0, 'if_discard', '实时丢弃包数超过阀值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (57, 4, 0, 'if_error', '实时出错包数超过阀值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (58, 4, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (59, 4, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (60, 4, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (61, 4, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (62, 4, 2, 'disk_sn', '硬盘序列号发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (63, 4, 2, 'sys_version', '操作系统版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (64, 4, 2, 'core_version', '操作系统内核版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (65, 4, 2, 'soc_version', 'SOC安全卡版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (66, 4, 2, 'io_version', '三合一版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (67, 5, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2020-01-13 15:32:16');
INSERT INTO `nms_rule` VALUES (68, 5, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (69, 5, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (70, 5, 0, 'mem_rate', '物理内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:24');
INSERT INTO `nms_rule` VALUES (71, 5, 0, 'swap_rate', '交换内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:35');
INSERT INTO `nms_rule` VALUES (72, 5, 0, 'filesys_rate', '文件系统分区利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:31:30');
INSERT INTO `nms_rule` VALUES (73, 5, 0, 'diskio_speed', '磁盘IO传输速率超过阈值', 'kB', 1, 1, 51200, 102400, 153600, '2020-11-20 03:29:53');
INSERT INTO `nms_rule` VALUES (74, 5, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (75, 5, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (76, 5, 0, 'if_error', '实时出错包数超过阈值', '个', 0, 1, 1000, 1500, 2000, '2020-11-20 03:30:35');
INSERT INTO `nms_rule` VALUES (77, 5, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (78, 5, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (79, 5, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (80, 5, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (81, 5, 0, 'process_num', '进程总数量超过阈值', '个', 1, 1, 500, 600, 700, '2020-11-20 03:28:47');
INSERT INTO `nms_rule` VALUES (82, 5, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (83, 5, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (84, 5, 2, 'disk_sn', '硬盘序列号发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (85, 5, 2, 'sys_version', '操作系统版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (86, 5, 2, 'core_version', '操作系统内核版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (87, 5, 2, 'soc_version', 'SOC安全卡版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (88, 5, 2, 'io_version', '三合一版本发生变更', '', 0, 1, 0, 1, 0, '2019-11-24 05:20:01');
INSERT INTO `nms_rule` VALUES (89, 6, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (90, 6, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (91, 6, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (92, 6, 0, 'mem_rate', '物理内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:24');
INSERT INTO `nms_rule` VALUES (93, 6, 0, 'swap_rate', '交换内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:35');
INSERT INTO `nms_rule` VALUES (94, 6, 0, 'filesys_rate', '文件系统分区利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:31:30');
INSERT INTO `nms_rule` VALUES (95, 6, 0, 'diskio_speed', '磁盘IO传输速率超过阈值', 'kB', 1, 1, 51200, 102400, 153600, '2020-11-20 03:29:53');
INSERT INTO `nms_rule` VALUES (96, 6, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (97, 6, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (98, 6, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (99, 6, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (100, 6, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (101, 6, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (102, 6, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (103, 6, 0, 'process_num', '进程总数量超过阈值', '个', 1, 1, 500, 600, 700, '2020-11-20 03:28:47');
INSERT INTO `nms_rule` VALUES (104, 6, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (105, 6, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (106, 6, 2, 'disk_sn', '硬盘序列号发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (107, 6, 2, 'sys_version', '操作系统版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (108, 6, 2, 'core_version', '操作系统内核版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (109, 6, 2, 'soc_version', 'SOC安全卡版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (110, 6, 2, 'io_version', '三合一版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (111, 7, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (112, 7, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (113, 7, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (114, 7, 0, 'mem_rate', '物理内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:24');
INSERT INTO `nms_rule` VALUES (115, 7, 0, 'swap_rate', '交换内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:35');
INSERT INTO `nms_rule` VALUES (116, 7, 0, 'filesys_rate', '文件系统分区利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:31:30');
INSERT INTO `nms_rule` VALUES (117, 7, 0, 'diskio_speed', '磁盘IO传输速率超过阈值', 'kB', 1, 1, 51200, 102400, 153600, '2020-11-20 03:29:53');
INSERT INTO `nms_rule` VALUES (118, 7, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (119, 7, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (120, 7, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (121, 7, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (122, 7, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (123, 7, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (124, 7, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (125, 7, 0, 'process_num', '进程总数量超过阈值', '个', 1, 1, 500, 600, 700, '2020-11-20 03:28:47');
INSERT INTO `nms_rule` VALUES (126, 7, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (127, 7, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (128, 7, 2, 'disk_sn', '硬盘序列号发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (129, 7, 2, 'sys_version', '操作系统版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (130, 7, 2, 'core_version', '操作系统内核版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (131, 7, 2, 'soc_version', 'SOC安全卡版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (132, 7, 2, 'io_version', '三合一版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (133, 8, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (134, 8, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (135, 8, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (136, 8, 0, 'mem_rate', '物理内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:24');
INSERT INTO `nms_rule` VALUES (137, 8, 0, 'swap_rate', '交换内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:35');
INSERT INTO `nms_rule` VALUES (138, 8, 0, 'filesys_rate', '文件系统分区利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:31:30');
INSERT INTO `nms_rule` VALUES (139, 8, 0, 'diskio_speed', '磁盘IO传输速率超过阈值', 'kB', 1, 1, 51200, 102400, 153600, '2020-11-20 03:29:53');
INSERT INTO `nms_rule` VALUES (140, 8, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (141, 8, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (142, 8, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (143, 8, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (144, 8, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (145, 8, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (146, 8, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (147, 8, 0, 'process_num', '进程总数量超过阈值', '个', 1, 1, 500, 600, 700, '2020-11-20 03:28:47');
INSERT INTO `nms_rule` VALUES (148, 8, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (149, 8, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (150, 8, 2, 'disk_sn', '硬盘序列号发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (151, 8, 2, 'sys_version', '操作系统版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (152, 8, 2, 'core_version', '操作系统内核版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (153, 8, 2, 'soc_version', 'SOC安全卡版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (154, 8, 2, 'io_version', '三合一版本发生变更', '', 0, 1, 0, 0, 0, '2019-07-22 07:21:12');
INSERT INTO `nms_rule` VALUES (155, 9, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:06:07');
INSERT INTO `nms_rule` VALUES (156, 9, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (157, 9, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (158, 9, 0, 'mem_rate', '物理内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:24');
INSERT INTO `nms_rule` VALUES (159, 9, 0, 'swap_rate', '交换内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:35');
INSERT INTO `nms_rule` VALUES (160, 9, 0, 'filesys_rate', '文件系统分区利用率超过阈值', '%', 1, 1, 85, 90, 95, '2019-11-10 13:37:21');
INSERT INTO `nms_rule` VALUES (161, 9, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (162, 9, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (163, 9, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (164, 9, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (165, 9, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (166, 9, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (167, 9, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (168, 9, 0, 'process_num', '进程总数量超过阈值', '个', 1, 1, 500, 600, 700, '2020-11-20 03:28:47');
INSERT INTO `nms_rule` VALUES (169, 9, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:06:07');
INSERT INTO `nms_rule` VALUES (170, 9, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:06:07');
INSERT INTO `nms_rule` VALUES (171, 10, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:07:18');
INSERT INTO `nms_rule` VALUES (172, 10, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (173, 10, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (174, 10, 0, 'mem_rate', '物理内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:24');
INSERT INTO `nms_rule` VALUES (175, 10, 0, 'swap_rate', '虚拟内存利用率超过阈值', '%', 1, 1, 50, 60, 70, '2019-11-10 13:41:38');
INSERT INTO `nms_rule` VALUES (176, 10, 0, 'filesys_rate', '文件系统分区利用率超过阈值', '%', 1, 1, 85, 90, 95, '2019-11-10 13:37:21');
INSERT INTO `nms_rule` VALUES (177, 10, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (178, 10, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (179, 10, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (180, 10, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (181, 10, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (182, 10, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (183, 10, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (184, 10, 0, 'process_num', '进程总数量超过阈值', '个', 1, 1, 500, 600, 700, '2020-11-20 03:28:47');
INSERT INTO `nms_rule` VALUES (185, 10, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:07:18');
INSERT INTO `nms_rule` VALUES (186, 10, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:07:18');
INSERT INTO `nms_rule` VALUES (187, 11, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (188, 11, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (189, 11, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (190, 11, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (191, 11, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (192, 11, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (193, 11, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (194, 11, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (195, 11, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (196, 11, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (197, 11, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (198, 11, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (199, 11, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (200, 12, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (201, 12, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (202, 12, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (203, 12, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (204, 12, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (205, 12, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (206, 12, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (207, 12, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (208, 12, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (209, 12, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (210, 12, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (211, 12, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (212, 12, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (213, 13, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (214, 13, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (215, 13, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (216, 13, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (217, 13, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (218, 13, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (219, 13, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (220, 13, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (221, 13, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (222, 13, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (223, 13, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (224, 13, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (225, 13, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (226, 14, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (227, 14, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (228, 14, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (229, 14, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (230, 14, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (231, 14, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (232, 14, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (233, 14, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (234, 14, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (235, 14, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (236, 14, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (237, 14, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (238, 14, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (239, 15, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (240, 15, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (241, 15, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (242, 15, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (243, 15, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (244, 15, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (245, 15, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (246, 15, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (247, 15, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (248, 15, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (249, 15, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (250, 15, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (251, 15, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (252, 16, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (253, 16, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (254, 16, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (255, 16, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (256, 16, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (257, 16, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (258, 16, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (259, 16, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (260, 16, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (261, 16, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (262, 16, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (263, 16, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (264, 16, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (265, 17, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (266, 17, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (267, 17, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (268, 17, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (269, 17, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (270, 17, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (271, 17, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (272, 17, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (273, 17, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (274, 17, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (275, 17, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (276, 17, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (277, 17, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (278, 18, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (279, 18, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (280, 18, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (281, 18, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (282, 18, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (283, 18, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (284, 18, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (285, 18, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (286, 18, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (287, 18, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (288, 18, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (289, 18, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (290, 18, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (291, 19, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (292, 19, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (293, 19, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (294, 19, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (295, 19, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (296, 19, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (297, 19, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (298, 19, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (299, 19, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (300, 19, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (301, 19, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (302, 19, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (303, 19, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (304, 20, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (305, 20, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (306, 20, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (307, 20, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (308, 20, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (309, 20, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (310, 20, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (311, 20, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (312, 20, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (313, 20, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (314, 20, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (315, 20, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (316, 20, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (317, 21, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (318, 21, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (319, 21, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (320, 21, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (321, 21, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (322, 21, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (323, 21, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (324, 21, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (325, 21, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (326, 21, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (327, 21, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (328, 21, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (329, 21, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (330, 22, 0, 'real_mem_size', '数据库实际内存使用超过阈值', 'KB', 1, 1, 1000000, 1500000, 2000000, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (331, 22, 0, 'cur_conn_num', '数据库当前连接数超过阈值', '个', 1, 1, 50, 100, 150, '2019-11-19 05:09:44');
INSERT INTO `nms_rule` VALUES (332, 22, 0, 'cur_active_conn_num', '数据库当前活跃连接数超过阈值', '个', 1, 1, 50, 100, 150, '2019-11-10 13:08:16');
INSERT INTO `nms_rule` VALUES (333, 22, 0, 'cur_process_num', '数据库当前运行进程或线程数超过阈值', '个', 1, 1, 5, 10, 15, '2019-11-19 08:59:26');
INSERT INTO `nms_rule` VALUES (334, 22, 0, 'cur_deadlock_num', '数据库死锁数超过阈值', '个', 1, 1, 10, 20, 30, '2019-11-19 08:59:22');
INSERT INTO `nms_rule` VALUES (335, 23, 0, 'cur_conn_num', '中间件当前连接数超过阈值', '个', 1, 1, 100, 200, 300, '2019-11-10 13:43:21');
INSERT INTO `nms_rule` VALUES (336, 23, 0, 'used_mem_size', '中间件已用内存超过阈值', 'KB', 1, 1, 512000, 1024000, 1536000, '2019-11-10 13:08:13');
INSERT INTO `nms_rule` VALUES (337, 23, 0, 'used_heap_mem_size', '中间件已用堆内存超过阈值', 'KB', 1, 1, 512000, 1024000, 1536000, '2019-11-10 13:08:13');
INSERT INTO `nms_rule` VALUES (338, 23, 0, 'used_noheap_mem_size', '中间件已用非堆内存超过阈值', 'KB', 1, 1, 512000, 1024000, 1536000, '2019-11-10 13:08:13');
INSERT INTO `nms_rule` VALUES (339, 23, 0, 'cur_jvm_thread_num', '中间件JVM线程数数超过阈值', '个', 1, 1, 50, 80, 100, '2019-11-19 08:59:22');
INSERT INTO `nms_rule` VALUES (340, 24, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (341, 24, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (342, 24, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (343, 24, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (344, 24, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (345, 24, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (346, 24, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (347, 24, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (348, 24, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (349, 24, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (350, 24, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (351, 24, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (352, 24, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (353, 25, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (354, 25, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (355, 25, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (356, 25, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (357, 25, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (358, 25, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (359, 25, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (360, 25, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (361, 25, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (362, 25, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (363, 25, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (364, 25, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (365, 25, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (366, 26, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (367, 26, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (368, 26, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (369, 26, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (370, 26, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (371, 26, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (372, 26, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (373, 26, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (374, 26, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (375, 26, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (376, 26, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (377, 26, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (378, 26, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (379, 27, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (380, 27, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (381, 27, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (382, 27, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (383, 27, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (384, 27, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (385, 27, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (386, 27, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (387, 27, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (388, 27, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (389, 27, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (390, 27, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (391, 27, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (392, 28, 0, 'ping_rate', '可用性低于阈值', '%', 0, 1, 60, 40, 20, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (393, 28, 0, 'ping_rtt', '网络响应时间超过阈值', 'μs', 1, 1, 4000, 6000, 8000, '2020-12-09 16:00:08');
INSERT INTO `nms_rule` VALUES (394, 28, 0, 'cpu_rate', 'CPU利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:32:13');
INSERT INTO `nms_rule` VALUES (395, 28, 0, 'mem_rate', '内存利用率超过阈值', '%', 1, 1, 85, 90, 95, '2020-11-20 03:33:49');
INSERT INTO `nms_rule` VALUES (396, 28, 0, 'if_speed', '实时流速超过阈值', 'kB/s', 1, 1, 12000, 15000, 18000, '2019-11-19 08:21:29');
INSERT INTO `nms_rule` VALUES (397, 28, 0, 'if_discard', '实时丢弃包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:39:32');
INSERT INTO `nms_rule` VALUES (398, 28, 0, 'if_error', '实时出错包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-11-10 13:40:15');
INSERT INTO `nms_rule` VALUES (399, 28, 0, 'if_in_discard', '实时因资源紧张丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (400, 28, 0, 'if_in_error', '实时因出错丢弃接收包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (401, 28, 0, 'if_out_discard', '实时因资源紧张丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (402, 28, 0, 'if_out_error', '实时因出错丢弃发送包数超过阈值', '个', 1, 1, 1000, 1500, 2000, '2019-07-21 23:21:12');
INSERT INTO `nms_rule` VALUES (403, 28, 2, 'admin_status', '接口配置状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');
INSERT INTO `nms_rule` VALUES (404, 28, 2, 'oper_status', '接口运行状态发生变更', '', 0, 1, 0, 0, 0, '2019-08-05 13:08:44');

-- ----------------------------
-- Table structure for nms_rule_asset
-- ----------------------------
DROP TABLE IF EXISTS `nms_rule_asset`;
CREATE TABLE `nms_rule_asset`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT '资产id, 与nms_asset表id相关联',
  `atype_id` int(11) NULL DEFAULT NULL COMMENT '资产类别, 与nms_asset_type表id相关联',
  `d_type` int(3) NULL DEFAULT NULL COMMENT '0:number, 1:boolean, 2:string',
  `r_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称, 定义规则指标的告警名称',
  `r_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警内容, cpu利用率超过阈值等',
  `r_unit` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阀值单位, %, kb等',
  `r_seq` int(3) NULL DEFAULT NULL COMMENT '阀值比较顺序, 0按降序比较, 1按生序比较',
  `r_enable` int(2) NOT NULL DEFAULT 1 COMMENT '告警使能状态, 0表示不告警, 1表示告警',
  `r_value1` int(11) NULL DEFAULT NULL COMMENT '对number类型, 1级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `r_value2` int(11) NULL DEFAULT NULL COMMENT '对number类型, 2级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `r_value3` int(11) NULL DEFAULT NULL COMMENT '对number类型, 3级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '规则添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_ruleasset_id`(`asset_id`) USING BTREE,
  INDEX `fk_ruleasset_typeid`(`atype_id`) USING BTREE,
  CONSTRAINT `fk_ruleasset_id` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_ruleasset_typeid` FOREIGN KEY (`atype_id`) REFERENCES `nms_asset_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 154 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_rule_asset_index
-- ----------------------------
DROP TABLE IF EXISTS `nms_rule_asset_index`;
CREATE TABLE `nms_rule_asset_index`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT '资产id, 与nms_asset表id相关联',
  `index_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产指标索引值, 例if指标有多个值, if_index值',
  `atype_id` int(11) NULL DEFAULT NULL COMMENT '资产类别, 与nms_asset_type表id相关联',
  `d_type` int(3) NULL DEFAULT NULL COMMENT '0:number, 1:boolean, 2:string',
  `r_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称, 定义规则指标的告警名称',
  `r_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警内容, cpu利用率超过阈值等',
  `r_unit` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阀值单位, %, kb等',
  `r_seq` int(3) NULL DEFAULT NULL COMMENT '阀值比较顺序, 0按降序比较, 1按生序比较',
  `r_enable` int(2) NOT NULL DEFAULT 1 COMMENT '告警使能状态, 0表示不告警, 1表示告警',
  `r_value1` int(11) NULL DEFAULT NULL COMMENT '对number类型, 1级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `r_value2` int(11) NULL DEFAULT NULL COMMENT '对number类型, 2级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `r_value3` int(11) NULL DEFAULT NULL COMMENT '对number类型, 3级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '规则添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_asset_index_id`(`asset_id`) USING BTREE,
  INDEX `fk_asset_index_typeid`(`atype_id`) USING BTREE,
  CONSTRAINT `fk_asset_index_id` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_asset_index_typeid` FOREIGN KEY (`atype_id`) REFERENCES `nms_asset_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_rule_soft
-- ----------------------------
DROP TABLE IF EXISTS `nms_rule_soft`;
CREATE TABLE `nms_rule_soft`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `soft_id` int(11) NULL DEFAULT NULL COMMENT '软件id, 与nms_soft表id相关联',
  `atype_id` int(11) NULL DEFAULT NULL COMMENT '软件类别, 与nms_asset_type表id相关联',
  `d_type` int(3) NULL DEFAULT NULL COMMENT '0:number, 1:boolean, 2:string',
  `r_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称, 定义规则指标的告警名称',
  `r_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '告警内容, cpu利用率超过阈值等',
  `r_unit` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阀值单位, %, kb等',
  `r_seq` int(3) NULL DEFAULT NULL COMMENT '阀值比较顺序, 0按降序比较, 1按生序比较',
  `r_enable` int(2) NOT NULL DEFAULT 1 COMMENT '告警使能状态, 0表示不告警, 1表示告警',
  `r_value1` int(11) NULL DEFAULT NULL COMMENT '对number类型, 1级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `r_value2` int(11) NULL DEFAULT NULL COMMENT '对number类型, 2级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `r_value3` int(11) NULL DEFAULT NULL COMMENT '对number类型, 3级告警阀值, boolean比较结果一致则告警, string和以一次比较则告警',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '规则添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_rulesoft_id`(`soft_id`) USING BTREE,
  INDEX `fk_rulesoft_typeid`(`atype_id`) USING BTREE,
  CONSTRAINT `fk_rulesoft_id` FOREIGN KEY (`soft_id`) REFERENCES `nms_soft` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_rulesoft_typeid` FOREIGN KEY (`atype_id`) REFERENCES `nms_asset_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_sec_rule
-- ----------------------------
DROP TABLE IF EXISTS `nms_sec_rule`;
CREATE TABLE `nms_sec_rule`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `session_timeout` int(11) NOT NULL DEFAULT 600 COMMENT 'session超时时长，默认600秒',
  `pwd_min_size` int(11) NOT NULL DEFAULT 10 COMMENT '最小密码长度，默认10个字符。',
  `pwd_complexity` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '11111111' COMMENT '密码复杂度：从左到右:大写字母，小写字母，数字，特殊字符，与账号不同，与原密码不同，剩余两位预留；默认11111111。',
  `pwd_period` int(11) NOT NULL DEFAULT 90 COMMENT '密码修改周期，默认90天',
  `login_attempt` int(11) NOT NULL DEFAULT 5 COMMENT '最大尝试登录次数，默认5次。',
  `sec_interval` int(11) NOT NULL DEFAULT 30 COMMENT '超过最大尝试登录次数后锁定时长，默认30分钟。',
  `app_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'SYSMPT-000000' COMMENT 'appId',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '系统管理基础平台' COMMENT 'appName',
  `sso_switch` int(2) NOT NULL DEFAULT 0,
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `jms_status` int(2) NOT NULL DEFAULT 1 COMMENT 'jms连接状态默认1已连接，0连接失败',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_sec_rule
-- ----------------------------
INSERT INTO `nms_sec_rule` VALUES (1, 30000, 10, '11111111', 7, 5, 50, 'SYSMPT-000000', '系统管理基础平台', 1, '2021-02-24 15:27:04', 1);

-- ----------------------------
-- Table structure for nms_soft
-- ----------------------------
DROP TABLE IF EXISTS `nms_soft`;
CREATE TABLE `nms_soft`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `a_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '软件安装服务器ip地址,ip和port为联合唯一',
  `a_port` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '软件安装服务器监听端口号',
  `a_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '名称描述',
  `type_id` int(11) NULL DEFAULT 0 COMMENT '类别,与nms_asset_type表id关联',
  `a_pos` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '安装位置',
  `a_manu` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '采购厂商名称',
  `a_date` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '采购日期,按照2017-05-05格式存储',
  `a_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '责任人,通过选择nms_user表中人名,这里不做外键关联只做user表的属性',
  `dept_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属部门,与nms_department中id关联',
  `colled` tinyint(2) NULL DEFAULT 0 COMMENT '是否采集数据,0采集,1不采集',
  `coll_mode` int(3) NULL DEFAULT 0 COMMENT '软件数据采集方式ZYJ:0等方式',
  `deled` int(1) NULL DEFAULT 0 COMMENT '逻辑删除标记0:未删除,1已删除',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_soft_typeid`(`type_id`) USING BTREE,
  INDEX `fd_soft_deptid`(`dept_id`) USING BTREE,
  CONSTRAINT `fd_soft_deptid` FOREIGN KEY (`dept_id`) REFERENCES `nms_department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_soft_typeid` FOREIGN KEY (`type_id`) REFERENCES `nms_asset_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_static_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_static_info`;
CREATE TABLE `nms_static_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `unique_ident` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `product_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称, 涉密专用服务器交换机等',
  `manufacturer` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备制造商',
  `cpu_info` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备CPU描述信息或交换机sysDescr',
  `disk_sn` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备硬盘序列号,如果多个则;号分割',
  `sys_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备操作系统名称, 例如中科方德操作系统',
  `sys_arch` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备操作系统处理器架构, 例如 x86, arm, mips等',
  `sys_bits` int(11) NULL DEFAULT NULL COMMENT '设备操作系统位数,32,64等',
  `sys_version` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备操作系统版本, 例如 SVS1.4FD.0049.190123',
  `core_version` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备操作系统内核版本, 例如 3.10.0.017.64.181223',
  `net_num` int(4) NULL DEFAULT NULL COMMENT '设备物理业务网口数量',
  `cpu_num` int(4) NULL DEFAULT NULL COMMENT '设备物理CPU个数',
  `soc_version` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备soc卡版本, SR1.0005.0405.181129',
  `io_version` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备io安全保密模块版本, IO-04.2.04.02.000008',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_staticaid_assetid`(`asset_id`) USING BTREE,
  CONSTRAINT `fk_staticaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_tomcat_info
-- ----------------------------
DROP TABLE IF EXISTS `nms_tomcat_info`;
CREATE TABLE `nms_tomcat_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `asset_id` int(11) NULL DEFAULT 0 COMMENT 'asset_id关联nms_asset表主键id',
  `vm_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'java虚拟机名称',
  `vm_version` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'java虚拟机版本',
  `vm_vendor` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'java虚拟机厂商',
  `jit_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'JIT名称',
  `start_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '启动时间,2019-05-05 12:00:00格式',
  `class_path` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `max_heap_memory` int(11) NULL DEFAULT 0 COMMENT '最大的堆内存大小',
  `commit_heap_memory` int(11) NULL DEFAULT 0 COMMENT 'commit的堆内存大小',
  `used_heap_memory` int(11) NULL DEFAULT 0 COMMENT '有效的堆内存大小',
  `commit_non_heap_memory` int(11) NULL DEFAULT 0 COMMENT 'commit的非堆内存大小',
  `used_non_heap_memory` int(11) NULL DEFAULT 0 COMMENT '有效的非堆内存大小',
  `thread_count` int(11) NULL DEFAULT 0 COMMENT '线程总数量',
  `loaded_class_count` int(11) NULL DEFAULT 0 COMMENT 'load的类的总数量',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_aid_mysql`(`asset_id`) USING BTREE,
  CONSTRAINT `fk_aid_mysql` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_topo_area
-- ----------------------------
DROP TABLE IF EXISTS `nms_topo_area`;
CREATE TABLE `nms_topo_area`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `mapId` int(11) NULL DEFAULT 1 COMMENT '指定该节点属于哪个map图, 关联nms_topo_map的id',
  `divLeft` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '20px' COMMENT '节点的横坐标值单位px',
  `divTop` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '20px' COMMENT '节点的纵坐标值单位px',
  `divWidth` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '100px' COMMENT '节点的宽度px',
  `divHeight` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '100px' COMMENT '节点的高度px',
  `divName` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '应用业务名',
  `lineDashed` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'dashed' COMMENT '虚线框是实线还是虚线',
  `lineWidth` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '2' COMMENT '虚线框线的宽度',
  `lineColor` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '#09efe0' COMMENT '虚线框线的颜色',
  `deled` int(2) NULL DEFAULT 0 COMMENT '0表示显示在拓扑中,1表示不显示',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '节点入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_topo_link
-- ----------------------------
DROP TABLE IF EXISTS `nms_topo_link`;
CREATE TABLE `nms_topo_link`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `map_id` int(11) NOT NULL COMMENT '指定该节点属于哪个map图, 关联nms_topo_map的id',
  `s_node_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链路起始的node_id值对应nms_topo_node中node_id',
  `s_index` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链路起始端口的索引值',
  `s_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链路起始端口对应的ip地址',
  `s_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链路起始端口描述',
  `s_mac` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链路起始端口对应的mac地址',
  `e_node_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链路终点的node_id值对应nms_topo_node中node_id',
  `e_index` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链路终端端口的索引值',
  `e_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链路终端始端口对应的ip地址',
  `e_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链路终端端口描述',
  `e_mac` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链路终端端口对应的mac地址',
  `l_type` int(2) NULL DEFAULT NULL COMMENT '链路对应的类型 0实体链路, 1示意链路',
  `l_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链路名称:start_ip_index/end_ip_index',
  `l_dash` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'solid' COMMENT '链路线的类型:solid',
  `l_width` int(4) NULL DEFAULT 1 COMMENT '链路线的宽度',
  `l_offset` int(4) NULL DEFAULT 0 COMMENT '链路坐标偏移量, 如果有两个设备连接多根链路,线的坐标需要偏移不能重复在一个位置',
  `col1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留字段1',
  `col2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留字段2',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '链路入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_topolink_mapid`(`map_id`) USING BTREE,
  CONSTRAINT `fk_topolink_mapid` FOREIGN KEY (`map_id`) REFERENCES `nms_topo_map` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_topo_map
-- ----------------------------
DROP TABLE IF EXISTS `nms_topo_map`;
CREATE TABLE `nms_topo_map`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `t_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成map名称=submap+时间戳, 唯一, 默认根拓扑名rootmap',
  `t_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拓扑图名称',
  `t_picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拓扑图背景图片',
  `t_type` int(3) NULL DEFAULT 0 COMMENT '类别0网络拓扑,1业务拓扑',
  `z_percent` float NULL DEFAULT 1 COMMENT '页面zoom的缩放比例',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建拓扑图入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_topo_map
-- ----------------------------
INSERT INTO `nms_topo_map` VALUES (1, '0', '基础管理平台', 'submap.jpg', 0, 1, '2019-08-22 22:24:32');

-- ----------------------------
-- Table structure for nms_topo_meta
-- ----------------------------
DROP TABLE IF EXISTS `nms_topo_meta`;
CREATE TABLE `nms_topo_meta`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `m_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '添加示意单元的名称',
  `m_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '添加示意单元的图标',
  `m_wid` int(4) NULL DEFAULT NULL COMMENT '添加示意单元图片宽度',
  `m_hei` int(4) NULL DEFAULT NULL COMMENT '添加示意单元图标高度',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '示意单位配置入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nms_topo_meta
-- ----------------------------
INSERT INTO `nms_topo_meta` VALUES (1, '服务器(专用)', './img/server/server_special_green.png', 70, 70, '2020-09-02 07:11:05');
INSERT INTO `nms_topo_meta` VALUES (2, '服务器(普通)', './img/server/server_linux_common_green.png', 70, 70, '2020-09-02 07:11:05');
INSERT INTO `nms_topo_meta` VALUES (3, '网络设备(交换机)', './img/interchanger/interchanger_access_special_green.png', 70, 70, '2020-09-02 07:11:05');
INSERT INTO `nms_topo_meta` VALUES (4, '网络设备(核心交换机)', './img/interchanger/interchanger_access_common_green.png', 70, 70, '2020-09-02 07:11:05');
INSERT INTO `nms_topo_meta` VALUES (5, '安全设备(防火墙)', './img/firewall/firewall_common_green.png', 70, 70, '2020-09-02 07:11:05');
INSERT INTO `nms_topo_meta` VALUES (6, '安全设备(入侵检测)', './img/IDS/IDS_common_green.png', 70, 70, '2020-09-02 07:11:05');
INSERT INTO `nms_topo_meta` VALUES (7, '数据库', './img/database/database_common_green.png', 70, 70, '2020-09-02 07:11:05');
INSERT INTO `nms_topo_meta` VALUES (8, '中间件', './img/middleware/middleware_common_green.png', 70, 70, '2020-09-02 07:11:05');
INSERT INTO `nms_topo_meta` VALUES (9, '终端机(专用)', './img/middleware/middleware_special_green.png', 70, 70, '2020-09-02 07:11:05');

-- ----------------------------
-- Table structure for nms_topo_node
-- ----------------------------
DROP TABLE IF EXISTS `nms_topo_node`;
CREATE TABLE `nms_topo_node`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `map_id` int(11) NOT NULL COMMENT '指定该节点属于哪个map图, 关联nms_topo_map的id',
  `node_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拓扑node_id值=net+asset_id或met+asset_id',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '资产类别, 与nms_asset_type表id相关联, 0表示示意节点',
  `img` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储改拓扑图中节点的正常情况下的图标路径',
  `x` int(11) NULL DEFAULT NULL COMMENT '节点的横坐标值单位px',
  `y` int(11) NULL DEFAULT NULL COMMENT '节点的纵坐标值单位px',
  `width` int(11) NULL DEFAULT NULL COMMENT '节点的宽度px',
  `height` int(11) NULL DEFAULT NULL COMMENT '节点的高度px',
  `ip` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点的ip地址',
  `alias` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点的设备别名',
  `rel_map` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点关联的拓扑图名称与nms_topo_map中map_id相同',
  `col1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留字段1',
  `col2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留字段2',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '节点入库时间',
  `container_id` int(11) NULL DEFAULT NULL COMMENT '容器Id,关联容器',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_toponode_mapid`(`map_id`) USING BTREE,
  INDEX `fk_toponode_typeid`(`type_id`) USING BTREE,
  CONSTRAINT `fk_toponode_mapid` FOREIGN KEY (`map_id`) REFERENCES `nms_topo_map` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_toponode_typeid` FOREIGN KEY (`type_id`) REFERENCES `nms_asset_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_user
-- ----------------------------
DROP TABLE IF EXISTS `nms_user`;
CREATE TABLE `nms_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `card` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一标识建议身份证号',
  `deptId` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门,与nms_department中id关联',
  `education` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '学历',
  `nationality` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '民族',
  `birthDate` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '出生日期',
  `sex` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '性别',
  `createtime` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建时间',
  `reserver1` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '预留字段1',
  `reserver2` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '预留字段2',
  `deled` int(1) NULL DEFAULT 0 COMMENT '逻辑删除标记0:未删除,1已删除',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fd_user_deptid`(`deptId`) USING BTREE,
  CONSTRAINT `fd_user_deptid` FOREIGN KEY (`deptId`) REFERENCES `nms_department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_yth_account
-- ----------------------------
DROP TABLE IF EXISTS `nms_yth_account`;
CREATE TABLE `nms_yth_account`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `biosName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号bios名称',
  `userName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号用户名称',
  `userRealName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号用户真实名称',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号类型（管理员or 普通用户）',
  `uniqueIdent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主机唯一标识',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT '资产IP，在本系统唯一',
  `freq` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_accountaid_assetid`(`asset_id`) USING BTREE,
  CONSTRAINT `fk_accountaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_yth_app
-- ----------------------------
DROP TABLE IF EXISTS `nms_yth_app`;
CREATE TABLE `nms_yth_app`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `appName` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用系统名称',
  `appPort` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用系统端口号多个用英文;分割',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT '资产IP，在本系统唯一',
  `freq` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_appaid_assetid`(`asset_id`) USING BTREE,
  CONSTRAINT `fk_appaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_yth_app_account
-- ----------------------------
DROP TABLE IF EXISTS `nms_yth_app_account`;
CREATE TABLE `nms_yth_app_account`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `appId` int(11) NOT NULL COMMENT 'appId关联nms_yth_app表主键id',
  `biosName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号bios名称',
  `userName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号用户名称',
  `userRealName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号用户真实名称',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号类型（管理员or 普通用户）',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nms_yth_soft
-- ----------------------------
DROP TABLE IF EXISTS `nms_yth_soft`;
CREATE TABLE `nms_yth_soft`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id自增',
  `softName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '软件名称',
  `softVersion` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '软件版本',
  `architecture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '软件架构',
  `productType` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '软件产品类型中文描述',
  `sm3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '软件包校验sm3值',
  `jobId` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务ID',
  `decInfo` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '软件的描述信息',
  `updateTime` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '软件安装或更新时间',
  `uniqueIdent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主机唯一标识',
  `platformType` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属平台类型',
  `asset_id` int(11) NULL DEFAULT NULL COMMENT '资产IP，在本系统唯一',
  `freq` int(11) NULL DEFAULT NULL COMMENT 'asset_id关联nms_asset表主键id',
  `itime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_softaid_assetid`(`asset_id`) USING BTREE,
  CONSTRAINT `fk_softaid_assetid` FOREIGN KEY (`asset_id`) REFERENCES `nms_asset` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
