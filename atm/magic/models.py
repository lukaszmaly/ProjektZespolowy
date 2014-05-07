from django.db import models
from time import time
from django.contrib.auth.models import User, UserManager

class Gracz(models.Model):
    imie = models.CharField(max_length=20)
    rozgrywki = models.IntegerField()
    wygrane = models.IntegerField()
    przegrane = models.IntegerField()
    logowania = models.IntegerField()
        
    mail = models.CharField(max_length=40)
    
    ulubionakarta = models.CharField(max_length=40)
    ulubionakarta2 = models.CharField(max_length=40) 
    ulubionakarta3 = models.CharField(max_length=40)
    
    ulubionakartapick= models.IntegerField()
    ulubionakarta2pick = models.IntegerField()
    ulubionakarta3pick = models.IntegerField()

    najdluzszagra= models.IntegerField()
    najkrotszagra=models.IntegerField()
    sredniailosctur=models.IntegerField()
    
    najmniejzagranychkart=models.IntegerField()
    najwiecejzagranychkart=models.IntegerField()
    sredniozagranychkart=models.IntegerField()
    
    iloscrozegranychtur=models.IntegerField()
    
    def nowy(self,imie, mail):
        self.imie=str(imie)
        self.wygrane=0
        self.przegrane=0
        self.logowania=0
        self.mail=str(mail)
		
        self.rozgrywki = 0
        #trueimie = str(trueimie)
        #truenazwisko = str(truenazwisko)
		
        self.ulubionakarta =''
        self.ulubionakarta2 = '' 
        self.ulubionakarta3 = ''
		
        self.iloscrozegranychtur=0
		
        self.ulubionakartapick= 0
        self.ulubionakarta2pick =0
        self.ulubionakarta3pick =0
		
        self.najdluzszagra=0
        self.najkrotszagra=0
        self.sredniailosctur=0
		
        self.najmniejzagranychkart=0
        self.najwiecejzagranychkart=0
        self.sredniozagranychkart=0
	         
    def __str__(self):
        return self.imie       

    def win(self):
        self.wygrane+=1
    def lost(self): 
        self.przegrane+=1

    def log(self):
        self.logowania+=1



class karta(models.Model):
   nazwa=models.CharField(max_length=30)   
   typ=models.CharField(max_length=10) 
   mana=models.IntegerField()
   kolor=models.CharField(max_length=10)
   atak=models.IntegerField()
   obrona=models.IntegerField()
   wystapilawilugrach=models.IntegerField()
   zagrania=models.IntegerField()
   
   
   
class statystyki(models.Model):
	ogolgier=models.IntegerField()
	ogolwygranych=models.IntegerField()
	ogolprzegranych=models.IntegerField()
	ilosckart=models.IntegerField()
	

   
