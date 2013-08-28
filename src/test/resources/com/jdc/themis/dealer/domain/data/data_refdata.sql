insert into Menu values (1, 'DealerEntrySystem', 'DealerEntrySystemText')
insert into Menu values (2, 'DealerDataEntry', 'DealerDataEntryText')
insert into Menu values (3, 'DealerDataDisplay', 'DealerDataDisplayText')
insert into Menu values (4, 'Income', 'IncomeText')
insert into Menu values (5, 'Expense', 'ExpenseText')

insert into MenuHierachy values (1, 2, 1)
insert into MenuHierachy values (1, 3, 2)
insert into MenuHierachy values (2, 4, 3)
insert into MenuHierachy values (3, 5, 4)

insert into TaxJournalItem values (1, 'IncomeTax', {ts '2012-09-17 18:47:52.69'})

insert into SalesServiceJournalItem values (1, 'SalesServiceJournalItem1', 1, {ts '2012-09-17 18:47:52.69'})
insert into SalesServiceJournalItem values (2, 'SalesServiceJournalItem2', 1, {ts '2012-09-17 18:47:52.69'})

insert into GeneralJournalItem values (1, 'GeneralJournalItem1', 1, {ts '2012-09-17 18:47:52.69'})
insert into GeneralJournalItem values (2, 'GeneralJournalItem2', 1, {ts '2012-09-17 18:47:52.69'})

insert into EmployeeFeeSummaryItem values (1, 'EmployeeFeeSummaryItem1', {ts '2012-09-17 18:47:52.69'})
insert into EmployeeFeeSummaryItem values (2, 'EmployeeFeeSummaryItem2', {ts '2012-09-17 18:47:52.69'})

insert into EmployeeFeeItem values (1, 'EmployeeFeeItem1', {ts '2012-09-17 18:47:52.69'})

insert into InventoryDurationItem values (1, 'InventoryDurationItem1', {ts '2012-09-17 18:47:52.69'})
insert into InventoryDurationItem values (2, 'InventoryDurationItem2', {ts '2012-09-17 18:47:52.69'})
insert into InventoryDurationItem values (3, 'InventoryDurationItem3', {ts '2012-09-17 18:47:52.69'})

insert into AccountReceivableDurationItem values (1, 'AccountReceivableDurationItem1', {ts '2012-09-17 18:47:52.69'})
insert into AccountReceivableDurationItem values (2, 'AccountReceivableDurationItem2', {ts '2012-09-17 18:47:52.69'})
