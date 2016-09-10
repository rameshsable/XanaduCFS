package com.xanadutec.coreflex.mom;
import java.util.Comparator;

import com.xanadutec.coreflex.model.FeedBackSonumberSortingByDate;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.Report;


public class MomtDateCompairator  implements Comparator<Mom> {

	@Override
	public int compare(Mom o1, Mom o2) {
		
		 return o2.getDateTime().compareTo(o1.getDateTime());
		
	}

}

