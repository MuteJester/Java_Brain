package JBrain;

import java.util.ArrayList;

import JSipl.Image;
import JSipl.LabPixel;
import JSipl.Pixel;

public class Image_Tools {
	public static Java_Brain Image_Pixels_To_CSV(char Channel,Image source) {
		Java_Brain output = new Java_Brain();
		for(int i=0;i<source.Image_Width;i++) {
			output.Data.Add_Column("1x"+i);
		}
		output.Data.Number_Of_Columns=source.Image_Width;
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
			output.Data.Add_Row(Row);
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
			output.Data.Add_Column(String.format("0x%d",i ));
		}
		
		output.Data.Number_Of_Columns=w;
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
			output.Data.Add_Row(Row);
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
