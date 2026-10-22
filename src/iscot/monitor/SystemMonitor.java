package iscot.monitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.Message;
import iscot.message.RegistrationMessage;
import iscot.message.sensor.LightSensorMessage;
import iscot.message.sensor.TemperatureSensorMessage;
import iscot.monitor.SystemMonitor.DebugThread;
import iscot.monitor.SystemMonitor.MonitorThread;
import iscot.server.Client;
import iscot.server.DealWithClient;
import iscot.server.Server;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class SystemMonitor {

	public abstract class MonitorThread extends Client{
		public MonitorThread(Server server, Areas area,JPanel panel) {
			super(server, area);
			this.panel=panel;
			// TODO Auto-generated constructor stub
		}
		public JPanel getPanel() {
			return panel;
		}
		private JPanel panel;

	}

	public class DebugThread extends MonitorThread implements Observer {
		public DebugThread(Server server, Areas area, JPanel panel) {
			super(server, area, panel);
			server.addObserver(this);
		}

		@Override
		public Type getType() {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public void update(Observable arg0, Object arg1) {

			Object obj=arg1;
			if(obj instanceof Message) {
				Message message=(Message)obj;
				if(logMessageExists(message)) {
					removeLogMessage(message.toString());
					log("Deleted:"+message.toString());
				}
				else { 
					log(message.toString());
				}
			} else {// Client!
				log(obj.toString());
				Client client=(Client)obj;
				adicionarCliente(client.getArea(), client.getClientId()+"-"+client.getType());
			}
			numMessagesLabel.setText(  server==null ||server.checkMessageQueue()==null?"0": server.checkMessageQueue().size()+"");
			getPanel().invalidate();
		}



	}

	public class SalaThread extends MonitorThread{

		public SalaThread(Server server,JPanel jPanel) {
			super(server, Areas.Sala,jPanel);
		}

		@Override
		public void run() {
			try {
				while(true) {
					Message message=server.getMessage(Type.SENSOR, getArea());
//					System.out.println("Sala got "+message);
					if(message instanceof LightSensorMessage) {
						System.err.println("Updating GUI to"+((LightSensorMessage)message));
						sensorIluminacaoSala.setText(((LightSensorMessage)message).light+"");
						sensorIluminacaoSala.invalidate();
					}
					else
						System.err.println("Undealt message in Sala:"+message);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public Type getType() {
			return Type.DECIDER;
		}

	}
	//	private final Map<Integer, String> debugMessages =
	//			new LinkedHashMap<>();

	private final DefaultListModel<String> debugListModel =
			new DefaultListModel<>();

	private final JList<String> debugList =
			new JList<>(debugListModel);

	private final Map<String, DefaultListModel<String>> clientesPorArea =
			new LinkedHashMap<>();

	private Server server;

	// editable components
	JTextField sensorIluminacaoSala;
	JLabel numMessagesLabel;
	
	private JTextField sensorIluminacaoQuarto;
	//	private int nextLogId = 0;
	public SystemMonitor(Server server) throws IOException {
		this.server=server;
		// Distribuir as janelas em duas colunas

		JPanel cozinhaPanel= criarCozinha();
		JPanel wcPanel=criarWC();
		JPanel quartoPanel=criarQuarto(true); 
		JPanel salaPanel=criarSala(true);
		JPanel jardimPanel=criarJardim();

		// Lançar threads
		SalaThread salaThread=new SalaThread(server,salaPanel);
		salaThread.start();

		abrirJanela("Cozinha", cozinhaPanel);
		abrirJanela("WC", wcPanel);
		abrirJanela("Sala",salaPanel);
		abrirJanela("Quarto", quartoPanel);
		abrirJanela("Jardim", jardimPanel);


		JPanel debugPanel= abrirJanelaDebug();
		DebugThread debugThread= new DebugThread(server,null,debugPanel);
		debugThread.start();

		// DEBUG
		//		log("TESTE");
		System.err.println("Janelas lançadas!");

	}

	private JPanel abrirJanelaDebug() {
		JPanel painel = new JPanel(new BorderLayout());

		painel.setBorder(
				BorderFactory.createTitledBorder("Mensagens do sistema")
				);

		painel.add(new JScrollPane(debugList), BorderLayout.CENTER);

		JFrame janela = new JFrame("Debug");
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		janela.setContentPane(painel);
		janela.setSize(600, 300);
		janela.setLocation(450, 580);
		// Janela de debug na parte inferior do ecrã
		janela.setVisible(true);
		return painel;
	}

	private void abrirJanela(String titulo, JPanel conteudo) {
		JFrame janela = new JFrame(titulo);

		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		janela.setContentPane(conteudo);
		janela.pack();
		janela.setMinimumSize(janela.getSize());

		// Distribuir as janelas em duas colunas
		int[][] posicoes = {
				{20, 20},    // Cozinha
				{450, 20},   // WC
				{20, 300},   // Sala
				{450, 300},  // Quarto
				{20, 580}    // Jardim
		};

		int indice;
		switch (titulo) {
		case "Cozinha": indice=0;
		break;
		case "WC": indice= 1;
		break;
		case "Sala" : indice=2;
		break;
		case "Quarto" : indice=3;
		break;
		case "Jardim" : indice=4;
		break;
		default : indice=-1;
		};

		if (indice >= 0) {
			janela.setLocation(posicoes[indice][0], posicoes[indice][1]);
		}

		janela.setVisible(true);
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

	private JTextField adicionarLinha(JPanel painel, String etiqueta,String texto) {
		painel.add(new JLabel(etiqueta));
		if(texto.length()>0) {
			JTextField valor=new JTextField(texto);
			painel.add(valor);
			return valor;
		}
		return null; 
	}

	private void criarListaClientes(JPanel painel, String area) {
		painel.add(new JLabel("Clientes inscritos:"));

		DefaultListModel<String> modelo = new DefaultListModel<>();
		clientesPorArea.put(area, modelo);

		JList<String> lista = new JList<>(modelo);
		lista.setVisibleRowCount(3);

		painel.add(new JScrollPane(lista));
	}

	public void adicionarCliente(Areas areas, String string) {
		SwingUtilities.invokeLater(() -> {
			DefaultListModel<String> modelo = clientesPorArea.get(areas.toString());

			if (modelo == null) {
				throw new IllegalArgumentException(
						"Área desconhecida: " + areas
						);
			}
			modelo.addElement(string);
		});
	}

	// DEBUG
	public JPanel criarDebug() {
		JPanel debugPanel = new JPanel(new BorderLayout());

		debugPanel.setBorder(
				BorderFactory.createTitledBorder("Debug / Mensagens do sistema")
				);

		debugPanel.add(
				new JScrollPane(debugList),
				BorderLayout.CENTER
				);
		debugPanel.setLocation(450,500);
		
		numMessagesLabel=new JLabel("0");
		debugPanel.add(
				numMessagesLabel,
				BorderLayout.NORTH
				);

		return debugPanel;
	}


	public void log(String mensagem) {
		SwingUtilities.invokeLater(() -> {
			debugListModel.addElement(mensagem.toString() );

			int last = debugListModel.size() - 1;
			debugList.setSelectedIndex(last);
			debugList.ensureIndexIsVisible(last);
		});
	}

	public boolean logMessageExists(Message message) {
		return debugListModel.contains(message.toString());
	}
	public void removeLogMessage(String mensagem) {
		SwingUtilities.invokeLater(() -> {
			if (debugListModel.contains(mensagem.toString())) 
				debugListModel.removeElement(mensagem.toString());
		});
	}

	//
	//	public void removeLogMessage(int id) {
	//		SwingUtilities.invokeLater(() -> {
	//			if (debugMessages.containsKey(id)) {
	//				debugMessages.remove(id);
	//
	//				for (int i = 0; i < debugListModel.size(); i++) {
	//					if (debugListModel.getElementAt(i).startsWith("["+i+"]")) {
	//						debugListModel.remove(i);
	//						break;
	//					}
	//				}
	//			}
	//		});
	//	}
	public void clearLog() {
		SwingUtilities.invokeLater(() -> {
			debugListModel.clear();
		});
	}
	// COZINHA
	private JPanel criarCozinha() {
		JPanel painel = criarPainel("Cozinha");

		adicionarLinha(painel, "Sensor de iluminação:","320 lux");

		JCheckBox luz = new JCheckBox("Luz ligada");
		painel.add(luz);

//		adicionarLinha(painel, "Posição do estore:","");
//
//		JSlider estore = new JSlider(0, 100, 0);
//		estore.setMajorTickSpacing(25);
//		estore.setPaintTicks(true);
//		estore.setPaintLabels(true);
//		painel.add(estore);

//		adicionarLinha(painel,"Estore:", "0%");
		criarListaClientes(painel, "Cozinha");

		return painel;
	}

	// WC
	private JPanel criarWC() {
		JPanel painel = criarPainel("WC");

		JTextField sensorHumidadeWC= adicionarLinha(painel, "Sensor de humidade:", "68%");

		JCheckBox ventilacao = new JCheckBox("Ventilação ligada");
		painel.add(ventilacao);

		criarListaClientes(painel, "WC");

		return painel;
	}

	// SALA E QUARTO
	private JPanel criarQuarto(boolean arCondicionado) {
		JPanel painel = criarPainel("Quarto");

		adicionarLinha(painel, "Sensor de temperatura:", "22.5 °C");
		sensorIluminacaoQuarto= adicionarLinha(painel, "Sensor de iluminação:", "250 lux");

		JCheckBox teto = new JCheckBox("Lâmpada de teto ligada");
		painel.add(teto);

		JCheckBox candeeiro = new JCheckBox("Candeeiro ligado");
		painel.add(candeeiro);

		if (arCondicionado) {
			adicionarLinha(painel, "Ar condicionado:","");

			JComboBox<String> modoAC = new JComboBox<>(
					new String[] {
							"Desligado",
							"Frio",
							"Quente",
							"Ventilação"
					});
			painel.add(modoAC);
		}

		criarListaClientes(painel, "Quarto");

		return painel;
	}


	private JPanel criarSala( boolean arCondicionado) {
		JPanel painel = criarPainel("Sala");

		adicionarLinha(painel, "Sensor de temperatura:", "22.5 °C");
		sensorIluminacaoSala= adicionarLinha(painel, "Sensor de iluminação:", "250 lux");

		JCheckBox teto = new JCheckBox("Lâmpada de teto ligada");
		painel.add(teto);

		JCheckBox candeeiro = new JCheckBox("Candeeiro ligado");
		painel.add(candeeiro);

		if (arCondicionado) {
			adicionarLinha(painel, "Ar condicionado:","");

			JComboBox<String> modoAC = new JComboBox<>(
					new String[] {
							"Desligado",
							"Frio",
							"Quente",
							"Ventilação"
					});
			painel.add(modoAC);
		}

		criarListaClientes(painel, "Sala");

		return painel;
	}

	// JARDIM
	private JPanel criarJardim() {
		JPanel painel = criarPainel("Jardim");

		adicionarLinha(painel, "Humidade do solo:"," 42%");
		adicionarLinha(painel, "Pluviosidade:"," 0 mm/h");

		adicionarLinha(painel, "Comandos de rega:","");

		JCheckBox rega1 = new JCheckBox("Área de rega 1 ligada");
		JCheckBox rega2 = new JCheckBox("Área de rega 2 ligada");
		JCheckBox rega3 = new JCheckBox("Área de rega 3 ligada");

		painel.add(rega1);
		painel.add(rega2);
		painel.add(rega3);

		criarListaClientes(painel, "Jardim");

		return painel;
	}



}
