# subjectiveLogic


A small framework to support and Subjective Logic operations. Pull requests very welcome.

## Subjective Logic Java Framework

There is the abstract framework.SubjectiveOpinion class, and then the two concrete subclasses: BinomialOpinion and MultinomialOpinion. These represent the raw opinions.

Operators implement the simple framework.operators.Operator interface, which has the single apply function. This takes an arbitrary number of parameters (the relevant number of parameters depends on the operator) and returns an opinion of a given type (i.e. operators that implement BinomialOpinion operators will return an opinion of type BinomialOpinion).

You can add additional operators to the framework.operators package.

Examples are provided within the test cases. Test cases are currently based on examples from the Subjective Logic book.

Please feel free to add operations as you see fit to the concrete classes. 


## R code

The R code is really just some utility code to support visualisation of barycentric triangles (for binomial opinions) and corresponding Beta-distributions.
