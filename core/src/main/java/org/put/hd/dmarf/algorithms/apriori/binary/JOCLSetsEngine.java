package org.put.hd.dmarf.algorithms.apriori.binary;

import static org.jocl.CL.CL_CONTEXT_DEVICES;
import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_CPU;
import static org.jocl.CL.CL_DEVICE_TYPE_GPU;
import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_READ_WRITE;
import static org.jocl.CL.CL_MEM_WRITE_ONLY;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clCreateCommandQueue;
import static org.jocl.CL.clCreateContextFromType;
import static org.jocl.CL.clCreateKernel;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clFinish;
import static org.jocl.CL.clGetContextInfo;
import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.clReleaseCommandQueue;
import static org.jocl.CL.clReleaseContext;
import static org.jocl.CL.clReleaseKernel;
import static org.jocl.CL.clReleaseMemObject;
import static org.jocl.CL.clReleaseProgram;
import static org.jocl.CL.clSetKernelArg;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.naming.directory.InvalidAttributesException;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.stopwatches.StopWatch;

/**
 * JOCLs implementation of the {@link ISetsEngine} mostly for data mining.
 * 
 * @author Miki & Piotr
 * 
 */
public class JOCLSetsEngine implements ISetsEngine {

	// OpenCL stuff
	private cl_context context;
	private cl_command_queue commandQueue;
	private cl_kernel kernel1;
	private cl_kernel kernel2;
	private cl_program program;

	// transactions Map
	private cl_mem transCharMapMem;
	private Pointer transCharMapPointer;
	private char[] transCharMap;

	// ~support counting vector
	private Pointer outSuppLongArrayPointer;
	private cl_mem outSuppLongArrayMem;
	private long[] outSuppLongArray;

	// candidateSet Array
	private cl_mem candSetMem;
	private Pointer candSetPointer;
	private char[] candSet;

	// tmp Map
	private Pointer tmpMapPointer;
	private cl_mem tmpMapMem;
	private char[] tmpCharMap;

	private long global_work_size[];
	private long local_work_size[];

	private DataRepresentationBase data;

	private StopWatch sw;
	private boolean hasRun = false;

	public Set<BinaryItemSet> getCandidateSets(
			Set<BinaryItemSet> frequentSupportMap, int i) {
		// TODO Auto-generated method stub
		// TEN KOD TE¯ MO¯E TRAFIÆ NA JOCLA - LATER
		return null;
	}

	public SortedMap<BinaryItemSet, Integer> verifyCandidatesInData(
			DataRepresentationBase data, Set<BinaryItemSet> candidates,
			Integer supportThreshold) {

		// initialize the output map
		SortedMap<BinaryItemSet, Integer> outputSM = new TreeMap<BinaryItemSet, Integer>();

		// do not upload data before each mining
		if (!hasRun)
			try {
				initEngine(data);
			} catch (InvalidAttributesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		for (BinaryItemSet item : candidates) {

			// get support for item and add if bigger than support minimal value
			int value = getSupport(item.getAttributeVector());
			if (value >= supportThreshold) {
				outputSM.put(item, value);
			}
		}

		hasRun = true;
		return outputSM;
	}

	public SortedMap<BinaryItemSet, Integer> getSingleCandidateSets(
			DataRepresentationBase data, Integer support) {
		return null;
	}

	public void initEngine(DataRepresentationBase data) throws InvalidAttributesException {

		long numBytes[] = new long[1];

		// Create input- and output data
		this.data = data;
		
		if (data.getNumberOfAttributesClusters() % 4 != 0){
			throw new InvalidAttributesException("Attribute clusters not aligned to %4");
		}
		System.out.println("Getting the charMap");
		transCharMap = data.getTransactionsCharMap();
		transCharMapPointer = Pointer.to(transCharMap);

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

		tmpCharMap = new char[transCharMap.length];
		tmpMapPointer = Pointer.to(tmpCharMap);

		/**
		 * The source code of the OpenCL support verifying program to execute
		 */
		String supportKernelSource = "__kernel void "
				+ "supportKernel1(__global const ulong *inMap,"
				+ "             __global const ulong *inSet,"
				+ "             __global ulong *outMap)" + "{"
				+ "    int gid = get_global_id(0);"

				+ "    int glid = get_local_id(0);"
				+ "	   outMap[gid] = (inSet[glid] & inMap[gid]) ^ inSet[glid];"
				+ "}" + "__kernel void "
				+ "supportKernel2(__global const ulong *inTmpMap,"
				+ "             __global ulong *outSupp)" + "{"
				+ "    int gid = get_global_id(0);"
				+ "    for (uint pos = 0; pos <"
				+ data.getNumberOfAttributesClusters() / 4 + "; pos++) {"
				+ "	       outSupp[gid] = outSupp[gid] | inTmpMap[gid * "
				+ data.getNumberOfAttributesClusters() / 4 + "+ pos];"
				+ "    };" + "}";

		// Create the program from the source code
		program = clCreateProgramWithSource(context, 1,
				new String[] { supportKernelSource }, null, null);

		// Build the program
		clBuildProgram(program, 0, null, null, null, null);

		// Create the kernel
		kernel1 = clCreateKernel(program, "supportKernel1", null);
		kernel2 = clCreateKernel(program, "supportKernel2", null);

		// Set the arguments for the kernel
		clSetKernelArg(kernel1, 0, Sizeof.cl_mem, Pointer.to(transCharMapMem));

		global_work_size = new long[] { transCharMap.length };
		local_work_size = new long[] { data.getNumberOfAttributesClusters() };

	}

	/**
	 * Finds number of transactions supporting a given set.
	 * 
	 * @param candidateSet
	 *            set to be tested
	 * @return number of transactions supporting given set.
	 */
	public int getSupport(char[] candidateSet) {

		// preparing candidateSet argument for kernel
		candSetPointer = Pointer.to(candidateSet);

		candSetMem = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_short * data.getNumberOfAttributesClusters(),
				candSetPointer, null);

		// preparing tmpMap argument for kernels

		tmpMapMem = clCreateBuffer(context, CL_MEM_READ_WRITE, Sizeof.cl_short
				* transCharMap.length, null, null);

		clSetKernelArg(kernel1, 1, Sizeof.cl_mem, Pointer.to(candSetMem));
		clSetKernelArg(kernel1, 2, Sizeof.cl_mem, Pointer.to(tmpMapMem));

		// Execute kernel1
		// We got a worker per each comparison
		global_work_size[0] = transCharMap.length / 4;
		local_work_size[0] = data.getNumberOfAttributesClusters() / 4;

		clEnqueueNDRangeKernel(commandQueue, kernel1, 1, null,
				global_work_size, local_work_size, 0, null, null);

		// preparing data for reduction kernel2
		outSuppLongArray = new long[data.getNumberOfTransactions()];
		outSuppLongArrayPointer = Pointer.to(outSuppLongArray);

		outSuppLongArrayMem = clCreateBuffer(context, CL_MEM_WRITE_ONLY,
				Sizeof.cl_ulong * data.getNumberOfTransactions(), null, null);

		clSetKernelArg(kernel2, 0, Sizeof.cl_mem, Pointer.to(tmpMapMem));
		clSetKernelArg(kernel2, 1, Sizeof.cl_mem,
				Pointer.to(outSuppLongArrayMem));

		// Execute the reduction kernel with 1 worker per transaction
		global_work_size[0] = data.getNumberOfTransactions();
		local_work_size[0] = 1;

		clEnqueueNDRangeKernel(commandQueue, kernel2, 1, null,
				global_work_size, local_work_size, 0, null, null);

		// Read the output data
		clEnqueueReadBuffer(commandQueue, outSuppLongArrayMem, CL_TRUE, 0,
				data.getNumberOfTransactions() * Sizeof.cl_ulong,
				outSuppLongArrayPointer, 0, null, null);

		clFinish(commandQueue);

		int supp = 0;
		for (int i = 0; i < outSuppLongArray.length; i++) {
			// System.out.println((int) outSuppCharArray[i] + " ");
			if (outSuppLongArray[i] == 0)
				supp++;
		}

		// cleaning gpuMem
		clReleaseMemObject(candSetMem);
		clReleaseMemObject(tmpMapMem);
		clReleaseMemObject(outSuppLongArrayMem);
		return supp;
	}

	public void cleanupEngine() {

		// Release kernel, program, and memory objects
		System.out.println("Releasing objects.");
		clReleaseMemObject(transCharMapMem);
		clReleaseKernel(kernel1);
		clReleaseKernel(kernel2);
		clReleaseProgram(program);
		clReleaseCommandQueue(commandQueue);
		clReleaseContext(context);
	}

}
