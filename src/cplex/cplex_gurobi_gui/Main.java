package cplex.cplex_gurobi_gui;

import java.awt.EventQueue;
import javax.swing.JDialog;
import javax.swing.JFrame;

/** 
  * @author:Dylan 
  * @date:2017年11月29日 上午10:56:52 
  */
public class Main {
	// Launch the application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JDialog.setDefaultLookAndFeelDecorated(true);
					JFrame.setDefaultLookAndFeelDecorated(true);
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
