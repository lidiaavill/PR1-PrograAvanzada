package es.usal.pa;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Agente2 extends Agent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected CyclicBehaviour cyclicBehaviour;

    protected void setup() {
        cyclicBehaviour = new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;
            public void action() {
                block ();
            }
        };
        addBehaviour(cyclicBehaviour);


    }
}