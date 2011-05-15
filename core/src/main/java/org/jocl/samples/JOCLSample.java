/*
 * JOCL - Java bindings for OpenCL
 * 
 * Copyright 2009 Marco Hutter - http://www.jocl.org/
 */

package org.jocl.samples;

import static org.jocl.CL.*;

import org.jocl.*;

/**
 * A small JOCL sample.
 */
public class JOCLSample
{
    /**
     * The source code of the OpenCL program to execute
     */
    private static String programSource =
        "__kernel void "+
        "sampleKernel(__global const float *a,"+
        "             __global const float *b,"+
        "             __global float *c)"+
        "{"+
        "    int gid = get_global_id(0);"+
        "    c[gid] = a[gid] * b[gid];"+
        "}";
    

    /**
     * The entry point of this sample
     * 
     * @param args Not used
     */
    public static void runJOCL_1_0_Sample()
    {
        long numBytes[] = new long[1];
        
        // Create input- and output data 
        int n = 10;
        float srcArrayA[] = new float[n];
        float srcArrayB[] = new float[n];
        float dstArray[] = new float[n];
        for (int i=0; i<n; i++)
        {
            srcArrayA[i] = i;
            srcArrayB[i] = i;
        }
        Pointer srcA = Pointer.to(srcArrayA);
        Pointer srcB = Pointer.to(srcArrayB);
        Pointer dst = Pointer.to(dstArray);

        // Obtain the platform IDs and initialize the context properties
        System.out.println("Obtaining platform...");
        cl_platform_id platforms[] = new cl_platform_id[1];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platforms[0]);
        
        // Create an OpenCL context on a GPU device
        cl_context context = clCreateContextFromType(
            contextProperties, CL_DEVICE_TYPE_GPU, null, null, null);
        if (context == null)
        {
            // If no context for a GPU device could be created,
            // try to create one for a CPU device.
            context = clCreateContextFromType(
                contextProperties, CL_DEVICE_TYPE_CPU, null, null, null);
            
            if (context == null)
            {
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
        cl_command_queue commandQueue = 
            clCreateCommandQueue(context, devices[0], 0, null);

        // Allocate the memory objects for the input- and output data
        cl_mem memObjects[] = new cl_mem[3];
        memObjects[0] = clCreateBuffer(context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float * n, srcA, null);
        memObjects[1] = clCreateBuffer(context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float * n, srcB, null);
        memObjects[2] = clCreateBuffer(context, 
            CL_MEM_READ_WRITE, 
            Sizeof.cl_float * n, null, null);
        
        // Create the program from the source code
        cl_program program = clCreateProgramWithSource(context,
            1, new String[]{ programSource }, null, null);
        
        // Build the program
        clBuildProgram(program, 0, null, null, null, null);
        
        // Create the kernel
        cl_kernel kernel = clCreateKernel(program, "sampleKernel", null);
        
        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0, 
            Sizeof.cl_mem, Pointer.to(memObjects[0]));
        clSetKernelArg(kernel, 1, 
            Sizeof.cl_mem, Pointer.to(memObjects[1]));
        clSetKernelArg(kernel, 2, 
            Sizeof.cl_mem, Pointer.to(memObjects[2]));
        
        // Set the work-item dimensions
        long global_work_size[] = new long[]{n};
        long local_work_size[] = new long[]{1};
        
        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
            global_work_size, local_work_size, 0, null, null);
        
        // Read the output data
        clEnqueueReadBuffer(commandQueue, memObjects[2], CL_TRUE, 0,
            n * Sizeof.cl_float, dst, 0, null, null);
        
        // Release kernel, program, and memory objects
        clReleaseMemObject(memObjects[0]);
        clReleaseMemObject(memObjects[1]);
        clReleaseMemObject(memObjects[2]);
        clReleaseKernel(kernel);
        clReleaseProgram(program);
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);
        
        // Verify the result
        boolean passed = true;
        final float epsilon = 1e-7f;
        for (int i=0; i<n; i++)
        {
            float x = dstArray[i];
            float y = srcArrayA[i] * srcArrayB[i];
            boolean epsilonEqual = Math.abs(x - y) <= epsilon * Math.abs(x);
            if (!epsilonEqual)
            {
                passed = false;
                break;
            }
        }
        System.out.println("Test "+(passed?"PASSED":"FAILED"));
        if (n <= 10)
        {
            System.out.println("Result: "+java.util.Arrays.toString(dstArray));
        }
    }
}
