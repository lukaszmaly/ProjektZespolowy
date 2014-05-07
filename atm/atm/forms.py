#!/usr/bin/enb python
# -*- coding: utf-8 -*-
from django import forms
from django.contrib.auth.models import User
from django.contrib.auth.forms import UserCreationForm
from magic.models import *
from django.core.mail import EmailMessage
#from django.forms.decorators_csrf import csrf_extempt
#import sys
import qrcode
#, qrtools
import base64
import os

class MyRegistrationForm(UserCreationForm):
         
    class Meta:
        model = User
        fields = ('username','email', 'password1','password2')    
        		#'trueimie','truenazwisko'
    def save(self, commit=True):
        user= super(MyRegistrationForm, self).save(commit=False)
        
        user.email = self.cleaned_data['email']  
        user.name = self.cleaned_data['username']
        user.passw =self.cleaned_data['password1']
        
       # user.trueimie=self.cleaned_data['trueimie']
       # user.truenazwisko=self.cleaned_data['truenazwisko']

        a=Gracz()
        a.nowy(user.name,user.email) 
        
        qr=qrcode.QRCode(
            version=1,
            error_correction=qrcode.constants.ERROR_CORRECT_L,
            box_size=10,
            )
     

        if commit:
            user.save()
            a.save()
            
            probka=user.name
            print probka
            
            qr.add_data(probka)
            qr.make(fit=True)
            
            img=qr.make_image()
            img.save('/users/stud/inf/erwinr/atm/magic/qrc/'+user.name+'.png')
            
            email=EmailMessage('Mail aktywacyjny', 'Witaj '+user.name+', oto qrkod, ktory pomoze ci zalogowac sie  w ATM. Do zobaczenia w grze! ATM', to=[user.email])
            email.attach_file('/users/stud/inf/erwinr/atm/magic/qrc/'+user.name+'.png')
            email.send()
            
            directory='/users/stud/inf/erwinr/atm/magic/players/'+user.name
            print directory
            
            if not os.path.exists(directory):
                os.makedirs(directory)
            
        return user
    

 
class EmailForm(forms.Form):
    email = forms.EmailField()
    subject = forms.CharField(max_length=100)
    message = forms.CharField(widget = forms.Textarea)
