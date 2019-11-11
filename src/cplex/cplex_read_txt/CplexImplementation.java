package cplex.cplex_read_txt;

import java.util.List;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/** 
  * @author:Dylan 
  * @date:2017年10月13日 上午9:19:42 
  */
public class CplexImplementation implements Algorithm{
	private IloCplex cplex;
	private IloNumVar[] x;
	
	public CplexImplementation() {
		super();
		try {
			cplex = new IloCplex();
		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	@Override
	public OptResult tryToSolve(List<Variable> variables, Objective objective, List<Constraint> constraints) {
		OptResult optResult = new OptResult();
		try {
			//Initialize variables
			x = new IloNumVar[variables.size()];
			for(int i = 0; i < variables.size(); i++) {
				if(variables.get(i).getType().equals(Constants.type_Bool)) {
					x[i] = cplex.boolVar();
				}else if (variables.get(i).getType().equals(Constants.type_Integer)) {
					x[i] = cplex.intVar((int)(variables.get(i).getLb()),(int)(variables.get(i).getUb()));
				}else {
					x[i] = cplex.numVar((variables.get(i).getLb()),(variables.get(i).getUb()));
				}
			}
			//Initialize objective
			if(objective.getType().equals(Constants.preference_Maximize)) {
				cplex.addMaximize(cplex.scalProd(x, objective.getCoef()));
			}
			else {
				cplex.addMinimize(cplex.scalProd(x, objective.getCoef()));
			}
			//Initialize constraints
			for(int i = 0; i < constraints.size(); i++) {
				String name = "constraint"+(i+1);
				if(constraints.get(i).getRelation().equals(Constants.relation_LE)){
					cplex.addLe(cplex.scalProd(x, constraints.get(i).getCoef()), constraints.get(i).getRhs(),name);
				}else if(constraints.get(i).getRelation().equals(Constants.relation_EQ)) {
					cplex.addEq(cplex.scalProd(x, constraints.get(i).getCoef()), constraints.get(i).getRhs(),name);
				}else {
					cplex.addGe(cplex.scalProd(x, constraints.get(i).getCoef()), constraints.get(i).getRhs(),name);
				}
			}
			//Solve the model
			if(cplex.solve()) {
				//cplex.setParam(IloCplex.IntParam.Simplex.Display, 0);//显示最少的求解过程信息
				optResult.setSolved(true);
				optResult.setxValues(cplex.getValues(x));
				optResult.setObjValue(cplex.getObjValue());
				}
			else {
				optResult.setSolved(false);
				cplex.end();
			}
			
		} catch (IloException e) {
			e.printStackTrace();
		}
		return optResult;
	}
	public void exportModel(String exportPath) {
		try {
			cplex.exportModel(exportPath);
			cplex.end();
		} catch (IloException e) {
			e.printStackTrace();
		}
	}
}
