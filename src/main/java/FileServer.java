import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor esperando conexiones...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Manejar la solicitud del cliente en un hilo separado
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Enviar la lista de archivos disponibles
            out.println("Lista de archivos disponibles:");
            File folder = new File("C:\\Users\\jared\\OneDrive\\Escritorio\\SistemasPrueba\\Servidor");
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        out.println(file.getName());
                    }
                }
            }
            out.println("Fin de la lista");

            // Leer la selección del cliente
            String fileName = in.readLine();

            // Leer las opciones de transferencia del cliente
            String transferOption = in.readLine();

            // Enviar el contenido del archivo solicitado
            File selectedFile = new File("C:\\Users\\jared\\OneDrive\\Escritorio\\SistemasPrueba\\Servidor" + File.separator + fileName);
            if (selectedFile.exists() && selectedFile.isFile()) {
                BufferedReader fileReader = new BufferedReader(new FileReader(selectedFile));
                String line;
                while ((line = fileReader.readLine()) != null) {
                    if (transferOption.equalsIgnoreCase("MAYUSCULAS")) {
                        line = line.toUpperCase();
                    } else if (transferOption.equalsIgnoreCase("MINUSCULAS")) {
                        line = line.toLowerCase();
                    }
                    out.println(line);
                }
            } else {
                out.println("El archivo solicitado no existe");
            }

            out.println("Fin de la transferencia");

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
