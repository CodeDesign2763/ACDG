/*
 * Интерфейс IProcStrategy
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
import java.util.ArrayList;


/**
 * Интерфейс для стратегия распознавания xml-дерева
 */
interface IProcStrategy {
	/* Обратная подстановка для символов "<", ">", "&" */
	default public String reverseSubst(String s) {
		String q=s;
		q=q.replaceAll("zzzzz","<");
		q=q.replaceAll("xxxxx",">");
		q=q.replaceAll("wwwww","&");
		return q;
	}
	
	/* Распознавание отношений из типов данных */
	default public void dataTypeRelProc(String cl1Name,
			String dataType, ModelRelationIface modelRelIFace,
			ModelScannerIface modelScanIFace,
			RelationCode relCode, String stereotype) {
		String dataType1;
		String dataType2;
		if (dataType.indexOf("<")>-1) {
			dataType1 = 
					dataType.substring(0,
					dataType.indexOf("<")).trim();
			dataType2 = 
					dataType.substring(dataType.indexOf("<")
					+ 1,dataType.indexOf(">")).trim();
			//try {
			if (modelRelIFace.getClassInd(dataType1)>-1) {
				modelScanIFace.addRelation(
						new Relation(cl1Name, dataType1,
						relCode, stereotype,
						modelRelIFace));
			} //} catch (Exception e) {out.println("XXX:"+dataType +"\n"+dataType1+"|"+dataType2); out.println(modelRelIFace.getClassInd(dataType1));}
			if (modelRelIFace.getClassInd(dataType2)>-1) {
				modelScanIFace.addRelation(
						new Relation(cl1Name, dataType2,
						relCode, stereotype,
						modelRelIFace));
			}
		} else {
			if (modelRelIFace.getClassInd(dataType)>-1) {
				modelScanIFace.addRelation(
						new Relation(cl1Name, dataType,
						relCode, stereotype,
						modelRelIFace));
			}
		}
		
	}
	
	/* Метод для распознавания XML-дерева */
	public ClassDescr readXMLFile(String path2File, 
			ModelScannerIface model,
			ModelRelationIface modelRelIface)
			throws Exception;
}

