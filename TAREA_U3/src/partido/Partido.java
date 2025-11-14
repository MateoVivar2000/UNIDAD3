package partido;

import java.io.Serializable;
import equipo.Equipo; 

public class Partido implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Equipo competidor1;
    private Equipo competidor2;

    public Partido(Equipo c1, Equipo c2) {
        this.competidor1 = c1;
        this.competidor2 = c2;
    }

    public Equipo getCompetidor1() {
        return competidor1;
    }

    public Equipo getCompetidor2() {
        return competidor2;
    }

    @Override
    public String toString() {
        return competidor1.getNombre() + " vs " + competidor2.getNombre();
    }
}