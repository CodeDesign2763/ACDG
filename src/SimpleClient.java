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

package ACDG;
import static java.lang.System.out;
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.util.ArrayList;

enum CLIProcMode {INITIAL,CHECK_PATH, ADD_FWC, ADD_CLASS, 
		ADD_ALL_FWC, SET_PROJ_NAME, SET_PL, SET_EXCL}


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
	
	
	
	public SimpleClient(String args[]) {
		cliArgs=args;
		paths2FilesWithClasses = new ArrayList<String>();
		classNames = new ArrayList<String>();
		path2Java="";
		programLanguage=null;
		excludePattern="";
		cliArgsProc();
		checkDataFromCLIArgs();
		model = new Model(programLanguage, projectName, path2Java);
		model.addACDGEventListener(this);
		
		for (String s : paths2FilesWithClasses) {
			model.addFileWithClass(s);
		}
		
		for (String s : classNames) {
			model.addClass(s);
		}
		
		model.genFinalPlantUMLFile();
		model.genClassDiagr();
				
		
		
	}
	
	
	
	
	public void cliArgsProc() {
		CLIProcMode mode = CLIProcMode.INITIAL;
		for (String s : cliArgs) {
			switch (s) {
				case "-path2java" : 
					mode=CLIProcMode.CHECK_PATH;
					break;
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
								out.println("Некорректное название"
										+ " языка программирования");
								System.exit(1);
							}
							
						case CHECK_PATH :
							if (checkPath2Java(s)) {
								out.println("Путь к Java корректный");
								path2Java=s;
							} else {
								out.println(
										"Путь к Java некорректный");
								System.exit(1);
							}
							break;
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
								out.println("Ошибка аргументов "
										+ "командной строки");
								System.exit(1);
							}
						case SET_PROJ_NAME :
							projectName = s;
							break;
						case SET_EXCL :
							excludePattern = s;
							break;
					}
			}
		}
		
		/* Для отладки */
		out.println("path2Java=" + path2Java);
		out.println("FWC:");
		for (String s : paths2FilesWithClasses) {
			out.println(s);
		}
		
		out.println("Classes:");
		for (String s : classNames) {
			out.println(s);
		}
		out.println("ProjectName=" + projectName);
		out.println("PL=" + programLanguage.getPLName());
		
	}
	
	private void checkDataFromCLIArgs() {
		if ((path2Java=="")
				|| (paths2FilesWithClasses.size()==0)
				|| (programLanguage == null)) {
			out.println("Неверные данные из командной строки");
			System.exit(1);
		}
	}
	
	@Override
	public void onACDGEventReceived(ACDGEvent event) {
		if (event.getEventType() == ACDGEventType.TEXTMESSAGE)
			out.println(((String) event.getPayload()));
	}
	
	private  ArrayList<String> getAllFilesFromDir(String path, 
			String ext) {
		ArrayList<String> resultList = new ArrayList<String>();
		File myFolder = new File(path);
		File[] files = myFolder.listFiles();
		for (File f : files) {
			String s=f.getName();
			if (s.indexOf("."+ext)>-1) {
				resultList.add(s);
			}
		}
		return resultList;
	}
	
	private  File NULL_FILE = new File(
          (System.getProperty("os.name")
                     .startsWith("Windows") ? "NUL" : "/dev/null"));

	private  boolean checkPath2Java(String path) {
		boolean fResult=true;
		Process proc;
		try {
			
			var processBuilder = new ProcessBuilder();
			processBuilder.command(path);
			processBuilder.redirectOutput(NULL_FILE);
			proc = processBuilder.start();
			proc.waitFor();
		
		} catch (Exception e) { 
			//e.printStackTrace();
			fResult=false;
		} 
		return fResult;
	}
	
		
}
