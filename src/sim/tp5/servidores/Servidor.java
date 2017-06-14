/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.servidores;

import java.util.ArrayList;
import sim.tp5.estados.EstadoServidor;
import sim.tp5.Cliente;


/**
 *
 * @author filardo
 */
public class Servidor {
  
    //Cola de clientes esperando por el servidor
    private Cola<Cliente> cola;
    protected Cliente clienteActual;
    protected EstadoServidor estado;
    private String nombre;
    
    
    public Servidor(String nombre){
        cola = new Cola<Cliente>();
        estado = new EstadoServidor(EstadoServidor.LIBRE);
        this.nombre = nombre;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    public ArrayList<Cliente> getCola(){
        return cola.getCola();
    }
    
    
    public boolean estaLibre(){
        return this.estado.getEstado().equalsIgnoreCase(EstadoServidor.getEstado(EstadoServidor.LIBRE));
    }
    
    public boolean estaOcupado(){
        return this.estado.getEstado().equalsIgnoreCase(EstadoServidor.getEstado(EstadoServidor.OCUPADO));
    }
    
    public String getEstado(){
        return this.estado.getEstado();
    }
    
    public int getTamañoCola(){
        return this.cola.size();
    }
    
    public boolean colaVacia(){
        return this.cola.esVacia();
    }
    
    public void addCola(Cliente c, double reloj){
        c.proximaActividad().encolar(reloj);
        this.cola.encolar(c);
    }
    
    /**
     * Método que procesa a un cliente, calculando el tiempo de atención y creando 
     * un evento FinDeAtención.
     * @param c -> Cliente que procesará el servidor
     * @param reloj
     *
     */
    public double iniciarAtencionCola(Cliente c,double reloj){
        clienteActual = c;
        if (this.estaLibre()) this.estado = new EstadoServidor(EstadoServidor.OCUPADO);
        
        c.actividadEnCola().atender(reloj);
        return calcularTiempoAtencion();
    }
    
    
    public double iniciarAtencion(Cliente c,double reloj){
        clienteActual = c;
        if (this.estaLibre()) this.estado = new EstadoServidor(EstadoServidor.OCUPADO);
        
        c.proximaActividad().atender(reloj);
        return calcularTiempoAtencion();
    }
    
    protected double calcularTiempoAtencion(){
        return 0.0;
    }
    
    /**
     * Atiende al primer cliente que espera en la cola
     */
    public Double atenderCola(double reloj){
        if (!this.colaVacia()){
            Cliente c = this.cola.getElemento();          
            iniciarAtencionCola(c,reloj);
        }
        else {
            this.estado = new EstadoServidor(EstadoServidor.LIBRE);
            return null;
        }
        //calcularTiempoAtencion es un método polimórfico que varía según en la clase que se ecnuetre
        return calcularTiempoAtencion();
    }
    
    public Cliente finalizar(){
        Cliente cli = this.clienteActual;
        this.clienteActual = null;
        return cli;
    }
    
    public Cliente getClienteActual(){
        return this.clienteActual;
    }
    
    
}
