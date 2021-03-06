from django.shortcuts import render, render_to_response
from django.http import HttpResponse, HttpResponseRedirect
from django.template import Context, RequestContext
from django.template.loader import get_template
from django.views.generic.base import TemplateView
from magic.models import *
from django.core.context_processors import csrf
from forms import *
import os
from django.core.mail.message import EmailMessage
from django.core.mail import send_mail
from datetime import datetime
import glob
import shutil
import re


def getwords(text):
    return re.compile('\w+').findall(text)


def language(request, language='pl'):
    response = HttpResponse("setting language to %s" % language)
    response.set_cookie("lang", language)
    response.session['lang'] = language
    return response

def gracze(request):
    
        
    ilosctur=0
    wygranych=0
    przegranych=0
    gier=0
    log=0
    najmk=0
    najwk=0
    srk=0
    srt=0
    ils=0
    najkr=0
    logi=0
    
    li=range(1,len(Gracz.objects.all())+1)
    dlugosc=len(Gracz.objects.all())
    for i in li:
	    wygranych+=Gracz.objects.get(id=i).wygrane
	    przegranych+=Gracz.objects.get(id=i).przegrane
	    gier+=Gracz.objects.get(id=i).rozgrywki
	    logi+=Gracz.objects.get(id=1).logowania
	    
	    srt+=Gracz.objects.get(id=i).sredniailosctur/dlugosc
	    srk+=Gracz.objects.get(id=i).sredniozagranychkart/dlugosc
	    najmk+=Gracz.objects.get(id=i).najmniejzagranychkart/dlugosc
	    najwk+=Gracz.objects.get(id=i).najwiecejzagranychkart/dlugosc
	    
	    ils+=Gracz.objects.get(id=i).iloscrozegranychtur
	    najkr+=Gracz.objects.get(id=i).najkrotszagra/dlugosc
	    log+=Gracz.objects.get(id=i).najdluzszagra/dlugosc
	    
	    
    dane=[wygranych, przegranych, gier,logi, srt, srk, najmk, najwk, ils, najkr, log]
    
    
    return render_to_response('gracze.html',
                             {'gracze' : Gracz.objects.all(), 'dane':dane})  
                             
                                                  
def gracz(request,gracz_id):
	
    tury=1
    turygracza1=0
    turygracza2=0
    kartygracza1=[]
    wystapienia1={}
    wystapienia2={}
    kartygracza2=[]
    zyciegracza1=[]
    zyciegracza2=[]
    wszystkiekarty=[]
    
   
    player=Gracz.objects.get(id=int(gracz_id))
    print player
    
    partia={}
    party=[]
    daty=[]
    d=0
    nowy=glob.glob("/users/stud/inf/erwinr/atm/magic/players/"+player.imie+"/*.txt")
    print nowy
    
    for i in nowy:
        k=i[-14:-4]
        k=int(k)
	party.append(k)
        daty.append(datetime.fromtimestamp(k).strftime('%Y-%m-%d %H:%M:%S'))
        with open('/users/stud/inf/erwinr/atm/magic/players/'+player.imie+'/'+i[-14:-4]+'.txt') as f:
            kolejne_tury=f.readlines()
	    linia=getwords(kolejne_tury[0])
	    win=linia[1]
	    lost=linia[2]
	        
	          
	    for linia in kolejne_tury:
                tlumacz= getwords(linia)
	        if 'PLAY' in linia:
	            if tlumacz[1]=='1':
                        turygracza1+=1
	                kartygracza1.append(tlumacz[3])
	                if tlumacz[3] in wystapienia1:
	                    wystapienia1[tlumacz[3]]+=1
	                else:
	                    wystapienia1[tlumacz[3]]=1
	            else:
	                turygracza2+=1
	                kartygracza2.append(tlumacz[3])
	                if tlumacz[3] in wystapienia2:
	                    wystapienia2[tlumacz[3]]+=1
	                else:
	                    wystapienia2[tlumacz[3]]=1
		            if 'SUBLIFE' in linia:
		                if tlumacz[1]=='1':
		                    zyciegracza1.append(tlumacz[2])
		                else:
		                    zyciegracza2.append(tlumacz[2])

    partia=dict(zip(party,daty))
    return render_to_response('gracz.html', {'partia':partia, 
	    'wystapienia1':wystapienia1,'gracz':player})
                  
def gra(request,part,gracz_id):
    
    tury=1
    turygracza1=0
    turygracza2=0
    kartygracza1=[]
    wystapienia1={}
    wystapienia2={}
    kartygracza2=[]
    zyciegracza1=[20]
    zyciegracza2=[20]
    wszystkiekarty=[]
    
    player=Gracz.objects.get(id=int(gracz_id))
    
    
    with open('/users/stud/inf/erwinr/atm/magic/players/'+player.imie+'/'+part+'.txt') as f:
        kolejne_tury=f.readlines()
        linia=getwords(kolejne_tury[0])
        P1=linia[1]
        P2=linia[2]
          
        for linia in kolejne_tury:
            
	        tlumacz= getwords(linia)
	        if 'PLAY' in linia:
	            if tlumacz[1]=='1':
	                turygracza1+=1
	                kartygracza1.append(tlumacz[3])
	                if tlumacz[3] in wystapienia1:
	                    wystapienia1[tlumacz[3]]+=1
	                else:
	                    wystapienia1[tlumacz[3]]=1
	            else:
	                turygracza2+=1
	                kartygracza2.append(tlumacz[3])
	                if tlumacz[3] in wystapienia2:
	                    wystapienia2[tlumacz[3]]+=1
	                else:
	                    wystapienia2[tlumacz[3]]=1
	        if 'SUBLIFE' in linia:
	            if tlumacz[1]=='0':
	                zyciegracza1.append(tlumacz[2])
	            elif tlumacz[2]=='0':
	                zyciegracza2.append(tlumacz[2])
	win=''
	lost=''
	statp1=''
	statp2=''
	if '0' in zyciegracza1:
		win=P2
		statp2='Wygral'
		statp1='Przegral'
		lost=P1
	else:
		win=P1
		statp1='Wygral'
		statp2="Przegral"
		lost=P2
	
	print zyciegracza1, zyciegracza2
	losthp=20-int(zyciegracza1[-1])
	losthp2=20-int(zyciegracza2[-1])
	
    paczka=[P1,P2,statp1, statp2, turygracza1, turygracza2, tury, kartygracza1, 
    kartygracza2, losthp, losthp2]    
    return render_to_response('gra.html',{'dane':paczka, 'wystapienia1' :wystapienia1, 
                              'wystapienia2':wystapienia2})

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
		#klucz to id , value to karta
		idkarta={};
		#w co celuje karta
		celkarty={};
		#czary
		czary={};
		#jakdlugo
		jakdlugo={};
		#HP poczatkowe 20
		
		#otwieramy plik i tworzymy obiekt gry
	fo=open('/users/stud/inf/erwinr/atm/magic/history/LOGI.txt', 'r');
	obiekt = Game();
	wynik = 0;

	#pierwsza petla - czytamy nicki
	for x in range (0, 1):
		line = fo.readline();
		list = line.split(" ");
		obiekt.nick1=list[1];
		obiekt.nick2=list[2].strip();
	
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
				
					fn=open('/users/stud/inf/erwinr/atm/magic/history/czary.txt', 'r');
					for k in range (0,70):
						line2=fn.readline();
						if not line2 :break;
						wyraz = line2.strip();
						if (dolacz==wyraz):
							obiekt.czary.update({obiekt.count:dolacz});
							wynik = 1;
							break;
					if (wynik == 0):
						fl=open('/users/stud/inf/erwinr/atm/magic/history/stats.txt', 'r');
						for m in range (0, 100):
							line3=fl.readline();
							if not line3 : break;
							list3 = line3.split(" ");
							if (dolacz == list3[0]):
								obiekt.stats.update({obiekt.count : list[2]});
								obiekt.stats1.update({obiekt.count : list3[1]});
								obiekt.stats2.update({obiekt.count : list3[2].strip()});
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
					dolacz=list[3].strip();	
				
					fn=open('/users/stud/inf/erwinr/atm/magic/history/czary.txt', 'r');
					for k in range (0,70):
						line2=fn.readline();
						if not line2 :break;
						wyraz = line2.strip();
						if (dolacz==wyraz):
							obiekt.czary.update({obiekt.count:dolacz.strip()});
							wynik = 1;
							break;
					if (wynik == 0):
						fl=open('/users/stud/inf/erwinr/atm/magic/history/stats.txt', 'r');
						for m in range (0, 100):
							line3=fl.readline();
							if not line3 : break;
							list3 = line3.split(" ");
							if (dolacz == list3[0]):
								obiekt.stats.update({obiekt.count : list[2]});
								obiekt.stats1.update({obiekt.count : list3[1]});
								obiekt.stats2.update({obiekt.count : list3[2].strip()});
								break;
						obiekt.cardlist.update({obiekt.count : dolacz.strip()});
						obiekt.idlist.update({obiekt.count : list[2]});
						ind = int(list[2]);
						obiekt.idkarta.update({ind : dolacz.strip()});
					obiekt.ktogra.update({obiekt.count : list[1]});
					if (list[1]=="1"):					
						obiekt.actionlist.update({obiekt.count : obiekt.nick1+" gra karte "+dolacz.strip()});
					else:
						obiekt.actionlist.update({obiekt.count : obiekt.nick2+" gra karte "+dolacz.strip()});
					ostktogra = obiekt.count;
			else:
				dolacz=list[3];	
				if (dolacz[len(dolacz)-1]=="\n" or dolacz[len(dolacz)-1]==" "):
					dolacz=dolacz[:-1];
				fn=open('/users/stud/erwinr/atm/magic/history/czary.txt', 'r');
				for k in range (0,70):
					line2=fn.readline();
					if not line2 :break;
					wyraz = line2.strip();
					if (dolacz==wyraz):
						obiekt.czary.update({obiekt.count:dolacz.strip()});
						wynik = 1;
						break;
				if (wynik == 0):
					fl=open('/users/stud/inf/erwinr/atm/magic/history/stats.txt', 'r');
					for m in range (0, 100):
						line3=fl.readline();
						if not line3 : break;
						list3 = line3.split(" ");
						if (dolacz == list3[0]):
							obiekt.stats.update({obiekt.count : list[2]});
							obiekt.stats1.update({obiekt.count : list3[1]});
							obiekt.stats2.update({obiekt.count : list3[2].strip()});
							break;
					obiekt.cardlist.update({obiekt.count : dolacz.strip()});
					obiekt.idlist.update({obiekt.count : list[2]});
					ind = int(list[2]);
					obiekt.idkarta.update({ind : dolacz.strip()});
				obiekt.ktogra.update({obiekt.count : list[1]});
				if (list[1]=="1"):					
					obiekt.actionlist.update({obiekt.count : obiekt.nick1+" gra karte "+dolacz.strip()});
				else:
					obiekt.actionlist.update({obiekt.count : obiekt.nick2+" gra karte "+dolacz.strip()});
			if (list[4] != "-1" and list[4] != " " and list[4]!="\n" and len(list)>4 ):
				obiekt.celkarty.update({obiekt.count : list[4].strip()});
		elif ("SUBLIFE" in list[0]):
			if ("1" in list[1]):
				if (list[2][len(list[2])-1]=="\n"):
					list[2]=list[2][:-1];
				obiekt.hp1list.update({obiekt.count : list[2].strip()});
				if (list[2]!="0"):
					obiekt.actionlist.update({obiekt.count : "Wskaznik zdrowia gracza "+
						obiekt.nick1+": "+list[2].strip()});
				else:
					obiekt.actionlist.update({obiekt.count : "Gracz "+ obiekt.nick2+" odnosi zwyciestwo!"});
			else: 
				if (list[2][len(list[2])-1]=="\n"):
					list[2]=list[2][:-1];
				obiekt.hp2list.update({obiekt.count : list[2].strip()});
				if (list[2]!="0"):
					obiekt.actionlist.update({obiekt.count : "Wskaznik zdrowia gracza "+
						obiekt.nick2+": "+list[2].strip()});
				else :
					obiekt.actionlist.update({obiekt.count : "Gracz "+ obiekt.nick1+" odnosi zwyciestwo!"});
		elif ("NEXTTURN" in list[0]):
			element = obiekt.ktogra.get(ostktogra,obiekt.ktogra);
			obiekt.nextturn.update({obiekt.count : element});
			obiekt.actionlist.update({obiekt.count : "Nastepna tura"});
		elif ("STATS" in list[0]):
			obiekt.stats.update({obiekt.count : list[1]});
			obiekt.stats1.update({obiekt.count : list[2]});
			obiekt.stats2.update({obiekt.count : list[3].strip()});
			obiekt.jakdlugo.update({obiekt.count : list[4].strip()});
			ind=int(list[1]);
			element=obiekt.idkarta[ind];
			obiekt.actionlist.update({obiekt.count : "Nadanie statystyki karcie: "+element+"  wartosci: "+list[2]+" "+list[3].strip()});
		elif ("DEFENCE" in list[0]):
			obiekt.defenslist.update({obiekt.count : list[2].strip()});
			obiekt.celeobrony.update({obiekt.count : list[1]});
			ind=int(list[2].strip());
			element=obiekt.idkarta[ind];
			ind2 = int(list[1]);
			element2=obiekt.idkarta[ind2];
			obiekt.actionlist.update({obiekt.count : "Karta "+element+" broni atak karty "+element2+"!"});
		elif ("ATTACK" in list[0]):
			obiekt.ataklist.update({obiekt.count : list[1].strip()});
			ind=int(list[1].strip());
			element=obiekt.idkarta[ind];
			obiekt.actionlist.update({obiekt.count : "Atak karty "+element+"!"});
		elif ("DEAD" in list[0]):
			obiekt.deadlist.update({obiekt.count : list[1].strip()});
			ind=int(list[1].strip());
			element=obiekt.idkarta[ind];
			obiekt.actionlist.update({obiekt.count : "Smierc karty "+element});
		elif ("ADDDAMAGE" in list[0]):
			obiekt.dmglist.update({obiekt.count : list[1]});
			obiekt.dmgvalue.update({obiekt.count : list[2].strip()});
			ind=int(list[1]);
			element=obiekt.idkarta[ind];
			obiekt.actionlist.update({obiekt.count : ("Zmiana wartosci damage dla karty  "+element+" na wartosc: "+list[2]).strip() });
		#zamykamy plik
	fo.close();
	fn.close();
	fl.close();
	return render_to_response ('video.html',{'karty':obiekt.cardlist, 'licznik':obiekt.count,
	 'gracz1': 1, 'gracz2': 2, 'ktogra':obiekt.ktogra, 'akcje':obiekt.actionlist, 
	 'deads':obiekt.deadlist, 'idlist':obiekt.idlist, 'ileakcji':obiekt.count, 
	 'hp1':[ str(i).strip() for i in obiekt.hp1list ], 'hp2': [ str(i).strip() for i in  obiekt.hp2list], 'ataki':obiekt.ataklist,
	 'obrony':obiekt.defenslist, 'dmg':obiekt.dmglist,'dmgvalue':obiekt.dmgvalue, 
	 'tury':obiekt.nextturn, 'cele':obiekt.celeobrony, 'stats':obiekt.stats, 
	 'stats1':obiekt.stats1, 'stats2':obiekt.stats2, 'celataku':obiekt.celkarty, 
	 'czary':obiekt.czary, 'jakdlugo':obiekt.jakdlugo})
	 
def about(request):
    return render_to_response('about.html')

def download(request):
    return render_to_response('download.html')

def info(request):
    return render_to_response('info.html')

def graf(request):
    return render_to_response('graf.html')

def popular(request):
    
    d={'AngelicWall':6,'Shock':3,'Seraphin':4}
    return render_to_response('popular.html', {'d':d})

def leng(request):
    d={'gra1':5,'gra2':12,'gra3':14,'gra4':21,'gra5':21}
    return render_to_response('leng.html', {'d':d})
def color(request):
    d={'bialy':9,'czarny':5,'zielony':15, 'niebieski':12, 'czerwony':7}
    return render_to_response('color.html', {'d':d})

    

def best(request):
    li=range(1,len(Gracz.objects.all())+1)
    print li
    tab2={}
    tab1={}
    for i in li:
        #tab.append(i)
        print ''
        tab2[(Gracz.objects.get(id=i).imie)]=int(Gracz.objects.get(id=i).wygrane)
    
    sorted(tab2.items(),key=lambda x:x[1]) 
    print tab2   
   
  
    return render_to_response('best.html', {'d':tab2})

def hello_template_simple(request):
    return render_to_response('hello.html', {'name' : name})


