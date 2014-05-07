import sqlite3
import os
import re
import sys
import shutil
import glob
from time import sleep
import time
conn=sqlite3.connect('/users/stud/inf/erwinr/atm/db.sqlite3')

cur=conn.cursor()

def getwords(text):
    return re.compile('\w+').findall(text)

class czytaj():
    def __init__(self):
		self.win=0
		self.tury=1
		self.turygracza1=0
		self.turygracza2=0
		self.kartygracza1=[]
		self.wystapienia1={}
		self.wystapienia2={}
		self.kartygracza2=[]
		self.zyciegracza1=[]
		self.zyciegracza2=[]
		self.wszystkiekarty=[]
		self.counter=0
    
    def zeruj(self):
		self.win=0
		self.tury=1
		self.turygracza1=0
		self.turygracza2=0
		self.kartygracza1=[]
		self.wystapienia1={}
		self.wystapienia2={}
		self.kartygracza2=[]
		self.zyciegracza1=[]
		self.zyciegracza2=[]
		self.wszystkiekarty=[]
		self.counter=0    
       
    def analyze(self,i):
        with open(i,'r') as f:
            kolejne_tury=f.readlines()
            linia=getwords(kolejne_tury[0])
            play1=linia[1]
            play2=linia[2]  
            
            print play1, play2
            

            for i in kolejne_tury:
                self.licztury(i)
                self.liczzagrania(i)
               
            #print win, lost
            #print self.tury
            #print self.turygracza1,self.turygracza2
            #print self.kartygracza1,self.kartygracza2
            print self.zyciegracza1,self.zyciegracza2
            if '0' in self.zyciegracza1:
				win=play2
				lost=play1
            else:
				win=play1
				lost=play2
            #print self.wystapienia1,self.wystapienia2
            #try:

            sql="""
            select wygrane,przegrane, rozgrywki, ulubionakarta, ulubionakarta2,ulubionakarta3,
            iloscrozegranychtur, ulubionakartapick, ulubionakarta2pick, ulubionakarta3pick,
            najdluzszagra, najkrotszagra, sredniailosctur, najmniejzagranychkart, 
            najwiecejzagranychkart, sredniozagranychkart
             from magic_gracz where imie=?
            """             
            wygrane=cur.execute(sql,[play1])
            wygrane=cur.fetchall()
            l= wygrane[0]
            
				
            trin=list(l)
            
            if win==play1:
			    trin[0]+=1
            else:
                trin[1]+=1
            
            trin[2]+=1
            vallog=0

            for key, val in self.wystapienia1.iteritems():                  
                if val>trin[8]:
                    trin[8]=val
                    trin[4]=key
   
                elif val>trin[9]:
					trin[9]=val
					trin[5]=key
        				
                elif val>trin[7]:
					trin[7]=val
					trin[3]=key
                    
                vallog+=val
            trin[6]+=self.turygracza1
            if self.turygracza1>trin[10]:
				trin[10]=self.turygracza1
				if trin[11]==0:
					trin[11]=self.turygracza1
				if self.turygracza1<trin[11]:
					trin[11]=self.turygracza1
			
            #print self.turygracza1
            trin[12]=int(trin[6]/trin[2])
            if vallog<trin[13]:
				trin[13]=vallog
            elif trin[13]==0:
                trin[13]=vallog
			
            if vallog>trin[14]:
			    trin[14]=vallog
            trin[15]+=vallog
            noname=trin+[play1]
            drin=tuple(noname)
            print drin
			
            sql2="""
            update magic_gracz set wygrane=?,przegrane=?, rozgrywki=?, ulubionakarta=?, ulubionakarta2=?,ulubionakarta3=?,
            iloscrozegranychtur=?, ulubionakartapick=?, ulubionakarta2pick=?, ulubionakarta3pick=?,
            najdluzszagra=?, najkrotszagra=?, sredniailosctur=?, najmniejzagranychkart=?, 
            najwiecejzagranychkart=?, sredniozagranychkart=? where imie=? 
            """
            cur.execute(sql2,drin)
			
            sql="""
            select wygrane,przegrane, rozgrywki, ulubionakarta, ulubionakarta2,ulubionakarta3,
            iloscrozegranychtur, ulubionakartapick, ulubionakarta2pick, ulubionakarta3pick,
            najdluzszagra, najkrotszagra, sredniailosctur, najmniejzagranychkart, 
            najwiecejzagranychkart, sredniozagranychkart
             from magic_gracz where imie=?
            """             
            wygrane=cur.execute(sql, [play2])
            wygrane=cur.fetchall()
            l= wygrane[0]
            
				
            trin=list(l)
            
            if win==play2:
			    trin[0]+=1
            else:
                trin[1]+=1
            
            trin[2]+=1
            vallog=0
            
        
            
            for key, val in self.wystapienia2.iteritems():
                  
                if val>trin[8]:
                    trin[8]=val
                    trin[4]=key
                elif key==trin[4]:
					trin[8]+=val
                
                elif val>trin[9]:
					trin[9]=val
					trin[5]=key
                elif key==trin[5]:
                    trin[9]+=val
					
                elif val>trin[7]:
					trin[7]=val
					trin[3]=key
                elif key==trin[3]:
                    trin[7]+=val
                    
                vallog+=val
            
            trin[6]+=self.turygracza2
            if self.turygracza1>trin[10]:
				trin[10]=self.turygracza2
            if trin[11]==0:
				trin[11]=self.turygracza2
            if self.turygracza1<trin[11]:
				trin[11]=self.turygracza2
				
            #print self.turygracza2
            
            trin[12]=int(trin[6]/trin[2])
            
            if vallog<trin[13]:
				trin[13]=vallog
            elif trin[13]==0:
				trin[13]=vallog
			
            if vallog>trin[14]:
				trin[14]=vallog
			
            trin[15]+=vallog
            noname=trin+[play2]
            drin=tuple(noname)
            print drin
            sql2="""
            update magic_gracz set wygrane=?,przegrane=?, rozgrywki=?, ulubionakarta=?, ulubionakarta2=?,ulubionakarta3=?,
            iloscrozegranychtur=?, ulubionakartapick=?, ulubionakarta2pick=?, ulubionakarta3pick=?,
            najdluzszagra=?, najkrotszagra=?, sredniailosctur=?, najmniejzagranychkart=?, 
            najwiecejzagranychkart=?, sredniozagranychkart=? where imie=? 
            """
            cur.execute(sql2,drin)
			
            conn.commit()
                    
            #except (IndexError) as e:
			#	print e
            
            #print type(wygrane)
            #print wygrane
            #wygrane[0]+=1
            #print wygrane
            print "baza works!"
        return [win,lost]
        
    
    def licztury(self,linia):
        if 'NEXTTURN' in linia:
            self.tury+=1
            
    def liczzagrania(self, linia):
        tlumacz= getwords(linia)
        if 'PLAY' in linia:
            if tlumacz[1]=='1':
                self.turygracza1+=1
                if tlumacz[3] in self.wystapienia1:
                    self.wystapienia1[tlumacz[3]]+=1
                else:
                    self.wystapienia1[tlumacz[3]]=1
            else:
                self.turygracza2+=1    
                if tlumacz[3] in self.wystapienia2:
                    self.wystapienia2[tlumacz[3]]+=1
                else:
                    self.wystapienia2[tlumacz[3]]=1
        if 'SUBLIFE' in linia:
            if tlumacz[1]=='1':
                self.zyciegracza1.append(tlumacz[2])
                
            else:
                self.zyciegracza2.append(tlumacz[2])
                
    def printit(self):
        try:
            while True:
                nowy=glob.glob("/users/stud/inf/erwinr/atm/magic/historia/*.txt")
                for i in nowy:
                   j=i[-14:-4]
                   k='/users/stud/inf/erwinr/atm/magic/history/odczytane/'+i[-14:]
                   self.zeruj()
                   gracze=self.analyze(i)
                   print gracze
                   z='/users/stud/inf/erwinr/atm/magic/players/'+gracze[0]+'/'+i[-14:0]
                   shutil.copy(i, z)
                   try:
                       z='/users/stud/inf/erwin/atm/magic/players/'+gracze[1]+'/'+i[-14:0]
                       shutil.copy(i, z)
                   except (IOError):
                       pass
                   shutil.move(i, k)
                time.sleep(10)
                print 'd'
        except (KeyboardInterrupt):
            print 'przerwano'
            
a=czytaj()
a.printit()
