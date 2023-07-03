import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FileClient {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            String option;

            do {
                Socket socket = new Socket("localhost", 12345);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Mostrar la lista de archivos disponibles
                String line;
                while ((line = in.readLine()) != null && !line.equals("Fin de la lista")) {
                    System.out.println(line);
                }

                // Solicitar al cliente que seleccione un archivo
                System.out.print("Seleccione un archivo: ");
                String fileName = scanner.nextLine();
                out.println(fileName);

                // Solicitar al cliente que seleccione las opciones de transferencia
                String transferOption;
                do {
                    System.out.print("Seleccione las opciones de transferencia (mayusculas/minusculas/original): ");
                    transferOption = scanner.nextLine();
                } while (!transferOption.equalsIgnoreCase("MAYUSCULAS") &&
                        !transferOption.equalsIgnoreCase("MINUSCULAS") &&
                        !transferOption.equalsIgnoreCase("original"));

                out.println(transferOption);

                // Recibir el contenido del archivo del servidor
                File outputFile = new File("C:\\Users\\jared\\OneDrive\\Escritorio\\SistemasPrueba\\Cliente\\" + transferOption + "_" + fileName);  // Especifica la ruta de la carpeta donde deseas guardar el archivo
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

                boolean fileExists = true;
                while ((line = in.readLine()) != null && !line.equals("Fin de la transferencia")) {
                    System.out.println(line);
                    if (line.equals("El archivo solicitado no existe")) {
                        fileExists = false;
                        break;
                    }
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }

                bufferedWriter.close();
                fileOutputStream.close();
                in.close();
                out.close();
                socket.close();

                if (!fileExists) {
                    outputFile.delete();
                } else {
                    System.out.println("Descarga completa: " + outputFile.getAbsolutePath());
                }

                // Preguntar al cliente si desea realizar otra operación
                do {
                    System.out.print("¿Desea realizar otra operación? (si/no): ");
                    option = scanner.nextLine();
                } while (!option.equalsIgnoreCase("SI") && !option.equalsIgnoreCase("NO"));

            } while (option.equalsIgnoreCase("SI"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
