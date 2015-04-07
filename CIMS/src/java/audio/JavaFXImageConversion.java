package audio;

import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JavaFXImageConversion {

    public JavaFXImageConversion() {

    }

    public Image getJavaFXImage(byte[] rawPixels, int width, int height) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage) createBufferedImage(rawPixels, width, height), "png", out);
            out.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        return new javafx.scene.image.Image(in);
    }

    private BufferedImage createBufferedImage(byte[] pixels, int width, int height) {
        SampleModel sm = getIndexSampleModel(width, height);
        DataBuffer db = new DataBufferByte(pixels, width * height, 0);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        IndexColorModel cm = getDefaultColorModel();
        BufferedImage image = new BufferedImage(cm, raster, false, null);
        return image;
    }

    private SampleModel getIndexSampleModel(int width, int height) {
        IndexColorModel icm = getDefaultColorModel();
        WritableRaster wr = icm.createCompatibleWritableRaster(1, 1);
        SampleModel sampleModel = wr.getSampleModel();
        sampleModel = sampleModel.createCompatibleSampleModel(width, height);
        return sampleModel;
    }

    private IndexColorModel getDefaultColorModel() {
        byte[] r = new byte[256];
        byte[] g = new byte[256];
        byte[] b = new byte[256];
        for (int i = 0; i < 256; i++) {
            r[i] = (byte) i;
            g[i] = (byte) i;
            b[i] = (byte) i;
        }
        IndexColorModel defaultColorModel = new IndexColorModel(8, 256, r, g, b);
        return defaultColorModel;
    }

    public void findMinAndMax(short[] pixels, int width, int height) {
        int size = width * height;
        int value;
        int min = 65535;
        int max = 0;
        for (int i = 0; i < size; i++) {
            value = pixels[i] & 0xffff;
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
    }
}
