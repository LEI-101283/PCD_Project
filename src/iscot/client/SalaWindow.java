package iscot.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import iscot.client.gui.ACIcon;
import iscot.client.gui.ActuatorPanel;
import iscot.client.gui.IrrigationIcon;
import iscot.client.gui.LightIcon;
import iscot.client.gui.SensorControl;
import iscot.client.gui.VentilatorIcon;
import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.events.AreaIrrigationOrderEvent;
import iscot.events.AreaLightingOrderEvent;
import iscot.events.AreaTemperatureOrderEvent;
import iscot.events.AreaVentilationOrderEvent;
import iscot.events.GroundMoistureSensorEvent;
import iscot.events.HumiditySensorEvent;
import iscot.events.LightSensorEvent;
import iscot.events.SensorEvent;
import iscot.events.TemperatureSensorEvent;

/**
 * Janela única com todos os sensores e atuadores da Sala (demonstração da fase 3).
 *
 * Sensores: ao largar o slider gera-se o evento (consola + registo da janela).
 * Atuadores: o botão "Simular ordens" aplica ordens de exemplo e regista-as no histórico.
 */
public class SalaWindow {

	private static final Areas AREA = Areas.Sala;
	private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("HH:mm:ss");

	private final LightIcon luz = new LightIcon();
	private final VentilatorIcon ventilador = new VentilatorIcon();
	private final ACIcon ar = new ACIcon();
	private final IrrigationIcon rega = new IrrigationIcon();

	private final ActuatorPanel painelLuz = new ActuatorPanel(luz, 60);
	private final ActuatorPanel painelVentilador = new ActuatorPanel(ventilador, 60);
	private final ActuatorPanel painelAr = new ActuatorPanel(ar, 60);
	private final ActuatorPanel painelRega = new ActuatorPanel(rega, 60);

	private final JTextArea registo = new JTextArea(5, 40);

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// fica com o aspeto por omissão
		}
		SwingUtilities.invokeLater(() -> new SalaWindow().mostrar());
	}

	private void mostrar() {
		JFrame janela = new JFrame("IscoT - " + AREA);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel titulo = new JLabel("Casa inteligente IscoT  |  " + AREA);
		titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));

		JPanel centro = new JPanel(new BorderLayout(10, 10));
		JPanel sensores = criarSensores();
		sensores.setPreferredSize(new Dimension(300, 0));
		centro.add(sensores, BorderLayout.WEST);
		centro.add(criarAtuadores(), BorderLayout.CENTER);

		JPanel conteudo = new JPanel(new BorderLayout(10, 10));
		conteudo.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		conteudo.add(titulo, BorderLayout.NORTH);
		conteudo.add(centro, BorderLayout.CENTER);
		conteudo.add(criarRodape(), BorderLayout.SOUTH);

		janela.setContentPane(conteudo);
		janela.pack();
		janela.setSize(1600,900);
		janela.setLocationRelativeTo(null);
		janela.setVisible(true);
	}

	// ---------- Sensores ----------

	private JPanel criarSensores() {
		JPanel p = new JPanel(new GridLayout(4, 1, 8, 8));
		p.setBorder(BorderFactory.createTitledBorder("Sensores"));

		p.add(new SensorControl("☀", "Luminosidade", "%", 0, 100, 1, 50,
				v -> sensorLido("Luz", "%", new LightSensorEvent(AREA, v))));
		p.add(new SensorControl("🌡", "Temperatura", "ºC", 16, 30, 1, 22,
		        v -> sensorLido("Temperatura", "ºC", new TemperatureSensorEvent(AREA, v))));
		p.add(new SensorControl("💧", "Humidade do ar", "%", 0, 100, 1, 50,
				v -> sensorLido("Humidade", "%", new HumiditySensorEvent(AREA, v))));
		p.add(new SensorControl("🌱", "Humidade do solo", "%", 0, 100, 1, 50,
				v -> sensorLido("Solo", "%", new GroundMoistureSensorEvent(AREA, v))));
		return p;
	}

	private void sensorLido(String nome, String unidade, SensorEvent evento) {
		System.out.println("GERADO (Sensor " + nome + "): " + evento);
		// TODO enviar o evento ao servidor (fase 4)
		escreverRegisto(String.format("%s  Sensor %s: %.0f%s", hora(), nome, evento.getValue(), unidade));
	}

	// ---------- Atuadores ----------

	private JPanel criarAtuadores() {
		JPanel p = new JPanel(new GridLayout(2, 2, 8, 8));
		p.setBorder(BorderFactory.createTitledBorder("Atuadores"));
		p.add(caixa("Luz", painelLuz));
		p.add(caixa("Ventilador", painelVentilador));
		p.add(caixa("Ar condicionado", painelAr));
		p.add(caixa("Rega", painelRega));
		return p;
	}

	private static ActuatorPanel caixa(String nome, ActuatorPanel painel) {
		painel.setBorder(BorderFactory.createTitledBorder(nome));
		return painel;
	}

	private void aplicar(AreaLightingOrderEvent ordem) {
		luz.setLigada(ordem.isTurnOnLights());
		painelLuz.registar(ordem.toString());
		ordemRecebida(ordem.toString());
	}

	private void aplicar(AreaVentilationOrderEvent ordem) {
		ventilador.setLigado(ordem.isOn()); // arranca/pára a rotação das pás
		painelVentilador.registar(ordem.toString());
		ordemRecebida(ordem.toString());
	}

	private void aplicar(AreaTemperatureOrderEvent ordem) {
		if (ordem.getMode() != AirConditioningMode.Desligado) {
			ar.setTemperatura(ordem.getTargetTemperature()); // o ACIcon só aceita 16 a 30 ºC
		}
		ar.setModo(ordem.getMode());
		painelAr.registar(ordem.toString());
		ordemRecebida(ordem.toString());
	}

	private void aplicar(AreaIrrigationOrderEvent ordem) {
		rega.setAtiva(ordem.isTurnOnWater());
		painelRega.registar(ordem.toString());
		ordemRecebida(ordem.toString());
	}

	private void ordemRecebida(String descricao) {
		System.out.println("Ordem recebida: " + descricao);
		escreverRegisto(hora() + "  Ordem: " + descricao);
	}

	// ---------- Rodapé: botão e registo ----------

	private JPanel criarRodape() {
		registo.setEditable(false);
		JScrollPane scroll = new JScrollPane(registo);
		scroll.setBorder(BorderFactory.createTitledBorder("Registo de eventos"));

		JButton simular = new JButton("Simular ordens");
		simular.addActionListener(e -> simularOrdens(simular));

		JPanel botoes = new JPanel(new BorderLayout());
		botoes.add(simular, BorderLayout.NORTH);

		JPanel p = new JPanel(new BorderLayout(10, 0));
		p.add(scroll, BorderLayout.CENTER);
		p.add(botoes, BorderLayout.EAST);
		return p;
	}

	/** Fase 3: ordens de exemplo geradas localmente (na fase 4 virão do servidor). */
	private void simularOrdens(JButton botao) {
		botao.setEnabled(false);
		new Thread(() -> {
			try {
				aplicar(new AreaLightingOrderEvent(AREA, true));
				aplicar(new AreaVentilationOrderEvent(AREA, true));
				aplicar(new AreaTemperatureOrderEvent(AREA, AirConditioningMode.Frio, 22));
				aplicar(new AreaIrrigationOrderEvent(AREA, true));
				Thread.sleep(5000);

				aplicar(new AreaLightingOrderEvent(AREA, false));
				aplicar(new AreaVentilationOrderEvent(AREA, false));
				aplicar(new AreaTemperatureOrderEvent(AREA, AirConditioningMode.Aquecimento, 18));
				aplicar(new AreaIrrigationOrderEvent(AREA, false));
				Thread.sleep(5000);

				aplicar(new AreaTemperatureOrderEvent(AREA, AirConditioningMode.Desligado, 0));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} finally {
				SwingUtilities.invokeLater(() -> botao.setEnabled(true));
			}
		}).start();
	}

	// ---------- Utilitários ----------

	private static String hora() {
		return LocalTime.now().format(FORMATO);
	}

	private void escreverRegisto(String linha) {
		SwingUtilities.invokeLater(() -> {
			registo.append(linha + "\n");
			registo.setCaretPosition(registo.getDocument().getLength());
		});
	}
}
