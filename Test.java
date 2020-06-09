import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Test{

    //Test keys (exponents)
    static ArrayList<BigInteger> keys = new ArrayList<BigInteger>();

    static byte [] testKey1arr = {(byte)0x7f, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff,   //1111111111......
                           (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff};
    static BigInteger testKey1 = new BigInteger(testKey1arr);

    static byte [] testKey2arr = {(byte)0x55, (byte) 0x55,(byte)0x55, (byte) 0x55,(byte)0x55,   //0101010101........
                          (byte) 0x55, (byte)0x55, (byte) 0x55, (byte)0x55, (byte) 0x55};
    static BigInteger testKey2 = new BigInteger(testKey2arr);

    static byte [] testKey3arr = {(byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,     //10000000000......
                           (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
    static BigInteger testKey3 = new BigInteger(testKey3arr);


    //Test samples (bases)
    static ArrayList <BigInteger> samples = new ArrayList <BigInteger>();

    static byte [] testBasearr1= "blah ".getBytes();
    static BigInteger testBase1 = new BigInteger(testBasearr1);

    static byte [] testBasearr2= "Hello".getBytes();
    static BigInteger testBase2 = new BigInteger(testBasearr2);

    static byte [] testBasearr3= "Friday".getBytes();
    static BigInteger testBase3 = new BigInteger(testBasearr3);

    static byte [] testBasearr4= "test ".getBytes();
    static BigInteger testBase4 = new BigInteger(testBasearr4);

    static byte [] testBasearr5 = "test2".getBytes();
    static BigInteger testBase5 = new BigInteger(testBasearr5);


    //Test mod
    static byte [] testModarr = "thisismod!".getBytes();
    static BigInteger testMod = new BigInteger(testModarr);


    //to store all times across test. used to calculate overall variance
    static ArrayList<Double> allTimes = new ArrayList<Double>();

    /**
    * Performs modular exponentiation with the indicated key for each of the samples.
    * Times these exponentiations, then calculates their average and variance.
    * Calls printTestResults to report the average and variance of these times
    * @param key exponent for the modular exponentiation
    */
    public static void performTest(BigInteger key){
      long startTime;
      long endTime;
      long totalTime;
      long [] times = new long [samples.size()];

      //print out header
      System.out.print("TEST FOR KEY ");
      byte [] bytes = key.toByteArray();
      System.out.print(Arrays.toString(bytes));
      System.out.println("\n----------------------");

      //time the exponentiations
      for(int i = 0; i < samples.size(); i++){
        startTime = System.nanoTime();
        ModularExponentiation.regularModExp(samples.get(i), key, testMod);
        endTime = System.nanoTime();
        totalTime = (endTime - startTime);
        times[i] = totalTime;
        allTimes.add((double)totalTime);
        System.out.println("Time " + i + ": " + totalTime + "ns");
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

      //print results
      System.out.println("Average time: " + mean + " ns");
      System.out.println("Variance of times " + variance + " ns\n");

    }

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

      //print average and variance
      System.out.println("Average of all times: " + mean + "ns");
      System.out.println("Variance of all times: " + variance + "ns\n");

    }


    public static void main(String [] args){

        //init key ArrayList and base arraylist
        keys.add(testKey1);
        keys.add(testKey2);
        keys.add(testKey3);

        samples.add(testBase1);
        samples.add(testBase2);
        samples.add(testBase3);
        samples.add(testBase4);
        samples.add(testBase5);


        //run tests
        for(BigInteger key: keys){
          performTest(key);
        }

        //calculate variance of test timing averages
        overallAverageAndVariance();
    }
}
