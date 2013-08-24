CREATE TABLE Menu (id integer PRIMARY KEY, name varchar(100) NOT NULL, displayText varchar(100), link varchar(200)) 
CREATE TABLE MenuHierachy (parentID integer, childID integer, itemOrder integer) 

CREATE TABLE TaxJournalItem (id integer, name varchar(100), timestamp timestamp)

CREATE TABLE TaxJournal (timestamp timestamp, timeEnd timestamp, validDate date, dealerID integer, id integer, amount double, updateBy varchar(50))
ALTER TABLE TaxJournal ALTER COLUMN timestamp SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN timeEnd SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN validDate SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN dealerID SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN id SET NOT NULL
ALTER TABLE TaxJournal ALTER COLUMN amount SET NOT NULL