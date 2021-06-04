/*
 * Класс AvailablePLs
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


/**
 * Доступные языки программирования
 */
class AvailablePLs {
	
	private static ArrayList<CProgramLanguage> availablePLList;
	
	static {
		availablePLList=new ArrayList<CProgramLanguage>();
		JavaProcStrategy javaProcStrategy = new JavaProcStrategy();
		
		availablePLList.add((int) ProgramLanguage.JAVA.ordinal(),
				new CProgramLanguage("Java",true,"java.bnf",
				ProgramLanguage.JAVA,javaProcStrategy, "java"));
		availablePLList.add((int) ProgramLanguage.CSHARP.ordinal(),
				new CProgramLanguage("C#",false,"cs.bnf",
				ProgramLanguage.CSHARP,null,"cs"));
		availablePLList.add((int) ProgramLanguage.CPP.ordinal(),
				new CProgramLanguage("C++",false,"cpp.bnf",
				ProgramLanguage.CPP,null,"cpp"));
	}
	
	public static CProgramLanguage getPLbyEnum(ProgramLanguage pl) {
		return availablePLList.get(pl.ordinal());
	}
	
	public static CProgramLanguage getPLbyName(String name) {
		CProgramLanguage res=null;
		for (CProgramLanguage language : availablePLList) {
			if (language.getPLName().equals(name)) {
				res=language;
				break;
			}
		}
		return res;
	}
		
}
