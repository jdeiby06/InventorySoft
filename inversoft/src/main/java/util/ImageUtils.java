package util;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    /**
     * Cargar una imagen desde recursos y escalarla al tamaño especificado.
     * @param resourceName nombre del archivo en resources (e.g., "logo.png")
     * @param width ancho deseado
     * @param height alto deseado
     * @return ImageIcon escalada, o null si no se encuentra
     */
    public static ImageIcon loadAndScaleImage(String resourceName, int width, int height) {
        try {
            InputStream is = ImageUtils.class.getClassLoader().getResourceAsStream(resourceName);
            if (is == null) {
                System.err.println("Recurso no encontrado: " + resourceName);
                return null;
            }
            byte[] imageData = is.readAllBytes();
            is.close();

            ImageIcon icon = new ImageIcon(imageData);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (IOException e) {
            System.err.println("Error cargando imagen: " + e.getMessage());
            return null;
        }
    }

    /**
     * Cargar imagen desde recursos sin escalar (tamaño original).
     * @param resourceName nombre del archivo en resources
     * @return ImageIcon, o null si no se encuentra
     */
    public static ImageIcon loadImage(String resourceName) {
        try {
            InputStream is = ImageUtils.class.getClassLoader().getResourceAsStream(resourceName);
            if (is == null) {
                System.err.println("Recurso no encontrado: " + resourceName);
                return null;
            }
            byte[] imageData = is.readAllBytes();
            is.close();
            return new ImageIcon(imageData);
        } catch (IOException e) {
            System.err.println("Error cargando imagen: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crear un panel con el logo centrado y un fondo de color.
     * @param logoResource nombre del recurso logo
     * @param backgroundColor color de fondo
     * @param logoWidth ancho del logo
     * @param logoHeight alto del logo
     * @return JPanel con el logo
     */
    public static JPanel createLogoPanelCentered(String logoResource, Color backgroundColor, int logoWidth, int logoHeight) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(backgroundColor);

        ImageIcon logo = loadAndScaleImage(logoResource, logoWidth, logoHeight);
        if (logo != null) {
            JLabel lblLogo = new JLabel(logo);
            lblLogo.setHorizontalAlignment(JLabel.CENTER);
            lblLogo.setVerticalAlignment(JLabel.CENTER);
            panel.add(lblLogo, BorderLayout.CENTER);
        }

        return panel;
    }

    /**
     * Crear un panel con imagen de fondo escalada que cubra todo el panel.
     * @param backgroundResource nombre del recurso de imagen
     * @return JPanel con fondo de imagen
     */
    public static JPanel createBackgroundPanel(String backgroundResource) {
        return new JPanel() {
            private ImageIcon bgImage = ImageUtils.loadImage(backgroundResource);

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    Image scaledImg = bgImage.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                    g.drawImage(scaledImg, 0, 0, this);
                }
            }
        };
    }
}
