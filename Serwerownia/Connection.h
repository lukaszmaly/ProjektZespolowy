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

private:
	int port;
	sf::IpAddress ip;
	sf::TcpListener listener;
	sf::TcpSocket socket;
};