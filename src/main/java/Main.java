import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Test 1
        int perfect = getScore("X|X|X|X|X|X|X|X|X|X||XX");

        //Test 2
        int allNines = getScore("9-|9-|9-|9-|9-|9-|9-|9-|9-|9-||");

        //Test 3
        int allSpares = getScore("5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||5");

        //Test 4
        int score = getScore("X|7/|9-|X|-8|8/|-6|X|X|X||81");

        System.out.println(perfect);
        System.out.println(allNines);
        System.out.println(allSpares);
        System.out.println(score);

        //user can input custom game result here to test
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("enter game result: ");
            String newGame = scanner.nextLine();
            int newScore = getScore(newGame);
            System.out.println(newScore);
        }
    }

    private static int getScore(String input) {
        //convert the input string into a character array;
        char[] array = input.toCharArray();
        //tracking the running score for each roll
        int score = 0;
        //tracking the value of the previous roll
        int preRoll = -1;
        //tracking the value of the roll before the previous roll
        int prePreRoll = -1;

        for (int i = 0; i < array.length; i++) {
            //skip over frame breaks
            if (array[i] == '|') {
                continue;
            }
            //get the value of the current character by helper function
            int value = helper(array[i], preRoll);
            //throw an exception if string contains invalid character
            if(value == -1) {
                throw new IllegalArgumentException("Invalid character: " + array[i]);
            }

            //check the second to the last character to determine if
            //it is a bonus roll, which indicates that the last frame
            //was a strike
            if ((i == array.length - 2 && array[i] != '|')) {
                score += value;
                //check if the second to last frame is a strike,
                //if so add the current value again
                if (prePreRoll == 10) {
                    score += value;
                }
                //skip the normal conditions to avoid repetition
                continue;
            }else{
                //add the current value to the score
                score += value;
            }
            //check if the previous roll is a strike AND the current
            //roll is not bonus roll, if so add the current value again
            if (preRoll == 10 && i != array.length - 1) {
                score += value;
            }
            //check if the roll before the previous roll is a strike AND
            //the current roll is not a bonus roll, if so add the current
            //value again
            if (prePreRoll == 10 && i != array.length - 1) {
                score += value;
            }
            //check if two previous rolls add up to 10 AND the current roll
            //is the first roll of a frame AND not a bonus roll, which indicates
            //a spare,if so add the current value again
            if (preRoll + prePreRoll == 10 && array[i - 1] == '|' && i != array.length - 1) {
                score += value;
            }
            //keep tracking the last two non bonus rolls
            if (i < array.length - 1){
                prePreRoll = preRoll;
                preRoll = value;
            }
        }
        return score;
    }

    private static int helper(char input, int preRoll) {
        //'X' is a strike
        if (input == 'X') {
            return 10;
            //'/' is a spare, but only the number of pins knocked down
            //is returned
        } else if (input == '/') {
            return (10 - preRoll);
            //'-' is a miss
        } else if (input == '-') {
            return 0;
            //convert all number characters from 1 to 9 inclusive to integers
        } else if (input > '0' && input <= '9') {
            return input - '0';
        }
        //return -1 for all invalid characters
        return -1;
    }
}
