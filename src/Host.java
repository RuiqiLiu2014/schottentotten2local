import java.util.*;
import java.io.*;
import java.net.*;

public class Host {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("waiting for connection");
        Socket socket = serverSocket.accept();
        System.out.println("connected");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Scanner scan = new Scanner(System.in);

        while (true) {
            // your turn
            System.out.print("your move: ");
            String move = scan.nextLine();
            out.println(move); // send move
            System.out.println("waiting for opponent");

            // opponent's turn
            String opponentMove = in.readLine();
            if (opponentMove == null) {
                break;
            }
            System.out.println("opponent played: " + opponentMove);
        }

        socket.close();
        serverSocket.close();
    }
}
