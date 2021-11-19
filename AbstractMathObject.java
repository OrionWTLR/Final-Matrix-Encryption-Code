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

    abstract E plus(E a);
    abstract E times(E a);
    abstract E minus(E a);
    abstract E divide(E a);

    abstract E plus(long a);
    abstract E times(long a);
    abstract E minus(long a);
    abstract E divide(long a);

    abstract boolean equalTo(E a);
    abstract boolean notEqualTo(E a);
    abstract boolean lessThan(E a);
    abstract boolean greaterThan(E a);

    abstract int compareDenominator(E a);
    abstract E getDenominator();

    abstract E mod(long m);
    abstract E pow(long m);
    abstract E sqrt();
    abstract E inverse();

    abstract E copy();

    abstract E sendValue(long a);
    abstract E sendValue(long a, long b);
    abstract E[] sendValues(long[] a);
    abstract E sendRandomValue(int limit);

    abstract long toLong();

    abstract boolean lessThanZero();

}
