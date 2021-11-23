package citcom.subjectiveLogic.operators.binomial;

import citcom.subjectiveLogic.BinomialOpinion;
import citcom.subjectiveLogic.operators.Operator;

public class BinomialDeduction implements Operator<BinomialOpinion> {

    protected BinomialOpinion yGx, yNx, x;
    protected double biy, diy, uiy, ay;

    public BinomialDeduction(double ay){
        this.ay = ay;
    }

    @Override
    public BinomialOpinion apply(BinomialOpinion... opinions) {
        yGx = opinions[0];
        yNx = opinions[1];
        x = opinions[2];
        biy = x.getBelief()*yGx.getBelief() + x.getDisbelief()*yNx.getBelief() + x.getUncertainty()*(yGx.getBelief() * x.getApriori() + yNx.getBelief() * (1 - x.getApriori()));
        diy = x.getBelief()*yGx.getDisbelief() + x.getDisbelief()*yNx.getDisbelief() + x.getUncertainty()*(yGx.getDisbelief() * x.getApriori() + yNx.getDisbelief() * (1 - x.getApriori()));
        uiy = x.getBelief()*yGx.getUncertainty() + x.getDisbelief()*yNx.getUncertainty() + x.getUncertainty()*(yGx.getUncertainty() * x.getApriori() + yNx.getUncertainty() * (1 - x.getApriori()));

        //operation is only defined for situations where both yGx and yNx are not vacuous.
        assert((yGx.getUncertainty() + yNx.getUncertainty() < 2));
        return deduce();
    }

    private BinomialOpinion deduce() {
        
        double k = calculateK();
        double belief = biy-aY()*k;
        double disbelief = diy-(1-aY())*k;
        double uncertainty = uiy + k;
        BinomialOpinion result = new BinomialOpinion(belief, disbelief, uncertainty);
        return result;
    }

    
    private double aY(){
        return this.ay;
    }

    private double pYXHat(){
        return yGx.getBelief() * x.getApriori() + yNx.getBelief() * (1-x.getApriori()) + aY()*(yGx.getUncertainty() * x.getApriori() + yNx.getUncertainty()*(1-x.getApriori()));
    }

    private double calculateK(){
        double k = -1;
        if(case1()){
            k = 0;
        }
        else if(case2A1()){
            k = (x.getApriori() * x.getUncertainty() * (biy - yNx.getBelief())/
            (x.getBelief() + x.getApriori() * x.getUncertainty())*aY());
        }
        else if(case2A2()){
            k = (x.getApriori() * x.getUncertainty() * (diy - yGx.getDisbelief())*(yGx.getBelief() - yNx.getBelief())/
            ((x.getDisbelief() + (1-x.getApriori())*x.getUncertainty())*aY()*(yNx.getDisbelief()-yGx.getDisbelief())));
        }
        else if(case2B1()){
            k=((1-x.getApriori())*x.getUncertainty()*(biy-yNx.getBelief())*(yNx.getDisbelief()-yGx.getDisbelief()))/
            ((x.getBelief()+x.getApriori()*x.getUncertainty())*(1-aY())*(yGx.getBelief()-yNx.getBelief()));
        }
        else if(case2B2()){
            k=((1-x.getApriori())*x.getUncertainty()*(diy-yGx.getDisbelief()))/
            ((x.getDisbelief()+(1-x.getApriori())*x.getUncertainty())*(1-aY()));
        }
        else if(case3A1()){
            k=((1-x.getApriori())*x.getUncertainty()*(diy-yNx.getDisbelief())*(yNx.getBelief()-yGx.getBelief()))/
            ((x.getBelief()+(x.getApriori()*x.getUncertainty()))*aY()*(yGx.getDisbelief()-yNx.getDisbelief()));
        }
        else if(case3A2()){
            k=((1-x.getApriori())*x.getUncertainty()*(biy-yGx.getBelief()))/
            ((x.getDisbelief()+(1-x.getApriori())*x.getUncertainty())*aY());
        }
        else if(case3B1()){
            k=(x.getApriori()*x.getUncertainty()*(diy-yNx.getDisbelief()))/
            ((x.getBelief()+x.getApriori()*x.getUncertainty())*(1-aY()));
        }
        else if(case3B2()){
            k=(x.getApriori()*x.getUncertainty()*(biy-yGx.getBelief())*(yGx.getDisbelief()-yNx.getDisbelief()))/
            ((x.getDisbelief()+(1-x.getApriori())*x.getUncertainty())*(1-aY())*(yNx.getBelief()-yGx.getBelief()));
        }
        return k;
    }


    private boolean case1(){
        return((yGx.getBelief() > yNx.getBelief())&&
            (yGx.getDisbelief() > yNx.getDisbelief())
            ||(yGx.getBelief() <= yNx.getBelief())&&
            (yGx.getDisbelief() <= yNx.getDisbelief()));
    }

    private boolean case2A1(){
        return((yGx.getBelief()>yNx.getBelief())&&
        (yGx.getDisbelief() > yNx.getDisbelief())&&
        (pYXHat() <= (yNx.getBelief() + aY() * (1-yNx.getBelief() - yGx.getDisbelief()))) &&
        (x.getProjectedProbability() <= x.getApriori()));
    }

    private boolean case2A2(){
        return((yGx.getBelief()>yNx.getBelief())&&
        (yGx.getDisbelief() <= yNx.getDisbelief())&&
        (pYXHat() <= (yNx.getBelief() + aY() * (1-yNx.getBelief() - yGx.getDisbelief()))) &&
        (x.getProjectedProbability() > x.getApriori()));
    }

    private boolean case2B1(){
        return((yGx.getBelief()>yNx.getBelief())&&
        (yGx.getDisbelief() <= yNx.getDisbelief())&&
        (pYXHat() > (yNx.getBelief() + aY() * (1-yNx.getBelief() - yGx.getDisbelief()))) &&
        (x.getProjectedProbability() <= x.getApriori()));
    }

    private boolean case2B2(){
        return((yGx.getBelief()>yNx.getBelief())&&
        (yGx.getDisbelief() <= yNx.getDisbelief())&&
        (pYXHat() > (yNx.getBelief() + aY() * (1-yNx.getBelief() - yGx.getDisbelief()))) &&
        (x.getProjectedProbability() > x.getApriori()));
    }

    private boolean case3A1(){
        return((yGx.getBelief()<=yNx.getBelief())&&
        (yGx.getDisbelief() > yNx.getDisbelief())&&
        (pYXHat() <= (yGx.getBelief() + aY() * (1-yGx.getBelief() - yNx.getDisbelief()))) &&
        (x.getProjectedProbability() <= x.getApriori()));
    }

    private boolean case3A2(){
        return((yGx.getBelief()<=yNx.getBelief())&&
        (yGx.getDisbelief() > yNx.getDisbelief())&&
        (pYXHat() <= (yGx.getBelief() + aY() * (1-yGx.getBelief() - yNx.getDisbelief()))) &&
        (x.getProjectedProbability() > x.getApriori()));
    }

    private boolean case3B1(){
        return((yGx.getBelief()<=yNx.getBelief())&&
        (yGx.getDisbelief() > yNx.getDisbelief())&&
        (pYXHat() > (yGx.getBelief() + aY() * (1-yGx.getBelief() - yNx.getDisbelief()))) &&
        (x.getProjectedProbability() <= x.getApriori()));
    }

    private boolean case3B2(){
        return((yGx.getBelief()<=yNx.getBelief())&&
        (yGx.getDisbelief() > yNx.getDisbelief())&&
        (pYXHat() > (yGx.getBelief() + aY() * (1-yGx.getBelief() - yNx.getDisbelief()))) &&
        (x.getProjectedProbability() > x.getApriori()));
    }
    
}
