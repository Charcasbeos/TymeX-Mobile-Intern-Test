package Missing_Number;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] array = new int[]{3,7,1,6,2,4};
        int n = 6;
        int missingNumber = findMissingNumber(array);
        if(missingNumber == -1){
            System.out.println("Invalid array");
        }else {
        System.out.println("Missing number: "+missingNumber);
        }
    }
    public static int findMissingNumber(int[] nums) {
        int missingNumber = -1;
        int expectedSum = (nums.length+1)*(nums.length+2)/2;
        int actualSum = 0;
        actualSum = Arrays.stream(nums).sum();
        missingNumber = actualSum > expectedSum ? missingNumber : expectedSum - actualSum;
        return missingNumber;
    }
}
