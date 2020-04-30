package assignment1;

//Importing Libraries:
import java.util.Objects;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

//The class is as follows:
public class Complex 
{
	
	// The following two variables represent the complex no:
 private double real;   
 private double imaginary;   


 // Making a Constructor for this class:
 public Complex(double real, double imaginary) 
 {
     this.real = real;
     this.imaginary = imaginary;
 }

 // The following function returns the real part of the complex no:
 public double real() 
 { 
 	return this.real;
 }
 
 // The following function returns the imaginary part of the complex no:
 public double imaginary() 
 { 
 	return imaginary; 
 }
 
 // The following function returns a new Complex object=original complex no*scalingfactor:
 public Complex scaling(double scalingfactor) 
 {
     return new Complex(scalingfactor * this.real, scalingfactor * this.imaginary);
 }
 
 public String ComplextoString() 
 {
	 if (imaginary <  0) 
	 {
    	 return real + " - " + (-imaginary) + "i";
	 }
     if (imaginary == 0) 
     {
    	 return real + "";
     }
     if (real == 0) 
     {
    	 return imaginary + "i";
     }
     return real + " + " + imaginary + "i";
 }
 
 // The following function returns a new Complex object equal to the conjugate of the original complex no:
 public Complex conj() 
 {
     return new Complex(real, -imaginary);
 }
 
 // The following function returns a new Complex object whose value is original complex no * b, where b is the new complex no:
 public Complex multiply(Complex b) 
 {
     return new Complex(this.real * b.real - this.imaginary * b.imaginary, this.real * b.imaginary + this.imaginary * b.real);
 }
 
 // The following function adds complex1 and complex2:
 public static Complex add(Complex complex1, Complex complex2)
 {
     return new Complex(complex1.real + complex2.real, complex1.imaginary + complex2.imaginary);
 }
 
 // The following function returns a new Complex object whose value is original complex no + b, where b is the new complex no:
 public Complex add(Complex b) 
 { 
     return new Complex(this.real + b.real, this.imaginary + b.imaginary);
 }

 // The following function returns a new Complex object whose value is original complex no - b, where b is the new complex no:
 public Complex subtract(Complex b)    
 {
     return new Complex(this.real - b.real, this.imaginary - b.imaginary);  
 }

 // The following function returns a new Complex object whose value is the reciprocal of the original complex no:
 public Complex reciprocal() 
 {
     return new Complex(real / (Math.pow(real,2) + Math.pow(imaginary,2)), -imaginary / (Math.pow(real,2) + Math.pow(imaginary,2)));
 }

 // The following function returns a new Complex object=original complex no/b:
 public Complex divides(Complex b) 
 {
     return this.multiply(b.reciprocal());
 }

 // The following function returns the multiplicative inverse of the complex object:
 public Complex inv() 
 {
 	Complex div = new Complex (this.real*this.real-this.imaginary*this.imaginary, 0);
 	return this.conj().divides(div);
 }
 

 
}

