-- MySQL dump 10.13  Distrib 5.7.32, for Linux (x86_64)
--
-- Host: localhost    Database: BricksMgt
-- ------------------------------------------------------
-- Server version	5.7.32-0ubuntu0.18.04.1

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
-- Table structure for table `EmployeeDetails`
--

DROP TABLE IF EXISTS `EmployeeDetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EmployeeDetails` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Age` int(11) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `Gender` char(7) DEFAULT NULL,
  `PhoneNumber` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EmployeeDetails`
--

LOCK TABLES `EmployeeDetails` WRITE;
/*!40000 ALTER TABLE `EmployeeDetails` DISABLE KEYS */;
INSERT INTO `EmployeeDetails` VALUES (21,'VEERA',24,'No 202,Tsunami nagar,\nKaraikal medu,\nkaraikal,\npuducherry.\n','Male','8072801272'),(22,'VEERA SILAMBAN',24,'No 202,tsunami nagar,\nkkl	\n','Male','8072801272'),(24,'YOGESH',24,'NO 202	\n','Male','8072801272'),(25,'Y',24,'No 202,	\nkkl\n','Male','8072801272'),(26,'KAM',25,'No 202,\nKaraikal,\npondicherry.\n','Male','8072801272');
/*!40000 ALTER TABLE `EmployeeDetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `INV222021`
--

DROP TABLE IF EXISTS `INV222021`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `INV222021` (
  `InvoiceID` varchar(150) DEFAULT NULL,
  `ProductID` int(11) DEFAULT NULL,
  `Description` varchar(20) DEFAULT NULL,
  `Price` double DEFAULT NULL,
  `Quantity` int(20) DEFAULT NULL,
  `GSTP` int(10) DEFAULT NULL,
  `GSTAmt` double DEFAULT NULL,
  `Amount` double DEFAULT NULL,
  `SubTotal` double DEFAULT NULL,
  `TotalGSTAmount` double DEFAULT NULL,
  `TotalAmount` double DEFAULT NULL,
  `CustomerName` varchar(20) DEFAULT NULL,
  `CustomerNumber` varchar(20) DEFAULT NULL,
  `CustomerAddress` varchar(200) DEFAULT NULL,
  `ProductsSold` int(11) DEFAULT NULL,
  `Balance` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `INV222021`
--

LOCK TABLES `INV222021` WRITE;
/*!40000 ALTER TABLE `INV222021` DISABLE KEYS */;
INSERT INTO `INV222021` VALUES ('INV222021',18,'Cement',2000,5,28,2800,10000,10095000,507050,10602050,'Veera','8072801272','NO 202',2035,9397950),('INV222021',15,'MSand',2500,10,5,1250,25000,10095000,507050,10602050,'Veera','8072801272','NO 202',2035,9397950),('INV222021',17,'1/2 jally',3000,20,5,3000,60000,10095000,507050,10602050,'Veera','8072801272','NO 202',2035,9397950),('INV222021',16,'1/4 jally',5000,2000,5,500000,10000000,10095000,507050,10602050,'Veera','8072801272','NO 202',2035,9397950);
/*!40000 ALTER TABLE `INV222021` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `InvoiceNumbers`
--

DROP TABLE IF EXISTS `InvoiceNumbers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `InvoiceNumbers` (
  `InvNumber` varchar(25) DEFAULT NULL,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CustomerName` varchar(50) DEFAULT NULL,
  `TotalAmount` text CHARACTER SET utf8mb4,
  `Date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ProductsSold` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  FULLTEXT KEY `InvNumber` (`InvNumber`,`CustomerName`),
  FULLTEXT KEY `invoiceIndex` (`InvNumber`,`CustomerName`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `InvoiceNumbers`
--

LOCK TABLES `InvoiceNumbers` WRITE;
/*!40000 ALTER TABLE `InvoiceNumbers` DISABLE KEYS */;
INSERT INTO `InvoiceNumbers` VALUES ('Inv',1,NULL,NULL,'2021-01-30 14:18:08',NULL),('INV222021',60,'Veera','10602050','2021-02-10 15:21:27',2035);
/*!40000 ALTER TABLE `InvoiceNumbers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TodayProduction`
--

DROP TABLE IF EXISTS `TodayProduction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TodayProduction` (
  `TP_ID` int(11) NOT NULL,
  `TodayStocks` int(11) DEFAULT NULL,
  `Date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ProductName` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TodayProduction`
--

LOCK TABLES `TodayProduction` WRITE;
/*!40000 ALTER TABLE `TodayProduction` DISABLE KEYS */;
INSERT INTO `TodayProduction` VALUES (11,100,'2021-02-08 16:51:42','MSand'),(10,100,'2021-02-08 16:51:52','Cement'),(11,50,'2021-02-08 16:52:28','MSand'),(10,200,'2021-02-09 10:41:56','Cement'),(14,100,'2021-02-10 14:57:59','Cement'),(15,1000,'2021-02-10 14:59:35','MSand'),(15,2000,'2021-02-10 14:59:44','MSand'),(16,1000,'2021-02-10 14:59:59','1/4 jally'),(17,100,'2021-02-10 15:00:09','1/2 jally'),(16,2000,'2021-02-10 15:05:09','1/4 jally'),(16,2000,'2021-02-10 15:06:13','1/4 jally'),(16,2000,'2021-02-10 15:06:29','1/4 jally'),(16,1000,'2021-02-10 15:16:17','1/4 jally');
/*!40000 ALTER TABLE `TodayProduction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productDetail`
--

DROP TABLE IF EXISTS `productDetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productDetail` (
  `ProductName` varchar(255) DEFAULT NULL,
  `productPrice` double DEFAULT NULL,
  `GST` int(25) DEFAULT NULL,
  `stocks` int(11) NOT NULL DEFAULT '0',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`,`stocks`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productDetail`
--

LOCK TABLES `productDetail` WRITE;
/*!40000 ALTER TABLE `productDetail` DISABLE KEYS */;
INSERT INTO `productDetail` VALUES ('MSand',2500,5,2990,'2021-02-10 15:21:35',15),('1/4 jally',5000,5,6000,'2021-02-10 15:21:35',16),('1/2 jally',3000,5,80,'2021-02-10 15:21:35',17),('Cement',2000,28,-5,'2021-02-10 15:21:35',18);
/*!40000 ALTER TABLE `productDetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) DEFAULT NULL,
  `uname` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `age` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ID` (`id`),
  UNIQUE KEY `uname` (`uname`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Veera cilamban','veeras22','bowvs22',24);
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

-- Dump completed on 2021-02-10 21:36:24
