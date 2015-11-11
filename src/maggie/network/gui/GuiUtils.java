/*
 * Created on 14.02.2005
 *
 */
package maggie.network.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Gui helper class for relative positioning of windows.
 * 
 * @author Claudia Denz, FHNW
 * @author Thomas Hellstern, FHNW
 * @author LIU Xiaofan, PolyU HK
 */
public class GuiUtils {
	public static void centerWindow(Window window) {
		Dimension size = window.getSize();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int newX = (screen.width - size.width) / 2;
		int newY = (screen.height - size.height) / 2;
		window.setLocation(newX, newY);
	}

	public static Image getImageFromFile(File file) {
		Image displayImage = null;
		int scale = 100;
		try {
			displayImage =
					Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath())
							.getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return displayImage;
	}

	// This method returns true if the specified image has transparent pixels
	public static boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {
			BufferedImage bimage = (BufferedImage) image;
			return bimage.getColorModel().hasAlpha();
		}

		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
		}

		// Get the image's color model
		ColorModel cm = pg.getColorModel();
		return cm.hasAlpha();
	}

	public static void maxFrame(Frame frame) {
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	public static void drawVisibleComponentToFile(Component component, File imageFile,
			String mode) {
		// use createimage method
		Dimension dim = component.getSize();
		Image im = component.createImage(dim.width, dim.height);
		component.paint(im.getGraphics());
		// save to file
		try {
			ImageIO.write((RenderedImage) im, mode, imageFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @param c the component to draw.
	 * @param imageFile create a new file
	 * @param mode "jpg", "bmp", "png" ,etc.
	 * @return
	 */
	public static boolean drawComponentToFile(Component c, File imageFile, String mode) {
		// create buffered image
		BufferedImage exportImage =
				new BufferedImage(c.getSize().width, c.getSize().height,
						BufferedImage.TYPE_INT_RGB);
		// paint to image
		c.paint(exportImage.createGraphics());
		// save to file
		try {
			return ImageIO.write(exportImage, mode, imageFile);
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
	}

	// This method returns a buffered image with the contents of an image
	public BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage)
			return (BufferedImage) image;

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent
		// Pixels
		boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage =
					gc.createCompatibleImage(image.getWidth(null), image.getHeight(null),
							transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}
}
