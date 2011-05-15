package org.put.hd.dmarf.jocl;

import org.junit.Test;
import org.jocl.samples.*;

public class JOCLSamplesTest {

    @Test
    public void deviceQueryTest() {
	JOCLDeviceQuery.runJOCLDeviceQuery();
    }

    @Test
    public void OpenCL_1_0_Test() {
	JOCLSample.runJOCL_1_0_Sample();
    }

    /*
     * fails on Intel OCL 1.1 beta... with disabled CLexceptions moves bith
     * further but cannot end.
     */
    /*
     * @Test public void OpenCL_1_1_CPU_Test() {
     * 
     * JOCLSample_1_1.runJOCL_1_1_Sample(); }
     */
}
