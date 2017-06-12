/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5;

import javax.swing.table.DefaultTableModel;
import sim.tp5.eventos.Evento;
import sim.tp5.servidores.Heap;
import sim.tp5.servidores.Servidor;
import sim.Distribucion;
import sim.tp5.eventos.FinAtencion;
import sim.tp5.eventos.LlegadaCliente;
import sim.tp5.servidores.EmpleadoGomeria;
import sim.tp5.servidores.Gomeria;
import sim.tp5.servidores.Negocio;
import sim.tp5.servidores.Surtidor;

/**
 *
 * @author filardo
 */
/* ¡FALTA!
    
    Agregar las variables que van a la tabla, están los valores calculados en muchos casos,
    solo hay que asignar
    Al final de cada iteración, habría que recorrer los eventos de tipo FIN de ATENCIÓN para
    colocar en la tabla en la columan Fin de Atención de cada servidor el momento en que se producirá el evento
    (es solo para visualización, no sirve ara nada mas)
    Corrregir los límites para cuando se genera el tiempo de atención (en los servidores), no tengo el enunciado
    Pantalla para visualizar los datos
    Probar, que seguro hay que pulir una banda

    Cualquier cosa preguntenme, no sigo porque estoy re duro, lo último que hice seguro fue cualquiera jaja

 */
public class GestorSimulacion {

    DefaultTableModel modeloPrincipal;
    DefaultTableModel modeloClientes;

    Servidor gomeria, negocio, surtidor1, surtidor2, surtidor3;
    //Próximos eventos. El heap nos asegura de obtener el próximo
    private Heap<Evento> eventos;

    //Datos del ejercicio
    int media = 24 / 60; //En minutos
    int varianza = 23 / 60; //En minutos

    //VARIABLES DE TABLA
    //Reloj del sistema
    double reloj;
    //Eventos
    Double rndProxLlegada = null;
    Double tiempoEntreLlegadas = null;
    Double proximaLlegada = null;

    //Random para cliente 
    Double rndCargaCombustible = 0.0;
    Double rndActividadCarga = 0.0;

    //Para ver que actividades realiza el cliente
    Boolean _combustible = false;
    Boolean _negocio = false;
    Boolean _gomeria = false;
    //Solo para la tabla
    String actividadCliente = "";

    //servidores
    //tiempo atencion
    Double tiempoAtencionSurtidor = 0.0;
    Double finAtencionSurtidor1 = 0.0;
    Double finAtencionSurtidor2 = 0.0;
    Double finAtencionSurtidor3 = 0.0;
    Double tiempoAtencionNegocio = 0.0;
    Double finAtencionNegocio = 0.0;
    Double tiempoAtencionGomeria = 0.0;
    Double finAtencionG1 = 0.0;
    Double finAtencionG2 = 0.0;
    Double tiempoAtencion = 0.0;
    Double horaInicioOcupacionNegocio = 0.0;
    //fines de atención

    String servidorActual = "";

    Evento evt = null;

    
    //Acumula clientes que pasaron por esa cola
    Double acumOcupacionNegocio = 0.0;
    Double acumTiempoEnColaSurtidor = 0.0;
    Double acumTiempoEnColaGomeria = 0.0;
    Double acumTiempoEnColaNegocio = 0.0;
    //Cuenta ls clientes que pasaron por esa cola
    Integer contColaSurtidor = 0;
    Integer contColaGomeria = 0;
    Integer contColaNegocio = 0;

    boolean primeraIteracion = true;

    //Varible que se utiliza para guardar el segundo valor que nos devuelve el generador
    //Si la variable está en null, se generan los números. Si no, se usa el valor de la variable
    Double normalCalculado = null;

    public GestorSimulacion() {

        //En primer lugar se inicializan los servidores
        gomeria = new Gomeria("gomeria");
        negocio = new Negocio("negocio");
        surtidor1 = new Surtidor("surtidor1");
        surtidor2 = new Surtidor("surtidor2");
        surtidor3 = new Surtidor("surtidor3");

        modeloClientes = new DefaultTableModel();
        modeloPrincipal = new DefaultTableModel();
        //Inicializamos el heap de eventos
        eventos = new Heap(10);

        //Incializamos reloj en 0;
        reloj = 0;
    }

    /**
     *
     * @param random Un número aleatorio entre 0 y 1
     * @return true si es random es menor a 0.80, false si no
     */
    private boolean cargaCombustible(double random) {
        return (random < 0.80);
    }

    /**
     *
     * @param random Un número aleatorio entre 0 y 1
     * @return "Negocio" si es menor a 0.40; "Gomeria" si es mayor
     */
    private String obtenerActividadSiNoCarga(double random) {
        return (random < 0.40) ? Actividad.NEGOCIO : Actividad.GOMERIA;
    }

    /**
     *
     * @param random Un número aleatorio entre 0 y 1
     * @return "Negocio" si es menor a 0.30; "Gomeria" si 0.30 <= random < 0.50;
     * null si es mayor a 0.50
     */
    private String obtenerActividadSiCarga(double random) {

        if (random < 0.50) {
            return (random < 0.30) ? Actividad.NEGOCIO : Actividad.GOMERIA;
        } else {
            return null;
        }
    }

    /**
     * Se calcula el tiempo entre llegadas de los clientes. Distribución Normal!
     *
     * @return Double que refleja el tiempo para la siguiente llegada
     */
    private double calcularTiempoEntreLlegadas() {
        double tiempoEntreLlegadas = 0;
        if (normalCalculado == null) {
            double numNorm[] = Distribucion.generarNormal(media, varianza);
            tiempoEntreLlegadas = numNorm[0];
            normalCalculado = numNorm[1];
        } else {
            tiempoEntreLlegadas = normalCalculado;
            normalCalculado = null;
        }
        return tiempoEntreLlegadas;
    }

    private Cliente crearCliente() {
        Cliente nuevo = new Cliente();
        rndCargaCombustible = Math.random();
        if (cargaCombustible(rndCargaCombustible)) {
            nuevo.agregarActividad(new Actividad(Actividad.SURTIDOR, reloj));
            rndActividadCarga = Math.random();
            String act = obtenerActividadSiCarga(rndActividadCarga);
            nuevo.agregarActividad(new Actividad(act, reloj));
            this._combustible = true;
            if (act.equalsIgnoreCase(Actividad.GOMERIA)) {
                this._gomeria = true;
            } else {
                this._negocio = true;
            }
        } else {
            rndActividadCarga = Math.random();
            String act = obtenerActividadSiNoCarga(rndActividadCarga);
            nuevo.agregarActividad(new Actividad(obtenerActividadSiNoCarga(Math.random()), reloj));
            if (act.equalsIgnoreCase(Actividad.GOMERIA)) {
                this._gomeria = true;
            } else {
                this._negocio = true;
            }
        }
        return nuevo;
    }

    private Servidor asignarClienteAServidor(Cliente c) {
        Servidor candidato = null;
        if (c.proximaActividad().getNombre().equalsIgnoreCase(Actividad.SURTIDOR)) {
            candidato = surtidorLibre();
            if (candidato != null) {
                tiempoAtencion = candidato.iniciarAtencion(c, reloj);
                eventos.add(new Evento(Evento.FIN_ATENCION, (reloj + tiempoAtencion)));
                asignarTiempoAtencion(candidato, tiempoAtencion);
            } else {
                candidato = surtidorConMenorCola();
                candidato.addCola(c, reloj);
            }
        } else if (c.proximaActividad().getNombre().equalsIgnoreCase(Actividad.GOMERIA)) {
            if (gomeria.estaLibre()) {
                tiempoAtencion = gomeria.iniciarAtencion(c, reloj);
                eventos.add(new Evento(Evento.FIN_ATENCION, (reloj + tiempoAtencion)));
                asignarTiempoAtencion(candidato, tiempoAtencion);
            } else {
                gomeria.addCola(c, reloj);
            }
        } else if (c.proximaActividad().getNombre().equalsIgnoreCase(Actividad.NEGOCIO)) {
            if (negocio.estaLibre()) {
                tiempoAtencion = negocio.iniciarAtencion(c, reloj);
                eventos.add(new Evento(Evento.FIN_ATENCION, (reloj + tiempoAtencion)));
                asignarTiempoAtencion(candidato, tiempoAtencion);
            } else {
                negocio.addCola(c, reloj);
            }
        }
        return candidato;
    }

    private Servidor surtidorLibre() {
        if (surtidor1.estaLibre()) {
            return surtidor1;
        } else if (surtidor2.estaLibre()) {
            return surtidor2;
        } else if (surtidor3.estaLibre()) {
            return surtidor3;
        } else {
            return null;
        }
    }

    private Servidor surtidorConMenorCola() {
        if (surtidor1.getTamañoCola() <= surtidor2.getTamañoCola()) {
            if (surtidor1.getTamañoCola() <= surtidor3.getTamañoCola()) {
                return surtidor1;
            } else {
                return surtidor3;
            }
        } else if (surtidor2.getTamañoCola() <= surtidor3.getTamañoCola()) {
            return surtidor2;
        } else {
            return surtidor3;
        }
    }

    private void primeraIteracion() {
        //MODELO DE TABLA 

        reloj = 0;

        tiempoEntreLlegadas = calcularTiempoEntreLlegadas();
        proximaLlegada = reloj + tiempoEntreLlegadas;
        //se crea evento para la próxima llegada y se lo agrega al heap
        eventos.add(new LlegadaCliente(proximaLlegada));

        _combustible = null;
        _negocio = null;
        _gomeria = null;
        rndActividadCarga = null;
        rndCargaCombustible = null;
        Evento evt = null;

        acumTiempoEnColaSurtidor = 0.0;
        acumTiempoEnColaGomeria = 0.0;
        acumTiempoEnColaNegocio = 0.0;
        contColaSurtidor = 0;
        contColaGomeria = 0;
        contColaNegocio = 0;

        tiempoAtencionSurtidor = null;
        finAtencionSurtidor1 = null;
        finAtencionSurtidor2 = null;
        finAtencionSurtidor3 = null;
        tiempoAtencionNegocio = null;
        finAtencionNegocio = null;
        tiempoAtencionGomeria = null;
        finAtencionG1 = null;
        finAtencionG2 = null;
        tiempoAtencion = null;

        sumarFila();

    }

    public void simular(double relojMaximo) {
        if (primeraIteracion) {
            primeraIteracion();
        } else {
            // Arranca desde la segunda
            while (reloj <= relojMaximo) {
                //  calcular próximo evento
                //Se obtiene y se borra el elemento de la cima del heap
                evt = this.eventos.remove();
                // Setear reloj según el evento que ocurre
                reloj = evt.getHora();

                if (evt.getTipo().equalsIgnoreCase(Evento.LLEGADA_CLIENTE)) {
                    //Calcular próxima llegada
                    tiempoEntreLlegadas = calcularTiempoEntreLlegadas();
                    proximaLlegada = reloj + tiempoEntreLlegadas;
                    //se crea evento para la próxima llegada y se lo agrega al heap
                    eventos.add(new LlegadaCliente(proximaLlegada));
                    //Se crea el cliente que llegó y se llena la lista de actividades que 
                    //quiere realizar
                    Cliente c = crearCliente();
                    servidorActual = asignarClienteAServidor(c).getNombre();
                } else if (evt.getTipo().equalsIgnoreCase(Evento.FIN_ATENCION)) {
                    FinAtencion fa = (FinAtencion) evt;
                    //Referencio al servidor que termino la atención
                    Servidor target = fa.getServidor();
                    servidorActual = target.getNombre();
                    //Finaliza la atención del servidor!!
                    Cliente atendido = target.finalizar();
                    //Se calcula el contador y el acumulador de la variable que corresponda
                    calcularVariablesEstadisticas(atendido);
                    //Cambio de estado a la actividad del cliente
                    atendido.actividadSiendoAtendida().finalizar();
                    //Pregunto si el cliente tiene agluna otra actividad por realizar
                    Actividad proxActividad = atendido.proximaActividad();
                    if (proxActividad != null) {
                        asignarClienteAServidor(atendido);
                    }
                    tiempoAtencion = target.atenderCola(reloj);

                    asignarTiempoAtencion(target, tiempoAtencion);

                    //Se agrega el evento de fin de atención 
                    eventos.add(new Evento(Evento.FIN_ATENCION, (reloj + tiempoAtencion)));

                }

                sumarFila();
                //Sumar fila a default table models
            }
            //Faltan las demas lineas de la tabla
        }

        //calcularPromedioDeEsperaEnCola()
        //calcularPorcentajeOcupacion
    }

    private void sumarFila() {

        //modeloPrincipal
        String carga = (this.cargaCombustible(rndCargaCombustible)) ? "Si" : "No";
        String tel = (this.tiempoEntreLlegadas != null) ? tiempoEntreLlegadas.toString() : "-";
        String rndCarga = (this.rndCargaCombustible != null) ? rndCargaCombustible.toString() : "-";
        String rndGoN = (this.rndActividadCarga != null) ? this.rndActividadCarga.toString() : "-";
        String actividad;
        if (rndCargaCombustible != null) {
            if (this.cargaCombustible(rndCargaCombustible)) {
                actividad = this.obtenerActividadSiCarga(rndActividadCarga);
            }
            else actividad = this.obtenerActividadSiNoCarga(rndActividadCarga);
        }
        else actividad = null;
        //Visualización en la tabla
        if (actividad == null) actividad = "NADA";
        String tAtSurt, finAtSurt1, finAtSurt2, finAtSurt3, finAtNeg="-", finAtG1, finAtG2, tAtGom = "-", tAtNeg = "-";
        tAtSurt = (this.tiempoAtencionSurtidor != null) ? tiempoAtencionSurtidor.toString() : "-";
        finAtSurt1 = (this.finAtencionSurtidor1 != null) ? this.finAtencionSurtidor1.toString() : "-";
        finAtSurt2 = (this.finAtencionSurtidor2 != null) ? this.finAtencionSurtidor2.toString() : "-";
        finAtSurt3 = (this.finAtencionSurtidor3 != null) ? this.finAtencionSurtidor3.toString() : "-";
        
       
        if (this.finAtencionNegocio != null) finAtNeg = this.finAtencionNegocio.toString();
        if (this.tiempoAtencionGomeria != null) tAtGom = this.tiempoAtencionGomeria.toString();
        if (this.tiempoAtencionNegocio != null) tAtNeg = this.tiempoAtencionNegocio.toString();
        
        EmpleadoGomeria eg1 = ((Gomeria) this.gomeria).getEmpleado(1);
        EmpleadoGomeria eg2 = ((Gomeria) this.gomeria).getEmpleado(2);
        finAtencionG1 = eg1.getFinDeAtencion();
        finAtencionG2 = eg2.getFinDeAtencion();
        
        finAtG1 =  (finAtencionG1 != null) ? finAtencionG1.toString() : "-";
        finAtG2 =  (finAtencionG1 != null) ? finAtencionG2.toString() : "-";
        
        String estadoSurt1 = surtidor1.getEstado();
        String colaSurt1 = surtidor1.getTamañoCola()+"";
        String estadoSurt2 = surtidor2.getEstado();
        String colaSurt2 = surtidor2.getTamañoCola()+"";
        String estadoSurt3 = surtidor3.getEstado();
        String colaSurt3 = surtidor3.getTamañoCola()+"";
        String estadoNeg = negocio.getEstado();
        String colaNegocio = negocio.getTamañoCola()+"";
        String estadoGom = gomeria.getEstado();
        String colaGomeria = gomeria.getTamañoCola()+"";
        
        String contClientesSurtidor = this.contColaSurtidor.toString();
        String acumTEnColaSurt = this.acumTiempoEnColaSurtidor.toString();
        String contClientesNegocio = this.contColaNegocio.toString();
        String acumTEnColaNegocio = this.acumTiempoEnColaNegocio.toString();
        String contClientesGomeria = this.contColaGomeria.toString();
        String acumTEnColaGom = this.acumTiempoEnColaGomeria.toString();
        String horaInicioOcNeg = (this.horaInicioOcupacionNegocio != null) ? this.horaInicioOcupacionNegocio.toString() : "-";
        String acumOcupacion = this.acumOcupacionNegocio.toString();
        
       
        
        Object[] rowPrincipal = {reloj, tel, proximaLlegada, rndCarga, carga, rndGoN, actividad, tAtSurt, finAtSurt1,
        finAtSurt2, finAtSurt3, estadoSurt1, colaSurt1 ,estadoSurt2, colaSurt2, estadoSurt3, colaSurt3, tAtGom,finAtG1,
        finAtG2,estadoGom,colaGomeria, tAtNeg, finAtNeg, estadoNeg, colaNegocio};
        modeloPrincipal.addRow(rowPrincipal);
        //modeloClientes

    }

    private void asignarTiempoAtencion(Servidor target, double tiempoAtencion) {
        if (target instanceof Surtidor) {
            tiempoAtencionSurtidor = tiempoAtencion;
            if (target.getNombre().equalsIgnoreCase("surtidor1")) {
                finAtencionSurtidor1 = tiempoAtencion + reloj;
            } else if (target.getNombre().equalsIgnoreCase("surtidor2")) {
                finAtencionSurtidor2 = tiempoAtencion + reloj;
            } else if (target.getNombre().equalsIgnoreCase("surtidor3")) {
                finAtencionSurtidor3 = tiempoAtencion + reloj;
            }
        } else if (target instanceof Gomeria) {
            //Se asigna dentro de la clase Gomeria
        } else if (target instanceof Negocio) {
            tiempoAtencionNegocio = tiempoAtencion;
            finAtencionNegocio = tiempoAtencionNegocio + reloj;
        }
    }

    private void calcularVariablesEstadisticas(Cliente cliente) {
        //Se pregunta por el que está siendo atendido ya que todavía no se finalizó la actividad
        Actividad atendida = cliente.actividadSiendoAtendida();
        if (atendida.getNombre().equalsIgnoreCase(Actividad.SURTIDOR)) {
            //Me parece que esto no anda xq el getEspera necesita horaFin para calcularse
            this.acumTiempoEnColaSurtidor += atendida.getEspera();
            this.contColaSurtidor++;
        } else if (atendida.getNombre().equalsIgnoreCase(Actividad.GOMERIA)) {
            this.acumTiempoEnColaGomeria += atendida.getEspera();
            this.contColaGomeria++;
        } else if (atendida.getNombre().equalsIgnoreCase(Actividad.NEGOCIO)) {
            this.acumTiempoEnColaNegocio += atendida.getEspera();
            this.contColaNegocio++;
        }
        
        this.horaInicioOcupacionNegocio = ((Negocio)negocio).getHoraInicio();
        this.acumOcupacionNegocio = ((Negocio)negocio).calcularOcupacion(reloj);
    }

}
