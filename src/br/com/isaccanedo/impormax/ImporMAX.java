package br.com.isaccanedo.impormax;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.sun.jndi.url.corbaname.corbanameURLContextFactory;

public class ImporMAX extends JFrame {
	
	private JTable table;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ImporMAX form = new ImporMAX();
				form.setVisible(true);
			}
		});
	}

	public ImporMAX() {

		// Create Form Frame
		super("SONDA IT - ImportMAX - Importação de dados CSV/TXT para persistência - Isac Canedo de Almeida");
		setSize(668, 290);
		setLocation(500, 280);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		// Label Result
		final JLabel lblTitulo = new JLabel("Selecionar um arquivo CSV/TXT para importação", JLabel.CENTER);
		lblTitulo.setBounds(130, 14, 460, 14);
		getContentPane().add(lblTitulo);
		
		// Label Result
		final JLabel lblResult = new JLabel("", JLabel.LEFT);
		lblResult.setBounds(20, 42, 668, 14);
		lblResult.setForeground(Color.RED);
		getContentPane().add(lblResult);

		// Table
		table = new JTable();
		getContentPane().add(table);
		
		// Table Model
		final DefaultTableModel model = (DefaultTableModel)table.getModel();
		
		model.addColumn("DATA");
		model.addColumn("CHAMADO");
		model.addColumn("MOTIVO");
		model.addColumn("CENTRODECUSTO");
		model.addColumn("MATRICULA");		
		model.addColumn("NOME");
		model.addColumn("CELULAR");
		model.addColumn("MODELO");
		model.addColumn("SERIE");
		model.addColumn("CHIP");	

		
		// ScrollPane
		JScrollPane scroll = new JScrollPane(table);
		scroll.setBounds(20, 78, 620, 140);
		getContentPane().add(scroll);


		// Create Button Open JFileChooser
		JButton btnButton = new JButton("Abrir Arquivo", new ImageIcon(getClass().getClassLoader().getResource("find.png")));		
		btnButton.setBounds(505, 10, 135, 23);
		btnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileopen = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter(
						"Text/CSV file", "txt", "csv");
				fileopen.addChoosableFileFilter(filter);

				int ret = fileopen.showDialog(null, "Escolher Arquivo");

				if (ret == JFileChooser.APPROVE_OPTION) {

					// Read Text file
					File file = fileopen.getSelectedFile();

					try {
						BufferedReader br = new BufferedReader(new FileReader(
								file));
						String line;
						int row = 0;
						while ((line = br.readLine()) != null) {
							String[] arr = line.split(",");
							model.addRow(new Object[0]);
							model.setValueAt(arr[0], row, 0);
							model.setValueAt(arr[1], row, 1);
							model.setValueAt(arr[2], row, 2);
							model.setValueAt(arr[3], row, 3);
							model.setValueAt(arr[4], row, 4);
							model.setValueAt(arr[5], row, 5);							
							model.setValueAt(arr[6], row, 6);
							model.setValueAt(arr[7], row, 7);
							model.setValueAt(arr[8], row, 8);
							model.setValueAt(arr[9], row, 9);														
							row++;
						}
						br.close();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}

					lblResult.setText(fileopen.getSelectedFile().toString());
				}

			}
		});
		getContentPane().add(btnButton);
		
		// Button Save
		JButton btnSave = new JButton("Importar dados",new ImageIcon(getClass().getClassLoader().getResource("add.png")));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SaveData(); // save Data
			}
		});
		btnSave.setBounds(252, 228, 170, 23);
		getContentPane().add(btnSave);

	}
	
	
	private void SaveData()
	{
		Connection connect = null;
		Statement s = null;

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			connect = DriverManager.getConnection(""
					+ "jdbc:sqlserver://UIPAXXXXX\\DESV:1433;databaseName=DB_ATIVOS_TELEFONIA","serv_ativos_02","xxxxxxxx");
					

			s = connect.createStatement();
			
			for(int i = 0; i<table.getRowCount();i++)
			{				
				String CHAMADO = table.getValueAt(i, 0).toString();
				String MOTIVO = table.getValueAt(i, 1).toString();				
				String CHIP = table.getValueAt(i, 2).toString();
				
				// SQL Insert		
			

				String sql = "INSERT INTO TelefoniaMovel "
						+ "(CHAMADO,MOTIVO,CHIP) "
						+ "VALUES ('" 						
						+ CHAMADO + "'" + ",'"
						+ MOTIVO + "','"						
						+ CHIP + "') ";
				s.execute(sql);
			}
				
			JOptionPane.showMessageDialog(null,
					"Importação realizada com Sucesso!!!");


		} catch (Exception ex) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, ex.getMessage());
			ex.printStackTrace();
		}

		try {
			if (s != null) {
				s.close();
				connect.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
