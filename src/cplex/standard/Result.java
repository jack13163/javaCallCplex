package cplex.standard;

public class Result {
	private boolean isSolved;
	private double objValue;
	private double[] xValue;
	public boolean isSolved() {
		return isSolved;
	}
	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
	}
	public double getObjValue() {
		return objValue;
	}
	public void setObjValue(double objValue) {
		this.objValue = objValue;
	}
	public double[] getxValue() {
		return xValue;
	}
	public void setxValue(double[] xValue) {
		this.xValue = xValue;
	}
	
}
