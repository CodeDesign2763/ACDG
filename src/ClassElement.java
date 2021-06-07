/*
 * Класс ClassElement
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

enum ACCESS_MODIFIERS {
	PRIVATE,
	PUBLIC,
	PROTECTED
}
/**
 * Класс ClassElement, описывающий метод или поле класса
 * с точки зрения PlantUML 
 * Repository
 */
class ClassElement {
	private String elemID;
	private String elemType;
	private ACCESS_MODIFIERS accModif;
	private boolean fStatic;
	private boolean fDefault;
	private boolean fSub;
	
	/* Параметры вместе со скобками */
	private String parameters;
	
	public ClassElement(String eID, String eType, ACCESS_MODIFIERS
			aM, boolean isStatic, boolean isSub, String p,
			boolean fDflt) {
		elemID=eID;
		elemType=eType;
		accModif=aM;
		fStatic=isStatic;
		fSub=isSub;
		parameters=p;
		fDefault=fDflt;
	}
	
	public String genPlantUMLCode() {
		String res="";
		switch (accModif) {
			case PRIVATE: 
				res="-";
				break;
			case PUBLIC: 
				res="+";
				break;
			case PROTECTED:
				res="#";
		}
		
		res=res+" ";
		
		if (fStatic) res=res+"{static} ";
		if (fDefault) res=res+"<<default>> ";
		
		res=res+elemID;
		
		if (fSub) res=res+parameters;
		
		if (!elemType.equals(""))
			res=res+":"+elemType;
		
		return res;
	}	
	
	
}

