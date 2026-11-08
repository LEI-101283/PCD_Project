package iscot.client.gui;

import java.awt.*;
import javax.swing.*;

public abstract class ComponentState extends JPanel {

    private boolean ativo;

    protected ComponentState() {
        setPreferredSize(new Dimension(100, 100));
        setOpaque(false);
    }

    public final boolean isAtivo() {
        return ativo;
    }

    public final void setAtivo(boolean ativo) {
    	Runnable tarefa = () -> {
            this.ativo = ativo;
            repaint();
        };

        if (SwingUtilities.isEventDispatchThread()) {
            tarefa.run();
        } else {
            SwingUtilities.invokeLater(tarefa);
        }
    }

    protected final Color corEstado() {
        return ativo
            ? new Color(30, 160, 75)
            : new Color(165, 170, 175);
    }

    @Override
    protected abstract void paintComponent(Graphics g);
}