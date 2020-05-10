package JBrain;

import JSipl.Math_Toolbox;

public class Neuron_Layer {
	
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
