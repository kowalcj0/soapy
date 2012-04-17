package com.yelllabs.soapy.helpers;


import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.IOUtils
import net.schmizz.sshj.connection.channel.direct.Session
import net.schmizz.sshj.connection.channel.direct.Session.Command
import org.apache.log4j.Logger

import java.util.concurrent.TimeUnit

/**
* @brief Simple class to connect to any server using SSH. This code is based on a example 
* given along with SSHj lib https://github.com/shikhar/sshj
* @author Janusz Kowalczyk 38115ja
*/
public class SSHConnector {

  private Logger log = null;
  private String authPublicKeyName = "";
  private String hostname = "";
  private SSHClient ssh = null;
  private Session session = null;
  private Command cmd = null;



  /**
  * @brief Default constructor which initialize SSHClient and load known hosts.
  *
  * @param hostname A hostname to which you want to connect, if "" or null then localhost is used
  * @param authPublicKeyName Name of public key used to authenticate, if "" or null then System.getProperty("user.name") is used.
  * @param log A log4j Logger
  *
  */
  public SSHConnector( String hostname, String authPublicKeyName, Logger log ) {
    this.hostname = ( hostname != "" && hostname != null ) ? hostname : "localhost";
    this.authPublicKeyName = ( authPublicKeyName != "" && authPublicKeyName != null ) ? authPublicKeyName : System.getProperty("user.name");
    this.log = log;

    this.ssh = new SSHClient();
    this.ssh.loadKnownHosts();
  }


  /**
  * @brief Connects via SSH and executes a list of given commands
  *
  * @param commands A varArgs of commands to execute
  *
  * @return A Map of command output in format: [ [command, commandOutput], [command2. command2Output],...]
  */
  public Map exec( String... commands ) {
    
    Map cmndsResults = [:];

    try {
      this.ssh.connect( this.hostname );
      this.ssh.authPublickey( this.authPublicKeyName );


      for ( cmnd in commands ) {
        this.session = this.ssh.startSession();
      
        try {

            this.cmd = this.session.exec( cmnd );
            String cmndOutput = IOUtils.readFully( cmd.getInputStream() ).toString();
            
            cmndsResults.put( cmnd , cmndOutput );

            //cmndOutput.split("\n").each{
              //line -> this.log.info line;
            //}
            this.cmd.join(5, TimeUnit.SECONDS);
            //this.log.info("\n** exit status: " + this.cmd.getExitStatus());

        } finally {
          this.session.close();
        }

      } // for end

    } finally {
      this.ssh.disconnect();
    }

    return cmndsResults;
  }

}
