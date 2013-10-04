CREATE TABLE EnumType (    id integer,     name varchar(100)  ) 

CREATE TABLE EnumValue (    typeID integer,     name varchar(100),     value integer   ) 

CREATE TABLE UserRole( id integer,    name varchar(20),     timestamp timestamp) 
CREATE TABLE UserInfo (username varchar(20),     password varchar(50),     userRoleID integer,    dealerID integer,    active boolean,  version integer,   timestamp timestamp, timeEnd timestamp, updatedBy varchar(20) ) 

CREATE TABLE Dealer (   id integer,   name varchar(100) NOT NULL,   fullName varchar(250) NOT NULL,    code varchar(10) NOT NULL,    city varchar(20),    timestamp timestamp ) 

CREATE TABLE Menu (id integer PRIMARY KEY, name varchar(100) NOT NULL, displayText varchar(100)) 
CREATE TABLE MenuHierachy (parentID integer, childID integer, itemOrder integer) 

CREATE TABLE Department (   id integer,    name varchar(100),    timestamp timestamp)

CREATE TABLE HumanResourceAllocation(   timestamp timestamp,   timeEnd timestamp,    validDate date NOT NULL,    dealerID integer NOT NULL,   departmentID integer NOT NULL,   id integer NOT NULL,   version integer,   allocation double,   updatedBy varchar(20) NOT NULL,    CONSTRAINT HRA_Unique UNIQUE (timestamp, validDate, dealerID, departmentID, id, version)) 

CREATE TABLE JobPosition (   id integer,    name varchar(100),    timestamp timestamp)
CREATE TABLE Vehicle (    id integer,  name varchar(100), categoryID integer, type integer, timestamp timestamp) 

CREATE TABLE TaxJournalItem (id integer, name varchar(100), timestamp timestamp)

CREATE TABLE SalesServiceJournalCategory (    id integer,    name varchar(100),     timestamp timestamp, CONSTRAINT SSJI_PK PRIMARY KEY (id))

CREATE TABLE SalesServiceJournalItem (id integer, name varchar(100), categoryID integer, journalType integer, description varchar(100), timestamp timestamp)

CREATE TABLE TaxJournal (timestamp timestamp, timeEnd timestamp, validDate date, dealerID integer, id integer, version integer, amount double, updatedBy varchar(50))
ALTER TABLE TaxJournal ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN validDate SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN id SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN amount SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN version SET NOT NULL
ALTER TABLE TaxJournal ADD PRIMARY KEY (timestamp, validDate, dealerID, id)
ALTER TABLE TaxJournal ADD CONSTRAINT TJ_Unique UNIQUE(timeEnd, validDate, dealerID, id, version)

CREATE TABLE DealerEntryItemStatus (timestamp timestamp, timeEnd timestamp, validDate date, dealerID integer, entryItemID integer, version integer, updateBy varchar(100)) 
ALTER TABLE DealerEntryItemStatus ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE DealerEntryItemStatus ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE DealerEntryItemStatus ALTER COLUMN validDate SET NOT NULL
ALTER TABLE DealerEntryItemStatus ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE DealerEntryItemStatus ALTER COLUMN entryItemID SET NOT NULL
ALTER TABLE DealerEntryItemStatus ALTER COLUMN version SET NOT NULL
ALTER TABLE DealerEntryItemStatus ADD PRIMARY KEY (timestamp, validDate, dealerID, entryItemID)
ALTER TABLE DealerEntryItemStatus ADD CONSTRAINT DEIS_Unique UNIQUE(timeEnd, validDate, dealerID, entryItemID, version)

CREATE TABLE VehicleSalesJournal (timestamp timestamp, timeEnd timestamp, validDate date, dealerID integer, departmentID integer, id integer, version integer, amount double, margin double, count integer, updatedBy varchar(20))
ALTER TABLE VehicleSalesJournal ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE VehicleSalesJournal ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE VehicleSalesJournal ALTER COLUMN validDate SET NOT NULL
ALTER TABLE VehicleSalesJournal ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE VehicleSalesJournal ALTER COLUMN departmentID SET NOT NULL
ALTER TABLE VehicleSalesJournal ALTER COLUMN id SET NOT NULL
ALTER TABLE VehicleSalesJournal ALTER COLUMN amount SET NOT NULL
ALTER TABLE VehicleSalesJournal ALTER COLUMN margin SET NOT NULL
ALTER TABLE VehicleSalesJournal ALTER COLUMN version SET NOT NULL
ALTER TABLE VehicleSalesJournal ADD PRIMARY KEY (timestamp, validDate, dealerID, id, departmentID)
ALTER TABLE VehicleSalesJournal ADD CONSTRAINT VSJ_Unique UNIQUE(timeEnd, validDate, dealerID, id, departmentID, version)

CREATE TABLE SalesServiceJournal (timestamp timestamp, timeEnd timestamp, validDate date, dealerID integer, departmentID integer, id integer, version integer, amount double, margin double, count integer, updatedBy varchar(20))
ALTER TABLE SalesServiceJournal ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE SalesServiceJournal ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE SalesServiceJournal ALTER COLUMN validDate SET NOT NULL
ALTER TABLE SalesServiceJournal ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE SalesServiceJournal ALTER COLUMN departmentID SET NOT NULL
ALTER TABLE SalesServiceJournal ALTER COLUMN id SET NOT NULL
ALTER TABLE SalesServiceJournal ALTER COLUMN amount SET NOT NULL
ALTER TABLE SalesServiceJournal ALTER COLUMN margin SET NOT NULL
ALTER TABLE SalesServiceJournal ALTER COLUMN version SET NOT NULL
ALTER TABLE SalesServiceJournal ADD PRIMARY KEY (timestamp, validDate, dealerID, id, departmentID)
ALTER TABLE SalesServiceJournal ADD CONSTRAINT SSJ_Unique UNIQUE(timeEnd, validDate, dealerID, id, departmentID, version)

CREATE TABLE GeneralJournalCategory (   id integer,    name varchar(100), timestamp timestamp) 

CREATE TABLE GeneralJournalItem (   id integer,    name varchar(100),    categoryID integer, journalType integer, timestamp timestamp ) 

CREATE TABLE GeneralJournal (timestamp timestamp, timeEnd timestamp, validDate date, dealerID integer, departmentID integer, id integer, version integer, amount double, updatedBy varchar(40)) 
ALTER TABLE GeneralJournal ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE GeneralJournal ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE GeneralJournal ALTER COLUMN validDate SET NOT NULL
ALTER TABLE GeneralJournal ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE GeneralJournal ALTER COLUMN departmentID SET NOT NULL
ALTER TABLE GeneralJournal ALTER COLUMN id SET NOT NULL
ALTER TABLE GeneralJournal ALTER COLUMN amount SET NOT NULL
ALTER TABLE GeneralJournal ALTER COLUMN version SET NOT NULL
ALTER TABLE GeneralJournal ADD PRIMARY KEY (timestamp, validDate, dealerID, id, departmentID)
ALTER TABLE GeneralJournal ADD CONSTRAINT GJ_Unique UNIQUE(timeEnd, validDate, dealerID, id, departmentID, version)

CREATE TABLE Duration (   id integer,    unit integer,    lowerBound integer,  upperBound integer ) 

CREATE TABLE AccountReceivableDurationItem (   id integer,    name varchar(100),    timestamp timestamp ) 

CREATE TABLE AccountReceivableDuration (   timestamp timestamp,   timeEnd timestamp,   validDate date,    dealerID integer,   durationID integer,   id integer,   version integer, amount double,   updatedBy varchar(20) ) 
ALTER TABLE AccountReceivableDuration ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE AccountReceivableDuration ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE AccountReceivableDuration ALTER COLUMN validDate SET NOT NULL
ALTER TABLE AccountReceivableDuration ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE AccountReceivableDuration ALTER COLUMN durationID SET NOT NULL
ALTER TABLE AccountReceivableDuration ALTER COLUMN id SET NOT NULL
ALTER TABLE AccountReceivableDuration ALTER COLUMN amount SET NOT NULL
ALTER TABLE AccountReceivableDuration ALTER COLUMN version SET NOT NULL
ALTER TABLE AccountReceivableDuration ADD PRIMARY KEY (timestamp, validDate, dealerID, id, durationID)
ALTER TABLE AccountReceivableDuration ADD CONSTRAINT ARD_Unique UNIQUE(timeEnd, validDate, dealerID, id, durationID, version)

CREATE TABLE EmployeeFeeItem (   id integer,    name varchar(100),    timestamp timestamp ) 

CREATE TABLE EmployeeFee (   timestamp timestamp,   timeEnd timestamp,   validDate date,    dealerID integer,   departmentID integer,   id integer,   feeTypeID integer,  version integer, amount double,   updatedBy varchar(20) ) 
   ALTER TABLE EmployeeFee ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE EmployeeFee ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE EmployeeFee ALTER COLUMN validDate SET NOT NULL
ALTER TABLE EmployeeFee ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE EmployeeFee ALTER COLUMN departmentID SET NOT NULL
ALTER TABLE EmployeeFee ALTER COLUMN id SET NOT NULL
ALTER TABLE EmployeeFee ALTER COLUMN amount SET NOT NULL
ALTER TABLE EmployeeFee ALTER COLUMN version SET NOT NULL
ALTER TABLE EmployeeFee ADD PRIMARY KEY (timestamp, validDate, dealerID, id, departmentID)
ALTER TABLE EmployeeFee ADD CONSTRAINT EF_Unique UNIQUE(timeEnd, validDate, dealerID, id, departmentID, version)


CREATE TABLE EmployeeFeeSummaryItem (id integer, name varchar(100), timestamp timestamp ) 

CREATE TABLE EmployeeFeeSummary (timestamp timestamp,  timeEnd timestamp,  validDate date,    dealerID integer,   departmentID integer,   id integer,   version integer, amount double,   updatedBy varchar(20) ) 
ALTER TABLE EmployeeFeeSummary ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE EmployeeFeeSummary ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE EmployeeFeeSummary ALTER COLUMN validDate SET NOT NULL
ALTER TABLE EmployeeFeeSummary ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE EmployeeFeeSummary ALTER COLUMN departmentID SET NOT NULL
ALTER TABLE EmployeeFeeSummary ALTER COLUMN id SET NOT NULL
ALTER TABLE EmployeeFeeSummary ALTER COLUMN amount SET NOT NULL
ALTER TABLE EmployeeFeeSummary ALTER COLUMN version SET NOT NULL
ALTER TABLE EmployeeFeeSummary ADD PRIMARY KEY (timestamp, validDate, dealerID, id, departmentID)
ALTER TABLE EmployeeFeeSummary ADD CONSTRAINT EFS_Unique UNIQUE(timeEnd, validDate, dealerID, id, departmentID, version)

CREATE TABLE InventoryDurationItem (   id integer,    name varchar(100),    timestamp timestamp) 

CREATE TABLE InventoryDuration (timestamp timestamp, timeEnd timestamp, validDate date, dealerID integer, departmentID integer, durationID integer, id integer, version integer, amount double, updatedBy varchar(20)) 
ALTER TABLE InventoryDuration ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE InventoryDuration ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE InventoryDuration ALTER COLUMN validDate SET NOT NULL
ALTER TABLE InventoryDuration ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE InventoryDuration ALTER COLUMN durationID SET NOT NULL
ALTER TABLE InventoryDuration ALTER COLUMN departmentID SET NOT NULL
ALTER TABLE InventoryDuration ALTER COLUMN id SET NOT NULL
ALTER TABLE InventoryDuration ALTER COLUMN amount SET NOT NULL
ALTER TABLE InventoryDuration ALTER COLUMN version SET NOT NULL
ALTER TABLE InventoryDuration ADD PRIMARY KEY (timestamp, validDate, dealerID, id, departmentID, durationID)
ALTER TABLE InventoryDuration ADD CONSTRAINT ID_Unique UNIQUE(timeEnd, validDate, dealerID, id, departmentID, durationID, version)

CREATE TABLE ReportTime (   id bigint auto_increment,    validDate date,   monthOfYear integer, year integer) 
CREATE TABLE ReportItem(   id bigint auto_increment,    name varchar(100),   sourceItemID integer NOT NULL,   itemSource integer NOT NULL,  itemCategory varchar(100) ,  CONSTRAINT ReportItem_PK PRIMARY KEY (id)) 
CREATE TABLE DealerIncomeRevenueFact (   timeID integer  NOT NULL,    dealerID integer  NOT NULL,   departmentID integer  NOT NULL,   itemID integer  NOT NULL,   version integer NOT NULL,   amount double,   margin double,   count integer,   timestamp timestamp NOT NULL,    timeEnd timestamp NOT NULL)
ALTER TABLE DealerIncomeRevenueFact ADD PRIMARY KEY (timestamp, timeID, dealerID, itemID, departmentID)
ALTER TABLE DealerIncomeRevenueFact ADD CONSTRAINT DIRF_Unique UNIQUE(timeEnd, timeID, dealerID, itemID, departmentID, version)

CREATE TABLE DealerIncomeExpenseFact (   timeID integer NOT NULL,    dealerID integer NOT NULL,   departmentID integer NOT NULL,   itemID integer NOT NULL,   version integer,   amount double,   timestamp timestamp,    timeEnd timestamp,   CONSTRAINT DIEF_Unique UNIQUE (timestamp, timeID, dealerID, departmentID, itemID, version)) 

