/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50561
Source Host           : localhost:3306
Source Database       : project

Target Server Type    : MYSQL
Target Server Version : 50561
File Encoding         : 65001

Date: 2019-12-01 22:50:17
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `books`
-- ----------------------------
DROP TABLE IF EXISTS `books`;
CREATE TABLE `books` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `writer_id` int(11) DEFAULT NULL,
  `genre_id` int(11) DEFAULT NULL,
  `pub_id` int(11) DEFAULT NULL,
  `publishdate` date DEFAULT NULL,
  PRIMARY KEY (`book_id`),
  KEY `writer_id` (`writer_id`),
  KEY `genre_id` (`genre_id`),
  KEY `pub_id` (`pub_id`),
  CONSTRAINT `books_ibfk_1` FOREIGN KEY (`writer_id`) REFERENCES `writers` (`writer_id`),
  CONSTRAINT `books_ibfk_2` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`genre_id`),
  CONSTRAINT `books_ibfk_3` FOREIGN KEY (`pub_id`) REFERENCES `publisher` (`pub_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=euckr;

-- ----------------------------
-- Records of books
-- ----------------------------
INSERT INTO `books` VALUES ('1', '난장이가 쏘아올린 작은 공', '2', '3', '1', '1976-01-01');
INSERT INTO `books` VALUES ('2', '살인자의 기억법', '9', '3', '9', '2013-07-24');
INSERT INTO `books` VALUES ('3', '돌이킬 수 없는 약속', '8', '3', '8', '2017-02-02');
INSERT INTO `books` VALUES ('4', '시간의 역사', '1', '2', '2', '1988-01-01');
INSERT INTO `books` VALUES ('5', '사피엔스', '16', '2', '3', '2015-11-24');
INSERT INTO `books` VALUES ('6', '두근두근 c언어', '3', '14', '4', '2015-01-23');
INSERT INTO `books` VALUES ('7', '어서와 java는 처음이지!', '3', '14', '17', '2015-11-25');
INSERT INTO `books` VALUES ('8', 'HEAD FIRST SQL', '17', '14', '18', '2008-05-01');
INSERT INTO `books` VALUES ('9', '설민석의 한국사 대모험1', '4', '7', '5', '2017-02-08');
INSERT INTO `books` VALUES ('10', '설민석의 한국사 대모험2', '4', '7', '5', '2017-03-14');
INSERT INTO `books` VALUES ('11', '설민석의 한국사 대모험3', '4', '7', '5', '2017-05-26');
INSERT INTO `books` VALUES ('12', '지금 이 순간을 살아라', '14', '4', '13', '2008-09-02');
INSERT INTO `books` VALUES ('13', '지쳤거나 좋아하는게 없거나', '5', '6', '6', '2019-09-04');
INSERT INTO `books` VALUES ('14', '혼자가 혼자에게', '6', '5', '7', '2019-09-19');
INSERT INTO `books` VALUES ('15', '악의 꽃', '12', '5', '10', '2003-10-16');
INSERT INTO `books` VALUES ('16', '서양미술사', '13', '13', '11', '2003-07-10');
INSERT INTO `books` VALUES ('17', '오페라에 빠지다', '10', '12', '12', '2008-10-25');
INSERT INTO `books` VALUES ('18', '탈무드', '15', '11', '14', '2001-03-05');
INSERT INTO `books` VALUES ('19', '꽃 자수 수업', '7', '8', '15', '2014-01-23');
INSERT INTO `books` VALUES ('20', '숲으로 떠나는 건강 여행', '11', '9', '16', '2007-05-31');

-- ----------------------------
-- Table structure for `country`
-- ----------------------------
DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `country_id` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=euckr;

-- ----------------------------
-- Records of country
-- ----------------------------
INSERT INTO `country` VALUES ('1', '한국');
INSERT INTO `country` VALUES ('2', '독일');
INSERT INTO `country` VALUES ('3', '이스라엘');
INSERT INTO `country` VALUES ('4', '일본');
INSERT INTO `country` VALUES ('5', '중국');
INSERT INTO `country` VALUES ('6', '프랑스');
INSERT INTO `country` VALUES ('7', '영국');
INSERT INTO `country` VALUES ('8', '캐나다');
INSERT INTO `country` VALUES ('9', '뉴욕');
INSERT INTO `country` VALUES ('10', '벨기에');

-- ----------------------------
-- Table structure for `genre`
-- ----------------------------
DROP TABLE IF EXISTS `genre`;
CREATE TABLE `genre` (
  `genre_id` int(11) NOT NULL AUTO_INCREMENT,
  `genre` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`genre_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=euckr;

-- ----------------------------
-- Records of genre
-- ----------------------------
INSERT INTO `genre` VALUES ('1', '수필');
INSERT INTO `genre` VALUES ('2', '과학');
INSERT INTO `genre` VALUES ('3', '소설');
INSERT INTO `genre` VALUES ('4', '자기계발');
INSERT INTO `genre` VALUES ('5', '시');
INSERT INTO `genre` VALUES ('6', '에세이');
INSERT INTO `genre` VALUES ('7', '역사');
INSERT INTO `genre` VALUES ('8', '취미');
INSERT INTO `genre` VALUES ('9', '건강');
INSERT INTO `genre` VALUES ('10', '철학');
INSERT INTO `genre` VALUES ('11', '종교');
INSERT INTO `genre` VALUES ('12', '음악');
INSERT INTO `genre` VALUES ('13', '미술');
INSERT INTO `genre` VALUES ('14', '컴퓨터');
INSERT INTO `genre` VALUES ('15', '동화');

-- ----------------------------
-- Table structure for `member`
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `member_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `birthday` date NOT NULL,
  `phone` char(20) NOT NULL,
  `borrowdate` date DEFAULT NULL,
  `returndate` date DEFAULT NULL,
  `is_return` char(1) DEFAULT NULL,
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=euckr;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES ('1', '황선주', '2000-02-09', '010-4050-2057', '2019-10-15', '2019-10-22', 'N');
INSERT INTO `member` VALUES ('29', '문보미', '1999-04-19', '010-2216-4754', '2019-12-01', '2019-12-08', 'Y');
INSERT INTO `member` VALUES ('35', '문보성', '1995-03-29', '010-9906-4754', '2019-11-30', '2019-12-07', 'Y');
INSERT INTO `member` VALUES ('36', '김소영', '1998-06-18', '010-3335-6818', '2019-11-22', '2019-12-06', 'Y');
INSERT INTO `member` VALUES ('37', '나가람', '2002-10-06', '010-7894-6566', '2000-01-01', '2000-01-01', 'Y');
INSERT INTO `member` VALUES ('38', '박예지', '1990-08-15', '010-4694-6214', '2019-05-23', '2019-05-30', 'N');
INSERT INTO `member` VALUES ('40', '장하나', '2005-11-07', '010-8954-5658', '2000-01-01', '2000-01-01', 'Y');
INSERT INTO `member` VALUES ('41', '이승하', '1996-03-27', '010-5655-7659', '2000-01-01', '2000-01-01', 'Y');
INSERT INTO `member` VALUES ('42', '이석민', '1997-02-18', '010-5617-0526', '2000-01-01', '2000-01-01', 'Y');
INSERT INTO `member` VALUES ('43', '정지훈', '1988-04-25', '010-9984-3658', '2018-05-26', '2018-06-04', 'Y');
INSERT INTO `member` VALUES ('44', '민소희', '1978-09-06', '010-5686-1234', '2019-11-01', '2019-11-08', 'N');
INSERT INTO `member` VALUES ('45', '최예빈', '1999-10-09', '010-4569-8265', '2019-12-05', '2019-12-12', 'Y');

-- ----------------------------
-- Table structure for `member_book`
-- ----------------------------
DROP TABLE IF EXISTS `member_book`;
CREATE TABLE `member_book` (
  `member_id` int(11) NOT NULL DEFAULT '0',
  `book_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`member_id`,`book_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `member_book_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `member_book_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

-- ----------------------------
-- Records of member_book
-- ----------------------------
INSERT INTO `member_book` VALUES ('36', '1');
INSERT INTO `member_book` VALUES ('1', '2');
INSERT INTO `member_book` VALUES ('44', '2');
INSERT INTO `member_book` VALUES ('35', '5');
INSERT INTO `member_book` VALUES ('36', '5');
INSERT INTO `member_book` VALUES ('29', '8');
INSERT INTO `member_book` VALUES ('29', '9');
INSERT INTO `member_book` VALUES ('38', '9');
INSERT INTO `member_book` VALUES ('38', '10');
INSERT INTO `member_book` VALUES ('38', '11');
INSERT INTO `member_book` VALUES ('45', '12');
INSERT INTO `member_book` VALUES ('45', '13');
INSERT INTO `member_book` VALUES ('1', '14');
INSERT INTO `member_book` VALUES ('44', '15');
INSERT INTO `member_book` VALUES ('36', '16');
INSERT INTO `member_book` VALUES ('44', '20');

-- ----------------------------
-- Table structure for `publisher`
-- ----------------------------
DROP TABLE IF EXISTS `publisher`;
CREATE TABLE `publisher` (
  `pub_id` int(11) NOT NULL AUTO_INCREMENT,
  `publisher` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`pub_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=euckr;

-- ----------------------------
-- Records of publisher
-- ----------------------------
INSERT INTO `publisher` VALUES ('1', '문학과 지성사');
INSERT INTO `publisher` VALUES ('2', '벤탐 델');
INSERT INTO `publisher` VALUES ('3', '김영사');
INSERT INTO `publisher` VALUES ('4', '생능출판');
INSERT INTO `publisher` VALUES ('5', '아이휴먼');
INSERT INTO `publisher` VALUES ('6', '강한별');
INSERT INTO `publisher` VALUES ('7', '달');
INSERT INTO `publisher` VALUES ('8', '북플라자');
INSERT INTO `publisher` VALUES ('9', '문학동네');
INSERT INTO `publisher` VALUES ('10', '문학과지성사');
INSERT INTO `publisher` VALUES ('11', '예경');
INSERT INTO `publisher` VALUES ('12', '아이세움');
INSERT INTO `publisher` VALUES ('13', '양문');
INSERT INTO `publisher` VALUES ('14', '인디북');
INSERT INTO `publisher` VALUES ('15', '나무수');
INSERT INTO `publisher` VALUES ('16', '지성사');
INSERT INTO `publisher` VALUES ('17', '인피니티북스');
INSERT INTO `publisher` VALUES ('18', '한빛미디어');

-- ----------------------------
-- Table structure for `writers`
-- ----------------------------
DROP TABLE IF EXISTS `writers`;
CREATE TABLE `writers` (
  `writer_id` int(11) NOT NULL AUTO_INCREMENT,
  `writer` char(100) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`writer_id`),
  KEY `country_id` (`country_id`),
  CONSTRAINT `writers_ibfk_1` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=euckr;

-- ----------------------------
-- Records of writers
-- ----------------------------
INSERT INTO `writers` VALUES ('1', '스티븐호킹', '7');
INSERT INTO `writers` VALUES ('2', '조세희', '1');
INSERT INTO `writers` VALUES ('3', '천인국', '1');
INSERT INTO `writers` VALUES ('4', '설민석', '1');
INSERT INTO `writers` VALUES ('5', '글배우', '1');
INSERT INTO `writers` VALUES ('6', '이병률', '1');
INSERT INTO `writers` VALUES ('7', '이연희', '1');
INSERT INTO `writers` VALUES ('8', '야쿠마루 가쿠', '4');
INSERT INTO `writers` VALUES ('9', '김영하', '1');
INSERT INTO `writers` VALUES ('10', '허영한', '1');
INSERT INTO `writers` VALUES ('11', '신원섭', '1');
INSERT INTO `writers` VALUES ('12', '샤를 피에르 보들레르', '6');
INSERT INTO `writers` VALUES ('13', '에른스트 곰브리치', '7');
INSERT INTO `writers` VALUES ('14', '에크하르트 톨레', '8');
INSERT INTO `writers` VALUES ('15', '마빈 토카이어', '9');
INSERT INTO `writers` VALUES ('16', '유발 하라리', '3');
INSERT INTO `writers` VALUES ('17', '린 베일리', '7');
