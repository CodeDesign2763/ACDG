/*
 * Учебное задание по дисциплине БД
 * Программа для взаимодействия с БД из ЛР5
 * 
 * Класс SpecialityForm
 * 
 * Предназначен для работы с сущностью Speciality из БД
 * 
 * Версия 1
 * 
 * 20.01.2021
 * 
 * (C) 2021 by Alexander Chernokrylov <CodeDesign2763@gmail.com>
 *
 * This program is free software; 
 * you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * as published by
 * the Free Software Foundation
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 */
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
 
class SpecialityForm extends JFrame 
implements ActionListener, DBProcIface, ListSelectionListener
{
	private JPanel centralPanel;
	private JPanel horPanel1;
	private JPanel horPanel2;
	private JPanel verPanel1;
	
	private DefaultListModel<String> specListData=
	new DefaultListModel<String>();
	
	private JList<String> specList = new JList<String>(specListData);
	private JScrollPane specListScroll;
	
	private JButton addButton = new JButton("Добавить");
	private JButton changeButton = new JButton("Изменить");
	private JButton deleteButton = new JButton("Удалить");
	private JButton okButton= new JButton("ОК");
	
	private DefaultComboBoxModel<String> subjListData=
	new DefaultComboBoxModel<String>();
	private JComboBox<String> subjList=
	new JComboBox<String>(subjListData);
	
	private JTextField specName=new JTextField(50);
	private JTextField specCode=new JTextField(10);
	
	private JTextArea output= new JTextArea();
	private JScrollPane outputScroll;
	
	
	private DBConnectionIface dbCon;
	
	private void printMessage(String s) {
		output.setText(output.getText()+"\n"+s);
	}
	
	public void valueChanged(ListSelectionEvent e) {
		/* Обязательно нужно делать проверку 
		 * getSelectedValue()!=null
		 * если в дальнейшем используется метод toString()
		 * иначе может быть nullpointexception
		 */
		Speciality spec;
		QueryResult qr;
		if ((e.getSource()==specList) 
		&& (specList.getSelectedValue()!=null))
		{
			qr=dbCon.getSpecialityByCode(
			specList.getSelectedValue().toString().trim());
			if (qr.getResult()) {
				printMessage(
				"Запись из Speciality получена успешно");
				specCode.setText(qr.getSpec().getSpecialityCode());
				specName.setText(qr.getSpec().getName());
				subjList.setSelectedItem(
				dbCon.getSubjectNameByID(
				qr.getSpec().getSubjectVips()).getString());
			}
			else 
				printMessage(
				"Ошибка при получении записи из Speciality");

			changeButton.setEnabled(true);
			deleteButton.setEnabled(true);
		}
    }
	
	public void setDBcon(DBConnectionIface dbc)
	{
		this.dbCon=dbc;
	} 
	
	public void reset() {
		specList.clearSelection();
		specList.setEnabled(false);
		specListData.removeAllElements();
		
		/* Получение списка НП */
		QueryResult qr;
		qr=dbCon.getSpecialityCodeList();
		if (qr.getResult()) {
			printMessage("Список НП успешно получен");
			fillListWQueryResults(qr,specListData);
		}
		else 
			printMessage("Ошибка при получении списка НП");
		
		/* Получение списка предметов ВИПС */
		qr=dbCon.getSubjectNameList();
		if (qr.getResult()) {
			printMessage("Список предметов успешно получен");
			fillComboBoxWQueryResults(qr,subjListData);
		}
		else 
			printMessage("Ошибка при получении списка предметов");
 		
		specName.setText("");
		specName.setEnabled(false);
		
		specCode.setText("");
		specCode.setEnabled(false);
		
		subjList.setEnabled(false);
		subjList.setSelectedItem("");
		specList.setEnabled(true);
		
		okButton.setEnabled(false);
		addButton.setEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==addButton) {
			reset();
			
			specCode.setEnabled(true);
			okButton.setEnabled(true);
			subjList.setEnabled(true);
			specName.setEnabled(true);
			okButton.setActionCommand("add");
			
			addButton.setEnabled(false);
			changeButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
		if (e.getSource()==changeButton) {
			specCode.setEnabled(true);
			okButton.setEnabled(true);
			subjList.setEnabled(true);
			specName.setEnabled(true);
			okButton.setActionCommand("change");
			
			addButton.setEnabled(false);
			changeButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
		if (e.getSource()==deleteButton)
		{
			
			okButton.setEnabled(true);
			okButton.setActionCommand("delete");
			
			addButton.setEnabled(false);
			changeButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
		if (e.getSource()==okButton)
		{
			if (e.getActionCommand().equals("add"))
			{
				/* Обязательно нужно делать проверку
				 * .getSelectedItem()!=null
				 */
				if (subjList.getSelectedItem()!=null) {
					/* Определим id предмета ВИПС */
					QueryResult qr1=dbCon.getSubjectIDByName(
					subjList.getSelectedItem().toString());
					if (qr1.getResult()) {
						printMessage(
						"ID предмета определен успешно");
						if (dbCon.addSpeciality(
						new Speciality(specCode.getText(),
						qr1.getInt(),specName.getText())))
							printMessage(
							"Новая запись успешно добавлена");
						else
							printMessage(
							"Ошибка при добавлении новой записи");
					} else {
						printMessage(
						"Ошибка при определении ID предмета");
					}
				} else {
					printMessage(
					"Ошибка! Не выбран предмет ВИПС");
				}
			} 
			
			if (e.getActionCommand().equals("change") 
			&& (specList.getSelectedValue()!=null)) {
				
				if (subjList.getSelectedItem()!=null) {
					/* Определим id предмета ВИПС */
					QueryResult qr1=dbCon.getSubjectIDByName(
					subjList.getSelectedItem().toString());
					if (qr1.getResult()) {
						printMessage(
						"ID предмета определен успешно");
						if (dbCon.updateSpecialityByCode(
						specList.getSelectedValue().toString(), 
						new Speciality(specCode.getText(),
						qr1.getInt(),specName.getText())))
							printMessage(
							"Запись успешно отредактирована");
						else
							printMessage(
							"Ошибка при редактировании записи");
					} else {
						printMessage(
						"Ошибка при определении ID предмета");
					}
				} else {
					printMessage(
					"Ошибка! Не выбран предмет ВИПС");
				}
			}
			
			if (e.getActionCommand().equals("delete") 
			&& (specList.getSelectedValue()!=null)) {
				if (dbCon.deleteSpecialityByCode(
				specList.getSelectedValue().toString())) 
					printMessage("Запись удалена успешно");
				else 
					printMessage("Ошибка при удалении записи");
			}
			
			okButton.setEnabled(false);
			specName.setEnabled(false);
			specCode.setEnabled(false);
			subjList.setEnabled(false);
			changeButton.setEnabled(false);
			deleteButton.setEnabled(false);
			reset();
		}
	}
	
	public SpecialityForm(String title,DBConnectionIface dbc)
	{
		//Configuring the graphical interface
		dbCon=dbc;
		setTitle(title);
		centralPanel=new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel,
		BoxLayout.Y_AXIS));
		horPanel1=new JPanel(new FlowLayout());
		verPanel1=new JPanel(new GridLayout(3,1,10,10));
		horPanel2=new JPanel(new FlowLayout());
		
		/* horPanel1 */
		
		specListScroll=new JScrollPane(specList);
		specListScroll.setPreferredSize(new Dimension(300, 200));
		
		horPanel1.add(specListScroll);
		/* verPanel1 */
		specName.setBorder(
		BorderFactory.createTitledBorder("Название НП"));
		verPanel1.add(specName);
		subjList.setBorder(
		BorderFactory.createTitledBorder("Предмет ВИПС"));
		verPanel1.add(subjList);
		specCode.setBorder(
		BorderFactory.createTitledBorder("Код НП"));
		verPanel1.add(specCode);
		
		horPanel1.add(verPanel1);
		centralPanel.add(horPanel1);
		
		/* horPanel2 */
		horPanel2.add(addButton);
		horPanel2.add(changeButton);
		horPanel2.add(deleteButton);
		horPanel2.add(okButton);
		centralPanel.add(horPanel2);
		
		/* output */
		outputScroll=new JScrollPane(output);
		output.setBorder(
		BorderFactory.createTitledBorder("Результат операций"));
		centralPanel.add(outputScroll);
		outputScroll.setPreferredSize(new Dimension(200,300));
		centralPanel.setBorder(
		BorderFactory.createEmptyBorder(10,10,10,10));
		
		add(centralPanel);
    
		/* Настройка обработчиков событий */
    	specList.addListSelectionListener(this);
    	addButton.addActionListener(this);
    	okButton.addActionListener(this);
    	changeButton.addActionListener(this);
    	deleteButton.addActionListener(this);

		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(false);
		//checkanswer.requestFocus();
		this.requestFocusInWindow();
		
		/* Действия при закрытии окна */
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				specList.clearSelection();
				setVisible(false);
			}
		});
	}
}
