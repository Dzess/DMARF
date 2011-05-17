package org.put.hd.dmarf.algorithms.apriori.binary;

import static org.jocl.CL.*;
import org.jocl.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.SortedMap;

import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * JOCLs implementation of the {@link ISetsEngine} mostly for data mining.
 * 
 * @author Piotr
 * 
 */
public class JOCLSetsEngine implements ISetsEngine {

	private cl_context context;
	private cl_command_queue commandQueue;
	private cl_kernel kernel;
	private cl_mem transCharMapMem;

	private DataRepresentationBase data;

	public Set<BinaryItemSet> getCandidateSets(
			SortedMap<BinaryItemSet, Integer> frequentSupportMap, int i) {
		// TODO Auto-generated method stub
		// TEN KOD TE¯ MO¯E TRAFIÆ NA JOCLA - LATER
		return null;
	}

	public Set<BinaryItemSet> verifyCandidatesInData(
			DataRepresentationBase data, Set<BinaryItemSet> candidates) {
		// TODO Auto-generated method stub

		// TUTAJ PISZESZ KOD PRZESZUKUJ¥CY JOCLa

		return null;
	}

	public SortedMap<BinaryItemSet, Integer> getSingleCandidateSets(
			DataRepresentationBase data, Integer support) {

		// TODO: write
		// TUTAJ MO¯NA WRZUCIÆ ELEGANCKO WRZUCANIE DANYCH DO JOCLA

		return null;
	}

	/**
	 * Initialize OpenCL: Create the context, the command queue and the kernel.
	 */
	private void initCL() {
		// Obtain the platform IDs and initialize the context properties
		System.out.println("Obtaining platform...");
		cl_platform_id platforms[] = new cl_platform_id[1];
		clGetPlatformIDs(platforms.length, platforms, null);
		cl_context_properties contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL_CONTEXT_PLATFORM, platforms[0]);

		// Create an OpenCL context on a GPU device
		context = clCreateContextFromType(contextProperties,
				CL_DEVICE_TYPE_GPU, null, null, null);
		if (context == null) {
			// If no context for a GPU device could be created,
			// try to create one for a CPU device.
			context = clCreateContextFromType(contextProperties,
					CL_DEVICE_TYPE_CPU, null, null, null);

			if (context == null) {
				System.out.println("Unable to create a context");
				System.exit(1);
				return;
			}
		}

		// Enable exceptions and subsequently omit error checks in this sample
		setExceptionsEnabled(true);

		// Get the list of GPU devices associated with context
		long numBytes[] = new long[1];
		clGetContextInfo(context, CL_CONTEXT_DEVICES, 0, null, numBytes);
		int numDevices = (int) numBytes[0] / Sizeof.cl_device_id;
		cl_device_id devices[] = new cl_device_id[numDevices];
		clGetContextInfo(context, CL_CONTEXT_DEVICES, numBytes[0],
				Pointer.to(devices), null);
		cl_device_id device = devices[0];

		// Create a command-queue
		commandQueue = clCreateCommandQueue(context, device, 0, null);

		// Program Setup
		String source = readFile("SimpleMandelbrot.cl");

		// Create the program
		cl_program cpProgram = clCreateProgramWithSource(context, 1,
				new String[] { source }, null, null);

		// Build the program
		clBuildProgram(cpProgram, 0, null, "-cl-mad-enable", null, null);

		// Create the kernel
		kernel = clCreateKernel(cpProgram, "computeMandelbrot", null);

		// Create the memory object which will be filled with the
		// transactionsCharMap
/*
		transCharMapMem = clCreateBuffer(
				context,
				CL_MEM_READ_ONLY,
				data.getNumberOfAttributesClusters()
						* data.getNumberOfTransactions() * Sizeof.cl_ushort16,
				null, null);
		clEnqueueWriteBuffer(commandQueue, transCharMapMem, true, 0, cb, ptr, num_events_in_wait_list, event_wait_list, event)

		// Create and fill the memory object containing the color map
		initColorMap(32, Color.RED, Color.GREEN, Color.BLUE);
		colorMapMem = clCreateBuffer(context, CL_MEM_READ_WRITE,
				colorMap.length * Sizeof.cl_uint, null, null);
		clEnqueueWriteBuffer(commandQueue, colorMapMem, true, 0,
				colorMap.length * Sizeof.cl_uint, Pointer.to(data.getTransactionsCharMap()[1]), 0,
				null, null);
	*/	
	}

	/**
	 * Helper function which reads the file with the given name and returns the
	 * contents of this file as a String. Will exit the application if the file
	 * can not be read.
	 * 
	 * @param fileName
	 *            The name of the file to read.
	 * @return The contents of the file
	 */
	private String readFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName)));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while (true) {
				line = br.readLine();
				if (line == null) {
					break;
				}
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
}
