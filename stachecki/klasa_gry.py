class Game:
	nick1="";
	nick2="";
	winner = "";
	count = 0;
	nicklist=[];
	cardlist=[];
#otwieramy plik itworzymy obiekt gry
fo=open("C:\\abc.txt", "r");
obiekt = Game();
#pierwsza petla - czytamy podstawowe bzdury
for x in range (0, 6):
	line = fo.readline();
	if x==0: obiekt.nick1 = line;
	if x==1: obiekt.nick2 = line;
	if x==3: obiekt.winner= line;
	if x==5: obiekt.count=line;	
#indeks naszej listy
i=0;
#ogr to numer ostatniej linijki pliku
ogr = int( int(obiekt.count)+8);
#wczytujemy przebieg gry
for y in range (7,ogr):
	line = fo.readline();
	#gdy linijka odstepu to nie wczytujemy
	if line == "\n":
		continue;
	elif not line:break;
	#dzielimy funkcja split dana linie
	else: 	list=line.split ("#")
	#stad otrzymujeym nick i nazwe karty, ale trza nad tym popracowac
	obiekt.nicklist.append( list[0]);
	obiekt.cardlist.append(list[1]);
	#wyswietlamy i zwiekszamy indeks
	print (obiekt.nicklist[i]+" "+obiekt.cardlist[i]);
	i= i+1;
#zamykamy plik
fo.close();
#wyswietlanie danych
print obiekt.nick1;
print obiekt.nick2;
print obiekt.winner;
print obiekt.count;

#sprawdzenie maksa
tablecount=[];
for j in range (0, len(obiekt.cardlist)):
	tablecount.append(obiekt.cardlist.count(obiekt.cardlist[j]));

for k in range (0, len(tablecount)):
	if (obiekt.cardlist.count(obiekt.cardlist[k])== max(tablecount)):
		mx=k;

print (obiekt.cardlist[mx].lstrip()+" "+str(obiekt.cardlist.count(obiekt.cardlist[mx])).lstrip());

