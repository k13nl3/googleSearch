package vn.fsoft.laruetools.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import vn.fsoft.laruetools.service.SearchService;
import vn.fsoft.laruetools.utils.AppConfig;
import vn.fsoft.laruetools.utils.CommonUtils;

public class MainFrame extends JFrame {
	private static MainFrame mainFrame;
	private JPanel contentPane;
	private JTextField txtOutputFile;
	private JTextArea txtareaKeyword;
	private JTextArea txtareaSite;
	private JProgressBar progressBar;
	private JLabel lblProgress;
	private JLabel lblKeywordList;
	private JLabel lblSiteList;
	private JLabel lblOutputFile;
	private JMenu mnMenu;
	private JMenu mnConfiguration;
	private JMenuItem mntmProxy;
	private JButton btnBrowser;
	private JButton btnSaveKeyword;
	private JButton btnSaveSite;
	/**
	 * @wbp.nonvisual location=24,439
	 */
	private final JFileChooser fileChooser = new JFileChooser();

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
					mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				String keywordList = CommonUtils.readTextFile(AppConfig.KEYWORD_FILE_PATH);
				String siteList = CommonUtils.readTextFile(AppConfig.SITE_FILE_PATH);
				txtareaKeyword.setText(keywordList);
				txtareaSite.setText(siteList);
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/fpt_logo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 392);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		mnConfiguration = new JMenu("Configuration");
		menuBar.add(mnConfiguration);

		mntmProxy = new JMenuItem("Proxy");
		mnConfiguration.add(mntmProxy);
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

		txtareaKeyword = new JTextArea();
		txtareaKeyword.setBounds(5, 36, 288, 192);
		contentPane.add(txtareaKeyword);

		txtareaSite = new JTextArea();
		txtareaSite.setBounds(321, 36, 283, 192);
		contentPane.add(txtareaSite);

		txtOutputFile = new JTextField();
		txtOutputFile.setBounds(5, 253, 500, 20);
		contentPane.add(txtOutputFile);
		txtOutputFile.setColumns(10);

		lblOutputFile = new JLabel("Output file:");
		lblOutputFile.setBounds(5, 232, 93, 14);
		contentPane.add(lblOutputFile);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] arrKeyword = txtareaKeyword.getText().split("\\r?\\n");
				List<String> lstKeyword = new ArrayList<String>(Arrays.asList(arrKeyword));
				String[] arrSite = txtareaSite.getText().split("\\r?\\n");
				List<String> lstSite = new ArrayList<String>(Arrays.asList(arrSite));
				SearchService service = new SearchService();
				service.search(lstKeyword, lstSite, txtOutputFile.getText(), progressBar, lblProgress);
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSearch.setBounds(515, 279, 89, 43);
		contentPane.add(btnSearch);

		progressBar = new JProgressBar();
		progressBar.setBounds(5, 294, 500, 14);
		contentPane.add(progressBar);

		lblProgress = new JLabel("Success!");
		lblProgress.setBounds(5, 308, 500, 14);
		contentPane.add(lblProgress);

		btnBrowser = new JButton("Browser");
		btnBrowser.addActionListener(new ActionListener() {
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
		btnBrowser.setBounds(515, 252, 89, 23);
		contentPane.add(btnBrowser);

		btnSaveKeyword = new JButton("Save");
		btnSaveKeyword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CommonUtils.saveTextFile(txtareaKeyword.getText(), AppConfig.KEYWORD_FILE_PATH);
				JOptionPane.showMessageDialog(mainFrame, "Content has been saved");
			}
		});
		btnSaveKeyword.setBounds(226, 7, 67, 23);
		contentPane.add(btnSaveKeyword);

		btnSaveSite = new JButton("Save");
		btnSaveSite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CommonUtils.saveTextFile(txtareaSite.getText(), AppConfig.SITE_FILE_PATH);
				JOptionPane.showMessageDialog(mainFrame, "Content has been saved");
			}
		});
		btnSaveSite.setBounds(537, 7, 67, 23);
		contentPane.add(btnSaveSite);
	}

	public void changeLocale(ResourceBundle rb) {

	}
}
