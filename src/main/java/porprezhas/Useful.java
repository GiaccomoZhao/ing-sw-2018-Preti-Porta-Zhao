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



    public static double getMaxBetween(double firstValue, double secondValue){
        return firstValue > secondValue ? firstValue : secondValue;
    }

    public static double getMinBetween(double firstValue, double secondValue){
        return firstValue < secondValue ? firstValue : secondValue;
    }



    public static StringBuilder appendSpaces(StringBuilder sb, int nSpace) {
        for (int i = 0; i < nSpace; i++) {
            sb.append(' ');
        }
        return sb;
    }


    /**
     * Force Convert the type of the Object into clazz's Type
     * @param o     the Object to be converted
     * @param clazz the Class -Type- you want convert to be, example: String.class
     * @param <T>   Generic Type of class
     * @return Object o Casted in type of clazz, if there is not exception
     *         null, otherwise
     */
    public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return clazz.cast(o);
        } catch (ClassCastException e) {
            return null;
        }
    }
}
