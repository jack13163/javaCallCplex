package cplex.cplex_gurobi_gui;
/** 
  * @author:Dylan 
  * @date:2017年10月12日 下午10:24:31 
  */
public class Objective {
	private double[] coef;
	private String type;
	
	public Objective() {
		super();
	}

	public Objective(double[] coef, String type) {
		super();
		this.coef = coef;
		this.type = type;
	}

	public double[] getCoef() {
		return coef;
	}

	public void setCoef(double[] coef) {
		this.coef = coef;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}