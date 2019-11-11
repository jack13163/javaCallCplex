package cplex.cplex_read_txt;
/** 
  * @author:Dylan 
  * @date:2017年10月12日 下午10:33:46 
  */
public class Constraint {
	private double[] coef;
	private String relation;
	private double rhs;
	
	public Constraint() {
		super();
	}
	
	public Constraint(double[] coef, String relation, double rhs) {
		super();
		this.coef = coef;
		this.relation = relation;
		this.rhs = rhs;
	}
	
	public double[] getCoef() {
		return coef;
	}
	
	public void setCoef(double[] coef) {
		this.coef = coef;
	}
	
	public String getRelation() {
		return relation;
	}
	
	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	public double getRhs() {
		return rhs;
	}
	
	public void setRhs(double rhs) {
		this.rhs = rhs;
	}

}
