/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.servidores;

import java.util.ArrayList;

/**
 *
 * @author fedeb
 */
public class Cola <E> {
    
    private ArrayList<E> cola;
    
    public Cola(){
        cola = new ArrayList<E>();
    }
    
    public int size(){
        return cola.size();
    }
    
    public void encolar(E e){
        cola.add(e);
    }
    
    public E getElemento(){
        return cola.remove(0);
    }
    
    public boolean esVacia(){
        return (this.size() == 0);
    }
}
