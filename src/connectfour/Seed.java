package connectfour;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

    /**
     * This enum is used by:
     * 1. Player: takes value of CROSS or NOUGHT
     * 2. Cell content: takes value of CROSS, NOUGHT, or NO_SEED.
     * <p>
     * We also attach a display image icon (text or image) for the items.
     * and define the related variable/constructor/getter.
     * To draw the image:
     * g.drawImage(content.getImage(), x, y, width, height, null);
     * <p>
     * Ideally, we should define two enums with inheritance, which is,
     * however, not supported.
     */
    public enum Seed {   // to save as "Seed.java"
        CROSS("X", "images/Circle-PNG-File.png"),   // displayName, imageFilename
        NOUGHT("O", "images/Red-Circle-Transparent.png"),
        NO_SEED(" ", null);

        // Private variables
        private String displayName;
        private Image img = null;

        // Constructor (must be private)
        private Seed(String name, String imageFilename) {
            this.displayName = name;

            if (imageFilename != null) {
                URL imgURL = getClass().getClassLoader().getResource(imageFilename);
                ImageIcon icon = null;
                if (imgURL != null) {
                    icon = new ImageIcon(imgURL);
                    //System.out.println(icon);  // debugging
                } else {
                    System.err.println("Couldn't find file " + imageFilename);
                }
                img = icon.getImage();
            }
        }

        // Public getters
        public String getDisplayName() {
            return displayName;
        }

        public Image getImage() {
            return img;
        }
    }
