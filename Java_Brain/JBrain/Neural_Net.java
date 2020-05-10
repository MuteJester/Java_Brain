package JBrain;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import JSipl.Color_Palette;
import JSipl.Image;
import JSipl.Math_Toolbox;
import JSipl.Pixel;

public class Neural_Net {
	
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
	/**
	 * The method performs a standard sigmoid operation
	 * @param
	 * x : The value that will be emplaced into the sigmoid operation
	 * @return
	 * The result of the sigmoid operation
	 * */
	public static double Sigmoid(double x) {
		return (1.0/(1.0 + Math.exp(-x) ));
	}
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
				delta = Math.pow((output-target),2)/2;
			}else {
				delta =Math.pow((output-target),2)*0.01;
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
	    		System.out.println("== Epoch Status : [" + i + " / " + Training_Iterations+" ] ==" );
	    	}
	    	System.out.println("\n");
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
