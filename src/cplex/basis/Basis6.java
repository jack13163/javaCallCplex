package cplex.basis;

import ilog.concert.*;
import ilog.cplex.*;

/** 
  * @author:Dylan 
  * @date:2017年10月2日 下午3:50:25 
  */
public class Basis6 {
	//Solution to a very simple quadratic model: a quadratic objective with linear constraints.
	public static void main(String[] args) throws IloException {
		IloCplex cplex = new IloCplex();
		
		IloNumVar x1 = cplex.numVar(0, 40,"x1");
		IloNumVar x2 = cplex.numVar(0, Double.MAX_VALUE,"x2");
		IloNumVar x3 = cplex.numVar(0, Double.MAX_VALUE,"x3");
		
		cplex.addMaximize(cplex.sum(
				x1,
				cplex.prod(2, x2),
				cplex.prod(3, x3),
				cplex.prod(-16.5, x1,x1),
				cplex.prod(-11,x2,x2),
				cplex.prod(-5.5,x3,x3),
				cplex.prod(6,x1,x2),
				cplex.prod(11.5, x2,x3)
				));
		
		cplex.addLe(cplex.sum(cplex.prod(-1, x1),x2,x3), 20.0);
		cplex.addLe(cplex.sum(x1,cplex.prod(-3, x2),x3), 30.0);
		if(cplex.solve()) {
			System.out.println("obj = "+ cplex.getObjValue());
			System.out.println("x1 = "+ cplex.getValue(x1));
			System.out.println("x2 = "+ cplex.getValue(x2));
			System.out.println("x3 = "+ cplex.getValue(x3));
		}else {
			System.out.println("Failed to solve this model!");
		}
		
	}

}
