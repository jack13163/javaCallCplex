package cplex.basis;

import java.util.Random;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * @author:Dylan
 * @date:2017年10月2日 下午1:31:50
 */
public class Basis4 {
	// n:number of cities
	static int size = 5;
	// customers
	static double[][] customers = new double[size][2];
	// distance between cities
	static double[][] c = new double[size][size];

	// TSP solver
	public static void main(String[] args) {
		randomGenerateByEdge();
		//randomGenerateByPoint();
		//printMatrix();
		modelAndSolveTSP();
	}

	public static void printMatrix() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(c[i][j] + "  ");
			}
			System.out.println("\n ");
		}
	}

	public static void modelAndSolveTSP() {
		Long start = System.currentTimeMillis();
		try {
			// define model
			IloCplex cplex = new IloCplex();

			// define variable
			IloNumVar[][] x = new IloNumVar[size][];
			for (int i = 0; i < size; i++) {
				x[i] = cplex.boolVarArray(size);
			}
			IloNumVar[] u = cplex.numVarArray(size, 0, Double.MAX_VALUE);

			// define objective
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (j != i) {
						objective.addTerm(c[i][j], x[i][j]);
					}
				}
			}
			cplex.addMinimize(objective);

			// define constraints
			for (int j = 0; j < size; j++) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for (int i = 0; i < size; i++) {
					if (i != j) {
						expr.addTerm(1.0, x[i][j]);
					}
				}
				cplex.addEq(expr, 1.0);
			}
			for (int i = 0; i < size; i++) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for (int j = 0; j < size; j++) {
					if (j != i) {
						expr.addTerm(1.0, x[i][j]);
					}
				}
				cplex.addEq(expr, 1.0);
			}
			for (int i = 1; i < size; i++) {
				for (int j = 1; j < size; j++) {
					if (i != j) {
						IloLinearNumExpr expr = cplex.linearNumExpr();
						expr.addTerm(1.0, u[i]);
						expr.addTerm(-1.0, u[j]);
						expr.addTerm(size - 1, x[i][j]);
						cplex.addLe(expr, size - 2);
					}
				}
			}
			// cplex.readParam("D:\\config.prm");
			// solve model
			if (cplex.solve()) {
				Long stop = System.currentTimeMillis() - start;
				System.out.println(String.format("Size: %d\t Time: %d ", size, stop));
				System.out.println("obj = " + cplex.getObjValue());
				for (int i = 0; i < x.length; i++) {
					for (int j = 0; j < x[i].length; j++) {
						if (i != j && cplex.getValue(x[i][j]) != 0)
							System.out
									.println("x" + (i + 1) + " -> " + "x" + (j + 1) + " = " + cplex.getValue(x[i][j]));
					}
				}
				for (int i = 1; i < u.length; i++) {
					System.out.println("u" + (i + 1) + " = " + cplex.getValue(u[i]));
				}
				// cplex.exportModel("D:\\TSP.lp");
			} else {
				System.out.println("problem not solved");
			}
			cplex.end();

		} catch (IloException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void randomGenerateByPoint() {
		Random random = new Random(100);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < 2; j++) {
				customers[i][j] = random.nextDouble() * 100 + 1;
			}
			//System.out.print(customers[i][0] + "  ");
			//System.out.println(customers[i][1]);
		}
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (i != j)
					c[i][j] = Math.sqrt(Math.pow(customers[i][0] - customers[j][0], 2)
							+ Math.pow(customers[i][1] - customers[j][1], 2));
	}

	public static void randomGenerateByEdge() {
		Random random = new Random(100);
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (row != col) {
					double value = random.nextDouble() * 100 + 1;
					c[row][col] = value;
					c[col][row] = value;
				}
			}
		}
	}
}
