from __future__ import with_statement
from django.shortcuts import render_to_response, get_object_or_404
from django.http import HttpResponse,HttpResponseRedirect
from django.contrib import auth
from django.core.context_processors import csrf
from django.contrib.auth.forms import UserCreationForm
from magic.models import *
from forms import *
from django.core.mail import EmailMessage
from django.template import Context, RequestContext
from datetime import datetime
from django.views.decorators.csrf import csrf_exempt
import logging
import time

#from PIL import Image
logger = logging.getLogger(__name__)

def myfunction():
    logger.debug('debug message!')

def myotherfunction():
    logger.error('this is an error message!')


#import requests

@csrf_exempt
def authorize(request):
    test=request.POST.get('nick','')
    response=HttpResponse()
    tescik=[]
    #print request.POST
    #len=len(Gracz.objects.all())
    tescik=Gracz.objects.all().values_list('imie', flat=True)
    
    logger.debug(request.POST)
    response.status=200


    if test in tescik:
        response.write('OK')
    else:
        response.write('ERROR')
    return response    

def log(request):
   test=request.POST.get('log','')
   response=HttpResponse()
   ts=time.time()
   ts=int(ts) 
   with open('/users/stud/inf/erwinr/atm/magic/historia/'+str(ts)+'.txt','wb+') as f:
       f.write(test)

   logger.debug(request.POST)
   response.status=200

   return response


#def log(request):
   #test=request.POST.get('log)


	#    response_data['nick']='OK'
	#else:
#		response_data['nick']='ERROR'
    
	

def login(request):
    c={}
    c.update(csrf(request))
    return render_to_response('login.html', c)

def auth_view(request):
    username = request.POST.get('username', '')
    password = request.POST.get('password', '')
    
    user = auth.authenticate(username=username, password=password)    
    if user is not None:
        auth.login(request, user)
        return HttpResponseRedirect('/accounts/logged_in')
    else:
        return HttpResponseRedirect('/accounts/invalid')

def logged_in(request):    
    a=Gracz.objects.get(imie=request.user.username)
    a.log()
    a.save()
    return render_to_response('logged_in.html', {'full_name' : request.user.username})

def logout(request):
    auth.logout(request)
    return render_to_response('logout.html')

#@csrf_exempt
def register_user(request):
    if request.method == 'POST':
        form = MyRegistrationForm(request.POST)
        
        if form.is_valid():
            form.save()
            return HttpResponseRedirect('/accounts/register_success')
    
    args = {}
    args.update(csrf(request)) 
   
    args['form'] = MyRegistrationForm()
    print args
    return render_to_response('register.html', args)

def invalid_login(request):
    return render_to_response('invalid_login.html')

def register_success(request):
    return render_to_response('register_success.html')


def admin_page(request):
    Gracze=Gracz.objects.all()
    Userzy=User.objects.all()
    
    return render_to_response('admin.html', {'Gracze':Gracze})







