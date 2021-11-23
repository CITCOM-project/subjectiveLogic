package citcom.subjectiveLogic;

import citcom.subjectiveLogic.operators.binomial.BinomialMultiSourceFusion;
import citcom.subjectiveLogic.operators.binomial.BinomialMultiplication;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class BinomialOpinionTest {

    @Test
    public void multiply() {
        BinomialOpinion from = new BinomialOpinion(0.15,0.5,0.34,0.5);
        BinomialOpinion to = new BinomialOpinion(0.56,0.09,0.35,0.31);

        BinomialMultiplication multiply = new BinomialMultiplication();

        BinomialOpinion result = multiply.apply(from,to);

        assertEquals(0.17,result.getBelief(),0.01);
        assertEquals(0.55,result.getDisbelief(),0.01);
        assertEquals(0.27,result.getUncertainty(),0.01);
        assertEquals(0.16,result.getApriori(),0.01);


    }

    /**
     * Example from Figure 7.2 in Josang Subjective Logic Book
     */
    @Test
    public void multiply2() {
        BinomialOpinion from = new BinomialOpinion(0.75,0.15,0.10,0.5);
        BinomialOpinion to = new BinomialOpinion(0.10,0.00,0.90,0.2);
        
        BinomialMultiplication multiply = new BinomialMultiplication();
        BinomialOpinion result = multiply.apply(from,to);

        assertEquals(0.15,result.getBelief(),0.01);
        assertEquals(0.15,result.getDisbelief(),0.01);
        assertEquals(0.70,result.getUncertainty(),0.01);
        assertEquals(0.10,result.getApriori(),0.01);


    }

    @Test
    public void multiplyExample() {
        BinomialOpinion openA = new BinomialOpinion(0.3,0.15,0.55,0.5);
        BinomialOpinion editC = new BinomialOpinion(0.45,0.3,0.25,0.5);
        BinomialOpinion closeD = new BinomialOpinion(0.15,0.6,0.25,0.5);
        BinomialOpinion exitA = new BinomialOpinion(0.15,0,0.85,0.5);

        BinomialMultiplication multiply = new BinomialMultiplication();

        BinomialOpinion result = multiply.apply(openA,editC,closeD,exitA);

        System.out.println(result);


    }


    /**
     * Test from Table 1 (WBF column) in van der Hejden paper
     * https://arxiv.org/pdf/1805.01388.pdf
     */
    @Test
    public void multiSourceFusion() {

        BinomialOpinion b1 = new BinomialOpinion(0.1,0.3,0.6);
        BinomialOpinion b2 = new BinomialOpinion(0.4,0.2,0.4);
        BinomialOpinion b3 = new BinomialOpinion(0.7,0.1,0.2);


        BinomialMultiSourceFusion bmsf = new BinomialMultiSourceFusion();
        BinomialOpinion result = bmsf.apply(b1,b2,b3);

        assertEquals(0.562,result.getBelief(),0.01);
        assertEquals(0.146,result.getDisbelief(),0.01);
        assertEquals(0.292,result.getUncertainty(),0.01);
        assertEquals(0.5,result.getApriori(),0.01);

        System.out.println(result);

    }
}