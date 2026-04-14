package com.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        // Tâche 3 : Affichage du thread et de l'adresse IP
        String threadName = Thread.currentThread().getName();
        String clientIP = clientSocket.getInetAddress().getHostAddress();

        System.out.println("Thread " + threadName + " traite le client");

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // Message de bienvenue
            writer.println("Bienvenue sur le serveur ! (tapez 'bye' pour quitter)");

            String message;

            // Lecture des messages du client
            while ((message = reader.readLine()) != null) {

                // Nettoyage du message (Telnet peut envoyer \r)
                message = message.trim();

                // Tâche 3 : Journalisation
                System.out.println("[" + threadName + " | " + clientIP + "] Message reçu : " + message);

                // Tâche 2 : Réponse selon le message reçu
                switch (message.toLowerCase()) {
                    case "hello":
                        writer.println("Bonjour client !");
                        break;

                    case "time":
                        String dateTime = LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                        writer.println(dateTime);
                        break;

                    case "bye":
                        writer.println("Connexion fermée");
                        System.out.println("[" + threadName + "] Client " + clientIP + " déconnecté.");
                        return; // Sort de run() → ferme uniquement cette connexion

                    default:
                        writer.println("Message reçu : " + message);
                        break;
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur avec le client " + clientIP + " : " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erreur fermeture socket : " + e.getMessage());
            }
        }
    }
}
