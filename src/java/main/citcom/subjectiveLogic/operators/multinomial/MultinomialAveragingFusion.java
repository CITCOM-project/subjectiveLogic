package citcom.subjectiveLogic.operators.multinomial;

import citcom.subjectiveLogic.operators.Operator;
import citcom.subjectiveLogic.MultinomialOpinion;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class MultinomialAveragingFusion implements Operator<MultinomialOpinion>{

    @Override
    public MultinomialOpinion apply(MultinomialOpinion... opinions) {
        List<MultinomialOpinion> sources = new ArrayList<MultinomialOpinion>();
        for(MultinomialOpinion bo : opinions){
            sources.add(bo);
        }
        return averagingFusion(sources);
    }
    
    public static MultinomialOpinion averagingFusion(Collection<MultinomialOpinion> opinions){
        List<MultinomialOpinion> opList = new ArrayList();
        opList.addAll(opinions);
        boolean nonZeroUncertainty = true;
        int beliefs = 0;
        for(MultinomialOpinion op : opList){
            if(op.getUncertainty()==0D)
                nonZeroUncertainty = false;
            beliefs = op.getBelief().size();
        }
        List<Double> fusedBeliefs = new ArrayList<>();
        double uncertainty=0;
        if(nonZeroUncertainty){
            for(int i = 0; i<beliefs; i++){
                double numeratorSum = 0D;
                for(int j = 0; j<opList.size(); j++){
                    MultinomialOpinion op = opList.get(j);


                    double uncertaintyProduct = 1D;
                    for(int k = 0; k<opList.size(); k++){
                        if(k==j)
                            continue;
                        MultinomialOpinion curr = opList.get(k);
                        uncertaintyProduct = uncertaintyProduct * curr.getUncertainty();
                    }
                    numeratorSum = numeratorSum + (uncertaintyProduct * op.getBelief().get(i));
                }

                double denominatorSum = getUncertaintyProduct(opList);
                fusedBeliefs.add(i,numeratorSum/denominatorSum);
            }

            double uncertaintyProduct = 1D;
            for(int k = 0; k<opList.size(); k++){
                MultinomialOpinion curr = opList.get(k);
                uncertaintyProduct = uncertaintyProduct * curr.getUncertainty();
            }

            double numerator = beliefs * uncertaintyProduct;

            double denominator = getUncertaintyProduct(opList);
            uncertainty = numerator / denominator;
        }
        else{
            double weight = 1D/opList.size();
            for(int i = 0; i<beliefs; i++) {
                double beliefSum = 0D;
                for (int j = 0; j < opList.size(); j++) {
                    MultinomialOpinion op = opList.get(j);
                    beliefSum+=weight*op.getBelief().get(i);

                }
                fusedBeliefs.add(i,beliefSum);
            }

        }


        return new MultinomialOpinion(fusedBeliefs,opList.get(0).getDomain());
    }



    protected static double getUncertaintyProduct(List<MultinomialOpinion> opList) {
        double denominatorSum = 0D;
        for(int j = 0; j<opList.size(); j++){
            double uncertaintyProduct = 1D;
            for(int k = 0; k<opList.size(); k++){
                if(k==j)
                    continue;
                MultinomialOpinion curr = opList.get(k);
                uncertaintyProduct = uncertaintyProduct * curr.getUncertainty();
            }
            denominatorSum = denominatorSum + (uncertaintyProduct);
        }
        return denominatorSum;
    }




}
