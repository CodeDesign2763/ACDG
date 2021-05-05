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

/**
 * Класс предметной области
 * описывающий файл с описанием класса на языке Java
 */
 
class FileWithClass {
	private String fileName;
	private boolean convResult;
	private boolean fClearedOfComments;
	
	public FileWithClass(String fn) {
		fileName=fn;
		convResult=false;
		fClearedOfComments=false;
	}
	
	public void setConvResult(boolean result) {
		convResult=result;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public boolean getConvResult() {
		return convResult;
	}
	
	public String getClassName() {
		int dotIndex;
		String res="ERROR!";
		dotIndex=fileName.indexOf(".");
		if (dotIndex!=-1) {
			res=fileName.substring(0,dotIndex);
		}
		return res;
	}
}

