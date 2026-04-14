package com.activity;

import com.activity.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {

    private static final int PORT = 5000;
    private static final int POOL_SIZE = 5;

    public static void main(String[] args) {
        // Création du pool de threads fixe (5 threads max)
        ExecutorService threadPool = Executors.newFixedThreadPool(POOL_SIZE);

        System.out.println("Serveur démarré sur le port " + PORT);
        System.out.println("En attente de connexions...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            // Boucle infinie pour accepter les connexions
            while (true) {
                // Attente bloquante d'un nouveau client
                Socket clientSocket = serverSocket.accept();

                // Affichage de l'adresse IP du client
                String clientIP = clientSocket.getInetAddress().getHostAddress();
                System.out.println("Client connecté : " + clientIP);

                // Soumission du client au pool de threads
                threadPool.submit(new ClientHandler(clientSocket));
            }

        } catch (IOException e) {
            System.err.println("Erreur du serveur : " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }
}