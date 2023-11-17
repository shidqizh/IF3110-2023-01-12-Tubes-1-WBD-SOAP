CREATE DATABASE IF NOT EXISTS soap_api_db;
USE soap_api_db;

DROP TABLE IF EXISTS `Logging`;

CREATE TABLE `Logging` (
    `id` int NOT NULL AUTO_INCREMENT,
    `description` varchar(256) NOT NULL,
    `ip` varchar(256) NOT NULL,
    `endpoint` varchar(256) NOT NULL,
    `requested_at` timestamp NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=284 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


LOCK TABLES `Logging` WRITE;
INSERT INTO `Logging` VALUES (1,'UNAUTHORIZED','1234','getAllSubscription','2023-11-18 05:17:47'),(2,'UNAUTHORIZED','1234','getAllSubscription','2023-12-02 05:19:51'),(3,'SUCCESS: get subs by subscriber_id request from REST','/127.0.0.1:57235','getAllSubsByIds','2023-12-02 13:40:03'),(4,'SUCCESS: get subs by subscriber_id request from REST','/127.0.0.1:57241','getAllSubsByIds','2023-12-02 13:40:06'),(5,'SUCCESS: get subs by subscriber_id request from REST','/127.0.0.1:57245','getAllSubsByIds','2023-12-02 13:40:07'),(6,'SUCCESS: get subs by subscriber_id request from REST','/127.0.0.1:57248','getAllSubsByIds','2023-11-02 13:40:08'),(7,'SUCCESS: get subs by subscriber_id request from REST','/127.0.0.1:57251','getAllSubsByIds','2023-12-02 03:50:29'),(8,'SUCCESS: get subs by subscriber_id request from REST','/127.0.0.1:57256','getAllSubsByIds','2023-10-02 19:44:35');
UNLOCK TABLES;


DROP TABLE IF EXISTS `Subscription`;
CREATE TABLE `Subscription` (
    `creator_id` int NOT NULL,
    `subscriber_id` int NOT NULL,
    `status` enum('PENDING','ACCEPTED','REJECTED') DEFAULT 'PENDING',
    PRIMARY KEY (`creator_id`,`subscriber_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


LOCK TABLES `Subscription` WRITE;
INSERT INTO `Subscription` VALUES (1,1,'ACCEPTED'),(2,1,'REJECTED'),(3,1,'ACCEPTED'),(4,1,'PENDING'),(5,1,'ACCEPTED'),(6,1,'PENDING'),(7,1,'REJECTED');

UNLOCK TABLES;
