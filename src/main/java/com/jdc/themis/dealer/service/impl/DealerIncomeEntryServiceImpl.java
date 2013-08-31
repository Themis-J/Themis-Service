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
import com.jdc.themis.dealer.domain.AccountReceivableDuration;
import com.jdc.themis.dealer.domain.DealerEntryItemStatus;
import com.jdc.themis.dealer.domain.EmployeeFee;
import com.jdc.themis.dealer.domain.EmployeeFeeSummary;
import com.jdc.themis.dealer.domain.GeneralJournal;
import com.jdc.themis.dealer.domain.HumanResourceAllocation;
import com.jdc.themis.dealer.domain.InventoryDuration;
import com.jdc.themis.dealer.domain.SalesServiceJournal;
import com.jdc.themis.dealer.domain.TaxJournal;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;
import com.jdc.themis.dealer.service.DealerIncomeEntryService;
import com.jdc.themis.dealer.utils.Performance;
import com.jdc.themis.dealer.utils.Utils;
import com.jdc.themis.dealer.web.domain.AccountReceivableDurationDetail;
import com.jdc.themis.dealer.web.domain.DealerEntryItemStatusDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeSummaryDetail;
import com.jdc.themis.dealer.web.domain.GeneralJournalDetail;
import com.jdc.themis.dealer.web.domain.GetAccountReceivableDurationResponse;
import com.jdc.themis.dealer.web.domain.GetDealerEntryItemStatusResponse;
import com.jdc.themis.dealer.web.domain.GetEmployeeFeeResponse;
import com.jdc.themis.dealer.web.domain.GetEmployeeFeeSummaryResponse;
import com.jdc.themis.dealer.web.domain.GetGeneralJournalResponse;
import com.jdc.themis.dealer.web.domain.GetHumanResourceAllocationResponse;
import com.jdc.themis.dealer.web.domain.GetInventoryDurationResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceJournalResponse;
import com.jdc.themis.dealer.web.domain.GetTaxResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleSalesJournalResponse;
import com.jdc.themis.dealer.web.domain.HumanResourceAllocationDetail;
import com.jdc.themis.dealer.web.domain.InventoryDurationDetail;
import com.jdc.themis.dealer.web.domain.SalesServiceJournalDetail;
import com.jdc.themis.dealer.web.domain.SaveAccountReceivableDurationRequest;
import com.jdc.themis.dealer.web.domain.SaveDealerEntryItemStatusRequest;
import com.jdc.themis.dealer.web.domain.SaveEmployeeFeeRequest;
import com.jdc.themis.dealer.web.domain.SaveEmployeeFeeSummaryRequest;
import com.jdc.themis.dealer.web.domain.SaveGeneralJournalRequest;
import com.jdc.themis.dealer.web.domain.SaveHumanResourceAllocationRequest;
import com.jdc.themis.dealer.web.domain.SaveInventoryDurationRequest;
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
		Preconditions.checkArgument(
				refDataDAL.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		if ( departmentID != null ) {
			Preconditions.checkArgument(
				refDataDAL.getDepartment(departmentID) != null,
				"unknown department id " + departmentID);
		}
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
		Preconditions.checkArgument(
				refDataDAL.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		Preconditions.checkArgument(
				refDataDAL.getDepartment(departmentID) != null,
				"unknown department id " + departmentID);
		
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
		Preconditions.checkArgument(
				refDataDAL.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		
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
		Preconditions.checkArgument(
				refDataDAL.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);

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
	public Instant saveGeneralIncome(SaveGeneralJournalRequest request) {
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
			Preconditions.checkNotNull(refDataDAL.getGeneralJournalItem(detail.getItemID()),
					"unknown item id " + detail.getItemID());
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
		Preconditions.checkArgument(
				refDataDAL.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		Preconditions.checkArgument(
				refDataDAL.getDepartment(departmentID) != null,
				"unknown department id " + departmentID);
		
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
			item.setName(refDataDAL.getGeneralJournalItem(journal.getId())
					.getName());
			item.setItemID(journal.getId());
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}

	@Override
	public Instant saveAccountReceivableDuration (
			SaveAccountReceivableDurationRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
		Preconditions.checkNotNull(request.getValidDate(),
				"valid date can't be null");
		Preconditions.checkNotNull(request.getDetail().size() == 0,
				"no detail is posted");
		Preconditions.checkArgument(
				refDataDAL.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());
		
		final List<AccountReceivableDuration> journals = Lists.newArrayList();
		for (final AccountReceivableDurationDetail detail : request.getDetail()) {
			final AccountReceivableDuration journal = new AccountReceivableDuration();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			Preconditions.checkNotNull(refDataDAL.getAccountReceivableDurationItem(detail.getItemID()),
					"unknown item id " + detail.getItemID());
			Preconditions.checkNotNull(refDataDAL.getDuration(detail.getDurationID()),
					"unknown duration id " + detail.getDurationID());
			journal.setId(detail.getItemID());
			journal.setDurationID(detail.getDurationID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveAccountReceivableDuration(
				request.getDealerID(), journals);
		if (timestamp == null) {
			throw new RuntimeException("Database save returns null timestamp!");
		}

		return timestamp;
	}

	@Override
	public GetAccountReceivableDurationResponse getAccountReceivableDuration(
			Integer dealerID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataDAL.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);

		final GetAccountReceivableDurationResponse response = new GetAccountReceivableDurationResponse();
		final Collection<AccountReceivableDuration> list = incomeJournalDAL
				.getAccountReceivableDuration(dealerID, 
						LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final AccountReceivableDuration journal : list) {
			final AccountReceivableDurationDetail item = new AccountReceivableDurationDetail();
			item.setAmount(journal.getAmount().doubleValue());
			item.setName(refDataDAL.getAccountReceivableDurationItem(journal.getId())
					.getName());
			item.setItemID(journal.getId());
			item.setDurationID(journal.getDurationID());
			item.setDurationDesc(Utils.getDurationDesc(refDataDAL.getDuration(journal.getDurationID()), refDataDAL));
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}

	@Override
	public Instant saveInventoryDuration(SaveInventoryDurationRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
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
		
		final List<InventoryDuration> journals = Lists.newArrayList();
		for (final InventoryDurationDetail detail : request.getDetail()) {
			final InventoryDuration journal = new InventoryDuration();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			Preconditions.checkNotNull(refDataDAL.getInventoryDurationItem(detail.getItemID()),
					"unknown item id " + detail.getItemID());
			Preconditions.checkNotNull(refDataDAL.getDuration(detail.getDurationID()),
					"unknown duration id " + detail.getDurationID());
			journal.setId(detail.getItemID());
			journal.setDepartmentID(request.getDepartmentID());
			journal.setDurationID(detail.getDurationID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveInventoryDuration(
				request.getDealerID(), request.getDepartmentID(), journals);
		if (timestamp == null) {
			throw new RuntimeException("Database save returns null timestamp!");
		}

		return timestamp;
	}

	@Override
	public GetInventoryDurationResponse getInventoryDuration(Integer dealerID,
			Integer departmentID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataDAL.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		Preconditions.checkArgument(
				refDataDAL.getDepartment(departmentID) != null,
				"unknown department id " + departmentID);
		
		final GetInventoryDurationResponse response = new GetInventoryDurationResponse();
		final Collection<InventoryDuration> list = incomeJournalDAL
				.getInventoryDuration(dealerID, departmentID,
						LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setDeparmentID(departmentID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final InventoryDuration journal : list) {
			final InventoryDurationDetail item = new InventoryDurationDetail();
			item.setAmount(journal.getAmount().doubleValue());
			item.setName(refDataDAL.getInventoryDurationItem(journal.getId())
					.getName());
			item.setItemID(journal.getId());
			item.setDurationID(journal.getDurationID());
			item.setDurationDesc(Utils.getDurationDesc(refDataDAL.getDuration(journal.getDurationID()), refDataDAL));
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}

	@Override
	public Instant saveEmployeeFee(SaveEmployeeFeeRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
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
		
		final List<EmployeeFee> journals = Lists.newArrayList();
		for (final EmployeeFeeDetail detail : request.getDetail()) {
			final EmployeeFee journal = new EmployeeFee();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			Preconditions.checkArgument(
					refDataDAL.getEmployeeFeeItem(detail.getItemID()) != null,
					"unknown item id " + detail.getItemID());
			Preconditions.checkArgument(
					refDataDAL.getEnumValue("FeeType", detail.getFeeTypeID()) != null,
					"unknown fee type id " + detail.getFeeTypeID());
			journal.setId(detail.getItemID());
			journal.setFeeTypeID(detail.getFeeTypeID());
			journal.setDepartmentID(request.getDepartmentID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveEmployeeFee(
				request.getDealerID(), request.getDepartmentID(), journals);
		if (timestamp == null) {
			throw new RuntimeException("Database save returns null timestamp!");
		}

		return timestamp;
	}

	@Override
	public GetEmployeeFeeResponse getEmployeeFee(Integer dealerID,
			Integer departmentID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataDAL.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		Preconditions.checkArgument(
				refDataDAL.getDepartment(departmentID) != null,
				"unknown department id " + departmentID);
		
		final GetEmployeeFeeResponse response = new GetEmployeeFeeResponse();
		final Collection<EmployeeFee> list = incomeJournalDAL
				.getEmployeeFee(dealerID, departmentID,
						LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setDepartmentID(departmentID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final EmployeeFee journal : list) {
			final EmployeeFeeDetail item = new EmployeeFeeDetail();
			item.setAmount(journal.getAmount().doubleValue());
			item.setName(refDataDAL.getEmployeeFeeItem(journal.getId())
					.getName());
			item.setItemID(journal.getId());
			item.setFeeTypeID(journal.getFeeTypeID());
			item.setFeeType(refDataDAL.getEnumValue("FeeType", journal.getFeeTypeID()).getName());
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}

	@Override
	public Instant saveEmployeeFeeSummary(SaveEmployeeFeeSummaryRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
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
		
		final List<EmployeeFeeSummary> journals = Lists.newArrayList();
		for (final EmployeeFeeSummaryDetail detail : request.getDetail()) {
			final EmployeeFeeSummary journal = new EmployeeFeeSummary();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			Preconditions.checkArgument(
					refDataDAL.getEmployeeFeeSummaryItem(detail.getItemID()) != null,
					"unknown item id " + detail.getItemID());
			journal.setId(detail.getItemID());
			journal.setDepartmentID(request.getDepartmentID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveEmployeeFeeSummary(
				request.getDealerID(), request.getDepartmentID(), journals);
		if (timestamp == null) {
			throw new RuntimeException("Database save returns null timestamp!");
		}

		return timestamp;
	}

	@Override
	public GetEmployeeFeeSummaryResponse getEmployeeFeeSummary(
			Integer dealerID, Integer departmentID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataDAL.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		Preconditions.checkArgument(
				refDataDAL.getDepartment(departmentID) != null,
				"unknown department id " + departmentID);
		
		final GetEmployeeFeeSummaryResponse response = new GetEmployeeFeeSummaryResponse();
		final Collection<EmployeeFeeSummary> list = incomeJournalDAL
				.getEmployeeFeeSummary(dealerID, departmentID,
						LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setDepartmentID(departmentID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final EmployeeFeeSummary journal : list) {
			final EmployeeFeeSummaryDetail item = new EmployeeFeeSummaryDetail();
			item.setAmount(journal.getAmount().doubleValue());
			item.setName(refDataDAL.getEmployeeFeeSummaryItem(journal.getId())
					.getName());
			item.setItemID(journal.getId());
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}

	@Override
	public Instant saveHumanResourceAllocation(
			SaveHumanResourceAllocationRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
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
		
		final List<HumanResourceAllocation> journals = Lists.newArrayList();
		for (final HumanResourceAllocationDetail detail : request.getDetail()) {
			final HumanResourceAllocation journal = new HumanResourceAllocation();
			journal.setAllocation(detail.getAllocation() != null ? new BigDecimal(detail.getAllocation()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			Preconditions.checkArgument(
					refDataDAL.getJobPosition(detail.getItemID()) != null,
					"unknown job position id " + detail.getItemID());
			journal.setId(detail.getItemID());
			journal.setDepartmentID(request.getDepartmentID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveHumanResourceAllocation(
				request.getDealerID(), request.getDepartmentID(), journals);
		if (timestamp == null) {
			throw new RuntimeException("Database save returns null timestamp!");
		}

		return timestamp;
	}

	@Override
	public GetHumanResourceAllocationResponse getHumanResourceAllocation(
			Integer dealerID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataDAL.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		
		final GetHumanResourceAllocationResponse response = new GetHumanResourceAllocationResponse();
		final Collection<HumanResourceAllocation> list = incomeJournalDAL
				.getHumanResourceAllocation(dealerID, 
						LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final HumanResourceAllocation journal : list) {
			final HumanResourceAllocationDetail item = new HumanResourceAllocationDetail();
			item.setAllocation(journal.getAllocation().doubleValue());
			item.setName(refDataDAL.getJobPosition(journal.getId())
					.getName());
			item.setItemID(journal.getId());
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}
}
