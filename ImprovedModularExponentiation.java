import java.io.*;
import java.math.BigInteger;
import java.math.*;
import java.util.*;
public class ImprovedModularExponentiation{

  static int m;                   //bit length of the modulus
  static int r;                   //modulus in the montgomery domain. r = 2^m
  static BigInteger R;            //r but as a BigInteger
  static BigInteger mod;          //the modulus in exponentiation. AKA n
  static BigInteger nPrime;       //n'= -1/n mod r
  static BigInteger rInverse;     //modular multiplicative inverse of r
  static BigInteger exponent;     //current exponent
  static BigInteger vi;           //initial blinding value- gets multiplied into base before exponentiation
  static BigInteger vf;           //final blinding value- gets multiplied by result to undo blinding
  static BigInteger previousExponent = null;

  /**
  * Performs montgomery multiplication
  */
  public static BigInteger MontgomeryMultiplication(BigInteger x, BigInteger y){
      //do multiplication
      BigInteger result = x.multiply(y);

      //reduce
      return reduce(result);
  }

  /**
  * Performs modular exponentiation using montgomery multiplication
  */
  public static BigInteger modexp(BigInteger base, BigInteger exp, BigInteger mod){
    //derive variables needed for montgomery multiplication
    init(base, exp, mod);

    //calculate vi and vf for blinding
    calculateBlinding();

    //blind the base
    base = (base.multiply(vi)).mod(mod);

    //convert base and result initialization to montgomery domain
    base = toMontgomeryDomain(base);
    BigInteger result = toMontgomeryDomain(new BigInteger("1"));

    //actual exponentiation
    int numBits = exp.bitLength();
    //square and mutiply algorithm
    for(int i = numBits - 1; i >= 0; i--){
      //result = result.multiply(result);
      result = MontgomeryMultiplication(result, result);
      //System.out.println("square: " + result);
      if(exp.testBit(i)){
        //result = result.multiply(base);
        result = MontgomeryMultiplication(result, base);
        //System.out.println("multiply: " + result);
      }
    }

    //convert result out of montgomery domain
    result = outMontgomeryDomain(result);

    //unblind the result
    result = (result.multiply(vf)).mod(mod);
    return result;


  }

  /**
  * Converts a BigInteger to the Montgomery Domain
  */
  public static BigInteger toMontgomeryDomain(BigInteger x){
    //x' = xRmodn = MM(x, R^2modn)
    return MontgomeryMultiplication(x, (R.multiply(R)).mod(mod));
  }

  /**
  * Converts a BigInteger out of Montgomery Domain
  */
  public static BigInteger outMontgomeryDomain(BigInteger x){
    //x = x'R^-1modn = MM(x, 1) = reduce(x)
      return reduce(x);
  }

  /**
  * Initializes variables needed for montogmery multiplication.
  * Sets global variables r, m, R, rInverse, mod, and nPrime
  * @param base the base of the exponentiation
  * @param exp the exponent of the exponentiation
  * @param mod the modulus of the exponentation
  */
  public static void init(BigInteger base, BigInteger exp, BigInteger modulus){
    //set mod
    mod = modulus;

    //set exp, save previous exponent
    previousExponent = exponent;
    exponent = exp;


    //select r to be 2^m where m is bitlength of mod
    m = mod.bitLength();
    r = (int)Math.pow(2.0, m);

    //create a BigInteger version of r
    String rString = Integer.toString(r);
    R = new BigInteger(rString);

    //calculate r^-1
    //System.out.println("R is " + R + " mod is " + mod);
    rInverse = R.modInverse(mod);

    //calculate n'
    //Because r and n are coprime (GCD(r,n) = 1), rr^-1 + nn' = 1
    //Solving for n' gives  n' = (1 - rr^-1)/n
    nPrime = ((BigInteger.ONE).subtract(R.multiply(rInverse))).divide(mod); //calculate n'
    nPrime = nPrime.negate();             //correct n' to make positive
  }

  /**
  * @param toReduce the value to reduce
  * @param modulus
  * @param r GCD(r, modulus) = 1, r = 2^m
  * @param modulusPrime n', so that nn' = -1 modr
  * @param m the bitlength of modulus
  */
  public static BigInteger reduce(BigInteger toReduce){

    //q = toReduce modr * n'modr
    BigInteger temp = (toReduce.mod(R));
    temp = (temp.multiply(nPrime.mod(R))).mod(R);

    //q = (toReduce + q * n )/r
    temp = (toReduce.add(temp.multiply(mod)));
    temp = temp.shiftRight(m);

    if(temp.compareTo(mod) >= 0){
      temp = temp.subtract(mod);
    }
    return temp;
  }

  /**
  *
  */
  public static void calculateBlinding(){
      if(previousExponent != null && previousExponent.compareTo(exponent) == 0 ){
        vi = (vi.multiply(vi)).mod(mod);
        vf = (vf.multiply(vf)).mod(mod);
      }
      else{
        //generate vi and vf from scratch
        vi = BigInteger.probablePrime(10, new Random()).mod(mod);
        vf = vi.modInverse(mod);
        vf = vf.modPow(exponent, mod);
      }

  }
  public static void main(String [] args){
  //test for accuracy
  BigInteger modulus = new BigInteger(15, new Random());
  BigInteger two = new BigInteger("2");
  //ensure modulus is odd
  if(modulus.mod(two).compareTo(BigInteger.ZERO) == 0){
    modulus = modulus.add(BigInteger.ONE);
  }

  BigInteger exp = new BigInteger(15, new Random());
  for(int i =0 ; i < 20; i++){
    BigInteger base = new BigInteger(15, new Random());

    BigInteger result = modexp(base, exp, modulus);
    BigInteger expected = base.modPow(exp, modulus);
    if(result.compareTo(expected) !=0){
      System.out.println("FAILURE: " + base + " ^ " + exp + " mod " + modulus);
      System.out.println(result + " != " + expected);
    }
  }
}
}
