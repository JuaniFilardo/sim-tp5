/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.servidores;

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
    private EstadoServidor estado;
    
    
    public Servidor(){
        cola = null;
        estado = new EstadoServidor(EstadoServidor.LIBRE);
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
     * @param Cliente c -> Cliente que procesará el servidor
     *
     */
    public void iniciarAtencion(Cliente c,double reloj){
         clienteActual = c;
         if (this.estaLibre()) this.estado = new EstadoServidor(EstadoServidor.OCUPADO);
         c.actividadEnCola().atender(reloj);
    }
    
    protected double calcularTiempoAtencion(){
        return 0.0;
    }
    
    /**
     * Atiende al primer cliente que espera en la cola
     */
    public double atenderCola(double reloj){
        if (!this.colaVacia()){
            Cliente c = this.cola.getElemento();
            iniciarAtencion(c,reloj);
        }
        else {
            this.estado = new EstadoServidor(EstadoServidor.LIBRE);
        }
        //calcularTiempoAtencion es un método polimórfico que varía según en la clase que se ecnuetre
        return calcularTiempoAtencion();
    }
    
    public Cliente finalizar(){
        Cliente cli = clienteActual;
        this.clienteActual = null;
        return cli;
    }
    
    public Cliente getClienteActual(){
        return this.clienteActual;
    }
}
