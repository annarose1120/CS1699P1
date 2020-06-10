import java.io.*;
import java.math.BigInteger;
import java.math.*;
public class ImprovedModularMultiplication{

  public static BigInteger MontgomeryMultiplication(BigInteger base, BigInteger exp, BigInteger mod){
    //select r to be 2^m where m is bitlength of base and exp
    int m = mod.bitLength();
    int r = (int)Math.pow(2.0, m);

    //calculate r^-1 and n'
    String rString = Integer.toString(r);   //create a BigInteger version of r
    BigInteger R = new BigInteger(rString);
    BigInteger rInverse = R.modInverse(mod);  //calculate r^-1
    BigInteger nPrime = ((BigInteger.ONE).subtract(R.multiply(rInverse))).divide(mod); //calculate n'
    nPrime = nPrime.negate();             //correct n' to make positive
    System.out.println("r is " + R  + " rInverse is " + rInverse + " nPrime is " + nPrime);

    //convert to montgomery domain
    base = (base.multiply(R)).mod(mod);
    exp = (exp.multiply(R)).mod(mod);


    //TODO make this exponentiation
    //do multiplication
    BigInteger result = base.multiply(exp);
    System.out.println("first multiply: " + result);
    //reduce
    result = reduce(result, mod, r, nPrime, m);
    System.out.println("reduce " + result);

    //out of montgomery domain
    //result = reduce(result, mod, r, nPrime, m);
    result = reduce(result, mod, r, nPrime, m);
    System.out.println(result);
    return null;
  }

  /**
  * @param toReduce the value to reduce
  * @param modulus
  * @param r GCD(r, modulus) = 1, r = 2^m
  * @param modulusPrime n', so that nn' = -1 modr
  * @param m the bitlength of modulus
  */
  public static BigInteger reduce(BigInteger toReduce, BigInteger modulus, int r, BigInteger modulusPrime, int m){
    //create a BigInteger version of r
    String rString = Integer.toString(r);
    BigInteger R = new BigInteger(rString);

    //q = toReduce modr * n'modr
    BigInteger temp = (toReduce.mod(R));
    System.out.println("step 1 : " + temp);
    temp = (temp.multiply(modulusPrime.mod(R))).mod(R);
    System.out.println("step 1 : " + temp);

    //q = (toReduce - q * n )/r
    temp = (toReduce.add(temp.multiply(modulus)));
    System.out.println("step 1 " + temp);
    temp = temp.shiftRight(m);

    if(temp.compareTo(modulus) >= 0){
      temp = temp.subtract(modulus);
    }
    return temp;
  }
  public static void main(String [] args){


    MontgomeryMultiplication(new BigInteger("6"), new BigInteger("11"), new BigInteger("9"));
  }
}
