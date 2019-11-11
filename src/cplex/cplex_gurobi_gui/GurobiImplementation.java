package cplex.cplex_gurobi_gui;

import java.util.List;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

/**
 * @author:Dylan
 * @date:2017年10月28日 下午8:22:52
 */
public class GurobiImplementation implements Algorithm {
	private GRBEnv env;
	private GRBModel model;
	private GRBVar[] x;

	public GurobiImplementation() {
		super();
		try {
			env = new GRBEnv();
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

	public GRBEnv getEnv() {
		return env;
	}

	// Construct the model
	private void constructModel(List<Variable> variables, Objective objective, List<Constraint> constraints) {
		try {
			model = new GRBModel(env);
			// Initialize variables
			x = new GRBVar[variables.size()];
			for (int i = 0; i < variables.size(); i++) {
				if (variables.get(i).getType().equals(Constants.type_Bool)) {
					x[i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, null);
				} else if (variables.get(i).getType().equals(Constants.type_Integer)) {
					x[i] = model.addVar((int) (variables.get(i).getLb()), (int) (variables.get(i).getUb()), 0.0,
							GRB.INTEGER, null);
				} else if (variables.get(i).getType().equals(Constants.type_Number)) {
					x[i] = model.addVar((variables.get(i).getLb()), (variables.get(i).getUb()), 0.0, GRB.CONTINUOUS,
							null);
				}
			}
			// Initialize objective
			GRBLinExpr expr = new GRBLinExpr();
			expr.addTerms(objective.getCoef(), x);
			if (objective.getType().equals(Constants.preference_Maximize)) {
				model.setObjective(expr, GRB.MAXIMIZE);
			} else if (objective.getType().equals(Constants.preference_Minimize)) {
				model.setObjective(expr, GRB.MINIMIZE);
			}
			// Initialize constraints
			for (int i = 0; i < constraints.size(); i++) {
				expr = new GRBLinExpr();
				String name = "constraint" + (i + 1);
				if (constraints.get(i).getRelation().equals(Constants.relation_LE)) {
					expr.addTerms(constraints.get(i).getCoef(), x);
					model.addConstr(expr, GRB.LESS_EQUAL, constraints.get(i).getRhs(), name);
				} else if (constraints.get(i).getRelation().equals(Constants.relation_EQ)) {
					expr.addTerms(constraints.get(i).getCoef(), x);
					model.addConstr(expr, GRB.EQUAL, constraints.get(i).getRhs(), name);
				} else if (constraints.get(i).getRelation().equals(Constants.relation_GE)) {
					expr.addTerms(constraints.get(i).getCoef(), x);
					model.addConstr(expr, GRB.GREATER_EQUAL, constraints.get(i).getRhs(), name);
				}
			}
		} catch (GRBException e) {
			e.printStackTrace();
		}

	}

	// Try to solve the model
	@Override
	public OptResult tryToSolve(List<Variable> variables, Objective objective, List<Constraint> constraints,
			String modelFilePath, String configFilePath, String solverName) {
		if (modelFilePath != null) {
			try {
				// Load model file
				model = new GRBModel(env,modelFilePath);
				// Load parameter file
				if (configFilePath != null) {
					model.read(configFilePath);
				}
				model.optimize();
				model.dispose();
				env.dispose();
			} catch (GRBException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			OptResult optResult = new OptResult();
			try {
				constructModel(variables, objective, constraints);
				// Solve the model
				model.optimize();
				int optimstatus = model.get(GRB.IntAttr.Status);
				if (optimstatus == GRB.Status.OPTIMAL) {
					optResult.setSolved(true);
					optResult.setxValues(model.get(GRB.DoubleAttr.X, x));
					optResult.setObjValue(model.get(GRB.DoubleAttr.ObjVal));
					optResult.setSolverName(solverName);
					optResult.setSolutionStatus("Optimal");
				} else {
					optResult.setSolved(false);
					if(optimstatus == GRB.Status.INF_OR_UNBD)
						optResult.setSolutionStatus("Infeasible or unbounded");
				}
				model.dispose();
				env.dispose();
			} catch (GRBException e) {
				System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
			}
			return optResult;
		}
	}
}
