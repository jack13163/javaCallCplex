package cplex.cplex_read_txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * @author:Dylan
 * @date:2017年10月13日 上午8:39:02
 */
public class ModelReader {
	private String path;
	FileReader fr = null;
	BufferedReader br = null;

	public ModelReader() {
		super();
	}

	public ModelReader(String path) {
		super();
		this.path = path;
	}

	// Read model and construct the variables, objective and constraints
	public void readAndConstruct(List<Variable> variables, Objective objective, List<Constraint> constraints) {
		try {
			File file = new File(path);
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String str;
			String[] splits;
			double[] coef = null;
			// Set variables
			while (!(str = br.readLine()).equals("#")) {
				splits = str.trim().split("\\s+");
				Variable variale = new Variable();
				if (splits[0].equals(Constants.type_Bool)) {
					variale.setType(Constants.type_Bool);
					variale.setLb(0); 
					variale.setUb(1);
					variables.add(variale);
				} else if (splits[0].equals(Constants.type_Integer)) {
					variale.setType(Constants.type_Integer);
					variale.setLb(stringToDouble(splits[1]));
					if (splits[2].equals("inf")) {
						variale.setUb(Integer.MAX_VALUE);
					} else {
						variale.setUb(stringToDouble(splits[2]));
					}
					variables.add(variale);
				} else {
					variale.setType(Constants.type_Number);
					variale.setLb(stringToDouble(splits[1]));
					if (splits[2].equals("inf")) {
						variale.setUb(Double.MAX_VALUE);
					} else {
						variale.setUb(stringToDouble(splits[2]));
					}
					variables.add(variale);
				}
			}
			// Set objective
			str = br.readLine();
			splits = str.trim().split("\\s+");
			if (splits[0].equals(Constants.preference_Maximize))
				objective.setType(Constants.preference_Maximize);
			else
				objective.setType(Constants.preference_Minimize);
			coef = new double[splits.length - 1];
			for (int i = 1; i < splits.length; i++) {
				coef[i - 1] = stringToDouble(splits[i]);
			}
			objective.setCoef(coef);
			// Set constraints
			while ((str = br.readLine()) != null) {
				splits = str.trim().split("\\s+");
				Constraint constraint = new Constraint();
				coef = new double[variables.size()];
				for (int i = 0; i < variables.size(); i++) {
					coef[i] = stringToDouble(splits[i]);
				}
				constraint.setCoef(coef);
				if (splits[variables.size()].equals("<=")) {
					constraint.setRelation(Constants.relation_LE);
				} else if (splits[variables.size()].equals("=")) {
					constraint.setRelation(Constants.relation_EQ);
				} else {
					constraint.setRelation(Constants.relation_GE);
				}
				constraint.setRhs(stringToDouble(splits[variables.size() + 1]));
				constraints.add(constraint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//String to double
	private double stringToDouble(String s) {
		String regex = "-?\\d+/\\d+";
		if (s.matches(regex)) {
			String temp[] = s.split("/");
			return Double.parseDouble(temp[0]) / Double.parseDouble(temp[1]);
		} else {
			return Double.parseDouble(s);
		}

	}

}
