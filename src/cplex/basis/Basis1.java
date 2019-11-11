package cplex.basis;

import ilog.concert.*;
import ilog.cplex.*;

/**
 * @author:Dylan
 * @date:2017年10月1日 下午2:39:51
 */
public class Basis1 {
	//多个同质变量问题（一维）
	public static void main(String[] args) {
		try {
			IloCplex cplex = new IloCplex();// 创建模型对象

			double[] lb = { 0.0, 0.0, 0.0 };// 变量下界
			double[] ub = { Double.MAX_VALUE, 5.0, Double.MAX_VALUE };// 变量上界
			IloNumVar[] x = cplex.numVarArray(3, lb, ub);// 变量数组（可以是连续型、整数、0-1变量）

			double[] objvals = { 60.0, 30.0, 20.0 };
			cplex.addMaximize(cplex.scalProd(x, objvals));// 最大化目标函数

			// 约束条件
			cplex.addLe(cplex.sum(cplex.prod(8.0, x[0]), cplex.prod(6.0, x[1]), cplex.prod(1.0, x[2])), 48.0);
			cplex.addLe(cplex.sum(cplex.prod(4.0, x[0]), cplex.prod(2.0, x[1]), cplex.prod(1.5, x[2])), 20.0);
			cplex.addLe(cplex.sum(cplex.prod(2.0, x[0]), cplex.prod(1.5, x[1]), cplex.prod(0.5, x[2])), 8.0);

			//cplex.setParam(IloCplex.IntParam.Simplex.Display, 0);//显示最少的求解过程信息
		
			if (cplex.solve()) {
				System.out.println("Model is solved!-----------------------");
				System.out.println("Solution status = " + cplex.getStatus());
				System.out.println("Solution value = " + cplex.getObjValue());
				double[] val = cplex.getValues(x);
				for (int j = 0; j < val.length; ++j)
					System.out.println("x" + (j+1) + " = " + val[j]);
			}
			else {
				System.out.println("Model is not solved!");
			}
			cplex.end();

		} catch (IloException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
