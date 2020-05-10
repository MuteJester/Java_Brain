package JBrain;

import JSipl.Math_Toolbox;
import JSipl.Matrix;

public class Q_Learner {
	int States,Actions;
	double Learning_Rate;
	double Discount_Rate;
	double Epsilon = 1.0;
	Matrix Q_Table;
	public Q_Learner(int Amount_Of_States,int Amount_Of_Actions,double Learning_Rate,double Discount_Rate) {
		this.Learning_Rate=Learning_Rate;
		this.Discount_Rate=Discount_Rate;
		Q_Table = new Matrix(Amount_Of_States,Amount_Of_Actions);
		for(int i=0;i<Amount_Of_States;i++) {
			for(int j=0;j<Amount_Of_Actions;j++) {
				Q_Table.Matrix_Body[i][j] = Math_Toolbox.random_double_in_range(-2, 1);
			}
		}
		this.Actions=Amount_Of_Actions;
		this.States=Amount_Of_States;
		
	}
	public int Get_Action(int State) {
		double max = Double.MIN_VALUE;
		int Greedy_pos =0,Random_Pos=Math_Toolbox.random_int_in_range(0, Actions);
		for(int i=0;i<Q_Table.Cols;i++) {
			if(Q_Table.Matrix_Body[State][i] >= max) {
				max = Q_Table.Matrix_Body[State][i];
				Greedy_pos =i;
			}
		}
		 
		if(Math_Toolbox.random_double_in_range(0, 1.0) < this.Epsilon) {
			return Random_Pos;
		}else {
			return Greedy_pos;
		}
		
	}

	public void Train(int state,int action ,int next_state,double reward,boolean done) {
		
		double[] next_states = this.Q_Table.Matrix_Body[ next_state];
		
		if(done == true) {
			for(int i=0;i<next_states.length;i++) {
				next_states[i] =0;
			}
		}
		double max = Double.MIN_VALUE;
		for(int i=0;i<Q_Table.Cols;i++) {
			if(next_states[i] >= max) {
				max = next_states[i];
			}
		}
		double target_q = reward + this.Discount_Rate*max;
		double update_q = target_q - this.Q_Table.Matrix_Body[state][action];
		 this.Q_Table.Matrix_Body[state][action] += this.Learning_Rate*update_q;
		
		
		if(done == true) {
			this.Epsilon*=0.99;
		}
	}

}
