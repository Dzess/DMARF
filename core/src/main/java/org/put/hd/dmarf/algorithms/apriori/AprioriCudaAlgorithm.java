/**
 * 
 */
package org.put.hd.dmarf.algorithms.apriori;

import java.util.LinkedList;
import jcuda.*;
import jcuda.runtime.*;
import java.util.List;

import org.put.hd.dmarf.algorithms.AlgorithmBase;
import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * @author Ankhazam
 *
 */
public class AprioriCudaAlgorithm extends AlgorithmBase {

    private List<Rule> rules = new LinkedList<Rule>();
    
    @Override
    protected void startRuleGeneration(DataRepresentationBase data,
	    double minSupport, double minCredibility) {
	// TODO Auto-generated method stub

    }


    @Override
    protected void startSetGeneration(DataRepresentationBase data,
	    double minSupport, double minCredibility) {
	// TODO Auto-generated method stub

    }

    @Override
    public List<Rule> getRules() {
	// TODO Auto-generated method stub
        Pointer pointer = new Pointer();
        JCuda.cudaMalloc(pointer, 4);
        System.out.println("Apriori CUDA Pointer: "+pointer);
        JCuda.cudaFree(pointer);
	return rules;
    }

}
