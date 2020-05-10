package JBrain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import JSipl.Matrix;
import JSipl.Point;

public class CSV_File {
	public ArrayList<Column> Data;
	public 	int Number_Of_Rows;
	public 	int Number_Of_Columns;
	public CSV_File() {
		this.Data = new ArrayList<Column>();
	}



	
		
	/**
	 * 
	 * The method will import the data from the specified csv file
	 * into the calling Java_Brain instance and render it as ready to use
	 * in any of the models.
	 *  
	 * @param
	 * CSV_PATH : A String instance which contains the path for the file ,valid 
	 * path my be local paths on your system such as /bin/myfile.csv
	 * or a URL of the files location for example https://www.mysource.com/files/data.csv
	 * 
	 * 
	 * */
	public void Load_CSV_File(String CSV_PATH) throws IOException {
		 try {
			 ArrayList<ArrayList<String> > CSV_DATA = new  ArrayList<ArrayList<String> >();
			 this.Data = new ArrayList<Column>();
			 if(CSV_PATH.contains("www.") || CSV_PATH.contains("http:")|| CSV_PATH.contains("https:")) {
				    URL url = new URL(CSV_PATH);
				    BufferedReader read = new BufferedReader(
				    new InputStreamReader(url.openStream())); 
					 String line;
					 int max_number_of_Columns = 0;
					 int j =0;
					 line = read.readLine();
					  String[] Names = null;
					 if((line) != null) {
					       Names = line.split(",");
					        
					 }
					 
					    while ((line = read.readLine()) != null) {
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
						
						for(int i =0;i<this.Number_Of_Columns;i++) {
							ArrayList<String> col = new ArrayList<String>();
							int missing =0;
							for(int z =0;z<this.Number_Of_Rows;z++) {
								col.add(CSV_DATA.get(z).get(i));
								if (CSV_DATA.get(z).get(i).equals("")) {
									missing++;
								}
							}
							Column nc = new Column();
							nc.Values = col;
							nc.Column_Name = Names[i];
							nc.Column_Number = i+1;
							nc.Missing=missing;
							nc.Rescan_Info();
							this.Data.add(nc);
						}
						
						
			 }
			 else {
			 
		     BufferedReader  Reader = new BufferedReader(new FileReader(CSV_PATH));
			 String line;
			 int max_number_of_Columns = 0;
			 int j =0;
			 line = Reader.readLine();
		      String[] Names = null;
			 if((line) != null) {
			        Names = line.split(",");
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
				for(int i =0;i<this.Number_Of_Columns;i++) {
					ArrayList<String> col = new ArrayList<String>();
					int missing =0;
					for(int z =0;z<this.Number_Of_Rows;z++) {
						col.add(CSV_DATA.get(z).get(i));
						if (CSV_DATA.get(z).get(i).equals("")) {
							missing++;
						}
					}
					Column nc = new Column();
					nc.Values = col;
					nc.Column_Name = Names[i];
					nc.Column_Number = i+1;
					nc.Missing=missing;
					nc.Rescan_Info();
					this.Data.add(nc);
				}
			    Reader.close();
			 }
		} catch (FileNotFoundException e) {
			System.out.println("Error In File Openning");
			e.printStackTrace();
		}
		 catch (IOException e) {
				System.out.println("Error In File Openning");
				e.printStackTrace();
			}
		 
	}
	/**
	 * 
	 * The method will export the data formed in the calling Java_Brain
	 * instance to a CSV file in the project folder
	 *  
	 * @param
	 * F_Name : A String instance which contains the name that will be given
	 * to the saved CSV file . ( there is no need to add ".csv" at the end of
	 * the file name)
	 * 
	 * 
	 * */
	public void Write_CSV(String F_Name) throws IOException {
			FileWriter csvWriter = new FileWriter(F_Name + ".csv");
				for(int i=0;i<this.Number_Of_Columns;i++) {
					csvWriter.append(Data.get(i+1).Column_Name);
					csvWriter.append(",");
				}
			    csvWriter.append("\n");
			for (int i =0;i<this.Number_Of_Rows;i++) {
			    csvWriter.append(String.join(",", this.Get_Row(i)));
			    csvWriter.append("\n");
			}

			csvWriter.flush();
			csvWriter.close();
		
		}
		
	public	Column Get(int index) {
			if(index < 1 || index > this.Number_Of_Rows) {
				System.out.println("Error Invalid Index ");
				return null;
			}
			return this.Data.get(index-1);
		}	
	public	ArrayList<String> Get_Row(int Row_Number) {
		if (Row_Number < 1 || Row_Number > this.Number_Of_Rows) {
			System.out.println("==== Error -> [ Invalid Row ] ====");;
			return null;
		}
		ArrayList<String> Row = new  ArrayList<String>();
		for (int i = 0; i < this.Number_Of_Columns; i++) {
			Row.add(this.Get(Row_Number, i));
		}
		return Row;
		}
	/**
	 * The method will add a new row of '0' to the csv data loaded in the calling Java_Brain instance
	 * */
	public	void Add_Row() {
		this.Number_Of_Rows++;
		for(int i=0;i<this.Number_Of_Columns;i++) {
			this.Data.get(i).Values.add("0");
		}
	}
	/**
	 * The method will add a new row containing the values in the ArrayList passed to the method 
	 * please keep in mind that if the length of the row is longer then the amount of columns
	 * Currently loaded in the Java_Brain instances then the method will be aborted.
	 * @param
	 * Row_Values :  An ArrayList of strings representing the values to be added to the new row
	 * */
	public	void Add_Row(ArrayList<String> Values) {
		if(Values.size()!=this.Number_Of_Columns) {
			System.out.println("Aborted...\nRow Length Exceeds The Amount Of Registerd Columns ");
		}
		this.Number_Of_Rows++;
		for(int i =0;i<this.Number_Of_Columns;i++) {
			this.Data.get(i).Values.add(Values.get(i));
		}
	}
	/**
	 * The method will add a new column with the name specified in the parameters to the loaded 
	 * Csv file inside the calling Java_Brain instance.
	 * the new column will be by default filled with zeros.
	 * @param
	 * Column_Name : the name that will be given to the column.
	 * */
	public void Add_Column(String Column_Name) {
		this.Number_Of_Columns++;
		this.Data.add(new Column());
		this.Data.get(Data.size()-1).Column_Name = Column_Name;
		for(int i=0;i<this.Number_Of_Rows;i++) {
			this.Data.get(Data.size()-1).Values.add("0");
		}
	}
	/**
	 * The method will add a new column with the name specified in the parameters to the loaded 
	 * Csv file inside the calling Java_Brain instance.
	 * the new column will contain the values in the ArrayList of strings passed as a parameter to this method 
	 * if there is a smaller amount of values in the passed ArrayList then the amount of current rows in the file
	 * all missing values from the end of passed ArrayList up to the amount of rows in the loaded file will be filled with zeros.
	 * @param
	 * Column_Name : the name that will be given to the column.
	 * @param
	 * Column_Values : the values that will in the newly created column.
	 * */
	public	void Add_Column(String Column_Name, ArrayList<String> Values) {
		this.Number_Of_Columns++;
		this.Data.add(new Column());
		this.Data.get(Data.size()-1).Column_Name=Column_Name;
		if(Values.size()>this.Number_Of_Rows) {
			int i =1;
			while(i<=this.Number_Of_Rows) {
				for( i=1;i<=this.Number_Of_Rows;i++) {
					this.Set(i, Number_Of_Columns-1, Values.get(i-1));
				}
			}
			
			int n_of_rows_needed =Values.size()-Number_Of_Rows;
			int cr = this.Number_Of_Rows;
			for(int j=0;j<=n_of_rows_needed-1;j++) {
				this.Add_Row();
			}

			
			for(i=cr;i<Values.size();i++) {
				this.Set(i+1, Number_Of_Columns, Values.get(i));
				
			}
			
			
		}
		else {
			for(int i=1;i<=this.Number_Of_Rows;i++) {
				this.Data.get(i-1).Values.add(Values.get(i-1));
			}
		}
	}
	/**
	 * The method will remove from the loaded data the entire row corresponding  to the number passed to the method
	 * @param
	 * Row_Number : The number of the row which the method should remove
	 * */
	public	void Remove_Row(int Row_Number) {
		if(Row_Number > this.Number_Of_Rows) {
			System.out.println("No Such Row Number In File");
			return;
		}
		
		for(int i =0 ;i<this.Number_Of_Columns;i++) {
			this.Data.get(i).Values.remove(Row_Number-1);
		}
		this.Number_Of_Rows--;
	}
	public void Remove_Row(int From_Row_Number, int To_Row_Number) {
		for(int i = From_Row_Number;i<=To_Row_Number;i++) {
			this.Remove_Row(i);
		}
	}
	/**
	 * The method will remove from the loaded data the entire column corresponding  to the number passed to the method
	 * @param
	 * Column_Number : The number of the column which the method should remove
	 * */
	public	void Remove_Column(int Column_Number) {
		if(Column_Number > this.Number_Of_Columns) {
			System.out.println("No Such Column Number In File");
			return;
		}
		this.Data.remove(Column_Number);
		this.Number_Of_Columns--;

	}
	/**
	 * The method will print to ide console all the column information of the loaded csv file in the calling
	 * Java_Brain instance
	 * for each column the following information will be printed:
	 * Column Number , Column Name , Column Type (if categorical then the categories will be printed as well),
	 * Number of missing values in the column.
	 * */
	public void Print_Column_Info() {
		for(int i=1;i<=this.Number_Of_Columns;i++) {
			System.out.println("=============================");
			System.out.print("Column Number: [" + this.Data.get(i-1).Column_Number + "]\n");
			System.out.print("Column Name: [" + this.Data.get(i-1).Column_Name + "]\n");
			System.out.print("Column Type: [" + this.Data.get(i-1).Get_Column_Type()+ "]\n");
			if(this.Data.get(i-1).Column_Type ==1) {
				System.out.print("Categories: ");
				ArrayList<String> cat = Data.get(i-1).Get_Categories();
				for(int z = 0 ;z < cat.size();z++) {
					System.out.print("{" + cat.get(z) + "} ");
				}
				System.out.print("\n");
			}
			System.out.print("Column Missing Values: [" + this.Data.get(i-1).Missing+ "]");
			System.out.println("\n=============================\n");

			
		}
	}
	/**
	 * the method will return a String instance that matches the value
	 * at the specified row and column
	 * 
	 * @param
	 * Row : the row of the desired value
	 * @param
	 * Column : the column of the desired value
	 * */
	public String Get(int Row,int Column) {
		if(Row < 1 || Row > this.Number_Of_Rows || Column < 1 || Column > this.Number_Of_Columns) {
			System.out.println("Error - Invalid Parameters");
		}
		return this.Data.get(Column-1).Get(Row);
	}
	/**
	 * the method will set the passed argument String to the specified position
	 * as long as the specified position is  in range
	 *  [1 - total number of rows /cols ]
	 * @param
	 * Row : the row of the desired position
	 * @param
	 * Column : the column of the desired position
	 * @param
	 * Value : a string instance which contains the Value that will be 
	 * set in the specified position
	 * */
	public void Set(int Row,int Column,String Value) {
		if(Row < 1 || Row > this.Number_Of_Rows || Column < 1 || Column > this.Number_Of_Columns) {
			System.out.println("Error - Invalid Parameters");
		}
		this.Data.get(Column-1).Values.set(Row-1, Value);
	}
	/**
	 * the method splits the data in the calling Java_Brain instance into two parts
	 * returning the amount stated in the function call parameter .
	 * Example: if Split_Percentage = 30, then 70% of the data will be left as is in
	 * the current Java_Brain instance and from the 70% mark till the end will be
	 * removed and returned as a new Java_Brain instance containing the removed data.
	 * @param 
	 * Split_Percentage : the percentage of data that will be disconnected.
	 * @return
	 *  A new Java_Brain instance containing the removed data.
	 * 
	 * */
	public	CSV_File Split_Data(int Split_Percentage) {
		if(Split_Percentage > 100) {
			System.out.println("Cannot Split Into: " + (100 - Split_Percentage) +"%  /  " + Split_Percentage + "% ");
			return null;
		}
		
		CSV_File Split = new CSV_File();
		int number_of_rows;
		number_of_rows = (int) (((double)this.Number_Of_Rows/100)*Split_Percentage);
		Split.Number_Of_Rows = number_of_rows+1;
		Split.Number_Of_Columns = this.Number_Of_Columns;
		
		
		Split.Data = new ArrayList<Column>();
		
		for(int i=0;i<this.Number_Of_Columns;i++) {
			Split.Add_Column(this.Data.get(i).Column_Name);
		}
				
		for(int i = this.Number_Of_Rows-number_of_rows;i<=this.Number_Of_Rows;i++) {
			Split.Add_Row(this.Get_Row(i));
		}
		
		int j;
		j=Number_Of_Rows-number_of_rows;
		for(int i = this.Number_Of_Rows-number_of_rows;i<this.Number_Of_Rows;i++) {
			this.Remove_Row(i);
		}
		
		this.Number_Of_Rows -=number_of_rows;
		return Split;
	}
	/**
	 * The method will calculate the covariance of the specified numeric column
	 * @param
	 * Column_Number : The number of the column on which the method should run.
	 * @return
	 * The Covariance value of the selected column
	 * */
	public double Get_Column_Covariance(int Column_X,int Column_Y) {
		double covar = 0;
		double x_mean = this.Data.get(Column_X-1).Column_Mean();
		double y_mean = this.Data.get(Column_Y-1).Column_Mean();
		for(int i=1;i<=this.Number_Of_Rows;i++) {
			covar += (Double.parseDouble(this.Get(i, Column_X)) - x_mean)*(Double.parseDouble(this.Get(i, Column_Y)) - y_mean);
		}
		return covar/(this.Number_Of_Rows-1);
	}
	/**
	 * The method will remove all the rows with a missing value in the loaded csv data 
	 * */
	public	void Remove_Rows_With_Missing_Values() {
		String ms;
		ArrayList<Integer> Miss = new ArrayList<Integer>();
		
		for(int j=1;j<=this.Number_Of_Columns;j++) {
			for(int i = 1; i<=this.Number_Of_Rows;i++) {
				ms = this.Get(i, j);
				if(ms.equals("")) {
					Miss.add(i+1);
				}
			}
		}
		if(Miss.size() == 0) 
		{
			return;
		}else
		{
		LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(Miss);
        ArrayList<Integer> listWithoutDuplicates = new ArrayList<>(hashSet);
        
      
        for(int i=0;i<listWithoutDuplicates.size();i++) {
        	//System.out.println(listWithoutDuplicates.get(i)-(i));
        	this.Remove_Row(listWithoutDuplicates.get(i)-(i+1));
        }
        
       			
	}
	}
	/**
	 * The method will search the entire column and find the pattern/value specified and replace it with the replacement token
	 * passed to the method.
	 * you can use "" as the pattern for blank values in the csv for example
	 * and replace them with 0 or any value that suits your needs
	 * @param
	 * Column_Number : The number of the column on which the method should run
	 * @param
	 * Pattern : A string of the pattern or value that should be replaced
	 * @param
	 * Replace_With : A string of the pattern or value that will be replaced instead of the old pattern
	 * */
	public void Replace_Pattern_In_Column(int Column_Number,String Pattern,String Replace_With) {
		for(int i =1;i<=this.Number_Of_Rows;i++) {
			if(this.Get(i, Column_Number).equals(Pattern)) {
				this.Set(i, Column_Number, Replace_With);
			}
		}
		
	}
	public	int Number_Of_Missing() {
		int miss = 0;
		for(int i =0;i<this.Number_Of_Columns;i++) {
			miss+=this.Data.get(i).Missing;
		}
		return miss;
	}
	/**
	 * The method takes a Matrix instance and converts the matrix into a Java_Brain instances
	 * @param
	 * source : A Matrix instance which the method will convert
	 * @return
	 * A Java_Brain instance containing the values as well as the same shape as the matrix
	 * that was converted . meaning the same amount of rows and columns 
	 * */
	public static CSV_File Matrix_To_CSV(Matrix source) {
		CSV_File output = new CSV_File();
		int w = source.Cols;
		int h = source.Rows;
		for(int i =0;i<w;i++) {
			output.Add_Column(" ");
		}
		ArrayList<String> row = new ArrayList<String>();
		for(int i =0;i<h;i++) {
			for(int j=0;j<w;j++) {
				row.add(String.format("%f", source.Matrix_Body[i][j]));
			}
			output.Add_Row(row);
			row = new ArrayList<String>();
		}
		for(int i=0;i< source.Cols;i++) {
			output.Get(i+1).Column_Name= "Column "+ (i+1);
			output.Get(i+1).Column_Number = (i+1);
		}
		return output;
	}
	/**
	 * the method takes 2 numbers representing columns and inserts the numeric values 
	 * in those columns into an ArrayList of Point objects , each Point object in the 
	 * ArrayList represents a Row in the loaded data set and the 2 values of the columns passed to
	 * the function , meaning if the parameters (4,5) passed to the method,
	 * the ArrayList returned in any given index 'I' represents the 'I'th row of the loaded
	 * CSV file and the Point object at the 'I'th position will hold both values of the 4 and 5 column
	 * at that row. 
	 * @param
	 * Column_X : The number of the column that will be represented as the X value inside 
	 * each Point Instances
	 * @param
	 * Column_Y : The number of the column that will be represented as the Y value inside 
	 * each Point Instances
	 * @return
	 * An ArrayList of Point objects 
	 * 
	 * */
	public ArrayList<Point> Column_To_Point_List(int Column_X,int Column_Y){
		ArrayList<String> X = this.Data.get(Column_X-1).Values;
		ArrayList<String> Y = this.Data.get(Column_Y-1).Values;
		ArrayList<Point> p = new ArrayList<Point>();
		for(int i =0;i<this.Number_Of_Rows;i++) {
			p.add(new Point(Double.parseDouble(X.get(i)),Double.parseDouble(Y.get(i)),0));
		}
		return p;
		
	}
	/**
	 * the method takes 2 ArrayLists representing columns and inserts the numeric values 
	 * in those columns into an ArrayList of Point objects , each Point object in the 
	 * ArrayList represents a Row in the loaded data set and the 2 values of the columns passed to
	 * the function , meaning if the parameters (4,5) passed to the method,
	 * the ArrayList returned in any given index 'I' represents the 'I'th row of the loaded
	 * CSV file and the Point object at the 'I'th position will hold both values of the 4 and 5 column
	 * at that row. 
	 * @param
	 * X_Values : An ArrayList of Strings that holds the values of the column that will set as the X value inside 
	 * each Point Instances
	 * @param
	 * Y_Values : An ArrayList of Strings that holds the values of the column that will set as the Y value inside 
	 * each Point Instances
	 * @return
	 * An ArrayList of Point objects 
	 * 
	 * */
	public static ArrayList<Point> Column_To_Point_List(ArrayList<String> X_Values, ArrayList<String> Y_Values){
		ArrayList<Point> p = new ArrayList<Point>();
		for(int i =0;i<X_Values.size();i++) {
			p.add(new Point(Double.parseDouble(X_Values.get(i)),Double.parseDouble(Y_Values.get(i)),0));
		}
		return p;
		
	}
	/**
	 * the method takes 3 numbers representing columns and inserts the numeric values 
	 * in those columns into an ArrayList of Point objects , each Point object in the 
	 * ArrayList represents a Row in the loaded data set and the 3 values of the columns passed to
	 * the function , meaning if the parameters (4,5,8) passed to the method,
	 * the ArrayList returned in any given index 'I' represents the 'I'th row of the loaded
	 * CSV file and the Point object at the 'I'th position will hold both values of the 4,5 and 8 column
	 * at that row. 
	 * @param
	 * Column_X : The number of the column that will be represented as the X value inside 
	 * each Point Instances
	 * @param
	 * Column_Y : The number of the column that will be represented as the Y value inside 
	 * each Point Instances
	 *  * @param
	 * Column_Z : The number of the column that will be represented as the Z value inside 
	 * each Point Instances
	 * @return
	 * An ArrayList of Point objects 
	 * 
	 * */
	public ArrayList<Point> Column_To_Point_List(int Column_X,int Column_Y,int Column_Z){
		ArrayList<String> X = this.Data.get(Column_X-1).Values;
		ArrayList<String> Y = this.Data.get(Column_Y-1).Values;
		ArrayList<String> Z = this.Data.get(Column_Z-1).Values;
		ArrayList<Point> p = new ArrayList<Point>();
		for(int i =0;i<this.Number_Of_Rows;i++) {
			p.add(new Point(Double.parseDouble(X.get(i)),Double.parseDouble(Y.get(i)),Double.parseDouble(Z.get(i))));
		}
		return p;
	}
	/**
	 * the method takes 3 ArrayLists representing columns and inserts the numeric values 
	 * in those columns into an ArrayList of Point objects , each Point object in the 
	 * ArrayList represents a Row in the loaded data set and the 3 values of the columns passed to
	 * the function , meaning if the parameters (4,5,8) passed to the method,
	 * the ArrayList returned in any given index 'I' represents the 'I'th row of the loaded
	 * CSV file and the Point object at the 'I'th position will hold both values of the 4,5 and 8 column
	 * at that row. 
	 * @param
	 * X_Values : An ArrayList of Strings that holds the values of the column that will set as the X value inside 
	 * each Point Instances
	 * @param
	 * Y_Values : An ArrayList of Strings that holds the values of the column that will set as the Y value inside 
	 * each Point Instances
	 *  @param
	 * Z_Values : An ArrayList of Strings that holds the values of the column that will set as the Y value inside 
	 * each Point Instances
	 * @return
	 * An ArrayList of Point objects 
	 * 
	 * */
	public static ArrayList<Point> Column_To_Point_List(ArrayList<String> X_Values, ArrayList<String> Y_Values,ArrayList<String> Z_Values){
		ArrayList<Point> p = new ArrayList<Point>();
		for(int i =0;i<X_Values.size();i++) {
			p.add(new Point(Double.parseDouble(X_Values.get(i)),Double.parseDouble(Y_Values.get(i)),Double.parseDouble(Z_Values.get(i))));
		}
		return p;
		
	}
	/**
	 * The method takes an arraylist of doubles and formats each one of them into a string 
	 * making an arraylist of strings  
	 * @param
	 * 	to_convert : An ArrayList of doubles needed to be converted to and ArrayList of strings
	 * @return
	 * 	An ArrayList of strings 
	 * */
	public static ArrayList<String> DoubleList_To_StringList(ArrayList<Double> to_convert){
		ArrayList<String> res= new ArrayList<String>();
		for(int i =0;i<to_convert.size();i++) {
			res.add(String.format("%f",to_convert.get(i)));
		}
		return res;
	}
	/**
	 * The method takes an arraylist of strings and parses each one of them into a double 
	 * making an arraylist of double  
	 * @param
	 * 	to_convert : An ArrayList of strings needed to be parsed to and ArrayList of doubles
	 * @return
	 * 	An ArrayList of doubles 
	 * */
	public static ArrayList<Double> StringList_To_DoubleList(ArrayList<String> to_convert){
		ArrayList<Double> res= new ArrayList<Double>();
		for(int i =0;i<to_convert.size();i++) {
			res.add(Double.parseDouble(to_convert.get(i)));
		}
		return res;
	}


}
