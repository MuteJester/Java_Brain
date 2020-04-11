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
		for(int i=0;i<this.CSV_DATA.size();i++) {
			Split.CSV_DATA.add(new ArrayList<String>());
		}
		int j=0;
		for(int i = this.Number_Of_Rows-number_of_rows;i<=this.Number_Of_Rows;i++) {
			ArrayList<String> temp = new ArrayList<String>(this.Get_Spesific_Row(i));
			Split.CSV_DATA.set(j, temp);
			j++;			
		}
		
		System.out.println("ROWS: " + this.CSV_DATA.size());
		j=Number_Of_Rows-number_of_rows;
		for(int i = this.Number_Of_Rows-number_of_rows;i<this.Number_Of_Rows;i++) {
			System.out.println(i);
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
		//System.out.println("A : " + ax);
		//System.out.println("BX : " + b);

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
	public Point LR_Gradient_Descent(ArrayList<String> X_values,ArrayList<String> Y_values,double Leaning_Rate,int number_of_iterations) {
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
		double B0_Guess=0.9,B1_Guess=0.9;
		double B0 =0.9,B1=0.9;
		double Px=0;

		for(int i =0;i<Binary_Category.size();i++) {
			if(Double.parseDouble(Binary_Category.get(i)) == 1) {
				Px++;
			}
		}
		Px/=Binary_Category.size();
		ArrayList<Double> Predictions = new ArrayList<Double>();
		double Cost =0,Lowest_Cost=Double.MAX_VALUE;
		for(int i = 0;i<number_of_iterations;i++) {
			
			//predictions
			for(int j =0;j<Values.size();j++) {
				Predictions.add(this.Sigmoid(B0_Guess + B1_Guess*Double.parseDouble(Values.get(j))));
				//System.out.println("Res: "  + this.Sigmoid(B0_Guess + B1_Guess*Double.parseDouble(Values.get(j))));
			}
			
			//cost
			
			for(int j =0;j<Predictions.size();j++) {
				if(Double.parseDouble(Binary_Category.get(j)) == 1) {
					Cost += -Math.log(Predictions.get(j));
				}else {
					Cost += -Math.log((double)1-Predictions.get(j));
				}
			}
			
			Cost *= 1.0/Predictions.size();
			//System.out.println("COST:" +  Cost);
			
			if(Cost<Lowest_Cost) {
				Lowest_Cost = Cost;
				B0 = B0_Guess;
				B1 = B1_Guess;
				//System.out.println("COST: " + Cost + " B0 = "  + B0 + " B1 = " + B1);

			}
			
			
			double grad =0,div =0;
			for(int j =0;j<Predictions.size();j++) {
				grad += Predictions.get(j)*(1-Predictions.get(j));
				div += Predictions.get(j) - Double.parseDouble(Binary_Category.get(j));
			}
			
			grad/=Predictions.size();
			div /=Predictions.size();
			grad*=learning_rate;
			div *= learning_rate;
			//gd
			B1_Guess = B1_Guess - grad;
			B0_Guess = B0_Guess - div;
			
			//Cost =0;

			
			Predictions.clear();
			Predictions = new ArrayList<Double>();
		}
		

		return new Point(B1,B0,0);
		
	}
	
	
	//Visual
	public void Show_Linear_Regression(ArrayList<Point> Data,String X_Name,String Y_Name) {
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
    public void Show_Scatter_Plot(ArrayList<Point> Data,String X_Name,String Y_Name) {
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
		

    	Point convergence_test =this.LR_Gradient_Descent(X_values, Y_values, Leaning_Rate, number_of_iterations);
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
		double B0_Guess=0.9,B1_Guess=0.9;
		double B0 =0.9,B1=0.9;
		double Px=0;

		for(int i =0;i<Binary_Category.size();i++) {
			if(Double.parseDouble(Binary_Category.get(i)) == 1) {
				Px++;
			}
		}
		Px/=Binary_Category.size();
		ArrayList<Double> Predictions = new ArrayList<Double>();
		double Cost =0,Lowest_Cost=Double.MAX_VALUE;
		for(int i = 0;i<number_of_iterations;i++) {
			
			//predictions
			for(int j =0;j<Values.size();j++) {
				Predictions.add(this.Sigmoid(B0_Guess + B1_Guess*Double.parseDouble(Values.get(j))));
				//System.out.println("Res: "  + this.Sigmoid(B0_Guess + B1_Guess*Double.parseDouble(Values.get(j))));
			}
			
			//cost
			
			for(int j =0;j<Predictions.size();j++) {
				if(Double.parseDouble(Binary_Category.get(j)) == 1) {
					Cost += -Math.log(Predictions.get(j));
				}else {
					Cost += -Math.log((double)1-Predictions.get(j));
				}
			}
			
			Cost *= 1.0/Predictions.size();
			//System.out.println("COST:" +  Cost);
			
			if(Cost<Lowest_Cost) {
				Lowest_Cost = Cost;
				B0 = B0_Guess;
				B1 = B1_Guess;
				System.out.println("COST: " + Cost + " B0 = "  + B0 + " B1 = " + B1);

			}
			
			
		
			//gd
			B1_Guess = B1_Guess ;
			B0_Guess = B0_Guess -((learning_rate*Cost)/Predictions.size());
			
			//Cost =0;

			
			Predictions.clear();
			Predictions = new ArrayList<Double>();
		}
		
		double y_val=0,tx,ty;
		Image rg = plt.Get_Scatter_Plot(vals, "X","Y");
		for(double i =0 ;i<30;i+=0.01) {
			y_val = this.Sigmoid((B1 * i + B0));
			System.out.println(y_val);
			tx = tlb.Remap((float)i, (float)0, (float)30, 105, 720);
			ty = tlb.Remap((float)y_val, (float)0 , (float)(1) ,575, 80);
			
			rg.Draw_Circle((int)tx, (int)ty, 1, CSET.Red,"Fill");
			//data_plot.Pixel_Matrix[(int)ty][(int)tx] = new Pixel(CSET.Red);

		
		}
		
		rg.Commint_Matrix_Changes();
		rg.Show_Image();
		
    }
    
    
}
