import java.util.Random;

public class Fraction extends AbstractMathObject<Fraction>{
    private long numerator;
    private long denominator;

    public Fraction(){
        numerator = 0; denominator = 1;
    }

    public Fraction(long n, long d){
        this();

        if(d == 0) return;

        if(n == 0) {
            numerator = 0;
            denominator = 1;
            return;
        }

        numerator = n;
        denominator = d;

        if(d < 0){
            numerator*=-1;
            denominator*=-1;
        }
    }

    public Fraction(long n){
        this();
        numerator = n;
        denominator = 1;
    }

    public Fraction(double n){
        this();
        numerator = (long)n;
        denominator = 1;
    }


    @Override
    public Fraction plus(Fraction f){
        long n1 = numerator * f.denominator;
        long n2 = f.numerator * denominator;
        long d = denominator * f.denominator;

        long n = n1 + n2;

        return simplestForm(n,d);
    }
    @Override
    public Fraction times(Fraction f){
        long n = numerator * f.numerator;
        long d = denominator * f.denominator;

        return simplestForm(n,d);
    }

    public long[] reduceToSimplestForm(long a, long b){
        Fraction f = simplestForm(a, b);
        return new long[]{f.numerator, f.denominator};
    }
    public Fraction simplestForm(){
        return simplestForm(numerator, denominator);
    }

    @Override
    public Fraction minus(Fraction f){
        return plus(new Fraction(-1*f.numerator, denominator));
    }
    @Override
    public Fraction divide(Fraction f){
        return times(new Fraction(f.denominator, f.numerator));
    }
    @Override
    public Fraction plus(long x){
        return new Fraction(x, 1);
    }
    @Override
    public Fraction times(long x){
        return times(new Fraction(x, 1));
    }
    @Override
    public Fraction minus(long x){
        return minus(new Fraction(x,1));
    }
    @Override
    public Fraction divide(long x){
        return times(new Fraction(1, x));
    }
    @Override
    public boolean lessThan(Fraction f){
        long n1 = numerator * f.denominator;
        long n2 = f.numerator * denominator;

        long n = n1 - n2;

        return n < 0;
    }
    @Override
    public boolean greaterThan(Fraction f){
        long n1 = numerator * f.denominator;
        long n2 = f.numerator * denominator;

        long n = n1 - n2;

        return n > 0;
    }
    @Override
    public boolean equalTo(Fraction f){return numerator == f.numerator && denominator == f.denominator;}
    @Override
    public boolean notEqualTo(Fraction f){return !equals(f);}

    public boolean lessThan(int number){
        return lessThan(new Fraction(number));
    }
    public boolean greaterThan(int number){
        return greaterThan(new Fraction(number));
    }
    public boolean isEqual(int number){
        Fraction f = new Fraction(number);
        f.numerator*=denominator;
        f.denominator*=denominator;
        return this.equalTo(f);
    }

    public int compareDenominator(Fraction a){
        return Long.compare(denominator, a.denominator);
    }
    public Fraction getDenominator(){
        return new Fraction(denominator);
    }

    @Override
    public Fraction inverse(){return new Fraction(denominator, numerator);}
    @Override
    public Fraction pow(long x){
        if(x == 0) return new Fraction(1);
        if(x == 1) return this;

        long d;
        long n;

        if(x < 0){
            n = denominator;
            d = numerator;
            x*=-1;
        }else{
            n = numerator;
            d = denominator;
        }

        for(int i = 1; i < x; i++) {
            n*=n;
            d*=d;
        }

        return new Fraction(n,d);
    }

    @Override
    Fraction sqrt() {
        throw new IllegalStateException("Attempted sqrt(). Use RootFraction instead");
    }

    @Override
    public Fraction mod(long m){
        if(numerator < 0 && denominator == 1) return new Fraction(numerator % m);
        if(denominator == 1) return new Fraction(numerator % m);
        return new  Fraction((long)((numerator % m)*(Math.pow(denominator, m-2) % m))%m);
    }
    @Override
    public long toLong(){
        if(denominator == 1) return numerator;
        return -1;
    }

    @Override
    boolean lessThanZero() {
        return this.toDouble() < 0;
    }

    public double toDouble(){return (numerator+0.0)/(denominator+0.0);}




    public String toString(){
        if(numerator == 0) return "0";
        if(denominator == 1) return ""+numerator+"";
        return "("+numerator+"/"+denominator+")";
    }
    public void print(){System.out.print(this);}
    public void println(){System.out.println(this);}

    public long numerator() {
        return numerator;
    }
    public long denominator(){
        return denominator;
    }
    public void setNumerator(long a) {
        numerator = a;
    }
    public void setDenominator(long b){
        denominator = b;
    }

    public Fraction random(int limit){
        Random r = new Random();
        return new Fraction(r.nextInt(limit));
    }

    Fraction sendValue(long a){
        return new Fraction(a);
    }

    @Override
    Fraction sendValue(long a, long b) {
        return new Fraction(a,b);
    }

    @Override
    Fraction[] sendValues(long[] a) {
        Fraction[] fractions = new Fraction[a.length];
        int i = 0;
        for(long l : a){
            fractions[i++] = new Fraction(l);
        }
        return fractions;

    }


    Fraction copy(){
        return new Fraction(numerator, denominator);
    }

    Fraction sendRandomValue(int limit){
        return new Fraction(r.nextInt(limit)).simplestForm();
    }

}
