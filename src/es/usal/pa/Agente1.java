package es.usal.pa;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

/*Configuracion: -container -host 127.0.0.1 -port 1099 nombre2:es.usal.pa.Agente2
"Conectate al conteiner que esta corrienda en mi propia máquina (127.0.0.1) en el puerto 1099,
y dentro de ese contenedor crea un agente llamado nombre2 que corresponde a l calse es.usal.pa.Agente2*/

public class Agente1 extends Agent {
//Agente1 hereda de jade.core.Agent
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected CyclicBehaviour cyclicBehaviour;

    protected void setup() {
        //Este es el pto de entrada de un agente, es el primer método que se ejecutando cuando el agente arranca
        //Se configura lo que el agente hará al inicio

        System.out.println(getLocalName() + ": Agente Iniciado");

        //Crear directorio de servicios (paginas amarillas)
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID()); //Recoge id del agente para el servicio dfd

        //Crear servicio
        ServiceDescription imprimir = new ServiceDescription();
        imprimir.setType("imprimir"); //Tipo de servicio
        imprimir.setName("Coordinador imprimir");//Nombre del servicio

        //Añadir el servicio a la descripcion del agente
        dfd.addServices(imprimir);

        //Registrar en el DF
        try {
            DFService.register(this, dfd);
            System.out.println(getLocalName() + "ha registrado servicio 'imprimir'");
        } catch (FIPAException e) {
            System.out.println(getLocalName() + "No pudo registrarse" + e.getACLMessage());
        }


        //Comportamiento ciclico
        cyclicBehaviour = new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;
            public void action() { //Define el codigo que se exe cada vez que el comportamiento se active
               //Solo se aceptan mensajes de un tipo, en este caso REQUEST y de tipo ontologia
                MessageTemplate mt = MessageTemplate.and (
                        MessageTemplate.MatchPerformative (ACLMessage.REQUEST),
                        MessageTemplate.MatchOntology("ontologia")
                );

                //El agente se bloquea (espera) hasta que llegue un mensaje que cumpla el filtro mt
                ACLMessage msg = blockingReceive(mt);

                //Si recibido
                if (msg!=null){
                    try{
                        System.out.println(getLocalName() + "ha recibido: " + msg.getContentObject());
                    }
                    catch (UnreadableException e){
                        e.printStackTrace();
                    }
                }
                //Si no hay mennsajes, el comportamiento se bloquea y no socnume recursos del sistema
                //esperando a que llegue un mensaje valido
                else{
                    block();
                }
            }
        };

        //Enviar mensaje a Agente2
        ACLMessage mensajeAgente2 = new ACLMessage(ACLMessage.REQUEST);
        mensajeAgente2.addReceiver(new AID("Agente2", AID.ISLOCALNAME));
        mensajeAgente2.setOntology( ("ontologia"));
        try {
            mensajeAgente2.setContentObject("Hola desde Agente1");
        } catch (Exception e) {
             e.printStackTrace();
        }
        send (mensajeAgente2);
        System.out.println(getLocalName () + "ha enviado mensaje a Agente2");

    }

}
