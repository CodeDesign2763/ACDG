/*
 * Класс SimpleClient
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
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Scanner;

enum CLIProcMode {INITIAL,CHECK_PATH, ADD_FWC, ADD_CLASS, 
		ADD_ALL_FWC, SET_PROJ_NAME, SET_PL, SET_EXCL, SET_PPM,
		SETUP_USER_PL_GRAMMAR, SETUP_USER_PL_STRATEGY,
		SETUP_USER_PL_EXT};


/**
 * Класс предназначен для реализации простейшего консольного клиента
 */
class SimpleClient implements ACDGEventListener {
	
	private Model model;
	
	private String[] cliArgs;
	
	private boolean failed=false;
	
	private ArrayList<String> paths2FilesWithClasses;
	private ArrayList<String> classNames;
	
	private String path2Java;
	
	private String projectName;
	private CProgramLanguage programLanguage;
	private String excludePattern;
	
	private ParamProcMode paramProcMode;
	
	private boolean fUseSmetana;
	private boolean fSimpleClassDescr=false;
	
	/* Running under Windows? */
	private static boolean fWindows=System.getProperty("os.name")
			.startsWith("Windows");
	
	public SimpleClient(String args[]) {
		/* Constant for self-test */
		final String[] selfTestCLIArgs = {"-setppm", "ONLY_DATATYPE", 
				"-exclude", "Test", "-setpl", "Java", 
				"-allfwcfromdir", "../src/main/java/com/acdg", 
				"-setprojname", "ClassDiagramOfItsOwnCode", 
				//"-addclasses", "ClassA"
				"--use-smetana"
				};
		final String[] selfTestSDCLIArgs = {"-setppm", "ONLY_DATATYPE", 
				"-exclude", "Test", "-setpl", "Java", 
				"-allfwcfromdir", "../src/main/java/com/acdg", 
				"-setprojname", "ClassDiagramOfItsOwnCode", 
				//"-addclasses", "ClassA"
				"--use-smetana", "--simple-class-descr"
				};
			
		// java -jar ACDG.jar -setppm ALL -exclude Test -setpl Java 
		// -allfwcfromdir path 2 dir -setprojname 
		// ClassDiagramOfItsOwnCode -addclasses ClassA
		
		String osMessage;
		osMessage = (fWindows) ? "OS is Windows-like" :
				"OS is Unix-like";
		out.println(osMessage);
		
		/* Default project name */
		projectName="DefaultProjectName";
		
		/* Path to Java test */
		path2Java="java";
		if (checkPath2Java(path2Java)) {
			out.println("Path to Java is correct");
		} else {
			out.println("Path to Java is incorrect");
			System.exit(1);
		}
		
		/* Self-test check */
		if (args[0].equals("-test")) {
			cliArgs=selfTestCLIArgs;
			out.println("Self-test mode");
			out.println("CLI arguments have been replaced by:");
			out.println("-setppm ALL -exclude Test -setpl Java"  
					+ " -allfwcfromdir ../src/main/java/com/acdg"
					+ " -setprojname ClassDiagramOfItsOwnCode" 
					+ " --use-smetana");
		} else if (args[0].equals("-test-simple-class-descr")) {
			cliArgs=selfTestSDCLIArgs;
			out.println("Self-test mode");
			out.println("CLI arguments have been replaced by:");
			out.println("-setppm ALL -exclude Test -setpl Java"  
					+ " -allfwcfromdir ../src/main/java/com/acdg"
					+ " -setprojname ClassDiagramOfItsOwnCode" 
					+ " --use-smetana" + " --simple-class-descr");
		} else
			cliArgs=args;
		
		/* Version information */
		if (args[0].equals("-version")) {
			out.println("Version 1.0.0");
			System.exit(0);
		}
			
		paths2FilesWithClasses = new ArrayList<String>();
		classNames = new ArrayList<String>();
		
		programLanguage=null;
		excludePattern="";
		paramProcMode=ParamProcMode.ALL;
		fUseSmetana=false;
		fSimpleClassDescr=false;
		
		cliArgsProc();
		
		checkDataFromCLIArgs();
		
		model = new Model(programLanguage, projectName, path2Java, 
				paramProcMode, fUseSmetana, fSimpleClassDescr);
		model.addACDGEventListener(this);
		
		for (String s : paths2FilesWithClasses) {
			model.addFileWithClass(s);
		}
		
		for (String s : classNames) {
			model.addClass(s);
		}
		
		model.genFinalPlantUMLFile(fSimpleClassDescr);
		model.genClassDiagr();
		
		out.println("File with class diagram PlantUml code"
				+ " generated:");
		out.println(model.getPath2FinalPlantUMLFile());
		out.println("Class diagram generated:");
		out.println(model.getPath2FinalPlantUMLDiagram());
	}
	
	/* Command line arguments processing */
	public void cliArgsProc() {
		String path2Grammar="";
		String strategyPLName="Java";
		String extension4UserDefPL="txt";
		CLIProcMode mode = CLIProcMode.INITIAL;
		String s;
		for (int i=0; i<cliArgs.length; i++) {
			s=cliArgs[i];
			switch (s) {
				//case "-path2java" : 
					//mode=CLIProcMode.CHECK_PATH;
					//break;
				case "-addfwc" : 
					mode=CLIProcMode.ADD_FWC;
					break;
				case "-addclasses" : 
					mode=CLIProcMode.ADD_CLASS;
					break;
				case "-setprojname" :
					mode=CLIProcMode.SET_PROJ_NAME;
					break;
				case "-allfwcfromdir" : 
					mode=CLIProcMode.ADD_ALL_FWC;
					break;
				case "-setpl" :
					mode=CLIProcMode.SET_PL;
					break;
				case "-exclude" :
					mode=CLIProcMode.SET_EXCL;
					break;
				case "-setppm" :
					mode=CLIProcMode.SET_PPM;
					break;
				case "-help" : 
					/* Вывести подсказку */
					try {
						File f=new File("../data/README.TXT");
						Scanner scanfile;
			
						scanfile=new Scanner(f);
						String str;
						while (scanfile.hasNext()) {
							str=scanfile.nextLine();
							out.println(str);
						}
					} catch (Exception e) {
						out.println("Error! README file not found!");
					}
					System.exit(1);
					break;
				
				case "--use-smetana" :
					fUseSmetana=true;
					break;
					
				case "--user-def-pl-grammar" :
					mode=CLIProcMode.SETUP_USER_PL_GRAMMAR;
					break;
				case "--user-def-pl-strategy" :
					mode=CLIProcMode.SETUP_USER_PL_GRAMMAR;
					break;
				case "--user-def-pl-ext" :
					mode=CLIProcMode.SETUP_USER_PL_EXT;
					break;

				case "--user-def-pl" :
					AvailablePLs.addPL(
						ProgramLanguage.USER_DEFINED
							.ordinal(),
						new CProgramLanguage(
							"USER_DEFINED", true, path2Grammar, 
							ProgramLanguage.USER_DEFINED,
							AvailablePLs.getPLbyName(strategyPLName)
							.getProcStrategy(),
							extension4UserDefPL, 
							new BypassCodePreprocStrategy()));
					programLanguage=AvailablePLs.getPLbyName(
						"USER_DEFINED");
				
				case "--simple-class-descr" : 
						fSimpleClassDescr=true;
					break;
					
				default : 
					switch (mode) {
						case SET_PL :
							if (AvailablePLs.getPLbyName(s)!=null) {
								programLanguage = 
										(AvailablePLs.getPLbyName(s)
										.isSupported()) ? 
										AvailablePLs.getPLbyName(s) :
										null;
								break;
							} else {
								out.println("Error! Incorrect PL name"
										);
								System.exit(1);
							}
							
						//case CHECK_PATH :
							//if (checkPath2Java(s)) {
								//out.println("Путь к Java корректный");
								//path2Java=s;
							//} else {
								//out.println(
										//"Путь к Java некорректный");
								//System.exit(1);
							//}
							//break;
						case ADD_FWC : 
							paths2FilesWithClasses.add(s);
							break;
						case ADD_CLASS : 
							classNames.add(s);
							break;
						case ADD_ALL_FWC :
							if (programLanguage!=null) {
								for (String str : 
										getAllFilesFromDir(s,
										programLanguage.getExt())) 
										{
								if (!((str.indexOf(excludePattern)>-1) 
										& 
										(!excludePattern.equals(""))))
									paths2FilesWithClasses.add(str);
							}
							break;
							} else {
								out.println("Command line arguments"
										+ " error");
								System.exit(1);
							}
						case SET_PROJ_NAME :
							projectName = s;
							break;
						case SET_EXCL :
							excludePattern = s;
							break;
						case SET_PPM :
							switch (s) {
								case "ONLY_DATATYPE":
									paramProcMode=
											ParamProcMode
											.ONLY_DATATYPE;
									break;
								case "ONLY_ID":
									paramProcMode=
											ParamProcMode
											.ONLY_ID;
									break;
								case "ALL":
									paramProcMode=
											ParamProcMode
											.ALL;
									break;
								default :
									out.println(
											"Error! Incorrect"
											+ " parameters "
											+ " processing mode");
									System.exit(1);
							}
						case SETUP_USER_PL_GRAMMAR :
							path2Grammar=s;
							break;
						case SETUP_USER_PL_STRATEGY :
							strategyPLName=s;
							break;
						case SETUP_USER_PL_EXT :
							extension4UserDefPL=s;
						break;
						
							
						
						// МЕНЯТЬ ЗДЕСЬ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
							//AvailablePLs.addPL(
								//ProgramLanguage.USER_DEFINED
									//.ordinal(),
								//new CProgramLanguage(
								//"USER_DEFINED", true, s, 
								//ProgramLanguage.USER_DEFINED,
								
								
								
					}
			}
		}
		
		/* Для отладки */
		//out.println("path2Java=" + path2Java);
		//out.println("FWC:");
		//for (String s : paths2FilesWithClasses) {
			//out.println(s);
		//}
		
		//out.println("Classes:");
		//for (String s : classNames) {
			//out.println(s);
		//}
		//out.println("ProjectName=" + projectName);
		//out.println("PL=" + programLanguage.getPLName());
	}
	
	/* Проверка достаточности данных, 
	 * полученных из командной строки */
	private void checkDataFromCLIArgs() {
		//if ((path2Java=="")
				//|| (paths2FilesWithClasses.size()==0)
				//|| (programLanguage == null)) {
		if ((paths2FilesWithClasses.size()==0)
				|| (programLanguage == null)) {
			out.println("Command line arguments error:"
					+ " need more data");
			System.exit(1);
		}
	}
	
	@Override
	public void onACDGEventReceived(ACDGEvent event) {
		if (event.getEventType() == ACDGEventType.TEXTMESSAGE)
			out.println(((String) event.getPayload()));
	}
	
	/* Получить список путей ко всем файлам заданного расширения из
	 * заданного каталога */
	private  ArrayList<String> getAllFilesFromDir(String path, 
			String ext) {
		
		ArrayList<String> resultList = new ArrayList<String>();
		try {
			File myFolder = new File(path);
			File[] files = myFolder.listFiles();
			for (File f : files) {
				String s=f.getCanonicalPath();
				if (s.indexOf("."+ext)>-1) {
					resultList.add(s);
				}
			}
		} catch (Exception e) {};
		return resultList;
	}
	
	/* Проверка пути к Java */
	private  boolean checkPath2Java(String path) {
		boolean fResult=true;
		Process proc;
		try {
			
			File NULL_F = new File(
			(System.getProperty("os.name")
                     .startsWith("Windows") ? "NUL" : "/dev/null"));
			var processBuilder = new ProcessBuilder();
			processBuilder.command(path);
			processBuilder.redirectOutput(NULL_F);
			processBuilder.redirectError(NULL_F);
			proc = processBuilder.start();
			proc.waitFor();
		
		} catch (Exception e) { 
			//e.printStackTrace();
			fResult=false;
		} 
		return fResult;
	}
}
