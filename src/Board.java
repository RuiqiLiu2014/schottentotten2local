public class Board {
    private static Board instance;
    private final Wall[] board;

    private Board() {
        board = new Wall[Constants.numWalls];
        for (int i = 0; i < Constants.numWalls; i++) {
            board[i] = new Wall(Constants.wallLengths[i], Constants.damagedWallLengths[i], Constants.wallPatterns[i], Constants.damagedWallPatterns[i]);
        }
    }

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    public void display() {
        System.out.print("           ATTACKER                       DECK:");
        if (deck.size() < 10) {
            System.out.print("0");
        }
        System.out.print(deck.size() + "                       DEFENDER ");
        for (int i = 0; i < cauldronCount; i++) {
            System.out.print(Constants.CAULDRON);
        }
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            System.out.println(board[i].toString(i + 1));
        }

        System.out.println("------------------------------------------DISCARD------------------------------------------");
        discard.display();
        System.out.println("-------------------------------------------------------------------------------------------");
    }
}
