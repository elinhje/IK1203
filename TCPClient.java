//package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    private static int BUFFERSIZE = 1024;

    public static String askServer(String hostname, int port, String ToServer) throws IOException {

        if (ToServer == null) return askServer(hostname, port);

        else{
            Socket clientSocket = new Socket(hostname, port);
            clientSocket.setSoTimeout(2000);

            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();

            byte[] bla = encode(ToServer);
            output.write(bla);

            byte[] buffer = new byte[BUFFERSIZE];
            StringBuilder hepp = new StringBuilder();
            int byteLength = 0;

            while(clientSocket.isConnected() && byteLength != -1){
                try{
                    byteLength = input.read(buffer);
                    if(byteLength != -1) hepp.append(decode(buffer, byteLength));
                }
                catch(Exception ex){
                    byteLength = -1;
                }
            }

            clientSocket.close();

            String ret = hepp.toString();
            return ret;
        }
    }

    public static String askServer(String hostname, int port) throws IOException {
        //return null;
        Socket clientSocket = new Socket(hostname, port);
        clientSocket.setSoTimeout(2000);

        InputStream input = clientSocket.getInputStream();

        byte[] buffer = new byte[BUFFERSIZE];
        StringBuilder hepp = new StringBuilder();
        int byteLength = 0;

        while(clientSocket.isConnected() && byteLength != -1){
            try{
                byteLength = input.read(buffer);
                if(byteLength != -1) hepp.append(decode(buffer, byteLength));
            }
            catch(Exception ex){
                byteLength = -1;
            }
        }

        clientSocket.close();

        String ret = hepp.toString();
        return ret;
    }

    public static String decode(byte[] bla, int length) throws UnsupportedEncodingException{
        String ret = new String(bla, 0, length, "UTF-8");
        return ret;
    }

    public static byte[] encode(String ret) throws UnsupportedEncodingException{
        ret = ret + '\n';
        byte[] bytes = ret.getBytes("UTF-8");
        return bytes;
    }
}
