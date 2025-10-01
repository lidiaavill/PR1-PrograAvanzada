package es.usal.pa;


import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.SearchConstraints;

import java.util.Iterator;

public class Agente2 extends Agent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected CyclicBehaviour cyclicBehaviour;

    protected DFAgentDescription buscarAgente (String tipo){
        DFAgentDescription buscarAgente = new DFAgentDescription();
        ServiceDescription buscarAServicio = new ServiceDescription();
        buscarAServicio.setType(tipo); //tipo del servicio a buscar
        buscarAgente.addServices(buscarAServicio); //Añade la descripción del servicio a la descripcion del agente

        SearchConstraints sc = new SearchConstraints(); //Prepara objeto para limitar rdos de busqueda
        sc.setMaxResults(1l); // "Solo quiero el primer agente que encuentres que ofrezca este servicio".

        try{
            //Busca en el DF agentes que coincidan con la plantilla buscarAgente y restricciones
            DFAgentDescription[] agentesEncontrados= DFService.search(this, buscarAgente, sc);

            if (agentesEncontrados.length >0) {
                System.out.println(getLocalName() + "encontro los siguientes agentes");

                //Recorre cada agente encontrado
                for (DFAgentDescription dfd : agentesEncontrados) {
                    // en cada iteracion asigna la descripcion del agente actual a la variable dfd
                    AID idAgent = dfd.getName(); //guarda el id del agente descrito por dfd en provider

                    //Recorre todos los servicios que ofrece el agente descrito por dfd
                    //it.hasNext () verifica si hay más servicios por recorrer
                    for (Iterator it = dfd.getAllServices(); it.hasNext(); ) {
                        ServiceDescription servicioBuscado = (ServiceDescription) it.next(); //(ServiceDescription) es un "cast" que convierte el objeto devuelto por it.next() a tipo ServiceDescription.

                        if (servicioBuscado.getType().equals(tipo)) {
                            System.out.println("- Servicio \"" + servicioBuscado.getName() +
                                    "\" proporcionado por el agente " + idAgent.getLocalName());
                            return dfd;
                        }
                    }
                }
            }

            else
                System.out.println  (getLocalName() + "no encontro ningun servicio");
        }

        catch (FIPAException e) {
            e.printStackTrace();
        }

        return null;

    }


    protected void setup() {

        System.out.println(getLocalName() + "iniciando y buscando servicio 'imprimir'");

        //Buscar al agente que proporcione el servicio "imprimir"
        DFAgentDescription dfd = buscarAgente("imprimir");
        if (dfd!=null) {
            AID receptor = dfd.getName();

            ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
            mensaje.addReceiver(receptor);
            mensaje.setOntology("ontologia");

            try {
                mensaje.setContentObject("Hola Agente1, soy Agente 2 encontrandote por el DF ");

            }catch(Exception e) {
                e.printStackTrace();
            }

            send(mensaje);
            System.out.println(getLocalName() + "ha enviado mensaje a" + receptor.getLocalName());

        }

            //Comportamiento ciclico para recibir mensajes

            addBehaviour(new CyclicBehaviour(this) {
                private static final long serialVersionUID = 1L;

                public void action() {

                    MessageTemplate mt = MessageTemplate.and( //Plantilla para filtrar los mensajes que el agente recibe
                        MessageTemplate.MatchPerformative(ACLMessage.REQUEST), //Filtra los mensajes, solo los REQUEST
                        MessageTemplate.MatchOntology("ontologia") //Solo mensajes qu tengan ontologia, es decir, tema
                );

                //Esperamos un mensaje que coincida con la plantilla
                    ACLMessage msg = blockingReceive(mt);
                    if (msg != null) {
                        try {
                            System.out.println("Mensaje recibido:" + msg.getContentObject());

                        }catch (UnreadableException e) {
                            e.printStackTrace();
                        }

                    }
                    else
                        block(); //Bloqueamos hasta que llegue el mensaje

            }


        });

        System.out.println (getLocalName() + " iniciado y esperando mensajes ...");
    }
}