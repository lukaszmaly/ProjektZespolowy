#include "Connection.h"
#include <SFML\Network.hpp>
#include <iostream>
#include <atlstr.h>


NetworkGame::NetworkGame(void)
{
}


NetworkGame::~NetworkGame(void)
{
}

void NetworkGame::Init(std::string adres)
{
	port = 54545;
	//ip = "192.168.0.100";
	ip = adres;
}

void NetworkGame::HostGame()
{
	sf::TcpListener listener;
//	sf::Socket::Status status = listener.listen(port);	
	if (listener.listen(port) != sf::Socket::Done)
		printf("HostGame: nieudana inicjalizacja nas³uchu");
	sf::TcpSocket socket;
	printf("czekam na polaczenie");
	if (listener.accept(socket) != sf::Socket::Done)
		printf("HostGame: nieudana akcpetacja po³¹czenia");
	else
	{
		char cos[] = "polaczono";
		socket.send(cos,strlen(cos));
	}

	//petle gry
}

void NetworkGame::ConnectGame()
{
	std::size_t received;
	char data[100];
	memset(data, 0, sizeof data);
	sf::TcpSocket socket;
	if (socket.connect(ip, port) != sf::Socket::Done)
		printf("ConnectGame: nieudane po³aczenie z serwerem");
	else
	{
		socket.receive(data,sizeof(data),received); 
		printf(data);
		getchar();
	}
}