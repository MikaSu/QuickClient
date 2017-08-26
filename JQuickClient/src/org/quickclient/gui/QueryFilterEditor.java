package org.quickclient.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.quickclient.classes.ExJTextField;
import org.quickclient.classes.QueryFilter;

import com.documentum.fc.common.DfLogger;

public class QueryFilterEditor extends JFrame {

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final QueryFilterEditor frame = new QueryFilterEditor();
					frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JPanel contentPane;
	private ExJTextField txtAttribute;
	private ExJTextField txtValue;
	private JTable table;
	private JCheckBox chckbxMatchCase;
	private JComboBox<?> comboValidationType;
	private JPanel panel;
	private JButton btnOk;
	private JButton btnCancel;
	private ArrayList<QueryFilter> queryfilter = new ArrayList<QueryFilter>();

	private ExJTextField txtCount;

	/**
	 * Create the frame.
	 */
	public QueryFilterEditor() {
		setTitle("Filter Editor");
		try {
			UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.PlasticXPLookAndFeel());
		} catch (final UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 527, 413);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JLabel lblNewLabel = new JLabel("Attribute:");
		lblNewLabel.setBounds(12, 19, 66, 14);
		contentPane.add(lblNewLabel);

		txtAttribute = TextFieldFactory.createJTextField();
		txtAttribute.setText("object_name");
		txtAttribute.setToolTipText("attribute validate, i.e. object_name");
		txtAttribute.setBounds(98, 16, 176, 20);
		contentPane.add(txtAttribute);
		txtAttribute.setColumns(10);

		final JLabel lblNewLabel_1 = new JLabel("Value:");
		lblNewLabel_1.setBounds(12, 50, 46, 14);
		contentPane.add(lblNewLabel_1);

		txtValue = TextFieldFactory.createJTextField();
		txtValue.setToolTipText("Validation String");
		txtValue.setBounds(98, 47, 176, 20);
		contentPane.add(txtValue);
		txtValue.setColumns(10);

		chckbxMatchCase = new JCheckBox("Match Case");
		chckbxMatchCase.setToolTipText("Use case sensitive validation");
		chckbxMatchCase.setBounds(98, 70, 81, 23);
		contentPane.add(chckbxMatchCase);

		comboValidationType = new JComboBox();
		comboValidationType.setToolTipText("Type of validation");
		comboValidationType.setModel(new DefaultComboBoxModel(new String[] { "Contains", "Exact Match", "Begins With", "Ends With", "Regular Expression", "Max Rows" }));
		comboValidationType.setBounds(98, 93, 176, 22);
		contentPane.add(comboValidationType);

		final JButton btnAddFilter = new JButton("Add Filter");
		btnAddFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {

				final QueryFilter filter = new QueryFilter();
				if (chckbxMatchCase.isSelected()) {
					filter.setMatchcase(true);
				} else {
					filter.setMatchcase(false);
				}
				final String countText = txtCount.getText();
				// int jeps = Integer

				filter.setAttributename(txtAttribute.getText());
				filter.setRequiredvalue(txtValue.getText());
				final String validaationtyyppi = (String) comboValidationType.getSelectedItem();

				filter.setFiltertype(QueryFilter.FILTER_TYPE_EXACT_MATCH);

				if (validaationtyyppi.equals("Exact Match")) {
					filter.setFiltertype(QueryFilter.FILTER_TYPE_EXACT_MATCH);
				} else if (validaationtyyppi.equals("Begins With")) {
					filter.setFiltertype(QueryFilter.FILTER_TYPE_BEGINS_WITH);
				} else if (validaationtyyppi.equals("Contains")) {
					filter.setFiltertype(QueryFilter.FILTER_TYPE_CONTAINS);
				} else if (validaationtyyppi.equals("Ends With")) {
					filter.setFiltertype(QueryFilter.FILTER_TYPE_ENDS_WITH);
				} else if (validaationtyyppi.equals("Regular Expression")) {
					filter.setFiltertype(QueryFilter.FILTER_TYPE_REGEX);
				} else if (validaationtyyppi.equals("Max Rows")) {
					filter.setFiltertype(QueryFilter.FILTER_TYPE_MAX_ROWS);
				}
				if (countText.length() > 0) {
					try {
						final int jep = Integer.parseInt(countText);
						filter.setMaxcount(jep);
						filter.setFiltertype(QueryFilter.FILTER_TYPE_MAX_ROWS);
					} catch (final NumberFormatException ex) {
						DfLogger.error(this, ex.getMessage(), null, ex);
					}
				}
				queryfilter.add(filter);
				txtCount.setText("");
				init();
			}
		});
		btnAddFilter.setBounds(98, 157, 91, 23);
		contentPane.add(btnAddFilter);

		final JLabel lblValidationType = new JLabel("Validation type:");
		lblValidationType.setBounds(12, 97, 91, 14);
		contentPane.add(lblValidationType);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Current Filterset", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(6, 181, 499, 164);
		contentPane.add(panel);
		panel.setLayout(null);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 20, 487, 144);
		panel.add(scrollPane);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Attribute", "Validation String", "Match Case", "Validation Type", "Max rows" }) {
			Class[] columnTypes = new Class[] { String.class, String.class, Boolean.class, String.class, String.class };

			@Override
			public Class getColumnClass(final int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(1).setPreferredWidth(129);
		table.getColumnModel().getColumn(3).setPreferredWidth(142);
		scrollPane.setViewportView(table);

		btnOk = new JButton("Clear Filters");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				queryfilter.clear();
				init();
			}
		});
		btnOk.setBounds(199, 157, 91, 23);
		contentPane.add(btnOk);

		btnCancel = new JButton("Close");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(402, 352, 91, 23);
		contentPane.add(btnCancel);

		final JLabel lblMaxRows = new JLabel("Max Rows:");
		lblMaxRows.setBounds(12, 130, 81, 14);
		contentPane.add(lblMaxRows);

		txtCount = TextFieldFactory.createJTextField();
		txtCount.setToolTipText("Max rows to show");
		txtCount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				comboValidationType.setSelectedItem("Max Rows");
			}
		});
		txtCount.setBounds(98, 126, 176, 20);
		contentPane.add(txtCount);
		txtCount.setColumns(10);
	}

	public void init() {
		final DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		for (final QueryFilter filter : queryfilter) {
			final Vector v = new Vector();
			v.add(filter.getAttributename());
			v.add(filter.getRequiredvalue());
			v.add(filter.isMatchcase());
			final int filtertype = filter.getFiltertype();
			String filterString = "";
			if (filtertype == QueryFilter.FILTER_TYPE_BEGINS_WITH) {
				filterString = "Starts With";
			}
			if (filtertype == QueryFilter.FILTER_TYPE_CONTAINS) {
				filterString = "Contains";
			}
			if (filtertype == QueryFilter.FILTER_TYPE_ENDS_WITH) {
				filterString = "Ends With";
			}
			if (filtertype == QueryFilter.FILTER_TYPE_EXACT_MATCH) {
				filterString = "Exact Match";
			}
			if (filtertype == QueryFilter.FILTER_TYPE_REGEX) {
				filterString = "Regular Expression";
			}
			if (filtertype == QueryFilter.FILTER_TYPE_MAX_ROWS) {
				filterString = "Max Rows";
			}
			v.add(filterString);
			v.add(filter.getMaxcount());
			model.addRow(v);
		}
		table.setModel(model);
		table.validate();
	}

	public void setQueryfilter(final ArrayList<QueryFilter> queryfilter) {
		this.queryfilter = queryfilter;

	}
}
