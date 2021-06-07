/*
 * Класс CProgramLanguage
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

/**
 * Класс предметной области
 * описывающий поддерживаемый язык программирования
 */
class CProgramLanguage {
	private String plName;
	
	/* Поддерживается ли данный язык */
	private boolean fSupported;
	private String grammarFileName;
	private ProgramLanguage plID;
	private IProcStrategy strategy;
	
	/* Running under Windows? */
	private static boolean fWindows=System.getProperty("os.name")
			.startsWith("Windows");
	
	/* Расширение файла */
	private String extension;
	
	public String getExt() {
		return extension;
	}
	
	public CProgramLanguage(String name, boolean fs, String g, 
			ProgramLanguage pl, IProcStrategy str, String ext) {
		plName=name;
		fSupported=fs;
		grammarFileName=g;
		plID=pl;
		strategy=str;
		extension=ext;
	}
	
	public String getPLName() {
		return plName;
	}
	
	public ProgramLanguage getPLID() {
		return plID;
	}
	
	public boolean isSupported() {
		return fSupported;
	}
	
	public String getPath2Grammar() {
		String res;
		res = ((fWindows) ? "..\\src\\" : "../src/");
		return res+grammarFileName;
	}
	
	public IProcStrategy getProcStrategy() {
		return strategy;
	}
}

