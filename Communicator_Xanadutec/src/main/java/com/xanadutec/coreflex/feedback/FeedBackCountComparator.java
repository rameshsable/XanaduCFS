package com.xanadutec.coreflex.feedback;

import java.util.Comparator;

import com.xanadutec.coreflex.model.Feedback;

public class FeedBackCountComparator implements Comparator<Feedback> {

	@Override
	public int compare(Feedback o1, Feedback o2) {
		int tv1Size = o1.getCounter().getFeedbackCounter();
		int tv2Size = o2.getCounter().getFeedbackCounter();
 
		if (tv1Size > tv2Size) {
			return 1;
		} else if (tv1Size < tv2Size) {
			return -1;
		} else {
			return 0;
		}
	}

}
