package JBrain;

import JSipl.Image;
import JSipl.Matrix;

public class Training_Data {
	
	Data_Cartridge[] Data;
	
	
	public Training_Data(){
		
	}
	
	public void Load_Data(Java_Brain Data_Set,int[] Data_Column_Number,int Result_Column[]) {
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
	public void Load_Data(Matrix[] input,double Results[]) {
		this.Data = new Data_Cartridge[input.length];
		for(int i = 0 ;i < input.length;i++) {
			this.Data[i] = new Data_Cartridge(Matrix.Flatten(input[i]),new double[] {Results[i]});
		}
	}
	public void Load_Data(Image[] input,double Results[]) {
		this.Data = new Data_Cartridge[input.length];
		for(int i = 0 ;i < input.length;i++) {
			Matrix im[] = Matrix.Image_Brakedown(input[i]);
			this.Data[i] = new Data_Cartridge(Matrix.Flatten(im[0]),new double[] {Results[i]});
		}
	}
	public void Load_Data(Image[] input,Image Results[]) {
		this.Data = new Data_Cartridge[input.length];
		for(int i = 0 ;i < input.length;i++) {
			Matrix im[] = Matrix.Image_Brakedown(input[i]);
			Matrix res[] = Matrix.Image_Brakedown(Results[i]);
			im[0].Divide(255);
			res[0].Divide(255);
			this.Data[i] = new Data_Cartridge(Matrix.Flatten(im[0]),Matrix.Flatten(res[0]));
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
