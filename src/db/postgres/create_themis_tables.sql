DROP TABLE IF EXISTS public.EnumValue CASCADE;
DROP TABLE IF EXISTS public.EnumType CASCADE;

CREATE TABLE public.EnumType
(
   id integer, 
   name character varying(100), 
   CONSTRAINT EnumType_PK PRIMARY KEY (id), 
   CONSTRAINT EnumType_Unique UNIQUE (name)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE public.EnumValue
(
   typeID integer, 
   name character varying(100), 
   value integer, 
   CONSTRAINT EnumValue_PK PRIMARY KEY (typeID, value), 
   CONSTRAINT EnumValue_FK FOREIGN KEY (typeID) REFERENCES EnumType (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS public.UserInfo CASCADE;
DROP TABLE IF EXISTS public.Dealer CASCADE;
DROP TABLE IF EXISTS public.UserDealer CASCADE;

CREATE TABLE public.UserInfo
(
   id integer, 
   username character varying(20), 
   password character varying(50), 
   userType integer, 
   active boolean, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT UserID_PK PRIMARY KEY (id), 
   CONSTRAINT Username_Unique UNIQUE (username)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE public.Dealer
(
   id integer, 
   name character varying(100) NOT NULL, 
   fullName character varying(250) NOT NULL, 
   code character varying(10) NOT NULL, 
   city character varying(20), 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT Dealer_PK PRIMARY KEY (id), 
   CONSTRAINT DealerName_Unique UNIQUE (name), 
   CONSTRAINT DealerCode_Unique UNIQUE (code)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE public.UserDealer
(
   userID integer, 
   dealerID integer, 
   CONSTRAINT User_FK FOREIGN KEY (userID) REFERENCES UserInfo (id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT dealerID FOREIGN KEY (dealerID) REFERENCES Dealer (id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT UserDealer_Unique UNIQUE (userID, dealerID)
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS public.Department CASCADE;

CREATE TABLE public.Department
(
   id integer, 
   name character varying(100), 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT Department_PK PRIMARY KEY (id), 
   CONSTRAINT Department_Unique UNIQUE (name)
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS public.MenuHierachy CASCADE;
DROP TABLE IF EXISTS public.Menu CASCADE;

CREATE TABLE public.Menu
(
   id integer, 
   name character varying(100) NOT NULL, 
   displayText character varying(100), 
   CONSTRAINT Menu_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE public.MenuHierachy
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

DROP TABLE IF EXISTS public.DealerEntryItemStatus CASCADE;
CREATE TABLE public.DealerEntryItemStatus
(
   timestamp timestamp without time zone, 
   timeEnd timestamp without time zone, 
   validDate date, 
   dealerID integer, 
   entryItemID integer NOT NULL, 
   updateBy character varying(100),
   CONSTRAINT DEIS_Unique UNIQUE (dealerID, entryItemID, validDate)
) 
WITH (
  OIDS = FALSE
)
;


DROP TABLE IF EXISTS public.HumanResourceAllocation CASCADE;
DROP TABLE IF EXISTS public.JobPosition CASCADE;

CREATE TABLE public.JobPosition
(
   id integer, 
   name character varying(100),
   timestamp timestamp without time zone, 
   CONSTRAINT JobPosition_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.HumanResourceAllocation
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   allocation double precision,
   updatedBy character varying(20) NOT NULL, 
   CONSTRAINT HRA_Unique UNIQUE (timestamp, validDate, dealerID, departmentID, id)
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS public.GeneralJournal CASCADE;
DROP TABLE IF EXISTS public.GeneralJournalItem CASCADE;
DROP TABLE IF EXISTS public.GeneralJournalCategory CASCADE;
DROP TABLE IF EXISTS public.SalesServiceJournal CASCADE;
DROP TABLE IF EXISTS public.SalesServiceJournalItem CASCADE;
DROP TABLE IF EXISTS public.SalesServiceJournalCategory CASCADE;
DROP TABLE IF EXISTS public.VehicleSalesJournal CASCADE;
DROP TABLE IF EXISTS public.VehicleJournalItem CASCADE;
DROP TABLE IF EXISTS public.Vehicle CASCADE;


CREATE TABLE public.GeneralJournalCategory
(
   id integer, 
   name character varying(100), 
   categoryType integer, 
   timestamp timestamp without time zone, 
   CONSTRAINT GJC_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE public.GeneralJournalItem
(
   id integer, 
   name character varying(100), 
   categoryID integer, 
   timestamp timestamp without time zone, 
   CONSTRAINT GJI_PK PRIMARY KEY (id), 
   CONSTRAINT GJI_FK FOREIGN KEY (categoryID) REFERENCES GeneralJournalCategory (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.GeneralJournal
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   amount double precision,
   updatedBy character varying(20) NOT NULL, 
   CONSTRAINT GeneralJournal_Unique UNIQUE (timestamp, validDate, dealerID, departmentID, id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.SalesServiceJournalCategory
(
   id integer, 
   name character varying(100), 
   timestamp timestamp without time zone, 
   CONSTRAINT SSJI_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.SalesServiceJournalItem
(
   id integer, 
   name character varying(100), 
   categoryID integer,
   timestamp timestamp without time zone, 
   CONSTRAINT SSJLI_PK PRIMARY KEY (id),
   CONSTRAINT SSJI_FK FOREIGN KEY (categoryID) REFERENCES SalesServiceJournalCategory (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.Vehicle
(
   id integer, 
   name character varying(100) NOT NULL, 
   categoryID integer NOT NULL, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT Vehicle_PK PRIMARY KEY (id), 
   CONSTRAINT SSJI_FK FOREIGN KEY (categoryID) REFERENCES SalesServiceJournalCategory (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.SalesServiceJournal
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   amount double precision,
   margin double precision,
   count integer,
   updatedBy character varying(20) NOT NULL, 
   CONSTRAINT SSJ_Unique UNIQUE (timestamp, validDate, dealerID, departmentID, id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.VehicleSalesJournal
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   amount double precision,
   margin double precision,
   count integer,
   updatedBy character varying(20) NOT NULL, 
   CONSTRAINT VSJ_Unique UNIQUE (timestamp, validDate, dealerID, departmentID, id)
) 
WITH (
  OIDS = FALSE
)
;


DROP TABLE IF EXISTS public.AccountReceivableDuration CASCADE;
DROP TABLE IF EXISTS public.AccountReceivableDurationItem CASCADE;
DROP TABLE IF EXISTS public.EmployeeFee CASCADE;
DROP TABLE IF EXISTS public.EmployeeFeeItem CASCADE;
DROP TABLE IF EXISTS public.EmployeeFeeSummary CASCADE;
DROP TABLE IF EXISTS public.EmployeeFeeSummaryItem CASCADE;
DROP TABLE IF EXISTS public.InventoryDuration CASCADE;
DROP TABLE IF EXISTS public.InventoryDurationItem CASCADE;
DROP TABLE IF EXISTS public.Duration CASCADE;


CREATE TABLE public.Duration
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

CREATE TABLE public.AccountReceivableDurationItem
(
   id integer, 
   name character varying(100) NOT NULL, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT ARDI_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE public.AccountReceivableDuration
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   durationID integer NOT NULL,
   id integer NOT NULL,
   amount double precision,
   updatedBy character varying(20) NOT NULL, 
   CONSTRAINT ARDuration_Unique UNIQUE (timestamp, validDate, dealerID, durationID, id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.EmployeeFeeItem
(
   id integer, 
   name character varying(100) NOT NULL, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT EFI_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.EmployeeFee
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   feeTypeID NOT NULL, 
   amount double precision,
   updatedBy character varying(20) NOT NULL, 
   CONSTRAINT EF_Unique UNIQUE (timestamp, validDate, dealerID, departmentID, id, feeTypeID)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE public.EmployeeFeeSummaryItem
(
   id integer, 
   name character varying(100) NOT NULL, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT EFSI_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.EmployeeFeeSummary
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   id integer NOT NULL,
   amount double precision,
   updatedBy character varying(20) NOT NULL, 
   CONSTRAINT EFS_Unique UNIQUE (timestamp, validDate, dealerID, departmentID, id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.InventoryDurationItem
(
   id integer, 
   name character varying(100) NOT NULL, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT IDI_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;

CREATE TABLE public.InventoryDuration
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   departmentID integer NOT NULL,
   durationID integer NOT NULL,
   id integer NOT NULL,
   amount double precision,
   count integer,
   updatedBy character varying(20) NOT NULL, 
   CONSTRAINT ID_Unique UNIQUE (timestamp, validDate, dealerID, departmentID, durationID, id)
) 
WITH (
  OIDS = FALSE
)
;

DROP TABLE IF EXISTS public.TaxJournalItem CASCADE;
DROP TABLE IF EXISTS public.TaxJournal CASCADE;
CREATE TABLE public.TaxJournalItem
(
   id integer, 
   name character varying(100) NOT NULL, 
   timestamp timestamp without time zone NOT NULL, 
   CONSTRAINT TaxJI_PK PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
CREATE TABLE public.TaxJournal
(
   timestamp timestamp without time zone NOT NULL, 
   timeEnd timestamp without time zone NOT NULL, 
   validDate date NOT NULL, 
   dealerID integer NOT NULL,
   id integer NOT NULL,
   amount double precision,
   updatedBy character varying(20) NOT NULL, 
   CONSTRAINT TaxJ_Unique UNIQUE (timestamp, validDate, dealerID, id)
) 
WITH (
  OIDS = FALSE
)
;