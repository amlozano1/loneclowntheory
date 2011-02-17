SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `LoneClownTheory` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `LoneClownTheory` ;

-- -----------------------------------------------------
-- Table `LoneClownTheory`.`entityTable`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LoneClownTheory`.`entityTable` ;

CREATE  TABLE IF NOT EXISTS `LoneClownTheory`.`entityTable` (
  `entityID` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `entityName` VARCHAR(10) NOT NULL ,
  `subject_or_object` TINYINT(1)  NOT NULL ,
  PRIMARY KEY (`entityID`, `entityName`) ,
  UNIQUE INDEX `entityName_UNIQUE` (`entityName` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `LoneClownTheory`.`acm`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LoneClownTheory`.`acm` ;

CREATE  TABLE IF NOT EXISTS `LoneClownTheory`.`acm` (
  `Subject` VARCHAR(10) NOT NULL ,
  `Object` VARCHAR(10) NOT NULL ,
  `Granter` VARCHAR(10) NOT NULL ,
  `right` VARCHAR(1) NOT NULL ,
  `timestamp` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`Subject`, `Object`) ,
  INDEX `entity` (`Object` ASC) ,
  INDEX `subject` (`Subject` ASC) ,
  INDEX `granter` (`Granter` ASC) ,
  CONSTRAINT `subject`
    FOREIGN KEY (`Subject` )
    REFERENCES `LoneClownTheory`.`entityTable` (`entityName` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `entity`
    FOREIGN KEY (`Object` )
    REFERENCES `LoneClownTheory`.`entityTable` (`entityName` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `granter`
    FOREIGN KEY (`Granter` )
    REFERENCES `LoneClownTheory`.`entityTable` (`entityName` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `LoneClownTheory`.`entityTable`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `LoneClownTheory`;
INSERT INTO `LoneClownTheory`.`entityTable` (`entityID`, `entityName`, `subject_or_object`) VALUES (1, 'subject0', 1);

COMMIT;
