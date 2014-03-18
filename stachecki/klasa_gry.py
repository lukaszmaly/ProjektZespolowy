class Game:
	nick1="";
	nick2="";
	nick1hp=20;
	nick2hp=20;
	winner = "";
	count = 0;
	nicklist=[];
	cardlist=[];
	manalist=[];
	actionlist=[];
#HP poczatkowe 20
#otwieramy plik itworzymy obiekt gry
fo=open("C:\\abc.txt", "r");
obiekt = Game();

#pierwsza petla - czytamy podstawowe bzdury
for x in range (0, 2):
	line = fo.readline();
	if x==0: obiekt.nick1=line;
	if x==1: obiekt.nick2=line;
	

#indeks naszej listy
i=0;
obiekt.nick1 = obiekt.nick1[:-1];
obiekt.nick2 = obiekt.nick2[:-2];

#ogr to numer ostatniej linijki pliku
ogr = int( int(obiekt.count)+3)*100;
#wczytujemy przebieg gry
for y in range (3,ogr):
	line = fo.readline();
	#gdy linijka odstepu to nie wczytujemy
	if line == "\n":
		continue;
	elif not line:break;
	else:
	#dzielimy funkcja split dana linie
		list=line.split (" ");	
		if("win" in list[0]):
			obiekt.winner=list[1];
		elif (obiekt.nick1 in list[1]) or (obiekt.nick2 in list[1]):
			obiekt.nicklist.append(list[1][:-1]);
		#jeszcze nie wiem, co blok mialby robic przy wizualizacji
		elif ("block:" in list[0]):
			newlist=line.split(":");
			obiekt.actionlist.append(line);
		elif ("tap:" in list[0] or "counter:" in list[0] or "dead:"in list[0] or "skill:" in list[0]):		
			if ("target" in line):
				obiekt.actionlist.append(line);
			elif("mana" in list[1]):
				slowo=list[1];
				if (slowo[len(slowo)-1]=="\n"):
					slowo=slowo[:-1];
					obiekt.manalist.append(slowo);
				else:
					obiekt.manalist.append(list[1]);
				obiekt.actionlist.append(line);
			else: 
				dolacz="";
				for j in range (1,len(list)):
					dolacz=dolacz+" "+list[j];
				obiekt.cardlist.append(dolacz);
				obiekt.actionlist.append(line);
		elif("HP_"+obiekt.nick1 in list[0]):
			nick1hp = list[1];
		elif ("HP_"+obiekt.nick2 in list[0]):
			nick2hp=list[1];
		#samo play, z targetem lub 1/1 cokolwiek to znaczy
		elif ("play:" in list[0]):
			continue;
		
		else:
			obiekt.actionlist.append(line);
	obiekt.count = obiekt.count + 1;
#zamykamy plik
fo.close();

#nie dzialaja nazwy kart i targety
for k in range (0,len(obiekt.cardlist)):
	print(obiekt.cardlist[k]);

#nicki dzialaja
#hp dzialaja
#mana dziala

#play, play target i block 
	