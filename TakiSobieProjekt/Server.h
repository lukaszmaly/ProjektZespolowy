#pragma once
#include <iostream>
#include <string>
#include "opencv2/core/core.hpp"
#include "SFML/Network.hpp"

using namespace std;
using namespace sf;
using namespace cv;

class Server
{
private:
	int port;
	int interval;
	int time;
	UdpSocket soc;
	IpAddress client;
public:
	Server();
	~Server();
	void Update();
	void Init(string ip,int port,int interval);
	void SendNewCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped);
	void UpdateCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped);
	void AddPlayer(int id,const char name[]);
};

