ALTER TABLE  GeneralJournalItem ADD  tooltip VARCHAR( 2048 ) default '<p>暂无说明</p>' NULL;
ALTER TABLE  SalesServiceJournalItem ADD  tooltip VARCHAR( 2048 ) NULL;
ALTER TABLE  AccountReceivableDurationItem   ADD  tooltip VARCHAR( 2048 ) default '<p>暂无说明</p>' NULL;
ALTER TABLE  InventoryDurationItem    ADD  tooltip VARCHAR( 2048 ) default '<p>暂无说明</p>' NULL;
ALTER TABLE  EmployeeFeeSummaryItem  ADD  tooltip VARCHAR( 2048 ) default '<p>暂无说明</p>' NULL;
