#include "Server.h"

void Server::Update()
{
	time++;
	if(time > interval)
	{
		time = 0;
	}
}

void Server::Init(string ip,int port,int interval)
{
	client = ip;
	this->port = port;
	this->interval = interval;
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
}

void Server::SendNewCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped)
{
	char data[100];
	int n = sprintf_s(data,"| ADD %d %d %d %d %d %d %d %d %d %d %d %d |",id,idb,gracz,taped,a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y);	
	if(soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Blad podczas wysylanie danych o nowej karcie"<<endl;
	}
	else
	{
		cout<< data <<endl;
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
	else
	{
		cout<< data <<endl;
	}
}

void Server::AddPlayer(int id,const char name[])
{
	char data[100];
	int n = sprintf(data,"| PLAYER %d %s |",id,name);	
	if (soc.send(data, n, client, port) != sf::Socket::Done)
	{
		cout<<"Nie wyslano"<<endl;
	}
	else
	{
		cout<<data<<endl;
	}
}

Server::~Server()
{
	soc.unbind();
}