/**
 * 
 */
package maggie.network.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.SystemColor;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * @author LIU Xiaofan
 * 
 */
public class AboutDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7562983875060927413L;

	private JPanel aboutContentPane = null;

	private JTextArea aboutTextArea = null;
	private String author;
	private String url;
	private String version;
	private String project;

	public AboutDialog(Frame owner) {
		super(owner);
		try {
			Properties ps = new Properties();
			ps.load(new FileInputStream("./src/etc/project.properties"));
			this.project = ps.getProperty("project");
			this.version = ps.getProperty("version");
			this.author = ps.getProperty("author");
			this.url = ps.getProperty("URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
		initialize();
	}

	/**
	 * This method initializes aboutContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAboutContentPane() {
		if (aboutContentPane == null) {
			aboutContentPane = new JPanel();
			aboutContentPane.setLayout(new BorderLayout());
			aboutContentPane.add(getAboutTextArea(), BorderLayout.CENTER);
		}
		return aboutContentPane;
	}

	/**
	 * This method initializes aboutTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getAboutTextArea() {
		if (aboutTextArea == null) {
			aboutTextArea = new JTextArea();
			aboutTextArea.append("Project " + this.project + "\n" + this.version);
			aboutTextArea.append("\n"
					+ new SimpleDateFormat("yyyy-MM-dd").format(new Date(System
							.currentTimeMillis())));
			aboutTextArea.append("\nAuthor: " + this.author);
			aboutTextArea.append("\nURL: " + this.url);
			aboutTextArea.setEditable(false);
			aboutTextArea.setBackground(SystemColor.control);
		}
		return aboutTextArea;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setTitle("About");
		this.setContentPane(getAboutContentPane());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setResizable(false);
		this.setFocusable(true);
		this.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {
				if (e.getKeyCode() == 0) {
					dispose();
				}
			}
		});
		this.pack();
		GuiUtils.centerWindow(this);
	}
}
