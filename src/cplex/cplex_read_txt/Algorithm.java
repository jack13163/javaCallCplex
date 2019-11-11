package cplex.cplex_read_txt;
/** 
  * @author:Dylan 
  * @date:2017年10月25日 下午2:08:12 
  */
import java.util.List;
public interface Algorithm {
	public OptResult tryToSolve(List<Variable> variables, Objective objective, List<Constraint> constraints);
	public void exportModel(String exportPath);
}

	