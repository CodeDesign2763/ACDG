/*
 * Класс JavaProcStrategy
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
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;  
import java.io.File;

/**
 * Стратегия распознавания xml-дерева для языка Java
 */
class JavaProcStrategy implements IProcStrategy {
	@Override
	public ClassDescr readXMLFile(String path2File, 
			ModelScannerIface model,
			ModelRelationIface modelRelIFace,
			ParamProcMode paramProcMode) throws Exception {
		
		NodeList nodeList;
		
		/* Описание класса */
		boolean fClass=true;
		String classID;
		
		/* Описание элемента класса */
		String elemName="";
		String elemDataType="";
		boolean fStatic=false;
		boolean fDefault=false;
		ACCESS_MODIFIERS am=ACCESS_MODIFIERS.PRIVATE;
		boolean fSub;
		String parameters="";
		String paramDataType;
		String paramID;
		
		RelationCode relCode;		
		String class2Name;
				
		ClassDescr classDescr;
		
		/* Первая задача - вывести все на печать */
		
		//try {
		File f=new File(path2File);
		DocumentBuilderFactory dbf = 
				DocumentBuilderFactory.newInstance();  
		DocumentBuilder db = dbf.newDocumentBuilder();  
		Document doc = db.parse(f);  
		doc.getDocumentElement().normalize();  
		
		/* Класс или интерфейс */
		nodeList = doc.getElementsByTagName("ClassType"); 
		if (nodeList.item(0).getTextContent().trim()
				.equals("class")) {
			fClass=true; } else {
			fClass=false;
		}
			
		/* Название класса */
		nodeList = 
				doc
				.getElementsByTagName(
				"ClassIdentifierWithInheritanceBlocks"); 
		classID = ((Element) nodeList.item(0))
				.getElementsByTagName(
				"Identifier").item(0).getTextContent().trim();
		classID = reverseSubst(classID);
		classID = classID.replaceAll("\\<.*\\>$","");
			
		classDescr= new ClassDescr(fClass, classID);
			
		/* [Отношения] Наследование / реализация */
		if (doc.getElementsByTagName("InheritanceBlock")
				.getLength()>0) {
				
			nodeList = doc
					.getElementsByTagName(
					"InheritanceBlock"); 
			for (int i=0; i < nodeList.getLength(); i++) {
				/* Определяем тип отношения */
				String inhModif = 
						( (Element) nodeList.item(i))
						.getElementsByTagName(
						"InheritanceModifiers")
						.item(0)
						.getTextContent()
						.trim();
				relCode = inhModif.equals("extends") ? 
						RelationCode.INHERITANCE : 
						RelationCode.REALIZATION;
						
				NodeList classNameList = 
						( (Element) nodeList.item(i))
						.getElementsByTagName(
						"Identifier");
				for (int k=0; k< classNameList.getLength(); k++ ) 
						{
					class2Name = 
							( (Element) classNameList.item(k))
							.getTextContent().trim();
					class2Name = reverseSubst(class2Name);
					class2Name = class2Name.replaceAll("\\<.*\\>$","");
					/* FIX ME
					 * Perhaps, if there is no such class, 
					 * then neither this class 
					 * nor the relation should be added?
					 * 
					 * It is also possible to add an 
					 * additional command line option
					 */
					 
					//if (modelRelIFace.getClassInd(class2Name)==-1) {
						//model.addClass(class2Name);
					//}
					//model.addRelation(new Relation(classID, 
							//class2Name,
							//relCode, "", modelRelIFace));
					 
					if (modelRelIFace.getClassInd(class2Name)!=-1) {
						model.addRelation(new Relation(classID, 
							class2Name,
							relCode, "", modelRelIFace));
					}
				}
				
			}
				
		}
			
		/* Поля */
			
		fSub=false;
		parameters="";
			
		nodeList = 
				doc
				.getElementsByTagName(
				"Field"); 
			
		for (int i = 0; i < nodeList.getLength(); i++) {  
			Node node = nodeList.item(i);  
			if (node.getNodeType() == Node.ELEMENT_NODE) {  
				Element elem = (Element) node;
					
				/* Определяем название  поля */
				elemName=elem
						.getElementsByTagName("FieldIdentifier")
						.item(0)
						.getTextContent()
						.trim();
				
				/* Определяем тип поля */
				
				elemDataType=elem
						.getElementsByTagName("FieldDataType")
						.item(0)
						.getTextContent()
						.trim();
				elemDataType = reverseSubst(elemDataType);
				
				/* [Отношения] Ассоциация */
				dataTypeRelProc(classID, elemDataType,
						modelRelIFace, model, 
						RelationCode.ASSOCIATION, "");
				
				/* Изучаем модификаторы */
				fStatic=false;
				fDefault=false;
				NodeList modifList = elem
						.getElementsByTagName("Modifier");
				for (int k=0; k < modifList.getLength(); k++) {
					Node modif = modifList.item(k);
					if (modif.getNodeType() == Node.ELEMENT_NODE) 
							{
						String value = ((Element) modif)
								.getTextContent().trim();
								
						switch (value) {
							case "private":
								am = ACCESS_MODIFIERS.PRIVATE;
								break;
							case "protected":
								am = ACCESS_MODIFIERS.PROTECTED;
								break;
							case "public":
								am = ACCESS_MODIFIERS.PUBLIC;
								break;
							case "static":
								fStatic = true;
								break;
							case "default":
								fDefault = true;
								break;
							//default:
								//throw 
										//new 
										//Exception(
										//"Неверный модификатор");
						}
					}
				}
			}
			classDescr.addClassElement(new ClassElement(
					elemName, elemDataType, am, fStatic, fSub,
					parameters,fDefault));
		}
			
		/* Методы */
			
		fSub=true;
		parameters="";
		
		nodeList = 
				doc
				.getElementsByTagName(
				"Method"); 
			
			
		for (int i = 0; i < nodeList.getLength(); i++) {  
			Node node = nodeList.item(i);  
			if (node.getNodeType() == Node.ELEMENT_NODE) {  
				Element elem = (Element) node;
				
				/* Определяем название  метода */
				elemName=elem
						.getElementsByTagName("MethodIdentifier")
						.item(0)
						.getTextContent()
						.trim();
				
				/* Определяем тип метода */
				
				elemDataType="";
				
				if (elem.getElementsByTagName("MethodDataType")
						.getLength()>0) {
					elemDataType=elem
							.getElementsByTagName(
							"MethodDataType")
							.item(0)
							.getTextContent()
							.trim();
				}
				elemDataType = reverseSubst(elemDataType);
				
				/* [Отношения] Зависимость "Создание" */
				dataTypeRelProc(classID, elemDataType,
						modelRelIFace, model, 
						RelationCode.DEPENDENCY, "Create");
				
				
				/* Изучаем модификаторы */
				fStatic=false;
				fDefault=false;
				NodeList modifList = elem
						.getElementsByTagName("Modifier");
				for (int k=0; k < modifList.getLength(); k++) {
					Node modif = modifList.item(k);
					if (modif.getNodeType() == Node.ELEMENT_NODE) 
							{
						String value = ((Element) modif)
								.getTextContent().trim();
								
						switch (value) {
							case "private":
								am = ACCESS_MODIFIERS.PRIVATE;
								break;
							case "protected":
								am = ACCESS_MODIFIERS.PROTECTED;
								break;
							case "public":
								am = ACCESS_MODIFIERS.PUBLIC;
								break;
							case "static":
								fStatic = true;
								break;
							case "default":
								fDefault = true;
								break;
							
							//default:
								//throw 
										//new 
										//Exception(
										//"Неверный модификатор");
						}
					}
				}
					
				/* Список параметров со скобками */
				parameters="";
				/* Если параметров нет - останется значение "" */
				NodeList paramList = elem
						.getElementsByTagName("Parameter");
				for (int k=0; k < paramList.getLength(); k++) {
					Node param = paramList.item(k);
					if (param.getNodeType() == Node.ELEMENT_NODE)
							{
						/* Определяем название  параметра */
						paramID=((Element) param)
								.getElementsByTagName(
								"ParameterIdentifier")
								.item(0)
								.getTextContent()
								.trim();
							
						/* Определяем тип данных параметра */
						paramDataType=((Element) param)
								.getElementsByTagName(
								"ParameterDataType")
								.item(0)
								.getTextContent()
								.trim();
						paramDataType = 
								reverseSubst(paramDataType);
							
						if (k>0) parameters += ", ";
							
						switch (paramProcMode) {
							case ONLY_DATATYPE:
								parameters += paramDataType;
								break;
							case ONLY_ID:
								parameters += paramID;
								break;
							case ALL:
								parameters += paramID + ":" 
										+ paramDataType;
						}
						
						/* [Отношения] Зависимость "derive" */
						dataTypeRelProc(classID, paramDataType,
						modelRelIFace, model, 
						RelationCode.DEPENDENCY, "Derive");
					}
				}
				parameters = "(" + parameters + ")";
			}  
			classDescr.addClassElement(new ClassElement(
					elemName, elemDataType, am, fStatic, fSub,
					parameters,fDefault));
		}
		
		return classDescr;
	}
	
	
}

