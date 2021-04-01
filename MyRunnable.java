import java.net.*;
import java.io.*;
import java.lang.Runnable;
import java.nio.charset.StandardCharsets;

public class MyRunnable implements Runnable{

    /**
     * Creating MyRunnable object.
     */
    Socket clientSocket;
    public MyRunnable(Socket clientSocket) {       //skapa client väl
        this.clientSocket = clientSocket;
    }

    /**
     * Run method implemented. HTTPAsk from task3.
     */
    public void run() {

        int BUFFERSIZE = 1024;

        //responses
        String HTTPHeader = "HTTP/1.1 200 OK\r\n\r\n";
        String badReq = "HTTP/1.1 400 Bad Request\r\n\r\n";
        String notFound = "HTTP/1.1 404 Not Found\r\n\r\n";

        //lista på bra saker att ha
        String host;
        String toServer;
        int port;
        String serverResponse;
        int GET = 0;

        while (true) {

            try {
                clientSocket.setSoTimeout(3000);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            StringBuilder build = new StringBuilder();
            byte[] input = new byte[BUFFERSIZE];

            //en fräsch start va
            host = null;
            toServer = null;
            port = 0;

            try {
                GET = clientSocket.getInputStream().read(input);        //GET int för att kunna läsa block
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                while (GET >= 0) {
                    String message = new String(input, 0, GET, StandardCharsets.UTF_8);
                    build.append(message);
                    GET = clientSocket.getInputStream().read(input);
                }
            } catch (IOException e) {
            }     //skriv ut nåt fint litet meddelande kanske

            String read = build.toString();
            String[] reader = read.split("[?\\&\\=\\ ]");

            //gå igenom
            for (int a = 0; a < reader.length; a++) {
                if (reader[a].equals("hostname")) {
                    host = reader[++a];
                } else if (reader[a].equals("port")) {
                    port = Integer.parseInt(reader[++a]);
                } else if (reader[a].equals("string")) {
                    toServer = reader[++a];
                }
            }

            if (reader[1].equals("/ask") && host != null && port != 0) {
                if (toServer != null) {
                    try {
                        serverResponse = TCPClient.askServer(host, port, toServer);
                        byte[] output = (HTTPHeader + serverResponse).getBytes(StandardCharsets.UTF_8);
                        clientSocket.getOutputStream().write(output);
                    } catch (IOException e) {
                        byte[] output = badReq.getBytes(StandardCharsets.UTF_8);
                        try {
                            clientSocket.getOutputStream().write(output);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                } else {
                    try {
                        serverResponse = TCPClient.askServer(host, port);
                        byte[] output = (HTTPHeader + serverResponse).getBytes(StandardCharsets.UTF_8);
                        clientSocket.getOutputStream().write(output);
                    } catch (IOException e) {
                        byte[] output = badReq.getBytes(StandardCharsets.UTF_8);
                        try {
                            clientSocket.getOutputStream().write(output);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            } else {
                byte[] output = notFound.getBytes(StandardCharsets.UTF_8);
                try {
                    clientSocket.getOutputStream().write(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}