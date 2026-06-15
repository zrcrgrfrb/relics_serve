/*
 Navicat Premium Dump SQL

 Source Server         : code
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : relic_db

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 15/06/2026 09:38:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_users
-- ----------------------------
DROP TABLE IF EXISTS `admin_users`;
CREATE TABLE `admin_users`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'administrator' COMMENT '角色',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_users
-- ----------------------------
INSERT INTO `admin_users` VALUES (1, 'admin', '$2a$12$f0/G2D/juap9RjhBLIX4IebSjouLNY2ejbMmJHau9N5L1liXHCDyG', 'administrator', '2026-06-10 17:50:05', '2026-06-13 19:28:49');

-- ----------------------------
-- Table structure for relic_categories
-- ----------------------------
DROP TABLE IF EXISTS `relic_categories`;
CREATE TABLE `relic_categories`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类描述',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序顺序',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文物分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of relic_categories
-- ----------------------------
INSERT INTO `relic_categories` VALUES (1, '印信图章', '各类印章、图章文物', 1, '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relic_categories` VALUES (2, '旗匾证徽', '旗帜、牌匾、证件、徽章等', 2, '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relic_categories` VALUES (3, '货币票证', '货币、票据、证券等', 3, '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relic_categories` VALUES (4, '邮票邮品', '邮票、邮品等', 4, '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relic_categories` VALUES (5, '生活器具', '生活用具、器具等', 5, '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relic_categories` VALUES (6, '武装器械', '武器、装备等', 6, '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relic_categories` VALUES (7, '其它', '其它类型文物', 7, '2026-06-10 12:36:36', '2026-06-10 12:36:36');

-- ----------------------------
-- Table structure for relic_image_features
-- ----------------------------
DROP TABLE IF EXISTS `relic_image_features`;
CREATE TABLE `relic_image_features`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `feature_vector` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `image_hash` bigint NULL DEFAULT NULL,
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `relic_id` int NOT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK2luklhc7u33j7oujfwtfraqng`(`relic_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of relic_image_features
-- ----------------------------
INSERT INTO `relic_image_features` VALUES (1, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Red%20revolutionary%20document%20historical%20land%20law%20Chinese%20Soviet%20period&image_size=landscape_16_9', 1, '2026-06-15 09:01:25.466234');
INSERT INTO `relic_image_features` VALUES (2, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ancient%20Chinese%20seal%20stamp%20red%20ink%20historical%20relic&image_size=landscape_16_9', 2, '2026-06-15 09:01:26.394556');
INSERT INTO `relic_image_features` VALUES (3, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Red%20Army%20flag%20Chinese%20revolution%20historical%20banner&image_size=landscape_16_9', 3, '2026-06-15 09:01:27.246133');
INSERT INTO `relic_image_features` VALUES (4, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Soviet%20badge%20Chinese%20revolution%20red%20star&image_size=landscape_16_9', 4, '2026-06-15 09:01:27.929051');
INSERT INTO `relic_image_features` VALUES (5, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Soviet%20currency%20banknote%20Chinese%20revolution&image_size=landscape_16_9', 5, '2026-06-15 09:01:28.577459');
INSERT INTO `relic_image_features` VALUES (6, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Soviet%20area%20paper%20money%20historical%20currency&image_size=landscape_16_9', 6, '2026-06-15 09:01:29.222498');
INSERT INTO `relic_image_features` VALUES (7, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=revolutionary%20stamp%20collection%20postage%20China&image_size=landscape_16_9', 7, '2026-06-15 09:01:29.817264');
INSERT INTO `relic_image_features` VALUES (8, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=commemorative%20stamp%20historical%20event%20China&image_size=landscape_16_9', 8, '2026-06-15 09:01:30.390705');
INSERT INTO `relic_image_features` VALUES (9, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ceramic%20vase%20Chinese%20traditional%20porcelain&image_size=landscape_16_9', 9, '2026-06-15 09:01:31.075054');
INSERT INTO `relic_image_features` VALUES (10, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=traditional%20Chinese%20daily%20utensils%20wooden&image_size=landscape_16_9', 10, '2026-06-15 09:01:31.758569');
INSERT INTO `relic_image_features` VALUES (11, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=old%20rifle%20military%20weapon%20historical&image_size=landscape_16_9', 11, '2026-06-15 09:01:32.334174');
INSERT INTO `relic_image_features` VALUES (12, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Chinese%20broadsword%20traditional%20weapon%20steel&image_size=landscape_16_9', 12, '2026-06-15 09:01:33.039060');
INSERT INTO `relic_image_features` VALUES (13, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=revolutionary%20document%20manuscript%20historical&image_size=landscape_16_9', 13, '2026-06-15 09:01:33.669181');
INSERT INTO `relic_image_features` VALUES (14, '0.046630859375,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.8828125E-4,2.44140625E-4,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.008056640625,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.944580078125,0.8829759191176442,0.9380780637254867,0.9378705882352907,0.8795753676470559,0.8798027420343107,0.9277292585784291,0.9263203431372523,0.8808062499999978,0.8765760723039188,0.9063196078431341,0.9140159160539186,0.8826682751225469,0.8246986060048995,0.879437499999997,0.8809820006127425,0.8301552696078414', -9223372036854513664, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=historical%20black%20white%20photo%20China&image_size=landscape_16_9', 14, '2026-06-15 09:01:34.343570');

-- ----------------------------
-- Table structure for relics
-- ----------------------------
DROP TABLE IF EXISTS `relics`;
CREATE TABLE `relics`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '文物ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文物标题',
  `category_id` int NULL DEFAULT NULL COMMENT '分类ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '文物描述',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片URL',
  `period` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '年代',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收藏地',
  `publish_date` date NULL DEFAULT NULL COMMENT '发布日期',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `relics_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `relic_categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文物表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of relics
-- ----------------------------
INSERT INTO `relics` VALUES (1, '《兴国土地法》', 1, '《兴国土地法》是中国共产党在土地革命时期制定的重要土地法规。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Red%20revolutionary%20document%20historical%20land%20law%20Chinese%20Soviet%20period&image_size=landscape_16_9', '土地革命时期', '江西省博物馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (2, '古代印章', 1, '古代官印，具有重要的历史价值和艺术价值。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ancient%20Chinese%20seal%20stamp%20red%20ink%20historical%20relic&image_size=landscape_16_9', '明清时期', '故宫博物院', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (3, '红军军旗', 2, '中国工农红军军旗，见证了中国革命的光辉历程。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Red%20Army%20flag%20Chinese%20revolution%20historical%20banner&image_size=landscape_16_9', '土地革命时期', '中国革命博物馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (4, '苏维埃徽章', 2, '中华苏维埃共和国时期的徽章。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Soviet%20badge%20Chinese%20revolution%20red%20star&image_size=landscape_16_9', '土地革命时期', '瑞金革命纪念馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (5, '苏维埃货币', 3, '中华苏维埃共和国发行的货币。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Soviet%20currency%20banknote%20Chinese%20revolution&image_size=landscape_16_9', '土地革命时期', '国家博物馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (6, '苏区纸币', 3, '中央苏区发行的纸币，具有重要的历史意义。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Soviet%20area%20paper%20money%20historical%20currency&image_size=landscape_16_9', '土地革命时期', '江西省博物馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (7, '革命邮票', 4, '纪念革命历史事件的邮票。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=revolutionary%20stamp%20collection%20postage%20China&image_size=landscape_16_9', '近现代', '中国邮政博物馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (8, '纪念邮票', 4, '纪念重要历史人物和事件的邮票。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=commemorative%20stamp%20historical%20event%20China&image_size=landscape_16_9', '近现代', '中国邮政博物馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (9, '陶瓷器具', 5, '革命时期使用的陶瓷器具。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ceramic%20vase%20Chinese%20traditional%20porcelain&image_size=landscape_16_9', '近现代', '井冈山革命博物馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (10, '生活用品', 5, '革命时期军民使用的生活用品。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=traditional%20Chinese%20daily%20utensils%20wooden&image_size=landscape_16_9', '近现代', '延安革命纪念馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (11, '步枪', 6, '革命战争时期使用的步枪。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=old%20rifle%20military%20weapon%20historical&image_size=landscape_16_9', '抗日战争时期', '军事博物馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (12, '大刀', 6, '八路军使用的大刀，象征着不屈的战斗精神。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Chinese%20broadsword%20traditional%20weapon%20steel&image_size=landscape_16_9', '抗日战争时期', '军事博物馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (13, '革命文献', 7, '重要的革命历史文献资料。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=revolutionary%20document%20manuscript%20historical&image_size=landscape_16_9', '近现代', '国家图书馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');
INSERT INTO `relics` VALUES (14, '历史照片', 7, '记录革命历史的珍贵照片。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=historical%20black%20white%20photo%20China&image_size=landscape_16_9', '近现代', '中国照片档案馆', '2025-10-19', '2026-06-10 12:36:36', '2026-06-10 12:36:36');

SET FOREIGN_KEY_CHECKS = 1;
