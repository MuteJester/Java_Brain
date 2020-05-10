package JBrain;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import JSipl.*;
import javax.swing.JButton;
import javax.swing.JFrame;








public class Java_Brain {
	public static Math_Toolbox tlb = new Math_Toolbox();
	public static Color_Palette CSET = new Color_Palette();
	SPlot plot = new SPlot();
	public CSV_File Data;
	public Java_Brain() {
		Data = new CSV_File();
	}
	
	//Data Set Handling

	/**
	 * The following method prints to the ide console all data in csv
	 * keep in mid that large csv file may not be as informative when
	 * printed to console
	 * 
	 */
	public void Print_CSV_Data() {
		
		Iterator<String> itr;
		for(int i=0;i<this.Data.Number_Of_Columns;i++) {
			System.out.print(this.Data.Get(i+1).Column_Name+"     ");
		}
		System.out.println();
		for(int i = 0 ; i <this.Data.Number_Of_Rows;i++) {
			for(int j =0;j<this.Data.Number_Of_Columns;j++) {
				System.out.print(this.Data.Get(i+1, j+1) + "    ");
			}
			System.out.println("");
		}
	}

	/**
	 * The method will prompt a GUI window that will allow the user to paint points on a 
	 * 2D Cartesian Coordinate System and convert the painted points into a Java_Brain data set instance
	 * @param
	 * Max_X_Value : The maximum X value of the Cartesian Coordinate System (also the highest value that can be painted)
	 * @param
	 * Max_Y_Value : The maximum Y value of the Cartesian Coordinate System (also the highest value that can be painted)
	 * @return
	 * A Java_Brain instances containing the painted data points
	 * */
	public static Java_Brain Paint_Data(double Max_X_Value,double Max_Y_Value) {
		Java_Brain result = new Java_Brain();
		SPlot plot = new SPlot();
		Image DrawCanvas = plot.Get_Scatter_Plot("X", "Y",Max_X_Value , Max_Y_Value);
		class mouse_l implements MouseListener{
			public  int last_x_click;
			public 	int last_y_click;
			boolean click =false;
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				last_x_click = e.getX();
				last_y_click = e.getY();
				click = true;

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		}
		class AL implements ActionListener{
			boolean state =false;

			@Override
			public void actionPerformed(ActionEvent e) {
				state = true;				
			}
		}
		AL al = new AL();
		mouse_l ml = new mouse_l();
		SIPL_Window rend = new SIPL_Window(DrawCanvas.IMG);
		rend.addMouseListener(ml);
		JButton finish = new JButton("Render Points");
		finish.setBounds(DrawCanvas.IMG.getWidth()+10, 50, 140, 30);
		rend.add(finish);
		rend.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		finish.addActionListener(al);
		ArrayList<String> X_Vals = new ArrayList<String>();	
		ArrayList<String> Y_Vals = new ArrayList<String>();	
		double tx,ty;
		while(al.state==false) {
			if(ml.click==true) {
				
				tx = tlb.Remap((float)ml.last_x_click-6, (float)105, (float)720, 0, (float)Max_X_Value );
				ty = tlb.Remap((float)ml.last_y_click-35, (float)565, (float)80, 0, (float)Max_Y_Value);
				
				DrawCanvas.Draw_Circle((int)ml.last_x_click-6, (int)ml.last_y_click-35, 3, CSET.Royal_Blue,"Fill");
				DrawCanvas.Draw_Circle((int)ml.last_x_click-6, (int)ml.last_y_click-35, 4, CSET.White_Smoke);
				ml.click=false;
				//DrawCanvas.Commint_Matrix_Changes();
				X_Vals.add(String.format("%f", tx));
				Y_Vals.add(String.format("%f", ty));


			}
			
			rend.Refresh_Frame(DrawCanvas.IMG,DrawCanvas.IMG.getWidth()+170,DrawCanvas.IMG.getHeight());

		}
		rend.dispose();
		result.Data.Add_Column("X", X_Vals);
		result.Data.Add_Column("Y", Y_Vals);
		return result;
	}
	
	/**
	 * The method computes the Pearson Correlation Coefficient for the given parameter ArrayLists
	 * @param
	 * Y : An ArrayList of strings containing the original values (usually the real results from your data set)
	 * @param
	 * Y_Hat : An ArrayList of String containing the modified values (usually this will be the guess your model made for the Y arraylist) 
	 * @return
	 * The Pearson Correlation Coefficient
	 * */ 
	public static double Get_Pearson_Correlation_Coefficient(ArrayList<String> Y, ArrayList<String> Y_Hat) {
		double r =0;
		double sigma_xy=0,sigma_x=0,sigma_y=0,sigma_xs=0,sigma_ys=0;
		ArrayList<Double>y,y_h;
		y = CSV_File.StringList_To_DoubleList(Y);
		y_h = CSV_File.StringList_To_DoubleList(Y_Hat);
		for(int i =0;i<Y.size();i++) {
			sigma_xy += y.get(i)*y_h.get(i);
			sigma_x += y.get(i);
			sigma_y +=y_h.get(i);
			sigma_xs += y.get(i)*y.get(i);
			sigma_ys +=y_h.get(i)*y_h.get(i);
		}
		r=y.size()*sigma_xy - sigma_x*sigma_y;
		r /= Math.sqrt(  ((Y.size()*sigma_xs - sigma_x*sigma_x)) * ((Y.size()*sigma_ys - sigma_y*sigma_y)) );
		return r;
	}
	/**
	 * The method computes the Spearmans Correlation Coefficient for the given parameter ArrayLists
	 * @param
	 * Y : An ArrayList of strings containing the original values (usually the real results from your data set)
	 * @param
	 * Y_Hat : An ArrayList of String containing the modified values (usually this will be the guess your model made for the Y arraylist) 
	 * @return
	 * The Spearmans Correlation Coefficient
	 * */ 
	public static double Get_Spearman_Correlation_Coefficient(ArrayList<String> Y, ArrayList<String> Y_Hat) {
		double SCC = 0;
    	double[] rankY=Java_Brain.Rankify(Y);
		double[] rankYhat=Java_Brain.Rankify(Y_Hat);
        double sum_X = 0, sum_Y = 0,sum_XY = 0; 
        double squareSum_X = 0, squareSum_Y = 0; 
        for (int i = 0; i < rankY.length; i++) 
        { 
            sum_X += + rankY[i]; 
            sum_Y += rankYhat[i]; 
            sum_XY += rankY[i] * rankYhat[i]; 
            squareSum_X += rankY[i] * rankY[i]; 
            squareSum_Y += rankYhat[i] * rankYhat[i]; 
        } 
      
    	SCC  =(rankY.length * sum_XY -  sum_X * sum_Y) /  Math.sqrt((rankY.length * squareSum_X - sum_X * sum_X) *  (rankY.length * squareSum_Y - sum_Y * sum_Y)); 
    	
    	return SCC;
	}
	/**
	 * The method take an ArrayList of strings (or just a column from your data set) and calculates the rank
	 * array for the column / ArrayList of string (where each string is a numeric value)
	 * @param
	 * Y : An ArrayList of strings (where the strings are numeric values) to be rankified
	 * @return
	 * A Double array of rank ( each i index of the array corresponds to the i'th index of the ArrayList )
	 * */
	public	static double[] Rankify(ArrayList<String> Y) throws NumberFormatException{
		
  	  	try {
    		double Rank_Y[] = new double[Y.size()]; 
    	      
    	    for(int i = 0; i < Rank_Y.length; i++)  
    	    { 
    	        int r = 1, s = 1; 
    	        for(int j = 0; j < i; j++) { 
    	            if (Double.parseDouble(Y.get(j)) < Double.parseDouble(Y.get(i)) ) r++; 
    	            if (Double.parseDouble(Y.get(j)) == Double.parseDouble(Y.get(i)) ) s++; 
    	        } 
    	        for (int j = i+1; j < Rank_Y.length; j++) { 
    	            if (Double.parseDouble(Y.get(j)) < Double.parseDouble(Y.get(i))) r++; 
    	            if (Double.parseDouble(Y.get(j)) == Double.parseDouble(Y.get(i))) s++; 
    	        }
    	        Rank_Y[i] = r + (s-1) * 0.5;         
    	    } 
    	    return Rank_Y; 
    	  	}
    	  	catch(NumberFormatException e) {
    	  		System.out.println("There Are Missing Values In One Of The Columns\nPlease Take Care Of Missing Values");
    	  		return null;
    	  	}
	}
	
	/**
	 * The method will compute the correlations between all numeric columns in the loaded data set and 
	 * output a new Java_Brain instance containing 2 columns ,the first columns is the name of the pair 
	 * and the second column will contain the correlation between the pair
	 * @param
	 * Correlation_Type : The type of correlation formula to implement ,the options are - 1)"Spearman" ,
	 * 2)"Pearson"
	 * @return
	 * A Java_Brain instance containing pair of numeric columns and there correlation
	 * */
	public Java_Brain Compute_Column_Correlations(String Correlation_Type) {
		ArrayList<Integer> numeric = new ArrayList<Integer>();
		for(int i =1;i<=this.Data.Number_Of_Columns;i++) {
			if(this.Data.Get(i).Column_Type == 0) {
				numeric.add(i);
			}
		}
		if(numeric.size() < 2) {
			System.out.println("You Need Minimum 2 Numeric Columns To Find Correlations");
			return null;
		}
		Java_Brain Cors = new Java_Brain();
		
		int number_of_correlations = Math.round(((numeric.size())*(numeric.size()-1))/2);
		Cors.Data.Add_Column("Between");
		Cors.Data.Add_Column("Correlation");

		//Cors.Column_Names.add(Between);
		//Cors.Column_Names.add("Correlation");

		for(int i =0;i<number_of_correlations;i++) {
			Cors.Data.Add_Row();
		}
	

		
		int pos =1;
		for(int i=0;i<numeric.size();i++) {
			for(int j=i+1;j<numeric.size();j++) {
				Cors.Data.Set(pos, 1, this.Data.Get(numeric.get(i)).Column_Name + " - " + this.Data.Get(numeric.get(j)).Column_Name);
				if(Correlation_Type.equals("Pearson")) { 
				Cors.Data.Set(pos, 2, String.format("%.5f",
						Java_Brain.Get_Pearson_Correlation_Coefficient(
								this.Data.Get(numeric.get(i)).Values, this.Data.Get(numeric.get(j)).Values)));
				}
				else if(Correlation_Type.equals("Spearman")) {
					Cors.Data.Set(pos, 2, String.format("%.5f",
							Java_Brain.Get_Spearman_Correlation_Coefficient(
									this.Data.Get(numeric.get(i)).Values, this.Data.Get(numeric.get(j)).Values)));
					
					
				}else {
					System.out.println("Unknown Correlation Type Please Specify - Pearson/Spearman");
					return null;
				}
				pos++;
			}
		}
		
		
		return Cors;
	}
	/**
	 * The method will compute and return the square distance between two Point instances
	 * @param
	 * first : the first Point instance 
	 * @param
	 * second : the second point instance
	 * @return
	 * The square distance between both points 
	 * */
	
	//Algorithms
	public double Squared_Point_Distance(Point first, Point second) {
		return (first.x - second.x)*(first.x - second.x) + (first.y - second.y)*(first.y - second.y) + (first.z - second.z)*(first.z - second.z);
	}
	/**
	 * The method will perform  2 variable linear regression on 2 selected numeric columns using
	 * a constant statistical formula  (without gradient descent) this method is much faster then using
	 * the method with gradient descent but may be less accurate
	 * @param
	 * X_Values_Column_Number : A number corresponding to a column number in the loaded data set. this is the first variable for your
	 * linear regression
	 * @param
	 * Y_Values_Column_Number : A number corresponding to a column number in the loaded data set. this is the second variable for your
	 * linear regression
	 * @return
	 * A Matrix instance which will contain the linear equation ( y = aX+b ) parameters calculated by the linear regression formula
	 * The Matrix instance will contain the result as following - Matrix_Body[0][0] will be equal to the constant b
	 * and Matrix_Body[1][0] will be equal to the (a)X value. 
	 * */
	public Matrix Linear_Regression_Static_Formula(int X_Values_Column_Number ,int Y_Values_Column_Number) {
		ArrayList<String> X_Values = this.Data.Get(X_Values_Column_Number).Values;
		ArrayList<String> Y_Values = this.Data.Get(Y_Values_Column_Number).Values;

		if(X_Values.size() != Y_Values.size()) {
			System.out.println("Error Both Value Arrays Must Have The Same Amount Of Elements");
			return null;
		}
		double sum_xy=0,sum_xsquared=0,sum_ysquared=0,sum_x=0,sum_y=0;
		for(int i = 0 ; i<X_Values.size();i++) {
			double tx=Double.parseDouble(X_Values.get(i)),ty=Double.parseDouble(Y_Values.get(i));
			sum_x += tx;
			sum_y += ty;
			sum_xy += ty*tx;
			sum_xsquared += tx*tx;
			sum_ysquared += ty*ty;
		}

		Matrix res = new Matrix(2,1);
		res.Matrix_Body[0][0] = (sum_y*sum_xsquared - sum_x*sum_xy)/(X_Values.size()*sum_xsquared - sum_x*sum_x);
		res.Matrix_Body[1][0] = (X_Values.size() * sum_xy - sum_x*sum_y)/(X_Values.size()*sum_xsquared - sum_x*sum_x);
		return res;
		
	}
	/**
	 * The method will perform the K Means calculation on the ArrayList of Points instances passed to the method
	 * @param
	 * data : An ArrayList of Points instances ( each Point instance contains and X,Y,Z value ).
	 * @param
	 * k : the amount of means the algorithm should calculate 
	 * @param
	 * number_of_iterations : the amount of learning iterations the algorithm will run
	 * @return
	 * An ArrayList of Point instances where each point in the returned ArrayList is a computed mean.
	 * */
	public ArrayList<Point> K_Means(ArrayList<Point> data, int k, int number_of_iterations) {
		Random random_machine = new Random();
		ArrayList<Point> means = new ArrayList<Point>(k);
		for(int i = 0 ; i<k;i++) {
			means.add(new Point());
		}
		
		
		for(int cluster =0 ;cluster < means.size();cluster++) {
			means.set(cluster, data.get(random_machine.nextInt(data.size())));
		}

		ArrayList<Integer> assignments = new ArrayList<Integer>(data.size());
		for(int i=0;i<data.size();i++) {
			assignments.add(0);
		}

		for (int iteration = 0; iteration < number_of_iterations; ++iteration) {
			// Find assignments.
			for (int point = 0; point < data.size(); ++point) {
				double best_distance = Double.MAX_VALUE;
				int best_cluster = 0;
				for (int cluster = 0; cluster < k; ++cluster) {
					double distance = Math.sqrt(Squared_Point_Distance(data.get(point), means.get(cluster)));
					if (distance < best_distance) {
						best_distance = distance;
						best_cluster = cluster;
					}
				}
				assignments.set(point, best_cluster);

			}

			// Sum up and count points for each cluster.
			ArrayList<Point> new_means = new ArrayList<Point>(k);
			for(int i = 0 ; i <k ; i++) {
				new_means.add(new Point());
			}
			ArrayList<Integer> counts = new ArrayList<>(k);
			for(int i=0;i<k;i++) {
				counts.add(0);
			}
		

			for (int point = 0; point < data.size(); ++point) {
				int cluster = assignments.get(point);
				double x,y,z;
				Point temp = new Point();
				x = new_means.get(cluster).x + data.get(point).x;
				y = new_means.get(cluster).y + data.get(point).y;
				z = new_means.get(cluster).z + data.get(point).z;
				temp.x=x;
				temp.y=y;
				temp.z=z;
				//System.out.println("Iter: "+iteration  + " temp: " + temp );

				new_means.set(cluster, temp);
				int c = counts.get(cluster);
				c++;
				counts.set(cluster, c);
			}

			// Divide sums by counts to get new centroids.
			for (int cluster = 0; cluster < k; ++cluster) {
				// Turn 0/0 into 0/1 to avoid zero division.
				int count = Math.max(1, counts.get(cluster));
				Point temp = new Point();
				temp.x = new_means.get(cluster).x / count;
				temp.y = new_means.get(cluster).y / count;
				temp.z = new_means.get(cluster).z / count;
				//System.out.println(temp);
				means.set(cluster, temp);
			
			}
		}

		return means;

		
		
		
	}
	private Matrix Step_Gradient(double Current_B,double Current_M,double Learning_Rate ,int X_COL,int Y_COL) {
		ArrayList<String> X_val = this.Data.Get(X_COL).Values;
		ArrayList<String> Y_val = this.Data.Get(Y_COL).Values;

		double b_gradient = 0;
		double m_gradient = 0;
		double x , y;
		for(int i =0;i<this.Data.Number_Of_Rows;i++) {
			x = Double.parseDouble(X_val.get(i));
			y = Double.parseDouble(Y_val.get(i));
			m_gradient += ((double)2/Data.Number_Of_Rows)*-x*(y - (Current_M * x + Current_B));
			b_gradient += ((double)2/Data.Number_Of_Rows)*-(y - (Current_M * x + Current_B));
		}
		Matrix result = new Matrix(2,1);
		result.Matrix_Body[0][0] = Current_B - (Learning_Rate * b_gradient);
		result.Matrix_Body[1][0] =Current_M - (Learning_Rate * m_gradient);
		return result;
		
	}
	/**
	 * The method computes the Mean Squared Error for the given parameter ArrayLists
	 * @param
	 * Y : An ArrayList of strings containing the original values (usually the real results from your data set)
	 * @param
	 * Y_Hat : An ArrayList of String containing the modified values (usually this will be the guess your model made for the Y arraylist) 
	 * @return
	 * The Mean Squared Error
	 * */
	public double Get_MSE(ArrayList<String> Y ,ArrayList<String> Y_Hat) {
		if(Y.size()!= Y_Hat.size()) {
			System.out.println("Test Groups Have To Be The Same Size");
			return -99;
		}
		double es=0;
		for(int i=0;i<Y.size();i++) {
			es+=Math.pow(Double.parseDouble(Y.get(i))-Double.parseDouble(Y_Hat.get(i)),2);
		}
		es/=Y.size();
		return es;
	}
	/**
	 * The method computes the Mean Absolute Error for the given parameter ArrayLists
	 * @param
	 * Y : An ArrayList of strings containing the original values (usually the real results from your data set)
	 * @param
	 * Y_Hat : An ArrayList of String containing the modified values (usually this will be the guess your model made for the Y arraylist) 
	 * @return
	 * The Mean Absolute Error
	 * */
	public double Get_MAE(ArrayList<String> Y ,ArrayList<String> Y_Hat) {
		if(Y.size()!= Y_Hat.size()) {
			System.out.println("Test Groups Have To Be The Same Size");
			return -99;
		}
		double es=0;
		for(int i=0;i<Y.size();i++) {
			es+=Math.abs(Double.parseDouble(Y.get(i))-Double.parseDouble(Y_Hat.get(i)));
		}
		es/=Y.size();
		return es;
	}
	/**
	 * The method computes the R^2 for the given parameter ArrayLists
	 * @param
	 * Y : An ArrayList of strings containing the original values (usually the real results from your data set)
	 * @param
	 * Y_Hat : An ArrayList of String containing the modified values (usually this will be the guess your model made for the Y arraylist) 
	 * @return
	 *  R^2 correlation between given ArrayLists
	 * */ 
    public double Get_R_Squared(ArrayList<String> Y ,ArrayList<String> Y_Hat) {
		double R_S = this.Get_Pearson_Correlation_Coefficient(Y, Y_Hat);
		R_S*=R_S;
		return R_S;
		
	}
    /**
	 * The method computes the Adjusted R^2 for the given parameter ArrayLists
	 * @param
	 * Y : An ArrayList of strings containing the original values (usually the real results from your data set)
	 * @param
	 * Y_Hat : An ArrayList of String containing the modified values (usually this will be the guess your model made for the Y arraylist) 
	 * @param
	 * Indpendent_Variables : The Number of independent variable or in some text books written as number of predictors
	 * @return
	 *  Adjusted R^2 correlation between given ArrayLists
	 * */ 
    public double Get_Adjusted_R_Squared(ArrayList<String> Y ,ArrayList<String> Y_Hat,int Indpendent_Variables) {
		double R_S = this.Get_R_Squared(Y, Y_Hat);
		double ARS=(1.0-R_S)*(this.Data.Number_Of_Rows-1);
		ARS/=this.Data.Number_Of_Rows-1-Indpendent_Variables;
		ARS=1.0-ARS;
		return ARS;
		
	}
	/**
	 * The method will compute the Confusion Matrix for the given variable columns ,the weights for each column and the 
	 * result column using a decision boundary.
	 * @param
	 * Regression_Weights : A Matrix instance which contains the weights corresponding to each variable column (usually this type of matrix 
	 * if not constructed in a custom way , is a matrix returned by the Logistic Regression method. 
	 * @param
	 * Binary_Column : the column number in the loaded data set which contains binary result (1 or 0 ) against which the the 
	 * guessed values (using the weights and the decision boundary) will be compared 
	 * @param 
	 * Sampled_Rows : An array of integer where each integer is a a column number where each column is a variable meaning the i'th
	 * column corresponds to the i'th weight in the weight matrix .
	 * @param
	 * Decision_Boundary : a value between 0.0 and 1.0 this threshold is the boundary for the guess to count as correct or incorrect
	 * meaning if the decision boundary is 0.75 , all guess below 0.75 count as 0 and all guess above 0.75 count as 1.
	 * @return
	 * A 2x2 Matrix that is formated in the following order - Matrix_Body[0][0] = True Positive ,Matrix_Body[1][0] = False Positive,
	 * Matrix_Body[0][1] = False Negative , Matrix_Body[1][1] = True Negative
	 * */
    public Matrix Confusion_Matrix(Matrix Regression_Weights,int Binary_Column,int[] Sampled_Rows,double Decision_Boundary ) {		
		Matrix CM = new Matrix(2,2);
		double TruePositive=0;
		double TrueNegative=0;
		double FalsePositive=0;
		double FalseNegative=0;
		for(int i =1;i<=this.Data.Number_Of_Rows;i++) {
			double pred_s = Regression_Weights.Matrix_Body[0][0];
			for(int j=1;j<=Sampled_Rows.length;j++) {
				pred_s += Regression_Weights.Matrix_Body[j][0] *Double.parseDouble(this.Data.Get(i,Sampled_Rows[j-1]));
			}
			pred_s = Neural_Net.Sigmoid(pred_s);
			double actual = Double.parseDouble(this.Data.Get(i, Binary_Column));
			
			if(pred_s > Decision_Boundary) {
				pred_s=1;
			}else {
				pred_s=0;
			}
			
			
			if(pred_s == 0 && actual == 0) {
				TrueNegative++;
			}
			else if(pred_s == 1 && actual == 0) {
				FalsePositive++;
			}
			else if(pred_s == 1 && actual == 1) {
				TruePositive++;
			}else if(pred_s == 0 && actual == 1){
				FalseNegative++;
			}
			
			
		}
		
		CM.Matrix_Body[0][0] = TruePositive;
		CM.Matrix_Body[1][0] = FalsePositive;
		CM.Matrix_Body[0][1] =FalseNegative;
		CM.Matrix_Body[1][1] =TrueNegative; 
		return CM;
	}
	/**
	 * The method take a Matrix instance containing the values of the Confusion Matrix and prints to console
	 * the following measurements - [ Accuracy , Misclassification Rate,Sensitivity,Precision,Recall ]
	 * @param
	 * Confusion_Matrix : A Matrix instance of a Confusion Matrix imported or a one generated by the Confusion_Matrix method
	 * @param
	 * Number_Sampled : the number of samples tested when creating the confusion matrix (usually the number of rows in the variable columns 
	 * upon the confusion matrix creation)
	 * */
    public void Print_Confusion_Matrix_List_Of_Rates(Matrix Confusion_Matrix,int Number_Sampled) {
		double TP = Confusion_Matrix.Matrix_Body[0][0];
		double FP = Confusion_Matrix.Matrix_Body[1][0];
		double TN = Confusion_Matrix.Matrix_Body[1][1];
		double FN = Confusion_Matrix.Matrix_Body[0][1];
		System.out.println("Accuracy: [ " +((TP+TN)/Number_Sampled)+" ]");
		System.out.println("Misclassification Rate: [ " +((FP+FN)/Number_Sampled)+" ]");
		System.out.println("Sensitivity : [ " +((TP)/Number_Sampled)+" ]");
		System.out.println("Precision : [ " +((TP)/TP+FP)+" ]");
		System.out.println("Recall : [ " +((TP)/TP+FN)+" ]");
		
	}
	/**
	 * The method takes two n variable arrays and computes the Euclidean distance between both of them 
	 * keep in mind both array must have the same amount of variables 
	 * @param
	 * X1 : An array of variables 
	 * @param
	 * X2 : An array of variables 
	 * @return
	 * The Euclidean distance between the two points
	 * */
    public double Euclidean_Distance(double[] X1,double[] X2) {
		if(X1.length != X2.length) {
			System.out.println("Both Points Must Contain The Same Amount Of Values");
			return Double.POSITIVE_INFINITY;
		}else {
			double dis =0;
			for(int i=0;i<X1.length;i++) {
				dis+=Math.pow((X2[i]-X1[i]),2);
			}
			return Math.sqrt(dis);
		}
	}
	/**
	 * The method takes two n variable arrays and computes the Manhattan distance between both of them 
	 * keep in mind both array must have the same amount of variables 
	 * @param
	 * X1 : An array of variables 
	 * @param
	 * X2 : An array of variables 
	 * @return
	 * The Manhattan distance between the two points
	 * */
	public double Manhattan_Distance(double[] X1,double[] X2) {
		if(X1.length != X2.length) {
			System.out.println("Both Points Must Contain The Same Amount Of Values");
			return Double.POSITIVE_INFINITY;
		}else {
			double dis =0;
			for(int i=0;i<X1.length;i++) {
				dis+=Math.abs(X2[i]-X1[i]);
			}
			return dis;
		}
	}
	/**
	 * The method will perform kNN on the specified values and compare the result of the KNN
	 * to the result columns finding the closest neighbour in distance and return a String 
	 * with the value of the categorical variable for the given new variables.
	 * @param
	 * K : the amount of closest neighbours to search for
	 * @param
	 * Test_Values : an array of doubles ,each index of the array corresponds to a row in the loaded data set
	 * and the sample_columns parameter meaning - the i'th value in the Test_Values parameter will be
	 * Perceived as a value of the i'th column in the Sample_Columns array 
	 * @param
	 * Sample_Columns : an array of integers each integer in this array represents a column
	 * in order to match the Test_Values parameter to the correct desired testing columns
	 * @param
	 * Result_Column : the number of the Result column , in KNN it usually is a categorical column
	 * @return
	 * A String containing the category that is the closest match to the given Test_Values
	 *  
	 * */
	public String KNN(int K ,double Test_Values[],int Sample_Columns[],int Result_Column) {
		double distances[] = new double[this.Data.Number_Of_Rows];
		double x[];
		
		for(int i=1;i<=this.Data.Number_Of_Rows;i++) {
			x = new double[Sample_Columns.length];
			for(int j=0;j<Sample_Columns.length;j++) {
				x[j] = Double.parseDouble(this.Data.Get(i, Sample_Columns[j]));
			}
			distances[i-1] = this.Euclidean_Distance(x, Test_Values);
		}
		 int[] knn = new int[K];
		 double min =Double.MAX_VALUE;
		 for(int i=0;i<K;i++) {
			 for(int j=0;j<distances.length;j++) {
				 if(distances[j] < min) {
					 min = distances[j];
					 knn[i] = j+1;
				 }
			 }
			 distances[knn[i]-1] = Double.POSITIVE_INFINITY;
			  min =Double.MAX_VALUE;
		 }
		 
		String[] categories = this.Data.Get(Result_Column-1).Get_Categories().toArray(new String[0]);
		 int cats[] = new int[categories.length];
		 Arrays.fill(cats, 0);
		 for(int i=0;i<K;i++) {
			 String val = this.Data.Get(knn[i], Result_Column);
			 for(int j=0;j<categories.length;j++) {
				 if(val.equals(categories[j])) {
					 cats[j]++;
					 break;
				 }
			 }
		 }
		 
		 Arrays.sort(cats);
		 return categories[cats[cats.length-1]- 1];
	}
	/**
	 * The method will perform kNN on the specified values and compare the result of the KNN
	 * to the result columns finding the closest neighbour in distance and return a Matrix 
	 * with the distances of the k neighbours and the rows that correspond to those neighbours
	 * @param
	 * K : the amount of closest neighbours to search for
	 * @param
	 * Test_Values : an array of doubles ,each index of the array corresponds to a row in the loaded data set
	 * and the sample_columns parameter meaning - the i'th value in the Test_Values parameter will be
	 * Perceived as a value of the i'th column in the Sample_Columns array 
	 * @param
	 * Sample_Columns : an array of integers each integer in this array represents a column
	 * in order to match the Test_Values parameter to the correct desired testing columns
	 * 
	 * 
	 * @return
	 * A Matrix 
	 * with the distances of the k neighbours and the rows that correspond to those neighbours
	 * */
	public Matrix KNN(int K ,double Test_Values[],int Sample_Columns[]) {
		double distances[] = new double[this.Data.Number_Of_Rows];
		double x[];
		 Matrix result = new Matrix(K,2);

		for(int i=1;i<=this.Data.Number_Of_Rows;i++) {
			x = new double[Sample_Columns.length];
			for(int j=0;j<Sample_Columns.length;j++) {
				x[j] = Double.parseDouble(this.Data.Get(i, Sample_Columns[j]));
			}
			distances[i-1] = this.Euclidean_Distance(x, Test_Values);
		}
		 int[] knn = new int[K];
		 double min =Double.MAX_VALUE;
		 for(int i=0;i<K;i++) {
			 for(int j=0;j<distances.length;j++) {
				 if(distances[j] < min) {
					 min = distances[j];
					 knn[i] = j+1;
				 }
			 }
			 result.Matrix_Body[i][1] = min;
			 distances[knn[i]-1] = Double.POSITIVE_INFINITY;
			  min =Double.MAX_VALUE;
		 }
		 
		 for(int i=0;i<K;i++) {
			 result.Matrix_Body[i][0] = knn[i];

		 }
		return result;
	}
	/**
	 * The method will perform kNN on the specified values in the argument Java_Brain instance and find  KNN
	 * to the calling Java_Brain instance finding the closest neighbours in distance and returning a Matrix 
	 * which will represent the distances of each row in the Test_Dataset to all the row in the calling instances data set
	 * with the distances of the k neighbours and the rows that correspond to those neighbours
	 * @param
	 * K : the amount of closest neighbours to search for
	 * @param
	 * Test_Dataset : an array of doubles ,each index of the array corresponds to a row in the loaded data set
	 * and the sample_columns parameter meaning - the i'th value in the Test_Values parameter will be
	 * Perceived as a value of the i'th column in the Sample_Columns array 
	 * @param
	 * Sample_Columns : an array of integers each integer in this array represents a column
	 * in order to match the Test_Dataset parameter to the correct desired testing columns
	 * 
	 * 
	 * @return
	 * A Matrix 
	 * with the distances of the k neighbours of each row in the Test_Dataset and the rows of the calling instance that correspond to those neighbours
	 * */
	public Matrix KNN(int K ,Java_Brain Test_Dataset,int Sample_Columns[]) {
		Matrix Final_Res = new Matrix(Test_Dataset.Data.Number_Of_Rows,(1+K));
		double xv[] = new double[Sample_Columns.length];
		for(int i=1;i<=Test_Dataset.Data.Number_Of_Rows;i++) {
			for(int j=0;j<xv.length;j++) {
				xv[j] = Double.parseDouble(Test_Dataset.Data.Get(i, Sample_Columns[j]));
			}
			Matrix res = this.KNN(K, xv, Sample_Columns);
			for(int j = 1 ; j <K+1;j++) {
				Final_Res.Matrix_Body[i-1][j] = res.Matrix_Body[j-1][0];	
			}

			Final_Res.Matrix_Body[i-1][0] = i;	

		}
		
		return Final_Res;
	}
	/**
	 * The method will compute and return a Matrix instance containing the Variance-Covariance matrix of the
	 * specified columns
	 * @param
	 * input_Columns - the columns which the method should calculate the matrix for
	 * @return
	 * A matrix instance containing the ariance-Covariance matrix
	 * */
	public Matrix Get_Variance_Covariance_Matirx(int[] input_Columns) {
    	
    	Matrix data = new Matrix(this.Data.Number_Of_Rows,input_Columns.length);
    	Matrix dataOnes = new Matrix(this.Data.Number_Of_Rows,1);
    	for(int i=0;i<this.Data.Number_Of_Rows;i++) {
    			dataOnes.Matrix_Body[i][0]=1;
    		
    	}
    	Matrix dataOnestag = new Matrix(dataOnes);
    	dataOnestag.Matrix_Transpose();
    	for(int i=1;i<=this.Data.Number_Of_Rows;i++) {
    		for(int j=0;j<input_Columns.length;j++) {
    			data.Matrix_Body[i-1][j] = Double.parseDouble(this.Data.Get(i, input_Columns[j]));
    		}
    	}
    	//transformation a = A - 11'x/n
    	dataOnestag = dataOnes.Dot_Product(dataOnestag);
    	dataOnestag = dataOnestag.Dot_Product(data);
    	dataOnestag.Divide(this.Data.Number_Of_Rows);
    	data.Subtract(dataOnestag);
    	dataOnes = new Matrix(data);
    	dataOnes.Matrix_Transpose();
    	//(a*a')/n 
    	data = dataOnes.Dot_Product(data);
    	data.Divide(Data.Number_Of_Rows);
    	
    	return data;
    	
    }
	private Matrix Step_Gradient(Matrix Current_Weights,double Learning_Rate ,int[] Columns_Of_Sampels,ArrayList<String> True_Y) {
		int nof = Columns_Of_Sampels.length+1;
		Matrix Gradients = new Matrix(nof,1);
		Matrix Teta = new Matrix(Current_Weights);
		Matrix Values = new Matrix(nof,1);
		Matrix Prediction = new Matrix(Current_Weights);
		Matrix h0 = null;
		Teta.Matrix_Transpose();

		for(int i=1;i<=this.Data.Number_Of_Rows;i++) {
			//h0  guess
			Values.Matrix_Body[0][0]=1;
			for(int j =1;j<nof;j++) {
				Values.Matrix_Body[j][0] = Double.parseDouble(this.Data.Get(i, Columns_Of_Sampels[j-1]));
			}			
			h0=Teta.Dot_Product(Values);
						
			for(int j =0;j<nof;j++) {
				Gradients.Matrix_Body[j][0] += (h0.Matrix_Body[0][0] - Double.parseDouble(True_Y.get(i-1)))*Values.Matrix_Body[j][0];
			}

		}

		for(int j =0;j<nof;j++) {
			Gradients.Matrix_Body[j][0]*=(Learning_Rate/this.Data.Number_Of_Rows);
			
			Prediction.Matrix_Body[j][0] = Prediction.Matrix_Body[j][0] - Gradients.Matrix_Body[j][0];
		}
		

		
		
		
		return Prediction;
		
	}
	/**
	 * The method performs multivariable linear regression with gradient descent on the selected columns and outputs a matrix if weights 
	 * @param
	 * Sample_Columns : an array of integer where each integer is a column number , each column is a variable for which a weight needs
	 * to be.
	 * @param
	 * True_Y : the actual value of the weight in order to increase the accuracy of the prediction with each epoch
	 * @param
	 * Leaning_Rate : the learning rate constant (it is recommended to keep the value low usually between 0.001 - 0.0001)
	 * @param
	 * number_of_iterations : the number of iterations or Epochs the algorithm will perform
	 * @return
	 * A Matrix instance of weight the matrix will be of n x 2 dimension where [0][0] is the constant and [1][0] - [n-1][0] are the
	 * weight corresponding to the columns specified in the Sample_Columns array 
	 * */
	public Matrix Linear_Regression_Gradient_Descent(int[] Sample_Columns,ArrayList<String> True_Y,double Leaning_Rate,int number_of_iterations) {
		//y = mx + b - for slope calculation
		Matrix LE = new Matrix(Sample_Columns.length+1,1);
		for(int i = 0;i<number_of_iterations;i++) {
			LE = this.Step_Gradient(LE , Leaning_Rate, Sample_Columns, True_Y);
		}
		return LE;
		
	}
	/**
	 * The method performs 2 variable linear regression with gradient descent on the selected columns and outputs a matrix if weights 
	 * (it is much more faster for computation if you only have 2 variables in compression to the multivariable method)
	 * @param
	 * X_Values_Column_Number : an integer that corresponds to a column number 
	 *  @param
	 * Y_Values_Column_Number : an integer that corresponds to a column number 
	 * @param
	 * True_Y : the actual value of the weight in order to increase the accuracy of the prediction with each epoch
	 * @param
	 * Leaning_Rate : the learning rate constant (it is recommended to keep the value low usually between 0.001 - 0.0001)
	 * @param
	 * number_of_iterations : the number of iterations or Epochs the algorithm will perform
	 * @return
	 * A Matrix instance of weight the matrix will be of 2 x 2 dimension where [0][0] is the constant and [1][0] is the
	 * weight corresponding to the columns specified in the Sample_Columns array 
	 * */
	public Matrix Linear_Regression_Gradient_Descent(int X_Values_Column_Number,int Y_Values_Column_Number,double Leaning_Rate,int number_of_iterations) {
		
		Matrix LE = new Matrix(2,1);
		for(int i = 0;i<number_of_iterations;i++) {
			LE = this.Step_Gradient(LE.Matrix_Body[0][0], LE.Matrix_Body[1][0], Leaning_Rate, X_Values_Column_Number, Y_Values_Column_Number);
		}
		//LE.z = Compute_Error_For_Given_Points(X_values,Y_values,LE.y,LE.x);

		return LE;
		
	}
	/**
	 * The method performs Logistic Regression computing the optimal weights for each variable 
	 * @param
	 * Value_Column_Numbers : an array of numeric column numbers which will represent our variables
	 * @param
	 * Binary_Category_Number : the column number of the actual result , against this column our gradient descent will compare its guesses
	 * @param
	 * number_of_iterations : the number of epochs given for the machine to train and try to learn the weights
	 * @param
	 * learning_rate : the constant regulator of our learning rate ,usually its recommended to use values [0.01 - 0.9]
	 * */
	public Matrix Logistic_Regression(int Value_Column_Numbers[],int Binary_Category_Number,int number_of_iterations,double learning_rate) {
		ArrayList<ArrayList<String>> Values = new ArrayList<ArrayList<String>>();
		for(int i=0;i<Value_Column_Numbers.length;i++) {
			Values.add(this.Data.Get(Value_Column_Numbers[i]).Values);
		}
		ArrayList<String> Binary_Category = this.Data.Get(Binary_Category_Number).Values;
		int number_of_features =Values.size()+1;
		int number_of_sampels = Values.get(0).size();
		Matrix Weights = new Matrix(number_of_features,1);
		for(int i =0;i<number_of_features;i++) {
			Weights.Matrix_Body[i][0] = 0;
		}
		ArrayList<Double> Predictions = new ArrayList<Double>();
		double Cost =0,Lowest_Cost=Double.MAX_VALUE;
		for(int i = 0;i<number_of_iterations;i++) {
			
			//predictions
			for(int j =0;j<number_of_sampels;j++) {
				
				double y = Double.parseDouble(Binary_Category.get(j));
				double s_pred=Weights.Matrix_Body[0][0];
				for(int k=1;k<number_of_features;k++) {
					double instance =Double.parseDouble(Values.get(k-1).get(j));
					
					s_pred += Weights.Matrix_Body[k][0] *instance;
					//System.out.println("k" + (k-1)+" " +instance);
				}
				double pred = Neural_Net.Sigmoid(s_pred);
				Predictions.add(pred);
				
				
				//updating weights
				Weights.Matrix_Body[0][0] =	Weights.Matrix_Body[0][0] + learning_rate*(y - pred)*pred*(1.0-pred)*1.0;
				
				for(int k=1;k<number_of_features;k++) {
					Weights.Matrix_Body[k][0] = Weights.Matrix_Body[k][0]+ learning_rate*(y - pred)*pred*(1.0-pred)*Double.parseDouble(Values.get(k-1).get(j));
				}
				
				//B0 = B0 + learning_rate*(y - pred)*pred*(1.0-pred)*1.0;
				//B1 = B1 + learning_rate*(y - pred)*pred*(1.0-pred)*Double.parseDouble(Values.get(j));
			}
			
			//cost
			
			for(int j =0;j<Predictions.size();j++) {
				double y = Double.parseDouble(Binary_Category.get(j));
				Cost += -y*Math.log(Predictions.get(j)) - (1-y)*Math.log(Predictions.get(j));
			}
			
			Cost *= 1.0/Predictions.size();
			//System.out.println("COST:" +  Cost);
			
			if(Cost<Lowest_Cost) {
				Lowest_Cost = Cost;
				//System.out.println("COST: " + Cost + " B0 = "  + B0 + " B1 = " + B1);
			}
			
			
			Predictions.clear();
			Predictions = new ArrayList<Double>();
		}
		
		

		return Weights;
		
	}
	public void Validate_Linear_Regression(Matrix LR,int True_Column,int[] Samples_Column_Numbers){
		for(int i =1;i<=this.Data.Number_Of_Rows;i++) {
			double pred = LR.Matrix_Body[0][0];
			for(int j=1;j<Samples_Column_Numbers.length+1;j++) {
				pred += LR.Matrix_Body[j][0]*Double.parseDouble(this.Data.Get(i, Samples_Column_Numbers[j-1]));
			}
			System.out.println("Predictions: " + pred + "  Actual "  + this.Data.Get(True_Column).Get(i));
		}
	}
	public void Validate_Logistic_Regression(Matrix LR_Weights,int Binary_Column,int[] Sampled_Rows) {		
		for(int i =1;i<=this.Data.Number_Of_Rows;i++) {
			double pred_s = LR_Weights.Matrix_Body[0][0];
			for(int j=1;j<=Sampled_Rows.length;j++) {
				pred_s += LR_Weights.Matrix_Body[j][0] *Double.parseDouble(this.Data.Get(i,Sampled_Rows[j-1]));
			}
			pred_s = Neural_Net.Sigmoid(pred_s);
			
			System.out.print("\n====================\n");
			System.out.print("Values: ");
			for(int z =0;z<Sampled_Rows.length;z++) {
				System.out.print(this.Data.Get(i,Sampled_Rows[z]) + " ");

			}
			System.out.print("\n====================\n");

			System.out.println("Prediction: [" + pred_s + "] Actual: [" + this.Data.Get(i, Binary_Column)+"]");
			System.out.print("\n---------------------\n");

		}

			
	}
	public Matrix PCA(int[] Selected_Columns) {
		double means[] = new double[Selected_Columns.length];
		for(int i=0;i<Selected_Columns.length;i++) {
			means[i] = this.Data.Get(Selected_Columns[i]).Column_Mean();
		}
    	Matrix data = new Matrix(this.Data.Number_Of_Rows,Selected_Columns.length);

    	for(int i=1;i<=this.Data.Number_Of_Rows;i++) {
    		for(int j=0;j<Selected_Columns.length;j++) {
    			data.Matrix_Body[i-1][j] = Double.parseDouble(this.Data.Get(i, Selected_Columns[j])) - means[j];
    		}
    	}
    	CSV_File tocsv = CSV_File.Matrix_To_CSV(data);
    	Matrix Var_CO_Var = Get_Variance_Covariance_Matirx(Selected_Columns);
    	
    	Matrix EV = Var_CO_Var.Get_Eigen_Vectors();
    	
    	data = data.Dot_Product(EV);
    	//data.print_Matrix();

    	
    	
		
		
		return data;
	}

	//Visual
	public void Plot_Linear_Regression(int X_Values_Column_Number,int Y_Values_Column_Number,String X_Name,String Y_Name) {
		Matrix LE = this.Linear_Regression_Static_Formula(X_Values_Column_Number, Y_Values_Column_Number);
		Math_Toolbox tlb = new Math_Toolbox();
		SPlot plt = new SPlot();
		ArrayList<Point> Data = CSV_File.Column_To_Point_List(this.Data.Get(X_Values_Column_Number).Values, this.Data.Get(Y_Values_Column_Number).Values);
		Image data_plot = plt.Get_Scatter_Plot(Data, X_Name, Y_Name);
		double Max_X = Double.MIN_VALUE,Max_Y = Double.MIN_VALUE;
		for(int i = 0 ;i<Data.size();i++) {
			if(Data.get(i).x > Max_X) {
				Max_X = Data.get(i).x;
			}
			if(Data.get(i).y > Max_Y) {
				Max_Y = Data.get(i).y;
			}
		}

		Max_X+=Max_X/4;
		Max_Y+=Max_Y/4;
		double tx,ty;
		double y_val;
		int draw_i = 5000;
		for(double i =0 ;i<=Max_X;i+=0.01) {
			y_val = LE.Matrix_Body[1][0] * i + LE.Matrix_Body[0][0];
			tx = tlb.Remap((float)i, 0, (float)Max_X, 105, 720);
			ty = tlb.Remap((float)y_val, (float)0 , (float)(Max_Y) ,575, 80);
			data_plot.Draw_Circle((int)tx, (int)ty, 1, CSET.Red,"Fill");
			//data_plot.Pixel_Matrix[(int)ty][(int)tx] = new Pixel(CSET.Red);

		
		}
		data_plot.Commint_Matrix_Changes();
		data_plot.Show_Image();
		
	}
    public void Plot_Scatter_Plot(int Column_X,int Column_Y,String X_Name,String Y_Name) {
    	ArrayList<Point> Data = this.Data.Column_To_Point_List(Column_X, Column_Y);
    	plot.Show_Scatter_Plot(Data, X_Name, Y_Name);
    }
    public void Plot_Gradient_Descent(int X_values_column,int Y_values_column,double Leaning_Rate,int number_of_iterations) {
		ArrayList<String>  X_values = this.Data.Get(X_values_column).Values;
		ArrayList<String>  Y_values = this.Data.Get(Y_values_column).Values;

    	ArrayList<Point> points = new ArrayList<Point>();
		for(int i = 0 ;i<Data.Number_Of_Rows;i++) {
			points.add(new Point(Double.parseDouble(X_values.get(i)),Double.parseDouble(Y_values.get(i)),0));
		}
		
    	Image plot = this.plot.Get_Scatter_Plot(points, "X", "Y");
    	
    	
		
    	SIPL_Window sw = new SIPL_Window(plot.IMG);
		sw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int z =0;
		Matrix LE = new Matrix(2,1);
		

    	Matrix convergence_test =this.Linear_Regression_Gradient_Descent(new int[] {X_values_column}, Y_values, Leaning_Rate, number_of_iterations);
		if(convergence_test.Matrix_Body[1][0] == Double.NaN || convergence_test.Matrix_Body[0][0] == Double.NaN) {
			System.out.println("Cannot Converge,Try To Adjust The Learning Rate");
			return;
		}
		double Max_X = Double.MIN_VALUE,Max_Y = Double.MIN_VALUE;
		for(int i = 0 ;i<points.size();i++) {
			if(points.get(i).x > Max_X) {
				Max_X = points.get(i).x;
			}
			if(points.get(i).y > Max_Y) {
				Max_Y = points.get(i).y;
			}
		}
		Max_X+=Max_X/4;
		Max_Y+=Max_Y/4;
		double y_val,tx,ty;
		for(int a = 0;a<number_of_iterations;a++) {
	
		plot.Update_Pixel_Matrix();
		Image nolines = new Image(plot);
		while(z<number_of_iterations) {
			//calculating gradient decent and getting the equation
			LE = this.Step_Gradient(LE.Matrix_Body[0][0], LE.Matrix_Body[1][0], Leaning_Rate, X_values_column, Y_values_column);
			
			//line drawing and mapping
			for(double p =0 ;p<=Max_X;p+=0.001) {
				y_val = LE.Matrix_Body[0][0] * p + LE.Matrix_Body[1][0];
				tx = tlb.Remap((float)p, 0, (float)Max_X, 105, 720);
				ty = tlb.Remap((float)y_val, (float)0 , (float)(Max_Y) ,565, 80);
				//System.out.println("TX: " + tx +" TY: "+ ty + " Y_VAL : " + y_val + "\nLE.x:  " + LE.x);
				plot.Draw_Circle((int)tx, (int)ty, 1, CSET.Red,"Fill");
				//plot.Pixel_Matrix[(int)ty][(int)tx] = new Pixel(CSET.Red);

			
			}
			plot.Commint_Matrix_Changes();
			
			//next iter
			z++;

			
			
			//frame rate control
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sw.Refresh_Frame(plot.IMG);
			
			
			
			
			//returning to original background
			for(int a1 = 0 ;a1<plot.Image_Height;a1++) {
				for(int b = 0; b<plot.Image_Width;b++) {
					plot.Pixel_Matrix[a1][b] = new Pixel(nolines.Pixel_Matrix[a1][b]);
				}
			}
		}

		
    }

    }
    public void Plot_Logistic_Regression(ArrayList<String> Values,ArrayList<String> Binary_Category,int number_of_iterations,double learning_rate) {
		ArrayList<Point> vals = CSV_File.Column_To_Point_List(Values, Binary_Category);
		SPlot plt = new SPlot();
		double B0 =0,B1=0;
		ArrayList<Double> Predictions = new ArrayList<Double>();
		double Cost =0,Lowest_Cost=Double.MAX_VALUE;
		for(int i = 0;i<number_of_iterations;i++) {
			
			//predictions
			for(int j =0;j<Values.size();j++) {
				double y = Double.parseDouble(Binary_Category.get(j));
				double pred = Neural_Net.Sigmoid(B0 + B1*Double.parseDouble(Values.get(j)));
				Predictions.add(pred);
				//System.out.println("Predictions:" +pred + " Reality: " + y);
				//System.out.println("B0: " + B0 + " B1: " + B1);
				B0 = B0 + learning_rate*(y - pred)*pred*(1.0-pred)*1.0;
				B1 = B1 + learning_rate*(y - pred)*pred*(1.0-pred)*Double.parseDouble(Values.get(j));

				//System.out.println("Res: "  + this.Sigmoid(B0_Guess + B1_Guess*Double.parseDouble(Values.get(j))));
			}
			
			//cost
			
			for(int j =0;j<Predictions.size();j++) {
				double y = Double.parseDouble(Binary_Category.get(j));
				Cost += -y*Math.log(Predictions.get(j)) - (1-y)*Math.log(Predictions.get(j));
			}
			
			Cost *= 1.0/Predictions.size();
			//System.out.println("COST:" +  Cost);
			
			if(Cost<Lowest_Cost) {
				Lowest_Cost = Cost;
				//System.out.println("COST: " + Cost + " B0 = "  + B0 + " B1 = " + B1);

			}
			
			
			Predictions.clear();
			Predictions = new ArrayList<Double>();
		}
		
		double y_val=0,tx,ty;
		Image rg = plt.Get_Scatter_Plot(vals, "X","Y");
		for(double i =0 ;i<30;i+=0.01) {
			y_val = Neural_Net.Sigmoid((B1 * i + B0));
			//System.out.println(y_val);
			tx = tlb.Remap((float)i, (float)0, (float)30, 105, 720);
			ty = tlb.Remap((float)y_val, (float)0 , (float)(1) ,575, 80);
			
			rg.Draw_Circle((int)tx, (int)ty, 1, CSET.Red,"Fill");
			//data_plot.Pixel_Matrix[(int)ty][(int)tx] = new Pixel(CSET.Red);

		
		}
		
		rg.Commint_Matrix_Changes();
		rg.Show_Image();
		
    }
    public void Plot_Quick_KNN(int K ,double Test_Values[],int Sample_Columns[]) {
    	if(Sample_Columns.length>2) {
    		System.out.println("Quick Plot Supports Only 2 Sample Column Plots");
    	}
    	ArrayList<Point> data = this.Data.Column_To_Point_List(Sample_Columns[0], Sample_Columns[1]);
		Image Scatter_Plot = new Image();
		Color_Palette CSET = new Color_Palette();
		Math_Toolbox tlb = new Math_Toolbox();
		double Max_X = Double.MIN_VALUE,Max_Y = Double.MIN_VALUE;
		double Min_X = Double.MAX_VALUE,Min_Y = Double.MAX_VALUE;

		for(int i = 0 ;i<data.size();i++) {
			if(data.get(i).x > Max_X) {
				Max_X = data.get(i).x;
			}
			if(data.get(i).x < Min_X) {
				Min_X = data.get(i).x;
			}
			if(data.get(i).y > Max_Y) {
				Max_Y = data.get(i).y;
			}
			if(data.get(i).y < Min_Y) {
				Min_Y = data.get(i).y;
			}
		}
		Max_X+=Max_X/4;
		Max_Y+=Max_Y/4;
		Max_X = Math.round(Max_X);
		Max_Y = Math.round(Max_Y);
		Scatter_Plot.Load_Blank_Canvas(650, 800, CSET.White_Smoke);
		Scatter_Plot.Draw_Square(75, 100, 575, 725, CSET.Black, "Corners");
		Scatter_Plot.Draw_Square(74, 99, 576, 726, CSET.Black, "Corners");
		Scatter_Plot.Draw_Square(73, 98, 577, 727, CSET.Black, "Corners");
		Scatter_Plot.Draw_Text(325, 750, "Y", CSET.Black);
		Scatter_Plot.Draw_Text(65, 400, "X", CSET.Black);
		double distX = (Math.abs(Min_X)+Math.abs(Max_X))/4;
		double distY = (Math.abs(Min_Y)+Math.abs(Max_Y))/4;

		Scatter_Plot.Draw_Text(595, 90 + 0 * 78*2,String.format("%.2f",((Min_X))), CSET.Black);
		Scatter_Plot.Draw_Text(595, 99 + 1 * 78*2,String.format("%.2f",((Min_X)+distX*1)), CSET.Black);
		Scatter_Plot.Draw_Text(595, 99 + 2 * 78*2,String.format("%.2f",((Min_X)+distX*2)), CSET.Black);
		Scatter_Plot.Draw_Text(595, 99 + 3 * 78*2,String.format("%.2f",((Min_X)+distX*3)), CSET.Black);
		Scatter_Plot.Draw_Text(595, 99 + 4 * 78*2,String.format("%.2f",((Max_X))), CSET.Black);

		Scatter_Plot.Draw_Text(575 - 0 * 62*2, 40,String.format("%.2f",(Min_Y)), CSET.Black);
		Scatter_Plot.Draw_Text(575 - 1 * 62*2, 40,String.format("%.2f",((Min_Y)+distY*1)), CSET.Black);
		Scatter_Plot.Draw_Text(575 - 2 * 62*2, 40,String.format("%.2f",((Min_Y)+distY*2)), CSET.Black);
		Scatter_Plot.Draw_Text(575 - 3 * 62*2, 40,String.format("%.2f",((Min_Y)+distY*3)), CSET.Black);
		Scatter_Plot.Draw_Text(575 - 4 * 62*2, 40,String.format("%.2f",((Max_Y))), CSET.Black);

		for(int i = 0 ;i<5;i++) {
			Scatter_Plot.Draw_Line(575 - 62 * i*2,100,575 - 62 * i*2,90,CSET.Black);
			
			Scatter_Plot.Draw_Line(575,100 + i * 78*2 ,575 + 10,100 + i * 78*2,CSET.Black);
			Scatter_Plot.Draw_Line(575,99 + i * 78*2 ,575 + 10,99 + i * 78*2,CSET.Black);



		}
		double tx,ty;
		Point b;
		Scatter_Plot.Update_Pixel_Matrix();
		for(int i = 0 ;i<data.size();i++) {
			b = data.get(i);
			tx = tlb.Remap((float)b.x, (float)Min_X, (float)Max_X, 105, 720 );
			ty = tlb.Remap((float)b.y, (float)Min_Y, (float)Max_Y, 565, 80);
			
			Scatter_Plot.Draw_Circle((int)tx, (int)ty, 3, CSET.Royal_Blue,"Fill");
			Scatter_Plot.Draw_Circle((int)tx, (int)ty, 4, CSET.White_Smoke);


		
		}
		
		Scatter_Plot.Commint_Matrix_Changes();
		Matrix Knn = this.KNN(K, Test_Values, Sample_Columns);
		tx = tlb.Remap((float)Test_Values[0], (float)Min_X, (float)Max_X, 105, 720 );
		ty = tlb.Remap((float)Test_Values[1], (float)Min_Y, (float)Max_Y, 565, 80);
		Scatter_Plot.Draw_Circle((int)tx, (int)ty, 6, CSET.Red,"Fill");
		Scatter_Plot.Draw_Circle((int)tx, (int)ty, 4, CSET.White_Smoke,"Fill");
		Scatter_Plot.Draw_Circle((int)tx, (int)ty, 2, CSET.Green,"Fill");



    	for(int i=0;i<K;i++) {
    		double Nx = Double.parseDouble(this.Data.Get((int)Knn.Matrix_Body[i][0], Sample_Columns[0]));
    		double Ny = Double.parseDouble(this.Data.Get((int)Knn.Matrix_Body[i][0], Sample_Columns[1]));
			tx = tlb.Remap((float)Nx, (float)Min_X, (float)Max_X, 105, 720 );
			ty = tlb.Remap((float)Ny, (float)Min_Y, (float)Max_Y, 565, 80);

			Scatter_Plot.Draw_Circle((int)tx, (int)ty, 5, CSET.White_Smoke,"Fill");
			Scatter_Plot.Draw_Circle((int)tx, (int)ty, 4, CSET.Hot_Pink,"Fill");

    		
    	}
    	Scatter_Plot.Show_Image();
    	
    	
    }

}







