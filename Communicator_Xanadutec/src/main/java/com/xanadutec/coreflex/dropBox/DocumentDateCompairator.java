package com.xanadutec.coreflex.dropBox;
import java.util.Comparator;

import com.xanadutec.coreflex.model.Documents;



public class DocumentDateCompairator  implements Comparator<Documents> {

	@Override
	public int compare(Documents o1, Documents o2) {
		
		 return o2.getDateTime().compareTo(o1.getDateTime());
		
	}

}

