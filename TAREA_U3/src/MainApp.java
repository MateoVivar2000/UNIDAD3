import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Importamos las clases de tus paquetes
import equipo.Equipo;
import partido.Partido;
import gestorTorneo.GestorTorneo;
import gestorArchivos.GestorArchivos;
import numeroInvalidoEquiposException.NumeroInvalidoEquiposException;

public class MainApp {

    // Método auxiliar para verificar si un equipo ya existe en la lista
    private static boolean contieneEquipo(List<Equipo> equipos, String nombre) {
        for (Equipo equipo : equipos) {
            if (equipo.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

   //Pide los equipos por consola basándose en la cantidad requerida (maxEquipos).
    private static List<Equipo> pedirEquiposPorConsola(Scanner scanner, int maxEquipos, String etapa) {
        List<Equipo> equipos = new ArrayList<>();
        
        System.out.println("\n--- INGRESO DE EQUIPOS PARA " + etapa.toUpperCase() + " ---");
        System.out.println("Por favor, ingresa los nombres de los " + maxEquipos + " equipos uno por uno:");

        for (int i = 0; i < maxEquipos; i++) {
            System.out.print("Equipo " + (i + 1) + ": ");
            String nombre = scanner.nextLine().trim();
            
            if (nombre.isEmpty()) {
                System.out.println("El nombre del equipo no puede estar vacío. Intenta de nuevo.");
                i--; 
            } 
            else if (contieneEquipo(equipos, nombre)) { 
                System.out.println("⛔ ¡ATENCIÓN! El equipo '" + nombre + "' ya fue ingresado. Intenta con un nombre diferente.");
                i--; 
            }
            else {
                equipos.add(new Equipo(nombre));
            }
        }
        return equipos;
    }
    
    public static void main(String[] args) {
        
        GestorTorneo gestorTorneo = new GestorTorneo();
        GestorArchivos gestorArchivos = new GestorArchivos();
        Scanner scanner = new Scanner(System.in);
        List<Equipo> equiposCargados = null;
        int numEquiposInicial = 0;
        
        try {
            System.out.println("--- LIGA PROFESIONAL DE FÚTBOL ---");
            
            // 1. Preguntar la Etapa 
            System.out.print("Ingrese la etapa inicial (octavos, cuartos, o semifinales): ");
            String etapa = scanner.nextLine().trim().toLowerCase();
            
            // 2. Determinar la cantidad de equipos necesarios
            switch (etapa) {
                case "octavos":
                    numEquiposInicial = 16;
                    break;
                case "cuartos":
                    numEquiposInicial = 8;
                    break;
                case "semifinales":
                    numEquiposInicial = 4;
                    break;
                default:
                    System.err.println("❌ ERROR: Etapa no reconocida. Inicie el programa nuevamente.");
                    return; // Termina la ejecución
            }
            
            // 3. Pide los equipos según la etapa 
            equiposCargados = pedirEquiposPorConsola(scanner, numEquiposInicial, etapa);
            
            // 4. Validación de regla de negocio (Número par de equipos para el sorteo)
            if (equiposCargados.size() % 2 != 0 || equiposCargados.isEmpty()) {
                 throw new NumeroInvalidoEquiposException("La cantidad de equipos (" + equiposCargados.size() + ") no es válida para iniciar un sorteo.");
            }
            
            System.out.println("\nCarga exitosa. Iniciando sorteo desde " + etapa.toUpperCase() + ".");

            // 5. Encadenar el Torneo a partir de la etapa inicial ingresada
            List<Equipo> equiposActuales = new ArrayList<>(equiposCargados);
            String etapaActual = etapa;
            
            // Bucle que continúa mientras haya más de un equipo
            while (equiposActuales.size() > 1) {
                
                // Mapear el nombre de la etapa para la salida
                if (equiposActuales.size() == 8) etapaActual = "Cuartos de Final";
                else if (equiposActuales.size() == 4) etapaActual = "Semifinales";
                else if (equiposActuales.size() == 2) etapaActual = "Final";
                
                // 5a. Sorteo Recursivo
                List<Partido> partidosRonda = gestorTorneo.realizarSorteo(etapaActual, equiposActuales);
                
                // 5b. Simulación y Avance
                List<Equipo> ganadoresRonda = gestorTorneo.simularEtapa(partidosRonda);
                equiposActuales = ganadoresRonda; 
                
                if (equiposActuales.size() == 1) {
                    System.out.println("\n🏆 ¡EL CAMPEÓN DEL TORNEO ES: " + ganadoresRonda.get(0).getNombre() + "!");
                    
                    // Serialización solo de la Final
                    String nombreArchivoSerializado = "resultados_finales.bin";
                    gestorArchivos.guardarPartidosSerializados(nombreArchivoSerializado, partidosRonda);
                    
                    // Deserialización de Verificación
                    List<Partido> resultadosCargados = gestorArchivos.cargarPartidosSerializados(nombreArchivoSerializado);
                    if (!resultadosCargados.isEmpty()) {
                        System.out.println("El último partido guardado es: " + resultadosCargados.get(0).toString());
                    }
                }
            }
            
        } catch (NumeroInvalidoEquiposException e) {
            System.err.println("\n ERROR DE REGLA DE NEGOCIO: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\n Ocurrió un error crítico inesperado: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            System.out.println("\n--- PROGRAMA FINALIZADO ---");
        }
    }
}