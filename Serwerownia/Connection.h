#include <SFML\Network.hpp>
#include <iostream>
#include <atlstr.h>

class NetworkGame
{
public:
	NetworkGame(void);
	~NetworkGame(void);
	void HostGame();
	void ConnectGame();
	void Init(std::string adres);
	void Send(char tresc[]);
	void Start_Game();
	void Wyslij(std::string tresc);
	void Odbierz();
	void Sending_Server();
	void Receiving_Server();

private:
	int port;
	sf::IpAddress ip;
	sf::TcpListener listener;
	sf::TcpSocket socket;
	std::string odbior;
	std::string tmp; // zmienna do testow, mozna wywalic
	int tmp_odbior;
};