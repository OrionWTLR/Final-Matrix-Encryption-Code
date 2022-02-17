package NumberTypes;

public class RootNumber extends AbstractMathObject<RootNumber>{
    private long coefficient = 1, base = 1;
    private Fraction exponent = new Fraction(1);
    private final Fraction half = new Fraction(1,2);
    private final Fraction oneF = new Fraction(1);

    RootNumber(){
    }
    RootNumber(long a){
        make(a, 1, 1,1 );
    }
    RootNumber(long a, long x, long y){
        make(a, x, y, 2);
    }
    RootNumber(long a, long x, long y, long z){
        make(a, x, y, z);
    }
    RootNumber(long a, long x, Fraction exp){
        make(a, x, exp.numerator(), exp.denominator());
    }
    RootNumber(RootNumber sr){
        make(sr.constant(), sr.base(), sr.exponent().numerator(), sr.exponent().denominator());
    }
    private void make(long a, long x, long y, long z){

        if(a == 0) {
            coefficient = 0;
            return;
        }

        coefficient = a; base = x;
        if(base == 1){
            exponent = new Fraction(1); return;
        }

        exponent = new Fraction(y,z);

        long perf = findPerfectRoot(base, exponent);
        if(perf != 1) {
            coefficient = perf;
            base = 1;
            exponent = oneF;
            return;
        }

        if(exponent.equalTo(oneF)){
            coefficient = coefficient * base;
            base = 1;
        }

    }


    boolean sameExponent(RootNumber a){
        return base == a.base && exponent.equalTo(a.exponent);
    }
    boolean sameRoot(RootNumber a){
        return base == a.base && exponent.denominator() == a.exponent.denominator();
    }
    boolean isWhole(){
        return base == 1;
    }
    boolean isOne(){
        return coefficient == 1 && base == 1;
    }
    boolean isZero(){
        return coefficient == 0;
    }
    boolean equalExp(RootNumber a){
        return this.exponent().equalTo(a.exponent());
    }

    long constant(){return coefficient;}
    long base(){return base;}
    Fraction exponent(){return exponent;}

    long rootPow(){
        return (long) Math.pow(base, exponent.numerator());
    }
    long constantPow(long n){
        return (long) Math.pow(coefficient, n);
    }

    double toDouble(){
        return coefficient *Math.pow(base, ((double) exponent.numerator()/ exponent.denominator()));
    }

    public String toString(){
        if(base == 1) return coefficient +"";
        if(coefficient == 1 && exponent().equalTo(half)) return "√("+ base +")";
        if(exponent().equalTo(half)) return coefficient +"√("+ base +")";
        return coefficient +"*("+ base +")^"+exponent();
    }

    public String fullString(){
        return constant()+"*("+base()+")^"+exponent();
    }



    @Override
    public RootNumber plus(RootNumber a) {
        if(base == 1 && a.base == 1) return new RootNumber(coefficient + a.coefficient, 1, 1);
        if(coefficient == 0) return a;
        if(a.coefficient == 0) return this;
        if(base == a.base && exponent.equalTo(a.exponent)) return new RootNumber(coefficient +a.coefficient, base, exponent);
        throw new IllegalStateException(this+" + "+a+" is an Invalid Addition");
    }

    @Override
    public RootNumber times(RootNumber a) {
        if(base == 1 && a.base == 1) return new RootNumber(coefficient * a.coefficient, 1, 1);
        if(base == 1) return new RootNumber(coefficient *a.coefficient, a.base, a.exponent);
        if(a.base == 1) return new RootNumber(coefficient *a.coefficient, base, exponent);
        if(base == a.base) return new RootNumber(coefficient *a.coefficient, base, exponent.plus(a.exponent));
        if(exponent.equalTo(a.exponent)) return new RootNumber(coefficient * a.coefficient, base * a.base, exponent);
        throw new IllegalStateException(this+" * "+a+" is an Invalid Multiplication");
    }

    @Override
    public RootNumber minus(RootNumber a) {
        return plus(new RootNumber(-1* coefficient, base, exponent));
    }

    @Override
    public RootNumber divide(RootNumber a) {
        throw new IllegalStateException("Division is not allowed");
    }

    @Override
    public RootNumber plus(long a) {
        return plus(new RootNumber(a));
    }

    @Override
    public RootNumber times(long a) {
        return times(new RootNumber(a));
    }

    @Override
    public RootNumber minus(long a) {
        return minus(new RootNumber(a));
    }

    @Override
    public RootNumber divide(long a) {
        throw new IllegalStateException("Division is not allowed");
    }

    @Override
    public boolean equalTo(RootNumber a) {
        return coefficient == a.coefficient && base == a.base && exponent.equalTo(a.exponent);
    }
    @Override
    public boolean notEqualTo(RootNumber a) {
        return !equalTo(a);
    }
    @Override
    public boolean lessThan(RootNumber a) {
        return toDouble() < a.toDouble();
    }
    @Override
    public boolean greaterThan(RootNumber a) {
        return toDouble() > a.toDouble();
    }

    @Override
    public int compareDenominator(RootNumber a){
        return 0;
    }
    public RootNumber getDenominator(){
        return new RootNumber(1);
    }

    @Override
    public RootNumber mod(long m) {
        if(isWhole()) return new RootNumber(coefficient % m);
        throw new IllegalStateException("Mod on root value is not allowed");
    }

    @Override
    public RootNumber pow(long m) {
        return new RootNumber(constantPow(m), base, exponent.times(m));
    }

    @Override
    public RootNumber sqrt() {
        if(isWhole()) return new RootNumber(1, coefficient, 1,2);
        if(!isWhole() && coefficient == 1) return new RootNumber(1, base, exponent.numerator(), 2* exponent.denominator());
        throw new IllegalStateException("Third case is a WIP");
    }

    @Override
    public RootNumber inverse() {
        throw new IllegalStateException("No inverse");
    }

    @Override
    public RootNumber copy() {
        return new RootNumber(coefficient, base, exponent.copy());
    }

    @Override
    public RootNumber sendValue(long a) {
        return new RootNumber(a);
    }

    @Override
    public RootNumber sendValue(long a, long b) {
        return new RootNumber(a, b, 1);
    }

    @Override
    public RootNumber[] sendValues(long[] a) {
        int i = 0; RootNumber[] array = new RootNumber[a.length];
        while(i < a.length) array[i++] = new RootNumber(a[i]);
        return array;
    }

    @Override
    public RootNumber sendRandomValue(int limit) {
        return new RootNumber(r.nextInt(limit));
    }

    @Override
    public long toLong() {
        return coefficient;
    }

    @Override
    public boolean lessThanZero() {
        return this.toDouble() < 0;
    }
}
