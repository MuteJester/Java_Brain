package JBrain;

import java.util.ArrayList;
import java.util.Arrays;

public class Column {

	public String Column_Name;
	public int Column_Number;
	public int Column_Type;
	public int Missing;
	public ArrayList<String> Values;
	public ArrayList<String> Categories;
	
	
	
	Column(){
		this.Column_Name = "";
		this.Column_Number =0;
		this.Column_Type = 0;
		this.Values = new ArrayList<String>();
		this.Categories = new ArrayList<String>();
	}
	Column(Column copy){
		this.Missing = copy.Missing;
		this.Column_Name = copy.Column_Name;
		this.Categories = new ArrayList<String>(copy.Categories);
		this.Values = new ArrayList<String>(copy.Values);
		this.Column_Type = copy.Column_Type;
		this.Column_Number=copy.Column_Number;
	}
	
	/**
	 * The method runs during the constructor phase and when Refresh_Info method is called
	 * this method determinates the type of the column corresponding to the number passed to the method
	 * as a parameter .
	 * there are 4 types which the method tries to determinate:
	 * Numeric - a column that contains strictly integers/floats/doubles
	 * Categorical - a column which consists of a small finite amount of strings or categories 
	 * which repeat themselves trough out the column (for example - High/Medium/Low or Yes/No )
	 * */
	private void Determinate_Column_Type() {
		boolean numeric = true;
		ArrayList<String> Categories = new ArrayList<String>();
		//numeric test
		String tm;
		for(int i=0;i<this.Values.size();i++) {
			tm = this.Values.get(i);
			if(!tm.matches("[-,+,0-9,.,0-9,E]+")) {
				if(!tm.equals("")) {
					numeric=false;
				}
			}
		}
		
		if(numeric == true) {
			this.Column_Type = 0;
			return;
		}
		
		
	//date time test
		int num_of_dates =0;
		for(int i =1;i<=this.Values.size();i++) {
			tm = this.Values.get(i);
			if(tm.matches("[0-9/0-9/0-9]+")) {
				num_of_dates++;
			}
		}
		if(num_of_dates>0) {
			this.Column_Type =3;
			return;
		}
		
		Categories = new ArrayList<String>(this.Values);
		//categorical test
		for(int i = 0;i<Categories.size();i++) {
			tm = Categories.get(i);
			if(!tm.equals("0") && !tm.equals("")) {
				for(int j = i+1;j<this.Values.size();j++) {
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
			this.Column_Type=1;
			return;
		}
		
	
		
		
		//else set as text
		this.Column_Type =2;
		return;
	}
	public String Get_Column_Type() {
		switch(Column_Type) {
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
	/**
	 * The method rescans the column passed as a parameter and updates 
	 * all the Column information of the requested column.
	 * @param
	 * Column_Number : the number of the column on which to run the method
	 * 
	 * */
	public void Rescan_Info() {
		Determinate_Column_Type();
		if(this.Column_Type==1) {
			ArrayList<String> Categories = new ArrayList<String>();
			String tm;
			Categories = new ArrayList<String>(Values);
			//categorical test
			for(int i = 0;i<Categories.size();i++) {
				tm = Categories.get(i);
				if(!tm.equals("0") && !tm.equals("")) {
					for(int j = i+1;j<this.Values.size();j++) {
						if(Categories.get(j).equals(tm)) {
							Categories.set(j, "0");
						}
					}
				}
			}
			ArrayList<String> Z = new ArrayList<String>();
			Z.add("0");
			Categories.removeAll(Z);
			Set_Categories(Categories);
			
		}

		
		
	}
	@Override
	public String toString() {
		System.out.println(this.Column_Name);
		for(int i = 0; i<this.Values.size();i++) {
			System.out.println(i+1 +  " .)   " + Values.get(i) );
		}
		return null;
	}
	public String Get(int index) {
		if(index <1 || index > this.Values.size()) {
			System.out.println("Index Out Of Range");
			return null;
		}
		return this.Values.get(index-1);
	}
	public ArrayList<String> Get_Categories() {
		if(Column_Type != 1) {
			System.out.println("Column Is Not Categorical");
			return null;
		}else{
			return this.Categories;
		}
	}
	public void Set_Categories(ArrayList<String> Categories) {
		this.Categories = Categories;
	}
	/**
	 * The method will calculate the mean of the specified numeric column
	 * @return
	 * The Mean value of the selected column
	 * */
	public double Column_Mean() {
		double[] dt = new double[Values.size()];
		for(int i = 0 ; i<this.Values.size();i++) {
			dt[i] = Double.parseDouble(this.Values.get(i));
		}		
		
		double mean=0;
		for(int i =0;i<this.Values.size();i++) {
			mean += dt[i];
		}
		return mean/Values.size();
	}
	/**
	 * The method will calculate the median of the specified numeric column
	 * @param
	 * Column_Number : The number of the column on which the method should run.
	 * @return
	 * The Median value of the selected column
	 * */
	public double Column_Median() {
		double[] dt = new double[Values.size()];
		for(int i = 0 ; i<this.Values.size();i++) {
			dt[i] = Double.parseDouble(this.Values.get(i));
		}
		Arrays.sort(dt);
		if (dt.length % 2 == 0) {
			return (double)(dt[dt.length / 2] + (double)dt[(dt.length / 2) - 1]) / 2;
		}
		else {

			return dt[dt.length / 2];
		}
	
	}
	/**
	 * The method will calculate the Standard Deviation of the specified numeric column
	 * @return
	 * The Standard Deviation value of the selected column
	 * */
	public double Column_Standard_Deviation() {
		double[] dt = new double[Values.size()];
		for(int i = 0 ; i< this.Values.size();i++) {
			dt[i] = Double.parseDouble(this.Values.get(i));
		}
		double mean = this.Column_Mean();
		double sum = 0;
		for (int i = 0; i < dt.length; i++) {
			sum += ((dt[i] - mean)*(dt[i] - mean));
		}
		sum /= (double)dt.length;
		sum = Math.sqrt(sum);
		return sum;
	}
	/**
	 * The method will calculate the variance of the specified numeric column
	 * @return
	 * The Variance value of the selected column
	 * */
	public 	double Column_Variance() {
		double Deviation = Column_Standard_Deviation();
		return (Deviation)*(Deviation);
	}
	/**
	 * The method will find the largest value of the specified numeric column.
	 * @return
	 * The largest value of the selected column
	 * */
	public double Max_Value() {
		double max = Double.MIN_VALUE;
		for(int i =0;i<this.Values.size();i++) {
			double cur_val = Double.parseDouble(Values.get(i));
			if(max < cur_val) {
				max = cur_val;
			}
		}
		return max;
	}
	/**
	 * The method will find the smallest value of the specified numeric column
	 * @return
	 * The smallest value of the selected column
	 * */
	public double Min_Value() {
		double min = Double.MAX_VALUE;
		for(int i =0;i<this.Values.size();i++) {
			double cur_val = Double.parseDouble(Values.get(i));
			if(min > cur_val) {
				min = cur_val;
			}
		}
		return min;
	}
	/**
	 * The method will fill the specified numeric column empty(missing) values with the mean value of the column
	 * */
	public void Fill_Missing_With_Column_Mean() {
		double mean =0;
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for(int i=0;i<this.Values.size();i++) {
			String val = this.Values.get(i);
			if(!val.equals("")) {
				mean+=Double.parseDouble(val);
			}else {
				positions.add(i);
			}
		}
		mean /= this.Values.size();
		String smean = String.format("%.4f", mean);
		for(int i =0; i<positions.size();i++) {
			this.Values.set(positions.get(i), smean);
		}
	}
	/**
	 * The method will fill the specified numeric column empty(missing) values with the median value of the column
	 * */
	public void Fill_Missing_With_Column_Median() {
		double median =0;
		double[] dt = new double[Values.size()];
		ArrayList<Integer> blanks = new ArrayList<Integer>();
		String sam ;
		for(int i = 0 ; i<this.Values.size();i++) {
			sam = Values.get(i);
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
			Values.set(blanks.get(i), sm);
		}
		
	}
	/**
	 * The method will fill the specified numeric column empty(missing) values with the pattern specified
	 * @param
	 * Pattern : The Pattern Which will be placed in the positions of all missing values.
	 * */
	public void Fill_Missing_With_Pattern(String Pattern) {
		String sm;
		for(int i =1;i <= this.Values.size();i++) {
			sm = this.Values.get(i);
			if(sm.equals("")) {
				Values.set(i, Pattern);
			}
		}
	}
	public void Set(Column source) {
		this.Missing = source.Missing;
		this.Column_Name = source.Column_Name;
		this.Categories = new ArrayList<String>(source.Categories);
		this.Values = new ArrayList<String>(source.Values);
		this.Column_Type = source.Column_Type;
		this.Column_Number=source.Column_Number;
	}
}
