package cplex.cplex_read_txt;
/** 
  * @author:Dylan 
  * @date:2017年10月12日 下午8:42:02 
  */
public class Variable {
	private double lb;
	private double ub;
	private String type;
	
	public Variable() {
		super();
	}
	
	public Variable(double lb, double ub, String type) {
		super();
		this.lb = lb;
		this.ub = ub;
		this.type = type;
	}
	
	public double getLb() {
		return lb;
	}
	
	public void setLb(double lb) {
		this.lb = lb;
	}
	
	public double getUb() {
		return ub;
	}
	
	public void setUb(double ub) {
		this.ub = ub;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
}
