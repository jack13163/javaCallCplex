package cplex.cplex_gurobi_gui;
/** 
  * @author:Dylan 
  * @date:2017年11月22日 下午9:05:49 
  */

import java.util.Arrays;
import java.util.List;

public class CSPResult {
	private double[] xValues;
	private double objValue;
	private List<double[]> bestPatterns;
	private List<double[]> columns;

	public CSPResult(double[] xValues, double objValue, List<double[]> bestPatterns2, List<double[]> columns2) {
		super();
		this.xValues = xValues;
		this.objValue = objValue;
		this.bestPatterns = bestPatterns2;
		this.columns = columns2;
	}

	public String showCSPResults() {
		StringBuilder res = new StringBuilder();
		int j = 0;
		for (double[] c : columns) {
			res.append("Add column" + j +" = "+ Arrays.toString(c) + "\n");
			j++;
		}
		res.append("The objective value is: " + objValue + "\n");
		res.append("The decision variables are:\n");
		for (int i = 0; i < xValues.length; i++)
			res.append("x[" + (i + 1) + "] = " + xValues[i] + "\n");
		j = 0;
		for (double[] b : bestPatterns) {
			res.append("Pattern[" + (j + 1) + "] = " + Arrays.toString(b) + "\n");
			j++;
		}
		return res.toString();
	}

	public double[] getxValues() {
		return xValues;
	}

	public void setxValues(double[] xValues) {
		this.xValues = xValues;
	}

	public double getObjValue() {
		return objValue;
	}

	public void setObjValue(double objValue) {
		this.objValue = objValue;
	}

	public List<double[]> getBestPatterns() {
		return bestPatterns;
	}

	public void setBestPatterns(List<double[]> bestPatterns) {
		this.bestPatterns = bestPatterns;
	}

	public List<double[]> getColumns() {
		return columns;
	}

	public void setColumns(List<double[]> columns) {
		this.columns = columns;
	}

}
