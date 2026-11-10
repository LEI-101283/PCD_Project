package iscot.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Painel de um atuador: ícone ao centro e histórico de ações por baixo.
 */
public class ActuatorPanel extends JPanel {

	private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("HH:mm:ss");

	private final DefaultListModel<String> historico = new DefaultListModel<>();

	public ActuatorPanel(JComponent icone) {
		super(new BorderLayout(5, 5));

		add(icone, BorderLayout.CENTER);

		JScrollPane scroll = new JScrollPane(new JList<>(historico));
		scroll.setPreferredSize(new Dimension(260, 100));
		scroll.setBorder(BorderFactory.createTitledBorder("Histórico de ações"));
		add(scroll, BorderLayout.SOUTH);
	}

	/** Regista uma ação no histórico, com a hora (HH:MM:SS). Pode ser chamado de qualquer thread. */
	public void registar(String acao) {
		String linha = acao;
		SwingUtilities.invokeLater(() -> historico.addElement(linha));
	}
}
