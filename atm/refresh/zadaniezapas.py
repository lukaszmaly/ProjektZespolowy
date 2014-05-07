from magic.models.py import *


def getwords(text):
    return re.compile('\w+').findall(text)

class czytaj():
    def __init__(self):
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
            win=linia[1]
            lost=linia[2]  
    
            for i in kolejne_tury:
                self.licztury(i)
                self.liczzagrania(i)
           
            playerwin=Gracz.objects.get(imie=win)
            playerlost=Gracz.objects.get(imie=lost)
			
            playerwin.win()
      
            print win, lost
            print self.tury
            print self.turygracza1,self.turygracza2
            print self.kartygracza1,self.kartygracza2
            print self.zyciegracza1,self.zyciegracza2
            print self.wystapienia1,self.wystapienia2
            
        return [win,lost]
        
    
    def licztury(self,linia):
        if 'NEXTTURN' in linia:
            self.tury+=1
            
    def liczzagrania(self, linia):
        tlumacz= getwords(linia)
        if 'PLAY' in linia:
            if tlumacz[1]=='1':
                self.turygracza1+=1
                self.kartygracza1.append(tlumacz[3])
                if tlumacz[3] in self.wystapienia1:
                    self.wystapienia1[tlumacz[3]]+=1
                else:
                    self.wystapienia1[tlumacz[3]]=1
            else:
                self.turygracza2+=1
                self.kartygracza2.append(tlumacz[3])
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
                nowy=glob.glob("/home/erwin/atm/magic/history/*.txt")
                for i in nowy:
                   j=i[-14:-4]
                   k=i[0:-14]+'odczytane/'+i[-14:]
                   gracze=self.analyze(i)
                   z='/home/erwin/atm/magic/players/'+gracze[0]+'/'+i[-14:0]
                   shutil.copy(i, z)
                   z='/home/erwin/atm/magic/players/'+gracze[1]+'/'+i[-14:0]
                   shutil.copy(i, z)
                   shutil.move(i, k)
                time.sleep(10)
                print 'd'
        except (KeyboardInterrupt):
            print 'przerwano'
