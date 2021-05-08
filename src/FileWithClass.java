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

/**
 * Класс предметной области
 * описывающий файл с описанием класса на языке Java
 */
 
class FileWithClass {
	private String path2File;
	private String fileName;
	private boolean convResult;
	
	private boolean fClearedOfComments;
	
	public FileWithClass(String fn) {
		path2File=fn;
		convResult=false;
		
		fClearedOfComments=false;
	}
	
	public boolean isClearedOfComments() {
		return fClearedOfComments;
	}
	
	//public void setConvResult(boolean result) {
		//convResult=result;
	//}
	
	public String getPath2File() {
		return path2File;
	}
	
	public String getFileName() {
		return (path2File.lastIndexOf("/") > -1) ? 
		path2File.substring(path2File.lastIndexOf("/")+1) : path2File;
	}
	
	public boolean getConvResult() {
		return convResult;
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
	
	/* Убирает из исходного файла комментарии и некоторые декларации
	 * и сохраняет результат в файл, определяемый при помощи
	 * getPath2FileWOComments() */
	private void deleteCommentsAndOtherStuff(
			ProgramLanguage progLang) {
		/* Классы для работы с регулярными выражениями */
		/* Экранирование двойным \\ */
		Pattern patternImport = Pattern.compile("^import");
		Pattern patternPackage = Pattern.compile("^package");
		Pattern patternCPPComment = Pattern.compile("\\/\\/");
		Pattern patternCCommentOpen = Pattern.compile("\\/\\*");;
		Pattern patternCCommentClose = Pattern.compile("\\*\\/");;
		Matcher matcherImport;
		Matcher matcherPackage;
		Matcher matcherCPPComment;
		Matcher matcherCCommentOpen;
		Matcher matcherCCommentClose;
		
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
				//out.println("Строка из файла "+s);
				matcherImport = patternImport.matcher(s);
				matcherPackage = patternPackage.matcher(s);
				matcherCPPComment = patternCPPComment.matcher(s);
				matcherCCommentOpen = patternCCommentOpen.matcher(s);
				matcherCCommentClose = 
						patternCCommentClose.matcher(s);
				
				fDontWriteLine = false;
				/* Если включен режим комментариев С или
				 * язык - Java и строчка начинается с package или
				 * import - вообще ее не записывать в выходной файл */
				if ((progLang == ProgramLanguage.JAVA) &&
						((matcherImport.find()) ||
						(matcherPackage.find()) )) {
					fDontWriteLine = true;
				}
				
				if ((!fCComment) && (matcherCPPComment.find())) {
					s=s.substring(0,s.indexOf("//"));
				}
				
				if ((matcherCCommentOpen.find()) && 
						(!matcherCCommentClose.find()) ){
					s=s.substring(0,s.indexOf("/*"));
					fCComment=true;
				}
				
				matcherCCommentOpen.reset();
				matcherCCommentClose.reset();
				
				if ((matcherCCommentClose.find()) && 
						(!matcherCCommentOpen.find())) {
					if (s.indexOf("*/") + 2 < s.length()) {
						s=s.substring(s.indexOf("*/")+2);
					} else s="";
					fCComment=false;
					//out.println("RR" +s);
				}
				
				matcherCCommentOpen.reset();
				matcherCCommentClose.reset();
				
				if ((matcherCCommentOpen.find()) 
						&& (matcherCCommentClose.find())) {
					if (s.indexOf("*/") + 2 < s.length()) {
						s=s.substring(0,s.indexOf("/*")) + 
								s.substring(s.indexOf("*/")+2);
					} else s=s.substring(0,s.indexOf("/*"));
					//out.println("CR");
				}
				
				
				if ((!fDontWriteLine) && (!s.equals(""))
						&& 	(!fCComment)) {
					writer.write(s + "\n");
					//out.println(s);
				}
			}
			scanfile.close();
			writer.close();
			
			//Меняем флаг класса
			fClearedOfComments = true;
		}	catch (IOException ex1) {
				//ex1.printStackTrace();
		}	catch (Exception e) {}
			
	}
	
	
	public String getPath2XMLTree() {
		return getPath2FileWOComments()+"_XML";
	}
	
	public void convSourceFile2XML(ProgramLanguage pl) {
		Process proc;
		boolean fSuccess=true;
		
		deleteCommentsAndOtherStuff(pl);
		
		fSuccess=isClearedOfComments();
		
		String path2Grammar=
				Model.availablePLList.get(
						pl.ordinal()).getPath2Grammar();
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

			out.println("Выполнение программы завершено");
			
		
		} catch (Exception e) { 
			fSuccess=false;
		} finally {
			try {
				/* Удаляем промежуточный файл */
				Files.deleteIfExists(
						Paths.get(getPath2FileWOComments()));
			}	catch (Exception e) {}
		}
		
		convResult=fSuccess;
		/* Для отладки */
		out.println(fSuccess);
		
	

	}
}

