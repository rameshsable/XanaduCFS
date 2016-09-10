package com.xanadutec.coreflex.dropBox;
import java.util.Comparator;
import com.xanadutec.coreflex.model.Report;


public class ReportDateCompairator  implements Comparator<Report> {

	@Override
	public int compare(Report o1, Report o2) {
		
		 return o2.getDateTime().compareTo(o1.getDateTime());
		
	}

}

