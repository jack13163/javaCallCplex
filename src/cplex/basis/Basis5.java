package cplex.basis;
/** 
  * @author:Dylan 
  * @date:2017年10月2日 下午3:00:21 
  */

import ilog.concert.*;
import ilog.cplex.*;

public class Basis5 {
    // 读取mps文件进行求解，并调整参数
	public static void main(String[] args) {
           try {
			IloCplex cplex = new IloCplex();
			cplex.importModel("D:\\MyCodes\\workspace(Java)\\javaCallCplex\\cases\\aflow40b(MBP).mps");
			//cplex.setParam(IloCplex.Param.MIP.Tolerances.MIPGap,0.1);//设置目标GAP值
			//cplex.setParam(IloCplex.Param.TimeLimit,15);//设置最大运行时间
			//读取prm文件（参数设置文件）
			cplex.readParam("D:\\MyCodes\\workspace(Java)\\javaCallCplex\\cases\\config_Cplex.prm");
			cplex.solve();
			//cplex.exportModel("t1.lp");
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}
}
