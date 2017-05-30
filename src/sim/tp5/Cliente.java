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
    
    public Cliente(){}
    
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
                break;
            }
        }
        return actual;
    }
    
    
    public Actividad actividadSiendoAtendida(){
        Actividad actual = null;
        for (int i = 0; i < actividades.size(); i++) {
            actual = actividades.get(i);
            if (actual.esSiendoAtendida()){
                break;
            }
        }
        return actual;
    }
    
    public Actividad actividadEnCola(){
        Actividad actual = null;
        for (int i = 0; i < actividades.size(); i++) {
            actual = actividades.get(i);
            if (actual.esEnCola()){
                break;
            }
        }
        return actual;
    }
    
    public void agregarActividad(Actividad a){
        actividades.add(a);
    }
    
    
}
