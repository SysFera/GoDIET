/*
 * ConsoleController.java
 *
 * Created on 11 juin 2004, 02:39
 */

package goDiet.Controller;
import goDiet.Interface.GoDIETFrame;
import goDiet.Interface.GoDIETConsolePanel;
/**
 *
 * @author  rbolze
 */
public class ConsoleController implements java.awt.event.ActionListener{
    private DietPlatformController mainController;
    private GoDIETConsolePanel goDietConsolePanel;
    private javax.swing.JTextArea goDIETconsole;
    private java.util.Vector history;
    private java.util.Date startTime, endTime;
    double timeDiff;
    private boolean fileLoaded= false;
    private static boolean interfaceMode;
    //boolean shellmode = false;
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
        this.goDietConsolePanel=consolePanel;
        this.goDIETconsole=goDietConsolePanel.getGoDIETConsole();
        goDietConsolePanel.getCommandTextField().addActionListener(this);
        goDietConsolePanel.getOpenButton().addActionListener(this);
        goDietConsolePanel.getLaunchButton().addActionListener(this);
        goDietConsolePanel.getStopButton().addActionListener(this);
        goDietConsolePanel.getStatusButton().addActionListener(this);
        history = new java.util.Vector();
        this.mainController = new DietPlatformController(this);
        this.interfaceMode=true;
    }
    public ConsoleController(){
        history = new java.util.Vector();
        this.mainController = new DietPlatformController(this);
        this.interfaceMode=false;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof javax.swing.JTextField ){
            javax.swing.JTextField command= (javax.swing.JTextField) source;
            doCommand(command.getText());
            command.setText("");
        }else if ( source == goDietConsolePanel.getOpenButton()){
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Choose xml file");
            fileChooser.showDialog(goDIETconsole, "ok");
            try{
                loadXmlFile(fileChooser.getSelectedFile().toString());
                 goDietConsolePanel.getStatusButton().setEnabled(true);
            }catch(java.lang.NullPointerException x){
                printToConsole("You must load a xml file");
            }
           
        }else if ( source == goDietConsolePanel.getLaunchButton()){
            //launch();
            doCommand("launch");
        }else if ( source == goDietConsolePanel.getStopButton()){
            stop();
        }else if ( source == goDietConsolePanel.getStatusButton()){
            status();
        }
    }
    
    public void doCommand(String cmd){
        history.add(cmd);
        java.util.StringTokenizer strTok = new java.util.StringTokenizer(cmd," ");
        String command= strTok.nextToken();
        if (command.compareTo("load") == 0){
            if(!fileLoaded && !interfaceMode){
                try{
                    String arg= strTok.nextToken();
                    System.out.println("command :"+command);
                    System.out.println("arg :"+arg);
                    loadXmlFile(arg);
                    fileLoaded=true;
                }catch(Exception x){
                    printToConsole("Usage load <file.xml>");
                }
            }else{
                printToConsole("you have already load a file");
            }
        }else if (command.compareTo("launch") ==0){
            launch();
        }else if (command.compareTo("stop") ==0 ){
            stop();
        }else if (command.compareTo("status") ==0){
            status();
        }else if (command.compareTo("history") ==0){
            history();
        }else if (command.compareTo("exit") ==0){
            exit();
        }else{
            help();
        }
    }
    
    public void loadXmlFile(String xmlFileName){
        if ((new java.io.File(xmlFileName).canRead())){
            mainController.parseXmlFile(xmlFileName);
            setFileloaded();
        }else{
            printToConsole("No Such file");
        }
    }
    private void launch(){
        if (fileLoaded){
            startTime = new java.util.Date();
            printToConsole("* Launching DIET platform at " + startTime.toString());
            mainController.launchPlatform();
            endTime = new java.util.Date();
            timeDiff = (endTime.getTime() - startTime.getTime())/1000;
            printToConsole("* DIET launch done at " + endTime.toString() +
            " [time= " + timeDiff + " sec]");
            if(interfaceMode){
                goDietConsolePanel.getStopButton().setEnabled(true);
                goDietConsolePanel.getLaunchButton().setEnabled(false);
            }
        }else {
            printToConsole("You must load the XML file before launch !");
        }
    }
    
    private void stop(){
        if (fileLoaded){
            startTime = new java.util.Date();
            printToConsole("\n* Stopping DIET platform at " + startTime.toString());
            mainController.stopPlatform();
            endTime = new java.util.Date();
            timeDiff = (endTime.getTime() - startTime.getTime())/1000;
            printToConsole("\n* DIET platform stopped at " + endTime.toString() +
            "[time= " + timeDiff + " sec]");
            if(interfaceMode){
                goDietConsolePanel.getStopButton().setEnabled(false);
                goDietConsolePanel.getLaunchButton().setEnabled(true);
            }else{
                printToConsole("\n* Exiting GoDIET. Bye.");
                exit();
            }
        }else {
            printToConsole("You must load the XML file before stop !");
        }
    }
    private void status(){
        if (fileLoaded){
            mainController.printPlatformStatus();
        }else {
            printToConsole("You must load the XML file before get status !");
        }
    }
    private void help(){
        printToConsole(HELP);
    }
    private void history(){
        for (java.util.Iterator it =history.iterator();it.hasNext();){
            printToConsole((String)it.next());
        }
    }
    private String exit(){
        System.exit(0);
        return "exit";
    }
    
    public  void printToConsole(String msg){
        //if (this.goDIETconsole != null){
        if (interfaceMode){
            this.goDIETconsole.append(msg+"\n");
            this.goDietConsolePanel.update(goDietConsolePanel.getGraphics());
            this.goDietConsolePanel.updateUI();
        }else{
            System.out.println(msg);
        }
    }
    private void setFileloaded(){
        this.fileLoaded=true;
        if (this.goDIETconsole != null){
            goDietConsolePanel.getLaunchButton().setEnabled(true);
        }
    }
}
