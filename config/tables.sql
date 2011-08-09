-- MySQL dump 10.11
--
-- Host: localhost    Database: accounter
-- ------------------------------------------------------
-- Server version	5.0.51b-community-nt

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `account` (
  `ID` bigint(20) NOT NULL auto_increment,
  `A_TYPE` int(11) default NULL,
  `BASE_TYPE` int(11) default NULL,
  `SUB_BASE_TYPE` int(11) default NULL,
  `GROUP_TYPE` int(11) default NULL,
  `A_NUMBER` varchar(255) default NULL,
  `NAME` varchar(255) NOT NULL,
  `IS_ACTIVE` bit(1) default NULL,
  `PARENT_ID` bigint(20) default NULL,
  `CASHFLOW_CATEGORY` int(11) default NULL,
  `OPENING_BALANCE` double default NULL,
  `AS_OF` bigint(20) default NULL,
  `IS_CASH_ACCOUNT` bit(1) default NULL,
  `COMMENT` varchar(255) default NULL,
  `CREDIT_LIMIT` double default NULL,
  `CARD_NUMBER` varchar(255) default NULL,
  `IS_INCREASE` bit(1) default NULL,
  `CURRENT_BALANCE` double default NULL,
  `TOTAL_BALANCE` double default NULL,
  `IS_OPENING_BALANCE_EDITABLE` bit(1) default NULL,
  `HIERARCHY` varchar(255) default NULL,
  `VERSION` int(11) default NULL,
  `FLOW` varchar(255) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `LINKED_ID` bigint(20) default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  UNIQUE KEY `A_NUMBER` (`A_NUMBER`),
  KEY `FKE49F160DF1AE8CDE` (`CREATED_BY`),
  KEY `FKE49F160D9E5A0E30` (`LAST_MODIFIER`),
  KEY `FKE49F160D914588D8` (`PARENT_ID`),
  KEY `FKE49F160DB55B4109` (`LINKED_ID`),
  CONSTRAINT `FKE49F160D914588D8` FOREIGN KEY (`PARENT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FKE49F160D9E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FKE49F160DB55B4109` FOREIGN KEY (`LINKED_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FKE49F160DF1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_amounts`
--

DROP TABLE IF EXISTS `account_amounts`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `account_amounts` (
  `ACCOUNT_ID` bigint(20) NOT NULL,
  `amount` double default NULL,
  `month` int(11) NOT NULL,
  PRIMARY KEY  (`ACCOUNT_ID`,`month`),
  KEY `FKC18DA6C9E5FCF475` (`ACCOUNT_ID`),
  CONSTRAINT `FKC18DA6C9E5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `account_amounts`
--

LOCK TABLES `account_amounts` WRITE;
/*!40000 ALTER TABLE `account_amounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_amounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_transaction`
--

DROP TABLE IF EXISTS `account_transaction`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `account_transaction` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TRANSACTION_ID` bigint(20) default NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  `AMOUNT` double default NULL,
  `IS_CLOSING_FISCALYEAR_ENTRY` bit(1) default NULL,
  `IS_CASH_BASIS_ENTRY` bit(1) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK28362F8CF1AE8CDE` (`CREATED_BY`),
  KEY `FK28362F8CE5FCF475` (`ACCOUNT_ID`),
  KEY `FK28362F8C9E5A0E30` (`LAST_MODIFIER`),
  KEY `FK28362F8C63880555` (`TRANSACTION_ID`),
  CONSTRAINT `FK28362F8C63880555` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK28362F8C9E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK28362F8CE5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK28362F8CF1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `account_transaction`
--

LOCK TABLES `account_transaction` WRITE;
/*!40000 ALTER TABLE `account_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activation`
--

DROP TABLE IF EXISTS `activation`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `activation` (
  `ID` bigint(20) NOT NULL auto_increment,
  `EMAIL_ID` varchar(255) default NULL,
  `TOKEN` varchar(255) default NULL,
  `SIGN_UP_DATE` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `activation`
--

LOCK TABLES `activation` WRITE;
/*!40000 ALTER TABLE `activation` DISABLE KEYS */;
/*!40000 ALTER TABLE `activation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adjustment_reason`
--

DROP TABLE IF EXISTS `adjustment_reason`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `adjustment_reason` (
  `ID` bigint(20) NOT NULL auto_increment,
  `SECTION` varchar(255) default NULL,
  `NAME` varchar(255) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK4D50A556F1AE8CDE` (`CREATED_BY`),
  KEY `FK4D50A5569E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK4D50A5569E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK4D50A556F1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `adjustment_reason`
--

LOCK TABLES `adjustment_reason` WRITE;
/*!40000 ALTER TABLE `adjustment_reason` DISABLE KEYS */;
/*!40000 ALTER TABLE `adjustment_reason` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank`
--

DROP TABLE IF EXISTS `bank`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bank` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `VERSION` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `bank`
--

LOCK TABLES `bank` WRITE;
/*!40000 ALTER TABLE `bank` DISABLE KEYS */;
/*!40000 ALTER TABLE `bank` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank_account`
--

DROP TABLE IF EXISTS `bank_account`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bank_account` (
  `ID` bigint(20) NOT NULL,
  `BANK_ID` bigint(20) default NULL,
  `BANK_ACCOUNT_TYPE` int(11) default NULL,
  `BANK_ACCOUNT_NUMBER` varchar(255) default NULL,
  `LAST_RECONCILATION_DATE` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK1979BF0A15DEE523` (`ID`),
  KEY `FK1979BF0A8BEAF03F` (`BANK_ID`),
  CONSTRAINT `FK1979BF0A15DEE523` FOREIGN KEY (`ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK1979BF0A8BEAF03F` FOREIGN KEY (`BANK_ID`) REFERENCES `bank` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `bank_account`
--

LOCK TABLES `bank_account` WRITE;
/*!40000 ALTER TABLE `bank_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `bank_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `box`
--

DROP TABLE IF EXISTS `box`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `box` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `BOX_NUMBER` int(11) default NULL,
  `AMOUNT` double default NULL,
  `BOX_ID` bigint(20) default NULL,
  `BX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK101ABD8F488E3` (`BOX_ID`),
  CONSTRAINT `FK101ABD8F488E3` FOREIGN KEY (`BOX_ID`) REFERENCES `vat_return` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `box`
--

LOCK TABLES `box` WRITE;
/*!40000 ALTER TABLE `box` DISABLE KEYS */;
/*!40000 ALTER TABLE `box` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branding_theme`
--

DROP TABLE IF EXISTS `branding_theme`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `branding_theme` (
  `ID` bigint(20) NOT NULL auto_increment,
  `THEME_NAME` varchar(255) NOT NULL,
  `PAGE_SIZE_TYPE` int(11) default NULL,
  `TOP_MARGIN` double default NULL,
  `BOTTOM_MARGIN` double default NULL,
  `MARGIN_MEASUREMENT_TYPE` int(11) default NULL,
  `ADDRESS_PADDING` double default NULL,
  `FONT` varchar(255) default NULL,
  `FONT_SIZE` varchar(255) default NULL,
  `OVERDUE_INVOICE_TITLE` varchar(255) default NULL,
  `CREDIT_MEMO_TITLE` varchar(255) default NULL,
  `STATEMENT_TITLE` varchar(255) default NULL,
  `IS_SHOW_TAX_NUMBER` bit(1) default NULL,
  `IS_SHOW_COLUMN_HEADING` bit(1) default NULL,
  `IS_SHOW_PRICE_AND_QUANTITY` bit(1) default NULL,
  `FILE_NAME` varchar(255) default NULL,
  `IS_SHOW_TAX_COLUMN` bit(1) default NULL,
  `IS_SHOW_REGISTERED_ADDRESS` bit(1) default NULL,
  `IS_SHOW_LOGO` bit(1) default NULL,
  `IS_LOGO_ADDED` bit(1) default NULL,
  `PAYPAL_EMAIL_ID` varchar(255) default NULL,
  `LOGO_ALIGNMENT_TYPE` int(11) default NULL,
  `CONTACT_DETAILS` varchar(255) default NULL,
  `TERMS_AND_PAYMENT_ADVICE` varchar(255) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `THEME_NAME` (`THEME_NAME`),
  KEY `FK17493365F1AE8CDE` (`CREATED_BY`),
  KEY `FK174933659E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK174933659E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK17493365F1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `branding_theme`
--

LOCK TABLES `branding_theme` WRITE;
/*!40000 ALTER TABLE `branding_theme` DISABLE KEYS */;
/*!40000 ALTER TABLE `branding_theme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `budget`
--

DROP TABLE IF EXISTS `budget`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `budget` (
  `ID` bigint(20) NOT NULL auto_increment,
  `MONTH` int(11) default NULL,
  `AMOUNT` double default NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK756DA345E5FCF475` (`ACCOUNT_ID`),
  CONSTRAINT `FK756DA345E5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `budget`
--

LOCK TABLES `budget` WRITE;
/*!40000 ALTER TABLE `budget` DISABLE KEYS */;
/*!40000 ALTER TABLE `budget` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cash_purchase`
--

DROP TABLE IF EXISTS `cash_purchase`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `cash_purchase` (
  `ID` bigint(20) NOT NULL,
  `VENDOR_ID` bigint(20) default NULL,
  `CP_IS_PRIMARY` bit(1) default NULL,
  `CP_NAME` varchar(255) default NULL,
  `CP_TITLE` varchar(255) default NULL,
  `CP_BUSINESS_PHONE` varchar(255) default NULL,
  `CP_EMAIL` varchar(255) default NULL,
  `VENDOR_ADDRESS_TYPE` int(11) default NULL,
  `VENDOR_ADDRESS_ADDRESS1` varchar(100) default NULL,
  `VENDOR_ADDRESS_STREET` varchar(255) default NULL,
  `VENDOR_ADDRESS_CITY` varchar(255) default NULL,
  `VENDOR_ADDRESS_STATE` varchar(255) default NULL,
  `VENDOR_ADDRESS_ZIP` varchar(255) default NULL,
  `VENDOR_ADDRESS_COUNTRY` varchar(255) default NULL,
  `VENDOR_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `PHONE` varchar(255) default NULL,
  `PAYFROM_ACCOUNT_ID` bigint(20) default NULL,
  `CASH_EXPENSE_ACCOUNT_ID` bigint(20) default NULL,
  `EMPLOYEE` bigint(20) default NULL,
  `CHECK_NUMBER` varchar(255) default NULL,
  `DELIVERY_DATE` bigint(20) default NULL,
  `EXPENSE_STATUS` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKF7279C8DA133FB48` (`CASH_EXPENSE_ACCOUNT_ID`),
  KEY `FKF7279C8D274BC854` (`ID`),
  KEY `FKF7279C8D891A177F` (`VENDOR_ID`),
  KEY `FKF7279C8D68A08E82` (`PAYFROM_ACCOUNT_ID`),
  KEY `FKF7279C8DE72DCA7E` (`EMPLOYEE`),
  CONSTRAINT `FKF7279C8D274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKF7279C8D68A08E82` FOREIGN KEY (`PAYFROM_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FKF7279C8D891A177F` FOREIGN KEY (`VENDOR_ID`) REFERENCES `vendor` (`ID`),
  CONSTRAINT `FKF7279C8DA133FB48` FOREIGN KEY (`CASH_EXPENSE_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FKF7279C8DE72DCA7E` FOREIGN KEY (`EMPLOYEE`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `cash_purchase`
--

LOCK TABLES `cash_purchase` WRITE;
/*!40000 ALTER TABLE `cash_purchase` DISABLE KEYS */;
/*!40000 ALTER TABLE `cash_purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cash_sales`
--

DROP TABLE IF EXISTS `cash_sales`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `cash_sales` (
  `ID` bigint(20) NOT NULL,
  `CUSTOMER_ID` bigint(20) default NULL,
  `CS_IS_PRIMARY` bit(1) default NULL,
  `CS_NAME` varchar(255) default NULL,
  `CS_TITLE` varchar(255) default NULL,
  `CS_BUSINESS_PHONE` varchar(255) default NULL,
  `CS_EMAIL` varchar(255) default NULL,
  `BILLING_ADDRESS_TYPE` int(11) default NULL,
  `BILLING_ADDRESS1` varchar(100) default NULL,
  `BILLING_ADDRESS_STREET` varchar(255) default NULL,
  `BILLING_ADDRESS_CITY` varchar(255) default NULL,
  `BILLING_ADDRESS_STATE` varchar(255) default NULL,
  `BILLING_ADDRESS_ZIP` varchar(255) default NULL,
  `BILLING_ADDRESS_COUNTRY` varchar(255) default NULL,
  `BILLING_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `SHIPPING_ADDRESS_TYPE` int(11) default NULL,
  `SHIPPING_ADDRESS1` varchar(100) default NULL,
  `SHIPPING_ADDRESS_STREET` varchar(255) default NULL,
  `SHIPPING_ADDRESS_CITY` varchar(255) default NULL,
  `SHIPPING_ADDRESS_STATE` varchar(255) default NULL,
  `SHIPPING_ADDRESS_ZIP` varchar(255) default NULL,
  `SHIPPING_ADDRESS_COUNTRY` varchar(255) default NULL,
  `SHIPPING_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `PHONE` varchar(255) default NULL,
  `SALES_PERSON_ID` bigint(20) default NULL,
  `DEPOSIT_IN_ACCOUNT_ID` bigint(20) default NULL,
  `SHIPPING_TERMS_ID` bigint(20) default NULL,
  `SHIPPING_METHOD_ID` bigint(20) default NULL,
  `DELIVERY_DATE` bigint(20) default NULL,
  `PRICE_LEVEL_ID` bigint(20) default NULL,
  `SALES_TAX` double default NULL,
  `DISCOUNT_TOTAL` double default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKCA27B4E0AD0A95DC` (`SHIPPING_METHOD_ID`),
  KEY `FKCA27B4E0274BC854` (`ID`),
  KEY `FKCA27B4E09A3059EC` (`PRICE_LEVEL_ID`),
  KEY `FKCA27B4E097EDD458` (`SHIPPING_TERMS_ID`),
  KEY `FKCA27B4E04C74BEAE` (`SALES_PERSON_ID`),
  KEY `FKCA27B4E0172FFFEE` (`DEPOSIT_IN_ACCOUNT_ID`),
  KEY `FKCA27B4E0DFE06A7F` (`CUSTOMER_ID`),
  CONSTRAINT `FKCA27B4E0172FFFEE` FOREIGN KEY (`DEPOSIT_IN_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FKCA27B4E0274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKCA27B4E04C74BEAE` FOREIGN KEY (`SALES_PERSON_ID`) REFERENCES `sales_person` (`ID`),
  CONSTRAINT `FKCA27B4E097EDD458` FOREIGN KEY (`SHIPPING_TERMS_ID`) REFERENCES `shippingterms` (`ID`),
  CONSTRAINT `FKCA27B4E09A3059EC` FOREIGN KEY (`PRICE_LEVEL_ID`) REFERENCES `pricelevel` (`ID`),
  CONSTRAINT `FKCA27B4E0AD0A95DC` FOREIGN KEY (`SHIPPING_METHOD_ID`) REFERENCES `shippingmethod` (`ID`),
  CONSTRAINT `FKCA27B4E0DFE06A7F` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `cash_sales`
--

LOCK TABLES `cash_sales` WRITE;
/*!40000 ALTER TABLE `cash_sales` DISABLE KEYS */;
/*!40000 ALTER TABLE `cash_sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `client` (
  `ID` bigint(20) NOT NULL auto_increment,
  `FIRST_NAME` varchar(255) default NULL,
  `LAST_NAME` varchar(255) default NULL,
  `EMAIL_ID` varchar(255) default NULL,
  `PASSWORD` varchar(255) default NULL,
  `IS_ACTIVE` bit(1) default NULL,
  `PHONE_NUMBER` varchar(255) default NULL,
  `COUNTRY` varchar(255) default NULL,
  `IS_SUBSCRIBED_TO_NEWS_LETTER` bit(1) default NULL,
  `IS_REQUIRE_PASSWORD_RESET` bit(1) default NULL,
  `LOGIN_COUNT` int(11) default NULL,
  `LAST_LOGIN_TIME` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `EMAIL_ID` (`EMAIL_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (1,'Nagaraju','Palla','***REMOVED***','ea553d04203cd9a261effda37c7da7526c756731','','9701169376','India','','\0',0,0);
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_companies`
--

DROP TABLE IF EXISTS `client_companies`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `client_companies` (
  `CLIENT_ID` bigint(20) NOT NULL,
  `COMPANY_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`CLIENT_ID`,`COMPANY_ID`),
  KEY `FK942DD247A8D54972` (`COMPANY_ID`),
  KEY `FK942DD247384AE49F` (`CLIENT_ID`),
  CONSTRAINT `FK942DD247384AE49F` FOREIGN KEY (`CLIENT_ID`) REFERENCES `client` (`ID`),
  CONSTRAINT `FK942DD247A8D54972` FOREIGN KEY (`COMPANY_ID`) REFERENCES `server_company` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `client_companies`
--

LOCK TABLES `client_companies` WRITE;
/*!40000 ALTER TABLE `client_companies` DISABLE KEYS */;
INSERT INTO `client_companies` VALUES (1,1),(1,2);
/*!40000 ALTER TABLE `client_companies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commodity_code`
--

DROP TABLE IF EXISTS `commodity_code`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `commodity_code` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `commodity_code`
--

LOCK TABLES `commodity_code` WRITE;
/*!40000 ALTER TABLE `commodity_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `commodity_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `company` (
  `ID` bigint(20) NOT NULL auto_increment,
  `ACCOUNTING_TYPE` int(11) default NULL,
  `FULL_NAME` varchar(255) NOT NULL,
  `COMPANY_ID` varchar(255) default NULL,
  `COMPANY_EMAIL` varchar(255) default NULL,
  `COMPANY_EMAIL_FOR_CUSTOMERS` varchar(255) default NULL,
  `COMPANY_CONTACT__IS_PRIMARY` bit(1) NOT NULL,
  `COMPANY_CONTACT__NAME` varchar(255) NOT NULL,
  `COMPANY_CONTACT__TITLE` varchar(255) NOT NULL,
  `COMPANY_CONTACT__BUSINESS_PHONE` varchar(255) NOT NULL,
  `COMPANY_CONTACT_EMAIL` varchar(255) NOT NULL,
  `EIN` varchar(255) default NULL,
  `FIRSTMONTH_OF_FISCALYEAR` int(11) default NULL,
  `FIRSTMONTH_OF_INCOMETAXYEAR` int(11) default NULL,
  `TAX_FORM` int(11) default NULL,
  `BOOKS_COLSING_DATE` bigint(20) default NULL,
  `CLOSING_DATE_WARNINGTYPE` int(11) default NULL,
  `ENABLE_ACCOUNT_NUMBERS` bit(1) default NULL,
  `CUSTOMER_TYPE` int(11) default NULL,
  `ENABLE_AUTORECALL` bit(1) default NULL,
  `RESTART_SETUP_INTERVIEWS` bit(1) default NULL,
  `FISCAL_YEAR_STARTING` int(11) default NULL,
  `INDUSTRY` int(11) default NULL,
  `TAX_ID` varchar(255) default NULL,
  `LEGALNAME` varchar(255) default NULL,
  `CP_USE_ACCOUNT_NUMBERS` bit(1) NOT NULL,
  `CP_USE_CLASSES` bit(1) NOT NULL,
  `CP_USE_JOBS` bit(1) NOT NULL,
  `CP_USE_CHANGE_LOG` bit(1) NOT NULL,
  `CP_LOG_SPACE_USED` double NOT NULL,
  `CP_ALLOW_DUPLICATE_DOCUMENT_NUMBERS` bit(1) NOT NULL,
  `CP_DO_YOU_PAY_SALES_TAX` bit(1) NOT NULL,
  `CP_IS_ACCURAL_BASI` bit(1) NOT NULL,
  `CP_START_OF_FISCAL_YEAR` bigint(20) NOT NULL,
  `CP_END_OF_FISCAL_YEAR` bigint(20) NOT NULL,
  `CP_USE_FOREIGN_CURRENCY` bit(1) NOT NULL,
  `CP_IS_MY_ACCOUNTANT_WILL_RUN_PAYROLL` bit(1) NOT NULL,
  `CP_FISCAL_YEAR_START_DATE` bigint(20) NOT NULL,
  `CP_DEPRECIATION_START_DATE` bigint(20) NOT NULL,
  `CP_PREVENT_POSTING_DATE` bigint(20) NOT NULL,
  `DATE_FORMAT` varchar(255) NOT NULL,
  `CP_USE_CUSTOMER_ID` bit(1) NOT NULL,
  `CP_DEFAULT_SHIPPING_TERM` bigint(20) default NULL,
  `CP_DEFAULT_ANNUAL_INTEREST_RATE` int(11) NOT NULL,
  `CP_DEFAULT_MINIMUM_FINANCE_CHARGE` double NOT NULL,
  `CP_GRACE_DAYS` int(11) NOT NULL,
  `CP_CALCULATE_FINANCE_CHARGE_FROM_INVOICE_DATE` bit(1) NOT NULL,
  `CP_USE_VENDOR_ID` bit(1) NOT NULL,
  `SALES_ORDER_ENABLED` bit(1) NOT NULL,
  `CP_USE_ITEM_NUMBERS` bit(1) NOT NULL,
  `CP_CHECK_FOR_ITEM_QUANTITY_ON_HAND` bit(1) NOT NULL,
  `CP_UPDATE_COST_AUTOMATICALLY` bit(1) NOT NULL,
  `ENTER_VAT_INFORMATION_NOW` bit(1) NOT NULL,
  `VAT_REGISTRATION_NUMBER` varchar(255) NOT NULL,
  `VAT_REPORTING_PERIOD` int(11) NOT NULL,
  `ENDING_PERIOD_FOR_VAT_REPORTING` int(11) NOT NULL,
  `REPORT_VAT_ON_ACCURAL_BASIS` bit(1) NOT NULL,
  `TRACK_VAT` bit(1) NOT NULL,
  `VAT_TAX_AGENCY_NAME` varchar(255) NOT NULL,
  `IS_SALES_PERSON_ENABLED` bit(1) default NULL,
  `SELL_SERVICES` bit(1) default NULL,
  `SELL_PRODUCTS` bit(1) default NULL,
  `PHONE` varchar(255) default NULL,
  `FAX` varchar(255) default NULL,
  `WEBSITE` varchar(255) default NULL,
  `BANK_ACCOUNT_NO` varchar(255) default NULL,
  `SORT_CODE` varchar(255) default NULL,
  `ACCOUNTS_RECEIVABLE_ID` bigint(20) default NULL,
  `ACCOUNTS_PAYABLE_ID` bigint(20) default NULL,
  `OPENING_BALANCES_ID` bigint(20) default NULL,
  `RETAINED_EARNINGS_ID` bigint(20) default NULL,
  `OTHER_CASH_INCOME_ID` bigint(20) default NULL,
  `OTHER_CASH_EXPENSE_ID` bigint(20) default NULL,
  `VAT_LIABILITY_ACCOUNT_ID` bigint(20) default NULL,
  `VAT_FILED_LIABILITY_ACCOUNT_ID` bigint(20) default NULL,
  `PENDING_ITEM_RECEIPTS_ACCOUNT_ID` bigint(20) default NULL,
  `VERSION` int(11) default NULL,
  `SERVICE_ITEM_DEFAULT_INCOME_ACCOUNT` varchar(255) default NULL,
  `SERVICE_ITEM_DEFAULT_EXPENSE_ACCOUNT` varchar(255) default NULL,
  `NON_INVENTORY_ITEM_DEFAULT_INCOME_ACCOUNT` varchar(255) default NULL,
  `NON_INVENTORY_ITEM_DEFAULT_EXPENSE_ACCOUNT` varchar(255) default NULL,
  `UK_SERVICE_ITEM_DEFAULT_INCOME_ACCOUNT` varchar(255) default NULL,
  `UK_SERVICE_ITEM_DEFAULT_EXPENSE_ACCOUNT` varchar(255) default NULL,
  `UK_NON_INVENTORY_ITEM_DEFAULT_INCOME_ACCOUNT` varchar(255) default NULL,
  `UK_NON_INVENTORY_ITEM_DEFAULT_EXPENSE_ACCOUNT` varchar(255) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `REGISTRATION_NUMBER` varchar(255) default NULL,
  `TRADING_ADDRESS_TYPE` int(11) default NULL,
  `TRADING_ADDRESS1` varchar(100) NOT NULL,
  `TRADING_STREET` varchar(100) NOT NULL,
  `TRADING_CITY` varchar(100) NOT NULL,
  `TRADING_STATE` varchar(100) NOT NULL,
  `TRADING_ZIP` varchar(100) NOT NULL,
  `TRADING_COUNTRY` varchar(100) NOT NULL,
  `TRADING_IS_SELECTED` bit(1) NOT NULL,
  `REGISTRED_ADDRESS_TYPE` int(11) default NULL,
  `REGISTRED_ADDRESS1` varchar(100) NOT NULL,
  `REGISTRED_STREET` varchar(100) NOT NULL,
  `REGISTRED_CITY` varchar(100) NOT NULL,
  `REGISTRED_STATE` varchar(100) NOT NULL,
  `REGISTRED_ZIP` varchar(100) NOT NULL,
  `REGISTRED_COUNTRY` varchar(100) NOT NULL,
  `REGISTRED_IS_SELECTED` bit(1) NOT NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `FULL_NAME` (`FULL_NAME`),
  KEY `FK6372C85DF1AE8CDE` (`CREATED_BY`),
  KEY `FK6372C85D84106475` (`PENDING_ITEM_RECEIPTS_ACCOUNT_ID`),
  KEY `FK6372C85DCF7D8E79` (`ACCOUNTS_PAYABLE_ID`),
  KEY `FK6372C85D7C4DCF34` (`VAT_FILED_LIABILITY_ACCOUNT_ID`),
  KEY `FK6372C85D44E2B63D` (`VAT_LIABILITY_ACCOUNT_ID`),
  KEY `FK6372C85D9E66A0E4` (`OPENING_BALANCES_ID`),
  KEY `FK6372C85D2CBF1A7C` (`OTHER_CASH_INCOME_ID`),
  KEY `FK6372C85DB1A5391E` (`RETAINED_EARNINGS_ID`),
  KEY `FK6372C85D9E5A0E30` (`LAST_MODIFIER`),
  KEY `FK6372C85DC7B23C7` (`OTHER_CASH_EXPENSE_ID`),
  KEY `FK6372C85D32175D8D` (`ACCOUNTS_RECEIVABLE_ID`),
  KEY `FK6372C85DCA4E42A1` (`CP_DEFAULT_SHIPPING_TERM`),
  CONSTRAINT `FK6372C85D2CBF1A7C` FOREIGN KEY (`OTHER_CASH_INCOME_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK6372C85D32175D8D` FOREIGN KEY (`ACCOUNTS_RECEIVABLE_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK6372C85D44E2B63D` FOREIGN KEY (`VAT_LIABILITY_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK6372C85D7C4DCF34` FOREIGN KEY (`VAT_FILED_LIABILITY_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK6372C85D84106475` FOREIGN KEY (`PENDING_ITEM_RECEIPTS_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK6372C85D9E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK6372C85D9E66A0E4` FOREIGN KEY (`OPENING_BALANCES_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK6372C85DB1A5391E` FOREIGN KEY (`RETAINED_EARNINGS_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK6372C85DC7B23C7` FOREIGN KEY (`OTHER_CASH_EXPENSE_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK6372C85DCA4E42A1` FOREIGN KEY (`CP_DEFAULT_SHIPPING_TERM`) REFERENCES `shippingterms` (`ID`),
  CONSTRAINT `FK6372C85DCF7D8E79` FOREIGN KEY (`ACCOUNTS_PAYABLE_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK6372C85DF1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_nominal_code_range`
--

DROP TABLE IF EXISTS `company_nominal_code_range`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `company_nominal_code_range` (
  `COMPANY_ID` bigint(20) NOT NULL,
  `ACCOUNT_SUB_BASE_TYPE` int(11) NOT NULL,
  `MINIMUM` int(11) NOT NULL,
  `MAXIMUM` int(11) NOT NULL,
  PRIMARY KEY  (`COMPANY_ID`,`ACCOUNT_SUB_BASE_TYPE`,`MINIMUM`,`MAXIMUM`),
  KEY `FK7783B6D0622C1275` (`COMPANY_ID`),
  CONSTRAINT `FK7783B6D0622C1275` FOREIGN KEY (`COMPANY_ID`) REFERENCES `company` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `company_nominal_code_range`
--

LOCK TABLES `company_nominal_code_range` WRITE;
/*!40000 ALTER TABLE `company_nominal_code_range` DISABLE KEYS */;
/*!40000 ALTER TABLE `company_nominal_code_range` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_card_charges`
--

DROP TABLE IF EXISTS `credit_card_charges`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `credit_card_charges` (
  `ID` bigint(20) NOT NULL,
  `VENDOR_ID` bigint(20) default NULL,
  `CCC_IS_PRIMARY` bit(1) default NULL,
  `CCC_NAME` varchar(255) default NULL,
  `CCC_TITLE` varchar(255) default NULL,
  `CCC_BUSINESS_PHONE` varchar(255) default NULL,
  `CCC_EMAIL` varchar(255) default NULL,
  `VENDOR_ADDRESS_TYPE` int(11) default NULL,
  `VENDOR_ADDRESS_STREET` varchar(255) default NULL,
  `VENDOR_ADDRESS_CITY` varchar(255) default NULL,
  `VENDOR_ADDRESS_STATE` varchar(255) default NULL,
  `VENDOR_ADDRESS_ZIP` varchar(255) default NULL,
  `VENDOR_ADDRESS_COUNTRY` varchar(255) default NULL,
  `VENDOR_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `PHONE` varchar(255) default NULL,
  `PAYFROM_ACCOUNT_ID` bigint(20) default NULL,
  `CHECK_NUMBER` varchar(255) default NULL,
  `DELIVERY_DATE` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK87C7176274BC854` (`ID`),
  KEY `FK87C7176891A177F` (`VENDOR_ID`),
  KEY `FK87C717668A08E82` (`PAYFROM_ACCOUNT_ID`),
  CONSTRAINT `FK87C7176274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK87C717668A08E82` FOREIGN KEY (`PAYFROM_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK87C7176891A177F` FOREIGN KEY (`VENDOR_ID`) REFERENCES `vendor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `credit_card_charges`
--

LOCK TABLES `credit_card_charges` WRITE;
/*!40000 ALTER TABLE `credit_card_charges` DISABLE KEYS */;
/*!40000 ALTER TABLE `credit_card_charges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `creditrating`
--

DROP TABLE IF EXISTS `creditrating`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `creditrating` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `VERSION` int(11) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK792265D6F1AE8CDE` (`CREATED_BY`),
  KEY `FK792265D69E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK792265D69E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK792265D6F1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `creditrating`
--

LOCK TABLES `creditrating` WRITE;
/*!40000 ALTER TABLE `creditrating` DISABLE KEYS */;
/*!40000 ALTER TABLE `creditrating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credits_and_payments`
--

DROP TABLE IF EXISTS `credits_and_payments`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `credits_and_payments` (
  `ID` bigint(20) NOT NULL auto_increment,
  `MEMO` varchar(255) default NULL,
  `CREDIT_AMOUNT` double default NULL,
  `BALANCE` double default NULL,
  `TRANSACTION_ID` bigint(20) default NULL,
  `PAYEE_ID` bigint(20) default NULL,
  `VERSION` int(11) default '0',
  PRIMARY KEY  (`ID`),
  KEY `FK303EF05AB2FC5555` (`PAYEE_ID`),
  KEY `FK303EF05A63880555` (`TRANSACTION_ID`),
  CONSTRAINT `FK303EF05A63880555` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK303EF05AB2FC5555` FOREIGN KEY (`PAYEE_ID`) REFERENCES `payee` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `credits_and_payments`
--

LOCK TABLES `credits_and_payments` WRITE;
/*!40000 ALTER TABLE `credits_and_payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `credits_and_payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `currency`
--

DROP TABLE IF EXISTS `currency`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `currency` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `SYMBOL` varchar(255) default NULL,
  `FORMAL_NAME` varchar(255) default NULL,
  `VERSION` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `currency`
--

LOCK TABLES `currency` WRITE;
/*!40000 ALTER TABLE `currency` DISABLE KEYS */;
/*!40000 ALTER TABLE `currency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `customer` (
  `ID` bigint(20) NOT NULL,
  `BALANCE_AS_OF` bigint(20) default NULL,
  `NUMBER` varchar(255) default NULL,
  `SALES_PERSON_ID` bigint(20) default NULL,
  `CREDIT_LIMIT` double default NULL,
  `PRICE_LEVEL_ID` bigint(20) default NULL,
  `CREDIT_RATING_ID` bigint(20) default NULL,
  `SHIPPING_METHOD_ID` bigint(20) default NULL,
  `PAYMENT_TERM_ID` bigint(20) default NULL,
  `CUSTOMER_GROUP_ID` bigint(20) default NULL,
  `TAX_GROUP_ID` bigint(20) default NULL,
  `CURRET_DUE` double default NULL,
  `OVER_DUE_ONE_TO_THIRY_DAYS` double default NULL,
  `OVER_DUE_THIRTY_ONE_TO_SIXTY_DAYS` double default NULL,
  `OVER_DUE_SIXTY_ONE_TO_NINTY_DAYS` double default NULL,
  `OVER_DUE_OVER_NINTY_DAYS` double default NULL,
  `OVER_DUE_TOTAL_BALANCE` double default NULL,
  `AVERAGE_DAYS_TO_PAY` int(11) default NULL,
  `AVERAGE_DAYS_TO_PAY_YTD` int(11) default NULL,
  `MONTH_TO_DATE` double default NULL,
  `YEAR_TO_DATE` double default NULL,
  `LAST_YEAR` double default NULL,
  `LIFE_TIME_SALES` double default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NUMBER` (`NUMBER`),
  KEY `FK52C76FDE338BD6BB` (`PAYMENT_TERM_ID`),
  KEY `FK52C76FDEAD0A95DC` (`SHIPPING_METHOD_ID`),
  KEY `FK52C76FDEE8300E32` (`CREDIT_RATING_ID`),
  KEY `FK52C76FDE42DB20F8` (`CUSTOMER_GROUP_ID`),
  KEY `FK52C76FDE610348FE` (`ID`),
  KEY `FK52C76FDECD4BA408` (`TAX_GROUP_ID`),
  KEY `FK52C76FDE9A3059EC` (`PRICE_LEVEL_ID`),
  KEY `FK52C76FDE4C74BEAE` (`SALES_PERSON_ID`),
  CONSTRAINT `FK52C76FDE338BD6BB` FOREIGN KEY (`PAYMENT_TERM_ID`) REFERENCES `paymentterms` (`ID`),
  CONSTRAINT `FK52C76FDE42DB20F8` FOREIGN KEY (`CUSTOMER_GROUP_ID`) REFERENCES `customer_group` (`ID`),
  CONSTRAINT `FK52C76FDE4C74BEAE` FOREIGN KEY (`SALES_PERSON_ID`) REFERENCES `sales_person` (`ID`),
  CONSTRAINT `FK52C76FDE610348FE` FOREIGN KEY (`ID`) REFERENCES `payee` (`ID`),
  CONSTRAINT `FK52C76FDE9A3059EC` FOREIGN KEY (`PRICE_LEVEL_ID`) REFERENCES `pricelevel` (`ID`),
  CONSTRAINT `FK52C76FDEAD0A95DC` FOREIGN KEY (`SHIPPING_METHOD_ID`) REFERENCES `shippingmethod` (`ID`),
  CONSTRAINT `FK52C76FDECD4BA408` FOREIGN KEY (`TAX_GROUP_ID`) REFERENCES `tax_group` (`ID`),
  CONSTRAINT `FK52C76FDEE8300E32` FOREIGN KEY (`CREDIT_RATING_ID`) REFERENCES `creditrating` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_credit_memo`
--

DROP TABLE IF EXISTS `customer_credit_memo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `customer_credit_memo` (
  `ID` bigint(20) NOT NULL,
  `CUSTOMER_ID` bigint(20) default NULL,
  `CCM_IS_PRIMARY` bit(1) default NULL,
  `CCM_NAME` varchar(255) default NULL,
  `CCM_TITLE` varchar(255) default NULL,
  `CCM_BUSINESS_PHONE` varchar(255) default NULL,
  `CCM_EMAIL` varchar(255) default NULL,
  `BILLING_ADDRESS_TYPE` int(11) default NULL,
  `BILLING_ADDRESS1` varchar(100) default NULL,
  `BILLING_ADDRESS_STREET` varchar(255) default NULL,
  `BILLING_ADDRESS_CITY` varchar(255) default NULL,
  `BILLING_ADDRESS_STATE` varchar(255) default NULL,
  `BILLING_ADDRESS_ZIP` varchar(255) default NULL,
  `BILLING_ADDRESS_COUNTRY` varchar(255) default NULL,
  `BILLING_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `PHONE` varchar(255) default NULL,
  `SALES_PERSON_ID` bigint(20) default NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  `PRICE_LEVEL_ID` bigint(20) default NULL,
  `SALES_TAX` double default NULL,
  `DISCOUNT_TOTAL` double default NULL,
  `BALANCE_DUE` double default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK50A48BDF274BC854` (`ID`),
  KEY `FK50A48BDF9A3059EC` (`PRICE_LEVEL_ID`),
  KEY `FK50A48BDF4C74BEAE` (`SALES_PERSON_ID`),
  KEY `FK50A48BDFE5FCF475` (`ACCOUNT_ID`),
  KEY `FK50A48BDFDFE06A7F` (`CUSTOMER_ID`),
  CONSTRAINT `FK50A48BDF274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK50A48BDF4C74BEAE` FOREIGN KEY (`SALES_PERSON_ID`) REFERENCES `sales_person` (`ID`),
  CONSTRAINT `FK50A48BDF9A3059EC` FOREIGN KEY (`PRICE_LEVEL_ID`) REFERENCES `pricelevel` (`ID`),
  CONSTRAINT `FK50A48BDFDFE06A7F` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`ID`),
  CONSTRAINT `FK50A48BDFE5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `customer_credit_memo`
--

LOCK TABLES `customer_credit_memo` WRITE;
/*!40000 ALTER TABLE `customer_credit_memo` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_credit_memo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_group`
--

DROP TABLE IF EXISTS `customer_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `customer_group` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `VERSION` int(11) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK3B0AF2BEF1AE8CDE` (`CREATED_BY`),
  KEY `FK3B0AF2BE9E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK3B0AF2BE9E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK3B0AF2BEF1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `customer_group`
--

LOCK TABLES `customer_group` WRITE;
/*!40000 ALTER TABLE `customer_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_prepayment`
--

DROP TABLE IF EXISTS `customer_prepayment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `customer_prepayment` (
  `ID` bigint(20) NOT NULL,
  `DEPOSITIN_ID` bigint(20) default NULL,
  `CUSTOMER_ID` bigint(20) default NULL,
  `ENDING_BALANCE` double default NULL,
  `BALANCE_DUE` double default NULL,
  `CHECK_NUMBER` varchar(255) default NULL,
  `CPP_ADDRESS_TYPE` int(11) default NULL,
  `CPP_ADDRESS_ADDRESS1` varchar(100) default NULL,
  `CPP_ADDRESS_STREET` varchar(255) default NULL,
  `CPP_ADDRESS_CITY` varchar(255) default NULL,
  `CPP_ADDRESS_STATE` varchar(255) default NULL,
  `CPP_ADDRESS_ZIP` varchar(255) default NULL,
  `CPP_ADDRESS_COUNTRY` varchar(255) default NULL,
  `CPP_ADDRESS_IS_SELECTED` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKBEF0AD0427CE4A9F` (`DEPOSITIN_ID`),
  KEY `FKBEF0AD04274BC854` (`ID`),
  KEY `FKBEF0AD04DFE06A7F` (`CUSTOMER_ID`),
  CONSTRAINT `FKBEF0AD04274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKBEF0AD0427CE4A9F` FOREIGN KEY (`DEPOSITIN_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FKBEF0AD04DFE06A7F` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `customer_prepayment`
--

LOCK TABLES `customer_prepayment` WRITE;
/*!40000 ALTER TABLE `customer_prepayment` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_prepayment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_refund`
--

DROP TABLE IF EXISTS `customer_refund`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `customer_refund` (
  `ID` bigint(20) NOT NULL,
  `CUSTOMER_ID` bigint(20) default NULL,
  `CR__ADDRESS_TYPE` int(11) default NULL,
  `CR_ADDRESS_ADDRESS1` varchar(100) default NULL,
  `CR__ADDRESS_STREET` varchar(255) default NULL,
  `CR__ADDRESS_CITY` varchar(255) default NULL,
  `CR__ADDRESS_STATE` varchar(255) default NULL,
  `CR__ADDRESS_ZIP` varchar(255) default NULL,
  `CR__ADDRESS_COUNTRY` varchar(255) default NULL,
  `CR__ADDRESS_IS_SELECTED` bit(1) default NULL,
  `PAYFROM_ACCOUNT_ID` bigint(20) default NULL,
  `IS_TO_BE_PRINTED` bit(1) default NULL,
  `ENDING_BALANCE` double default NULL,
  `CUSTOMER_BALANCE` double default NULL,
  `IS_PAID` bit(1) default NULL,
  `PAYMENTS` double default NULL,
  `BALANCE_DUE` double default NULL,
  `CHECK_NUMBER` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK385D6AF9274BC854` (`ID`),
  KEY `FK385D6AF968A08E82` (`PAYFROM_ACCOUNT_ID`),
  KEY `FK385D6AF9DFE06A7F` (`CUSTOMER_ID`),
  CONSTRAINT `FK385D6AF9274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK385D6AF968A08E82` FOREIGN KEY (`PAYFROM_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK385D6AF9DFE06A7F` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `customer_refund`
--

LOCK TABLES `customer_refund` WRITE;
/*!40000 ALTER TABLE `customer_refund` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_refund` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `depreciation`
--

DROP TABLE IF EXISTS `depreciation`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `depreciation` (
  `ID` bigint(20) NOT NULL auto_increment,
  `STATUS` int(11) default NULL,
  `DEPRECIATE_FROM` bigint(20) default NULL,
  `DEPRECIATE_TO` bigint(20) default NULL,
  `FIXED_ASSET_ID` bigint(20) default NULL,
  `DEPRECIATION_FOR` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK59F2A58D33118956` (`FIXED_ASSET_ID`),
  CONSTRAINT `FK59F2A58D33118956` FOREIGN KEY (`FIXED_ASSET_ID`) REFERENCES `fixed_asset` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `depreciation`
--

LOCK TABLES `depreciation` WRITE;
/*!40000 ALTER TABLE `depreciation` DISABLE KEYS */;
/*!40000 ALTER TABLE `depreciation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `developer`
--

DROP TABLE IF EXISTS `developer`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `developer` (
  `ID` bigint(20) NOT NULL auto_increment,
  `CLIENT` bigint(20) default NULL,
  `API_KEY` varchar(255) default NULL,
  `SECRET_KEY` varchar(255) default NULL,
  `APPLICATION_NAME` varchar(255) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `INTEGRATIONURL` varchar(255) default NULL,
  `APPLICATIONTYPE` varchar(255) default NULL,
  `APPLICATIONUSE` varchar(255) default NULL,
  `DEVELOPER_EMAIL_ID` varchar(255) default NULL,
  `CONTACT` varchar(255) default NULL,
  `SUCCEES_REQUESTS` bigint(20) default NULL,
  `FAILURE_REQUESTS` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKA148F7AA58DBF25B` (`CLIENT`),
  CONSTRAINT `FKA148F7AA58DBF25B` FOREIGN KEY (`CLIENT`) REFERENCES `client` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `developer`
--

LOCK TABLES `developer` WRITE;
/*!40000 ALTER TABLE `developer` DISABLE KEYS */;
/*!40000 ALTER TABLE `developer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enter_bill`
--

DROP TABLE IF EXISTS `enter_bill`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `enter_bill` (
  `ID` bigint(20) NOT NULL,
  `VENDOR_ID` bigint(20) default NULL,
  `EB_IS_PRIMARY` bit(1) default NULL,
  `EB_NAME` varchar(255) default NULL,
  `EB_TITLE` varchar(255) default NULL,
  `EB_BUSINESS_PHONE` varchar(255) default NULL,
  `EB_EMAIL` varchar(255) default NULL,
  `VENDOR_ADDRESS_TYPE` int(11) default NULL,
  `VENDOR_ADDRESS_ADDRESS1` varchar(100) default NULL,
  `VENDOR_ADDRESS_STREET` varchar(255) default NULL,
  `VENDOR_ADDRESS_CITY` varchar(255) default NULL,
  `VENDOR_ADDRESS_STATE` varchar(255) default NULL,
  `VENDOR_ADDRESS_ZIP` varchar(255) default NULL,
  `VENDOR_ADDRESS_COUNTRY` varchar(255) default NULL,
  `VENDOR_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `PHONE` varchar(255) default NULL,
  `PAYMENT_TERM_ID` bigint(20) default NULL,
  `DUE_DATE` bigint(20) default NULL,
  `DELIVERY_DATE` bigint(20) default NULL,
  `IS_PAID` bit(1) default NULL,
  `PAYMENTS` double default NULL,
  `BALANCE_DUE` double default NULL,
  `DISCOUNT_DATE` bigint(20) default NULL,
  `PURCHASE_ORDER_ID` bigint(20) default NULL,
  `ITEM_RECEIPT_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKEDDD392E338BD6BB` (`PAYMENT_TERM_ID`),
  KEY `FKEDDD392EA936E12E` (`ITEM_RECEIPT_ID`),
  KEY `FKEDDD392E39750592` (`PURCHASE_ORDER_ID`),
  KEY `FKEDDD392E274BC854` (`ID`),
  KEY `FKEDDD392E891A177F` (`VENDOR_ID`),
  CONSTRAINT `FKEDDD392E274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKEDDD392E338BD6BB` FOREIGN KEY (`PAYMENT_TERM_ID`) REFERENCES `paymentterms` (`ID`),
  CONSTRAINT `FKEDDD392E39750592` FOREIGN KEY (`PURCHASE_ORDER_ID`) REFERENCES `purchase_order` (`ID`),
  CONSTRAINT `FKEDDD392E891A177F` FOREIGN KEY (`VENDOR_ID`) REFERENCES `vendor` (`ID`),
  CONSTRAINT `FKEDDD392EA936E12E` FOREIGN KEY (`ITEM_RECEIPT_ID`) REFERENCES `item_receipt` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `enter_bill`
--

LOCK TABLES `enter_bill` WRITE;
/*!40000 ALTER TABLE `enter_bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `enter_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entry`
--

DROP TABLE IF EXISTS `entry`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `entry` (
  `ID` bigint(20) NOT NULL auto_increment,
  `VERSION` int(11) default NULL,
  `TYPE` int(11) default NULL,
  `JOURNAL_ENTRY_TYPE` int(11) default NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  `VENDOR_ID` bigint(20) default NULL,
  `CUSTOMER_ID` bigint(20) default NULL,
  `TAXITEM_ID` bigint(20) default NULL,
  `TAXCODE_ID` bigint(20) default NULL,
  `MEMO` varchar(255) default NULL,
  `DEBIT` double default NULL,
  `CREDIT` double default NULL,
  `TOTAL` double default NULL,
  `VOUCHER_NUMBER` varchar(255) default NULL,
  `TRANSACTION_ID` bigint(20) default NULL,
  `ENTRY_DATE` bigint(20) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK3F11052891A177F` (`VENDOR_ID`),
  KEY `FK3F11052912718B5` (`TAXCODE_ID`),
  KEY `FK3F11052E5FCF475` (`ACCOUNT_ID`),
  KEY `FK3F110525EF9C09C` (`TRANSACTION_ID`),
  KEY `FK3F11052DFE06A7F` (`CUSTOMER_ID`),
  KEY `FK3F11052D7293EF5` (`TAXITEM_ID`),
  CONSTRAINT `FK3F110525EF9C09C` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `journal_entry` (`ID`),
  CONSTRAINT `FK3F11052891A177F` FOREIGN KEY (`VENDOR_ID`) REFERENCES `vendor` (`ID`),
  CONSTRAINT `FK3F11052912718B5` FOREIGN KEY (`TAXCODE_ID`) REFERENCES `tax_code` (`ID`),
  CONSTRAINT `FK3F11052D7293EF5` FOREIGN KEY (`TAXITEM_ID`) REFERENCES `tax_item` (`ID`),
  CONSTRAINT `FK3F11052DFE06A7F` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`ID`),
  CONSTRAINT `FK3F11052E5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `entry`
--

LOCK TABLES `entry` WRITE;
/*!40000 ALTER TABLE `entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estimate`
--

DROP TABLE IF EXISTS `estimate`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `estimate` (
  `ID` bigint(20) NOT NULL,
  `CUSTOMER_ID` bigint(20) default NULL,
  `EST_IS_PRIMARY` bit(1) default NULL,
  `EST_NAME` varchar(255) default NULL,
  `EST_TITLE` varchar(255) default NULL,
  `EST_BUSINESS_PHONE` varchar(255) default NULL,
  `EST_EMAIL` varchar(255) default NULL,
  `ESTIMATE_ADDRESS_TYPE` int(11) default NULL,
  `ESTIMATE_ADDRESS_ADDRESS1` varchar(100) default NULL,
  `ESTIMATE_ADDRESS_STREET` varchar(255) default NULL,
  `ESTIMATE_ADDRESS_CITY` varchar(255) default NULL,
  `ESTIMATE_ADDRESS_STATE` varchar(255) default NULL,
  `ESTIMATE_ADDRESS_ZIP` varchar(255) default NULL,
  `ESTIMATE_ADDRESS_COUNTRY` varchar(255) default NULL,
  `ESTIMATE_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `PHONE` varchar(255) default NULL,
  `SALES_PERSON_ID` bigint(20) default NULL,
  `PAYMENT_TERMS_ID` bigint(20) default NULL,
  `EXPIRATION_DATE` bigint(20) default NULL,
  `DELIVERY_DATE` bigint(20) default NULL,
  `PRICE_LEVEL_ID` bigint(20) default NULL,
  `SALES_TAX` double default NULL,
  `IS_TURNED_TO_INVOICE` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKB9D61528274BC854` (`ID`),
  KEY `FKB9D615289A3059EC` (`PRICE_LEVEL_ID`),
  KEY `FKB9D615284C74BEAE` (`SALES_PERSON_ID`),
  KEY `FKB9D61528AEA76B12` (`PAYMENT_TERMS_ID`),
  KEY `FKB9D61528DFE06A7F` (`CUSTOMER_ID`),
  CONSTRAINT `FKB9D61528274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKB9D615284C74BEAE` FOREIGN KEY (`SALES_PERSON_ID`) REFERENCES `sales_person` (`ID`),
  CONSTRAINT `FKB9D615289A3059EC` FOREIGN KEY (`PRICE_LEVEL_ID`) REFERENCES `pricelevel` (`ID`),
  CONSTRAINT `FKB9D61528AEA76B12` FOREIGN KEY (`PAYMENT_TERMS_ID`) REFERENCES `paymentterms` (`ID`),
  CONSTRAINT `FKB9D61528DFE06A7F` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `estimate`
--

LOCK TABLES `estimate` WRITE;
/*!40000 ALTER TABLE `estimate` DISABLE KEYS */;
/*!40000 ALTER TABLE `estimate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expense`
--

DROP TABLE IF EXISTS `expense`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `expense` (
  `ID` bigint(20) NOT NULL,
  `BILL_FROM` varchar(255) default NULL,
  `BILL_DATE` bigint(20) default NULL,
  `STATUS` int(11) default NULL,
  `SUBMITTED_DATE` bigint(20) default NULL,
  `PAYMENT_DUE_DATE` bigint(20) default NULL,
  `REPORTING_DATE` bigint(20) default NULL,
  `AMOUNT_DUE` double default NULL,
  `PAID_AMOUNT` double default NULL,
  `CATEGORY` int(11) default NULL,
  `IS_AUTHORISED` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKDCC05438274BC854` (`ID`),
  CONSTRAINT `FKDCC05438274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `expense`
--

LOCK TABLES `expense` WRITE;
/*!40000 ALTER TABLE `expense` DISABLE KEYS */;
/*!40000 ALTER TABLE `expense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finance_log`
--

DROP TABLE IF EXISTS `finance_log`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `finance_log` (
  `ID` bigint(20) NOT NULL auto_increment,
  `LOG_MESSAGE` text,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `finance_log`
--

LOCK TABLES `finance_log` WRITE;
/*!40000 ALTER TABLE `finance_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `finance_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fiscal_year`
--

DROP TABLE IF EXISTS `fiscal_year`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `fiscal_year` (
  `ID` bigint(20) NOT NULL auto_increment,
  `START_DATE` bigint(20) default NULL,
  `END_DATE` bigint(20) default NULL,
  `STATUS` int(11) default NULL,
  `IS_CURRENT_FISCAL_YEAR` bit(1) default NULL,
  `VERSION` int(11) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `START_DATE` (`START_DATE`),
  UNIQUE KEY `END_DATE` (`END_DATE`),
  KEY `FK9722721EF1AE8CDE` (`CREATED_BY`),
  KEY `FK9722721E9E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK9722721E9E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK9722721EF1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `fiscal_year`
--

LOCK TABLES `fiscal_year` WRITE;
/*!40000 ALTER TABLE `fiscal_year` DISABLE KEYS */;
/*!40000 ALTER TABLE `fiscal_year` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fixed_asset`
--

DROP TABLE IF EXISTS `fixed_asset`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `fixed_asset` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `ASSET_NUMBER` varchar(255) default NULL,
  `ASSET_ACCOUNT_ID` bigint(20) default NULL,
  `PURCHASE_DATE` bigint(20) default NULL,
  `PURCHASE_PRICE` double default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `ASSET_TYPE` varchar(255) default NULL,
  `DEPRECIATION_RATE` double default NULL,
  `DEPRECIATION_METHOD` int(11) default NULL,
  `DEPRECIATION_EXPENSE_ACCOUNT_ID` bigint(20) default NULL,
  `ACCUMULATED_DEPRECIATION_AMOUNT` double default NULL,
  `status` int(11) default NULL,
  `BOOK_VALUE` double default NULL,
  `OPENING_BALANCE_FOR_FISCALYEAR` double default NULL,
  `IS_SOLD_OR_DISPOSED` bit(1) default NULL,
  `SOLD_OR_DISPOSED_DATE` bigint(20) default NULL,
  `SALE_ACCOUNT_ID` bigint(20) default NULL,
  `SALE_PRICE` double default NULL,
  `NO_DEPRECIATION` bit(1) default NULL,
  `DEPRECIATION_TILL_DATE` bigint(20) default NULL,
  `NOTES` varchar(255) default NULL,
  `LOSS_OR_GAIN_DISPOSAL_ACCOUNT_ID` bigint(20) default NULL,
  `TOTAL_CAPITAL_GAIN_ACCOUNT_ID` bigint(20) default NULL,
  `LOSS_OR_GAIN` double default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `TOTAL_CAPITAL_GAIN_AMOUNT` double default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK9B076259BF222C3` (`LOSS_OR_GAIN_DISPOSAL_ACCOUNT_ID`),
  KEY `FK9B07625F1AE8CDE` (`CREATED_BY`),
  KEY `FK9B07625B489C463` (`TOTAL_CAPITAL_GAIN_ACCOUNT_ID`),
  KEY `FK9B07625C1D2676D` (`SALE_ACCOUNT_ID`),
  KEY `FK9B07625C598D6AE` (`DEPRECIATION_EXPENSE_ACCOUNT_ID`),
  KEY `FK9B076259E5A0E30` (`LAST_MODIFIER`),
  KEY `FK9B076259832924` (`ASSET_ACCOUNT_ID`),
  CONSTRAINT `FK9B076259832924` FOREIGN KEY (`ASSET_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK9B076259BF222C3` FOREIGN KEY (`LOSS_OR_GAIN_DISPOSAL_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK9B076259E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK9B07625B489C463` FOREIGN KEY (`TOTAL_CAPITAL_GAIN_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK9B07625C1D2676D` FOREIGN KEY (`SALE_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK9B07625C598D6AE` FOREIGN KEY (`DEPRECIATION_EXPENSE_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK9B07625F1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `fixed_asset`
--

LOCK TABLES `fixed_asset` WRITE;
/*!40000 ALTER TABLE `fixed_asset` DISABLE KEYS */;
/*!40000 ALTER TABLE `fixed_asset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fixed_asset_history`
--

DROP TABLE IF EXISTS `fixed_asset_history`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `fixed_asset_history` (
  `ID` bigint(20) NOT NULL auto_increment,
  `ACTION_DATE` bigint(20) default NULL,
  `ACTION_TYPE` varchar(255) default NULL,
  `USER` varchar(255) default NULL,
  `DETAILS` varchar(255) default NULL,
  `JOURNAL_ENTRY_ID` bigint(20) default NULL,
  `FIXED_ASSET_HISTORY_ID` bigint(20) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK4159985A778F16E1` (`FIXED_ASSET_HISTORY_ID`),
  KEY `FK4159985A408F6290` (`JOURNAL_ENTRY_ID`),
  CONSTRAINT `FK4159985A408F6290` FOREIGN KEY (`JOURNAL_ENTRY_ID`) REFERENCES `journal_entry` (`ID`),
  CONSTRAINT `FK4159985A778F16E1` FOREIGN KEY (`FIXED_ASSET_HISTORY_ID`) REFERENCES `fixed_asset` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `fixed_asset_history`
--

LOCK TABLES `fixed_asset_history` WRITE;
/*!40000 ALTER TABLE `fixed_asset_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `fixed_asset_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fixed_asset_note`
--

DROP TABLE IF EXISTS `fixed_asset_note`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `fixed_asset_note` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NOTE` varchar(255) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `FIXED_ASSET_NOTE_ID` bigint(20) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK24ABF9CCF1AE8CDE` (`CREATED_BY`),
  KEY `FK24ABF9CC492B60CF` (`FIXED_ASSET_NOTE_ID`),
  KEY `FK24ABF9CC9E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK24ABF9CC492B60CF` FOREIGN KEY (`FIXED_ASSET_NOTE_ID`) REFERENCES `fixed_asset` (`ID`),
  CONSTRAINT `FK24ABF9CC9E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK24ABF9CCF1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `fixed_asset_note`
--

LOCK TABLES `fixed_asset_note` WRITE;
/*!40000 ALTER TABLE `fixed_asset_note` DISABLE KEYS */;
/*!40000 ALTER TABLE `fixed_asset_note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `invoice` (
  `ID` bigint(20) NOT NULL,
  `CUSTOMER_ID` bigint(20) default NULL,
  `INVOICE_IS_PRIMARY` bit(1) default NULL,
  `INVOICE_NAME` varchar(255) default NULL,
  `INVOICE_TITLE` varchar(255) default NULL,
  `INVOICE_BUSINESS_PHONE` varchar(255) default NULL,
  `INVOICE_EMAIL` varchar(255) default NULL,
  `BILLING_ADDRESS_TYPE` int(11) default NULL,
  `BILLING_ADDRESS1` varchar(100) default NULL,
  `BILLING_ADDRESS_STREET` varchar(255) default NULL,
  `BILLING_ADDRESS_CITY` varchar(255) default NULL,
  `BILLING_ADDRESS_STATE` varchar(255) default NULL,
  `BILLING_ADDRESS_ZIP` varchar(255) default NULL,
  `BILLING_ADDRESS_COUNTRY` varchar(255) default NULL,
  `BILLING_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `SHIPPING_ADDRESS_TYPE` int(11) default NULL,
  `SHIPPING_ADDRESS1` varchar(100) default NULL,
  `SHIPPING_ADDRESS_STREET` varchar(255) default NULL,
  `SHIPPING_ADDRESS_CITY` varchar(255) default NULL,
  `SHIPPING_ADDRESS_STATE` varchar(255) default NULL,
  `SHIPPING_ADDRESS_ZIP` varchar(255) default NULL,
  `SHIPPING_ADDRESS_COUNTRY` varchar(255) default NULL,
  `SHIPPING_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `PHONE` varchar(255) default NULL,
  `SALES_PERSON_ID` bigint(20) default NULL,
  `PAYMENT_TERMS_ID` bigint(20) default NULL,
  `SHIPPING_TERMS_ID` bigint(20) default NULL,
  `SHIPPING_METHOD_ID` bigint(20) default NULL,
  `DUE_DATE` bigint(20) default NULL,
  `DELIVERY_DATE` bigint(20) default NULL,
  `SALES_ORDER_NO` varchar(255) default NULL,
  `DISCOUNT_TOTAL` double default NULL,
  `PRICE_LEVEL_ID` bigint(20) default NULL,
  `SALES_TAX_AMOUNT` double default NULL,
  `PAYMENTS` double default NULL,
  `BALANCE_DUE` double default NULL,
  `IS_PAID` bit(1) default NULL,
  `IS_EDITED` bit(1) default NULL,
  `ESTIMATE_ID` bigint(20) default NULL,
  `SALES_ORDER_ID` bigint(20) default NULL,
  `DISCOUNT_DATE` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK9FA1CF0DAD0A95DC` (`SHIPPING_METHOD_ID`),
  KEY `FK9FA1CF0D274BC854` (`ID`),
  KEY `FK9FA1CF0D9A3059EC` (`PRICE_LEVEL_ID`),
  KEY `FK9FA1CF0D97EDD458` (`SHIPPING_TERMS_ID`),
  KEY `FK9FA1CF0D4C74BEAE` (`SALES_PERSON_ID`),
  KEY `FK9FA1CF0D2843EE3F` (`ESTIMATE_ID`),
  KEY `FK9FA1CF0DAEA76B12` (`PAYMENT_TERMS_ID`),
  KEY `FK9FA1CF0DDFE06A7F` (`CUSTOMER_ID`),
  KEY `FK9FA1CF0D8C6D8B66` (`SALES_ORDER_ID`),
  CONSTRAINT `FK9FA1CF0D274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK9FA1CF0D2843EE3F` FOREIGN KEY (`ESTIMATE_ID`) REFERENCES `estimate` (`ID`),
  CONSTRAINT `FK9FA1CF0D4C74BEAE` FOREIGN KEY (`SALES_PERSON_ID`) REFERENCES `sales_person` (`ID`),
  CONSTRAINT `FK9FA1CF0D8C6D8B66` FOREIGN KEY (`SALES_ORDER_ID`) REFERENCES `sales_order` (`ID`),
  CONSTRAINT `FK9FA1CF0D97EDD458` FOREIGN KEY (`SHIPPING_TERMS_ID`) REFERENCES `shippingterms` (`ID`),
  CONSTRAINT `FK9FA1CF0D9A3059EC` FOREIGN KEY (`PRICE_LEVEL_ID`) REFERENCES `pricelevel` (`ID`),
  CONSTRAINT `FK9FA1CF0DAD0A95DC` FOREIGN KEY (`SHIPPING_METHOD_ID`) REFERENCES `shippingmethod` (`ID`),
  CONSTRAINT `FK9FA1CF0DAEA76B12` FOREIGN KEY (`PAYMENT_TERMS_ID`) REFERENCES `paymentterms` (`ID`),
  CONSTRAINT `FK9FA1CF0DDFE06A7F` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issuepayment`
--

DROP TABLE IF EXISTS `issuepayment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `issuepayment` (
  `ID` bigint(20) NOT NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  `CHECK_NUMBER` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKE1EF7B0D274BC854` (`ID`),
  KEY `FKE1EF7B0DE5FCF475` (`ACCOUNT_ID`),
  CONSTRAINT `FKE1EF7B0D274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKE1EF7B0DE5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `issuepayment`
--

LOCK TABLES `issuepayment` WRITE;
/*!40000 ALTER TABLE `issuepayment` DISABLE KEYS */;
/*!40000 ALTER TABLE `issuepayment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `item` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TYPE` int(11) default NULL,
  `NAME` varchar(255) NOT NULL,
  `UPC_OR_SKU` varchar(255) default NULL,
  `WEIGHT` int(11) default NULL,
  `ITEMGROUP_ID` bigint(20) default NULL,
  `IS_ACTIVE` bit(1) default NULL,
  `IS_SELL_ITEM` bit(1) default NULL,
  `SALES_DESCRIPTION` varchar(255) default NULL,
  `SALES_PRICE` double default NULL,
  `INCOME_ACCOUNT_ID` bigint(20) default NULL,
  `MIN_STOCK_ALERT_LEVEL` int(11) default NULL,
  `MAX_STOCK_ALERT_LEVEL` int(11) default NULL,
  `MEASUREMENT` bigint(20) default NULL,
  `IS_TAXABLE` bit(1) default NULL,
  `IS_COMMISSION_ITME` bit(1) default NULL,
  `IS_BUY_ITEM` bit(1) default NULL,
  `PURCHASE_DESCRIPTION` varchar(255) default NULL,
  `PURCHASE_PRISE` double default NULL,
  `EXPENSE_ACCOUNT_ID` bigint(20) default NULL,
  `PREFFERED_VENDOR` bigint(20) default NULL,
  `TAX_CODE` bigint(20) default NULL,
  `VENDOR_ITEM_NUMBER` varchar(255) default NULL,
  `SATNDARD_COST` double default NULL,
  `VERSION` int(11) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK227313BD4AF465` (`PREFFERED_VENDOR`),
  KEY `FK227313F1AE8CDE` (`CREATED_BY`),
  KEY `FK227313B7B51814` (`TAX_CODE`),
  KEY `FK2273134545F135` (`ITEMGROUP_ID`),
  KEY `FK227313CF909FD3` (`MEASUREMENT`),
  KEY `FK2273139E5A0E30` (`LAST_MODIFIER`),
  KEY `FK2273136F1934CB` (`INCOME_ACCOUNT_ID`),
  KEY `FK227313E2AA5ABC` (`EXPENSE_ACCOUNT_ID`),
  CONSTRAINT `FK2273134545F135` FOREIGN KEY (`ITEMGROUP_ID`) REFERENCES `itemgroup` (`ID`),
  CONSTRAINT `FK2273136F1934CB` FOREIGN KEY (`INCOME_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK2273139E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK227313B7B51814` FOREIGN KEY (`TAX_CODE`) REFERENCES `tax_code` (`ID`),
  CONSTRAINT `FK227313BD4AF465` FOREIGN KEY (`PREFFERED_VENDOR`) REFERENCES `vendor` (`ID`),
  CONSTRAINT `FK227313CF909FD3` FOREIGN KEY (`MEASUREMENT`) REFERENCES `measurement` (`ID`),
  CONSTRAINT `FK227313E2AA5ABC` FOREIGN KEY (`EXPENSE_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK227313F1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_back_up`
--

DROP TABLE IF EXISTS `item_back_up`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `item_back_up` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TRANSACTION_ITEM_ID` bigint(20) default NULL,
  `ITEM_ID` bigint(20) default NULL,
  `INCOME_ACCOUNT_ID` bigint(20) default NULL,
  `EXPENSE_ACCOUNT_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK1FBD4BC778F5DB52` (`TRANSACTION_ITEM_ID`),
  KEY `FK1FBD4BC71E282CDF` (`ITEM_ID`),
  KEY `FK1FBD4BC76F1934CB` (`INCOME_ACCOUNT_ID`),
  KEY `FK1FBD4BC7E2AA5ABC` (`EXPENSE_ACCOUNT_ID`),
  CONSTRAINT `FK1FBD4BC71E282CDF` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ID`),
  CONSTRAINT `FK1FBD4BC76F1934CB` FOREIGN KEY (`INCOME_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK1FBD4BC778F5DB52` FOREIGN KEY (`TRANSACTION_ITEM_ID`) REFERENCES `transaction_item` (`ID`),
  CONSTRAINT `FK1FBD4BC7E2AA5ABC` FOREIGN KEY (`EXPENSE_ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `item_back_up`
--

LOCK TABLES `item_back_up` WRITE;
/*!40000 ALTER TABLE `item_back_up` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_back_up` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_receipt`
--

DROP TABLE IF EXISTS `item_receipt`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `item_receipt` (
  `ID` bigint(20) NOT NULL,
  `VENDOR_ID` bigint(20) default NULL,
  `SHIP_TO_ID` bigint(20) default NULL,
  `PURCHASE_ORDER_DATE` bigint(20) default NULL,
  `TO_BE_PRINTED` bit(1) default NULL,
  `TO_BE_EMAILED` bit(1) default NULL,
  `EB_IS_PRIMARY` bit(1) default NULL,
  `EB_NAME` varchar(255) default NULL,
  `EB_TITLE` varchar(255) default NULL,
  `EB_BUSINESS_PHONE` varchar(255) default NULL,
  `EB_EMAIL` varchar(255) default NULL,
  `VENDOR_ADDRESS_TYPE` int(11) default NULL,
  `VENDOR_ADDRESS_ADDRESS1` varchar(100) default NULL,
  `VENDOR_ADDRESS_STREET` varchar(255) default NULL,
  `VENDOR_ADDRESS_CITY` varchar(255) default NULL,
  `VENDOR_ADDRESS_STATE` varchar(255) default NULL,
  `VENDOR_ADDRESS_ZIP` varchar(255) default NULL,
  `VENDOR_ADDRESS_COUNTRY` varchar(255) default NULL,
  `VENDOR_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `PHONE` varchar(255) default NULL,
  `PAYMENT_TERM_ID` bigint(20) default NULL,
  `DUE_DATE` bigint(20) default NULL,
  `DELIVERY_DATE` bigint(20) default NULL,
  `SHIPPING_ADDRESS_TYPE` int(11) default NULL,
  `SHIPPING_ADDRESS_ADDRESS1` varchar(100) default NULL,
  `SHIPPING_ADDRESS_STREET` varchar(255) default NULL,
  `SHIPPING_ADDRESS_CITY` varchar(255) default NULL,
  `SHIPPING_ADDRESS_STATE` varchar(255) default NULL,
  `SHIPPING_ADDRESS_ZIP` varchar(255) default NULL,
  `SHIPPING_ADDRESS_COUNTRY` varchar(255) default NULL,
  `SHIPPING_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `SHIPPING_TERMS_ID` bigint(20) default NULL,
  `SHIPPING_METHOD_ID` bigint(20) default NULL,
  `PURCHASE_ORDER_ID` bigint(20) default NULL,
  `IS_BILLED` bit(1) default NULL,
  `BALANCE_DUE` double default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK74F154ECAD0A95DC` (`SHIPPING_METHOD_ID`),
  KEY `FK74F154EC338BD6BB` (`PAYMENT_TERM_ID`),
  KEY `FK74F154EC39750592` (`PURCHASE_ORDER_ID`),
  KEY `FK74F154EC274BC854` (`ID`),
  KEY `FK74F154EC891A177F` (`VENDOR_ID`),
  KEY `FK74F154EC97EDD458` (`SHIPPING_TERMS_ID`),
  KEY `FK74F154EC777205A4` (`SHIP_TO_ID`),
  CONSTRAINT `FK74F154EC274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK74F154EC338BD6BB` FOREIGN KEY (`PAYMENT_TERM_ID`) REFERENCES `paymentterms` (`ID`),
  CONSTRAINT `FK74F154EC39750592` FOREIGN KEY (`PURCHASE_ORDER_ID`) REFERENCES `purchase_order` (`ID`),
  CONSTRAINT `FK74F154EC777205A4` FOREIGN KEY (`SHIP_TO_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK74F154EC891A177F` FOREIGN KEY (`VENDOR_ID`) REFERENCES `vendor` (`ID`),
  CONSTRAINT `FK74F154EC97EDD458` FOREIGN KEY (`SHIPPING_TERMS_ID`) REFERENCES `shippingterms` (`ID`),
  CONSTRAINT `FK74F154ECAD0A95DC` FOREIGN KEY (`SHIPPING_METHOD_ID`) REFERENCES `shippingmethod` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `item_receipt`
--

LOCK TABLES `item_receipt` WRITE;
/*!40000 ALTER TABLE `item_receipt` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_receipt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_status`
--

DROP TABLE IF EXISTS `item_status`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `item_status` (
  `ID` bigint(20) NOT NULL auto_increment,
  `ITEM_ID` bigint(20) default NULL,
  `QTY_VALUE` double default NULL,
  `UNIT` bigint(20) default NULL,
  `WAREHOUSE` bigint(20) NOT NULL,
  `VALUE` double default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK64D429EA041AB0D` (`UNIT`),
  KEY `FK64D429E1E282CDF` (`ITEM_ID`),
  KEY `FK64D429E984DB8A1` (`WAREHOUSE`),
  CONSTRAINT `FK64D429E1E282CDF` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ID`),
  CONSTRAINT `FK64D429E984DB8A1` FOREIGN KEY (`WAREHOUSE`) REFERENCES `warehouse` (`ID`),
  CONSTRAINT `FK64D429EA041AB0D` FOREIGN KEY (`UNIT`) REFERENCES `unit` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `item_status`
--

LOCK TABLES `item_status` WRITE;
/*!40000 ALTER TABLE `item_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itemgroup`
--

DROP TABLE IF EXISTS `itemgroup`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `itemgroup` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `VERSION` int(11) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK26E87F2CF1AE8CDE` (`CREATED_BY`),
  KEY `FK26E87F2C9E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK26E87F2C9E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK26E87F2CF1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `itemgroup`
--

LOCK TABLES `itemgroup` WRITE;
/*!40000 ALTER TABLE `itemgroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `itemgroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `journal_entry`
--

DROP TABLE IF EXISTS `journal_entry`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `journal_entry` (
  `ID` bigint(20) NOT NULL,
  `DEBIT_TOTAL` double default NULL,
  `CREDIT_TOTAL` double default NULL,
  `JOURNAL_ENTRY_TYPE` int(11) default NULL,
  `BALANCE_DUE` double default NULL,
  `TRANSACTION_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKE894C12A274BC854` (`ID`),
  KEY `FKE894C12A63880555` (`TRANSACTION_ID`),
  CONSTRAINT `FKE894C12A274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKE894C12A63880555` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `transaction` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `journal_entry`
--

LOCK TABLES `journal_entry` WRITE;
/*!40000 ALTER TABLE `journal_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `journal_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `make_deposit`
--

DROP TABLE IF EXISTS `make_deposit`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `make_deposit` (
  `ID` bigint(20) NOT NULL,
  `DEPOSIT_IN_ID` bigint(20) default NULL,
  `CASH_BACK_ACCOUNT_ID` bigint(20) default NULL,
  `CASH_BACK_MEMO` varchar(255) default NULL,
  `CASH_BACK_AMOUNT` double default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKBA78792DDEA009C1` (`CASH_BACK_ACCOUNT_ID`),
  KEY `FKBA78792D274BC854` (`ID`),
  KEY `FKBA78792D308D799C` (`DEPOSIT_IN_ID`),
  CONSTRAINT `FKBA78792D274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKBA78792D308D799C` FOREIGN KEY (`DEPOSIT_IN_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FKBA78792DDEA009C1` FOREIGN KEY (`CASH_BACK_ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `make_deposit`
--

LOCK TABLES `make_deposit` WRITE;
/*!40000 ALTER TABLE `make_deposit` DISABLE KEYS */;
/*!40000 ALTER TABLE `make_deposit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measurement`
--

DROP TABLE IF EXISTS `measurement`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `measurement` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `measurement`
--

LOCK TABLES `measurement` WRITE;
/*!40000 ALTER TABLE `measurement` DISABLE KEYS */;
/*!40000 ALTER TABLE `measurement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pay_bill`
--

DROP TABLE IF EXISTS `pay_bill`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `pay_bill` (
  `ID` bigint(20) NOT NULL,
  `PAYFROM_ID` bigint(20) default NULL,
  `BILL_DUE_ONORBEFORE` bigint(20) default NULL,
  `VENDOR_ID` bigint(20) default NULL,
  `IS_TO_BE_PRINTED` bit(1) default NULL,
  `ENDING_BALANCE` double default NULL,
  `UNUSED_AMOUNT` double default NULL,
  `VENDOR_BALANCE` double default NULL,
  `PAYBILL_TYPE` int(11) default NULL,
  `PAYBILL_ADDRESS_TYPE` int(11) default NULL,
  `PAYBILL_ADDRESS_ADDRESS1` varchar(100) default NULL,
  `PAYBILL_ADDRESS_STREET` varchar(255) default NULL,
  `PAYBILL_ADDRESS_CITY` varchar(255) default NULL,
  `PAYBILL_ADDRESS_STATE` varchar(255) default NULL,
  `PAYBILL_ADDRESS_ZIP` varchar(255) default NULL,
  `PAYBILL_ADDRESS_COUNTRY` varchar(255) default NULL,
  `PAYBILL_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `CHECK_NUMBER` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK820C355E19EFDC30` (`PAYFROM_ID`),
  KEY `FK820C355E274BC854` (`ID`),
  KEY `FK820C355E891A177F` (`VENDOR_ID`),
  CONSTRAINT `FK820C355E19EFDC30` FOREIGN KEY (`PAYFROM_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK820C355E274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK820C355E891A177F` FOREIGN KEY (`VENDOR_ID`) REFERENCES `vendor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `pay_bill`
--

LOCK TABLES `pay_bill` WRITE;
/*!40000 ALTER TABLE `pay_bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `pay_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pay_expense`
--

DROP TABLE IF EXISTS `pay_expense`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `pay_expense` (
  `ID` bigint(20) NOT NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  `REFERENCE_OR_CHEQUE_NUMBER` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK83382781274BC854` (`ID`),
  KEY `FK83382781E5FCF475` (`ACCOUNT_ID`),
  CONSTRAINT `FK83382781274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK83382781E5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `pay_expense`
--

LOCK TABLES `pay_expense` WRITE;
/*!40000 ALTER TABLE `pay_expense` DISABLE KEYS */;
/*!40000 ALTER TABLE `pay_expense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pay_sales_tax`
--

DROP TABLE IF EXISTS `pay_sales_tax`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `pay_sales_tax` (
  `ID` bigint(20) NOT NULL,
  `BILLS_DUE_ON_OR_BEFORE` bigint(20) default NULL,
  `PAYFROM_ACCOUNT_ID` bigint(20) default NULL,
  `TAX_AGENCY_ID` bigint(20) default NULL,
  `ENDING_BALANCE` double default NULL,
  `IS_EDITED` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK890292C1274BC854` (`ID`),
  KEY `FK890292C1C368D6AC` (`TAX_AGENCY_ID`),
  KEY `FK890292C168A08E82` (`PAYFROM_ACCOUNT_ID`),
  CONSTRAINT `FK890292C1274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK890292C168A08E82` FOREIGN KEY (`PAYFROM_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK890292C1C368D6AC` FOREIGN KEY (`TAX_AGENCY_ID`) REFERENCES `taxagency` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `pay_sales_tax`
--

LOCK TABLES `pay_sales_tax` WRITE;
/*!40000 ALTER TABLE `pay_sales_tax` DISABLE KEYS */;
/*!40000 ALTER TABLE `pay_sales_tax` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pay_sales_tax_entries`
--

DROP TABLE IF EXISTS `pay_sales_tax_entries`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `pay_sales_tax_entries` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TRANSACTION_ID` bigint(20) default NULL,
  `TAXITEM_ID` bigint(20) default NULL,
  `TAXAGENCY_ID` bigint(20) default NULL,
  `AMOUNT` double default NULL,
  `BALANCE` double default NULL,
  `VERSION` int(11) default NULL,
  `TRANSACTION_DATE` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK8E65F8F2BCD926F5` (`TAXAGENCY_ID`),
  KEY `FK8E65F8F263880555` (`TRANSACTION_ID`),
  KEY `FK8E65F8F2D7293EF5` (`TAXITEM_ID`),
  CONSTRAINT `FK8E65F8F263880555` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK8E65F8F2BCD926F5` FOREIGN KEY (`TAXAGENCY_ID`) REFERENCES `taxagency` (`ID`),
  CONSTRAINT `FK8E65F8F2D7293EF5` FOREIGN KEY (`TAXITEM_ID`) REFERENCES `tax_item` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `pay_sales_tax_entries`
--

LOCK TABLES `pay_sales_tax_entries` WRITE;
/*!40000 ALTER TABLE `pay_sales_tax_entries` DISABLE KEYS */;
/*!40000 ALTER TABLE `pay_sales_tax_entries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pay_vat`
--

DROP TABLE IF EXISTS `pay_vat`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `pay_vat` (
  `ID` bigint(20) NOT NULL,
  `RETURNS_DUE_ON_OR_BEFORE` bigint(20) default NULL,
  `PAYFROM_ACCOUNT_ID` bigint(20) default NULL,
  `TAX_AGENCY_ID` bigint(20) default NULL,
  `ENDING_BALANCE` double default NULL,
  `IS_EDITED` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKFBF02AD2274BC854` (`ID`),
  KEY `FKFBF02AD2C368D6AC` (`TAX_AGENCY_ID`),
  KEY `FKFBF02AD268A08E82` (`PAYFROM_ACCOUNT_ID`),
  CONSTRAINT `FKFBF02AD2274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKFBF02AD268A08E82` FOREIGN KEY (`PAYFROM_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FKFBF02AD2C368D6AC` FOREIGN KEY (`TAX_AGENCY_ID`) REFERENCES `taxagency` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `pay_vat`
--

LOCK TABLES `pay_vat` WRITE;
/*!40000 ALTER TABLE `pay_vat` DISABLE KEYS */;
/*!40000 ALTER TABLE `pay_vat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pay_vat_entries`
--

DROP TABLE IF EXISTS `pay_vat_entries`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `pay_vat_entries` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TRANSACTION_ID` bigint(20) default NULL,
  `TAXCODE_ID` bigint(20) default NULL,
  `TAXAGENCY_ID` bigint(20) default NULL,
  `AMOUNT` double default NULL,
  `BALANCE` double default NULL,
  `VERSION` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK3EC6F003BCD926F5` (`TAXAGENCY_ID`),
  KEY `FK3EC6F003912718B5` (`TAXCODE_ID`),
  KEY `FK3EC6F00363880555` (`TRANSACTION_ID`),
  CONSTRAINT `FK3EC6F00363880555` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK3EC6F003912718B5` FOREIGN KEY (`TAXCODE_ID`) REFERENCES `tax_code` (`ID`),
  CONSTRAINT `FK3EC6F003BCD926F5` FOREIGN KEY (`TAXAGENCY_ID`) REFERENCES `taxagency` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `pay_vat_entries`
--

LOCK TABLES `pay_vat_entries` WRITE;
/*!40000 ALTER TABLE `pay_vat_entries` DISABLE KEYS */;
/*!40000 ALTER TABLE `pay_vat_entries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payee`
--

DROP TABLE IF EXISTS `payee`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `payee` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `PAYEE_SINCE` bigint(20) default NULL,
  `FILE_AS` varchar(255) default NULL,
  `VERSION` int(11) default NULL,
  `TYPE` int(11) default NULL,
  `P_DATE` bigint(20) default NULL,
  `BALANCE` double default NULL,
  `OPENING_BALANCE` double default NULL,
  `PAYMENT_METHOD` varchar(255) default NULL,
  `PHONE_NUMBER` varchar(255) default NULL,
  `FAX_NUMBER` varchar(255) default NULL,
  `EMAIL_ID` varchar(255) default NULL,
  `WEB_PAGE_ADDRESS` varchar(255) default NULL,
  `IS_ACTIVE` bit(1) default NULL,
  `MEMO` varchar(255) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `VAT_REGISTRATION_NUMBER` varchar(255) default NULL,
  `TAX_CODE_ID` bigint(20) default NULL,
  `IS_OPENING_BALANCE_EDITABLE` bit(1) default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  `PAN_NUMBER` varchar(255) default NULL,
  `CST_NUMBER` varchar(255) default NULL,
  `SERVICE_TAX_REGISTRATION_NUMBER` varchar(255) default NULL,
  `TIN_NUMBER` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK4863B28F1AE8CDE` (`CREATED_BY`),
  KEY `FK4863B28147D4A2C` (`TAX_CODE_ID`),
  KEY `FK4863B289E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK4863B28147D4A2C` FOREIGN KEY (`TAX_CODE_ID`) REFERENCES `tax_code` (`ID`),
  CONSTRAINT `FK4863B289E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK4863B28F1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `payee`
--

LOCK TABLES `payee` WRITE;
/*!40000 ALTER TABLE `payee` DISABLE KEYS */;
/*!40000 ALTER TABLE `payee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payee_address`
--

DROP TABLE IF EXISTS `payee_address`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `payee_address` (
  `PAYEE_ID` bigint(20) NOT NULL,
  `TYPE` int(11) NOT NULL,
  `ADDRESS1` varchar(100) NOT NULL,
  `STREET` varchar(100) NOT NULL,
  `CITY` varchar(100) NOT NULL,
  `STATE` varchar(100) NOT NULL,
  `ZIP` varchar(100) NOT NULL,
  `COUNTRY` varchar(100) NOT NULL,
  `IS_SELECTED` bit(1) NOT NULL,
  PRIMARY KEY  (`PAYEE_ID`,`TYPE`,`ADDRESS1`,`STREET`,`CITY`,`STATE`,`ZIP`,`COUNTRY`,`IS_SELECTED`),
  KEY `FK81BF61BDB2FC5555` (`PAYEE_ID`),
  CONSTRAINT `FK81BF61BDB2FC5555` FOREIGN KEY (`PAYEE_ID`) REFERENCES `payee` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `payee_address`
--

LOCK TABLES `payee_address` WRITE;
/*!40000 ALTER TABLE `payee_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `payee_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payee_contact`
--

DROP TABLE IF EXISTS `payee_contact`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `payee_contact` (
  `PAYEE_ID` bigint(20) NOT NULL,
  `IS_PRIMARY` bit(1) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `TITLE` varchar(100) NOT NULL,
  `BUSINESS_PHONE` varchar(100) NOT NULL,
  `EMAIL` varchar(255) NOT NULL,
  PRIMARY KEY  (`PAYEE_ID`,`IS_PRIMARY`,`NAME`,`TITLE`,`BUSINESS_PHONE`,`EMAIL`),
  KEY `FKFEDEE9E9B2FC5555` (`PAYEE_ID`),
  CONSTRAINT `FKFEDEE9E9B2FC5555` FOREIGN KEY (`PAYEE_ID`) REFERENCES `payee` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `payee_contact`
--

LOCK TABLES `payee_contact` WRITE;
/*!40000 ALTER TABLE `payee_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `payee_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paymentterms`
--

DROP TABLE IF EXISTS `paymentterms`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `paymentterms` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `DUE` int(11) default NULL,
  `DUE_DAYS` int(11) default NULL,
  `DISCOUNT_PERCENT` double default NULL,
  `IF_PAID_WITHIN` int(11) default NULL,
  `VERSION` int(11) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK6DA15341F1AE8CDE` (`CREATED_BY`),
  KEY `FK6DA153419E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK6DA153419E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK6DA15341F1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `paymentterms`
--

LOCK TABLES `paymentterms` WRITE;
/*!40000 ALTER TABLE `paymentterms` DISABLE KEYS */;
/*!40000 ALTER TABLE `paymentterms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pricelevel`
--

DROP TABLE IF EXISTS `pricelevel`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `pricelevel` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `PERCENTAGE` double default NULL,
  `IS_P_DECREASE_BY_THIS_PERCENT` bit(1) default NULL,
  `VERSION` int(11) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FKC6643BDBF1AE8CDE` (`CREATED_BY`),
  KEY `FKC6643BDB9E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FKC6643BDB9E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FKC6643BDBF1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `pricelevel`
--

LOCK TABLES `pricelevel` WRITE;
/*!40000 ALTER TABLE `pricelevel` DISABLE KEYS */;
/*!40000 ALTER TABLE `pricelevel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order`
--

DROP TABLE IF EXISTS `purchase_order`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `purchase_order` (
  `ID` bigint(20) NOT NULL,
  `VENDOR_ID` bigint(20) default NULL,
  `SHIP_TO_ID` bigint(20) default NULL,
  `PURCHASE_ORDER_DATE` bigint(20) default NULL,
  `TO_BE_PRINTED` bit(1) default NULL,
  `TO_BE_EMAILED` bit(1) default NULL,
  `EB_IS_PRIMARY` bit(1) default NULL,
  `EB_NAME` varchar(255) default NULL,
  `EB_TITLE` varchar(255) default NULL,
  `EB_BUSINESS_PHONE` varchar(255) default NULL,
  `EB_EMAIL` varchar(255) default NULL,
  `VENDOR_ADDRESS_TYPE` int(11) default NULL,
  `VENDOR_ADDRESS_ADDRESS1` varchar(100) default NULL,
  `VENDOR_ADDRESS_STREET` varchar(255) default NULL,
  `VENDOR_ADDRESS_CITY` varchar(255) default NULL,
  `VENDOR_ADDRESS_STATE` varchar(255) default NULL,
  `VENDOR_ADDRESS_ZIP` varchar(255) default NULL,
  `VENDOR_ADDRESS_COUNTRY` varchar(255) default NULL,
  `VENDOR_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `PHONE` varchar(255) default NULL,
  `PAYMENT_TERM_ID` bigint(20) default NULL,
  `DUE_DATE` bigint(20) default NULL,
  `DELIVERY_DATE` bigint(20) default NULL,
  `DISPATCH_DATE` bigint(20) default NULL,
  `SHIPPING_ADDRESS_TYPE` int(11) default NULL,
  `SHIPPING_ADDRESS_ADDRESS1` varchar(100) default NULL,
  `SHIPPING_ADDRESS_STREET` varchar(255) default NULL,
  `SHIPPING_ADDRESS_CITY` varchar(255) default NULL,
  `SHIPPING_ADDRESS_STATE` varchar(255) default NULL,
  `SHIPPING_ADDRESS_ZIP` varchar(255) default NULL,
  `SHIPPING_ADDRESS_COUNTRY` varchar(255) default NULL,
  `SHIPPING_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `SHIPPING_TERMS_ID` bigint(20) default NULL,
  `SHIPPING_METHOD_ID` bigint(20) default NULL,
  `PURCHASE_ORDER_NUMBER` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK26456270AD0A95DC` (`SHIPPING_METHOD_ID`),
  KEY `FK26456270338BD6BB` (`PAYMENT_TERM_ID`),
  KEY `FK26456270274BC854` (`ID`),
  KEY `FK26456270891A177F` (`VENDOR_ID`),
  KEY `FK2645627097EDD458` (`SHIPPING_TERMS_ID`),
  KEY `FK26456270777205A4` (`SHIP_TO_ID`),
  CONSTRAINT `FK26456270274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK26456270338BD6BB` FOREIGN KEY (`PAYMENT_TERM_ID`) REFERENCES `paymentterms` (`ID`),
  CONSTRAINT `FK26456270777205A4` FOREIGN KEY (`SHIP_TO_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK26456270891A177F` FOREIGN KEY (`VENDOR_ID`) REFERENCES `vendor` (`ID`),
  CONSTRAINT `FK2645627097EDD458` FOREIGN KEY (`SHIPPING_TERMS_ID`) REFERENCES `shippingterms` (`ID`),
  CONSTRAINT `FK26456270AD0A95DC` FOREIGN KEY (`SHIPPING_METHOD_ID`) REFERENCES `shippingmethod` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `purchase_order`
--

LOCK TABLES `purchase_order` WRITE;
/*!40000 ALTER TABLE `purchase_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receive_payment`
--

DROP TABLE IF EXISTS `receive_payment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `receive_payment` (
  `ID` bigint(20) NOT NULL,
  `CUSTOMER_ID` bigint(20) default NULL,
  `AMOUNT` double default NULL,
  `CUSTOMER_BALANCE` double default NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  `UNUSED_CREDITS` double default NULL,
  `UNUSED_PAYMENTS` double default NULL,
  `TOTAL_CASH_DISCOUNT` double default NULL,
  `TOTAL_WRITE_OFF` double default NULL,
  `TOTAL_APPLIED_CREDITS` double default NULL,
  `CREDITS_AND_PAYMENTS_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK2D07F06AAC60FBCF` (`CREDITS_AND_PAYMENTS_ID`),
  KEY `FK2D07F06A274BC854` (`ID`),
  KEY `FK2D07F06AE5FCF475` (`ACCOUNT_ID`),
  KEY `FK2D07F06ADFE06A7F` (`CUSTOMER_ID`),
  CONSTRAINT `FK2D07F06A274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK2D07F06AAC60FBCF` FOREIGN KEY (`CREDITS_AND_PAYMENTS_ID`) REFERENCES `credits_and_payments` (`ID`),
  CONSTRAINT `FK2D07F06ADFE06A7F` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`ID`),
  CONSTRAINT `FK2D07F06AE5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `receive_payment`
--

LOCK TABLES `receive_payment` WRITE;
/*!40000 ALTER TABLE `receive_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `receive_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receive_vat`
--

DROP TABLE IF EXISTS `receive_vat`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `receive_vat` (
  `ID` bigint(20) NOT NULL,
  `RETURNS_DUE_ON_OR_BEFORE` bigint(20) default NULL,
  `DEPOSIT_IN_ACCOUNT_ID` bigint(20) default NULL,
  `TAX_AGENCY_ID` bigint(20) default NULL,
  `ENDING_BALANCE` double default NULL,
  `IS_EDITED` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK3075F8D274BC854` (`ID`),
  KEY `FK3075F8DC368D6AC` (`TAX_AGENCY_ID`),
  KEY `FK3075F8D172FFFEE` (`DEPOSIT_IN_ACCOUNT_ID`),
  CONSTRAINT `FK3075F8D172FFFEE` FOREIGN KEY (`DEPOSIT_IN_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK3075F8D274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK3075F8DC368D6AC` FOREIGN KEY (`TAX_AGENCY_ID`) REFERENCES `taxagency` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `receive_vat`
--

LOCK TABLES `receive_vat` WRITE;
/*!40000 ALTER TABLE `receive_vat` DISABLE KEYS */;
/*!40000 ALTER TABLE `receive_vat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reset_password_token`
--

DROP TABLE IF EXISTS `reset_password_token`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reset_password_token` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TOKEN` varchar(255) default NULL,
  `USER_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `reset_password_token`
--

LOCK TABLES `reset_password_token` WRITE;
/*!40000 ALTER TABLE `reset_password_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `reset_password_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_order`
--

DROP TABLE IF EXISTS `sales_order`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sales_order` (
  `ID` bigint(20) NOT NULL,
  `CUSTOMER_ID` bigint(20) default NULL,
  `INVOICE_IS_PRIMARY` bit(1) default NULL,
  `INVOICE_NAME` varchar(255) default NULL,
  `INVOICE_TITLE` varchar(255) default NULL,
  `INVOICE_BUSINESS_PHONE` varchar(255) default NULL,
  `INVOICE_EMAIL` varchar(255) default NULL,
  `BILLING_ADDRESS_TYPE` int(11) default NULL,
  `BILLING_ADDRESS1` varchar(100) default NULL,
  `BILLING_ADDRESS_STREET` varchar(255) default NULL,
  `BILLING_ADDRESS_CITY` varchar(255) default NULL,
  `BILLING_ADDRESS_STATE` varchar(255) default NULL,
  `BILLING_ADDRESS_ZIP` varchar(255) default NULL,
  `BILLING_ADDRESS_COUNTRY` varchar(255) default NULL,
  `BILLING_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `SHIPPING_ADDRESS_TYPE` int(11) default NULL,
  `SHIPPING_ADDRESS1` varchar(100) default NULL,
  `SHIPPING_ADDRESS_STREET` varchar(255) default NULL,
  `SHIPPING_ADDRESS_CITY` varchar(255) default NULL,
  `SHIPPING_ADDRESS_STATE` varchar(255) default NULL,
  `SHIPPING_ADDRESS_ZIP` varchar(255) default NULL,
  `SHIPPING_ADDRESS_COUNTRY` varchar(255) default NULL,
  `SHIPPING_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `PHONE` varchar(255) default NULL,
  `SALES_PERSON_ID` bigint(20) default NULL,
  `PAYMENT_TERMS_ID` bigint(20) default NULL,
  `SHIPPING_TERMS_ID` bigint(20) default NULL,
  `SHIPPING_METHOD_ID` bigint(20) default NULL,
  `DUE_DATE` bigint(20) default NULL,
  `DISCOUNT_TOTAL` double default NULL,
  `PRICE_LEVEL_ID` bigint(20) default NULL,
  `SALES_TAX_AMOUNT` double default NULL,
  `ITEM_ID` bigint(20) default NULL,
  `ESTIMATE_ID` bigint(20) default NULL,
  `CUSTOMER_ORDER_NUMBER` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK1E0EE21BAD0A95DC` (`SHIPPING_METHOD_ID`),
  KEY `FK1E0EE21B274BC854` (`ID`),
  KEY `FK1E0EE21B1E282CDF` (`ITEM_ID`),
  KEY `FK1E0EE21B9A3059EC` (`PRICE_LEVEL_ID`),
  KEY `FK1E0EE21B97EDD458` (`SHIPPING_TERMS_ID`),
  KEY `FK1E0EE21B4C74BEAE` (`SALES_PERSON_ID`),
  KEY `FK1E0EE21B2843EE3F` (`ESTIMATE_ID`),
  KEY `FK1E0EE21BAEA76B12` (`PAYMENT_TERMS_ID`),
  KEY `FK1E0EE21BDFE06A7F` (`CUSTOMER_ID`),
  CONSTRAINT `FK1E0EE21B1E282CDF` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ID`),
  CONSTRAINT `FK1E0EE21B274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK1E0EE21B2843EE3F` FOREIGN KEY (`ESTIMATE_ID`) REFERENCES `estimate` (`ID`),
  CONSTRAINT `FK1E0EE21B4C74BEAE` FOREIGN KEY (`SALES_PERSON_ID`) REFERENCES `sales_person` (`ID`),
  CONSTRAINT `FK1E0EE21B97EDD458` FOREIGN KEY (`SHIPPING_TERMS_ID`) REFERENCES `shippingterms` (`ID`),
  CONSTRAINT `FK1E0EE21B9A3059EC` FOREIGN KEY (`PRICE_LEVEL_ID`) REFERENCES `pricelevel` (`ID`),
  CONSTRAINT `FK1E0EE21BAD0A95DC` FOREIGN KEY (`SHIPPING_METHOD_ID`) REFERENCES `shippingmethod` (`ID`),
  CONSTRAINT `FK1E0EE21BAEA76B12` FOREIGN KEY (`PAYMENT_TERMS_ID`) REFERENCES `paymentterms` (`ID`),
  CONSTRAINT `FK1E0EE21BDFE06A7F` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sales_order`
--

LOCK TABLES `sales_order` WRITE;
/*!40000 ALTER TABLE `sales_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_person`
--

DROP TABLE IF EXISTS `sales_person`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sales_person` (
  `ID` bigint(20) NOT NULL,
  `TITLE` varchar(255) default NULL,
  `FIRST_NAME` varchar(255) default NULL,
  `MIDDLE_NAME1` varchar(255) default NULL,
  `MIDDLE_NAME2` varchar(255) default NULL,
  `MIDDLE_NAME3` varchar(255) default NULL,
  `LAST_NAME` varchar(255) default NULL,
  `SUFFIX` varchar(255) default NULL,
  `JOB_TITLE` varchar(255) default NULL,
  `EXPENSE_ACCOUNT_ID` bigint(20) default NULL,
  `GENDER` varchar(255) default NULL,
  `DATE_OF_BIRTH` bigint(20) default NULL,
  `DATE_OF_HIRE` bigint(20) default NULL,
  `DATE_OF_LASTREVIEW` bigint(20) default NULL,
  `DATE_OF_RELEASE` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKA4D19A08610348FE` (`ID`),
  KEY `FKA4D19A08E2AA5ABC` (`EXPENSE_ACCOUNT_ID`),
  CONSTRAINT `FKA4D19A08610348FE` FOREIGN KEY (`ID`) REFERENCES `payee` (`ID`),
  CONSTRAINT `FKA4D19A08E2AA5ABC` FOREIGN KEY (`EXPENSE_ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sales_person`
--

LOCK TABLES `sales_person` WRITE;
/*!40000 ALTER TABLE `sales_person` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server_company`
--

DROP TABLE IF EXISTS `server_company`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `server_company` (
  `ID` bigint(20) NOT NULL auto_increment,
  `COMPANY_NAME` varchar(255) default NULL,
  `CREATED_DATE` datetime default NULL,
  `COMPANY_TYPE` int(11) default NULL,
  `SERVER_ADDRESS` varchar(255) default NULL,
  `IS_CONFIGURED` bit(1) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `server_company`
--

LOCK TABLES `server_company` WRITE;
/*!40000 ALTER TABLE `server_company` DISABLE KEYS */;
INSERT INTO `server_company` VALUES (1,'vimukti',NULL,1,NULL,'\0'),(2,'accounter234',NULL,1,NULL,'\0');
/*!40000 ALTER TABLE `server_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shippingmethod`
--

DROP TABLE IF EXISTS `shippingmethod`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `shippingmethod` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `VERSION` int(11) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK8128180FF1AE8CDE` (`CREATED_BY`),
  KEY `FK8128180F9E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK8128180F9E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK8128180FF1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `shippingmethod`
--

LOCK TABLES `shippingmethod` WRITE;
/*!40000 ALTER TABLE `shippingmethod` DISABLE KEYS */;
/*!40000 ALTER TABLE `shippingmethod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shippingterms`
--

DROP TABLE IF EXISTS `shippingterms`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `shippingterms` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `VERSION` int(11) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FKDB42E079F1AE8CDE` (`CREATED_BY`),
  KEY `FKDB42E0799E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FKDB42E0799E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FKDB42E079F1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `shippingterms`
--

LOCK TABLES `shippingterms` WRITE;
/*!40000 ALTER TABLE `shippingterms` DISABLE KEYS */;
/*!40000 ALTER TABLE `shippingterms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_adjustment`
--

DROP TABLE IF EXISTS `stock_adjustment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `stock_adjustment` (
  `ID` bigint(20) NOT NULL,
  `COMPLETED` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKD2B15A56274BC854` (`ID`),
  CONSTRAINT `FKD2B15A56274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `stock_adjustment`
--

LOCK TABLES `stock_adjustment` WRITE;
/*!40000 ALTER TABLE `stock_adjustment` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock_adjustment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_adjustment_item`
--

DROP TABLE IF EXISTS `stock_adjustment_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `stock_adjustment_item` (
  `ID` bigint(20) NOT NULL auto_increment,
  `ADJUSTMENT_PRICE_VALUE` double default NULL,
  `ADJUSTMENT_QTY` double default NULL,
  `COMMENT` varchar(255) default NULL,
  `ITEM` bigint(20) default NULL,
  `QTY_BEFORE_TRANSACTION` double default NULL,
  `REASON` bigint(20) default NULL,
  `WAREHOUSE` bigint(20) default NULL,
  `STOCK_ADJUSTMENT` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK6034D75CA036EE2B` (`ITEM`),
  KEY `FK6034D75CF58BC234` (`STOCK_ADJUSTMENT`),
  KEY `FK6034D75C8F9890DA` (`REASON`),
  KEY `FK6034D75C984DB8A1` (`WAREHOUSE`),
  CONSTRAINT `FK6034D75C8F9890DA` FOREIGN KEY (`REASON`) REFERENCES `adjustment_reason` (`ID`),
  CONSTRAINT `FK6034D75C984DB8A1` FOREIGN KEY (`WAREHOUSE`) REFERENCES `warehouse` (`ID`),
  CONSTRAINT `FK6034D75CA036EE2B` FOREIGN KEY (`ITEM`) REFERENCES `item` (`ID`),
  CONSTRAINT `FK6034D75CF58BC234` FOREIGN KEY (`STOCK_ADJUSTMENT`) REFERENCES `stock_adjustment` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `stock_adjustment_item`
--

LOCK TABLES `stock_adjustment_item` WRITE;
/*!40000 ALTER TABLE `stock_adjustment_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock_adjustment_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_transfer`
--

DROP TABLE IF EXISTS `stock_transfer`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `stock_transfer` (
  `ID` bigint(20) NOT NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_ON` datetime default NULL,
  `LAST_MODIFIED_ON` datetime default NULL,
  `FROM_WAREHOUSE` bigint(20) default NULL,
  `TO_WAREHOUSE` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKE77B4F14F1AE8CDE` (`CREATED_BY`),
  KEY `FKE77B4F142FACBE4C` (`FROM_WAREHOUSE`),
  KEY `FKE77B4F144B3723DD` (`TO_WAREHOUSE`),
  KEY `FKE77B4F149E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FKE77B4F142FACBE4C` FOREIGN KEY (`FROM_WAREHOUSE`) REFERENCES `warehouse` (`ID`),
  CONSTRAINT `FKE77B4F144B3723DD` FOREIGN KEY (`TO_WAREHOUSE`) REFERENCES `warehouse` (`ID`),
  CONSTRAINT `FKE77B4F149E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FKE77B4F14F1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `stock_transfer`
--

LOCK TABLES `stock_transfer` WRITE;
/*!40000 ALTER TABLE `stock_transfer` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock_transfer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax_adjustment`
--

DROP TABLE IF EXISTS `tax_adjustment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tax_adjustment` (
  `ID` bigint(20) NOT NULL,
  `INCREASE_VAT_LINE` bit(1) default NULL,
  `IS_FILED` bit(1) default NULL,
  `ADJUSTMENT_ACCOUNT` bigint(20) default NULL,
  `TAX_ITEM` bigint(20) default NULL,
  `TAX_AGENCY_ID` bigint(20) default NULL,
  `JOURNAL_ENTRY` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK24BBD6A187003963` (`ADJUSTMENT_ACCOUNT`),
  KEY `FK24BBD6A1274BC854` (`ID`),
  KEY `FK24BBD6A1C368D6AC` (`TAX_AGENCY_ID`),
  KEY `FK24BBD6A11AF90BEA` (`JOURNAL_ENTRY`),
  KEY `FK24BBD6A1B7BAB260` (`TAX_ITEM`),
  CONSTRAINT `FK24BBD6A11AF90BEA` FOREIGN KEY (`JOURNAL_ENTRY`) REFERENCES `journal_entry` (`ID`),
  CONSTRAINT `FK24BBD6A1274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK24BBD6A187003963` FOREIGN KEY (`ADJUSTMENT_ACCOUNT`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK24BBD6A1B7BAB260` FOREIGN KEY (`TAX_ITEM`) REFERENCES `tax_item` (`ID`),
  CONSTRAINT `FK24BBD6A1C368D6AC` FOREIGN KEY (`TAX_AGENCY_ID`) REFERENCES `taxagency` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tax_adjustment`
--

LOCK TABLES `tax_adjustment` WRITE;
/*!40000 ALTER TABLE `tax_adjustment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_adjustment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax_code`
--

DROP TABLE IF EXISTS `tax_code`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tax_code` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `ISTAXABLE` bit(1) default NULL,
  `ISACTIVE` bit(1) default NULL,
  `IS_EC_SALES_ENTRY` bit(1) default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  `TAXITEMGROUP_PURCHASES` bigint(20) default NULL,
  `TAXITEMGROUP_SALES` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK1FDC9A212D1A5F9A` (`TAXITEMGROUP_PURCHASES`),
  KEY `FK1FDC9A21BC51FE14` (`TAXITEMGROUP_SALES`),
  CONSTRAINT `FK1FDC9A212D1A5F9A` FOREIGN KEY (`TAXITEMGROUP_PURCHASES`) REFERENCES `tax_item_groups` (`ID`),
  CONSTRAINT `FK1FDC9A21BC51FE14` FOREIGN KEY (`TAXITEMGROUP_SALES`) REFERENCES `tax_item_groups` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tax_code`
--

LOCK TABLES `tax_code` WRITE;
/*!40000 ALTER TABLE `tax_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax_group`
--

DROP TABLE IF EXISTS `tax_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tax_group` (
  `ID` bigint(20) NOT NULL,
  `GROUP_RATE` double default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKDBF090AB880103E1` (`ID`),
  CONSTRAINT `FKDBF090AB880103E1` FOREIGN KEY (`ID`) REFERENCES `tax_item_groups` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tax_group`
--

LOCK TABLES `tax_group` WRITE;
/*!40000 ALTER TABLE `tax_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax_group_tax_item`
--

DROP TABLE IF EXISTS `tax_group_tax_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tax_group_tax_item` (
  `TAX_GROUP_ID` bigint(20) NOT NULL,
  `TAX_ITEM_ID` bigint(20) NOT NULL,
  `VTX` int(11) NOT NULL,
  PRIMARY KEY  (`TAX_GROUP_ID`,`VTX`),
  KEY `FK3404985BCD4BA408` (`TAX_GROUP_ID`),
  KEY `FK3404985B5A7F706C` (`TAX_ITEM_ID`),
  CONSTRAINT `FK3404985B5A7F706C` FOREIGN KEY (`TAX_ITEM_ID`) REFERENCES `tax_item` (`ID`),
  CONSTRAINT `FK3404985BCD4BA408` FOREIGN KEY (`TAX_GROUP_ID`) REFERENCES `tax_group` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tax_group_tax_item`
--

LOCK TABLES `tax_group_tax_item` WRITE;
/*!40000 ALTER TABLE `tax_group_tax_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_group_tax_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax_item`
--

DROP TABLE IF EXISTS `tax_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tax_item` (
  `ID` bigint(20) NOT NULL,
  `TAX_AGENCY` bigint(20) default NULL,
  `TAX_RATE` double default NULL,
  `VAT_RETURN_BOX` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK1FDF674798EAF569` (`VAT_RETURN_BOX`),
  KEY `FK1FDF6747880103E1` (`ID`),
  KEY `FK1FDF6747971BCDC4` (`TAX_AGENCY`),
  CONSTRAINT `FK1FDF6747880103E1` FOREIGN KEY (`ID`) REFERENCES `tax_item_groups` (`ID`),
  CONSTRAINT `FK1FDF6747971BCDC4` FOREIGN KEY (`TAX_AGENCY`) REFERENCES `taxagency` (`ID`),
  CONSTRAINT `FK1FDF674798EAF569` FOREIGN KEY (`VAT_RETURN_BOX`) REFERENCES `vatreturnbox` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tax_item`
--

LOCK TABLES `tax_item` WRITE;
/*!40000 ALTER TABLE `tax_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax_item_groups`
--

DROP TABLE IF EXISTS `tax_item_groups`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tax_item_groups` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `IS_ACTIVE` bit(1) default NULL,
  `IS_SALES_TYPE` bit(1) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_PERCENTAGE` bit(1) default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK9F02F5CCF1AE8CDE` (`CREATED_BY`),
  KEY `FK9F02F5CC9E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK9F02F5CC9E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK9F02F5CCF1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tax_item_groups`
--

LOCK TABLES `tax_item_groups` WRITE;
/*!40000 ALTER TABLE `tax_item_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_item_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax_rate_calculation`
--

DROP TABLE IF EXISTS `tax_rate_calculation`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tax_rate_calculation` (
  `ID` bigint(20) NOT NULL auto_increment,
  `VAT_GROUP_ENTRY` bit(1) default NULL,
  `VAT_AMOUNT` double default NULL,
  `LINE_TOTAL` double default NULL,
  `TAX_DUE` double default NULL,
  `TRANSACTION_ITEM_ID` bigint(20) default NULL,
  `TAX_ITEM_ID` bigint(20) default NULL,
  `TRANSACTION_DATE` bigint(20) default NULL,
  `TAX_AGENCY_ID` bigint(20) default NULL,
  `RATE` double default NULL,
  `VAT_RETURN_BOX_ID` bigint(20) default NULL,
  `PURCHASE_LIABILITY_ACCOUNT_ID` bigint(20) default NULL,
  `SALES_LIABILITY_ACCOUNT_ID` bigint(20) default NULL,
  `VAT_RETURN` bigint(20) default NULL,
  `TAX_RATE_CALCULATION_ID` bigint(20) default NULL,
  `VRC` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK559C0BFE84D8B222` (`TAX_RATE_CALCULATION_ID`),
  KEY `FK559C0BFE6EF42FDA` (`VAT_RETURN`),
  KEY `FK559C0BFE941A7BBF` (`VAT_RETURN_BOX_ID`),
  KEY `FK559C0BFE78F5DB52` (`TRANSACTION_ITEM_ID`),
  KEY `FK559C0BFEC368D6AC` (`TAX_AGENCY_ID`),
  KEY `FK559C0BFE5A7F706C` (`TAX_ITEM_ID`),
  KEY `FK559C0BFEFA01503A` (`SALES_LIABILITY_ACCOUNT_ID`),
  KEY `FK559C0BFE1A90F65` (`PURCHASE_LIABILITY_ACCOUNT_ID`),
  CONSTRAINT `FK559C0BFE1A90F65` FOREIGN KEY (`PURCHASE_LIABILITY_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK559C0BFE5A7F706C` FOREIGN KEY (`TAX_ITEM_ID`) REFERENCES `tax_item` (`ID`),
  CONSTRAINT `FK559C0BFE6EF42FDA` FOREIGN KEY (`VAT_RETURN`) REFERENCES `vat_return` (`ID`),
  CONSTRAINT `FK559C0BFE78F5DB52` FOREIGN KEY (`TRANSACTION_ITEM_ID`) REFERENCES `transaction_item` (`ID`),
  CONSTRAINT `FK559C0BFE84D8B222` FOREIGN KEY (`TAX_RATE_CALCULATION_ID`) REFERENCES `box` (`ID`),
  CONSTRAINT `FK559C0BFE941A7BBF` FOREIGN KEY (`VAT_RETURN_BOX_ID`) REFERENCES `vatreturnbox` (`ID`),
  CONSTRAINT `FK559C0BFEC368D6AC` FOREIGN KEY (`TAX_AGENCY_ID`) REFERENCES `taxagency` (`ID`),
  CONSTRAINT `FK559C0BFEFA01503A` FOREIGN KEY (`SALES_LIABILITY_ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tax_rate_calculation`
--

LOCK TABLES `tax_rate_calculation` WRITE;
/*!40000 ALTER TABLE `tax_rate_calculation` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_rate_calculation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taxagency`
--

DROP TABLE IF EXISTS `taxagency`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `taxagency` (
  `ID` bigint(20) NOT NULL,
  `PAYMENT_TERM` bigint(20) default NULL,
  `SALES_ACCOUNT_ID` bigint(20) default NULL,
  `PURCHASE_ACCOUNT_ID` bigint(20) default NULL,
  `VAT_RETURN` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKA8B93F50610348FE` (`ID`),
  KEY `FKA8B93F505F952188` (`SALES_ACCOUNT_ID`),
  KEY `FKA8B93F50633ECA73` (`PURCHASE_ACCOUNT_ID`),
  KEY `FKA8B93F5011D2BD2B` (`PAYMENT_TERM`),
  CONSTRAINT `FKA8B93F5011D2BD2B` FOREIGN KEY (`PAYMENT_TERM`) REFERENCES `paymentterms` (`ID`),
  CONSTRAINT `FKA8B93F505F952188` FOREIGN KEY (`SALES_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FKA8B93F50610348FE` FOREIGN KEY (`ID`) REFERENCES `payee` (`ID`),
  CONSTRAINT `FKA8B93F50633ECA73` FOREIGN KEY (`PURCHASE_ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `taxagency`
--

LOCK TABLES `taxagency` WRITE;
/*!40000 ALTER TABLE `taxagency` DISABLE KEYS */;
/*!40000 ALTER TABLE `taxagency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taxrates`
--

DROP TABLE IF EXISTS `taxrates`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `taxrates` (
  `ID` bigint(20) NOT NULL auto_increment,
  `RATE` double default NULL,
  `AS_OF` bigint(20) default NULL,
  `VERSION` int(11) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `taxrates`
--

LOCK TABLES `taxrates` WRITE;
/*!40000 ALTER TABLE `taxrates` DISABLE KEYS */;
/*!40000 ALTER TABLE `taxrates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction` (
  `ID` bigint(20) NOT NULL auto_increment,
  `VERSION` int(11) default NULL,
  `T_DATE` bigint(20) default NULL,
  `T_TYPE` int(11) default NULL,
  `NUMBER` varchar(255) default NULL,
  `IS_VOID` bit(1) default NULL,
  `PAYMENT_METHOD` varchar(255) default NULL,
  `MEMO` varchar(255) default NULL,
  `REFERENCE` varchar(255) default NULL,
  `SUB_TOTAL` double default NULL,
  `TOTAL_TAXABLE_AMOUNT` double default NULL,
  `TOTAL_NON_TAXABLE_AMOUNT` double default NULL,
  `AMOUNTS_INCLUDE_VAT` bit(1) default NULL,
  `STATUS` int(11) default NULL,
  `CAN_VOID_OR_EDIT` bit(1) default NULL,
  `IS_DEPOSITED` bit(1) default NULL,
  `TOTAL` double default NULL,
  `NET_AMOUNT` double default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` bigint(20) default NULL,
  `LAST_MODIFIED_DATE` bigint(20) default NULL,
  `CURRENCY_CODE` varchar(255) default NULL,
  `CURRENCY_FACTOR` double default NULL,
  `CREDITS_AND_PAYMENTS_ID` bigint(20) default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  `TRANSACTION_MAKE_DEPOSIT_ENTRY_ID` bigint(20) default NULL,
  `FIXED_ASSET_ID` bigint(20) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKFFF466BEAC60FBCF` (`CREDITS_AND_PAYMENTS_ID`),
  KEY `FKFFF466BE7AB4E4D2` (`TRANSACTION_MAKE_DEPOSIT_ENTRY_ID`),
  KEY `FKFFF466BEF1AE8CDE` (`CREATED_BY`),
  KEY `FKFFF466BE33118956` (`FIXED_ASSET_ID`),
  KEY `FKFFF466BE9E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FKFFF466BE33118956` FOREIGN KEY (`FIXED_ASSET_ID`) REFERENCES `fixed_asset` (`ID`),
  CONSTRAINT `FKFFF466BE7AB4E4D2` FOREIGN KEY (`TRANSACTION_MAKE_DEPOSIT_ENTRY_ID`) REFERENCES `transaction_make_deposit_entries` (`ID`),
  CONSTRAINT `FKFFF466BE9E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FKFFF466BEAC60FBCF` FOREIGN KEY (`CREDITS_AND_PAYMENTS_ID`) REFERENCES `credits_and_payments` (`ID`),
  CONSTRAINT `FKFFF466BEF1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_credits_and_payments`
--

DROP TABLE IF EXISTS `transaction_credits_and_payments`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_credits_and_payments` (
  `ID` bigint(20) NOT NULL auto_increment,
  `DATE` bigint(20) default NULL,
  `MEMO` varchar(255) default NULL,
  `AMOUNT_TO_USE` double default NULL,
  `TRANSACTION_RECEIVE_PAYMENT_ID` bigint(20) default NULL,
  `TRANSACTION_PAYBILL_ID` bigint(20) default NULL,
  `CREDITS_AND_PAYMENTS_ID` bigint(20) default NULL,
  `IS_VOID` bit(1) default NULL,
  `VERSION` int(11) default '0',
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK3D29E73BAC60FBCF` (`CREDITS_AND_PAYMENTS_ID`),
  KEY `FK3D29E73B8D37ED0D` (`TRANSACTION_RECEIVE_PAYMENT_ID`),
  KEY `FK3D29E73B33659A02` (`TRANSACTION_PAYBILL_ID`),
  CONSTRAINT `FK3D29E73B33659A02` FOREIGN KEY (`TRANSACTION_PAYBILL_ID`) REFERENCES `transaction_paybill` (`ID`),
  CONSTRAINT `FK3D29E73B8D37ED0D` FOREIGN KEY (`TRANSACTION_RECEIVE_PAYMENT_ID`) REFERENCES `transaction_receive_payment` (`ID`),
  CONSTRAINT `FK3D29E73BAC60FBCF` FOREIGN KEY (`CREDITS_AND_PAYMENTS_ID`) REFERENCES `credits_and_payments` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_credits_and_payments`
--

LOCK TABLES `transaction_credits_and_payments` WRITE;
/*!40000 ALTER TABLE `transaction_credits_and_payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_credits_and_payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_expense`
--

DROP TABLE IF EXISTS `transaction_expense`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_expense` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TYPE` int(11) default NULL,
  `ITEM_ID` bigint(20) default NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `QUANTITY` double default NULL,
  `UNIT_PRICE` double default NULL,
  `AMOUNT` double default NULL,
  `EXPENSE_ID` bigint(20) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK8448C3B71E282CDF` (`ITEM_ID`),
  KEY `FK8448C3B729FC095` (`EXPENSE_ID`),
  KEY `FK8448C3B7E5FCF475` (`ACCOUNT_ID`),
  CONSTRAINT `FK8448C3B71E282CDF` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ID`),
  CONSTRAINT `FK8448C3B729FC095` FOREIGN KEY (`EXPENSE_ID`) REFERENCES `expense` (`ID`),
  CONSTRAINT `FK8448C3B7E5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_expense`
--

LOCK TABLES `transaction_expense` WRITE;
/*!40000 ALTER TABLE `transaction_expense` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_expense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_issue_payment`
--

DROP TABLE IF EXISTS `transaction_issue_payment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_issue_payment` (
  `ID` bigint(20) NOT NULL auto_increment,
  `DATE` bigint(20) default NULL,
  `WRITE_CHECK_ID` bigint(20) default NULL,
  `CUSTOMER_REFUND_ID` bigint(20) default NULL,
  `PAYBILL_ID` bigint(20) default NULL,
  `CREDIT_CARD_CHARGE_ID` bigint(20) default NULL,
  `CASH_PURCHASE_ID` bigint(20) default NULL,
  `PAY_SALES_TAX_ID` bigint(20) default NULL,
  `NUMBER` varchar(255) default NULL,
  `NAME` varchar(255) default NULL,
  `MEMO` varchar(255) default NULL,
  `AMOUNT` double default NULL,
  `TRANSACTION_ID` bigint(20) default NULL,
  `VERSION` int(11) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK56D7E09FB5E824C0` (`WRITE_CHECK_ID`),
  KEY `FK56D7E09FBAAE8A66` (`CASH_PURCHASE_ID`),
  KEY `FK56D7E09FA45E105F` (`CREDIT_CARD_CHARGE_ID`),
  KEY `FK56D7E09F4E05C155` (`PAYBILL_ID`),
  KEY `FK56D7E09F44F7980E` (`TRANSACTION_ID`),
  KEY `FK56D7E09F63880555` (`TRANSACTION_ID`),
  KEY `FK56D7E09F6C246F1B` (`PAY_SALES_TAX_ID`),
  KEY `FK56D7E09F66E25EDC` (`CUSTOMER_REFUND_ID`),
  CONSTRAINT `FK56D7E09F44F7980E` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `issuepayment` (`ID`),
  CONSTRAINT `FK56D7E09F4E05C155` FOREIGN KEY (`PAYBILL_ID`) REFERENCES `pay_bill` (`ID`),
  CONSTRAINT `FK56D7E09F63880555` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK56D7E09F66E25EDC` FOREIGN KEY (`CUSTOMER_REFUND_ID`) REFERENCES `customer_refund` (`ID`),
  CONSTRAINT `FK56D7E09F6C246F1B` FOREIGN KEY (`PAY_SALES_TAX_ID`) REFERENCES `pay_sales_tax` (`ID`),
  CONSTRAINT `FK56D7E09FA45E105F` FOREIGN KEY (`CREDIT_CARD_CHARGE_ID`) REFERENCES `credit_card_charges` (`ID`),
  CONSTRAINT `FK56D7E09FB5E824C0` FOREIGN KEY (`WRITE_CHECK_ID`) REFERENCES `write_checks` (`ID`),
  CONSTRAINT `FK56D7E09FBAAE8A66` FOREIGN KEY (`CASH_PURCHASE_ID`) REFERENCES `cash_purchase` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_issue_payment`
--

LOCK TABLES `transaction_issue_payment` WRITE;
/*!40000 ALTER TABLE `transaction_issue_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_issue_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_item`
--

DROP TABLE IF EXISTS `transaction_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_item` (
  `ID` bigint(20) NOT NULL auto_increment,
  `VERSION` int(11) default NULL,
  `TYPE` int(11) default NULL,
  `ITEM_ID` bigint(20) default NULL,
  `TAXITEM_ID` bigint(20) default NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  `TAX_CODE` bigint(20) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `QTY_VALUE` double default NULL,
  `QTY_UNIT` bigint(20) default NULL,
  `UNIT_PRICE` double default NULL,
  `DISCOUNT` double default NULL,
  `LINE_TOTAL` double default NULL,
  `IS_TAXABLE` bit(1) default NULL,
  `TAXGROUP_ID` bigint(20) default NULL,
  `TRANSACTION_ID` bigint(20) default NULL,
  `VAT_FRACTION` double default NULL,
  `INVOICED` double default NULL,
  `BACK_ORDER` double default NULL,
  `REFERRING_TRANSACTION_ITEM_ID` bigint(20) default NULL,
  `IS_VOID` bit(1) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK30714BF4CAA74743` (`REFERRING_TRANSACTION_ITEM_ID`),
  KEY `FK30714BF4B7B51814` (`TAX_CODE`),
  KEY `FK30714BF41E282CDF` (`ITEM_ID`),
  KEY `FK30714BF4E5DBA69F` (`TAXGROUP_ID`),
  KEY `FK30714BF446AE6EF6` (`QTY_UNIT`),
  KEY `FK30714BF4E5FCF475` (`ACCOUNT_ID`),
  KEY `FK30714BF463880555` (`TRANSACTION_ID`),
  KEY `FK30714BF4D7293EF5` (`TAXITEM_ID`),
  CONSTRAINT `FK30714BF41E282CDF` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ID`),
  CONSTRAINT `FK30714BF446AE6EF6` FOREIGN KEY (`QTY_UNIT`) REFERENCES `unit` (`ID`),
  CONSTRAINT `FK30714BF463880555` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK30714BF4B7B51814` FOREIGN KEY (`TAX_CODE`) REFERENCES `tax_code` (`ID`),
  CONSTRAINT `FK30714BF4CAA74743` FOREIGN KEY (`REFERRING_TRANSACTION_ITEM_ID`) REFERENCES `transaction_item` (`ID`),
  CONSTRAINT `FK30714BF4D7293EF5` FOREIGN KEY (`TAXITEM_ID`) REFERENCES `tax_item` (`ID`),
  CONSTRAINT `FK30714BF4E5DBA69F` FOREIGN KEY (`TAXGROUP_ID`) REFERENCES `tax_group` (`ID`),
  CONSTRAINT `FK30714BF4E5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_item`
--

LOCK TABLES `transaction_item` WRITE;
/*!40000 ALTER TABLE `transaction_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_make_deposit`
--

DROP TABLE IF EXISTS `transaction_make_deposit`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_make_deposit` (
  `ID` bigint(20) NOT NULL auto_increment,
  `VERSION` int(11) default NULL,
  `DATE` bigint(20) default NULL,
  `TYPE` int(11) default NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  `VENDOR_ID` bigint(20) default NULL,
  `CUSTOMER_ID` bigint(20) default NULL,
  `REFERENCE` varchar(255) default NULL,
  `AMOUNT` double default NULL,
  `IS_NEW_ENTRY` bit(1) default NULL,
  `CASH_ACCOUNT_ID` bigint(20) default NULL,
  `MAKE_DEPOSIT_ID` bigint(20) default NULL,
  `PAYMENTS` double default NULL,
  `BALANCE_DUE` double default NULL,
  `DEPOSITED_TRANSACTION_ID` bigint(20) default NULL,
  `NUMBER` varchar(255) default NULL,
  `IS_VOID` bit(1) default NULL,
  `CREDITS_AND_PAYMENTS_ID` bigint(20) default NULL,
  `TRANSACTION_ID` bigint(20) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKCA94E10EC0C5E101` (`CASH_ACCOUNT_ID`),
  KEY `FKCA94E10EAC60FBCF` (`CREDITS_AND_PAYMENTS_ID`),
  KEY `FKCA94E10E891A177F` (`VENDOR_ID`),
  KEY `FKCA94E10E2DA4EE17` (`DEPOSITED_TRANSACTION_ID`),
  KEY `FKCA94E10E3BA00678` (`MAKE_DEPOSIT_ID`),
  KEY `FKCA94E10EE5FCF475` (`ACCOUNT_ID`),
  KEY `FKCA94E10E8D6BF347` (`TRANSACTION_ID`),
  KEY `FKCA94E10EDFE06A7F` (`CUSTOMER_ID`),
  CONSTRAINT `FKCA94E10E2DA4EE17` FOREIGN KEY (`DEPOSITED_TRANSACTION_ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKCA94E10E3BA00678` FOREIGN KEY (`MAKE_DEPOSIT_ID`) REFERENCES `make_deposit` (`ID`),
  CONSTRAINT `FKCA94E10E891A177F` FOREIGN KEY (`VENDOR_ID`) REFERENCES `vendor` (`ID`),
  CONSTRAINT `FKCA94E10E8D6BF347` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `make_deposit` (`ID`),
  CONSTRAINT `FKCA94E10EAC60FBCF` FOREIGN KEY (`CREDITS_AND_PAYMENTS_ID`) REFERENCES `credits_and_payments` (`ID`),
  CONSTRAINT `FKCA94E10EC0C5E101` FOREIGN KEY (`CASH_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FKCA94E10EDFE06A7F` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`ID`),
  CONSTRAINT `FKCA94E10EE5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_make_deposit`
--

LOCK TABLES `transaction_make_deposit` WRITE;
/*!40000 ALTER TABLE `transaction_make_deposit` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_make_deposit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_make_deposit_entries`
--

DROP TABLE IF EXISTS `transaction_make_deposit_entries`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_make_deposit_entries` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TYPE` int(11) default NULL,
  `TRANSACTION_ID` bigint(20) default NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  `AMOUNT` double default NULL,
  `BALANCE` double default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK905FAA3FE5FCF475` (`ACCOUNT_ID`),
  KEY `FK905FAA3F63880555` (`TRANSACTION_ID`),
  CONSTRAINT `FK905FAA3F63880555` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK905FAA3FE5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_make_deposit_entries`
--

LOCK TABLES `transaction_make_deposit_entries` WRITE;
/*!40000 ALTER TABLE `transaction_make_deposit_entries` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_make_deposit_entries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_pay_expense`
--

DROP TABLE IF EXISTS `transaction_pay_expense`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_pay_expense` (
  `ID` bigint(20) NOT NULL auto_increment,
  `EXPENSE_ID` bigint(20) default NULL,
  `PAYMENT` double default NULL,
  `PAY_EXPENSE_ID` bigint(20) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK49EEBF808D47192E` (`PAY_EXPENSE_ID`),
  KEY `FK49EEBF8029FC095` (`EXPENSE_ID`),
  CONSTRAINT `FK49EEBF8029FC095` FOREIGN KEY (`EXPENSE_ID`) REFERENCES `expense` (`ID`),
  CONSTRAINT `FK49EEBF808D47192E` FOREIGN KEY (`PAY_EXPENSE_ID`) REFERENCES `pay_expense` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_pay_expense`
--

LOCK TABLES `transaction_pay_expense` WRITE;
/*!40000 ALTER TABLE `transaction_pay_expense` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_pay_expense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_pay_sales_tax`
--

DROP TABLE IF EXISTS `transaction_pay_sales_tax`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_pay_sales_tax` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TAX_AGENCY_ID` bigint(20) default NULL,
  `TAX_ITEM_ID` bigint(20) default NULL,
  `AMOUNT_TO_PAY` double default NULL,
  `TAX_DUE` double default NULL,
  `TRANSACTION_ID` bigint(20) default NULL,
  `VERSION` int(11) default NULL,
  `TRANSACTION_PAYSALES_TAX_ID` bigint(20) default NULL,
  `LIABILITY_ACCOUNT_ID` bigint(20) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK7C732700C368D6AC` (`TAX_AGENCY_ID`),
  KEY `FK7C7327005A7F706C` (`TAX_ITEM_ID`),
  KEY `FK7C7327005E041EE7` (`LIABILITY_ACCOUNT_ID`),
  KEY `FK7C732700236FF23E` (`TRANSACTION_ID`),
  KEY `FK7C73270063880555` (`TRANSACTION_ID`),
  KEY `FK7C73270024CDC817` (`TRANSACTION_PAYSALES_TAX_ID`),
  CONSTRAINT `FK7C732700236FF23E` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `pay_sales_tax` (`ID`),
  CONSTRAINT `FK7C73270024CDC817` FOREIGN KEY (`TRANSACTION_PAYSALES_TAX_ID`) REFERENCES `pay_sales_tax_entries` (`ID`),
  CONSTRAINT `FK7C7327005A7F706C` FOREIGN KEY (`TAX_ITEM_ID`) REFERENCES `tax_item` (`ID`),
  CONSTRAINT `FK7C7327005E041EE7` FOREIGN KEY (`LIABILITY_ACCOUNT_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK7C73270063880555` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK7C732700C368D6AC` FOREIGN KEY (`TAX_AGENCY_ID`) REFERENCES `taxagency` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_pay_sales_tax`
--

LOCK TABLES `transaction_pay_sales_tax` WRITE;
/*!40000 ALTER TABLE `transaction_pay_sales_tax` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_pay_sales_tax` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_pay_vat`
--

DROP TABLE IF EXISTS `transaction_pay_vat`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_pay_vat` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TAX_AGENCY_ID` bigint(20) default NULL,
  `AMOUNT_TO_PAY` double default NULL,
  `TAX_DUE` double default NULL,
  `VAT_RETURN_ID` bigint(20) default NULL,
  `PAY_VAT_ID` bigint(20) default NULL,
  `VERSION` int(11) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKA3789A51EDBFF5C8` (`VAT_RETURN_ID`),
  KEY `FKA3789A51573AB18E` (`PAY_VAT_ID`),
  KEY `FKA3789A51C368D6AC` (`TAX_AGENCY_ID`),
  CONSTRAINT `FKA3789A51573AB18E` FOREIGN KEY (`PAY_VAT_ID`) REFERENCES `pay_vat` (`ID`),
  CONSTRAINT `FKA3789A51C368D6AC` FOREIGN KEY (`TAX_AGENCY_ID`) REFERENCES `taxagency` (`ID`),
  CONSTRAINT `FKA3789A51EDBFF5C8` FOREIGN KEY (`VAT_RETURN_ID`) REFERENCES `vat_return` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_pay_vat`
--

LOCK TABLES `transaction_pay_vat` WRITE;
/*!40000 ALTER TABLE `transaction_pay_vat` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_pay_vat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_paybill`
--

DROP TABLE IF EXISTS `transaction_paybill`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_paybill` (
  `ID` bigint(20) NOT NULL auto_increment,
  `VERSION` int(11) default NULL,
  `DUE_DATE` bigint(20) default NULL,
  `ENTER_BILL_ID` bigint(20) default NULL,
  `ORIGINAL_AMOUNT` double default NULL,
  `AMOUNT_DUE` double default NULL,
  `DISCOUNT_DATE` bigint(20) default NULL,
  `DISCOUNT_ID` bigint(20) default NULL,
  `CASH_DISCOUNT` double default NULL,
  `APPLIED_CREDITS` double default NULL,
  `PAYMENT` double default NULL,
  `PAYBILL_ID` bigint(20) default NULL,
  `IS_VOID` bit(1) default NULL,
  `TRANSACTION_MAKE_DEPOSIT_ID` bigint(20) default NULL,
  `BILL_NUMBER` varchar(255) default NULL,
  `JOURNAL_ENTRY_ID` bigint(20) default NULL,
  `TRANSACTION_ID` bigint(20) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKA36B3C0E9DBAA9C3` (`TRANSACTION_MAKE_DEPOSIT_ID`),
  KEY `FKA36B3C0E4E05C155` (`PAYBILL_ID`),
  KEY `FKA36B3C0E20610806` (`ENTER_BILL_ID`),
  KEY `FKA36B3C0EF5E540E1` (`DISCOUNT_ID`),
  KEY `FKA36B3C0E69504CC6` (`TRANSACTION_ID`),
  KEY `FKA36B3C0E408F6290` (`JOURNAL_ENTRY_ID`),
  CONSTRAINT `FKA36B3C0E20610806` FOREIGN KEY (`ENTER_BILL_ID`) REFERENCES `enter_bill` (`ID`),
  CONSTRAINT `FKA36B3C0E408F6290` FOREIGN KEY (`JOURNAL_ENTRY_ID`) REFERENCES `journal_entry` (`ID`),
  CONSTRAINT `FKA36B3C0E4E05C155` FOREIGN KEY (`PAYBILL_ID`) REFERENCES `pay_bill` (`ID`),
  CONSTRAINT `FKA36B3C0E69504CC6` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `pay_bill` (`ID`),
  CONSTRAINT `FKA36B3C0E9DBAA9C3` FOREIGN KEY (`TRANSACTION_MAKE_DEPOSIT_ID`) REFERENCES `transaction_make_deposit` (`ID`),
  CONSTRAINT `FKA36B3C0EF5E540E1` FOREIGN KEY (`DISCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_paybill`
--

LOCK TABLES `transaction_paybill` WRITE;
/*!40000 ALTER TABLE `transaction_paybill` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_paybill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_receive_payment`
--

DROP TABLE IF EXISTS `transaction_receive_payment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_receive_payment` (
  `ID` bigint(20) NOT NULL auto_increment,
  `VERSION` int(11) default NULL,
  `DUE_DATE` bigint(20) default NULL,
  `INVOICE_ID` bigint(20) default NULL,
  `INVOICE_AMOUNT` double default NULL,
  `DISCOUNT_DATE` bigint(20) default NULL,
  `DISCOUNT_ID` bigint(20) default NULL,
  `CASH_DISCOUNT` double default NULL,
  `WRITE_OFF_ID` bigint(20) default NULL,
  `WRITE_OFF` double default NULL,
  `APPLIED_CREDITS` double default NULL,
  `PAYMENT` double default NULL,
  `TRANSACTION_ID` bigint(20) default NULL,
  `IS_VOID` bit(1) default NULL,
  `CUSTOMER_REFUND_ID` bigint(20) default NULL,
  `JOURNAL_ENTRY_ID` bigint(20) default NULL,
  `NUMBER` varchar(255) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK6A470E9B8E9CE53` (`WRITE_OFF_ID`),
  KEY `FK6A470E942D65475` (`INVOICE_ID`),
  KEY `FK6A470E9F5E540E1` (`DISCOUNT_ID`),
  KEY `FK6A470E962AA184` (`TRANSACTION_ID`),
  KEY `FK6A470E9408F6290` (`JOURNAL_ENTRY_ID`),
  KEY `FK6A470E966E25EDC` (`CUSTOMER_REFUND_ID`),
  CONSTRAINT `FK6A470E9408F6290` FOREIGN KEY (`JOURNAL_ENTRY_ID`) REFERENCES `journal_entry` (`ID`),
  CONSTRAINT `FK6A470E942D65475` FOREIGN KEY (`INVOICE_ID`) REFERENCES `invoice` (`ID`),
  CONSTRAINT `FK6A470E962AA184` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `receive_payment` (`ID`),
  CONSTRAINT `FK6A470E966E25EDC` FOREIGN KEY (`CUSTOMER_REFUND_ID`) REFERENCES `customer_refund` (`ID`),
  CONSTRAINT `FK6A470E9B8E9CE53` FOREIGN KEY (`WRITE_OFF_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK6A470E9F5E540E1` FOREIGN KEY (`DISCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_receive_payment`
--

LOCK TABLES `transaction_receive_payment` WRITE;
/*!40000 ALTER TABLE `transaction_receive_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_receive_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_receive_vat`
--

DROP TABLE IF EXISTS `transaction_receive_vat`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transaction_receive_vat` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TAX_AGENCY_ID` bigint(20) default NULL,
  `AMOUNT_TO_RECEIVE` double default NULL,
  `TAX_DUE` double default NULL,
  `VAT_RETURN_ID` bigint(20) default NULL,
  `RECEIVE_VAT_ID` bigint(20) default NULL,
  `VERSION` int(11) default NULL,
  `IDX` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKC9BDF78CEDBFF5C8` (`VAT_RETURN_ID`),
  KEY `FKC9BDF78CC368D6AC` (`TAX_AGENCY_ID`),
  KEY `FKC9BDF78CB02EEC58` (`RECEIVE_VAT_ID`),
  CONSTRAINT `FKC9BDF78CB02EEC58` FOREIGN KEY (`RECEIVE_VAT_ID`) REFERENCES `receive_vat` (`ID`),
  CONSTRAINT `FKC9BDF78CC368D6AC` FOREIGN KEY (`TAX_AGENCY_ID`) REFERENCES `taxagency` (`ID`),
  CONSTRAINT `FKC9BDF78CEDBFF5C8` FOREIGN KEY (`VAT_RETURN_ID`) REFERENCES `vat_return` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transaction_receive_vat`
--

LOCK TABLES `transaction_receive_vat` WRITE;
/*!40000 ALTER TABLE `transaction_receive_vat` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_receive_vat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transfer_fund`
--

DROP TABLE IF EXISTS `transfer_fund`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transfer_fund` (
  `ID` bigint(20) NOT NULL,
  `TRANSFER_FROM` bigint(20) default NULL,
  `TRANSFER_TO` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK43706699274BC854` (`ID`),
  KEY `FK437066999E230757` (`TRANSFER_TO`),
  KEY `FK437066999DCE2B86` (`TRANSFER_FROM`),
  CONSTRAINT `FK43706699274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FK437066999DCE2B86` FOREIGN KEY (`TRANSFER_FROM`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK437066999E230757` FOREIGN KEY (`TRANSFER_TO`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transfer_fund`
--

LOCK TABLES `transfer_fund` WRITE;
/*!40000 ALTER TABLE `transfer_fund` DISABLE KEYS */;
/*!40000 ALTER TABLE `transfer_fund` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transfer_item`
--

DROP TABLE IF EXISTS `transfer_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `transfer_item` (
  `ID` bigint(20) NOT NULL auto_increment,
  `ITEM` bigint(20) default NULL,
  `QTY_VALUE` double default NULL,
  `QTY_UNIT` bigint(20) default NULL,
  `MEMO` varchar(255) default NULL,
  `STOCK_TRANSFER` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK4371BEE7A036EE2B` (`ITEM`),
  KEY `FK4371BEE746AE6EF6` (`QTY_UNIT`),
  KEY `FK4371BEE73B3365B0` (`STOCK_TRANSFER`),
  CONSTRAINT `FK4371BEE73B3365B0` FOREIGN KEY (`STOCK_TRANSFER`) REFERENCES `stock_transfer` (`ID`),
  CONSTRAINT `FK4371BEE746AE6EF6` FOREIGN KEY (`QTY_UNIT`) REFERENCES `unit` (`ID`),
  CONSTRAINT `FK4371BEE7A036EE2B` FOREIGN KEY (`ITEM`) REFERENCES `item` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `transfer_item`
--

LOCK TABLES `transfer_item` WRITE;
/*!40000 ALTER TABLE `transfer_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `transfer_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unit`
--

DROP TABLE IF EXISTS `unit`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `unit` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TYPE` varchar(255) default NULL,
  `FACTOR` double default NULL,
  `MEASUREMENT` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK27D1843B9DD132` (`ID`),
  KEY `FK27D184CF909FD3` (`MEASUREMENT`),
  CONSTRAINT `FK27D1843B9DD132` FOREIGN KEY (`ID`) REFERENCES `measurement` (`ID`),
  CONSTRAINT `FK27D184CF909FD3` FOREIGN KEY (`MEASUREMENT`) REFERENCES `measurement` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `unit`
--

LOCK TABLES `unit` WRITE;
/*!40000 ALTER TABLE `unit` DISABLE KEYS */;
/*!40000 ALTER TABLE `unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unit_of_measure`
--

DROP TABLE IF EXISTS `unit_of_measure`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `unit_of_measure` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TYPE` int(11) default NULL,
  `NAME` varchar(255) NOT NULL,
  `ABBREVIATION` varchar(255) default NULL,
  `VERSION` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `unit_of_measure`
--

LOCK TABLES `unit_of_measure` WRITE;
/*!40000 ALTER TABLE `unit_of_measure` DISABLE KEYS */;
/*!40000 ALTER TABLE `unit_of_measure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_permissions`
--

DROP TABLE IF EXISTS `user_permissions`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user_permissions` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TYPE_OF_BANK_RECONCILATION` int(11) default NULL,
  `TYPE_OF_INVOICES` int(11) default NULL,
  `TYPE_OF_EXPENCES` int(11) default NULL,
  `TYPE_OF_SYSTEM_SETTINGS` int(11) default NULL,
  `TYPE_OF_VIEW_REPORTS` int(11) default NULL,
  `TYPE_OF_PUBLISH_REPORTS` int(11) default NULL,
  `TYPE_OF_LOCK_DATES` int(11) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `user_permissions`
--

LOCK TABLES `user_permissions` WRITE;
/*!40000 ALTER TABLE `user_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `users` (
  `ID` bigint(20) NOT NULL auto_increment,
  `FIRST_NAME` varchar(255) default NULL,
  `LAST_NAME` varchar(255) default NULL,
  `FULL_NAME` varchar(255) default NULL,
  `EMAIL_ID` varchar(255) default NULL,
  `USER_ROLE` varchar(255) default NULL,
  `PASSWORD` varchar(255) default NULL,
  `IS_ACTIVE` bit(1) default NULL,
  `CAN_DO_USER_MANAGEMENT` bit(1) default NULL,
  `IS_DELETED` bit(1) default NULL,
  `PHONE_NO` varchar(255) default NULL,
  `COUNTRY_NAME` varchar(255) default NULL,
  `IS_ADMIN` bit(1) default NULL,
  `USER_PERMISSIONS_ID` bigint(20) default NULL,
  `COMPANY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK4D495E8622C1275` (`COMPANY_ID`),
  KEY `FK4D495E8D7CF483E` (`USER_PERMISSIONS_ID`),
  CONSTRAINT `FK4D495E8622C1275` FOREIGN KEY (`COMPANY_ID`) REFERENCES `company` (`ID`),
  CONSTRAINT `FK4D495E8D7CF483E` FOREIGN KEY (`USER_PERMISSIONS_ID`) REFERENCES `user_permissions` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vat_return`
--

DROP TABLE IF EXISTS `vat_return`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `vat_return` (
  `ID` bigint(20) NOT NULL,
  `VAT_PERIOD_START_DATE` bigint(20) default NULL,
  `VAT_PERIOD_END_DATE` bigint(20) default NULL,
  `TAX_AGENCY` bigint(20) default NULL,
  `JOURNAL_ENTRY` bigint(20) default NULL,
  `BALANCE` double default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKFD401726274BC854` (`ID`),
  KEY `FKFD401726971BCDC4` (`TAX_AGENCY`),
  KEY `FKFD4017261AF90BEA` (`JOURNAL_ENTRY`),
  CONSTRAINT `FKFD4017261AF90BEA` FOREIGN KEY (`JOURNAL_ENTRY`) REFERENCES `journal_entry` (`ID`),
  CONSTRAINT `FKFD401726274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKFD401726971BCDC4` FOREIGN KEY (`TAX_AGENCY`) REFERENCES `taxagency` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `vat_return`
--

LOCK TABLES `vat_return` WRITE;
/*!40000 ALTER TABLE `vat_return` DISABLE KEYS */;
/*!40000 ALTER TABLE `vat_return` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vatreturnbox`
--

DROP TABLE IF EXISTS `vatreturnbox`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `vatreturnbox` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `VAT_BOX` varchar(255) default NULL,
  `TOTAL_BOX` varchar(255) default NULL,
  `VAT_RETURN_TYPE` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `vatreturnbox`
--

LOCK TABLES `vatreturnbox` WRITE;
/*!40000 ALTER TABLE `vatreturnbox` DISABLE KEYS */;
/*!40000 ALTER TABLE `vatreturnbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendor`
--

DROP TABLE IF EXISTS `vendor`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `vendor` (
  `ID` bigint(20) NOT NULL,
  `ACCOUNT_NUMBER` varchar(255) default NULL,
  `BALANCE_AS_OF` bigint(20) default NULL,
  `EXPENSE_ACCOUNT_ID` bigint(20) default NULL,
  `CREDIT_LIMIT` double default NULL,
  `SHIPPING_METHOD_ID` bigint(20) default NULL,
  `PAYMENT_TERMS_ID` bigint(20) default NULL,
  `VENDOR_GROUP_ID` bigint(20) default NULL,
  `FEDERAL_TAXID` varchar(255) default NULL,
  `CURRET_DUE` double default NULL,
  `OVER_DUE_ONE_TO_THIRY_DAYS` double default NULL,
  `OVER_DUE_THIRTY_ONE_TO_SIXTY_DAYS` double default NULL,
  `OVER_DUE_SIXTY_ONE_TO_NINTY_DAYS` double default NULL,
  `OVER_DUE_OVER_NINTY_DAYS` double default NULL,
  `OVER_DUE_TOTAL_BALANCE` double default NULL,
  `MONTH_TO_DATE` double default NULL,
  `YEAR_TO_DATE` double default NULL,
  `LAST_YEAR` double default NULL,
  `LIFE_TIME_PURCHASES` double default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK96B19948AD0A95DC` (`SHIPPING_METHOD_ID`),
  KEY `FK96B19948610348FE` (`ID`),
  KEY `FK96B19948AEA76B12` (`PAYMENT_TERMS_ID`),
  KEY `FK96B199482AACCA64` (`VENDOR_GROUP_ID`),
  KEY `FK96B19948E2AA5ABC` (`EXPENSE_ACCOUNT_ID`),
  CONSTRAINT `FK96B199482AACCA64` FOREIGN KEY (`VENDOR_GROUP_ID`) REFERENCES `vendor_group` (`ID`),
  CONSTRAINT `FK96B19948610348FE` FOREIGN KEY (`ID`) REFERENCES `payee` (`ID`),
  CONSTRAINT `FK96B19948AD0A95DC` FOREIGN KEY (`SHIPPING_METHOD_ID`) REFERENCES `shippingmethod` (`ID`),
  CONSTRAINT `FK96B19948AEA76B12` FOREIGN KEY (`PAYMENT_TERMS_ID`) REFERENCES `paymentterms` (`ID`),
  CONSTRAINT `FK96B19948E2AA5ABC` FOREIGN KEY (`EXPENSE_ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `vendor`
--

LOCK TABLES `vendor` WRITE;
/*!40000 ALTER TABLE `vendor` DISABLE KEYS */;
/*!40000 ALTER TABLE `vendor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendor_credit_memo`
--

DROP TABLE IF EXISTS `vendor_credit_memo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `vendor_credit_memo` (
  `ID` bigint(20) NOT NULL,
  `VENDOR_ID` bigint(20) default NULL,
  `VCM_IS_PRIMARY` bit(1) default NULL,
  `VCM_NAME` varchar(255) default NULL,
  `VCM_TITLE` varchar(255) default NULL,
  `VCM_BUSINESS_PHONE` varchar(255) default NULL,
  `VCM_EMAIL` varchar(255) default NULL,
  `PHONE` varchar(255) default NULL,
  `BALANCE_DUE` double default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKC3D9E649274BC854` (`ID`),
  KEY `FKC3D9E649891A177F` (`VENDOR_ID`),
  CONSTRAINT `FKC3D9E649274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKC3D9E649891A177F` FOREIGN KEY (`VENDOR_ID`) REFERENCES `vendor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `vendor_credit_memo`
--

LOCK TABLES `vendor_credit_memo` WRITE;
/*!40000 ALTER TABLE `vendor_credit_memo` DISABLE KEYS */;
/*!40000 ALTER TABLE `vendor_credit_memo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendor_group`
--

DROP TABLE IF EXISTS `vendor_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `vendor_group` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `VERSION` int(11) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `IS_DEFAULT` bit(1) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK2E46E4A8F1AE8CDE` (`CREATED_BY`),
  KEY `FK2E46E4A89E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK2E46E4A89E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK2E46E4A8F1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `vendor_group`
--

LOCK TABLES `vendor_group` WRITE;
/*!40000 ALTER TABLE `vendor_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `vendor_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse`
--

DROP TABLE IF EXISTS `warehouse`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `warehouse` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIER` bigint(20) default NULL,
  `CREATED_DATE` datetime default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `TYPE` int(11) NOT NULL,
  `ADDRESS1` varchar(100) NOT NULL,
  `STREET` varchar(100) NOT NULL,
  `CITY` varchar(100) NOT NULL,
  `STATE` varchar(100) NOT NULL,
  `ZIP` varchar(100) NOT NULL,
  `COUNTRY` varchar(100) NOT NULL,
  `IS_SELECTED` bit(1) NOT NULL,
  `COMPANY_CONTACT__IS_PRIMARY` bit(1) NOT NULL,
  `COMPANY_CONTACT__NAME` varchar(255) NOT NULL,
  `COMPANY_CONTACT__TITLE` varchar(255) NOT NULL,
  `COMPANY_CONTACT__BUSINESS_PHONE` varchar(255) NOT NULL,
  `COMPANY_CONTACT_EMAIL` varchar(255) NOT NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK2F074AA3F1AE8CDE` (`CREATED_BY`),
  KEY `FK2F074AA39E5A0E30` (`LAST_MODIFIER`),
  CONSTRAINT `FK2F074AA39E5A0E30` FOREIGN KEY (`LAST_MODIFIER`) REFERENCES `users` (`ID`),
  CONSTRAINT `FK2F074AA3F1AE8CDE` FOREIGN KEY (`CREATED_BY`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `warehouse`
--

LOCK TABLES `warehouse` WRITE;
/*!40000 ALTER TABLE `warehouse` DISABLE KEYS */;
/*!40000 ALTER TABLE `warehouse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `write_checks`
--

DROP TABLE IF EXISTS `write_checks`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `write_checks` (
  `ID` bigint(20) NOT NULL,
  `PAY_TO_TYPE` int(11) default NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  `BALANCE` double default NULL,
  `CUSTOMER_ID` bigint(20) default NULL,
  `VENDOR_ID` bigint(20) default NULL,
  `TAX_AGENCY_ID` bigint(20) default NULL,
  `WRITECHECK_ADDRESS_TYPE` int(11) default NULL,
  `WRITECHECK_ADDRESS_ADDRESS1` varchar(100) default NULL,
  `WRITECHECK_ADDRESS_STREET` varchar(255) default NULL,
  `WCHECK_ADDRESS_CITY` varchar(255) default NULL,
  `WCHECK_ADDRESS_STATE` varchar(255) default NULL,
  `WCHECK_ADDRESS_ZIP` varchar(255) default NULL,
  `WCHECK_ADDRESS_COUNTRY` varchar(255) default NULL,
  `WCHECK_ADDRESS_IS_SELECTED` bit(1) default NULL,
  `AMOUNT` double default NULL,
  `TO_BE_PRINTED` bit(1) default NULL,
  `SALES_PERSON_ID` bigint(20) default NULL,
  `IN_WORDS` varchar(255) default NULL,
  `CHECK_NUMBER` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FKB592E8EB274BC854` (`ID`),
  KEY `FKB592E8EB891A177F` (`VENDOR_ID`),
  KEY `FKB592E8EBC368D6AC` (`TAX_AGENCY_ID`),
  KEY `FKB592E8EB4C74BEAE` (`SALES_PERSON_ID`),
  KEY `FKB592E8EBE5FCF475` (`ACCOUNT_ID`),
  KEY `FKB592E8EBDFE06A7F` (`CUSTOMER_ID`),
  CONSTRAINT `FKB592E8EB274BC854` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `FKB592E8EB4C74BEAE` FOREIGN KEY (`SALES_PERSON_ID`) REFERENCES `sales_person` (`ID`),
  CONSTRAINT `FKB592E8EB891A177F` FOREIGN KEY (`VENDOR_ID`) REFERENCES `vendor` (`ID`),
  CONSTRAINT `FKB592E8EBC368D6AC` FOREIGN KEY (`TAX_AGENCY_ID`) REFERENCES `taxagency` (`ID`),
  CONSTRAINT `FKB592E8EBDFE06A7F` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`ID`),
  CONSTRAINT `FKB592E8EBE5FCF475` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `write_checks`
--

LOCK TABLES `write_checks` WRITE;
/*!40000 ALTER TABLE `write_checks` DISABLE KEYS */;
/*!40000 ALTER TABLE `write_checks` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-08-09  4:27:15
