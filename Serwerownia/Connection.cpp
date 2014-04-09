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
	ip = adres;
}

void NetworkGame::HostGame()
{
	tmp_odbior = 1;
	if (listener.listen(port) != sf::Socket::Done)
		printf("HostGame: nieudana inicjalizacja nas³uchu");
	if (listener.accept(socket) != sf::Socket::Done)
		printf("HostGame: nieudana akcpetacja po³¹czenia");
	else
	{
		Wyslij("polaczono");
		//while(true)
		//{
		//Wyslij("strzelam!");
		//Wyslij("strzelam!");
	//		}
		}
}

void NetworkGame::ConnectGame()
{
	char data[10];
	memset(data, 0, sizeof data);
	sf::TcpSocket socket;
	if (socket.connect(ip, port) != sf::Socket::Done)
	{
		printf("ConnectGame: nieudane po³aczenie z serwerem");
		getchar();
	}
	else
	{
		while(true) {
			std::string cos;
			sf::Packet packet;
			socket.receive(packet);
			if (packet >> cos)
				 std::cout << cos << "\n"; 
		}
	}
}

void NetworkGame::Send(char tresc[])
{
	if(socket.send(tresc,strlen(tresc)) != sf::Socket::Done)
		printf("wyslano");
	else
		printf("nie wys³ano");
}

void NetworkGame::Start_Game()
{
	NetworkGame game;
	odbior = "";
	game.Init("192.168.0.100");
	sf::Thread thread(&NetworkGame::HostGame, &game);
	thread.launch();
	NetworkGame serv;
	sf::Thread thread1(&NetworkGame::Receiving_Server, &serv);
	thread1.launch();
}

void NetworkGame::Wyslij(std::string tresc)
{
	sf::Packet packet; 
	packet << tresc;
	socket.send(packet);
	packet.clear(); 
}

void NetworkGame::Odbierz()
{
	std::string cos;
	sf::Packet packet;
	if(socket.receive(packet) != sf::Socket::Done)
	printf("nie odebralem");
	else
	if (packet >> cos)
	{
	std::cout << cos << "\n"; 
	LPCSTR lpc = cos.c_str();
	MessageBoxA(NULL,lpc,NULL,NULL);
	}
}

void NetworkGame::Sending_Server()
{
	while(true)
		{
			if(tmp_odbior == 1) 
			Wyslij("strzelam!");
			Wyslij("strzelam!");
		}
}

void NetworkGame::Receiving_Server()
{
	//while(true) {			Odbierz();		}
}
