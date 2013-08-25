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