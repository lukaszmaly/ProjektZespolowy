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
void Server::Markers()
{	ile++;
	char data[100];
	int n = sprintf_s(data,"| MARKERS |");	

	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else
	{
		cout<< data <<endl;
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
		cout<<"Utworzono serwer UDP na porcie "<<port<<endl;
	}
}

Server::Server()
{
	time = 0;
	ile=0;
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
			cout<< data <<endl;
		}
	
}

void Server::AddMana(int id,int count)
{
		ile++;
		char data[100];
	int n = sprintf_s(data,"| ADDMANA %d %d |",id,count);	

	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;
	}
}

void Server::SubMana(int id,int count)
{
		ile++;
		char data[100];
	int n = sprintf_s(data,"| SUBMANA %d %d |",id,count);	

	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;
	}
}

void Server::Dead(int id)
{
	ile++;
		char data[100];
	int n = sprintf_s(data,"| DEAD %d |",id);	

	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;
	}

}

void Server::Block(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped,int id2)
{

			ile++;
		char data[100];

		int n = sprintf(data,"| BLOCK %d %d %d %d %d %d %d %d %d %d %d %d %d |",id,idb,gracz,taped,a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y,id2);	
		if (soc.send(data, n, client, port) != sf::Socket::Done)
		{
			cout<<"Blad podczas wysylanie aktualnych danych o karcie"<<endl;
		}
		else if(showLog)
		{
			cout<< data <<endl;
		}
	
}


void Server::SendNewCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped)
{
	char data[100];
	int n = sprintf_s(data,"| ADD %d %d %d %d %d %d %d %d %d %d %d %d |",id,idb,gracz,taped,a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y);	
		ile++;
	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else if(showLog)
	{
		cout<< data <<endl;
	}

}

void Server::UpdateCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped)
{

			ile++;
		char data[100];

		int n = sprintf(data,"| UPDATE %d %d %d %d %d %d %d %d %d %d %d %d |",id,idb,gracz,taped,a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y);	
		if (soc.send(data, n, client, port) != sf::Socket::Done)
		{
			cout<<"Blad podczas wysylanie aktualnych danych o karcie"<<endl;
		}
		else if(showLog)
		{
			cout<< data <<endl;
		}
	
}
int Server::GetInterval()
{
	return interval;
}
void Server::AddPlayer(int id,const char name[])
{	ile++;
	char data[100];
	int n = sprintf(data,"| PLAYER %d %s |",id,name);	
	if (soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Nie wyslano"<<endl;
	}
	else if(showLog)
	{
		cout<<data<<endl;
	}
}

Server::~Server()
{
	soc.unbind();
}