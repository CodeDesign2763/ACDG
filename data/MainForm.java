/*
 * Учебное задание по дисциплине БД
 * Программа для взаимодействия с БД из ЛР5
 * 
 * Класс MainForm
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
 
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.FocusEvent;
//import java.awt.event.FocusListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.event.*;

class MainForm extends JFrame implements ActionListener 
{
	/* Элементы графического интефейса */
	private JLabel lInfo;
	private JPanel centralPanel;
	private JButton specButton;
	private JButton olympButton;
	private JButton ratingButton;
	private JButton orderButton;
	
	/* Интерфейс для соединения с БД */
	private DBConnectionIface dbCon;
	
	/* Вспомогательные формы */
	private SpecialityForm specForm;
	//private OlympiadForm olympForm;
	
	public void setDBcon(DBConnectionIface dbc) {
		specForm=new SpecialityForm("Таблица Speciality",dbc);
		//OlympForm=new OlympiadForm("Таблица Olympiad",dbc);
		dbCon=dbc;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==specButton) {
			specForm.setVisible(true);
			specForm.setDBcon(dbCon);
			specForm.reset();
		}
	}
	
	public MainForm(String title) {
		/* Настройка графического интерфейса */
		setTitle(title);
		centralPanel=new JPanel(new GridLayout(5,1,10,10));
		centralPanel.setBorder(
		BorderFactory.createEmptyBorder(10, 10, 10, 10));
		lInfo=new JLabel("<html>ЛР №5 по дисциплине БД <br>"
		+"Выполнил: студент группы ВТВ-467 <br>"
		+"А. В. Чернокрылов <br>"
		+"Проверил: к. т. н. <br>"
		+"А. А. Соколов"
		+"</html>");
		centralPanel.add(lInfo);
		specButton=new JButton("Таблица Speciality");
		centralPanel.add(specButton);
		olympButton=new JButton("Таблица Olympiad");
		centralPanel.add(olympButton);
		ratingButton=new JButton("Рейтинг абитуриентов");
		centralPanel.add(ratingButton);
		orderButton=new JButton(
		"Список лиц, рекомендованных к зачислению");
		centralPanel.add(orderButton);
		add(centralPanel);
		/* Блокировка кнопок */
		olympButton.setEnabled(false);
		ratingButton.setEnabled(false);
		orderButton.setEnabled(false);
		
		/* Настройка обработчиков событий */
		specButton.addActionListener(this);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(false);
		/* Действия при закрытии окна */
		this.requestFocusInWindow();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dbCon.disconnect();
				System.exit(0);
			}
		});
	}
}
