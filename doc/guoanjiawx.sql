/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50713
Source Host           : 127.0.0.1:3306
Source Database       : guoanjiawx

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2017-01-22 15:48:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for wx_activity
-- ----------------------------
DROP TABLE IF EXISTS `wx_activity`;
CREATE TABLE `wx_activity` (
  `id` varchar(36) NOT NULL DEFAULT '',
  `act_name` varchar(100) DEFAULT NULL COMMENT '活动名称',
  `act_rule` varchar(20000) DEFAULT NULL COMMENT '活动规则',
  `act_backimg` varchar(255) DEFAULT NULL COMMENT '活动背景图',
  `act_min_amount` double(10,2) DEFAULT '0.00' COMMENT '最小金额提取上限',
  `act_max_amount` double(10,2) DEFAULT '0.00' COMMENT '最大提现金额',
  `act_start_time` datetime DEFAULT NULL COMMENT '活动开始时间',
  `act_end_time` datetime DEFAULT NULL COMMENT '活动结束时间',
  `act_end_condition` varchar(255) DEFAULT NULL COMMENT '活动结束条件',
  `act_type` char(2) DEFAULT NULL COMMENT '活动类型',
  `act_create_time` datetime DEFAULT NULL COMMENT '活动创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_activity
-- ----------------------------
INSERT INTO `wx_activity` VALUES ('4028daf3593f3e4201593f418ad70002', '给您拜年啦！', '规则', '', '12.00', '11.00', '2017-01-16 20:29:44', '2017-01-31 15:49:37', '', '01', null);

-- ----------------------------
-- Table structure for wx_agent_funds
-- ----------------------------
DROP TABLE IF EXISTS `wx_agent_funds`;
CREATE TABLE `wx_agent_funds` (
  `id` varchar(36) NOT NULL,
  `a_openid` varchar(50) DEFAULT NULL,
  `a_money` double(10,2) DEFAULT NULL,
  `a_status` varchar(10) DEFAULT NULL,
  `a_description` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_agent_funds
-- ----------------------------
INSERT INTO `wx_agent_funds` VALUES ('4028daf35948f64c015948f704160001', 'oEsD8wkDqCMvruc6Ia_jiLNOzF0Q', '222.00', '01', '2545454', '2016-12-15 22:09:28');

-- ----------------------------
-- Table structure for wx_blessing_info
-- ----------------------------
DROP TABLE IF EXISTS `wx_blessing_info`;
CREATE TABLE `wx_blessing_info` (
  `id` varchar(36) NOT NULL,
  `bi_openid` varchar(36) DEFAULT NULL COMMENT 'OpenId',
  `bi_sender` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '发送者名称',
  `bi_content` varchar(1000) DEFAULT NULL COMMENT '信息内容',
  `bi_recipient` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '接收者名称',
  `bi_create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `bi_headImg` varchar(255) DEFAULT NULL COMMENT '发送者头像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_blessing_info
-- ----------------------------
INSERT INTO `wx_blessing_info` VALUES ('1', 'o8awnwmRoSfc3wcdCLSxzmlyVtZ4', '对方很好v', '春节一年一次，朋友一生一世。给您拜年啦！', '', null, 'http://wx.qlogo.cn/mmopen/XCopLcwfzedBuMl41OhQia9x39y65zAib3WIVqzxI6G0EiaAcoz2LsOmWKFmZFg1epM2OdT5zGf802okxibOrtMtTw/0');
INSERT INTO `wx_blessing_info` VALUES ('5eeb3545-f83a-429e-baf4-14826fa217d5', 'o8awnwilwxhF4EeocRDN-1w2EZOc', '行走在路上', '新年新气象，新年新希望。祝您新年大喜，富贵好运！', '', '2017-01-22 00:03:50', 'http://wx.qlogo.cn/mmopen/iayagTuYNicDSSWlQ5bjiaBqv3CIqqrjWCBwlWAjlUdJbnUg3V722nEhZNcHC6HUrU5lmDLUGKses9TcnkIStqJsQyfvd19vPia5/0');

-- ----------------------------
-- Table structure for wx_blessing_message
-- ----------------------------
DROP TABLE IF EXISTS `wx_blessing_message`;
CREATE TABLE `wx_blessing_message` (
  `id` varchar(36) NOT NULL,
  `bm_content` varchar(20000) DEFAULT NULL COMMENT '模板信息内容',
  `bm_sort` int(4) DEFAULT NULL COMMENT '模板信息排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_blessing_message
-- ----------------------------

-- ----------------------------
-- Table structure for wx_capital_pool
-- ----------------------------
DROP TABLE IF EXISTS `wx_capital_pool`;
CREATE TABLE `wx_capital_pool` (
  `id` varchar(36) NOT NULL,
  `cp_money` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '资金总额',
  `cp_balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '资金入耳',
  `cp_create_time` datetime DEFAULT NULL COMMENT '开始时间',
  `cp_end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `cp_date` date DEFAULT NULL COMMENT '使用时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_capital_pool
-- ----------------------------
INSERT INTO `wx_capital_pool` VALUES ('add55', '1.00', '1.00', '2017-01-19 00:00:00', '2017-01-19 23:59:59', '2017-01-10');

-- ----------------------------
-- Table structure for wx_prize_detail
-- ----------------------------
DROP TABLE IF EXISTS `wx_prize_detail`;
CREATE TABLE `wx_prize_detail` (
  `id` varchar(36) NOT NULL,
  `act_id` varchar(255) DEFAULT NULL COMMENT '活动ID',
  `pd_condition` varchar(20) DEFAULT NULL COMMENT '条件',
  `pd_type` char(2) DEFAULT NULL COMMENT '奖励类型',
  `pd_item` double(4,2) DEFAULT NULL COMMENT '奖品',
  `pd_status` char(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_prize_detail
-- ----------------------------
INSERT INTO `wx_prize_detail` VALUES ('4028daf3593f3e4201593f418ad70001', '4028daf3593f3e4201593f418ad70000', 'first', '01', '1.00', '01');
INSERT INTO `wx_prize_detail` VALUES ('4028daf3593f3e4201593f418ad70002', '4028daf3593f3e4201593f418ad70000', 'second', '01', '0.50', '01');

-- ----------------------------
-- Table structure for wx_red_pack_record
-- ----------------------------
DROP TABLE IF EXISTS `wx_red_pack_record`;
CREATE TABLE `wx_red_pack_record` (
  `id` varchar(36) NOT NULL,
  `return_code` varchar(16) DEFAULT NULL COMMENT '返回码',
  `return_msg` varchar(128) DEFAULT NULL COMMENT '返回消息',
  `sign` varchar(32) DEFAULT NULL COMMENT '签名',
  `result_code` varchar(16) DEFAULT NULL COMMENT '业务结果编码',
  `err_code` varchar(32) DEFAULT NULL COMMENT '错误编码',
  `err_code_des` varchar(128) DEFAULT NULL COMMENT '错误编码解释',
  `mch_billno` varchar(32) DEFAULT NULL COMMENT '商户订单号',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号',
  `wxappid` varchar(32) DEFAULT NULL COMMENT '公众账号APPID',
  `re_openid` varchar(32) DEFAULT NULL COMMENT '用户OpenId',
  `total_amount` int(10) NOT NULL DEFAULT '0' COMMENT '付款金额',
  `send_listid` varchar(32) DEFAULT NULL COMMENT '微信单号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_red_pack_record
-- ----------------------------

-- ----------------------------
-- Table structure for wx_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user` (
  `id` varchar(36) NOT NULL COMMENT '索引',
  `u_openid` varchar(50) NOT NULL COMMENT '用户OpenID',
  `u_nickname` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户昵称',
  `u_sex` varchar(10) DEFAULT NULL COMMENT '用户性别',
  `u_province` varchar(50) DEFAULT NULL COMMENT '用户省份',
  `u_city` varchar(50) DEFAULT NULL COMMENT '用户城市',
  `u_country` varchar(50) DEFAULT NULL COMMENT '用户区县',
  `u_headimg` varchar(255) DEFAULT NULL COMMENT '用户头像地址',
  `u_privilege` varchar(2000) DEFAULT NULL COMMENT '用户权限',
  `u_unionid` varchar(255) DEFAULT NULL COMMENT 'Unionid',
  `u_userphone` varchar(12) DEFAULT NULL COMMENT '用户手机号码',
  `u_name_custom` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户自定义名称',
  `u_headimg_custom` varchar(255) DEFAULT NULL COMMENT '用户自定义头像地址',
  `u_headimg_local` varchar(255) DEFAULT NULL COMMENT '用户头像本地地址',
  `u_create_time` datetime DEFAULT NULL COMMENT '用户创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `openid` (`u_openid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_user
-- ----------------------------
INSERT INTO `wx_user` VALUES ('4028daf3594db53e01594db56e880000', 'o8awnwilwxhF4EeocRDN-1w2EZOc', '行走在路上', '', '北京', '海淀', '中国', 'http://wx.qlogo.cn/mmopen/iayagTuYNicDSSWlQ5bjiaBqv3CIqqrjWCBwlWAjlUdJbnUg3V722nEhZNcHC6HUrU5lmDLUGKses9TcnkIStqJsQyfvd19vPia5/0', '', '', '18401636111', '', '', 'd:\\4028daf3594db83301594db8381a0001.png', '2016-12-30 11:10:53');
INSERT INTO `wx_user` VALUES ('4028daf3594dc56e01594dc5a8780000', 'oEsD8wlDAIh_IyNAlYMD92tTyctk', '嘻嘻', null, '北京', '朝阳', '中国', 'http://wx.qlogo.cn/mmopen/fqYKudVENibwZF5VI2zs5L64RZ7h5OYy5t3BLoNEXZc44Qy52IKsaGInzsFyBpqgrfXvGFHIdaB1M5sXV5cLAuRq5DJT6qs0D/0', null, null, '', '', '', 'd:\\4028daf3594dceed01594dceed9b0000.png', '2016-12-30 11:28:36');
INSERT INTO `wx_user` VALUES ('dc9bf180-85e9-4783-83bf-c16fb61cb5ef', 'o8awnwhsPrQoJ1lLhfv2RDGYYuj8', '铁蛋儿', '女', '', '', '中国', 'http://wx.qlogo.cn/mmopen/JxkulElekKpZnPjXCGqwBx4N2icLdBmVQPW1MDc3SFiaeIxvgkcvIThXAyrRE0N1lVyfUA4QuhOuQKE0s19VUcd5XhGbiaPycUg/0', 'null', '', null, null, null, null, null);

-- ----------------------------
-- Table structure for wx_user_activity
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_activity`;
CREATE TABLE `wx_user_activity` (
  `id` varchar(36) NOT NULL,
  `uact_name` varchar(255) DEFAULT NULL COMMENT '活动名称',
  `uact_openid` varchar(36) DEFAULT NULL COMMENT '用户Openid',
  `uact_uid` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `uact_actid` varchar(36) DEFAULT NULL COMMENT '活动ID',
  `uact_create_time` datetime DEFAULT NULL COMMENT '活动创建时间',
  `uact_userphone` varchar(12) DEFAULT NULL COMMENT '用户手机号码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_user_activity
-- ----------------------------
INSERT INTO `wx_user_activity` VALUES ('4028daf3593f3e4201593f418ad70022', 'aaa', 'o8awnwmRoSfc3wcdCLSxzmlyVtZ4a', '', '4028daf3593f3e4201593f418ad70002', '2017-01-20 11:45:44', '1212121212');

-- ----------------------------
-- Table structure for wx_user_click
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_click`;
CREATE TABLE `wx_user_click` (
  `id` varchar(36) NOT NULL COMMENT '索引',
  `uc_openid` varchar(50) DEFAULT NULL COMMENT '用户openid',
  `uc_click_type` char(2) DEFAULT '1' COMMENT '用户点击状态',
  `uc_click_count` int(2) DEFAULT '0' COMMENT '用户点击次数',
  `uc_click_time` datetime DEFAULT NULL COMMENT '用户点击时间',
  `uc_count` int(2) DEFAULT '0' COMMENT '用户当天生成次数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `openid` (`uc_openid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_user_click
-- ----------------------------
INSERT INTO `wx_user_click` VALUES ('4028daf3594db7ed01594db833390000', 'oEsD8wkDqCMvruc6Ia_jiLNOzF0Q', '01', '0', '2017-01-21 22:30:12', '5');
INSERT INTO `wx_user_click` VALUES ('4028daf3594dc56e01594dc5fb890004', 'oEsD8wlDAIh_IyNAlYMD92tTyctk', '01', '0', '2017-01-21 22:30:12', '5');

-- ----------------------------
-- Table structure for wx_user_extract
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_extract`;
CREATE TABLE `wx_user_extract` (
  `id` varchar(36) NOT NULL,
  `ur_id` varchar(36) DEFAULT NULL COMMENT '用户关联表ID',
  `ue_openid` varchar(36) DEFAULT NULL COMMENT 'OpenId',
  `ue_type` char(2) DEFAULT NULL COMMENT '资金类型',
  `ue_money` double(32,2) DEFAULT NULL COMMENT '金额',
  `ue_createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `ue_userphone` varchar(12) DEFAULT NULL COMMENT '用户手机号',
  `act_id` varchar(36) DEFAULT NULL COMMENT '活动ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_user_extract
-- ----------------------------

-- ----------------------------
-- Table structure for wx_user_funds
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_funds`;
CREATE TABLE `wx_user_funds` (
  `id` varchar(36) NOT NULL,
  `ur_id` varchar(36) DEFAULT NULL COMMENT '用户关联表ID',
  `uf_openid` varchar(36) DEFAULT NULL COMMENT 'OpenId',
  `uf_type` char(2) DEFAULT NULL COMMENT '资金类型',
  `uf_money` double(32,2) DEFAULT NULL COMMENT '金钱',
  `uf_createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `uf_userphone` varchar(12) DEFAULT NULL,
  `act_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_user_funds
-- ----------------------------
INSERT INTO `wx_user_funds` VALUES ('4028daf3594db53e01594db56ecd0001', null, 'oEsD8wkDqCMvruc6Ia_jiLNOzF0Q', '00', '1.00', '2016-12-30 11:10:53', null, '4028daf3593f3e4201593f418ad70000');
INSERT INTO `wx_user_funds` VALUES ('4028daf3594dc56e01594dc5a8b00001', null, 'oEsD8wlDAIh_IyNAlYMD92tTyctk', '00', '1.00', '2016-12-30 11:28:37', null, '4028daf3593f3e4201593f418ad70000');
INSERT INTO `wx_user_funds` VALUES ('4028daf3594dc56e01594dc5a8d70003', '4028daf3594dc56e01594dc5a8d70002', 'oEsD8wlDAIh_IyNAlYMD92tTyctk', '01', '0.50', '2016-12-30 11:28:37', null, '4028daf3593f3e4201593f418ad70000');

-- ----------------------------
-- Table structure for wx_user_relation
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_relation`;
CREATE TABLE `wx_user_relation` (
  `id` varchar(36) NOT NULL COMMENT '索引',
  `ur_openid` varchar(50) DEFAULT NULL COMMENT '用户OpenId',
  `ur_parent_openid` varchar(50) DEFAULT NULL COMMENT '分享者OpenId',
  `ur_create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_user_relation
-- ----------------------------
INSERT INTO `wx_user_relation` VALUES ('4028daf35948f64c015948f707bb0002', 'oEsD8wkDqCMvruc6Ia_jiLNOzF0Q', 'oEsD8wkDqCMvruc6Ia_jiLNOzF0Q', '2016-12-29 13:04:25');
INSERT INTO `wx_user_relation` VALUES ('4028daf3594dc56e01594dc5a8d70002', 'oEsD8wlDAIh_IyNAlYMD92tTyctk', 'oEsD8wkDqCMvruc6Ia_jiLNOzF0Q', '2016-12-30 11:28:37');

-- ----------------------------
-- View structure for wx_v_activity
-- ----------------------------
DROP VIEW IF EXISTS `wx_v_activity`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `wx_v_activity` AS select `a`.`id` AS `id`,`a`.`act_name` AS `act_name`,sum(`vf`.`vf_money`) AS `act_money`,`vf`.`vf_openid` AS `act_openid` from (`wx_activity` `a` left join `wx_v_funds` `vf` on((`vf`.`act_name` = `a`.`id`))) group by `a`.`act_name` ;

-- ----------------------------
-- View structure for wx_v_funds
-- ----------------------------
DROP VIEW IF EXISTS `wx_v_funds`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `wx_v_funds` AS select `uf`.`act_id` AS `act_name`,`u`.`u_nickname` AS `vf_nickname`,`uf`.`uf_createtime` AS `vf_createtime`,`uf`.`uf_money` AS `vf_money`,`uf`.`uf_type` AS `vf_type`,`uf`.`uf_openid` AS `vf_openid`,`uf`.`uf_userphone` AS `vf_userphone`,`uf`.`id` AS `id` from ((`wx_user_funds` `uf` left join `wx_user_relation` `ur` on((`ur`.`id` = `uf`.`ur_id`))) left join `wx_user` `u` on((`ur`.`ur_parent_openid` = `u`.`u_openid`))) where (`uf`.`uf_type` = '01') union all select `uf`.`act_id` AS `act_name`,`u`.`u_nickname` AS `vf_nickname`,`uf`.`uf_createtime` AS `vf_createtime`,`uf`.`uf_money` AS `vf_money`,`uf`.`uf_type` AS `vf_type`,`uf`.`uf_openid` AS `vf_openid`,`uf`.`uf_userphone` AS `vf_userphone`,`uf`.`id` AS `id` from (`wx_user_funds` `uf` left join `wx_user` `u` on((`uf`.`uf_openid` = `u`.`u_openid`))) where (`uf`.`uf_type` = '00') union all select `ue`.`act_id` AS `act_name`,`u`.`u_nickname` AS `vf_nickname`,`ue`.`ue_createtime` AS `vf_createtime`,(-(1) * `ue`.`ue_money`) AS `vf_money`,`ue`.`ue_type` AS `vf_type`,`ue`.`ue_openid` AS `vf_openid`,`ue`.`ue_userphone` AS `vf_userphone`,`ue`.`id` AS `id` from (`wx_user_extract` `ue` left join `wx_user` `u` on((`ue`.`ue_openid` = `u`.`u_openid`))) where (`ue`.`ue_type` = '02') ;

-- ----------------------------
-- Event structure for updateClickType
-- ----------------------------
DROP EVENT IF EXISTS `updateClickType`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` EVENT `updateClickType` ON SCHEDULE EVERY 1 DAY STARTS '2016-11-16 22:30:12' ON COMPLETION NOT PRESERVE ENABLE COMMENT '每日0点更新用户点击状态及点击次数' DO BEGIN
UPDATE wx_user_click wuc SET wuc.uc_click_type='01' , wuc.uc_click_count=0,wuc.uc_click_time=now();
END
;;
DELIMITER ;
