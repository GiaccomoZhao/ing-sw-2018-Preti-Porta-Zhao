package porprezhas;

public class Useful {

    // return a true when the value is between the bound, Including the bound
    public static boolean isValueBetweenInclusive(long x, long lowBound, long highBound) {
        if(x < lowBound)
            return false;
        if(x > highBound)
            return false;
        return true;
    }
    public static boolean isValueBetweenInclusive(double x, double lowBound, double highBound) {
        if(x < lowBound)
            return false;
        if(x > highBound)
            return false;
        return true;
    }

    // return a true when the value is between the bound, Excluding the bound
    public static boolean isValueBetween(long x, long lowBound, long highBound) {
        if(x <= lowBound)
            return false;
        if(x >= highBound)
            return false;
        return true;
    }
    public static boolean isValueBetween(double x, double lowBound, double highBound) {
        if(x <= lowBound)
            return false;
        if(x >= highBound)
            return false;
        return true;
    }

    // return a true when the value is out of bound, Excluding the bound
    public static boolean isValueOutOfBounds(long x, long lowBound, long highBound) {
        return !isValueBetweenInclusive(x, lowBound, highBound);
    }
    public static boolean isValueOutOfBounds(double x, double lowBound, double highBound) {
        return !isValueBetweenInclusive(x, lowBound, highBound);
    }
}
