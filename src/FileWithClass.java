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
	
	public void setConvResult(boolean result) {
		convResult=result;
	}
	
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
		
		//Адрес в момент КОМПИЛЯЦИИ
		File f=new File(path2File);
		Scanner scanfile;
		try {
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
			fClearedOfComments = true;
		}	catch (IOException ex1) {
				ex1.printStackTrace();
		}		
	}
	
	public String getPath2XMLTree() {
		return getPath2FileWOComments()+"_XML";
	}
	
	public void convSourceFile2XML(ProgramLanguage pl) {
		Process proc;
		InputStream inputStream;
		InputStream errStream;
		boolean fSuccess=true;
		
		deleteCommentsAndOtherStuff(pl);
		
		fSuccess=(fSuccess && isClearedOfComments());
		
		String grammarFileName=
				Model.PLs.get(pl.ordinal()).getGrammarFileName();
		String errStr;
		String stoutStr;
					 
		try {
			
			//proc = Runtime.getRuntime().exec(
					//"java -jar ../bin/bullwinkle.jar -f xml"
					//+ " ../src/" + grammarFileName + " "
					//+ getPath2FileWOComments()
					//+ " > " + getPath2XMLTree());
			
			proc = Runtime.getRuntime().exec(
					"java -jar ../bin/bullwinkle.jar ../src/java.bnf ../data/source1.txt >> ../qqq.xml");
			
		
			
			/* Получение текстового вывода программы */
			//inputStream = proc.getInputStream();
			//errStream = proc.getErrorStream();
			
			proc.waitFor();
			
			//byte inp[]=new byte[inputStream.available()];
			//inputStream.read(inp,0,inp.length);
			//stoutStr=new String(inp);
			//out.println("Вывод STDIN:\n " + stoutStr);

			//byte err[]=new byte[errStream.available()];
			//errStream.read(err,0,err.length);
			//errStr=new String(err);
			//out.println("Вывод STDERR:\n" + errStr);
			
			//if ((!errStr.equals("")) 
					//|| (stoutStr.indexOf("rror") == -1)) {
				//fSuccess=false;
			//}
			
			out.println("Выполнение программы завершено");
		} catch (Exception e) { 
			fSuccess=false;
		};
		
		convResult=fSuccess;

	}
}

