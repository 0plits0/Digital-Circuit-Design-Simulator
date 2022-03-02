import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MainWindow extends JFrame implements ActionListener {
	JPanel UpperPanel;
	JPanel ToolPanel;
	JMenuBar MainMenu;
	JMenu curMenu;
	JMenuItem MenuItem;
	JCheckBoxMenuItem Showg;
	JComboBox<String> Element;
	static Canvas Grid;
	And gate1;
	
	

	public MainWindow() throws HeadlessException {
		super("Digital Design Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UpperPanel = new JPanel(new BorderLayout());
		BuildMenu();
		ToolPanel = new JPanel();
		ToolPanel.add(new JLabel("Element:"));
		Element = new JComboBox<String>(new String[]{"AND","OR","NOT","ONE/ZERO","LED","CLOCK","TFLIPFLOP"});
		ToolPanel.add(Element);
		Grid = new Canvas(this);
		UpperPanel.add(MainMenu,BorderLayout.NORTH);
		UpperPanel.add(ToolPanel,BorderLayout.SOUTH);
		getContentPane().add(UpperPanel,BorderLayout.NORTH);
		getContentPane().add(Grid,BorderLayout.SOUTH);
		
		this.setSize(500, 500);
		this.setVisible(true);
	}
	
	protected void BuildMenu(){
		MainMenu = new JMenuBar();
		curMenu = new JMenu("File");
		curMenu.add(NewMenuItem("Open"));
		curMenu.add(NewMenuItem("Save"));
		curMenu.add(NewMenuItem("Clear"));
		curMenu.add(NewMenuItem("Exit"));
		MainMenu.add(curMenu);
		
		curMenu = new JMenu("Editor");
		JMenu subMenu = new JMenu("Grid");
		subMenu.add(NewCheckBoxMenuItem("Use Grid",true));
		Showg = NewCheckBoxMenuItem("Show Grid",true);
		subMenu.add(Showg);
		curMenu.add(subMenu);
		
		MainMenu.add(curMenu);
		curMenu = new JMenu("Info");
		curMenu.add(NewMenuItem("About"));
		curMenu.add(NewMenuItem("Exit"));
		MainMenu.add(curMenu);
	}
	
	protected JMenuItem NewMenuItem(String name){
		JMenuItem x = new JMenuItem(name);
		x.addActionListener(this);
		return x;
	}
	
	protected JCheckBoxMenuItem NewCheckBoxMenuItem(String name,boolean checked){
		JCheckBoxMenuItem x = new JCheckBoxMenuItem(name);
		if (checked) x.doClick();
		x.addActionListener(this);
		return x;
	}
	
	protected void About(){
		String str = "Digital Design Simulator\n";
		str=str+"This program is a personal project, ";
		str=str+"made only for educational purposes.\n";
		str=str+"Email: plits_robotics@yahoo.gr | ";
		str=str+"Feel free to report any bugs.\n";
		str=str+"Anastasios Pikridas 2015\n";
		JOptionPane.showMessageDialog(this, str,"About",1);
	}
	
	protected void Save(){
		JFileChooser fc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Digital Design Simulator File *.ddf", new String[] {"ddf"});
		fc.setFileFilter(filter);
		if (fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
			File OutFile;
			OutFile = fc.getSelectedFile();
			if(!OutFile.toString().endsWith(".ddf"))
				OutFile = new File(OutFile.toString() + ".ddf");  // append .xml if "foo.jpg.xml" is OK

			
			//if (fc.getSelectedFile().exists()) OutFile = fc.getSelectedFile();
				FileOutputStream out;
				try {
					out = new FileOutputStream(OutFile);
					ObjectOutputStream ObOut = new ObjectOutputStream(out);
					ObOut.writeObject(Grid.Comp);
					ObOut.writeObject(Grid.Dots);
					ObOut.writeObject(Grid.Lines);
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	protected void Open(){
		JFileChooser fc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Digital Design Simulator File *.ddf", new String[] {"ddf"});
		fc.setFileFilter(filter);
		if (fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
			File InFile;
			InFile = fc.getSelectedFile();
				FileInputStream in;
				try {
					in = new FileInputStream(InFile);
					ObjectInputStream ObIn = new ObjectInputStream(in);
					try {
						Grid.Comp = (List<Component>) ObIn.readObject();
						Grid.Dots = (List<Dot>) ObIn.readObject();
						Grid.Lines = (List<List<Dot>>) ObIn.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					ObIn.close();
					in.close();
					Grid.AfterOpen();
					Grid.repaint();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new MainWindow();
		Grid.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()){
			case "Exit":
				System.exit(0);
				break;
			case "About":
				About();
				break;
			case "Save":
				Save();
				break;
			case "Open":
				Open();
				break;
			case "Clear":
				ClearG();
				break;
			case "Use Grid":
				Grid.UseGrid = !Grid.UseGrid;
				if (!Grid.UseGrid) Showg.setEnabled(false);
				else Showg.setEnabled(true);
				Grid.repaint();
				break;
			case "Show Grid":
				Grid.ShowGrid = !Grid.ShowGrid;
				Grid.repaint();
				break;
		}
	}

	private void ClearG() {
		Grid.Comp.clear();
		Grid.Dots.clear();
		Grid.Lines.clear();
		Grid.repaint();
	}

}
