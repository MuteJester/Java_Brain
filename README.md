[![Stargazers][stars-shield]][stars-url]
[![Commits][commits-shield]][commits-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/MuteJester/Java_Brain">
    <img src="ReadmeMisc/Logo JavaBrain.png" alt="Logo" width="280" height="230">
  </a>

  <h3 align="center">Java Brain Library</h3>

  <p align="center">
    Simple Solution For Fast Programing Of Data Mining ,Deep Learning And Machine Learning Models As Well As CSV Management.
    <br />
    <a href="https://github.com/MuteJester/Java_Brain/wiki"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/MuteJester/Java_Brain/wiki/A-Simple-Example-Of-Java-Brain-Usage">View Demo</a>
    ·
    <a href="https://github.com/MuteJester/Java_Brain/issues">Report Bug</a>
    ·
    <a href="https://github.com/MuteJester/Java_Brain/issues">Request Feature</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
* [Usage](#usage)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)



<!-- ABOUT THE PROJECT -->
## About The Project
There are a few Deep Learning And Machine Learning Java libraries out there, but none of them in my mind were simple enough to use and required the user to install loads of additional parts trough maven, etc.
At that moment the idea to create a simple plug and play java classes which can be simply dragged into the src folder of any project and functionality of all the tools that the Java Brain library can be quickly and easily accessed and used.

Here's Why Java Brain :brain: :
* It's As Simple As Drag And Drop and you are ready to code!
* Function and methods are named meaningful names that way it is always clear what are the parameters or what does the method do!
* CSV handling is all included in the library working with them made much easier trough java code!
* Developing complex matrix algorithms ? we've got you covered with a dynamic Matrix class filled with quick usful methods strating from eigen value/vector extraction trough basic operations up to decompositions.
### Built With
* [Eclipse](https://www.eclipse.org/)
* [Inetlij](https://www.jetbrains.com/)
* [SIPL Image Proccesing Library](https://github.com/MuteJester/Simple_Image_Processing_Library)

### Other Languages
 A python wrapper for the java brain library , it uses numpy,pandas,sympy and matplotlib inorder to give you the same coding experience as with java brain but under the python language ,all method and class are named and function exactly the same way they would in java,all that is needed to start working with the python version is to drag to 'Python_Brain' .py file into your project folder and import it.
the usage is the same and the logic is the same.


### Installation
1. Download Both Java Packages (JSipl and JBrain) From The Repository [Go To Repository Folder](https://github.com/MuteJester/Java_Brain/tree/master/Java_Brain)
2. Copy Both Packages Into Your Projects /src Folder
3. Import JBrain.* and JSipl.*
4. You Are Ready To Code!

<!-- USAGE EXAMPLES -->
## Usage


#### Ploting Linear Regression Algorithm

<img src="Examples/Linear_Regression_data.png" alt="Logo" width="600" height="600">
Java Brain Offers Binary Linear Regression As Well As Multivariable Linear Regression
in the following exmaple a fast method for immediate ploting of a linear regression model.


#### Ploting Logistic Regression Algorithm

<img src="Examples/Logistic_regression_shoots.png" alt="Logo" width="600" height="600">
Java Brain Offers Binary Logistic Regression As Well As Multivariable Logistic Regression
in the following example a fast method for immediate plotting of a logistic regression model.
the logistic regression functionality, as well as the linear regression, can be used as pleased
the methods return the weights resulting after computing the optimal state using gradient descent;



#### Ploting 2D K-Means Algorithm

<img src="Examples/kmeans-k=2.png" alt="Logo" width="600" height="600">
Java Brain Offers a fast and efficenet K-means calculation aglorithm although currently only up to 3 dimensional K - Means
calculations supported soon there will a higher dimension support system.

#### Ploting 2D K-Nearest-Neighbors Algorithm

<img src="Examples/KNN_example.png" alt="Logo" width="600" height="600">
Java Brain Offers a simple and dynamic kNN implementation aglorithm for all your use case as well as a quick
plot method for immediate display of 2D kNN results. 
In the above image- the red ciricle with the green dot in the center is the new added value, blue points that turned pink are the specified K nearest neighbors.

#### Ploting User Designed Fully Connected Neural Network Topology

<img src="Examples/FCNN-topology.png" alt="Logo" width="600" height="600">

Java Brain Offers you a method which is a part of the Neural_Net class which allows you to visualize you neural network topology inorder for you to validate that the topology you have specified in the Neural_Net class constructor is indeed the topology created and set.  



### Currently Supported Data Mining / Machine Learning / Deep Learning, Algorithms
  * __Linear Regression using Static Formula (Only supported for single variable linear regression)__
  * __Linear Regression using Gradient Descent (Multivariable regression supported using matrix with Lasso Regression and L1 Regularization)__
  * __Logistic Regression of binary variable as well as multivariable equations__
  * __Validation Of Logistic Regression - Confusion Matrix__
  * __K-Means calculation (up to 3-dimensional data variables)__
  * __kNN - k Nearest Neighbors__
  * __Fully Connected Neural Network Constructor class accepting all forms of network topologies for dynamic use__
  * __Q Learning "Custom-fit" Class applicable for any use__
  * __Error computations such as MSE,MAE,MPE__
  * __Correlation computations such as R^2, Adj-R^2, Pearsons,Spearman -__
  * __Confusion Matrix Construction For A Given Model Weights__
  * __Some of the models have supported validation methods which will automatically compare machine guesses against actual values__
  * __Principal Component Analysis__
  * __Corpus And Bag Of Words Containers__
  * __Sentiment Analysis (Vader Algorithm)__
  
  ### Currently Supported CSV Handling Functionality
  
  * __Reading/Writing CSV Files__
  * __CSV Importation From Web URL's__
  * __Column Classification (Numeric/Categorical/Date-Time/Text)__
  * __Single Block/Column/Row Manipulations__
  * __Removal and Addition of columns and rows__
  * __Pattern Replacment__
  * __Pulling and pushing values into csv stated position__
  * __CSV spliting by percentage into seperate data sets__
  * __Column Statistical Calculations such as - Column Standard Deviation and Column Variance/Covariance,Column Correlation..__
  * __Column Missing Value Reduction / Replacement__
  
  
  
  ### All Visual Elements Are Created Using The SIPL Library
  
 







_For more Detailed Instructions, please refer to the [User Instructions](https://github.com/MuteJester/Java_Brain/wiki/User-Manual)_



<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/MuteJester/Java_Brain/issues) for a list of proposed features (and known issues).

<!-- CONTRIBUTING -->
## Contributing


Contributions are what make the open-source community such a powerful place to create new ideas, inspire, and make progress. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

[Thomas Konstantinovsky]() - thomaskon90@gmail.com

Project Link: [https://github.com/MuteJester/Java_Brain](https://github.com/MuteJester/Java_Brain)





<!-- MARKDOWN LINKS & IMAGES -->
[stars-shield]: https://img.shields.io/github/stars/MuteJester/Java_Brain.svg?style=flat-square
[stars-url]: https://github.com/MuteJester/Java_Brain/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=flat-square
[issues-url]: https://github.com/MuteJester/Java_Brain/issues
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=flat-square
[license-url]: https://github.com/MuteJester/Java_Brain/blob/master/LICENSE
[commits-shield]: https://img.shields.io/github/commit-activity/m/MuteJester/Java_Brain?style=flat-square
[commits-url]: https://github.com/MuteJester/Java_Brain
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=flat-square&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/thomas-konstantinovsky-56230117b/
