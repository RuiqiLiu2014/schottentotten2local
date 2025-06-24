public class Main {
    // delay between displaying hands
    private static final int delay = 3000;

    public static void main(String[] args) {
        Board board = Board.getInstance();
        Player attacker = new Attacker();
        Player defender = new Defender();
        board.setup(attacker, defender);

        while (true) {
            board.display();
            delay();
            attacker.takeTurn();
            board.declareControl();
            attacker.draw();

            int won = board.won();
            if (won != Constants.noWinner) {
                board.display();
                displayWinner(won);
                break;
            }

            board.display();
            delay();
            defender.takeTurn();
            defender.draw();
        }
    }

    private static void displayWinner(int won) {
        if (won == Constants.attackerWins) {
            System.out.println("Attacker wins");
        } else if (won == Constants.defenderWins) {
            System.out.println("Defender wins");
        } else {
            System.out.println("Game not over yet");
        }
    }

    private static void delay() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}