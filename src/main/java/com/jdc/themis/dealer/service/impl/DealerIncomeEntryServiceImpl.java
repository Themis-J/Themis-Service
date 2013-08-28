package com.jdc.themis.dealer.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.time.Instant;
import javax.time.calendar.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.DealerEntryItemStatus;
import com.jdc.themis.dealer.domain.GeneralJournal;
import com.jdc.themis.dealer.domain.SalesServiceJournal;
import com.jdc.themis.dealer.domain.TaxJournal;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;
import com.jdc.themis.dealer.service.DealerIncomeEntryService;
import com.jdc.themis.dealer.utils.Performance;
import com.jdc.themis.dealer.web.domain.DealerEntryItemStatusDetail;
import com.jdc.themis.dealer.web.domain.GeneralJournalDetail;
import com.jdc.themis.dealer.web.domain.GetDealerEntryItemStatusResponse;
import com.jdc.themis.dealer.web.domain.GetGeneralJournalResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceJournalResponse;
import com.jdc.themis.dealer.web.domain.GetTaxResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleSalesJournalResponse;
import com.jdc.themis.dealer.web.domain.SalesServiceJournalDetail;
import com.jdc.themis.dealer.web.domain.SaveDealerEntryItemStatusRequest;
import com.jdc.themis.dealer.web.domain.SaveGeneralIncomeRequest;
import com.jdc.themis.dealer.web.domain.SaveSalesServiceRevenueRequest;
import com.jdc.themis.dealer.web.domain.SaveTaxRequest;
import com.jdc.themis.dealer.web.domain.SaveVehicleSalesJournalRequest;
import com.jdc.themis.dealer.web.domain.VehicleSalesJournalDetail;

@Service
public class DealerIncomeEntryServiceImpl implements DealerIncomeEntryService {

	@Autowired
	private RefDataDAO refDataDAL;
	@Autowired
	private IncomeJournalDAO incomeJournalDAL;

	public RefDataDAO getRefDataDAL() {
		return refDataDAL;
	}

	public void setRefDataDAL(RefDataDAO refDataDAL) {
		this.refDataDAL = refDataDAL;
	}

	public IncomeJournalDAO getIncomeJournalDAL() {
		return incomeJournalDAL;
	}

	public void setIncomeJournalDAL(IncomeJournalDAO incomeJournalDAL) {
		this.incomeJournalDAL = incomeJournalDAL;
	}

	/**
	 * Save a list of vehicle sales revenue.
	 * 
	 * @param request
	 * @return
	 */
	@Performance
	@Override
	public Instant saveVehicleSalesRevenue(
			final SaveVehicleSalesJournalRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
		Preconditions.checkNotNull(request.getValidDate(),
				"valid date can't be null");
		Preconditions.checkArgument(request.getDetail().size() != 0,
				"no detail is posted");
		Preconditions.checkArgument(
				refDataDAL.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());

		final List<VehicleSalesJournal> journals = Lists.newArrayList();
		final Integer requestedDeparmentID = request.getDepartmentID() == null ? DEFAULT_VEHICLE_DEPARTMENT_ID
				: request.getDepartmentID();
		for (final VehicleSalesJournalDetail detail : request.getDetail()) {
			final VehicleSalesJournal journal = new VehicleSalesJournal();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setMargin(detail.getMargin() != null ? new BigDecimal(detail.getMargin()) : BigDecimal.ZERO);
			journal.setCount(detail.getCount() != null ? detail.getCount() : 0);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getVehicleID(),
					"vehicle id can't be null");
			journal.setId(detail.getVehicleID());
			journal.setDepartmentID(requestedDeparmentID);
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveVehicleSalesJournal(
				request.getDealerID(), requestedDeparmentID, journals);
		if (timestamp == null) {
			throw new RuntimeException("Database save returns null timestamp!");
		}
		return timestamp;
	}

	private final static Integer DEFAULT_VEHICLE_DEPARTMENT_ID = 1; // new
																	// vehicle
																	// department

	/**
	 * Get a list of vehicle sales revenue per vehicle type.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Performance
	@Override
	public GetVehicleSalesJournalResponse getVehicleSalesRevenue(
			Integer dealerID, Integer departmentID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		final GetVehicleSalesJournalResponse response = new GetVehicleSalesJournalResponse();
		final Integer requestedDeparmentID = departmentID == null ? DEFAULT_VEHICLE_DEPARTMENT_ID
				: departmentID;
		final Collection<VehicleSalesJournal> list = incomeJournalDAL
				.getVehicleSalesJournal(dealerID, requestedDeparmentID,
						LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setDepartmentID(requestedDeparmentID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final VehicleSalesJournal journal : list) {
			final VehicleSalesJournalDetail item = new VehicleSalesJournalDetail();
			item.setAmount(journal.getAmount().doubleValue());
			item.setCount(journal.getCount());
			item.setMargin(journal.getMargin().doubleValue());
			item.setName(refDataDAL.getVehicle(journal.getId()).getName());
			item.setVehicleID(journal.getId());
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}
		return response;
	}

	/**
	 * Save a list of sales & service revenue.
	 * 
	 * @param request
	 * @return
	 */
	@Performance
	@Override
	public Instant saveSalesServiceRevenue(
			final SaveSalesServiceRevenueRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
		Preconditions.checkNotNull(request.getDepartmentID(),
				"department id can't be null");
		Preconditions.checkNotNull(request.getValidDate(),
				"valid date can't be null");
		Preconditions.checkNotNull(request.getDetail().size() == 0,
				"no detail is posted");
		Preconditions.checkArgument(
				refDataDAL.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());
		Preconditions.checkArgument(
				refDataDAL.getDepartment(request.getDepartmentID()) != null,
				"unknown department id " + request.getDepartmentID());

		final List<SalesServiceJournal> journals = Lists.newArrayList();
		for (final SalesServiceJournalDetail detail : request.getDetail()) {
			final SalesServiceJournal journal = new SalesServiceJournal();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setMargin(detail.getMargin() != null ? new BigDecimal(detail.getMargin()) : BigDecimal.ZERO);
			journal.setCount(detail.getCount() != null ? detail.getCount() : 0);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			journal.setId(detail.getItemID());
			journal.setDepartmentID(request.getDepartmentID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);

		}
		final Instant timestamp = incomeJournalDAL.saveSalesServiceJournal(
				request.getDealerID(), request.getDepartmentID(), journals);
		if (timestamp == null) {
			throw new RuntimeException("Database save returns null timestamp!");
		}

		return timestamp;
	}

	/**
	 * Get a list sales & service revenue per sales item.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Override
	@Performance
	public GetSalesServiceJournalResponse getSalesServiceRevenue(
			Integer dealerID, Integer departmentID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(departmentID, "department id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		final GetSalesServiceJournalResponse response = new GetSalesServiceJournalResponse();
		final Collection<SalesServiceJournal> list = incomeJournalDAL
				.getSalesServiceJournal(dealerID, departmentID,
						LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setDepartmentID(departmentID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final SalesServiceJournal journal : list) {
			final SalesServiceJournalDetail item = new SalesServiceJournalDetail();
			item.setAmount(journal.getAmount().doubleValue());
			item.setCount(journal.getCount());
			item.setMargin(journal.getMargin().doubleValue());
			item.setName(refDataDAL.getSalesServiceJournalItem(journal.getId())
					.getName());
			item.setItemID(journal.getId());
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}

	/**
	 * Save income tax.
	 * 
	 * @param request
	 * @return
	 */
	@Override
	@Performance
	public Instant saveIncomeTax(final SaveTaxRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
		Preconditions.checkNotNull(request.getValidDate(),
				"valid date can't be null");
		Preconditions
				.checkNotNull(request.getTax(), "tax amount can't be null");
		Preconditions.checkArgument(
				refDataDAL.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());

		final List<TaxJournal> journals = Lists.newArrayList();
		final TaxJournal taxJournal = new TaxJournal();
		taxJournal.setAmount(new BigDecimal(request.getTax()));
		taxJournal.setDealerID(request.getDealerID());
		taxJournal.setUpdatedBy(request.getUpdateBy());
		taxJournal.setValidDate(LocalDate.parse(request.getValidDate()));
		journals.add(taxJournal);
		final Instant timestamp = incomeJournalDAL.saveTaxJournal(
				request.getDealerID(), journals);
		if (timestamp == null) {
			throw new RuntimeException("Database save returns null timestamp!");
		}
		return timestamp;
	}

	/**
	 * Get a list sales & service revenue per sales item.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Override
	@Performance
	public GetTaxResponse getIncomeTax(Integer dealerID, String validDate) {
		final GetTaxResponse response = new GetTaxResponse();
		final Collection<TaxJournal> list = incomeJournalDAL.getTaxJournal(
				dealerID, LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final TaxJournal journal : list) {
			response.setTax(journal.getAmount().doubleValue());
		}
		return response;
	}

	/**
	 * Save dealer entry item status.
	 * 
	 * @param request
	 * @return
	 */
	@Override
	@Performance
	public Instant saveDealerEntryItemStatus(
			final SaveDealerEntryItemStatusRequest request) {
		Preconditions.checkArgument(
				refDataDAL.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());

		final List<DealerEntryItemStatus> journals = Lists.newArrayList();
		final DealerEntryItemStatus journal = new DealerEntryItemStatus();
		journal.setEntryItemID(request.getItemID());
		journal.setDealerID(request.getDealerID());
		journal.setUpdateBy(request.getUpdateBy());
		journal.setValidDate(LocalDate.parse(request.getValidDate()));
		journals.add(journal);
		final Instant timestamp = incomeJournalDAL.saveDealerEntryItemStatus(
				request.getDealerID(), journals);
		if (timestamp == null) {
			throw new RuntimeException("Database save returns null timestamp!");
		}
		return timestamp;
	}

	/**
	 * Get dealer entry item status.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Override
	@Performance
	public GetDealerEntryItemStatusResponse getDealerEntryItemStatus(
			Integer dealerID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		final GetDealerEntryItemStatusResponse response = new GetDealerEntryItemStatusResponse();
		final Collection<DealerEntryItemStatus> list = incomeJournalDAL
				.getDealerEntryItemStatus(dealerID, LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final DealerEntryItemStatus journal : list) {
			final DealerEntryItemStatusDetail status = new DealerEntryItemStatusDetail();
			status.setId(journal.getEntryItemID());
			status.setName(refDataDAL.getMenu(status.getId()).getName());
			status.setTimestamp(journal.getTimestamp());
			response.getDetail().add(status);
		}
		return response;
	}

	@Override
	@Performance
	public Instant saveGeneralIncome(SaveGeneralIncomeRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
		Preconditions.checkNotNull(request.getDepartmentID(),
				"department id can't be null");
		Preconditions.checkNotNull(request.getValidDate(),
				"valid date can't be null");
		Preconditions.checkNotNull(request.getDetail().size() == 0,
				"no detail is posted");
		Preconditions.checkArgument(
				refDataDAL.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());
		Preconditions.checkArgument(
				refDataDAL.getDepartment(request.getDepartmentID()) != null,
				"unknown department id " + request.getDepartmentID());

		
		final List<GeneralJournal> journals = Lists.newArrayList();
		for (final GeneralJournalDetail detail : request.getDetail()) {
			final GeneralJournal journal = new GeneralJournal();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			journal.setId(detail.getItemID());
			journal.setDepartmentID(request.getDepartmentID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveGeneralJournal(
				request.getDealerID(), request.getDepartmentID(), journals);
		if (timestamp == null) {
			throw new RuntimeException("Database save returns null timestamp!");
		}

		return timestamp;
	}

	@Override
	@Performance
	public GetGeneralJournalResponse getGeneralIncome(Integer dealerID,
			Integer departmentID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(departmentID, "department id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		
		final GetGeneralJournalResponse response = new GetGeneralJournalResponse();
		final Collection<GeneralJournal> list = incomeJournalDAL
				.getGeneralJournal(dealerID, departmentID,
						LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setDepartmentID(departmentID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final GeneralJournal journal : list) {
			final GeneralJournalDetail item = new GeneralJournalDetail();
			item.setAmount(journal.getAmount().doubleValue());
			item.setName(refDataDAL.getSalesServiceJournalItem(journal.getId())
					.getName());
			item.setItemID(journal.getId());
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}
}
