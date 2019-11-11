package cplex.standard;
import java.util.List;

import cplex.cplex_gurobi_gui.Constants;
import ilog.cplex.IloCplex;
import ilog.concert.*;
public class CplexImplementation implements Algorithm {
	private IloCplex cplex;
	private IloNumVar[] x;
	public CplexImplementation() {
		super();
		try {
			cplex=new IloCplex();
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void solveAndOutputResult(Result result) throws IloException{
		if(cplex.solve()) {
			//cplex.setParam(IloCplex.IntParam.Simplex.Display, 0);//显示最少的求解过程信息
			result.setSolved(true);
			result.setObjValue(cplex.getObjValue());
			result.setxValue(cplex.getValues(x));
			}
		else {
			result.setSolved(false);
		}
		cplex.end();
	}
	
	private void constructCplexModel(List<Variable> variableList, Objective objective, List<Constraint> constraintList) throws IloException {
		x=new IloNumVar[variableList.size()];
		for(int i=0;i<variableList.size();i++) {
			if(variableList.get(i).getType().equals(MyConstants.type_Integer)) 
				x[i]=cplex.intVar((int)(variableList.get(i).getLowerbound()), (int)(variableList.get(i).getUpperbound()));
			else if(variableList.get(i).getType().equals(MyConstants.type_Number))
				x[i]=cplex.numVar(variableList.get(i).getLowerbound(), variableList.get(i).getUpperbound());
		}
		if(objective.getPreference().equals(MyConstants.preference_Maximize))
			cplex.addMaximize(cplex.scalProd(x, objective.getObjCoefficient()));
		else if(objective.getPreference().equals(MyConstants.preference_Minimize))
			cplex.addMinimize(cplex.scalProd(x, objective.getObjCoefficient()));
		for(int j=0;j<constraintList.size();j++) {
		//	IloLinearNumExpr left=cplex.linearNumExpr();
		//	left.addTerms(x, constraintList.get(j).getConCoefficient());
	    //  cplex.addEq(left,constraintList.get(j).getRightHandSide())
			if(constraintList.get(j).getRelation().equals(MyConstants.relation_EQ))
				cplex.addEq(cplex.scalProd(x, constraintList.get(j).getConCoefficient()), constraintList.get(j).getRightHandSide(),constraintList.get(j).getName());
			else if(constraintList.get(j).getRelation().equals(MyConstants.relation_GE))
				cplex.addGe(cplex.scalProd(x, constraintList.get(j).getConCoefficient()), constraintList.get(j).getRightHandSide(),constraintList.get(j).getName());
			else if(constraintList.get(j).getRelation().equals(MyConstants.relation_LE))
				cplex.addLe(cplex.scalProd(x, constraintList.get(j).getConCoefficient()), constraintList.get(j).getRightHandSide(),constraintList.get(j).getName());
		}
	};
	public void exportModel() {
		try {
			cplex.exportModel(MyConstants.export_Address);
		} catch (IloException e) {
			e.printStackTrace();
		}
	}
	public Result calculate(List<Variable> variableList, Objective objective, List<Constraint> constraintList) {
		Result result=new Result();
		try {
			constructCplexModel(variableList, objective,constraintList);
			exportModel();
			solveAndOutputResult(result);
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}