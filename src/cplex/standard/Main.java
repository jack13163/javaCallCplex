package cplex.standard;
import java.util.ArrayList;
import java.util.List;
public class Main {
	public static void main(String[] args) {
		//input
		List<Variable> variableList=new ArrayList<Variable>();
		Variable variable1=new Variable();
		variable1.setLowerbound(0);
		variable1.setUpperbound(Integer.MAX_VALUE);
		variable1.setType(MyConstants.type_Integer);
		variableList.add(variable1);
		Variable variable2=new Variable();
		variable2.setLowerbound(0);
		variable2.setUpperbound(Double.MAX_VALUE);
		variable2.setType(MyConstants.type_Number);
		variableList.add(variable2);
		Objective objective=new Objective();
		double[] objCoefficient={8,5};
		objective.setObjCoefficient(objCoefficient);
		objective.setPreference(MyConstants.preference_Maximize);
		List<Constraint> constraintList=new ArrayList<Constraint>();
		Constraint constraint1=new Constraint();
		double[] conCoefficient1={1,1};
		constraint1.setConCoefficient(conCoefficient1);
		constraint1.setRelation(MyConstants.relation_LE);
		constraint1.setRightHandSide(6);
		constraint1.setName("Money");
		Constraint constraint2=new Constraint();
		double[] conCoefficient2={9,5};
		constraint2.setConCoefficient(conCoefficient2);
		constraint2.setRelation(MyConstants.relation_LE);
		constraint2.setRightHandSide(45);
		constraint2.setName("Time");
		constraintList.add(constraint1);
		constraintList.add(constraint2);
		
		//algorithm
		Algorithm algorithm;
		algorithm=new CplexImplementation();
		Result result=algorithm.calculate(variableList, objective, constraintList);
		
		//output
		System.out.println(result.isSolved());
		if(result.isSolved()){
			System.out.println("The objective value: "+result.getObjValue());
			for(int i=0;i<result.getxValue().length;i++) 
				System.out.println("x["+(i+1)+"] = "+result.getxValue()[i]);
		}
		else 
			System.out.println("Sorry, the model has not yet been solved.");	
	}
}
