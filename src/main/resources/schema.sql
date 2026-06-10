CREATE DATABASE IF NOT EXISTS relic_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE relic_db;

CREATE TABLE IF NOT EXISTS relic_categories (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description VARCHAR(200) COMMENT '分类描述',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文物分类表';

CREATE TABLE IF NOT EXISTS relics (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '文物ID',
    title VARCHAR(200) NOT NULL COMMENT '文物标题',
    category_id INT COMMENT '分类ID',
    content TEXT COMMENT '文物描述',
    image_url VARCHAR(500) COMMENT '图片URL',
    period VARCHAR(100) COMMENT '年代',
    location VARCHAR(200) COMMENT '收藏地',
    publish_date DATE COMMENT '发布日期',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES relic_categories(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文物表';

-- ============================================================
-- 管理员用户表
-- ============================================================
CREATE TABLE IF NOT EXISTS admin_users (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(200) NOT NULL COMMENT '密码',
    role VARCHAR(20) NOT NULL DEFAULT 'administrator' COMMENT '角色',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员用户表';

-- 插入默认管理员（密码：admin123）
INSERT IGNORE INTO admin_users (id, username, password, role) VALUES
(1, 'admin', 'admin123', 'administrator');

INSERT IGNORE INTO relic_categories (id, name, description, sort_order) VALUES
(1, '印信图章', '各类印章、图章文物', 1),
(2, '旗匾证徽', '旗帜、牌匾、证件、徽章等', 2),
(3, '货币票证', '货币、票据、证券等', 3),
(4, '邮票邮品', '邮票、邮品等', 4),
(5, '生活器具', '生活用具、器具等', 5),
(6, '武装器械', '武器、装备等', 6),
(7, '其它', '其它类型文物', 7);

INSERT IGNORE INTO relics (id, title, category_id, content, image_url, period, location, publish_date) VALUES
(1, '《兴国土地法》', 1, '《兴国土地法》是中国共产党在土地革命时期制定的重要土地法规。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Red%20revolutionary%20document%20historical%20land%20law%20Chinese%20Soviet%20period&image_size=landscape_16_9', '土地革命时期', '江西省博物馆', '2025-10-19'),
(2, '古代印章', 1, '古代官印，具有重要的历史价值和艺术价值。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ancient%20Chinese%20seal%20stamp%20red%20ink%20historical%20relic&image_size=landscape_16_9', '明清时期', '故宫博物院', '2025-10-19'),
(3, '红军军旗', 2, '中国工农红军军旗，见证了中国革命的光辉历程。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Red%20Army%20flag%20Chinese%20revolution%20historical%20banner&image_size=landscape_16_9', '土地革命时期', '中国革命博物馆', '2025-10-19'),
(4, '苏维埃徽章', 2, '中华苏维埃共和国时期的徽章。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Soviet%20badge%20Chinese%20revolution%20red%20star&image_size=landscape_16_9', '土地革命时期', '瑞金革命纪念馆', '2025-10-19'),
(5, '苏维埃货币', 3, '中华苏维埃共和国发行的货币。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Soviet%20currency%20banknote%20Chinese%20revolution&image_size=landscape_16_9', '土地革命时期', '国家博物馆', '2025-10-19'),
(6, '苏区纸币', 3, '中央苏区发行的纸币，具有重要的历史意义。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Soviet%20area%20paper%20money%20historical%20currency&image_size=landscape_16_9', '土地革命时期', '江西省博物馆', '2025-10-19'),
(7, '革命邮票', 4, '纪念革命历史事件的邮票。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=revolutionary%20stamp%20collection%20postage%20China&image_size=landscape_16_9', '近现代', '中国邮政博物馆', '2025-10-19'),
(8, '纪念邮票', 4, '纪念重要历史人物和事件的邮票。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=commemorative%20stamp%20historical%20event%20China&image_size=landscape_16_9', '近现代', '中国邮政博物馆', '2025-10-19'),
(9, '陶瓷器具', 5, '革命时期使用的陶瓷器具。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ceramic%20vase%20Chinese%20traditional%20porcelain&image_size=landscape_16_9', '近现代', '井冈山革命博物馆', '2025-10-19'),
(10, '生活用品', 5, '革命时期军民使用的生活用品。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=traditional%20Chinese%20daily%20utensils%20wooden&image_size=landscape_16_9', '近现代', '延安革命纪念馆', '2025-10-19'),
(11, '步枪', 6, '革命战争时期使用的步枪。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=old%20rifle%20military%20weapon%20historical&image_size=landscape_16_9', '抗日战争时期', '军事博物馆', '2025-10-19'),
(12, '大刀', 6, '八路军使用的大刀，象征着不屈的战斗精神。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Chinese%20broadsword%20traditional%20weapon%20steel&image_size=landscape_16_9', '抗日战争时期', '军事博物馆', '2025-10-19'),
(13, '革命文献', 7, '重要的革命历史文献资料。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=revolutionary%20document%20manuscript%20historical&image_size=landscape_16_9', '近现代', '国家图书馆', '2025-10-19'),
(14, '历史照片', 7, '记录革命历史的珍贵照片。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=historical%20black%20white%20photo%20China&image_size=landscape_16_9', '近现代', '中国照片档案馆', '2025-10-19');
