package cplex.standard;

public class Variable {
	private double lowerbound;
	private double upperbound;
	private String type;
	public Variable() {
		super();
	}
	public double getLowerbound() {
		return lowerbound;
	}
	public void setLowerbound(double lowerbound) {
		this.lowerbound = lowerbound;
	}
	public double getUpperbound() {
		return upperbound;
	}
	public void setUpperbound(double upperbound) {
		this.upperbound = upperbound;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
