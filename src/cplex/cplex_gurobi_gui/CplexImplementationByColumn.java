package cplex.cplex_gurobi_gui;

import java.util.List;

import ilog.concert.IloColumn;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

/**
 * @author:Dylan
 * @date:2017年11月1日 下午1:57:14
 */
public class CplexImplementationByColumn implements Algorithm {
	private IloCplex cplex;
	private IloColumn column;
	private IloRange[] cons;
	private IloObjective obj;
	private IloNumVar[] x;

	public CplexImplementationByColumn() {
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
			// Initialize objective type
			if (objective.getType().equals(Constants.preference_Maximize)) {
				obj = cplex.addMaximize();
			} else if (objective.getType().equals(Constants.preference_Minimize)) {
				obj = cplex.addMinimize();
			}
			// Initialize constraint range
			cons = new IloRange[constraints.size()];
			for (int i = 0; i < constraints.size(); i++) {
				String name = "constraint" + (i + 1);
				if (constraints.get(i).getRelation().equals(Constants.relation_EQ)) {
					cons[i] = cplex.addRange(constraints.get(i).getRhs(), constraints.get(i).getRhs(), name);
				} else if (constraints.get(i).getRelation().equals(Constants.relation_GE)) {
					cons[i] = cplex.addRange(constraints.get(i).getRhs(), Double.MAX_VALUE, name);
				} else if (constraints.get(i).getRelation().equals(Constants.relation_LE)) {
					cons[i] = cplex.addRange(-Double.MAX_VALUE, constraints.get(i).getRhs(), name);
				}
			}
			// Initialize variable coefficients
			x = new IloNumVar[variables.size()];
			for (int i = 0; i < variables.size(); i++) {
				column = cplex.column(obj, objective.getCoef()[i]);
				for (int j = 0; j < constraints.size(); j++)
					column = column.and(cplex.column(cons[j], constraints.get(j).getCoef()[i]));
				if (variables.get(i).getType().equals(Constants.type_Bool)) {
					x[i] = cplex.numVar(column, variables.get(i).getLb(), variables.get(i).getUb(), IloNumVarType.Bool);
				} else if (variables.get(i).getType().equals(Constants.type_Integer)) {
					x[i] = cplex.numVar(column, variables.get(i).getLb(), variables.get(i).getUb(), IloNumVarType.Int);
				} else if (variables.get(i).getType().equals(Constants.type_Number)) {
					x[i] = cplex.numVar(column, variables.get(i).getLb(), variables.get(i).getUb(),
							IloNumVarType.Float);
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
