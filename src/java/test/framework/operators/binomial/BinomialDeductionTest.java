package framework.operators.binomial;

import org.junit.Test;

import framework.BinomialOpinion;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class BinomialDeductionTest {


    /**
     * Test data from Subjective Logic book p.152
     */
    @Test
    public void deductionTest(){
        BinomialOpinion x = new BinomialOpinion(0.1, 0.8, 0.1,0.8);
        BinomialOpinion ygx = new BinomialOpinion(0.4, 0.5, 0.1,0.4);
        BinomialOpinion ynx = new BinomialOpinion(0.0, 0.4, 0.6,0.4);

        BinomialDeduction bd = new BinomialDeduction(0.4);
        BinomialOpinion result = bd.apply(ygx,ynx,x);
        assertEquals(0.07,result.getBelief(),0.01);
        assertEquals(0.42,result.getDisbelief(),0.01);
        assertEquals(0.51,result.getUncertainty(),0.01);

    }

    /**
     * Oracle data taken from https://folk.universitetetioslo.no/josang/sl/Op.html
     */
    @Test
    public void deductionTest2(){
        BinomialOpinion x = new BinomialOpinion(0.43, 0.18, 0.39,0.5);
        BinomialOpinion ygx = new BinomialOpinion(0.7, 0, 0.3,0.65);
        BinomialOpinion ynx = new BinomialOpinion(0.41, 0.59, 0,0.65);

        BinomialDeduction bd = new BinomialDeduction(0.65);
        BinomialOpinion result = bd.apply(ygx,ynx,x);
        assertEquals(0.53,result.getBelief(),0.01);
        assertEquals(0.19,result.getDisbelief(),0.01);
        assertEquals(0.27,result.getUncertainty(),0.01);

    }



    

}
