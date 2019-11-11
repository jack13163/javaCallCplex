package cplex.cplex_gurobi_gui;

import java.util.List;
import gurobi.GRB;
import gurobi.GRBColumn;
import gurobi.GRBConstr;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

/**
 * @author:Dylan
 * @date:2017年11月2日 下午7:46:43
 */
public class GurobiImplementationByColumn implements Algorithm {
	private GRBEnv env;
	private GRBModel model;
	private GRBColumn column;
	private GRBConstr[] cons;
	private GRBVar[] x;

	public GurobiImplementationByColumn() {
		super();
		try {
			env = new GRBEnv();
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

	// Construct the model
	private void constructModel(List<Variable> variables, Objective objective, List<Constraint> constraints) {
		try {
			model = new GRBModel(env);
			// Initialize objective type
			if (objective.getType().equals(Constants.preference_Maximize)) {
				model.set(GRB.IntAttr.ModelSense, GRB.MAXIMIZE);
			} else if (objective.getType().equals(Constants.preference_Minimize)) {
				model.set(GRB.IntAttr.ModelSense, GRB.MINIMIZE);
			}
			// Initialize constraint range
			cons = new GRBConstr[constraints.size()];
			for (int i = 0; i < constraints.size(); i++) {
				String name = "constraint" + (i + 1);
				GRBLinExpr expr = new GRBLinExpr();
				if (constraints.get(i).getRelation().equals(Constants.relation_EQ)) {
					cons[i] = model.addRange(expr, constraints.get(i).getRhs(), constraints.get(i).getRhs(), name);
				} else if (constraints.get(i).getRelation().equals(Constants.relation_GE)) {
					cons[i] = model.addRange(expr, constraints.get(i).getRhs(), Double.MAX_VALUE, name);
				} else if (constraints.get(i).getRelation().equals(Constants.relation_LE)) {
					cons[i] = model.addRange(expr, -Double.MAX_VALUE, constraints.get(i).getRhs(), name);
				}
			}
			// Initialize variable coefficients
			x = new GRBVar[variables.size()];
			for (int i = 0; i < variables.size(); i++) {
				column = new GRBColumn();
				for (int j = 0; j < constraints.size(); j++)
					column.addTerm(constraints.get(j).getCoef()[i], cons[j]);
				if (variables.get(i).getType().equals(Constants.type_Bool)) {
					x[i] = model.addVar(variables.get(i).getLb(), variables.get(i).getUb(), objective.getCoef()[i],
							GRB.BINARY, column, null);
				} else if (variables.get(i).getType().equals(Constants.type_Integer)) {
					x[i] = model.addVar(variables.get(i).getLb(), variables.get(i).getUb(), objective.getCoef()[i],
							GRB.INTEGER, column, null);
				} else if (variables.get(i).getType().equals(Constants.type_Number)) {
					x[i] = model.addVar(variables.get(i).getLb(), variables.get(i).getUb(), objective.getCoef()[i],
							GRB.CONTINUOUS, column, null);
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
				model = new GRBModel(env, modelFilePath);
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
