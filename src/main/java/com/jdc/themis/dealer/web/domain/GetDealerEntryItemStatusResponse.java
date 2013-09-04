package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.time.calendar.LocalDate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.common.collect.Lists;
import com.jdc.themis.common.jaxb.adaptor.JaxbCalendarLocalDateAdaptor;

import com.jdc.themis.common.json.adaptor.JsonCalendarLocalDateSerializer;

/**
 * Response of getting dealer entry item complete status. 
 * 
 * @author Kai Chen
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GetDealerEntryItemStatusResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer dealerID;
	@XmlJavaTypeAdapter(JaxbCalendarLocalDateAdaptor.class)
	private LocalDate validDate;
	@XmlElement(name = "status")
	private final List<DealerEntryItemStatusDetail> status = Lists.newArrayList();
	
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	@JsonSerialize(using = JsonCalendarLocalDateSerializer.class)
	public LocalDate getValidDate() {
		return validDate;
	}
	public void setValidDate(LocalDate validDate) {
		this.validDate = validDate;
	}
	public List<DealerEntryItemStatusDetail> getDetail() {
		return status;
	}
	
}
