package com.xanadutec.coreflex.feedback;
import java.util.Comparator;

import com.xanadutec.coreflex.model.FeedBackSonumberSortingByDate;
import com.xanadutec.coreflex.model.Feedback;


public class FeedBackSortingCompairator  implements Comparator<FeedBackSonumberSortingByDate> {

	@Override
	public int compare(FeedBackSonumberSortingByDate o1, FeedBackSonumberSortingByDate o2) {
		/*int tv1Size = o1.getFeedBackSonumberSortingId();
		int tv2Size = o2.getFeedBackSonumberSortingId();*/
		
		
		 return o2.getFdate().compareTo(o1.getFdate());
		/*if (tv1Size > tv2Size) {
			return 1;
		} else if (tv1Size < tv2Size) {
			return -1;
		} else {
			return 0;
		}*/
	}

}

