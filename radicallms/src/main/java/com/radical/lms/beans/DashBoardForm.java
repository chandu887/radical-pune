package com.radical.lms.beans;

import java.util.List;

public class DashBoardForm {
	int pageLimit;
	int pageNumber;
	int startLimit;
	int endLimit;
	int currentStatus;
	int newCount;
	int openCount;
	int closedCount;
	int totalLeadsCount;
	List<Integer> pageList;
	
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
}
