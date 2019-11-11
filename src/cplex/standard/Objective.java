package cplex.standard;

public class Objective {
	private String preference;
	private double[] objCoefficient;
	
	public Objective() {
		super();
	}
	public String getPreference() {
		return preference;
	}
	public void setPreference(String preference) {
		this.preference = preference;
	}
	public double[] getObjCoefficient() {
		return objCoefficient;
	}
	public void setObjCoefficient(double[] objCoefficient) {
		this.objCoefficient = objCoefficient;
	}
	
}
