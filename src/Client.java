import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("192.168.7.222", 12345);
        System.out.println("connected");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Scanner scan = new Scanner(System.in);

        while(true) {
            // opponent's turn
            System.out.println("waiting for opponent");
            String opponentMove = in.readLine();
            if (opponentMove == null) {
                break;
            }
            System.out.println("opponent played: " + opponentMove);

            // your turn
            System.out.print("your move: ");
            String move = scan.nextLine();
            out.println(move); // send move
        }
        socket.close();
    }
}
