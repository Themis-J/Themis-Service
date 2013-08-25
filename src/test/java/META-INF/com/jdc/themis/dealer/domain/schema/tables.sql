CREATE TABLE Menu (id integer PRIMARY KEY, name varchar(100) NOT NULL, displayText varchar(100)) 
CREATE TABLE MenuHierachy (parentID integer, childID integer, itemOrder integer) 

CREATE TABLE TaxJournalItem (id integer, name varchar(100), timestamp timestamp)

CREATE TABLE TaxJournal (timestamp timestamp, timeEnd timestamp, validDate date, dealerID integer, id integer, amount double, updateBy varchar(50))
ALTER TABLE TaxJournal ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN validDate SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN id SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN amount SET NOT NULL

CREATE TABLE DealerEntryItemStatus (timestamp timestamp, timeEnd timestamp, validDate date, dealerID integer, entryItemID integer, updateBy varchar(100)) 
ALTER TABLE DealerEntryItemStatus ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE DealerEntryItemStatus ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE DealerEntryItemStatus ALTER COLUMN validDate SET NOT NULL
ALTER TABLE DealerEntryItemStatus ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE DealerEntryItemStatus ALTER COLUMN entryItemID SET NOT NULL