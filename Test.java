import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Test{

    static int CONVERSION_FROM_NANO = 100000000;
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

    static byte [] testBasearr1= "blah blah blah blah blah".getBytes();
    static BigInteger testBase1 = new BigInteger(testBasearr1);

    static byte [] testBasearr2= "Hello my name is Anna".getBytes();
    static BigInteger testBase2 = new BigInteger(testBasearr2);

    static byte [] testBasearr3= "Today is friday and it is June".getBytes();
    static BigInteger testBase3 = new BigInteger(testBasearr3);

    static byte [] testBasearr4= "test base test base".getBytes();
    static BigInteger testBase4 = new BigInteger(testBasearr4);

    //Test mod
    static byte [] testModarr = "thisismod!".getBytes();
    static BigInteger testMod = new BigInteger(testModarr);

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

      //time the exponentiations
      for(int i = 0; i < samples.size(); i++){
        startTime = System.nanoTime();
        ModularExponentiation.regularModExp(samples.get(i), key, testMod);
        endTime = System.nanoTime();
        totalTime = (endTime - startTime);
        times[i] = totalTime;
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
      printTestResults(key, mean, variance);
    }

    /**
    * Prints test results.
    * @param key the key used as exponent during tests
    * @param avg the average time taken to exponentiate the sample bases with key
    * @param variance the variance of the times taken to exponentiante the sample bases with key
    */
    public static void printTestResults(BigInteger key, double avg, double variance){
      System.out.print("TEST FOR KEY ");
      byte [] bytes = key.toByteArray();
      System.out.print(Arrays.toString(bytes));
      System.out.println("\n----------------------");
      System.out.println("Average time: " + avg + " ns");
      System.out.println("Variance of times " + variance + " ns\n");
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

        //run tests
        for(BigInteger key: keys){
          performTest(key);
        }
    }
}
