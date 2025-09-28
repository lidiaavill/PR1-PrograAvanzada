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
        //Este es el pto de entrada d eun agente
        cyclicBehaviour = new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;

            public void action() {
                //Código que se repite continuamente
                block();
            }
        };
        addBehaviour(cyclicBehaviour);
        //"Este agente va a exe. este comportamiento"

        //REGISTRAR SERVICIO
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        //Crear descripcion del servicio
        ServiceDescription sd = new ServiceDescription();
        sd.setType("imprimir"); //Tipo de servicio
        sd.setName("Coordinador imprimir");//Nombre del servicio

        //Añadir el servicio a la descripcion del agente
        dfd.addServices(sd);

        //Registrar en el DF
        try {
            DFService.register(this, dfd);
            System.out.println(getLocalName() + "ha registrado servicio 'imprimir'");
        } catch (FIPAException e) {
            System.out.println(getLocalName() + "No pudo registrarse" + e.getACLMessage());
        }
    }

}
