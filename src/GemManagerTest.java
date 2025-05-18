/**
 * GemManagerTest Class
 * @author Mustafa Alismaeli
 * CS251 GemManager Part 1
 * Test class for checking and verifying the gem manager
 * Creates a new game, with specified rows, columns, the gemtypes,
 * and a random seed
 *
 * I tried to follow the tester insturctions hopefully didn't miss anything,
 * should've demonstrated everything works
 */
public class GemManagerTest {
    public static void main(String[] args) {
        // gem types that will be used for this game
        char[] gemTypes = {'◆', '⟡', '◼'};
        // creates two games
        GemManager gm1 = new GemManager(5, 7, gemTypes, 42);
        GemManager gm2 = new GemManager(10, 15,
                new char[]{'+', '*', '-', '.'}, 99);

        // Creating game 1
        System.out.println("Initial board 1:");
        System.out.println(gm1);

        System.out.println("Swapping (2,5) and (3,5):");
        gm1.swap(2, 5, 3, 5);
        System.out.println(gm1);
        // this should get 3 in a row
        System.out.println("Swapping (4,4) and (3,4):");
        gm1.swap(4, 4, 3, 4);
        System.out.println(gm1);

        int removed;
        do {
            removed = gm1.removeMatches();
            if (removed > 0) {
                System.out.println(removed + " gems removed:");
                System.out.println(gm1);
            }
        } while (removed > 0);


//       Second game
        System.out.println("Initial board 2:");
        System.out.println(gm2);
        System.out.println("Swapping (6,7) and (7,7):");
        gm2.swap(6, 7, 7, 7);
        System.out.println(gm2);


        // Keeps removing matches until a valid game board is found
        do {
            removed = gm2.removeMatches();
            if (removed > 0) {
                System.out.println(removed + " gems removed:");
                System.out.println(gm2);
            }
        } while (removed > 0);


        // goes back to game 1 to ensure its seperate
        System.out.println("Testing board 1 again:");
        System.out.println(gm1);
        gm1.swap(2, 3, 3, 3);
        System.out.println(gm1);
    }
}
