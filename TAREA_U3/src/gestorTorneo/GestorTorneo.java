package gestorTorneo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import equipo.Equipo; 
import partido.Partido; 

public class GestorTorneo {

    private List<Partido> resultadosEtapa;

    public List<Partido> realizarSorteo(String etapa, List<Equipo> equipos) {
        
        if (equipos.size() % 2 != 0 || equipos.isEmpty()) {
            System.err.println("ERROR: La etapa '" + etapa + "' requiere un número par y positivo de equipos.");
            return new ArrayList<>(); 
        }

        System.out.println("\n---  INICIANDO SORTEO: " + etapa + " ---");
        Collections.shuffle(equipos); 
        
        this.resultadosEtapa = new ArrayList<>(); 
        
     // INICIA LA RECURSIVIDAD
        sortearEtapaRecursivo(equipos); 
        
        return resultadosEtapa;
    }

     //Método RECURSIVO para generar los partidos.
     
    private void sortearEtapaRecursivo(List<Equipo> equiposRestantes) {
        
        // 1.Si no quedan equipos, la recursión se detiene.
        if (equiposRestantes.isEmpty()) {
            return;
        }
        
        // 2. Paso Recursivo: Emparejar y crear objeto Partido.
        Equipo competidor = equiposRestantes.remove(0); 
        
        Random rand = new Random();
        int indiceOponente = rand.nextInt(equiposRestantes.size());
        Equipo oponente = equiposRestantes.remove(indiceOponente); 

        // Crea el objeto Partido
        Partido nuevoPartido = new Partido(competidor, oponente);
        resultadosEtapa.add(nuevoPartido); 
        
        // Salida: Muestra el enfrentamiento
        System.out.println("Partido: " + nuevoPartido.toString()); 

        // 3. Llamada Recursiva: Llama al método con la lista reducida.
        sortearEtapaRecursivo(equiposRestantes);
    }
    
     //Simula los partidos y devuelve una lista de ganadores aleatorios (Clave para encadenar etapas).
  
    public List<Equipo> simularEtapa(List<Partido> partidos) {
        List<Equipo> ganadores = new ArrayList<>();
        Random rand = new Random();
        
        System.out.println("\n--- RESULTADOS DE LA ETAPA ---");
        
        for (Partido partido : partidos) {
            // Selecciona el ganador de forma aleatoria (50/50)
            Equipo ganador = rand.nextBoolean() 
                               ? partido.getCompetidor1()
                               : partido.getCompetidor2();
            
            ganadores.add(ganador);
            System.out.println(partido.toString() + " -> Ganador: " + ganador.getNombre());
        }
        return ganadores;
    }
}