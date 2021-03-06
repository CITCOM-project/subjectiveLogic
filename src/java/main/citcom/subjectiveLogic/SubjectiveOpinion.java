package citcom.subjectiveLogic;

public abstract class SubjectiveOpinion<T> {

    protected T belief,  apriori;

    protected double uncertainty;

    public SubjectiveOpinion(T belief, T apriori, double uncertainty) {
        this.belief = belief;
        this.apriori = apriori;
        this.uncertainty = uncertainty;
    }

    public T getBelief() {
        return belief;
    }

    public T getApriori() {
        return apriori;
    }

    public Double getUncertainty() {
        return uncertainty;
    }

    public void setBelief(T belief) {
        this.belief = belief;
    }

    public void setApriori(T apriori) {
        this.apriori = apriori;
    }

    public void setUncertainty(double uncertainty) {
        this.uncertainty = uncertainty;
    }

    

}
