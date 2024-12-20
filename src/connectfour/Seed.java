/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #5
 * 1 - 5026231158 - Kayla Putri Maharani
 * 2 - 5026231170 - Tahiyyah Mufhimah
 * 3 - 5026231206 - Rafael Dimas K
 */

package connectfour;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

    public enum Seed {
        CROSS("X", "images/sun.png"),
        NOUGHT("O", "images/ball.png"),
        NO_SEED(" ", null);

        private String displayName;
        private Image img = null;

        private Seed(String name, String imageFilename) {
            this.displayName = name;

            if (imageFilename != null) {
                URL imgURL = getClass().getClassLoader().getResource(imageFilename);
                ImageIcon icon = null;
                if (imgURL != null) {
                    icon = new ImageIcon(imgURL);
                } else {
                    System.err.println("Couldn't find file " + imageFilename);
                }
                img = icon.getImage();
            }
        }

        public String getDisplayName() {
            return displayName;
        }

        public Image getImage() {
            return img;
        }
    }
