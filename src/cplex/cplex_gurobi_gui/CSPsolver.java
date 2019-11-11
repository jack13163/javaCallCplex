package cplex.cplex_gurobi_gui;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import ilog.concert.IloColumn;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

/**
 * @author:Dylan
 * @date:2017年11月22日 下午9:25:15
 */
public class CSPsolver {
	private int type;
	private double width;
	private double[][] size;
	private double[][] demand;
	private double[] xValues;
	private double objValue;
	private List<double[]> columns;
	private List<double[]> bestPatterns;

	public CSPsolver(double width, double[][] size, double[][] demand, int type) {
		super();
		this.width = width;
		this.size = size;
		this.demand = demand;
		this.type = type;
	}

	public CSPResult solveCSP() {
		// Problem size
		int n = size[0].length;
		// Decision variables
		xValues = new double[n];
		// Objective value
		objValue = 0;
		// Record the cut patterns of LPM
		columns = new ArrayList<double[]>();
		// Record the final cut patterns
		bestPatterns = new ArrayList<double[]>();

		// ---Initialize matrix---
		double[][] Binit = new double[n][n];
		double[][] cbinit = new double[1][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					Binit[i][j] = (int) (width / size[0][i]);
				}
				if (i == 0) {
					cbinit[0][j] = 1.;
				}
			}
		}
		// Basis
		Matrix B = new Matrix(Binit);
		// rhs
		Matrix b = new Matrix(demand);
		b = b.transpose();
		// cB
		Matrix cb = new Matrix(cbinit);
		// X
		Matrix X = null;
		// Dual price
		Matrix D = null;
		try {
			// ---Subproblem problem---
			IloCplex subModel = new IloCplex();
			IloObjective subObj = subModel.addMinimize();
			IloNumVar[] use = subModel.numVarArray(size[0].length, 0., Double.MAX_VALUE, IloNumVarType.Int);
			subModel.addRange(-Double.MAX_VALUE, subModel.scalProd(size[0], use), width);
			// ---Main loop---
			while (true) {
				// Solve X
				X = B.inverse().times(b);
				// Calculate dual price
				D = cb.times(B.inverse());
				// Set the objective of subproblem
				subObj.setExpr(subModel.diff(1.0, subModel.scalProd(use, D.getArray()[0])));
				// Optimize the subproblem
				subModel.solve();
				if (subModel.getObjValue() > -1e-8)
					break;
				// Get the new pattern column coefficients
				double[][] newPattern = new double[1][n];
				newPattern[0] = subModel.getValues(use);
				columns.add(newPattern[0]);
				// Minimum ratio test
				Matrix Y = new Matrix(newPattern);
				Y = Y.transpose();
				Matrix lower = B.inverse().times(Y);
				Matrix ratio = new Matrix(1, n, Double.MAX_VALUE);
				for (int i = 0; i < n; i++) {
					if (lower.get(i, 0) > 0) {
						ratio.set(0, i, X.get(i, 0) / lower.get(i, 0));
					}
				}
				int index = 0;
				double minRatio = 0;
				for (int i = 0; i < n; i++) {
					if (i == 0) {
						minRatio = ratio.get(0, i);
					}
					if (ratio.get(0, i) < minRatio) {
						minRatio = ratio.get(0, i);
						index = i;
					}
				}
				// Update basis
				for (int i = 0; i < n; i++) {
					B.set(i, index, Y.get(i, 0));
				}
			}
			// Set x and objective values and bestPatterns
			for (int i = 0; i < n; i++) {
				xValues[i] = X.get(i, 0);
				objValue += xValues[i];
				double[] temp = new double[n];
				for (int j = 0; j < n; j++) {
					temp[j] = B.get(j, i);
				}
				bestPatterns.add(temp);
			}

		} catch (IloException e) {
			e.printStackTrace();
		}
		if (type == 1) {
			ceilingResult();
		} else if (type == 2) {
			integerResult();
		}
		return new CSPResult(xValues, objValue, bestPatterns, columns);
	}

	private void ceilingResult() {
		objValue = 0;
		for (int i = 0; i < xValues.length; i++) {
			xValues[i] = Math.ceil(xValues[i]);
			objValue += xValues[i];
		}
	}

	private void integerResult() {
		try {
			IloCplex cplex = new IloCplex();
			// Initialize objective type
			IloObjective obj = cplex.addMinimize();
			// Initialize constraint range
			IloRange[] cons = new IloRange[demand[0].length];
			for (int i = 0; i < cons.length; i++) {
				cons[i] = cplex.addRange(demand[0][i], Double.MAX_VALUE);
			}
			// Initialize variable coefficients
			IloColumn column;
			IloNumVar[] x = new IloNumVar[demand[0].length];
			for (int i = 0; i < x.length; i++) {
				column = cplex.column(obj, 1.);
				for (int j = 0; j < cons.length; j++) {
					column = column.and(cplex.column(cons[j], bestPatterns.get(i)[j]));
					x[i] = cplex.numVar(column, 0, Integer.MAX_VALUE, IloNumVarType.Int);
				}
			}
			// Solve
			if (cplex.solve()) {
				for (int i = 0; i < x.length; i++) {
					xValues[i] = cplex.getValue(x[i]);
				}
				objValue = cplex.getObjValue();
			}

		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
