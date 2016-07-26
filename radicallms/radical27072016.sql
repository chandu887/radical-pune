-- MySQL dump 10.13  Distrib 5.7.13, for Win64 (x86_64)
--
-- Host: localhost    Database: radicallms
-- ------------------------------------------------------
-- Server version	5.7.13-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `coursecategory`
--

DROP TABLE IF EXISTS `coursecategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coursecategory` (
  `categoryid` int(3) NOT NULL AUTO_INCREMENT,
  `categoryname` varchar(100) NOT NULL,
  PRIMARY KEY (`categoryid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coursecategory`
--

LOCK TABLES `coursecategory` WRITE;
/*!40000 ALTER TABLE `coursecategory` DISABLE KEYS */;
INSERT INTO `coursecategory` VALUES (1,'Software'),(2,'Hardware'),(3,'Testing');
/*!40000 ALTER TABLE `coursecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `courses` (
  `courseid` int(5) NOT NULL AUTO_INCREMENT,
  `categoryid` int(3) NOT NULL,
  `coursename` varchar(100) NOT NULL,
  PRIMARY KEY (`courseid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,1,'JAVA'),(2,1,'ANDROID'),(3,1,'.NET'),(4,2,'WIRING'),(5,2,'ELECTONICS'),(6,3,'SELINIUM'),(7,3,'MANUAL'),(8,3,'AUTOMACHINE');
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leads`
--

DROP TABLE IF EXISTS `leads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leads` (
  `leadid` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `mobileno` varchar(13) NOT NULL,
  `emailid` varchar(100) DEFAULT NULL,
  `status` int(2) DEFAULT '1',
  `course` int(3) DEFAULT NULL,
  `coursecategory` int(4) DEFAULT '0',
  `leadsource` int(2) DEFAULT NULL,
  `assignedto` int(5) DEFAULT '0',
  `createdtime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastupdatetime` datetime DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`leadid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leads`
--

LOCK TABLES `leads` WRITE;
/*!40000 ALTER TABLE `leads` DISABLE KEYS */;
INSERT INTO `leads` VALUES (1,'Ganesh','7306434240','ganesh@newtel.in',1,1,1,1,2,'2016-07-23 12:14:10',NULL,NULL),(2,'Chandu','879017599','chandu@gmail.com',1,2,1,2,2,'2016-07-21 12:14:11',NULL,NULL),(3,'prasad','9988998899','prasad@gmail.com',1,2,1,3,3,'2016-07-20 12:14:11',NULL,NULL),(4,'narendra','8008997388','narednra@gmail.com',1,2,1,4,2,'2016-07-24 12:14:11',NULL,NULL),(5,'kiran','8187883707','kiran@gmail.com',2,4,2,1,3,'2016-07-24 12:14:11',NULL,NULL),(6,'srinivas','9989335550','srinu@gmail.com',3,6,3,1,2,'2016-07-24 12:14:11',NULL,NULL);
/*!40000 ALTER TABLE `leads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leadsouces`
--

DROP TABLE IF EXISTS `leadsouces`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leadsouces` (
  `leadsourceid` int(2) NOT NULL AUTO_INCREMENT,
  `leadsourcename` varchar(100) NOT NULL,
  `leadsourceemailid` varchar(100) NOT NULL,
  PRIMARY KEY (`leadsourceid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leadsouces`
--

LOCK TABLES `leadsouces` WRITE;
/*!40000 ALTER TABLE `leadsouces` DISABLE KEYS */;
INSERT INTO `leadsouces` VALUES (1,'YET5','yet5@gmail.com'),(2,'sulekha','sulekha@gmail.com'),(3,'jsutdail','justdail@gmail.com'),(4,'walkin','walkin@gmail.com'),(5,'phone enquiry','enquiry@gmail.com');
/*!40000 ALTER TABLE `leadsouces` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sendemail`
--

DROP TABLE IF EXISTS `sendemail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sendemail` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `receiveremailid` varchar(100) NOT NULL,
  `senderemailid` varchar(100) NOT NULL,
  `status` int(2) DEFAULT '0',
  `createdtime` datetime DEFAULT CURRENT_TIMESTAMP,
  `emailsendtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sendemail`
--

LOCK TABLES `sendemail` WRITE;
/*!40000 ALTER TABLE `sendemail` DISABLE KEYS */;
/*!40000 ALTER TABLE `sendemail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sendsms`
--

DROP TABLE IF EXISTS `sendsms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sendsms` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `receivermobileno` varchar(13) NOT NULL,
  `smstemplateId` int(3) NOT NULL,
  `status` int(2) DEFAULT '0',
  `createdtime` datetime DEFAULT CURRENT_TIMESTAMP,
  `smssendtime` datetime DEFAULT NULL,
  `response` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sendsms`
--

LOCK TABLES `sendsms` WRITE;
/*!40000 ALTER TABLE `sendsms` DISABLE KEYS */;
/*!40000 ALTER TABLE `sendsms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smstemplates`
--

DROP TABLE IF EXISTS `smstemplates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smstemplates` (
  `templateid` int(3) NOT NULL,
  `smstemplate` text NOT NULL,
  PRIMARY KEY (`templateid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smstemplates`
--

LOCK TABLES `smstemplates` WRITE;
/*!40000 ALTER TABLE `smstemplates` DISABLE KEYS */;
/*!40000 ALTER TABLE `smstemplates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `userid` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `emailid` varchar(100) NOT NULL,
  `createdtime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastlogindate` datetime DEFAULT NULL,
  `roleid` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin','admin@radical.com','2016-07-21 22:00:41',NULL,1),(2,'Tom','tom','tom@radical.com','2016-07-21 22:05:25',NULL,2),(3,'Prasad','prasad','prasad@radical.com','2016-07-22 22:22:22',NULL,2);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-27  1:39:03
