
class Game:
	nick1="";
	nick2="";
	id1=1;
	id2=2;
	hp1=20;
	hp2=20;
	winner = "";
	count=0;
	cardlist={};
	actionlist={};
#HP poczatkowe 20
#otwieramy plik itworzymy obiekt gry
fo=open('LOGI_V3.txt', 'r');
obiekt = Game();


#pierwsza petla - czytamy nicki
for x in range (0, 1):
	line = fo.readline();
	list = line.split(" ");
	obiekt.nick1=list[1];
	obiekt.nick2=list[2];
	
if (obiekt.nick2[len(obiekt.nick2)-1] == "\n"):
	obiekt.nick2=obiekt.nick2[:-1];

#indeks naszej listy
i=0;

licznik=0;
#ogr to numer ostatniej linijki pliku
ogr = 1000;
#wczytujemy przebieg gry
for y in range (1,ogr):
	line = fo.readline();
	#gdy linijka odstepu to nie wczytujemy
	if line == "NEXTTURN":
		continue;
	elif not line:break;
	else:
	#dzielimy funkcja split dana linie
		list=line.split (" ");
		obiekt.count=obiekt.count+1;
	#wczytujemy akcje
		#obiekt.actionlist.update({obiekt.count: line[:-1]});
		obiekt.count = obiekt.count +1;
	if ("PLAY" in list[0]):
		if (list[2] != "-1" ):
			dolacz="";
			if (len(list) > 6):
				limit=len(list)-1;
				for i in range (4,limit):
					dolacz=dolacz+" "+list[i-1];
				
				dolacz=dolacz[1:];
				print(dolacz);
				obiekt.cardlist.update({list[2] : dolacz});
			else:
				for i in range (4,len(list)+1):
					dolacz=dolacz+" "+list[i-1];
				dolacz=dolacz[1:];
				dolacz=dolacz[:-1];
				print(dolacz);
				obiekt.cardlist.update({list[2] : dolacz});
	elif ("SUBLIFE" in list[0]):
		if (str(obiekt.id1) in list[1]):
			obiekt.hp1=list[2];
		else: 
			obiekt.hp2=list[2];
	elif ("DEFENCE" in list[0] or "STATS" in list[0] or "ADDDAMAGE" in list[0]):
		continue;
	elif ("DEAD" in list[0]):
		#del obiekt.cardlist[list[1][:-1]];
		continue;
#zamykamy plik
fo.close();

print (obiekt.nick1);
print(obiekt.nick2);
print(obiekt.cardlist);