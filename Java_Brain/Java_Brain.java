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

import javax.swing.JButton;
import javax.swing.JFrame;



class Column{
	public int Type;
	// 0 = Numeric
	// 1 = Categorical
	// 2 = Text
	// 3 = Date Time
	public int Column_Number;
	public int Missing;
	public String Column_Name;
	public String[] Get_Categories() {
		if(Type != 1) {
			System.out.println("Column Is Not Categorical");
			return null;
		}else{
			return this.Categories;
		}
	}
	public String Get_Column_Type() {
		switch(Type) {
		case 0:
			return "Numeric";
		case 1:
			return "Categorical";
		case 2:
			return "Text";
		case 3:
			return "Date-Time";
		}
		return null;
	}
	public void Set_Categories(ArrayList<String> categories) {
		this.Categories = categories.toArray(new String[0]);
	}
	private String[] Categories;
}


public class Java_Brain {
	public static Math_Toolbox tlb = new Math_Toolbox();
	public static Color_Palette CSET = new Color_Palette();
	public static BufferedReader Reader;
	SPlot plot = new SPlot();
	protected ArrayList<ArrayList<String>> CSV_DATA;
	public ArrayList<Column> Column_Info;

	public int Number_Of_Rows=0,Number_Of_Columns=0;
	public Java_Brain() {
		CSV_DATA = new ArrayList<ArrayList<String>>();
		this.Column_Info = new ArrayList<Column>();
	}
	
	//Data Set Handling
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
			 if(CSV_PATH.contains("www.") || CSV_PATH.contains("http:")|| CSV_PATH.contains("https:")) {
				    URL url = new URL(CSV_PATH);
				    BufferedReader read = new BufferedReader(
				    new InputStreamReader(url.openStream())); 
					 String line;
					 int max_number_of_Columns = 0;
					 int j =0;
					 line = read.readLine();
					 if((line) != null) {
						 Column_Info = new ArrayList<Column>();
					        String[] values = line.split(",");
					        for(int i = 0;i<values.length;i++) {
					        	Column_Info.add(new Column());
					        	Column_Info.get(Column_Info.size()-1).Column_Name=values[i];
					        }
					        
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
						
						for(int z=1;z<=this.Number_Of_Columns;z++) {
							this.Refresh_Column_Info(z);
						}
			 }
			 else {
			 
			Reader = new BufferedReader(new FileReader(CSV_PATH));
			 String line;
			 int max_number_of_Columns = 0;
			 int j =0;
			 line = Reader.readLine();
			 if((line) != null) {
				 Column_Info = new ArrayList<Column>();
			        String[] values = line.split(",");
			        for(int i = 0;i<values.length;i++) {
			        	Column_Info.add(new Column());
			        	Column_Info.get(Column_Info.size()-1).Column_Name=values[i];
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
				for(int z=1;z<=this.Number_Of_Columns;z++) {
					this.Refresh_Column_Info(z);
				}
			    
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
				csvWriter.append(this.Column_Info.get(i).Column_Name);
				csvWriter.append(",");
			}
		    csvWriter.append("\n");
		for (int i =0;i<this.Number_Of_Rows;i++) {
		    csvWriter.append(String.join(",", CSV_DATA.get(i)));
		    csvWriter.append("\n");
		}

		csvWriter.flush();
		csvWriter.close();
	
	}
	/**
	 * The following method prints to the ide console all data in csv
	 * keep in mid that large csv file may not be as informative when
	 * printed to console
	 * 
	 */
	public void Print_CSV_Data() {
		
		Iterator<String> itr;
		for(int i=0;i<this.Column_Info.size();i++) {
			System.out.print(Column_Info.get(i).Column_Name+" ");
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
	/**
	 * the method will return an ArrayList of strings that
	 * match the values in the row specified
	 * @param
	 * Row_Number : a positive integer > 0 and < number of rows total in the
	 * CSV file
	 * 
	 * */
	public ArrayList<String> Get_Specific_Row(int Row_Number) {
		if(Row_Number < 0 || Row_Number > this.Number_Of_Rows) {
			System.out.println("Invalid Row Number");
			return null;
		}
		return this.CSV_DATA.get(Row_Number-1);
	}
	/**
	 * the method will return an ArrayList of strings that
	 * match the values in the column specified
	 * @param
	 * Column_Number : a positive integer > 0 and < number of columns total in the
	 * CSV file
	 * 
	 * */
	public ArrayList<String> Get_Specific_Column(int Column_Number){
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
	/**
	 * the method will return a String instance that matches the value
	 * at the specified row and column
	 * 
	 * @param
	 * Row : the row of the desired value
	 * @param
	 * Column : the column of the desired value
	 * */
	public String CSV_Get_Value(int Row,int Column) {
		if(Row > this.Number_Of_Rows || Column > this.Number_Of_Columns || 
			Row < 0 || Column < 0 ){
				System.out.println("Error In Value Location");
				return null;
		}
		return CSV_DATA.get(Row-1).get(Column-1);
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
	public void CSV_Set_Value(int Row,int Column,String Value) {
		if(Row > this.Number_Of_Rows || Column > this.Number_Of_Columns || 
			Row < 0 || Column < 0 ){
				System.out.println("Error In Value Location");
				return;
		}
		 CSV_DATA.get(Row-1).set(Column-1, Value);
		 
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
		
		Split.Column_Info = new ArrayList<Column>();
		for(int i=0;i<this.Column_Info.size();i++) {
			Split.Column_Info.add(this.Column_Info.get(i));
		}
		Split.CSV_DATA = new ArrayList<ArrayList<String>>();
		
		for(int i = this.Number_Of_Rows-number_of_rows;i<=this.Number_Of_Rows;i++) {
			ArrayList<String> temp = new ArrayList<String>(this.Get_Specific_Row(i));
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
		ArrayList<String> X = this.Get_Specific_Column(Column_X);
		ArrayList<String> Y = this.Get_Specific_Column(Column_Y);
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
	public ArrayList<Point> Column_To_Point_List(ArrayList<String> X_Values, ArrayList<String> Y_Values){
		ArrayList<Point> p = new ArrayList<Point>();
		for(int i =0;i<this.Number_Of_Rows;i++) {
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
		ArrayList<String> X = this.Get_Specific_Column(Column_X);
		ArrayList<String> Y = this.Get_Specific_Column(Column_Y);
		ArrayList<String> Z = this.Get_Specific_Column(Column_Z);
		ArrayList<Point> p = new ArrayList<Point>();
		for(int i =0;i<this.Number_Of_Rows;i++) {
			p.add(new Point(Double.parseDouble(X.get(i)),Double.parseDouble(Z.get(i)),Double.parseDouble(Z.get(i))));
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
	private int Determinate_Column_Type(int Column_Number) {
		boolean numeric = true;
		ArrayList<String> Categories = new ArrayList<String>();
		//numeric test
		String tm;
		for(int i=1;i<=this.Number_Of_Rows;i++) {
			tm = this.CSV_Get_Value(i, Column_Number);
			if(!tm.matches("[-,+,0-9,.,0-9]+")) {
				if(!tm.equals("")) {
					numeric=false;
				}
			}
		}
		
		if(numeric == true) {
			return 0;
		}
		
		
	//date time test
		int num_of_dates =0;
		for(int i =1;i<=this.Number_Of_Rows;i++) {
			tm = this.CSV_Get_Value(i, Column_Number);
			if(tm.matches("[0-9/0-9/0-9]+")) {
				num_of_dates++;
			}
		}
		if(num_of_dates>0) {
			return 3;
		}
		
		Categories = this.Get_Specific_Column(Column_Number);
		//categorical test
		for(int i = 0;i<Categories.size();i++) {
			tm = Categories.get(i);
			if(!tm.equals("0") && !tm.equals("")) {
				for(int j = i+1;j<this.Number_Of_Rows;j++) {
					if(Categories.get(j).equals(tm)) {
						Categories.set(j, "0");
					}
				}
			}
		}
		int num_of_cat = 0;
		for(int i =0;i<Categories.size();i++) {
			if(!Categories.get(i).equals("0")){
				num_of_cat++;
			}
		}
		
		if(num_of_cat < (Categories.size()/2)+1) {
			return 1;
		}
		
	
		
		
		//else set as text
		return 2;
	}
	public void Refresh_Column_Info(int Column_Number) {
		Column t = this.Column_Info.get(Column_Number-1);
		t.Type = this.Determinate_Column_Type(Column_Number);
		if(t.Type==1) {
			ArrayList<String> Categories = new ArrayList<String>();
			String tm;
			Categories = this.Get_Specific_Column(Column_Number);
			//categorical test
			for(int i = 0;i<Categories.size();i++) {
				tm = Categories.get(i);
				if(!tm.equals("0") && !tm.equals("")) {
					for(int j = i+1;j<this.Number_Of_Rows;j++) {
						if(Categories.get(j).equals(tm)) {
							Categories.set(j, "0");
						}
					}
				}
			}
			ArrayList<String> Z = new ArrayList<String>();
			Z.add("0");
			Categories.removeAll(Z);
			t.Set_Categories(Categories);
			
		}
		t.Column_Number = Column_Number;
		t.Missing = this.Number_Of_Missing(Column_Number);
		
		
	}
	public void Print_CSV_Column_Info() {
		for(int i=1;i<=this.Number_Of_Columns;i++) {
			System.out.println("=============================");
			System.out.print("Column Number: [" + this.Column_Info.get(i-1).Column_Number + "]\n");
			System.out.print("Column Name: [" + this.Column_Info.get(i-1).Column_Name + "]\n");
			System.out.print("Column Type: [" + this.Column_Info.get(i-1).Get_Column_Type()+ "]\n");
			if(this.Column_Info.get(i-1).Type ==1) {
				System.out.print("Categories: ");
				String[] cat = Column_Info.get(i-1).Get_Categories();
				for(int z = 0 ;z < cat.length;z++) {
					System.out.print("{" + cat[z] + "} ");
				}
				System.out.print("\n");
			}
			System.out.print("Column Missing Values: [" + this.Column_Info.get(i-1).Missing+ "]");
			System.out.println("\n=============================\n");

			
		}
	}
	public void Add_Column(String Column_Name) {
		this.Number_Of_Columns++;
		this.Column_Info.add(new Column());
		this.Column_Info.get(this.Column_Info.size()-1).Column_Name = Column_Name;
		for(int i=1;i<=this.Number_Of_Rows;i++) {
			this.CSV_DATA.get(i-1).add("0");
		}
		this.Refresh_Column_Info(this.Column_Info.size());
	}
	public void Add_Column(String Column_Name,ArrayList<String> Column_Values) {
		this.Number_Of_Columns++;
		this.Column_Info.add(new Column());
		this.Column_Info.get(this.Column_Info.size()-1).Column_Name = Column_Name;
		if(Column_Values.size()>this.Number_Of_Rows) {
			int i =1;
			while(i<=this.Number_Of_Rows) {
				for( i=1;i<=this.Number_Of_Rows;i++) {
					this.CSV_DATA.get(i-1).add(Column_Values.get(i));
				}
			}
			
			int n_of_rows_needed =Column_Values.size()-Number_Of_Rows;
			int cr = this.Number_Of_Rows;
			for(int j=0;j<=n_of_rows_needed-1;j++) {
				this.Add_Row();
			}

			
			for(i=cr;i<Column_Values.size();i++) {
				this.CSV_Set_Value(i+1, Number_Of_Columns, Column_Values.get(i));
				
			}
			
			
		}
		else {
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
		this.Column_Info.remove(Column_Number-1);
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
	public static Java_Brain Matrix_To_CSV(Matrix source) {
		Java_Brain output = new Java_Brain();
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
		
		return output;
	}
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
		result.Add_Column("X", X_Vals);
		result.Add_Column("Y", Y_Vals);
		return result;
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
	public double Get_Column_Covariance(int Column_X,int Column_Y) {
		double covar = 0;
		double x_mean = this.Get_Column_Mean(Column_X);
		double y_mean = this.Get_Column_Mean(Column_Y);
		for(int i=1;i<=this.Number_Of_Rows;i++) {
			covar += (Double.parseDouble(this.CSV_Get_Value(i, Column_X)) - x_mean)*(Double.parseDouble(this.CSV_Get_Value(i, Column_Y)) - y_mean);
		}
		return covar/(this.Number_Of_Rows-1);
	}
	public double Get_Max_Value_In_Column(int Column_Number) {
		ArrayList<String> col = this.Get_Specific_Column(Column_Number);
		double max = Double.MIN_VALUE;
		for(int i =0;i<this.Number_Of_Rows;i++) {
			double cur_val = Double.parseDouble(col.get(i));
			if(max < cur_val) {
				max = cur_val;
			}
		}
		return max;
	}
	public double Get_Min_Value_In_Column(int Column_Number) {
		ArrayList<String> col = this.Get_Specific_Column(Column_Number);
		double min = Double.MAX_VALUE;
		for(int i =0;i<this.Number_Of_Rows;i++) {
			double cur_val = Double.parseDouble(col.get(i));
			if(min > cur_val) {
				min = cur_val;
			}
		}
		return min;
	}
	public void Fill_Missing_With_Column_Mean(int Column_Number) {
		double mean =0;
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for(int i=1;i<=this.Number_Of_Rows;i++) {
			String val = this.CSV_Get_Value(i, Column_Number);
			if(!val.equals("")) {
				mean+=Double.parseDouble(val);
			}else {
				positions.add(i);
			}
		}
		mean /= this.Number_Of_Rows;
		String smean = String.format("%.4f", mean);
		for(int i =0; i<positions.size();i++) {
			this.CSV_Set_Value(positions.get(i), Column_Number, smean);
		}
	}
	public void Remove_Rows_With_Missing_Values() {
			String ms;
			ArrayList<Integer> Miss = new ArrayList<Integer>();
			
			for(int j=1;j<=this.Number_Of_Columns;j++) {
				for(int i = 1; i<=this.Number_Of_Rows;i++) {
					ms = this.CSV_Get_Value(i, j);
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
	public void Fill_Missing_With_Column_Median(int Column_Number) {
		double median =0;
		double[] dt = new double[Number_Of_Rows];
		ArrayList<Integer> blanks = new ArrayList<Integer>();
		String sam ;
		for(int i = 1 ; i<=this.Number_Of_Rows;i++) {
			sam = this.CSV_Get_Value(i, Column_Number);
			if(!sam.equals("")) {
				dt[i-1] = Double.parseDouble(sam);

			}else {
				blanks.add(i);
			}
		}
		Arrays.sort(dt);
		if (dt.length % 2 == 0) {
			median = (double)(dt[dt.length / 2] + (double)dt[(dt.length / 2) - 1]) / 2;
		}
		else {

			median = dt[dt.length / 2];
		}
		String sm = String.format("%.4f", median);
		for(int i =0; i<blanks.size();i++) {
			this.CSV_Set_Value(blanks.get(i), Column_Number, sm);
		}
		
	}
	public void Fill_Missing_With_Pattern(int Column_Number,String Pattern) {
		String sm;
		for(int i =1;i <= this.Number_Of_Rows;i++) {
			sm = this.CSV_Get_Value(i, Column_Number);
			if(sm.equals("")) {
				this.CSV_Set_Value(i, Column_Number, Pattern);
			}
		}
	}
	public int Number_Of_Missing(int Column_Number) {
		int miss =0;
		String ms;
		for(int i = 1; i<=this.Number_Of_Rows;i++) {
			ms = this.CSV_Get_Value(i, Column_Number);
			if(ms.equals("")) {
				miss++;
			}
		}
		return miss;
	}
	public int Number_Of_Missing() {
		int miss =0;
		for(int i = 1 ; i<=this.Number_Of_Columns;i++) {
			miss += this.Number_Of_Missing(i);
		}
		return miss;
	}
	public Java_Brain Compute_Column_Correlations(String Correlation_Type) {
		ArrayList<Integer> numeric = new ArrayList<Integer>();
		for(int i =1;i<=this.Number_Of_Columns;i++) {
			if(this.Column_Info.get(i-1).Type == 0) {
				numeric.add(i);
			}
		}
		if(numeric.size() < 2) {
			System.out.println("You Need Minimum 2 Numeric Columns To Find Correlations");
			return null;
		}
		Java_Brain Cors = new Java_Brain();
		
		int number_of_correlations = Math.round(((numeric.size())*(numeric.size()-1))/2);
		Cors.Add_Column("Between");
		Cors.Add_Column("Correlation");

		//Cors.Column_Names.add(Between);
		//Cors.Column_Names.add("Correlation");

		for(int i =0;i<number_of_correlations;i++) {
			Cors.Add_Row();
		}
	

		
		int pos =1;
		for(int i=0;i<numeric.size();i++) {
			for(int j=i+1;j<numeric.size();j++) {
				Cors.CSV_Set_Value(pos, 1, this.Column_Info.get(numeric.get(i)-1).Column_Name + " - " + this.Column_Info.get(numeric.get(j)-1).Column_Name);
				if(Correlation_Type.equals("Pearson")) { 
				Cors.CSV_Set_Value(pos, 2, String.format("%.5f",
						this.Get_Pearson_Correlation_Coefficient(
								this.Get_Specific_Column(numeric.get(i)), this.Get_Specific_Column(numeric.get(j)))));
				}
				else if(Correlation_Type.equals("Spearman")) {
					Cors.CSV_Set_Value(pos, 2, String.format("%.5f",
							this.Get_Spearmans_Correlation_Coefficient(
									this.Get_Specific_Column(numeric.get(i)), this.Get_Specific_Column(numeric.get(j)))));
					
					
				}else {
					System.out.println("Unknown Correlation Type Please Specify - Pearson/Spearman");
					return null;
				}
				pos++;
			}
		}
		
		
		return Cors;
	}

	
	
	
	//Algorithms
	public double Squared_Point_Distance(Point first, Point second) {
		return (first.x - second.x)*(first.x - second.x) + (first.y - second.y)*(first.y - second.y) + (first.z - second.z)*(first.z - second.z);
	}
	public Matrix Linear_Regression_Static_Formula(int X_Values_Column_Number ,int Y_Values_Column_Number) {
		ArrayList<String> X_Values = this.Get_Specific_Column(X_Values_Column_Number);
		ArrayList<String> Y_Values = this.Get_Specific_Column(Y_Values_Column_Number);

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
	private Matrix Step_Gradient(double Current_B,double Current_M,double Learning_Rate ,int X_COL,int Y_COL) {
		ArrayList<String> X_val = this.Get_Specific_Column(X_COL);
		ArrayList<String> Y_val = this.Get_Specific_Column(Y_COL);

		double b_gradient = 0;
		double m_gradient = 0;
		double x , y;
		for(int i =0;i<this.Number_Of_Rows;i++) {
			x = Double.parseDouble(X_val.get(i));
			y = Double.parseDouble(Y_val.get(i));
			m_gradient += ((double)2/Number_Of_Rows)*-x*(y - (Current_M * x + Current_B));
			b_gradient += ((double)2/Number_Of_Rows)*-(y - (Current_M * x + Current_B));
		}
		Matrix result = new Matrix(2,1);
		result.Matrix_Body[0][0] = Current_B - (Learning_Rate * b_gradient);
		result.Matrix_Body[1][0] =Current_M - (Learning_Rate * m_gradient);
		return result;
		
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
    public  double Get_Pearson_Correlation_Coefficient(ArrayList<String> Y ,ArrayList<String> Y_Hat) {
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
    public double[] Rankify(ArrayList<String> Y) throws NumberFormatException{
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
    public  double Get_Spearmans_Correlation_Coefficient(ArrayList<String> Y ,ArrayList<String> Y_Hat) {
    	double SCC = 0;
    	double rankY[]=this.Rankify(Y),rankYhat[]=this.Rankify(Y_Hat);
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
	public Matrix Confusion_Matrix(Matrix Regression_Weights,int Binary_Column,int[] Sampled_Rows,double Decision_Boundary ) {		
		Matrix CM = new Matrix(2,2);
		double TruePositive=0;
		double TrueNegative=0;
		double FalsePositive=0;
		double FalseNegative=0;
		for(int i =1;i<=this.Number_Of_Rows;i++) {
			double pred_s = Regression_Weights.Matrix_Body[0][0];
			for(int j=1;j<=Sampled_Rows.length;j++) {
				pred_s += Regression_Weights.Matrix_Body[j][0] *Double.parseDouble(this.CSV_Get_Value(i,Sampled_Rows[j-1]));
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
		CM.Matrix_Body[0][1] =FalseNegative;
		CM.Matrix_Body[1][1] =TrueNegative; 
		return CM;
	}
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
	public String KNN(int K ,double Test_Values[],int Sample_Columns[],int Result_Column) {
		double distances[] = new double[this.Number_Of_Rows];
		double x[];
		
		for(int i=1;i<=this.Number_Of_Rows;i++) {
			x = new double[Sample_Columns.length];
			for(int j=0;j<Sample_Columns.length;j++) {
				x[j] = Double.parseDouble(this.CSV_Get_Value(i, Sample_Columns[j]));
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
		 
		 String categories[] = this.Column_Info.get(Result_Column-1).Get_Categories();
		 int cats[] = new int[categories.length];
		 Arrays.fill(cats, 0);
		 for(int i=0;i<K;i++) {
			 String val = this.CSV_Get_Value(knn[i], Result_Column);
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
	public Matrix KNN(int K ,double Test_Values[],int Sample_Columns[]) {
		double distances[] = new double[this.Number_Of_Rows];
		double x[];
		 Matrix result = new Matrix(K,2);

		for(int i=1;i<=this.Number_Of_Rows;i++) {
			x = new double[Sample_Columns.length];
			for(int j=0;j<Sample_Columns.length;j++) {
				x[j] = Double.parseDouble(this.CSV_Get_Value(i, Sample_Columns[j]));
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
	public Matrix KNN(int K ,Java_Brain Test_Dataset,int Sample_Columns[]) {
		Matrix Final_Res = new Matrix(Test_Dataset.Number_Of_Rows,(1+K));
		double xv[] = new double[Sample_Columns.length];
		for(int i=1;i<=Test_Dataset.Number_Of_Rows;i++) {
			for(int j=0;j<xv.length;j++) {
				xv[j] = Double.parseDouble(Test_Dataset.CSV_Get_Value(i, Sample_Columns[j]));
			}
			Matrix res = this.KNN(K, xv, Sample_Columns);
			for(int j = 1 ; j <K+1;j++) {
				Final_Res.Matrix_Body[i-1][j] = res.Matrix_Body[j-1][0];	
			}

			Final_Res.Matrix_Body[i-1][0] = i;	

		}
		
		return Final_Res;
	}
    public Matrix Get_Variance_Covariance_Matirx(int[] input_Columns) {
    	
    	Matrix data = new Matrix(this.Number_Of_Rows,input_Columns.length);
    	Matrix dataOnes = new Matrix(this.Number_Of_Rows,1);
    	for(int i=0;i<this.Number_Of_Rows;i++) {
    			dataOnes.Matrix_Body[i][0]=1;
    		
    	}
    	Matrix dataOnestag = new Matrix(dataOnes);
    	dataOnestag.Matrix_Transpose();
    	for(int i=1;i<=this.Number_Of_Rows;i++) {
    		for(int j=0;j<input_Columns.length;j++) {
    			data.Matrix_Body[i-1][j] = Double.parseDouble(this.CSV_Get_Value(i, input_Columns[j]));
    		}
    	}
    	//transformation a = A - 11'x/n
    	dataOnestag = dataOnes.Dot_Product(dataOnestag);
    	dataOnestag = dataOnestag.Dot_Product(data);
    	dataOnestag.Divide(this.Number_Of_Rows);
    	data.Subtract(dataOnestag);
    	dataOnes = new Matrix(data);
    	dataOnes.Matrix_Transpose();
    	//(a*a')/n 
    	data = dataOnes.Dot_Product(data);
    	data.Divide(Number_Of_Rows);
    	
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

		for(int i=1;i<=this.Number_Of_Rows;i++) {
			//h0  guess
			Values.Matrix_Body[0][0]=1;
			for(int j =1;j<nof;j++) {
				Values.Matrix_Body[j][0] = Double.parseDouble(this.CSV_Get_Value(i, Columns_Of_Sampels[j-1]));
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
	public Matrix Linear_Regression_Gradient_Descent(int[] Sample_Columns,ArrayList<String> True_Y,double Leaning_Rate,int number_of_iterations) {
		//y = mx + b - for slope calculation
		Matrix LE = new Matrix(Sample_Columns.length+1,1);
		for(int i = 0;i<number_of_iterations;i++) {
			LE = this.Step_Gradient(LE , Leaning_Rate, Sample_Columns, True_Y);
		}
		return LE;
		
	}
	public Matrix Linear_Regression_Gradient_Descent(int X_Values_Column_Number,int Y_Values_Column_Number,double Leaning_Rate,int number_of_iterations) {
		
		Matrix LE = new Matrix(2,1);
		for(int i = 0;i<number_of_iterations;i++) {
			LE = this.Step_Gradient(LE.Matrix_Body[0][0], LE.Matrix_Body[1][0], Leaning_Rate, X_Values_Column_Number, Y_Values_Column_Number);
		}
		//LE.z = Compute_Error_For_Given_Points(X_values,Y_values,LE.y,LE.x);

		return LE;
		
	}
	public Matrix Logistic_Regression(int Value_Column_Numbers[],int Binary_Category_Number,int number_of_iterations,double learning_rate) {
		ArrayList<ArrayList<String>> Values = new ArrayList<ArrayList<String>>();
		for(int i=0;i<Value_Column_Numbers.length;i++) {
			Values.add(this.Get_Specific_Column(Value_Column_Numbers[i]));
		}
		ArrayList<String> Binary_Category = this.Get_Specific_Column(Binary_Category_Number);
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
	public void Validate_Linear_Regression(Matrix LR,int True_Column,int[] Samples_Column_Numbers){
		for(int i =1;i<=this.Number_Of_Rows;i++) {
			double pred = LR.Matrix_Body[0][0];
			for(int j=1;j<Samples_Column_Numbers.length+1;j++) {
				pred += LR.Matrix_Body[j][0]*Double.parseDouble(this.CSV_Get_Value(i, Samples_Column_Numbers[j-1]));
			}
			System.out.println("Predictions: " + pred + "  Actual "  + this.Get_Specific_Column(True_Column).get(i-1));
		}
	}
	public void Validate_Logistic_Regression(Matrix LR_Weights,int Binary_Column,int[] Sampled_Rows) {		
		for(int i =1;i<=this.Number_Of_Rows;i++) {
			double pred_s = LR_Weights.Matrix_Body[0][0];
			for(int j=1;j<=Sampled_Rows.length;j++) {
				pred_s += LR_Weights.Matrix_Body[j][0] *Double.parseDouble(this.CSV_Get_Value(i,Sampled_Rows[j-1]));
			}
			pred_s = this.Sigmoid(pred_s);
			
			System.out.print("\n====================\n");
			System.out.print("Values: ");
			for(int z =0;z<Sampled_Rows.length;z++) {
				System.out.print(this.CSV_Get_Value(i,Sampled_Rows[z]) + " ");

			}
			System.out.print("\n====================\n");

			System.out.println("Prediction: [" + pred_s + "] Actual: [" + this.CSV_Get_Value(i, Binary_Column)+"]");
			System.out.print("\n---------------------\n");

		}

			
	}
	public Matrix PCA(int[] Selected_Columns) {
		double means[] = new double[Selected_Columns.length];
		for(int i=0;i<Selected_Columns.length;i++) {
			means[i] = this.Get_Column_Mean(Selected_Columns[i]);
		}
    	Matrix data = new Matrix(this.Number_Of_Rows,Selected_Columns.length);

    	for(int i=1;i<=this.Number_Of_Rows;i++) {
    		for(int j=0;j<Selected_Columns.length;j++) {
    			data.Matrix_Body[i-1][j] = Double.parseDouble(this.CSV_Get_Value(i, Selected_Columns[j])) - means[j];
    		}
    	}
    	Java_Brain tocsv = Java_Brain.Matrix_To_CSV(data);
    	Matrix Var_CO_Var = tocsv.Get_Variance_Covariance_Matirx(Selected_Columns);
    	
    	Matrix EV = Var_CO_Var.Get_Eigen_Vectors();
    	
    	data = data.Dot_Product(EV);
    	data.print_Matrix();

    	
    	
		
		
		return data;
	}
	
	
	//Visual
	public void Plot_Linear_Regression(int X_Values_Column_Number,int Y_Values_Column_Number,String X_Name,String Y_Name) {
		Matrix LE = this.Linear_Regression_Static_Formula(X_Values_Column_Number, Y_Values_Column_Number);
		Math_Toolbox tlb = new Math_Toolbox();
		SPlot plt = new SPlot();
		ArrayList<Point> Data = this.Column_To_Point_List(this.Get_Specific_Column(X_Values_Column_Number), this.Get_Specific_Column(Y_Values_Column_Number));
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
    	ArrayList<Point> Data = this.Column_To_Point_List(Column_X, Column_Y);
    	plot.Show_Scatter_Plot(Data, X_Name, Y_Name);
    }
    public void Plot_Gradient_Descent(int X_values_column,int Y_values_column,double Leaning_Rate,int number_of_iterations) {
		ArrayList<String>  X_values = this.Get_Specific_Column(X_values_column);
		ArrayList<String>  Y_values = this.Get_Specific_Column(Y_values_column);

    	ArrayList<Point> points = new ArrayList<Point>();
		for(int i = 0 ;i<Number_Of_Rows;i++) {
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
			for(double p =0 ;p<=Max_X;p+=0.01) {
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
    public void Plot_Quick_KNN(int K ,double Test_Values[],int Sample_Columns[]) {
    	if(Sample_Columns.length>2) {
    		System.out.println("Quick Plot Supports Only 2 Sample Column Plots");
    	}
    	ArrayList<Point> data = this.Column_To_Point_List(Sample_Columns[0], Sample_Columns[1]);
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
    		double Nx = Double.parseDouble(this.CSV_Get_Value((int)Knn.Matrix_Body[i][0], Sample_Columns[0]));
    		double Ny = Double.parseDouble(this.CSV_Get_Value((int)Knn.Matrix_Body[i][0], Sample_Columns[1]));
			tx = tlb.Remap((float)Nx, (float)Min_X, (float)Max_X, 105, 720 );
			ty = tlb.Remap((float)Ny, (float)Min_Y, (float)Max_Y, 565, 80);

			Scatter_Plot.Draw_Circle((int)tx, (int)ty, 5, CSET.White_Smoke,"Fill");
			Scatter_Plot.Draw_Circle((int)tx, (int)ty, 4, CSET.Hot_Pink,"Fill");

    		
    	}
    	Scatter_Plot.Show_Image();
    	
    	
    }






}

class Image_Tools{
	public static Java_Brain Image_Pixels_To_CSV(char Channel,Image source) {
		Java_Brain output = new Java_Brain();
		for(int i=0;i<source.Image_Width;i++) {
			output.Add_Column("1x"+i);
		}
		output.Number_Of_Columns=source.Image_Width;
		ArrayList<String> Row = new ArrayList<String>();
		for(int i=0;i<source.Image_Height;i++) {
			for(int j=0;j<source.Image_Width;j++) {
				switch(Channel) {
				case 'R':
					Row.add(String.format("%d", source.Pixel_Matrix[i][j].r));
					break;
				case 'G':
					Row.add(String.format("%d", source.Pixel_Matrix[i][j].g));
					break;
				case 'B':
					Row.add(String.format("%d", source.Pixel_Matrix[i][j].b));
					break;
				}
			}
			output.Add_Row(Row);
			Row = new ArrayList<String>();
		}
		
		
		
		return output;
		
	}
	public static LabPixel[][] Get_Lab_Matrix(Image source){
		LabPixel[][] lab_mat = new LabPixel[source.Image_Height][source.Image_Width];
		
		for(int i=0;i<source.Image_Height;i++) {
			for(int j =0 ; j <source.Image_Width;j++) {
				lab_mat[i][j] = new LabPixel();
				lab_mat[i][j].RGB_to_LAB(source.Pixel_Matrix[i][j]);
			}
		}
		
		return lab_mat;
	}
	public static Java_Brain Lab_Matrix_To_Csv(char Channel,LabPixel[][] lab_matrix) {
		int w = lab_matrix[0].length;
		int h = lab_matrix.length;
		Java_Brain output = new Java_Brain();
		for(int i=0;i<w;i++) {
			output.Add_Column(String.format("0x%d",i ));
		}
		
		output.Number_Of_Columns=w;
		ArrayList<String> Row = new ArrayList<String>();
		for(int i=0;i<h;i++) {
			for(int j=0;j<w;j++) {
				switch(Channel) {
				case 'L':
					Row.add(String.format("%.5f", lab_matrix[i][j].L));
					break;
				case 'A':
					Row.add(String.format("%.5f", lab_matrix[i][j].A));
					break;
				case 'B':
					Row.add(String.format("%.5f",lab_matrix[i][j].B));
					break;
				}
			}
			output.Add_Row(Row);
			Row = new ArrayList<String>();
		}
		
		return output;
	}
	public static Image Lab_Matrix_To_RGB_Image(LabPixel lab_matrix[][]) {
		Image output = new Image();
		int w = lab_matrix[0].length;
		int h = lab_matrix.length;
		output.Load_Blank_Canvas(h, w, new Pixel(0,0,0));
		for(int i=0;i<h;i++) {
			for(int j = 0; j<w;j++) {
				output.Pixel_Matrix[i][j].LAB_to_RGB(lab_matrix[i][j]);
				output.Pixel_Matrix[i][j].Clamp_Outliers();
			}
		}
		output.Commint_Matrix_Changes();
		return output;
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
    public Neuron(double[] weights, double bias,double value){
        this.weights = weights;
        this.bias = bias;
        this.backprop_weights = this.weights;
        this.gradient = 0;
        this.value=value;
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
			neurons[i] = new Neuron(weights,1,Math_Toolbox.random_double_in_range(Neuron.minWeightValue, Neuron.maxWeightValue));
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
	
	public void Load_Columns_As_Data(Java_Brain Data_Set,int[] Data_Column_Number,int Result_Column[]) {
		this.Data = new Data_Cartridge[Data_Set.Number_Of_Rows];
		for(int i=0;i<Data.length;i++) {
			double row_values[] = new  double[Data_Column_Number.length];
			double result_values[] = new  double[Result_Column.length];

			for(int j =0;j<Data_Column_Number.length;j++) {
				row_values[j] = Double.parseDouble(Data_Set.CSV_Get_Value(i+1, Data_Column_Number[j]));
			}
			
			for(int q =0;q<Result_Column.length;q++) {
				result_values[q] = Double.parseDouble(Data_Set.CSV_Get_Value(i+1, Result_Column[q]));
			}
			this.Data[i] = new Data_Cartridge(row_values,result_values);
			
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
	Neuron_Layer[] Layers;
	Training_Data Training_Data;
	boolean Sigmoid = true;
	boolean Tanh = false;
	boolean Relu = false;
	//for model constructing and reconstructing 
	int[] Topology_Of_Neurons;
	double Learning_Rate;
	double Min_Weight;
	double Max_Weight;
	
	public Neural_Net(int[] Topology_Of_Neurons,Training_Data Training_Data,double Learning_Rate,double Min_Weight,double Max_Weight) {
		Neuron.setRangeWeight(Min_Weight, Max_Weight);
		Layers = new Neuron_Layer[Topology_Of_Neurons.length/2 + 1];
		Layers[0] = null;
		int j =1;
		for(int i=0;i<Topology_Of_Neurons.length;i+=2) {
			Layers[j] = new Neuron_Layer(Topology_Of_Neurons[i],Topology_Of_Neurons[i+1]);
			j++;
		}
		
		this.Training_Data=Training_Data;
		this.Learning_Rate=Learning_Rate;
		this.Min_Weight=Min_Weight;
		this.Max_Weight=Max_Weight;
		this.Topology_Of_Neurons = Topology_Of_Neurons;
	}
	public Neural_Net(String Brain_File_Saved_Model) throws IOException {
		DataInputStream in;
		if(!Brain_File_Saved_Model.contains(".Brain")) {
			System.out.println("Unkown File Format Specifed, Please Use Only Java_Brain Supported Formats");
			return;
		}
		try {
			int sampler;
			in = new DataInputStream(new FileInputStream(Brain_File_Saved_Model));
			this.Learning_Rate = in.readDouble();
			this.Max_Weight = in.readDouble();
			this.Min_Weight = in.readDouble();
			sampler = in.readInt();
			this.Topology_Of_Neurons=new int[sampler];
			for(int i=0;i<sampler;i++) {
				this.Topology_Of_Neurons[i] = in.readInt();
			}
			
			Neuron.setRangeWeight(Min_Weight, Max_Weight);
			Layers = new Neuron_Layer[Topology_Of_Neurons.length/2 + 1];
			Layers[0] = null;
			int j =1;
			for(int i=0;i<Topology_Of_Neurons.length;i+=2) {
				Layers[j] = new Neuron_Layer(Topology_Of_Neurons[i],Topology_Of_Neurons[i+1]);
				j++;
			}
			
			//reading layer structure
			
			for(int layer=1;layer<this.Layers.length;layer++) {
				for(int ners =0;ners<this.Layers[layer].neurons.length;ners++) {
					
					this.Layers[layer].neurons[ners].bias = in.readDouble();
					this.Layers[layer].neurons[ners].gradient = in.readDouble();
					this.Layers[layer].neurons[ners].value = in.readDouble();

					this.Layers[layer].neurons[ners].backprop_weights = new double[in.readInt()];
					double backprop =  this.Layers[layer].neurons[ners].backprop_weights.length;
					for(int m = 0 ; m <backprop;m++) {
						this.Layers[layer].neurons[ners].backprop_weights[m] = in.readDouble();
					}
					
					this.Layers[layer].neurons[ners].weights = new double[in.readInt()];
					double weight = this.Layers[layer].neurons[ners].weights.length;
					for(int m = 0 ; m <weight;m++) {
						this.Layers[layer].neurons[ners].weights[m]=in.readDouble();
					}



					
				}
			}	
			
	
				
			
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	};
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
	        		sum += Layers[i].neurons[j].bias; // add in the bias 
	        		
	        		if(Sigmoid == true) {
		        		Layers[i].neurons[j].value = Math_Toolbox.Sigmoid(sum);
	        		}else if(Tanh == true) {
		        		Layers[i].neurons[j].value = Math.tanh(sum);

	        		}else if(Relu==true) {
	        			Layers[i].neurons[j].value  = Math_Toolbox.Rectified(sum);
	        		}else {
	        			System.out.println("No Activaition Function Selected");
	        		}
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
			double derivative = 0;
			double delta = 0;

	    	// Update the output layers 
	// For each output
	for(int i = 0; i < Layers[out_index].neurons.length; i++) {
		// and for each of their weights
		double output = Layers[out_index].neurons[i].value;
		double target = training_Data.Expected_Output[i];

		
		
		if(Sigmoid == true) {
			derivative = (output-target);
			delta = derivative*(output*(1-output));
		}else if(Tanh == true) {
			derivative = (output-target);
			 delta = derivative*(1 - output*output);
		}else if(Relu==true) {
			if(output >=0) {
				delta = (output-target);
			}else {
				delta =(output-target)*0.01;
			}
		}
			 

	
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
			
		
			
			
			if(Sigmoid == true) {
				 delta = gradient_sum*(output*(1-output));
    		}else if(Tanh == true) {
    			delta = gradient_sum*(1 - output*output);

    		}else if(Relu==true) {
    			if(output >=0) {
    				delta = gradient_sum;
    			}else {
    				delta =gradient_sum*0.01;
    			}
    		}
		
		
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
	 public  void Start_Training(int Training_Iterations) {
	    	for(int i = 0; i < Training_Iterations; i++) {
	    		for(int j = 0; j < Training_Data.Data.length; j++) {
	    			Forward_Data(Training_Data.Data[j].Data);
	    			Back_Propagate(Training_Data.Data[j]);
	    		}
	    	}
	    }
	 public	 double[] Get_Output_Values() {
		 double[] results = new double[this.Layers[Layers.length-1].neurons.length];
		 for(int m = 0;m < this.Layers[Layers.length-1].neurons.length;m++) {
			 results[m] = this.Layers[Layers.length-1].neurons[m].value;
		 }
		 return results;
	 }
	 public  void Print_Output_Neurons_Values() {
		 	System.out.println("============");
	System.out.println("   Output");
	System.out.println("============");
		        	for(int j=0;j<Layers[Layers.length-1].neurons.length;j++) {
			            System.out.println(Layers[Layers.length-1].neurons[j].value);
		        }
	 }
	 public  void Save_Model(String Model_Name) {
		 
		 File model = new File(Model_Name);
		 try (FileOutputStream fos = new FileOutputStream(model+".Brain");
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					DataOutputStream dos = new DataOutputStream(bos)) {
				dos.writeDouble(this.Learning_Rate);
				dos.writeDouble(this.Max_Weight);
				dos.writeDouble(this.Min_Weight);
				dos.writeInt(this.Topology_Of_Neurons.length);
				for(int i=0;i<Topology_Of_Neurons.length;i++) {
					dos.writeInt(this.Topology_Of_Neurons[i]);
				}
			
				
				for(int layer=1;layer<this.Layers.length;layer++) {
					for(int ners =0;ners<this.Layers[layer].neurons.length;ners++) {
						
						dos.writeDouble(this.Layers[layer].neurons[ners].bias);
						dos.writeDouble(this.Layers[layer].neurons[ners].gradient);
						dos.writeDouble(this.Layers[layer].neurons[ners].value);
						
						dos.writeInt(this.Layers[layer].neurons[ners].backprop_weights.length);
						double backprop =  this.Layers[layer].neurons[ners].backprop_weights.length;
						for(int m = 0 ; m <backprop;m++) {
							dos.writeDouble(this.Layers[layer].neurons[ners].backprop_weights[m]);
						}
						
						dos.writeInt(this.Layers[layer].neurons[ners].weights.length);
						double weight = this.Layers[layer].neurons[ners].weights.length;
						for(int m = 0 ; m <weight;m++) {
							dos.writeDouble(this.Layers[layer].neurons[ners].weights[m]);
						}



						
					}
				}
				
				
				
				
				
				System.out.println("Your Model Was Successfully Saved As: [" + Model_Name +".Brain]");
				dos.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	 }
	 public  void Visualise_Neural_Network() {
		Color_Palette CSET = new Color_Palette();
		Math_Toolbox tlb = new Math_Toolbox();
		Image VNET = new Image();
		int max_layer_size = tlb.get_array_max(Topology_Of_Neurons);
		int image_width;
		if(max_layer_size > this.Layers.length) {
			 image_width = (max_layer_size+1)*90 + 200;

		}else {
		 image_width = this.Layers.length*200 + 400;
		}
		
		VNET.Load_Blank_Canvas(image_width, image_width, CSET.White_Smoke);
		
		
		
		double p = tlb.Remap(0, 0, this.Topology_Of_Neurons.length, 100, image_width);
		double h = ((image_width-100)/(Topology_Of_Neurons[0]+1));
		double growth= h;
		
		for(int j=0;j<Topology_Of_Neurons[0];j++) {		
			
			double p2 = tlb.Remap(0+1, 0, this.Topology_Of_Neurons.length, 100, image_width);
			double h2 = ((image_width-100)/(Topology_Of_Neurons[0+1]+1));
			double growth2= h2;
			
			for(int k=0;k<Topology_Of_Neurons[0+1];k++) {
				VNET.Draw_Line((int)growth, (int)p, (int)growth2, (int)p2, CSET.Red);
				VNET.Draw_Line((int)growth-1, (int)p-1, (int)growth2+1, (int)p2+1, CSET.Red);
				VNET.Draw_Line((int)growth+1, (int)p+1, (int)growth2-1, (int)p2-1, CSET.Red);
				VNET.Draw_Line((int)growth-1, (int)p+1, (int)growth2-1, (int)p2+1, CSET.Red);
				VNET.Draw_Line((int)growth+1, (int)p-1, (int)growth2+1, (int)p2-1, CSET.Red);
				VNET.Draw_Line((int)growth+1, (int)p-1, (int)growth2-1, (int)p2-1, CSET.Red);
				VNET.Draw_Line((int)growth+1, (int)p-1, (int)growth2+1, (int)p2+1, CSET.Red);
				VNET.Draw_Line((int)growth-1, (int)p+1, (int)growth2+1, (int)p2+1, CSET.Red);
				VNET.Draw_Line((int)growth-1, (int)p+1, (int)growth2-1, (int)p2-1, CSET.Red);



				growth2+= h2;
				
			}
			
			
			growth+=h;

		}
		
		
		
		
		
		for(int i =1;i<this.Topology_Of_Neurons.length-2;i+=2) {
			 p = tlb.Remap(i, 0, this.Topology_Of_Neurons.length, 100, image_width);
			 h = ((image_width-100)/(Topology_Of_Neurons[i]+1));
			 growth= h;
			
		
			
			for(int j=0;j<Topology_Of_Neurons[i];j++) {				
				double p2 = tlb.Remap(i+2, 0, this.Topology_Of_Neurons.length, 100, image_width);
				double h2 = ((image_width-100)/(Topology_Of_Neurons[i+2]+1));
				double growth2= h2;
				
				for(int k=0;k<Topology_Of_Neurons[i+2];k++) {
					VNET.Draw_Line((int)growth, (int)p, (int)growth2, (int)p2, CSET.Red);
					VNET.Draw_Line((int)growth-1, (int)p-1, (int)growth2+1, (int)p2+1, CSET.Red);
					VNET.Draw_Line((int)growth+1, (int)p+1, (int)growth2-1, (int)p2-1, CSET.Red);
					VNET.Draw_Line((int)growth-1, (int)p+1, (int)growth2-1, (int)p2+1, CSET.Red);
					VNET.Draw_Line((int)growth+1, (int)p-1, (int)growth2+1, (int)p2-1, CSET.Red);
					VNET.Draw_Line((int)growth+1, (int)p-1, (int)growth2-1, (int)p2-1, CSET.Red);
					VNET.Draw_Line((int)growth+1, (int)p-1, (int)growth2+1, (int)p2+1, CSET.Red);
					VNET.Draw_Line((int)growth-1, (int)p+1, (int)growth2+1, (int)p2+1, CSET.Red);
					VNET.Draw_Line((int)growth-1, (int)p+1, (int)growth2-1, (int)p2-1, CSET.Red);



					growth2+= h2;
					
				}
				
				
				growth+=h;

			}
			
		}
		VNET.Update_Pixel_Matrix();
		
		
		 p = tlb.Remap(0, 0, this.Layers.length, 100, image_width);
		 h = ((image_width-100)/(Topology_Of_Neurons[0]+1));
		 growth= h;
		for(int j=0;j<Topology_Of_Neurons[0];j++) {
			VNET.Draw_Circle((int)p, (int)growth, 37, CSET.Royal_Blue,"Fill");
			VNET.Draw_Circle((int)p, (int)growth, 32, new Pixel(85,85,85),"Fill");

			growth+=h;

		}
		
		
		for(int i =1;i<this.Topology_Of_Neurons.length;i+=2) {
			 p = tlb.Remap(i, 0, this.Topology_Of_Neurons.length, 100, image_width);
			 h = ((image_width-100)/(Topology_Of_Neurons[i]+1));
			 growth= h;
			for(int j=0;j<Topology_Of_Neurons[i];j++) {
				VNET.Draw_Circle((int)p, (int)growth, 37, CSET.Royal_Blue,"Fill");
				VNET.Draw_Circle((int)p, (int)growth, 32, new Pixel(85,85,85),"Fill");
				growth+=h;

			}
			
		}
		VNET.Commint_Matrix_Changes();

		
		VNET.Set_Scale(650, 900);
		VNET.Show_Image();
	 }
	 public  void Set_Activation_Function(String Activation_Function_Name) {
		 if(Activation_Function_Name.equals("Relu")) {
			 this.Sigmoid=false;
			 this.Tanh=false;
			 this.Relu=true;
		 }else if(Activation_Function_Name.equals("Sigmoid")) {
			 this.Sigmoid=true;
			 this.Tanh=false;
			 this.Relu=false;
		 }else if(Activation_Function_Name.equals("Tanh")) {
			 this.Sigmoid=false;
			 this.Tanh=true;
			 this.Relu=false;
		 }
	 }
}


class Q_Learner{
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
