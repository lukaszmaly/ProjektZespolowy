class Game:
	nick1="";
	nick2="";
	winner = "";
	count = 0;
	nicklist=[];
	cardlist=[];
	actionlist=[];
#otwieramy plik itworzymy obiekt gry
fo=open("C:\\abc.txt", "r");
obiekt = Game();

#pierwsza petla - czytamy podstawowe bzdury
for x in range (0, 6):
	line = fo.readline();
	if x==0: obiekt.nick1=line;
	if x==1: obiekt.nick2=line;
	if x==3: obiekt.winner= line;
	if x==5: obiekt.count=line;	
#indeks naszej listy
i=0;
print obiekt.nick1;
print obiekt.nick2;
print obiekt.winner;
print obiekt.count;

#ogr to numer ostatniej linijki pliku
ogr = int( int(obiekt.count)+8)*100;
#wczytujemy przebieg gry
for y in range (7,ogr):
	line = fo.readline();
	#gdy linijka odstepu to nie wczytujemy
	if line == "\n":
		continue;
	elif not line:break;
	else:
	#dzielimy funkcja split dana linie
		list=line.split (" ");
	
		if (list[0]=="Smierc"):
			obiekt.actionlist.append(line);			
		elif (list[0]=="Obrazenia"):
			obiekt.actionlist.append(line);
		elif (list[0]=="Brak"):
			obiekt.actionlist.append(line);
			#CZEMU TO NIE DZIALA !!!! 
		elif (list[0] == obiekt.nick1) or (list[0] == obiekt.nick2):
			
			if(list[3]=="mana"):
				dolacz="";
				dolacz.join(list[2]);
				dolacz.join(" ");
				dolacz.join(list[3]);
				obiekt.cardlist.append(dolacz);
				obiekt.actionlist.append(line);
			else: 
				dolacz="";
				for j in range (2,len(list)):
					dolacz.join(list[j]);
					dolacz.join(" ");
				obiekt.cardlist.append(dolacz);
				obiekt.actionlist.append(line);
		else:
			print(list[0]);
			obiekt.actionlist.append(line);
#zamykamy plik
fo.close();



