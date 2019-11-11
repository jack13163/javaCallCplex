package cplex.standard;
import java.util.List;
public interface Algorithm {
	public Result calculate(List<Variable> variableList, Objective objective, List<Constraint> constraintList);
}
