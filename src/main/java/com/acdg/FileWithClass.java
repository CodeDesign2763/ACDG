/*
 * Класс ClassFromCode
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.io.FileWriter;
import static java.lang.System.out;
import java.lang.Exception;
import java.io.InputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;  

import java.util.ArrayList;

/**
 * Класс предметной области
 * описывающий файл с исходным кодом
 */
class FileWithClass implements ACDGEventSource, ACDGEventListener {
	private String path2File;
	private String fileName;
	
	/* Результат преобразования в XML */
	private boolean conv2XMLResult;
	
	/* Результат распознавания XML-дерева */
	private boolean xmlFileAnalysisResult;
	
	/* Результат предварительной процедурной обработки */
	private boolean fClearedOfComments;
	
	/* Формируется в результате распознавания XML-дерева */
	private ClassDescr classDescription;
	
	private CProgramLanguage programLanguage;
	
	/* Интерфейсы для взаимодействия с моделью */
	private ModelScannerIface modScanIFace;
	private ModelRelationIface modRelIFace;
	private ModelFWCIface modFWCIface;
	
	private ArrayList<ACDGEventListener> eventListeners;
	
	/* Режим обработки параметров при распознавании XML-дерева */
	private ParamProcMode paramProcMode;
	
	/* Число попыток преобразования в XML-дерево */
	private int nAttempts;
	
	/* Running under Windows? */
	private static boolean fWindows=System.getProperty("os.name")
			.startsWith("Windows");
	
	@Override
	public void addACDGEventListener(ACDGEventListener listener) {
		eventListeners.add(listener);
	}	
	
	private void fireEvent(ACDGEvent event) {
		for (ACDGEventListener listener : eventListeners) {
			listener.onACDGEventReceived(event);
		}
	}
	
	/* Текстовое сообщения для контроллера/презентера.
	 * Передается через модель */
	private void textMessage(String s) {
		fireEvent(new ACDGEvent(this, ACDGEventType.TEXTMESSAGE,
				s));
	}
	
	/* Передача текстовых сообщений в модель из методов-стратегий */
	public void onACDGEventReceived(ACDGEvent event) {
		if (event.getEventType() == ACDGEventType.TEXTMESSAGE)
			fireEvent(event);
	}
	
	/* Версия конструктора для отладки */
	public FileWithClass(String fn, CProgramLanguage pl) {
		path2File=fn;
		conv2XMLResult=false;
		fClearedOfComments=false;
		programLanguage=pl;
		modScanIFace=null;
		modRelIFace=null;
		modFWCIface=new FWCIfaceMock();
		eventListeners = new ArrayList<ACDGEventListener>();
		paramProcMode=ParamProcMode.ONLY_DATATYPE;
		
	}
	
	public FileWithClass(String fn, CProgramLanguage pl,
			ModelScannerIface scanIFace, ModelRelationIface relIFace) 
			{
		path2File=fn;
		conv2XMLResult=false;
		fClearedOfComments=false;
		xmlFileAnalysisResult=false;
		programLanguage=pl;
		modScanIFace=scanIFace;
		modRelIFace=relIFace;
		modFWCIface=new FWCIfaceMock();
		eventListeners = new ArrayList<ACDGEventListener>();
		paramProcMode=ParamProcMode.ONLY_DATATYPE;
		
	}
	
	public FileWithClass(String fn, CProgramLanguage pl,
			ModelScannerIface scanIFace, ModelRelationIface relIFace,
			ModelFWCIface mFWCI) {
		path2File=fn;
		conv2XMLResult=false;
		fClearedOfComments=false;
		xmlFileAnalysisResult=false;
		programLanguage=pl;
		modScanIFace=scanIFace;
		modRelIFace=relIFace;
		modFWCIface=mFWCI;
		eventListeners = new ArrayList<ACDGEventListener>();
		paramProcMode=ParamProcMode.ONLY_DATATYPE;
	}
	
	public FileWithClass(String fn, CProgramLanguage pl,
			ModelScannerIface scanIFace, ModelRelationIface relIFace,
			ModelFWCIface mFWCI, ParamProcMode ppm) {
		path2File=fn;
		conv2XMLResult=false;
		fClearedOfComments=false;
		xmlFileAnalysisResult=false;
		programLanguage=pl;
		modScanIFace=scanIFace;
		modRelIFace=relIFace;
		modFWCIface=mFWCI;
		eventListeners = new ArrayList<ACDGEventListener>();
		paramProcMode=ppm;
	}
	
	public boolean getXMLFileAnalysisResult() {
		return xmlFileAnalysisResult;
	}
	
	public boolean isClearedOfComments() {
		return fClearedOfComments;
	}
	
	private String getPath2File() {
		return path2File;
	}
	
	public String getFileName() {
		String res;
		if (!fWindows) {
			res = (path2File.lastIndexOf("/") > -1) ? 
					path2File.substring(path2File.lastIndexOf("/")+1) 
					: path2File;
		} else {
			res = (path2File.lastIndexOf("\\") > -1) ? 
					path2File.substring(path2File.lastIndexOf("\\")+1) 
					: path2File;
		}
		return res;
	}
	
	public boolean getConv2XMLResult() {
		return conv2XMLResult;
	}
	
	public String getClassName() {
		int dotIndex;
		String fileName;
		String res="ERROR!";
		dotIndex=getFileName().indexOf(".");
		if (dotIndex!=-1) {
			res=getFileName().substring(0,dotIndex);
		}
		return res;
	}
	
	private String getPath2FileWOComments() {
		String res;
		if (!fWindows) {
			res = "../temp/"+getFileName()+"_wo_comments";
		} else {
			res = "..\\temp\\"+getFileName()+"_wo_comments";
		}
		return res;
	}
	
	private String getPath2XMLTreeWOFalseTags() {
		return getPath2XMLTree()+"_WFT";
	}
	
	private String getPath2XMLTree() {
		return getPath2FileWOComments()+"_XML";
	}
	
	/* Убирает из исходного файла комментарии и некоторые декларации
	 * и сохраняет результат в файл, определяемый при помощи
	 * getPath2FileWOComments() */
	//public void deleteCommentsAndOtherStuff() {
		///* Классы для работы с регулярными выражениями */
		///* Экранирование двойным \\ */
		
		//fClearedOfComments = true;
		
		//Pattern patternImport = Pattern.compile("^import");
		//Pattern patternPackage = Pattern.compile("^package");
		//Pattern patternCPPComment = Pattern.compile("\\/\\/");
		//Pattern patternCCommentOpen = Pattern.compile("\\/\\*");;
		//Pattern patternCCommentClose = Pattern.compile("\\*\\/");;
		
		//Pattern patternCPPCommentInsideDQ = 
				//Pattern.compile("\".*\\/\\/.*\"");
		//Pattern patternCCommentOpenInsideDQ = 
				//Pattern.compile("\".*\\/\\*.*\"");
		//Pattern patternCCommentCloseInsideDQ = 
				//Pattern.compile("\".*\\*\\/.*\"");
		
		//Pattern patternClass = Pattern.compile("^class");
		//Pattern patternEnum = Pattern.compile("enum");
		//Pattern patternRBrace = Pattern.compile("\\}");
		
		//Matcher matcherImport;
		//Matcher matcherPackage;
		//Matcher matcherCPPComment;
		//Matcher matcherCCommentOpen;
		//Matcher matcherCCommentClose;
		//Matcher matcherCPPCommentInsideDQ;
		//Matcher matcherCCommentOpenInsideDQ;
		//Matcher matcherCCommentCloseInsideDQ;
		//Matcher matcherClass;
		//Matcher matcherEnum;
		//Matcher matcherRBrace;
		
		///* В строке найден import */
		//boolean fImport;
		///* В строке найден package */
		//boolean fPackage;
		///* В строке найден /* */
		//boolean fCCommentOpen;
		///* В строке найдена закр комб коммент Си*/
		//boolean fCCommentClose; 
		///* В строке найдены коммент CPP*/
		//boolean fCPPComment; 
		///* "class" уже встречался */
		//boolean fClass=false;
		///* В строке найдена "enum" */
		//boolean fEnum;
		///* Состояние enum */
		//boolean fEnumMode=false;
		///* В строке найдена "}" */
		//boolean fRBrace;
		
		///* Строка для хранения строки файла для отладки при ошибках */
		//String s1="Строка отсутствует";
		
		//ProgramLanguage progLang = programLanguage.getPLID();
		
		///* Режим комментариев С */
		//boolean fCComment=false;
		
		//boolean fDontWriteLine=false;
		
		//try {
			////Адрес в момент КОМПИЛЯЦИИ
			//File f=new File(path2File);
			//Scanner scanfile;
			
			//scanfile=new Scanner(f);
			//String s;
			//FileWriter writer = 
					//new FileWriter(getPath2FileWOComments(),false);

			//while (scanfile.hasNext())
			//{
				//s=scanfile.nextLine();
				//s1=s;
				////textMessage("Строка из файла "+s);
				//matcherImport = patternImport.matcher(s);
				//matcherPackage = patternPackage.matcher(s);
				//matcherCPPComment = patternCPPComment.matcher(s);
				//matcherCCommentOpen = patternCCommentOpen.matcher(s);
				//matcherCCommentClose = 
						//patternCCommentClose.matcher(s);
				//matcherCPPCommentInsideDQ = 
						//patternCPPCommentInsideDQ.matcher(s);
				//matcherCCommentOpenInsideDQ = 
						//patternCCommentOpenInsideDQ.matcher(s);
				//matcherCCommentCloseInsideDQ = 
						//patternCCommentCloseInsideDQ.matcher(s);
						
				//matcherClass = patternClass.matcher(s);
				//matcherEnum = patternEnum.matcher(s);
				//matcherRBrace = patternRBrace.matcher(s);
				
				///* Получение значений флагов */
				//fImport = matcherImport.find();
				//fPackage = matcherPackage.find();
				//fCPPComment = matcherCPPComment.find()
						//& (!matcherCPPCommentInsideDQ.find());
				//fCCommentOpen= matcherCCommentOpen.find()
						//& (!matcherCCommentOpenInsideDQ.find());
				//fCCommentClose= matcherCCommentClose.find()
						//& (!matcherCCommentCloseInsideDQ.find());
						
				//fRBrace = matcherRBrace.find();
				//fEnum = matcherEnum.find();
				
				
				//if (matcherClass.find()) {
					//fClass=true; 
				//}
				
				///* Для удаления одной строки */
				//if ((fDontWriteLine) && (!fEnumMode)) {
					//fDontWriteLine=false;
					////textMessage(s);
				//}
				
				///* Удаление enum */
				//if (!fClass) {
				
					///* Enum на одной строке */
					//if ((fEnum) && (fRBrace) && (!fEnumMode)) {
						///* Удаляем всего одну строку */
						//fDontWriteLine=true;
						////textMessage(s);
					//}
					
					//if ((fEnum) && (!fRBrace) && (!fEnumMode)) {
						//fEnumMode=true;
						//fDontWriteLine=true; 
					//}
					
					//if ((!fEnum) && (fRBrace) && (fEnumMode)) {
						//fEnumMode=false;
						///* А строка все равно будет удалена */
					//}
				//}
				
				///* Если включен режим комментариев С или
				 //* язык - Java и строчка начинается с package или
				 //* import - вообще ее не записывать в выходной файл */
				//if ((progLang == ProgramLanguage.JAVA) &&
						//(fImport) ||
						//(fPackage) ) {
					//fDontWriteLine = true;
				//}
				
				//if ((!fCComment) && (fCPPComment)) {
					//s=s.substring(0,s.indexOf("//")); XXXXXXXX
				//}
				
				//if ((fCCommentOpen) && 
						//(!fCCommentClose) ){
					//s=s.substring(0,s.indexOf("/*"));
					//fCComment=true;
				//}
				
				//if ((fCCommentClose) && 
						//(!fCCommentOpen)) {
					//if (s.indexOf("*/") + 2 < s.length()) {
						//s=s.substring(s.indexOf("*/")+2);
					//} else s="";
					//fCComment=false;
					////textMessage("RR" +s);
				//}
				
				//if ((fCCommentOpen) 
						//&& (fCCommentClose)) {
					
					//if (s.indexOf("*/") + 2 < s.length()) {
						///* BOGUS */
						//s=s.substring(0,s.indexOf("/*")) + 
								//s.substring(s.indexOf("*/")+2);
					//} else s=s.substring(0,s.indexOf("/*"));
					////textMessage("CR");
				//}
				
				//if ((!fDontWriteLine) && (!s.equals(""))
						//&& 	(!fCComment)) {
					///* Замена "<" и ">" на "я" и "ч" */
					///* иначе xml распознается некорректно */
					//s=s.replaceAll("<","zzzzz");
					//s=s.replaceAll(">","xxxxx");
					//s=s.replaceAll("&","wwwww");
					
					///* Удаляем все строковые константы
					 //* в режиме ленивой квантификации */
					//s=s.replaceAll("\".*?\"","");
					//writer.write(s + "\n");
					////textMessage(s);
				//}
			//}
			//scanfile.close();
			//writer.close();
		//}	catch (IOException ex1) {
				//ex1.printStackTrace();
				//fClearedOfComments = false;
		//}	catch (Exception e) {
				//e.printStackTrace();
				//fClearedOfComments = false;
				//textMessage("A line that caused the error:"
						//+ s1);
		//}
			
	//}
	
	
	
	public void convSourceFile2XML() {
		
		ProgramLanguage pl = programLanguage.getPLID();
		String path2Grammar=
				programLanguage.getPath2Grammar();
		Process proc;
		String errStr;
		String stoutStr;
		
		boolean fSuccess;
		boolean fExtraCheck;
		
		/* Иногда Bullwinkle дает сбой и на преобразование 1 файла
		 * в XML отводится до 5 попыток */
		int i=0;
		do  {
			fSuccess=true;
		
			/* Предварительная процедурная обработка */
			/* Удаляем комментарии и некоторые инструкции */
			fClearedOfComments = 
					programLanguage.getCodePreprocStrategy()
					.deleteCommentsAndOtherStuff(path2File,
					getPath2FileWOComments(), this);
		
			/* Если предыдущий шаг выполнить не удалось, то итог
			* выполнения метода convSourceFile2XML - неудача */
			/* Может быть, можно отказаться от этого? */
			fSuccess=isClearedOfComments();
					 
			try {
			
				/* Запускаем bullwinkle.jar и перенаправляем stdout
				* в xml-файл */
				//textMessage(path2Grammar);
				String path2Bullwinkle;
				path2Bullwinkle = ((!fWindows) ? 
						"../bin/bullwinkle.jar"
						: "..\\bin\\bullwinkle.jar");
				//if (fWindows) {
					//path2Bullwinkle = "..\\bin\\bullwinkle.jar";
				//} else {
					//path2Bullwinkle = "../bin/bullwinkle.jar";
				//}
				
				File NULL_F = new File(
						((fWindows)  
						? "NUL" : "/dev/null"));
				var processBuilder = new ProcessBuilder();
				processBuilder.command(modFWCIface.getPath2Java(),
						"-jar",
						path2Bullwinkle,
						path2Grammar,
						getPath2FileWOComments());
				var fileXML = new File(getPath2XMLTree());
				processBuilder.redirectOutput(fileXML);
				//processBuilder.redirectError(NULL_F);
				proc = processBuilder.start();
				proc.waitFor();
			
				/* Определяем, удачно ли произведено 
				 * преобразование через
				 * подсчет числа строк в полученном xml-файле */
				BufferedReader reader = 
						new BufferedReader(
						new FileReader(getPath2XMLTree()));
				long lines=0;
				while (reader.readLine()!=null) lines++;
				reader.close();
			
				if (lines>5) {
					//textMessage("Преобразование прошло удачно");
				} else {
					//textMessage("Преобразование прошло неудачно");
					fSuccess=false;
				}

				//textMessage("Выполнение программы завершено");
			} catch (Exception e) { 
				fSuccess=false;
				e.printStackTrace();
			} finally {
				/* Дополнительная проверка, выявляющая сбой 
				 * Bullwinkle */
				fExtraCheck=xmlTreeExtraCheck();
				
				try {
					/* Удаляем промежуточный файл */
					Files.deleteIfExists(
							Paths.get(getPath2FileWOComments()));
				}	catch (Exception e) {}
			}
		
			/* Удаляем с XML-файла лишние вложенные тэги token */
			fSuccess = fSuccess && xmlWrongTagDeletion();
			/* Удаляем промежуточный файл */
			try {
				/* Удаляем промежуточный файл */
				Files.deleteIfExists(
				Paths.get(getPath2XMLTree()));
			}	catch (Exception e) {}
		
			i++;
			//if (i>0) textMessage("Попытка "+String.valueOf(i));
		
		/* Повторяем пока не будет сбоя Bullwinkle (до 5 раз) */
		} while ((!fExtraCheck) && (i<5));

		nAttempts=i;
		conv2XMLResult=fSuccess;
		/* Для отладки */
		//textMessage(fSuccess);
	}
	
	/* Удаление из XML-файла тэгов token,
	 * вложенных в тэг token вместе с их содержимым */
	public boolean xmlWrongTagDeletion() {
		boolean fTokenTag = false;
		boolean fDeleteLine = false;
		boolean fResult = true;
		//Pattern patternTokenOpen = Pattern.compile("\\<Token\\>");
		//Pattern patternTokenClose = Pattern.compile("\\</Token\\>");
		Pattern patternTokenOpen = Pattern.compile("\\<token\\>");
		Pattern patternTokenClose = Pattern.compile("<\\/token>");
		Matcher matcherTokenOpen;
		Matcher matcherTokenClose;
		
		try {
			File f=new File(getPath2XMLTree());
			Scanner scanfile;
			
			scanfile=new Scanner(f);
			String s;
			FileWriter writer = 
					new 
					FileWriter(getPath2XMLTreeWOFalseTags(),false);
			while (scanfile.hasNext())
				{
					s=scanfile.nextLine();
					matcherTokenOpen = patternTokenOpen.matcher(s);
					matcherTokenClose = patternTokenClose.matcher(s);
					if (matcherTokenOpen.find()) {
						if (fTokenTag==false) {
							fTokenTag=true;
						} else {
							fDeleteLine=true;
						}
					}
					if (!fDeleteLine) writer.write(s + "\n");
					if (matcherTokenClose.find()) {
						if (fDeleteLine==true) {
							fDeleteLine=false;
						} else {
							fTokenTag=false;
						}
					}
			
				}
				scanfile.close();
				writer.close();
			
				//Меняем флаг класса
				//fClearedOfComments = true;
		}	catch (Exception e) {
			e.printStackTrace();
			fResult=false;
		}
		
		return fResult;
	}
	
	/* Чтение XML-дерева 
	 * В итоге заполняется classDescription и добавляются
	 * отношения в модель */
	private void readXMLFile() {
		xmlFileAnalysisResult = true;
		try {
			classDescription = programLanguage.getProcStrategy()
					.readXMLFile(getPath2XMLTreeWOFalseTags(),
					modScanIFace, modRelIFace, paramProcMode);
		} catch (Exception e) {
			e.printStackTrace();
			xmlFileAnalysisResult = false;
		} finally {
			/* Удаляем промежуточный файл */
			try {
				Files.deleteIfExists(
				Paths.get(getPath2XMLTreeWOFalseTags()));
		}	catch (Exception e) {}
		}
	}
	
	/* Дополнительная проверка XML-дерева, выявляющая сбой 
	 * в работе Bullwinkle */
	private boolean xmlTreeExtraCheck() {
		boolean res=true;
		try {
			File f=new File(getPath2XMLTree());
			Scanner scanfile;
			scanfile=new Scanner(f);
			String s=""; 
			s=scanfile.nextLine();
			if (s.equals("#")) res=false;
			scanfile.close();
		} catch (Exception e) {
			e.printStackTrace();
			res=false;
		} finally {
			
		}
		return res;
	}
	
	/* Преобразование типа boolean в "Успешно" / "Ошибка" */
	private String bool2String(boolean a) {
		return (a) ? new String("Successful") : new String("Failed");
	}
	
	/* Сгенерировать описание класса */
	public void genClassDescr() {
		convSourceFile2XML();
		readXMLFile();
		/* Для отладки */
		textMessage("File: " + getFileName() + "\n"
		//+ "File Path:" + getPath2File() + "\n"
		//+ "Path 2 file wo comments:" +getPath2FileWOComments() +"\n"
		//+ "Path 2 xml tree:" + getPath2XMLTree() + "\n"
		//+ "Path 2 xml tree WFT:" + getPath2XMLTreeWOFalseTags() + "\n"
				+ "Pre-processing: "
				+ bool2String(isClearedOfComments()) + "\n"
				+ "Conversion to XML tree: " 
				+ bool2String(getConv2XMLResult()) + "\n"
				+ "Number of attempts: " + 
				String.valueOf(nAttempts) + "\n"
				+ "XML tree analysis: "
				+ bool2String(
				getXMLFileAnalysisResult()) + "\n");
	}
	
	/* Получение PlantUML кода для файла с классом */
	public String conv2PlantUMLString() {
		return classDescription.conv2PlantUMLString();
	}
	
	/* Для отладки - вывод списка методов на экран */
	public void showMethodsList() {
		try {
			File f=new File(getPath2XMLTreeWOFalseTags());
			DocumentBuilderFactory dbf = 
					DocumentBuilderFactory.newInstance();  
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(f);  
			doc.getDocumentElement().normalize();  
		
			NodeList nodeList = doc.getElementsByTagName("Method"); 
			for (int i = 0; i < nodeList.getLength(); i++) {  
				Node node = nodeList.item(i);  
				if (node.getNodeType() == Node.ELEMENT_NODE) {  
					Element eElement = (Element) node;
					textMessage("Название метода: "
							+ eElement.getElementsByTagName(
							"MethodIdentifier")
							.item(0).getTextContent().trim());  
				}  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

