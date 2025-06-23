public class Local {
    public static void main(String[] args) {
        Board board = Board.getInstance();
        Player attacker = new Attacker();
        Player defender = new Defender();
        board.setup(attacker, defender);

        while (true) {
            board.display();
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
            defender.takeTurn();
            defender.draw();
        }
    }

    public static void displayWinner(int won) {
        if (won == Constants.attackerWins) {
            System.out.println("Attacker wins");
        } else if (won == Constants.defenderWins) {
            System.out.println("Defender wins");
        } else {
            System.out.println("Game not over yet");
        }
    }
}