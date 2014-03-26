#include "StdAfx.h"
#include "GraSieciowa.h"
#include <SFML\Network.hpp>
#include <iostream>


GraSieciowa::GraSieciowa(void)
{
}


GraSieciowa::~GraSieciowa(void)
{
}

void GraSieciowa::Serwer()
{
	char data[100] = "I wanna thank you much!";
	sf::TcpListener listener;
	listener.listen(54545);	
	sf::TcpSocket serwer;
	listener.accept(serwer);
	serwer.send(data,100);
}

void GraSieciowa::Klient()
{
	std::size_t received;
	char data[100];
	sf::TcpSocket klient;
	sf::Socket::Status status = klient.connect("192.168.0.101", 54545);
	klient.receive(data, 100, received);
	printf(data);
	getchar();
}
