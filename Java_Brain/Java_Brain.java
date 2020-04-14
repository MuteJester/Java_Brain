import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;



public class Java_Brain {
	public static Math_Toolbox tlb = new Math_Toolbox();
	public static Color_Palette CSET = new Color_Palette();
	public static BufferedReader Reader;
	SPlot plot = new SPlot();
	public ArrayList<ArrayList<String>> CSV_DATA;
	public ArrayList<String> Column_Names;

	public int Number_Of_Rows=0,Number_Of_Columns=0;
	public Java_Brain() {
		CSV_DATA = new ArrayList<ArrayList<String>>();
		Column_Names = new ArrayList<String>();
	}
	
	//Data Set Handling
	public void Load_CSV_File(String CSV_PATH) throws IOException {
		 try {
			Reader = new BufferedReader(new FileReader(CSV_PATH));
			 String line;
			 int max_number_of_Columns = 0;
			 int j =0;
			 line = Reader.readLine();
			 if((line) != null) {
				 Column_Names = new ArrayList<String>();
			        String[] values = line.split(",");
			        for(int i = 0;i<values.length;i++) {
			        	Column_Names.add(values[i]);
			        }
			        
			 }
			 
			    while ((line = Reader.readLine()) != null) {
			        String[] values = line.split(",");
			        if(values.length > max_number_of_Columns) {
			        	max_number_of_Columns = values.length;
			        }
			        CSV_DATA.add(new ArrayList<String>());
			        ArrayList<String> ptr = CSV_DATA.get(j);
			        for(int i = 0;i<values.length;i++) {
			        	ptr.add(values[i]);
			        }
			        
			        j++;
			    }
			    
			    Number_Of_Rows=j;
			    Number_Of_Columns=max_number_of_Columns;
			    int mg;
				for(int i = 0 ; i <CSV_DATA.size();i++) {
					ArrayList<String> ptr = CSV_DATA.get(i);
					for(int k =0;k<max_number_of_Columns;k++) {
						mg = ptr.size();
						if(mg < max_number_of_Columns) {
							while(mg < max_number_of_Columns) {
								ptr.add("Null");
								mg++;
							}
						}else {
							break;
						}
						
					}
				}
			    
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error In File Openning");
			e.printStackTrace();
		}
		 catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error In File Openning");
				e.printStackTrace();
			}
		 
	}
	public void Write_CSV(String F_Name) throws IOException {
		FileWriter csvWriter = new FileWriter(F_Name + ".csv");
		    csvWriter.append(String.join(",", this.Column_Names));
		    csvWriter.append("\n");
		for (int i =0;i<this.Number_Of_Rows;i++) {
		    csvWriter.append(String.join(",", CSV_DATA.get(i)));
		    csvWriter.append("\n");
		}

		csvWriter.flush();
		csvWriter.close();
	}
	public double Get_Max_Value_In_Column(int Column_Number) {
		ArrayList<String> col = this.Get_Spesific_Column(Column_Number);
		double max = Double.MIN_VALUE;
		for(int i =0;i<this.Number_Of_Rows;i++) {
			double cur_val = Double.parseDouble(col.get(i));
			if(max < cur_val) {
				max = cur_val;
			}
		}
		return max;
	}
	public void Print_CSV_Data() {
		Iterator<String> itr;
		for(int i=0;i<this.Column_Names.size();i++) {
			System.out.print(Column_Names.get(i)+" ");
		}
		System.out.println();
		for(int i = 0 ; i <CSV_DATA.size();i++) {
			itr = CSV_DATA.get(i).iterator();
			ArrayList<String> ptr = CSV_DATA.get(i);
			for(int j =0;j<ptr.size();j++) {
				System.out.print(ptr.get(j) + "  ");
			}
			System.out.println(" ");
		}
	}
	public ArrayList<String> Get_Spesific_Row(int Row_Number) {
		if(Row_Number < 0 || Row_Number > this.Number_Of_Rows) {
			System.out.println("Invalid Row Number");
			return null;
		}
		return this.CSV_DATA.get(Row_Number-1);
	}
	public ArrayList<String> Get_Spesific_Column(int Column_Number){
		if(Column_Number < 0 || Column_Number > this.Number_Of_Columns) {
			System.out.println("Invalid Row Number");
			return null;
		}
		ArrayList<String> Spesific_Coulmn = new ArrayList<String>();
		
		for(int  i = 0;i<this.Number_Of_Rows;i++) {
			ArrayList<String> ptr = CSV_DATA.get(i);
			Spesific_Coulmn.add(ptr.get(Column_Number-1));
		}
		
		return Spesific_Coulmn;
		
	}
	public String CSV_Get_Value(int Row,int Column) {
		if(Row > this.Number_Of_Rows || Column > this.Number_Of_Columns || 
			Row < 0 || Column < 0 ){
				System.out.println("Error In Value Location");
				return null;
		}
		return CSV_DATA.get(Row-1).get(Column-1);
	}
	public void CSV_Set_Value(int Row,int Column,String Value) {
		if(Row > this.Number_Of_Rows || Column > this.Number_Of_Columns || 
			Row < 0 || Column < 0 ){
				System.out.println("Error In Value Location");
				return;
		}
		 CSV_DATA.get(Row-1).set(Column-1, Value);
		 
	}
	public Java_Brain Split_Data_Set(int Split_Percentage) {
		if(Split_Percentage > 100) {
			System.out.println("Cannot Split Into: " + (100 - Split_Percentage) +"%  /  " + Split_Percentage + "% ");
			return null;
		}
		
		Java_Brain Split = new Java_Brain();
		int number_of_rows;
		number_of_rows = (int) (((double)this.Number_Of_Rows/100)*Split_Percentage);
		Split.Number_Of_Rows = number_of_rows+1;
		Split.Number_Of_Columns = this.Number_Of_Columns;
		
		Split.Column_Names=new ArrayList<String>();
		for(int i=0;i<this.Column_Names.size();i++) {
			Split.Column_Names.add(this.Column_Names.get(i));
		}
		Split.CSV_DATA = new ArrayList<ArrayList<String>>();
		
		for(int i = this.Number_Of_Rows-number_of_rows;i<=this.Number_Of_Rows;i++) {
			ArrayList<String> temp = new ArrayList<String>(this.Get_Spesific_Row(i));
			Split.CSV_DATA.add(temp);
		}
		
		int j;
		j=Number_Of_Rows-number_of_rows;
		for(int i = this.Number_Of_Rows-number_of_rows;i<this.Number_Of_Rows;i++) {
			//System.out.println(i);
			this.CSV_DATA.remove(j);
		}
		
		this.Number_Of_Rows -=number_of_rows;
		return Split;
		
	}
	public ArrayList<Point> Column_To_Point_List(int Column_X,int Column_Y){
		ArrayList<String> X = this.Get_Spesific_Column(Column_X);
		ArrayList<String> Y = this.Get_Spesific_Column(Column_Y);
		ArrayList<Point> p = new ArrayList<Point>();
		for(int i =0;i<this.Number_Of_Rows;i++) {
			p.add(new Point(Double.parseDouble(X.get(i)),Double.parseDouble(Y.get(i)),0));
		}
		return p;
		
	}
	public ArrayList<Point> Column_To_Point_List(ArrayList<String> X_Values, ArrayList<String> Y_Values){
		ArrayList<Point> p = new ArrayList<Point>();
		for(int i =0;i<this.Number_Of_Rows;i++) {
			p.add(new Point(Double.parseDouble(X_Values.get(i)),Double.parseDouble(Y_Values.get(i)),0));
		}
		return p;
		
	}
	public ArrayList<Point> Column_To_Point_List(int Column_X,int Column_Y,int Column_Z){
		ArrayList<String> X = this.Get_Spesific_Column(Column_X);
		ArrayList<String> Y = this.Get_Spesific_Column(Column_Y);
		ArrayList<String> Z = this.Get_Spesific_Column(Column_Z);
		ArrayList<Point> p = new ArrayList<Point>();
		for(int i =0;i<this.Number_Of_Rows;i++) {
			p.add(new Point(Double.parseDouble(X.get(i)),Double.parseDouble(Z.get(i)),Double.parseDouble(Z.get(i))));
		}
		return p;
	}
	public ArrayList<Point> Column_To_Point_List(ArrayList<String> X_Values, ArrayList<String> Y_Values,ArrayList<String> Z_Values){
		ArrayList<Point> p = new ArrayList<Point>();
		for(int i =0;i<this.Number_Of_Rows;i++) {
			p.add(new Point(Double.parseDouble(X_Values.get(i)),Double.parseDouble(Y_Values.get(i)),Double.parseDouble(Z_Values.get(i))));
		}
		return p;
		
	}
	public ArrayList<String> DoubleList_To_StringList(ArrayList<Double> to_convert){
		ArrayList<String> res= new ArrayList<String>();
		for(int i =0;i<to_convert.size();i++) {
			res.add(String.format("%f",to_convert.get(i)));
		}
		return res;
	}
	public ArrayList<Double> StringList_To_DoubleList(ArrayList<String> to_convert){
		ArrayList<Double> res= new ArrayList<Double>();
		for(int i =0;i<to_convert.size();i++) {
			res.add(Double.parseDouble(to_convert.get(i)));
		}
		return res;
	}
	public void Add_Column(String Column_Name) {
		this.Number_Of_Columns++;
		this.Column_Names.add(Column_Name);
		for(int i=1;i<=this.Number_Of_Rows;i++) {
			this.CSV_DATA.get(i-1).add("0");
		}
	}
	public void Add_Column(String Column_Name,ArrayList<String> Column_Values) {
		this.Number_Of_Columns++;
		if(Column_Values.size()>this.Number_Of_Rows) {
			int i =1;
			while(i<=this.Number_Of_Rows) {
				for( i=1;i<=this.Number_Of_Rows;i++) {
					this.CSV_DATA.get(i-1).add(Column_Values.get(i));
				}
			}
			
			int n_of_rows_needed =Column_Values.size()-Number_Of_Rows;
			int cr = this.Number_Of_Rows;
			for(int j=0;j<n_of_rows_needed-1;j++) {
				this.Add_Row();
			}

			
			for(i=cr;i<Column_Values.size();i++) {
				this.CSV_Set_Value(i, Number_Of_Columns, Column_Values.get(i));
			}
			
		}
		else {
			this.Column_Names.add(Column_Name);
			for(int i=1;i<=this.Number_Of_Rows;i++) {
				this.CSV_DATA.get(i-1).add(Column_Values.get(i-1));
			}
		}
	}
	public void Add_Row() {
		this.Number_Of_Rows++;
		this.CSV_DATA.add(new ArrayList<String>());
		for(int i=0;i<this.Number_Of_Columns;i++) {
			this.CSV_DATA.get(Number_Of_Rows-1).add("0");
		}
	}
	public void Add_Row(ArrayList<String> Row_Values) {
		if(Row_Values.size()>this.Number_Of_Columns) {
			System.out.println("Aborted...\nRow Length Exceeds The Amount Of Registerd Columns ");
		}
		this.Number_Of_Rows++;
		this.CSV_DATA.add(Row_Values);
		
	}
	public void Remove_Column(int Column_Number) {
		if(Column_Number > this.Number_Of_Columns) {
			System.out.println("No Such Column Number In File");
			return;
		}
		for(int i=0;i<this.Number_Of_Rows;i++) {
			this.CSV_DATA.get(i).remove(Column_Number-1);
		}
		this.Number_Of_Columns--;
		this.Column_Names.remove(Column_Number-1);
	}
	public void Remove_Row(int Row_Number) {
		if(Row_Number > this.Number_Of_Rows) {
			System.out.println("No Such Row Number In File");
			return;
		}
		
		this.CSV_DATA.remove(Row_Number-1);
		this.Number_Of_Rows--;
	}
	public void Replace_Pattern_In_Column(int Column_Number,String Pattern,String Replace_With) {
		for(int i =1;i<=this.Number_Of_Rows;i++) {
			if(this.CSV_Get_Value(i, Column_Number).equals(Pattern)) {
				this.CSV_Set_Value(i, Column_Number, Replace_With);
			}
		}
		
	}
	
	//
	
	//Math Utilities
	public double Sigmoid(double x) {
		return (1.0/(1.0 + Math.exp(-x) ));
	}
	public double Get_Column_Mean(int Column_Number) {
		if(Column_Number < 0 || Column_Number > this.Number_Of_Columns) {
			System.out.println("Invalid Row Number");
			return -1;
		}
		double[] dt = new double[Number_Of_Rows];
		for(int i = 1 ; i<=this.Number_Of_Rows;i++) {
			dt[i-1] = Double.parseDouble(this.CSV_Get_Value(i, Column_Number));
		}		
		
		double mean=0;
		for(int i =0;i<this.Number_Of_Rows;i++) {
			mean += dt[i];
		}
		return mean/Number_Of_Rows;
		
	}
	public double Get_Column_Median(int Column_Number) {
		if(Column_Number < 0 || Column_Number > this.Number_Of_Columns) {
			System.out.println("Invalid Row Number");
			return -1;
		}
		double[] dt = new double[Number_Of_Rows];
		for(int i = 1 ; i<=this.Number_Of_Rows;i++) {
			dt[i-1] = Double.parseDouble(this.CSV_Get_Value(i, Column_Number));
		}
		Arrays.sort(dt);
		if (dt.length % 2 == 0) {
			return (double)(dt[dt.length / 2] + (double)dt[(dt.length / 2) - 1]) / 2;
		}
		else {

			return dt[dt.length / 2];
		}
	
	}	
	public double Get_Column_Standard_Deviation(int Column_Number) {
		if(Column_Number < 0 || Column_Number > this.Number_Of_Columns) {
			System.out.println("Invalid Row Number");
			return -1;
		}
		double[] dt = new double[Number_Of_Rows];
		for(int i = 1 ; i<=this.Number_Of_Rows;i++) {
			if(!CSV_Get_Value(i, Column_Number).matches("[0-9,-,.]+")) {
				System.out.println("Error Not All Column Value Are Numerical");
				return -1;
			}
			dt[i-1] = Double.parseDouble(this.CSV_Get_Value(i, Column_Number));
		}
		double mean = this.Get_Column_Mean(Column_Number);
		double sum = 0;
		for (int i = 0; i < dt.length; i++) {
			sum += ((dt[i] - mean)*(dt[i] - mean));
		}
		sum /= (double)dt.length;
		sum = Math.sqrt(sum);
		return sum;
		
	}
	public double Get_Column_Variance(int Column_Number) {
		double Deviation = Get_Column_Standard_Deviation(Column_Number);
		return (Deviation)*(Deviation);
	}
	
	//Algorithms
	public double Squared_Point_Distance(Point first, Point second) {
		return (first.x - second.x)*(first.x - second.x) + (first.y - second.y)*(first.y - second.y) + (first.z - second.z)*(first.z - second.z);
	}
	public Point Get_Linear_Regression_Equation(ArrayList<Point> Data) {
		double a,bx;
		double sum_xy=0,sum_xsquared=0,sum_ysquared=0,sum_x=0,sum_y=0;
		Point t;
		for(int i = 0 ; i<Data.size();i++) {
			t = Data.get(i);
			sum_x += t.x;
			sum_y += t.y;
			sum_xy += t.y*t.x;
			sum_xsquared += t.x*t.x;
			sum_ysquared += t.y*t.y;
		}
		a = (sum_y*sum_xsquared - sum_x*sum_xy)/(Data.size()*sum_xsquared - sum_x*sum_x);
		bx = (Data.size() * sum_xy - sum_x*sum_y)/(Data.size()*sum_xsquared - sum_x*sum_x);
		return new Point(a,bx,0); 
		
	}
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
	private double Compute_Error_For_Given_Points(ArrayList<String> X_val,ArrayList<String> Y_val,double b,double m) {
		double totalError=0;
		double x,y;
		for(int i = 0 ;i<this.Number_Of_Rows;i++) {
			x = Double.parseDouble(X_val.get(i));
			y = Double.parseDouble(Y_val.get(i));
			
			totalError += (y - (x * m + b) ) * (y - (x * m + b) );
		}
		
		totalError *= (double)1/(this.Number_Of_Rows);
		return totalError;
	}
	private Point Step_Gradient(double Current_B,double Current_M,double Learning_Rate ,ArrayList<String> X_val,ArrayList<String> Y_val) {
		double b_gradient = 0;
		double m_gradient = 0;
		double x , y;
		double new_b,new_m;
		for(int i =0;i<this.Number_Of_Rows;i++) {
			x = Double.parseDouble(X_val.get(i));
			y = Double.parseDouble(Y_val.get(i));
			m_gradient += ((double)2/Number_Of_Rows)*-x*(y - (Current_M * x + Current_B));
			b_gradient += ((double)2/Number_Of_Rows)*-(y - (Current_M * x + Current_B));
		}
		new_b = Current_B - (Learning_Rate * b_gradient);
		new_m = Current_M - (Learning_Rate * m_gradient);
		
		return new Point(new_m,new_b,0);
		
	}
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
	public double Get_Pearson_Correlation_Coefficient(ArrayList<String> Y ,ArrayList<String> Y_Hat) {
		double r =0;
		double sigma_xy=0,sigma_x=0,sigma_y=0,sigma_xs=0,sigma_ys=0;
		ArrayList<Double>y,y_h;
		y = this.StringList_To_DoubleList(Y);
		y_h = this.StringList_To_DoubleList(Y_Hat);
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
	public double Get_R_Squared(ArrayList<String> Y ,ArrayList<String> Y_Hat) {
		double R_S = this.Get_Pearson_Correlation_Coefficient(Y, Y_Hat);
		R_S*=R_S;
		return R_S;
		
	}
	public double Get_Adjusted_R_Squared(ArrayList<String> Y ,ArrayList<String> Y_Hat,int Indpendent_Variables) {
		double R_S = this.Get_R_Squared(Y, Y_Hat);
		double ARS=(1.0-R_S)*(this.Number_Of_Rows-1);
		ARS/=this.Number_Of_Rows-1-Indpendent_Variables;
		ARS=1.0-ARS;
		return ARS;
		
	}
	public Matrix Logistic_Regression_Confusion_Matrix(Matrix LR_Weights,int Binary_Column,ArrayList<Integer> Sampled_Rows,double Decision_Boundary ) {		
		Matrix CM = new Matrix(2,2);
		double TruePositive=0;
		double TrueNegative=0;
		double FalsePositive=0;
		double FalseNegative=0;
		for(int i =1;i<=this.Number_Of_Rows;i++) {
			double pred_s = LR_Weights.Matrix_Body[0][0];
			for(int j=1;j<=Sampled_Rows.size();j++) {
				pred_s += LR_Weights.Matrix_Body[j][0] *Double.parseDouble(this.CSV_Get_Value(i,Sampled_Rows.get(j-1)));
			}
			pred_s = this.Sigmoid(pred_s);
			double actual = Double.parseDouble(this.CSV_Get_Value(i, Binary_Column));
			
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
		CM.Matrix_Body[0][1] =FalsePositive;
		CM.Matrix_Body[1][1] =TrueNegative; 
		return CM;
	}
	public void Print_List_Of_Rates(Matrix Confusion_Matrix,int Number_Sampled) {
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
	public void Validate_Logistic_Regression(Matrix LR_Weights,int Binary_Column,ArrayList<Integer> Sampled_Rows) {		
		for(int i =1;i<=this.Number_Of_Rows;i++) {
			double pred_s = LR_Weights.Matrix_Body[0][0];
			for(int j=1;j<=Sampled_Rows.size();j++) {
				pred_s += LR_Weights.Matrix_Body[j][0] *Double.parseDouble(this.CSV_Get_Value(i,Sampled_Rows.get(j-1)));
			}
			pred_s = this.Sigmoid(pred_s);

			System.out.println("Prediction: " + pred_s + " Reality " + this.CSV_Get_Value(i, Binary_Column));
		}
	
			
	}

	private Matrix Step_Gradient(Matrix Current_Weights,double Learning_Rate ,ArrayList<Integer> Columns_Of_Sampels,ArrayList<String> True_Y) {
		int nof = Columns_Of_Sampels.size()+1;
		Matrix Gradients = new Matrix(nof,1);
		Matrix Teta = new Matrix(Current_Weights);
		Matrix Values = new Matrix(nof,1);
		Matrix Prediction = new Matrix(Current_Weights);
		Matrix h0 = null;
		Teta.Matrix_Transpose();

		for(int i=1;i<=this.Number_Of_Rows;i++) {
			//h0  guess
			Values.Matrix_Body[0][0]=1;
			for(int j =1;j<nof;j++) {
				Values.Matrix_Body[j][0] = Double.parseDouble(this.CSV_Get_Value(i, Columns_Of_Sampels.get(j-1)));
			}			
			h0=Teta.Dot_Product(Values);
						
			for(int j =0;j<nof;j++) {
				Gradients.Matrix_Body[j][0] += (h0.Matrix_Body[0][0] - Double.parseDouble(True_Y.get(i-1)))*Values.Matrix_Body[j][0];
			}

		}

		for(int j =0;j<nof;j++) {
			Gradients.Matrix_Body[j][0]*=(Learning_Rate/this.Number_Of_Rows);
			
			Prediction.Matrix_Body[j][0] = Prediction.Matrix_Body[j][0] - Gradients.Matrix_Body[j][0];
		}
		

		
		
		
		return Prediction;
		
	}
	public Matrix Linear_Regression_Gradient_DescentM(ArrayList<Integer> Columns_Of_Sampels,ArrayList<String> True_Y,double Leaning_Rate,int number_of_iterations) {
		//y = mx + b - for slope calculation
		Matrix LE = new Matrix(Columns_Of_Sampels.size()+1,1);
		for(int i = 0;i<number_of_iterations;i++) {
			LE = this.Step_Gradient(LE , Leaning_Rate, Columns_Of_Sampels, True_Y);
		}
		return LE;
		
	}

	public Point Linear_Regression_Gradient_Descent(ArrayList<String> X_values,ArrayList<String> Y_values,double Leaning_Rate,int number_of_iterations) {
		//y = mx + b - for slope calculation
		Point LE = new Point();
		for(int i = 0;i<number_of_iterations;i++) {
			LE = this.Step_Gradient(LE.y, LE.x, Leaning_Rate, X_values, Y_values);
		}
		LE.z = Compute_Error_For_Given_Points(X_values,Y_values,LE.y,LE.x);

		return LE;
		
	}
	public Point Get_Logistic_Regression(ArrayList<String> Values,ArrayList<String> Binary_Category,int number_of_iterations,double learning_rate) {
		ArrayList<Point> vals = this.Column_To_Point_List(Values, Binary_Category);
		SPlot plt = new SPlot();
		double B0 =0,B1=0;
		ArrayList<Double> Predictions = new ArrayList<Double>();
		double Cost =0,Lowest_Cost=Double.MAX_VALUE;
		for(int i = 0;i<number_of_iterations;i++) {
			
			//predictions
			for(int j =0;j<Values.size();j++) {
				double y = Double.parseDouble(Binary_Category.get(j));
				double pred = this.Sigmoid(B0 + B1*Double.parseDouble(Values.get(j)));
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
		
		

		return new Point(B1,B0,0);
		
	}
	public Matrix Get_Multi_Value_Logistic_Regression(ArrayList<ArrayList<String>> Values,ArrayList<String> Binary_Category,int number_of_iterations,double learning_rate) {
		
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
				double pred = this.Sigmoid(s_pred);
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
	public ArrayList<String> Get_Linear_Regression_Predictions(Matrix LR,int True_Column,ArrayList<Integer> Columns_Of_Sampels){
		ArrayList<String> res = new ArrayList<String>();
		for(int i =1;i<=this.Number_Of_Rows;i++) {
			double pred = LR.Matrix_Body[0][0];

			for(int j=1;j<Columns_Of_Sampels.size()+1;j++) {
				pred += LR.Matrix_Body[j][0]*Double.parseDouble(this.CSV_Get_Value(i, Columns_Of_Sampels.get(j-1)));
			}
			System.out.println("Predictions: " + pred + "  Actual "  + this.Get_Spesific_Column(True_Column).get(i-1));
			res.add(String.format("%f", pred));
		}
		return res;
	}
	
	//Visual
	public void Plot_Linear_Regression(ArrayList<Point> Data,String X_Name,String Y_Name) {
		Point LE = Get_Linear_Regression_Equation(Data) ;
		Math_Toolbox tlb = new Math_Toolbox();
		SPlot plt = new SPlot();
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
			y_val = LE.y * i + LE.x;
			tx = tlb.Remap((float)i, 0, (float)Max_X, 105, 720);
			ty = tlb.Remap((float)y_val, (float)0 , (float)(Max_Y) ,575, 80);
			data_plot.Draw_Circle((int)tx, (int)ty, 1, CSET.Red,"Fill");
			//data_plot.Pixel_Matrix[(int)ty][(int)tx] = new Pixel(CSET.Red);

		
		}
		data_plot.Commint_Matrix_Changes();
		data_plot.Show_Image();
		
	}
    public void Plot_Scatter_Plot(ArrayList<Point> Data,String X_Name,String Y_Name) {
    	plot.Show_Scatter_Plot(Data, X_Name, Y_Name);
    }
    public void Plot_Gradient_Descent(ArrayList<String> X_values,ArrayList<String> Y_values,double Leaning_Rate,int number_of_iterations) {
		ArrayList<Point> points = new ArrayList<Point>();
		for(int i = 0 ;i<Number_Of_Rows;i++) {
			points.add(new Point(Double.parseDouble(X_values.get(i)),Double.parseDouble(Y_values.get(i)),0));
		}
		
    	Image plot = this.plot.Get_Scatter_Plot(points, "X", "Y");
    	
    	
		
    	SIPL_Window sw = new SIPL_Window(plot.IMG);
		sw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int z =0;
		Point LE = new Point();
		

    	Point convergence_test =this.Linear_Regression_Gradient_Descent(X_values, Y_values, Leaning_Rate, number_of_iterations);
		if(convergence_test.x == Double.NaN || convergence_test.y == Double.NaN) {
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
			LE = this.Step_Gradient(LE.y, LE.x, Leaning_Rate, X_values, Y_values);
			
			//line drawing and mapping
			for(double p =0 ;p<=Max_X;p+=0.01) {
				y_val = LE.x * p + LE.y;
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
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
		ArrayList<Point> vals = this.Column_To_Point_List(Values, Binary_Category);
		SPlot plt = new SPlot();
		double B0 =0,B1=0;
		ArrayList<Double> Predictions = new ArrayList<Double>();
		double Cost =0,Lowest_Cost=Double.MAX_VALUE;
		for(int i = 0;i<number_of_iterations;i++) {
			
			//predictions
			for(int j =0;j<Values.size();j++) {
				double y = Double.parseDouble(Binary_Category.get(j));
				double pred = this.Sigmoid(B0 + B1*Double.parseDouble(Values.get(j)));
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
			y_val = this.Sigmoid((B1 * i + B0));
			//System.out.println(y_val);
			tx = tlb.Remap((float)i, (float)0, (float)30, 105, 720);
			ty = tlb.Remap((float)y_val, (float)0 , (float)(1) ,575, 80);
			
			rg.Draw_Circle((int)tx, (int)ty, 1, CSET.Red,"Fill");
			//data_plot.Pixel_Matrix[(int)ty][(int)tx] = new Pixel(CSET.Red);

		
		}
		
		rg.Commint_Matrix_Changes();
		rg.Show_Image();
		
    }
    
}






class Neuron{
	
    double[] weights;
    double[] backprop_weights;
    double gradient;
    double bias;
    double value = 0;
    
	static double minWeightValue;
	static double maxWeightValue;
	
	  // Constructor for the hidden / output neurons
    public Neuron(double[] weights, double bias){
        this.weights = weights;
        this.bias = bias;
        this.backprop_weights = this.weights;
        this.gradient = 0;
    }
    
    // Constructor for the input neurons
    public Neuron(double value){
        this.weights = null;
        this.bias = -1;
        this.backprop_weights = this.weights;
        this.gradient = -1;
        this.value = value;
    }
    
    // Static function to set min and max weight for all variables
    public static void setRangeWeight(double min,double max) {
    	minWeightValue = min;
    	maxWeightValue = max;
    }
    
    // Function used at the end of the backprop to switch the calculated value in the
    // cache weight in the weights
    public void Update_Weights() {
    	this.weights = this.backprop_weights;
    }
}

class Neuron_Layer{
	
	public Neuron[] neurons;
	
	// Constructor for the hidden and output layer
	public Neuron_Layer(int Neurons_Connections,int Number_of_Neurons) {
		this.neurons = new Neuron[Number_of_Neurons];
		
		for(int i = 0; i < Number_of_Neurons; i++) {
			double[] weights = new double[Neurons_Connections];
			for(int j = 0; j < Neurons_Connections; j++) {
				weights[j] = Math_Toolbox.random_double_in_range(Neuron.minWeightValue, Neuron.maxWeightValue);
			}
			neurons[i] = new Neuron(weights,Math_Toolbox.random_double_in_range(0, 1));
		}
	}
	
	
	// Constructor for the input layer
	public Neuron_Layer(double input[]) {
		this.neurons = new Neuron[input.length];
		for(int i = 0; i < input.length; i++) {
			this.neurons[i] = new Neuron(input[i]);
		}
	}

}

class Data_Cartridge{
	double[] Data;
	double[] Expected_Output;
	public Data_Cartridge(double[] in_data,double expected_out[]) {
		this.Data=in_data;
		this.Expected_Output=expected_out;
	}
	
}

class Training_Data{
	Data_Cartridge[] Data;
	
	public Training_Data(){
		
	}
	
	public void Load_Columns_As_Data(Java_Brain Data_Set,int[] Data_Column_Number,int Result_Column) {
		this.Data = new Data_Cartridge[Data_Set.Number_Of_Rows];
		for(int i=0;i<Data.length;i++) {
			double row_values[] = new  double[Data_Column_Number.length];
			for(int j =0;j<Data_Column_Number.length;j++) {
				row_values[j] = Double.parseDouble(Data_Set.CSV_Get_Value(i+1, Data_Column_Number[j]));
			}
			this.Data[i] = new Data_Cartridge(row_values,new double[] {Double.parseDouble(Data_Set.CSV_Get_Value(i+1, Result_Column))});
			
		}
	}
	
	

	public void Print_Data() {
		for(int i=0;i<Data.length;i++) {
			for(int j=0;j<this.Data[i].Data.length;j++) {
				System.out.print(this.Data[i].Data[j] + " ");
			}
			System.out.print("\n");
		}
	}
	
}

class Neural_Net{
	static Neuron_Layer[] Layers;
	Training_Data Training_Data;
	double Learning_Rate;
	public Neural_Net(int[] Topology_Of_Neurons,Training_Data Training_Data,double Learning_Rate) {
		Neuron.setRangeWeight(-1, 1);
		Layers = new Neuron_Layer[Topology_Of_Neurons.length/2 + 1];
		Layers[0] = null;
		int j =1;
		for(int i=0;i<Topology_Of_Neurons.length;i+=2) {
			Layers[j] = new Neuron_Layer(Topology_Of_Neurons[i],Topology_Of_Neurons[i+1]);
			j++;
			//System.out.println("Done " + i );
		}
		this.Training_Data=Training_Data;
		this.Learning_Rate=Learning_Rate;
	}
	
	 public void Forward_Data(double[] inputs) {
	    	// First bring the inputs into the input layer layers[0]
	    	Layers[0] = new Neuron_Layer(inputs);
	        for(int i = 1; i < Layers.length; i++) {
	        	for(int j = 0; j < Layers[i].neurons.length; j++) {
	        		//summing the weights
	        		double sum = 0;
	        		for(int k = 0; k < Layers[i-1].neurons.length; k++) {
	        			sum += Layers[i-1].neurons[k].value*Layers[i].neurons[j].weights[k];
	        		}
	        		//sum += layers[i].neurons[j].bias; // add in the bias 
	        		Layers[i].neurons[j].value = Math_Toolbox.Sigmoid(sum);
	        	}
	        } 	
	    }
	
	    // This function sums up all the gradient connecting a given neuron in a given layer
	  public double sumGradient(int neuron_index,int layer_index) {
		  double gradient_sum = 0;
	    	Neuron_Layer current_layer = Layers[layer_index];
	    	for(int i = 0; i < current_layer.neurons.length; i++) {
	    		Neuron current_neuron = current_layer.neurons[i];
	    		gradient_sum += current_neuron.weights[neuron_index]*current_neuron.gradient;
	    	}
	    	return gradient_sum;
	    }
	 
	 public  void Back_Propagate(Data_Cartridge training_Data) {
	    	
	    	int number_layers = Layers.length;
	    	int out_index = number_layers-1;
	    	
	    	// Update the output layers 
	    	// For each output
	    	for(int i = 0; i < Layers[out_index].neurons.length; i++) {
	    		// and for each of their weights
	    		double output = Layers[out_index].neurons[i].value;
	    		double target = training_Data.Expected_Output[i];
	    		double derivative = output-target;
	    		double delta = derivative*(output*(1-output));
	    		Layers[out_index].neurons[i].gradient = delta;
	    		for(int j = 0; j < Layers[out_index].neurons[i].weights.length;j++) { 
	    			double previous_output = Layers[out_index-1].neurons[j].value;
	    			double error = delta*previous_output;
	    			Layers[out_index].neurons[i].backprop_weights[j] = Layers[out_index].neurons[i].weights[j] - Learning_Rate*error;
	    		}
	    	}
	    	
	    	//Update all the subsequent hidden layers
	    	for(int i = out_index-1; i > 0; i--) {
	    		// For all neurons in that layers
	    		for(int j = 0; j < Layers[i].neurons.length; j++) {
	    			double output = Layers[i].neurons[j].value;
	    			double gradient_sum = sumGradient(j,i+1);
	    			double delta = (gradient_sum)*(output*(1-output));
	    			Layers[i].neurons[j].gradient = delta;
	    			// And for all their weights
	    			for(int k = 0; k < Layers[i].neurons[j].weights.length; k++) {
	    				double previous_output = Layers[i-1].neurons[k].value;
	    				double error = delta*previous_output;
	    				Layers[i].neurons[j].backprop_weights[k] = Layers[i].neurons[j].weights[k] - Learning_Rate*error;
	    			}
	    		}
	    	}
	    	
	    	// Here we do another pass where we update all the weights
	    	for(int i = 0; i< Layers.length;i++) {
	    		for(int j = 0; j < Layers[i].neurons.length;j++) {
	    			Layers[i].neurons[j].Update_Weights();
	    		}
	    	}
	    	
	    }
	 public  void Start_Trainig(int Training_Iterations) {
	    	for(int i = 0; i < Training_Iterations; i++) {
	    		for(int j = 0; j < Training_Data.Data.length; j++) {
	    			Forward_Data(Training_Data.Data[j].Data);
	    			Back_Propagate(Training_Data.Data[j]);
	    		}
	    	}
	    }
	 public  void Print_Outputs_Neurons() {
		 	System.out.println("============");
	        System.out.println("Output After Training");
	        System.out.println("============");
	        for(int i = 0; i < Training_Data.Data.length; i++) {
	        	Forward_Data(Training_Data.Data[i].Data);
	        	for(int j=0;j<Layers[Layers.length-1].neurons.length;j++) {
		            System.out.println(Layers[2].neurons[j].value);

	        	}
	        }
	 }

}
