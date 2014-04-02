from collections import OrderedDict
#klasa gry, zmodyfikowana o ilosc akcji wykonana przez gracza
class Game:
	nick1="";
	nick2="";
	nick1hp=20;
	nick2hp=20;
	winner = "";
	count=0;
	cardcount=0;
	othercount=0;
	nicklist={};
	cardlist={};
	manalist={};
	actionlist={};
	liczniklist={};
#HP poczatkowe 20
#otwieramy plik itworzymy obiekt gry
fo=open("C:\\abc.txt", "r");
obiekt = Game();

#pierwsza petla - czytamy nicki
for x in range (0, 2):
	line = fo.readline();
	if x==0: obiekt.nick1=line;
	if x==1: obiekt.nick2=line;


#indeks naszej listy
i=0;
#odejmujemy znaki nowej linii
obiekt.nick1 = obiekt.nick1[:-1];
#nie wiem, jaki jest znak
obiekt.nick2 = obiekt.nick2[:-2];
#czasami wystepowaly podwojne znaki
if (obiekt.nick2[len(obiekt.nick2)-1]=="\n"):
	obiekt.nick2=obiekt.nick2[:-1];

if (obiekt.nick1[len(obiekt.nick1)-1]=="\n"):
	obiekt.nick1=obiekt.nick1[:-1];

licznik=0;
#ogr to numer ostatniej linijki pliku
ogr = 1000;
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
	#wczytujemy akcje
		obiekt.actionlist.update({obiekt.count: line[:-1]});
		obiekt.count = obiekt.count + 1;
		if("win" in list[0]):
			obiekt.winner=list[1];
			obiekt.othercount=obiekt.othercount+1;
			obiekt.liczniklist.update({obiekt.othercount-1 : licznik } );
		elif (obiekt.nick1 in list[1] or obiekt.nick2 in list[1]):
			obiekt.othercount+=1;
			obiekt.nicklist.update({obiekt.othercount : list[1][:-1] } );
			if (licznik != 0):			
				obiekt.liczniklist.update({obiekt.othercount-1 : licznik } );		
				licznik=0;
		#jeszcze nie wiem, co blok mialby robic przy wizualizacji
		elif ("block:" in list[0]):
			newlist=line.split(":");
			licznik +=1;
		elif ("tap:" in list[0] or "counter:" in list[0] or "dead:"in list[0] or "skill:" in list[0] or "atack:" in list[0]):		
			if("mana" in list[1]):
				slowo=list[1];
				if (slowo[len(slowo)-1]=="\n"):
					slowo=slowo[:-1];
					obiekt.manalist.update({obiekt.count : slowo});

				else:
					obiekt.manalist.update({obiekt.count : list[1]});
					
			else: 
				dolacz="";
				for j in range (1,len(list)):
					dolacz=dolacz+" "+list[j];
				if (dolacz[len(dolacz)-1]==",") or (dolacz[len(dolacz)-1] == "\n"):
					dolacz=dolacz[:-1];	
				dolacz=dolacz[1:];
				if (dolacz[len(dolacz)-1]==",") or (dolacz[len(dolacz)-1] == "\n"):
					dolacz=dolacz[:-1];	
				obiekt.cardlist.update({obiekt.count : dolacz});
				obiekt.cardcount=obiekt.cardcount+1;	
			
				
			licznik +=1;
		elif("HP_"+obiekt.nick1 in list[0]):
			nick1hp = list[1];
			licznik +=1;
		elif ("HP_"+obiekt.nick2 in list[0]):
			nick2hp=list[1];
			licznik +=1;
		#samo play, z targetem lub 1/1 cokolwiek to znaczy
		elif ("play:" in list[0]):
			if("mana" in list[1]):
				slowo=list[1];
				if (slowo[len(slowo)-1]=="\n"):
					slowo=slowo[:-1];
					obiekt.manalist.update({obiekt.count : slowo});
				else:
					obiekt.manalist.update({obiekt.count : list[1]});
			elif ("target" in line):
				ind = list.index("target:");
				dolacz="";
				for j in range (1,ind):
					dolacz=dolacz+" "+list[j];
				if (dolacz[len(dolacz)-1]==",") or (dolacz[len(dolacz)-1] == "\n"):
					dolacz=dolacz[:-1];
				dolacz=dolacz[1:];
				if (dolacz[len(dolacz)-1]==",") or (dolacz[len(dolacz)-1] == "\n"):
					dolacz=dolacz[:-1];	
				obiekt.cardlist.update({obiekt.count : dolacz});
				
			else:
				dolacz="";
				for j in range (1,len(list)):
					dolacz=dolacz+" "+list[j];
				if (dolacz[len(dolacz)-1]==",") or (dolacz[len(dolacz)-1] == "\n"):
					dolacz=dolacz[:-1];	
				dolacz=dolacz[1:];
				obiekt.cardlist.update({obiekt.count : dolacz});
			licznik +=1;

print (obiekt.cardlist);

