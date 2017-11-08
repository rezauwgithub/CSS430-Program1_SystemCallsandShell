/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   main.cpp
 * Author: rezan
 *
 * Created on January 8, 2017, 12:39 PM
 */

#include <cstdlib>
#include <iostream>

#include <unistd.h>     // For fork and pipe
#include <sys/wait.h>   // For wait
#include <stdlib.h>     // For exit

/*
 * 
 */
int main(int argc, char** argv) {

	const int READ = 0;
	const int WRITE = 1;
    
    	int pid;
    	int status;
    
    
    	// The file descriptors for the pipes
    	int fileDescriptor01[2];
    	int fileDescriptor02[2];
    
    
    	// Check to make sure number of arguments passed in are valid
    	if (argc < 2)
    	{
        	std::cerr << "Error: Invalid Argument Count" << std::endl;
        	return 0;
    	}
    
    	if (pipe(fileDescriptor01) < 0)
    	{
        	std::cerr << "Error: Invalid Pipe01" << std::endl; // Need to create Pipe01
    	}
    
    	if (pipe(fileDescriptor02) < 0)
    	{
        	std::cerr << "Error: Invalid Pipe02" << std::endl;  // Need to create Pipe02
    	}
    
    	// Create new child process
	pid = fork();
	// std::cerr << "child pid: " << pid << std::endl;
    	if (pid < 0)
    	{
        	std::cerr << "Error: Invalid Fork" << std::endl;
        	return EXIT_FAILURE;
    	}
    
    	// pid is being initialize in previous if statement (pid = fork())
    	if (pid == 0)
    	{
        	// Create new child process
		pid = fork();
		// std::cerr << "grand child pid: " << pid << std::endl;
        	if(pid < 0)
        	{
            		std::cerr << "Error: Invalid Fork" << std::endl;
            		return EXIT_FAILURE;
        	}
        
        
        	if (pid == 0)
        	{
            		// Create new child process
			pid = fork();
			// std::cerr << "great-grand child pid: " << pid << std::endl;
            		if (pid < 0)
            		{
                		std::cerr << "Error: Invalid Fork" << std::endl;
                		return EXIT_FAILURE;
            		}
            
            
            		if (pid == 0)
            		{
                		// child
                		close(fileDescriptor02[WRITE]);
                		close(fileDescriptor01[WRITE]);
                		close(fileDescriptor01[READ]);

				int dup2Child = dup2(fileDescriptor02[READ], READ);
				// std::cerr << "dup2(fileDescriptor02[READ], READ) dup2Child: " << dup2Child << std::endl;

                		execlp("wc", "wc", "-l", NULL);
            		}
            		else
            		{
                		// grand-child
                		close(fileDescriptor02[READ]);
                		close(fileDescriptor01[WRITE]);

                		int dup2GrandChild = dup2(fileDescriptor02[WRITE], WRITE);
				// std::cerr << "dup2(fileDescriptor02[WRITE], WRITE) dup2GrandChild: " << dup2GrandChild << std::endl;
                		dup2GrandChild = dup2(fileDescriptor01[READ], READ);
				// std::cerr << "dup2(fileDescriptor01[READ], READ) dup2GrandChild: " << dup2GrandChild << std::endl;

                		execlp("grep", "grep", argv[1], NULL);
            		}
        	}
        	else
        	{
            		// child's child's child
            		close(fileDescriptor02[READ]);
            		close(fileDescriptor02[WRITE]);
            		close(fileDescriptor01[READ]);

            		int dup2GreatGrandChild = dup2(fileDescriptor01[WRITE], WRITE);
			// std::cerr << "dup2(fileDescriptor01[WRITE], WRITE) dup2GreatGrandChild: " << dup2GreatGrandChild << std::endl;

            		execlp("ps", "ps", "-A", NULL);
        	}
    	}
    	else
    	{
        	// Parent
        	wait(&status);
    	}    
    
    	return 0;
}

