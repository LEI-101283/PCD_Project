
package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import iscot.events.Event;
import iscot.events.RegistrationEvent;

public class Server {

	private static final int PORT = 55000;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Servidor iniciado na porta " + PORT);
            System.out.println("À espera de clientes...");

            while (true) {

                Socket clientSocket = serverSocket.accept();

                System.out.println(
                    "Novo cliente ligado: " +
                    clientSocket.getInetAddress()
                );

                try {

                    ObjectOutputStream output =
                        new ObjectOutputStream(clientSocket.getOutputStream());

                    output.flush();

                    ObjectInputStream input =
                        new ObjectInputStream(clientSocket.getInputStream());

                    Event evento = (Event) input.readObject();

                    if (evento instanceof RegistrationEvent) {

                        RegistrationEvent registo =
                            (RegistrationEvent) evento;

                        System.out.println(
                            "Cliente registado: " +
                            registo.getDeviceType() +
                            " | Tipo: " + registo.getType() +
                            " | Área: " + registo.getArea()
                        );

                        // Confirmar o registo
                        output.writeObject(Boolean.TRUE);
                        output.flush();

                        System.out.println("Registo confirmado!");

                    } else {

                        output.writeObject(Boolean.FALSE);
                        output.flush();

                    }

                } catch (IOException | ClassNotFoundException e) {

                    System.err.println(
                        "Erro ao comunicar com o cliente: " + e.getMessage()
                    );

                }

            }

        } catch (IOException e) {

            System.err.println("Erro no servidor: " + e.getMessage());

        }
    }
}


