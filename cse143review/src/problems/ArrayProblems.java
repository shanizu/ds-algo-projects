package problems;

/**
 * See the spec on the website for example behavior.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - Do not add any additional imports
 * - Do not create new `int[]` objects for `toString` or `rotateRight`
 */
public class ArrayProblems {

    /**
     * Returns a `String` representation of the input array.
     * Always starts with '[' and ends with ']'; elements are separated by ',' and a space.
     */
    public static String toString(int[] array) {
        StringBuilder result = new StringBuilder("[");
        if (array.length > 0) {
            result.append(array[0]);
            for (int i = 1; i < array.length; i++) {
                result.append(", ").append(array[i]);
            }
        }
        return result + "]";
    }

    /**
     * Returns a new array containing the input array's elements in reversed order.
     * Does not modify the input array.
     */
    public static int[] reverse(int[] array) {
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[array.length - (i + 1)];
        }
        return result;
    }

    /**
     * Rotates the values in the array to the right.
     */
    public static void rotateRight(int[] array) {
        int size = array.length;
        if (size > 1) {
            int temp = array[size - 1];
            for (int i = 1; i < size; i++) {
                array[size - i] = array[size - (i + 1)];
            }
            array[0] = temp;
        }
    }
}
