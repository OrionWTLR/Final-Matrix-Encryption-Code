public class TestClass extends AbstractMathObject<TestClass>{
    private double value;

    TestClass(){
        value = 0;
    }
    TestClass(double a){
        make(a);
    }


    private void make(double a){
        value = a;
    }

    public String toString(){
        return value+"";
    }

    @Override
    TestClass plus(TestClass a) {
        return new TestClass(value + a.value);
    }

    @Override
    TestClass times(TestClass a) {
        return new TestClass(value * a.value);
    }

    @Override
    TestClass minus(TestClass a) {
        return new TestClass(value - a.value);
    }

    @Override
    TestClass divide(TestClass a) {
        return new TestClass(value/a.value);
    }

    @Override
    TestClass plus(long a) {
        return plus(new TestClass(a));
    }

    @Override
    TestClass times(long a) {
        return times(new TestClass(a));
    }

    @Override
    TestClass minus(long a) {
        return minus(new TestClass(a));
    }

    @Override
    TestClass divide(long a) {
        return divide(new TestClass(a));
    }

    @Override
    boolean equalTo(TestClass a) {
        return value == a.value;
    }

    @Override
    boolean notEqualTo(TestClass a) {
        return !equalTo(a);
    }

    @Override
    boolean lessThan(TestClass a) {
        return value  < a.value;
    }

    @Override
    boolean greaterThan(TestClass a) {
        return value  > a.value ;
    }

    @Override
    public int compareDenominator(TestClass a){
        return 0;
    }
    public TestClass getDenominator(){
        return new TestClass(1);
    }

    @Override
    TestClass mod(long m) {

        return new TestClass(value%m);
    }

    @Override
    TestClass pow(long m) {
        return new TestClass(Math.pow(value, m));
    }

    @Override
    TestClass sqrt() {
        return new TestClass(Math.pow(value, 0.5));
    }

    @Override
    TestClass inverse() {
        return new TestClass(1/ value);
    }

    @Override
    TestClass copy() {
        return new TestClass(value);
    }

    @Override
    TestClass sendValue(long a) {
        return new TestClass(a);
    }

    @Override
    TestClass sendValue(long a, long b) {
        return new TestClass(a);
    }

    @Override
    TestClass[] sendValues(long[] a) {
        TestClass[] val = new TestClass[a.length];
        int i =0; for(long l : a) val[i++] = new TestClass(l);
        return val;
    }

    @Override
    TestClass sendRandomValue(int limit) {
        return new TestClass(r.nextInt(limit));
    }

    @Override
    long toLong() {
        return (long) value;
    }

    @Override
    boolean lessThanZero() {
        return value < 0;
    }
}
