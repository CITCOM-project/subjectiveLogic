package framework.operators.binomial;

import framework.BinomialOpinion;

public class BinomialMultiplication extends BinomialOperator {


    @Override
    public BinomialOpinion apply(BinomialOpinion... binomialOpinions) {
        BinomialOpinion result = binomialOpinions[0];
        for(int i = 1; i< binomialOpinions.length; i++){
            result = multiply(result, binomialOpinions[i]);
        }
        return(result);
    }

    private BinomialOpinion multiply(BinomialOpinion from, BinomialOpinion to){
        double newbelief = (from.getBelief()*to.getBelief())+((((1-from.getApriori())*to.getApriori()*from.getBelief()*to.getUncertainty())+(from.getApriori()*(1-to.getApriori())*from.getUncertainty()*to.getBelief()))/
                (1-(from.getApriori()*to.getApriori())));
        double newDisbelief = from.getDisbelief() + to.getDisbelief()-( from.getDisbelief()*to.getDisbelief());
        double newUncertainty = (from.getUncertainty()*to.getUncertainty())+((((1-to.getApriori())*from.getBelief()*to.getUncertainty())+((1-from.getApriori())*from.getUncertainty()*to.getBelief()))/
                (1-(from.getApriori()*to.getApriori())));
        double newApriori = from.getApriori()*to.getApriori();
        return new BinomialOpinion(newbelief, newDisbelief, newUncertainty, newApriori);
    
    }
}
