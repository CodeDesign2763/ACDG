/*
 * Класс JavaCodePreprocStrategy
 * 
 * Copyright 2021 Alexander Chernokrylov <CodeDesign2763@gmail.com>
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
class JavaCodePreprocStrategy implements ICodePreprocStrategy {
	
	public JavaCodePreprocStrategy() {
		
	}		
	
	/* Текстовое сообщения для контроллера/презентера.
	 * Передается через FileWithClass */
	private void textMessage(String s, 
		ACDGEventListener debugMessageListener) {
		debugMessageListener.onACDGEventReceived(
				new ACDGEvent(this, ACDGEventType.TEXTMESSAGE,
				s) );
	}
	
	@Override
	public boolean deleteCommentsAndOtherStuff(String path2File,
			String path2FileWOComments,
			ACDGEventListener debugMessageListener) {
		/* Классы для работы с регулярными выражениями */
		/* Экранирование двойным \\ */
		
		/* Теперь это локальная переменная */
		boolean fClearedOfComments = true;
		
		Pattern patternImport = Pattern.compile("^import");
		Pattern patternPackage = Pattern.compile("^package");
		Pattern patternCPPComment = Pattern.compile("\\/\\/");
		Pattern patternCCommentOpen = Pattern.compile("\\/\\*");;
		Pattern patternCCommentClose = Pattern.compile("\\*\\/");;
		
		Pattern patternCPPCommentInsideDQ = 
				Pattern.compile("\".*\\/\\/.*\"");
		Pattern patternCCommentOpenInsideDQ = 
				Pattern.compile("\".*\\/\\*.*\"");
		Pattern patternCCommentCloseInsideDQ = 
				Pattern.compile("\".*\\*\\/.*\"");
		
		Pattern patternClass = Pattern.compile("^class");
		Pattern patternEnum = Pattern.compile("enum");
		Pattern patternRBrace = Pattern.compile("\\}");
		
		Matcher matcherImport;
		Matcher matcherPackage;
		Matcher matcherCPPComment;
		Matcher matcherCCommentOpen;
		Matcher matcherCCommentClose;
		Matcher matcherCPPCommentInsideDQ;
		Matcher matcherCCommentOpenInsideDQ;
		Matcher matcherCCommentCloseInsideDQ;
		Matcher matcherClass;
		Matcher matcherEnum;
		Matcher matcherRBrace;
		
		/* В строке найден import */
		boolean fImport;
		/* В строке найден package */
		boolean fPackage;
		/* В строке найден /* */
		boolean fCCommentOpen;
		/* В строке найдена закр комб коммент Си*/
		boolean fCCommentClose; 
		/* В строке найдены коммент CPP*/
		boolean fCPPComment; 
		/* "class" уже встречался */
		boolean fClass=false;
		/* В строке найдена "enum" */
		boolean fEnum;
		/* Состояние enum */
		boolean fEnumMode=false;
		/* В строке найдена "}" */
		boolean fRBrace;
		
		/* Строка для хранения строки файла для отладки при ошибках */
		String s1="Строка отсутствует";
		
		//ProgramLanguage progLang = programLanguage.getPLID();
		
		/* Режим комментариев С */
		boolean fCComment=false;
		
		boolean fDontWriteLine=false;
		
		try {
			//Адрес в момент КОМПИЛЯЦИИ
			File f=new File(path2File);
			Scanner scanfile;
			
			scanfile=new Scanner(f);
			String s;
			FileWriter writer = 
					new FileWriter(path2FileWOComments,false);

			while (scanfile.hasNext())
			{
				s=scanfile.nextLine();
				s1=s;
				textMessage("Строка из файла "+s, debugMessageListener);
				matcherImport = patternImport.matcher(s);
				matcherPackage = patternPackage.matcher(s);
				matcherCPPComment = patternCPPComment.matcher(s);
				matcherCCommentOpen = patternCCommentOpen.matcher(s);
				matcherCCommentClose = 
						patternCCommentClose.matcher(s);
				matcherCPPCommentInsideDQ = 
						patternCPPCommentInsideDQ.matcher(s);
				matcherCCommentOpenInsideDQ = 
						patternCCommentOpenInsideDQ.matcher(s);
				matcherCCommentCloseInsideDQ = 
						patternCCommentCloseInsideDQ.matcher(s);
						
				matcherClass = patternClass.matcher(s);
				matcherEnum = patternEnum.matcher(s);
				matcherRBrace = patternRBrace.matcher(s);
				
				/* Получение значений флагов */
				fImport = matcherImport.find();
				fPackage = matcherPackage.find();
				fCPPComment = matcherCPPComment.find()
						& (!matcherCPPCommentInsideDQ.find());
				fCCommentOpen= matcherCCommentOpen.find()
						& (!matcherCCommentOpenInsideDQ.find());
				fCCommentClose= matcherCCommentClose.find()
						& (!matcherCCommentCloseInsideDQ.find());
						
				fRBrace = matcherRBrace.find();
				fEnum = matcherEnum.find();
				
				
				if (matcherClass.find()) {
					fClass=true; 
				}
				
				/* Для удаления одной строки */
				if ((fDontWriteLine) && (!fEnumMode)) {
					fDontWriteLine=false;
					//textMessage(s);
				}
				
				/* Удаление enum */
				if (!fClass) {
				
					/* Enum на одной строке */
					if ((fEnum) && (fRBrace) && (!fEnumMode)) {
						/* Удаляем всего одну строку */
						fDontWriteLine=true;
						//textMessage(s);
					}
					
					if ((fEnum) && (!fRBrace) && (!fEnumMode)) {
						fEnumMode=true;
						fDontWriteLine=true; 
					}
					
					if ((!fEnum) && (fRBrace) && (fEnumMode)) {
						fEnumMode=false;
						/* А строка все равно будет удалена */
					}
				}
				
				/* Если включен режим комментариев С или
				 * язык - Java и строчка начинается с package или
				 * import - вообще ее не записывать в выходной файл */
				if 	( (fImport) || (fPackage) ) {
					fDontWriteLine = true;
				}
				
				if ((!fCComment) && (fCPPComment)) {
					s=s.substring(0,s.indexOf("//"));
				}
				
				if ((fCCommentOpen) && 
						(!fCCommentClose) ){
					s=s.substring(0,s.indexOf("/*"));
					fCComment=true;
				}
				
				if ((fCCommentClose) && 
						(!fCCommentOpen)) {
					if (s.indexOf("*/") + 2 < s.length()) {
						s=s.substring(s.indexOf("*/")+2);
					} else s="";
					fCComment=false;
					//textMessage("RR" +s);
				}
				
				if ((fCCommentOpen) 
						&& (fCCommentClose)) {
					
					if (s.indexOf("*/") + 2 < s.length()) {
						/* BOGUS */
						s=s.substring(0,s.indexOf("/*")) + 
								s.substring(s.indexOf("*/")+2);
					} else s=s.substring(0,s.indexOf("/*"));
					//textMessage("CR");
				}
				
				if ((!fDontWriteLine) && (!s.equals(""))
						&& 	(!fCComment)) {
					/* Замена "<" и ">" на "я" и "ч" */
					/* иначе xml распознается некорректно */
					s=s.replaceAll("<","zzzzz");
					s=s.replaceAll(">","xxxxx");
					s=s.replaceAll("&","wwwww");
					
					/* Удаляем все строковые константы
					 * в режиме ленивой квантификации */
					s=s.replaceAll("\".*?\"","");
					writer.write(s + "\n");
					//textMessage(s);
				}
			}
			scanfile.close();
			writer.close();
		}	catch (IOException ex1) {
				ex1.printStackTrace();
				fClearedOfComments = false;
		}	catch (Exception e) {
				e.printStackTrace();
				fClearedOfComments = false;
				textMessage("A line that caused the error:"
						+ s1,debugMessageListener);
		}
		return fClearedOfComments;
			
	}
	
	
}

