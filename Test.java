import java.io.*;
import java.math.BigInteger;
import java.util.*;

//-----------------------------------------------------------------------------------------------------------------
//This class tests the time taken to perform modular exponentiation.
//The inputs to the modular exponentiation are the base/sample, the key/exponent, and the modulus.
//The modulus is constant for each test. NUMBER_OF_SAMPLES number of samples are randomly generated,
//each of length KEY_SIZE. KEY_SIZE number of keys are generated with Hamming weights 1 to KEY_SIZE.
//For example, the first key is 10000..., the second is 110000...., the third is 1110000......
//Each test times one of the keys with each of the samples (and the constant modulus).
//For each test, the average and variance of these times is calculated.
//After all of the tests are performed (one for each key), the overall variance and average of
//all times is calculated.
//-----------------------------------------------------------------------------------------------------------------

public class Test{

    final static int KEY_SIZE = 512;                //bit length of keys/exponents and samples/bases used
    final static int NUMBER_OF_SAMPLES = 200;       //number of samples/bases used for each test

    //Test keys (exponents)
    static ArrayList<BigInteger> keys = new ArrayList<BigInteger>();

    //Test samples (bases)
    static ArrayList <BigInteger> samples = new ArrayList <BigInteger>();

    //Test mod. Same for each test 
    static byte [] testModarr = "thisismod!".getBytes();
    static BigInteger testMod = new BigInteger(testModarr);

    //to store all times across test. used to calculate overall variance
    static ArrayList<Double> allTimes = new ArrayList<Double>();

    /**
    * Performs modular exponentiation with the indicated key for each of the samples.
    * Times these exponentiations, then calculates their average and variance.
    * Prints results based on mode.
    * @param key exponent for the modular exponentiation
    */
    public static void performTest(BigInteger key, String mode){
      long startTime;
      long endTime;
      long totalTime;
      long [] times = new long [samples.size()];

      //print header if in full test mode
      if(mode.equals("all")){
          System.out.print("TEST FOR KEY ");
          System.out.print(key.toString(2));
          System.out.println("\n----------------------");
      }

      //time the exponentiations
      for(int i = 0; i < samples.size(); i++){
        startTime = System.nanoTime();
        ModularExponentiation.regularModExp(samples.get(i), key, testMod);
        endTime = System.nanoTime();
        totalTime = (endTime - startTime);
        times[i] = totalTime;
        allTimes.add((double)totalTime);
        //System.out.println("Time " + i + ": " + totalTime + "ns");
      }
      //calculate mean
      double sum = 0;
      for(int j = 0; j < samples.size(); j++){
        sum+=times[j];
      }
      double mean = (double)sum/(double)samples.size();

      //calculate variance
      long temp = 0;
      for(int k = 0; k < samples.size(); k++){
          temp += (times[k] - mean) * (times[k] - mean);
      }
      double variance = (double)temp/(double)(samples.size() - 1);

      //print results based on mode
      if(mode.equals("avg")){
          System.out.println(mean);
      }
      else if(mode.equals("var")){
          System.out.println(variance);
        }
      else if(mode.equals("all")){
          System.out.println("Average time: " + mean + " ns");
          System.out.println("Variance of times " + variance + " ns\n");
        }

    }

    /**
    * Calculates the overall average and variance of test times
    */
    public static void overallAverageAndVariance(){
      //calculate mean
      double sum = 0;
      for(int j = 0; j < allTimes.size(); j++){
        sum += allTimes.get(j);
      }
      double mean = (double)sum/(double)allTimes.size();

      //calculate variance
      double temp = 0;
      for(int k = 0; k < allTimes.size(); k++){
          temp += (allTimes.get(k) - mean) * (allTimes.get(k) - mean);
      }
      double variance = (double)temp/(double)(allTimes.size() - 1);

      System.out.println("Average of all times: " + mean + "ns");
      System.out.println("Variance of all times: " + variance + "ns\n");

    }


    public static void main(String [] args){
        //validate mode the user entered
        //all = full test, avg = just averages for each hamming weight, var = just variances for each hamming weight
        if(args.length != 1){
          System.out.println("Please enter an argument.\n" +
           "\"all\" for full test, \"avg\" for just averages, \"var\" for just variances");
           System.exit(0);
        }
        else if(!args[0].equals("all") && !args[0].equals("avg") && !args[0].equals("var")){
          System.out.println("Please enter an argument.\n" +
           "\"all\" for full test, \"avg\" for just averages, \"var\" for just variances");
           System.exit(0);
        }

        //set the mode so it can be accessed from all methods
        String mode = args[0];

        //add keys of hamming weights 1 to KEY_SIZE to arraylist of keys to use
        BigInteger temp = new BigInteger("2").pow(KEY_SIZE); //100000.....
        for(int i = 0; i < KEY_SIZE; i++){
          keys.add(temp);
          temp = temp.flipBit(KEY_SIZE-i-1);
        }

        //add random bases of length KEY_SIZE to arraylist of samples/bases to use
        for(int k = 0; k < NUMBER_OF_SAMPLES; k++){
          temp = new BigInteger(KEY_SIZE, new Random());
          samples.add(temp);
        }

        //run tests
        for(BigInteger key: keys){
          performTest(key, mode);
        }

        //calculate variance of test timing averages if in full test mode
        if(mode.equals("all")){
          overallAverageAndVariance();
        }
    }
}
