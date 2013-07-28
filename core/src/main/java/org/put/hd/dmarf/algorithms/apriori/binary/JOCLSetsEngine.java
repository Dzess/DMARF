package org.put.hd.dmarf.algorithms.apriori.binary;

import static org.jocl.CL.*;
import org.jocl.*;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.naming.directory.InvalidAttributesException;

import org.put.hd.dmarf.algorithms.apriori.binary.JOCLSetsEngine.DeviceTypeSelector;
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
	private int candSetSize;
	private long outSupplyLongArraySize;
	private int numberOfTransactions;
	private int numberOfAttClusters;
	private long numBytes[];
	private DeviceTypeSelector selectedDeviceType;

	public enum DeviceTypeSelector {
		Any, CPU, GPU
	};

	public DeviceTypeSelector getSelectedDeviceType() {
		return selectedDeviceType;
	}

	/**
	 * This engine will try to run OpenCL on any found device type by default.
	 */
	public JOCLSetsEngine(){
		this.selectedDeviceType = DeviceTypeSelector.Any;
	}
	
	/**
	 * Creates the OpenCL engine with runtime on desired deviceType.
	 * @param dts selected device type for OpenCL to run on.
	 */
	public JOCLSetsEngine(DeviceTypeSelector dts){
		this.selectedDeviceType = dts;
	}

	public Set<BinaryItemSet> getCandidateSets(
			Set<BinaryItemSet> frequentSupportMap, int i) {

		throw new RuntimeException("Not yet implemented");
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

		numBytes= new long[1];

		// Create input- and output data
		this.data = data;
		this.numberOfTransactions = data.getNumberOfTransactions();
		this.numberOfAttClusters = data.getNumberOfAttributesClusters();
		
		if (data.getNumberOfAttributesClusters() % 4 != 0){
			throw new InvalidAttributesException("Attribute clusters not aligned to %4");
		}

		// Sets up the CPU | GPU profile, which deceives to use and so on
		setUpPlatforms();

		// Enable exceptions and subsequently omit error checks in this sample
		CL.setExceptionsEnabled(true);

		// Sets up the devices with configuring context and command queue
		setUpDevices();		

		// Set Up kernel
		setUpKernels();
		
		// preparing MapData argument for kernels (read only map)
		transCharMap = data.getTransactionsCharMap();
		transCharMapPointer = Pointer.to(transCharMap);
		transCharMapMem = clCreateBuffer(
				context, 
				CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, 
				Sizeof.cl_short * transCharMap.length,
				transCharMapPointer, null);		

		//  Map for writes after kernel1
		tmpMapMem = clCreateBuffer(
				context,
				CL_MEM_READ_WRITE , 
				Sizeof.cl_short* transCharMap.length,
				null, null);
		

		// candidate vector for reads before kernel 1 (allocates with 0)
		this.candSetSize = Sizeof.cl_short * numberOfAttClusters;
		this.candSetPointer = Pointer.to(new char[candSetSize]);
		candSetMem = clCreateBuffer(
				context,
				CL_MEM_READ_ONLY,
				candSetSize,
				null, null);
		
		// Set up supply vector
		outSuppLongArray = new long[numberOfTransactions];
		outSuppLongArrayPointer = Pointer.to(outSuppLongArray);
		outSupplyLongArraySize = Sizeof.cl_ulong * numberOfTransactions;
		
		outSuppLongArrayMem = clCreateBuffer(
				context,
				CL_MEM_WRITE_ONLY | CL_MEM_COPY_HOST_PTR,
				outSupplyLongArraySize,
				outSuppLongArrayPointer, null);
		
		// Set up the dimensions for the workers
		global_work_size = new long[] { transCharMap.length };
		local_work_size = new long[] { numberOfAttClusters };
		

		// Set the arguments for the kernel
		clSetKernelArg(kernel1, 0, Sizeof.cl_mem, Pointer.to(transCharMapMem));
		clSetKernelArg(kernel1, 1, Sizeof.cl_mem, Pointer.to(candSetMem));
		clSetKernelArg(kernel1, 2, Sizeof.cl_mem, Pointer.to(tmpMapMem));
		
		
		clSetKernelArg(kernel2, 0, Sizeof.cl_mem, Pointer.to(tmpMapMem));
		clSetKernelArg(kernel2, 1, Sizeof.cl_mem,
				Pointer.to(outSuppLongArrayMem));
	}

	private void setUpDevices() {
		// Get the list of GPU devices associated with the context
		clGetContextInfo(context, CL_CONTEXT_DEVICES, 0, null, numBytes);

		// Obtain the cl_device_id for the first device
		int numDevices = (int) numBytes[0] / Sizeof.cl_device_id;
		cl_device_id devices[] = new cl_device_id[numDevices];
		clGetContextInfo(context, CL_CONTEXT_DEVICES, numBytes[0],
				Pointer.to(devices), null);

		// Create a command-queue
		commandQueue = clCreateCommandQueue(context, devices[0], 0, null);
	}

	private void setUpPlatforms() {
		// Obtain the number of platforms
		int numPlatforms[] = new int[1];
		clGetPlatformIDs(0, null, numPlatforms);

		// Obtain the platform IDs and initialize the context properties
		// System.out.println("Number of platforms: " + numPlatforms[0]);

		// System.out.println("Obtaining platform...");
		cl_platform_id platforms[] = new cl_platform_id[numPlatforms[0]];
		clGetPlatformIDs(platforms.length, platforms, null);

		// Get devices from platform
		int platformWithGPU = -1;
		int platformWithCPU = -1;
		for (int i = 0; i < platforms.length; i++) {

			int result;
			if (selectedDeviceType != DeviceTypeSelector.CPU) 
				try {
					result = clGetDeviceIDs(platforms[i], CL_DEVICE_TYPE_GPU, 0,
							null, null);
					if (result == CL_SUCCESS) {
						platformWithGPU = i;
					}
				} catch (CLException e) {
					// well do nothing now
				}
			if (selectedDeviceType != DeviceTypeSelector.GPU) 
				try {
					result = clGetDeviceIDs(platforms[i], CL_DEVICE_TYPE_CPU, 0,
							null, null);
					if (result == CL_SUCCESS) {
						platformWithCPU = i;
					}
				} catch (CLException e) {
					// well do nothing now
				}
		}

		if (platformWithGPU != -1) {
			// Create an OpenCL context on a GPU device
			cl_context_properties contextProperties = new cl_context_properties();
			contextProperties.addProperty(CL_CONTEXT_PLATFORM,
					platforms[platformWithGPU]);

			context = clCreateContextFromType(contextProperties,
					CL_DEVICE_TYPE_GPU, null, null, null);
			
			selectedDeviceType = DeviceTypeSelector.GPU;
		}
		// if no GPU the create the CPU context
		else if (platformWithCPU != -1) {
			cl_context_properties contextProperties = new cl_context_properties();
			contextProperties.addProperty(CL_CONTEXT_PLATFORM,
					platforms[platformWithCPU]);

			context = clCreateContextFromType(contextProperties,
					CL_DEVICE_TYPE_CPU, null, null, null);
			
			selectedDeviceType = DeviceTypeSelector.CPU;
		}
		// if no CPU context available then throw and exception about
		else {
			throw new RuntimeException(
					"No CPU or GPU device to get the code working");
		}

		if (context == null) {
			throw new RuntimeException(
					"Could not create the context on the device");
		}
	}

	private void setUpKernels() {
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
				+ numberOfAttClusters / 4 + "; pos++) {"
				+ "	       outSupp[gid] = outSupp[gid] | inTmpMap[gid * "
				+ numberOfAttClusters / 4 + "+ pos];"
				+ "    };" + "}";

		// Create the program from the source code
		program = clCreateProgramWithSource(context, 1,
				new String[] { supportKernelSource }, null, null);

		// Build the program
		clBuildProgram(program, 0, null, null, null, null);

		// Create the kernel
		kernel1 = clCreateKernel(program, "supportKernel1", null);
		kernel2 = clCreateKernel(program, "supportKernel2", null);
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

//		readVector(data.getNumberOfAttributesClusters(),this.candSetMem,this.candSetSize);
		
		// Write the new candidate set vector to the already defined buffer
		clEnqueueWriteBuffer(commandQueue, // use normal queue
				candSetMem, // write into candidate memory buffer
				CL_TRUE, // blocking write
				0, // offset goes to zero
				this.candSetSize, // this size of the vector
				candSetPointer, 0, null, null);

		// DEBUG: (read the tranCharMap)
//		readTrans();
//		readVector(data.getNumberOfAttributesClusters(),this.candSetMem,this.candSetSize);
		


		// Execute kernel1
		// We got a worker per each comparison
		global_work_size[0] = transCharMap.length / 4;
		local_work_size[0] = numberOfAttClusters / 4;

		clEnqueueNDRangeKernel(commandQueue, kernel1, 1, null,
				global_work_size, local_work_size, 0, null, null);

		
		// preparing data for reduction kernel2		
		outSuppLongArray = new long[numberOfTransactions];
		outSuppLongArrayPointer = Pointer.to(outSuppLongArray);
		clEnqueueWriteBuffer(commandQueue, // use normal queue
				this.outSuppLongArrayMem, // write into candidate memory buffer
				CL_TRUE, // blocking write
				0, // offset goes to zero
				this.outSupplyLongArraySize, // this size of the vector
				this.outSuppLongArrayPointer, 0, null, null);
		
		// DEBUG: (read the tranCharMap)
//		readSupplyArray();

		
		// DEBUG: (read the tranCharMap)
//		readTrans();
//		readTmp();
//		readSupplyArray();
		
		// Execute the reduction kernel with 1 worker per transaction
		global_work_size[0] = numberOfTransactions;
		local_work_size[0] = 1;
		
		clEnqueueNDRangeKernel(commandQueue, kernel2, 1, null,
				global_work_size, local_work_size, 0, null, null);

		// Read the output data
		clEnqueueReadBuffer(commandQueue, outSuppLongArrayMem, CL_TRUE, 0,
				numberOfTransactions * Sizeof.cl_ulong,
				outSuppLongArrayPointer, 0, null, null);

		int supp = 0;
        for (long anOutSuppLongArray : outSuppLongArray) {
            if (anOutSuppLongArray == 0)
                supp++;
        }

		return supp;
	}

	private void readSupplyArray() {
		
		char[] vector = new char[this.outSuppLongArray.length*4];
		Pointer vectorPointer = Pointer.to(vector);
		
		clEnqueueReadBuffer(
				commandQueue,
				this.outSuppLongArrayMem, 
				CL_TRUE, 0,
				data.getNumberOfTransactions() * Sizeof.cl_ulong,
				vectorPointer, 0, null, null);
	
		// Print the data with java
		System.out.println("\n Supply Vector");
		for (int i = 0; i < vector.length; i++) {
			System.out.print( new String(BinaryItemSet.getBinaryString(vector[i])) + "\t");
			if( (i + 1) % 4 == 0)
				System.out.println("");
		}
		System.out.println("");
	}

	private void readVector(int vector_size, cl_mem mem, long mem_size){
		
		char[] vector = new char[vector_size];
		Pointer vectorPointer = Pointer.to(vector);
		
		clEnqueueReadBuffer(
				commandQueue,
				mem, 
				CL_TRUE, 0,
				mem_size,
				vectorPointer, 0, null, null);
	
		// Print the data with java
		System.out.println("\nDebuging Vector");
        for (char c : vector) {
            System.out.print(new String(BinaryItemSet.getBinaryString(c)) + "\t");
        }
		System.out.println("");
	}
	
	private void readTrans(){
		
		// Get the data
		clEnqueueReadBuffer(
				commandQueue,
				transCharMapMem, 
				CL_TRUE, 0,
				Sizeof.cl_short * this.transCharMap.length,
				this.transCharMapPointer, 0, null, null);
	
		// Print the data with java
		System.out.println("\nDebuging Transactions");
		for (int i = 0; i < transCharMap.length; i++) {
			System.out.print( new String(BinaryItemSet.getBinaryString(transCharMap[i])) + "\t");
			if( (i +1) % this.data.getNumberOfAttributesClusters() == 0)
				System.out.println("");
		}
	}
	
	private void readTmp(){
		char[] tmpMap = new char[this.transCharMap.length];
		Pointer tmpMapPtr = Pointer.to(tmpMap);
		// Get the data
		clEnqueueReadBuffer(
				commandQueue,
				tmpMapMem, 
				CL_TRUE, 0,
				Sizeof.cl_short * this.transCharMap.length,
				tmpMapPtr, 0, null, null);
	
		// Print the data with java
		System.out.println("\nDebuging Temporary");
		for (int i = 0; i < transCharMap.length; i++) {
			System.out.print( new String(BinaryItemSet.getBinaryString(tmpMap[i])) + "\t");
			if( (i + 1) % this.data.getNumberOfAttributesClusters() == 0)
				System.out.println("");
		}
	}
	
	public void cleanupEngine() {

		// cleaning gpuMem
		clReleaseMemObject(candSetMem);
		clReleaseMemObject(tmpMapMem);
		clReleaseMemObject(transCharMapMem);
		clReleaseMemObject(outSuppLongArrayMem);

		// Release kernel, program, and memory objects
		// System.out.println("Releasing objects.");
		clReleaseKernel(kernel1);
		clReleaseKernel(kernel2);
		clReleaseProgram(program);
		clReleaseCommandQueue(commandQueue);
		clReleaseContext(context);
	}

}
