#include "Server.h"


Server::Server()
{
	


}
void Server::Init(string ip,int port)
{
	client=ip;
	this->port=port;
		if(soc.bind(port) !=Socket::Done)
	{
		cout<<"Blad podczas tworzenia socketa"<<endl;
	
	}
	else
	{
		cout<<"Utworzono serwer UDP na porcie "<<port<<endl;
	}
}


Server::~Server(void)
{
}
