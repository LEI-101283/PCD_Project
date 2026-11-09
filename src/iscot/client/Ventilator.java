package iscot.client;

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
import iscot.client.gui.*;
import iscot.enums.*;
import iscot.events.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Ventilator {

	private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	private static class AreaState {
        boolean ventilador;
        boolean luz;
        boolean irrigacao;
        AirConditioningMode arCondicionado =
            AirConditioningMode.Desligado;
    }
	
	private static final List<String> historico = new ArrayList<>();
	private static boolean estadoVentilador = false;
	
	private static final double HUM_INICIAL = 50;
	private static final double TEMP_INICIAL = 26;
	private static final double LUM_INICIAL = 70;
	private static final double SOLO_INICIAL = 50;

	/**
	 * 
	 * @param server    address and port
	 * @param roomName: string
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException,
			InterruptedException, ClassNotFoundException {
		if (args.length != 3) {
			System.err.println("Invalid arguments!");
			return;
		} else {
			// TODO connect to server

			// TODO register with the server

			// TODO: receive and handle messages from server

			// TODO: for first phase: create an appropriate event hardcoded locally and
			// change the GUI accordingly
			
			try {
	            Areas area = Areas.valueOf(args[2]);
	            SwingUtilities.invokeLater(() -> criarJanela(area));
	        } catch (IllegalArgumentException e) {
	            System.err.println("Área inválida: " + args[2]);
	        }
			
		}
	}

	private static void criarJanela(Areas area) {

	    AreaState estado = new AreaState();

	    JFrame janela = new JFrame("IscoT - " + area);
	    janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    // Ícones dos atuadores
	    VentilatorIcon ventilador = new VentilatorIcon();
	    LightIcon luz = new LightIcon();
	    ACIcon ar = new ACIcon();
	    IrrigationIcon irrigacao = new IrrigationIcon();

	    // Painéis principais
	    JPanel sensores = new JPanel();
	    sensores.setBorder(BorderFactory.createTitledBorder("Sensores"));

	    JPanel atuadores = new JPanel();
	    atuadores.setBorder(BorderFactory.createTitledBorder("Atuadores"));

	    // Histórico
	    JTextArea textoHistorico = new JTextArea(7, 35);
	    textoHistorico.setEditable(false);
	    textoHistorico.setLineWrap(true);
	    textoHistorico.setWrapStyleWord(true);

	    // Criar sensores e atuadores conforme a divisão
	    switch (area) {

	        case WC:

	            sensores.setLayout(new GridLayout(2, 2, 10, 10));
	            atuadores.setLayout(new GridLayout(1, 2, 10, 10));

	            sensores.add(new SensorControl(
	                "💧", "Humidade", "%", 0, 100, 1, 50,
	                valor -> processarHumidade(
	                    area, estado, ventilador, valor, textoHistorico
	                )
	            ));

	            sensores.add(new SensorControl(
	                "🌡️", "Temperatura", "°C", 0, 40, 1, 26,
	                valor -> processarTemperatura(
	                    area, estado, ar, valor, textoHistorico
	                )
	            ));

	            sensores.add(new SensorControl(
	                "☀️", "Luminosidade", "%", 0, 100, 1, 70,
	                valor -> processarLuminosidade(
	                    area, estado, luz, valor, textoHistorico
	                )
	            ));

	            atuadores.add(criarPainelAtuador("Ventilador", ventilador));
	            atuadores.add(criarPainelAtuador("Iluminação", luz));

	            break;

	        case Cozinha:

	            sensores.setLayout(new GridLayout(2, 2, 10, 10));
	            atuadores.setLayout(new GridLayout(2, 2, 10, 10));

	            sensores.add(new SensorControl(
	                "💧", "Humidade", "%", 0, 100, 1, HUM_INICIAL,
	                valor -> processarHumidade(
	                    area, estado, ventilador, valor, textoHistorico
	                )
	            ));

	            sensores.add(new SensorControl(
		                "🌡️", "Temperatura", "°C", 0, 40, 1, TEMP_INICIAL,
		                valor -> processarTemperatura(
		                    area, estado, ar, valor, textoHistorico
		                )
		            ));

	            sensores.add(new SensorControl(
	                "☀️", "Luminosidade", "%", 0, 100, 1, LUM_INICIAL,
	                valor -> processarLuminosidade(
	                    area, estado, luz, valor, textoHistorico
	                )
	            ));

	            atuadores.add(criarPainelAtuador("Ventilador", ventilador));
	            atuadores.add(criarPainelAtuador("Iluminação", luz));
	            atuadores.add(criarPainelAtuador("Ar condicionado", ar));

	            break;

	        case Quarto:

	            sensores.setLayout(new GridLayout(1, 2, 10, 10));
	            atuadores.setLayout(new GridLayout(1, 2, 10, 10));

	            sensores.add(new SensorControl(
		                "🌡️", "Temperatura", "°C", 0, 40, 1, TEMP_INICIAL,
		                valor -> processarTemperatura(
		                    area, estado, ar, valor, textoHistorico
		                )
		            ));

	            sensores.add(new SensorControl(
	                "☀️", "Luminosidade", "%", 0, 100, 1, LUM_INICIAL,
	                valor -> processarLuminosidade(
	                    area, estado, luz, valor, textoHistorico
	                )
	            ));

	            atuadores.add(criarPainelAtuador("Ar condicionado", ar));
	            atuadores.add(criarPainelAtuador("Iluminação", luz));

	            break;

	        case Sala:

	            sensores.setLayout(new GridLayout(1, 2, 10, 10));
	            atuadores.setLayout(new GridLayout(1, 2, 10, 10));

	            sensores.add(new SensorControl(
		                "🌡️", "Temperatura", "°C", 0, 40, 1, TEMP_INICIAL,
		                valor -> processarTemperatura(
		                    area, estado, ar, valor, textoHistorico
		                )
		            ));

	            sensores.add(new SensorControl(
	                "☀️", "Luminosidade", "%", 0, 100, 1, LUM_INICIAL,
	                valor -> processarLuminosidade(
	                    area, estado, luz, valor, textoHistorico
	                )
	            ));

	            atuadores.add(criarPainelAtuador("Ar condicionado", ar));
	            atuadores.add(criarPainelAtuador("Iluminação", luz));

	            break;

	        case Jardim:

	            sensores.setLayout(new GridLayout(1, 2, 10, 10));
	            atuadores.setLayout(new GridLayout(1, 2, 10, 10));

	            sensores.add(new SensorControl(
	                "☀️", "Luminosidade", "%", 0, 100, 1, LUM_INICIAL,
	                valor -> processarLuminosidade(
	                    area, estado, luz, valor, textoHistorico
	                )
	            ));

	            sensores.add(new SensorControl(
	                "🌱", "Humidade do solo", "%", 0, 100, 1, SOLO_INICIAL,
	                valor -> processarSolo(
	                    area, estado, irrigacao, valor, textoHistorico
	                )
	            ));

	            atuadores.add(criarPainelAtuador("Iluminação", luz));
	            atuadores.add(criarPainelAtuador("Irrigação", irrigacao));

	            break;
	    }

	    // Organizar a interface
	    JPanel centro = new JPanel(new GridLayout(2, 1, 10, 10));
	    centro.add(sensores);
	    centro.add(atuadores);

	    JPanel principal = new JPanel(new BorderLayout(10, 10));
	    principal.setBorder(
	        BorderFactory.createEmptyBorder(10, 10, 10, 10)
	    );

	    principal.add(centro, BorderLayout.CENTER);
	    principal.add(new JScrollPane(textoHistorico), BorderLayout.SOUTH);

	    janela.setContentPane(principal);
	    janela.setSize(750, 650);
	    janela.setLocationRelativeTo(null);
	    janela.setVisible(true);

	    // Processar valores iniciais apenas dos sensores existentes
	    switch (area) {

	        case WC:
	        case Cozinha:
	            processarHumidade(area, estado, ventilador, HUM_INICIAL, textoHistorico);
	            processarTemperatura(area, estado, ar, TEMP_INICIAL, textoHistorico);
	            processarLuminosidade(area, estado, luz, LUM_INICIAL, textoHistorico);
	            break;

	        case Quarto:
	        case Sala:
	            processarTemperatura(area, estado, ar, TEMP_INICIAL, textoHistorico);
	            processarLuminosidade(area, estado, luz, LUM_INICIAL, textoHistorico);
	            break;

	        case Jardim:
	            processarLuminosidade(area, estado, luz, LUM_INICIAL, textoHistorico);
	            processarSolo(area, estado, irrigacao, SOLO_INICIAL, textoHistorico);
	            break;
	    }
	}
	
	private static JPanel criarPainelAtuador(
            String nome, JComponent componente) {

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder(nome));
        painel.add(componente, BorderLayout.CENTER);
        return painel;
    }
	
	private static void processarHumidade(
            Areas area, AreaState estado, VentilatorIcon ventilador,
            double valor, JTextArea historicoUI) {

        HumiditySensorMessage evento =
            new HumiditySensorMessage(area, valor);

        boolean ligar = evento.getValue() > 75;

        AreaVentilationOrderMessage ordem =
            new AreaVentilationOrderMessage(area, ligar);

        if (estado.ventilador != ordem.isOn()) {
            estado.ventilador = ordem.isOn();
            ventilador.setLigado(estado.ventilador);

            registarAcao(
                estado.ventilador ? "Ventilador ligado" : "Ventilador desligado",
                area, historicoUI
            );
        }
    }
	
	private static void processarTemperatura(
	        Areas area,
	        AreaState estado,
	        ACIcon ar,
	        double temperatura,
	        JTextArea textoHistorico) {

	    TemperatureSensorMessage evento =
	            new TemperatureSensorMessage(area, temperatura);

	    AirConditioningMode novoModo;

	    if (temperatura < 20) {
	        novoModo = AirConditioningMode.Aquecimento;
	    } else if (temperatura <= 30) {
	        novoModo = AirConditioningMode.Desligado;
	    } else {
	        novoModo = AirConditioningMode.Frio;
	    }

	    if (estado.arCondicionado != novoModo) {
	        estado.arCondicionado = novoModo;
	        ar.setModo(novoModo);

	        registarAcao("Temperatura: " + temperatura + " °C — Ar condicionado: " + novoModo,
	                area, textoHistorico
	        );
	    }
	}
	
	private static void processarLuminosidade(
            Areas area, AreaState estado, LightIcon luz,
            double valor, JTextArea historicoUI) {

        LightSensorMessage evento =
            new LightSensorMessage(area, valor);

        boolean ligar = evento.getValue() < 40;

        AreaLightingOrderMessage ordem =
            new AreaLightingOrderMessage(area, ligar);

        if (estado.luz != ordem.isOn()) {
            estado.luz = ordem.isOn();
            luz.setLigada(estado.luz);
            luz.setIntensidade(estado.luz ? 100 : 0);

            registarAcao(
                estado.luz ? "Iluminação ligada" : "Iluminação desligada",
                area, historicoUI
            );
        }
    }
	
	private static void processarSolo(
            Areas area, AreaState estado, IrrigationIcon irrigacao,
            double valor, JTextArea historicoUI) {

        GroundMoistureSensorMessage evento =
            new GroundMoistureSensorMessage(area, valor);

        boolean ligar = evento.getValue() < 35;

        AreaIrrigationOrderMessage ordem =
            new AreaIrrigationOrderMessage(area, ligar);

        if (estado.irrigacao != ordem.isOn()) {
            estado.irrigacao = ordem.isOn();
            irrigacao.setAtiva(estado.irrigacao);

            registarAcao(
                estado.irrigacao ? "Irrigação ligada" : "Irrigação desligada",
                area, historicoUI
            );
        }
    }
	
	private static void registarAcao(
            String acao, Areas area, JTextArea historicoUI) {

        String linha = acao + " em " + area
            + " - " + LocalTime.now().format(HORA);

        historico.add(linha);
        System.out.println(linha);

        historicoUI.append(linha + "\n");
    }
}
