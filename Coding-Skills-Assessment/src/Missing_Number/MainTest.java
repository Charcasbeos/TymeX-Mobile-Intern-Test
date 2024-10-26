package Missing_Number;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void testFindMissingNumber() {
        // Test case 1
        int[] array1 = {3, 7, 1, 60, 2, 4};
        int expected1 = -1;
        int result1 = Main.findMissingNumber(array1);
        assertEquals(expected1, result1);

        // Test case 2
        int[] array2 = {1, 2, 4, 5, 6};
        int expected2 = 3;
        int result2 = Main.findMissingNumber(array2);
        assertEquals(expected2, result2);

        // Test case 3
        int[] array3 = {2, 3, 4, 5, 6};
        int expected3 = 1;
        int result3 = Main.findMissingNumber(array3);
        assertEquals(expected3, result3);
    }
}
