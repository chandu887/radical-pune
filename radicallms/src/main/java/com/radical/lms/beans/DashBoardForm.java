package com.radical.lms.beans;

import java.util.List;
import java.util.Map;

public class DashBoardForm {
	int pageLimit;
	int pageNumber;
	int startLimit;
	int endLimit;
	int currentStatus;
	int newCount;
	int openCount;
	int hotCount;
	int closedCount;
	int totalLeadsCount;
	int pageTotalCount;
	List<Integer> pageList;
	String fromDate;
	String toDate;
	int course;
	int category;
	int filterType;
	List<Integer> limitList;
	String searchData;
	String viewPage;
	String emailTemplateCount;
	
	String email;
	String mobileNumber;
	int totalPages;
	
	public int getHotCount() {
		return hotCount;
	}
	public void setHotCount(int hotCount) {
		this.hotCount = hotCount;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	
	public int getPageLimit() {
		return pageLimit;
	}
	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(int currentStatus) {
		this.currentStatus = currentStatus;
	}
		
	public int getTotalLeadsCount() {
		return totalLeadsCount;
	}
	public void setTotalLeadsCount(int totalLeadsCount) {
		this.totalLeadsCount = totalLeadsCount;
	}
	public List<Integer> getPageList() {
		return pageList;
	}
	public void setPageList(List<Integer> pageList) {
		this.pageList = pageList;
	}
	public int getNewCount() {
		return newCount;
	}
	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}
	public int getOpenCount() {
		return openCount;
	}
	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}
	public int getClosedCount() {
		return closedCount;
	}
	public void setClosedCount(int closedCount) {
		this.closedCount = closedCount;
	}
	public int getStartLimit() {
		return startLimit;
	}
	public void setStartLimit(int startLimit) {
		this.startLimit = startLimit;
	}
	public int getEndLimit() {
		return endLimit;
	}
	public void setEndLimit(int endLimit) {
		this.endLimit = endLimit;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public int getCourse() {
		return course;
	}
	public void setCourse(int course) {
		this.course = course;
	}
	public int getFilterType() {
		return filterType;
	}
	public void setFilterType(int filterType) {
		this.filterType = filterType;
	}
	public List<Integer> getLimitList() {
		return limitList;
	}
	public void setLimitList(List<Integer> limitList) {
		this.limitList = limitList;
	}
	public int getPageTotalCount() {
		return pageTotalCount;
	}
	public void setPageTotalCount(int pageTotalCount) {
		this.pageTotalCount = pageTotalCount;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getSearchData() {
		return searchData;
	}
	public void setSearchData(String searchData) {
		this.searchData = searchData;
	}
	public String getViewPage() {
		return viewPage;
	}
	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}
	public String getEmailTemplateCount() {
		return emailTemplateCount;
	}
	public void setEmailTemplateCount(String emailTemplateCount) {
		this.emailTemplateCount = emailTemplateCount;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	
}
