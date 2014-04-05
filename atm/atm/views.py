from __future__ import with_statement
from django.shortcuts import render_to_response
from django.http import HttpResponseRedirect
from django.contrib import auth
from django.core.context_processors import csrf
from django.contrib.auth.forms import UserCreationForm
from magic.models import *
from forms import *


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


