package iscot.client.gui;

import java.awt.BorderLayout;
import java.util.function.DoubleConsumer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;

public class SensorControl extends JPanel {

	private final JSlider slider;
	private final JLabel valorLabel;
	private final double minimo;
	private final double passo;
	private final String unidade;
	private final DoubleConsumer atualizador;

	public SensorControl(String icone, String nome, String unidade, double minimo, double maximo, double passo,
			double valorInicial, DoubleConsumer atualizador) {

		super(new BorderLayout(5, 5));

		if (passo <= 0 || maximo <= minimo || valorInicial < minimo || valorInicial > maximo || !Double.isFinite(minimo)
				|| !Double.isFinite(maximo) || !Double.isFinite(passo) || !Double.isFinite(valorInicial)) {
			throw new IllegalArgumentException("Intervalo, passo ou valor inicial inválido.");
		}

		this.minimo = minimo;
		this.passo = passo;
		this.unidade = unidade;
		this.atualizador = atualizador;

		JPanel titulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));

		titulo.setOpaque(false);

		JLabel iconeLabel = new JLabel(icone);
		iconeLabel.setForeground(obterCor(icone));

		JLabel nomeLabel = new JLabel(nome);
		nomeLabel.setForeground(Color.BLACK);

		titulo.add(iconeLabel);
		titulo.add(nomeLabel);

		setBorder(BorderFactory.createLineBorder(new Color(180, 200, 220)));

		setLayout(new BorderLayout(5, 5));

		add(titulo, BorderLayout.NORTH);

		int numeroPassos = (int) Math.round((maximo - minimo) / passo);

		if (numeroPassos < 1) {
			throw new IllegalArgumentException("O intervalo tem de conter pelo menos um passo.");
		}

		int passoInicial = (int) Math.round((valorInicial - minimo) / passo);

		slider = new JSlider(0, numeroPassos, passoInicial);
		slider.setMajorTickSpacing(Math.max(1, numeroPassos / 4));
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		
		java.util.Hashtable<Integer, JLabel> rotulos = new java.util.Hashtable<>();
		String formato = passo < 1 ? "%.1f" : "%.0f";
		for (int i = 0; i <= numeroPassos; i += slider.getMajorTickSpacing()) {
		    rotulos.put(i, new JLabel(String.format(formato, minimo + i * passo)));
		}
		rotulos.put(numeroPassos, new JLabel(String.format(formato, maximo))); // marca do máximo
		slider.setLabelTable(rotulos);

		valorLabel = new JLabel("", SwingConstants.CENTER);
		atualizarEtiqueta();

		slider.addChangeListener(e -> {
			atualizarEtiqueta();

			// Comunicar apenas quando o utilizador
			// termina de arrastar o slider.
			if (!slider.getValueIsAdjusting() && this.atualizador != null) {
				this.atualizador.accept(getValor());
			}
		});

		add(slider, BorderLayout.CENTER);
		add(valorLabel, BorderLayout.SOUTH);
	}

	private void atualizarEtiqueta() {
		valorLabel.setText(String.format("%.2f %s", getValor(), unidade));
	}

	public double getValor() {
		return minimo + slider.getValue() * passo;
	}

	public void setValor(double valor) {
		if (!Double.isFinite(valor) || valor < minimo || valor > minimo + slider.getMaximum() * passo) {
			throw new IllegalArgumentException("Valor fora do intervalo permitido.");
		}

		Runnable tarefa = () -> slider.setValue((int) Math.round((valor - minimo) / passo));

		if (SwingUtilities.isEventDispatchThread()) {
			tarefa.run();
		} else {
			SwingUtilities.invokeLater(tarefa);
		}
	}

	private Color obterCor(String icone) {
		switch (icone) {
		case "💧":
			return new Color(30, 144, 255);

		case "🌡️":
			return new Color(220, 70, 50);

		case "☀️":
			return new Color(230, 180, 0);

		case "🌱":
			return new Color(46, 139, 87);

		default:
			return Color.BLACK;
		}
	}
}
