package cplex.basis;

import ilog.concert.*;
import ilog.cplex.*;

/**
 * @author:Dylan
 * @date:2017年10月2日 上午9:04:45
 */
public class Basis3 {
	// 变量为二维下标问题（二维）
	public static void main(String[] args) {
		int n = 4; // cargos
		int m = 3; // compartments
		double[] p = { 310.0, 380.0, 350.0, 285.0 }; // profit
		double[] v = { 480.0, 650.0, 580.0, 390.0 }; // volume per ton of cargo
		double[] a = { 18.0, 15.0, 23.0, 12.0 }; // available weight
		double[] c = { 10.0, 16.0, 8.0 }; // capacity of compartment
		double[] V = { 6800.0, 8700.0, 5300.0 }; // volume capacity of
		
		try {
			// define model
			IloCplex cplex = new IloCplex();
			
			// define variables
			IloNumVar[][] x = new IloNumVar[n][];
			for (int i = 0; i < n; i++) {
				x[i] = cplex.numVarArray(m, 0, Double.MAX_VALUE);
			}
			IloNumVar y = cplex.numVar(0, Double.MAX_VALUE);
			// define objective
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					objective.addTerm(p[i], x[i][j]);
				}
			}
			cplex.addMaximize(objective);
			
			// constraints:type1
			IloLinearNumExpr availableWeight[] = new IloLinearNumExpr[n];
			for (int i = 0; i < n; i++) {
				availableWeight[i] = cplex.linearNumExpr();
				for (int j = 0; j < m; j++) {
					availableWeight[i].addTerm(1.0, x[i][j]);
				}
				cplex.addLe(availableWeight[i], a[i]);
			}

			// constraints:type2
			IloLinearNumExpr[] usedWeightCapacity = new IloLinearNumExpr[m];
			IloLinearNumExpr[] usedVolumeCapacity = new IloLinearNumExpr[m];
			for (int j = 0; j < m; j++) {
				usedWeightCapacity[j] = cplex.linearNumExpr();
				usedVolumeCapacity[j] = cplex.linearNumExpr();
				for (int i = 0; i < n; i++) {
					usedWeightCapacity[j].addTerm(1.0, x[i][j]);
					usedVolumeCapacity[j].addTerm(v[i], x[i][j]);
				}
				cplex.addLe(usedWeightCapacity[j], c[j]);
				cplex.addLe(usedVolumeCapacity[j], V[j]);
				cplex.addEq(cplex.prod(1 / c[j], usedWeightCapacity[j]), y);
			}

			//control the output information
			cplex.setParam(IloCplex.Param.Simplex.Display, 0);

			// solve model
			if (cplex.solve()) {
				System.out.println("obj = " + cplex.getObjValue());
				for (int i = 0; i < x.length; i++) {
					for (int j = 0; j < x[i].length; j++) {
						System.out.println("x" + (i + 1) + (j + 1) + " = " + cplex.getValue(x[i][j]));
					}
				}

				System.out.println("y = " + cplex.getValue(y));
			} else {
				System.out.println("problem not solved");
			}
			cplex.end();
			
		} catch (IloException e) {
			e.printStackTrace();
		}
	}
}
