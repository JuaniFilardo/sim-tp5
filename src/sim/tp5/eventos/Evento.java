/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.eventos;

/**
 *
 * @author filardo
 */
public class Evento implements Comparable {

    public static final String FIN_ATENCION = "FinAtencion";
    public static final String LLEGADA_CLIENTE = "LlegadaCliente";
    public static final String INICIO_PURGA = "InicioPurga";
    public static final String FIN_PURGA = "FinPurga";

    // La hora a la que ocurre el Evento
    private Double hora;
    // El nombre del Evento
    private String tipo;

    public Evento(String tipo, Double hora) {

        if (tipo.equalsIgnoreCase(FIN_ATENCION) || tipo.equalsIgnoreCase(LLEGADA_CLIENTE) 
                || tipo.equalsIgnoreCase(INICIO_PURGA) || tipo.equalsIgnoreCase(FIN_PURGA)) {
            this.tipo = tipo;
        } else {
            this.tipo = "";
        }
        this.hora = hora;
    }

    public Double getHora() {
        return hora;
    }

    public String getTipo() {
        return tipo;
    }
    
    
    public void setHora(double hora){
        this.hora = hora;
    }

    public int compareTo(Object evento){
        Evento e = (Evento) evento;
  
        
        if (e.getHora() <= this.getHora()){

            return 1;
        }
        else{

            return -1;
        }
    }

   

}
