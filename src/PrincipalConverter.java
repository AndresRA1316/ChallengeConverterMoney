/*Importaciones de las clases*/

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.google.gson.Gson;

public class PrincipalConverter {
    // API_KEY
    private static final String API_KEY = "7dbbd09a3d676413dadd544e";
    public static void main(String[] args) throws IOException, InterruptedException {

        //Scanner
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();

        int option;
        do {
            //Menu de Opciones
            System.out.println("********************************\n ");
            System.out.println("Bienvenid@!, Seleccione una opción: \n");
            System.out.println("1) Dolar =>> Peso argentino");
            System.out.println("2) Peso argentino =>> Dolar");
            System.out.println("3) Dolar =>> Real brasileño");
            System.out.println("4) Real brasileño =>> Dolar");
            System.out.println("5) Dolar =>> Peso colombiano");
            System.out.println("6) Peso colombiano =>> Dolar");
            System.out.println("7) Salir \n");
            System.out.println("********************************\n ");

            option = scanner.nextInt();
            if (option == 7) {
                System.out.println("Saliendo...");
                break;
            }

            // Conversor entrada y salida
            double amount = 0;
            boolean validInput = false;
            do {
                System.out.println("Introduce la cantidad a convertir: ");
                try {
                    amount = scanner.nextDouble();
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Entrada no válida. Por favor,\ningresa el valor con una coma example: 20,20.");
                    scanner.next();
                }
            } while (!validInput);

            // Verificar la moneda seleccionada
            String fromCurrency = "";
            String toCurrency = "";

            switch (option) {
                case 1:
                    fromCurrency = "USD";
                    toCurrency = "ARS";
                    break;
                case 2:
                    fromCurrency = "ARS";
                    toCurrency = "USD";
                    break;
                case 3:
                    fromCurrency = "USD";
                    toCurrency = "BRL";
                    break;
                case 4:
                    fromCurrency = "BRL";
                    toCurrency = "USD";
                    break;
                case 5:
                    fromCurrency = "USD";
                    toCurrency = "COP";
                    break;
                case 6:
                    fromCurrency = "COP";
                    toCurrency = "USD";
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    continue;
            }

            // Estructuracion de la URL de la api
            String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + fromCurrency;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            // Recibir respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Vertificar la respuesta JSON
            Gson gson = new Gson();
            ConverterResponse exchangeRateResponse = gson.fromJson(response.body(), ConverterResponse.class);

            // Moneda verificar si existe
            if (exchangeRateResponse.conversion_rates.containsKey(toCurrency)) {
                double rate = exchangeRateResponse.conversion_rates.get(toCurrency);
                double convertedAmount = amount * rate;
                System.out.println("La cantidad " + amount + " " + fromCurrency + " equivale a " + convertedAmount + " " + toCurrency);
            } else {
                System.out.println("Moneda no encontrada.");
            }
        } while (option != 7);
    }


}