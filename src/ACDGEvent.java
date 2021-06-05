/*
 * Класс ACDGEvent
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

enum ACDGEventType{
  
    TEXTMESSAGE
}

/**
 * Класс для описания универсального события
 */
class ACDGEvent {
	private ACDGEventType eventType;
	private Object payload;
	private Object source;
	
	public ACDGEvent(Object s, ACDGEventType type, Object pload) {
		source=s;
		eventType=type;
		payload=pload;
	}
	
	public Object getSource() {
		return source;
	}
	
	public Object getPayload() {
		return payload;
	}
	
	public ACDGEventType getEventType() {
		return eventType;
	}
}

