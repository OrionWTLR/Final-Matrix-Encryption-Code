package NumberTypes;

import java.util.Random;

public abstract class AbstractMathObject<E>{
    public long gcd(long x, long y){
        if(x == 0){
            return y;
        }
        return gcd(y % x, x);
    }

    public Fraction simplestForm(long n, long d){
        long gcd = gcd(n,d);
        n/=gcd;
        d/=gcd;
        return new Fraction(n,d);
    }

    public long findPerfectRoot(long num, Fraction exp){
        double sq = Math.sqrt(num);

        if(sq - Math.floor(sq) == 0) return (long)sq;

        return 1;
    }

    public Random r = new Random();

    public abstract E plus(E a);
    public abstract E times(E a);
    public abstract E minus(E a);
    public abstract E divide(E a);

    public abstract E plus(long a);
    public abstract E times(long a);
    public abstract E minus(long a);
    public abstract E divide(long a);

    public abstract boolean equalTo(E a);
    public abstract boolean notEqualTo(E a);
    public abstract boolean lessThan(E a);
    public abstract boolean greaterThan(E a);

    public abstract int compareDenominator(E a);
    public abstract E getDenominator();

    public abstract E mod(long m);
    public abstract E pow(long m);
    public abstract E sqrt();
    public abstract E inverse();

    public abstract E copy();

    public abstract E sendValue(long a);
    public abstract E sendValue(long a, long b);
    public abstract E[] sendValues(long[] a);
    public abstract E sendRandomValue(int limit);

    public abstract long toLong();

    public abstract boolean lessThanZero();

}