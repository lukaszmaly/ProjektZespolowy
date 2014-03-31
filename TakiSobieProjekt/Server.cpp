#include "Server.h"

///Czasami usuwa karte z karty, popraw
//popraw czas wysy³ania wiadomoœci
void Server::Update()
{
	time++;
	if(time > interval)
	{
		time = 0;
	}
}
void Server::Write(const char tab[100])
{
	fstream plik;
	plik.open( "log.txt", std::ios::in | std::ios::out | std::ios::app);
	plik << tab<<endl;
	plik.close();
}

void Server::Markers(int w,int h)
{	
char data[100];
int n = sprintf_s(data,"| MARKERS %d %d |",w,h);	

if(soc.send(data, n, client, port) != sf::Socket::Done)
{
	cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
}
else
{
	cout<< data <<endl;Write(data);
}
}
void Server::Played(int id)
{	
char data[100];
int n = sprintf_s(data,"| PLAYED %d |",id);	

if(soc.send(data, n, client, port) != sf::Socket::Done)
{
	cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
}
else
{
	cout<< data <<endl;Write(data);
}
}

void Server::Init(string ip,int port,int interval,bool showLog)
{
	client = ip;
	this->port = port;
	this->interval = interval;
	this->showLog=showLog;
	if(soc.bind(port) != Socket::Done)
	{
		cout<<"Blad podczas tworzenia socketa"<<endl;

	}
	else
	{
		Write("Utworzono serwer");
		cout<<"Utworzono serwer UDP na porcie "<<port<<" z interval = "<<this->interval<<endl;
	}
}

Server::Server()
{
	time = 0;
}


void Server::DrawCard(int id,int value)
{
	char data[100];
	int n = sprintf(data,"| DRAW %d %d |",id,value);	
	if (soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie aktualnych danych o karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}
}
void Server::Attack(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped)
{
	char data[100];

	int n = sprintf(data,"| ATTACK %d %d %d %d %d %d %d %d %d %d %d %d |",id,idb,gracz,taped,a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y);	
	if (soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie aktualnych danych o karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}

}

void Server::AddMana(int id,int count)
{
	char data[100];
	int n = sprintf_s(data,"| ADDMANA %d %d |",id,count);	

	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}
}

void Server::SubMana(int id,int count)
{
	char data[100];
	int n = sprintf_s(data,"| SUBMANA %d %d |",id,count);	

	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}
}

void Server::SubLife(int id,int count)
{
	char data[100];
	int n = sprintf_s(data,"| SUBLIFE %d %d |",id,count);	

	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}
}

void Server::AddLife(int id,int count)
{
	char data[100];
	int n = sprintf_s(data,"| ADDLIFE %d %d |",id,count);	

	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}
}

void Server::Cost(int owner,int cost)
{
	char data[100];
	int n = sprintf_s(data,"| COST %d %d |",owner,cost);	

	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}
}
void Server::NextPhase()
{
	char data[100];
	int n = sprintf_s(data,"| NEXTPHASE |");	

	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}
}

void Server::Dead(int id)
{
	char data[100];
	int n = sprintf_s(data,"| DEAD %d |",id);	

	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}

}

void Server::Block(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped,int id2)
{
	char data[100];

	int n = sprintf(data,"| BLOCK %d %d %d %d %d %d %d %d %d %d %d %d %d |",id,idb,gracz,taped,a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y,id2);	
	if (soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie aktualnych danych o karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}

}


void Server::SendNewCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped)
{
	char data[100];
	int n = sprintf_s(data,"| ADD %d %d %d %d %d %d %d %d %d %d %d %d |",id,idb,gracz,taped,a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y);	
	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}

}

void Server::UpdateCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped)
{
	char data[100];

	int n = sprintf(data,"| UPDATE %d %d %d %d %d %d %d %d %d %d %d %d |",id,idb,gracz,taped,a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y);	
	if (soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie aktualnych danych o karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;Write(data);
	}

}
int Server::GetInterval()
{
	return interval;
}

void Server::AddPlayer(int id,const char name[])
{	
	char data[100];
	int n = sprintf(data,"| PLAYER %d %s |",id,name);	
	if (soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Nie wyslano"<<endl;
	}
	else if(showLog)
	{
		cout<<data<<endl;Write(data);
	}
}

Server::~Server()
{
	soc.unbind();
	Write("Zamknieto serwer");
}