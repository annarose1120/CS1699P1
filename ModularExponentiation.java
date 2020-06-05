import java.io.*;
import java.math.BigInteger;

public class ModularExponentiation{


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


  public static void main(String [] args){
  //  BigInteger base = new BigInteger("5");
  //  BigInteger exp = new BigInteger("6");
  //  BigInteger mod = new BigInteger("7");
    //System.out.println("Result of " + base + "^" + exp + "(mod" + mod + ") is " + regularModExp(base, exp, mod));


  }
}
