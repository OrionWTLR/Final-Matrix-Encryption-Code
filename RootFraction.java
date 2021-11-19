public class RootFraction extends AbstractMathObject<RootFraction>{
    private RootNumber numerator;
    private RootNumber denominator;
    private final RootNumber ZERO = new RootNumber(0);
    private final RootNumber ONE = new RootNumber(1);

    RootFraction(){
        numerator = new RootNumber(0);
        denominator = new RootNumber(1);
    }
    RootFraction(long a){
        make(new RootNumber(a), new RootNumber(1));
    }
    RootFraction(long a, long b){
        make(new RootNumber(a), new RootNumber(b));
    }
    RootFraction(RootNumber n){
        make(n, ONE);
    }
    RootFraction(RootNumber n, RootNumber d){
        make(n,d);
    }
    RootFraction(RootFraction rf){
        numerator = rf.numerator;
        denominator = rf.denominator;
    }

    private void make(RootNumber n, RootNumber d){
        Fraction constantFraction = new Fraction(n.constant(), d.constant()).simplestForm();

        if(n.exponent().equalTo(d.exponent())){
            Fraction baseFraction = new Fraction(n.base(), d.base()).simplestForm();
            numerator = new RootNumber(constantFraction.numerator(), baseFraction.numerator(), n.exponent());
            denominator = new RootNumber(constantFraction.denominator(), baseFraction.denominator(), d.exponent());
            return;
        }

        numerator = new RootNumber(constantFraction.numerator(), n.base(), n.exponent());
        denominator = new RootNumber(constantFraction.denominator(), d.base(), d.exponent());
    }

    public String toString(){
        if(denominator.equalTo(ONE)) return numerator+"";
        //return "("+numerator+"/"+denominator+")";
        return ""+numerator+"/"+denominator+"";
    }

    public void print(){
        System.out.println(this);
    }
    public void printFull(){
        System.out.print(this.numerator+"/"+this.denominator);
    }
    public void printFullLine(){
        System.out.println(this.numerator+"/"+this.denominator);
    }

    void simplify(){
        if(numerator.equalTo(new RootNumber(0))){
            denominator = new RootNumber(1);
        }

    }

    public double toDouble(){
       return numerator.toDouble()/denominator.toDouble();
    }

    @Override
    RootFraction plus(RootFraction number) {
        number.simplify();

        if(number.numerator.isZero()) return this;
        if(numerator.isZero()) return number;

        if(numerator.isWhole() && number.numerator.isWhole() && denominator.isWhole() && number.denominator.isWhole()){
            Fraction f = new Fraction(numerator.constant(), denominator.constant()), g = new Fraction(number.numerator.constant(), number.denominator.constant());
            Fraction fg = f.plus(g).simplestForm();
            return new RootFraction(new RootNumber(fg.numerator()), new RootNumber(fg.denominator()));
        }

        RootNumber a = numerator,
                b = denominator,
                x = number.numerator,
                y = number.denominator;
        return new RootFraction(a.times(y).plus(b.times(x)), b.times(y));
    }


    @Override
    RootFraction times(RootFraction number) {
        number.simplify();

        if(numerator.isWhole() && number.numerator.isWhole() && denominator.isWhole() && number.denominator.isWhole()){
            Fraction f = new Fraction(numerator.constant(), denominator.constant()), g = new Fraction(number.numerator.constant(), number.denominator.constant());
            Fraction fg = f.times(g).simplestForm();
            return new RootFraction(new RootNumber(fg.numerator()), new RootNumber(fg.denominator()));
        }

        RootNumber a = numerator,
                b = denominator,
                x = number.numerator,
                y = number.denominator;
        return new RootFraction(a.times(x), b.times(y));
    }

    @Override
    RootFraction minus(RootFraction a) {
        return this.plus(a.times(-1));
    }

    @Override
    RootFraction divide(RootFraction a) {
        return this.times(new RootFraction(a.denominator, a.numerator));
    }

    @Override
    RootFraction plus(long a) {
        return this.plus(new RootFraction(a));
    }

    @Override
    RootFraction times(long a) {
        return new RootFraction(numerator.times(a), denominator);
    }

    @Override
    RootFraction minus(long a) {
        return this.minus(new RootFraction(a));
    }

    @Override
    RootFraction divide(long a) {
        return divide(new RootFraction(a));
    }

    @Override
    boolean equalTo(RootFraction a) {
        return this.numerator.equalTo(a.numerator) && this.denominator.equalTo(a.denominator);
    }

    boolean notEqualTo(RootFraction a) {
        return !equalTo(a);
    }

    @Override
    boolean lessThan(RootFraction a) {
        return (this.numerator.toDouble()/this.denominator.toDouble()) < (a.numerator.toDouble()/a.denominator.toDouble());
    }

    @Override
    boolean greaterThan(RootFraction a) {
        return (this.numerator.toDouble()/this.denominator.toDouble()) > (a.numerator.toDouble()/a.denominator.toDouble());
    }

    public int compareDenominator(RootFraction a){
        if(denominator.lessThan(a.denominator)) return -1;
        if(denominator.equalTo(a.denominator)) return 0;
        return 1;
    }
    public RootFraction getDenominator(){
        return new RootFraction(denominator);
    }

    @Override
    RootFraction mod(long m) {
        if(!numerator.isWhole() || !denominator.isWhole()) throw new IllegalStateException("Attempt at root modular operation");

        Fraction f = new Fraction(numerator.toLong(), denominator.toLong());
        f = f.mod(m);

        return new RootFraction(f.numerator(), f.denominator());
    }

    @Override
    RootFraction pow(long n) {
        return new RootFraction(numerator.copy().pow(n), denominator.copy().pow(n));
    }

    @Override
    RootFraction sqrt() {
        return new RootFraction(numerator.sqrt(), denominator.sqrt());
    }

    @Override
    RootFraction inverse() {
        return new RootFraction(denominator, numerator);
    }

    @Override
    RootFraction copy() {
        return new RootFraction(numerator.copy(), denominator.copy());
    }

    @Override
    RootFraction sendValue(long a) {
        return new RootFraction(a);
    }

    @Override
    RootFraction sendValue(long a, long b) {

        return new RootFraction(a, b);
    }

    @Override
    RootFraction[] sendValues(long[] a) {
        RootFraction[] fractions = new RootFraction[a.length];
        int i = 0;
        for(long l : a){
            fractions[i++] = new RootFraction(l);
        }
        return fractions;
    }

    @Override
    RootFraction sendRandomValue(int limit) {
        return new RootFraction(r.nextInt(limit));
    }

    @Override
    long toLong() {
        return numerator.toLong();
    }

    @Override
    boolean lessThanZero() {
        return this.toDouble() < 0;
    }
}
