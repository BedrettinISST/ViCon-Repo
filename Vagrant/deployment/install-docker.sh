#!/bin/bash

### set timezone ##
timedatectl set-timezone Europe/Berlin

### install docker ###
apt-get --assume-yes install default-jdk
apt-get --assume-yes install docker.io

### install python packages for sending ecg ###
apt-get --assume-yes install python3-pip
pip3 install pandas
pip3 install paho-mqtt
pip3 install matplotlib

# add user to docker group
gpasswd -a vagrant docker
newgrp docker

# set docker proxy
mkdir /etc/systemd/system/docker.service.d
echo '[Service]' > /etc/systemd/system/docker.service.d/http-proxy.conf
echo 'Environment="HTTP_PROXY=http://proxy:3128/"' >> /etc/systemd/system/docker.service.d/http-proxy.conf
echo '[Service]' > /etc/systemd/system/docker.service.d/https-proxy.conf
echo 'Environment="HTTPS_PROXY=http://proxy:3128/"' >> /etc/systemd/system/docker.service.d/https-proxy.conf

### install docker-compose ###
curl -L https://github.com/docker/compose/releases/download/1.21.2/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose