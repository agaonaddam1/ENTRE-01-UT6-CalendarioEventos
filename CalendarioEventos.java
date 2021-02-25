 
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
/**
 * Esta clase modela un sencillo calendario de eventos.
 * 
 * Por simplicidad consideraremos que los eventos no se solapan
 * y no se repiten
 * 
 * El calendario guarda en un map los eventos de una serie de meses
 * Cada mes (la clave en el map, un enumerado Mes) tiene asociados 
 * en una colección ArrayList los eventos de ese mes
 * 
 * Solo aparecen los meses que incluyen algún evento
 * 
 * Las claves se recuperan en orden alfabético
 * 
 * @author Ander Gaona
 * 
 */
public class CalendarioEventos {
    private TreeMap<Mes, ArrayList<Evento>> calendario;

    /**
     * el constructor
     */
    public CalendarioEventos() {
        this.calendario = new TreeMap<>();
    }

    /**
     * añade un nuevo evento al calendario
     * Si la clave (el mes del nuevo evento) no existe en el calendario
     * se creará una nueva entrada con dicha clave y la colección formada
     * por ese único evento
     * Si la clave (el mes) ya existe se añade el nuevo evento insertándolo de forma
     * que quede ordenado por fecha y hora de inicio
     * 
     * Pista! Observa que en la clase Evento hay un método antesDe() que vendrá
     * muy bien usar aquí
     */
    public void addEvento(Evento nuevo) {
        if (calendario.containsKey(nuevo.getMes())) {
            calendario.get(nuevo.getMes()).add(indiceInsercion(nuevo), nuevo);
        }
        else {
            ArrayList<Evento> eventos = new ArrayList<>();
            eventos.add(nuevo);
            calendario.put(nuevo.getMes(), eventos);
        }
    }

    /**
     * 
     */
    private int indiceInsercion(Evento eventoA) {
        for (int i = 0; i < calendario.get(eventoA.getMes()).size(); i++){
            Evento eventoB = calendario.get(eventoA.getMes()).get(i);
            if(eventoA.antesDe(eventoB)){
                return i;
            }
        }
        return calendario.get(eventoA.getMes()).size();
    }

    /**
     * Representación textual del calendario
     * Hacer de forma eficiente 
     * Usar el conjunto de entradas  
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<Mes, ArrayList<Evento>>> entradas = calendario.entrySet();
        Iterator<Map.Entry<Mes, ArrayList<Evento>>> it = entradas.iterator();
        while(it.hasNext()){
            Map.Entry<Mes, ArrayList<Evento>> entrada = it.next();
            sb.append(entrada.getKey().toString() + "\n\n");
            for(Evento evento: entrada.getValue()){
                sb.append(evento.toString());
                sb.append("\n");
            }
            sb.append("\n\n");
        }
        return sb.toString();
    }

    /**
     * Dado un mes devolver la cantidad de eventos que hay en ese mes
     * Si el mes no existe se devuelve 0
     */
    public int totalEventosEnMes(Mes mes) {
        if(calendario.containsKey(mes)){
            return calendario.get(mes).size();
        }
        return 0;
    }

    /**
     * Devuelve un conjunto (importa el orden) 
     * con los meses que tienen mayor nº de eventos
     * Hacer un solo recorrido del map con el conjunto de claves
     *  
     */
    public TreeSet<Mes> mesesConMasEventos() {
        TreeSet<Mes> meses = new TreeSet<>();
        Set<Mes> keys = calendario.keySet();
        Iterator<Mes> it = keys.iterator();
        while(it.hasNext()){
            Mes key = it.next();
            if(totalEventosEnMes(key) == maxEventos()){
                meses.add(key);
            }
        }
        return meses;
    }

    /**
     * 
     */
    private int maxEventos() {
        int max = 0;
        Set<Mes> keys = calendario.keySet();
        Iterator<Mes> it = keys.iterator();
        while(it.hasNext()){
            Mes key = it.next();
            if(totalEventosEnMes(key) > max){
                max = totalEventosEnMes(key);
            }
        }
        return max;
    }

    /**
     * Devuelve el nombre del evento de mayor duración en todo el calendario
     * Se devuelve uno solo (el primero encontrado) aunque haya varios
     */
    public String eventoMasLargo() {
        Set<Map.Entry<Mes, ArrayList<Evento>>> entradas = calendario.entrySet();
        Iterator<Map.Entry<Mes, ArrayList<Evento>>> it = entradas.iterator();
        while(it.hasNext()){
            Map.Entry<Mes, ArrayList<Evento>> entrada = it.next();
            for(Evento evento: entrada.getValue()){
                if(evento.getDuracion() == maxDuración()){
                    return evento.getNombre();
                }
            }
        }
        return null;
    }
    
    /**
     * 
     */
    private int maxDuración() {
        int max = 0;
        Set<Map.Entry<Mes, ArrayList<Evento>>> entradas = calendario.entrySet();
        Iterator<Map.Entry<Mes, ArrayList<Evento>>> it = entradas.iterator();
        while(it.hasNext()){
            Map.Entry<Mes, ArrayList<Evento>> entrada = it.next();
            for(Evento evento: entrada.getValue()){
                if(evento.getDuracion() > max){
                    max = evento.getDuracion();
                }
            }
        }
        return max;
    }
    
    /**
     * Borrar del calendario todos los eventos de los meses indicados en el array
     * y que tengan lugar el día de la semana proporcionado (se entiende día de la
     * semana como 1 - Lunes, 2 - Martes ..  6 - Sábado, 7 - Domingo)
     * 
     * Si alguno de los meses del array no existe el el calendario no se hace nada
     * Si al borrar de un mes los eventos el mes queda con 0 eventos se borra la entrada
     * completa del map
     */
    public int cancelarEventos(Mes[] meses, int dia) {
        int borrados = 0; 
        for (int i = 0; i < meses.length; i++){
            if(calendario.containsKey(meses[i])){
                ArrayList<Evento> eventos = calendario.get(meses[i]);
                for(int j = eventos.size() - 1; j >= 0; j--){
                    if (eventos.get(j).getDia() == dia){
                        eventos.remove(eventos.get(j));
                        borrados++;
                    }
                }
                if (eventos.isEmpty()){
                    calendario.remove(meses[i]);
                }
            }
        }
        return borrados;
    }

    /**
     * Código para testear la clase CalendarioEventos
     */
    public static void main(String[] args) {
        CalendarioEventos calendario = new CalendarioEventos();
        CalendarioIO.cargarEventos(calendario);
        System.out.println(calendario);

        System.out.println();

        Mes mes = Mes.FEBRERO;
        System.out.println("Eventos en " + mes + " = "
        + calendario.totalEventosEnMes(mes));
        mes = Mes.MARZO;
        System.out.println("Eventos en " + mes + " = "
        + calendario.totalEventosEnMes(mes));
        System.out.println("Mes/es con más eventos "
        + calendario.mesesConMasEventos());

        System.out.println();
        System.out.println("Evento de mayor duración: "
        + calendario.eventoMasLargo());

        System.out.println();
        Mes[] meses = {Mes.FEBRERO, Mes.MARZO, Mes.MAYO, Mes.JUNIO};
        int dia = 6;
        System.out.println("Cancelar eventos de " + Arrays.toString(meses));
        int cancelados = calendario.cancelarEventos(meses, dia);
        System.out.println("Se han cancelado " + cancelados +
        " eventos");
        System.out.println();
        System.out.println("Después de cancelar eventos ...");
        System.out.println(calendario);
    }

}
