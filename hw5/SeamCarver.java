import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private Picture picture;
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        int xPlusOne = x + 1;
        int xMinusOne = x - 1;
        int yPlusOne = y + 1;
        int yMinusOne = y - 1;
        if (xPlusOne == width()) {
            xPlusOne = 0;
        }
        if (xMinusOne == -1) {
            xMinusOne = width() - 1;
        }
        if (yPlusOne == height()) {
            yPlusOne = 0;
        }
        if (yMinusOne == -1) {
            yMinusOne = height() - 1;
        }

        Color leftPixel = picture.get(xMinusOne, y);
        Color rightPixel = picture.get(xPlusOne, y);
        Color upperPixel = picture.get(x, yPlusOne);
        Color lowerPixel = picture.get(x, yMinusOne);
        double soxg = sog(leftPixel, rightPixel);
        double soyg = sog(upperPixel, lowerPixel);
        return soxg + soyg;
    }

    //compute square of x/y gradient.
    private double sog(Color color1, Color color2) {
        double red = color1.getRed() - color2.getRed();
        double blue = color1.getBlue() - color2.getBlue();
        double green = color1.getGreen() - color2.getGreen();
        return square(red) + square(blue) + square(green);
    }

    private double square(double num) {
        return Math.pow(num, 2);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] rowPath = findVerticalSeam();
        transpose();
        return rowPath;
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = SCUtility.toEnergyMatrix(this);
        int[] seam = new int[height()];
        //conner case for number of rows equal to one.
        if  (energy[0].length == 1) {
            double smallest = Double.MAX_VALUE;
            int m = 0;
            for (int col = 0; col < energy.length; col++) {
                if (energy[col][0] < smallest) {
                    smallest = energy[col][0];
                    m = col;
                }
            }
            seam[0] = m;
            return seam;
        }
        //conner case for number of number elements in each row equals to one.
        // return seam immediately (i.e: each column is 0, default value when creating the instance)
        if (energy.length == 1) {
            return seam;
        }
        for (int j = 1; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                if (i == 0) {
                    energy[i][j] = Math.min(energy[i][j - 1], energy[i + 1][j - 1]) + energy[i][j];
                } else if (i == width() - 1) {
                    energy[i][j] = Math.min(energy[i - 1][j - 1], energy[i][j - 1]) + energy[i][j];
                } else {
                    energy[i][j] = Math.min(Math.min(energy[i - 1][j - 1], energy[i][j - 1]), energy[i + 1][j - 1]) + energy[i][j];
                }
            }
        }
        //go through the each grid in last row, find the column has minimum cost path
        int lastRow = height() - 1;
        double smallest = energy[0][lastRow];
        int col = 0;
        for (int i = 1; i < width(); i++) {
            if (energy[i][lastRow] < smallest) {
                smallest = energy[i][lastRow];
                col = i;
            }
        }
        //This seems to be easiest way to find the column path. is there a better approach?
        // in seam array, each index position represents a paricular row, each value in that particular row represent a column.
        seam[height() - 1] = col;
        for (int i = height() - 2; i >= 0; i--) {
            if (col == 0) {
                if (energy[0][i] > energy[1][i]) {
                    seam[i] = 1;
                    //update col for next computation.
                    col = 1;
                } else {
                    seam[i] = 0;
                    col = 0;
                }
            } else if (col == width() - 1) {
                if (energy[width() - 2][i] > energy[width() - 1][i]) {
                    seam[i] = width() - 1;
                    col = width() - 1;
                } else {
                    seam[i] = width() - 2;
                    col = width() - 2;
                }
            } else {
                double tempMin = energy[col - 1][i];
                int tempColumn = col - 1;
                for (int j = col; j <= col + 1; j++) {
                    if (energy[j][i] < tempMin) {
                        tempMin = energy[j][i];
                        tempColumn = j;
                    }
                }
                seam[i] = tempColumn;
                col = tempColumn;
            }
        }
        return seam;
    }

    private void transpose() {
        Picture tempPicture = new Picture(height(), width());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                tempPicture.set(i, j, picture.get(j, i));
            }
        }
        this.picture = tempPicture;
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        Picture tempPicture = new Picture(width() - 1, height());
        int k = 0;
        for (int i = 0; i < height(); i++) {
            int remove = seam[i];
            for (int j = 0; j < remove; j++) {
                tempPicture.set(j, i, picture.get(j, i));
            }
            for (int j = remove + 1; j < width(); j++) {
                tempPicture.set(j - 1, i, picture.get(j, i));
            }
            k++;
        }
        picture = new Picture(tempPicture);
    }
}
