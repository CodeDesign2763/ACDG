/*
 * Класс BypassCodePreprocStrategy
 * 
 * Copyright 2022 Alexander Chernokrylov <CodeDesign2763@gmail.com>
 * 
 * This is a part of ACDG.
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  
 * If not, see <https://www.gnu.org/licenses/>.
*/

package com.acdg;
import static java.lang.System.out;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;
import java.util.regex.Matcher;
import java.util.regex.Pattern;  
import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Стратегия предварительной процедурной обработки 
 * файлов с исходным кодом
 * для языка Java
 */
class BypassCodePreprocStrategy implements ICodePreprocStrategy {
	
	public BypassCodePreprocStrategy() {
		
	}		
	
	
	/* Текстовое сообщения для контроллера/презентера.
	 * Передается через FileWithClass */
	private void textMessage(String s, 
		ACDGEventListener debugMessageListener) {
		debugMessageListener.onACDGEventReceived(
				new ACDGEvent(this, ACDGEventType.TEXTMESSAGE,
				s) );
		/* Для отладки */
		//out.println(" ОТЛАДКА "+s);
	}
	
	@Override
	public boolean deleteCommentsAndOtherStuff(String path2File,
			String path2FileWOComments,
			ACDGEventListener debugMessageListener) {
				
		boolean fClearedOfComments = true;

		try {
			File f=new File(path2File);
			Scanner scanfile;
			
			scanfile=new Scanner(f);
			String s;
			FileWriter writer = 
					new FileWriter(path2FileWOComments,false);

			while (scanfile.hasNext())
			{
				s=scanfile.nextLine();
				writer.write(s + "\n");
			}
			scanfile.close();
			writer.close();
		}	catch (IOException ex1) {
				ex1.printStackTrace();
				fClearedOfComments = false;
		}	catch (Exception e) {
				e.printStackTrace();
				fClearedOfComments = false;
		}
		return fClearedOfComments;
	}
}

