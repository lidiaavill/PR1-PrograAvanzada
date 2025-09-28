package es.usal.pa;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class Agente2 extends Agent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected CyclicBehaviour cyclicBehaviour;

    protected void setup() {
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
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                } else {
                    block(); //Bloqueamos hasta que llegue el mensaje
                }
            }


        });

        System.out.println (getLocalName() + " iniciado y esperando mensajes ...");
    }
}