package org.put.hd.dmarf.algorithms.apriori.binary;

import static org.jocl.CL.*;

import org.jocl.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;
import java.util.SortedMap;

import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * JOCLs implementation of the {@link ISetsEngine} mostly for data mining.
 * 
 * @author Miki & Piotr
 * 
 */
public class JOCLSetsEngine implements ISetsEngine {

	private cl_context context;
	private cl_command_queue commandQueue;
	private cl_kernel kernel;
	private cl_program program;
	private cl_mem transCharMapMem;
	private Pointer transCharMapPointer;
	private char[] transCharMap;

	private Pointer outMapPointer;
	private cl_mem outMapMem;
	private char[] outTestCharMap;

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
	 * The source code of the OpenCL program to execute
	 */
	private String programSource = "__kernel void "
			+ "sampleKernel(__global const ushort *a,"
			+ "             __global ushort *c)" + "{"
			+ "    int gid = get_global_id(0);"
			+ "    c[gid] =  a[gid]+get_group_id(0);" + "}";

	/**
	 * Initialize OpenCL: Create the context, the command queue and the kernel.
	 */
	public void initCL(DataRepresentationBase data) {

		long numBytes[] = new long[1];

		// Create input- and output data
		this.data = data;
		System.out.println("Getting the charMap");
		transCharMap = data.getTransactionsCharMap();
		outTestCharMap = new char[transCharMap.length];

		transCharMapPointer = Pointer.to(transCharMap);
		outMapPointer = Pointer.to(outTestCharMap);

		// Obtain the number of platforms
		int numPlatforms[] = new int[1];
		clGetPlatformIDs(0, null, numPlatforms);

		// Obtain the platform IDs and initialize the context properties
		System.out.println("Number of platforms: " + numPlatforms[0]);

		System.out.println("Obtaining platform...");
		cl_platform_id platforms[] = new cl_platform_id[numPlatforms[0]];
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
				return;
			}
		}

		// Enable exceptions and subsequently omit error checks in this sample
		CL.setExceptionsEnabled(true);

		// Get the list of GPU devices associated with the context
		clGetContextInfo(context, CL_CONTEXT_DEVICES, 0, null, numBytes);

		// Obtain the cl_device_id for the first device
		int numDevices = (int) numBytes[0] / Sizeof.cl_device_id;
		cl_device_id devices[] = new cl_device_id[numDevices];
		clGetContextInfo(context, CL_CONTEXT_DEVICES, numBytes[0],
				Pointer.to(devices), null);

		// Create a command-queue
		commandQueue = clCreateCommandQueue(context, devices[0], 0, null);

		// Allocate the memory objects for the input- and output data
		transCharMapMem = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_short * transCharMap.length,
				transCharMapPointer, null);
		outMapMem = clCreateBuffer(context, CL_MEM_READ_WRITE, Sizeof.cl_short
				* transCharMap.length, null, null);

		// Create the program from the source code
		program = clCreateProgramWithSource(context, 1,
				new String[] { programSource }, null, null);

		// Build the program
		clBuildProgram(program, 0, null, null, null, null);

		// Create the kernel
		kernel = clCreateKernel(program, "sampleKernel", null);

	}

	public void runCL() {

		// Set the arguments for the kernel
		clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(transCharMapMem));
		clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(outMapMem));

		// Set the work-item dimensions
		long global_work_size[] = new long[] { transCharMap.length };
		long local_work_size[] = new long[] { data
				.getNumberOfAttributesClusters() };

		// Execute the kernel
		clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, global_work_size,
				local_work_size, 0, null, null);

		// Read the output data
		clEnqueueReadBuffer(commandQueue, outMapMem, CL_TRUE, 0,
				transCharMap.length * Sizeof.cl_short, outMapPointer, 0, null,
				null);
	}

	public void verifyOutputCL() {
		System.out.println("Verifying output.");
		int diff = 0;
		for (int i = 0; i < data.getNumberOfTransactions(); i++) {
			for (int j = 0; j < data.getNumberOfAttributesClusters(); j++) {
				if (transCharMap[i * data.getNumberOfAttributesClusters() + j] != (outTestCharMap[i
						* data.getNumberOfAttributesClusters() + j] - i))
					diff++;
				/*
				 * System.out.print((int) transCharMap[i
				 * data.getNumberOfAttributesClusters() + j] + " " + (int)
				 * outTestCharMap[i data.getNumberOfAttributesClusters() + j] +
				 * " | ");
				 */
			}
			// System.out.println("");

		}
		if (diff > 0)
			System.out.println("Arrays differ :/");
		else
			System.out.println("Arrays are ok");
	}

	public void cleanupCL() {

		// Release kernel, program, and memory objects
		System.out.println("Releasing objects.");
		clReleaseMemObject(transCharMapMem);
		clReleaseMemObject(outMapMem);
		clReleaseKernel(kernel);
		clReleaseProgram(program);
		clReleaseCommandQueue(commandQueue);
		clReleaseContext(context);
	}

}
