package gestorArchivos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import equipo.Equipo;
import partido.Partido;
import numeroInvalidoEquiposException.NumeroInvalidoEquiposException; 

public class GestorArchivos {
    
//Este método se mantiene por si se quiere volver a la carga por archivo. 
//Se reemplaza en MainApp por la entrada de consola.
     
    public List<Equipo> cargarEquiposDesdeArchivo(String nombreArchivo) 
            throws NumeroInvalidoEquiposException, IOException { 
        
        List<Equipo> equipos = new ArrayList<>();
        throw new FileNotFoundException("La carga de equipos desde archivo fue reemplazada por la entrada de consola.");
    }
    
//Serializa (guarda) la lista de partidos en un archivo binario.
    
    public void guardarPartidosSerializados(String nombreArchivo, List<Partido> partidos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(partidos);
            System.out.println("✅ Partidos guardados por serialización en: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("❌ Error de Serialización (escritura): " + e.getMessage());
        }
    }

//Deserializa (lee) la lista de partidos desde un archivo binario (Clase 15).
    
    @SuppressWarnings("unchecked") 
    public List<Partido> cargarPartidosSerializados(String nombreArchivo) {
        List<Partido> partidosLeidos = new ArrayList<>();
        
        System.out.println("\n--- Verificando resultados guardados en: " + nombreArchivo + " ---");
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            
            partidosLeidos = (List<Partido>) ois.readObject(); 
            System.out.println("✅ Partidos cargados exitosamente para verificación.");
            
        } catch (FileNotFoundException e) {
            System.err.println("❌ Error: El archivo de resultados '" + nombreArchivo + "' no ha sido encontrado.");
        } catch (IOException e) {
            System.err.println("❌ Error de Deserialización (lectura): " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: Una de las clases necesarias no fue encontrada.");
        } catch (ClassCastException e) {
            System.err.println("❌ Error: El objeto leído no tiene el formato esperado.");
        }
        
        return partidosLeidos;
    }
}