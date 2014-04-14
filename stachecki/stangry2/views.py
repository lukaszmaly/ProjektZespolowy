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
		idlist={};
		actionlist={};
		deadlist={};
		#ktory gracz gra
		ktogra={};
		hp1list={};
		hp2list={};
		ataklist={};
		defenslist={};
		celelist={};
		#HP poczatkowe 20
		#otwieramy plik itworzymy obiekt gry
	fo=open('C:\\Users\\np550\\Desktop\\atm\\magic\\history\\LOGI_V3.txt', 'r');
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
			obiekt.actionlist.update({obiekt.count: line[:-1]});
		#wczytujemy akcje
		if ("PLAY" in list[0]):
			if (list[2] != "-1" ):
				dolacz="";
				if (len(list) > 5):
					dolacz=list[3];			
					if (dolacz[len(dolacz)-1]=="\n" or dolacz[len(dolacz)-1]==" "):
						dolacz=dolacz[:-1];
					obiekt.cardlist.update({obiekt.count : dolacz});
					obiekt.idlist.update({obiekt.count : list[2]});
					obiekt.ktogra.update({obiekt.count : list[1]});
				else:
					dolacz=list[3];	
					if (dolacz[len(dolacz)-1]=="\n" or dolacz[len(dolacz)-1]==" "):
						dolacz=dolacz[:-1];
					obiekt.cardlist.update({obiekt.count : dolacz});
					obiekt.idlist.update({obiekt.count : list[2]});
					obiekt.ktogra.update({obiekt.count : list[1]});

		elif ("SUBLIFE" in list[0]):
			if ("1" in list[1]):
				list[2]=list[2][:-1];
				obiekt.hp1list.update({obiekt.count : list[2]});
			else: 
				list[2]=list[2][:-1];
				obiekt.hp2list.update({obiekt.count : list[2]});
		elif ("STATS" in list[0] or "NEXTTURN" in list[0]):
			continue;
		elif ("DEFENCE" in list[0]):
			#bedzie mnie interesowalo, co sie blokuje... chyba
			obiekt.defenslist.update({obiekt.count : list[2][:-1]});
			obiekt.celelist.update({obiekt.count : list[1]});
		elif ("ATTACK" in list[0]):
			obiekt.ataklist.update({obiekt.count : list[1][:-1]});
		elif ("DEAD" in list[0]):
			obiekt.deadlist.update({obiekt.count : list[1][:-1]});
		elif ("ADDDAMAGE" in list[0]):
			obiekt.celelist.update({obiekt.count : list [1]});
		#zamykamy plik
	fo.close();
	return render_to_response ('video.html',{'karty':obiekt.cardlist, 'licznik':obiekt.count, 'gracz1': 1, 'gracz2': 2, 'ktogra':obiekt.ktogra, 'akcje':obiekt.actionlist, 'deads':obiekt.deadlist, 'idlist':obiekt.idlist, 'ileakcji':obiekt.count, 'hp1':obiekt.hp1list, 'hp2':obiekt.hp2list, 'ataki':obiekt.ataklist,'obrony':obiekt.defenslist, 'cele':obiekt.celelist})

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
