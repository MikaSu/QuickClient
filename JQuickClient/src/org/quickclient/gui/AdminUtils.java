package org.quickclient.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.quickclient.classes.ExJTextField;
import org.quickclient.classes.SwingHelper;

import com.documentum.fc.common.DfException;
import com.documentum.fc.impl.util.RegistryPasswordUtils;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class AdminUtils extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final AdminUtils frame = new AdminUtils();
					frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private final JPanel contentPane;
	private final ExJTextField textClearText;
	private final ExJTextField textEncText;
	private final ExJTextField textDataticket;

	private final ExJTextField textFilepath;

	/**
	 * Create the frame.
	 */
	public AdminUtils() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 740, 215);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("708px"), }, new RowSpec[] { FormFactory.UNRELATED_GAP_ROWSPEC, RowSpec.decode("fill:default"), FormFactory.NARROW_LINE_GAP_ROWSPEC, RowSpec.decode("fill:default"), }));

		final JPanel encr = new JPanel();
		encr.setBorder(new TitledBorder(null, "Encryption", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(encr, "2, 2, fill, fill");
		encr.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("53px"), FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("284px"), FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("50px"), FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("283px"), }, new RowSpec[] { RowSpec.decode("27px"), RowSpec.decode("20px"), }));

		final JLabel lblNewLabel = new JLabel("Encrypted:");
		encr.add(lblNewLabel, "2, 1, left, center");

		textEncText = new ExJTextField();
		encr.add(textEncText, "4, 1, fill, top");
		textEncText.setColumns(10);

		final JButton btDecrypt = new JButton("Decrypt");
		btDecrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final String crypt = textEncText.getText();
				try {
					final String ct = RegistryPasswordUtils.decrypt(crypt);
					textClearText.setText(ct);
				} catch (final DfException e) {
					SwingHelper.showErrorMessage(e.getMessage(), e.getMessage());
				}

				// java com.documentum.fc.tools.RegistryPasswordUtil
			}
		});

		final JLabel lblClearText = new JLabel("Clear Text");
		encr.add(lblClearText, "6, 1, left, center");

		textClearText = new ExJTextField();
		encr.add(textClearText, "8, 1, fill, top");
		textClearText.setColumns(10);
		encr.add(btDecrypt, "4, 2, left, top");

		final JButton btnEncrypt = new JButton("Encrypt");
		btnEncrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final String ct = textClearText.getText();
				try {
					final String enctext = RegistryPasswordUtils.encrypt(ct);
					textEncText.setText(enctext);
				} catch (final DfException e) {
					SwingHelper.showErrorMessage(e.getMessage(), e.getMessage());
				}
			}
		});
		encr.add(btnEncrypt, "8, 2, left, top");

		final JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Dataticket calculator", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_1, "2, 4, fill, fill");
		panel_1.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("42px"), FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("294px"), FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("53px"), FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("281px"), }, new RowSpec[] { RowSpec.decode("27px"), RowSpec.decode("20px"), }));

		final JLabel lblFilepath = new JLabel("Filepath:");
		panel_1.add(lblFilepath, "2, 1, left, center");

		textFilepath = new ExJTextField();
		panel_1.add(textFilepath, "4, 1, fill, top");
		textFilepath.setColumns(10);

		final JButton btnCalculateDataticket = new JButton("Calculate dataticket");
		btnCalculateDataticket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final String filepath = textFilepath.getText();
				if (filepath.length() != 8) {
					SwingHelper.showErrorMessage("Error", "Example: 802e8a62");
				}
				final String binary = Long.toBinaryString(Long.parseLong(filepath, 16) - 1);
				String invert = "";
				for (int i = 0; i < binary.length(); i++) {
					if (binary.substring(i, i + 1).equals("0")) {
						invert += "1";
					} else {
						invert += "0";
					}
				}
				final Long piitka = -Long.parseLong(invert, 2);
				textDataticket.setText(String.valueOf(piitka.longValue()));
			}
		});

		final JLabel lblDataticket = new JLabel("Dataticket:");
		panel_1.add(lblDataticket, "6, 1, left, center");

		textDataticket = new ExJTextField();
		panel_1.add(textDataticket, "8, 1, fill, top");
		textDataticket.setColumns(10);
		panel_1.add(btnCalculateDataticket, "4, 2, left, top");

		final JButton btnCalculateFilepath = new JButton("Calculate filepath");
		btnCalculateFilepath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final Long piitka = Long.parseLong(textDataticket.getText());

			}
		});
		panel_1.add(btnCalculateFilepath, "8, 2, left, top");
	}

}
