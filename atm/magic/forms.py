from django import forms
from models import *

#class GraczForm(forms.ModelForm):

 #   class Meta:
 #       model = Gracz

class EmailForm(forms.Form):
    email = forms.EmailField()
    subject = forms.CharField(max_length=100)
    attach = forms.Field(widget = forms.FileInput)
    message = forms.CharField(widget = forms.Textarea)
