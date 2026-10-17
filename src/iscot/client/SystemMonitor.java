package iscot.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import iscot.enums.Type;
import iscot.message.Message;
import iscot.message.RegistrationMessage;
import iscot.message.sensor.LightSensorMessage;
import iscot.message.sensor.TemperatureSensorMessage;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class SystemMonitor extends JFrame {

    private static final long serialVersionUID = 1L;



	public SystemMonitor() {
        super("Sistema de Domótica");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setLocationRelativeTo(null);

        JPanel casa = new JPanel(new GridLayout(0, 2, 10, 10));
        casa.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        casa.add(criarCozinha());
        casa.add(criarWC());
        casa.add(criarDivisao("Sala", true));
        casa.add(criarDivisao("Quarto", true));
        casa.add(criarJardim());

        // Permite apresentar cinco divisões.
        JPanel principal = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel(
                "Monitorização e controlo da casa",
                SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        principal.add(titulo, BorderLayout.NORTH);
        principal.add(casa, BorderLayout.CENTER);

        setContentPane(principal);
    }

    private JPanel criarPainel(String nome) {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                nome,
                TitledBorder.LEFT,
                TitledBorder.TOP));
        return painel;
    }

    private void adicionarLinha(JPanel painel, String texto) {
        painel.add(new JLabel(texto));
    }

    // COZINHA
    private JPanel criarCozinha() {
        JPanel painel = criarPainel("Cozinha");

        adicionarLinha(painel, "Sensor de iluminação: 320 lux");

        JCheckBox luz = new JCheckBox("Luz ligada");
        painel.add(luz);

        adicionarLinha(painel, "Posição do estore:");

        JSlider estore = new JSlider(0, 100, 0);
        estore.setMajorTickSpacing(25);
        estore.setPaintTicks(true);
        estore.setPaintLabels(true);
        painel.add(estore);

        JLabel posicao = new JLabel("Estore: 0%");
        painel.add(posicao);

        estore.addChangeListener(e ->
                posicao.setText("Estore: " + estore.getValue() + "%"));

        return painel;
    }

    // WC
    private JPanel criarWC() {
        JPanel painel = criarPainel("WC");

        adicionarLinha(painel, "Sensor de humidade: 68%");

        JCheckBox ventilacao = new JCheckBox("Ventilação ligada");
        painel.add(ventilacao);

        return painel;
    }

    // SALA E QUARTO
    private JPanel criarDivisao(String nome, boolean arCondicionado) {
        JPanel painel = criarPainel(nome);

        adicionarLinha(painel, "Sensor de temperatura: 22.5 °C");
        adicionarLinha(painel, "Sensor de iluminação: 250 lux");

        JCheckBox teto = new JCheckBox("Lâmpada de teto ligada");
        painel.add(teto);

        JCheckBox candeeiro = new JCheckBox("Candeeiro ligado");
        painel.add(candeeiro);

        if (arCondicionado) {
            adicionarLinha(painel, "Ar condicionado:");

            JComboBox<String> modoAC = new JComboBox<>(
                    new String[] {
                        "Desligado",
                        "Frio",
                        "Quente",
                        "Ventilação"
                    });
            painel.add(modoAC);
        }

        return painel;
    }

    // JARDIM
    private JPanel criarJardim() {
        JPanel painel = criarPainel("Jardim");

        adicionarLinha(painel, "Humidade do solo: 42%");
        adicionarLinha(painel, "Pluviosidade: 0 mm/h");

        adicionarLinha(painel, "Comandos de rega:");

        JCheckBox rega1 = new JCheckBox("Área de rega 1 ligada");
        JCheckBox rega2 = new JCheckBox("Área de rega 2 ligada");
        JCheckBox rega3 = new JCheckBox("Área de rega 3 ligada");

        painel.add(rega1);
        painel.add(rega2);
        painel.add(rega3);

        return painel;
    }



	/**
	 * 
	 * @param server address and port
	 * @param id: int
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if(args.length!=3) {
			System.err.println("Invalid arguments!");
		}
		else {
			Socket socket=new Socket(args[0],Integer.parseInt(args[1]));
			ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); // useless, no incoming messages

			// register with the server
			String area=args[2];
			out.writeObject(new RegistrationMessage("All", iscot.enums.Type.DECIDER ,Integer.parseInt(args[2])));


	        SwingUtilities.invokeLater(() -> {
	            SystemMonitor gui = new SystemMonitor();
	            gui.setVisible(true);
	        });
	        
			while(true) {
				Message message=(Message)in.readObject();
				System.err.println("Monitor got:"+message);
			}
		}
	}

}
