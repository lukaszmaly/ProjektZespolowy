from django import forms
from django.contrib.auth.models import User
from django.contrib.auth.forms import UserCreationForm
from magic.models import *

class MyRegistrationForm(UserCreationForm):
    email = forms.EmailField(required=True)
    
    class Meta:
        model = User
        fields = ('username', 
	'email', 
	'password1', 
	'password2')
        
    def save(self, commit=True):
        user= super(MyRegistrationForm, self).save(commit=False)
        user.email = self.cleaned_data['email']
          
        a=Gracz()
        a.nowy(self.cleaned_data['username'],user.email)
        #a.save()
      
        if commit:
            user.save()
            a.save()
        return user
 
 
