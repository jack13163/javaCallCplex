package cplex.basis;
/** 
  * @author:Dylan 
  * @date:2017��10��2�� ����3:00:21 
  */

import ilog.concert.*;
import ilog.cplex.*;

public class Basis5 {
    // ��ȡmps�ļ�������⣬����������
	public static void main(String[] args) {
           try {
			IloCplex cplex = new IloCplex();
			cplex.importModel("D:\\MyCodes\\workspace(Java)\\javaCallCplex\\cases\\aflow40b(MBP).mps");
			//cplex.setParam(IloCplex.Param.MIP.Tolerances.MIPGap,0.1);//����Ŀ��GAPֵ
			//cplex.setParam(IloCplex.Param.TimeLimit,15);//�����������ʱ��
			//��ȡprm�ļ������������ļ���
			cplex.readParam("D:\\MyCodes\\workspace(Java)\\javaCallCplex\\cases\\config_Cplex.prm");
			cplex.solve();
			//cplex.exportModel("t1.lp");
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}
}
