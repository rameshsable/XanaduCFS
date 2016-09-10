package com.xanadutec.coreflex.model;

public class FeedBackCounterForHome {

	long count;
	FeedBackTypeTable feedBackTypeTable;
	int redflagCount;
	
	long closeCount;
	long totalCount;
	long inProgressCount;
	
	public int getRedflagCount() {
		return redflagCount;
	}

	public void setRedflagCount(int redflagCount) {
		this.redflagCount = redflagCount;
	}


	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getCloseCount() {
		return closeCount;
	}

	public void setCloseCount(long closeCount) {
		this.closeCount = closeCount;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public FeedBackTypeTable getFeedBackTypeTable() {
		return feedBackTypeTable;
	}

	public void setFeedBackTypeTable(FeedBackTypeTable feedBackTypeTable) {
		this.feedBackTypeTable = feedBackTypeTable;
	}

	public long getInProgressCount() {
		return inProgressCount;
	}

	public void setInProgressCount(long inProgressCount) {
		this.inProgressCount = inProgressCount;
	}

	

	
}
