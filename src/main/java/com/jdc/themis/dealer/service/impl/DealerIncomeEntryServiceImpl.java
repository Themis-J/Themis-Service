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
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.utils.Performance;
import com.jdc.themis.dealer.utils.Utils;
import com.jdc.themis.dealer.web.domain.AccountReceivableDurationDetail;
import com.jdc.themis.dealer.web.domain.DealerEntryItemStatusDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeSummaryDetail;
import com.jdc.themis.dealer.web.domain.GeneralJournalDetail;
import com.jdc.themis.dealer.web.domain.GeneralJournalItemDetail;
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
import com.jdc.themis.dealer.web.domain.SalesServiceJournalItemDetail;
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
import com.jdc.themis.dealer.web.domain.VehicleDetail;
import com.jdc.themis.dealer.web.domain.VehicleSalesJournalDetail;

import fj.data.Option;

@Service
public class DealerIncomeEntryServiceImpl implements DealerIncomeEntryService {

	@Autowired
	private IncomeJournalDAO incomeJournalDAL;
	@Autowired
	private RefDataQueryService refDataQueryService;

	public void setRefDataQueryService(RefDataQueryService refDataQueryService) {
		this.refDataQueryService = refDataQueryService;
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
				refDataQueryService.getDealer(request.getDealerID()) != null,
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
			journal.setId(refDataQueryService.getVehicle(detail.getVehicleID()).getId());
			journal.setDepartmentID(requestedDeparmentID);
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveVehicleSalesJournal(
				request.getDealerID(), requestedDeparmentID, journals);
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
			Integer dealerID, Option<Integer> departmentID, String validDate, Option<Integer> categoryID) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		if ( departmentID.isSome() ) {
			Preconditions.checkArgument(
				refDataQueryService.getDepartment(departmentID.some()) != null,
				"unknown department id " + departmentID);
		}
		final GetVehicleSalesJournalResponse response = new GetVehicleSalesJournalResponse();
		final Integer requestedDeparmentID = departmentID.isNone() ? DEFAULT_VEHICLE_DEPARTMENT_ID
				: departmentID.some();
		final Collection<VehicleSalesJournal> list = incomeJournalDAL
				.getVehicleSalesJournal(dealerID, requestedDeparmentID,
						LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setDepartmentID(requestedDeparmentID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final VehicleSalesJournal journal : list) {
			final VehicleDetail vehicle = refDataQueryService.getVehicle(journal.getId());
			if ( categoryID.isSome() && !vehicle.getCategoryID().equals(categoryID.some()) ) {
				continue;
			}
			final VehicleSalesJournalDetail item = new VehicleSalesJournalDetail();
			item.setAmount(journal.getAmount().doubleValue());
			item.setCount(journal.getCount());
			item.setMargin(journal.getMargin().doubleValue());
			item.setName(vehicle.getName());
			item.setVehicleID(vehicle.getId());
			item.setCategoryID(vehicle.getCategoryID());
			item.setCategory(vehicle.getCategory());
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
				refDataQueryService.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());
		Preconditions.checkArgument(
				refDataQueryService.getDepartment(request.getDepartmentID()) != null,
				"unknown department id " + request.getDepartmentID());
		
		final List<SalesServiceJournal> journals = Lists.newArrayList();
		for (final SalesServiceJournalDetail detail : request.getDetail()) {
			final SalesServiceJournal journal = new SalesServiceJournal();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			journal.setId(refDataQueryService.getSalesServiceRevenueItem(detail.getItemID()).getId());
			journal.setMargin(detail.getMargin() != null && !"Expense".equals(refDataQueryService.getSalesServiceRevenueItem(detail.getItemID()).getJournalType()) ? new BigDecimal(detail.getMargin()) : BigDecimal.ZERO);
			journal.setCount(detail.getCount() != null && !"Expense".equals(refDataQueryService.getSalesServiceRevenueItem(detail.getItemID()).getJournalType()) ? detail.getCount() : 0);			
			journal.setDepartmentID(request.getDepartmentID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveSalesServiceJournal(
				request.getDealerID(), request.getDepartmentID(), journals);
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
			Integer dealerID, Integer departmentID, String validDate, Option<Integer> categoryID) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(departmentID, "department id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		Preconditions.checkArgument(
				refDataQueryService.getDepartment(departmentID) != null,
				"unknown department id " + departmentID);
		
		final GetSalesServiceJournalResponse response = new GetSalesServiceJournalResponse();
		final Collection<SalesServiceJournal> list = incomeJournalDAL
				.getSalesServiceJournal(dealerID, departmentID,
						LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setDepartmentID(departmentID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final SalesServiceJournal journal : list) {
			final SalesServiceJournalItemDetail itemDetail = refDataQueryService.getSalesServiceRevenueItem(journal.getId());
			if ( categoryID.isSome() && !itemDetail.getCategoryID().equals(categoryID.some()) ) {
				continue;
			}
			final SalesServiceJournalDetail item = new SalesServiceJournalDetail();
			item.setAmount(journal.getAmount().doubleValue());
			item.setCount(journal.getCount());
			item.setMargin(journal.getMargin().doubleValue());
			item.setName(itemDetail.getName());
			item.setItemID(itemDetail.getId());
			item.setCategory(itemDetail.getCategory());
			item.setCategoryID(itemDetail.getCategoryID());
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
				refDataQueryService.getDealer(request.getDealerID()) != null,
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
				refDataQueryService.getDealer(dealerID) != null,
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
				refDataQueryService.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());

		final List<DealerEntryItemStatus> journals = Lists.newArrayList();
		final DealerEntryItemStatus journal = new DealerEntryItemStatus();
		Preconditions.checkArgument(refDataQueryService.getMenu(request.getItemID()) != null, "unknown item id");
		journal.setEntryItemID(request.getItemID());
		journal.setDealerID(request.getDealerID());
		journal.setUpdateBy(request.getUpdateBy());
		journal.setValidDate(LocalDate.parse(request.getValidDate()));
		journals.add(journal);
		final Instant timestamp = incomeJournalDAL.saveDealerEntryItemStatus(
				request.getDealerID(), journals);
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
				refDataQueryService.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);

		final GetDealerEntryItemStatusResponse response = new GetDealerEntryItemStatusResponse();
		final Collection<DealerEntryItemStatus> list = incomeJournalDAL
				.getDealerEntryItemStatus(dealerID, LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final DealerEntryItemStatus journal : list) {
			final DealerEntryItemStatusDetail status = new DealerEntryItemStatusDetail();
			status.setId(journal.getEntryItemID());
			status.setName(refDataQueryService.getMenu(status.getId()).getName());
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
				refDataQueryService.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());
		Preconditions.checkArgument(
				refDataQueryService.getDepartment(request.getDepartmentID()) != null,
				"unknown department id " + request.getDepartmentID());

		
		final List<GeneralJournal> journals = Lists.newArrayList();
		for (final GeneralJournalDetail detail : request.getDetail()) {
			final GeneralJournal journal = new GeneralJournal();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			journal.setId(refDataQueryService.getGeneralIncomeItem(detail.getItemID()).getId());
			journal.setDepartmentID(request.getDepartmentID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveGeneralJournal(
				request.getDealerID(), request.getDepartmentID(), journals);
		return timestamp;
	}

	@Override
	@Performance
	public GetGeneralJournalResponse getGeneralIncome(Integer dealerID,
			Integer departmentID, String validDate, Option<Integer> categoryID) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(departmentID, "department id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		Preconditions.checkArgument(
				refDataQueryService.getDepartment(departmentID) != null,
				"unknown department id " + departmentID);
		
		final GetGeneralJournalResponse response = new GetGeneralJournalResponse();
		final Collection<GeneralJournal> list = incomeJournalDAL
				.getGeneralJournal(dealerID, departmentID,
						LocalDate.parse(validDate));

		response.setDealerID(dealerID);
		response.setDepartmentID(departmentID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final GeneralJournal journal : list) {
			final GeneralJournalItemDetail itemDetail = refDataQueryService.getGeneralIncomeItem(journal.getId());
			if ( categoryID.isSome() && !itemDetail.getCategoryID().equals(categoryID.some()) ) {
				continue;
			}
			final GeneralJournalDetail item = new GeneralJournalDetail();
			item.setAmount(journal.getAmount().doubleValue());
			item.setName(itemDetail.getName());
			item.setItemID(itemDetail.getId());
			item.setCategory(itemDetail.getCategory());
			item.setCategoryID(itemDetail.getCategoryID());
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}

	@Override
	@Performance
	public Instant saveAccountReceivableDuration (
			SaveAccountReceivableDurationRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
		Preconditions.checkNotNull(request.getValidDate(),
				"valid date can't be null");
		Preconditions.checkNotNull(request.getDetail().size() == 0,
				"no detail is posted");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());
		
		final List<AccountReceivableDuration> journals = Lists.newArrayList();
		for (final AccountReceivableDurationDetail detail : request.getDetail()) {
			final AccountReceivableDuration journal = new AccountReceivableDuration();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			journal.setId(refDataQueryService.getAccountReceivableDurationItem(detail.getItemID()).getId());
			journal.setDurationID(refDataQueryService.getDuration(detail.getDurationID()).getId());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveAccountReceivableDuration(
				request.getDealerID(), journals);
		return timestamp;
	}

	@Override
	@Performance
	public GetAccountReceivableDurationResponse getAccountReceivableDuration(
			Integer dealerID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(dealerID) != null,
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
			item.setName(refDataQueryService.getAccountReceivableDurationItem(journal.getId()).getName());
			item.setItemID(journal.getId());
			item.setDurationID(journal.getDurationID());
			item.setDurationDesc(Utils.getDurationDesc(refDataQueryService.getDuration(journal.getDurationID()), refDataQueryService));
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}

	@Override
	@Performance
	public Instant saveInventoryDuration(SaveInventoryDurationRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
		Preconditions.checkNotNull(request.getValidDate(),
				"valid date can't be null");
		Preconditions.checkNotNull(request.getDetail().size() == 0,
				"no detail is posted");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());
		Preconditions.checkArgument(
				refDataQueryService.getDepartment(request.getDepartmentID()) != null,
				"unknown department id " + request.getDepartmentID());
		
		final List<InventoryDuration> journals = Lists.newArrayList();
		for (final InventoryDurationDetail detail : request.getDetail()) {
			final InventoryDuration journal = new InventoryDuration();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			journal.setId(refDataQueryService.getInventoryDurationItem(detail.getItemID()).getId());
			journal.setDepartmentID(request.getDepartmentID());
			journal.setDurationID(refDataQueryService.getDuration(detail.getDurationID()).getId());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveInventoryDuration(
				request.getDealerID(), request.getDepartmentID(), journals);
		return timestamp;
	}

	@Override
	@Performance
	public GetInventoryDurationResponse getInventoryDuration(Integer dealerID,
			Integer departmentID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		Preconditions.checkArgument(
				refDataQueryService.getDepartment(departmentID) != null,
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
			item.setName(refDataQueryService.getInventoryDurationItem(journal.getId()).getName());
			item.setItemID(journal.getId());
			item.setDurationID(journal.getDurationID());
			item.setDurationDesc(Utils.getDurationDesc(refDataQueryService.getDuration(journal.getDurationID()), refDataQueryService));
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}

	@Override
	@Performance
	public Instant saveEmployeeFee(SaveEmployeeFeeRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
		Preconditions.checkNotNull(request.getValidDate(),
				"valid date can't be null");
		Preconditions.checkNotNull(request.getDetail().size() == 0,
				"no detail is posted");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());
		Preconditions.checkArgument(
				refDataQueryService.getDepartment(request.getDepartmentID()) != null,
				"unknown department id " + request.getDepartmentID());
		
		final List<EmployeeFee> journals = Lists.newArrayList();
		for (final EmployeeFeeDetail detail : request.getDetail()) {
			final EmployeeFee journal = new EmployeeFee();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");

			Preconditions.checkArgument(
					refDataQueryService.getEnumValue("FeeType", detail.getFeeTypeID()).isSome(),
					"unknown fee type id " + detail.getFeeTypeID());
			journal.setId(refDataQueryService.getEmployeeFeeItem(detail.getItemID()).getId());
			journal.setFeeTypeID(detail.getFeeTypeID());
			journal.setDepartmentID(request.getDepartmentID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveEmployeeFee(
				request.getDealerID(), request.getDepartmentID(), journals);
		return timestamp;
	}

	@Override
	@Performance
	public GetEmployeeFeeResponse getEmployeeFee(Integer dealerID,
			Integer departmentID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		Preconditions.checkArgument(
				refDataQueryService.getDepartment(departmentID) != null,
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
			item.setName(refDataQueryService.getEmployeeFeeItem(journal.getId()).getName());
			item.setItemID(journal.getId());
			item.setFeeTypeID(journal.getFeeTypeID());
			item.setFeeType(refDataQueryService.getEnumValue("FeeType", journal.getFeeTypeID()).some().getName());
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}

	@Override
	@Performance
	public Instant saveEmployeeFeeSummary(SaveEmployeeFeeSummaryRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
		Preconditions.checkNotNull(request.getValidDate(),
				"valid date can't be null");
		Preconditions.checkNotNull(request.getDetail().size() == 0,
				"no detail is posted");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());
		Preconditions.checkArgument(
				refDataQueryService.getDepartment(request.getDepartmentID()) != null,
				"unknown department id " + request.getDepartmentID());
		
		final List<EmployeeFeeSummary> journals = Lists.newArrayList();
		for (final EmployeeFeeSummaryDetail detail : request.getDetail()) {
			final EmployeeFeeSummary journal = new EmployeeFeeSummary();
			journal.setAmount(detail.getAmount() != null ? new BigDecimal(detail.getAmount()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			journal.setId(refDataQueryService.getEmployeeFeeSummaryItem(detail.getItemID()).getId());
			journal.setDepartmentID(request.getDepartmentID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveEmployeeFeeSummary(
				request.getDealerID(), request.getDepartmentID(), journals);
		return timestamp;
	}

	@Override
	@Performance
	public GetEmployeeFeeSummaryResponse getEmployeeFeeSummary(
			Integer dealerID, Integer departmentID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(dealerID) != null,
				"unknown dealer id " + dealerID);
		Preconditions.checkArgument(
				refDataQueryService.getDepartment(departmentID) != null,
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
			item.setName(refDataQueryService.getEmployeeFeeSummaryItem(journal.getId()).getName());
			item.setItemID(journal.getId());
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}

	@Override
	@Performance
	public Instant saveHumanResourceAllocation(
			SaveHumanResourceAllocationRequest request) {
		Preconditions.checkNotNull(request.getDealerID(),
				"dealer id can't be null");
		Preconditions.checkNotNull(request.getValidDate(),
				"valid date can't be null");
		Preconditions.checkNotNull(request.getDetail().size() == 0,
				"no detail is posted");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(request.getDealerID()) != null,
				"unknown dealer id " + request.getDealerID());
		Preconditions.checkArgument(
				refDataQueryService.getDepartment(request.getDepartmentID()) != null,
				"unknown department id " + request.getDepartmentID());
		
		final List<HumanResourceAllocation> journals = Lists.newArrayList();
		for (final HumanResourceAllocationDetail detail : request.getDetail()) {
			final HumanResourceAllocation journal = new HumanResourceAllocation();
			journal.setAllocation(detail.getAllocation() != null ? new BigDecimal(detail.getAllocation()) : BigDecimal.ZERO);
			journal.setDealerID(request.getDealerID());
			Preconditions.checkNotNull(detail.getItemID(),
					"item id can't be null");
			journal.setId(refDataQueryService.getHumanResourceAllocationItem(detail.getItemID()).getId());
			journal.setDepartmentID(request.getDepartmentID());
			journal.setUpdatedBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
		}
		final Instant timestamp = incomeJournalDAL.saveHumanResourceAllocation(
				request.getDealerID(), request.getDepartmentID(), journals);

		return timestamp;
	}

	@Override
	@Performance
	public GetHumanResourceAllocationResponse getHumanResourceAllocation(
			Integer dealerID, String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		Preconditions.checkArgument(
				refDataQueryService.getDealer(dealerID) != null,
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
			item.setName(refDataQueryService.getHumanResourceAllocationItem(journal.getId()).getName());
			item.setItemID(journal.getId());
			item.setDepartmentID(journal.getDepartmentID());
			item.setTimestamp(journal.getTimestamp());
			response.getDetail().add(item);
		}

		return response;
	}
}
