/*
 * Класс Relation
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

enum RelationCode{
  
    INHERITANCE,
    REALIZATION,
    DEPENDENCY,
    AGREGATION,
    COMPOSITION,
    ASSOCIATION
    
}

/**
 * Класс предметной области
 * описывающий отношение между классами на диаграмме классов
 */
class Relation {
	private int class1Ind;
	private int class2Ind;
	private RelationCode relationCode;
	
	public Relation(int c1i, int c2i, RelationCode rcode) {
		class1Ind=c1i;
		class2Ind=c2i;
		relationCode=rcode;
	}
	
	public int getClass1Ind() {
		return class1Ind;
	}
	
	public int getClass2Ind() {
		return class2Ind;
	}
	
	public RelationCode getRelationCode() {
		return relationCode;
	}
	
	/* Генерация словесного описания отношения */
	public String conv2String() {
		String relName="";
		
		/* Трасформируем relationCode в словесное описание */
		switch (relationCode) {
		case INHERITANCE:
			relName="наследует";
			break;
			
		case REALIZATION: 
			relName="реализует";
			break;
			
		case DEPENDENCY:
			relName="зависит";
			break;
		
		case AGREGATION:
			relName="содержит (агр.)";
			break;
		
		case COMPOSITION:
			relName="содержит (комп.)";
			break;
		
		case ASSOCIATION:
			relName="ссылается";
		}
		
		return String.valueOf(class1Ind) + " " + relName + " " + 
				String.valueOf(class2Ind);
	}
	
	/* Переопределяем метод от класса Object,
	 * который используется для сравнения объектов ArrayList */
	@Override
	public boolean equals(Object o) {
		boolean result=true;
		Relation r = (Relation) o;
		if (class1Ind != r.getClass1Ind()) result=false;
		if (class2Ind != r.getClass2Ind()) result=false;
		if (relationCode != r.getRelationCode()) result=false;
		return result;
	}
}

