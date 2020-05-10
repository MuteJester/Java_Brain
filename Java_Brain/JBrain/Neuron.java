package JBrain;

public class Neuron {
	
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
