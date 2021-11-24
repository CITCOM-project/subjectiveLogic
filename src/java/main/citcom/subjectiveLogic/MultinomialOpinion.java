package citcom.subjectiveLogic;

import java.util.ArrayList;
import java.util.List;

public class MultinomialOpinion extends SubjectiveOpinion<List<Double>>{


    /**
     * The top-level list: A list of domains that have been multiplied together to produce this domain.
     * Second-level list: List of elements corresponding to a specific domain.
     * Third-level list: Either a single String or, if product, a list of strings that have been combined together.
     * TODO - need a better data structure to represent this...
     */
    protected List<List<List>> domain;

    protected boolean isSimplified = false;


    public MultinomialOpinion(List<Double> belief, List<Double> apriori, List<List<List>> domain) {
        super(belief,apriori,0);
        uncertainty = 1-sum(belief);
        this.domain = domain;
    }

    public boolean isSimplified(){
        return isSimplified;
    }

    /**
     * Create multinomial opinion with uninformative priors.
     * @param belief
     */
    public MultinomialOpinion(List<Double> belief, List domain) {
        super(belief,null,0);
        apriori = new ArrayList<>();
        for(int i = 0; i<belief.size(); i++){
            apriori.add(1D/belief.size());
        }
        this.domain = domain;
        this.uncertainty = 1-sum(belief);
    }

    public BinomialOpinion coarsen(String outgoing){
        List<List> dom = domain.get(domain.size()-1);
        int targetIndex = -1;
        for(int i = 0; i<dom.size(); i++){
            List seq = dom.get(i);
            if(seq.get(0).equals(outgoing)){

                targetIndex = i;
                break;
            }

        }

        if(targetIndex<0)
            targetIndex = 0; //We assume it's asking for a vacuous opinion.




        double targetBelief = belief.get(targetIndex);
        double apriori = getApriori().get(targetIndex);


        BinomialOpinion retOp = new BinomialOpinion(targetBelief,1-(uncertainty+targetBelief),uncertainty,apriori);
        return retOp;
    }


    

    /**
     * Big challenge with multinomial opinions is the exponential growth when multiplied together, especially several
     * times in a row. If, however, you are only interested in the probabilities arising from a specific belief, you
     * can really trim down the effort by turning the belief into a 2-valued one: by retaining the belief mass
     * for the element X in question, and aggregating to another synthetic belief standing for "not X".
     * @param outgoing
     * @return
     */
    public MultinomialOpinion simplify(List outgoing){
        List<List> dom = domain.get(domain.size()-1);
        int targetIndex = -1;
        for(int i = 0; i<dom.size(); i++){
            List<List> seq = dom.get(i);
            boolean match = true;
            for(int j = 0; j< outgoing.size(); j++){
                Object from = seq.get(j);
                Object to = outgoing.get(j);
                if(!from.equals(to)){
                    match = false;
                    break;
                }
            }
            if(match){
                targetIndex = i;
                break;
            }

        }

        if(targetIndex<0)
            targetIndex = 0; //We assume it's asking for a vacuous opinion.
        double targetBelief = belief.get(targetIndex);
        double apriori = getApriori().get(targetIndex);
        double restBelief = sum(belief) - targetBelief;
        double restApriori = sum(getApriori())-apriori;
        List subject = new ArrayList<String>();
        subject.addAll(outgoing);
        List notSubject = new ArrayList<String>();
        notSubject.add("not");

        List<Double> beliefs = new ArrayList<>();
        beliefs.add(targetBelief);
        beliefs.add(restBelief);
        List<Double> simpleApriori = new ArrayList<>();
        simpleApriori.add(apriori);
        simpleApriori.add(restApriori);

        List<List<List>> newDoms = new ArrayList<>();
        newDoms.addAll(domain.subList(0,domain.size()-1));

        List<List> latest = new ArrayList<>();
        latest.add(subject);
        latest.add(notSubject);

        newDoms.add(latest);
        MultinomialOpinion simplified = new MultinomialOpinion(beliefs,simpleApriori,newDoms);
        simplified.isSimplified = true;
        assert(beliefs.size() == simpleApriori.size());
        return simplified;
    }


    public MultinomialOpinion clone(){
        return new MultinomialOpinion(belief,apriori,domain);
    }


    public List getDomain() {
        return domain;
    }

    public static double sum(List<Double> brows) {
        double result = 0D;
        for(Double b : brows){
            result+=b;
        }
        return result;
    }

}
