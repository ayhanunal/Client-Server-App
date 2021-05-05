package com.ayhanunal.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    private static Socket clientSocket = null;
    private static PrintStream ps = null;
    private static DataInputStream dis = null;
    private static BufferedReader postdata = null;
    private static boolean isClose = false;

    public static void main(String[] args) {

        int portNo = 0;
        String host = "localhost";

        if (args.length < 2) {
            System.out.println(portNo);
        } else {
            host = args[0];
            portNo = Integer.valueOf(args[1]).intValue();
        }




        try {
            clientSocket = new Socket(host, portNo);
            postdata = new BufferedReader(new InputStreamReader(System.in));
            ps = new PrintStream(clientSocket.getOutputStream());
            dis = new DataInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Connection Problems..");
        }




        if (clientSocket != null && ps != null && dis != null) {
            try {
                new Thread(new Client()).start();
                while (!isClose) {
                    ps.println(postdata.readLine().trim());
                }
                ps.close();
                dis.close();
                clientSocket.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void run() {
        String response;
        try {
            while ((response = dis.readLine()) != null) {
                System.out.println(response);
                if (response.indexOf("0") != -1) {
                    break;
                }
            }
            isClose = true;
        } catch (IOException e) {
            e.getLocalizedMessage();
        }
    }
}
