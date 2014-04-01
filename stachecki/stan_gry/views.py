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
    with open('C:\\Users\\np550\\Desktop\\atm\\magic\\history\\'+Gracz.objects.get(id=gracz_id).imie+'.txt') as f:
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
    
    if not os.path.exists('C:\\Users\\np550\\Desktop\\atm\\magic\\history\\'+looser+'.txt'):
        g=open('C:\\Users\\np550\\Desktop\\atm\\magic\\history'+looser+'.txt','w')
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
		nick1hp=20;
		nick2hp=20;
		winner = "";
		count=0;
		cardcount=0;
		#licznik do nickow
		othercount=0;
		nicklist={};
		cardlist={};
		manalist=[];
		actionlist={};
		liczniklist={};
	#HP poczatkowe 20
	#otwieramy plik itworzymy obiekt gry
	fo=open('C:\\Users\\np550\\Desktop\\atm\\magic\\history\\newfile.txt', 'r');
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
			obiekt.count = obiekt.count +1;
			if("win" in list[0]):
				obiekt.winner=list[1];
				obiekt.liczniklist.update({obiekt.othercount : licznik } );
				obiekt.othercount+=1;
			elif (obiekt.nick1 in list[1] or obiekt.nick2 in list[1]):
				obiekt.nicklist.update({obiekt.othercount : list[1][:-1] } );
				if (licznik != 0):
					obiekt.liczniklist.update({obiekt.othercount : licznik } );
					obiekt.othercount+=1;
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
						obiekt.manalist.append(slowo);
	
					else:
						obiekt.manalist.append(list[1]);
					
				else: 
					dolacz="";
					for j in range (1,len(list)):
						dolacz=dolacz+" "+list[j];
					obiekt.cardlist.update({obiekt.cardcount : dolacz});
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
						obiekt.manalist.append(slowo);
					else:
						obiekt.manalist.append(list[1]);
				elif ("target" in line):
					ind = list.index("target:");
					dolacz="";
					for j in range (1,ind):
						dolacz=dolacz+" "+list[j];
					if (dolacz[len(dolacz)-1]==",") or (dolacz[len(dolacz)-1] == "\n"):
						dolacz=dolacz[:-1];
					obiekt.cardlist.update({obiekt.cardcount : dolacz});
					obiekt.cardcount=obiekt.cardcount+1;	
				
				else:
					dolacz="";
					for j in range (1,len(list)):
						dolacz=dolacz+" "+list[j];
					if (dolacz[len(dolacz)-1]==",") or (dolacz[len(dolacz)-1] == "\n"):
						dolacz=dolacz[:-1];			
					obiekt.cardlist.update({obiekt.cardcount : dolacz});
					obiekt.cardcount=obiekt.cardcount+1;				
				licznik +=1;
	#zamykamy plik
	fo.close();
	dlugosc = len (obiekt.nicklist)-1;
	return render_to_response ('video.html', {'akcje':obiekt.actionlist, 'liczba_akcji':obiekt.count})

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
