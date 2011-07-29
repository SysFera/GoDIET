
package com.sysfera.godiet.shell.command

import jline.Completor;
import jline.ConsoleReader;
import jline.FileNameCompletor
import jline.MultiCompletor
import jline.NullCompletor
import jline.SimpleCompletor

import org.codehaus.groovy.tools.shell.ComplexCommandSupport
import org.codehaus.groovy.tools.shell.Shell

import com.sysfera.godiet.common.model.generated.User.Ssh.Key;
import com.sysfera.godiet.common.services.GoDietService;
import com.sysfera.godiet.common.services.UserService;
import com.sysfera.godiet.shell.GoDietSh
import com.sysfera.godiet.common.controllers.SSHKeyController;
import com.sysfera.godiet.common.controllers.SSHKeyController.Status;
import com.sysfera.godiet.common.model.generated.User.Ssh.Key;


class SSHCommand extends ComplexCommandSupport {
	SSHCommand(final Shell shell) {
		super(shell, 'ssh', 'sh')
		this.functions = [
			'addkey',
			'loadkey',
			'modifykey',
			'status',
			'initpasswords'
		]
	}


	private String askPassword() {
		io.out.println "Passphrase: "
		//TODO: modify to never have clear password
		String password;
		if(System.getProperty('jline.terminal').equals("jline.UnsupportedTerminal")){
			password = io.in.readLine()
		}else {
			password = new jline.ConsoleReader().readLine(new Character('*' as char));
		}
		return password
	}

	def do_addkey = { addkeyForm(); }
	private void addkeyForm(){
		io.out.println "Enter private key path: "
		ConsoleReader consoleFileCompletor = new jline.ConsoleReader();
		consoleFileCompletor.addCompletor(new FileNameCompletor())
		String privKeyPath = consoleFileCompletor.readLine()
		//TODO: Check if exist

		io.out.println "Enter public key path (${privKeyPath}.pub): "
		String pubkeyPath = consoleFileCompletor.readLine()
		String password = askPassword()
		Key newKey = new Key()
		newKey.pathPub = pubkeyPath
		newKey.path = privKeyPath
		newKey.encrypted = true
		Integer newManagedkey = ((GoDietSh)shell).godiet.userService.addSSHKey(newKey)

		((GoDietSh)shell).godiet.userService.modifyKey(newManagedkey, privKeyPath, pubkeyPath, password)
	}

	private void modifyKeyForm(SSHKeyController key){
		assert key != null
		io.out.println "Enter private key path"
		ConsoleReader consoleFileCompletor = new jline.ConsoleReader();
		consoleFileCompletor.addCompletor(new FileNameCompletor())
		consoleFileCompletor.putString key.privKeyPath
		String privKeyPath = consoleFileCompletor.readLine()

		io.out.println "Enter public key path "
		consoleFileCompletor.putString("${privKeyPath}.pub")
		String pubkeyPath = consoleFileCompletor.readLine()
		String password = askPassword()
		((GoDietSh)shell).godiet.userService.modifyKey(key.getId(), privKeyPath, pubkeyPath, password)
	}

	private SSHKeyController modifyKey( Integer id, List<SSHKeyController> keys )
	{
		if(keys == null) return null
		for (SSHKeyController k : keys) {
			if(k.id.equals(id))
				return k
		}
		return null
	}

	def do_modifykey = {
		assert it.size() == 1 , 'Init key need one argument'
		def argument = it.head()
		if(argument.isInteger()) argument = argument as Integer
		def keys = ((GoDietSh)shell).godiet.userService.getManagedKeys()
		def lastIndexKeys = keys.size()-1

		switch (argument) {
			case 'all':
				keys.each{ modifyKeyForm(it) }
				break;
			case 0..lastIndexKeys:
				def k = modifyKey(argument,keys)
				modifyKeyForm(k)
				break;
			default:
				io.err.println("Only \'all\' or 0 to ${lastIndexKeys} are permitted. Use ssh status to display all key loaded.")
				break;
		}
	}


	def do_status = {
		UserService godiet = ((GoDietSh)shell).godiet.userService

		def keys = godiet.managedKeys
		if(keys.size() == 0){
			io.out.println("No key loaded")
			return
		}
		io.out.println("@|bold ${keys.size()} key Loaded|@ : ")




		io.out.println("@|bold Key\tStatus\tPublic key path|@")
		io.out.println("---------------------------------------------------------")

		keys.each{SSHKeyController key ->
			def coloredStatus
			switch (key.status) {
				case Status.LOADED:
					coloredStatus =  "@|green ${key.status}|@"
					break;
				default:
					coloredStatus = "@|red ${key.status}|@"
					break;
			}
			io.out.println "${key.getId()}\t${coloredStatus}\t${key.pubKeyPath}"

		}
	}

	def do_initpasswords = {
		UserService godiet = ((GoDietSh)shell).godiet.userService
		def keys = godiet.managedKeys
		keys.each { SSHKeyController key ->
			if(key.status == Status.PASSWORDNOTSET) {
				io.out.println("${key.pubKeyPath}");
				String password = askPassword()

				((GoDietSh)shell).godiet.userService.modifyKey(key.getId(), key.getPrivKeyPath(), key.getPubKeyPath(), password)
			}
		}
	}
}