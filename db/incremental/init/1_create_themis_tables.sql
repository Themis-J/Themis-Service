DROP TABLE IF EXISTS EnumValue CASCADE;
DROP TABLE IF EXISTS EnumType CASCADE;

CREATE TABLE EnumType
(
   id integer, 
   name varchar(100), 
   CONSTRAINT EnumType_PK PRIMARY KEY (id), 
   CONSTRAINT EnumType_Unique UNIQUE (name)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE EnumValue
(
   typeID integer, 
   name varchar(100), 
   value integer, 
   CONSTRAINT EnumValue_PK PRIMARY KEY (typeID, value), 
   CONSTRAINT EnumValue_FK FOREIGN KEY (typeID) REFERENCES EnumType (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS UserRole CASCADE;
DROP TABLE IF EXISTS UserInfo CASCADE;
DROP TABLE IF EXISTS Dealer CASCADE;


CREATE TABLE Dealer
(
   id integer, 
   name varchar(100) NOT NULL, 
   fullName varchar(250) NOT NULL, 
   code varchar(10) NOT NULL, 
   city varchar(20), 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT Dealer_PK PRIMARY KEY (id), 
   CONSTRAINT DealerName_Unique UNIQUE (name), 
   CONSTRAINT DealerCode_Unique UNIQUE (code)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE UserRole
(
   id integer, 
   name varchar(20),  
   timestamp timestamp NOT NULL, 
   CONSTRAINT UserRoleID_PK PRIMARY KEY (id), 
   CONSTRAINT UserRoleName_Unique UNIQUE (name)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE UserInfo
(
   username varchar(20) NOT NULL, 
   password varchar(100), 
   userRoleID integer NOT NULL, 
   dealerID integer,
   active boolean, 
   version integer NOT NULL,
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   updatedBy varchar(20) NOT NULL, 
   CONSTRAINT User_PK PRIMARY KEY (username,timestamp), 
   CONSTRAINT UserTE_Unique UNIQUE (username,timeEnd,version), 
   CONSTRAINT UserTS_Unique UNIQUE (username,timestamp,version), 
   CONSTRAINT UserRole_FK FOREIGN KEY (userRoleID) REFERENCES UserRole (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT UserDealer_FK FOREIGN KEY (dealerID) REFERENCES Dealer (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS Department CASCADE;

CREATE TABLE Department
(
   id integer, 
   name varchar(100), 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT Department_PK PRIMARY KEY (id), 
   CONSTRAINT Department_Unique UNIQUE (name)
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS MenuHierachy CASCADE;
DROP TABLE IF EXISTS Menu CASCADE;

CREATE TABLE Menu
(
   id integer, 
   name varchar(100) NOT NULL, 
   displayText varchar(100), 
   CONSTRAINT Menu_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE MenuHierachy
(
   parentID integer, 
   childID integer, 
   itemOrder integer default 0,
   CONSTRAINT MenuHierachy_PK PRIMARY KEY (parentID, childID), 
   CONSTRAINT MHP_FK FOREIGN KEY (parentID) REFERENCES Menu (id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT MHC_FK FOREIGN KEY (childID) REFERENCES Menu (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS DealerEntryItemStatus CASCADE;
CREATE TABLE DealerEntryItemStatus
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL, 
   entryItemID integer NOT NULL, 
   version integer NOT NULL,
   updateBy varchar(100) NOT NULL,
   CONSTRAINT DEIS_PK PRIMARY KEY (timestamp, validDate, dealerID, entryItemID), 
   CONSTRAINT DEIS_Unique UNIQUE (timeEnd, validDate, dealerID, entryItemID, version)
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS DealerJobPosition CASCADE;
DROP TABLE IF EXISTS HumanResourceAllocation CASCADE;
DROP TABLE IF EXISTS JobPosition CASCADE;

CREATE TABLE JobPosition
(
   id integer, 
   name varchar(100),
   timestamp timestamp without time zone, 
   CONSTRAINT JobPosition_PK PRIMARY KEY (id), 
   CONSTRAINT JobPosition_Unique UNIQUE (name)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE DealerJobPosition
(
   dealerID integer NOT NULL, 
   positionID integer NOT NULL,  
   validDate date NOT NULL,
   validEnd date NOT NULL,
   CONSTRAINT DJP_Unique UNIQUE (dealerID, positionID), 
   CONSTRAINT DJP_VID_FK FOREIGN KEY (positionID) REFERENCES JobPosition (id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT DJP_DID_FK FOREIGN KEY (dealerID) REFERENCES Dealer (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;CREATE TABLE HumanResourceAllocation
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   version integer NOT NULL,
   allocation double precision NOT NULL,
   updatedBy varchar(20) NOT NULL, 
   CONSTRAINT HRA_PK PRIMARY KEY (timestamp, validDate, dealerID, departmentID, id), 
   CONSTRAINT HRA_Unique UNIQUE (timeEnd, validDate, dealerID, departmentID, id, version)
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS DealerVehicle CASCADE;
DROP TABLE IF EXISTS DealerSalesServiceJournalItem CASCADE;
DROP TABLE IF EXISTS DealerGeneralJournalItem CASCADE;
DROP TABLE IF EXISTS GeneralJournal CASCADE;
DROP TABLE IF EXISTS GeneralJournalItem CASCADE;
DROP TABLE IF EXISTS GeneralJournalCategory CASCADE;
DROP TABLE IF EXISTS SalesServiceJournal CASCADE;
DROP TABLE IF EXISTS SalesServiceJournalItem CASCADE;
DROP TABLE IF EXISTS SalesServiceJournalCategory CASCADE;
DROP TABLE IF EXISTS VehicleSalesJournal CASCADE;
DROP TABLE IF EXISTS Vehicle CASCADE;

CREATE TABLE GeneralJournalCategory
(
   id integer, 
   name varchar(100), 
   timestamp timestamp without time zone, 
   CONSTRAINT GJC_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE GeneralJournalItem
(
   id integer, 
   name varchar(100), 
   categoryID integer, 
   journalType integer,
   timestamp timestamp without time zone, 
   CONSTRAINT GJI_PK PRIMARY KEY (id), 
   CONSTRAINT GJI_Unique UNIQUE (name),
   CONSTRAINT GJI_FK FOREIGN KEY (categoryID) REFERENCES GeneralJournalCategory (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE DealerGeneralJournalItem
(
   dealerID integer NOT NULL, 
   itemID integer NOT NULL, 
   validDate date NOT NULL,
   validEnd date NOT NULL,
   CONSTRAINT DGJI_Unique UNIQUE (dealerID, itemID), 
   CONSTRAINT DGJJI_VID_FK FOREIGN KEY (itemID) REFERENCES GeneralJournalItem (id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT DGJJI_DID_FK FOREIGN KEY (dealerID) REFERENCES Dealer (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE GeneralJournal
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   version integer NOT NULL,
   amount double precision NOT NULL,
   updatedBy varchar(20) NOT NULL, 
   CONSTRAINT GJ_PK PRIMARY KEY (timestamp, validDate, dealerID, departmentID, id), 
   CONSTRAINT GJ_Unique UNIQUE (timeEnd, validDate, dealerID, departmentID, id, version)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE SalesServiceJournalCategory
(
   id integer, 
   name varchar(100), 
   timestamp timestamp without time zone, 
   CONSTRAINT SSJI_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE SalesServiceJournalItem
(
   id integer, 
   name varchar(100), 
   categoryID integer,
   journalType integer,
   description text,
   timestamp timestamp without time zone, 
   CONSTRAINT SSJLI_PK PRIMARY KEY (id),
   CONSTRAINT SSJLI_Unique UNIQUE (name, categoryID),
   CONSTRAINT SSJLI_FK FOREIGN KEY (categoryID) REFERENCES SalesServiceJournalCategory (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE DealerSalesServiceJournalItem
(
   dealerID integer NOT NULL, 
   itemID integer NOT NULL,  
   validDate date NOT NULL,
   validEnd date NOT NULL,
   CONSTRAINT DSSJI_Unique UNIQUE (dealerID, itemID), 
   CONSTRAINT DSSJI_VID_FK FOREIGN KEY (itemID) REFERENCES SalesServiceJournalItem (id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT DSSJI_DID_FK FOREIGN KEY (dealerID) REFERENCES Dealer (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE Vehicle
(
   id integer, 
   name varchar(100) NOT NULL, 
   categoryID integer NOT NULL, 
   type integer NOT NULL,
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT Vehicle_PK PRIMARY KEY (id), 
   CONSTRAINT SSJI_FK FOREIGN KEY (categoryID) REFERENCES SalesServiceJournalCategory (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE DealerVehicle
(
   dealerID integer NOT NULL, 
   vehicleID integer NOT NULL,  
   validDate date NOT NULL,
   validEnd date NOT NULL,
   CONSTRAINT DV_Unique UNIQUE (dealerID, vehicleID), 
   CONSTRAINT DV_VID_FK FOREIGN KEY (vehicleID) REFERENCES Vehicle (id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT DV_DID_FK FOREIGN KEY (dealerID) REFERENCES Dealer (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE SalesServiceJournal
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   version integer NOT NULL,
   amount double precision NOT NULL,
   margin double precision NOT NULL,
   count integer NOT NULL,
   updatedBy varchar(20) NOT NULL, 
   CONSTRAINT SSJ_PK PRIMARY KEY (timestamp, validDate, dealerID, departmentID, id), 
   CONSTRAINT SSJ_Unique UNIQUE (timeEnd, validDate, dealerID, departmentID, id, version)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE VehicleSalesJournal
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   version integer NOT NULL,
   amount double precision NOT NULL,
   margin double precision NOT NULL,
   count integer NOT NULL,
   updatedBy varchar(20) NOT NULL, 
   CONSTRAINT VSJ_PK PRIMARY KEY (timestamp, validDate, dealerID, departmentID, id), 
   CONSTRAINT VSJ_Unique UNIQUE (timeEnd, validDate, dealerID, departmentID, id, version)
) 
WITH (
  OIDS = FALSE
)
;


DROP TABLE IF EXISTS DealerEmployeeFeeItem CASCADE;
DROP TABLE IF EXISTS DealerAccountReceivableDurationItem CASCADE;
DROP TABLE IF EXISTS DealerEmployeeFeeSummaryItem CASCADE;
DROP TABLE IF EXISTS DealerInventoryDurationItem CASCADE;
DROP TABLE IF EXISTS AccountReceivableDuration CASCADE;
DROP TABLE IF EXISTS AccountReceivableDurationItem CASCADE;
DROP TABLE IF EXISTS EmployeeFee CASCADE;
DROP TABLE IF EXISTS EmployeeFeeItem CASCADE;
DROP TABLE IF EXISTS EmployeeFeeSummary CASCADE;
DROP TABLE IF EXISTS EmployeeFeeSummaryItem CASCADE;
DROP TABLE IF EXISTS InventoryDuration CASCADE;
DROP TABLE IF EXISTS InventoryDurationItem CASCADE;
DROP TABLE IF EXISTS Duration CASCADE;


CREATE TABLE Duration
(
   id integer, 
   unit integer NOT NULL, 
   lowerBound integer NOT NULL, 
   upperBound integer, 
   CONSTRAINT Duration_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE AccountReceivableDurationItem
(
   id integer, 
   name varchar(100) NOT NULL, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT ARDI_PK PRIMARY KEY (id),
   CONSTRAINT ARDI_Unique UNIQUE (name)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE AccountReceivableDuration
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   durationID integer NOT NULL,
   id integer NOT NULL,
   version integer,
   amount double precision,
   updatedBy varchar(20) NOT NULL, 
   CONSTRAINT ARD_PK PRIMARY KEY (timestamp, validDate, dealerID, durationID, id), 
   CONSTRAINT ARD_Unique UNIQUE (timeEnd, validDate, dealerID, durationID, id, version)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE EmployeeFeeItem
(
   id integer, 
   name varchar(100) NOT NULL, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT EFI_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE EmployeeFee
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   version integer NOT NULL,
   feeTypeID integer NOT NULL, 
   amount double precision NOT NULL,
   updatedBy varchar(20) NOT NULL, 
   CONSTRAINT EF_PK PRIMARY KEY (timestamp, validDate, dealerID, departmentID, id, feeTypeID), 
   CONSTRAINT EF_Unique UNIQUE (timeEnd, validDate, dealerID, departmentID, id, feeTypeID, version)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE EmployeeFeeSummaryItem
(
   id integer, 
   name varchar(100) NOT NULL, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT EFSI_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE EmployeeFeeSummary
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   version integer NOT NULL,
   amount double precision NOT NULL,
   updatedBy varchar(20) NOT NULL, 
   CONSTRAINT EFS_PK PRIMARY KEY (timestamp, validDate, dealerID, departmentID, id), 
   CONSTRAINT EFS_Unique UNIQUE (timeEnd, validDate, dealerID, departmentID, id, version)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE InventoryDurationItem
(
   id integer, 
   name varchar(100) NOT NULL, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT IDI_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE InventoryDuration
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   durationID integer NOT NULL,
   id integer NOT NULL,
   version integer NOT NULL, 
   amount double precision NOT NULL, 
   updatedBy varchar(20) NOT NULL, 
   CONSTRAINT ID_PK PRIMARY KEY (timestamp, validDate, dealerID, departmentID, durationID, id), 
   CONSTRAINT ID_Unique UNIQUE (timeEnd, validDate, dealerID, departmentID, durationID, id, version)
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS TaxJournalItem CASCADE;
DROP TABLE IF EXISTS TaxJournal CASCADE;
CREATE TABLE TaxJournalItem
(
   id integer, 
   name varchar(100) NOT NULL, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT TaxJI_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE TaxJournal
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   id integer NOT NULL,
   version integer NOT NULL,
   amount double precision NOT NULL,
   updatedBy varchar(20) NOT NULL, 
   CONSTRAINT TaxJ_PK PRIMARY KEY (timestamp, validDate, dealerID, id), 
   CONSTRAINT TaxJ_Unique UNIQUE (timeEnd, validDate, dealerID, id, version)
) 
WITH (
  OIDS = FALSE
)
;
DROP TABLE IF EXISTS DealerIncomeRevenueFact CASCADE;
DROP TABLE IF EXISTS DealerIncomeExpenseFact CASCADE;
DROP TABLE IF EXISTS DealerEmployeeFeeFact CASCADE;
DROP TABLE IF EXISTS DealerInventoryFact CASCADE;
DROP TABLE IF EXISTS ReportTime CASCADE;
DROP TABLE IF EXISTS ReportItem CASCADE;

CREATE TABLE ReportTime
(
   id SERIAL, 
   validDate date NOT NULL,
   monthOfYear integer NOT NULL,
   year integer NOT NULL,
   CONSTRAINT ReportTime_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE ReportItem
(
   id SERIAL, 
   name varchar(100) NOT NULL, 
   sourceItemID integer NOT NULL, 
   itemSource integer NOT NULL, 
   itemCategory varchar(100), 
   CONSTRAINT ReportItem_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE DealerIncomeRevenueFact
(
   timeID integer NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   itemID integer NOT NULL,
   version integer NOT NULL,
   amount double precision NOT NULL,
   margin double precision NOT NULL,
   count integer NOT NULL,
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   CONSTRAINT DIRF_PK PRIMARY KEY (timestamp, timeID, dealerID, departmentID, itemID), 
   CONSTRAINT DIRF_Unique UNIQUE (timeEnd, timeID, dealerID, departmentID, itemID, version)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE DealerIncomeExpenseFact
(
   timeID integer NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   itemID integer NOT NULL,
   version integer NOT NULL,
   amount double precision NOT NULL,
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   CONSTRAINT DIEF_PK PRIMARY KEY (timestamp, timeID, dealerID, departmentID, itemID), 
   CONSTRAINT DIEF_Unique UNIQUE (timeEnd, timeID, dealerID, departmentID, itemID, version)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE DealerEmployeeFeeFact
(
   timeID integer NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   itemID integer NOT NULL,
   version integer,
   amount double precision,
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   CONSTRAINT DEFF_PK PRIMARY KEY (timestamp, timeID, dealerID, departmentID, itemID), 
   CONSTRAINT DEFF_Unique UNIQUE (timeEnd, timeID, dealerID, departmentID, itemID, version)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE DealerInventoryFact
(
   timeID integer NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   itemID integer NOT NULL,
   durationID integer NOT NULL,
   version integer,
   amount double precision,
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   CONSTRAINT DIF_PK PRIMARY KEY (timestamp, timeID, dealerID, departmentID, itemID, durationID, version),
   CONSTRAINT DIF_Unique UNIQUE (timeEnd, timeID, dealerID, departmentID, itemID, durationID, version)
) 
WITH (
  OIDS = FALSE
)
;