from django.db import models
from time import time

#def get_upload_file_name(instance, filename):
 #   return "uploaded_files/%s_%s" % (str(time()).replace('.','_'), filename)
# Create your models here.

class Gracz(models.Model):
    imie = models.CharField(max_length=20)
    date = models.DateTimeField('date published')
    wygrane = models.IntegerField()
    przegrane = models.IntegerField()
    logowania = models.IntegerField()
    mail = models.CharField(max_length=40)
    #ulubione_karty
    

    def __unicode__(self):
        return self.imie    

#lass karta(models.Model):
 #  nazwa=models.CharField(max_length=20)
  # thumb=models.FileField(upload_to=get_upload_file_name)    
