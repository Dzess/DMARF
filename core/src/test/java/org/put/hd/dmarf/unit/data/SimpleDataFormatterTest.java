package org.put.hd.dmarf.unit.data;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.utils.InvocationNumbered;

/**
 * Loads the trail data set defined in resources to test the if the simple data
 * formatter and data work good.
 * 
 * @author Piotr
 */
public class SimpleDataFormatterTest {

	@Mock
	private IDataRepresentationBuilder builderMock;
	
	/**
	 * System under test
	 */
	private SimpleDataFormatter formatter;

	@Before
	public void set_up() {
		MockitoAnnotations.initMocks(this);
		
		formatter = new SimpleDataFormatter(builderMock);
	}

	
	@Test
	public void getData_invokes_the_builder() {
		
		// mock up the reader
		String fileMock = "1 2 3" + System.getProperty("line.separator");
		Reader someReader = new StringReader(fileMock);

		// check if invoking data get uses the proper builder
		formatter.getFormattedData(someReader);
		
		// verify that 3 items have appeared in this sequence
		Mockito.verify(builderMock, new InvocationNumbered(0)).addTransaction(0);
		Mockito.verify(builderMock, new InvocationNumbered(1)).addItemInTransaction(1);
		Mockito.verify(builderMock, new InvocationNumbered(2)).addItemInTransaction(2);
		Mockito.verify(builderMock, new InvocationNumbered(3)).addItemInTransaction(3);
		Mockito.verify(builderMock,Mockito.times(1)).getDataRepresentation();
	}
	
	@Test
	public void getData_invokes_the_builder_triangulation(){
		// mock up the reader
		String fileMock = "2 3" + System.getProperty("line.separator");
		Reader someReader = new StringReader(fileMock);

		formatter.getFormattedData(someReader);
		
		// verify that 3 items have appeared in this sequence
		Mockito.verify(builderMock, new InvocationNumbered(0)).addTransaction(0);
		Mockito.verify(builderMock, new InvocationNumbered(1)).addItemInTransaction(2);
		Mockito.verify(builderMock, new InvocationNumbered(2)).addItemInTransaction(3);
		Mockito.verify(builderMock,Mockito.times(1)).getDataRepresentation();
	}
	
	@Test
	public void multiline_text_formating(){
		// mock up the reader
		String line1 = "2 3" + System.getProperty("line.separator");
		String line2 = "100 255" + System.getProperty("line.separator");
		String fileMock = line1 + line2;
		Reader someReader = new StringReader(fileMock);

		formatter.getFormattedData(someReader);
		
		// verify that 3 items have appeared in this sequence
		Mockito.verify(builderMock, new InvocationNumbered(0)).addTransaction(0);
		Mockito.verify(builderMock, new InvocationNumbered(1)).addItemInTransaction(2);
		Mockito.verify(builderMock, new InvocationNumbered(2)).addItemInTransaction(3);
		Mockito.verify(builderMock, new InvocationNumbered(3)).addTransaction(1);
		Mockito.verify(builderMock, new InvocationNumbered(4)).addItemInTransaction(100);
		Mockito.verify(builderMock, new InvocationNumbered(5)).addItemInTransaction(255);
		Mockito.verify(builderMock,Mockito.times(1)).getDataRepresentation();
	}
}


