package vn.fsoft.googlesearch.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import vn.fsoft.googlesearch.utils.AppConfig;

public class ConfigDialog extends JDialog {

	private final JFrame parentFrame;
	private final JPanel contentPanel = new JPanel();
	private JCheckBox cbxProxyActive;
	private JLabel lblHost;
	private JTextField txtProxyHost;
	private JLabel lblProxyPort;
	private JCheckBox cbxProxyAuthen;
	private JLabel lblUsername;
	private JTextField txtProxyUser;
	private JLabel lblPassword;
	private JPasswordField txtProxyPass;
	private JLabel lblRequestDelay;
	private JLabel lblS;
	private JLabel lblMaxSearchResult;
	private JLabel lblUseragent;
	private JLabel lblSearchUrl;
	private JTextField txtSearchUrl;
	private JTextArea txtUserAgent;
	private JSpinner txtDelayTime;
	private JSpinner txtMaxResult;
	private JLabel label;
	private JSpinner txtProxyPort;


	/**
	 * Create the dialog.
	 */
	public ConfigDialog(JFrame parentFrame) {
		this.parentFrame = parentFrame;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				if ("true".equals(AppConfig.config.get(AppConfig.USE_PROXY_PROP))) {
					cbxProxyActive.setSelected(true);
					txtProxyHost.setEnabled(true);
					txtProxyHost.setText(AppConfig.config.getProperty(AppConfig.PROXY_HOST_PROP));
					txtProxyPort.setEnabled(true);
					txtProxyPort.setValue(Integer.valueOf(AppConfig.config.getProperty(AppConfig.PROXY_PORT_PROP)));
					if (AppConfig.config.get(AppConfig.REQUIRE_AUTHEN_PROP).equals("true")) {
						cbxProxyAuthen.setSelected(true);
						txtProxyUser.setEnabled(true);
						txtProxyUser.setText(AppConfig.config.getProperty(AppConfig.PROXY_USER_PROP));
						txtProxyPass.setEnabled(true);
						txtProxyPass.setText(AppConfig.config.getProperty(AppConfig.PROXY_PASS_PROP));
					}
				}
				txtDelayTime.setValue(Integer.valueOf(AppConfig.config.getProperty(AppConfig.DELAY_TIME_PROP)));
				txtMaxResult.setValue(Integer.valueOf(AppConfig.config.getProperty(AppConfig.MAX_RESULT_PROP)));
				txtSearchUrl.setText(AppConfig.config.getProperty(AppConfig.SEARCH_URL_PROP));
				txtUserAgent.setText(AppConfig.config.getProperty(AppConfig.USER_AGENT_PROP));
			}
		});
		setResizable(false);
		setTitle("Config");
		setBounds(100, 100, 450, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		cbxProxyActive = new JCheckBox("Using proxy");
		cbxProxyActive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!cbxProxyActive.isSelected()) {
					txtProxyHost.setEnabled(false);
					txtProxyPort.setEnabled(false);
					txtProxyUser.setEnabled(false);
					txtProxyPass.setEnabled(false);
					cbxProxyAuthen.setEnabled(false);
				} else if (!cbxProxyAuthen.isSelected()) {
					txtProxyHost.setEnabled(true);
					txtProxyPort.setEnabled(true);
					txtProxyUser.setEnabled(false);
					txtProxyPass.setEnabled(false);
					cbxProxyAuthen.setEnabled(true);
				} else {
					txtProxyHost.setEnabled(true);
					txtProxyPort.setEnabled(true);
					txtProxyUser.setEnabled(true);
					txtProxyPass.setEnabled(true);
					cbxProxyAuthen.setEnabled(true);
				}
			}
		});
		cbxProxyActive.setBounds(6, 7, 97, 23);
		contentPanel.add(cbxProxyActive);

		lblHost = new JLabel("Host");
		lblHost.setBounds(16, 37, 46, 14);
		contentPanel.add(lblHost);

		txtProxyHost = new JTextField();
		txtProxyHost.setEnabled(false);
		txtProxyHost.setBounds(53, 34, 257, 20);
		contentPanel.add(txtProxyHost);
		txtProxyHost.setColumns(10);

		lblProxyPort = new JLabel("Port");
		lblProxyPort.setBounds(320, 37, 46, 14);
		contentPanel.add(lblProxyPort);

		cbxProxyAuthen = new JCheckBox("Requires Authenticed:");
		cbxProxyAuthen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbxProxyAuthen.isSelected()) {
					txtProxyUser.setEnabled(true);
					txtProxyPass.setEnabled(true);
				} else {
					txtProxyUser.setEnabled(false);
					txtProxyPass.setEnabled(false);
				}
			}
		});
		cbxProxyAuthen.setBounds(26, 58, 174, 23);
		contentPanel.add(cbxProxyAuthen);

		lblUsername = new JLabel("Username");
		lblUsername.setBounds(36, 88, 67, 14);
		contentPanel.add(lblUsername);

		txtProxyUser = new JTextField();
		txtProxyUser.setEnabled(false);
		txtProxyUser.setBounds(89, 85, 129, 20);
		contentPanel.add(txtProxyUser);
		txtProxyUser.setColumns(10);

		lblPassword = new JLabel("Password");
		lblPassword.setBounds(239, 88, 46, 14);
		contentPanel.add(lblPassword);

		txtProxyPass = new JPasswordField();
		txtProxyPass.setEnabled(false);
		txtProxyPass.setBounds(295, 85, 129, 20);
		contentPanel.add(txtProxyPass);

		lblRequestDelay = new JLabel("Time between requests");
		lblRequestDelay.setBounds(6, 122, 117, 14);
		contentPanel.add(lblRequestDelay);

		lblS = new JLabel("(3 - 30s)");
		lblS.setBounds(198, 122, 67, 14);
		contentPanel.add(lblS);

		lblMaxSearchResult = new JLabel("Max search result");
		lblMaxSearchResult.setBounds(6, 153, 117, 14);
		contentPanel.add(lblMaxSearchResult);

		lblUseragent = new JLabel("User-Agent");
		lblUseragent.setBounds(6, 216, 117, 14);
		contentPanel.add(lblUseragent);

		lblSearchUrl = new JLabel("Search url");
		lblSearchUrl.setBounds(6, 184, 117, 14);
		contentPanel.add(lblSearchUrl);

		txtSearchUrl = new JTextField();
		txtSearchUrl.setBounds(132, 180, 292, 20);
		contentPanel.add(txtSearchUrl);
		txtSearchUrl.setColumns(10);

		txtUserAgent = new JTextArea();
		JScrollPane scrllUserAgent = new JScrollPane(txtUserAgent);
		scrllUserAgent.setBounds(133, 211, 291, 67);
		contentPanel.add(scrllUserAgent);

		txtDelayTime = new JSpinner();
		txtDelayTime.setModel(new SpinnerNumberModel(3, 3, 30, 1));
		txtDelayTime.setBounds(133, 119, 55, 20);
		contentPanel.add(txtDelayTime);

		txtMaxResult = new JSpinner();
		txtMaxResult.setModel(new SpinnerNumberModel(3, 1, 10, 1));
		txtMaxResult.setBounds(133, 150, 55, 20);
		txtMaxResult.setEditor(new JSpinner.NumberEditor(txtMaxResult,"#"));
		contentPanel.add(txtMaxResult);

		label = new JLabel("(1-10)");
		label.setBounds(198, 153, 46, 14);
		contentPanel.add(label);

		txtProxyPort = new JSpinner();
		txtProxyPort.setEnabled(false);
		txtProxyPort.setModel(new SpinnerNumberModel(80, null, 65535, 1));
		txtProxyPort.setBounds(349, 34, 55, 20);
		txtProxyPort.setEditor(new JSpinner.NumberEditor(txtProxyPort,"#"));
		contentPanel.add(txtProxyPort);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				final Container c = getContentPane();
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AppConfig.setProxyActive(cbxProxyActive.isSelected());
						AppConfig.setProxyHost(txtProxyHost.getText());
						AppConfig.setProxyPort((Integer) txtProxyPort.getValue());
						AppConfig.setProxyAuthen(cbxProxyAuthen.isSelected());
						AppConfig.setProxyUser(txtProxyUser.getText());
						AppConfig.setProxyPass(String.valueOf(txtProxyPass.getPassword()));
						AppConfig.setDelayTime((Integer) txtDelayTime.getValue());
						AppConfig.setMaxResult((Integer) txtMaxResult.getValue());
						AppConfig.setSearchUrl(txtSearchUrl.getText());
						AppConfig.setUserAgent(txtUserAgent.getText());
						AppConfig.saveConfig();
						JOptionPane.showMessageDialog(c, "Config has been updated");
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
