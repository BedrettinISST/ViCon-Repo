# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  #config.proxy.http     = "http://proxy:3128"
  #config.proxy.https    = "http://proxy:3128"
  config.proxy.no_proxy = "localhost,127.0.0.1"

  provisioner = Vagrant::Util::Platform.windows? ? :guest_ansible : :ansible
  #config.vm.network "private_network", ip: "192.168.221.1"
  config.vm.box = "ubuntu/bionic64"
  config.disksize.size = '30GB'
  config.vm.provider "virtualbox" do |v|
    v.name = "ViCon"
  end

  config.vm.provision "shell",
	 preserve_order: true,
	   run: 'once',
		    inline: <<-SHELL
		    apt-get update
		    apt-get --assume-yes install dos2unix
		    cd /vagrant/Vagrant/deployment
            find ./ -type f -exec dos2unix {} \';'
            source /vagrant/Vagrant/deployment/install-docker.sh
		  SHELL

  config.vm.network :forwarded_port, guest: 8083, host: 8083 # portal
  config.vm.network :forwarded_port, guest: 8084, host: 8084 # fhir

  config.vm.provider "virtualbox" do |v|
      v.memory = 6000
      v.cpus = 3
  end
end
