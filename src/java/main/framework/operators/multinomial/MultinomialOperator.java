package framework.operators.multinomial;

import framework.MultinomialOpinion;

public abstract class MultinomialOperator {

    /**
     * Apply the operation. 
     * 
     * @param multinomialOpinion
     */
    public abstract MultinomialOpinion apply(MultinomialOpinion... multinomialOpinion);
}
