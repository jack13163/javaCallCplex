package cplex.cplex_read_txt;
/** 
  * @author:Dylan 
  * @date:2017年10月12日 下午10:49:57 
  */
public class OptResult {
	private boolean isSolved;
	private double[] xValues;
	private double objValue;
	
	public OptResult() {
		super();
	}

	public OptResult(boolean isSolved, double[] xValues, double objValue) {
		super();
		this.isSolved = isSolved;
		this.xValues = xValues;
		this.objValue = objValue;
	}
	
	public void showOptResults() {
		System.out.println("=============================");
		System.out.println("This problem has been solved!");
		System.out.println("The optimal objective value is: "+ objValue);
		System.out.println("The optimal decision variables are: ");
		for(int i = 0; i < xValues.length; i++) 
			System.out.println("x["+(i+1)+"] = "+xValues[i]);
	}
	public void showErrors() {
		System.out.println("Sorry, this problem can't be solved yet!");
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
	
}
