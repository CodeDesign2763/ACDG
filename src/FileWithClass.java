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



package ACDG;
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

/**
 * Класс предметной области
 * описывающий файл с описанием класса на языке Java
 */
 
class FileWithClass {
	private String path2File;
	private String fileName;
	private boolean conv2XMLResult;
	
	private boolean fClearedOfComments;
	private ClassDescr classDescription;
	
	
	
	private CProgramLanguage programLanguage;
	
	public FileWithClass(String fn, CProgramLanguage pl) {
		path2File=fn;
		conv2XMLResult=false;
		fClearedOfComments=false;
		programLanguage=pl;
		
	}
	
	public boolean isClearedOfComments() {
		return fClearedOfComments;
	}
	
	//public void setConvResult(boolean result) {
		//conv2XMLResult=result;
	//}
	
	private String getPath2File() {
		return path2File;
	}
	
	public String getFileName() {
		return (path2File.lastIndexOf("/") > -1) ? 
		path2File.substring(path2File.lastIndexOf("/")+1) : path2File;
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
		return "../temp/"+getFileName()+"_wo_comments";
	}
	
	private String getPath2XMLTreeWOFalseTags() {
		return getPath2XMLTree()+"_WFT";
	}
	
	/* Убирает из исходного файла комментарии и некоторые декларации
	 * и сохраняет результат в файл, определяемый при помощи
	 * getPath2FileWOComments() */
	public void deleteCommentsAndOtherStuff() {
		/* Классы для работы с регулярными выражениями */
		/* Экранирование двойным \\ */
		
		/* FIXME */
		/* Это жесть - нужно все переписать !!! */
		/* S меняется, а паттерн все ищет там что-то */
		fClearedOfComments = true;
		
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
		
		 
		
		ProgramLanguage progLang = programLanguage.getPLID();
		
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
					new FileWriter(getPath2FileWOComments(),false);

			while (scanfile.hasNext())
			{
				s=scanfile.nextLine();
				s1=s;
				//out.println("Строка из файла "+s);
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
					//out.println(s);
				}
				
				
				/* Удаление enum */
				
				if (!fClass) {
				
					/* Enum на одной строке */
					if ((fEnum) && (fRBrace) && (!fEnumMode)) {
						/* Удаляем всего одну строку */
						fDontWriteLine=true;
						out.println(s);
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
				if ((progLang == ProgramLanguage.JAVA) &&
						(fImport) ||
						(fPackage) ) {
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
					//out.println("RR" +s);
				}
				
				
				
				if ((fCCommentOpen) 
						&& (fCCommentClose)) {
					
					if (s.indexOf("*/") + 2 < s.length()) {
						/* BOGUS */
						s=s.substring(0,s.indexOf("/*")) + 
								s.substring(s.indexOf("*/")+2);
					} else s=s.substring(0,s.indexOf("/*"));
					//out.println("CR");
				}
				
				
				if ((!fDontWriteLine) && (!s.equals(""))
						&& 	(!fCComment)) {
					/* Замена "<" и ">" на "я" и "ч" */
					/* иначе xml распознается некорректно */
					s=s.replaceAll("<","zzzzz");
					s=s.replaceAll(">","xxxxx");
					s=s.replaceAll("&","wwwww");
					
					
					writer.write(s + "\n");
					//out.println(s);
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
				out.println("Строка, которая вызвала ошибку:"
						+ s1);
		}
			
	}
	
	
	private String getPath2XMLTree() {
		return getPath2FileWOComments()+"_XML";
	}
	
	public void convSourceFile2XML() {
		ProgramLanguage pl = programLanguage.getPLID();
		Process proc;
		boolean fSuccess=true;
		
		/* Удаляем комментарии и некоторые инструкции */
		deleteCommentsAndOtherStuff();
		
		/* Если предыдущий шаг выполнить не удалось, то итог
		 * выполнения метода convSourceFile2XML - неудача */
		fSuccess=isClearedOfComments();
		
		String path2Grammar=
				programLanguage.getPath2Grammar();
		String errStr;
		String stoutStr;
					 
		try {
			
			/* Запускаем bullwinkle.jar и перенаправляем stdout
			 * в xml-файл */
			out.println(path2Grammar);
			var processBuilder = new ProcessBuilder();
			processBuilder.command("java",
					"-jar",
					"../bin/bullwinkle.jar",
					path2Grammar,
					getPath2FileWOComments());
			var fileXML = new File(getPath2XMLTree());
			processBuilder.redirectOutput(fileXML);
			proc = processBuilder.start();
			proc.waitFor();
			
			/* Определяем, удачно ли произведено преобразование через
			 * подсчет числа строк в полученном xml-файле */
			BufferedReader reader = 
					new BufferedReader(
					new FileReader(getPath2XMLTree()));
			long lines=0;
			while (reader.readLine()!=null) lines++;
			reader.close();
			
			if (lines>5) {
				//out.println("Преобразование прошло удачно");
			} else {
				//out.println("Преобразование прошло неудачно");
				fSuccess=false;
			}

			//out.println("Выполнение программы завершено");
			
		
		} catch (Exception e) { 
			fSuccess=false;
			e.printStackTrace();
		} finally {
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
		
		
		conv2XMLResult=fSuccess;
		/* Для отладки */
		//out.println(fSuccess);
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
	
	/* Для отладки - вывод списка методов на экран */
	public void showMethodsList() {
		
		try {
			File f=new File(getPath2XMLTreeWOFalseTags());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(f);  
			doc.getDocumentElement().normalize();  
		
			NodeList nodeList = doc.getElementsByTagName("Method"); 
			for (int i = 0; i < nodeList.getLength(); i++) {  
				Node node = nodeList.item(i);  
				//System.out.println("\nNode Name :" + node.getNodeName());  
				if (node.getNodeType() == Node.ELEMENT_NODE) {  
					Element eElement = (Element) node;
					out.println("Название метода: "+ eElement.getElementsByTagName("MethodIdentifier").item(0).getTextContent().trim());  
				}  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

