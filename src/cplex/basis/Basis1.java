package cplex.basis;

import ilog.concert.*;
import ilog.cplex.*;

/**
 * @author:Dylan
 * @date:2017��10��1�� ����2:39:51
 */
public class Basis1 {
	//���ͬ�ʱ������⣨һά��
	public static void main(String[] args) {
		try {
			IloCplex cplex = new IloCplex();// ����ģ�Ͷ���

			double[] lb = { 0.0, 0.0, 0.0 };// �����½�
			double[] ub = { Double.MAX_VALUE, 5.0, Double.MAX_VALUE };// �����Ͻ�
			IloNumVar[] x = cplex.numVarArray(3, lb, ub);// �������飨�����������͡�������0-1������

			double[] objvals = { 60.0, 30.0, 20.0 };
			cplex.addMaximize(cplex.scalProd(x, objvals));// ���Ŀ�꺯��

			// Լ������
			cplex.addLe(cplex.sum(cplex.prod(8.0, x[0]), cplex.prod(6.0, x[1]), cplex.prod(1.0, x[2])), 48.0);
			cplex.addLe(cplex.sum(cplex.prod(4.0, x[0]), cplex.prod(2.0, x[1]), cplex.prod(1.5, x[2])), 20.0);
			cplex.addLe(cplex.sum(cplex.prod(2.0, x[0]), cplex.prod(1.5, x[1]), cplex.prod(0.5, x[2])), 8.0);

			//cplex.setParam(IloCplex.IntParam.Simplex.Display, 0);//��ʾ���ٵ���������Ϣ
		
			if (cplex.solve()) {
				System.out.println("Model is solved!-----------------------");
				System.out.println("Solution status = " + cplex.getStatus());
				System.out.println("Solution value = " + cplex.getObjValue());
				double[] val = cplex.getValues(x);
				for (int j = 0; j < val.length; ++j)
					System.out.println("x" + (j+1) + " = " + val[j]);
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
