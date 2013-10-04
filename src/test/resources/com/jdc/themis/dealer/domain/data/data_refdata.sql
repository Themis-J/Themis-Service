insert into EnumType values (2, 'ReportItemSource')
insert into EnumType values (3, 'DurationUnit')
insert into EnumType values (4, 'FeeType')
insert into EnumType values (5, 'JournalType')

insert into EnumValue values (2, 'VehicleSalesJournal', 0)
insert into EnumValue values (2, 'SalesServiceJournal', 1)
insert into EnumValue values (2, 'GeneralJournal', 2)
INSERT INTO EnumValue VALUES (2, 'TaxJournal', 3)
insert into EnumValue values (3, 'Days', 0)
insert into EnumValue values (3, 'Months', 1)
insert into EnumValue values (4, '标准工时收费', 0)
insert into EnumValue values (4, '有效工时比率', 1)
insert into EnumValue values (4, '技工平均工资', 2)
insert into EnumValue values (5, 'Revenue', 0)
insert into EnumValue values (5, 'Expense', 1)
insert into EnumValue values (5, 'Other', 2)
insert into EnumValue values (6, 'Mini', 0)

insert into Menu values (1, 'DealerEntrySystem', 'DealerEntrySystemText')
insert into Menu values (2, 'DealerDataEntry', 'DealerDataEntryText')
insert into Menu values (3, 'DealerDataDisplay', 'DealerDataDisplayText')
insert into Menu values (4, 'Income', 'IncomeText')
insert into Menu values (5, 'Expense', 'ExpenseText')

insert into Vehicle values (1, '微型车 Mini', 1, 0, {ts '2012-09-17 18:47:52.69'})
insert into Vehicle values (2, '微型车 XXX', 1, 0, {ts '2012-09-17 18:47:52.69'})

insert into Duration values (1, 1, 0, 30)
insert into Duration values (2, 1, 31, 60)

insert into Dealer values (1, 'Dealer1', '', '', 'D01-1', {ts '2012-09-17 18:47:52.69'})
insert into Dealer values (2, 'Dealer2', '', '', 'D01-1', {ts '2012-09-17 18:47:52.69'})
insert into Dealer values (3, 'Dealer3', '', '', 'D01-1', {ts '2012-09-17 18:47:52.69'})
insert into Dealer values (4, 'Dealer4', '', '', 'D01-1', {ts '2012-09-17 18:47:52.69'})

insert into MenuHierachy values (1, 2, 1)
insert into MenuHierachy values (1, 3, 2)
insert into MenuHierachy values (2, 4, 3)
insert into MenuHierachy values (3, 5, 4)

insert into Department values (1, 'Department1', {ts '2012-09-17 18:47:52.69'})
insert into Department values (2, 'Department2', {ts '2012-09-17 18:47:52.69'})
insert into Department values (3, 'Department3', {ts '2012-09-17 18:47:52.69'})
insert into Department values (4, 'Department4', {ts '2012-09-17 18:47:52.69'})

insert into JobPosition values (1, 'JobPosition1', {ts '2012-09-17 18:47:52.69'})
insert into JobPosition values (2, 'JobPosition2', {ts '2012-09-17 18:47:52.69'})
insert into JobPosition values (3, 'JobPosition3', {ts '2012-09-17 18:47:52.69'})
insert into JobPosition values (4, 'JobPosition4', {ts '2012-09-17 18:47:52.69'})
insert into JobPosition values (5, 'JobPosition5', {ts '2012-09-17 18:47:52.69'})

insert into TaxJournalItem values (1, 'IncomeTax', {ts '2012-09-17 18:47:52.69'})

insert into SalesServiceJournalCategory values (1, 'SalesServiceJournalCate1', {ts '2012-09-17 18:47:52.69'})
insert into SalesServiceJournalCategory values (2, 'SalesServiceJournalCate2', {ts '2012-09-17 18:47:52.69'})

insert into GeneralJournalCategory values (1, 'GeneralJournalCate1', {ts '2012-09-17 18:47:52.69'})
insert into GeneralJournalCategory values (2, 'GeneralJournalCate2', {ts '2012-09-17 18:47:52.69'})

insert into SalesServiceJournalItem values (1, 'SalesServiceJournalItem1', 1, 0, 'XX', {ts '2012-09-17 18:47:52.69'})
insert into SalesServiceJournalItem values (2, 'SalesServiceJournalItem2', 1, 0, 'XX', {ts '2012-09-17 18:47:52.69'})
insert into SalesServiceJournalItem values (3, 'SalesServiceJournalItem3', 2, 1, 'XX', {ts '2012-09-17 18:47:52.69'})

insert into GeneralJournalItem values (1, 'GeneralJournalItem1', 1, 0, {ts '2012-09-17 18:47:52.69'})
insert into GeneralJournalItem values (2, 'GeneralJournalItem2', 2, 1, {ts '2012-09-17 18:47:52.69'})

insert into EmployeeFeeSummaryItem values (1, 'EmployeeFeeSummaryItem1', {ts '2012-09-17 18:47:52.69'})
insert into EmployeeFeeSummaryItem values (2, 'EmployeeFeeSummaryItem2', {ts '2012-09-17 18:47:52.69'})

insert into EmployeeFeeItem values (1, 'EmployeeFeeItem1', {ts '2012-09-17 18:47:52.69'})

insert into InventoryDurationItem values (1, 'InventoryDurationItem1', {ts '2012-09-17 18:47:52.69'})
insert into InventoryDurationItem values (2, 'InventoryDurationItem2', {ts '2012-09-17 18:47:52.69'})
insert into InventoryDurationItem values (3, 'InventoryDurationItem3', {ts '2012-09-17 18:47:52.69'})

insert into AccountReceivableDurationItem values (1, 'AccountReceivableDurationItem1', {ts '2012-09-17 18:47:52.69'})
insert into AccountReceivableDurationItem values (2, 'AccountReceivableDurationItem2', {ts '2012-09-17 18:47:52.69'})
