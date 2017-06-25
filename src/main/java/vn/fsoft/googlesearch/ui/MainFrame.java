package vn.fsoft.googlesearch.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import vn.fsoft.googlesearch.service.SearchWorker;
import vn.fsoft.googlesearch.utils.AppConfig;
import vn.fsoft.googlesearch.utils.CommonUtils;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5292554289523876353L;
	private static MainFrame mainFrame;
	private JPanel contentPane;
	private JTextField txtOutputFile;
	private JTextArea txtKeyword;
	private JTextArea txtSite;
	private JProgressBar progressBar;
	private JLabel lblProgress;
	private JLabel lblKeywordList;
	private JLabel lblSiteList;
	private JLabel lblOutputFile;
	private JButton btnBrowserOutput;
	private JButton btnSaveKeyword;
	private JButton btnSaveSite;
	/**
	 * @wbp.nonvisual location=24,469
	 */
	private final JFileChooser fileChooser = new JFileChooser();
	private JLabel lblPreviousSearchFile;
	private JTextField txtPreviousFile;
	private JButton btnBrowserPrev;
	private JLabel lblPercent;
	private JMenu mnMenu;
	private JMenuItem mntmConfig;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainFrame = new MainFrame();
					mainFrame.initialize();
					mainFrame.setVisible(true);
					AppConfig.refreshConfig();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void initialize() {
		// TODO Auto-generated method stub
		lblProgress.setText("");
		lblPercent.setText("");
		progressBar.setVisible(false);
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Google Crawler");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				String keywordList = CommonUtils.readTextFile(AppConfig.KEYWORD_FILE_PATH);
				String siteList = CommonUtils.readTextFile(AppConfig.SITE_FILE_PATH);
				txtKeyword.setText(keywordList);
				txtSite.setText(siteList);
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/icon/fpt_logo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 444);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		mntmConfig = new JMenuItem("Config");
		mntmConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfigDialog dialog = new ConfigDialog(mainFrame);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(mainFrame);
				dialog.setVisible(true);
			}
		});
		mntmConfig.setIcon(null);
		mnMenu.add(mntmConfig);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblKeywordList = new JLabel("Keyword list:");
		lblKeywordList.setBounds(5, 11, 67, 14);
		contentPane.add(lblKeywordList);

		lblSiteList = new JLabel("Site list:");
		lblSiteList.setBounds(321, 11, 46, 14);
		contentPane.add(lblSiteList);

		txtKeyword = new JTextArea();
		JScrollPane scrollKeyword = new JScrollPane(txtKeyword);
		scrollKeyword.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollKeyword.setBounds(5, 36, 288, 192);
		contentPane.add(scrollKeyword);

		txtSite = new JTextArea();
		JScrollPane scrollSite = new JScrollPane(txtSite);
		scrollSite.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollSite.setBounds(321, 36, 283, 192);
		contentPane.add(scrollSite);

		txtOutputFile = new JTextField();
		txtOutputFile.setBounds(5, 305, 522, 20);
		contentPane.add(txtOutputFile);
		txtOutputFile.setColumns(10);

		lblOutputFile = new JLabel("Output file:");
		lblOutputFile.setLabelFor(txtOutputFile);
		lblOutputFile.setBounds(5, 291, 93, 14);
		contentPane.add(lblOutputFile);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SearchWorker worker = new SearchWorker(mainFrame, progressBar, lblProgress, lblPercent);
				List<String> listKeyword = CommonUtils.parseTextareaToList(txtKeyword.getText());
				List<String> listSite = CommonUtils.parseTextareaToList(txtSite.getText());
				worker.setListKeyword(listKeyword);
				worker.setListSite(listSite);
				worker.setOutputPath(txtOutputFile.getText());
				worker.setPreviousPath(txtPreviousFile.getText());
				worker.execute();
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSearch.setBounds(537, 331, 72, 43);
		contentPane.add(btnSearch);

		progressBar = new JProgressBar();
		progressBar.setBounds(5, 347, 486, 14);
		contentPane.add(progressBar);

		lblProgress = new JLabel("Success!");
		lblProgress.setBounds(5, 363, 500, 14);
		contentPane.add(lblProgress);

		btnBrowserOutput = new JButton("Browser");
		btnBrowserOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtOutputFile.getText().equals("")) {
					fileChooser.setSelectedFile(new File(
							AppConfig.OUTPUT_FILE_NAME + CommonUtils.generateFilename() + AppConfig.OUTPUT_EXTENSION));
				}
				int returnVal = fileChooser.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					txtOutputFile.setText(file.getAbsolutePath());
				}
			}
		});
		btnBrowserOutput.setBounds(537, 304, 72, 23);
		contentPane.add(btnBrowserOutput);

		btnSaveKeyword = new JButton("Save");
		btnSaveKeyword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CommonUtils.saveTextFile(txtKeyword.getText(), AppConfig.KEYWORD_FILE_PATH);
				JOptionPane.showMessageDialog(mainFrame, "Content has been saved");
			}
		});
		btnSaveKeyword.setBounds(226, 7, 67, 23);
		contentPane.add(btnSaveKeyword);

		btnSaveSite = new JButton("Save");
		btnSaveSite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CommonUtils.saveTextFile(txtSite.getText(), AppConfig.SITE_FILE_PATH);
				JOptionPane.showMessageDialog(mainFrame, "Content has been saved");
			}
		});
		btnSaveSite.setBounds(537, 7, 67, 23);
		contentPane.add(btnSaveSite);
		
		lblPreviousSearchFile = new JLabel("Previous search file:");
		lblPreviousSearchFile.setBounds(5, 239, 215, 14);
		contentPane.add(lblPreviousSearchFile);
		
		txtPreviousFile = new JTextField();
		lblPreviousSearchFile.setLabelFor(txtPreviousFile);
		txtPreviousFile.setBounds(5, 254, 522, 20);
		contentPane.add(txtPreviousFile);
		txtPreviousFile.setColumns(10);
		
		btnBrowserPrev = new JButton("Browser");
		btnBrowserPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fileChooser.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					txtPreviousFile.setText(file.getAbsolutePath());
				}
			}
		});
		btnBrowserPrev.setBounds(537, 253, 72, 23);
		contentPane.add(btnBrowserPrev);
		
		lblPercent = new JLabel("100%");
		lblPercent.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPercent.setForeground(Color.BLUE);
		lblPercent.setBounds(493, 345, 34, 14);
		contentPane.add(lblPercent);
	}

	public void changeLocale(ResourceBundle rb) {

	}
}
