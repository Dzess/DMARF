/*
 * JOCL - Java bindings for OpenCL
 * 
 * Copyright 2010 Marco Hutter - http://www.jocl.org/
 */
package org.jocl.samples;

import static org.jocl.CL.*;

import java.nio.*;
import java.util.*;

import org.jocl.*;


/**
 * A small JOCL sample, demonstrating some of the new features
 * that have been introduced with OpenCL 1.1.
 */
public class JOCLSample_1_1
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
        "    c[gid] = a[gid] + b[gid];"+
        "}";

    private static cl_context context;
    private static cl_command_queue commandQueue;
    private static cl_kernel kernel;
    private static cl_program program;
    

    /**
     * The entry point of this sample
     * 
     * @param args Not used
     */
    public static void runJOCL_1_1_Sample()
    {
        defaultInitialization();
        
        // Create input- and output data
        int sizeX = 4;
        int sizeY = 4;
        int n = sizeX * sizeY;
        float srcArrayA[] = new float[n];
        float srcArrayB[] = new float[n];
        for (int i=0; i<n; i++)
        {
            srcArrayA[i] = i;
            srcArrayB[i] = i;
        }
        final Pointer srcA = Pointer.to(srcArrayA);
        final Pointer srcB = Pointer.to(srcArrayB);

        // Allocate the memory objects for the input- and output data
        cl_mem srcMemA = clCreateBuffer(context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float * n, srcA, null);
        cl_mem srcMemB = clCreateBuffer(context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float * n, srcB, null);
        cl_mem dstMem = clCreateBuffer(context, 
            CL_MEM_READ_WRITE, 
            Sizeof.cl_float * n, null, null);
        
        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(srcMemA));
        clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(srcMemB));
        clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(dstMem));
        
        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
            new long[]{n}, null, 0, null, null);

        
        
        // New features of OpenCL 1.1 demonstrated here:
        // - User events
        // - Functions for handling memory regions
        // - Event callbacks
        // - Memory object destructor callbacks
        
        // The output buffer has 16 float elements. These elements may be
        // interpreted as a 4x4 matrix. The following setup will enqueue 
        // a command to read the center 2x2 matrix of this buffer. That
        // is, it will read a region starting at coordinates (1,1), with
        // a size of (2,2) elements. 
        final int regionSizeX = 2;
        final int regionSizeY = 2;
        long bufferOffset[] = new long[] { 1 * Sizeof.cl_float, 1, 0 };
        long hostOffset[] = new long[] { 0, 0, 0 };
        long region[] = new long[] { regionSizeX * Sizeof.cl_float, regionSizeY, 1 };
        long bufferRowPitch = sizeX * Sizeof.cl_float;
        long bufferSlicePitch = sizeX * sizeY;
        long hostRowPitch = regionSizeX * Sizeof.cl_float;
        long hostSlicePitch = regionSizeX * regionSizeY;
        final FloatBuffer regionData = 
            ByteBuffer.allocateDirect(regionSizeX * regionSizeY * Sizeof.cl_float).
                order(ByteOrder.nativeOrder()).asFloatBuffer();

        // Create a user event. Later, the command that reads the
        // result buffer will wait for this event to be completed.
        final cl_event userEvent = clCreateUserEvent(context, null);

        // The command to read the memory region will be non-blocking, but
        // waiting for the user event that was created above. Additionally,
        // the command will have an associated event, for which a callback
        // will be registered. This callback will be called when the read
        // command has completed.
        System.out.println("Enqueue buffer region read, waiting for user event");
        cl_event readEvent = new cl_event();
        clEnqueueReadBufferRect(
            commandQueue, dstMem, false, bufferOffset, hostOffset, 
            region, bufferRowPitch, bufferSlicePitch, hostRowPitch, 
            hostSlicePitch, Pointer.to(regionData), 1, 
            new cl_event[]{userEvent}, readEvent);

        // Create a callback function which will be called when
        // the read event reaches the status CL_COMPLETE
        EventCallbackFunction eventCallbackFunction = new EventCallbackFunction()
        {
            public void function(cl_event event, int type, Object user_data)
            {
                System.out.println("Event "+event+" reached status "+
                    CL.stringFor_command_execution_status(type)+
                    ", user data: "+user_data);
                
                // Print the output that was read
                System.out.println("Buffer region was read:");
                print2D(regionData, regionSizeX);
            }
        };
        clSetEventCallback(readEvent, CL.CL_COMPLETE, 
            eventCallbackFunction, "Event callback user data");
        
        // Create a thread that will set the user event status
        // to "CL_COMPLETE" after a few seconds. This will 
        // allow the read command to be completed. When the
        // read command is completed, the event callback 
        // function will be called.
        Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
                System.out.println("Waiting before setting " +
                    "event status to CL_COMPLETE");
                for (int i=3; i>=1; i--)
                {
                    System.out.println("Seconds left: "+i);
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("Setting event status to CL_COMPLETE");
                clSetUserEventStatus(userEvent, CL.CL_COMPLETE);
            }
        });
        thread.start();
        
        
        // Create the destructor callback which will be called
        // when the output memory object is destroyed
        MemObjectDestructorCallbackFunction 
            memObjectDestructorCallbackFunction = 
                new MemObjectDestructorCallbackFunction()
        {
            public void function(cl_mem memobj, Object user_data)
            {
                System.out.println("Memory object "+memobj+
                    " was destroyed, user data: "+user_data);
            }
        };
        clSetMemObjectDestructorCallback(dstMem, 
            memObjectDestructorCallbackFunction, 
            "Memory object destructor callback user data");
        
        // Wait until all commands have completed
        clFinish(commandQueue);
        
        // Release kernel, program, and memory objects. 
        clReleaseKernel(kernel);
        clReleaseProgram(program);
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);
        clReleaseMemObject(srcMemA);
        clReleaseMemObject(srcMemB);
        
        // Releasing the output memory object will cause  
        // the destructor callback to be called.        
        clReleaseMemObject(dstMem);

        // Verify the result
        float reference[] = new float[]{10,12,18,20};
        float result[] = new float[regionSizeX*regionSizeY];
        regionData.get(result);
        boolean passed = Arrays.equals(result, reference);
        System.out.println(passed ? "PASSED" : "FAILED");
    }
    
    /**
     * Default OpenCL initialization of the context, command queue,
     * program and kernel
     */
    private static void defaultInitialization()
    {
        // Obtain the platform IDs and initialize the context properties
        cl_platform_id platforms[] = new cl_platform_id[2];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platforms[1]);
        
        // Create an OpenCL context on a GPU device
        context = clCreateContextFromType(
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
        long numBytes[] = new long[1];
        clGetContextInfo(context, CL_CONTEXT_DEVICES, 0, null, numBytes); 
        
        // Obtain the cl_device_id for the first device
        int numDevices = (int) numBytes[0] / Sizeof.cl_device_id;
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetContextInfo(context, CL_CONTEXT_DEVICES, numBytes[0],  
            Pointer.to(devices), null);

        // Create a command-queue
        commandQueue = 
            clCreateCommandQueue(context, devices[0], 0, null);

        // Create the program from the source code
        program = clCreateProgramWithSource(context,
            1, new String[]{ programSource }, null, null);
        
        // Build the program
        clBuildProgram(program, 0, null, null, null, null);
        
        // Create the kernel
        kernel = clCreateKernel(program, "sampleKernel", null);
    }
    
    /**
     * Print the given buffer as a matrix with the given number of columns
     * 
     * @param data The buffer
     * @param columns The number of columns
     */
    private static void print2D(FloatBuffer data, int columns)
    {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<data.capacity(); i++)
        {
            sb.append(String.format(Locale.ENGLISH, "%5.1f ", data.get(i)));
            if (((i+1)%columns)==0)
            {
                sb.append("\n");
            }
        }
        System.out.print(sb.toString());
    }
    
}
