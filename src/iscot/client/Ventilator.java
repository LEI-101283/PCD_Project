package iscot.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import iscot.client.gui.*;
import iscot.enums.*;
import iscot.events.*;

public class Ventilator {

    private static final DateTimeFormatter HORA =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final double HUM_INICIAL = 50;
    private static final double TEMP_INICIAL = 24;
    private static final double TEMP_WC_INICIAL = 26;
    private static final double LUM_INICIAL = 70;
    private static final double SOLO_INICIAL = 50;

    private static class AreaState {
        boolean ventilador;
        boolean luz;
        boolean irrigacao;

        AirConditioningMode arCondicionado =
                AirConditioningMode.Desligado;
    }

    public static void main(String[] args) {

        if (args.length != 3) {
            System.err.println("Invalid arguments!");
            return;
        }

        // TODO connect to server
        // TODO register with the server
        // TODO receive and handle messages from server

        try {
            Areas area = Areas.valueOf(args[2]);

            SwingUtilities.invokeLater(
                    () -> criarJanela(area)
            );

        } catch (IllegalArgumentException e) {
            System.err.println("Área inválida: " + args[2]);
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
        sensores.setBorder(
                BorderFactory.createTitledBorder("Sensores")
        );

        JPanel atuadores = new JPanel();
        atuadores.setBorder(
                BorderFactory.createTitledBorder("Atuadores")
        );

        // Histórico
        JTextArea textoHistorico = new JTextArea(7, 35);
        textoHistorico.setEditable(false);
        textoHistorico.setLineWrap(true);
        textoHistorico.setWrapStyleWord(true);

        // Sensores e atuadores de cada divisão
        switch (area) {

            case WC:

                sensores.setLayout(new GridLayout(2, 2, 10, 10));
                atuadores.setLayout(new GridLayout(1, 2, 10, 10));

                sensores.add(new SensorControl(
                        "💧", "Humidade", "%", 0, 100, 1, HUM_INICIAL,
                        valor -> processarHumidade(
                                area, estado, ventilador,
                                valor, textoHistorico
                        )
                ));

                sensores.add(new SensorControl(
                        "🌡️", "Temperatura", "°C", 0, 40, 1,
                        TEMP_WC_INICIAL,
                        valor -> processarTemperatura(
                                area, estado, ar,
                                valor, textoHistorico
                        )
                ));

                sensores.add(new SensorControl(
                        "☀️", "Luminosidade", "%", 0, 100, 1,
                        LUM_INICIAL,
                        valor -> processarLuminosidade(
                                area, estado, luz,
                                valor, textoHistorico
                        )
                ));

                atuadores.add(
                        criarPainelAtuador("Ventilador", ventilador)
                );

                atuadores.add(
                        criarPainelAtuador("Iluminação", luz)
                );

                break;

            case Cozinha:

                sensores.setLayout(new GridLayout(2, 2, 10, 10));
                atuadores.setLayout(new GridLayout(2, 2, 10, 10));

                sensores.add(new SensorControl(
                        "💧", "Humidade", "%", 0, 100, 1, HUM_INICIAL,
                        valor -> processarHumidade(
                                area, estado, ventilador,
                                valor, textoHistorico
                        )
                ));

                sensores.add(new SensorControl(
                        "🌡️", "Temperatura", "°C", 0, 40, 1,
                        TEMP_INICIAL,
                        valor -> processarTemperatura(
                                area, estado, ar,
                                valor, textoHistorico
                        )
                ));

                sensores.add(new SensorControl(
                        "☀️", "Luminosidade", "%", 0, 100, 1,
                        LUM_INICIAL,
                        valor -> processarLuminosidade(
                                area, estado, luz,
                                valor, textoHistorico
                        )
                ));

                atuadores.add(
                        criarPainelAtuador("Ventilador", ventilador)
                );

                atuadores.add(
                        criarPainelAtuador("Iluminação", luz)
                );

                atuadores.add(
                        criarPainelAtuador("Ar condicionado", ar)
                );

                break;

            case Quarto:

                sensores.setLayout(new GridLayout(1, 2, 10, 10));
                atuadores.setLayout(new GridLayout(1, 2, 10, 10));

                sensores.add(new SensorControl(
                        "🌡️", "Temperatura", "°C", 0, 40, 1,
                        TEMP_INICIAL,
                        valor -> processarTemperatura(
                                area, estado, ar,
                                valor, textoHistorico
                        )
                ));

                sensores.add(new SensorControl(
                        "☀️", "Luminosidade", "%", 0, 100, 1,
                        LUM_INICIAL,
                        valor -> processarLuminosidade(
                                area, estado, luz,
                                valor, textoHistorico
                        )
                ));

                atuadores.add(
                        criarPainelAtuador("Ar condicionado", ar)
                );

                atuadores.add(
                        criarPainelAtuador("Iluminação", luz)
                );

                break;

            case Sala:

                sensores.setLayout(new GridLayout(1, 2, 10, 10));
                atuadores.setLayout(new GridLayout(1, 2, 10, 10));

                sensores.add(new SensorControl(
                        "🌡️", "Temperatura", "°C", 0, 40, 1,
                        TEMP_INICIAL,
                        valor -> processarTemperatura(
                                area, estado, ar,
                                valor, textoHistorico
                        )
                ));

                sensores.add(new SensorControl(
                        "☀️", "Luminosidade", "%", 0, 100, 1,
                        LUM_INICIAL,
                        valor -> processarLuminosidade(
                                area, estado, luz,
                                valor, textoHistorico
                        )
                ));

                atuadores.add(
                        criarPainelAtuador("Ar condicionado", ar)
                );

                atuadores.add(
                        criarPainelAtuador("Iluminação", luz)
                );

                break;

            case Jardim:

                sensores.setLayout(new GridLayout(1, 2, 10, 10));
                atuadores.setLayout(new GridLayout(1, 2, 10, 10));

                sensores.add(new SensorControl(
                        "☀️", "Luminosidade", "%", 0, 100, 1,
                        LUM_INICIAL,
                        valor -> processarLuminosidade(
                                area, estado, luz,
                                valor, textoHistorico
                        )
                ));

                sensores.add(new SensorControl(
                        "🌱", "Humidade do solo", "%", 0, 100, 1,
                        SOLO_INICIAL,
                        valor -> processarSolo(
                                area, estado, irrigacao,
                                valor, textoHistorico
                        )
                ));

                atuadores.add(
                        criarPainelAtuador("Iluminação", luz)
                );

                atuadores.add(
                        criarPainelAtuador("Irrigação", irrigacao)
                );

                break;
        }

        // Organizar a interface
        JPanel centro = new JPanel(
                new GridLayout(2, 1, 10, 10)
        );

        centro.add(sensores);
        centro.add(atuadores);

        JPanel principal = new JPanel(
                new BorderLayout(10, 10)
        );

        principal.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        principal.add(centro, BorderLayout.CENTER);
        principal.add(
                new JScrollPane(textoHistorico),
                BorderLayout.SOUTH
        );

        janela.setContentPane(principal);
        janela.setSize(750, 650);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);

        // Processar os valores iniciais dos sensores existentes
        switch (area) {

            case WC:

                processarHumidade(
                        area, estado, ventilador,
                        HUM_INICIAL, textoHistorico
                );

                processarTemperatura(
                        area, estado, ar,
                        TEMP_WC_INICIAL, textoHistorico
                );

                processarLuminosidade(
                        area, estado, luz,
                        LUM_INICIAL, textoHistorico
                );

                break;

            case Cozinha:

                processarHumidade(
                        area, estado, ventilador,
                        HUM_INICIAL, textoHistorico
                );

                processarTemperatura(
                        area, estado, ar,
                        TEMP_INICIAL, textoHistorico
                );

                processarLuminosidade(
                        area, estado, luz,
                        LUM_INICIAL, textoHistorico
                );

                break;

            case Quarto:
            case Sala:

                processarTemperatura(
                        area, estado, ar,
                        TEMP_INICIAL, textoHistorico
                );

                processarLuminosidade(
                        area, estado, luz,
                        LUM_INICIAL, textoHistorico
                );

                break;

            case Jardim:

                processarLuminosidade(
                        area, estado, luz,
                        LUM_INICIAL, textoHistorico
                );

                processarSolo(
                        area, estado, irrigacao,
                        SOLO_INICIAL, textoHistorico
                );

                break;
        }
    }

    private static JPanel criarPainelAtuador(
            String nome, JComponent componente) {

        JPanel painel = new JPanel(new BorderLayout());

        painel.setBorder(
                BorderFactory.createTitledBorder(nome)
        );

        painel.add(componente, BorderLayout.CENTER);

        return painel;
    }

    // -----------------------------------------
    // PROCESSAMENTO DA HUMIDADE
    // -----------------------------------------

    private static void processarHumidade(
            Areas area,
            AreaState estado,
            VentilatorIcon ventilador,
            double valor,
            JTextArea historicoUI) {

        HumiditySensorEvent evento =
                new HumiditySensorEvent(area, valor);

        apresentarEvento(evento, valor);

        boolean ligar = evento.getValue() > 75;

        AreaVentilationOrderEvent ordem =
                new AreaVentilationOrderEvent(area, ligar);

        if (estado.ventilador != ordem.isOn()) {

            estado.ventilador = ordem.isOn();

            ventilador.setLigado(estado.ventilador);

            registarAcao(
                    estado.ventilador
                            ? "Ventilador ligado"
                            : "Ventilador desligado",
                    area,
                    historicoUI
            );
        }
    }

    // -----------------------------------------
    // PROCESSAMENTO DA TEMPERATURA
    // -----------------------------------------

    private static void processarTemperatura(
            Areas area,
            AreaState estado,
            ACIcon ar,
            double temperatura,
            JTextArea historicoUI) {

        TemperatureSensorEvent evento =
                new TemperatureSensorEvent(area, temperatura);

        apresentarEvento(evento, temperatura);

        AirConditioningMode novoModo;

        if (temperatura < 20) {

            novoModo = AirConditioningMode.Aquecimento;

        } else if (temperatura <= 25) {

            novoModo = AirConditioningMode.Desligado;

        } else {

            novoModo = AirConditioningMode.Frio;
        }

        AreaTemperatureOrderEvent ordem =
                new AreaTemperatureOrderEvent(area, novoModo);

        if (estado.arCondicionado != ordem.getMode()) {

            estado.arCondicionado = ordem.getMode();

            ar.setModo(ordem.getMode());

            registarAcao(
                    "Temperatura: " + temperatura
                            + " °C — Ar condicionado: "
                            + ordem.getMode(),
                    area,
                    historicoUI
            );
        }
    }

    // -----------------------------------------
    // PROCESSAMENTO DA LUMINOSIDADE
    // -----------------------------------------

    private static void processarLuminosidade(
            Areas area,
            AreaState estado,
            LightIcon luz,
            double valor,
            JTextArea historicoUI) {

        LightSensorEvent evento =
                new LightSensorEvent(area, valor);

        apresentarEvento(evento, valor);

        boolean ligar = evento.getValue() < 40;

        AreaLightingOrderEvent ordem =
                new AreaLightingOrderEvent(area, ligar);

        if (estado.luz != ordem.isOn()) {

            estado.luz = ordem.isOn();

            luz.setLigada(estado.luz);
            luz.setIntensidade(estado.luz ? 100 : 0);

            registarAcao(
                    estado.luz
                            ? "Iluminação ligada"
                            : "Iluminação desligada",
                    area,
                    historicoUI
            );
        }
    }

    // -----------------------------------------
    // PROCESSAMENTO DA HUMIDADE DO SOLO
    // -----------------------------------------

    private static void processarSolo(
            Areas area,
            AreaState estado,
            IrrigationIcon irrigacao,
            double valor,
            JTextArea historicoUI) {

        GroundMoistureSensorEvent evento =
                new GroundMoistureSensorEvent(area, valor);

        apresentarEvento(evento, valor);

        boolean ligar = evento.getValue() < 35;

        AreaIrrigationOrderEvent ordem =
                new AreaIrrigationOrderEvent(area, ligar);

        if (estado.irrigacao != ordem.isOn()) {

            estado.irrigacao = ordem.isOn();

            irrigacao.setAtiva(estado.irrigacao);

            registarAcao(
                    estado.irrigacao
                            ? "Irrigação ligada"
                            : "Irrigação desligada",
                    area,
                    historicoUI
            );
        }
    }

    // -----------------------------------------
    // REGISTAR ALTERAÇÕES DOS ATUADORES
    // -----------------------------------------

    private static void registarAcao(
            String acao,
            Areas area,
            JTextArea historicoUI) {

        String linha = acao
                + " em " + area
                + " - " + LocalTime.now().format(HORA);

        System.out.println(linha);

        historicoUI.append(linha + "\n");
    }

    // -----------------------------------------
    // APRESENTAR EVENTOS DOS SENSORES
    // -----------------------------------------

    private static void apresentarEvento(Event evento, double valor) {
        String unidade = "";

        if (evento instanceof LightSensorEvent ||
            evento instanceof GroundMoistureSensorEvent ||
            evento instanceof HumiditySensorEvent) {
            unidade = "%";
        } else if (evento instanceof TemperatureSensorEvent) {
            unidade = " °C";
        }

        LocalDateTime dataHora = evento.getTimestamp();

        System.out.println("\n========== EVENTO DO SENSOR ==========");
        System.out.println("Sensor: " + evento.getClass().getSimpleName());
        System.out.println("Área: " + evento.getArea());
        System.out.printf("Valor: %.1f%s%n", valor, unidade);
        System.out.println("Data: " + dataHora.toLocalDate());
        System.out.println("Hora: " + dataHora.toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("======================================");
    }
}