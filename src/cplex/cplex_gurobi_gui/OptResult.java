package cplex.cplex_gurobi_gui;

/**
 * @author:Dylan
 * @date:2017年10月12日 下午10:49:57
 */
public class OptResult {
	private boolean isSolved;
	private double[] xValues;
	private double objValue;
	private String solverName;
	private String solutionStatus;

	public OptResult() {
		super();
	}

	public OptResult(boolean isSolved, double[] xValues, double objValue, String solverName) {
		super();
		this.isSolved = isSolved;
		this.xValues = xValues;
		this.objValue = objValue;
		this.solverName = solverName;
	}

	public String showOptResults() {
		StringBuilder res = new StringBuilder();
		res.append( "This problem has been solved by " + solverName + "!\n");
		res.append( "The solution status is: " + solutionStatus + "\n");
		res.append( "The objective value is: " + objValue + "\n");
		res.append( "The decision variables are:\n");
		for (int i = 0; i < xValues.length; i++)
			res.append( "x[" + (i + 1) + "] = " + xValues[i] + "\n");
		return res.toString();
	}

	public String showErrors() {
		String res = "Sorry, this problem can't be solved yet!\n";
		res = res + "The solution status is: " + solutionStatus + "\n";
		return res;
	}

	public String getSolverName() {
		return solverName;
	}

	public void setSolverName(String solver) {
		this.solverName = solver;
	}

	public boolean isSolved() {
		return isSolved;
	}

	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
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

	public String getSolutionStatus() {
		return solutionStatus;
	}

	public void setSolutionStatus(String solutionStatus) {
		this.solutionStatus = solutionStatus;
	}

}
