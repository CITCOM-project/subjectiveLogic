package framework.operators;

public abstract class Operator<T> {

    /**
     * Apply the operation. 
     * 
     * @param opinions
     */
    public abstract T apply(T... opinions);
    
}
