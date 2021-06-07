#!/bin/bash

#Script for automatic encoding conversion
 
#Copyright 2021 Alexander Chernokrylov <CodeDesign2763@gmail.com>
 
#This is a part of ACDG.
#This program is free software: you can redistribute it and/or 
#modify it under the terms of the GNU General Public License as 
#published by the Free Software Foundation, either version 3 of the 
#License, or (at your option) any later version.

#This program is distributed in the hope that it will be useful,
#but WITHOUT ANY WARRANTY; without even the implied warranty of
#MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#GNU General Public License for more details.

#You should have received a copy of the GNU General Public License
#along with this program.  
#If not, see <https://www.gnu.org/licenses/>.



#Automatic change of encoding for source files

#Reload file after conversion if using Geany.

for f in "src"/*java
do
	if [[ $1 = "utf8" ]]&&[[ "$#" -eq 1 ]]
	then
		file_before_conv="$f";
		file_after_conv="$f""_ch"
		iconv -f cp1251 -t utf8 "$f" -o "$f""_ch"
		rm "$f"
		mv "$f""_ch" "$f"
		s=" from CP1251 to UTF8"
	else
		file_before_conv="$f";
		file_after_conv="$f""_ch"
		iconv -f utf8 -t cp1251 "$f" -o "$f""_ch"
		rm "$f"
		mv "$f""_ch" "$f"
		s=" from UTF8 to СP1251"
	fi
	echo "$f : encoding changed$s"
done
