from django.shortcuts import render, render_to_response
from django.http import HttpResponse, HttpResponseRedirect
from django.template import Context
from django.template.loader import get_template
from django.views.generic.base import TemplateView
from magic.models import *
from django.core.context_processors import csrf
from forms import *
from collections import Counter
import os

lista=[]

def czytaj(player):
    with open('C:\\Users\\np550\\Desktop\\atm\\magic\\'+player+'.txt') as f:
        lines=f.read().splitlines()
    print lines
    return lines

def language(request, language='pl'):
    response = HttpResponse("setting language to %s" % language)
    response.set_cookie("lang", language)
    response.session['lang'] = language
    return response

def gracze(request):
    return render_to_response('gracze.html',
                             {'gracze' : Gracz.objects.all()})
                         

def gracz(request, gracz_id):
    with open('/home/erwin/atm/magic/history/'+Gracz.objects.get(id=gracz_id).imie+'.txt') as f:
        lines=f.read().splitlines()
    winner=lines[0] 
    if winner==Gracz.objects.get(id=gracz_id).imie:
    
        Gracz.objects.get(id=gracz_id).wygrane+=1
        Gracz.objects.get(id=gracz_id).save()
        print 'dodano'
    
    global lista
    looser=lines[1]
    timegame=lines[3]
    hp1 = lines[5]
    hp2 = lines[6]
    hist=lines[8:]
    #lines.append('koniec')
  #  with open('/home/erwin/atm/magic/history/'+Gracz.objects.get(id=gracz_id).imie+'.txt','a') as f:
   #     f.write(lines[-1])
    
    print hist
    lista=hist
    f.close()
    
    if not os.path.exists('C:\\Users\\np550\\Desktop\\atm\\history\\'+looser+'.txt'):
        g=open('C:\\Users\\np550\\Desktop\\atm\\history\\'+looser+'.txt','w')
        Gracz.objects.get(imie=looser).przegrane+=1
        Gracz.objects.get(imie=looser).save()
        for item in lines:
            g.write('%s\n' % item)
        g.close()
        
    return render_to_response('gracz.html',{'gracz' : Gracz.objects.get(id=gracz_id), 'winner': winner, 'looser': looser, 'timegame' : timegame, 'hp1' : hp1, 'hp2': hp2, 'hist': hist})

def stat(request):
    
    return render_to_response('stat.html')

def main(request, language='pl'):
    language='pl'
    session_language='pl'
    
    if 'lang' in request.COOKIES:
        language=request.COOKIES['lang']
    
    if 'lang' in request.session:
        session_language= request.session['lang']

    return render_to_response('main.html', {'language': language,
                              'session_language' : session_language})
 
def about(request):
    return render_to_response('about.html')

def download(request):
    return render_to_response('download.html')

def info(request):
    return render_to_response('info.html')

def graf(request):
    return render_to_response('graf.html')

def video(request):
	#klasa gry, zmodyfikowana o ilosc akcji wykonana przez gracza
	class Game:
		nick1="";
		nick2="";
		winner = "";
		count=0;
		cardlist={};
		#kto gra karte
		idlist={};
		#lista akcji
		actionlist={};
		#kto umiera
		deadlist={};
		#ktory gracz gra
		ktogra={};
		#hp gracza 1
		hp1list={};
		#hp gracza 2
		hp2list={};
		# co atakuje
		ataklist={};
		#co broni
		defenslist={};
		#komu dodajemy damage
		dmglist={};
		#wartosc prawego dolnego rogu
		dmgvalue = {};
		#gdzie sie mamy przesuwac
		celeobrony={};
		#nastepna tura
		nextturn={};
		#ogolne staty
		stats = {};
		#staty pierwsze
		stats1={};
		#staty drugie
		stats2={};
		#jak dlugo beda statsy utrzymywac sie
		jakdlugo={};
		#klucz to id , value to karta
		idkarta={};
		#w co celuje karta
		celkarty={};
		#czary
		czary={};
		#HP poczatkowe 20
		
		#otwieramy plik i tworzymy obiekt gry
	fo=open('C:\\Users\\np550\\Desktop\\atm\\magic\\history\\LOGI.txt', 'r');
	obiekt = Game();
	wynik = 0;

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
	ostktogra=0;
	licznik=0;
	#ogr to numer ostatniej linijki pliku
	ogr = 1000;
	#wczytujemy przebieg gry
	for y in range (1,ogr):
		line = fo.readline();
		if not line:break;
		else:
		#dzielimy funkcja split dana linie
			list=line.split (" ");
			obiekt.count=obiekt.count+1;
			wynik=0;
		#wczytujemy akcje
		if ("PLAY" in list[0]):
			if (list[2] != "-1" ):
				dolacz="";
				if (len(list) > 5):
					dolacz=list[3];			
					if (dolacz[len(dolacz)-1]=="\n" or dolacz[len(dolacz)-1]==" "):
						dolacz=dolacz[:-1];
					fn=open('C:\\Users\\np550\\Desktop\\atm\\magic\\history\\czary.txt', 'r');
					for k in range (0,70):
						line2=fn.readline();
						if not line2 :break;
						wyraz = line2[:-1];
						if (dolacz==wyraz):
							obiekt.czary.update({obiekt.count:dolacz});
							wynik = 1;
							break;
					if (wynik == 0):
						fl=open('C:\\Users\\np550\\Desktop\\atm\\magic\\history\\stats.txt', 'r');
						for m in range (0, 100):
							line3=fl.readline();
							if not line3 : break;
							list3 = line3.split(" ");
							if (dolacz == list3[0]):
								obiekt.stats.update({obiekt.count : list[2]});
								obiekt.stats1.update({obiekt.count : list3[1]});
								obiekt.stats2.update({obiekt.count : list3[2][:-1]});
								break;
						obiekt.cardlist.update({obiekt.count : dolacz});
						obiekt.idlist.update({obiekt.count : list[2]});					
						ind = int(list[2]);
						obiekt.idkarta.update({ind : dolacz});
					obiekt.ktogra.update({obiekt.count : list[1]});	
					if (list[1]=="1"):					
						obiekt.actionlist.update({obiekt.count : obiekt.nick1+" gra karte "+dolacz});
					else:
						obiekt.actionlist.update({obiekt.count : obiekt.nick2+" gra karte "+dolacz});
					ostktogra = obiekt.count;
				else:
					dolacz=list[3];	
					if (dolacz[len(dolacz)-1]=="\n" or dolacz[len(dolacz)-1]==" "):
						dolacz=dolacz[:-1];
					fn=open('C:\\Users\\np550\\Desktop\\atm\\magic\\history\\czary.txt', 'r');
					for k in range (0,70):
						line2=fn.readline();
						if not line2 :break;
						wyraz = line2[:-1];
						if (dolacz==wyraz):
							obiekt.czary.update({obiekt.count:dolacz});
							wynik = 1;
							break;
					if (wynik == 0):
						fl=open('C:\\Users\\np550\\Desktop\\atm\\magic\\history\\stats.txt', 'r');
						for m in range (0, 100):
							line3=fl.readline();
							if not line3 : break;
							list3 = line3.split(" ");
							if (dolacz == list3[0]):
								obiekt.stats.update({obiekt.count : list[2]});
								obiekt.stats1.update({obiekt.count : list3[1]});
								obiekt.stats2.update({obiekt.count : list3[2][:-1]});
								break;
						obiekt.cardlist.update({obiekt.count : dolacz});
						obiekt.idlist.update({obiekt.count : list[2]});
						ind = int(list[2]);
						obiekt.idkarta.update({ind : dolacz});
					obiekt.ktogra.update({obiekt.count : list[1]});
					if (list[1]=="1"):					
						obiekt.actionlist.update({obiekt.count : obiekt.nick1+" gra karte "+dolacz});
					else:
						obiekt.actionlist.update({obiekt.count : obiekt.nick2+" gra karte "+dolacz});
					ostktogra = obiekt.count;
			else:
				dolacz=list[3];	
				if (dolacz[len(dolacz)-1]=="\n" or dolacz[len(dolacz)-1]==" "):
					dolacz=dolacz[:-1];
				fn=open('C:\\Users\\np550\\Desktop\\atm\\magic\\history\\czary.txt', 'r');
				for k in range (0,70):
					line2=fn.readline();
					if not line2 :break;
					wyraz = line2[:-1];
					if (dolacz==wyraz):
						obiekt.czary.update({obiekt.count:dolacz});
						wynik = 1;
						break;
				if (wynik == 0):
					fl=open('C:\\Users\\np550\\Desktop\\atm\\magic\\history\\stats.txt', 'r');
					for m in range (0, 100):
						line3=fl.readline();
						if not line3 : break;
						list3 = line3.split(" ");
						if (dolacz == list3[0]):
							obiekt.stats.update({obiekt.count : list[2]});
							obiekt.stats1.update({obiekt.count : list3[1]});
							obiekt.stats2.update({obiekt.count : list3[2][:-1]});
							break;
					obiekt.cardlist.update({obiekt.count : dolacz});
					obiekt.idlist.update({obiekt.count : list[2]});
					ind = int(list[2]);
					obiekt.idkarta.update({ind : dolacz});
				obiekt.ktogra.update({obiekt.count : list[1]});
				if (list[1]=="1"):					
					obiekt.actionlist.update({obiekt.count : obiekt.nick1+" gra karte "+dolacz});
				else:
					obiekt.actionlist.update({obiekt.count : obiekt.nick2+" gra karte "+dolacz});
			if (list[4] != "-1" and list[4] != " " and list[4]!="\n" and len(list)>4 ):
				obiekt.celkarty.update({obiekt.count : list[4]});
		elif ("SUBLIFE" in list[0]):
			if ("1" in list[1]):
				if (list[2][len(list[2])-1]=="\n"):
					list[2]=list[2][:-1];
				obiekt.hp1list.update({obiekt.count : list[2]});
				if (list[2]!="0"):
					obiekt.actionlist.update({obiekt.count : "Wskaznik zdrowia gracza "+ obiekt.nick1+": "+list[2]});
				else:
					obiekt.actionlist.update({obiekt.count : "Gracz "+ obiekt.nick2+" odnosi zwyciestwo!"});
			else: 
				if (list[2][len(list[2])-1]=="\n"):
					list[2]=list[2][:-1];
				obiekt.hp2list.update({obiekt.count : list[2]});
				if (list[2]!="0"):
					obiekt.actionlist.update({obiekt.count : "Wskaznik zdrowia gracza "+ obiekt.nick2+": "+list[2]});
				else :
					obiekt.actionlist.update({obiekt.count : "Gracz "+ obiekt.nick1+" odnosi zwyciestwo!"});
		elif ("NEXTTURN" in list[0]):
			element = obiekt.ktogra.get(ostktogra,obiekt.ktogra);
			obiekt.nextturn.update({obiekt.count : element});
			obiekt.actionlist.update({obiekt.count : "Nastepna tura"});
		elif ("STATS" in list[0]):
			obiekt.stats.update({obiekt.count : list[1]});
			obiekt.stats1.update({obiekt.count : list[2]});
			obiekt.stats2.update({obiekt.count : list[3]});
			#obiekt.jakdlugo.update({obiekt.count :list[4]});
			ind=int(list[1]);
			element=obiekt.idkarta[ind];
			obiekt.actionlist.update({obiekt.count : "Nadanie statystyki karcie: "+element+"  wartosci: "+list[2]+" "+list[3][:-1]});
		elif ("DEFENCE" in list[0]):
			obiekt.defenslist.update({obiekt.count : list[2][:-1]});
			#obiekt.celeobrony.update({obiekt.count : list[1]});
			ind=int(list[2][:-1]);
			element=obiekt.idkarta[ind];
			ind2 = int(list[1]);
			element2=obiekt.idkarta[ind2];
			obiekt.actionlist.update({obiekt.count : "Karta "+element+" broni atak karty "+element2+"!"});
		elif ("ATTACK" in list[0]):
			obiekt.ataklist.update({obiekt.count : list[1][:-1]});
			ind=int(list[1]);
			element=obiekt.idkarta[ind];
			obiekt.actionlist.update({obiekt.count : "Atak karty "+element+"!"});
		elif ("DEAD" in list[0]):
			obiekt.deadlist.update({obiekt.count : list[1][:-1]});
			ind=int(list[1][:-1]);
			element=obiekt.idkarta[ind];
			obiekt.actionlist.update({obiekt.count : "Smierc karty "+element});
		elif ("ADDDAMAGE" in list[0]):
			obiekt.dmglist.update({obiekt.count : list[1]});
			obiekt.dmgvalue.update({obiekt.count : list[2]});
			ind=int(list[1]);
			element=obiekt.idkarta[ind];
			obiekt.actionlist.update({obiekt.count : "Zmiana wartosci damage dla karty  "+element+" na wartosc: "+list[2][:-1]});
		#zamykamy plik
	fo.close();
	fn.close();
	fl.close();
	return render_to_response ('video.html',{'karty':obiekt.cardlist, 'licznik':obiekt.count, 'gracz1': 1, 'gracz2': 2, 'ktogra':obiekt.ktogra, 'akcje':obiekt.actionlist, 'deads':obiekt.deadlist, 'idlist':obiekt.idlist, 'ileakcji':obiekt.count, 'hp1':obiekt.hp1list, 'hp2':obiekt.hp2list, 'ataki':obiekt.ataklist,'obrony':obiekt.defenslist, 'dmg':obiekt.dmglist,'dmgvalue':obiekt.dmgvalue, 'tury':obiekt.nextturn, 'cele':obiekt.celeobrony, 'stats':obiekt.stats, 'stats1':obiekt.stats1, 'stats2':obiekt.stats2, 'celeataku':obiekt.celkarty, 'czary':obiekt.czary})

def popular(request):
    
    d={'woj':3,'zwiad':1,'lucz':1}
    return render_to_response('popular.html', {'d':d})

def leng(request):
    d={'gra1':5,'gra2':24,'gra3':35,'gra4':21,'gra5':41}
    return render_to_response('leng.html', {'d':d})
def color(request):
    d={'bialy':9,'czarny':5,'zielony':15, 'niebieski':12, 'czerwony':7}
    return render_to_response('color.html', {'d':d})
def best(request):
    d={'gracz1':2,'gracz2':7,'gracz3':4}
    return render_to_response('best.html', {'d':d})

def hello_template_simple(request):
    return render_to_response('hello.html', {'name' : name})




# Create your views here.
