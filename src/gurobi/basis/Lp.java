package gurobi.basis;
/** 
  * @author:Dylan 
  * @date:2017年10月28日 下午8:49:19 
  */
import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBModel;

public class Lp {
  public static void main(String[] args) {

    if (args.length < 1) {
      System.out.println("Usage: java Lp filename");
      System.exit(1);
    }

    try {
      GRBEnv env = new GRBEnv();
      GRBModel model = new GRBModel(env, args[0]);

      model.optimize();

      int optimstatus = model.get(GRB.IntAttr.Status);

      if (optimstatus == GRB.Status.INF_OR_UNBD) {
        model.set(GRB.IntParam.Presolve, 0);
        model.optimize();
        optimstatus = model.get(GRB.IntAttr.Status);
      }

      if (optimstatus == GRB.Status.OPTIMAL) {
        double objval = model.get(GRB.DoubleAttr.ObjVal);
        System.out.println("Optimal objective: " + objval);
      } else if (optimstatus == GRB.Status.INFEASIBLE) {
        System.out.println("Model is infeasible");

        // Compute and write out IIS
        model.computeIIS();
        model.write("model.ilp");
      } else if (optimstatus == GRB.Status.UNBOUNDED) {
        System.out.println("Model is unbounded");
      } else {
        System.out.println("Optimization was stopped with status = "
                           + optimstatus);
      }

      // Dispose of model and environment
      model.dispose();
      env.dispose();

    } catch (GRBException e) {
      System.out.println("Error code: " + e.getErrorCode() + ". " +
          e.getMessage());
    }
  }
}