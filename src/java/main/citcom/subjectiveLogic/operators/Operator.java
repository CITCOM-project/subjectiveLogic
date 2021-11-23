package citcom.subjectiveLogic.operators;

public interface Operator<T> {

    /**
     * Applies the operator and returns the result. 
     * 
     * Depending on the operator, there can be an arbitrary number of parameters.
     * 
     * The assumption is that the return type will be the same as the parameter types.
     * 
     * I.e. An operator for BinomialOpinions will take BinomialOpinion objects as parameters
     * and will return a BinomialOpinion as output.
     * 
     * @param opinions
     */
    public abstract T apply(T... opinions);
    
}
