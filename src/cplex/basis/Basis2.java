package cplex.basis;

import java.util.ArrayList;
import java.util.List;

import ilog.concert.*;
import ilog.cplex.*;

/**
 * @author:Dylan
 * @date:2017��10��1�� ����2:39:51
 */
public class Basis2 {
	//����ͬ�ʱ������⣨һά��
	public static void main(String[] args) {
		try {
			IloCplex cplex = new IloCplex();// ����ģ�Ͷ���
            
			IloNumVar x = cplex.numVar(0, Double.MAX_VALUE,"x");
			IloNumVar y = cplex.numVar(0, Double.MAX_VALUE,"y");
			
			IloLinearNumExpr obj = cplex.linearNumExpr();//Ŀ�꺯��
			obj.addTerm(0.12, x);
			obj.addTerm(0.15, y);
			cplex.addMinimize(obj);
			
			List<IloRange> constraints = new ArrayList<IloRange>();//Լ������
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(60, x),cplex.prod(60, y)), 300));
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(12, x),cplex.prod(6, y)), 36));
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(10, x),cplex.prod(30, y)), 90));
			constraints.add(cplex.addEq(cplex.sum(cplex.prod(2, x),cplex.prod(-1, y)), 0));
			constraints.add(cplex.addLe(cplex.sum(cplex.prod(1,y),cplex.prod(-1, x)), 8));
			
			//cplex.setParam(IloCplex.IntParam.Simplex.Display, 0);//��ʾ���ٵ���������Ϣ
			
			if (cplex.solve()) {
				System.out.println("Model is solved!-----------------------");
				System.out.println("Solution status = " + cplex.getStatus());
				System.out.println("obj = "+ cplex.getObjValue());
				System.out.println("x = " + cplex.getValue(x));
				System.out.println("y = " + cplex.getValue(y));
				for(int i =0; i < constraints.size(); i++) {
					System.out.println("slack constraint"+(i+1)+":"+cplex.getSlack(constraints.get(i)));
					System.out.println("dual constraint"+(i+1)+":"+cplex.getDual(constraints.get(i)));//getDualֻ�����Թ滮��
				}
			}
			else {
				System.out.println("Model is not solved!");
			}
			cplex.end();
		
		} catch (IloException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
