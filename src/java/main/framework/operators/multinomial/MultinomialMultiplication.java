package framework.operators.multinomial;

import java.util.List;
import java.util.ArrayList;

import framework.operators.Operator;
import framework.MultinomialOpinion;

public class MultinomialMultiplication implements Operator<MultinomialOpinion>{

    @Override
    public MultinomialOpinion apply(MultinomialOpinion... opinions) {
        MultinomialOpinion result = opinions[0];
        for(int i = 1; i< opinions.length; i++){
            result = multiply(result, opinions[i]);
        }
        return(result);
    }

    private MultinomialOpinion multiply(MultinomialOpinion from, MultinomialOpinion to){
        List<List<Double>> singletonBeliefs = new ArrayList<>();
        List<Double> beliefsB = to.getBelief();
        for(int i = 0; i< from.getBelief().size(); i++){

            List<Double> sbeliefs = new ArrayList<>();
            double currentBelief = from.getBelief().get(i);
            for(int j = 0; j<beliefsB.size(); j++){
                sbeliefs.add(currentBelief * beliefsB.get(j));
            }
            singletonBeliefs.add(i,sbeliefs);
        }

        List<Double> brows = new ArrayList<>();
        for(int i = 0; i< from.getBelief().size(); i++){
            brows.add(from.getBelief().get(i) * to.getUncertainty());
        }

        List<Double> bcols = new ArrayList<>();
        for(int i = 0; i< to.getBelief().size(); i++){
            bcols.add(beliefsB.get(i) * from.getUncertainty());
        }

        List<Double> aprioriBeliefs = new ArrayList<>();
        List<Double> aprioriB = to.getApriori();
        for(int i = 0; i< from.getBelief().size(); i++){
            double currentApriori = from.getApriori().get(i);
            for(int j = 0; j<beliefsB.size(); j++){
                aprioriBeliefs.add(currentApriori * aprioriB.get(j));
            }
        }

        double uRows = MultinomialOpinion.sum(brows);
        double uCols = MultinomialOpinion.sum(bcols);
        double uDomain = from.getUncertainty() * to.getUncertainty();

        double maxU = uRows + uCols + uDomain;
        double minU = uDomain;


        double minUxys = maxU;
        for(int i = 0; i< from.getBelief().size(); i++){
            Double bx = from.getBelief().get(i);
            Double ax = from.getApriori().get(i);
            List<Double> singletons = singletonBeliefs.get(i);
            for(int j = 0; j<beliefsB.size(); j++){
                Double by = to.getBelief().get(j);
                Double ay = to.getApriori().get(j);
                double bxys = singletons.get(j);
                double uxy = uXY(bx,from.getUncertainty(),ax,by,to.getUncertainty(),ay,bxys);
                if(uxy < minUxys & uxy >= minU & uxy <= maxU){
                    minUxys = uxy;
                }
            }
        }

        List<Double> productBeliefs = new ArrayList<>();
        for(int i = 0; i< from.getBelief().size(); i++){
            double bx = from.getBelief().get(i);
            double ax = from.getApriori().get(i);
            for(int j = 0; j< to.getBelief().size(); j++){
                double by = beliefsB.get(j);
                double ay = aprioriB.get(j);
                double bxy = (bx+ax*from.getUncertainty())*(by+ay*to.getUncertainty())-ax*ay*minUxys;
                productBeliefs.add(bxy);
            }
        }

        List<List<List>> prodDomains = new ArrayList<>();

        prodDomains.addAll(from.getDomain());
        List<List> thisDomain = (List<List>)from.getDomain().get(from.getDomain().size()-1);
        List<List<List>> otherDomains = to.getDomain();
        List<List> otherDomain = otherDomains.get(to.getDomain().size()-1);
        List<List> prodDomain = new ArrayList();

        for(int i = 0; i<thisDomain.size();i++){
            for(int j = 0; j<otherDomain.size();j++){
                List prefix = thisDomain.get(i);
                List suffix = otherDomain.get(j);
                List concat = new ArrayList();
                concat.addAll(prefix);
                concat.addAll(suffix);
                prodDomain.add(concat);
            }
        }
        prodDomains.add(prodDomain);


        return new MultinomialOpinion(productBeliefs,aprioriBeliefs,prodDomains);

    }


    private double uXY(double bx, double ux,double ax, double by, double uy, double ay, double bxyS){
        return (((((bx+ax*ux)*(by+ay*uy))-bxyS)/(ax*ay)));
    }
    
}
