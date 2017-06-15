/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5;

import java.util.ArrayList;
import sim.tp5.estados.EstadoActividad;

/**
 *
 * @author filardo
 */
public class Cliente {
    
    private int id;
    private ArrayList<Actividad> actividades;
    
    public Cliente(int id){
        actividades = new ArrayList<Actividad>();
        this.id = id;
    }
    
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    /**
     * proximaActividad retorna la próxima actividad que hará el cliente
     * En caso de no tener más actividades, retorna null
     * @return Proxima Actividad | null
     */
    public Actividad proximaActividad(){
        Actividad actual = null;
        for (int i = 0; i < actividades.size(); i++) {
            actual = actividades.get(i);
            if (actual.esPendiente()){
                return actual;
            }
        }
        return null;
    }
    
    public String tieneGomeria(){
        for (Actividad actividad : actividades) {
            if (actividad.getNombre().equalsIgnoreCase(Actividad.GOMERIA))
                return actividad.getEstado();
        }
        return null;
    }
    
    public String tieneNegocio(){
        for (Actividad actividad : actividades) {
            if (actividad.getNombre().equalsIgnoreCase(Actividad.NEGOCIO))
                return actividad.getEstado();
        }
        return null;
    }
    
    public String tieneSurtidor(){
        for (Actividad actividad : actividades) {
            if (actividad.getNombre().equalsIgnoreCase(Actividad.SURTIDOR))
                return actividad.getEstado();
        }
        return null;
    }
    
    
    public Actividad actividadSiendoAtendida(){
        Actividad actual = null;
        for (int i = 0; i < actividades.size(); i++) {
            actual = actividades.get(i);
            if (actual.esSiendoAtendida()){
                return actual;
            }
        }
        
        return null;
    }
    
    public Actividad actividadEnCola(){
        Actividad actual = null;
        for (int i = 0; i < actividades.size(); i++) {
            actual = actividades.get(i);
            if (actual.esEnCola()){
                return actual;
            }
        }
        return actual;
    }
    
    public void agregarActividad(Actividad a){
        actividades.add(a);
    }
    
    
}
