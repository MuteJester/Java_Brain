package JBrain;

import JSipl.Matrix;

public class Training_Data {
	
	Data_Cartridge[] Data;
	
	
	public Training_Data(){
		
	}
	
	public void Load_Columns_As_Data(Java_Brain Data_Set,int[] Data_Column_Number,int Result_Column[]) {
		this.Data = new Data_Cartridge[Data_Set.Data.Number_Of_Rows];
		for(int i=0;i<Data_Set.Data.Number_Of_Rows;i++) {
			double row_values[] = new  double[Data_Column_Number.length];
			double result_values[] = new  double[Result_Column.length];
			
			for(int j =0;j<Data_Column_Number.length;j++) {
				row_values[j] = Double.parseDouble(Data_Set.Data.Get(i+1, Data_Column_Number[j]));
			}
			
			for(int q =0;q<Result_Column.length;q++) {
				result_values[q] = Double.parseDouble(Data_Set.Data.Get(i+1, Result_Column[q]));
			}
			this.Data[i] = new Data_Cartridge(row_values,result_values);
			
		}
	}
	public void Load_Marix_As_Data(Matrix input,Matrix Result) {
		this.Data = new Data_Cartridge[1];
		this.Data[0] = new Data_Cartridge(Matrix.Flatten(input),Matrix.Flatten(Result));
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
