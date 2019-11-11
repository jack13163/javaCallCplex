package com.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Jama.Matrix;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloObjective;
import ilog.cplex.IloCplex;

/**
 * @author:Dylan
 * @date:2017年11月22日 下午6:00:18
 */
// 主问题的求解基于改进单纯型法
// 矩阵运算基于jama包
public class MyColGenByRSM {
	public static void main(String[] args) {
		// ---Input parameters---
		double width = 17;
		double[][] size = { { 3, 4, 5 } };
		double[][] demand = { { 20, 25, 30 } };

		// ---Initialize matrix---
		int n = size[0].length;
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
	     // Record the cut patterns of LPM
        List<double[]> patterns = new ArrayList<double[]>();
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
				patterns.add(newPattern[0]);
				System.out.println("增加新的列" + Arrays.toString(newPattern[0]));
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
			for (int i = 0; i < n; i++) {
				System.out.println(X.get(i, 0));
			}
			B.print(4, 2);
		} catch (IloException e) {
			e.printStackTrace();
		}

	}
}
