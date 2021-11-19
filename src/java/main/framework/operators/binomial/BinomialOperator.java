package framework.operators.binomial;

import framework.BinomialOpinion;

public abstract class BinomialOperator {

    /**
     * Apply the operation. 
     * 
     * @param binomialOpinion
     */
    public abstract BinomialOpinion apply(BinomialOpinion... binomialOpinion);
}
