insert into EnumType values (1, 'EntitlementResourceType')
insert into EnumType values (3, 'DurationUnit')
insert into EnumType values (4, 'FeeType')
insert into EnumType values (5, 'JournalType')

insert into EnumValue values (1, 'Service', 0)
insert into EnumValue values (3, 'Days', 0)
insert into EnumValue values (3, 'Months', 1)
insert into EnumValue values (4, '标准工时收费', 0)
insert into EnumValue values (4, '有效工时比率', 1)
insert into EnumValue values (4, '技工平均工资', 2)
insert into EnumValue values (5, 'Revenue', 0)
insert into EnumValue values (5, 'Expense', 1)

insert into Menu values (1, 'DealerEntrySystem', 'DealerEntrySystemText')
insert into Menu values (2, 'DealerDataEntry', 'DealerDataEntryText')
insert into Menu values (3, 'DealerDataDisplay', 'DealerDataDisplayText')
insert into Menu values (4, 'Income', 'IncomeText')
insert into Menu values (5, 'Expense', 'ExpenseText')

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
