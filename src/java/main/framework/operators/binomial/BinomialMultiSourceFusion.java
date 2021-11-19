package framework.operators.binomial;

import java.util.List;
import java.util.ArrayList;

import framework.operators.Operator;
import framework.BinomialOpinion;

public class BinomialMultiSourceFusion extends Operator<BinomialOpinion>{

    @Override
    public BinomialOpinion apply(BinomialOpinion... binomialOpinion) {
        
        List<BinomialOpinion> sources = new ArrayList<BinomialOpinion>();
        for(BinomialOpinion bo : binomialOpinion){
            sources.add(bo);
        }
        return(multiSourceFusion(sources));
        
    }

    public static BinomialOpinion multiSourceFusion(List<BinomialOpinion> sources){
        double sm = 0;
        for(int i = 0; i<sources.size(); i++){
            BinomialOpinion bo = sources.get(i);
            double el = bo.getBelief() * (1-bo.getUncertainty());
            double pd = 1;
            for(int j = 0; j<sources.size(); j++){
                if(j==i)
                    continue;
                BinomialOpinion co = sources.get(j);
                pd = pd * co.getUncertainty();
            }
            sm = sm + (el * pd);
        }
        double dsm = 0;

        double overAllPD = 1;
        double uncertaintySum = 0;
        for(int i = 0; i<sources.size(); i++) {
            double pd = 1;
            uncertaintySum += sources.get(i).getUncertainty();
            overAllPD = overAllPD * sources.get(i).getUncertainty();
            for (int j = 0; j < sources.size(); j++) {
                if(j==i)
                    continue;
                BinomialOpinion co = sources.get(j);
                pd = pd * co.getUncertainty();

            }
            dsm = dsm + pd;
        }

        double numSources = (double)sources.size();


        double belief = (sm / (dsm-numSources* overAllPD));
        double uncertainty = ((numSources - uncertaintySum)*overAllPD)/(dsm - numSources * overAllPD);
        double disbelief = 1-(belief + uncertainty);
        return new BinomialOpinion(belief,disbelief,uncertainty);
    }
    



}
