/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5;

import java.util.ArrayList;
import sim.tp5.eventos.Evento;
import sim.tp5.servidores.Heap;
import sim.tp5.servidores.Servidor;
import sim.Distribucion;
import sim.tp5.eventos.FinAtencion;
import sim.tp5.eventos.FinPurga;
import sim.tp5.eventos.InicioPurga;
import sim.tp5.eventos.LlegadaCliente;
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

    int idCliente = 0;
    Servidor gomeria, negocio, surtidor1, surtidor2, surtidor3;
    //Próximos eventos. El heap nos asegura de obtener el próximo
    private Heap<Evento> eventos;

    //Datos del ejercicio
    int media = 24; //En segundos
    int varianza = 23; //En segundos

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
    Double finAtencionGom = 0.0;
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

    Double tiempoEntrePurgas = 0.0;
    Double proximaPurga = 0.0;
    Double finPurga = 0.0;
    Double RNDPurga = 0.0;

    //Varible que se utiliza para guardar el segundo valor que nos devuelve el generador
    //Si la variable está en null, se generan los números. Si no, se usa el valor de la variable
    Double normalCalculado = null;

    ArrayList modeloPrincipal = new ArrayList();
    ArrayList modeloClientes = new ArrayList();

    int k;

    public GestorSimulacion() {

        //En primer lugar se inicializan los servidores
        gomeria = new Gomeria("gomeria");
        negocio = new Negocio("negocio");
        surtidor1 = new Surtidor("surtidor1");
        surtidor2 = new Surtidor("surtidor2");
        surtidor3 = new Surtidor("surtidor3");

        //Inicializamos el heap de eventos
        eventos = new Heap(10, true);

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

    private Double calcularTiempoEntrePurgas(int porcentajeMaximo) {
        if (porcentajeMaximo == 100) {
            return 12.4;
        }
        if (porcentajeMaximo == 70) {
            return 10.9;
        }
        if (porcentajeMaximo == 50) {
            return 9.6;
        }
        return null;
    }

    private Integer obtenerPorcentajeDeInestabilidad(Double random) {
        if (random < 0 || random >= 1) {
            return null;
        }
        if (random < 0.5) {
            return 100;
        }
        if (random < 0.8) {
            return 70;
        }
        return 50;
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
        double tiEntreLlegadas = 0;
        if (normalCalculado == null) {
            double numNorm[] = Distribucion.generarNormal(media, varianza);
            tiEntreLlegadas = numNorm[0];
            normalCalculado = numNorm[1];
        } else {
            tiEntreLlegadas = normalCalculado;
            normalCalculado = null;
        }
        //Lo pasamos a minutos

        return Math.abs(tiEntreLlegadas / 60);
    }

    private Cliente crearCliente() {
        idCliente++;
        Cliente nuevo = new Cliente(idCliente);
        rndCargaCombustible = Math.random();
        if (cargaCombustible(rndCargaCombustible)) {
            nuevo.agregarActividad(new Actividad(Actividad.SURTIDOR, reloj));
            rndActividadCarga = Math.random();
            String act = obtenerActividadSiCarga(rndActividadCarga);
            if (act != null) {
                nuevo.agregarActividad(new Actividad(act, reloj));
                this._combustible = true;
                if (act.equalsIgnoreCase(Actividad.GOMERIA)) {
                    this._gomeria = true;

                } else if (act.equalsIgnoreCase(Actividad.NEGOCIO)) {
                    this._negocio = true;
                }
            }
        } else {
            rndActividadCarga = Math.random();
            String act = obtenerActividadSiNoCarga(rndActividadCarga);
            if (act != null) {
                nuevo.agregarActividad(new Actividad(obtenerActividadSiNoCarga(Math.random()), reloj));
                if (act.equalsIgnoreCase(Actividad.GOMERIA)) {
                    this._gomeria = true;
                } else {
                    this._negocio = true;
                }
            }
        }
        return nuevo;
    }

    private void asignarClienteAServidor(Cliente c) {
        Servidor candidato = null;
        Actividad proxima = c.proximaActividad();
        if (proxima.getNombre().equalsIgnoreCase(Actividad.SURTIDOR)) {
            candidato = surtidorLibre();
            if (candidato != null) {
                tiempoAtencion = candidato.iniciarAtencion(c, reloj);
                eventos.add(new FinAtencion((reloj + tiempoAtencion), candidato));
                asignarTiempoAtencion(candidato, tiempoAtencion);
            } else {
                candidato = surtidorConMenorCola();
                candidato.addCola(c, reloj);
            }
        } else if (proxima.getNombre().equalsIgnoreCase(Actividad.GOMERIA)) {
            if (gomeria.estaLibre()) {
                tiempoAtencion = gomeria.iniciarAtencion(c, reloj);
                eventos.add(new FinAtencion((reloj + tiempoAtencion), gomeria));
                asignarTiempoAtencion(gomeria, tiempoAtencion);
            } else {
                gomeria.addCola(c, reloj);
            }
        } else if (c.proximaActividad().getNombre().equalsIgnoreCase(Actividad.NEGOCIO)) {
            if (negocio.estaLibre()) {
                tiempoAtencion = negocio.iniciarAtencion(c, reloj);
                eventos.add(new FinAtencion((reloj + tiempoAtencion), negocio));
                asignarTiempoAtencion(negocio, tiempoAtencion);
            } else {
                negocio.addCola(c, reloj);
            }
        }

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
        finAtencionGom = null;
        tiempoAtencion = null;

        RNDPurga = Math.random();
        int porcentajeInestabilidad = this.obtenerPorcentajeDeInestabilidad(RNDPurga);
        ((Gomeria) gomeria).setPorcentajeInestabilidadMax(porcentajeInestabilidad);
        finPurga = null;
        this.tiempoEntrePurgas = this.calcularTiempoEntrePurgas(porcentajeInestabilidad);
        this.proximaPurga = reloj + tiempoEntrePurgas;
        Evento ip = new InicioPurga(reloj + tiempoEntrePurgas);
        eventos.add(ip);

        primeraIteracion = false;
    }

    public ArrayList simular(double relojMaximo, int k) {
        this.k = k;
        // Arranca desde la segunda
        while (reloj <= relojMaximo) {
            if (primeraIteracion) {
                primeraIteracion();
            } else {
                //  calcular próximo evento
                //Se obtiene y se borra el elemento de la cima del heap
                evt = this.eventos.remove();

                // Setear reloj según el evento que ocurre
                reloj = evt.getHora();

                //evt.getTipo().equalsIgnoreCase(Evento.LLEGADA_CLIENTE)
                if (evt instanceof LlegadaCliente) {
                    //Calcular próxima llegada
                    tiempoEntreLlegadas = calcularTiempoEntreLlegadas();
                    proximaLlegada = reloj + tiempoEntreLlegadas;
                    //se crea evento para la próxima llegada y se lo agrega al heap
                    eventos.add(new LlegadaCliente(proximaLlegada));
                    //Se crea el cliente que llegó y se llena la lista de actividades que 
                    //quiere realizar
                    Cliente c = crearCliente();

                    asignarClienteAServidor(c);

                    //evt.getTipo().equalsIgnoreCase(Evento.FIN_ATENCION)
                } else if (evt instanceof FinAtencion) {

                    FinAtencion fa = (FinAtencion) evt;
                    //Referencio al servidor que termino la atención
                    Servidor target = fa.getServidor();
                    //servidorActual = target.getNombre();
                    //Finaliza la atención del servidor!!
                    Cliente atendido;
                    if (target instanceof Negocio) {
                        atendido = ((Negocio) target).finalizar(reloj);
                    } else {
                        atendido = target.finalizar();
                    }
                    limpiarFinAtencion(target);
                    Actividad atendida = atendido.actividadSiendoAtendida();
                    if (atendida == null) {
                        Actividad act = atendido.proximaActividad();
                    }
                    //Se calcula el contador y el acumulador de la variable que corresponda

                    //calcularVariablesEstadisticas(atendida);
                    //Cambio de estado a la actividad del cliente
                    atendida.finalizar();
                    //Pregunto si el cliente tiene agluna otra actividad por realizar
                    Actividad proxActividad = atendido.proximaActividad();
                    if (proxActividad != null) {
                        asignarClienteAServidor(atendido);
                    }

                    tiempoAtencion = target.atenderCola(reloj);
                    if (tiempoAtencion != null) {
                        asignarTiempoAtencion(target, tiempoAtencion);

                        //Se agrega el evento de fin de atención 
                        eventos.add(new FinAtencion((reloj + tiempoAtencion), target));
                    }
                } else if (evt instanceof InicioPurga) {
                    Evento ev = eventos.remove();                   
                    ArrayList eventosRemovidos = new ArrayList();
                    
                                
                        
                    while (!ev.getTipo().equalsIgnoreCase(Evento.FIN_ATENCION) 
                            || !((FinAtencion) ev).getServidor().getNombre().equalsIgnoreCase(Actividad.GOMERIA))
                    {
                        eventosRemovidos.add(ev);
                        ev = eventos.remove();
                        if (ev == null) break;
                        
                        
                    }
                    double tiempoRemanencia;
                    if (ev != null) {
                        tiempoRemanencia = ev.getHora() - reloj;
                        //this.finAtencionGom = ev.getHora() + k;
                        finAtencionGom += k;
                        ev.setHora(finAtencionGom);
                        eventosRemovidos.add(ev);
                    } else {
                        tiempoRemanencia = -1;
                    }
                    ((Gomeria) gomeria).purgar(tiempoRemanencia);
                    
                   
                    for (Object evts : eventosRemovidos) {
                        eventos.add((Evento) evts);
                    }
                    finPurga = reloj+k;
                    proximaPurga = null;
                    
                    Evento fp = new FinPurga(reloj + k);
                    eventos.add(fp);

                } else if (evt instanceof FinPurga) {
                    double tiempoRemanencia = ((Gomeria) gomeria).terminarPurga();
                    if (tiempoRemanencia == -1) {
                        Double tiempoAtencion = gomeria.atenderCola(reloj);
                        if (tiempoAtencion != null) {
                            Servidor target = gomeria;
                            asignarTiempoAtencion(target, tiempoAtencion);
                            //Se agrega el evento de fin de atención 
                            eventos.add(new FinAtencion((reloj + tiempoAtencion), target));
                        }
                    }
                    

                    RNDPurga = Math.random();
                    int porcentajeInestabilidad = this.obtenerPorcentajeDeInestabilidad(RNDPurga);
                    ((Gomeria) gomeria).setPorcentajeInestabilidadMax(porcentajeInestabilidad);

                    this.tiempoEntrePurgas = this.calcularTiempoEntrePurgas(porcentajeInestabilidad);
                    this.proximaPurga = reloj + tiempoEntrePurgas;
                    Evento ip = new InicioPurga(reloj + tiempoEntrePurgas);
                    eventos.add(ip);
                    finPurga = null;

                }

                //Siempre actualizo e imprimo el AcumOcupacionNegocio
                if (negocio.estaOcupado()) {
                    //((Negocio)negocio).acumularOcupacion(reloj);
                }
                if (reloj >= relojMaximo) {
                    ((Negocio) negocio).finalizar(reloj);
                    //((Negocio)negocio).acumularOcupacion(reloj);
                }
                this.acumOcupacionNegocio = ((Negocio) negocio).getAcumOcupacion();

            }
            this.contColaGomeria = gomeria.getContadorCola();
            this.contColaNegocio = negocio.getContadorCola();
            this.contColaSurtidor = surtidor1.getContadorCola() + surtidor2.getContadorCola() + surtidor3.getContadorCola();

            this.acumTiempoEnColaGomeria = gomeria.getAcumulador();
            this.acumTiempoEnColaNegocio = negocio.getAcumulador();
            this.acumTiempoEnColaSurtidor = surtidor1.getAcumulador() + surtidor2.getAcumulador() + surtidor3.getAcumulador();

            //  limpiarVariables();
            sumarFilaPrincipal();
            sumarFilaClientes();
            limpiarVariables();

            //Faltan las demas lineas de la tabla
        }
        ArrayList general = new ArrayList();
        general.add(modeloPrincipal);
        general.add(modeloClientes);
        //calcularPromedioDeEsperaEnCola()
        //calcularPorcentajeOcupacion
        return general;
    }

    private void limpiarFinAtencion(Servidor servidor) {
        if (servidor.getNombre().equalsIgnoreCase("surtidor1")) {
            this.finAtencionSurtidor1 = null;
        } else if (servidor.getNombre().equalsIgnoreCase("surtidor2")) {
            this.finAtencionSurtidor2 = null;
        } else if (servidor.getNombre().equalsIgnoreCase("surtidor3")) {
            this.finAtencionSurtidor3 = null;
        } else if (servidor.getNombre().equalsIgnoreCase("negocio")) {
            this.finAtencionNegocio = null;
        } else if (servidor.getNombre().equalsIgnoreCase("gomeria")) {
            this.finAtencionGom = null;
        }
    }

    private void limpiarVariables() {
        rndCargaCombustible = null;
        this.rndActividadCarga = null;
        tiempoAtencion = null;
        if (proximaLlegada != null && reloj >= proximaLlegada) {
            proximaLlegada = null;
        }
        tiempoEntreLlegadas = null;
        tiempoAtencionSurtidor = null;
        tiempoAtencionGomeria = null;
        tiempoAtencionNegocio = null;
        RNDPurga = null;
        tiempoEntrePurgas = null;
        
    }

    /*
    
    Cambiar en limpiar variables los if at neg y if finatneg por:

    tAtNeg = (this.tiempoAtencionNegocio != null) ? this.tiempoAtencionNegocio.toString() : "-";
    finAtNeg = (this.finAtencionNegocio != null) ? this.finAtencionNegocio.toString() : "-";


    En simular:

    En el if Fin atencion, cambiar, a la hora de calcular las variables estadisticas:
    Agregar esta linea y cambiarle el parametro a calcularVar....
    Actividad atendida = atendido.actividadSiendoAtendida();
    calcularVariablesEstadisticas(atendida);

    Luego cambiar el parametro recibido en el calcularVar.... por una Actividad atendida y borrar las primeras lineas que involucran
    al cliente
    
     */
    private void sumarFilaClientes() {
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        if (surtidor1.getClienteActual() != null) {
            clientes.add(surtidor1.getClienteActual());
        }
        if (surtidor2.getClienteActual() != null) {
            clientes.add(surtidor2.getClienteActual());
        }
        if (surtidor3.getClienteActual() != null) {
            clientes.add(surtidor3.getClienteActual());
        }
        if (negocio.getClienteActual() != null) {
            clientes.add(negocio.getClienteActual());
        }
        if (gomeria.getClienteActual() != null) {
            clientes.add(gomeria.getClienteActual());
        }

        for (Cliente cliente : surtidor1.getCola()) {
            clientes.add(cliente);
        }
        for (Cliente cliente : surtidor2.getCola()) {
            clientes.add(cliente);
        }
        for (Cliente cliente : surtidor3.getCola()) {
            clientes.add(cliente);
        }
        for (Cliente cliente : negocio.getCola()) {
            clientes.add(cliente);
        }
        for (Cliente cliente : gomeria.getCola()) {
            clientes.add(cliente);
        }

        int id = 0;
        String estadoSurtidor1 = "Libre", estadoSurtidor2 = "Libre", estadoSurtidor3 = "Libre", estadoGomeria = "Libre", estadoNegocio = "Libre";

        String horaIngresoColaSurtidor = "-", horaIngresoColaNegocio = "-", horaIngresoColaGomeria = "-";
        String cSurtidor = "-";
        String cNegocio = "-";
        String cGomeria = "-";

        for (Cliente cliente : clientes) {
            horaIngresoColaSurtidor = "-";
            horaIngresoColaNegocio = "-";
            horaIngresoColaGomeria = "-";
            id = cliente.getId();
            Actividad actividad;
            actividad = cliente.actividadSiendoAtendida();
            if (actividad != null) {

            } else {
                actividad = cliente.actividadEnCola();
                if (actividad.getNombre().equalsIgnoreCase(Actividad.SURTIDOR)) {
                    horaIngresoColaSurtidor = actividad.getHoraInicioCola() + "";
                } else if (actividad.getNombre().equalsIgnoreCase(Actividad.GOMERIA)) {
                    horaIngresoColaGomeria = actividad.getHoraInicioCola() + "";
                } else if (actividad.getNombre().equalsIgnoreCase(Actividad.NEGOCIO)) {
                    horaIngresoColaNegocio = actividad.getHoraInicioCola() + "";
                }
            }
            cNegocio = cliente.tieneNegocio();
            if (cNegocio == null) {
                cNegocio = "-";
            }
            cSurtidor = cliente.tieneSurtidor();
            if (cSurtidor == null) {
                cSurtidor = "-";
            }
            cGomeria = cliente.tieneGomeria();
            if (cGomeria == null) {
                cGomeria = "-";
            }

            estadoSurtidor1 = surtidor1.getEstado();
            estadoSurtidor2 = surtidor2.getEstado();
            estadoSurtidor3 = surtidor3.getEstado();
            estadoNegocio = negocio.getEstado();
            estadoGomeria = gomeria.getEstado();

            Object[] rowPrincipal = {reloj, id, cSurtidor, horaIngresoColaSurtidor, cGomeria, horaIngresoColaGomeria,
                cNegocio, horaIngresoColaNegocio};
            modeloClientes.add(rowPrincipal);
        }

    }

    private void sumarFilaPrincipal() {
        this.horaInicioOcupacionNegocio = ((Negocio) negocio).getHoraInicio();
        //modeloPrincipal
        String carga = "-";
        if (rndCargaCombustible != null) {
            carga = (this.cargaCombustible(rndCargaCombustible)) ? "Si" : "No";
        }
        String tel = (this.tiempoEntreLlegadas != null) ? tiempoEntreLlegadas.toString() : "-";
        String rndCarga = (this.rndCargaCombustible != null) ? rndCargaCombustible.toString() : "-";
        String rndGoN = (this.rndActividadCarga != null) ? this.rndActividadCarga.toString() : "-";
        String actividad;
        if (rndCargaCombustible != null) {
            if (this.cargaCombustible(rndCargaCombustible)) {
                actividad = this.obtenerActividadSiCarga(rndActividadCarga);
            } else {
                actividad = this.obtenerActividadSiNoCarga(rndActividadCarga);
            }
        } else {
            actividad = null;
        }
        //Visualización en la tabla
        if (actividad == null) {
            actividad = "NADA";
        }
        String tAtSurt, finAtSurt1, finAtSurt2, finAtSurt3, finAtNeg = "-", finAtG, tAtGom = "-", tAtNeg = "-";
        tAtSurt = (this.tiempoAtencionSurtidor != null) ? tiempoAtencionSurtidor.toString() : "-";
        finAtSurt1 = (this.finAtencionSurtidor1 != null) ? this.finAtencionSurtidor1.toString() : "-";
        finAtSurt2 = (this.finAtencionSurtidor2 != null) ? this.finAtencionSurtidor2.toString() : "-";
        finAtSurt3 = (this.finAtencionSurtidor3 != null) ? this.finAtencionSurtidor3.toString() : "-";

        tAtNeg = (this.tiempoAtencionNegocio != null) ? this.tiempoAtencionNegocio.toString() : "-";
        finAtNeg = (this.finAtencionNegocio != null) ? this.finAtencionNegocio.toString() : "-";

        tAtGom = (this.tiempoAtencionGomeria != null) ? this.tiempoAtencionGomeria.toString() : "-";
        finAtG = (finAtencionGom != null) ? finAtencionGom.toString() : "-";

        String estadoSurt1 = surtidor1.getEstado();
        String colaSurt1 = surtidor1.getTamañoCola() + "";
        String estadoSurt2 = surtidor2.getEstado();
        String colaSurt2 = surtidor2.getTamañoCola() + "";
        String estadoSurt3 = surtidor3.getEstado();
        String colaSurt3 = surtidor3.getTamañoCola() + "";
        String estadoNeg = negocio.getEstado();
        String colaNegocio = negocio.getTamañoCola() + "";
        String estadoGom = gomeria.getEstado();
        String colaGomeria = gomeria.getTamañoCola() + "";

        String contClientesSurtidor = this.contColaSurtidor.toString();
        String acumTEnColaSurt = this.acumTiempoEnColaSurtidor.toString();
        String contClientesNegocio = this.contColaNegocio.toString();
        String acumTEnColaNegocio = this.acumTiempoEnColaNegocio.toString();
        String contClientesGomeria = this.contColaGomeria.toString();
        String acumTEnColaGom = this.acumTiempoEnColaGomeria.toString();
        String horaInicioOcNeg = (this.horaInicioOcupacionNegocio != null) ? this.horaInicioOcupacionNegocio.toString() : "-";
        String acumOcupacion = this.acumOcupacionNegocio.toString();
        String evento = "-";
        if (evt != null) {
            evento = evt.getTipo();
        }
        
        String rndPurg = (RNDPurga != null) ? RNDPurga.toString() : "-";
        String tiempoEntrePurgs = (this.tiempoEntrePurgas != null) ? this.tiempoEntrePurgas.toString() : "-";
        int porc = ((Gomeria)gomeria).getPorcentajeInestabilidadMax();
        String porcMaxInest = (porc != -1) ? porc + "" : "-";
        String proxPurga = (this.proximaPurga != null) ? this.proximaPurga.toString() : "-";
        String finDePurga = (this.finPurga != null) ? this.finPurga.toString() : "-";
        
        Object[] rowPrincipal = {reloj, evento, tel, proximaLlegada, rndCarga, carga, rndGoN, actividad, tAtSurt, finAtSurt1,
            finAtSurt2, finAtSurt3, estadoSurt1, colaSurt1, estadoSurt2, colaSurt2, estadoSurt3, colaSurt3, rndPurg, porcMaxInest,tiempoEntrePurgs,
            proxPurga, finDePurga, tAtGom, finAtG, estadoGom, colaGomeria, tAtNeg, finAtNeg, estadoNeg, colaNegocio, horaInicioOcNeg, 
            acumOcupacion, acumTEnColaSurt, acumTEnColaGom, acumTEnColaNegocio, contClientesSurtidor, contClientesGomeria, contClientesNegocio};
        modeloPrincipal.add(rowPrincipal);
        //modeloClientes

    }

    private void asignarTiempoAtencion(Servidor target, Double tiempoAtencion) {
        if (tiempoAtencion != null) {
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
                tiempoAtencionGomeria = tiempoAtencion;
                finAtencionGom = reloj + tiempoAtencionGomeria;
            } else if (target instanceof Negocio) {
                tiempoAtencionNegocio = tiempoAtencion;
                finAtencionNegocio = tiempoAtencionNegocio + reloj;
            }
        } else {
            tiempoAtencionSurtidor = null;
            tiempoAtencionGomeria = null;
            tiempoAtencionNegocio = null;
        }
    }

    private void calcularVariablesEstadisticas(Actividad atendida) {
        //Se pregunta por el que está siendo atendido ya que todavía no se finalizó la actividad
        if (atendida != null) {
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
        }

//        if (((Negocio)negocio).getHoraInicio() != null) {
//            Double tiempoOcupado = 0.0;
//            if (((Negocio) negocio).calcularOcupacion(reloj) != null) {
//                tiempoOcupado = ((Negocio) negocio).calcularOcupacion(reloj);
//            }
//            
//            this.acumOcupacionNegocio += tiempoOcupado;
//            ((Negocio)negocio).setHoraInicio(reloj);
//        }
    }

    public Double calcularPromedioSurtidor() {
        if (this.contColaSurtidor == 0) {
            return 0.0;
        }
        return this.acumTiempoEnColaSurtidor / this.contColaSurtidor;
    }

    public Double calcularPromedioNegocio() {
        if (this.contColaNegocio == 0) {
            return 0.0;
        }
        return this.acumTiempoEnColaNegocio / this.contColaNegocio;
    }

    public Double calcularPromedioGomeria() {
        if (this.contColaGomeria == 0) {
            return 0.0;
        }
        return this.acumTiempoEnColaGomeria / this.contColaGomeria;
    }

    public Double calcularPorcentajeOcupacion() {
        return ((this.acumOcupacionNegocio) * 100 / reloj);
    }

}
