package iscot.client;

import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import iscot.enums.Areas;

/**
 * Código comum aos clientes (sensores e atuadores) nas fases 1 a 3.
 */
public final class ClientWindow {

	private ClientWindow() {
	}

	/**
	 * Lê a área a partir dos argumentos (IP PORT Area).
	 * @return a área, ou null (com mensagem de erro) se os argumentos forem inválidos
	 */
	public static Areas parseArea(String[] args) {
		if (args.length != 3) {
			System.err.println("Invalid arguments! Uso: <IP> <PORT> <Area>");
			System.err.println("Áreas possíveis: " + Arrays.toString(Areas.values()));
			return null;
		}
		try {
			return Areas.valueOf(args[2]);
		} catch (IllegalArgumentException e) {
			System.err.println("Área inválida: " + args[2]);
			System.err.println("Áreas possíveis: " + Arrays.toString(Areas.values()));
			return null;
		}
	}

	/** Abre uma janela com o componente dado (na thread da interface gráfica). */
	public static void show(String titulo, JComponent conteudo) {
		SwingUtilities.invokeLater(() -> {
			JFrame janela = new JFrame(titulo);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			JPanel painel = new JPanel(new BorderLayout());
			painel.add(conteudo, BorderLayout.CENTER);
			janela.setContentPane(painel);

			janela.pack();
			janela.setMinimumSize(janela.getSize());
			janela.setLocation(600, 400);
			janela.setVisible(true);
		});
	}
}
