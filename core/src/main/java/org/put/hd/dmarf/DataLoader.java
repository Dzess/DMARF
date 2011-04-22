package org.put.hd.dmarf;

import java.io.File;

public class DataLoader implements IDataLoader {

	private File inputFile;
	
	public void setInputFileName(String name) {
		
		// constraints
		 if(name == null)
			 throw new NullPointerException("Name parameter cannot be null");
		
		 inputFile  = new File(name);
		 
		 if(!inputFile.exists())
			 throw new UnsupportedOperationException("No such file exists");
		 
		 // normal access logic
		 
	}

	public void loadData() {
		// TODO Auto-generated method stub
		
	}

}
