package cplex.cplex_gurobi_gui;

import java.util.List;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * @author:Dylan
 * @date:2017年10月13日 上午9:19:42
 */
public class CplexImplementation implements Algorithm {
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

	// Construct the model
	private void constructModel(List<Variable> variables, Objective objective, List<Constraint> constraints) {
		try {
			// Initialize variables
			x = new IloNumVar[variables.size()];
			for (int i = 0; i < variables.size(); i++) {
				if (variables.get(i).getType().equals(Constants.type_Bool)) {
					x[i] = cplex.boolVar();
				} else if (variables.get(i).getType().equals(Constants.type_Integer)) {
					x[i] = cplex.intVar((int) (variables.get(i).getLb()), (int) (variables.get(i).getUb()));
				} else if (variables.get(i).getType().equals(Constants.type_Number)) {
					x[i] = cplex.numVar((variables.get(i).getLb()), (variables.get(i).getUb()));
				}
			}
			// Initialize objective
			if (objective.getType().equals(Constants.preference_Maximize)) {
				cplex.addMaximize(cplex.scalProd(x, objective.getCoef()));
			} else if (objective.getType().equals(Constants.preference_Minimize)) {
				cplex.addMinimize(cplex.scalProd(x, objective.getCoef()));
			}
			// Initialize constraints
			for (int i = 0; i < constraints.size(); i++) {
				String name = "constraint" + (i + 1);
				if (constraints.get(i).getRelation().equals(Constants.relation_LE)) {
					cplex.addLe(cplex.scalProd(x, constraints.get(i).getCoef()), constraints.get(i).getRhs(), name);
				} else if (constraints.get(i).getRelation().equals(Constants.relation_EQ)) {
					cplex.addEq(cplex.scalProd(x, constraints.get(i).getCoef()), constraints.get(i).getRhs(), name);
				} else if (constraints.get(i).getRelation().equals(Constants.relation_GE)) {
					cplex.addGe(cplex.scalProd(x, constraints.get(i).getCoef()), constraints.get(i).getRhs(), name);
				}
			}
		} catch (IloException e) {
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
				cplex.importModel(modelFilePath);
				// Load parameter file
				if (configFilePath != null) {
					cplex.readParam(configFilePath);
				}
				cplex.solve();
				cplex.end();
			} catch (IloException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			OptResult optResult = new OptResult();
			try {
				constructModel(variables, objective, constraints);
				// Solve the model
				if (cplex.solve()) {
					optResult.setSolved(true);
					optResult.setxValues(cplex.getValues(x));
					optResult.setObjValue(cplex.getObjValue());
					optResult.setSolverName(solverName);
					optResult.setSolutionStatus(cplex.getStatus().toString());
				} else {
					optResult.setSolved(false);
					optResult.setSolutionStatus(cplex.getStatus().toString());
				}
				cplex.end();
			} catch (IloException e) {
				e.printStackTrace();
			}
			return optResult;
		}
	}
}