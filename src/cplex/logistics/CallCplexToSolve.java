package cplex.logistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * @author:Dylan
 * @date:2017��11��3�� ����9:36:45
 */
public class CallCplexToSolve {
	// ------------------------------------����׼��1-----------------------------------------
	static double CAPACITY = 3600; // ���Ķװ����
	static List<Arc> arcs = readData(); // ���л��ļ���
	static List<Arc> arcsD = new ArrayList<Arc>(); // ʡ�ʻ��ļ���
	static List<Arc> arcsS = new ArrayList<Arc>(); // ʡ�ڻ��ļ���

	public static void main(String[] args) {
		// ------------------------------------����׼��2-----------------------------------------
		for (Arc arc : arcs) {
			if ((arc.getFromNode() <= 5 && arc.getToNode() >= 6) || (arc.getFromNode() >= 6 && arc.getToNode() <= 5)) {
				arcsD.add(arc);
			}
			if ((arc.getFromNode() <= 5 && arc.getToNode() <= 5) || (arc.getFromNode() >= 6 && arc.getToNode() >= 6)) {
				arcsS.add(arc);
			}
		}
		int arcsLen = arcs.size(); // 90
		int arcsDLen = arcsD.size(); // 50
		int arcsSLen = arcsS.size(); // 40
		int[] des = new int[arcsLen]; // O-D����D�ļ���(��������Ϊ0��D)

		for (int i = 0; i < arcsLen; i++) {
			des[i] = arcs.get(i).getToNode();
		}
		try {
			IloCplex cplex = new IloCplex();
			// ------------------------------------������߱���-----------------------------------------
			IloNumVar[][] x = new IloNumVar[arcsLen][arcsDLen];
			IloNumVar[] y = new IloNumVar[arcsDLen];
			IloNumVar[][] load = new IloNumVar[arcsLen][arcsDLen];
			for (int i = 0; i < arcsLen; i++) {
				x[i] = cplex.boolVarArray(arcsDLen);
				load[i] = cplex.numVarArray(arcsDLen, 0, Double.MAX_VALUE);
			}
			for (int i = 0; i < arcsDLen; i++) {
				y[i] = cplex.intVar(0, Integer.MAX_VALUE);
			}
			// ------------------------------------����Ŀ�꺯��-----------------------------------------
			IloLinearNumExpr interCost = cplex.linearNumExpr();
			IloLinearNumExpr innerCost = cplex.linearNumExpr();
			IloLinearNumExpr obj = cplex.linearNumExpr();
			for (int i = 0; i < arcsDLen; i++) {
				interCost.addTerm(3.6 * arcsD.get(i).getDistance() + 450, y[i]);
			}

			for (int i = 0; i < arcsSLen; i++) {
				for (int j = 0; j < arcsDLen; j++) {
					innerCost.addTerm((3.6 * arcsS.get(i).getDistance() + 450) / CAPACITY,
							load[convertIndex(arcsS.get(i))][j]);
				}
			}
			obj.add(interCost);
			obj.add(innerCost);
			// cplex.addMinimize(cplex.sum(interCost, innerCost));
			cplex.addMinimize(obj);
			// ------------------------------------����Լ������-----------------------------------------
			IloLinearNumExpr expr;
			IloLinearNumExpr expr1;
			IloLinearNumExpr expr2;
			// Լ��1��ÿ��OD�е�O����Ҫ��������ֻ����һ��
			for (int i = 0; i < arcsDLen; i++) {
				expr = cplex.linearNumExpr();
				for (int j = 0; j < arcsLen; j++) {
					if (arcsD.get(i).getFromNode() == arcs.get(j).getFromNode())
						expr.addTerm(1.0, x[j][i]);
				}
				cplex.addEq(expr, 1.0);
			}
			// Լ��2��ÿ��OD�е�D����Ҫ���ﲢ��ֻ����һ��
			for (int i = 0; i < arcsDLen; i++) {
				expr = cplex.linearNumExpr();
				for (int j = 0; j < arcsLen; j++) {
					if (arcsD.get(i).getToNode() == arcs.get(j).getToNode())
						expr.addTerm(1.0, x[j][i]);
				}
				cplex.addEq(expr, 1.0);
			}
			// Լ��3��ÿ��OD�а���·���е��м�ڵ㣬 ��= ��
			for (int i = 0; i < arcsDLen; i++) {
				for (int j : des) {
					expr1 = cplex.linearNumExpr();
					expr2 = cplex.linearNumExpr();
					if (j != arcsD.get(i).getFromNode() && j != arcsD.get(i).getToNode()) {
						for (int k = 0; k < arcsLen; k++) {
							if (arcs.get(k).getToNode() == j) {
								expr1.addTerm(1.0, x[k][i]);
							}
							if (arcs.get(k).getFromNode() == j) {
								expr2.addTerm(1.0, x[k][i]);
							}
						}
					}
					cplex.addEq(expr1, expr2);
				}
			}
			// Լ��4����ʼ�㲻��
			for (int i = 0; i < arcsDLen; i++) {
				expr = cplex.linearNumExpr();
				for (int j = 0; j < arcsLen; j++) {
					if (arcsD.get(i).getFromNode() == arcs.get(j).getToNode())
						expr.addTerm(1.0, x[j][i]);
				}
				cplex.addEq(expr, 0.);
			}
			// Լ��5����ֹ�㲻��
			for (int i = 0; i < arcsDLen; i++) {
				expr = cplex.linearNumExpr();
				for (int j = 0; j < arcsLen; j++) {
					if (arcsD.get(i).getToNode() == arcs.get(j).getFromNode())
						expr.addTerm(1.0, x[j][i]);
				}
				cplex.addEq(expr, 0.);
			}
			// Լ��6�����ѡ�������������������ء������·�����������ʵ���������� ����Ϊ0
			for (int i = 0; i < arcsDLen; i++) {
				for (int j = 0; j < arcsLen; j++) {
					expr1 = cplex.linearNumExpr();
					expr2 = cplex.linearNumExpr();
					expr1.addTerm(1.0, load[j][i]);
					expr2.addTerm(arcsD.get(i).getDemand(), x[j][i]);
					cplex.addEq(expr1, expr2);
				}
			}
			// Լ��7����ʡ��֮������������3600����ȡ����
			for (int i = 0; i < arcsDLen; i++) {
				expr1 = cplex.linearNumExpr();
				expr2 = cplex.linearNumExpr();
				for (int j = 0; j < arcsDLen; j++) {
					expr1.addTerm(1 / CAPACITY, load[convertIndex(arcsD.get(i))][j]);
				}
				expr2.addTerm(1.0, y[i]);
				cplex.addLe(expr1, expr2);
			}
			// Լ��8����ʡ��֮������������3600����ȡ����
			for (int i = 0; i < arcsDLen; i++) {
				expr1 = cplex.linearNumExpr();
				expr2 = cplex.linearNumExpr();
				for (int j = 0; j < arcsDLen; j++) {
					expr1.addTerm(1 / CAPACITY, load[convertIndex(arcsD.get(i))][j]);
				}
				expr2.addTerm(1.0, y[i]);
				cplex.addGe(cplex.diff(expr1, expr2), -1.0);
			}
			// cplex.setParam(IloCplex.Param.TimeLimit, 20);
			// cplex.setParam(IloCplex.Param.MIP.Tolerances.MIPGap,0.1);
			// cplex.readParam("D:/MyCodes/workspace(Java)/javaCallCplex/config_Cplex.prm");
			if (cplex.solve()) {
				// cplex.exportModel("logistics.lp");
				System.out
						.println("------------------------------------���������ŵ�Ŀ�꺯��ֵ------------------------------------");
				System.out.println(cplex.getObjValue());
				System.out.println("------------------------------------������·��------------------------------------");
				for (int i = 0; i < arcsDLen; i++) {
					System.out.println("-------------------------------");
					System.out.println("y[" + arcsD.get(i).getFromNode() + ", " + arcsD.get(i).getToNode() + "]" + " = "
							+ cplex.getValue(y[i]));
					for (int j = 0; j < arcsLen; j++) {
						if (cplex.getValue(x[j][i]) != 0)
							System.out.println("x[" + arcs.get(j).getFromNode() + ", " + arcs.get(j).getToNode() + ", "
									+ arcsD.get(i).getFromNode() + ", " + arcsD.get(i).getToNode() + "]" + " = "
									+ cplex.getValue(x[j][i]) + "\t \t load = " + cplex.getValue(load[j][i]));

					}
				}

			}
		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	public static List<Arc> readData() {
		String path = "src/cplex/logistics/data.txt";
		FileReader fr = null;
		BufferedReader br = null;
		Arc arc = null;
		String str = null;
		String[] splits = null;
		List<Arc> arcs = new ArrayList<Arc>();
		try {
			File file = new File(path);
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			while ((str = br.readLine()) != null) {
				arc = new Arc();
				splits = str.trim().split("\\s+");
				arc.setFromNode(Integer.parseInt(splits[0]));
				arc.setToNode(Integer.parseInt(splits[1]));
				arc.setDistance(Double.parseDouble(splits[2]));
				arc.setDemand(Double.parseDouble(splits[3]));
				arcs.add(arc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arcs;
	}

	public static int convertIndex(Arc a) {
		int fromNode = a.getFromNode();
		int toNode = a.getToNode();
		int index = 0;
		for (int k = 0; k < arcs.size(); k++) {
			if (arcs.get(k).getFromNode() == fromNode && arcs.get(k).getToNode() == toNode) {
				index = k;
			}
		}
		return index;
	}

}

class Arc {
	private int fromNode;
	private int toNode;
	private double distance;
	private double demand;

	public Arc() {
		super();
	}

	public int getFromNode() {
		return fromNode;
	}

	public void setFromNode(int fromNode) {
		this.fromNode = fromNode;
	}

	public int getToNode() {
		return toNode;
	}

	public void setToNode(int toNode) {
		this.toNode = toNode;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDemand() {
		return demand;
	}

	public void setDemand(double demand) {
		this.demand = demand;
	}

}
