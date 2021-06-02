/*
 * Интерфейс Repository
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
 * Шаблонированный интерфейс, реализующий паттерн "Репозиторий"
 * с операциями add, get, update, delete
 * и одновременно шаблон "Адаптер"
 */
interface IRepository<T> {
	
	public void add(T entity);
	
	public T get(int index);
	
	public void update(int index, T entity);
	
	public void delete(int index);
	
	public boolean contains(T entity);
	
}
