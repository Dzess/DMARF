Data Mining  Association Rules Finder (DMARF Project)

Status:
	experimental / alfa

Goal:
	Intention of this project is to provide the software which will be able to utilize parallel processing in order to improve the speed of association rules acquisition. Java and OpenCL are the main technologies used in this project. 
Current version is still under heavy development and is not recommended for serious applications.

	
	
Java implementations of Apriori algorithm:
	- naive single threaded (dynamic java structures)
	- single threaded (bitmap index)
	- single threaded (bitmap index) with optional support for OpenCL accelerated computations.

Requires:
	* maven
	* jvm 1.6
	* opencl 1.0 capable hardware with drivers

Future plans:
	+ adding apriori mulltithreaded version with concurrent collections
	+ adding better support for heterogonous OpenCL enviorments
	+ adding another algorithms (FPGrowth, Eclat)	

Authors:
	Peter Jessa
	Nicolas Dobski
	