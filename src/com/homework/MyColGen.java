package com.homework;

import java.util.ArrayList;
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
  * @date:2017年10月19日 下午12:45:04 
  */
public class MyColGen {
	
	   public static void main(String[] args) {
		   // ---Input parameters---
		    double width = 17;
		    double[] size = {3,4,5};
		    double[] demand = {20,25,30};
		    
		  try {
			    // ---Linear programming master problem---
			    IloCplex lpmModel = new IloCplex();
		        IloObjective lpmObj = lpmModel.addMinimize();
		        IloRange[] constraint = new IloRange[demand.length];
		        // Add constraint range: demand[i] < = constraint[i] < = inf
		        for (int i = 0; i < demand.length; i++ ) {
		        	constraint[i] = lpmModel.addRange(demand[i], Double.MAX_VALUE);
		         }
		        // Record the decision variables of LPM
		        List<IloNumVar> cuts = new ArrayList<IloNumVar>();
		        // Record the cut patterns of LPM
		        List<double[]> patterns = new ArrayList<double[]>();
		        // Initialize column
		         for (int i = 0; i < size.length; i++)
		        	 cuts.add(lpmModel.numVar(lpmModel.column(lpmObj, 1.0).and(
		        			lpmModel.column(constraint[i], (int)(width/size[i]))), 0., Double.MAX_VALUE));
		         // ---Subproblem problem---
		         IloCplex subModel = new IloCplex();
		         IloObjective subObj = subModel.addMinimize();
		         IloNumVar[] use = subModel.numVarArray(size.length, 0., Double.MAX_VALUE,
		        		 IloNumVarType.Int);
		         // Add constraint
		         subModel.addRange(-Double.MAX_VALUE, subModel.scalProd(size, use), width);
		         // ---Main loop for column generation ---
		         while (true){
		        	 //Optimize the LPM problem 
		        	 lpmModel.solve();
		        	 //Get the dual price
		        	 double[] price = lpmModel.getDuals(constraint);
		        	 //Set the objective of subproblem
		        	 subObj.setExpr(subModel.diff(1.0,subModel.scalProd(use, price)));
		        	 //Optimize the subproblem 
		        	 subModel.solve();
		        	 //Stop condition
		             if (subModel.getObjValue() > -1e-8)
		                 break;
		             //Get the new pattern column coefficients
		             double[] newPattern = subModel.getValues(use);
		             patterns.add(newPattern);
		             //Set the new pattern column coefficients to the LPM
		             IloColumn column = lpmModel.column(lpmObj, 1.0);
		             for (int i = 0; i < newPattern.length; i++)
		                column = column.and(lpmModel.column(constraint[i], newPattern[i]));
		              cuts.add(lpmModel.numVar(column, 0., Double.MAX_VALUE));
		         }
		         // Use the following codes to get the integer solution of master-problem
		         for ( int i = 0; i < cuts.size(); i++ ) {
		        	 lpmModel.add(lpmModel.conversion(cuts.get(i),IloNumVarType.Int));
		          }
		         lpmModel.solve();
		         System.out.println("The optimal value is: " + lpmModel.getObjValue());
		         System.out.println("The optimal solution is:");
		         for ( int i = 0; i < cuts.size(); i++) { 
		        	 double temp = lpmModel.getValue(cuts.get(i));
		        	 if(temp != 0) {
		        		 System.out.println("Cut" + (i+1) + " = " + temp);
		        		 System.out.print("Pattern" + (i+1) + " = " + "[ ");
		        		 double[] temp2 = patterns.get(i-3);
		        		 for(int j = 0; j < temp2.length; j++)
		        			 System.out.printf("%.2f ",temp2[j]);
		        		 System.out.println("]");
		        	 } 	
		         }
		} catch (IloException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
