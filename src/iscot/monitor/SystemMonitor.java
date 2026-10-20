package iscot.monitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.Message;
import iscot.message.RegistrationMessage;
import iscot.message.sensor.LightSensorMessage;
import iscot.message.sensor.TemperatureSensorMessage;
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
		private Server server;
		private JPanel panel;

	}
	
	public class WcThread extends MonitorThread{

		public WcThread(Server server,JPanel jPanel) {
			super(server, Areas.WC,jPanel);
		}

		@Override
		public void run() {
			try {
				Message message=server.getMessage(Type.SENSOR, getArea());
				System.out.println("WC got "+message);
				
				
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
	private final Map<Integer, String> debugMessages =
			new LinkedHashMap<>();

	private final DefaultListModel<String> debugListModel =
			new DefaultListModel<>();

	private final JList<String> debugList =
			new JList<>(debugListModel);

	private final Map<String, DefaultListModel<String>> clientesPorArea =
			new LinkedHashMap<>();
	private Server server;

	private int nextLogId = 0;
	public SystemMonitor(Server server) throws IOException {
		this.server=server;
		// Distribuir as janelas em duas colunas

		JPanel cozinhaPanel= criarCozinha();
		JPanel wcPanel=criarWC();
		JPanel quartoPanel=criarDivisao("Quarto", true); 
		JPanel salaPanel=criarDivisao("Sala", true);
		JPanel jardimPanel=criarJardim();
		
		// Lançar threads
		WcThread wcThread=new WcThread(server,wcPanel);
		wcThread.start();
		
		abrirJanela("Cozinha", cozinhaPanel);
		abrirJanela("WC", wcPanel);
		abrirJanela("Sala",salaPanel);
		abrirJanela("Quarto", quartoPanel);
		abrirJanela("Jardim", jardimPanel);

		
		abrirJanelaDebug();


		// DEBUG
		log("TESTE");
		System.err.println("Janelas lançadas!");

	}

	private void abrirJanelaDebug() {
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

		int indice = switch (titulo) {
		case "Cozinha" -> 0;
		case "WC" -> 1;
		case "Sala" -> 2;
		case "Quarto" -> 3;
		case "Jardim" -> 4;
		default -> -1;
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

	private void adicionarLinha(JPanel painel, String texto) {
		painel.add(new JLabel(texto));
	}

	private void criarListaClientes(JPanel painel, String area) {
		painel.add(new JLabel("Clientes inscritos:"));

		DefaultListModel<String> modelo = new DefaultListModel<>();
		clientesPorArea.put(area, modelo);

		JList<String> lista = new JList<>(modelo);
		lista.setVisibleRowCount(3);

		painel.add(new JScrollPane(lista));
	}

	public void adicionarCliente(String area, String cliente) {
		SwingUtilities.invokeLater(() -> {
			DefaultListModel<String> modelo = clientesPorArea.get(area);

			if (modelo == null) {
				throw new IllegalArgumentException(
						"Área desconhecida: " + area
						);
			}

			modelo.addElement(cliente);
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

		return debugPanel;
	}


	public void log(String mensagem) {
		SwingUtilities.invokeLater(() -> {
			int id = nextLogId++;

			debugMessages.put(id, mensagem);
			debugListModel.addElement("["+id+"] - "+mensagem );

			int last = debugListModel.size() - 1;
			debugList.setSelectedIndex(last);
			debugList.ensureIndexIsVisible(last);
		});
	}

	public void removeLogMessage(int id) {
		SwingUtilities.invokeLater(() -> {
			if (debugMessages.containsKey(id)) {
				debugMessages.remove(id);

				for (int i = 0; i < debugListModel.size(); i++) {
					if (debugListModel.getElementAt(i).startsWith("["+i+"]")) {
						debugListModel.remove(i);
						break;
					}
				}
			}
		});
	}

	public void clearLog() {
		SwingUtilities.invokeLater(() -> {
			debugMessages.clear();
			debugListModel.clear();
		});
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

		criarListaClientes(painel, "Cozinha");

		return painel;
	}

	// WC
	private JPanel criarWC() {
		JPanel painel = criarPainel("WC");

		adicionarLinha(painel, "Sensor de humidade: 68%");

		JCheckBox ventilacao = new JCheckBox("Ventilação ligada");
		painel.add(ventilacao);

		criarListaClientes(painel, "WC");

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

		criarListaClientes(painel, nome);

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

		criarListaClientes(painel, "Jardim");

		return painel;
	}



}
