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
public abstract class Evento {

    public static final String FIN_ATENCION = "FinAtencion";
    public static final String LLEGADA_CLIENTE = "LlegadaCliente";

    // La hora a la que ocurre el Evento
    private Double hora;
    // El nombre del Evento
    private String nombre;

    public Evento(String nombre, Double hora) {

        if (nombre.equalsIgnoreCase(FIN_ATENCION) || nombre.equalsIgnoreCase(LLEGADA_CLIENTE)) {
            this.nombre = nombre;
        } else {
            this.nombre = "";
        }
        this.hora = hora;
    }

    public Double getHora() {
        return hora;
    }

    public String getNombre() {
        return nombre;
    }
}
