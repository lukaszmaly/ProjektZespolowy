from django.shortcuts import render, render_to_response
from django.http import HttpResponse, HttpResponseRedirect
from django.template import Context
from django.template.loader import get_template
from django.views.generic.base import TemplateView
from magic.models import *
from django.core.context_processors import csrf
from forms import *
import os

def analizeinfo(info, id2, z):
    a=Gracz.objects.get(id=id2)
    print 'point'
    if z==1:
        a.wygrane+=1
    elif z==0:
        a.przegrane+=1
       
    a.save()


def language(request, language='pl'):
    response = HttpResponse("setting language to %s" % language)
    response.set_cookie("lang", language)
    response.session['lang'] = language
    return response

def gracze(request):
    return render_to_response('gracze.html',
                             {'gracze' : Gracz.objects.all()})
                         

def gracz(request, gracz_id):      
    info=[]  
    hist=[]    
    lista=[]


    try:
        with open('/home/erwin/atm/magic/history/'+ 
str(Gracz.objects.get(id=gracz_id).imie)+'.txt','r+') as f:
            print 'ok'
            lista=f.readlines()
            stat=int(lista[0])
            info=lista[1:3]
            hist=lista[3:]
      
            if stat==1:        
               a=Gracz.objects.get(id=gracz_id)   
               a.wygrane+=1
               a.save()
            if stat==0:
            #analizeinfo(info,gracz_id,0)
               a=Gracz.objects.get(id=gracz_id)   
               a.przegrane+=1
               a.save()
       #    print 'done'
        f.close()
       #hist analize
       #info analize 
        return render_to_response('gracz.html',{'gracz' : Gracz.objects.get(id=gracz_id), 
'info':info, 'hist':hist})
    except IOError:
        return  render_to_response('gracz.html',{'gracz' : Gracz.objects.get(id=gracz_id)})

     

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
