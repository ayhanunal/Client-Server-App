package com.ayhanunal.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static final int maxClientCount = 10;
    private static final ClientThread[] threads = new ClientThread[maxClientCount];

    public static void main(String args[]) {

        int portNo = 0;
        if (args.length < 1) {
            System.out.println("Port:: " + portNo);
        } else {
            portNo = Integer.valueOf(args[0]).intValue();
        }

        try {
            serverSocket = new ServerSocket(portNo);
        } catch (IOException e) {
            e.getLocalizedMessage();
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClientCount; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new ClientThread(clientSocket, threads)).start();
                        break;
                    }
                }
                if (i == maxClientCount) {
                    PrintStream ps = new PrintStream(clientSocket.getOutputStream());
                    ps.println("Server full!!");
                    ps.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }
    }
}

class ClientThread extends Thread {

    private DataInputStream dis = null;
    private PrintStream ps = null;
    private Socket clientSocket = null;
    private final ClientThread[] threads;
    private int maxClientCount;

    public ClientThread(Socket clientSocket, ClientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientCount = threads.length;
    }

    @Override
    public void run() {
        int maxClientCount = this.maxClientCount;
        ClientThread[] threads = this.threads;

        try {
            dis = new DataInputStream(clientSocket.getInputStream());
            ps = new PrintStream(clientSocket.getOutputStream());
            ps.println("Server");
            String name = dis.readLine().trim();
            ps.println("Port");
            for (int i = 0; i < maxClientCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].ps.println(name);
                }
            }
            while (true) {
                String row = dis.readLine();
                if (row.startsWith("/quit")) {
                    break;
                }
                for (int i = 0; i < maxClientCount; i++) {
                    if (threads[i] != null) {
                        threads[i].ps.println("<" + name + ">: " + row);
                    }
                }
            }
            for (int i = 0; i < maxClientCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].ps.println(name + "client end");
                }
            }

            for (int i = 0; i < maxClientCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }
            dis.close();
            ps.close();
            clientSocket.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
        }
    }
}
