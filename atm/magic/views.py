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
    with open('/home/erwin/atm/magic/'+player+'.txt') as f:
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
    
    Gracz.objects.get(imie=looser).przegrane+=1
    Gracz.objects.get(imie=looser).save()
    
        
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
    return render_to_response('video.html')

def popular(request):
    
    d={'banshee':15,'overlord':12,'spiderling':10, 'dragon':5}
    return render_to_response('popular.html', {'d':d})

def leng(request): 
    d= {'najkrotsza':5,'najdluzsza':22,'srednio':12} 
    return render_to_response('leng.html', {'d':d})

def color(request): 
    d={'bialy':2,'czarny':1,'zielony':1} 
    return render_to_response('color.html', {'d':d})

def best(request): 
    d={'gracz1':5,'gracz2':4,'gracz3':3} 
    return render_to_response('best.html', {'d':d})

def hello_template_simple(request):
    return render_to_response('hello.html', {'name' : name})




# Create your views here.
