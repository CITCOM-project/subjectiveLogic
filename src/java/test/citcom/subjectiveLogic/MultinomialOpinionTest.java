package citcom.subjectiveLogic;


import org.junit.Test;
import static org.junit.Assert.*;


import citcom.subjectiveLogic.MultinomialOpinion;
import citcom.subjectiveLogic.operators.multinomial.MultinomialAveragingFusion;
import citcom.subjectiveLogic.operators.multinomial.MultinomialMultiplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MultinomialOpinionTest {

    /**
     * Test from page 126 in Subjective Logic book.
     */
    @Test
    public void multiply() {

        List<Double> gender = new ArrayList<>();
        gender.add(.6);
        gender.add(.3);
        List<Double> aprioriGender = new ArrayList<>();
        aprioriGender.add(0.5);
        aprioriGender.add(0.5);
        List<List<List>> domsA = new ArrayList<>();
        List<List> domainGender = new ArrayList<>();
        List<String> m = new ArrayList<>();
        m.add("M");
        List<String> f = new ArrayList<>();
        f.add("F");
        domainGender.add(m);
        domainGender.add(f);
        domsA.add(domainGender);
        MultinomialOpinion a = new MultinomialOpinion(gender, aprioriGender, domsA);

        List<Double> mutation = new ArrayList<>();
        mutation.add(.7);
        mutation.add(.2);
        List<Double> aprioriMutation = new ArrayList<>();
        aprioriMutation.add(0.5);
        aprioriMutation.add(0.5);
        List<List<List>> domsB = new ArrayList<>();
        List<List> domainMutant = new ArrayList<>();
        List<String> y1 = new ArrayList<>();
        y1.add("Y1");
        List<String> y2 = new ArrayList<>();
        y2.add("Y2");
        domainMutant.add(y1);
        domainMutant.add(y2);
        domsB.add(domainMutant);
        MultinomialOpinion b = new MultinomialOpinion(mutation, aprioriMutation, domsB);

        MultinomialMultiplication mm = new MultinomialMultiplication();
        MultinomialOpinion multi = mm.apply(a,b);

        for (Double product : multi.getBelief()) {
            System.out.println(product);
        }
    }

    @Test
    public void simplificationTest(){
        List<Double> gender = new ArrayList<>();
        gender.add(.6);
        gender.add(.3);
        List<Double> aprioriGender = new ArrayList<>();
        aprioriGender.add(0.5);
        aprioriGender.add(0.5);
        List<List<List>> domsA = new ArrayList<>();
        List<List> domainGender = new ArrayList<>();
        List<String> m = new ArrayList<>();
        m.add("M");
        List<String> f = new ArrayList<>();
        f.add("F");
        domainGender.add(m);
        domainGender.add(f);
        domsA.add(domainGender);
        MultinomialOpinion a = new MultinomialOpinion(gender,aprioriGender,domsA);

        List<Double> mutation = new ArrayList<>();
        mutation.add(.7);
        mutation.add(.2);
        List<Double> aprioriMutation = new ArrayList<>();
        aprioriMutation.add(0.5);
        aprioriMutation.add(0.5);
        List<List<List>> domsB = new ArrayList<>();
        List<List> domainMutant = new ArrayList<>();
        List<String> y1 = new ArrayList<>();
        y1.add("Y1");
        List<String> y2 = new ArrayList<>();
        y2.add("Y2");
        domainMutant.add(y1);
        domainMutant.add(y2);
        domsB.add(domainMutant);
        MultinomialOpinion b = new MultinomialOpinion(mutation,aprioriMutation,domsB);

        MultinomialMultiplication mm = new MultinomialMultiplication();
        MultinomialOpinion multiplied = mm.apply(a,b);

        List specificCombo = new ArrayList();
        specificCombo.add("M");
        specificCombo.add("Y2");

        MultinomialOpinion simplified = multiplied.simplify(specificCombo);

        assertTrue(simplified.belief.get(0) == 0.135);
    }

    @Test
    public void averagingFusionTest(){
        List<Double> gender = new ArrayList<>();
        gender.add(.6);
        gender.add(.3);
        List<Double> aprioriGender = new ArrayList<>();
        aprioriGender.add(0.5);
        aprioriGender.add(0.5);
        List<List<List>> domsA = new ArrayList<>();
        List<List> domainGender = new ArrayList<>();
        List<String> m = new ArrayList<>();
        m.add("M");
        List<String> f = new ArrayList<>();
        f.add("F");
        domainGender.add(m);
        domainGender.add(f);
        domsA.add(domainGender);
        MultinomialOpinion a = new MultinomialOpinion(gender,aprioriGender,domsA);

        List<Double> mutation = new ArrayList<>();
        mutation.add(.7);
        mutation.add(.2);
        List<Double> aprioriMutation = new ArrayList<>();
        aprioriMutation.add(0.5);
        aprioriMutation.add(0.5);
        List<List<List>> domsB = new ArrayList<>();
        List<List> domainMutant = new ArrayList<>();
        List<String> y1 = new ArrayList<>();
        y1.add("Y1");
        List<String> y2 = new ArrayList<>();
        y2.add("Y2");
        domainMutant.add(y1);
        domainMutant.add(y2);
        domsB.add(domainMutant);
        MultinomialOpinion b = new MultinomialOpinion(mutation,aprioriMutation,domsB);

        Collection<MultinomialOpinion> toFuse = new ArrayList<>();
        toFuse.add(a);
        toFuse.add(b);

        MultinomialAveragingFusion mm = new MultinomialAveragingFusion();
        MultinomialOpinion fused = mm.apply(a,b);

        for(int i = 0; i<fused.getBelief().size(); i++){
            System.out.println(fused.getBelief().get(i));
        }
        System.out.println("uncertainty: "+fused.getUncertainty());
    }

    @Test
    public void averagingFusionTestZeroUncertainty(){
        List<Double> gender = new ArrayList<>();
        gender.add(.6);
        gender.add(.4);
        List<Double> aprioriGender = new ArrayList<>();
        aprioriGender.add(0.5);
        aprioriGender.add(0.5);
        List<List<List>> domsA = new ArrayList<>();
        List<List> domainGender = new ArrayList<>();
        List<String> m = new ArrayList<>();
        m.add("M");
        List<String> f = new ArrayList<>();
        f.add("F");
        domainGender.add(m);
        domainGender.add(f);
        domsA.add(domainGender);
        MultinomialOpinion a = new MultinomialOpinion(gender,aprioriGender,domsA);

        List<Double> mutation = new ArrayList<>();
        mutation.add(.7);
        mutation.add(.2);
        List<Double> aprioriMutation = new ArrayList<>();
        aprioriMutation.add(0.5);
        aprioriMutation.add(0.5);
        List<List<List>> domsB = new ArrayList<>();
        List<List> domainMutant = new ArrayList<>();
        List<String> y1 = new ArrayList<>();
        y1.add("Y1");
        List<String> y2 = new ArrayList<>();
        y2.add("Y2");
        domainMutant.add(y1);
        domainMutant.add(y2);
        domsB.add(domainMutant);
        MultinomialOpinion b = new MultinomialOpinion(mutation,aprioriMutation,domsB);
        MultinomialAveragingFusion mm = new MultinomialAveragingFusion();
        MultinomialOpinion fused = mm.apply(a,b);

        for(int i = 0; i<fused.getBelief().size(); i++){
            System.out.println(fused.getBelief().get(i));
        }
        System.out.println("uncertainty: "+fused.getUncertainty());
    }
}