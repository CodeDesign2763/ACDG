/*
 * Класс Repo
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
import java.util.ArrayList;

enum ProgramLanguage {
	JAVA,
	CSHARP,
	CPP
}

/**
 * Класс RelationRepo, реализующий шаблонированный интефейс 
 * Repository
 */
class Model {
	public Repository<Relation> relations;
	public Repository<FileWithClass> filesWithClasses;
	private ProgramLanguage programLanguage;
	
	public static ArrayList<CProgramLanguage> PLs;
	static {
		PLs=new ArrayList<CProgramLanguage>();
		PLs.add((int) ProgramLanguage.JAVA.ordinal(),
				new CProgramLanguage("Java",true,"java.bnf"));
		PLs.add((int) ProgramLanguage.CSHARP.ordinal(),
				new CProgramLanguage("C#",false,"cs.bnf"));
		PLs.add((int) ProgramLanguage.CPP.ordinal(),
				new CProgramLanguage("C++",false,"cpp.bnf"));
	}
	
	public Model(ProgramLanguage pl) {
		programLanguage=pl;
		relations=new Repository<Relation>();
		filesWithClasses=new Repository<FileWithClass>();
	}
	
	public static void deleteCommentsAndOtherStuff(String path2File) {
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
		
		/* Для отладки */
		ProgramLanguage programLanguage=ProgramLanguage.JAVA;
		
		/* Режим комментариев С */
		boolean fCComment=false;
		boolean fDontWriteLine=false;
		
		//Pattern pattern1, pattern2;
		//pattern1=Pattern.compile("(^0$)|(^\\-?[1-9]\\d*$)");
		//pattern2=Pattern.compile("^[0-1]+$");
		//Matcher matcher;
		//int i=0;
		//Читаем текстовый файл по строчкам
		
		//Адрес в момент КОМПИЛЯЦИИ
		File f=new File(path2File);
		Scanner scanfile;
		try {
			scanfile=new Scanner(f);
			String s;
			FileWriter writer = new FileWriter("output.txt",false);
			//java.lang.System.out.println("Строка файла \t Рез. поиска");
			while (scanfile.hasNext())
			{
				s=scanfile.nextLine();
				out.println("Строка из файла "+s);
				matcherImport = patternImport.matcher(s);
				matcherPackage = patternPackage.matcher(s);
				matcherCPPComment = patternCPPComment.matcher(s);
				matcherCCommentOpen = patternCCommentOpen.matcher(s);
				matcherCCommentClose = patternCCommentClose.matcher(s);
				
				fDontWriteLine = false;
				/* Если включен режим комментариев С или
				 * язык - Java и строчка начинается с package или
				 * import - вообще ее не записывать в выходной файл */
				if ((programLanguage == ProgramLanguage.JAVA) &&
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
					out.println("RR" +s);
				}
				
				matcherCCommentOpen.reset();
				matcherCCommentClose.reset();
				
				if ((matcherCCommentOpen.find()) && 
						(matcherCCommentClose.find())) {
					if (s.indexOf("*/") + 2 < s.length()) {
						s=s.substring(0,s.indexOf("/*")) + 
								s.substring(s.indexOf("*/")+2);
					} else s=s.substring(0,s.indexOf("/*"));
					out.println("CR");
				}
				
				
				if ((!fDontWriteLine) && (!s.equals("")) && (!fCComment)) {
					writer.write(s+"\n");
					out.println(s);
				}
				
				//if (i<=6)
				//{
					//matcher=pattern1.matcher(s);
					//if (i==1) 
						//{Writer.write("Тестовые примеры для 1 задания\n");
						//out.println("Тестовые примеры для 1 задания");}
				//}
				//else 
				//{
					//matcher=pattern2.matcher(s);
					//if (i==7) 
						//{
						//Writer.write("Тестовые примеры для 2 задания\n");
						//out.println("Тестовые примеры для 2 задания");
						//}
				//}
				
				/* Отладка */
				//s= matcherCCommentOpen.find() ? s+" C Open" : s;
				//s= matcherCPPComment.find() ? s+" CPP" : s;
				//s= matcherCCommentClose.find() ? s+" C Close" : s;
				//s= matcherImport.find() ? s+" IMPORT" : s;
				//out.println(s);
				
				
				
				////Если есть совпадение - выводим выделенное число
				//if (matcher.find())
					//{
					//out.print(matcher.group());
					//Writer.write(s+","+matcher.group()+"\n");
					//}
				//else {Writer.write(s+",\n");}
				//out.println();
				
			}
			scanfile.close();
			writer.close();
			//Ловушка для исключения, выбрасываемого
			//  при отсутствии файла (обязательна)
		}	catch (IOException ex1) {
				ex1.printStackTrace();
		}		
		}
	
	public static void main(String args[]) {
		deleteCommentsAndOtherStuff("../bin/ACDG/xxx.txt");
	}
		
}
