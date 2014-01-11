#pragma once

#include <iostream>
#include <vector>
#include <string>
#include "opencv2/core/core.hpp"
#include "SFML\Network.hpp"
using namespace std;
using namespace sf;
using namespace cv;
class Server
{
public:
	UdpSocket soc;
	int port;
	void Init(string ip,int port);
	IpAddress client;
Server();

	void sendNewCard(int id,Point a, Point b,Point c,Point d,bool taped)
	{
		char data[100];
		sprintf(data,"|ADD %d %d %d %d %d %d %d %d %d %d|",id,a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y,taped);	
		cout<<data<<endl;
		if (soc.send(data, 6, client, port) != sf::Socket::Done)
		{
			cout<<"Nie wyslano"<<endl;
		}
	}
		void UpdateCard(int id,Point a, Point b,Point c,Point d,bool taped)
	{
		char data[20];
		sprintf(data,"|UPDATE %d %d %d %d %d %d %d %d %d %d|",id,a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y,taped);
		if (soc.send(data, 20, client, port) != sf::Socket::Done)
		{
			cout<<"Nie wyslano"<<endl;
		}
	}



	~Server(void);
};

