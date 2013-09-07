package com.jdc.themis.dealer.service;

import javax.time.Instant;
import org.springframework.transaction.annotation.Transactional;

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

import fj.data.Option;

/**
 * Dealer income entry service layer. 
 * 
 * Define database transaction behavior here as well. 
 * 
 * @author Kai Chen
 *
 */
public interface DealerIncomeEntryService {
	/**
	 * Save a list of vehicle sales revenue.
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Instant saveVehicleSalesRevenue(
			final SaveVehicleSalesJournalRequest request);

	/**
	 * Get a list of vehicle sales revenue per vehicle type.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Transactional(readOnly=true)
	public GetVehicleSalesJournalResponse getVehicleSalesRevenue(
			Integer dealerID,
			Option<Integer> departmentID,
			String validDate, 
			Option<Integer> categoryID);

	/**
	 * Save a list of sales & service revenue.
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Instant saveSalesServiceRevenue(
			final SaveSalesServiceRevenueRequest request);

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
	@Transactional(readOnly=true)
	public GetSalesServiceJournalResponse getSalesServiceRevenue(
			Integer dealerID,
			Integer departmentID,
			String validDate, Option<Integer> categoryID);

	/**
	 * Save income tax.
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Instant saveIncomeTax(final SaveTaxRequest request);

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
	@Transactional(readOnly=true)
	public GetTaxResponse getIncomeTax(Integer dealerID,
			String validDate);

	/**
	 * Save dealer entry item status.
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Instant saveDealerEntryItemStatus(
			final SaveDealerEntryItemStatusRequest request);

	/**
	 * Get dealer entry item status.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Transactional
	public GetDealerEntryItemStatusResponse getDealerEntryItemStatus(Integer dealerID, String validDate);
	
	/**
	 * Save a list of general income journals.
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Instant saveGeneralIncome(
			final SaveGeneralJournalRequest request);

	/**
	 * Get a list of general income journals.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Transactional(readOnly=true)
	public GetGeneralJournalResponse getGeneralIncome(
			Integer dealerID,
			Integer departmentID,
			String validDate, Option<Integer> categoryID);
	
	/**
	 * Save a list of account receivable durations.
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Instant saveAccountReceivableDuration(
			final SaveAccountReceivableDurationRequest request);

	/**
	 * Get a list of general income journals.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Transactional(readOnly=true)
	public GetAccountReceivableDurationResponse getAccountReceivableDuration(
			Integer dealerID,
			String validDate);
	
	/**
	 * Save a list of inventory durations.
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Instant saveInventoryDuration(
			final SaveInventoryDurationRequest request);

	/**
	 * Get a list of inventory durations.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Transactional(readOnly=true)
	public GetInventoryDurationResponse getInventoryDuration(
			Integer dealerID,
			Integer departmentID, 
			String validDate);
	
	/**
	 * Save a list of employee fees.
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Instant saveEmployeeFee(
			final SaveEmployeeFeeRequest request);

	/**
	 * Get a list of employee fee.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Transactional(readOnly=true)
	public GetEmployeeFeeResponse getEmployeeFee (
			Integer dealerID,
			Integer departmentID, 
			String validDate);
	
	/**
	 * Save a list of employee fees summary.
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Instant saveEmployeeFeeSummary (
			final SaveEmployeeFeeSummaryRequest request);

	/**
	 * Get a list of employee fee summary.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Transactional(readOnly=true)
	public GetEmployeeFeeSummaryResponse getEmployeeFeeSummary (
			Integer dealerID,
			Integer departmentID, 
			String validDate);
	
	/**
	 * Save a list of employee fees summary.
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Instant saveHumanResourceAllocation (
			final SaveHumanResourceAllocationRequest request);

	/**
	 * Get a list of employee fee summary.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@Transactional(readOnly=true)
	public GetHumanResourceAllocationResponse getHumanResourceAllocation (
			Integer dealerID,
			String validDate);

}
