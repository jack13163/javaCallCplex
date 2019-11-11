package cplex.cplex_read_txt;

import java.util.ArrayList;
import java.util.List;

/** 
  * @author:Dylan 
  * @date:2017年10月13日 上午9:10:52 
  */
public class Main {
	public static void main(String[] args) {
		//Inputs
		String importPath = "model.txt";
		String  exportPath = "model.lp";
		List<Variable> variables = new ArrayList<Variable>();
		Objective objective = new Objective();
		List<Constraint> constraints = new ArrayList<Constraint>();
		
		//Modeling and solving
		ModelReader modelReader = new ModelReader(importPath);
		modelReader.readAndConstruct(variables, objective, constraints);
		Algorithm algorithm = new CplexImplementation();
		OptResult optResult = algorithm.tryToSolve(variables, objective, constraints);
		
		//Outputs
		if(optResult.isSolved()){
			optResult.showOptResults();
			algorithm.exportModel(exportPath);
		}else {
			optResult.showErrors();
		}
		
	}
}

