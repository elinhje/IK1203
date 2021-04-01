import java.io.IOException;
import java.net.ServerSocket;
import java.lang.Runnable;

public class ConcHTTPAsk {

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portNumber);

        while(true){
            //Socket clientSocket = serverSocket.accept();
            MyRunnable runner = new MyRunnable(serverSocket.accept());
            Thread thread = new Thread(runner);
            thread.start();
            //hejdå <3
        }
    }
}
