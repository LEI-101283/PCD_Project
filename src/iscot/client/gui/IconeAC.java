package iscot.client.gui;

import java.awt.*;
import java.awt.geom.*;

public class IconeAC extends EstadoComponente {
	private String modo = "Automático";
	private double temperatura = 22.0;

	public void setLigado(boolean ligado) {
	    setAtivo(ligado);
	}

	public boolean isLigado() {
	    return isAtivo();
	}

	public void setModo(String modo) {
	    this.modo = modo;
	}

	public String getModo() {
	    return modo;
	}

	public void setTemperatura(double temperatura) {
	    if (temperatura < 16 || temperatura > 30) {
	        throw new IllegalArgumentException(
	            "A temperatura deve estar entre 16 e 30 °C."
	        );
	    }

	    this.temperatura = temperatura;
	}

	public double getTemperatura() {
	    return temperatura;
	}
	
    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        if (!isAtivo()) {
            g2.setColor(corEstado());
            g2.fillOval(cx - 22, cy - 22, 44, 44);
        } else {
            g2.setColor(new Color(0, 135, 220));
            g2.setStroke(new BasicStroke(
                4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND
            ));

            for (int angulo = 0; angulo < 180; angulo += 60) {
                double a = Math.toRadians(angulo);

                int dx = (int) (28 * Math.cos(a));
                int dy = (int) (28 * Math.sin(a));

                g2.drawLine(
                    cx - dx, cy - dy,
                    cx + dx, cy + dy
                );
            }
        }

        g2.dispose();
    }
}