/*
 * ConsoleController.java
 *
 * Created on 11 juin 2004, 02:39
 */

package goDiet.Controller;

import goDiet.Interface.GoDIETFrame;
import goDiet.Interface.GoDIETConsolePanel;
import goDiet.Utils.XmlScanner;
import goDiet.Events.DeployStateChange;
import goDiet.Model.RunConfig;
import goDiet.Defaults;

import java.io.IOException;


/**
 *
 * @author  rbolze hdail
 */
public class ConsoleController extends java.util.Observable
                               implements java.awt.event.ActionListener,
                                          java.util.Observer {
    private RunConfig runCfg;       // configure behavior of GoDIET
    private DietPlatformController modelController;
    private DeploymentController deployCtrl;
   
    private XmlScanner xmlScanner;
    private String xmlFileName;
    
    private GoDIETConsolePanel goDietConsolePanel;
    private javax.swing.JTextArea goDIETconsole;
    private java.util.Vector history;
    private boolean fileLoaded = false;
    private static boolean interfaceMode;
    private int deployState = goDiet.Defaults.DEPLOY_NONE;

    protected static final String HELP =
            "The following commands are available:\n" +
            "   launch:     launch entire DIET platform\n" +
            "   stop:       kill entire DIET platform using kill pid\n" +
            //        "   kill:       kill entire DIET platform using kill -9 pid\n" +
            "   status:     print run status of each DIET component\n" +
            "   history:    print history of commands executed\n" +
            "   help:       print this message\n" +
            "   exit:       exit GoDIET, do not change running platform.\n";
    
    /** Creates a new instance of ConsoleController */
    public ConsoleController(goDiet.Interface.GoDIETConsolePanel consolePanel) {
        this.interfaceMode=true;        
        
        this.goDietConsolePanel=consolePanel;
        this.goDIETconsole=goDietConsolePanel.getGoDIETConsole();
        goDietConsolePanel.getCommandTextField().addActionListener(this);
        goDietConsolePanel.getOpenButton().addActionListener(this);
        goDietConsolePanel.getLaunchButton().addActionListener(this);
        goDietConsolePanel.getStopButton().addActionListener(this);
        goDietConsolePanel.getStatusButton().addActionListener(this);

        initNonGraphic();
    }
    
    public ConsoleController(java.util.Observer deployObserver){
        this.interfaceMode=false;
        
        initNonGraphic();
        this.deployCtrl.addObserver(deployObserver);
    }
    
    public ConsoleController(){
        this.interfaceMode=false;
        
        initNonGraphic();
    }
    
    /** To be called by all contructors */
    private void initNonGraphic(){
        this.runCfg = new RunConfig();  // Set defaults
        history = new java.util.Vector();
        this.modelController = new DietPlatformController(this);
        this.deployCtrl = new DeploymentController(this, modelController);
        this.deployCtrl.addObserver(this);
        this.xmlScanner = new XmlScanner();        
    }
    
    public void setRunConfig(RunConfig runCfg){
        this.runCfg = runCfg;
    }
    
    public RunConfig getRunConfig(){
        return this.runCfg;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof javax.swing.JTextField ){
            javax.swing.JTextField command= (javax.swing.JTextField) source;
            doCommand(command.getText());
            command.setText("");
        } else if ( source == goDietConsolePanel.getOpenButton()){
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Choose xml file");
            fileChooser.showDialog(goDIETconsole, "ok");
            try{
                loadXmlFile(fileChooser.getSelectedFile().toString());
                goDietConsolePanel.getStatusButton().setEnabled(true);
            } catch(java.lang.NullPointerException x){
                printOutput("You must load a xml file", 0);
            }
           
        } else if ( source == goDietConsolePanel.getLaunchButton()){
            launch();
        } else if ( source == goDietConsolePanel.getStopButton()){
            stop();
        } else if ( source == goDietConsolePanel.getStatusButton()){
            status();
        }
    }
    
    public boolean doCommand(String cmd){
        history.add(cmd);
        java.util.StringTokenizer strTok = new java.util.StringTokenizer(cmd," ");
        String command= strTok.nextToken();
        boolean giveUserCtrl = true;
        if (command.compareTo("load") == 0){
            if(!fileLoaded && !interfaceMode){
                try{
                    loadXmlFile(strTok.nextToken());
                }catch(Exception x){
                    printOutput("Usage load <file.xml>", 0);
                }
            }else{
                printOutput("You have already loaded a file", 0);
            }
        }else if (command.compareTo("launch") == 0){
            launch();
            giveUserCtrl = false;
        }else if (command.compareTo("stop") == 0 ){
            stop();
            giveUserCtrl = false;
        }else if (command.compareTo("status") == 0){
            status();
        }else if (command.compareTo("history") == 0){
            history();
        }else if (command.compareTo("exit") == 0){
            exit();
        }else{
            help();
        }
        return giveUserCtrl;
    }

    public void loadXmlFile(String xmlFileName){
        if ((new java.io.File(xmlFileName).canRead())){
            this.xmlFileName = xmlFileName;
        } else {
            printOutput("No Such file", 0);
        }

        this.printOutput("Parsing xml file: " + xmlFileName, 0);
        try {
            xmlScanner.buildDietModel(xmlFileName, modelController,this);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            this.printError("Can not continue without valid XML.  Exiting.");
            System.exit(1);
        }
        setFileloaded();
    }
    
    private void launch(){
        if (fileLoaded){
            setChanged();
            this.printOutput("sending launch request all", 3);
            notifyObservers(new goDiet.Events.LaunchRequest(this,"all"));
            clearChanged();

            /*if(interfaceMode){
                goDietConsolePanel.getStopButton().setEnabled(true);
                goDietConsolePanel.getLaunchButton().setEnabled(false);
            }*/
        } else {
            printError("You must load the XML file before launch !");
        }
    }
    
    private void stop(){
        java.util.Date startTime, endTime;
        double timeDiff;

        if (fileLoaded){
            startTime = new java.util.Date();
            printOutput("\n* Stopping DIET platform at " + startTime.toString());
            deployCtrl.stopPlatform();
            endTime = new java.util.Date();
            timeDiff = (endTime.getTime() - startTime.getTime())/1000;
            printOutput("\n* DIET platform stopped at " + endTime.toString() +
                    "[time= " + timeDiff + " sec]", 0);
            if(interfaceMode){
                goDietConsolePanel.getStopButton().setEnabled(false);
                goDietConsolePanel.getLaunchButton().setEnabled(true);
            }else{
                printOutput("\n* Exiting GoDIET. Bye.");
                exit();
            }
        }else {
            printError("You must load the XML file before stop !");
        }
    }
    private void status(){
        if (fileLoaded){
            modelController.printPlatformStatus();
        }else {
            printError("You must load the XML file before get status !");
        }
    }
    private void help(){
        printOutput(HELP);
    }
    private void history(){
        for (java.util.Iterator it =history.iterator();it.hasNext();){
            printOutput((String)it.next());
        }
    }
    private String exit(){
        System.exit(0);
        return "exit";
    }
    
    public void printOutput(String msg){
        this.printOutput(msg, 0);
    }
    public void printOutput(String msg, int printLevel){
        //if (this.goDIETconsole != null){
        if (printLevel <= this.runCfg.getDebugLevel()){
            if (interfaceMode){
                this.goDIETconsole.append(msg+"\n");
                this.goDietConsolePanel.update(goDietConsolePanel.getGraphics());
                this.goDietConsolePanel.updateUI();
                this.goDIETconsole.setCaretPosition(goDIETconsole.getText().length());
            } else {
                System.out.println(msg);
            }
        }
    }
    public void printError(String msg){
        this.printError(msg, 0);
    }
    public void printError(String msg, int printLevel){
        //if (this.goDIETconsole != null){
        if (printLevel <= this.runCfg.getDebugLevel()){
            if (interfaceMode){
                this.goDIETconsole.append(msg+"\n");
                this.goDietConsolePanel.update(goDietConsolePanel.getGraphics());
                this.goDietConsolePanel.updateUI();
            }else{
                System.err.println(msg);
            }
        }
    }
   
    private void setFileloaded(){
        this.fileLoaded=true;
        if (this.goDIETconsole != null){
            goDietConsolePanel.getLaunchButton().setEnabled(true);
        }
    }

    public void update(java.util.Observable observable, Object obj) {
        java.awt.AWTEvent e = (java.awt.AWTEvent)obj;
        int newState;
        if ( e instanceof DeployStateChange){
            newState = ((DeployStateChange)e).getNewState();
            this.printOutput("Changing state to : " +
                goDiet.Defaults.getDeployStateString(newState), 3);
            if(newState == goDiet.Defaults.DEPLOY_ACTIVE){
                if(interfaceMode){
                    goDietConsolePanel.getStopButton().setEnabled(true);
                    goDietConsolePanel.getLaunchButton().setEnabled(false);
                }
            }
            this.deployState = newState;
        }
    }
}