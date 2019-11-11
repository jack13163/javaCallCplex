package cplex.standard;
public class Constraint {
	private double[] conCoefficient;
	private String relation;
	private double rightHandSide;
	private String name;
	
	public Constraint() {
		super();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public double[] getConCoefficient() {
		return conCoefficient;
	}
	public void setConCoefficient(double[] conCoefficient) {
		this.conCoefficient = conCoefficient;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public double getRightHandSide() {
		return rightHandSide;
	}
	public void setRightHandSide(double rightHandSide) {
		this.rightHandSide = rightHandSide;
	}
	
}
