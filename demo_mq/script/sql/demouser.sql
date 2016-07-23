-- demouser
CREATE TABLE `demo`.`demouser` (
  `usercode` VARCHAR(10) NOT NULL,
  `username` VARCHAR(25) NULL,
  `cremark` VARCHAR(45) NULL,
  PRIMARY KEY (`usercode`));
  
  
CREATE TABLE `demo`.`ju_users` (
  `ju_userID` VARCHAR(10) NOT NULL,
  `TaobaoID` int NULL,
  `ju_userName` VARCHAR(30) NOT NULL,
  `ju_userPWD` VARCHAR(10) NULL,
  PRIMARY KEY (`ju_userID`)
  );
  
  -- ju_userID,TaobaoID,ju_userName,ju_userPWD