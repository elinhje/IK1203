/**
 * Written by Elin Hjelmestam 2021-02-23
 *
 * This code was generated as part of examination of KTH course IK1203.
 */


import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HTTPEcho {

    private static int BUFFERSIZE = 1024 * 8;

    public static void main( String[] args) throws IOException {

        int port = Integer.parseInt(args[0]);
        //ServerSocket clientSocket = new ServerSocket(port);

        String HTTPHeader = "HTTP/1.1 200 OK\r\n\r\n";      //använd d här!!!!!

        try{
            ServerSocket serverSocket = new ServerSocket(port);
            int length;

            while(true){
                Socket clientSocket = serverSocket.accept();

                StringBuilder build = new StringBuilder();
                byte[] input = new byte[BUFFERSIZE];
                InputStream client = clientSocket.getInputStream();

                while(client.available() > 0){
                    length = client.read(input);
                    build.append(new String(input, 0, length, StandardCharsets.UTF_8));
                }

                String message = (HTTPHeader) + build.toString();
                byte[] output = message.getBytes(StandardCharsets.UTF_8);
                clientSocket.getOutputStream().write(output, 0, output.length);
                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

