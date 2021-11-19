package framework;


import java.util.Objects;

public class BinomialOpinion extends SubjectiveOpinion<Double>{

    protected Double disbelief;

    public BinomialOpinion(double belief, double disbelief, double uncertainty, double apriori) {
        super(belief,apriori,uncertainty);
        this.disbelief = disbelief;
    }

    public BinomialOpinion(double belief, double disbelief, double uncertainty) {
        super(belief,0.5D,uncertainty);
        this.disbelief = disbelief;
    }

    public double getProjectedProbability(){
        return belief + (apriori * uncertainty);
    }

    public Double getDisbelief() {
        return disbelief;
    }



    public void setDisbelief(Double disbelief) {
        this.disbelief = disbelief;
    }


    public BinomialOpinion clone(){
        return new BinomialOpinion(belief,disbelief,uncertainty,apriori);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinomialOpinion that = (BinomialOpinion) o;
        return Double.compare(that.belief, belief) == 0 &&
                Double.compare(that.disbelief, disbelief) == 0 &&
                Double.compare(that.uncertainty, uncertainty) == 0 &&
                Double.compare(that.apriori, apriori) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(belief, disbelief, uncertainty, apriori);
    }

    public String toString(){
        return belief+","+disbelief+","+uncertainty+","+apriori;
    }
}
