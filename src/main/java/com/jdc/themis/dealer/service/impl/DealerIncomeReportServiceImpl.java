package com.jdc.themis.dealer.service.impl;

import javax.time.calendar.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdc.themis.dealer.data.dao.ReportDAO;
import com.jdc.themis.dealer.service.DealerIncomeReportService;

@Service
public class DealerIncomeReportServiceImpl implements DealerIncomeReportService {

	@Autowired
	private ReportDAO reportDAL;

	public ReportDAO getReportDAL() {
		return reportDAL;
	}

	public void setReportDAL(ReportDAO reportDAL) {
		this.reportDAL = reportDAL;
	}

	@Override
	public void importReportData(Integer year, Integer monthOfYear) {
		reportDAL.importVehicleSalesJournal(LocalDate.of(year, monthOfYear, 1));
	}

}
