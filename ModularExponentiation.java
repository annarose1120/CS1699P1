import java.io.*;
import java.math.BigInteger;

public class ModularExponentiation{

  final static int NUMBER_OF_SAMPLES = 50;     //# of samples taken for each test

  /**
  * Performs the naive, square-and-multiply method of modular exponentiation.
  * Raises base^exponent mod(modulus).
  * @param base the base of the exponentiation
  * @param exponent the exponent of the exponentiation
  * @param modulus the modulus of the exponentation
  * @return the result of the modular exponentiation
  */
  public static BigInteger regularModExp(BigInteger base, BigInteger exponent, BigInteger modulus){
      //initialise result, retrive # bits in exponent
      BigInteger result = new BigInteger("1");
      int numBits = exponent.bitLength();
      //square and mutiply algorithm
      for(int i = numBits - 1; i >= 0; i--){
        result = result.multiply(result);
        if(exponent.testBit(i)){
          result = result.multiply(base);
        }
        result = result.mod(modulus);
      }

      return result;
  }

  /**
  * Test for the naive implementation of modular exponentation implemented in
  * regularModExp function.
  */
  public static void testC3(){
    //Test keys (exponents)
    byte [] testKey1arr = {(byte)0x7f, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff   //1111111111......
                          , (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff};
    BigInteger testKey1 = new BigInteger(testKey1arr);

    byte [] testKey2arr = {(byte)0x55, (byte) 0x55,(byte)0x55, (byte) 0x55,(byte)0x55,   //0101010101........
                          (byte) 0x55, (byte)0x55, (byte) 0x55, (byte)0x55, (byte) 0x55};
    BigInteger testKey2 = new BigInteger(testKey2arr);

    byte [] testKey3arr = {(byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,     //10000000000......
                           (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
    BigInteger testKey3 = new BigInteger(testKey3arr);

    //Test base
    byte [] testBasearr= "blah blah blah blah blah".getBytes();
    BigInteger testBase = new BigInteger(testBasearr);

    //Test mod
    byte [] testModarr = "this is the mod".getBytes();
    BigInteger testMod = new BigInteger(testModarr);
    System.out.println(testMod);

    //TEST 1
    double avgTime = performTest(testBase, testKey1, testMod);
    printTestResults(1, testBase, testKey1, testMod, avgTime);

    //TEST 2
    avgTime = performTest(testBase, testKey2, testMod);
    printTestResults(2, testBase, testKey2, testMod, avgTime);

    //TEST 3
    avgTime = performTest(testBase, testKey3, testMod);
    printTestResults(3, testBase, testKey3, testMod, avgTime);

  }

  /**
  * Performs modular exponentiation with the given arguments NUMBER_OF_SAMPLES times.
  * Returns the average amount of time taken.
  */
  public static double performTest(BigInteger base, BigInteger exp, BigInteger mod){
    long startTime;
    long endTime;
    long rawTotalTime = 0;
    BigInteger result;

    for(int i = 0; i < NUMBER_OF_SAMPLES; i++){
      startTime = System.nanoTime();
      regularModExp(base, exp, mod);
      endTime = System.nanoTime();
      rawTotalTime += endTime - startTime;
    }
     double averageTime = (double)rawTotalTime/(double)NUMBER_OF_SAMPLES;
     return averageTime;
  }

  /**
  * Prints the results of a test.
  */
  public static void printTestResults(int testNum, BigInteger base, BigInteger exp, BigInteger mod, double time){
    System.out.println("TEST " + testNum + "\n-------------");
    System.out.println("Base: " + base);
    System.out.println("Exponent: " + exp);
    System.out.println("Mod: " + mod);
    System.out.println("Average Time: " + time);
  }
  public static void main(String [] args){
  //  BigInteger base = new BigInteger("5");
  //  BigInteger exp = new BigInteger("6");
  //  BigInteger mod = new BigInteger("7");
    //System.out.println("Result of " + base + "^" + exp + "(mod" + mod + ") is " + regularModExp(base, exp, mod));
    testC3();

  }
}
