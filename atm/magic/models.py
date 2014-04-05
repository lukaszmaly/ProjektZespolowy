from django.db import models
from time import time
from django.contrib.auth.models import User, UserManager

class Gracz(models.Model):
    imie = models.CharField(max_length=20)
    wygrane = models.IntegerField()
    przegrane = models.IntegerField()
    logowania = models.IntegerField()
    mail = models.CharField(max_length=40)
      
    def nowy(self,imie, mail):
        self.imie=str(imie)
        self.wygrane=0
        self.przegrane=0
        self.logowania=0
        self.mail=str(mail)
    def __str__(self):
        return self.imie       

    def win(self):
        self.wygrane+=1
    def lost(self): 
        self.przegrane+=1
    #def game(self):
     #   self. 

    def log(self):
        self.logowania+=1



class karta(models.Model):
   nazwa=models.CharField(max_length=20)   
   mana=models.IntegerField()
   kolor=models.CharField(max_length=20)
   hp=models.IntegerField()

   
